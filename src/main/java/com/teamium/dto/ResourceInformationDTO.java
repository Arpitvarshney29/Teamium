package com.teamium.dto;

import javax.persistence.Column;

import com.teamium.domain.prod.resources.ResourceInformation;
import com.teamium.domain.prod.resources.ResourceInformationKey;

/**
 * DTO Class for ResourceInformation Entity
 * 
 * @author wittybrains
 *
 */
public class ResourceInformationDTO extends AbstractDTO {

	private ResourceInformationKey type;
	private String description;
	private String keywordValue;
	private String keyValue;
	private Long functionId;

	public ResourceInformationDTO() {
		super();
	}

	public ResourceInformationDTO(ResourceInformation entity) {
		super(entity);
		this.type = entity.getType();
		this.description = entity.getDescription();
		this.keywordValue = entity.getKeywordValue();
		this.keyValue = entity.getKeyValue();
		this.functionId = entity.getFunctionId();
	}

	/**
	 * Get ResourceInformation entity from DTO
	 * 
	 * @param resourceInformation
	 * @return ResourceInformation
	 */
	public ResourceInformation getResourceInformation(ResourceInformation resourceInformation) {
		resourceInformation.setId(this.getId());
		resourceInformation.setVersion(this.getVersion());
		resourceInformation.setType(type);
		resourceInformation.setDescription(description);
		resourceInformation.setKeywordValue(keywordValue);
		resourceInformation.setKeyValue(keyValue);
		resourceInformation.setFunctionId(functionId);
		// super.getAbstractEntityDetails(resourceInformation);
		return resourceInformation;
	}

	/**
	 * @return the type
	 */
	public ResourceInformationKey getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(ResourceInformationKey type) {
		this.type = type;
	}

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
	 * @return the keywordValue
	 */
	public String getKeywordValue() {
		return keywordValue;
	}

	/**
	 * @param keywordValue
	 *            the keywordValue to set
	 */
	public void setKeywordValue(String keywordValue) {
		this.keywordValue = keywordValue;
	}

	/**
	 * @return the keyValue
	 */
	public String getKeyValue() {
		return keyValue;
	}

	/**
	 * @param keyValue
	 *            the keyValue to set
	 */
	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	/**
	 * @return the functionId
	 */
	public Long getFunctionId() {
		return functionId;
	}

	/**
	 * @param functionId
	 *            the functionId to set
	 */
	public void setFunctionId(Long functionId) {
		this.functionId = functionId;
	}

}
