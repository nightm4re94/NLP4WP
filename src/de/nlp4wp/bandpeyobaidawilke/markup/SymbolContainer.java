package de.nlp4wp.bandpeyobaidawilke.markup;

import java.util.ArrayList;

public class SymbolContainer extends ArrayList<Symbol> {

	// the symbols' index needs to depend on the number of active symbols, not
	// the position in the list.

	/**
	 *
	 */
	private static final long serialVersionUID = 8691042651739478671L;

	@Override
	public void add(final int index, final Symbol symbol) {
		if (this.size() == 0) {
			super.add(symbol);
		}

		int tmp = -1;
		for (final Symbol sym : this) {
			tmp += sym.getPositionCount();
			if (tmp == index) {
				super.add(super.indexOf(sym), symbol);
				return;
			}
		}

	}

	@Override
	public Symbol get(final int arg0) {
		int tmp = -1;
		for (final Symbol symbol : this) {
			tmp += symbol.getPositionCount();
			if (tmp == arg0) {
				return super.get(tmp);
			}
		}
		return null;
	}

	public int numberOfActiveSymbols() {
		int tmp = -1;
		for (final Symbol symbol : this) {
			tmp += symbol.getPositionCount();
		}
		return tmp + 1;
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
