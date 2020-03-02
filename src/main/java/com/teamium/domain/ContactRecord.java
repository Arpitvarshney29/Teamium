package com.teamium.domain;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.teamium.domain.prod.Record;

/**
 * Describes the role of a given contact in a project
 * 
 * @author sraybaud
 * @version TEAM-18
 *
 */
@Entity
@Table(name = "t_contact_record")
public class ContactRecord implements Serializable {

	/**
	 * CLass UID
	 */
	private static final long serialVersionUID = -7092894598677293081L;

	/**
	 * Contact record id
	 */
	@EmbeddedId
	private ContactRecordId id;

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((person == null) ? 0 : person.hashCode());
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
		if (obj == null)
			return false;
		if (!(obj instanceof ContactRecord))
			return false;
		ContactRecord other = (ContactRecord) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (person == null) {
			if (other.person != null)
				return false;
		} else if (!person.equals(other.person))
			return false;
		return true;
	}

	/**
	 * Role
	 * 
	 * @see com.teamium.domain.prod.resources.contact.RoleContact
	 */
	@Embedded
	@AttributeOverride(name = "key", column = @Column(name = "c_role"))
	private XmlEntity role;

	/**
	 * Person
	 */
	@ManyToOne
	@JoinColumn(name = "c_idperson", insertable = false, updatable = false)
	private Person person;

	/**
	 * @return the role
	 */
	public XmlEntity getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(XmlEntity role) {
		this.role = role;
	}

	/**
	 * @return the person
	 */
	public Person getPerson() {
		return person;
	}

	/**
	 * @param person
	 *            the person to set
	 */
	public void setPerson(Person person) {
		this.person = person;
		if (this.id == null)
			id = new ContactRecordId();
		id.setIdPerson(person.getId());
	}

	/**
	 * @param record
	 *            the record to set
	 */
	public void setRecord(Record record) {
		if (this.id == null)
			id = new ContactRecordId();
		id.setIdRecord(record.getId());
	}
	
	public Long getRecord() {
		if (id != null) {
			return id.getIdRecord();
		}
		return null;
	}

	/**
	 * Clone the current object
	 * 
	 * @return the clone
	 * @throws CloneNotSupportedException
	 */
	@Override
	public ContactRecord clone() throws CloneNotSupportedException {
		return (ContactRecord) super.clone();
	}

	/**
	 * String description of a instance
	 */
	@Override
	public String toString() {
		return this.person + " as " + role + (id != null ? " on record#" + id.getIdRecord() : "");
	}

}
