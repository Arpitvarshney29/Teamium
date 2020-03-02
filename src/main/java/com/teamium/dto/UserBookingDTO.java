package com.teamium.dto;


import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.UserBooking;

@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UserBookingDTO {

	private Long id;

	/**
	 * Name of the reminder
	 */
	private String text;

	/**
	 * Start Time
	 */
	private String startTime;

	/**
	 * Night Time
	 */
	private String endTime;

	/**
	 * Reminder for staff.
	 */
	private Long staffMemberId;

	/**
	 * Start Time
	 */
	private String userStartTime;

	/**
	 * Night Time
	 */
	private String userEndTime;

	/**
	 * Booking
	 */
	private Long bookingId;

	/**
	 * Theme
	 */
	private String theme;

	/**
	 * Function Name
	 */
	private String functionName;

	/**
	 * Sick
	 */
	private boolean sick;

	public UserBookingDTO() {

	}

	public UserBookingDTO(UserBooking userBooking) {
		if (userBooking.getUserStartTime() != null) {
			this.userStartTime = new DateTime(userBooking.getUserStartTime()).withZone(DateTimeZone.UTC).toString();
		}

		if (userBooking.getUserEndTime() != null) {
			this.userEndTime = new DateTime(userBooking.getUserEndTime()).withZone(DateTimeZone.UTC).toString();
		}
		if (userBooking.getStartTime() != null) {
			this.startTime = new DateTime(userBooking.getStartTime()).withZone(DateTimeZone.UTC).toString();
		}

		if (userBooking.getEndTime() != null) {
			this.endTime = new DateTime(userBooking.getEndTime()).withZone(DateTimeZone.UTC).toString();
		}
		this.text = userBooking.getName();
		this.id = userBooking.getUserBookingId();
		if (userBooking.getStaffMember() != null) {
			this.staffMemberId = userBooking.getStaffMember().getId();
		}
		this.bookingId = userBooking.getBookingId();
		this.theme = userBooking.getTheme();
		if(userBooking.getSick()!=null) {
			this.sick=userBooking.getSick();
		}
	}

	public UserBooking getUserBookingEntity(UserBooking userBooking) {
		if (this.userStartTime != null) {
			userBooking.setUserStartTime(
					DateTime.parse(this.userStartTime).withZone(DateTimeZone.forID("Asia/Calcutta")).toDate());
		}
		if (this.userEndTime != null) {
			userBooking.setUserEndTime(
					DateTime.parse(this.userEndTime).withZone(DateTimeZone.forID("Asia/Calcutta")).toDate());
		}
		if (userBooking.getBookingId() == null) {
			userBooking.setStartTime(userBooking.getUserStartTime());
			userBooking.setEndTime(userBooking.getUserEndTime());
		}

		userBooking.setName(this.text);
		if (this.theme != null) {
			userBooking.setTheme(this.theme);
		}
		userBooking.setSick(this.sick);
		return userBooking;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Long getStaffMemberId() {
		return staffMemberId;
	}

	public void setStaffMemberId(Long staffMemberId) {
		this.staffMemberId = staffMemberId;
	}

	public String getUserStartTime() {
		return userStartTime;
	}

	public void setUserStartTime(String userStartTime) {
		this.userStartTime = userStartTime;
	}

	public String getUserEndTime() {
		return userEndTime;
	}

	public void setUserEndTime(String userEndTime) {
		this.userEndTime = userEndTime;
	}

	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
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

	@Override
	public String toString() {
		return "UserBookingDTO [id=" + id + ", text=" + text + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", staffMemberId=" + staffMemberId + "]";
	}

}
