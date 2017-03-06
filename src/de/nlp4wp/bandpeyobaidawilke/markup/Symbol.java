package de.nlp4wp.bandpeyobaidawilke.markup;

public class Symbol {
	private int positionCount;
	private int position;
	private int revisionNumber;
	private boolean active;
	private String character;

	public Symbol(final int revisionNumber, final String character) {
		this.setRevisionNumber(revisionNumber);
		this.setActive(true);
		this.setCharacter(character);
	}

	public String getCharacter() {
		return this.character;
	}

	public int getRevisionNumber() {
		return this.revisionNumber;
	}

	public void setRevisionNumber(final int revisionNumber) {
		this.revisionNumber = revisionNumber;
	}

	public int getPosition() {
		return this.position;
	}

	public int getPositionCount() {
		return this.positionCount;
	}

	public boolean isActive() {
		return this.active;
	}

	public void modifyPosition(final int amount) {
		this.position += amount;
	}

	public void setActive(final boolean active) {
		this.active = active;
		this.positionCount = this.active ? 1 : 0;
	}

	public void setCharacter(final String character) {
		this.character = character;
	}

	public void setPosition(final int position) {
		this.position = position;
	}

}
