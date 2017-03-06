package de.nlp4wp.bandpeyobaidawilke;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import de.nlp4wp.bandpeyobaidawilke.markup.Deletion;
import de.nlp4wp.bandpeyobaidawilke.markup.Insertion;
import de.nlp4wp.bandpeyobaidawilke.markup.Revision;
import de.nlp4wp.bandpeyobaidawilke.markup.Symbol;
import de.nlp4wp.bandpeyobaidawilke.markup.SymbolContainer;
import de.nlp4wp.bandpeyobaidawilke.xmltypes.Entry;
import de.nlp4wp.bandpeyobaidawilke.xmltypes.Event;
import de.nlp4wp.bandpeyobaidawilke.xmltypes.Log;
import de.nlp4wp.bandpeyobaidawilke.xmltypes.Part;

public class LogAnalyzer {
	private final Log log;
	private final List<Entry> metaEntries;

	private final List<Entry> sessionEntries;

	private final List<Event> eventList;
	private final SymbolContainer revisedText;

	public LogAnalyzer(final Log log) {
		this.log = log;
		this.metaEntries = new ArrayList<>();
		this.sessionEntries = new ArrayList<>();
		this.eventList = new ArrayList<>();
		this.revisedText = new SymbolContainer();
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
			int end = -1;
			int start = -1;
			int lastSelectionStart = -1, lastSelectionEnd = -1;
			String before = null;
			String after = null;
			String newText = null;

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

			// selection event
			if (event.getType().equals("selection") && start >= 0 && end >= 0) {
				lastSelectionStart = start;
				lastSelectionEnd = end;
			}

			// normal typing event
			if (event.getType().equals("keyboard") && key != null && value != null && position >= 0) {

				// System.out.println("keyPress: position: " + position + "
				// value: " + value + " key: " + key);
				if (key.equals("VK_BACK")) {
					// is there a selection left or right of the current
					// position? if so,
					// delete the whole selection from the revised text.
					if (lastSelectionStart == position - 1 || lastSelectionEnd == position) {
						this.log.addRevision(lastRevision);
						lastRevision = new Deletion(this.log.getRevisions().size());
						for (int i = lastSelectionStart; i < lastSelectionEnd; i++) {
							lastRevision.getRevisionSymbols().add(new Symbol(lastSelectionStart + i,
									this.revisedText.get(lastSelectionStart + i).getCharacter()));
							this.revisedText.get(i).setActive(false);
						}
					} else if (this.revisedText.get(position - 1) != null) {
						this.revisedText.get(position - 1).setActive(false);
						// is the last symbol of the last deletion the current
						// character?
						// if so, add the character to the revision and
						// deactivate the
						// symbols in the text.
						if (lastRevision instanceof Deletion && lastRevision.getFirstPosition() == position) {
							lastRevision.getRevisionSymbols().add(this.revisedText.get(position));
						} else if (lastTextPosition == position) {
							this.log.addRevision(lastRevision);
							lastRevision = new Deletion(this.log.getRevisions().size());
						}
					}

				} else if (key.equals("VK_DELETE")) {
					// is there a selection left or right of the current
					// position? if so,
					// delete the whole selection from the revised text.
					if (lastSelectionStart == position || lastSelectionEnd == position) {
						this.log.addRevision(lastRevision);
						lastRevision = new Deletion(this.log.getRevisions().size());
						for (int i = lastSelectionStart; i < lastSelectionEnd; i++) {
							lastRevision.getRevisionSymbols().add(new Symbol(lastSelectionStart + i,
									this.revisedText.get(lastSelectionStart + i).getCharacter()));
							this.revisedText.get(i).setActive(false);
						}
						// is the first symbol of the last deletion the current
						// character?
						// if so, add the character to the revision and
						// deactivate the
						// symbols in the text.
					} else if (lastRevision instanceof Deletion && lastRevision.getLastPosition() == position - 1) {
						lastRevision.getRevisionSymbols().add(this.revisedText.get(position));
					} else {
						this.log.addRevision(lastRevision);
						lastRevision = new Deletion(this.log.getRevisions().size());
						this.revisedText.get(position).setActive(false);
					}

				} else {
					this.revisedText.add(position, new Symbol(position, value));
					if (lastRevision != null && position != maxTextIndex
							&& lastRevision.getLastPosition() != position - 1) {
						this.log.addRevision(lastRevision);
						lastRevision = new Insertion(this.log.getRevisions().size());
						lastRevision.getRevisionSymbols().add(new Symbol(position, value));
					} else if (lastRevision != null && lastRevision.getLastPosition() == position - 1) {
						lastRevision.getRevisionSymbols().add(new Symbol(position, value));
					} else if (lastRevision != null && (this.log.getRevisions().size() == 0
							|| this.log.getRevisions().get(this.log.getRevisions().size() - 1) != lastRevision)) {
						this.log.addRevision(lastRevision);
					}
					lastTextPosition = position;
					// System.out.println(position + " - " + value);
					// System.out.println(this.revisedText.get(position).getCharacter());

				}
			}
			// // replacement events
			// TODO: Add revision handling (deleting and inserting)
			else if (event.getType().equals("replacement") && start >= 0 && end >= 0 && newText != null
					&& !newText.isEmpty()) {
				// System.out.println("replacement: startIndex: " + start + "
				// endIndex: " + end + " newText: " + newText);
				// System.out.println((end - start));
				// System.out.println(start + " - " + end + " - " + newText);

			}
			// insertion events (Zwischenablage o.Ä.)
			else if (event.getType().equals("insert") && position >= 0 && (before != null || after != null)) {
				// System.out.println(
				// "insertion: position: " + position + " text before: " +
				// before + " text after: " + after);
			}
		}
		System.out.println(this.log.getFilePath());
		// for (
		//
		// final Revision rev : this.log.getRevisions()) {
		// System.out.println(rev.getClass().getName() + " " +
		// rev.getSequentialNumber() + " - "
		// + rev.getFirstPosition() + " - " + rev.getRevisionSymbols());
		// System.out.println(rev.getLastPosition());
		// }
		System.out.println("Nr. of revisions: " + this.log.getRevisions().size());
		System.out.println("The total text is " + (maxTextIndex + 1) + " characters long.");
		for (final Revision rev : this.log.getRevisions()) {
			final StringBuilder sb = new StringBuilder();

			for (final Symbol symbol : rev.getRevisionSymbols()) {
				sb.append(symbol.getCharacter());
			}
			// System.out.println(sb.toString());
		}
		System.out.println(this.revisedText.toString());
	}

	public void flushToFile() {
		File f1 = new File(log.getFilePath());
		File f = new File("resources/OUTPUT/" + f1.getName() + "_OUTPUT.txt");

		try {
			f.createNewFile();
			PrintWriter writer = new PrintWriter(f, "UTF-8");
			writer.write(this.revisedText.toString());
			writer.close();
		} catch (IOException e) {
			Program.logException(e);
		}
	}

	private void addEvents() {

		try {
			// add events
			for (final Event e : this.log.getEventList()) {
				this.eventList.add(e);
			}
		} catch (final Exception e1) {
			Program.logException(e1);
		}
	}

	private void addMetaEntries() {
		// add meta entries
		try {
			for (final Entry e : this.log.getMeta().getEntries()) {
				this.metaEntries.add(e);
			}
		} catch (final Exception e1) {
			Program.logException(e1);
		}
	}

	private void addSessionEntries() {
		try {
			// add session entries
			for (final Entry e : this.log.getSession().getEntries()) {
				this.sessionEntries.add(e);
			}
		} catch (final Exception e1) {
			Program.logException(e1);
		}
	}
}
