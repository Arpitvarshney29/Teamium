package com.teamium.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "t_milestone_type")
public class MilestoneType {

	/**
	 * MilestoneType ID
	 */
	@Id
	@Column(name = "c_milestone_type_id", insertable = false, updatable = false)
	@TableGenerator(name = "idMilestoneType_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "milestonetype_idMilestoneType_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idMilestoneType_seq")
	private Long id;

	/**
	 * Name of the Format.
	 */
	@Column(name = "c_name")
	@NotNull
	private String name;

	@Column(name = "c_discriminator")
	@NotNull
	private String discriminator;

	public MilestoneType() {
	}

	public MilestoneType(String name, String discriminator) {
		this.name = name;
		this.discriminator = discriminator;
	}

	public MilestoneType(Long id, String name, String discriminator) {
		if (id != null) {
			this.id = id;
		}
		this.name = name;
		this.discriminator = discriminator;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
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
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the discriminator
	 */
	public String getDiscriminator() {
		return discriminator;
	}

	/**
	 * @param discriminator the discriminator to set
	 */
	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MilestoneType [id=" + id + ", name=" + name + ", discriminator=" + discriminator + "]";
	}

}
