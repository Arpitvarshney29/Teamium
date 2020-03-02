package com.teamium.exception;

/**
 * Forbidden Exception Handler class.
 * 
 * @author Avinash Gupta
 * @version 1.0
 */
public class ForbiddenException extends RuntimeException {

	private static final long serialVersionUID = 3082972573961285436L;

	public ForbiddenException() {
		super();
	}

	public ForbiddenException(String message) {
		super(message);
	}
}
