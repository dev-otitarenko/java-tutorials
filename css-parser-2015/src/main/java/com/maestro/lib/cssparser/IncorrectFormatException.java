package com.maestro.lib.cssparser;

/**
 * An exception that is thrown when the CSS parser finds a character it shouldn't have.
 */
public class IncorrectFormatException extends Exception {
	private static final long serialVersionUID = 1L;
	private final ErrorCode errorCode;

	/**
	 * Creates a new IncorrectFormatExeption with an error message;
	 * 
	 * @param errorCode S unique error code associated with the error.
	 * @param message Error message describing the problem.
	 */
	IncorrectFormatException(final ErrorCode errorCode, final String message) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * Returns a unique error code associated with the error.
	 */
	public ErrorCode getErrorCode() {
		return errorCode;
	}

	/**
	 * List of unique error codes.
	 */
	public enum ErrorCode {
		/**
		 * When the parse founds a semicolon ; when reading the property name.
		 */
		FOUND_SEMICOLON_WHEN_READING_PROPERTY_NAME,
		/**
		 * When the parse founds an end bracket } before the value's semicolon ; ending.
		 */
		FOUND_END_BRACKET_BEFORE_SEMICOLON,
		/**
		 * When the parse founds a colon , before reading a real selector name.
		 */
		FOUND_COLON_WHEN_READING_SELECTOR_NAME,
		/**
		 * When the parse the value a colon : was found.
		 */
		FOUND_COLON_WHILE_READING_VALUE;
	}
}
