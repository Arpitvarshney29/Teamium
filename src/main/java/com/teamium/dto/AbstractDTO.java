package com.teamium.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.AbstractEntity;

/**
 * Wrapper class for Abstract-Entity
 * 
 * @author Teamium
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AbstractDTO {

	private Long id;
	private Integer version;

	public AbstractDTO() {
		super();
	}

	public AbstractDTO(Long id, Integer version) {
		this.id = id;
		this.version = version;
	}

	public AbstractDTO(AbstractEntity entity) {
		if (entity != null) {
			this.id = entity.getId();
			this.version = entity.getVersion();
		}
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
	 * @return the version
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * Get abstract Entity Object from DTO
	 * 
	 * @param abstractEntity
	 * @return AbstractEntity
	 */
	public AbstractEntity getAbstractEntityDetails(AbstractEntity abstractEntity) {
		abstractEntity.setId(id);
		abstractEntity.setVersion(version);
		return abstractEntity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AbstractDTO [id=" + id + ", version=" + version + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	/*@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractDTO other = (AbstractDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}*/

}
