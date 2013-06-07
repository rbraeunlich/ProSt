package de.blogspot.wrongtracks.prost.ejb.transfer;

/**
 * Implementierung von {@link FormPropertyTransfer}.
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
	 * Bei einem Enum stecken hier die möglichen Werte drinne(Set von String) und bei einem Date ein DateTimeFormat mit dem Format.
	 */
	private Object extraFormTypeInformation;

	public FormPropertyTransferImpl(String id, String name, Class<?> type,
			String value, boolean readable, boolean required,
			boolean writable, Object extraFormTypeInformation) {
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
	public Object getExtraFormTypeInformation(){
		return extraFormTypeInformation;
	}

}
