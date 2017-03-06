package de.nlp4wp.bandpeyobaidawilke.markup;

public class Deletion extends Revision {

	public Deletion(final int sequentialNumber) {
		super(sequentialNumber);
		this.getRevisionSymbols().add(0, new LeftBracket(sequentialNumber));
		this.getRevisionSymbols().add(this.getRevisionSymbols().size(), new RightBracket(sequentialNumber));
	}
}
