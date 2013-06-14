package de.blogspot.wrongtracks.prost.ejb.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import org.activiti.engine.FormService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.repository.ProcessDefinition;

import de.blogspot.wrongtracks.prost.ejb.api.ProcessEJBRemote;
import de.blogspot.wrongtracks.prost.ejb.transfer.FormPropertyTransfer;
import de.blogspot.wrongtracks.prost.ejb.transfer.ProcessInformation;
import de.blogspot.wrongtracks.prost.ejb.transfer.converter.impl.FormPropertyConverters;
import de.blogspot.wrongtracks.prost.ejb.transfer.converter.impl.ProcessInformationBuilder;

/**
 * Session Bean implementation class ProcessEJB
 */
@Stateless(name = "ProcessEJB")
public class ProcessEJB implements ProcessEJBRemote {

	private RepositoryService repositoryService;

	private FormService formService;

	private RuntimeService runtimeService;

	private ProcessInformationBuilder processInfoBuilder;

	/**
	 * Default constructor.
	 */
	public ProcessEJB() {
	}

	@Override
	public Map<String, String> getDeployedProcesses() {
		List<ProcessDefinition> processDefinitions = repositoryService
				.createProcessDefinitionQuery().latestVersion().list();
		Map<String, String> result = new HashMap<String, String>(
				processDefinitions.size());
		for (ProcessDefinition definition : processDefinitions) {
			result.put(definition.getId(), definition.getName());
		}
		return Collections.unmodifiableMap(result);
	}

	public List<FormPropertyTransfer> getStartFormProperties(String processId) {
		List<FormProperty> formProperties = formService.getStartFormData(
				processId).getFormProperties();
		List<FormPropertyTransfer> transferForms = new ArrayList<FormPropertyTransfer>(
				formProperties.size());
		for (FormProperty formProperty : formProperties) {
			transferForms.add(FormPropertyConverters
					.convertProperty(formProperty));
		}
		return Collections.unmodifiableList(transferForms);
	}

	@Override
	public void startProcessById(String processId,
			Map<String, String> processVariables) {
		if (processVariables == null || processVariables.isEmpty()) {
			runtimeService.startProcessInstanceById(processId);
		} else {
			formService.submitStartFormData(processId, processVariables);
		}
	}

	@Override
	public List<ProcessInformation> getProcessInformationForAllProcesses() {
		return Collections.unmodifiableList(processInfoBuilder
				.buildProcessInformationForAllHistoricProcesses());
	}

	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

	public void setFormService(FormService formService) {
		this.formService = formService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

}
