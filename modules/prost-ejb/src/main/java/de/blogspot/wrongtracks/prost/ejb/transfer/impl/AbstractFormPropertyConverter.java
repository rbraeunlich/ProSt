package de.blogspot.wrongtracks.prost.ejb.transfer.impl;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;

import de.blogspot.wrongtracks.prost.ejb.transfer.FormPropertyTransfer;
import de.blogspot.wrongtracks.prost.ejb.transfer.converter.FormPropertyConverter;

public abstract class AbstractFormPropertyConverter<T extends FormProperty>
		implements FormPropertyConverter<T> {

	@Override
	public FormPropertyTransfer convert(T formProperty) {
		// @formatter:off
		String  id = formProperty.getId(), 
				name = formProperty.getName(), 
				value = formProperty.getValue();
		boolean readable = formProperty.isReadable(), 
				required = formProperty.isRequired(), 
				writable = formProperty.isWritable();
		// @formatter:on
		FormPropertyTransferImpl formPropertyTransfer = new FormPropertyTransferImpl(
				id, name, null, value, readable, required, writable, null);
		doConvert(formProperty, formPropertyTransfer);
		return formPropertyTransfer;
	}

	protected abstract FormPropertyTransfer doConvert(T formProperty,
			FormPropertyTransfer formPropertyTransfer);
}
