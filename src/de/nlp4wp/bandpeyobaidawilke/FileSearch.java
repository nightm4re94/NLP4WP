package de.nlp4wp.bandpeyobaidawilke;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
			System.out.println(directory.getPath() + " is not a directory!");
		}

	}

	public void setFileNameToSearch(final String fileNameToSearch) {
		this.fileNameToSearch = fileNameToSearch;
	}

	private void search(final File file) {
		if (file.isDirectory()) {
			System.out.println("Searching directory " + file.getPath());

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
				System.out.println(file.getAbsoluteFile() + "Permission Denied");
			}
		}
	}
}
