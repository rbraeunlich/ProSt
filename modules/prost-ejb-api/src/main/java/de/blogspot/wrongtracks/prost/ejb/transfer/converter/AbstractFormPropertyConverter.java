package de.blogspot.wrongtracks.prost.ejb.transfer.converter;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;

import de.blogspot.wrongtracks.prost.ejb.transfer.FormPropertyTransfer;
import de.blogspot.wrongtracks.prost.ejb.transfer.impl.FormPropertyTransferImpl;

public abstract class AbstractFormPropertyConverter<T extends FormType>
		implements FormPropertyConverter<T> {

	@Override
	public FormPropertyTransfer convert(FormProperty formProperty) {
		// @formatter:off
		String  id = formProperty.getId(), 
				name = formProperty.getName(), 
				value = formProperty.getValue();
		boolean readable = formProperty.isReadable(), 
				required = formProperty.isRequired(), 
				writable = formProperty.isWritable();
		// @formatter:on
		FormPropertyTransfer formPropertyTransfer = new FormPropertyTransferImpl(
				id, name, null, value, readable, required, writable, null);
		doConvert(formProperty, formPropertyTransfer);
		return formPropertyTransfer;
	}

	protected abstract void doConvert(FormProperty formProperty,
			FormPropertyTransfer formPropertyTransfer);
}
