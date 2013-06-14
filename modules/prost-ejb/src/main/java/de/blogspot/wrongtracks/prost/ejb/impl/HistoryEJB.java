package de.blogspot.wrongtracks.prost.ejb.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricFormProperty;

import de.blogspot.wrongtracks.prost.ejb.api.HistoryEJBRemote;
import de.blogspot.wrongtracks.prost.ejb.transfer.HistoricFormPropertyInfo;
import de.blogspot.wrongtracks.prost.ejb.transfer.impl.HistoricFormPropertyInfoImpl;

@Stateless(name = "HistoryEJB")
public class HistoryEJB implements HistoryEJBRemote {

	private HistoryService historyService;

	public List<HistoricFormPropertyInfo> getHistoricFormPropertyDataForProcess(
			String processInstanceId) {
		return Collections
				.unmodifiableList(findAndConvertHistoricFormPropertyData(processInstanceId));
	}

	@Override
	public List<HistoricFormPropertyInfo> getHistoricFormPropertyDataForTask(
			String taskId) {
		String processInstanceId = historyService
				.createHistoricTaskInstanceQuery().taskId(taskId)
				.singleResult().getProcessInstanceId();
		return Collections
				.unmodifiableList(findAndConvertHistoricFormPropertyData(processInstanceId));

	}

	private List<HistoricFormPropertyInfo> findAndConvertHistoricFormPropertyData(
			String processInstanceId) {
		List<HistoricFormPropertyInfo> result = new ArrayList<HistoricFormPropertyInfo>();
		List<HistoricDetail> historicFormProperties = historyService
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

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}
}
