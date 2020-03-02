package com.teamium.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

/**
 * To store the skills from the configuration page.
 * 
 * @author Himanshu
 *
 */
@Entity
@Table(name = "t_skill")
public class Skill extends AbstractEntity {

	@Id
	@Column(name = "c_idskill", insertable = false, updatable = false)
	@TableGenerator(name = "idskill_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "skill_idskill_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idskill_seq")
	private Long id;

	@NotNull
	@Column(name = "c_name", unique = true)
	private String name;

	public Skill() {
	}

	public Skill(String name) {
		this.name = name;
	}

	/**
	 * @return the id
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	@Override
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Skill [id=" + id + ", name=" + name + "]";
	}
}
