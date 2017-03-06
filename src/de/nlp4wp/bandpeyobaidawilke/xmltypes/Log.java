package de.nlp4wp.bandpeyobaidawilke.xmltypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.nlp4wp.bandpeyobaidawilke.markup.Revision;

@XmlRootElement
public class Log {

	private Meta meta;
	private Session session;
	private List<Event> eventList;
	private Map<Integer, String> finalText;
	private final List<Revision> revisions = new ArrayList<>();
	private String filePath;

	public void addRevision(final Revision revision) {
		if (revision == null) {
			return;
		}
		this.revisions.add(revision);
	}

	@XmlElement(name = "event")
	public List<Event> getEventList() {
		return this.eventList;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public Map<Integer, String> getFinalText() {
		return this.finalText;
	}

	@XmlElement(name = "meta")
	public Meta getMeta() {
		return this.meta;
	}

	public List<Revision> getRevisions() {
		return this.revisions;
	}

	@XmlElement(name = "session")
	public Session getSession() {
		return this.session;
	}

	public void setEventList(final List<Event> eventList) {
		this.eventList = eventList;
	}

	public void setFilePath(final String filePath) {
		this.filePath = filePath;
	}

	public void setMeta(final Meta meta) {
		this.meta = meta;
	}

	public void setSession(final Session session) {
		this.session = session;
	}

}