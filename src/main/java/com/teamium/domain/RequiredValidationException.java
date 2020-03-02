package com.teamium.domain;

public class RequiredValidationException extends TeamiumException  {

	/**
	 * Generated uid
	 */
	private static final long serialVersionUID = 3416472932762018232L;
	
	/**
	 * 
	 */
	public RequiredValidationException() {
	}

	/**
	 * @param arg0
	 */
	public RequiredValidationException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public RequiredValidationException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public RequiredValidationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
