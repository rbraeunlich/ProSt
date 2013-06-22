package de.blogspot.wrongtracks.prost.ejb.transfer.impl;

import java.io.Serializable;
import java.util.Date;

import de.blogspot.wrongtracks.prost.ejb.transfer.TaskInformation;

/**
 * Clazz for information about finished tasks.
 * 
 * @author Ronny Bräunlich
 * 
 */
public class TaskInformationImpl implements Serializable, TaskInformation {

	private static final long serialVersionUID = 2547341446985751623L;
	private Date startDate;
	private String bearbeiter;
	private String id;
	private String name;
	private Date endDate;

	@Override
	public Date getStartDate() {
		return startDate;
	}

	@Override
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Override
	public String getBearbeiter() {
		return bearbeiter;
	}

	@Override
	public void setBearbeiter(String bearbeiter) {
		this.bearbeiter = bearbeiter;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public Date getEndDate() {
		return endDate;
	}
}
