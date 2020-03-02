/**
 * 
 */
package com.teamium.domain.output.edition;

/**
 * Describes a exception thrown by the Edition business layer when no available edition was found for the xml output entity
 * @author sraybaud - NovaRem
 *
 */
public class NoEditionAvailableException extends EditionException {

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -1305690542685460847L;

	/**
	 * 
	 */
	public NoEditionAvailableException() {
		this((Throwable) null);
	}

	/**
	 * @param message
	 */
	public NoEditionAvailableException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 */
	public NoEditionAvailableException(Throwable throwable) {
		super("edition_unavailable", throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public NoEditionAvailableException(String message, Throwable throwable) {
		super(message, throwable);
		// TODO Auto-generated constructor stub
	}

}
