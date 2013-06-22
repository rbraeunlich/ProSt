package de.blogspot.wrongtracks.prost.ejb.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
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
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import de.blogspot.wrongtracks.prost.ejb.api.TaskEJBRemote;
import de.blogspot.wrongtracks.prost.ejb.exception.ServiceUnavailableException;
import de.blogspot.wrongtracks.prost.ejb.exception.TaskException;
import de.blogspot.wrongtracks.prost.ejb.transfer.FormPropertyTransfer;
import de.blogspot.wrongtracks.prost.ejb.transfer.converter.impl.FormPropertyConverters;

@Stateless(name = "TaskEJB")
public class TaskEJB implements TaskEJBRemote {

	@Resource
	private BundleContext context;
	private final Properties props = new Properties();
	private ServiceTracker taskServiceTracker;
	private ServiceTracker formServiceTracker;
	private ServiceTracker runtimeServiceTracker;
	private ServiceTracker repositoryServiceTracker;
	private static final String TASK_UNAVAILABLE_PROP_KEY = "taskUnavailable";

	// @Inject
	// FIXME eventing!
	// private Event<TaskBesitzerGewechseltEvent> taskBesitzerGewechseltEvent;

	@PostConstruct
	public void init() {
		taskServiceTracker = new ServiceTracker(context,
				TaskService.class.getName(), null);
		taskServiceTracker.open();
		formServiceTracker = new ServiceTracker(context,
				FormService.class.getName(), null);
		formServiceTracker.open();
		runtimeServiceTracker = new ServiceTracker(context,
				RuntimeService.class.getName(), null);
		runtimeServiceTracker.open();
		repositoryServiceTracker = new ServiceTracker(context,
				RepositoryService.class.getName(), null);
		repositoryServiceTracker.open();
		try {
			props.load(this.getClass().getResourceAsStream(
					"error-msg.properties"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@PreDestroy
	public void destroy() {
		taskServiceTracker.close();
		formServiceTracker.close();
		runtimeServiceTracker.close();
		repositoryServiceTracker.close();
	}

	/**
	 * @return Map<taskId, taskName>
	 */
	@Override
	public Map<String, String> getTasksForUser(String user) {
		List<Task> tasks = getService(TaskService.class, taskServiceTracker)
				.createTaskQuery().taskAssignee(user).list();
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
			getService(TaskService.class, taskServiceTracker).claim(taskId,
					user);
			// taskBesitzerGewechseltEvent.fire(new
			// TaskBesitzerGewechseltEvent());
		} catch (ActivitiException ae) {
			pruefeObGrundTaskWegIstUndWerfeTaskException(ae);
			throw ae;
		}
		List<FormProperty> formProperties = getService(FormService.class,
				formServiceTracker).getTaskFormData(taskId).getFormProperties();
		List<FormPropertyTransfer> transferObjects = new ArrayList<FormPropertyTransfer>(
				formProperties.size());
		for (FormProperty formProperty : formProperties) {
			FormPropertyTransfer transfer = FormPropertyConverters
					.convertProperty(formProperty);
			transferObjects.add(transfer);
		}
		return transferObjects;
	}

	@Override
	public void endTask(String taskId, String userId,
			Map<String, String> formValues) throws TaskException {
		// if someone took the task there won't be an assignee in history
		// that's why it is claimed here again
		try {
			getService(TaskService.class, taskServiceTracker).claim(taskId,
					userId);
			if (formValues == null || formValues.isEmpty()) {
				getService(TaskService.class, taskServiceTracker).complete(
						taskId);
			} else {
				getService(FormService.class, formServiceTracker)
						.submitTaskFormData(taskId, formValues);
			}
		} catch (ActivitiException ae) {
			pruefeObGrundTaskWegIstUndWerfeTaskException(ae);
			throw ae;
		}
	}

	@Override
	public byte[] getProcessImage(String taskId) {
		String processInstanceId = getService(TaskService.class,
				taskServiceTracker).createTaskQuery().taskId(taskId)
				.singleResult().getProcessInstanceId();

		ProcessInstance processInstance = getService(RuntimeService.class,
				runtimeServiceTracker).createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();

		BpmnModel model = getService(RepositoryService.class,
				repositoryServiceTracker).getBpmnModel(
				processInstance.getProcessDefinitionId());

		InputStream definitionImageStream = null;
		if (model != null) {
			definitionImageStream = ProcessDiagramGenerator.generateDiagram(
					model, "png",
					getService(RuntimeService.class, runtimeServiceTracker)
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
		List<Task> tasks = getService(TaskService.class, taskServiceTracker)
				.createTaskQuery().active().list();
		Map<String, Pair<String, String>> result = new HashMap<String, Pair<String, String>>(
				tasks.size());
		for (Task task : tasks) {
			List<IdentityLink> identityLinksForTask = getService(
					TaskService.class, taskServiceTracker)
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
	public void dispossessTask(String taskId) throws TaskException {
		try {
			getService(TaskService.class, taskServiceTracker).claim(taskId,
					null);
		} catch (ActivitiException ae) {
			pruefeObGrundTaskWegIstUndWerfeTaskException(ae);
			throw ae;
		}
		/*
		 * Changing the assignee is not (yet) an event for Activiti That's why I
		 * have to my own to make clients update the GUI
		 */
		// taskBesitzerGewechseltEvent.fire(new TaskBesitzerGewechseltEvent());
	}

	private void pruefeObGrundTaskWegIstUndWerfeTaskException(
			ActivitiException ae) throws TaskException {
		if (ae.getMessage().contains("Cannot find task")) {
			throw new TaskException(
					props.getProperty(TASK_UNAVAILABLE_PROP_KEY));
		}
	}

	@Override
	public Map<String, Object> getVariables(String taskId) throws TaskException {
		try {
			return getService(TaskService.class, taskServiceTracker)
					.getVariables(taskId);
		} catch (ActivitiException ae) {
			if (ae.getMessage().contains("doesn't exist")) {
				throw new TaskException(
						props.getProperty(TASK_UNAVAILABLE_PROP_KEY));
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private <T> T getService(Class<T> clazz, ServiceTracker tracker) {
		Object service = tracker.getService();
		if (service == null) {
			throw new ServiceUnavailableException();
		}
		return (T) service;
	}
}
