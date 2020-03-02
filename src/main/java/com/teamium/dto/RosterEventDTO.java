package com.teamium.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import com.teamium.domain.prod.projects.planning.roster.RosterEvent;

public class RosterEventDTO {

	private Long id;
	private String title;
	private String startTime;
	private String endTime;
	private String functionType;
	private String color;
	private int quantity;
	private Long functionId;
	private Set<ResourceDTO<?>> resourcesDto = new HashSet<ResourceDTO<?>>();
	private boolean isConflict;

	public RosterEventDTO() {

	}

	public RosterEventDTO(RosterEvent rosterEvent) {
		this.id = rosterEvent.getId();
		this.title = rosterEvent.getTitle();
		if (rosterEvent.getFrom() != null) {
			this.startTime = new DateTime(rosterEvent.getFrom().getTime()).withZone(DateTimeZone.UTC).toString();
		}
		if (rosterEvent.getTo() != null) {
			this.endTime = new DateTime(rosterEvent.getTo().getTime()).withZone(DateTimeZone.UTC).toString();
		}
		this.functionType = rosterEvent.getFunctionType();
		this.quantity = rosterEvent.getQuantity() != null ? rosterEvent.getQuantity().intValue() : 0;
		if (rosterEvent.getFunction() != null) {
			this.functionId = rosterEvent.getFunction().getId();
		}
		if (rosterEvent.getResources() != null && !rosterEvent.getResources().isEmpty()) {
			this.resourcesDto = rosterEvent.getResources().stream().filter(resource -> resource != null)
					.map(resource -> new ResourceDTO<>(resource.getId(), resource.getName()))
					.collect(Collectors.toSet());
		}
		this.color = getRosterEventTheme((int) this.quantity, (int) this.resourcesDto.size());
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the functionType
	 */
	public String getFunctionType() {
		return functionType;
	}

	/**
	 * @param functionType the functionType to set
	 */
	public void setFunctionType(String functionType) {
		this.functionType = functionType;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the functionId
	 */
	public Long getFunctionId() {
		return functionId;
	}

	/**
	 * @param functionId the functionId to set
	 */
	public void setFunctionId(Long functionId) {
		this.functionId = functionId;
	}

	/**
	 * @return the resourcesDto
	 */
	public Set<ResourceDTO<?>> getResourcesDto() {
		return resourcesDto;
	}

	/**
	 * @param resourcesDto the resourcesDto to set
	 */
	public void setResourcesDto(Set<ResourceDTO<?>> resourcesDto) {
		this.resourcesDto = resourcesDto;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * Decides color ie style class for Roster event based on quantity and selected
	 * resource.
	 * 
	 * @param quantity
	 * @param selectedResource
	 * @return
	 */
	private String getRosterEventTheme(int quantity, int selectedResource) {
		return (quantity != 0 && (selectedResource == quantity)) ? "green"
				: (quantity > selectedResource) ? "orange" : "blue";
	}

	/**
	 * @return the isConflit
	 */
	public boolean isConflict() {
		return isConflict;
	}

	/**
	 * @param isConflit the isConflit to set
	 */
	public void setConflict(boolean isConflict) {
		this.isConflict = isConflict;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RosterEventDTO [id=" + id + ", title=" + title + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", functionType=" + functionType + ", quantity=" + quantity + ", functionId=" + functionId
				+ ", resourcesId=" + resourcesDto + "]";
	}

}
