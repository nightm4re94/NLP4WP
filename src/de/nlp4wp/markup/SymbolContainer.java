package de.nlp4wp.markup;

import java.util.concurrent.CopyOnWriteArrayList;

public class SymbolContainer extends CopyOnWriteArrayList<Symbol> {
	/**
	 *
	 */
	private static final long serialVersionUID = 8691042651739478671L;
	private int currentRevision = 0;
	private int highestRevision = 0;

	private int lastDeletePosition = -1;
	private int lastInsertPosition = -1;
	private int lastEditPosition = -1;

	private int lastDeleteIndex = -1;
	// the symbols' index needs to depend on the number of active symbols, not
	// the position in the list.

	public void deleteMultiple(final int firstPos, final int lastPos) {
		for (int i = firstPos; i < lastPos; i++) {
			this.deleteSingle(i);
		}
	}

	public void deleteSingle(final int position) {
		if (position < 0 || this.getSingle(position) == null) {
			return;
		}
		final Symbol s = this.getSingle(position);
		int tmp = this.indexOf(s);
		this.remove(tmp);

		if (tmp > 0) {
			final Symbol previous = this.get(tmp - 1);
			if (previous instanceof RightBracket && previous.getRevisionNumber() == this.getRevision()) {
				tmp--;
			}
		}
		if (position != this.getLastDeletePosition()) {
			this.nextDeleteRevision();
			this.add(tmp, new RightBracket(this.getRevision()));
			this.add(tmp, s);
			this.add(tmp, new LeftBracket(this.getRevision()));
		} else {
			this.add(tmp, s);
		}

		s.setActive(false);
		s.setRevisionNumber(this.getRevision());
		this.setLastDeletePosition(position);
		this.lastDeleteIndex = tmp;
	}

	public void firstRevision() {
		this.currentRevision = 0;
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
				return symbol;
			}
		}
		return null;
	}

	public void insertMultiple(final int position, final SymbolContainer symbols) {
		for (final Symbol s : symbols) {
			this.insertSingle(position + symbols.indexOf(s), s);

		}

	}

	public void insertSingle(final int position, final Symbol symbol) {
		if (position < 0) {
			return;
		}

		final int tmp = this.indexOf(this.getSingle(position));
		int positionToAdd = tmp == -1 ? this.size() : tmp;
		if (positionToAdd > 0) {
			final Symbol previous = this.get(positionToAdd - 1);
			if (previous instanceof RightBrace && previous.getRevisionNumber() == this.getRevision()) {
				positionToAdd--;
			}
		}
		// not inserting at the end and not appending to an existing revision
		if (position != this.getLastInsertPosition() + 1 && position != this.getNumberOfActiveSymbols()) {
			this.nextInsertRevision();
			this.add(positionToAdd, new RightBrace(this.getRevision()));
			symbol.setRevisionNumber(this.getRevision());
			this.add(positionToAdd, symbol);
			this.add(positionToAdd, new LeftBrace(this.getRevision()));
		}
		// not inserting at the end but appending to an existing revision brace
		else if (position != this.getNumberOfActiveSymbols()) {
			symbol.setRevisionNumber(this.getRevision());
			this.add(positionToAdd, symbol);
		}
		// inserting at the end
		else {
			this.firstRevision();
			symbol.setRevisionNumber(this.getRevision());
			this.add(positionToAdd, symbol);
		}
		this.setLastInsertPosition(position);
	}

	public void nextDeleteRevision() {
		this.highestRevision++;
		this.currentRevision = this.highestRevision;
		int tmp = this.lastDeleteIndex;
		tmp = tmp == -1 ? this.size() : tmp;
		while (tmp < this.size() && !this.get(tmp).isActive()) {
			tmp++;
		}
		final Break b = new Break(this.getRevision());
		this.add(tmp, b);
	}

	public void nextInsertRevision() {
		this.highestRevision++;
		this.currentRevision = this.highestRevision;
		final int tmp = this.indexOf(this.getSingle(this.getLastEditPosition() + 1));
		final int positionToAdd = tmp == -1 ? this.size() : tmp;
		final Break b = new Break(this.getRevision());
		this.add(positionToAdd, b);
	}

	public void replaceMultiple(final int firstPos, final int lastPos, final SymbolContainer c) {
		this.deleteMultiple(firstPos, lastPos);
		this.insertMultiple(firstPos, c);
	}

	public void replaceSingle(final int position, final Symbol s) {
		this.deleteSingle(position);
		this.insertSingle(position, s);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		for (final Symbol c : this) {
			builder.append(c.getCharacter());
		}
		return builder.toString();
	}

	private void setLastDeletePosition(final int editPosition) {
		this.setLastEditPosition(editPosition);
		this.lastDeletePosition = editPosition;
	}

	private void setLastEditPosition(final int editPosition) {
		this.lastEditPosition = editPosition;
	}

	private void setLastInsertPosition(final int editPosition) {
		this.setLastEditPosition(editPosition);
		this.lastInsertPosition = editPosition;
	}
}
