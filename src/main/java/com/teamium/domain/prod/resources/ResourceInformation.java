/**
 * 
 */
package com.teamium.domain.prod.resources;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.AbstractXmlEntity;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.XmlEntity;

/**
 * @author slopes
 * @version 6 The equipment information
 *
 */
@Entity
@Table(name = "t_resource_information")
public class ResourceInformation extends AbstractEntity implements Comparable<ResourceInformation> {
	/**
	 * The generated serial UID
	 */
	private static final long serialVersionUID = 156402830839921618L;
	@Id
	@Column(name = "c_id", insertable = false, updatable = false)
	@TableGenerator(name = "idEquipementInformation_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "equipment_information", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idEquipementInformation_seq")
	private Long id;

	/**
	 * function-Id
	 */
	@Column(name = "c_function_id")
	private Long functionId;

	/**
	 * The equipment information type
	 */
	@Embedded
	@AttributeOverride(name = "key", column = @Column(name = "c_type"))
	private ResourceInformationKey type;

	/**
	 * The description
	 */
	@Column(name = "c_description")
	private String description;

	@Column(name = "c_function_keyword_value")
	private String keywordValue;

	@Column(name = "c_function_key_value")
	private String keyValue;

	@ManyToOne
	@JoinColumn(name = "c_resource")
	private Resource<?> resource;

	/**
	 * Getting the id
	 */
	@Override
	public Long getId() {
		return this.id;
	}

	/**
	 * @param id The id to set
	 */
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the equipmentInformationType
	 */
	public ResourceInformationKey getType() {
		return type;
	}

	/**
	 * @param equipmentInformationType the equipmentInformationType to set
	 */
	public void setType(ResourceInformationKey equipmentInformationType) {
		this.type = equipmentInformationType;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
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
	 * @param keywordValue the keywordValue to set
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
	 * @param keyValue the keyValue to set
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
	 * @param functionId the functionId to set
	 */
	public void setFunctionId(Long functionId) {
		this.functionId = functionId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ResourceInformation [id=" + id + ", type=" + type + ", description=" + description + ", keywordValue="
				+ keywordValue + ", keyValue=" + keyValue + "]";
	}

	/**
	 * Compare the type
	 * 
	 * @param section The section
	 * @return The comparison value
	 */
	@Override
	public int compareTo(ResourceInformation o) {
		int compare = 0;

		if (o == null) {
			return -1;
		}

		// Type
		if (compare == 0) {
			if (this.type != null) {
				if (o.type == null) {
					compare = -1;
				} else {
					compare = this.type.compareTo(o.type);
				}
			} else {
				if (o.type == null) {
					compare = 1;
				}
			}
		}

		// Id
		if (compare == 0) {
			if (this.id != null) {
				if (o.id == null) {
					compare = -1;
				} else {
					compare = this.id.compareTo(o.id);
				}
			} else {
				if (o.id == null) {
					compare = 1;
				}
			}
		}

		return compare;
	}

	/**
	 * @return the resource
	 */
	public Resource<?> getResource() {
		return resource;
	}

	/**
	 * @param resource the resource to set
	 */
	public void setResource(Resource<?> resource) {
		this.resource = resource;
	}

}
