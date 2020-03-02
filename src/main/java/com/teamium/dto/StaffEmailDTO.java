package com.teamium.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.prod.resources.staff.StaffEmail;

/**
 * Wrapper class for staff-email
 * 
 * @author Teamium
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class StaffEmailDTO {

	private Long id;
	private String email;
	private boolean primaryEmail = false;

	public StaffEmailDTO() {

	}

	public StaffEmailDTO(StaffEmail entity) {
		this.id = entity.getId();
		this.email = entity.getEmail();
		this.primaryEmail = entity.isPrimaryEmail();
	}

	public StaffEmail getStaffEmail(StaffEmail staffEmail) {
		staffEmail.setId(this.getId());
		staffEmail.setPrimaryEmail(primaryEmail);
		return staffEmail;
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StaffEmailDTO [id=" + id + ", email=" + email + ", primaryEmail=" + primaryEmail + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StaffEmailDTO other = (StaffEmailDTO) obj;
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

	// /*
	// * (non-Javadoc)
	// *
	// * @see java.lang.Object#hashCode()
	// */
	// @Override
	// public int hashCode() {
	// final int prime = 31;
	// int result = 1;
	// result = prime * result + ((email == null) ? 0 : email.hashCode());
	// return result;
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see java.lang.Object#equals(java.lang.Object)
	// */
	// @Override
	// public boolean equals(Object obj) {
	// if (this == obj)
	// return true;
	// if (obj == null)
	// return false;
	// if (getClass() != obj.getClass())
	// return false;
	// StaffEmailDTO other = (StaffEmailDTO) obj;
	// if (email == null) {
	// if (other.email != null)
	// return false;
	// } else if (!email.equals(other.email))
	// return false;
	// return true;
	// }

}
