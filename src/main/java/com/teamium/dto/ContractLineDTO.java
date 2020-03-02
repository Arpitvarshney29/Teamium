package com.teamium.dto;

import javax.persistence.Column;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.domain.prod.resources.staff.contract.ContractLine;

@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ContractLineDTO {
	private Long id;
	private Boolean maunalUpdateOn;
	private Integer extraQuantity;
	private Integer nightQuantity;
	private Integer travelQuantity;
	private Integer holidayQuantity;
	private String rootFunctionName;
	private String functionName;
	private String freelanacerName;
	private String bookingStart;
	private String bookingEnd;
	private String avatar;
	private String status;

	public ContractLineDTO() {
	}

	public ContractLineDTO(ContractLine contractLine) {
		this.id = contractLine.getId();
		Booking booking =null;
		if(contractLine.getBookings()!=null && contractLine.getBookings().size()>0) {
			booking =contractLine.getBookings().get(0);
		}
		
		if (booking != null) {
			// bookingDTO = new BookingDTO(booking);
			if (booking.getFrom() != null) {
				this.bookingStart = new DateTime(booking.getFrom().getTime()).withZone(DateTimeZone.UTC).toString();
			}
			if (booking.getTo() != null) {
				this.bookingEnd = new DateTime(booking.getTo().getTime()).withZone(DateTimeZone.UTC).toString();
			}
			if (booking.getResource() != null) {
				this.freelanacerName = booking.getResource().getName();
				StaffMember sf = (StaffMember) booking.getResource().getEntity();
				if (sf != null && sf.getPhoto() != null && sf.getPhoto().getUrl() != null) {
					this.avatar = sf.getPhoto().getUrl().toString();
				}
			}
			if (booking.getFunction() != null) {
				if (booking.getFunction().getParent() != null) {
					this.rootFunctionName = booking.getFunction().getParent().getValue();
				}
				this.functionName = booking.getFunction().getValue();
			}
			if (booking.getContractStatus() != null)
				this.status = booking.getContractStatus().getValue();
		}
		this.maunalUpdateOn = contractLine.getMaunalUpdateOn();
		if (this.maunalUpdateOn != null && this.maunalUpdateOn) {
			this.nightQuantity = contractLine.getNightQuantity();
			this.extraQuantity = contractLine.getExtraQuantity();
			this.travelQuantity = contractLine.getTravelQuantity();
			this.holidayQuantity = contractLine.getHolidayQuantity();
		}
	}

	public ContractLine getContractLineEntity(ContractLine contractLine) {
		contractLine.setMaunalUpdateOn(this.getMaunalUpdateOn());
		if (this.getMaunalUpdateOn()!=null &&this.getMaunalUpdateOn()) {
			contractLine.setNightQuantity(this.nightQuantity);
			contractLine.setExtraQuantity(this.extraQuantity);
			contractLine.setTravelQuantity(this.travelQuantity);
			contractLine.setHolidayQuantity(this.holidayQuantity);
		}
		return contractLine;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the maunalUpdateOn
	 */
	public Boolean getMaunalUpdateOn() {
		return maunalUpdateOn;
	}

	/**
	 * @param maunalUpdateOn
	 *            the maunalUpdateOn to set
	 */
	public void setMaunalUpdateOn(Boolean maunalUpdateOn) {
		this.maunalUpdateOn = maunalUpdateOn;
	}

	/**
	 * @return the extraQuantity
	 */
	public Integer getExtraQuantity() {
		return extraQuantity;
	}

	/**
	 * @param extraQuantity
	 *            the extraQuantity to set
	 */
	public void setExtraQuantity(Integer extraQuantity) {
		this.extraQuantity = extraQuantity;
	}

	/**
	 * @return the nightQuantity
	 */
	public Integer getNightQuantity() {
		return nightQuantity;
	}

	/**
	 * @param nightQuantity
	 *            the nightQuantity to set
	 */
	public void setNightQuantity(Integer nightQuantity) {
		this.nightQuantity = nightQuantity;
	}

	/**
	 * @return the travelQuantity
	 */
	public Integer getTravelQuantity() {
		return travelQuantity;
	}

	/**
	 * @param travelQuantity
	 *            the travelQuantity to set
	 */
	public void setTravelQuantity(Integer travelQuantity) {
		this.travelQuantity = travelQuantity;
	}

	/**
	 * @return the holidayQuantity
	 */
	public Integer getHolidayQuantity() {
		return holidayQuantity;
	}

	/**
	 * @param holidayQuantity
	 *            the holidayQuantity to set
	 */
	public void setHolidayQuantity(Integer holidayQuantity) {
		this.holidayQuantity = holidayQuantity;
	}

	/**
	 * @return the rootFunctionName
	 */
	public String getRootFunctionName() {
		return rootFunctionName;
	}

	/**
	 * @param rootFunctionName
	 *            the rootFunctionName to set
	 */
	public void setRootFunctionName(String rootFunctionName) {
		this.rootFunctionName = rootFunctionName;
	}

	/**
	 * @return the functionName
	 */
	public String getFunctionName() {
		return functionName;
	}

	/**
	 * @param functionName
	 *            the functionName to set
	 */
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	/**
	 * @return the freelanacerName
	 */
	public String getFreelanacerName() {
		return freelanacerName;
	}

	/**
	 * @param freelanacerName
	 *            the freelanacerName to set
	 */
	public void setFreelanacerName(String freelanacerName) {
		this.freelanacerName = freelanacerName;
	}

	/**
	 * @return the bookingStart
	 */
	public String getBookingStart() {
		return bookingStart;
	}

	/**
	 * @param bookingStart
	 *            the bookingStart to set
	 */
	public void setBookingStart(String bookingStart) {
		this.bookingStart = bookingStart;
	}

	/**
	 * @return the bookingEnd
	 */
	public String getBookingEnd() {
		return bookingEnd;
	}

	/**
	 * @param bookingEnd
	 *            the bookingEnd to set
	 */
	public void setBookingEnd(String bookingEnd) {
		this.bookingEnd = bookingEnd;
	}

	/**
	 * @return the avatar
	 */
	public String getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar
	 *            the avatar to set
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
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

}
