package com.maestro.lib.cssparser;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class CSSParserTest {
	@Test
	public void testNull() throws Exception {
		CSSParser.parse(null);
	}

	@Test
	public void testEmpty() throws Exception {
		CSSParser.parse("   ");
		CSSParser.parse(" \n  ");
		CSSParser.parse(" \n \t ");
		CSSParser.parse(" \n \t \r ");
	}

	@Test
	public void testComments() throws Exception {
		CSSParser.parse("  /* this should be ok */  ");
		CSSParser.parse(" /** JavaDoc comment **/  ");
		CSSParser.parse(" /** comment with \n new line **/  ");
		CSSParser.parse(" /* double **/ /** comment */ ");
	}

	@Test
	public void testBasicSingle() throws Exception {
		List<CSSRule> rules = CSSParser.parse("div { width: 100px; }");

		assertEquals(1, rules.size());

		CSSRule rule = rules.get(0);

		assertEquals("div", rule.getSelectors().get(0).toString());
		assertEquals("width", rule.getPropertyValues().get(0).getProperty());
		assertEquals("100px", rule.getPropertyValues().get(0).getValue());
	}

	@Test
	public void testCommentInsideRule() throws Exception {
		List<CSSRule> rules = CSSParser.parse("table /* and a comment here */ td { width: 100px; /* should be ignored */ text-decoration: underlined; }");

		assertEquals(1, rules.size());

		CSSRule rule = rules.get(0);
		assertEquals("table  td", rule.getSelectors().get(0).toString());
		assertEquals("width", rule.getPropertyValues().get(0).getProperty());
		assertEquals("100px", rule.getPropertyValues().get(0).getValue());
		assertEquals("text-decoration", rule.getPropertyValues().get(1).getProperty());
		assertEquals("underlined", rule.getPropertyValues().get(1).getValue());
	}

	@Test
	public void testBase64() throws Exception {
		List<CSSRule> rules = CSSParser.parse("image { background-image:url(data:image/gif;base64,ABC123/ABC123=ABC123);}");

		assertEquals(1, rules.size());

		CSSRule rule = rules.get(0);
		assertEquals("image", rule.getSelectors().get(0).toString());

		assertEquals("background-image", rule.getPropertyValues().get(0).getProperty());
		assertEquals("url(data:image/gif;base64,ABC123/ABC123=ABC123)", rule.getPropertyValues().get(0).getValue());
	}

	@Test
	public void testEmptPropertyValues() throws Exception {
		List<CSSRule> rules = CSSParser.parse("its-all-empty { /*empty*/ } empty { }");
		assertEquals(0, rules.size());
	}

	@Test
	public void testDoubleComments() throws Exception {
		List<CSSRule> rules = CSSParser.parse("its-all-empty { /* /* double comment */ } empty { height: 200px; /* /* double comment */width: 100px; }");

		System.out.println(rules);

		assertEquals(1, rules.size());

		CSSRule rule = rules.get(0);

		assertEquals("height", rule.getPropertyValues().get(0).getProperty());
		assertEquals("200px", rule.getPropertyValues().get(0).getValue());
		assertEquals("width", rule.getPropertyValues().get(1).getProperty());
		assertEquals("100px", rule.getPropertyValues().get(1).getValue());
	}

	@Test
	public void testBasicMultipleValues() throws Exception {
		List<CSSRule> rules = CSSParser.parse("div { width: 100px; -mozilla-opacity: 345; } /* a comment */ beta{height:200px;display:blocked;}table td{}");

		assertEquals(2, rules.size());

		CSSRule rule = rules.get(0);
		assertEquals("div", rule.getSelectors().get(0).toString());
		assertEquals("width", rule.getPropertyValues().get(0).getProperty());
		assertEquals("100px", rule.getPropertyValues().get(0).getValue());
		assertEquals("-mozilla-opacity", rule.getPropertyValues().get(1).getProperty());
		assertEquals("345", rule.getPropertyValues().get(1).getValue());

		rule = rules.get(1);
		assertEquals("beta", rule.getSelectors().get(0).toString());
		assertEquals("height", rule.getPropertyValues().get(0).getProperty());
		assertEquals("200px", rule.getPropertyValues().get(0).getValue());
		assertEquals("display", rule.getPropertyValues().get(1).getProperty());
		assertEquals("blocked", rule.getPropertyValues().get(1).getValue());
	}

	@Test
	public void testDuplicatePropertiesSameOrder() throws Exception {
		List<CSSRule> rules = CSSParser.parse("product-row { background: #ABC123; border: 1px black solid; border: none;background:   url(http://www.domain.com/image.jpg);}");

		CSSRule rule = rules.get(0);
		assertEquals("product-row", rule.getSelectors().get(0).toString());
		assertEquals("background", rule.getPropertyValues().get(0).getProperty());
		assertEquals("#ABC123", rule.getPropertyValues().get(0).getValue());
		assertEquals("border", rule.getPropertyValues().get(1).getProperty());
		assertEquals("1px black solid", rule.getPropertyValues().get(1).getValue());
		assertEquals("border", rule.getPropertyValues().get(2).getProperty());
		assertEquals("none", rule.getPropertyValues().get(2).getValue());
		assertEquals("background", rule.getPropertyValues().get(3).getProperty());
		assertEquals("url(http://www.domain.com/image.jpg)", rule.getPropertyValues().get(3).getValue());
	}

	@Test
	public void testMultipleSelectors() throws Exception {
		List<CSSRule> rules = CSSParser.parse("alpha, beta { width: 100px; text-decoration: underlined; } gamma delta { } epsilon, /* some comment */ zeta { height: 34px; } ");

		assertEquals(2, rules.size());

		/*
		 * Rule 1
		 */
		CSSRule rule = rules.get(0);
		assertEquals("alpha", rule.getSelectors().get(0).toString());
		assertEquals("beta", rule.getSelectors().get(1).toString());

		assertEquals("width", rule.getPropertyValues().get(0).getProperty());
		assertEquals("100px", rule.getPropertyValues().get(0).getValue());
		assertEquals("text-decoration", rule.getPropertyValues().get(1).getProperty());
		assertEquals("underlined", rule.getPropertyValues().get(1).getValue());

		/*
		 * Rule 2
		 */
		rule = rules.get(1);
		assertEquals("epsilon", rule.getSelectors().get(0).toString());
		assertEquals("zeta", rule.getSelectors().get(1).toString());

		assertEquals("height", rule.getPropertyValues().get(0).getProperty());
		assertEquals("34px", rule.getPropertyValues().get(0).getValue());
	}
}
