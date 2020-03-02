package com.teamium.domain.prod.resources.equipments;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;

import com.teamium.domain.prod.resources.functions.RatedFunction;

/**
 * Describe an equipment function
 * @author JS - NovaRem
 *
 */
@Entity
@DiscriminatorValue("equipment")
@NamedQuery(name=EquipmentFunction.QUERY_findAll,query="SELECT f FROM EquipmentFunction f WHERE f.archived != TRUE")
public class EquipmentFunction extends RatedFunction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1957319225329896108L;
	/**
	 * Name of the query returning the all staff functions
	 * @return the matching functions
	 */
	public static final String QUERY_findAll = "findAllEquipmentFunction";
}
