package de.nlp4wp.bandpeyobaidawilke;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class FileSearch {
	private final List<String> result = new ArrayList<>();
	private String fileNameToSearch;

	public String getFileNameToSearch() {
		return this.fileNameToSearch;
	}

	public List<String> getResults() {
		return this.result;
	}

	public void searchDirectory(final File directory, final String fileNameToSearch) {
		this.setFileNameToSearch(fileNameToSearch);

		if (directory.isDirectory()) {
			this.search(directory);
		} else {
			Program.LOGGER.log(Level.INFO, "\"" + directory.getPath() + "\" is not a directory!");
		}
		Program.LOGGER.log(Level.INFO,
				"Found " + this.result.size() + " files with file ending \"" + this.getFileNameToSearch() + "\"");
	}

	public void setFileNameToSearch(final String fileNameToSearch) {
		this.fileNameToSearch = fileNameToSearch;
	}

	private void search(final File file) {
		if (file.getName().equals("OUTPUT")) {
			return;
		}
		if (file.isDirectory()) {
			Program.LOGGER.log(Level.INFO, "Searching directory: \"" + file.getPath() + "\"");

			// do you have permission to read this directory?
			if (file.canRead()) {
				for (final File temp : file.listFiles()) {
					if (temp.isDirectory()) {
						this.search(temp);
					} else {
						if (temp.getName().toLowerCase().endsWith(this.getFileNameToSearch())) {
							this.result.add(temp.getPath().toString());
						}

					}
				}
			} else {
				Program.LOGGER.log(Level.INFO, file.getAbsoluteFile() + " - Permission Denied");
			}
		}
	}
}
