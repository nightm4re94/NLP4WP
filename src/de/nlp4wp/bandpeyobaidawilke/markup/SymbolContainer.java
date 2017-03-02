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
	public void add(final int arg0, final Symbol arg1) {
		int tmp = -1;
		for (final Symbol symbol : this) {
			tmp += symbol.getPositionCount();
			if (tmp == arg0) {
				super.add(super.indexOf(symbol), arg1);
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
				return super.get(super.indexOf(symbol));
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

}
