package de.nlp4wp.test;

import org.junit.Assert;
import org.junit.Test;

import de.nlp4wp.markup.Symbol;
import de.nlp4wp.markup.SymbolContainer;

public class LogAnalyzationTest {
	@Test
	public void testBLA() {
		final SymbolContainer s = new SymbolContainer();
		s.insertSingle(0, new Symbol("a"));
		s.insertSingle(1, new Symbol("b"));
		s.insertSingle(2, new Symbol("c"));
		Assert.assertEquals("abc", s.toString());
		s.insertSingle(0, new Symbol("s"));
		Assert.assertEquals("{s}(1)abc|(1)", s.toString());
		s.insertSingle(1, new Symbol("d"));
		Assert.assertEquals("{sd}(1)abc|(1)", s.toString());
		s.insertSingle(5, new Symbol("y"));
		Assert.assertEquals("{sd}(1)abc|(1)y", s.toString());
		s.insertSingle(3, new Symbol("e"));
		Assert.assertEquals("{sd}(1)a{e}(2)bc|(1)y|(2)", s.toString());
		s.insertSingle(1, new Symbol("o"));
		Assert.assertEquals("{s{o}(3)d}(1)a{e}(2)|(3)bc|(1)y|(2)", s.toString());
		s.insertSingle(8, new Symbol("B"));
		Assert.assertEquals("{s{o}(3)d}(1)a{e}(2)|(3)bc|(1)y|(2)B", s.toString());
	}

	@Test
	public void testBLUB() {
		final SymbolContainer s = new SymbolContainer();
		s.insertSingle(0, new Symbol("a"));
		s.insertSingle(1, new Symbol("b"));
		s.insertSingle(2, new Symbol("c"));
		s.insertSingle(3, new Symbol("d"));
		Assert.assertEquals("abcd", s.toString());
		s.deleteSingle(1);
		Assert.assertEquals("a[b](1)cd|(1)", s.toString());
		s.deleteSingle(1);
		Assert.assertEquals("a[bc](1)d|(1)", s.toString());
		s.deleteSingle(0);
		Assert.assertEquals("[a](2)[bc](1)|(2)d|(1)", s.toString());
	}

	@Test
	public void testHURZ() {
		final SymbolContainer s = new SymbolContainer();
		s.insertSingle(0, new Symbol("a"));
		s.insertSingle(1, new Symbol("b"));
		s.insertSingle(2, new Symbol("c"));
		s.insertSingle(3, new Symbol("d"));
		Assert.assertEquals("abcd", s.toString());
		s.insertSingle(1, new Symbol("p"));
		Assert.assertEquals("a{p}(1)bcd|(1)", s.toString());
		// s.deleteMultiple(0, 1);
		// Assert.assertEquals("[a{p}(1)](2)|(2)bcd|(1)", s.toString());

	}
}
