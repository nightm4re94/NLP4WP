package de.nlp4wp.markup;

public abstract class MarkupSymbol extends Symbol {

	public MarkupSymbol(final int revisionNumber, final String character) {
		super(revisionNumber, character);
		this.setActive(false);
	}

}
