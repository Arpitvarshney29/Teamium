package com.teamium.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.AbstractEntity;
import com.teamium.domain.ProductionUnit;

@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ProductionUnitDTO extends AbstractDTO {

	private Long id;
	private String name;
	private Boolean archived = Boolean.FALSE;

	public ProductionUnitDTO() {

	}

	public ProductionUnitDTO(AbstractEntity entity) {
		super(entity);
	}

	public ProductionUnitDTO(ProductionUnit productionUnit) {
		this.id = productionUnit.getId();
		this.setVersion(productionUnit.getVersion());
		this.name = productionUnit.getName();
		this.archived = productionUnit.getArchived();
	}

	/**
	 * To get Production-Unit
	 * 
	 * @param productionUnit
	 *            the productionUnit
	 * 
	 * @param productionUnitDTO
	 *            the productionUnitDTO
	 * 
	 * @return ProductionUnit object
	 */
	public ProductionUnit getProductionUnit(ProductionUnit productionUnit, ProductionUnitDTO productionUnitDTO) {
		setProductionUnitDetail(productionUnit, productionUnitDTO);
		return productionUnit;
	}

	/**
	 * To set production-unit
	 * 
	 * @param productionUnit
	 *            the productionUnit
	 * 
	 * @param productionUnitDTO
	 *            the productionUnitDTO
	 */
	public void setProductionUnitDetail(ProductionUnit productionUnit, ProductionUnitDTO productionUnitDTO) {
		productionUnit.setId(productionUnitDTO.getId());
		productionUnit.setVersion(productionUnitDTO.getVersion());
		productionUnit.setName(productionUnitDTO.getName());
		productionUnit.setArchived(productionUnitDTO.getArchived());
	}

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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the archived
	 */
	public Boolean getArchived() {
		return archived;
	}

	/**
	 * @param archived
	 *            the archived to set
	 */
	public void setArchived(Boolean archived) {
		this.archived = archived;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProductionUnitDTO [id=" + id + ", name=" + name + ", archived=" + archived + "]";
	}

}
