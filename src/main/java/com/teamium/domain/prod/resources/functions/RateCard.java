/**
 * 
 */
package com.teamium.domain.prod.resources.functions;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Describe a custom rate for a given company
 * @author sraybaud - NovaRem
 *
 */
@Entity
@DiscriminatorValue("card")
@NamedQueries({
	@NamedQuery(name=RateCard.QUERY_findByFunctionByCompany, query="SELECT r FROM RateCard r WHERE r.function = ?1 AND r.company = ?2 AND r.archived != TRUE"),
	@NamedQuery(name=RateCard.QUERY_deleteByCompany, query="DELETE FROM RateCard r WHERE r.company = ?1 AND r.archived != TRUE"),
})
public class RateCard extends Rate{
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -8253825883401178799L;
	
	/**
	 * Name of the query returning standard rates only for a given function
	 * @param the given function
	 * @param the given customer
	 * @return the matching rates card
	 */
	public static final String QUERY_findByFunctionByCompany = "findRateCardByFunctionByCompany";
	
	/**
	 * Name of the query deleting rates for a given company
	 * @param the given company
	 * @return the count of removed rates
	 */
	public static final String QUERY_deleteByCompany = "deleteRateCardByCompany";
	
	
	
	/**
	 * Return the string expression of the current object
	 * @return the string
	 */
	@Override
	public String toString(){
		return super.toString()+" for "+this.getCompany();
	}
}
