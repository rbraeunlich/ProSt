package de.blogspot.wrongtracks.prost.ejb.transfer.impl;

import java.text.DateFormat;

import de.blogspot.wrongtracks.prost.ejb.transfer.FormPropertyTransfer;

/**
 * @author Ronny Bräunlich
 * 
 */
public class FormPropertyTransferImpl implements FormPropertyTransfer {

	private static final long serialVersionUID = -5700795861463619299L;
	private String id;
	private String name;
	private Class<?> type;
	private String value;
	private boolean readable;
	private boolean required;
	private boolean writable;
	/**
	 * Field to store extra values. For enum-formproperties it'll be a set of
	 * string and for date-formproperties a {@link DateFormat}
	 */
	private Object extraFormTypeInformation;

	public FormPropertyTransferImpl(String id, String name, Class<?> type,
			String value, boolean readable, boolean required, boolean writable,
			Object extraFormTypeInformation) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.value = value;
		this.readable = readable;
		this.required = required;
		this.writable = writable;
		this.extraFormTypeInformation = extraFormTypeInformation;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setType(Class<?> type) {
		this.type = type;
	}

	@Override
	public Class<?> getType() {
		return type;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public boolean isReadable() {
		return readable;
	}

	@Override
	public boolean isWritable() {
		return writable;
	}

	@Override
	public boolean isRequired() {
		return required;
	}

	@Override
	public void setExtraFormTypeInformation(Object extraFormTypeInformation) {
		this.extraFormTypeInformation = extraFormTypeInformation;
	}

	@Override
	public Object getExtraFormTypeInformation() {
		return extraFormTypeInformation;
	}

}
