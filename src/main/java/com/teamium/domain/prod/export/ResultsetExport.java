package com.teamium.domain.prod.export;

import java.util.List;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represent an export for a list of ids
 * @author dolivier - NovaRem
 * @since v6
 */
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ResultsetExport extends ExportFile {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 4555000912673617484L;
	
	/**
	 * List of entities
	 */
	private List<Long> entities;

	/**
	 * @return the entities
	 */
	public List<Long> getEntities() {
		return entities;
	}

	/**
	 * @param entities the entities to set
	 */
	public void setEntities(List<Long> entities) {
		this.entities = entities;
	}

}
