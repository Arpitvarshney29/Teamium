package com.teamium.dto;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.Company;
import com.teamium.domain.Document;
import com.teamium.domain.prod.resources.SaleEntity;

/**
 * Wrapper class for SaleEntity Entity
 * 
 * @author Nishant Kumar
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SaleEntityDTO extends CompanyDTO {

	private Boolean archived;
	private String editionGroup;
	private Float validationThreshold;

	public SaleEntityDTO() {
		super();
	}

	public SaleEntityDTO(SaleEntity saleEntity) {
		super(saleEntity);
		this.archived = saleEntity.getArchived();
		this.editionGroup = saleEntity.getEditionGroup();
		this.validationThreshold = saleEntity.getValidationThreshold();
	}

	/**
	 * Get SaleEntity Details from DTO
	 * 
	 * @param saleEntity
	 *            the saleEntity
	 * 
	 * @return SaleEntity
	 */
	@JsonIgnore
	public SaleEntity getSaleEntityDetails(SaleEntity saleEntity) {
		super.getCompanyDetails(saleEntity);
		saleEntity.setArchived(archived);
		saleEntity.setEditionGroup(editionGroup);
		saleEntity.setValidationThreshold(validationThreshold);
		return saleEntity;
	}

	/**
	 * Get SaleEntity Details from DTO
	 * 
	 * @param saleEntity
	 *            the saleEntity
	 * 
	 * @return SaleEntity
	 */
	@JsonIgnore
	public SaleEntity getSaleEntity() {
		return this.getSaleEntityDetails(new SaleEntity());
	}

	public SaleEntityDTO(Company company) {
		super(company);
	}

	public SaleEntityDTO(String name, Document logo, AddressDTO address, String companyNumber, String vatNumber,
			String accountNumber, String comments, Map<String, String> numbers) {
		super(name, logo, address, companyNumber, vatNumber, accountNumber, comments, numbers, address, comments);

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

	/**
	 * @return the editionGroup
	 */
	public String getEditionGroup() {
		return editionGroup;
	}

	/**
	 * @param editionGroup
	 *            the editionGroup to set
	 */
	public void setEditionGroup(String editionGroup) {
		this.editionGroup = editionGroup;
	}

	/**
	 * @return the validationThreshold
	 */
	public Float getValidationThreshold() {
		return validationThreshold;
	}

	/**
	 * @param validationThreshold
	 *            the validationThreshold to set
	 */
	public void setValidationThreshold(Float validationThreshold) {
		this.validationThreshold = validationThreshold;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SaleEntityDTO [archived=" + archived + ", editionGroup=" + editionGroup + ", validationThreshold="
				+ validationThreshold + "]";
	}

}
