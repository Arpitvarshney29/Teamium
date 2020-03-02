package com.teamium.dto;

import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teamium.domain.Theme;
import com.teamium.domain.prod.projects.CompletionStatus;
import com.teamium.domain.prod.projects.planning.Event;
import com.teamium.domain.prod.resources.ResourceEntity;
import com.teamium.domain.prod.resources.equipments.Equipment;
import com.teamium.domain.prod.resources.functions.DefaultResource;
import com.teamium.domain.prod.resources.staff.StaffMember;

public class EventDTO extends AbstractDTO {

	private DateTime start;
	private DateTime end;

	private String startTime;
	private String endTime;

	private Long duration;

	private String text;

	private Float completion;

	private long resource;

	private String theme;

	private CompletionStatus completionStatus;

	private Boolean superposition;

	private boolean changeable = false;

	private String resourceUrl;

	private String resourceType;

	private int offSet;

	private boolean moveLinkedEvent = false;

	private String comment;

	private Calendar from;
	private Calendar to;
	private String resourceName;
	private String conflictStart;

	public EventDTO() {
		super();
	}

	/**
	 * @param entity
	 */
	public EventDTO(Event event) {
		super(event);

		this.duration = event.getDuration();
		if (event.getFrom() != null) {
			this.start = new DateTime(event.getFrom().getTimeInMillis());

			DateTime dt = new DateTime(event.getFrom().getTime()).withZone(DateTimeZone.UTC);
			this.startTime = dt.toString();
		}
		if (event.getTo() != null) {
			this.end = new DateTime(event.getTo().getTimeInMillis());

			DateTime dt = new DateTime(event.getTo().getTime()).withZone(DateTimeZone.UTC);
			this.endTime = dt.toString();
		}
		if (event.getResource() != null) {
			this.resource = event.getResource().getId() == null ? 0 : event.getResource().getId();
			ResourceEntity resourceEntity = event.getResource().getEntity();
			if (resourceEntity instanceof StaffMember) {
				StaffMember staff = (StaffMember) resourceEntity;
				if (staff != null && staff.getPhoto() != null && staff.getPhoto().getUrl() != null) {
					this.resourceUrl = staff.getPhoto().getUrl().toString();
				}
				this.resourceType = "Staff";
			}
			if (resourceEntity instanceof Equipment) {
				Equipment equipment = (Equipment) resourceEntity;
				if (equipment != null && equipment.getPhoto() != null && equipment.getPhoto().getUrl() != null) {
					this.resourceUrl = equipment.getPhoto().getUrl().toString();
				}
				this.resourceType = "Equipment";
			}
		}
		this.text = event.getTitle() != null ? event.getTitle() : "";
		if (event.getTheme() != null) {
			this.theme = event.getTheme().getKey();
		}
		this.completion = event.getCompletion();
		this.comment = event.getComment();

	}

	public EventDTO(Event event, Boolean forConflict) {
		super(event);
		if (event.getFrom() != null) {
			this.from = event.getFrom();
			this.start = new DateTime(event.getFrom().getTimeInMillis());
			DateTime dt = new DateTime(event.getFrom().getTime()).withZone(DateTimeZone.UTC);
			this.startTime = dt.toString();
		}

		if (event.getTo() != null) {
			this.to = event.getTo();
			this.end = new DateTime(event.getTo().getTimeInMillis());
			DateTime dt = new DateTime(event.getTo().getTime()).withZone(DateTimeZone.UTC);
			this.endTime = dt.toString();
		}

		if (event.getResource() != null) {
			if (!(event.getResource() instanceof DefaultResource)) {
				this.resourceName = event.getResource().getName();
			}
			this.resource = event.getResource().getId() == null ? 0 : event.getResource().getId();
			ResourceEntity resourceEntity = event.getResource().getEntity();
			if (resourceEntity instanceof StaffMember) {
				this.resourceType = "Staff";
			}
			if (resourceEntity instanceof Equipment) {
				this.resourceType = "Equipment";
			}
		}
	}

	/**
	 * @param id
	 * @param version
	 */
	public EventDTO(Long id, Integer version) {
		super(id, version);

	}

	/**
	 * @return the duration
	 */
	public Long getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(Long duration) {
		this.duration = duration;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the completion
	 */
	public Float getCompletion() {
		return completion;
	}

	/**
	 * @param completion the completion to set
	 */
	public void setCompletion(Float completion) {
		this.completion = completion;
	}

	/**
	 * @return the resource
	 */
	public long getResource() {
		return resource;
	}

	/**
	 * @param resource the resource to set
	 */
	public void setResource(long resource) {
		this.resource = resource;
	}

	/**
	 * @return the completionStatus
	 */
	public CompletionStatus getCompletionStatus() {
		return completionStatus;
	}

	/**
	 * @param completionStatus the completionStatus to set
	 */
	public void setCompletionStatus(CompletionStatus completionStatus) {
		this.completionStatus = completionStatus;
	}

	/**
	 * @return the superposition
	 */
	public Boolean getSuperposition() {
		return superposition;
	}

	/**
	 * @param superposition the superposition to set
	 */
	public void setSuperposition(Boolean superposition) {
		this.superposition = superposition;
	}

	/**
	 * @return the theme
	 */
	public String getTheme() {
		return theme;
	}

	/**
	 * @param theme the theme to set
	 */
	public void setTheme(String theme) {
		this.theme = theme;
	}

	/**
	 * @return the changeable
	 */
	public boolean isChangeable() {
		return changeable;
	}

	/**
	 * @param changeable the changeable to set
	 */
	public void setChangeable(boolean changeable) {
		this.changeable = changeable;
	}

	/**
	 * To get event entity from DTO
	 * 
	 * @param event
	 * @return Event
	 */
	@JsonIgnore
	public Event getEvent(Event event) {
		Calendar calendarFrom = Calendar.getInstance();
		calendarFrom.setTimeInMillis(this.start.getMillis());
		Calendar calendarEnd = Calendar.getInstance();
		calendarEnd.setTimeInMillis(this.end.getMillis());
		event.setFrom(calendarFrom);
		event.setTo(calendarEnd);
		event.setTitle(this.getText());
		event.setCompletion(completion);
		Theme tm = new Theme();
		tm.setKey(theme);
		event.setTheme(tm);
		event.setComment(comment);
		return event;
	}

	/**
	 * @return the start
	 */
	public DateTime getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(DateTime start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public DateTime getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(DateTime end) {
		this.end = end;
	}

	/**
	 * @return the resourceUrl
	 */
	public String getResourceUrl() {
		return resourceUrl;
	}

	/**
	 * @param resourceUrl the resourceUrl to set
	 */
	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
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
	 * @return the resourceType
	 */
	public String getResourceType() {
		return resourceType;
	}

	/**
	 * @param resourceType the resourceType to set
	 */
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	/**
	 * @return the offSet
	 */
	public int getOffSet() {
		return offSet;
	}

	/**
	 * @param offSet the offSet to set
	 */
	public void setOffSet(int offSet) {
		this.offSet = offSet;
	}

	/**
	 * @return the moveLinkedEvent
	 */
	public boolean isMoveLinkedEvent() {
		return moveLinkedEvent;
	}

	/**
	 * @param moveLinkedEvent the moveLinkedEvent to set
	 */
	public void setMoveLinkedEvent(boolean moveLinkedEvent) {
		this.moveLinkedEvent = moveLinkedEvent;
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

	/**
	 * @return the from
	 */
	public Calendar getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(Calendar from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public Calendar getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(Calendar to) {
		this.to = to;
	}

	/**
	 * @return the resourceName
	 */
	public String getResourceName() {
		if (this.resourceName == null) {
			this.resourceName = "";
		}
		return resourceName;
	}

	/**
	 * @param resourceName the resourceName to set
	 */
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	/**
	 * @return the conflictStart
	 */
	public String getConflictStart() {
		return conflictStart;
	}

	/**
	 * @param conflictStart the conflictStart to set
	 */
	public void setConflictStart(String conflictStart) {
		this.conflictStart = conflictStart;
	}

}
