package com.teamium.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class WidgetDataDTO {
	private String function;
	private String resource;
	private String projectTeamId;
	private String projectname;
	private String type;
	private String projectDate;
	private String numberOfDays;
	private String startTime;

	public WidgetDataDTO() {

	}

	public WidgetDataDTO(String function, String resource, String projectTeamId, String projectname, String type,
			String projectDate, String numberOfDays) {
		this.function = function;
		this.resource = resource;
		this.projectTeamId = projectTeamId;
		this.projectname = projectname;
		this.type = type;
		this.projectDate = projectDate;
		this.numberOfDays = numberOfDays;
	}

	/**
	 * 
	 * @return
	 */
	public String getFunction() {
		return function;
	}

	/**
	 * 
	 * @param function
	 */
	public void setFunction(String function) {
		this.function = function;
	}

	/**
	 * 
	 * @return
	 */
	public String getResource() {
		return resource;
	}

	/**
	 * 
	 * @param resource
	 */
	public void setResource(String resource) {
		this.resource = resource;
	}

	/**
	 * 
	 * @return
	 */
	public String getProjectTeamId() {
		return projectTeamId;
	}

	/**
	 * 
	 * @param projectTeamId
	 */
	public void setProjectTeamId(String projectTeamId) {
		this.projectTeamId = projectTeamId;
	}

	/**
	 * 
	 * @return
	 */
	public String getProjectname() {
		return projectname;
	}

	/**
	 * 
	 * @param projectname
	 */
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	/**
	 * 
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 
	 * @return
	 */
	public String getProjectDate() {
		return projectDate;
	}

	/**
	 * 
	 * @param projectDate
	 */
	public void setProjectDate(String projectDate) {
		this.projectDate = projectDate;
	}

	/**
	 * 
	 * @return
	 */
	public String getNumberOfDays() {
		return numberOfDays;
	}

	/**
	 * 
	 * @param numberOfDays
	 */
	public void setNumberOfDays(String numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	/**
	 * 
	 * @return
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * 
	 * @param startTime
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

}
