package com.teamium.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.prod.resources.equipments.Attachment;

@Entity
@Table(name = "t_leave_request")
public class LeaveRequest {

	/**
	 * LeaveRequest ID
	 */
	@Id
	@Column(name = "c_leave_request_id", insertable = false, updatable = false)
	@TableGenerator(name = "idLevaeRequest_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "leaverequest_idleaverequest_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idLevaeRequest_seq")
	private Long leaveRequestId;

	/**
	 * Start Time
	 */
	@Column(name = "c_start_time")
	private Date startTime;

	/**
	 * End Time
	 */
	@Column(name = "c_end_time")
	private Date endTime;

	/**
	 * Number of Day.
	 */
	@Column(name = "c_number_of_day")
	private int numberOfDay;

	/**
	 * Status of the leave.
	 */
	@Column(name = "c_leave_status")
	private String leaveStatus;

	/**
	 * comment on the leave.
	 */
	@Column(name = "c_comment")
	private String comment;

	/**
	 * Leave Record.
	 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "c_leave_record_id")
	private LeaveRecord leaveRecord;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "c_attachment_id", nullable = true)
	private Attachment attachment;

	public LeaveRequest() {

	}

	public Long getLeaveRequestId() {
		return leaveRequestId;
	}

	public void setLeaveRequestId(Long leaveRequestId) {
		this.leaveRequestId = leaveRequestId;
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

	public int getNumberOfDay() {
		return numberOfDay;
	}

	public void setNumberOfDay(int numberOfDay) {
		this.numberOfDay = numberOfDay;
	}

	public LeaveRecord getLeaveRecord() {
		return leaveRecord;
	}

	public void setLeaveRecord(LeaveRecord leaveRecord) {
		this.leaveRecord = leaveRecord;
	}

	public String getLeaveStatus() {
		return leaveStatus;
	}

	public void setLeaveStatus(String leaveStatus) {
		this.leaveStatus = leaveStatus;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the attachment
	 */
	public Attachment getAttachment() {
		return attachment;
	}

	/**
	 * @param attachment
	 *            the attachment to set
	 */
	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

}
