package de.nlp4wp.bandpeyobaidawilke.markup;

public class Break extends MarkupSymbol {

	public Break(final int revisionNumber) {
		super(revisionNumber, "|" + "(" + revisionNumber + ")");
	}

}
