package com.teamium.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * Entity class for Function-Keyword
 * 
 * @author Teamium
 *
 */
@Entity
@Table(name = "t_function_keyword")
public class FunctionKeyword extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4927474263067608811L;

	/**
	 * Id
	 */
	@Id
	@Column(name = "c_idfunctionkeyword", insertable = false, updatable = false)
	@TableGenerator(name = "idFunctionKeyword_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "function_keyword_idkeyword_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idFunctionKeyword_seq")
	private Long id;

	@Column(name = "c_keyword", unique = true)
	private String keyword;

	/**
	 * Function keyword keys-list
	 */
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "c_idfunctionkeyword", nullable = true)
	private List<FunctionKeys> keysList = new ArrayList<>();

	public FunctionKeyword() {

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
	 * @return the keysList
	 */
	public List<FunctionKeys> getKeysList() {
		return keysList;
	}

	/**
	 * @param keysList
	 *            the keysList to set
	 */
	public void setKeysList(List<FunctionKeys> keysList) {
		this.keysList = keysList;
	}

	/**
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * @param keyword
	 *            the keyword to set
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
