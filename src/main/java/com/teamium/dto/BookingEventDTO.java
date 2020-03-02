package com.teamium.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teamium.constants.Constants;
import com.teamium.domain.prod.projects.AbstractProject;
import com.teamium.domain.prod.projects.BookingEvent;
import com.teamium.domain.prod.resources.functions.RatedFunction;
import com.teamium.utils.CommonUtil;
import com.teamium.utils.FunctionUtil;

public class BookingEventDTO extends EventDTO {

	private Long bookingId;

	private Long functionId;

	private String functionType;

	private boolean collapse;
	private Long projectId;

	private List<BookingEventDTO> linkedEvents = new ArrayList<BookingEventDTO>();

	private String status;
	private Long cloneEventId;
	private boolean manualUpdate;
	private String projectStatus;
	private String projectTitle;
	private String projectDate;
	private String functionName;

	public BookingEventDTO() {
		super();
	}

	/**
	 * @param event
	 */
	public BookingEventDTO(BookingEvent event) {
		super(event);
		if (event.getOrigin() != null) {
			this.bookingId = event.getOrigin().getId();
			if (event.getOrigin().getRecord() != null) {
				this.projectId = event.getOrigin().getRecord().getId();
			}
			RatedFunction fun = event.getOrigin().getFunction();
			if (fun != null) {
				this.functionId = fun.getId();
				this.functionType = FunctionUtil.getFunctionType(fun);
			}
			this.manualUpdate = event.getOrigin().getSyncQty();
			if (event.getOrigin().getRecord() != null && event.getOrigin().getRecord().getStatus() != null) {
				this.projectStatus = event.getOrigin().getRecord().getStatus().getKey();
			}
		}
		if (event.getStatus() != null) {
			this.status = event.getStatus().getKey();
		}
		if (event.getLinkedEvents() != null) {
			this.linkedEvents = event.getLinkedEvents().stream().map(BookingEventDTO::new).collect(Collectors.toList());
		}
	}

	/**
	 * @param event
	 */
	public BookingEventDTO(BookingEvent event, Boolean forConflict) {
		super(event, forConflict);
		if (event.getOrigin() != null) {
			this.bookingId = event.getOrigin().getId();
			if (event.getOrigin().getRecord() != null) {
				this.projectId = event.getOrigin().getRecord().getId();
			}
			RatedFunction fun = event.getOrigin().getFunction();
			if (fun != null) {
				this.functionId = fun.getId();
				this.functionType = FunctionUtil.getFunctionType(fun);
				this.functionName = fun.getQualifiedName();
			}
			if (event.getOrigin().getRecord() != null && event.getOrigin().getRecord().getStatus() != null) {
				AbstractProject project = (AbstractProject) event.getOrigin().getRecord();
				this.projectTitle = project.getTitle();
				this.projectStatus = project.getStatus().getKey();
				this.projectDate = CommonUtil.datePattern(Constants.DATE_PATTERN,
						event.getOrigin().getRecord().getDate());
			}
		}
	}

	/**
	 * @param id
	 * @param version
	 */
	public BookingEventDTO(Long id, Integer version) {
		super(id, version);

	}

	/**
	 * To get booking event entity from DTO
	 * 
	 * @param event
	 * @return BookingEvent
	 */
	@JsonIgnore
	public BookingEvent getBookingEvent(BookingEvent event) {
		super.getEvent(event);
		return event;
	}

	/**
	 * To get booking event entity from DTO
	 * 
	 * @return BookingEvent
	 */
	@JsonIgnore
	public BookingEvent getBookingEvent() {
		BookingEvent event = new BookingEvent();
		return this.getBookingEvent(event);
	}

	/**
	 * @return the bookingId
	 */
	public Long getBookingId() {
		return bookingId;
	}

	/**
	 * @param bookingId the bookingId to set
	 */
	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
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
	 * @return the collapse
	 */
	public boolean isCollapse() {
		return collapse;
	}

	/**
	 * @param collapse the collapse to set
	 */
	public void setCollapse(boolean collapse) {
		this.collapse = collapse;
	}

	/**
	 * @return the projectId
	 */
	public Long getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return the linkedEvents
	 */
	public List<BookingEventDTO> getLinkedEvents() {
		return linkedEvents;
	}

	/**
	 * @param linkedEvents the linkedEvents to set
	 */
	public void setLinkedEvents(List<BookingEventDTO> linkedEvents) {
		this.linkedEvents = linkedEvents;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the cloneEventId
	 */
	public Long getCloneEventId() {
		return cloneEventId;
	}

	/**
	 * @param cloneEventId the cloneEventId to set
	 */
	public void setCloneEventId(Long cloneEventId) {
		this.cloneEventId = cloneEventId;
	}

	/**
	 * @return the manualUpdate
	 */
	public boolean isManualUpdate() {
		return manualUpdate;
	}

	/**
	 * @param manualUpdate the manualUpdate to set
	 */
	public void setManualUpdate(boolean manualUpdate) {
		this.manualUpdate = manualUpdate;
	}

	/**
	 * @return the projectStatus
	 */
	public String getProjectStatus() {
		return projectStatus;
	}

	/**
	 * @param projectStatus the projectStatus to set
	 */
	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	/**
	 * @return the projectTitle
	 */
	public String getProjectTitle() {
		return projectTitle;
	}

	/**
	 * @param projectTitle the projectTitle to set
	 */
	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}

	/**
	 * @return the projectDate
	 */
	public String getProjectDate() {
		return projectDate;
	}

	/**
	 * @param projectDate the projectDate to set
	 */
	public void setProjectDate(String projectDate) {
		this.projectDate = projectDate;
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

}
