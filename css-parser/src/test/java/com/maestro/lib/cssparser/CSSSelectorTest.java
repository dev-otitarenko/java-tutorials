package com.maestro.lib.cssparser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public final class CSSSelectorTest {
	@Test
	public void testHashCode() {
		CSSSelector selector = new CSSSelector("div");

		assertEquals(selector.toString().hashCode(), selector.hashCode());
	}

	@Test
	public void testEqualsNull() {
		CSSSelector selector1 = new CSSSelector("div1");
		CSSSelector selector2 = null;

		assertNotEquals(selector1, selector2);
	}

	@Test
	public void testEquals() {
		CSSSelector selector1a = new CSSSelector("div1");
		CSSSelector selector1b = new CSSSelector("div1");
		CSSSelector selector2 = new CSSSelector("div2");

		assertNotEquals(selector1a, selector2);
		assertEquals(selector1a, selector1b);
	}
}
