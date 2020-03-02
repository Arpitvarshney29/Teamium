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
@Table(name = "t_category")
public class Category {

	/**
	 * Category ID
	 */
	@Id
	@Column(name = "c_category_id", insertable = false, updatable = false)
	@TableGenerator(name = "idCategory_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "category_idCategory_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idCategory_seq")
	private Long id;

	/**
	 * Name of the Category.
	 */
	@Column(name = "c_name", unique = true)
	@NotNull
	private String name;

	public Category() {
	}

	public Category(String name) {
		this.name = name;
	}

	public Category(Long id, String name) {
		if (id != null) {
			this.id = id;
		}
		this.name = name;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Group [id=" + id + ", name=" + name + "]";
	}

}
