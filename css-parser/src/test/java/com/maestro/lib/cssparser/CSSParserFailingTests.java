package com.maestro.lib.cssparser;

import com.maestro.lib.cssparser.IncorrectFormatException.ErrorCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public final class CSSParserFailingTests {
	@Test
	public void testCommaFirstAsSelector() throws Exception {
		try {
			CSSParser.parse("alpha { width: 100px; } , beta { height: 200px; } ");
			fail("Exception not thrown");
		} catch (IncorrectFormatException e) {
			assertEquals(ErrorCode.FOUND_COLON_WHEN_READING_SELECTOR_NAME, e.getErrorCode());
		}
	}

	@Test
	public void testValueShouldEndWithSemiColon() throws Exception {
		try {
			CSSParser.parse("alpha { width: 100px }");
			fail("Exception not thrown");
		} catch (IncorrectFormatException e) {
			assertEquals(ErrorCode.FOUND_END_BRACKET_BEFORE_SEMICOLON, e.getErrorCode());
		}
	}

	@Test
	public void testMissingColon() throws Exception {
		try {
			CSSParser.parse("alpha { color red; }");
			fail("Exception not thrown");
		} catch (IncorrectFormatException e) {
			assertEquals(ErrorCode.FOUND_SEMICOLON_WHEN_READING_PROPERTY_NAME, e.getErrorCode());
		}
	}

	@Test
	public void testMissingSemiColon() throws Exception {
		try {
			CSSParser.parse("alpha { border: 1px solid green background-color:white; left: 0px; }");
			fail("Exception not thrown");
		} catch (IncorrectFormatException e) {
			assertEquals(ErrorCode.FOUND_COLON_WHILE_READING_VALUE, e.getErrorCode());
		}
	}
}
