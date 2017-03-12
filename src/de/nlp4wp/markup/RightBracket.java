package de.nlp4wp.markup;

public class RightBracket extends MarkupSymbol {

	public RightBracket(final int revisionNumber) {
		super(revisionNumber, "]" + "(" + revisionNumber + ")");
	}
}
