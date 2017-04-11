package de.nlp4wp.markup;

public class Symbol {
	private int positionCount;
	private int revisionNumber;
	private boolean active;
	private String character;

	public Symbol(final int revisionNumber, final String character) {
		this.setRevisionNumber(revisionNumber);
		this.setActive(true);
		this.setCharacter(character);
	}

	public Symbol(final String character) {
		this.setActive(true);
		this.setCharacter(character);
	}

	public String getCharacter() {
		return this.character;
	}

	public int getPositionCount() {
		return this.positionCount;
	}

	public int getRevisionNumber() {
		return this.revisionNumber;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(final boolean active) {
		this.active = active;
		this.positionCount = this.active ? 1 : 0;
	}

	public void setCharacter(final String character) {
		this.character = character;
	}

	public void setRevisionNumber(final int revisionNumber) {
		this.revisionNumber = revisionNumber;
	}

}
