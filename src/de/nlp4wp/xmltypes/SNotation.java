package de.nlp4wp.xmltypes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "s-notation")
@XmlType(propOrder = { "meta", "session", "markup" })
public class SNotation {
	private Meta meta;
	private Session session;
	private String markup;

	@XmlElement(name = "markup")
	public String getMarkup() {
		return markup;
	}

	public void setMarkup(String markup) {
		this.markup = markup;
	}

	public void setMeta(final Meta meta) {
		this.meta = meta;
	}

	public void setSession(final Session session) {
		this.session = session;
	}

	@XmlElement(name = "meta")
	public Meta getMeta() {
		return this.meta;
	}

	@XmlElement(name = "session")
	public Session getSession() {
		return this.session;
	}

}
