package de.blogspot.wrongtracks.prost.ejb.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Stateless;

import org.activiti.engine.FormService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.repository.ProcessDefinition;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

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

	private ServiceTracker repositoryServiceTracker;

	private ServiceTracker formServiceTracker;

	private ServiceTracker runtimeServiceTracker;

	private ProcessInformationBuilder processInfoBuilder = new ProcessInformationBuilder();

	@Resource
	private BundleContext context;

	/**
	 * Default constructor.
	 */
	public ProcessEJB() {
	}

	@PostConstruct
	public void init() {
		// @formatter:off
		repositoryServiceTracker = new ServiceTracker(context, RepositoryService.class.getName(), null);
		repositoryServiceTracker.open();
		formServiceTracker = new ServiceTracker(context, FormService.class.getName(), null);
		formServiceTracker.open();
		runtimeServiceTracker = new ServiceTracker(context, RuntimeService.class.getName(), null);
		runtimeServiceTracker.open();
		//@formatter:on
	}

	@PreDestroy
	public void destroy() {
		repositoryServiceTracker.close();
		formServiceTracker.close();
		runtimeServiceTracker.close();
	}

	@Override
	public Map<String, String> getDeployedProcesses() {
		List<ProcessDefinition> processDefinitions = ((RepositoryService) repositoryServiceTracker
				.getService()).createProcessDefinitionQuery().latestVersion()
				.list();
		Map<String, String> result = new HashMap<String, String>(
				processDefinitions.size());
		for (ProcessDefinition definition : processDefinitions) {
			result.put(definition.getId(), definition.getName());
		}
		return Collections.unmodifiableMap(result);
	}

	@Override
	public List<FormPropertyTransfer> getStartFormProperties(String processId) {
		List<FormProperty> formProperties = ((FormService) formServiceTracker
				.getService()).getStartFormData(processId).getFormProperties();
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
			((RuntimeService) runtimeServiceTracker.getService())
					.startProcessInstanceById(processId);
		} else {
			((FormService) formServiceTracker.getService())
					.submitStartFormData(processId, processVariables);
		}
	}

	@Override
	public List<ProcessInformation> getProcessInformationForAllProcesses() {
		return Collections.unmodifiableList(processInfoBuilder
				.buildProcessInformationForAllHistoricProcesses());
	}

}
