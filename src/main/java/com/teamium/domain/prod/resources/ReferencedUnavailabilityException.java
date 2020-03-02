/**
 * 
 */
package com.teamium.domain.prod.resources;

import com.teamium.domain.TeamiumException;

/**
 * Describes a exception thrown when a period of unavailability is already covered
 * @author slopes - NovaRem
 *
 */
public class ReferencedUnavailabilityException extends TeamiumException {
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 6869133535879571088L;

	/**
	 * 
	 */
	public ReferencedUnavailabilityException() {
		this((Throwable) null);
	}

	/**
	 * @param message
	 */
	public ReferencedUnavailabilityException(String message) {
		super(message);
	}

	/**
	 * @param throwable
	 */
	public ReferencedUnavailabilityException(Throwable throwable) {
		super("unavailability_referenced", throwable);
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public ReferencedUnavailabilityException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
