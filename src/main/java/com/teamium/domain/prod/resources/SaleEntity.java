package com.teamium.domain.prod.resources;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;

import com.teamium.domain.Company;

/**
 * Describe a sale entity
 * @author sraybaud - NovaRem
 *
 */
@Entity
@DiscriminatorValue("entity")
@NamedQuery(name=SaleEntity.QUERY_findAll, query="SELECT e From SaleEntity e WHERE e.archived != TRUE ORDER BY e.name ASC")
public class SaleEntity extends Company{
	
	/**
	 * Archived entity
	 */
	@Column(name="c_archived")
	private Boolean archived = Boolean.FALSE;

	/**
	 * Class uid
	 */
	private static final long serialVersionUID = -1745895812580636859L;
	
	/**
	 * Name of the query returning all recorded directors
	 * @return the directors' list
	 */
	public static final String QUERY_findAll = "findAllSaleEntities";
	
	/**
	 * Edition group
	 */
	@Column(name="c_entity_editiongroup")
	private String editionGroup;
	
	/**
	 * Validation threshold
	 */
	@Column(name="c_entity_validation_threshold")
	private Float validationThreshold;
	
	/**
	 * Edition group
	 */
	public String getEditionGroup(){
		return editionGroup;
	}

	/**
	 * @param editionGroup the editionGroup to set
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
	 * @param validationThreshold the validationThreshold to set
	 */
	public void setValidationThreshold(Float validationThreshold) {
		this.validationThreshold = validationThreshold;
	}

	/**
	 * @return the archived
	 */
	public Boolean getArchived() {
		return archived;
	}

	/**
	 * @param archived the archived to set
	 */
	public void setArchived(Boolean archived) {
		this.archived = archived;
	}	
}
