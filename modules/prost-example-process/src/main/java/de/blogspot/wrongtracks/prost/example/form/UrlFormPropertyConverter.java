package de.blogspot.wrongtracks.prost.example.form;

import java.net.URL;

import org.activiti.engine.form.FormProperty;

import de.blogspot.wrongtracks.prost.ejb.transfer.FormPropertyTransfer;
import de.blogspot.wrongtracks.prost.ejb.transfer.converter.AbstractFormPropertyConverter;

public class UrlFormPropertyConverter extends
		AbstractFormPropertyConverter<UrlFormType> {

	@Override
	public Class<UrlFormType> getFormTypeClass() {
		return UrlFormType.class;
	}

	@Override
	protected void doConvert(FormProperty formProperty,
			FormPropertyTransfer formPropertyTransfer) {
		formPropertyTransfer.setType(URL.class);
	}

}
