/**
 * 
 */
package com.teamium.domain.prod.resources.functions;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Describe a resource function
 * @author sraybaud - NovaRem
 *
 */
@Entity
@DiscriminatorValue("process")
@NamedQueries({
	@NamedQuery(name=ProcessFunction.QUERY_findAll, query="SELECT f FROM ProcessFunction f WHERE f.archived != TRUE")
})
public class ProcessFunction extends RatedFunction{
	/**
	 * Class UID
	 */	
	private static final long serialVersionUID = 4578805697979777778L;
	
	/**
	 * Retrieve all process functions
	 * @return the functions list
	 */
	public static final String QUERY_findAll = "findAllProcessFunction";
}
