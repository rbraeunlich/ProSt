package de.blogspot.wrongtracks.prost.ejb.api;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import org.apache.commons.lang3.tuple.Pair;

import de.blogspot.wrongtracks.prost.ejb.exception.TaskException;
import de.blogspot.wrongtracks.prost.ejb.transfer.FormPropertyTransfer;

/**
 * Bean für die Bearbeitung von Tasks.
 * 
 * @author Ronny Bräunlich
 * 
 */
@Remote
public interface TaskEJBRemote {

	Map<String, String> getTasksForUser(String user);

	/**
	 * Startet die Task mit der übergebenen ID und setzt den user als assignee.
	 * Die Task ist danach nicht mehr der Gruppen zugeordnet, sondern nur noch
	 * dem user.
	 * 
	 * @param taskId
	 * @param user
	 * @return Liste der für die Task erwarteten Eingaben oder leere Liste.
	 */
	List<FormPropertyTransfer> startTask(String taskId, String user)
			throws TaskException;

	/**
	 * Beendet die Task. Die formValues können auch leer oder null sein, falls
	 * vorher keine Eingaben erwartet wurden.
	 * 
	 * @param taskId
	 * @param userId
	 * @param formValues
	 */
	void endTask(String taskId, String userId, Map<String, String> formValues)
			throws TaskException;

	byte[] getProcessImage(String taskId);

	/**
	 * Liefert 2 Maps, mit Task-IDs, Task-Namen und Bearbeiter (wenn vorhanden).
	 * 
	 * @param group
	 * @return Map mit Tasks und Bearbeitern oder leere Map
	 */
	Map<String, Pair<String, String>> getAllTasksForGroup(String group);


	/**
	 * Entzieht die Task dem aktuell zugeordneten Nutzer und stellt sie damit
	 * wieder allen zur Verfügung.
	 * 
	 * @param taskId
	 * @throws TaskException
	 */
	void entzieheTask(String taskId) throws TaskException;

	/**
	 * Liefert alle Variablen, die im Scope der übergebenen Task sichtbar sind.
	 * 
	 * @param taskId
	 * @return
	 * @throws TaskException 
	 */
	Map<String, Object> getVariables(String taskId) throws TaskException;
}
