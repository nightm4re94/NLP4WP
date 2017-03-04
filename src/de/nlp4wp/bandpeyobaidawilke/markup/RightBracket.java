package de.nlp4wp.bandpeyobaidawilke.markup;

public class RightBracket extends MarkupSymbol {
	private int breakIndex;

	public RightBracket(final int position, final int breakIndex) {
		super(position, "]" + breakIndex);
		this.setBreakIndex(breakIndex);
	}

	public int getBreakIndex() {
		return this.breakIndex;
	}

	private void setBreakIndex(final int breakIndex) {
		this.breakIndex = breakIndex;
	}
}
