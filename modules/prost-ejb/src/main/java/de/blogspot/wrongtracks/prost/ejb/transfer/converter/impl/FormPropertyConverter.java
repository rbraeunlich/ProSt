package de.blogspot.wrongtracks.prost.ejb.transfer.converter.impl;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;
import org.activiti.engine.impl.form.BooleanFormType;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.EnumFormType;
import org.activiti.engine.impl.form.LongFormType;
import org.activiti.engine.impl.form.StringFormType;

import de.blogspot.wrongtracks.prost.ejb.transfer.FormPropertyTransfer;
import de.blogspot.wrongtracks.prost.ejb.transfer.impl.FormPropertyTransferImpl;
import de.blogspot.wrongtracks.prost.ejb.transfer.impl.UrlFormType;

/**
 * Klasse um aus {@link FormProperty} Objekten {@link FormPropertyTransfer}
 * Objekte zu erzeugen.
 * 
 * @author Ronny Bräunlich
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class FormPropertyConverter {

	public static FormPropertyTransfer convertProperty(FormProperty formProperty) {
		String 	id = formProperty.getId(), 
				name = formProperty.getName(), 
				value = formProperty.getValue();
		boolean readable = formProperty.isReadable(), 
				required = formProperty.isRequired(), 
				writable = formProperty.isWritable();
		
		FormType formType = formProperty.getType();
		Class type = null;
		Object extraFormTypeInformation = null;
		if (formType instanceof BooleanFormType) {
			type = Boolean.class;
		}
		if (formType instanceof DateFormType) {
			type = Date.class;
			extraFormTypeInformation = new SimpleDateFormat(
					String.valueOf(formType.getInformation("datePattern")));
		}
		if (formType instanceof EnumFormType) {
			type = Enumeration.class;
			Map<String, String> information = (Map<String, String>) formType
					.getInformation("values");
			// ArrayList, da diese im Ggsatz zum gelieferten Set serialisierbar
			// ist
			extraFormTypeInformation = new ArrayList(information.keySet());
		}
		if (formType instanceof LongFormType) {
			type = Long.class;
		}
		if (formType instanceof StringFormType) {
			type = String.class;
		}
		if(formType instanceof UrlFormType){
			type = URL.class;
		}
		return new FormPropertyTransferImpl(id, name, type, value, readable,
				required, writable, extraFormTypeInformation);

	}

}
