package de.nlp4wp.bandpeyobaidawilke.markup;

public class Insertion extends Revision {

	public Insertion(final int sequentialNumber) {
		super(sequentialNumber);
		this.getRevisionSymbols().add(0, new LeftBrace(sequentialNumber));
		this.getRevisionSymbols().add(this.getRevisionSymbols().size(), new RightBrace(sequentialNumber));
	}

}
