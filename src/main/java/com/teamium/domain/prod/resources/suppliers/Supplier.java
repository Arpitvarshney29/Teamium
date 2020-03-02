/**
 * 
 */
package com.teamium.domain.prod.resources.suppliers;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.teamium.domain.Company;

/**
 * Describe a supplier
 * @author sraybaud - NovaRem
 *
 */
@Entity
@DiscriminatorValue("supplier")
public class Supplier extends Company{

	/**
	 * Class uid
	 */
	private static final long serialVersionUID = 8761059193444168755L;
}
