package com.teamium.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * Entity class for document type table
 * 
 * @author Hansraj
 *
 */
@Entity
@Table(name = "t_document_type")
@NamedQuery(name = DocumentType.QUERY_findAll, query = "SELECT d FROM DocumentType d")
public class DocumentType {

	@Id
	@Column(name = "c_iddocumenttype", insertable = false, updatable = false)
	@TableGenerator(name = "idDocument_type_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "idDocument_type_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idDocument_type_seq")
	private Long id;

	@Column(name = "c_type", unique = true)
	private String type;

	/**
	 * Find all document types
	 * 
	 */
	public static final String QUERY_findAll = "findDocumentType";

	public DocumentType(String type) {
		this.type = type;
	}

	public DocumentType() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentType other = (DocumentType) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DocumentType [id=" + id + ", type=" + type + "]";
	}

}
