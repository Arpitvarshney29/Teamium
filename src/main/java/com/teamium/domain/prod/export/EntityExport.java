package com.teamium.domain.prod.export;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.teamium.domain.AbstractEntity;

/**
 * Represent an export for a selected entity
 * @author dolivier - NovaRem
 * @since v6
 */
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class EntityExport extends ExportFile {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = -4527292454964967597L;
	
	/**
	 * Entity for export
	 */
	@XmlTransient
	private AbstractEntity entity;

	/**
	 * Return the entity
	 */
	public AbstractEntity getEntity() {
		return entity;
	}

	/**
	 * Update the entity
	 */
	public void setEntity(AbstractEntity entity) {
		this.entity = entity;
	}

}
