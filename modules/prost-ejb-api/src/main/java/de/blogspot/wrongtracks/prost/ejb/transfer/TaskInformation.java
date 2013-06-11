package de.blogspot.wrongtracks.prost.ejb.transfer;

import java.util.Date;

public interface TaskInformation {

	Date getStartDate();

	void setStartDate(Date startDate);

	String getBearbeiter();

	void setBearbeiter(String bearbeiter);

	String getId();

	void setId(String id);

	String getName();

	void setName(String name);

	void setEndDate(Date endDate);

	Date getEndDate();

}