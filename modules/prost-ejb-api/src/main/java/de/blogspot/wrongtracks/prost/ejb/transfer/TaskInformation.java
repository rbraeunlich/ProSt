package de.blogspot.wrongtracks.prost.ejb.transfer;

import java.io.Serializable;
import java.util.Date;

/**
 * Klasse für Informationen über beendete Tasks.
 * 
 * @author Ronny Bräunlich
 * 
 */
public class TaskInformation implements Serializable {

	private static final long serialVersionUID = 2547341446985751623L;
	private Date startDate;
	private String bearbeiter;
	private String id;
	private String name;
	private Date endDate;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getBearbeiter() {
		return bearbeiter;
	}

	public void setBearbeiter(String bearbeiter) {
		this.bearbeiter = bearbeiter;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return endDate;
	}
}
