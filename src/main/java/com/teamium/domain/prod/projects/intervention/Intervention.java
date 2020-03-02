/**
 * 
 */
package com.teamium.domain.prod.projects.intervention;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.teamium.domain.prod.projects.Project;

/**
 * @author slopes
 * @since v6 Intervention context represent an operation provide by a supplier
 *        to fix equipment problem
 *
 */
@Entity
@DiscriminatorValue(Intervention.DISCRIMINATOR_VALUE)
@NamedQueries({
		@NamedQuery(name = Intervention.QUERY_FIND_BY_PERIOD, query = "SELECT i FROM Intervention i WHERE i.startDate <= ?2 AND i.endDate >= ?1 ORDER BY i.startDate ASC"),
		@NamedQuery(name = Intervention.QUERY_FIND_BY_PERIOD_BY_RESOURCE, query = "SELECT i FROM Intervention i WHERE EXISTS(SELECT b.id FROM Booking b WHERE b.from <= ?2 AND b.to >= ?1 AND b.record.id= i.id AND b.resource = ?3 ) ORDER BY i.startDate ASC"),
		@NamedQuery(name = Intervention.QUERY_FIND_BY_PEOJECT, query = "SELECT i FROM Intervention i WHERE i.source=?1"), })
public class Intervention extends Project {

	/**
	 * The generated serial UID
	 */
	private static final long serialVersionUID = 3704143296680924241L;

	/**
	 * The discriminator value used by the persistence context
	 */
	public static final String DISCRIMINATOR_VALUE = "intervention";

	/**
	 * Request for find intervention by period
	 * 
	 * @param 1
	 *            start
	 * @param 2
	 *            end
	 */
	public final static String QUERY_FIND_BY_PERIOD = "intervention_find_by_period";

	/**
	 * Request for find intervention by period
	 * 
	 * @param 1
	 *            start
	 * @param 2
	 *            end
	 * @param 3
	 *            resource
	 */
	public final static String QUERY_FIND_BY_PERIOD_BY_RESOURCE = "intervention_find_by_period_by_resource";

	/**
	 * Request for find intervention by project
	 * 
	 * @param 1
	 *            project
	 * 
	 */
	public final static String QUERY_FIND_BY_PEOJECT = "intervention+find_by_project";

}
