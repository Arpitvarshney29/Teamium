package com.teamium.dto;

/**
 * Wrapper class for callsheet
 * 
 * @author Hansraj
 *
 */
public class CallsheetDTO {

	private long projectId;
	private long contactId;
	private String orgnization;
	private String location;
	private String comment;

	/**
	 * @return the projectId
	 */
	public long getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return the contactId
	 */
	public long getContactId() {
		return contactId;
	}

	/**
	 * @param contactId the contactId to set
	 */
	public void setContactId(long contactId) {
		this.contactId = contactId;
	}

	/**
	 * @return the orgnization
	 */
	public String getOrgnization() {
		return orgnization;
	}

	/**
	 * @param orgnization the orgnization to set
	 */
	public void setOrgnization(String orgnization) {
		this.orgnization = orgnization;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CallsheetDTO [projectId=" + projectId + ", contactId=" + contactId + ", orgnization=" + orgnization
				+ ", location=" + location + ", comment=" + comment + "]";
	}

}
