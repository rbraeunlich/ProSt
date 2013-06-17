package de.blogspot.wrongtracks.prost.ejb.transfer;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.List;

/**
 * Imitiert die Klasse org.activiti.engine.form.FormProperty damit die GUI keine
 * Abhängigkeit zu Activiti hat. Kommentare an den Methoden sind auch davon
 * kopiert.
 * 
 * @author Ronny Bräunlich
 * 
 */
public interface FormPropertyTransfer extends Serializable {

	/**
	 * The key used to submit the property in
	 * {@link FormService#submitStartFormData(String, java.util.Map)} or
	 * {@link FormService#submitTaskFormData(String, java.util.Map)}
	 */
	String getId();

	/** The display label */
	String getName();

	/** Type of the property. */
	Class<?> getType();

	/** Optional value that should be used to display in this property */
	String getValue();

	/**
	 * Is this property read to be displayed in the form and made accessible
	 * with the methods {@link FormService#getStartFormData(String)} and
	 * {@link FormService#getTaskFormData(String)}.
	 */
	boolean isReadable();

	/** Is this property expected when a user submits the form? */
	boolean isWritable();

	/** Is this property a required input field */
	boolean isRequired();

	/**
	 * Liefert extra Informationen entsprechend dem erwarteten Eingabetyp.
	 * <table border="1">
	 * <tr>
	 * <th>Typ im XML</th>
	 * <th>Rückgabewert dieser Methode</th>
	 * </tr>
	 * <tr>
	 * <td>string</td>
	 * <td>{@link String}</td>
	 * </tr>
	 * <tr>
	 * <td>date</td>
	 * <td>{@link DateFormat}</td>
	 * </tr>
	 * <tr>
	 * <td>enum</td>
	 * <td>{@link List}</td>
	 * </tr>
	 * </table>
	 * 
	 * @return
	 */
	Object getExtraFormTypeInformation();

	void setExtraFormTypeInformation(Object extraFormTypeInformation);

	void setType(Class<?> type);
}
