package de.nlp4wp.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import de.nlp4wp.markup.Symbol;
import de.nlp4wp.markup.SymbolContainer;
import de.nlp4wp.xmltypes.Entry;
import de.nlp4wp.xmltypes.Event;
import de.nlp4wp.xmltypes.Log;
import de.nlp4wp.xmltypes.Part;
import de.nlp4wp.xmltypes.SNotation;

public class LogAnalyzer {
	private final Log log;
	private final List<Entry> metaEntries;

	private final List<Entry> sessionEntries;

	private final List<Event> eventList;
	private final SymbolContainer revisedText;
	private SNotation output;

	public LogAnalyzer(final Log log) {
		this.log = log;
		this.metaEntries = new ArrayList<>();
		this.sessionEntries = new ArrayList<>();
		this.eventList = new ArrayList<>();
		this.revisedText = new SymbolContainer();
		this.output = new SNotation();
		this.addMetaEntries();
		this.addSessionEntries();
		this.addEvents();
	}

	public void analyzeLog() {

		int lastSelectionStart = -1, lastSelectionEnd = -1;
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
			}

			// normal typing event
			if (event.getType().equals("keyboard") && key != null && value != null && position >= 0) {
				if (key.equals("VK_BACK")) {
					// is there a selection left or right of the current
					// position? if so,
					// delete the whole selection from the revised text.
					if (lastSelectionStart == position || lastSelectionEnd == position - 1) {
						this.revisedText.deleteMultiple(lastSelectionStart, lastSelectionEnd);
					} else if (revisedText.getNumberOfActiveSymbols() > position && position > 0) {
						this.revisedText.deleteSingle(position - 1);
					}
				} else if (key.equals("VK_DELETE")) {
					// is there a selection left or right of the current
					// position? if so,
					// delete the whole selection from the revised text.
					if (lastSelectionStart == position - 1 || lastSelectionEnd == position) {
						this.revisedText.deleteMultiple(lastSelectionStart, lastSelectionEnd);
					} else if (revisedText.getNumberOfActiveSymbols() >= position && position > 0) {
						this.revisedText.deleteSingle(position);
					}
				} else {
					this.revisedText.insertSingle(position, new Symbol(value));
				}
			}
			// // replacement events
			else if (event.getType().equals("replacement") && start >= 0 && end >= 0 && newText != null
					&& !newText.isEmpty() && !newText.equals("STARTOFHEADING")) {

				SymbolContainer s = new SymbolContainer();
				for (Character c : newText.toCharArray()) {
					s.add(new Symbol(c.toString()));
				}
				this.revisedText.replaceMultiple(start, end, s);
			}
			// insertion events (Zwischenablage o.Ä.)
			else if (event.getType().equals("insert") && position >= 0 && before != null && after != null) {
				SymbolContainer s = new SymbolContainer();
				for (Character c : after.toCharArray()) {
					s.add(new Symbol(c.toString()));
				}
				this.revisedText.insertMultiple(position, s);
			}
		}
		this.revisedText.cleanup();
		Program.LOGGER.log(Level.INFO, "Successfully analyzed log: " + this.log.getFilePath());
		Program.LOGGER.log(Level.INFO, "Nr. of revisions: " + this.revisedText.getRevision());
		Program.LOGGER.log(Level.INFO,
				"Nr. of active Symbols / Chars including spaces / Chars excluding spaces: "
						+ (this.revisedText.getNumberOfActiveSymbols()) + "/" + charsInclSpaces + "/" + charsExclSpaces
						+ " characters long.");
	}

	public void flushToFile() {
		this.output.setMarkup(this.revisedText.toString());
		this.output.setMeta(this.log.getMeta());
		this.output.setSession(this.log.getSession());
		File f1 = new File(log.getFilePath());
		File f = new File("resources/OUTPUT/" + f1.getName() + "_SN.xml");

		try {
			f.createNewFile();
			JAXBContext jaxbContext = JAXBContext.newInstance(SNotation.class);
			final Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.marshal(this.output, f);

			Program.LOGGER.log(Level.INFO, "Created S-Notation: " + f.getPath());
		} catch (JAXBException | IOException e) {
			// TODO Auto-generated catch block
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
