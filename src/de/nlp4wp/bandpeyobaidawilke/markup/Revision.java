package de.nlp4wp.bandpeyobaidawilke.markup;

import java.util.ArrayList;

public abstract class Revision {

	private final SymbolContainer revisionSymbols;
	private int sequentialNumber = 1;

	public Revision(final int sequentialNumber) {
		this.setSequentialNumber(sequentialNumber);
		this.revisionSymbols = new SymbolContainer();
	}

	public int getFirstPosition() {
		if (this.getRevisionSymbols().size() == 0) {
			return -1;
		}
		return this.getRevisionSymbols().get(0).getPosition();
	}

	public int getLastPosition() {
		if (this.getRevisionSymbols().size() == 0) {
			return -1;
		}
		return this.getRevisionSymbols().get(this.getRevisionSymbols().size() - 1).getPosition();
	}

	public SymbolContainer getRevisionSymbols() {
		return this.revisionSymbols;
	}

	public int getSequentialNumber() {
		return this.sequentialNumber;
	}

	private void setSequentialNumber(final int sequentialNumber) {
		this.sequentialNumber = sequentialNumber;
	}
}
