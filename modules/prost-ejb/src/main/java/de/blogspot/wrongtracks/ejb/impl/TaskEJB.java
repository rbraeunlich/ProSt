package de.blogspot.wrongtracks.ejb.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.FormService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import de.blogspot.wrongtracks.prost.ejb.api.TaskEJBRemote;
//import de.blogspot.wrongtracks.prost.event.TaskBesitzerGewechseltEvent;
import de.blogspot.wrongtracks.prost.ejb.exception.TaskException;
import de.blogspot.wrongtracks.ejb.transfer.impl.FormPropertyConverter;
import de.blogspot.wrongtracks.prost.ejb.transfer.FormPropertyTransfer;

@Stateless(name = "TaskEJB")
@Local
public class TaskEJB implements TaskEJBRemote {

	private TaskService taskService;
	private FormService formService;
	private RuntimeService runtimeService;
	private RepositoryService repositoryService;

	// @Inject
	// FIXME eventing!
	// private Event<TaskBesitzerGewechseltEvent> taskBesitzerGewechseltEvent;

	/**
	 * @return Map<taskId, taskName>
	 */
	@Override
	public Map<String, String> getTasksForUser(String user) {
		List<Task> tasks = taskService.createTaskQuery().taskAssignee(user)
				.list();
		Map<String, String> result = new HashMap<String, String>(tasks.size());
		for (Task task : tasks) {
			result.put(task.getId(), task.getName());
		}
		return result;
	}

	@Override
	public List<FormPropertyTransfer> startTask(String taskId, String user)
			throws TaskException {
		try {
			taskService.claim(taskId, user);
			// taskBesitzerGewechseltEvent.fire(new
			// TaskBesitzerGewechseltEvent());
		} catch (ActivitiException ae) {
			pruefeObGrundTaskWegIstUndWerfeTaskException(ae);
			throw ae;
		}
		List<FormProperty> formProperties = formService.getTaskFormData(taskId)
				.getFormProperties();
		List<FormPropertyTransfer> transferObjects = new ArrayList<FormPropertyTransfer>(
				formProperties.size());
		for (FormProperty formProperty : formProperties) {
			FormPropertyTransfer transfer = FormPropertyConverter
					.convertProperty(formProperty);
			transferObjects.add(transfer);
		}
		return transferObjects;
	}

	@Override
	public void endTask(String taskId, String userId,
			Map<String, String> formValues) throws TaskException {
		// falls die Task im letzten Moment entzogen wurde, steht kein Assignee
		// in der History darum wird die Task hier nochmal geholt
		try {
			taskService.claim(taskId, userId);
			if (formValues == null || formValues.isEmpty()) {
				taskService.complete(taskId);
			} else {
				formService.submitTaskFormData(taskId, formValues);
			}
		} catch (ActivitiException ae) {
			pruefeObGrundTaskWegIstUndWerfeTaskException(ae);
			throw ae;
		}
	}

	@Override
	public byte[] getProcessImage(String taskId) {
		String processInstanceId = taskService.createTaskQuery().taskId(taskId)
				.singleResult().getProcessInstanceId();

		ProcessInstance processInstance = runtimeService
				.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();

		BpmnModel model = repositoryService.getBpmnModel(processInstance
				.getProcessDefinitionId());

		InputStream definitionImageStream = null;
		if (model != null) {
			definitionImageStream = ProcessDiagramGenerator.generateDiagram(
					model, "png", runtimeService
							.getActiveActivityIds(processInstance.getId()));
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] data = new byte[128];
		try {
			while (definitionImageStream.read(data) > -1) {
				baos.write(data);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (baos != null)
					baos.close();
				if (definitionImageStream != null)
					definitionImageStream.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return baos.toByteArray();
	}

	@Override
	public Map<String, Pair<String, String>> getAllTasksForGroup(String group) {
		List<Task> tasks = taskService.createTaskQuery().active().list();
		Map<String, Pair<String, String>> result = new HashMap<String, Pair<String, String>>(
				tasks.size());
		for (Task task : tasks) {
			List<IdentityLink> identityLinksForTask = taskService
					.getIdentityLinksForTask(task.getId());
			for (IdentityLink link : identityLinksForTask) {
				if (link.getType().equals(IdentityLinkType.CANDIDATE)
						&& link.getGroupId().equalsIgnoreCase(group)) {
					HashMap<String, String> nameZuBearbeiter = new HashMap<String, String>(
							1);
					nameZuBearbeiter.put(task.getName(), task.getAssignee());
					result.put(task.getId(), new ImmutablePair<String, String>(
							task.getName(), task.getAssignee()));
				}
			}
		}
		return Collections.unmodifiableMap(result);
	}

	@Override
	public void entzieheTask(String taskId) throws TaskException {
		try {
			taskService.claim(taskId, null);
		} catch (ActivitiException ae) {
			pruefeObGrundTaskWegIstUndWerfeTaskException(ae);
			throw ae;
		}
		/*
		 * für Activiti ist das umsetzen des Bearbeiters einer Task (hier auf
		 * null) kein Event. Darum muss ich mein eigenes Event benutzen um die
		 * GUI zum Updaten zu bewegen
		 */
		// taskBesitzerGewechseltEvent.fire(new TaskBesitzerGewechseltEvent());
	}

	private void pruefeObGrundTaskWegIstUndWerfeTaskException(
			ActivitiException ae) throws TaskException {
		if (ae.getMessage().contains("Cannot find task")) {
			throw new TaskException("Ooops, die Task ist nicht mehr verfügbar.");
		}
	}

	@Override
	public Map<String, Object> getVariables(String taskId) throws TaskException {
		try {
			return taskService.getVariables(taskId);
		} catch (ActivitiException ae) {
			if (ae.getMessage().contains("doesn't exist")) {
				throw new TaskException(
						"Ooops, die Task ist nicht mehr verfügbar.");
			}
		}
		return null;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public void setFormService(FormService formService) {
		this.formService = formService;
	}

	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

}
