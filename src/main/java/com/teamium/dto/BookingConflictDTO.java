package com.teamium.dto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.constants.Constants;
import com.teamium.domain.prod.projects.planning.Event;
import com.teamium.domain.prod.resources.Resource;
import com.teamium.utils.CommonUtil;

/**
 * Wrapper class for Booking-Conflict
 * 
 * @author Himanshu
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BookingConflictDTO {

	/**
	 * Temp id
	 */
	private Integer tempId;

	/**
	 * List of conflicting events
	 */
	private List<EventDTO> events;

	/**
	 * Entity represent the state of conflict
	 */
	private String status;

	/**
	 * Start period of conflict
	 */
	private Calendar start;

	/**
	 * The conflict start in string
	 */
	private String conflictStartString;

	/**
	 * End period of conflict
	 */
	private Calendar end;

	/**
	 * ResourceId for the conflict
	 */
	private long resourceId;

	/**
	 * Resource-Name for the conflict
	 */
	private String resourceName;

	/**
	 * title of the project
	 */
	private Long functionId;

	/**
	 * Type of the function
	 */
	private String functionType;

	/**
	 * the name of the function
	 */
	private String functionName;

	/**
	 * the duration of the date period
	 */
	private String period;

	public BookingConflictDTO() {

	}

	public BookingConflictDTO(Integer tempId, BookingEventDTO event) {
		this.tempId = tempId;
		this.status = event.getProjectStatus();
		this.start = event.getFrom();
		this.end = event.getTo();
		this.conflictStartString = CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, event.getFrom());
		this.resourceId = event.getResource();
		this.resourceName = event.getResourceName();
		this.functionId = event.getFunctionId();
		this.functionType = event.getFunctionType();
		this.functionName = event.getFunctionName();
	}

	/**
	 * @return the bookings
	 */
	public List<EventDTO> getEvents() {
		if (events == null) {
			events = new ArrayList<EventDTO>();
		}
		return events;
	}

	/**
	 * @param events the events to set
	 */
	public void setEvents(List<EventDTO> events) {
		this.events = events;
	}

	/**
	 * @return the entity
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the tempId
	 */
	public Integer getTempId() {
		return tempId;
	}

	/**
	 * @param tempId the tempId to set
	 */
	public void setTempId(Integer tempId) {
		this.tempId = tempId;
	}

	/**
	 * @return the start
	 */
	public Calendar getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(Calendar start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public Calendar getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(Calendar end) {
		this.end = end;
	}

	/**
	 * @return the resourceId
	 */
	public long getResourceId() {
		return resourceId;
	}

	/**
	 * @param resourceId the resourceId to set
	 */
	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
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
	 * @return the conflictStartString
	 */
	public String getConflictStartString() {
		return conflictStartString;
	}

	/**
	 * @param conflictStartString the conflictStartString to set
	 */
	public void setConflictStartString(String conflictStartString) {
		this.conflictStartString = conflictStartString;
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
	 * @return the functionName
	 */
	public String getFunctionName() {
		return functionName;
	}

	/**
	 * @param functionName the functionName to set
	 */
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	/**
	 * @return the resourceName
	 */
	public String getResourceName() {
		return resourceName;
	}

	/**
	 * @param resourceName the resourceName to set
	 */
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	/**
	 * @return the period
	 */
	public String getPeriod() {
		return period;
	}

	/**
	 * @param period the period to set
	 */
	public void setPeriod(String period) {
		this.period = period;
	}

}
