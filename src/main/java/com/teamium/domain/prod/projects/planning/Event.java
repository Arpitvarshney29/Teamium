/**
 * 
 */
package com.teamium.domain.prod.projects.planning;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.Theme;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.projects.BookingEvent;
import com.teamium.domain.prod.projects.CompletionStatus;
import com.teamium.domain.prod.resources.Resource;
import com.teamium.domain.prod.resources.staff.contract.ContractStatus;

/**
 * Describes an event to display on planning
 * 
 * @author sraybaud - NovaRem
 *
 */
@Entity
@Table(name = "t_event")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "c_discriminator")
@NamedQueries({ @NamedQuery(name = Event.QUERY_delete, query = "DELETE FROM Event e WHERE e = ?1"),
		@NamedQuery(name = Event.QUERY_findByResourcesByPeriod, query = "SELECT ev FROM Event ev WHERE ev.resource IN (?1) AND ev.from <= ?3 AND (ev.to IS NULL OR ev.to >= ?2)"),
		@NamedQuery(name = Event.QUERY_findConflictByPeriod, query = "SELECT ev FROM Event ev WHERE ev.from <= ?2 AND ( ev.to IS NULL OR ev.to >= ?1) AND EXISTS (SELECT evc FROM Event evc WHERE evc.from <= ev.to AND ( evc.to IS NULL or evc.to >= ev.from ) AND ev.resource = evc.resource AND ev.id <> evc.id )"),
		@NamedQuery(name = Event.QUERY_findByResourceByDateRangeInCompletionStatus, query = "SELECT ev FROM Event ev WHERE ev.resource = ?1 AND ev.to >= ?2 AND ev.from <= ?3 AND ev.completionStatus IN ?4 ORDER BY ev.from ASC"),
		@NamedQuery(name = Event.QUERY_findByResourceByDateRange, query = "SELECT ev FROM Event ev WHERE ev.resource = ?1 AND ev.to >= ?2 AND ev.from <= ?3 ORDER BY ev.from ASC"),
		@NamedQuery(name = Event.QUERY_findByResourceByDateRangeInCompletionStatusByType, query = "SELECT ev FROM Event ev WHERE ev.resource = ?1 AND ev.to >= ?2 AND ev.from <= ?3 AND ev.completionStatus IN ?5 AND TYPE(ev) = ?4 ORDER BY ev.from ASC"),
		@NamedQuery(name = Event.QUERY_findByResourceByDateRangeByType, query = "SELECT ev FROM Event ev WHERE ev.resource = ?1 AND ev.to >= ?2 AND ev.from <= ?3 AND TYPE(ev) = ?4 ORDER BY ev.from ASC"), })
public abstract class Event extends AbstractEntity implements Cloneable, Comparable<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4052029991985349407L;

	/**
	 * Completion status : to do
	 */
	public static final CompletionStatus COMPLETION_STATUS_TODO = new CompletionStatus("todo");

	/**
	 * Completion status : in progress
	 */
	public static final CompletionStatus COMPLETION_STATUS_INPROGRESS = new CompletionStatus("inprogress");

	/**
	 * Completion status : to do
	 */
	public static final CompletionStatus COMPLETION_STATUS_DONE = new CompletionStatus("done");

	/**
	 * Completion status : to do
	 */
	public static final CompletionStatus COMPLETION_STATUS_OVERDUE = new CompletionStatus("overdue");

	/**
	 * Delete the given entity from the persistence context
	 * 
	 * @param 1 the given entity to delete
	 * @param 1 if entity has been successfully deleted, else returns 0
	 */
	public static final String QUERY_delete = "deleteEvent";

	/**
	 * Name of the query retrieving events for a given list resources on a given
	 * period
	 * 
	 * @param 1 the given list of resources
	 * @param 2 beginning of period
	 * @param 3 end of period
	 * @return the booking list
	 */
	public static final String QUERY_findByResourcesByPeriod = "findEventByResourcesByPeriod";

	/**
	 * Name of named query to find event conflict by date range
	 * 
	 * @param 1 The start date of the range
	 * @param 2 The end date of the range
	 */
	public static final String QUERY_findConflictByPeriod = "findConflictEventByPeriod";

	/**
	 * Name of the query retrieving booking events for a given resource and a given
	 * range of datetime, sorted by date ascending, with a completion status among
	 * the given list
	 * 
	 * @param 1 the given resource
	 * @param 2 the given start datetime
	 * @param 3 the given end datetime
	 * @param 4 the given list of completion status
	 * @return the events list
	 */
	public static final String QUERY_findByResourceByDateRangeInCompletionStatus = "findEventByResourceByDateRangeInCompletionStatus";

	/**
	 * Name of the query retrieving all booking events for a given resource and a
	 * given range of datetime, sorted by date ascending
	 * 
	 * @param 1 the given resource
	 * @param 2 the given start datetime
	 * @param 3 the given end datetime
	 * @return the events list
	 */
	public static final String QUERY_findByResourceByDateRange = "findEventByResourceByDateRange";

	/**
	 * Name of the query retrieving booking events for a given resource and a given
	 * range of datetime, sorted by date ascending, with a completion status among
	 * the given list
	 * 
	 * @param 1 the given resource
	 * @param 2 the given start datetime
	 * @param 3 the given end datetime
	 * @param 4 the entity class
	 * @param 5 the given list of completion status
	 * @return the events list
	 */
	public static final String QUERY_findByResourceByDateRangeInCompletionStatusByType = "findEventByResourceByDateRangeInCompletionStatusByType";

	/**
	 * Name of the query retrieving all booking events for a given resource and a
	 * given range of datetime, sorted by date ascending
	 * 
	 * @param 1 the given resource
	 * @param 2 the given start datetime
	 * @param 3 the given end datetime
	 * @param 4 the entity class
	 * @return the events list
	 */
	public static final String QUERY_findByResourceByDateRangeByType = "findEventByResourceByDateRangeByType";

	@Id
	@Column(name = "c_id")
	@TableGenerator(name = "idEvent_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "event_idevent_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idEvent_seq")
	private Long id;

	/**
	 * Estimated starting date
	 */
	@Column(name = "c_from")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar from;

	/**
	 * Estimated ending date
	 */
	@Column(name = "c_to")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar to;

	/**
	 * Estimated duration (for temporal quantities)
	 */
	@Column(name = "c_duration")
	private Long duration;

	/**
	 * Is the event editable ?
	 */
	@Transient
	private Boolean editable;

	/**
	 * Booking status
	 */
	@Embedded
	@AttributeOverride(name = "key", column = @Column(name = "c_status"))
	private XmlEntity status;

	/**
	 * Title for the event
	 */
	@Column(name = "c_title")
	private String title;

	/**
	 * Theme for the line
	 */
	@Embedded
	@AttributeOverride(name = "key", column = @Column(name = "c_theme"))
	private Theme theme;

	/**
	 * Task completion
	 */
	@Column(name = "c_completion")
	private Float completion;

	/**
	 * The resource concerned by the unavailability
	 */
	@ManyToOne
	@JoinColumn(name = "c_resource")
	private Resource<?> resource;

	/**
	 * Task completion
	 */
	@Embedded
	@AttributeOverride(name = "key", column = @Column(name = "c_completion_status"))
	private CompletionStatus completionStatus;

	/**
	 * Boolean if the booking is on superposition whit other(s) booking(s) Transient
	 * non init
	 */
	@Transient
	private Boolean superposition;

	@Column(name = "c_comment")
	private String comment;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;

	}

	/**
	 * Returns the start time of the event
	 * 
	 * @return the start time event
	 */
	public Calendar getFrom() {
		return from;
	};

	/**
	 * Sets the start timestamp of the event
	 * 
	 * @param the date to set
	 */
	public void setFrom(Calendar date) {
		this.from = date;
	};

	/**
	 * Returns the end time of the event
	 * 
	 * @param the end time of the event
	 */
	public Calendar getTo() {
		return to;
	};

	/**
	 * Sets the end timestamp of the event
	 * 
	 * @param the date to set
	 */
	public void setTo(Calendar date) {
		this.to = date;
	};

	/**
	 * Return the timestamp in millis from the duration, to adapt with the unit
	 * 
	 * @return the duration
	 */
	public Long getDuration() {
		if (from != null && to != null) {
			duration = (to.getTimeInMillis() - from.getTimeInMillis());
		}
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(Long duration) {
		this.duration = duration;
	}

	/**
	 * @return the resource (the function resource by default)
	 */
	public Resource<?> getResource() {
		return this.resource;
	}

	/**
	 * @param resource the resource to set
	 */
	public void setResource(Resource<?> resource) {
		this.resource = resource;
	}

	/**
	 * Returns the edit status of the event
	 */
	public Boolean getEditable() {
		if (this.editable == null)
			this.editable = true;
		return this.editable;
	}

	/**
	 * Set the event editable
	 * 
	 */
	public void setEditable(Boolean bool) {
		this.editable = bool;
	}

	/**
	 * Hide on large scale
	 * 
	 * @return true if the event has to be hidden on large scale
	 */
	public boolean getHideOnLargeScale() {
		return false;
	};

	/**
	 * Returns the theme used for formatting event
	 * 
	 * @return the theme
	 */
	public Theme getTheme() {
		return this.theme;
	}

	/**
	 * Update the theme
	 */
	public void setTheme(Theme theme) {
		this.theme = theme;
	}

	/**
	 * Returns the event detail
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns the event description
	 * 
	 * @return the description
	 */
	public abstract String getDescription();

	/**
	 * @return the status
	 */
	public XmlEntity getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(XmlEntity status) {
		this.status = status;
	}

	/**
	 * @return the completion
	 */
	public Float getCompletion() {
		if (completion == null)
			completion = 0F;
		return completion;
	}

	/**
	 * @param completion the completion to set
	 */
	public void setCompletion(Float completion) {
		this.completion = completion;
	}

	/**
	 * Return if the booking is on superposition whi other(s) booking(s) Transient
	 * non init
	 */
	public Boolean getSuperposition() {
		return this.superposition;
	}

	/**
	 * Update if the booking is on superposition whi other(s) booking(s)
	 */
	public void setSuperposition(Boolean superposition) {
		this.superposition = superposition;
	}

	/**
	 * Returns the completion status
	 * 
	 * @return the completion status
	 */
	public CompletionStatus getCompletionStatus() {
		if ((this.getCompletion() == null || this.completion < 1) && (this.getTo() != null
				&& (Calendar.getInstance().before(this.getTo()) && Calendar.getInstance().equals(this.getTo())))) {
			this.completionStatus = COMPLETION_STATUS_TODO;
		} else if ((this.getCompletion() == null || this.completion < 1)
				&& (this.getTo() == null || Calendar.getInstance().after(this.getTo()))) {
			this.completionStatus = COMPLETION_STATUS_OVERDUE;
		} else if ((this.getCompletion() == null || this.completion < 1)) {
			this.completionStatus = COMPLETION_STATUS_INPROGRESS;
		} else {
			this.completionStatus = COMPLETION_STATUS_DONE;
		}
		return this.completionStatus;
	}

	/**
	 * Before update in the persistent context
	 */
	@PreUpdate
	@PrePersist
	private void beforeUpdate() {
		this.getCompletionStatus();
	}

	@Override
	public Event clone() throws CloneNotSupportedException {
		Event clone = null;
		XmlEntity status = this.status;
		this.status = null;
		Resource resource = this.resource;
		this.resource = null;
		Theme theme = this.theme;
		this.theme = null;
		try {
			clone = (Event) super.clone();
			clone.from = (Calendar) this.from.clone();
			clone.to = (Calendar) this.to.clone();
			clone.status = this.status;
			clone.resource = this.resource;
			clone.theme = this.theme;
		} catch (CloneNotSupportedException e) {
			throw e;
		} finally {
			this.status = status;
			this.resource = resource;
			this.theme = theme;
		}
		return clone;
	}

	@Override
	public int compareTo(Event o) {
		int compare = 0;
		if (o == null)
			return 1;

		// From
		if (this.from != null) {
			if (o.from == null) {
				compare = -1;
			} else {
				compare = this.from.compareTo(o.from);
			}
		} else {
			if (o.from == null) {
				compare = 1;
			}
		}

		// To
		if (compare == 0) {
			if (this.to != null) {
				if (o.to == null) {
					compare = -1;
				} else {
					compare = this.to.compareTo(o.to);
				}
			} else {
				if (o.to == null) {
					compare = 1;
				}
			}
		}

		// Resource
		if (compare == 0) {
			if (this.resource != null) {
				if (o.resource == null) {
					compare = -1;
				} else {
					compare = this.resource.compareTo(o.resource);
				}
			} else {
				if (o.resource == null) {
					compare = 1;
				}
			}
		}

		// Id
		if (compare == 0) {
			if (this.getId() != null) {
				if (o.getId() == null) {
					compare = -1;
				} else {
					compare = this.getId().compareTo(o.getId());
				}
			} else {
				if (o.getId() == null) {
					compare = 1;
				}
			}
		}

		return compare;
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

}
