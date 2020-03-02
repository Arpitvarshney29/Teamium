/**
 * 
 */
package com.teamium.domain;

/**
 * Describe a exception thrown by the Teamium business layer
 * @author sraybaud - NovaRem
 *
 */
public class TeamiumException extends Exception {
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -2706813221043771303L;

	/**
	 * 
	 */
	public TeamiumException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public TeamiumException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public TeamiumException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public TeamiumException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}
