package de.blogspot.wrongtracks.prost.ejb.transfer;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.List;

/**
 * Imitates the class org.activiti.engine.form.FormProperty so that clients
 * don't depend on Activiti. Comments are copied, too.
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
	 * delivers extra informationen according to the expected input.
	 * <table border="1">
	 * <tr>
	 * <th>type in BPMN-XML</th>
	 * <th>retun type</th>
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
