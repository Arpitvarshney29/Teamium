/**
 * 
 */
package com.teamium.domain;

/**
 * Décrit une exception levée par la couche de persistence de Teamium
 * @author sraybaud - NovaRem
 *
 *
 */
public class TeamiumPersistenceException extends TeamiumException {

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 1674857882884885626L;

	/**
	 * 
	 */
	public TeamiumPersistenceException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public TeamiumPersistenceException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public TeamiumPersistenceException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public TeamiumPersistenceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}
