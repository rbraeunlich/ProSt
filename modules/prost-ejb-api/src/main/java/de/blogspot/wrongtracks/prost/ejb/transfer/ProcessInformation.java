package de.blogspot.wrongtracks.prost.ejb.transfer;

import java.util.Date;
import java.util.List;

public interface ProcessInformation {

	void setProcessId(String id);

	void setStartTime(Date startTime);

	void setEndTime(Date endTime);

	String getProcessId();

	Date getStartTime();

	Date getEndTime();

	List<TaskInformation> getTaskInfos();

	void setTaskInfos(List<TaskInformation> taskInfos);

}