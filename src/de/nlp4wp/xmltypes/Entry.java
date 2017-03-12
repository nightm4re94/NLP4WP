package de.nlp4wp.xmltypes;

import javax.xml.bind.annotation.XmlElement;

public class Entry {
	@XmlElement(name = "key")
	String key;
	String value;

	public String getkey() {
		return this.key;
	}

	@XmlElement(name = "value")
	public String getValue() {
		return this.value;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	public void setValue(final String value) {
		this.value = value;
	}
}
