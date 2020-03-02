package com.teamium.domain.prod.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.prod.resources.functions.Function;

/**
 * Describe an equipment used for resource planning.
 * 
 * @author sraybaud - NovaRem <T> the type of the managed resource <U> the type
 *         of function accomplished by the resource
 * @version 5.4 : Add Query findByName @ findByGroup
 * 
 * @author slopes
 * @since v5.7.0
 * @param <T>
 *            The resource type used as parent
 */
@Entity
@Table(name = "t_resource")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "c_discriminator", discriminatorType = DiscriminatorType.STRING)
@NamedQueries({
		/**
		 * @since v5.2.0
		 * @author slopes The query find groups has not anymore the group by because it
		 *         is not work : name=Resource.QUERY_findGroups, query="SELECT DISTINCT
		 *         r.group FROM Resource r WHERE r.group IS NOT NULL ORDER BY
		 *         r.group.title"
		 *
		 */
		@NamedQuery(name = Resource.QUERY_findByAvailability, query = "SELECT DISTINCT r FROM Resource r WHERE NOT EXISTS (SELECT pu FROM UnavailabilityEvent pu WHERE TYPE(pu) = UnavailabilityEvent AND pu.resource = r AND ((pu.from <= ?1 AND pu.to IS NULL) or (pu.from <= ?1 AND pu.to >= ?2)))"),
		@NamedQuery(name = Resource.QUERY_findByGroupByAvailability, query = "SELECT DISTINCT r FROM Resource r WHERE r.parent = ?1 AND NOT EXISTS (SELECT pu FROM UnavailabilityEvent pu WHERE TYPE(pu) = UnavailabilityEvent AND pu.resource = r AND ((pu.from <= ?2 AND pu.to IS NULL) or (pu.from <= ?2 AND pu.to >= ?3)))"),
		@NamedQuery(name = Resource.QUERY_findByFunctionByAvailability, query = "SELECT DISTINCT r FROM Resource r, IN(r.functions) f WHERE f.function IN (?1) AND NOT EXISTS (SELECT pu FROM UnavailabilityEvent pu WHERE TYPE(pu) = UnavailabilityEvent AND pu.resource = r AND ((pu.from <= ?2 AND pu.to IS NULL) or (pu.from <= ?2 AND pu.to >= ?3)))"),
		@NamedQuery(name = Resource.QUERY_findByProjectByAvailability, query = "SELECT l.resource FROM Line l WHERE l.record = ?1"),
		@NamedQuery(name = Resource.QUERY_findForProcessFunctionByProjectByAvailability, query = "SELECT l.resource FROM Line l WHERE l.record = ?1 AND l.function.class = ProcessFunction"),
		@NamedQuery(name = Resource.QUERY_findByName, query = "SELECT r FROM Resource r, IN (r.functions) f WHERE LOWER(r.name) LIKE ?1 AND f.function IN (?2)"),
		@NamedQuery(name = Resource.QUERY_findByGroup, query = "SELECT r FROM Resource r, IN (r.functions) f WHERE LOWER(r.parent.name) LIKE ?1 AND f.function IN (?2)"),
		@NamedQuery(name = Resource.QUERY_findGroups, query = "SELECT DISTINCT r FROM Resource r WHERE EXISTS(SELECT r2 FROM Resource r2 WHERE r2.parent = r)"),
		@NamedQuery(name = Resource.QUERY_findBookedByFunctionByDateRange, query = "SELECT DISTINCT r FROM Resource r WHERE EXISTS(SELECT b FROM Booking b WHERE b.resource = r AND b.function = ?1 AND (b.from >= ?2 AND b.to <= ?3))"),
		@NamedQuery(name = Resource.QUERY_countAvailableResources, query = "SELECT COUNT(r) FROM Resource r, IN(r.functions) f WHERE TYPE(r) != DefaultResource AND f.function = ?1 AND NOT EXISTS(SELECT ev FROM Event ev WHERE  ev.resource = r AND ev.from >= ?2 AND ev.to <= ?3)"),
		@NamedQuery(name = Resource.QUERY_findAvailableResources, query = "SELECT r FROM Resource r, IN(r.functions) f WHERE TYPE(r) != DefaultResource AND f.function = ?1 AND NOT EXISTS(SELECT ev FROM Event ev WHERE  ev.resource = r AND ev.from >= ?2 AND ev.to <= ?3)"),

		@NamedQuery(name = Resource.QUERY_countAvailableResourcesByFunctionAndStartAndEndDate, query = "SELECT COUNT(r) FROM Resource r, IN(r.functions) f WHERE TYPE(r) != DefaultResource AND f.function = ?1 "
				+ "AND NOT EXISTS(SELECT ev FROM Event ev WHERE  ev.resource = r AND ev.from >= ?2 AND ev.to <= ?3) "
				+ "AND NOT EXISTS(SELECT pu FROM UnavailabilityEvent pu WHERE TYPE(pu) = UnavailabilityEvent AND pu.resource = r AND ((pu.from <= ?2 AND pu.to IS NULL) or (pu.from <= ?2 AND pu.to >= ?3)) ) "
				+ "AND( TYPE(r) != StaffResource OR NOT EXISTS (SELECT rosterEvent FROM RosterEvent rosterEvent join rosterEvent.resources res WHERE (rosterEvent.from <= ?3 and rosterEvent.to >= ?2) AND rosterEvent.function =?1 AND res = r )) "
				+ "AND NOT EXISTS(SELECT b FROM Booking b WHERE b.resource = r AND b.function = ?1 AND (b.from >= ?2 AND b.to <= ?3))")

		

})
public abstract class Resource<T extends Resource<?>> extends AbstractEntity implements Comparable<Resource<?>> {

	/**
	 * Class uid
	 */
	private static final long serialVersionUID = -2070287085624569979L;

	/**
	 * Name of the query retrieving available resources during full or part of the
	 * given period
	 * 
	 * @param 1
	 *            beginning of period
	 * @param 2
	 *            end of period
	 * @return the list of matching resources
	 */
	public static final String QUERY_findByAvailability = "findResourceByAvailability";

	/**
	 * Name of the query retrieving available resources for a given list function
	 * name, during full or part of the given period
	 * 
	 * @param 1
	 *            the given list of function names
	 * @param 2
	 *            beginning of period
	 * @param 3
	 *            end of period
	 * @return the list of matching resources
	 */
	public static final String QUERY_findByFunctionByAvailability = "findResourceByFunctionByAvailability";

	/**
	 * Name of the query returning resources belonging to the given group
	 * 
	 * @param parent
	 *            the group to match ( resource )
	 * @return the list of matching resources
	 */
	public static final String QUERY_findByGroupByAvailability = "findResourceByGroupByAvailability";

	/**
	 * Name of the query returning resources belonging to the given project
	 * 
	 * @param group
	 *            the group to match
	 * @return the list of matching resources
	 */
	public static final String QUERY_findByProjectByAvailability = "findResourceByProjectByAvailability";

	/**
	 * Name of the query returning resources belonging to the given project and
	 * related to ProcessFunction
	 * 
	 * @param project
	 *            the project to match
	 * @return the list of matching resources
	 */
	public static final String QUERY_findForProcessFunctionByProjectByAvailability = "findForProcessFunctionByProjectByAvailability";

	/**
	 * Name of the query returning resources belonging to the given name
	 * 
	 * @param name
	 *            the name to match
	 * @return the list of matching resources
	 */
	public static final String QUERY_findByName = "findResourceByName";

	/**
	 * Name of the query returning resources belonging to the given group
	 * 
	 * @param group
	 *            the group to match
	 * @return the list of matching resources
	 */
	public static final String QUERY_findByGroup = "findResourceByGroup";

	/**
	 * Name of the query returning the resource used as group
	 */
	public static final String QUERY_findGroups = "findGroups";

	/**
	 * Name of query used to find resource booked with the function given and the
	 * date range given in parameter
	 * 
	 * @param 1
	 *            The function
	 * @param 2
	 *            Start date of the range
	 * @param 3
	 *            End date of range
	 */
	public static final String QUERY_findBookedByFunctionByDateRange = "find_resource_booked_by_function_by_date_range";
	/**
	 * Count the available resource for a given function and a given period
	 * 
	 * @param 1
	 *            the function
	 * @param 1
	 *            The start date of the range
	 * @param 2
	 *            The end date of the range
	 */
	public static final String QUERY_countAvailableResources = "resource_countAvailableResources";
	/**
	 * Find the available resources for a given function and a given period
	 * 
	 * @param 1
	 *            the function
	 * @param 1
	 *            The start date of the range
	 * @param 2
	 *            The end date of the range
	 */
	public static final String QUERY_findAvailableResources = "resource_findAvailableResources";

	/**
	 * Count the available resource for a given function and a given period
	 * 
	 * @param 1
	 *            the function
	 * @param 1
	 *            The start date of the range
	 * @param 2
	 *            The end date of the range
	 */
	public static final String QUERY_countAvailableResourcesByFunctionAndStartAndEndDate = "resource_countAvailableResourcesByFunctionAndStartAndEndDate";
	/**
	 * Find the available resources for a given function and a given period
	 * 
	 * @param 1
	 *            the function
	 * @param 1
	 *            The start date of the range
	 * @param 2
	 *            The end date of the range
	 */
	public static final String QUERY_findResourcesAvailableByFunctionAndStartAndEndDate = "resource_findResourcesAvailableByFunctionAndStartAndEndDate";

	/**
	 * The resource ID
	 */
	@Id
	@Column(name = "c_idresource", insertable = false, updatable = false)
	@TableGenerator(name = "idResource_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "resource_idresource_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idResource_seq")
	private Long id;

	/**
	 * The position of the resource (for display on planning)
	 */
	@Transient
	private String position;

	/**
	 * Cached name of the resource
	 */
	@Column(name = "c_name")
	private String name;

	/**
	 * The functions for which the resource is available to
	 */
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "c_idresource")
	private Set<ResourceFunction> functions;

	/**
	 * The informations generate in the fetch lazy data of equipment service
	 */
	@OneToMany(mappedBy="resource" ,cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private List<ResourceInformation> informations;

	/**
	 * The resource parent
	 */
	@ManyToOne
	@JoinColumn(name = "c_parent")
	private Resource<?> parent;

	@Transient
	private Boolean isExternalize;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the functions
	 */
	public Set<ResourceFunction> getFunctions() {
		if (this.functions == null)
			this.functions = new TreeSet<ResourceFunction>();
		return functions;
	}

	/**
	 * Return the function matching the given name
	 * 
	 * @param name
	 *            the name to match
	 * @return the matching function
	 */
	public ResourceFunction getFunction(Function name) {
		try {
			for (ResourceFunction function : this.getFunctions()) {
				if (name.equals(function.getFunction())) {
					return function;
				}
			}
		} catch (NullPointerException e) {
		}
		return null;
	}

	/**
	 * Assignes the given function to the current resource
	 * 
	 * @param function
	 *            the function to assign
	 * @retun the resource assignment if success, else returns false
	 */
	public ResourceFunction assignFunction(Function name) {
		ResourceFunction assignment = new ResourceFunction();
		assignment.setFunction(name);
		for (ResourceFunction f : this.getFunctions()) {
			if (name.equals(f.getFunction())) {
				return null;
			}
		}
		if (this.getFunctions().add(assignment)) {
			return assignment;
		}
		return null;
	}

	/**
	 * Removes the given assignement from the current resource
	 * 
	 * @return function the function assignment to remove
	 * @retun true if success, else returns false
	 */
	public boolean unassignFunction(ResourceFunction function) {
		return this.getFunctions().remove(function);
	}

	/**
	 * 
	 * Returns the cached name of the resource
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set the resource name in cache
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the position
	 */
	public String getPosition() {
		return this.getComparatorPosition() + "_" + this.getName() + "_" + this.getId();
	}

	/**
	 * @return the parent
	 */
	public T getParent() {
		return (T) parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(T parent) {
		this.parent = parent;
	}

	/**
	 * @return the managed entity as a resource
	 */
	public abstract ResourceEntity getEntity();

	/**
	 * @param entity
	 *            the entity to manage as a resource
	 */
	public abstract void setEntity(ResourceEntity entity);

	/**
	 * Comparator order
	 */
	protected abstract Integer getComparatorPosition();

	/**
	 * @return the reference position
	 */
	public Integer getRefPosition() {
		return getComparatorPosition();
	}

	/**
	 * @return the informations
	 */
	public List<ResourceInformation> getInformations() {
		if (informations == null) {
			informations = new ArrayList<ResourceInformation>();
		}
		return informations;
	}

	/**
	 * @param informations
	 *            the informations to set
	 */
	public void setInformations(List<ResourceInformation> informations) {
		this.informations = informations;
	}

	/**
	 * @return the isExternalize
	 */
	public Boolean getIsExternalize() {
		return isExternalize;
	}

	/**
	 * @param isExternalize
	 *            the isExternalize to set
	 */
	public void setIsExternalize(Boolean isExternalize) {
		this.isExternalize = isExternalize;
	}

	/**
	 * Compare the current resource with the given resource
	 * 
	 * @param other
	 *            the resource to compare
	 * @return -1 if current before the given resource, 0 if equals, +1 if it's
	 *         after the given one
	 */
	@Override
	public int compareTo(Resource<?> o) {
		if (o == null)
			return 1;
		if (this.getComparatorPosition() == null) {
			if (o.getComparatorPosition() != null) {
				return -1;
			}
		} else {
			int compare = this.getComparatorPosition().compareTo(o.getComparatorPosition());
			if (compare != 0)
				return compare;
		}
		if (this.getName() == null) {
			if (o.getName() != null) {
				return -1;
			}
		} else {
			if (o.getName() == null) {
				return 1;
			}
			int compare = this.getName().toLowerCase().compareTo(o.getName().toLowerCase());
			if (compare != 0)
				return compare;
		}
		if (this.getId() == null) {
			if (o.getId() == null) {
				return -1;
			}
		} else {
			int compare = this.getId().compareTo(o.getId());
			if (compare != 0)
				return compare;
		}
		return 0;
	}

	/**
	 * Returns the string expression of the current object
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		return super.toString() + this.getName();
	}
}
