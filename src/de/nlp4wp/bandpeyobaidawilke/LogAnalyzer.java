package de.nlp4wp.bandpeyobaidawilke;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
          if (lastRevision == null) {
            lastRevision = new Deletion(this.log.getRevisions().size());

          } else if (position != lastRevision.getFirstPosition()) {
            this.log.addRevision(lastRevision);
            lastRevision = new Deletion(this.log.getRevisions().size());

          }

          if (position != 0) {
          }

        } else if (key.equals("VK_DELETE")) {

        }

        else {
          this.revisedText.add(new Symbol(position, value));

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
        for (int i = 0; i < end - start; i++) {
          if (i < newText.length()) {
            Character c = newText.charAt(i);
            int charInd = start + i;
            if (charInd < revisedText.numberOfActiveSymbols()) {
              revisedText.set(charInd, new Symbol(charInd, c.toString()));
            } else {
              revisedText.add(charInd, new Symbol(charInd, c.toString()));

            }
          } else {
            revisedText.remove(start + newText.length());
          }
        }
      }
      // insertion events (Zwischenablage o.Ä.)
      else if (event.getType().equals("insert") && position >= 0 && (before != null || after != null)) {
        // System.out.println(
        // "insertion: position: " + position + " text before: " +
        // before + " text after: " + after);
        final Insertion newRevision = new Insertion(this.log.getRevisions().size());
        for (int i = 0; i < after.length(); i++) {
          final Character c = after.toCharArray()[i];
          this.revisedText.add(new Symbol(position + i, c.toString()));

        }
        lastRevision = newRevision;
        this.log.addRevision(lastRevision);
      }
      if (position >= 0) {
        lastTextPosition = position;
      } else if (end >= 0) {
        lastTextPosition = end;
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
    System.out.println("Nr. of revisions: " + log.getRevisions().size());
    System.out.println("The total text is " + (maxTextIndex + 1) + " characters long.");
    final StringBuilder builder = new StringBuilder();
    for (final Symbol c : this.revisedText) {
      builder.append(c.getCharacter());
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
