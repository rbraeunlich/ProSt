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
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.repository.ProcessDefinition;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import de.blogspot.wrongtracks.prost.ejb.api.ProcessEJBRemote;
import de.blogspot.wrongtracks.prost.ejb.exception.ServiceUnavailableException;
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

	private ServiceTracker historyServiceTracker;

	@Resource
	private BundleContext context;

	/**
	 * Default constructor.
	 */
	public ProcessEJB() {
	}

	@PostConstruct
	public void init() {
		repositoryServiceTracker = new ServiceTracker(context,
				RepositoryService.class.getName(), null);
		repositoryServiceTracker.open();
		formServiceTracker = new ServiceTracker(context,
				FormService.class.getName(), null);
		formServiceTracker.open();
		runtimeServiceTracker = new ServiceTracker(context,
				RuntimeService.class.getName(), null);
		runtimeServiceTracker.open();
		historyServiceTracker = new ServiceTracker(context,
				HistoryService.class.getName(), null);
		historyServiceTracker.open();
	}

	@PreDestroy
	public void destroy() {
		repositoryServiceTracker.close();
		formServiceTracker.close();
		runtimeServiceTracker.close();
		historyServiceTracker.close();
	}

	@Override
	public Map<String, String> getDeployedProcesses() {
		List<ProcessDefinition> processDefinitions = getService(
				RepositoryService.class, repositoryServiceTracker)
				.createProcessDefinitionQuery().latestVersion().list();
		Map<String, String> result = new HashMap<String, String>(
				processDefinitions.size());
		for (ProcessDefinition definition : processDefinitions) {
			result.put(definition.getId(), definition.getName());
		}
		return Collections.unmodifiableMap(result);
	}

	@Override
	public List<FormPropertyTransfer> getStartFormProperties(String processId) {
		List<FormProperty> formProperties = getService(FormService.class,
				formServiceTracker).getStartFormData(processId)
				.getFormProperties();
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
			getService(RuntimeService.class, runtimeServiceTracker)
					.startProcessInstanceById(processId);
		} else {
			getService(FormService.class, formServiceTracker)
					.submitStartFormData(processId, processVariables);
		}
	}

	@Override
	public List<ProcessInformation> getProcessInformationForAllProcesses() {
		ProcessInformationBuilder processInformationBuilder = new ProcessInformationBuilder(getService(HistoryService.class, historyServiceTracker));
		return Collections.unmodifiableList(processInformationBuilder
				.buildProcessInformationForAllHistoricProcesses());
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
