package com.teamium.dto;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Transient;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.projects.AbstractProject;
import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.projects.Project;
import com.teamium.domain.prod.projects.order.SupplyResource;
import com.teamium.domain.prod.projects.order.WorkAndTravelOrder;
import com.teamium.domain.prod.resources.functions.DefaultResource;
import com.teamium.domain.prod.resources.functions.ProcessFunction;
import com.teamium.domain.prod.resources.staff.StaffFunction;

/**
 * DTO for booking
 * 
 * @author wittybrains
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BookingDTO extends LineDTO {

	/**
	 * Estimated starting date
	 */
	private Calendar from;

	/**
	 * Estimated ending date
	 */
	private Calendar to;

	/**
	 * Estimated Return date
	 */
	private Calendar returnTime;

	/**
	 * Estimated Pickup date
	 */
	private Calendar pickupTime;

	/**
	 * Cost update
	 */
	@Transient
	private boolean updateCost = false;

	/**
	 * Contract booking status
	 */
	private String contractStatus;

	/**
	 * True if a mail was send to the staff resource in charge of the booking
	 */
	private Boolean mailed;

	/**
	 * The line used to generate the booking
	 */
	private Long origin;

	/**
	 * The assigned resourceId
	 */
	private Long resourceId;

	/**
	 * The assigned resource
	 */

	private ResourceDTO<?> resource;

	/**
	 * Booking event
	 */
	private BookingEventDTO event;

	/**
	 * Estimated duration (for temporal quantities)
	 */
	private Long duration;

	/**
	 * List of available resource to the booking
	 */
	private List<ResourceDTO<ResourceDTO<?>>> availableResources;

	private String start;
	private String end;
	private boolean defaultResource = false;

	private Long projectId;
	private String projectTheme;
	private String projectTitle;
	private GroupDTO group;
	private String userStartTime;
	private String userEndTime;
	private boolean staffBooking;
	private boolean showOrderForm;
	private WorkAndTravelOrder workOrder;
	private WorkAndTravelOrder travelOrder;
	private WorkAndTravelOrder mediaOrder;
	private boolean fromVendor = false;

	/**
	 * Sick
	 */
	private boolean sick;
	private boolean fromBudget;

	private long numberOfday;

	public BookingDTO() {
		super();
	}

	public BookingDTO(Line lineEntity) {
		super(lineEntity);
		Booking line = null;
		if (lineEntity instanceof Booking) {
			line = (Booking) lineEntity;
			this.from = line.getFrom();
			this.to = line.getTo();
			if (this.getFrom() != null) {
				DateTime dt = new DateTime(this.getFrom().getTime()).withZone(DateTimeZone.UTC);
				// this.start =
				// dt.toString(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
				this.start = dt.toString();
			}

			if (this.getTo() != null) {
				DateTime dt = new DateTime(this.getTo().getTime()).withZone(DateTimeZone.UTC);
				// this.end =
				// dt.toString(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
				this.end = dt.toString();
			}
			this.returnTime = line.getReturnTime();
			this.pickupTime = line.getPickupTime();

			this.contractStatus = line.getContractStatus() == null ? "" : line.getContractStatus().getValue();
			// this.duration = line.getDuration();
			this.resource = line.getResource() != null ? new ResourceDTO<>(line.getResource()) : new ResourceDTO<>();
			if (line.getResource() != null) {
				this.resource = new ResourceDTO<>(line.getResource());
				if (line.getResource() instanceof SupplyResource) {
					this.fromVendor = true;
				}
				if (line.getResource() instanceof DefaultResource) {
					this.defaultResource = true;
				}
				this.staffBooking = line.getFunction() instanceof StaffFunction ? true : false;
				this.showOrderForm = line.getFunction() instanceof StaffFunction
						|| line.getFunction() instanceof ProcessFunction ? true : false;
			}
			if (line.getBookingEvent() != null) {
				this.event = new BookingEventDTO(line.getBookingEvent());
			}
			if (line.getGroup() != null) {
				this.group = new GroupDTO(line.getGroup(), "forStaffView");
			}
			if (line.getWorkOrder() != null) {
				this.workOrder = line.getWorkOrder();
			}
			if (line.getTravelOrder() != null) {
				this.travelOrder = line.getTravelOrder();
			}
			if (line.getMediaOrder() != null) {
				this.mediaOrder = line.getMediaOrder();
			}
			if (line.getRecord() != null && (line.getRecord() instanceof AbstractProject)) {
				AbstractProject project = (AbstractProject) line.getRecord();
				this.setProjectTitle(project.getTitle());
			}
		}

		// TimeZone tz = null;
		// DateTimeZone jodaTz = null;
		// DateTime dateTime = null;
		// Booking line = null;
		// if (lineEntity instanceof Booking) {
		// line = (Booking) lineEntity;
		//
		// if (line.getFrom() != null) {
		// tz = line.getFrom().getTimeZone();
		// jodaTz = DateTimeZone.forID(tz.getID());
		// dateTime = new DateTime(line.getFrom().getTimeInMillis(), jodaTz);
		// }
		// this.from = dateTime;
		// if (line.getTo() != null) {
		// tz = line.getTo().getTimeZone();
		// jodaTz = DateTimeZone.forID(tz.getID());
		// dateTime = new DateTime(line.getTo().getTimeInMillis(), jodaTz);
		// }
		// this.to = dateTime;
		// if (line.getReturnTime() != null) {
		// tz = line.getReturnTime().getTimeZone();
		// jodaTz = DateTimeZone.forID(tz.getID());
		// dateTime = new DateTime(line.getReturnTime().getTimeInMillis(), jodaTz);
		// }
		// this.returnTime = dateTime;
		//
		// if (line.getPickupTime() != null) {
		// tz = line.getReturnTime().getTimeZone();
		// jodaTz = DateTimeZone.forID(tz.getID());
		// dateTime = new DateTime(line.getPickupTime().getTimeInMillis(), jodaTz);
		// }
		// this.pickupTime = dateTime;
		// if (line.getPickupTime() != null) {
		// tz = line.getReturnTime().getTimeZone();
		// jodaTz = DateTimeZone.forID(tz.getID());
		// dateTime = new DateTime(line.getPickupTime().getTimeInMillis(), jodaTz);
		// }

	}

	public BookingDTO(Booking booking) {
		super(booking);
		this.from = booking.getFrom();
		this.to = booking.getTo();
		if (this.getFrom() != null) {
			DateTime dt = new DateTime(this.getFrom().getTime()).withZone(DateTimeZone.UTC);
			// this.start =
			// dt.toString(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
			this.start = dt.toString();
		}

		if (this.getTo() != null) {
			DateTime dt = new DateTime(this.getTo().getTime()).withZone(DateTimeZone.UTC);
			// this.end =
			// dt.toString(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
			this.end = dt.toString();
		}
		if (booking.getUserStartTime() != null) {
			this.userStartTime = new DateTime(booking.getUserStartTime()).withZone(DateTimeZone.UTC).toString();
		}

		if (booking.getUserEndTime() != null) {
			this.userEndTime = new DateTime(booking.getUserEndTime()).withZone(DateTimeZone.UTC).toString();
		}
		if (booking.getBookingEvent() != null) {
			this.event = new BookingEventDTO(booking.getBookingEvent());
		}
		if (booking.getResource() != null) {
			this.resource = new ResourceDTO<>(booking.getResource());
		}
		if (booking.getGroup() != null) {
			this.group = new GroupDTO(booking.getGroup(), "forStaffView");
		}
		if (booking.getGroup() != null) {
			this.group = new GroupDTO(booking.getGroup(), "forStaffView");
		}
		if (booking.getWorkOrder() != null) {
			this.workOrder = booking.getWorkOrder();
		}
		if (booking.getTravelOrder() != null) {
			this.travelOrder = booking.getTravelOrder();
		}
		if (booking.getMediaOrder() != null) {
			this.mediaOrder = booking.getMediaOrder();
		}
		if (booking.getRecord() != null && (booking.getRecord() instanceof AbstractProject)) {
			AbstractProject project = (AbstractProject) booking.getRecord();
			this.setProjectTitle(project.getTitle());
		}
	}

	public BookingDTO(Booking booking, boolean customized) {
		this.setId(booking.getId());
		if (booking.getRecord() != null && (booking.getRecord() instanceof Project)) {
			AbstractProject abstractProject = (AbstractProject) booking.getRecord();
			this.setProjectTitle(abstractProject.getTitle());
		}
		this.from = booking.getFrom();
		this.to = booking.getTo();
		if (this.getFrom() != null) {
			DateTime dt = new DateTime(this.getFrom().getTime()).withZone(DateTimeZone.UTC);
			this.start = dt.toString();
		}
		if (this.getTo() != null) {
			DateTime dt = new DateTime(this.getTo().getTime()).withZone(DateTimeZone.UTC);
			this.end = dt.toString();
		}
	}

	public BookingDTO(Booking booking, boolean forOrder, String supplierName) {
		super(booking);
		if (forOrder) {
			this.from = booking.getFrom();
			this.to = booking.getTo();
			if (this.getFrom() != null) {
				DateTime dt = new DateTime(this.getFrom().getTime()).withZone(DateTimeZone.UTC);
				// this.start =
				// dt.toString(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
				this.start = dt.toString();
			}

			if (this.getTo() != null) {
				DateTime dt = new DateTime(this.getTo().getTime()).withZone(DateTimeZone.UTC);
				// this.end =
				// dt.toString(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
				this.end = dt.toString();
			}
			if (booking.getBookingEvent() != null) {
				this.event = new BookingEventDTO(booking.getBookingEvent());
			}
			if (booking.getResource() != null) {
				this.resource = new ResourceDTO<>(booking.getResource());
				this.staffBooking = booking.getFunction() instanceof StaffFunction ? true : false;
				this.showOrderForm = booking.getFunction() instanceof StaffFunction
						|| booking.getFunction() instanceof ProcessFunction ? true : false;
			}
			this.setSupplierName(supplierName);
		}
	}

	// public BookingDTO(Booking booking) {
	// super(booking);
	// this.resource = new ResourceDTO<>(booking.getResource());
	// }

	/**
	 * Booking Status.
	 */
	private Integer bookingStatus;

	/**
	 * @return the from
	 */
	public Calendar getFrom() {
		return from;
	}

	/**
	 * @param from
	 *            the from to set
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
	 * @param to
	 *            the to to set
	 */
	public void setTo(Calendar to) {
		this.to = to;
	}

	/**
	 * @return the returnTime
	 */
	public Calendar getReturnTime() {
		return returnTime;
	}

	/**
	 * @param returnTime
	 *            the returnTime to set
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
	 * @param pickupTime
	 *            the pickupTime to set
	 */
	public void setPickupTime(Calendar pickupTime) {
		this.pickupTime = pickupTime;
	}

	/**
	 * @return the updateCost
	 */
	public boolean isUpdateCost() {
		return updateCost;
	}

	/**
	 * @param updateCost
	 *            the updateCost to set
	 */
	public void setUpdateCost(boolean updateCost) {
		this.updateCost = updateCost;
	}

	/**
	 * @return the contractStatus
	 */
	public String getContractStatus() {
		return contractStatus;
	}

	/**
	 * @param contractStatus
	 *            the contractStatus to set
	 */
	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}

	/**
	 * @return the mailed
	 */
	public Boolean getMailed() {
		return mailed;
	}

	/**
	 * @param mailed
	 *            the mailed to set
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
	 * @param origin
	 *            the origin to set
	 */
	public void setOrigin(Long origin) {
		this.origin = origin;
	}

	/**
	 * @return the resourceId
	 */
	public Long getResourceId() {
		return resourceId;
	}

	/**
	 * @param resourceId
	 *            the resourceId to set
	 */
	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	/**
	 * @return the resource
	 */
	public ResourceDTO<?> getResource() {
		return resource;
	}

	/**
	 * @param resource
	 *            the resource to set
	 */
	public void setResource(ResourceDTO<?> resource) {
		this.resource = resource;
	}

	/**
	 * @return the duration
	 */
	public Long getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(Long duration) {
		this.duration = duration;
	}

	/**
	 * @return the bookingStatus
	 */
	public Integer getBookingStatus() {
		return bookingStatus;
	}

	/**
	 * @param bookingStatus
	 *            the bookingStatus to set
	 */
	public void setBookingStatus(Integer bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	/**
	 * @return the event
	 */
	public BookingEventDTO getEvent() {
		return event;
	}

	/**
	 * @param event
	 *            the event to set
	 */
	public void setEvent(BookingEventDTO event) {
		this.event = event;
	}

	/**
	 * @return the availableResources
	 */
	public List<ResourceDTO<ResourceDTO<?>>> getAvailableResources() {
		return availableResources;
	}

	/**
	 * @param availableResources
	 *            the availableResources to set
	 */
	public void setAvailableResources(List<ResourceDTO<ResourceDTO<?>>> availableResources) {
		this.availableResources = availableResources;
	}

	@JsonIgnore
	public Booking getBooking(Booking booking) {
		super.getLine(booking, this);
		return booking;
	}

	/**
	 * @return the start
	 */
	public String getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(String start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public String getEnd() {
		return end;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public void setEnd(String end) {
		this.end = end;
	}

	/**
	 * @return the defaultResource
	 */
	public boolean isDefaultResource() {
		return defaultResource;
	}

	/**
	 * @param defaultResource
	 *            the defaultResource to set
	 */
	public void setDefaultResource(boolean defaultResource) {
		this.defaultResource = defaultResource;
	}

	/**
	 * @return the projectId
	 */
	public Long getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId
	 *            the projectId to set
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return the projectTheme
	 */
	public String getProjectTheme() {
		return projectTheme;
	}

	/**
	 * @param projectTheme
	 *            the projectTheme to set
	 */
	public void setProjectTheme(String projectTheme) {
		this.projectTheme = projectTheme;
	}

	/**
	 * @return the sick
	 */
	public boolean isSick() {
		return sick;
	}

	/**
	 * @param sick
	 *            the sick to set
	 */
	public void setSick(boolean sick) {
		this.sick = sick;
	}

	/**
	 * @return the projectTitle
	 */
	public String getProjectTitle() {
		return projectTitle;
	}

	/**
	 * @param projectTitle
	 *            the projectTitle to set
	 */
	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}

	/**
	 * @return the staffBooking
	 */
	public boolean isStaffBooking() {
		return staffBooking;
	}

	/**
	 * @param staffBooking
	 *            the staffBooking to set
	 */
	public void setStaffBooking(boolean staffBooking) {
		this.staffBooking = staffBooking;
	}

	/**
	 * @return the group
	 */
	public GroupDTO getGroup() {
		return group;
	}

	/**
	 * @param group
	 *            the group to set
	 */
	public void setGroup(GroupDTO group) {
		this.group = group;
	}

	/**
	 * @return the userStartTime
	 */
	public String getUserStartTime() {
		return userStartTime;
	}

	/**
	 * @param userStartTime
	 *            the userStartTime to set
	 */
	public void setUserStartTime(String userStartTime) {
		this.userStartTime = userStartTime;
	}

	/**
	 * @return the userEndTime
	 */
	public String getUserEndTime() {
		return userEndTime;
	}

	/**
	 * @param userEndTime
	 *            the userEndTime to set
	 */
	public void setUserEndTime(String userEndTime) {
		this.userEndTime = userEndTime;
	}

	/**
	 * @return the workOrder
	 */
	public WorkAndTravelOrder getWorkOrder() {
		return workOrder;
	}

	/**
	 * @param workOrder
	 *            the workOrder to set
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
	 * @param travelOrder
	 *            the travelOrder to set
	 */
	public void setTravelOrder(WorkAndTravelOrder travelOrder) {
		this.travelOrder = travelOrder;
	}

	/**
	 * @return the mediaOrder
	 */
	public WorkAndTravelOrder getMediaOrder() {
		return mediaOrder;
	}

	/**
	 * @param mediaOrder
	 *            the mediaOrder to set
	 */
	public void setMediaOrder(WorkAndTravelOrder mediaOrder) {
		this.mediaOrder = mediaOrder;
	}

	/**
	 * @return the showOrderForm
	 */
	public boolean isShowOrderForm() {
		return showOrderForm;
	}

	/**
	 * @param showOrderForm
	 *            the showOrderForm to set
	 */
	public void setShowOrderForm(boolean showOrderForm) {
		this.showOrderForm = showOrderForm;
	}

	public boolean isFromVendor() {
		return fromVendor;
	}

	public void setFromVendor(boolean fromVendor) {
		this.fromVendor = fromVendor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BookingDTO [from=" + from + ", to=" + to + ", returnTime=" + returnTime + ", pickupTime=" + pickupTime
				+ ", updateCost=" + updateCost + ", contractStatus=" + contractStatus + ", mailed=" + mailed
				+ ", origin=" + origin + ", resourceId=" + resourceId + ", resource=" + resource + ", event=" + event
				+ ", duration=" + duration + ", availableResources=" + availableResources + ", start=" + start
				+ ", end=" + end + ", bookingStatus=" + bookingStatus + ", getId()=" + getId() + ", getRecord()="
				+ getRecord() + ", getFunction()=" + getFunction() + ", getQtyTotalUsed()=" + getQtyTotalUsed()
				+ ", getQtyTotalSold()=" + getQtyTotalSold() + ", getUnitPrice()=" + getUnitPrice()
				+ ", getFloorUnitPrice()=" + getFloorUnitPrice() + ", getUnitCost()=" + getUnitCost()
				+ ", getPersistentCurrency()=" + getPersistentCurrency() + ", getDiscountRate()=" + getDiscountRate()
				+ ", getTotalLocalPrice()=" + getTotalLocalPrice() + ", getTotalPrice()=" + getTotalPrice()
				+ ", getTotalLocalCost()=" + getTotalLocalCost() + ", getTotalCost()=" + getTotalCost()
				+ ", getDisabled()=" + getDisabled() + ", getComment()=" + getComment() + ", getOccurrenceCount()="
				+ getOccurrenceCount() + ", getQtyUsedPerOc()=" + getQtyUsedPerOc() + ", getQtySoldPerOc()="
				+ getQtySoldPerOc() + ", getSyncQty()=" + getSyncQty() + ", getFreeQuantity()=" + getFreeQuantity()
				+ ", getExtraCost()=" + getExtraCost() + ", getExtraPrice()=" + getExtraPrice() + ", getSaleEntity()="
				+ getSaleEntity() + ", isFaulty()=" + isFaulty() + ", getRate()=" + getRate() + ", getExtras()="
				+ getExtras() + ", getVat()=" + getVat() + ", toString()=" + super.toString() + ", getTotalWithTax()="
				+ getTotalWithTax() + ", getTotalPriceWithOccurenceCount()=" + getTotalPriceWithOccurenceCount()
				+ ", getTotalCostWithOccurenceCount()=" + getTotalCostWithOccurenceCount() + ", getVatAmount()="
				+ getVatAmount() + ", getVatRate()=" + getVatRate() + ", isQtyChange()=" + isQtyChange()
				+ ", isDateChange()=" + isDateChange() + ", getUnitPriceBasis()=" + getUnitPriceBasis()
				+ ", getDiscountAmount()=" + getDiscountAmount() + ", isApplyDateToAll()=" + isApplyDateToAll()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + "]";
	}

	/**
	 * 
	 * @return
	 */
	public boolean isFromBudget() {
		return fromBudget;
	}

	/**
	 * 
	 * @param fromBudget
	 */
	public void setFromBudget(boolean comeFromBudget) {
		this.fromBudget = comeFromBudget;
	}

	/**
	 * 
	 * @return
	 */
	public long getNumberOfday() {
		return numberOfday;
	}

	/**
	 * 
	 * @param numberOfday
	 */
	public void setNumberOfday(long numberOfday) {
		this.numberOfday = numberOfday;
	}

}
