package de.blogspot.wrongtracks.prost.ejb.api;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import de.blogspot.wrongtracks.prost.ejb.transfer.FormPropertyTransfer;
import de.blogspot.wrongtracks.prost.ejb.transfer.ProcessInformationImpl;

/**
 * Bean zur Abfrage von Daten über Prozesse.
 * 
 * @author Ronny Bräunlich
 * 
 */
@Remote
public interface ProcessEJBRemote {

	/**
	 * 
	 * @return Map in welcher Key = Process ID und Value = Name aller deployten
	 *         Prozesse
	 */
	Map<String, String> getDeployedProcesses();

	/**
	 * Liefert für den übergebenen Prozess die Liste der zum Starten erwarteten
	 * FormProperties. Falls der Prozess keine erwartet, wird eine leere Liste
	 * zurück gegeben.
	 * 
	 * @param processId
	 * @return Liste mit {@link FormPropertyTransfer} oder leere Liste
	 */
	public List<FormPropertyTransfer> getStartFormProperties(String processId);

	/**
	 * Startet den übergebenen Prozess. Die processVariables können auch leer
	 * oder null sein.
	 * 
	 * @param processId
	 * @param processVariables
	 */
	void startProcessById(String processId, Map<String, String> processVariables);

	/**
	 * Liefert Informationen zu allen Prozesses (laufend oder abgeschlossen) in
	 * Form von {@link ProcessInformationImpl}.
	 * 
	 * @return Liste mit {@link ProcessInformationImpl} oder leere Liste, bei keinen
	 *         Prozessen.
	 */
	public List<ProcessInformationImpl> getProcessInformationForAllProcesses();
}
