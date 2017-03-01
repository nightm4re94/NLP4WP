package de.nlp4wp.bandpeyobaidawilke.xmltypes;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "session")
public class Session {
	@XmlElement(name = "entry")
	private List<Entry> entryList;

	public List<Entry> getEntries() {
		return this.entryList;
	}

	public void setEntries(final List<Entry> entryList) {
		this.entryList = entryList;
	}

}
