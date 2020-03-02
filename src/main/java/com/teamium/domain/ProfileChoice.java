/**
 * 
 */
package com.teamium.domain;

import javax.persistence.Embeddable;



/**
 * Describe a company type of profile
 * @author sraybaud- NovaRem
 */
@Embeddable
public class ProfileChoice extends ProfileType{

	/**
	 * Class uid
	 */
	private static final long serialVersionUID = 2958565867567724579L;
	
	/**
	 * The current choice
	 */
	private String value;

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
