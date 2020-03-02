package com.teamium.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "t_function_keys")
public class FunctionKeys extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3515536418274620715L;

	@Id
	@Column(name = "c_idfunctionkey", insertable = false, updatable = false)
	@TableGenerator(name = "idFunctionKey_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "function_keys_idfunctionkey_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idFunctionKey_seq")
	private Long id;

	/**
	 * key-value
	 */
	@Column(name = "c_key_value")
	private String keyValue;

	public FunctionKeys() {

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
	 * @return the keyValue
	 */
	public String getKeyValue() {
		return keyValue;
	}

	/**
	 * @param keyValue
	 *            the keyValue to set
	 */
	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

}
