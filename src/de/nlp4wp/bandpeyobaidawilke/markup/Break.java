package de.nlp4wp.bandpeyobaidawilke.markup;

public class Break extends Symbol {
	private int breakIndex;

	public Break(final int position, final int breakIndex) {
		super(position, "|" + breakIndex);
		this.setBreakIndex(breakIndex);
		this.setActive(false);
	}

	public int getBreakIndex() {
		return this.breakIndex;
	}

	private void setBreakIndex(final int breakIndex) {
		this.breakIndex = breakIndex;
	}

}
