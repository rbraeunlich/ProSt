package de.blogspot.wrongtracks.prost.ejb.transfer.converter.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.form.DateFormType;

import de.blogspot.wrongtracks.prost.ejb.transfer.FormPropertyTransfer;
import de.blogspot.wrongtracks.prost.ejb.transfer.converter.AbstractFormPropertyConverter;

public class DateFormPropertyConverter extends
		AbstractFormPropertyConverter<DateFormType> {

	@Override
	public Class<DateFormType> getFormTypeClass() {
		return DateFormType.class;
	}

	@Override
	protected void doConvert(FormProperty formProperty,
			FormPropertyTransfer formPropertyTransfer) {
		formPropertyTransfer.setType(Date.class);
		formPropertyTransfer.setExtraFormTypeInformation(new SimpleDateFormat(
				String.valueOf(formProperty.getType().getInformation(
						"datePattern"))));
	}

}
