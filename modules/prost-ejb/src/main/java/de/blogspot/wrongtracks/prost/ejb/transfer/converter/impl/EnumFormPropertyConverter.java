package de.blogspot.wrongtracks.prost.ejb.transfer.converter.impl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.form.EnumFormType;

import de.blogspot.wrongtracks.prost.ejb.transfer.FormPropertyTransfer;
import de.blogspot.wrongtracks.prost.ejb.transfer.converter.AbstractFormPropertyConverter;

public class EnumFormPropertyConverter extends
		AbstractFormPropertyConverter<EnumFormType> {

	@Override
	public Class<EnumFormType> getFormTypeClass() {
		return EnumFormType.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doConvert(FormProperty formProperty,
			FormPropertyTransfer formPropertyTransfer) {

		formPropertyTransfer.setType(Enumeration.class);
		Map<String, String> information = (Map<String, String>) formProperty
				.getType().getInformation("values");
		// ArrayList, because it's serializable
		formPropertyTransfer.setExtraFormTypeInformation(new ArrayList<String>(
				information.keySet()));
	}

}
