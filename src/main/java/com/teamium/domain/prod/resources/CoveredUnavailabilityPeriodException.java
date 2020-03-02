/**
 * 
 */
package com.teamium.domain.prod.resources;

import com.teamium.domain.TeamiumException;

/**
 * Describes a exception thrown when a period of unavailability is already covered
 * @author sraybaud - NovaRem
 *
 */
public class CoveredUnavailabilityPeriodException extends TeamiumException {
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 6869133535879571088L;

	/**
	 * 
	 */
	public CoveredUnavailabilityPeriodException() {
		this((Throwable) null);
	}

	/**
	 * @param message
	 */
	public CoveredUnavailabilityPeriodException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 */
	public CoveredUnavailabilityPeriodException(Throwable throwable) {
		super("unavailability_covered", throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public CoveredUnavailabilityPeriodException(String message, Throwable throwable) {
		super(message, throwable);
		// TODO Auto-generated constructor stub
	}

}
