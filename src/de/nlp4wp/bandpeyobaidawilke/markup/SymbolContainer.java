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

		final Symbol previous = this.getPrevious(this.getSingle(firstPos));
		if (this.getRevision() == 0 || !(previous instanceof RightBracket)) {
			this.nextRevision();
			this.insertSingle(firstPos - 1, new LeftBracket(this.getRevision()));
			this.insertSingle(lastPos, new RightBracket(this.getRevision()));
		}

		int tmp = -1;
		for (final Symbol symbol : this) {
			tmp += symbol.getPositionCount();
			if (tmp >= firstPos && tmp <= lastPos) {
				this.get(tmp).setActive(false);
				this.setLastEditPosition(tmp);
			}
		}
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
		if (position < 0) {
			return;
		}

		final Symbol previous = this.getPrevious(this.getSingle(position));
		if (previous instanceof RightBrace && previous.getRevisionNumber() != this.getRevision()) {
			this.nextRevision();
			this.add(super.indexOf(this.getSingle(position)), new LeftBrace(this.getRevision()));
			this.add(super.indexOf(this.getSingle(position)) + symbols.size(), new RightBrace(this.getRevision()));
		}

		int tmp = -1;
		for (final Symbol sym : this) {
			tmp += sym.getPositionCount();
			if (tmp == position) {
				this.addAll(super.indexOf(sym), symbols);
				this.setLastEditPosition(position);
				return;
			}
		}

	}

	public void insertSingle(final int position, final Symbol symbol) {
		final SymbolContainer tmp = new SymbolContainer();
		tmp.add(symbol);
		this.insertMultiple(position, tmp);
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
