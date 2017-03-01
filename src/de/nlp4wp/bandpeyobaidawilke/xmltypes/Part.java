package de.nlp4wp.bandpeyobaidawilke.xmltypes;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "part")
public class Part {
	private String type;
	private String button;
	private String key;
	private String value;
	private int x;
	private int y;
	private long startTime;
	private long endTime;
	private String title;
	private String before;
	private String after;
	private int start = -1;
	private int end = -1;
	private String newText;
	private int position = -1;

	private boolean replay;

	private long documentLength;

	private int charexclspaces;

	private int charinclspaces;

	private int linecount;

	private int pagecount;

	private int paragraphcount;

	private int wordcount;

	@XmlElement(name = "after")
	public String getAfter() {
		return this.after;
	}

	@XmlElement(name = "before")
	public String getBefore() {
		return this.before;
	}

	@XmlElement(name = "button")
	public String getButton() {
		return this.button;
	}

	@XmlElement(name = "charexclspaces")
	public int getCharexclspaces() {
		return this.charexclspaces;
	}

	@XmlElement(name = "charinclspaces")
	public int getCharinclspaces() {
		return this.charinclspaces;
	}

	@XmlElement(name = "documentLength")
	public long getDocumentLength() {
		return this.documentLength;
	}

	@XmlElement(name = "end")
	public int getEnd() {
		return this.end;
	}

	@XmlElement(name = "endTime")
	public long getEndTime() {
		return this.endTime;
	}

	@XmlElement(name = "key")
	public String getKey() {
		return this.key;
	}

	@XmlElement(name = "linecount")
	public int getLinecount() {
		return this.linecount;
	}

	@XmlElement(name = "newtext")

	public String getNewText() {
		return this.newText;
	}

	@XmlElement(name = "pagecount")
	public int getPagecount() {
		return this.pagecount;
	}

	@XmlElement(name = "paragraphcount")
	public int getParagraphcount() {
		return this.paragraphcount;
	}

	@XmlElement(name = "position")
	public int getPosition() {
		return this.position;
	}

	@XmlElement(name = "start")
	public int getStart() {
		return this.start;
	}

	@XmlElement(name = "startTime")
	public long getStartTime() {
		return this.startTime;
	}

	@XmlElement(name = "title")
	public String getTitle() {
		return this.title;
	}

	@XmlAttribute(name = "type")
	public String getType() {
		return this.type;
	}

	@XmlElement(name = "value")
	public String getValue() {
		return this.value;
	}

	@XmlElement(name = "wordcount")
	public int getWordcount() {
		return this.wordcount;
	}

	@XmlElement(name = "x")
	public int getX() {
		return this.x;
	}

	@XmlElement(name = "y")
	public int getY() {
		return this.y;
	}

	@XmlElement(name = "replay")
	public boolean isReplay() {
		return this.replay;
	}

	public void setAfter(final String after) {
		this.after = after;
	}

	public void setBefore(final String before) {
		this.before = before;
	}

	public void setButton(final String button) {
		this.button = button;
	}

	public void setCharexclspaces(final int charexclspaces) {
		this.charexclspaces = charexclspaces;
	}

	public void setCharinclspaces(final int charinclspaces) {
		this.charinclspaces = charinclspaces;
	}

	public void setDocumentLength(final long documentLength) {
		this.documentLength = documentLength;
	}

	public void setEnd(final int end) {
		this.end = end;
	}

	public void setEndTime(final long endTime) {
		this.endTime = endTime;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	public void setLinecount(final int linecount) {
		this.linecount = linecount;
	}

	public void setNewText(final String newText) {
		this.newText = newText;
	}

	public void setPagecount(final int pagecount) {
		this.pagecount = pagecount;
	}

	public void setParagraphcount(final int paragraphcount) {
		this.paragraphcount = paragraphcount;
	}

	public void setPosition(final int position) {
		this.position = position;
	}

	public void setReplay(final boolean replay) {
		this.replay = replay;
	}

	public void setStart(final int start) {
		this.start = start;
	}

	public void setStartTime(final long startTime) {
		this.startTime = startTime;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public void setWordcount(final int wordcount) {
		this.wordcount = wordcount;
	}

	public void setX(final int x) {
		this.x = x;
	}

	public void setY(final int y) {
		this.y = y;
	}

}
