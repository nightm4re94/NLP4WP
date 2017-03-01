package de.nlp4wp.bandpeyobaidawilke.markup;

public class Revision {
	public enum RevisionType {
		INSERTION, DELETION;
	}

	private String revisionText;
	private int revisionIndex = -1, breakIndex = -1, sequentialNumber = 1;

	private RevisionType type;

	public Revision(final int sequentialNumber) {
		this.setSequentialNumber(sequentialNumber);
	}

	public void modifyBreakIndex(int amount) {
		this.setBreakIndex(this.getBreakIndex() + amount);
	}

	public void modifyRevisionIndex(int amount) {
		this.setRevisionIndex(this.getRevisionIndex() + amount);
	}

	public int getBreakIndex() {
		return this.breakIndex;
	}

	public String getRevisionText() {
		return this.revisionText;
	}

	public int getRevisionIndex() {
		return this.revisionIndex;
	}

	public int getSequentialNumber() {
		return this.sequentialNumber;
	}

	public RevisionType getType() {
		return this.type;
	}

	public void setBreakIndex(final int breakIndex) {
		this.breakIndex = breakIndex;
	}

	public void setRevisionIndex(final int firstIndex) {
		this.revisionIndex = firstIndex;
	}

	public void setRevisionText(final String deletedText) {
		this.revisionText = deletedText;
	}

	public void setType(final RevisionType type) {
		this.type = type;
	}

	private void setSequentialNumber(final int sequentialNumber) {
		this.sequentialNumber = sequentialNumber;
	}
}
