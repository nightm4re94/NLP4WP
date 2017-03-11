package de.nlp4wp.bandpeyobaidawilke.markup;

import java.util.concurrent.CopyOnWriteArrayList;

public class SymbolContainer extends CopyOnWriteArrayList<Symbol> {
	/**
	 *
	 */
	private static final long serialVersionUID = 8691042651739478671L;
	private int currentRevision = 0;

	private int lastDeletePosition = 0;
	private int lastInsertPosition = 0;
	private int lastEditPosition = 0;
	// the symbols' index needs to depend on the number of active symbols, not
	// the position in the list.

	public void deleteMultiple(final int firstPos, final int lastPos) {
		for (int i = firstPos; i <= lastPos; i++) {
			this.deleteSingle(i);
		}
	}

	public void deleteSingle(final int position) {
		if (position < 0 || this.getSingle(position) == null) {
			return;
		}
		// if (!this.isNeighbor(position, this.lastDeletePosition)) {
		// this.nextRevision();
		// this.add(position + 1, new RightBracket(this.getRevision()));
		// this.add(position, new LeftBracket(this.getRevision()));
		// }
		Symbol s = this.getSingle(position);
		s.setActive(false);
		this.setLastDeletePosition(super.indexOf(s));
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

	public int getLastDeletePosition() {
		return this.lastDeletePosition;
	}

	public int getLastEditPosition() {
		return this.lastEditPosition;
	}

	public int getLastInsertPosition() {
		return this.lastInsertPosition;
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
		return this.currentRevision;
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
		for (Symbol s : symbols) {
			this.insertSingle(position + symbols.indexOf(s), s);

		}

	}

	public void insertSingle(final int position, final Symbol symbol) {
		if (position < 0) {
			return;
		}
		int positionToAdd = 0;
		if (this.getSingle(position) == null || position == this.getNumberOfActiveSymbols()) {
			positionToAdd = this.getNumberOfActiveSymbols();
			this.firstRevision();

		} else {
			if (!(symbol instanceof MarkupSymbol || this.isNeighbor(position, lastInsertPosition))) {
				this.nextRevision();
			}
			positionToAdd = this.indexOf(this.getSingle(position)) + 1;
		}
		super.add(positionToAdd, symbol);
		this.setLastInsertPosition(positionToAdd);
	}

	public void nextRevision() {
		this.currentRevision++;
		// Break b = new Break(this.getRevision());
		// this.insertSingle(this.getLastEditPosition(), b);
	}

	public void firstRevision() {
		this.currentRevision = 0;
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

	private void setLastEditPosition(final int editPosition) {
		this.lastEditPosition = editPosition;
	}

	private void setLastDeletePosition(final int editPosition) {
		this.setLastEditPosition(editPosition);
		this.lastDeletePosition = editPosition;
	}

	private void setLastInsertPosition(final int editPosition) {
		this.setLastEditPosition(editPosition);
		this.lastInsertPosition = editPosition;
	}

	private boolean isNeighbor(final int position, final int positionToCompare) {
		return (position == positionToCompare + 1 || position == positionToCompare - 1);
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
