package de.nlp4wp.bandpeyobaidawilke;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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

		int lastSelectionStart = -1, lastSelectionEnd = -1, lastSelectionLength = -1;
		int charsInclSpaces = 0, charsExclSpaces = 0;

		for (final Event event : this.eventList) {
			int position = -1;
			String key = null;
			String value = null;
			int end = -1;
			int start = -1;
			String before = null;
			String after = null;
			String newText = null;
			for (final Part part : event.getPartlist()) {

				// extract position
				if (part.getPosition() >= 0) {
					position = part.getPosition();
				}
				// how many chars are in the text (w/o whitespaces)
				charsInclSpaces = part.getCharinclspaces();

				// how many chars are in the text (with whitespaces)
				charsExclSpaces = part.getCharexclspaces();

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
				lastSelectionLength = end - start;
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
						for (int i = 0; i < lastSelectionLength; i++) {
							this.revisedText.deleteSingle(i);
						}
					} else if (revisedText.getNumberOfActiveSymbols() > position && position > 0) {
						this.revisedText.deleteSingle(position);
					}
				} else if (key.equals("VK_DELETE")) {
					// is there a selection left or right of the current
					// position? if so,
					// delete the whole selection from the revised text.
					if (lastSelectionStart == position - 1 || lastSelectionEnd == position) {
						for (int i = 0; i < lastSelectionLength; i++) {
							this.revisedText.deleteMultiple(i, i);
						}
					} else if (revisedText.getNumberOfActiveSymbols() > position && position > 0) {
						this.revisedText.deleteSingle(position + 1);
					}
				} else {
					Symbol newSymbol = new Symbol(this.revisedText.getRevision(), value);
					if (position == this.revisedText.getNumberOfActiveSymbols()) {
						this.revisedText.add(newSymbol);
					} else {
						this.revisedText.insertSingle(position, newSymbol);
					}
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
				SymbolContainer s = new SymbolContainer();
				for (Character c : newText.toCharArray()) {
					s.add(new Symbol(this.revisedText.getRevision(), c.toString()));
				}
				this.revisedText.replace(start, end, s);
			}
			// insertion events (Zwischenablage o.Ä.)
			else if (event.getType().equals("insert") && position >= 0 && before != null && after != null) {
				// System.out.println(
				// "insertion: position: " + position + " text before: " +
				// before + " text after: " + after);
				SymbolContainer s = new SymbolContainer();
				for (Character c : after.toCharArray()) {
					s.add(new Symbol(this.revisedText.getRevision(), c.toString()));
				}
				this.revisedText.insertMultiple(position, s);
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
		System.out.println("Nr. of revisions: " + this.revisedText.getRevision());
		System.out.println("The total text is " + (this.revisedText.getNumberOfActiveSymbols()) + "/" + charsInclSpaces
				+ " characters long.");
		// System.out.println(this.revisedText.getActiveChars());
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
