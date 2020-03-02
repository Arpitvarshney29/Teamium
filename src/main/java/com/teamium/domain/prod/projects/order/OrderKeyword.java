package com.teamium.domain.prod.projects.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;

@Entity
@Table(name = "t_order_keyword")
public class OrderKeyword extends AbstractEntity {

	/**
	 * Auto generated value.
	 */
	private static final long serialVersionUID = 9058527667302321788L;

	@Id
	@Column(name = "c_idorderkeyword", insertable = false, updatable = false)
	@TableGenerator(name = "idOrderKeyword_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "orderKeyword_idOrderKeyword_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idOrderKeyword_seq")
	private Long id;

	@Column(name = "c_keyword")
	private String keyword;

	@Column(name = "c_keyword_value")
	private String keywordValue;

	public OrderKeyword() {

	}

	public OrderKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
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

	/**
	 * @return the keywordValue
	 */
	public String getKeywordValue() {
		return keywordValue;
	}

	/**
	 * @param keywordValue
	 *            the keywordValue to set
	 */
	public void setKeywordValue(String keywordValue) {
		this.keywordValue = keywordValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((keyword == null) ? 0 : keyword.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderKeyword other = (OrderKeyword) obj;
		if (keyword == null) {
			if (other.keyword != null)
				return false;
		} else if (!keyword.equals(other.keyword))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrderKeyword [id=" + id + ", keyword=" + keyword + ", keywordValue=" + keywordValue + "]";
	}
}
