package de.nlp4wp.bandpeyobaidawilke;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import de.nlp4wp.bandpeyobaidawilke.xmltypes.Log;

public class Program {
  private static Program program;
  public static Logger LOGGER = Logger.getGlobal();

  /**
   * Create a static method to get instance.
   */
  public static Program instance() {
    if (program == null) {
      program = new Program();
    }
    return program;
  }

  private List<Log> logs;

  private List<String> filePaths;

  private Program() {
    Program.this.filePaths = new ArrayList<>();
    Program.this.logs = new ArrayList<>();
    ConsoleHandler handler = new ConsoleHandler();
    handler.setFormatter(new de.nlp4wp.bandpeyobaidawilke.OneLineFormatter());
    handler.setLevel(Level.ALL);
    FileHandler fh;
    try {
      fh = new FileHandler("log.xml");
      LOGGER.addHandler(fh);
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    LOGGER.setUseParentHandlers(false);
    LOGGER.addHandler(handler);
  }

  public static void main(final String a[]) {
    Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
      public void uncaughtException(Thread t, Throwable e) {
        logException(e);
      }
    });

    Program.instance().searchFiles();
    Program.instance().createLogs();
    for (final Log log : Program.instance().logs) {
      final LogAnalyzer analyzer = new LogAnalyzer(log);
      analyzer.analyzeLog();
    }
  }

  private void createLogs() {
    final IDFXReader reader = new IDFXReader();
    for (final String path : Program.instance().filePaths) {
      LOGGER.log(Level.FINE, "Trying to unmarshal file: " + path);
      final Log log = reader.getMarshalledLog(path);
      Program.instance().logs.add(log);
    }

  }

  private void searchFiles() {
    final FileSearch fileSearch = new FileSearch();
    fileSearch.searchDirectory(new File("resources"), ".idfx");

    final int count = fileSearch.getResults().size();
    if (count == 0) {
      LOGGER.log(Level.FINE, "No matching files for fileName " + fileSearch.getFileNameToSearch() + " found!");
      System.exit(0);
    } else {
      LOGGER.log(Level.FINE, "Found " + count + " matching file(s)!");
      for (final String matched : fileSearch.getResults()) {
        LOGGER.log(Level.FINE, "Found file: " + matched);
        Program.instance().filePaths.add(matched);
      }
    }

  }

  public static void logException(Throwable e) {
    StringWriter sw = new StringWriter();
    e.printStackTrace(new PrintWriter(sw));
    String stacktrace = sw.toString();
    LOGGER.log(Level.SEVERE, stacktrace, e);
  }
}
