/**
 * 
 */
package com.teamium.domain.prod.projects;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.teamium.domain.Group;
import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.projects.order.SupplyResource;
import com.teamium.domain.prod.projects.order.WorkAndTravelOrder;
import com.teamium.domain.prod.resources.Resource;
import com.teamium.domain.prod.resources.equipments.EquipmentResource;
import com.teamium.domain.prod.resources.functions.Assignation;
import com.teamium.domain.prod.resources.functions.Function;
import com.teamium.domain.prod.resources.functions.units.RateUnit;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.domain.prod.resources.staff.StaffResource;
import com.teamium.domain.prod.resources.staff.contract.ContractStatus;
import com.teamium.domain.prod.resources.staff.contract.EntertainmentContractSetting;

/**
 * Describes a booking
 * 
 * @author sraybaud - NovaRem
 * @version DO & SLN 0.1 : Correction de QUERY_findByProjectByTypeResource. TYPE
 *          n'admet pas autre chose que la class sélectionné dans le from.
 */
@Entity
@DiscriminatorValue(value = Booking.DISCRIMINATOR)
@NamedQueries({ @NamedQuery(name = Booking.QUERY_delete, query = "DELETE FROM Booking b WHERE b = ?1"),
		@NamedQuery(name = Booking.QUERY_findWorkflowableByProject, query = "SELECT b FROM Booking b WHERE b.record = ?1 AND NOT EXISTS (SELECT DISTINCT w FROM ProcessingWorkflow w, IN(w.steps) s WHERE s.booking = b)"),
		@NamedQuery(name = Booking.QUERY_findByProjectByFunctionTypeByResourceType, query = "SELECT b FROM Booking b WHERE b.record = ?1 AND EXISTS(SELECT r FROM Resource r WHERE TYPE(r) = ?3 AND b.resource = r ) AND EXISTS(SELECT f FROM Function f WHERE TYPE(f) = ?2 AND b.function = f)"),
		@NamedQuery(name = Booking.QUERY_findByProjectAndFunctionTypeAndResource, query = "SELECT b FROM Booking b WHERE b.record = ?1 AND b.resource = ?3 AND EXISTS(SELECT f FROM Function f WHERE TYPE(f) = ?2 AND b.function = f)"),
		@NamedQuery(name = Booking.QUERY_findByFunctionTypeByResourceType, query = "SELECT b FROM Booking b WHERE EXISTS(SELECT r FROM Resource r WHERE TYPE(r) = ?2 AND b.resource = r) AND EXISTS(SELECT f FROM Function f WHERE TYPE(f) = ?1 AND b.function = f)"),
		@NamedQuery(name = Booking.QUERY_findByProjectByFunctionTypes, query = "SELECT b FROM Booking b WHERE b.record = ?1 AND EXISTS(SELECT f FROM Function f WHERE TYPE(f) IN ?2 AND b.function = f )"),
		@NamedQuery(name = Booking.QUERY_findByProjectByResourceTypes, query = "SELECT b FROM Booking b WHERE b.record = ?1 AND EXISTS(SELECT r FROM Resource r WHERE TYPE(r) = ?2 AND b.resource = r )"),
		@NamedQuery(name = Booking.QUERY_findByOrder, query = "SELECT b FROM Booking b WHERE EXISTS(SELECT r FROM SupplyResource r WHERE r.entity = ?1 AND b.resource = r)"),
		@NamedQuery(name = Booking.QUERY_findByResource, query = "SELECT b FROM Booking b WHERE b.resource = ?1 ORDER BY b.from ASC"),
		@NamedQuery(name = Booking.QUERY_findByDateRange, query = "SELECT b FROM Booking b WHERE b.to >= ?1 AND b.from <= ?2 AND b.record IN(SELECT p FROM Project p) ORDER BY b.from ASC"),
		@NamedQuery(name = Booking.QUERY_findByResourceByDateRange, query = "SELECT b FROM Booking b WHERE b.resource = ?1 AND b.to >= ?2 AND b.from <= ?3 ORDER BY b.from ASC"),
		@NamedQuery(name = Booking.QUERY_findByResourceByDateRangeFilterByResourceType, query = "SELECT b FROM Booking b WHERE b.to >= ?1 AND b.from <= ?2 AND EXISTS(SELECT r FROM Resource r WHERE r = b.resource AND TYPE(r) = ?3) ORDER BY b.from ASC"),
		@NamedQuery(name = Booking.QUERY_findByResourceFromDate, query = "SELECT b FROM Booking b WHERE b.resource = ?1 AND (b.to >= ?2) ORDER BY b.from ASC"),
		@NamedQuery(name = Booking.QUERY_findByFunctionTypeByDateRange, query = "SELECT b FROM Booking b WHERE EXISTS(SELECT f FROM Function f WHERE TYPE(f) = ?1 AND f = b.function) AND b.to >= ?2 AND b.from <= ?3 ORDER BY b.from ASC"),
		@NamedQuery(name = Booking.QUERY_countByFunctionTypeByDateRange, query = "SELECT COUNT(b) FROM Booking b WHERE EXISTS(SELECT f FROM Function f WHERE TYPE(f) = ?1 AND f = b.function) AND b.to >= ?2 AND b.from <= ?3 ORDER BY b.from ASC"),
		@NamedQuery(name = Booking.QUERY_findByFunctionsByDateRange, query = "SELECT b FROM Booking b WHERE b.function IN (?1) AND b.to >= ?2 AND b.from <= ?3 ORDER BY b.from ASC"),
		@NamedQuery(name = Booking.QUERY_findBookedResourcesByResourcesByDateRange, query = "SELECT DISTINCT b.resource FROM Booking b WHERE b.resource IN (?1) AND b.to >= ?2 AND b.from <= ?3"),
		@NamedQuery(name = Booking.QUERY_countByFunctionsByDateRange, query = "SELECT COUNT(b) FROM Booking b WHERE b.function IN (?1) AND b.to >= ?2 AND b.from <= ?3 ORDER BY b.from ASC"),
		@NamedQuery(name = Booking.QUERY_findByResourceByContractStatus, query = "SELECT b FROM Booking b WHERE b.resource = ?1 AND b.contractStatus = ?2 ORDER BY b.from ASC"),
		@NamedQuery(name = Booking.QUERY_findInterventionBookingByEquipmentResourceByDate, query = "SELECT b FROM Booking b WHERE EXISTS (SELECT i FROM Intervention i WHERE b.record = i) AND b.resource = ?1 AND b.from >= ?2"),
		@NamedQuery(name = Booking.QUERY_findInterventionBookingByEquipmentResourceByPeriod, query = "SELECT b FROM Booking b WHERE EXISTS (SELECT i FROM Intervention i WHERE b.record = i) AND b.resource = ?1 AND b.from < ?3 AND b.to > ?2"),
		@NamedQuery(name = Booking.QUERY_findInOutByMediaResource, query = "SELECT b FROM Booking b WHERE EXISTS ( SELECT inR FROM InOutResource inR WHERE b.resource = inR AND EXISTS ( SELECT i FROM InOut i WHERE i.media = ?1 AND inR.entity = i ))"),
		@NamedQuery(name = Booking.QUERY_findInterventionBookingByDateRange, query = "SELECT b FROM Booking b WHERE EXISTS (SELECT i FROM Intervention i WHERE b.record = i) AND b.from >= ?1 AND b.to <= ?2"),
		@NamedQuery(name = Booking.QUERY_findInterventionBookingByInterventions, query = "SELECT b FROM Booking b WHERE b.record IN(?1) AND EXISTS(SELECT r FROM Resource r WHERE TYPE(r) = ?2 AND b.resource = r )"),

})

public class Booking extends Line {

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -9087468679480941303L;

	/**
	 * Entity discriminator
	 */
	public static final String DISCRIMINATOR = "booking";

	/**
	 * The entity alias used in queries
	 */
	public static final String QUERY_ENTITY_ALIAS = "b";

	/**
	 * Name of the query retrieving bookings which can be added to a workflow for a
	 * given project
	 * 
	 * @param 1 the given project
	 * @return the booking list
	 */
	public static final String QUERY_findWorkflowableByProject = "findWorkflowableBookingByProject";

	/**
	 * Delete the given entity from the persistence context
	 * 
	 * @param 1 the given entity to delete
	 * @param 1 if entity has been successfully deleted, else returns 0
	 */
	public static final String QUERY_delete = "deleteBooking";

	/**
	 * Name of the query retrieving bookings from a given project with a given type
	 * of function assigned to a given type of resource
	 * 
	 * @param 2 the given type of function
	 * @param 3 the given type of resource
	 * @return the booking list
	 */
	public static final String QUERY_findByProjectByFunctionTypeByResourceType = "findBookingByProjectByFunctionTypeByResourceType";

	/**
	 * Name of the query retrieving bookings from a given project with a given type
	 * of function assigned to a given resource
	 * 
	 * @param 2 the given type of function
	 * @param 3 the given type of resource
	 * @return the booking list
	 */
	public static final String QUERY_findByProjectAndFunctionTypeAndResource = "findByProjectAndFunctionTypeAndResource";

	/**
	 * Name of the query retrieving bookings with a given type of function assigned
	 * to a given type of resource
	 * 
	 * @param 1 the given type of function
	 * @param 2 the given type of resource
	 * @return the bookings list
	 */
	public static final String QUERY_findByFunctionTypeByResourceType = "findBookingByFunctionTypeByResourceType";

	/**
	 * Name of the query retrieving bookings from a given project with function
	 * belonging to one of the given types
	 * 
	 * @param 1 the given project
	 * @param 2 the given types of functions
	 * @return the booking list
	 */
	public static final String QUERY_findByProjectByFunctionTypes = "findBookingByProjectByFunctionTypes";

	/**
	 * Name of the query retrieving bookings from a given project with Resource
	 * belonging to one of the given types
	 * 
	 * @param 1 the given project
	 * @param 2 the given types of Resource
	 * @return the booking list
	 */
	public static final String QUERY_findByProjectByResourceTypes = "findBookingByProjectByResourceTypes";

	/**
	 * Name of the query retrieving bookings placed on a given order
	 * 
	 * @param 1 the given order
	 * @return the booking list
	 */
	public static final String QUERY_findByOrder = "findBookingByOrder";

	/**
	 * Name of the query retrieving bookings for a given resource, sorted by date
	 * ascending
	 * 
	 * @param 1 the given resource
	 * @return the bookings list
	 */
	public static final String QUERY_findByResource = "findBookingByResource";

	/**
	 * Name of the query retrieving all bookings for a given resource and a given
	 * range of datetime, sorted by date ascending
	 * 
	 * @param 1 the given start datetime
	 * @param 2 the given end datetime
	 * @return the bookings list
	 */
	public static final String QUERY_findByDateRange = "findBookingByDateRange";

	/**
	 * Name of the query retrieving all bookings for a given resource and a given
	 * range of datetime, sorted by date ascending
	 * 
	 * @param 1 the given resource
	 * @param 2 the given start datetime
	 * @param 3 the given end datetime
	 * @return the bookings list
	 */
	public static final String QUERY_findByResourceByDateRange = "findBookingByResourceByDateRange";

	/**
	 * Name of the query retrieving all bookings for a given resource and a given
	 * range of datetime, sorted by date ascending
	 * 
	 * @param 1 the given start datetime
	 * @param 2 the given end datetime
	 * @param 3 the resource type filter
	 * @return the bookings list
	 */
	public static final String QUERY_findByResourceByDateRangeFilterByResourceType = "findBookingByResourceByDateRangeFilterByResourceType";

	/**
	 * Name of the query retrieving all bookings for a given resource from a given
	 * datetime, sorted by date ascending
	 * 
	 * @param 1 the given resource
	 * @param 2 the given start datetime
	 * @return the bookings list
	 */
	public static final String QUERY_findByResourceFromDate = "findBookingByResourceFromDate";

	/**
	 * Name of the query counting for a given function type and a given range of
	 * datetime, sorted by date asscending
	 * 
	 * @param 1 the given function type
	 * @param 2 the given start datetime
	 * @param 3 the given end datetime
	 * @return the bookings list
	 */
	public static final String QUERY_countByFunctionTypeByDateRange = "countBookingByFunctionTypeByDateRange";

	/**
	 * Name of the query retrieving all bookings for a given function type and a
	 * given range of datetime, sorted by date ascending
	 * 
	 * @param 1 the given function type
	 * @param 2 the given start datetime
	 * @param 3 the given end datetime
	 * @return the bookings list
	 */
	public static final String QUERY_findByFunctionTypeByDateRange = "findBookingByFunctionTypeByDateRange";

	/**
	 * Name of the query counting for a given list of functions and a given range of
	 * datetime, sorted by date ascending
	 * 
	 * @param 1 the given list of functions
	 * @param 2 the given start datetime
	 * @param 3 the given end datetime
	 * @return the bookings list
	 */
	public static final String QUERY_countByFunctionsByDateRange = "countBookingByFunctionsByDateRange";

	/**
	 * Name of the query retrieving bookings of a list of functions and a given
	 * range of datetime, sorted by date asccending
	 * 
	 * @param 1 the given list of functions
	 * @param 2 the given start datetime
	 * @param 3 the given end datetime
	 * @return the bookings list
	 */
	public static final String QUERY_findByFunctionsByDateRange = "findBookingByFunctionsByDateRange";

	/**
	 * Name of the query retrieving distinct resources from a list of resources that
	 * are booked within a given period
	 * 
	 * @param 1 the given list of resources to test
	 * @param 2 the given start datetime
	 * @param 3 the given end datetime
	 * @return the bookings list
	 */
	public static final String QUERY_findBookedResourcesByResourcesByDateRange = "findBookingResourcesByResourcesByDateRange";

	/**
	 * Name of the query retrieving bookings for a given resource with a given
	 * contract status
	 * 
	 * @param 1 the resource
	 * @param 2 the contract status
	 * @return the list of bookings
	 */
	public static final String QUERY_findByResourceByContractStatus = "findBookingByResourceByContractStatus";

	/**
	 * String of the query retrieving the ids the given ids,that may be completed by
	 * OrderByClause.getClause()
	 * 
	 * @param 1 the ids the match
	 * @return the list of matching ids
	 */
	public static final String QUERY_findOrderedByIdsByType = "SELECT b.id FROM Booking b WHERE r.id IN (?1)";

	/**
	 * Name of named query used to find booking in Intervention matching the
	 * equipment resource given in parameter after the date given in parameter
	 * 
	 * @param 1 The equipment resource
	 * @param 2 The start date
	 */
	public static final String QUERY_findInterventionBookingByEquipmentResourceByDate = "findInterventionByEquipmentResouce";

	/**
	 * Name of named query used to find booking in Intervention matching the
	 * equipment resource given in parameter after the date given in parameter
	 * 
	 * @param 1 The equipment resource
	 * @param 2 The start date
	 * @param 3 The end date
	 */
	public static final String QUERY_findInterventionBookingByEquipmentResourceByPeriod = "findInterventionByEquipmentResoucebyPeriod";

	/**
	 * Name of named query used to find booking in InOut matching the media resource
	 * given in parameter
	 * 
	 * @param 1 The media resource
	 */
	public static final String QUERY_findInOutByMediaResource = "findInOutByResourceMedia";
	/**
	 * Name of named query to find intervention booking by date range
	 * 
	 * @param 1 The start date of the range
	 * @param 2 The end date of the range
	 */
	public static final String QUERY_findInterventionBookingByDateRange = "findInterventionBookingByDateRange";

	/**
	 * Name of named query to find intervention booking by Interventions
	 * 
	 * @param 1 Interveniton list.
	 * @param 2 EquipmentResource
	 */
	public static final String QUERY_findInterventionBookingByInterventions = "findInterventionBookingByInterventions";

	/**
	 * Estimated starting date
	 */
	@Column(name = "c_booking_from")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar from;

	/**
	 * Estimated ending date
	 */
	@Column(name = "c_booking_to")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar to;

	/**
	 * Estimated Return date
	 */
	@Column(name = "c_return_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar returnTime;

	/**
	 * Estimated Pickup date
	 */
	@Column(name = "c_pickup_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar pickupTime;

	/**
	 * Cost update
	 */
	@Transient
	private boolean updateCost = false;

	/**
	 * Contract booking status
	 */
	@Column(name = "c_contractstatus")
	@Enumerated(EnumType.STRING)
	private ContractStatus contractStatus;

	/**
	 * True if a mail was send to the staff resource in charge of the booking
	 */
	@Column(name = "c_mailed")
	private Boolean mailed;

	/**
	 * The line used to generate the booking
	 */
	@Column(name = "c_origin")
	private Long origin;

	/**
	 * The assigned resource
	 */
	@ManyToOne
	@JoinColumn(name = "c_booking_idresource")
	private Resource<?> resource;

	/**
	 * Estimated duration (for temporal quantities)
	 */
	@Column(name = "c_duration")
	private Long duration;

	/**
	 * Booking event
	 */
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "origin")
	private BookingEvent bookingEvent;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "c_work_order_id")
	private WorkAndTravelOrder workOrder;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "c_travel_order_id")
	private WorkAndTravelOrder travelOrder;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "c_media_order_id")
	private WorkAndTravelOrder mediaOrder;

	/**
	 * Group event
	 */
	@OneToOne
	@JoinColumn(name = "c_group_id")
	private Group group;

	/**
	 * Issue
	 */
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "booking")
	private Issue issue;

	/**
	 * Booking Status.
	 */
	@Column(name = "c_bookings_status")
	private Integer bookingStatus;

	/**
	 * Start Time
	 */
	@Column(name = "c_user_start_time")
	private Date userStartTime;

	/**
	 * Night Time
	 */
	@Column(name = "c_user_end_time")
	private Date userEndTime;
	
	
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
	 * Returns the start time of the event
	 * 
	 * @return the start time event
	 */
	public Calendar getFrom() {
		if (this.getBookingEvent() != null) {
			return this.getBookingEvent().getFrom();
		}
		return from;
	};

	/**
	 * Sets the start timestamp of the event
	 * 
	 * @param the date to set
	 */
	public void setFrom(Calendar date) {
		// Temp keep old value for process update if needed
		Calendar oldFrom = from;
		this.from = date;
		if (this.bookingEvent != null) {
			this.bookingEvent.setFromRaw(this.from);
		}
		if (oldFrom != null && this.from != null && !oldFrom.equals(from)) {
			this.updateChangeFrom();
		}
	}

	/**
	 * Returns the end time of the event
	 * 
	 * @param the end time of the event
	 */
	public Calendar getTo() {
		if (this.getBookingEvent() != null) {
			this.getBookingEvent().getTo();
		}
		return to;
	};

	/**
	 * Sets the end timestamp of the event
	 * 
	 * @param the date to set
	 */
	public void setTo(Calendar date) {
		// Temp keep old value for process update if needed
		Calendar oldTo = this.to;
		this.to = date;
		if (this.bookingEvent != null) {
			this.bookingEvent.setToRaw(this.to);
		}
		if (oldTo != null && this.to != null && !oldTo.equals(to)) {
			this.updateChangeTo();
		}
	};

	/**
	 * @return the resource (the function resource by default)
	 */
	public Resource<?> getResource() {
		if (this.resource == null && this.getFunction() != null) {
			this.resource = this.getFunction().getDefaultResource();
		}
		// RETURN EVENT RESOURCE
		if (this.bookingEvent != null) {
			return this.getBookingEvent().getResource();
		}
		return resource;
	}

	/**
	 * @param resource the resource to set
	 */
	public void setResource(Resource<?> resource) {
		if (resource != null && !resource.equals(this.getResource()))
			this.setUpdateCost(true);
		this.resource = resource;
		if (this.bookingEvent != null) {
			this.bookingEvent.setRawResource(this.resource);
		}
		try {
			/**
			 * Process assignation
			 */
			if (this.resource instanceof StaffResource && ((StaffMember) this.resource.getEntity())
					.getContractSetting() instanceof EntertainmentContractSetting) {
				this.setAssignation(Assignation.FREELANCE);
			} else if (this.resource instanceof StaffResource && !(((StaffMember) this.resource.getEntity())
					.getContractSetting() instanceof EntertainmentContractSetting)) {
				this.setAssignation(Assignation.INTERNAL);
			} else if (this.resource instanceof EquipmentResource) {
				this.setAssignation(Assignation.INTERNAL);
			} else if (this.resource instanceof SupplyResource) {
				this.setAssignation(Assignation.EXTERNAL);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Returns the event description
	 * 
	 * @return the description
	 */
	public String getDescription() {
		String description = "";
		try {
			description += ((AbstractProject) this.getRecord()).getTitle() + "\n";
			description += (this.getRecord().getCompany() != null ? this.getRecord().getCompany().getName() + "\n"
					: "");
			Function function = this.getFunction() != null ? this.getFunction() : null;
			String name = null;
			while (function != null) {
				if (name == null)
					name = "";
				else
					name = ">" + name;
				name = function.getValue() + name;
				function = function.getParent();
			}
		} catch (Exception e) {
		}
		return description;
	}

	/**
	 * @return the contractStatus
	 */
	public ContractStatus getContractStatus() {
		return contractStatus;
	}

	/**
	 * @param contractStatus the contractStatus to set
	 */
	public void setContractStatus(ContractStatus contractStatus) {
		this.contractStatus = contractStatus;
	}

	/**
	 * Returns string description of the current object
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		return super.toString() + "[" + (this.getFrom() != null ? TSF.format(this.getFrom().getTime()) : "null") + "-"
				+ (this.getTo() != null ? TSF.format(this.getTo().getTime()) : "null") + "]";
	}

	/**
	 * Set the unitCost
	 */
	@Override
	public void setUnitCost(Float unitCost) {
		super.setUnitCost(unitCost);
		updateCost = false;
	}

	/**
	 * @return the updateCost
	 */
	public boolean isUpdateCost() {
		return updateCost;
	}

	/**
	 * @param updateCost the updateCost to set
	 */
	public void setUpdateCost(boolean updateCost) {
		this.updateCost = updateCost;
	}

	/**
	 * @return the mailed
	 */
	public Boolean getMailed() {
		return mailed;
	}

	/**
	 * @param mailed the mailed to set
	 */
	public void setMailed(Boolean mailed) {
		this.mailed = mailed;
	}

	/**
	 * @return the origin
	 */
	public Long getOrigin() {
		return origin;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(Long origin) {
		this.origin = origin;
	}

	/**
	 * @return the issue
	 */
	public Issue getIssue() {
		if (issue == null) {
			issue = new Issue();
			issue.setBooking(this);
		}
		return issue;
	}

	/**
	 * @param issue the issue to set
	 */
	public void setIssue(Issue issue) {
		this.issue = issue;
	}

	/**
	 * @return the bookingStatus
	 */
	public Integer getBookingStatus() {
		if (bookingStatus == null) {
			bookingStatus = 0;
		}
		return bookingStatus;
	}

	/**
	 * @param bookingStatus the bookingStatus to set
	 */
	public void setBookingStatus(Integer bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	/**
	 * @return the returnTime
	 */
	public Calendar getReturnTime() {
		return returnTime;
	}

	/**
	 * @param returnTime the returnTime to set
	 */
	public void setReturnTime(Calendar returnTime) {
		this.returnTime = returnTime;
	}

	/**
	 * @return the pickupTime
	 */
	public Calendar getPickupTime() {
		return pickupTime;
	}

	/**
	 * @param pickupTime the pickupTime to set
	 */
	public void setPickupTime(Calendar pickupTime) {
		this.pickupTime = pickupTime;
	}

	/**
	 * @return the mediaOrder
	 */
	public WorkAndTravelOrder getMediaOrder() {
		return mediaOrder;
	}

	/**
	 * @param mediaOrder the mediaOrder to set
	 */
	public void setMediaOrder(WorkAndTravelOrder mediaOrder) {
		this.mediaOrder = mediaOrder;
	}

	/**
	 * Compare the current object with the given one
	 * 
	 * @param other the other to compare
	 * @return -1 if the current is previous, 0 if both are equal, +1 if the given
	 *         is previous
	 */
	@Override
	public int compareTo(Line l) {
		if (!(l instanceof Booking)) {
			super.compareTo(l);
		}
		Booking o = (Booking) l;
		if (o == null)
			return 1;
		if (this.getFunction() == null) {
			if (o.getFunction() != null) {
				return -1;
			}
		} else {
			if (o.getFunction() != null) {
				int compare = this.getFunction().compareTo(o.getFunction());
				if (compare != 0)
					return compare;
			} else {
				return 1;
			}
		}
		if (this.getResource() == null) {
			if (o.getResource() != null) {
				return -1;
			}
		} else {
			if (o.getResource() != null) {
				int compare = this.getResource().compareTo(o.getResource());
				if (compare != 0)
					return compare;
			} else {
				return 1;
			}
		}
		if (this.getFrom() == null) {
			if (o.getFrom() != null) {
				return -1;
			}
		} else {
			if (o.getFrom() != null) {
				int compare = this.getFrom().compareTo(o.getFrom());
				if (compare != 0)
					return compare;
			} else {
				return 1;
			}
		}
		if (this.getTo() == null) {
			if (o.getTo() != null) {
				return -1;
			}
		} else {
			if (o.getTo() != null) {
				int compare = this.getTo().compareTo(o.getTo());
				if (compare != 0)
					return compare;
			} else {
				return 1;
			}
		}
		if (this.getId() == null) {
			if (o.getId() != null) {
				return -1;
			}
		} else {
			if (o.getId() != null) {
				int compare = this.getId().compareTo(o.getId());
				if (compare != 0)
					return compare;
			} else {
				return 1;
			}
		}
		return 0;
	}

	/**
	 * @return the bookingEvents
	 */
	public BookingEvent getBookingEvent() {
		return bookingEvent;
	}

	/**
	 * @param bookingEvents the bookingEvents to set
	 */
	public void setBookingEvent(BookingEvent bookingEvents) {
		this.bookingEvent = bookingEvents;
	}

	/**
	 * @return the userStartTime
	 */
	public Date getUserStartTime() {
		return userStartTime;
	}

	/**
	 * @param userStartTime the userStartTime to set
	 */
	public void setUserStartTime(Date userStartTime) {
		this.userStartTime = userStartTime;
	}

	/**
	 * @return the userEndTime
	 */
	public Date getUserEndTime() {
		return userEndTime;
	}

	/**
	 * @param userEndTime the userEndTime to set
	 */
	public void setUserEndTime(Date userEndTime) {
		this.userEndTime = userEndTime;
	}

	/**
	 * Before update in the persistent context
	 */
	@PreUpdate
	@PrePersist
	private void beforeUpdate() {
		// Calculating completion status;
		if (contractStatus == null)
			contractStatus = ContractStatus.NOT_MADE;
	}

	/**
	 * Update quantity quantity per oc for used
	 */
	@Override
	public void setQtyUsedPerOc(Float qtyUsedPerOc) {
		super.setQtyUsedPerOc(qtyUsedPerOc);
		this.updateChangeQtyUsed();
	}

	/**
	 * @param unit the unit to set
	 */
	@Override
	public void setUnitUsed(RateUnit unitUsed) {
		if (this.getUnitUsedRaw() != null && unitUsed != null && !this.getUnitUsed().equals(unitUsed)) {
			super.setUnitUsed(unitUsed);
			this.updateChangeQtyUsed();
		} else {
			super.setUnitUsed(unitUsed);
		}
	}

	/**
	 * @param unitSale the unitSale to set
	 */
	@Override
	public void setUnitSold(RateUnit unitSold) {
		if (this.getUnitSold() != null && unitSold != null && !this.getUnitSold().equals(unitSold)) {
			super.setUnitSold(unitSold);
			this.updateChangeQtyUsed();
		} else {
			super.setUnitSold(unitSold);
		}
	}

	/**
	 * Update quantity per oc for sold
	 */
	public void setQtySoldPerOc(Float qtySoldPerOc) {
		if (qtySoldPerOc != null && this.getQtySoldPerOc() != null && !qtySoldPerOc.equals(this.getQtySoldPerOc())) {
			super.setQtySoldPerOc(qtySoldPerOc);
			this.updateChangeQtyUsed();
		} else {
			super.setQtySoldPerOc(qtySoldPerOc);
		}
	}

	/**
	 * Update the booking after a change on from
	 */
	public void updateChangeFrom() {
		if (this.getFrom() != null && this.getQtyUsedPerOc() != null && this.getUnitUsed() != null
				&& this.getUnitUsed().getCalendarConstant() != null) {
			Calendar toTmp = (Calendar) this.getFrom().clone();
			toTmp.add(this.getUnitUsed().getCalendarConstant(), this.getQtyUsedPerOc().intValue());
			this.to = toTmp;
			// Update booking event
			if (this.getBookingEvent() != null) {
				this.getBookingEvent().setToRaw(toTmp);
			}
		}
	}

	/**
	 * Update the booking after a change on to
	 */
	public void updateChangeTo() {
		if (this.getTo() != null && this.getFrom() != null && this.getUnitUsed() != null
				&& this.getUnitUsed().getCalendarConstant() != null) {
			Long milliSec = 0l;
			Long qty = null;
			System.out.println(this.getUnitUsed() + " " + this.getUnitUsed().getCalendarConstant());
			switch (this.getUnitUsed().getCalendarConstant()) {
			// SECOND
			case 13:
				milliSec = 1000l;
				break;
			// MINUTES
			case 12:
				milliSec = 60000l;
				break;
			// HOUR OF DAY
			case 11:
				milliSec = 3600000l;
				break;
			// DAY OF MONTH
			case 5:
				milliSec = 86400000l;
				break;
			// DAY OF YEAR
			case 6:
				milliSec = 86400000l;
				break;
			// WEEK OF YEAR
			case 3:
				milliSec = 604800000l;
				break;
			// MONTH
			case 2:
				qty = Long.valueOf(this.getTo().get(Calendar.MONTH)
						+ ((this.getTo().get(Calendar.YEAR) - this.getFrom().get(Calendar.YEAR)) * 12)
						- this.getFrom().get(Calendar.MONTH));
				break;
			}
			if (qty == null && milliSec != 0l) {
				qty = (this.getTo().getTimeInMillis() - this.getFrom().getTimeInMillis()) / milliSec;
				if (qty.equals(0)) {
					qty = 1l;
				}
			}
			super.setQtyUsedPerOc(Float.valueOf(qty));
		}
	}

	/**
	 * Update the booking after a change on qty ( per oc )
	 */
	public void updateChangeQtyUsed() {
		if (this.getFrom() != null && this.getQtyUsedPerOc() != null && this.getUnitUsed() != null
				&& this.getUnitUsed().getCalendarConstant() != null) {
			Calendar toTmp = (Calendar) this.getFrom().clone();
			toTmp.add(this.getUnitUsed().getCalendarConstant(), this.getQtyUsedPerOc().intValue());
			this.to = toTmp;
			// Update booking event
			if (this.getBookingEvent() != null) {
				this.getBookingEvent().setToRaw(toTmp);
			}
		}
	}

	/**
	 * Clone the current object
	 * 
	 * @return the clone
	 * @throws CloneNotSupportedException
	 */
	@Override
	public Booking clone() throws CloneNotSupportedException {
		Booking clone = null;
		Resource resource = this.resource;
		this.resource = null;
		BookingEvent be = this.bookingEvent;
		this.bookingEvent = null;
		ContractStatus cs = this.contractStatus;
		this.contractStatus = null;
		Issue issue = this.issue;

		try {
			clone = (Booking) super.clone();
			clone.resource = resource;
			if (be != null) {
				clone.bookingEvent = BookingEvent.create(this);
			}
			clone.contractStatus = cs;
			clone.issue = null;
		} catch (CloneNotSupportedException e) {
			throw e;
		} finally {
			this.resource = resource;
			this.bookingEvent = be;
			this.contractStatus = cs;
			this.issue = issue;
		}

		return clone;
	}

	/**
	 * Clone the current object whitout the bookingEvent
	 * 
	 * @throws CloneNotSupportedException
	 */
	public Booking cloneWhitoutEvent() throws CloneNotSupportedException {
		Booking clone = this.clone();
		clone.setBookingEvent(null);

		return clone;
	}

	/**
	 * @return the workOrder
	 */
	public WorkAndTravelOrder getWorkOrder() {
		return workOrder;
	}

	/**
	 * @param workOrder the workOrder to set
	 */
	public void setWorkOrder(WorkAndTravelOrder workOrder) {
		this.workOrder = workOrder;
	}

	/**
	 * @return the travelOrder
	 */
	public WorkAndTravelOrder getTravelOrder() {
		return travelOrder;
	}

	/**
	 * @param travelOrder the travelOrder to set
	 */
	public void setTravelOrder(WorkAndTravelOrder travelOrder) {
		this.travelOrder = travelOrder;
	}

	/**
	 * @return the group
	 */
	public Group getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(Group group) {
		this.group = group;
	}

	public String getFromString() {
		if (this.from == null) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		return format.format(this.from.getTime());
	}

	

}
