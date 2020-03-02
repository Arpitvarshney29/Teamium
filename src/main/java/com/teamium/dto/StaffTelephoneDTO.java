package com.teamium.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.prod.resources.staff.StaffTelephone;

/**
 * Wrapper class for staff-telephone
 * 
 * @author Teamium
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class StaffTelephoneDTO {

	private Long id;
	private String telephone;
	private boolean primaryTelephone = false;

	public StaffTelephoneDTO() {

	}

	public StaffTelephoneDTO(StaffTelephone entity) {
		this.id = entity.getId();
		this.telephone = entity.getTelephone();
		this.primaryTelephone = entity.isPrimaryTelephone();
	}

	public StaffTelephone getStaffTelephone(StaffTelephone staffTelephone) {
		staffTelephone.setId(this.id);
		staffTelephone.setVersion(staffTelephone.getVersion());
		staffTelephone.setPrimaryTelephone(this.primaryTelephone);
		return staffTelephone;
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
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param telephone
	 *            the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * @return the primaryTelephone
	 */
	public boolean isPrimaryTelephone() {
		return primaryTelephone;
	}

	/**
	 * @param primaryTelephone
	 *            the primaryTelephone to set
	 */
	public void setPrimaryTelephone(boolean primaryTelephone) {
		this.primaryTelephone = primaryTelephone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StaffTelephoneDTO [id=" + id + ", telephone=" + telephone + ", primaryTelephone=" + primaryTelephone
				+ "]";
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((telephone == null) ? 0 : telephone.hashCode());
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
		StaffTelephoneDTO other = (StaffTelephoneDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (telephone == null) {
			if (other.telephone != null)
				return false;
		} else if (!telephone.equals(other.telephone))
			return false;
		return true;
	}

}
