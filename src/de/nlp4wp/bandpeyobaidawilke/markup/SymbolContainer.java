package de.nlp4wp.bandpeyobaidawilke.markup;

import java.util.concurrent.CopyOnWriteArrayList;

public class SymbolContainer extends CopyOnWriteArrayList<Symbol> {
	/**
	 *
	 */
	private static final long serialVersionUID = 8691042651739478671L;
	private int revision = 0;

	private int lastEditPosition = 0;
	// the symbols' index needs to depend on the number of active symbols, not
	// the position in the list.

	public void deleteMultiple(final int firstPos, final int lastPos) {
		if (firstPos < 0 || lastPos < 0) {
			return;
		}
		int leftBracketPosition = super.indexOf(this.get(firstPos));
		Symbol previous = this.getPrevious(this.getSingle(firstPos));
		Symbol next = this.getNext(this.getSingle(lastPos));
		if (previous instanceof RightBracket && previous.getRevisionNumber() == this.getRevision()
				|| next instanceof LeftBracket && next.getRevisionNumber() == this.getRevision()) {
		} else {
			this.nextRevision();
		}
		int symbolsToRemove = lastPos - firstPos;
		int currentPosition = super.indexOf(this.getSingle(firstPos));
		Symbol currentSymbol = this.get(currentPosition);

		while (symbolsToRemove > 0
				|| (currentSymbol instanceof Break && currentSymbol.getRevisionNumber() < this.getRevision())
				|| (currentSymbol instanceof LeftBracket || currentSymbol instanceof RightBracket)) {
			if (currentSymbol.isActive()) {
				currentSymbol.setActive(false);
				symbolsToRemove--;
			}
			this.setLastEditPosition(super.indexOf(currentSymbol));
			currentPosition++;
			currentSymbol = this.get(currentPosition);
		}
		this.add(currentPosition + 1, new RightBracket(this.getRevision()));
		this.add(leftBracketPosition, new LeftBracket(this.getRevision()));
	}

	public void deleteSingle(final int position) {
		this.deleteMultiple(position, position);
	}

	public String getActiveChars() {
		final StringBuilder builder = new StringBuilder();
		for (final Symbol c : this) {
			if (c.isActive()) {
				builder.append(c.getCharacter());
			}
		}
		return builder.toString();

	}

	public Break getBreak(final int sequentialNumber) {
		for (final Symbol b : this) {
			if (b instanceof Break && ((Break) b).getRevisionNumber() == sequentialNumber) {
				return (Break) b;
			}
		}
		return null;
	}

	public int getLastEditPosition() {
		return this.lastEditPosition;
	}

	public SymbolContainer getMultiple(final int firstPos, final int lastPos) {
		int tmp = -1;
		final SymbolContainer symbols = new SymbolContainer();
		for (final Symbol symbol : this) {
			tmp += symbol.getPositionCount();
			if (tmp >= firstPos && tmp <= lastPos) {
				symbols.add(symbol);
			}
		}
		return symbols;
	}

	public Symbol getNext(final Symbol symbol) {
		if (this.indexOf(symbol) == this.size() - 1) {
			return null;
		}
		final Symbol next = this.get(this.indexOf(symbol) + 1);
		return next;
	}

	public int getNumberOfActiveSymbols() {
		int tmp = -1;
		for (final Symbol symbol : this) {
			tmp += symbol.getPositionCount();
		}
		return tmp + 1;
	}

	public Symbol getPrevious(final Symbol symbol) {
		if (this.indexOf(symbol) <= 0) {
			return null;
		}
		final Symbol previous = this.get(this.indexOf(symbol) - 1);
		return previous;
	}

	public int getRevision() {
		return this.revision;
	}

	public Symbol getSingle(final int position) {
		int tmp = -1;
		for (final Symbol symbol : this) {
			tmp += symbol.getPositionCount();
			if (tmp == position) {
				return this.get(tmp);
			}
		}
		return null;
	}

	public void insertMultiple(final int position, final SymbolContainer symbols) {
		if (this.size() == 0) {
			super.addAll(symbols);
			return;
		}
		super.addAll(super.indexOf(this.getSingle(position)), symbols);
	}

	public void insertSingle(final int position, final Symbol symbol) {
		if (this.size() == 0) {
			super.add(symbol);
			return;
		}
		super.add(super.indexOf(this.getSingle(position)), symbol);
	}

	public void nextRevision() {
		this.revision++;
		Break b = new Break(getRevision());
		this.insertSingle(this.getLastEditPosition(), b);
	}

	public void replace(final int firstPos, final int lastPos, final SymbolContainer c) {
		this.deleteMultiple(firstPos, lastPos);
		this.insertMultiple(firstPos, c);
	}

	public void setActive(final int position, final boolean active) {
		this.getSingle(position).setActive(active);
	}

	public void setActive(final int firstPos, final int lastPos, final boolean active) {
		int tmp = -1;
		for (final Symbol symbol : this) {
			tmp += symbol.getPositionCount();
			if (tmp >= firstPos && tmp <= lastPos) {
				symbol.setActive(active);
			}
		}
	}

	public void setLastEditPosition(final int lastEditPosition) {
		this.lastEditPosition = lastEditPosition;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		for (final Symbol c : this) {
			builder.append(c.getCharacter());
		}
		return builder.toString();
	}
}
