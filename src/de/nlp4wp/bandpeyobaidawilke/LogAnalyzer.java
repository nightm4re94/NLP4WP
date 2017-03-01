package de.nlp4wp.bandpeyobaidawilke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.nlp4wp.bandpeyobaidawilke.markup.Revision;
import de.nlp4wp.bandpeyobaidawilke.markup.Revision.RevisionType;
import de.nlp4wp.bandpeyobaidawilke.xmltypes.Entry;
import de.nlp4wp.bandpeyobaidawilke.xmltypes.Event;
import de.nlp4wp.bandpeyobaidawilke.xmltypes.Log;
import de.nlp4wp.bandpeyobaidawilke.xmltypes.Part;

public class LogAnalyzer {
	private static final Logger LOGGER = Logger.getLogger(LogAnalyzer.class.getName());
	private Log log;
	private List<Entry> metaEntries;

	private List<Entry> sessionEntries;

	private List<Event> eventList;
	private List<String> revisedText;

	public LogAnalyzer(final Log log) {
		this.log = log;
		this.metaEntries = new ArrayList<>();
		this.sessionEntries = new ArrayList<>();
		this.eventList = new ArrayList<>();
		this.revisedText = new ArrayList<>();
		this.addMetaEntries();
		this.addSessionEntries();
		this.addEvents();
	}

	public void analyzeLog() {
		int maxTextIndex = -1;
		int lastTextPosition = -1;
		Revision lastRevision = null;
		for (final Event event : this.eventList) {
			int position = -1;
			String key = null;
			String value = null;
			int start = -1;
			int end = -1;
			String newText = null;
			String before = null;
			String after = null;

			for (final Part part : event.getPartlist()) {

				// extract position
				if (part.getPosition() >= 0) {
					position = part.getPosition();
					if (position > maxTextIndex) {
						maxTextIndex = position;
					}
				}

				// replacement events
				// extract start index
				if (part.getStart() >= 0) {
					start = part.getStart();
				}
				// extract end index
				if (part.getEnd() >= 0) {
					end = part.getEnd();
				}
				// extract newText
				if (part.getNewText() != null && !part.getNewText().isEmpty()) {
					newText = part.getNewText();
				}
				// -------------------------------------------------------------
				// keyboard events
				// extract key
				if (part.getKey() != null && !part.getKey().isEmpty()) {
					key = part.getKey();
				}
				// extract value
				if (part.getValue() != null && !part.getValue().isEmpty()) {
					value = part.getValue();
				}
				// -------------------------------------------------------------
				// insertion events
				// extract before text
				if (part.getBefore() != null && !part.getBefore().isEmpty()) {
					before = part.getBefore();
				}
				// extract after text
				if (part.getAfter() != null && !part.getAfter().isEmpty()) {
					after = part.getAfter();
				}
			}
			// normal typing event
			if (event.getType().equals("keyboard") && key != null && value != null && position >= 0) {

				// System.out.println("keyPress: position: " + position + "
				// value: " + value + " key: " + key);
				if (key.equals("VK_BACK")) {
					if (lastRevision == null) {
						lastRevision = new Revision(log.getRevisions().size());
						lastRevision.setType(RevisionType.DELETION);
					} else if (position != lastRevision.getRevisionIndex()) {
						lastRevision = new Revision(log.getRevisions().size());
						lastRevision.setType(RevisionType.DELETION);

					}
					String prevText = lastRevision.getRevisionText();
					if (prevText != null) {
						lastRevision.setRevisionText(revisedText.get(position - 1) + lastRevision.getRevisionText());
					} else {
						if (position != 0) {
							lastRevision.setRevisionText(revisedText.get(position - 1));
						}

					}
					lastRevision.setRevisionIndex(position - 1);
					if (position != 0) {
						revisedText.remove(position - 1);
					}

				} else {
					revisedText.add(position, value);
				}

			}
			// replacement events
			// TODO: Add revision handling (deleting and inserting)
			else if (event.getType().equals("replacement") && start >= 0 && end >= 0 && newText != null
					&& !newText.isEmpty()) {
				// System.out.println("replacement: startIndex: " + start + "
				// endIndex: " + end + " newText: " + newText);
				// System.out.println((end - start));
				// System.out.println(start + " - " + end + " - " + newText);
				for (int i = 0; i < end - start; i++) {
					if (i < newText.length()) {
						Character c = newText.charAt(i);
						int charInd = start + i;
						if (charInd < revisedText.size()) {
							revisedText.set(charInd, c.toString());
						} else {
							revisedText.add(charInd, c.toString());

						}
					} else {
						revisedText.remove(start + newText.length());
					}
				}
			}
			// insertion events (Zwischenablage o.Ä.
			// TODO: add revision handling
			else if (event.getType().equals("insert") && position >= 0 && (before != null || after != null)) {
				// System.out.println(
				// "insertion: position: " + position + " text before: " +
				// before + " text after: " + after);
				Revision newRevision = new Revision(log.getRevisions().size());
				newRevision.setType(RevisionType.INSERTION);
				newRevision.setRevisionIndex(position);
				newRevision.setBreakIndex(lastTextPosition);

				for (int i = 0; i < after.length(); i++) {
					Character c = after.toCharArray()[i];
					revisedText.add((position + i), c.toString());
					for (Revision rev : log.getRevisions()) {
						if (rev.getBreakIndex() >= position) {
							rev.modifyBreakIndex(+1);
						}
					}
					if (newRevision.getBreakIndex() >= position) {
						newRevision.modifyBreakIndex(+1);
					}
					if (newRevision.getRevisionText() != null) {
						newRevision.setRevisionText(newRevision.getRevisionText() + c);
					} else {
						newRevision.setRevisionText(c.toString());
					}

				}
				lastRevision = newRevision;
				log.addRevision(lastRevision);
			}
			if (position >= 0) {
				lastTextPosition = position;
			} else if (end >= 0) {
				lastTextPosition = end;
			}
		}
		System.out.println(this.log.getFilePath());
		for (

		Revision rev : this.log.getRevisions()) {
			System.out.println(rev.getType() + " " + rev.getSequentialNumber() + " - " + rev.getRevisionIndex() + " - "
					+ rev.getRevisionText());
			System.out.println(rev.getBreakIndex());
		}
		System.out.println("The total text is " + (maxTextIndex + 1) + " characters long.");
		final StringBuilder builder = new StringBuilder();
		for (final String c : this.revisedText) {
			builder.append(c);
		}
		System.out.println(builder.toString());
	}

	private void addEvents() {

		try {
			// add events
			for (final Event e : this.log.getEventList()) {
				this.eventList.add(e);
			}
		} catch (final Exception e1) {
			LOGGER.log(Level.SEVERE, e1.toString(), e1);
		}
	}

	private void addMetaEntries() {
		// add meta entries
		try {
			for (final Entry e : this.log.getMeta().getEntries()) {
				this.metaEntries.add(e);
			}
		} catch (final Exception e1) {
			LOGGER.log(Level.SEVERE, e1.toString(), e1);
		}
	}

	private void addSessionEntries() {
		try {
			// add session entries
			for (final Entry e : this.log.getSession().getEntries()) {
				this.sessionEntries.add(e);
			}
		} catch (final Exception e1) {
			LOGGER.log(Level.SEVERE, e1.toString(), e1);
		}
	}
}
