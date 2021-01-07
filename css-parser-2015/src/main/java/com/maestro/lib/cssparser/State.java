package com.maestro.lib.cssparser;

/**
 * Different states to aid the CSS parser.
 * 
 */
enum State {
	/**
	 * Inside a selector
	 */
	INSIDE_SELECTOR,
	/**
	 * Inside a comment.
	 */
	INSIDE_COMMENT,
	/**
	 * Inside a property value.
	 */
	INSIDE_PROPERTY_NAME,
	/**
	 * Inside value.
	 */
	INSIDE_VALUE,
	/**
	 * Inside value and also inside (
	 */
	INSIDE_VALUE_ROUND_BRACKET;
}