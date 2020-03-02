/**
 * 
 */
package com.teamium.domain.prod.resources.staff;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;

import com.teamium.domain.prod.resources.functions.RatedFunction;

/**
 * Describe a resource function
 * @author sraybaud - NovaRem
 *
 */
@Entity
@DiscriminatorValue("staff")
@NamedQuery(name=StaffFunction.QUERY_findAll,query="SELECT f FROM StaffFunction f WHERE f.archived != TRUE")
public class StaffFunction extends RatedFunction{
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -1564052585009784043L;
	
	/**
	 * Name of the query returning the all staff functions
	 * @return the matching functions
	 */
	public static final String QUERY_findAll = "findAllStaffFunction";

}
