package com.teamium.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teamium.domain.prod.projects.order.SupplyResource;

/**
 * DTO Class for SupplierResource Entity
 * 
 * @author Nishant Kumar
 *
 */
public class SupplyResourceDTO extends ResourceDTO {

	public SupplyResourceDTO() {
		super();
	}

	@SuppressWarnings("unchecked")
	public SupplyResourceDTO(SupplyResource entity) {
		super(entity);
	}

	/**
	 * Get SupplyResource Entity from DTO
	 * 
	 * @param supplierResource
	 * @return SupplyResource
	 */

	@JsonIgnore
	public SupplyResource getSupplyResource(SupplyResource supplierResource) {
		super.getResource(supplierResource);
		return supplierResource;
	}

}
