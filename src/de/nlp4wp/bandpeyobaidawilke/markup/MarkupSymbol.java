package de.nlp4wp.bandpeyobaidawilke.markup;

public abstract class MarkupSymbol extends Symbol {

	public MarkupSymbol(int position, String character) {
		super(position, character);
		this.setActive(false);
	}

}
