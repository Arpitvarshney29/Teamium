package com.teamium.dto;

import com.teamium.domain.prod.resources.equipments.EquipmentResource;

public class EquipmentResourceDTO extends ResourceDTO<ResourceDTO<?>> {

	// private EquipmentDTO entity;

	public EquipmentResourceDTO() {
		super();
	}

	public EquipmentResourceDTO(EquipmentResource equpmentResource) {
		super(equpmentResource);
		// if (equpmentResource.getEntity() != null) {
		// this.entity = new EquipmentDTO(equpmentResource.getEntity());
		// }

	}

	/**
	 * Get EquipmentResource from DTO
	 * 
	 * @param equipmentResource
	 * @return
	 */
	public EquipmentResource getEquipmentResource(EquipmentResource equipmentResource) {
		// equipmentResource.setEntity(entity.getCurrentEquipment());
		super.getResource(equipmentResource);
		return equipmentResource;
	}

}
