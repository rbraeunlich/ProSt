package de.blogspot.wrongtracks.prost.ejb.api;

import java.util.List;

import de.blogspot.wrongtracks.prost.ejb.transfer.HistoricFormPropertyInfo;

/**
 * Bean for historical data.
 * 
 * @author Ronny Bräunlich
 * 
 */
public interface HistoryEJBRemote {

	/**
	 * Get's all formProperty data. The list contains current values and old
	 * ones.
	 * 
	 */
	List<HistoricFormPropertyInfo> getHistoricFormPropertyDataForProcess(
			String processId);

	/**
	 * Get's all formProperty data. The list contains current values and old
	 * ones.
	 * 
	 */
	List<HistoricFormPropertyInfo> getHistoricFormPropertyDataForTask(
			String taskId);

}
