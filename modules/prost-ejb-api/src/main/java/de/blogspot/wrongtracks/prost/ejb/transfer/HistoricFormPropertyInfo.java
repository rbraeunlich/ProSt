package de.blogspot.wrongtracks.prost.ejb.transfer;

import java.util.Date;

public interface HistoricFormPropertyInfo {

	String getId();

	void setId(String id);

	String getPropertyId();

	void setPropertyId(String propertyId);

	String getValue();

	void setValue(String value);

	void setTime(Date time);

	Date getTime();

}