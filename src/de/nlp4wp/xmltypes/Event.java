package de.nlp4wp.xmltypes;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "event")
public class Event {
	private int id;
	private String type;
	@XmlElement(name = "part")
	private List<Part> part;

	@XmlAttribute(name = "id")
	public int getId() {
		return this.id;
	}

	public List<Part> getPartlist() {
		return this.part;
	}

	@XmlAttribute(name = "type")
	public String getType() {
		return this.type;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public void setPart(final List<Part> part) {
		this.part = part;
	}

	public void setType(final String type) {
		this.type = type;
	}
}
