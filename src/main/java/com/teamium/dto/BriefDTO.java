package com.teamium.dto;

import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.projects.Brief;

/**
 * DTO Class for ResourceInformation Entity
 * 
 * @author wittybrains
 *
 */
public class BriefDTO extends AbstractDTO {

	private Long id;
	private String briefType;
	private String description;

	public BriefDTO() {
		super();
	}

	public BriefDTO(Brief entity) {
		super(entity);
		this.id = entity.getId();
		this.setVersion(entity.getVersion());
		this.description = entity.getDescription();
		if (entity.getBriefType() != null) {
			this.briefType = entity.getBriefType().getKey();
		}
	}

	/**
	 * Get Brief entity from DTO
	 * 
	 * @param brief
	 */
	public Brief getResourceInformation(Brief entity) {
		entity.setId(this.getId());
		entity.setVersion(this.getVersion());
		entity.setDescription(this.getDescription());
		if (entity.getBriefType() != null) {
			XmlEntity briefTypeEntity = new XmlEntity();
			briefTypeEntity.setKey(this.getBriefType());
			entity.setBriefType(briefTypeEntity);
		}
		return entity;
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
	 * @return the briefType
	 */
	public String getBriefType() {
		return briefType;
	}

	/**
	 * @param briefType
	 *            the briefType to set
	 */
	public void setBriefType(String briefType) {
		this.briefType = briefType;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BriefDTO [id=" + id + ", briefType=" + briefType + ", description=" + description + "]";
	}

}
