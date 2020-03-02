/**
 * 
 */
package com.teamium.domain.prod.resources.functions;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Describe a resource function
 * @author sraybaud - NovaRem
 *
 */
@Entity
@DiscriminatorValue("right")
public class RightFunction extends RatedFunction{
	/**
	 * Class UID
	 */	
	private static final long serialVersionUID = 4578805697979777778L;
}
