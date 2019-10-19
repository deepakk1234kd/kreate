package com.moneyclick.exception;

/**
 * This is a Custom Exception for Date Parsing
 * 
 */
public class DateParseException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public DateParseException(String message) {
		super(message);
	}
	
}
