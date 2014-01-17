package de.blogspot.wrongtracks.prost.ejb.api;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import org.apache.commons.lang3.tuple.Pair;

import de.blogspot.wrongtracks.prost.ejb.exception.TaskException;
import de.blogspot.wrongtracks.prost.ejb.transfer.FormPropertyTransfer;

/**
 * Bean to work with tasks.
 * 
 * @author Ronny Bräunlich
 * 
 */
public interface TaskEJBRemote {

	Map<String, String> getTasksForUser(String user);

	/**
	 * Starts the task and sets the user as assignee. The task doesn't belong to
	 * the group anymore afterwards
	 * 
	 * @param taskId
	 * @param user
	 * @return list with expected input or empty list.
	 */
	List<FormPropertyTransfer> startTask(String taskId, String user)
			throws TaskException;

	/**
	 * Ends a task. formValues may be empty or null.
	 * 
	 * @param taskId
	 * @param userId
	 * @param formValues
	 */
	void endTask(String taskId, String userId, Map<String, String> formValues)
			throws TaskException;

	byte[] getProcessImage(String taskId);

	/**
	 * Returns 2 maps, with task-ids, task-names and editor (if exists).
	 * 
	 * @param group
	 * @return Map
	 */
	Map<String, Pair<String, String>> getAllTasksForGroup(String group);

	/**
	 * Dispossesses the task the current editor and makes it available for
	 * everyone.
	 * 
	 * @param taskId
	 * @throws TaskException
	 */
	void dispossessTask(String taskId) throws TaskException;

	/**
	 * Gets all variables, that are visible in the tasks scope
	 * 
	 * @param taskId
	 * @return
	 * @throws TaskException
	 */
	Map<String, Object> getVariables(String taskId) throws TaskException;
}
