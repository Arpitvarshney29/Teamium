package com.teamium.domain.prod.resources.staff;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import com.teamium.domain.prod.resources.Resource;
import com.teamium.domain.prod.resources.ResourceEntity;

/**
 * Describe staff faceted resource
 * 
 * @author sraybaud - NovaRem
 *
 */
@Entity
@DiscriminatorValue("staff")
@NamedQueries({
		@NamedQuery(name = StaffResource.FIND_GROUPS, query = "SELECT DISTINCT sr FROM StaffResource sr WHERE sr.entity IS NULL"),
		@NamedQuery(name = StaffResource.FIND_CHILDREN, query = "SELECT sr FROM StaffResource sr WHERE sr.parent = ?1"),
		@NamedQuery(name = StaffResource.FIND_STAFF_RESOURCES, query = "SELECT DISTINCT sr FROM StaffResource sr join sr.functions sr1 where sr1.function =?1  AND NOT EXISTS(SELECT pu FROM UnavailabilityEvent pu WHERE TYPE(pu) = UnavailabilityEvent AND pu.resource = sr AND ((pu.from <= ?2 AND pu.to IS NULL) or (pu.from <= ?2 AND pu.to >= ?3))) AND NOT EXISTS (SELECT rosterEvent FROM RosterEvent rosterEvent join rosterEvent.resources res WHERE (rosterEvent.from <= ?3 and rosterEvent.to >= ?2) AND rosterEvent.function =?1 AND rosterEvent.id !=?4 AND sr IN(res))")
})
public class StaffResource extends Resource<StaffResource> {

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 4670086237496146600L;

	/**
	 * The named query used to find equipment groups
	 */
	public static final String FIND_GROUPS = "find_staff_group";

	public static final String FIND_STAFF_RESOURCES = "find_staff_resources";

	/**
	 * The named query used to find children of the staff resource given in
	 * parameter
	 * 
	 * @param 1
	 *            The staff resource
	 */
	public static final String FIND_CHILDREN = "find_staff_resource_childrens";

	/**
	 * The staff member corresponding to the resource
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "c_idstaffmember")
	private StaffMember entity;

	
	/**
	 * The staff groups corresponding to the resource
	 */
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "t_staff_group", joinColumns = { @JoinColumn(name = "c_idparent") }, inverseJoinColumns = {
			@JoinColumn(name = "c_idresource") })
	private Set<StaffResource> staffGroups = new HashSet<>();

	/**
	 * @return the managed entity as a resource
	 */
	public StaffMember getEntity() {
		return entity;
	}

	/**
	 * @param entity
	 *            the entity to manage as a resource
	 */
	public void setStaffMember(StaffMember entity) {
		this.entity = entity;
	}

	/**
	 * @param entity
	 *            the entity to manage as a resource
	 */
	@Override
	public void setEntity(ResourceEntity entity) {
		if (entity instanceof StaffMember) {
			this.setStaffMember((StaffMember) entity);
		}
	}

	/**
	 * Comparator order
	 */
	@Override
	protected Integer getComparatorPosition() {
		return 100;
	}

	/**
	 * @return the staffGroups
	 */
	public Set<StaffResource> getStaffGroups() {
		return staffGroups;
	}

	/**
	 * @param staffGroups
	 *            the staffGroups to set
	 */
	public void setStaffGroups(Set<StaffResource> staffGroups) {
		this.staffGroups = staffGroups;
	}

}
