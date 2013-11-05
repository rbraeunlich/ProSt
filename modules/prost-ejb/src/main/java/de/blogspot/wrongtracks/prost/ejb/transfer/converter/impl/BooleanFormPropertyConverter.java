package de.blogspot.wrongtracks.prost.ejb.transfer.converter.impl;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.form.BooleanFormType;

import de.blogspot.wrongtracks.prost.ejb.transfer.FormPropertyTransfer;
import de.blogspot.wrongtracks.prost.ejb.transfer.converter.AbstractFormPropertyConverter;

public class BooleanFormPropertyConverter extends
		AbstractFormPropertyConverter<BooleanFormType> {

	@Override
	public Class<BooleanFormType> getFormTypeClass() {
		return BooleanFormType.class;
	}

	@Override
	protected void doConvert(FormProperty formProperty,
			FormPropertyTransfer formPropertyTransfer) {
		formPropertyTransfer.setType(Boolean.class);
	}

}
