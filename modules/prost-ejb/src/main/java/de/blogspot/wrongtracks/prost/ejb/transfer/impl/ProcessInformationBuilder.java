package de.blogspot.wrongtracks.prost.ejb.transfer.impl;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;

import de.blogspot.wrongtracks.prost.ejb.transfer.ProcessInformation;
import de.blogspot.wrongtracks.prost.ejb.transfer.ProcessInformationImpl;
import de.blogspot.wrongtracks.prost.ejb.transfer.TaskInformationImpl;

/**
 * Klasse, welche Informationen zu Prozessen zusammen sammelt.
 * 
 * @author Ronny Bräunlich
 * 
 */
public class ProcessInformationBuilder {

	private HistoryService historyService;

	/**
	 * Erzeugt {@link ProcessInformationImpl} für alle Prozesse, welche in der
	 * Prozessengine laufen oder irgendwann mal liefen.
	 * 
	 * @return Liste mit {@link ProcessInformationImpl} oder leere Liste, fall noch
	 *         nie ein Prozess gestartet wurde.
	 */
	public List<ProcessInformationImpl> buildProcessInformationForAllHistoricProcesses() {
		List<HistoricProcessInstance> allHistoricProcesses = getAllHistoricProcesses();

		List<ProcessInformationImpl> processInfos = new ArrayList<ProcessInformationImpl>(
				allHistoricProcesses.size());

		for (HistoricProcessInstance historicInstance : allHistoricProcesses) {
			ProcessInformationImpl processInfo = createProcessInfo(historicInstance);

			createAndAssignTaskInfos(processInfo);

			processInfos.add(processInfo);
		}
		return processInfos;
	}

	/**
	 * Erzeugt {@link TaskInformationImpl} Objekte zum Prozess der info und setzt
	 * diese an der {@link ProcessInformationImpl}.
	 * 
	 * @param processInfo
	 */
	private void createAndAssignTaskInfos(ProcessInformation processInfo) {
		List<HistoricTaskInstance> historicTasks = historyService
				.createHistoricTaskInstanceQuery()
				.processInstanceId(processInfo.getProcessId()).list();
		List<TaskInformationImpl> taskInfos = new ArrayList<TaskInformationImpl>();
		for (HistoricTaskInstance historicTaskInstance : historicTasks) {
			TaskInformationImpl taskInfo = new TaskInformationImpl();
			taskInfo.setBearbeiter(historicTaskInstance.getAssignee());
			taskInfo.setId(historicTaskInstance.getId());
			taskInfo.setName(historicTaskInstance.getName());
			taskInfo.setStartDate(historicTaskInstance.getStartTime());
			taskInfo.setEndDate(historicTaskInstance.getEndTime());
			taskInfos.add(taskInfo);
		}
		processInfo.setTaskInfos(taskInfos);
	}

	/**
	 * Erzeugt eine {@link ProcessInformationImpl} für die übergebene
	 * {@link HistoricProcessInstance}.
	 * 
	 * @param historicInstance
	 * @return
	 */
	private ProcessInformationImpl createProcessInfo(
			HistoricProcessInstance historicInstance) {
		ProcessInformationImpl processInfo = new ProcessInformationImpl();
		processInfo.setProcessId(historicInstance.getId());
		processInfo.setStartTime(historicInstance.getStartTime());
		processInfo.setEndTime(historicInstance.getEndTime());
		return processInfo;
	}

	private List<HistoricProcessInstance> getAllHistoricProcesses() {
		return historyService.createHistoricProcessInstanceQuery().list();
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}
	
}
