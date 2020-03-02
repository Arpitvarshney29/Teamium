/**
 * 
 */
package com.teamium.domain.prod.resources;

import com.teamium.domain.TeamiumException;

/**
 * @author slopes
 *
 */
public class EquipmentResourceParentCyclicException extends TeamiumException {

	/**
	 * The generated serial IUD
	 */
	private static final long serialVersionUID = -3289178021194284167L;

	/**
	 * 
	 */
	public EquipmentResourceParentCyclicException() {
		this("equipment_resource_cyclic_parent");
	}

	/**
	 * @param arg0
	 */
	public EquipmentResourceParentCyclicException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public EquipmentResourceParentCyclicException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public EquipmentResourceParentCyclicException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
