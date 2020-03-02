package com.teamium.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.prod.resources.staff.StaffMember;

@Entity
@Table(name = "t_user_booking")
public class UserBooking {
	/**
	 * LabourRule ID
	 */
	@Id
	@Column(name = "c_user_booking_id", insertable = false, updatable = false)
	@TableGenerator(name = "idUserBooking_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "userbooking_iduserbooking_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idUserBooking_seq")
	private Long userBookingId;

	/**
	 * Name of the reminder
	 */
	@Column(name = "c_name")
	private String name;

	/**
	 * Start Time
	 */
	@Column(name = "c_start_time")
	private Date startTime;

	/**
	 * Night Time
	 */
	@Column(name = "c_end_time")
	private Date endTime;

	/**
	 * Reminder for staff.
	 */
	@ManyToOne
	@JoinColumn(name = "c_user")
	private StaffMember staffMember;

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
	 * Booking
	 */
	@Column(name = "c_booking_id")
	private Long bookingId;

	/**
	 * Theme
	 */
	@Column(name = "c_theme")
	private String theme;

	/**
	 * Sick
	 */
	@Column(name = "c_sick")
	private Boolean sick;

	public UserBooking() {

	}

	public UserBooking(Date userStartTime, Date userEndTime) {
		this.userStartTime = userStartTime;
		this.userEndTime = userEndTime;
	}

	public Long getUserBookingId() {
		return userBookingId;
	}

	public void setUserBookingId(Long userBookingId) {
		this.userBookingId = userBookingId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public StaffMember getStaffMember() {
		return staffMember;
	}

	public void setStaffMember(StaffMember staffMember) {
		this.staffMember = staffMember;
	}

	public Date getUserStartTime() {
		return userStartTime;
	}

	public void setUserStartTime(Date userStartTime) {
		this.userStartTime = userStartTime;
	}

	public Date getUserEndTime() {
		return userEndTime;
	}

	public void setUserEndTime(Date userEndTime) {
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

	/**
	 * @return the sick
	 */
	public Boolean getSick() {
		return sick;
	}

	/**
	 * @param sick
	 *            the sick to set
	 */
	public void setSick(Boolean sick) {
		this.sick = sick;
	}

}
