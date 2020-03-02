/**
 * 
 */
package com.teamium.domain.prod.projects;

/**
 * @author slopes
 *
 */
public enum BookingStatusType {
	UPDATE, DELETE;
	
	/**
	 * The textual representation of the instance
	 */
	public String toString(){
		return this.name();
	}
}
