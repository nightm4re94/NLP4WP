package de.nlp4wp.markup;

public class RightBrace extends MarkupSymbol {
	public RightBrace(final int revisionNumber) {
		super(revisionNumber, "}" + "(" + revisionNumber + ")");
	}
}
