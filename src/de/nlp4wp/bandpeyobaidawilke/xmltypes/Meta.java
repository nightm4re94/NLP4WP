package de.nlp4wp.bandpeyobaidawilke.xmltypes;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "meta")
public class Meta {
	private List<Entry> entryList;

	@XmlElement(name = "entry")
	public List<Entry> getEntries() {
		return this.entryList;
	}

	public void setEntries(final List<Entry> entryList) {
		this.entryList = entryList;
	}

}
