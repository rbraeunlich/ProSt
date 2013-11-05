package de.blogspot.wrongtracks.prost.ejb.transfer.converter.impl;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.form.StringFormType;

import de.blogspot.wrongtracks.prost.ejb.transfer.FormPropertyTransfer;
import de.blogspot.wrongtracks.prost.ejb.transfer.converter.AbstractFormPropertyConverter;

public class StringFormPropertyConverter extends
		AbstractFormPropertyConverter<StringFormType> {

	@Override
	public Class<StringFormType> getFormTypeClass() {
		return StringFormType.class;
	}

	@Override
	protected void doConvert(FormProperty formProperty,
			FormPropertyTransfer formPropertyTransfer) {
		formPropertyTransfer.setType(String.class);
	}

}
