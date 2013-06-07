package de.blogspot.wrongtracks.prost.ejb.transfer.impl;

import java.net.MalformedURLException;
import java.net.URL;

import org.activiti.engine.form.AbstractFormType;

public class UrlFormType extends AbstractFormType {

	
	@Override
	public String getName() {
		return "url";
	}

	@Override
	public Object convertFormValueToModelValue(String propertyValue) {
		try {
			return new URL(propertyValue);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String convertModelValueToFormValue(Object modelValue) {
		if(modelValue == null)
			return null;
		return modelValue.toString();
	}
}
