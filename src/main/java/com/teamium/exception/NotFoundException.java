package com.teamium.exception;

/**
 * Request not found exception handler class.
 * 
 * @author Avinash Gupta
 * @version 1.0
 */
public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotFoundException() {
		super();
	}

	public NotFoundException(String message) {
		super(message);
	}
}
