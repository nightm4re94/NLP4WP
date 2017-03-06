package de.nlp4wp.bandpeyobaidawilke.markup;

public abstract class MarkupSymbol extends Symbol {

	public MarkupSymbol(final int position, final String character) {
		super(position, character);
		this.setActive(false);
	}

}
