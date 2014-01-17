package de.blogspot.wrongtracks.prost.ejb.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricFormProperty;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import de.blogspot.wrongtracks.prost.ejb.api.HistoryEJBRemote;
import de.blogspot.wrongtracks.prost.ejb.exception.ServiceUnavailableException;
import de.blogspot.wrongtracks.prost.ejb.transfer.HistoricFormPropertyInfo;
import de.blogspot.wrongtracks.prost.ejb.transfer.impl.HistoricFormPropertyInfoImpl;

@Remote(HistoryEJBRemote.class)
@Stateless(name = "HistoryEJB")
public class HistoryEJB implements HistoryEJBRemote {

	@Resource
	private BundleContext context;

	private ServiceTracker historyServiceTracker;

	@PostConstruct
	public void init() {
		historyServiceTracker = new ServiceTracker(context,
				HistoryService.class.getName(), null);
		historyServiceTracker.open();
	}

	@PreDestroy
	public void destroy() {
		historyServiceTracker.close();
	}

	public List<HistoricFormPropertyInfo> getHistoricFormPropertyDataForProcess(
			String processInstanceId) {
		return Collections
				.unmodifiableList(findAndConvertHistoricFormPropertyData(processInstanceId));
	}

	@Override
	public List<HistoricFormPropertyInfo> getHistoricFormPropertyDataForTask(
			String taskId) {
		String processInstanceId = getService(HistoryService.class,
				historyServiceTracker).createHistoricTaskInstanceQuery()
				.taskId(taskId).singleResult().getProcessInstanceId();
		return Collections
				.unmodifiableList(findAndConvertHistoricFormPropertyData(processInstanceId));

	}

	private List<HistoricFormPropertyInfo> findAndConvertHistoricFormPropertyData(
			String processInstanceId) {
		List<HistoricFormPropertyInfo> result = new ArrayList<HistoricFormPropertyInfo>();
		List<HistoricDetail> historicFormProperties = getService(
				HistoryService.class, historyServiceTracker)
				.createHistoricDetailQuery()
				.processInstanceId(processInstanceId).formProperties()
				.orderByTime().desc().list();

		for (HistoricDetail historicDetail : historicFormProperties) {
			HistoricFormProperty historicFormProperty = (HistoricFormProperty) historicDetail;
			HistoricFormPropertyInfo info = new HistoricFormPropertyInfoImpl();
			info.setId(historicFormProperty.getId());
			info.setPropertyId(historicFormProperty.getPropertyId());
			info.setValue(historicFormProperty.getPropertyValue());
			info.setTime(historicFormProperty.getTime());
			result.add(info);
		}
		return Collections.unmodifiableList(result);
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
