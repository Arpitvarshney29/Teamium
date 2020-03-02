package com.teamium.domain.prod.resources.staff;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;

/**
 * Entity class for staff-email table
 */
@Entity
@Table(name = "t_staff_email")
public class StaffEmail extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5471033801813680089L;

	@Id
	@Column(name = "c_idstaffemail", insertable = false, updatable = false)
	@TableGenerator(name = "idStaffEmail_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "staffemail_idStaffEmail_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idStaffEmail_seq")
	private Long id;

	@Column(name = "c_email")
	private String email;

	@Column(name = "c_primary_email")
	private boolean primaryEmail;

	public StaffEmail() {
	}

	public StaffEmail(String email,boolean primaryEmail) {
		this.email=email;
		this.primaryEmail=primaryEmail;
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the primaryEmail
	 */
	public boolean isPrimaryEmail() {
		return primaryEmail;
	}

	/**
	 * @param primaryEmail
	 *            the primaryEmail to set
	 */
	public void setPrimaryEmail(boolean primaryEmail) {
		this.primaryEmail = primaryEmail;
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
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		StaffEmail other = (StaffEmail) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
