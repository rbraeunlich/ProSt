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
public class ProcessInformation implements Serializable {

	private static final long serialVersionUID = -197009301365294430L;
	private String processId;
	private Date startTime;
	private Date endTime;
	private List<TaskInformation> taskInfos = new ArrayList<TaskInformation>();

	public void setProcessId(String id) {
		this.processId = id;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getProcessId() {
		return processId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public List<TaskInformation> getTaskInfos() {
		return taskInfos;
	}

	public void setTaskInfos(List<TaskInformation> taskInfos) {
		this.taskInfos = taskInfos;
	}
}
