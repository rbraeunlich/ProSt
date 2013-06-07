package de.blogspot.wrongtracks.prost.ejb.transfer;

import java.io.Serializable;
import java.util.Date;

/**
 * Klasse für Informationen über Werte, die über FormProperties eingegeben
 * wurden.
 * 
 * @author Ronny Bräunlich
 * 
 */
public class HistoricFormPropertyInfo implements Serializable {

	private static final long serialVersionUID = -7089132808797477417L;
	private String id;
	private String propertyId;
	private String value;
	private Date time;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HistoricFormPropertyInfo other = (HistoricFormPropertyInfo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Date getTime() {
		return time;
	}
}
