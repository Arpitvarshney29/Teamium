/**
 * 
 */
package com.teamium.domain.prod.resources.suppliers;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.teamium.domain.prod.resources.functions.RatedFunction;

/**
 * Describe a specific function name for a supplier nomenclatura
 * @author sraybaud - NovaRem
 *
 */
@Entity
@DiscriminatorValue("supply")
@NamedQueries({
	@NamedQuery(name=SupplyFunction.QUERY_findRootNames, query="SELECT n FROM SupplyFunction n WHERE n.parent IS NULL AND n.supplier = ?1 AND n.archived != TRUE ORDER BY n.value ASC"),
	@NamedQuery(name=SupplyFunction.QUERY_findNamesByParent, query="SELECT n FROM SupplyFunction n WHERE n.parent = ?1 AND n.supplier = ?2 AND n.archived != TRUE ORDER BY n.value ASC"),
	@NamedQuery(name=SupplyFunction.QUERY_findAll, query="SELECT f FROM SupplyFunction f WHERE f.archived != TRUE")
	// @NamedQuery(name=SupplyFunction.QUERY_deleteEmpty, query="DELETE FROM SupplyFunction f WHERE NOT EXISTS(SELECT r FROM Rate r WHERE r.function = f)") not used
})
public class SupplyFunction extends RatedFunction {
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 3428878835388292370L;
	
	/**
	 * Name of the query returning all generic functions
	 * @return the functions' list
	 */
	public static final String QUERY_findAll = "findAllSupplyFunction";	
	
	/**
	 * Name of the query deleting all empty SupplyFunction (without any rate)
	 */
	//public static final String QUERY_deleteEmpty = "deleteEmptySupplyFunction";	// Not used
	
	/**
	 * Name of the query returning all root names for the given supplier
	 * @param 1 the targeted supplier
	 * @return the list of function names
	 */
	public static final String QUERY_findRootNames = "findSupplyRootName";

	
	/**
	 * Name of the query returning child names of a given parent name
	 * @param 1 the parent function name
	 * @param 2 the targeted supplier
	 * @return the children function names
	 */
	public static final String QUERY_findNamesByParent = "findSupplyFunctionNameByParent";
	
	/**
	 * Associated supplier
	 */
	@ManyToOne
	@JoinColumn(name="c_idsupplier")
	private Supplier supplier;

	/**
	 * @return the supplier
	 */
	public Supplier getSupplier() {
		return supplier;
	}

	/**
	 * @param supplier the supplier to set
	 */
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	
	


}
