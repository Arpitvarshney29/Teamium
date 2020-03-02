package com.teamium.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.Skill;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.resources.staff.StaffMemberSkill;

/**
 * Wrapper class for staff-member-skill
 * 
 * @author Teamium
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class StaffMemberSkillDTO {

	private Long id;
	private String domain;
	private Integer rating;

	public StaffMemberSkillDTO() {

	}

	public StaffMemberSkillDTO(StaffMemberSkill entity) {
		this.id = entity.getId();
		Skill domain = entity.getSkill();
		if (domain != null) {
			this.domain = domain.getName();
		}
		this.rating = entity.getRating();
	}

	public StaffMemberSkill getStaffMemberSkill(StaffMemberSkill staffMemberSkill) {
		staffMemberSkill.setId(this.getId());
		staffMemberSkill.setRating(rating);
		return staffMemberSkill;
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
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @param domain
	 *            the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * @return the rating
	 */
	public Integer getRating() {
		return rating;
	}

	/**
	 * @param rating
	 *            the rating to set
	 */
	public void setRating(Integer rating) {
		int starRating = rating == null ? 0 : rating.intValue();
		this.rating = (starRating < 0) ? 0 : starRating > 5 ? 5 : starRating;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StaffMemberSkillDTO [id=" + id + ", domain=" + domain + ", rating=" + rating + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
		StaffMemberSkillDTO other = (StaffMemberSkillDTO) obj;
		if (domain == null) {
			if (other.domain != null)
				return false;
		} else if (!domain.equals(other.domain))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
