/**
 * 
 */
package com.teamium.domain.prod.resources;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * @author slopes
 *
 */
@Entity
@DiscriminatorValue("inout")
@NamedQueries({
	@NamedQuery(name=InOutResource.FIND_BY_KEYWORD, query="SELECT ior FROM InOutResource ior WHERE LOWER(ior.name) LIKE ?1"),
	@NamedQuery(name=InOutResource.QUERY_findByProjectByAvailability, query="SELECT ior FROM InOutResource ior WHERE EXISTS ( SELECT io FROM InOut io WHERE ior.entity = io AND EXISTS ( SELECT p FROM Project p WHERE p = ?1 AND io MEMBER OF p.inOuts))"),
	@NamedQuery(name=InOutResource.QUERY_findByAvailability, query="SELECT ior FROM InOutResource ior WHERE EXISTS ( SELECT b FROM Booking b WHERE b.resource = ior AND b.from >= ?1 AND b.to <= ?2 )"),
})
public class InOutResource extends Resource<InOutResource> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6359010219331581193L;
	
	/**
	 * The named query used to find equipment matching the keyword given in parameter
	 * @param 1 keyword
	 */
	public static final String FIND_BY_KEYWORD = "find_inout_resource_by_keyword";
	
	/**
	 * Name of the query returning resources belonging to the given project
	 * @param group the group to match
	 * @return the list of matching resources
	 */
	public static final String QUERY_findByProjectByAvailability = "findInOutResourceByProjectByAvailability";
	
	/**
	 * Name of the query returning resources belonging to the period whit booking
	 * @param start calendar
	 * @param end calendar
	 * @return the list of matching resources
	 */
	public static final String QUERY_findByAvailability = "findInOutResourceByAvailability";
	
	/**
	 * The staff member corresponding to the resource
	 */
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="c_idinout")
	private InOut entity;

	/**
	 * Return the in out
	 */
	@Override
	public InOut getEntity() {
		return this.entity;
	}
	
	@Override
	protected Integer getComparatorPosition() {
		return 400;
	}

	@Override
	public void setEntity(ResourceEntity entity) {
		if(entity instanceof InOut){
			this.entity = (InOut) entity;
		}
	}
}
