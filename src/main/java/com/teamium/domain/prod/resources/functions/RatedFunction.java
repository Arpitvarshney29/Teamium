/**
 * 
 */
package com.teamium.domain.prod.resources.functions;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import com.teamium.domain.prod.resources.ResourceEntity;
import com.teamium.domain.prod.resources.SaleEntity;
import com.teamium.domain.prod.resources.functions.units.RateUnit;

/**
 * Describe a resource function
 * 
 * @author sraybaud - NovaRem
 * @note : DO ajout de la query findByFunctionType
 */
@Entity
@NamedQueries({
		@NamedQuery(name = RatedFunction.QUERY_findByName, query = "SELECT f FROM RatedFunction f WHERE f = ?1 AND f.archived != TRUE"),
		@NamedQuery(name = RatedFunction.QUERY_findNamesByFunctionType, query = "SELECT DISTINCT f FROM Function f WHERE TYPE(f) = ?1 AND f.archived != TRUE"),
		@NamedQuery(name = RatedFunction.QUERY_findByFunctionType, query = "SELECT f FROM RatedFunction f WHERE TYPE(f) = ?1 AND f.archived != TRUE") })
public abstract class RatedFunction extends Function implements ResourceEntity {

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -2411417445622173208L;

	/**
	 * Name of the query returning the total count of generic function
	 * 
	 * @return the count
	 */
	public static final String QUERY_findByName = "findFunctionByName";

	/**
	 * Name of the query retrieving the names for which functions have been assigned
	 * for a given type
	 * 
	 * @param 1
	 *            the function class
	 * @return the list of matching names
	 */
	public static final String QUERY_findNamesByFunctionType = "findFunctionNameByFunctionType";

	/**
	 * Name of the query retrieving the functions have been assigned for a given
	 * type
	 * 
	 * @param 1
	 *            the function class
	 * @return the list of matching functions
	 */
	public static final String QUERY_findByFunctionType = "findFunctionByFunctionType";

	/**
	 * Query string changing type of the current function
	 * 
	 * @param type
	 *            the type to set
	 * @param id
	 *            id of the targeted function
	 * @return the result of the update
	 */
	public static final String QUERY_nativeUpdateType = "UPDATE t_function SET c_version = c_version + 1, c_discriminator = ?1 WHERE c_idfunction = ?2";

	/**
	 * Query string changing type of the current function
	 * 
	 * @param 1
	 *            the master id
	 * @param 2
	 *            the ralation
	 * @return the slave
	 */
	public static final String QUERY_nativeFindSlaveInRelation = "SELECT c_idslave FROM t_function_siblings WHERE c_idmaster = ?1 and c_relationship = ?2";

	/**
	 * Query string changing type of the current function
	 * 
	 * @param 1
	 *            the slave id
	 * @param 2
	 *            the relation
	 * @return the master
	 */
	public static final String QUERY_nativeFindMasterInRelation = "SELECT c_idmaster FROM t_function_siblings WHERE c_idslave = ?1 and c_relationship = ?2";

	/**
	 * Query for creating a new relation
	 * 
	 * @param 1
	 *            the master id
	 * @param 2
	 *            the slave id
	 * @param 3
	 *            the relation
	 * @return the master
	 */
	public static final String QUERY_createRelation = "INSERT INTO t_function_siblings VALUES (?1,?2,?3)";

	/**
	 * Query for deleting a relation
	 * 
	 * @param 1
	 *            the master id
	 * @param 2
	 *            the slave id
	 * @param 3
	 *            the relation
	 * @return the master
	 */
	public static final String QUERY_deleteRelation = "DELETE FROM t_function_siblings WHERE c_idmaster = ?1 and c_idslave = ?2 and c_relationship = ?3";

	/**
	 * Function description
	 */
	@Column(name = "c_description")
	private String description;

	/**
	 * Default resource
	 */
	@OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, mappedBy = "function")
	@JoinColumn(name = "c_defaultresource")
	private DefaultResource defaultResource;

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the defaultResource
	 */
	public DefaultResource getDefaultResource() {
		return defaultResource;
	}

	/**
	 * Return a textual description of the current object
	 * 
	 * @return the textual description
	 */
	@Override
	public String toString() {
		return super.toString() + "->" + this.getValue();
	}

	/**
	 * Set the default resource before update
	 */
	@PreUpdate
	@PrePersist
	@Override
	public void beforeUpdate() {
		super.beforeUpdate();
//		if (this.defaultResource == null)
//			this.defaultResource = new DefaultResource();
//		this.defaultResource.setFunction(this);
//		this.defaultResource.setName(this.getQualifiedName());
	}

}
