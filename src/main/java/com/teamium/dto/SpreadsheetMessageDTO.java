package com.teamium.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Wrapper class for Abstract-Entity
 * 
 * @author Teamium
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SpreadsheetMessageDTO {

	private String message;
	private boolean containsError = false;

	public SpreadsheetMessageDTO() {
		super();
	}

	public SpreadsheetMessageDTO(String message, boolean containsError) {
		this.message = message;
		this.containsError = containsError;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the containsError
	 */
	public boolean isContainsError() {
		return containsError;
	}

	/**
	 * @param containsError the containsError to set
	 */
	public void setContainsError(boolean containsError) {
		this.containsError = containsError;
	}

}
