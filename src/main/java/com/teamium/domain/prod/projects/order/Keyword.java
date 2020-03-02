package com.teamium.domain.prod.projects.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;

@Entity
@Table(name = "t_keyword")
public class Keyword extends AbstractEntity {

	private static final long serialVersionUID = -4138978345509702813L;

	@Id
	@Column(name = "c_idkeyword", insertable = false, updatable = false)
	@TableGenerator(name = "idKeyword_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "keyword_idkeyword_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idKeyword_seq")
	private Long id;

	@Column(name = "c_key")
	@NotNull
	private String key;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

}