package de.nlp4wp.bandpeyobaidawilke;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.nlp4wp.bandpeyobaidawilke.xmltypes.Log;

public class Program {
	private static Program program;
	private static final Logger LOGGER = Logger.getLogger(LogAnalyzer.class.getName());

	/**
	 * Create a static method to get instance.
	 */
	public static Program instance() {
		if (program == null) {
			program = new Program();
		}
		return program;
	}

	public static void main(final String a[]) {
		Program.instance().searchFiles();
		Program.instance().createLogs();
		for (final Log log : Program.instance().logs) {
			LogAnalyzer analyzer = new LogAnalyzer(log);
			analyzer.analyzeLog();
		}

	}

	private List<Log> logs;

	private List<String> filePaths;

	private Program() {
		Program.this.filePaths = new ArrayList<>();
		Program.this.logs = new ArrayList<>();
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
}
