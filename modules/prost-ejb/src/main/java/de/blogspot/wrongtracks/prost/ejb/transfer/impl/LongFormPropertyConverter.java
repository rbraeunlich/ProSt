package de.blogspot.wrongtracks.prost.ejb.transfer.impl;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.form.LongFormType;

import de.blogspot.wrongtracks.prost.ejb.transfer.FormPropertyTransfer;
import de.blogspot.wrongtracks.prost.ejb.transfer.converter.AbstractFormPropertyConverter;

public class LongFormPropertyConverter extends
		AbstractFormPropertyConverter<LongFormType> {

	@Override
	public Class<LongFormType> getFormTypeClass() {
		return LongFormType.class;
	}

	@Override
	protected void doConvert(FormProperty formProperty,
			FormPropertyTransfer formPropertyTransfer) {
		formPropertyTransfer.setType(Long.class);
	}

}
