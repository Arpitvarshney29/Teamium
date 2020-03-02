/**
 * 
 */
package com.teamium.domain.output.edition;

import com.teamium.domain.TeamiumException;

/**
 * Describes a exception thrown by the Edition business layer
 * @author sraybaud - NovaRem
 *
 */
public class EditionException extends TeamiumException {
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 6869133535879571088L;

	/**
	 * 
	 */
	public EditionException() {
		this((Throwable) null);
	}

	/**
	 * @param message
	 */
	public EditionException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 */
	public EditionException(Throwable throwable) {
		super("edition_failed", throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public EditionException(String message, Throwable throwable) {
		super(message, throwable);
		// TODO Auto-generated constructor stub
	}

}
