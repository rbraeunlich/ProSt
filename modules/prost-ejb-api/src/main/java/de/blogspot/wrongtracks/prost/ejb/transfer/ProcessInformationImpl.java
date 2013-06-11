package de.blogspot.wrongtracks.prost.ejb.transfer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Klasse für Informationen über laufende oder abgeschlossene Prozesse.
 * 
 * @author Ronny Bräunlich
 * 
 */
public class ProcessInformationImpl implements Serializable, ProcessInformation {

	private static final long serialVersionUID = -197009301365294430L;
	private String processId;
	private Date startTime;
	private Date endTime;
	private List<TaskInformation> taskInfos = new ArrayList<TaskInformation>();

	@Override
	public void setProcessId(String id) {
		this.processId = id;
	}

	@Override
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Override
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Override
	public String getProcessId() {
		return processId;
	}

	@Override
	public Date getStartTime() {
		return startTime;
	}

	@Override
	public Date getEndTime() {
		return endTime;
	}

	@Override
	public List<TaskInformation> getTaskInfos() {
		return taskInfos;
	}

	@Override
	public void setTaskInfos(List<TaskInformation> taskInfos) {
		this.taskInfos = taskInfos;
	}
}
