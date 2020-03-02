/**
 * 
 */
package com.teamium.domain.prod.resources;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.teamium.domain.Person;

/**
 * @author slopes
 *
 */
@Entity
@DiscriminatorValue("guest")
public class Guest extends Person implements Cloneable {
	/**
	 * The generated ID
	 */
	private static final long serialVersionUID = 6741549603275774871L;

}
