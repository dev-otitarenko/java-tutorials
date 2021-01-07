package com.maestro.lib.cssparser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a CSS rule.
 */
public final class CSSRule {
	private List<CSSSelector> selectors;
	private List<CSSPropertyValue> propertyValues;

	/**
	 * Creates a rule with a single selector.
	 * 
	 * @param selector A selector that the rule should initial with.
	 */
	public CSSRule(final CSSSelector selector) {
		this();
		this.selectors.add(selector);
	}

	/**
	 * Creates an empty rule.
	 */
	public CSSRule() {
		this(new ArrayList<CSSSelector>());
	}

	/**
	 * Creates a new rule based on a list of selectors.
	 * 
	 * @param selectors A list of selectors that the rule should initial with.
	 */
	public CSSRule(final List<CSSSelector> selectors) {
		this.selectors = selectors;
		this.propertyValues = new ArrayList<CSSPropertyValue>();
	}

	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();

		out.append(implode(selectors) + " {\n");
		for (CSSPropertyValue propertyValue : propertyValues) {
			out.append("\t" + propertyValue + ";\n");
		}
		out.append("}\n");

		return out.toString();
	}

	/**
	 * Adds a property value to the rule.
	 * 
	 * @param propertyValue The property value that should be attached.
	 */
	public void addPropertyValue(final CSSPropertyValue propertyValue) {
		propertyValues.add(propertyValue);
	}

	/**
	 * Returns a list of all property values attached to the rule.
	 * 
	 * @return A list of all property values attached to the rule.
	 */
	public List<CSSPropertyValue> getPropertyValues() {
		return propertyValues;
	}

	/**
	 * Returns a list of all selectors attached to the rule.
	 * 
	 * @return A list of all selectors attached to the rule.
	 */
	public List<CSSSelector> getSelectors() {
		return selectors;
	}

	/**
	 * Adds a list of selectors to the existing list of selectors.
	 * 
	 * @param selectors A list of selectors that should be appended.
	 */
	public void addSelectors(final List<CSSSelector> selectors) {
		this.selectors.addAll(selectors);
	}

	/**
	 * Implodes the list of selectors into a pretty String.
	 * 
	 * @param values A list of selectors.
	 * @return A fancy String.
	 */
	private String implode(final List<CSSSelector> values) {
		StringBuilder sb = new StringBuilder();
		Iterator<CSSSelector> iterator = values.iterator();

		while (iterator.hasNext()) {
			CSSSelector selector = iterator.next();
			sb.append(selector.toString());
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}

		return sb.toString();
	}

	/**
	 * Removes a property value from the rule.
	 * 
	 * @param propertyValue The property value that should be removed.
	 */
	public void removePropertyValue(final CSSPropertyValue propertyValue) {
		propertyValues.remove(propertyValue);
	}

	/**
	 * Adds a selector to the rule.
	 * 
	 * @param selector The selector that should be attached to the rule.
	 */
	public void addSelector(final CSSSelector selector) {
		selectors.add(selector);
	}

	/**
	 * Removes a selector from the rule.
	 * 
	 * @param selector The selector that should be removed from the rule.
	 */
	public void removeSelector(final CSSSelector selector) {
		selectors.remove(selector);
	}
}
