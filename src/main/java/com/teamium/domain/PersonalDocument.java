package com.teamium.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * Entity class for personal document table
 * 
 * @author Hansraj
 *
 */
@Entity
@Table(name = "t_personal_document")
public class PersonalDocument extends AbstractEntity {

	private static final long serialVersionUID = 5971859229138080871L;

	@Id
	@Column(name = "c_idpersonaldocument", insertable = false, updatable = false)
	@TableGenerator(name = "idPersonalDocument_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "document_iddocument_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idPersonalDocument_seq")
	private Long id;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "c_iddocumenttype", nullable = false)
	private DocumentType type;

	@Column(name = "c_number")
	private String number;

	@Column(name = "c_issue_date")
	private Calendar issueDate;

	@Column(name = "c_expiration_date")
	private Calendar expirationDate;
	
	@ManyToOne
	@JoinColumn(name = "c_idperson")
	private Person documentPerson;

	public PersonalDocument() {
		super();
	}

	public PersonalDocument(DocumentType type, String number, Calendar issueDate, Calendar expirationDate) {
		super();
		this.type = type;
		this.number = number;
		this.issueDate = issueDate;
		this.expirationDate = expirationDate;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the type
	 */
	public DocumentType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(DocumentType type) {
		this.type = type;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the issueDate
	 */
	public Calendar getIssueDate() {
		return issueDate;
	}

	/**
	 * @param issueDate
	 *            the issueDate to set
	 */
	public void setIssueDate(Calendar issueDate) {
		this.issueDate = issueDate;
	}

	/**
	 * @return the expirationDate
	 */
	public Calendar getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expirationDate
	 *            the expirationDate to set
	 */
	public void setExpirationDate(Calendar expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	

	/**
	 * @return the documentPerson
	 */
	public Person getDocumentPerson() {
		return documentPerson;
	}

	/**
	 * @param documentPerson the documentPerson to set
	 */
	public void setDocumentPerson(Person documentPerson) {
		this.documentPerson = documentPerson;
	}

	


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
		PersonalDocument other = (PersonalDocument) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PersonalDocument [id=" + id + ", type=" + type + ", number=" + number + ", issueDate=" + issueDate
				+ ", expirationDate=" + expirationDate + "]";
	}

}
