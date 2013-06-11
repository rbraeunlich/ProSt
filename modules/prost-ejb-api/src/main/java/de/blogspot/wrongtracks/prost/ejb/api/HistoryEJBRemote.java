package de.blogspot.wrongtracks.prost.ejb.api;

import java.util.List;

import javax.ejb.Remote;

import de.blogspot.wrongtracks.prost.ejb.transfer.HistoricFormPropertyInfo;
import de.blogspot.wrongtracks.prost.ejb.transfer.HistoricFormPropertyInfoImpl;

/**
 * Bean zur Abfrage von historischen Daten.
 * 
 * @author Ronny Bräunlich
 * 
 */
@Remote
public interface HistoryEJBRemote {

	/**
	 * Liefert zur übergebenene Task ID sämtliche FormProperty-Daten. Die Liste
	 * beinhaltet die aktuell gültigen Daten, sowie alte, überschriebene.
	 * 
	 * @param taskId
	 * @return Liste von {@link HistoricFormPropertyInfoImpl}
	 */
	List<HistoricFormPropertyInfo> getHistoricFormPropertyDataForProcess(String processId);

	List<HistoricFormPropertyInfo> getHistoricFormPropertyDataForTask(
			String taskId);

}
