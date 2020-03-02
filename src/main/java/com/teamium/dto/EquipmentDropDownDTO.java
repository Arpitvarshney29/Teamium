package com.teamium.dto;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Wrapper Class for Equipment drop-down lists
 * 
 * @author Teamium
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class EquipmentDropDownDTO {

	List<String> attachments;
	List<String> milestones;
	List<FunctionDTO> functions;
	List<String> type;
	List<String> brands;
	List<String> models;
	List<String> reference;
	List<String> locations;

	public EquipmentDropDownDTO() {
		super();
	}

	/**
	 * Parameterized drop-down constructor
	 * 
	 * @param attachments
	 * @param milestones
	 * @param functions
	 * @param type
	 * @param brands
	 * @param models
	 * @param reference
	 * @param locations
	 */
	public EquipmentDropDownDTO(List<String> attachments, List<String> milestones, List<FunctionDTO> functions,
			List<String> type, List<String> brands, List<String> models, List<String> reference,
			List<String> locations) {
		this.attachments = attachments;
		this.milestones = milestones;
		this.functions = functions;
		this.type = type;
		this.brands = brands;
		this.models = models;
		this.reference = reference;
		this.locations = locations;
	}

	/**
	 * @return the attachments
	 */
	public List<String> getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments
	 *            the attachments to set
	 */
	public void setAttachments(List<String> attachments) {
		this.attachments = attachments;
	}

	/**
	 * @return the milestones
	 */
	public List<String> getMilestones() {
		return milestones;
	}

	/**
	 * @param milestones
	 *            the milestones to set
	 */
	public void setMilestones(List<String> milestones) {
		this.milestones = milestones;
	}

	/**
	 * @return the functions
	 */
	public List<FunctionDTO> getFunctions() {
		return functions;
	}

	/**
	 * @param functions
	 *            the functions to set
	 */
	public void setFunctions(List<FunctionDTO> functions) {
		this.functions = functions;
	}

	/**
	 * @return the type
	 */
	public List<String> getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(List<String> type) {
		this.type = type;
	}

	/**
	 * @return the brands
	 */
	public List<String> getBrands() {
		return brands;
	}

	/**
	 * @param brands
	 *            the brands to set
	 */
	public void setBrands(List<String> brands) {
		this.brands = brands;
	}

	/**
	 * @return the models
	 */
	public List<String> getModels() {
		return models;
	}

	/**
	 * @param models
	 *            the models to set
	 */
	public void setModels(List<String> models) {
		this.models = models;
	}

	/**
	 * @return the reference
	 */
	public List<String> getReference() {
		return reference;
	}

	/**
	 * @param reference
	 *            the reference to set
	 */
	public void setReference(List<String> reference) {
		this.reference = reference;
	}

	/**
	 * @return the locations
	 */
	public List<String> getLocations() {
		return locations;
	}

	/**
	 * @param locations
	 *            the locations to set
	 */
	public void setLocations(List<String> locations) {
		this.locations = locations;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EquipmentDropDownDTO [milestones=" + milestones + ", functions=" + functions + ", type=" + type
				+ ", brands=" + brands + ", models=" + models + ", reference=" + reference + ", locations=" + locations
				+ "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((brands == null) ? 0 : brands.hashCode());
		result = prime * result + ((functions == null) ? 0 : functions.hashCode());
		result = prime * result + ((locations == null) ? 0 : locations.hashCode());
		result = prime * result + ((milestones == null) ? 0 : milestones.hashCode());
		result = prime * result + ((models == null) ? 0 : models.hashCode());
		result = prime * result + ((reference == null) ? 0 : reference.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EquipmentDropDownDTO other = (EquipmentDropDownDTO) obj;
		if (brands == null) {
			if (other.brands != null)
				return false;
		} else if (!brands.equals(other.brands))
			return false;
		if (functions == null) {
			if (other.functions != null)
				return false;
		} else if (!functions.equals(other.functions))
			return false;
		if (locations == null) {
			if (other.locations != null)
				return false;
		} else if (!locations.equals(other.locations))
			return false;
		if (milestones == null) {
			if (other.milestones != null)
				return false;
		} else if (!milestones.equals(other.milestones))
			return false;
		if (models == null) {
			if (other.models != null)
				return false;
		} else if (!models.equals(other.models))
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
