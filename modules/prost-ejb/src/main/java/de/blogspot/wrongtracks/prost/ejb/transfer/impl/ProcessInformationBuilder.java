package de.blogspot.wrongtracks.prost.ejb.transfer.impl;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricFormProperty;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;

import de.blogspot.wrongtracks.prost.ejb.transfer.ProcessInformation;
import de.blogspot.wrongtracks.prost.ejb.transfer.TaskInformation;

/**
 * Klasse, welche Informationen zu Prozessen zusammen sammelt.
 * 
 * @author Ronny Bräunlich
 * 
 */
public class ProcessInformationBuilder {

	private HistoryService historyService;

	/**
	 * Erzeugt {@link ProcessInformation} für alle Prozesse, welche in der
	 * Prozessengine laufen oder irgendwann mal liefen.
	 * 
	 * @return Liste mit {@link ProcessInformation} oder leere Liste, fall noch
	 *         nie ein Prozess gestartet wurde.
	 */
	public List<ProcessInformation> buildProcessInformationForAllHistoricProcesses() {
		List<HistoricProcessInstance> allHistoricProcesses = getAllHistoricProcesses();

		List<ProcessInformation> processInfos = new ArrayList<ProcessInformation>(
				allHistoricProcesses.size());

		for (HistoricProcessInstance historicInstance : allHistoricProcesses) {
			ProcessInformation processInfo = createProcessInfo(historicInstance);
			setStudentName(processInfo);

			createAndAssignTaskInfos(processInfo);

			processInfos.add(processInfo);
		}
		return processInfos;
	}

	/**
	 * Sucht aus den {@link HistoricFormProperty} zum Prozess der
	 * {@link ProcessInformation} den Namen des Studenten heraus und setzt in an
	 * der Info.
	 * 
	 * @param processInfo
	 */
	private void setStudentName(ProcessInformation processInfo) {
		List<HistoricDetail> list = historyService.createHistoricDetailQuery()
				.processInstanceId(processInfo.getProcessId()).formProperties()
				.list();
		for (HistoricDetail historicDetail : list) {
			HistoricFormProperty historicFormProperty = (HistoricFormProperty) historicDetail;
			if (historicFormProperty.getPropertyId().equals("student")) {
				processInfo.setStudent(historicFormProperty.getPropertyValue());
			}
		}

	}

	/**
	 * Erzeugt {@link TaskInformation} Objekte zum Prozess der info und setzt
	 * diese an der {@link ProcessInformation}.
	 * 
	 * @param processInfo
	 */
	private void createAndAssignTaskInfos(ProcessInformation processInfo) {
		List<HistoricTaskInstance> historicTasks = historyService
				.createHistoricTaskInstanceQuery()
				.processInstanceId(processInfo.getProcessId()).list();
		List<TaskInformation> taskInfos = new ArrayList<TaskInformation>();
		for (HistoricTaskInstance historicTaskInstance : historicTasks) {
			TaskInformation taskInfo = new TaskInformation();
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
	 * Erzeugt eine {@link ProcessInformation} für die übergebene
	 * {@link HistoricProcessInstance}.
	 * 
	 * @param historicInstance
	 * @return
	 */
	private ProcessInformation createProcessInfo(
			HistoricProcessInstance historicInstance) {
		ProcessInformation processInfo = new ProcessInformation();
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
