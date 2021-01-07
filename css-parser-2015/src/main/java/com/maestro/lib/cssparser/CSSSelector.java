package com.maestro.lib.cssparser;

/**
 * Represents a CSS selector.
 * 
 */
public final class CSSSelector {
	private String name;

	/**
	 * Creates a new selector.
	 * 
	 * @param name Selector name.
	 */
	public CSSSelector(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof CSSSelector) {
			CSSSelector target = (CSSSelector) object;
			return target.name.equalsIgnoreCase(name);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}
