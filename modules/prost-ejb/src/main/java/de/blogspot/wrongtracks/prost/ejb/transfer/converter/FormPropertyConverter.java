package de.blogspot.wrongtracks.prost.ejb.transfer.converter;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;

import de.blogspot.wrongtracks.prost.ejb.transfer.FormPropertyTransfer;

public interface FormPropertyConverter<T extends FormProperty> {

	public FormPropertyTransfer convert(T formProperty);
	
	public Class<? extends FormType> getFormTypeClass();
}
