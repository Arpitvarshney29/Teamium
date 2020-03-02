package com.teamium.domain.prod.projects;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.Theme;
import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.projects.planning.Event;
import com.teamium.domain.prod.resources.Resource;
import com.teamium.domain.prod.resources.functions.RatedFunction;

/**
 * Describe a event for the booking
 * 
 * @author JS - NovaRem
 * @since v7
 */
@Entity
@DiscriminatorValue(value = "BookingEvent")
@NamedQueries({
		@NamedQuery(name = BookingEvent.QUERY_countByFunctionTypeByDateRange, query = "SELECT COUNT(be) FROM BookingEvent be WHERE EXISTS(SELECT f FROM Function f WHERE TYPE(f) = ?1 AND f = be.origin.function) AND be.to >= ?2 AND be.from <= ?3 ORDER BY be.from ASC"),
		@NamedQuery(name = BookingEvent.QUERY_countByFunctionsByDateRange, query = "SELECT COUNT(be) FROM BookingEvent be WHERE be.origin.function IN (?1) AND be.to >= ?2 AND be.from <= ?3 ORDER BY be.from ASC"),
		@NamedQuery(name = BookingEvent.QUERY_findByFunctionTypeByDateRange, query = "SELECT be FROM BookingEvent be WHERE EXISTS(SELECT f FROM Function f WHERE TYPE(f) = ?1 AND f = be.origin.function) AND be.to >= ?2 AND be.from <= ?3 ORDER BY be.from ASC"),
		@NamedQuery(name = BookingEvent.QUERY_countByFunctionTypeByDateRangeInCompletionStatus, query = "SELECT COUNT(be) FROM BookingEvent be WHERE EXISTS(SELECT f FROM Function f WHERE TYPE(f) = ?1 AND f = be.origin.function) AND be.to >= ?2 AND be.from <= ?3 AND be.completionStatus IN ?4 AND EXISTS( SELECT r FROM Resource r WHERE be.resource = r AND TYPE(r) IN(?5))"),
		@NamedQuery(name = BookingEvent.QUERY_findByFunctionTypeByDateRangeInCompletionStatus, query = "SELECT be FROM BookingEvent be WHERE EXISTS(SELECT f FROM Function f WHERE TYPE(f) = ?1 AND f = be.origin.function) AND be.to >= ?2 AND be.from <= ?3 AND be.completionStatus IN ?4 AND EXISTS( SELECT r FROM Resource r WHERE be.resource = r AND TYPE(r) IN(?5)) ORDER BY be.from ASC"),
		@NamedQuery(name = BookingEvent.QUERY_findByFunctionsByDateRangeInCompletionStatus, query = "SELECT be FROM BookingEvent be WHERE be.origin.function IN (?1) AND be.to >= ?2 AND be.from <= ?3 AND be.completionStatus IN ?4 AND EXISTS( SELECT r FROM Resource r WHERE be.resource = r AND TYPE(r) IN(?5)) ORDER BY be.from ASC"),
		@NamedQuery(name = BookingEvent.QUERY_countByFunctionsByDateRangeInCompletionStatus, query = "SELECT COUNT(be) FROM BookingEvent be WHERE be.origin.function IN (?1) AND be.to >= ?2 AND be.from <= ?3 AND be.completionStatus IN ?4 AND EXISTS( SELECT r FROM Resource r WHERE be.resource = r AND TYPE(r) IN(?5))"),
		@NamedQuery(name = BookingEvent.QUERY_findByFunctionsByDateRange, query = "SELECT be FROM BookingEvent be WHERE be.origin.function IN (?1) AND be.to >= ?2 AND be.from <= ?3 ORDER BY be.from ASC"),
		@NamedQuery(name = BookingEvent.QUERY_findByReferenceByResourceByDate, query = "SELECT be FROM BookingEvent be WHERE be.reference = ?1 AND be.resource = ?2 AND be.from >= ?3"), })
public class BookingEvent extends Event {

	/**/
	private static final long serialVersionUID = -4750463996207881180L;

	/**
	 * Name of the query counting booking events for a given function type and a
	 * given range of datetime, sorted by date ascending, with a completion status
	 * among the given list
	 * 
	 * @param 1 the given function type
	 * @param 2 the given start datetime
	 * @param 3 the given end datetime
	 * @param 4 the given list of completion status
	 * @return the booking events list
	 */
	public static final String QUERY_countByFunctionTypeByDateRangeInCompletionStatus = "countBookingEventByFunctionTypeByDateRangeInCompletionStatus";

	/**
	 * Name of the query retrieving booking events for a given function type and a
	 * given range of datetime, sorted by date ascending, with a completion status
	 * among the given list
	 * 
	 * @param 1 the given function type
	 * @param 2 the given start datetime
	 * @param 3 the given end datetime
	 * @param 4 the given list of completion status
	 * @return the booking events list
	 */
	public static final String QUERY_findByFunctionTypeByDateRangeInCompletionStatus = "findBookingEventByFunctionTypeByDateRangeInCompletionStatus";

	/**
	 * Name of the query retrieving booking events for a given function type and a
	 * given range of datetime, sorted by date ascending, with a completion status
	 * among the given list
	 * 
	 * @param 1 the given function type
	 * @param 2 the given start datetime
	 * @param 3 the given end datetime
	 * @param 4 the given list of completion status
	 * @return the booking events list
	 */
	public static final String QUERY_findByFunctionsByDateRangeInCompletionStatus = "findBookingEventByFunctionsByDateRangeInCompletionStatus";

	/**
	 * Name of the query counting booking events for a given list of functions and a
	 * given range of datetime, sorted by date ascending, with a completion status
	 * among the given list
	 * 
	 * @param 1 the given function type
	 * @param 2 the given start datetime
	 * @param 3 the given end datetime
	 * @param 4 the given list of completion status
	 * @return the booking events list
	 */
	public static final String QUERY_countByFunctionsByDateRangeInCompletionStatus = "countBookingEventByFunctionsByDateRangeInCompletionStatus";

	/**
	 * Name of the query counting for a given function type and a given range of
	 * datetime, sorted by date asscending
	 * 
	 * @param 1 the given function type
	 * @param 2 the given start datetime
	 * @param 3 the given end datetime
	 * @return the booking events list
	 */
	public static final String QUERY_countByFunctionTypeByDateRange = "countBookingEventByFunctionTypeByDateRange";

	/**
	 * Name of the query counting for a given list of functions and a given range of
	 * datetime, sorted by date ascending
	 * 
	 * @param 1 the given list of functions
	 * @param 2 the given start datetime
	 * @param 3 the given end datetime
	 * @return the booking events list
	 */
	public static final String QUERY_countByFunctionsByDateRange = "countBookingEventByFunctionsByDateRange";

	/**
	 * Name of the query retrieving all booking events for a given function type and
	 * a given range of datetime, sorted by date ascending
	 * 
	 * @param 1 the given function type
	 * @param 2 the given start datetime
	 * @param 3 the given end datetime
	 * @return the booking events list
	 */
	public static final String QUERY_findByFunctionTypeByDateRange = "findBookingEventByFunctionTypeByDateRange";

	/**
	 * Name of the query retrieving booking events of a list of functions and a
	 * given range of datetime, sorted by date asccending
	 * 
	 * @param 1 the given list of functions
	 * @param 2 the given start datetime
	 * @param 3 the given end datetime
	 * @return the booking events list
	 */
	public static final String QUERY_findByFunctionsByDateRange = "findBookingEventByFunctionsByDateRange";

	/**
	 * Name of the query retrieving booking events for a given reference & resource
	 * & date
	 * 
	 * @param 1 the reference
	 * @param 2 the resource
	 * @param 3 the date ( from )
	 * @return the list of booking events
	 */
	public static final String QUERY_findByReferenceByResourceByDate = "findByReferenceByResourceByDate";

	/**
	 * Booking origin for the event
	 */
	@OneToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "c_origin")
	private Booking origin;

	/**
	 * Reference generate when a booking is generate from another (Example:
	 * Recurrence)
	 */
	@Column(name = "c_recurrence_reference")
	private Long reference;

	/**
	 * Set of Linked Booking Events.
	 */
	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "t_linked_event", joinColumns = { @JoinColumn(name = "c_event_id") }, inverseJoinColumns = {
			@JoinColumn(name = "c_link_event_id") })
	private Set<BookingEvent> linkedEvents = new HashSet<BookingEvent>();

	/**
	 * Return the booking origin for the event
	 */
	public Booking getOrigin() {
		return origin;
	}

	/**
	 * Update the booking origin for the event
	 */
	public void setOrigin(Booking origin) {
		this.origin = origin;
	}

	/**
	 * Returns the theme used for formatting event
	 * 
	 * @return the theme
	 */
	public Theme getTheme() {
		if (super.getTheme() == null) {
			if (this.getOrigin() != null && this.getOrigin().getRecord() != null) {
				Record r = this.getOrigin().getRecord();
				if (r instanceof Quotation) {
					Quotation q = (Quotation) r;
					if (q.getProgram() != null) {
						try {
							super.setTheme(q.getProgram().getTheme());
						} catch (Exception e) {
						}
					}
					if (super.getTheme() == null) {
						if (q.getProductionUnit() != null) {
							super.setTheme(q.getProductionUnit().getTheme());
						}
					}
				}
			}

			if (super.getTheme() == null) {
				try {
					super.setTheme(this.getOrigin().getFunction().getTheme());
				} catch (NullPointerException e) {
					return null;
				}
			}
		}

		return super.getTheme();
	}

	/**
	 * Returns the event title
	 * 
	 * @return the title
	 */
	public String getTitle() {
		if (super.getTitle() == null) {
			String title = "";
			try {
				title += this.getOrigin().getFunction().getQualifiedName() + " "
						+ ((AbstractProject) this.getOrigin().getRecord()).getTitle() + " "
						+ this.getOrigin().getResource().getName();
			} catch (Exception e) {
			}
			return title;
		} else {
			return super.getTitle();
		}
	}

	/**
	 * Return the description
	 */
	@Override
	public String getDescription() {
		return origin.getDescription();
	}

	/**
	 * Create the booking event
	 */
	public static BookingEvent create(Booking booking) {
		BookingEvent event = new BookingEvent();
		event.origin = (Booking) booking.copy(booking);
		event.setFrom(booking.getFrom());
		event.setTo(booking.getTo());
		event.setResource(booking.getResource());
		return event;
	}

	@Override
	public BookingEvent clone() throws CloneNotSupportedException {
		BookingEvent clone = null;
		Booking origin = this.origin;
		this.origin = null;
		try {
			clone = (BookingEvent) super.clone();
			clone.origin = origin;
		} catch (CloneNotSupportedException e) {
			throw e;
		} finally {
			this.origin = origin;
		}
		return clone;
	}

	/**
	 * Update the resource
	 */
	@Override
	public void setResource(Resource<?> resource) {
		super.setResource(resource);
		// if(this.origin != null){
		// this.origin.setResource(resource);
		// }
	}

	/**
	 * Update raw resource (doesn't affect linked booking)
	 */
	public void setRawResource(Resource<?> resource) {
		super.setResource(resource);
	}

	/**
	 * Update from
	 */
	@Override
	public void setFrom(Calendar date) {
		super.setFrom(date);
		// if(this.origin != null){
		// this.origin.setFrom(date);
		// }
	}

	/**
	 * Update from raw (doesn't affect linked booking)
	 */
	public void setFromRaw(Calendar date) {
		super.setFrom(date);
	}

	/**
	 * Update to
	 */
	public void setTo(Calendar date) {
		super.setTo(date);
		// if(this.origin != null){
		// this.origin.setTo(date);
		// }
	}

	/**
	 * Update to raw (doesn't affect linked booking)
	 */
	public void setToRaw(Calendar date) {
		super.setTo(date);
	}

	/**
	 * @return the reference
	 */
	public Long getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(Long reference) {
		this.reference = reference;
	}

	/**
	 * @return the linkedEvents
	 */
	public Set<BookingEvent> getLinkedEvents() {
		return linkedEvents;
	}

	/**
	 * @param linkedEvents the linkedEvents to set
	 */
	public void setLinkedEvents(Set<BookingEvent> linkedEvents) {
		this.linkedEvents = linkedEvents;
	}

}
