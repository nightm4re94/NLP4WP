package de.nlp4wp.markup;

public class Break extends MarkupSymbol {

	public Break(final int revisionNumber) {
		super(revisionNumber, "|" + "(" + revisionNumber + ")");
	}

}
