package de.blogspot.wrongtracks.prost.ejb.api;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import de.blogspot.wrongtracks.prost.ejb.transfer.FormPropertyTransfer;
import de.blogspot.wrongtracks.prost.ejb.transfer.ProcessInformation;

/**
 * Bean to get process data.
 * 
 * @author Ronny Bräunlich
 * 
 */
public interface ProcessEJBRemote {

	/**
	 * 
	 * @return Map where key = Process ID and value = process name
	 */
	Map<String, String> getDeployedProcesses();

	/**
	 * @param processId
	 * @return list containing {@link FormPropertyTransfer} oder empty list
	 */
	public List<FormPropertyTransfer> getStartFormProperties(String processId);

	/**
	 * @param processId
	 * @param processVariables (may be null)
	 */
	void startProcessById(String processId, Map<String, String> processVariables);

	/**
	 * @return list containing {@link ProcessInformation} oder empty list
	 */
	public List<ProcessInformation> getProcessInformationForAllProcesses();
}
