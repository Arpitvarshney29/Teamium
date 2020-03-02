package com.teamium.dto;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.LeaveRequest;
import com.teamium.enums.LeaveRequestStatus;

@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class LeaveRequestDTO {

	private Long leaveRequestId;
	private String leaveRequestRefrenceId;
	private String startTime;
	private String endTime;
	private int numberOfDay;
	private LeaveRecordDTO leaveRecordDTO;
	private String leaveStatus;
	private String comment;
	private FileUploadDTO fileUploadDTO;

	public LeaveRequestDTO() {

	}

	public LeaveRequestDTO(LeaveRequest leaveRequest) {
		this.leaveRequestId = leaveRequest.getLeaveRequestId();
		this.leaveRequestRefrenceId = "LR " + this.leaveRequestId;
		if (leaveRequest.getStartTime() != null) {
			this.startTime = new DateTime(leaveRequest.getStartTime()).withZone(DateTimeZone.UTC).toString();
		}

		if (leaveRequest.getEndTime() != null) {
			this.endTime = new DateTime(leaveRequest.getEndTime()).withZone(DateTimeZone.UTC).toString();
		}
		this.numberOfDay = leaveRequest.getNumberOfDay();
		this.leaveStatus = leaveRequest.getLeaveStatus();
		if (leaveRequest.getLeaveRecord() != null) {
			this.leaveRecordDTO = new LeaveRecordDTO(leaveRequest.getLeaveRecord());
		}
		this.comment = leaveRequest.getComment();
		if (leaveRequest.getAttachment() != null) {
			this.fileUploadDTO = new FileUploadDTO(leaveRequest.getAttachment());
		}
	}

	public LeaveRequest getLeaveRequestEntity(LeaveRequest leaveRequest) {
		leaveRequest.setNumberOfDay(this.numberOfDay);
		if (this.startTime != null) {
			leaveRequest.setStartTime(
					DateTime.parse(this.startTime).withZone(DateTimeZone.forID("Asia/Calcutta")).toDate());
		}
		if (this.endTime != null) {
			leaveRequest
					.setEndTime(DateTime.parse(this.endTime).withZone(DateTimeZone.forID("Asia/Calcutta")).toDate());
		}
		leaveRequest.setComment(this.comment);
		leaveRequest.setLeaveStatus(LeaveRequestStatus.getEnum(this.leaveStatus).getStatus());
		return leaveRequest;
	}

	public Long getLeaveRequestId() {
		return leaveRequestId;
	}

	public void setLeaveRequestId(Long leaveRequestId) {
		this.leaveRequestId = leaveRequestId;
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

	public int getNumberOfDay() {
		return numberOfDay;
	}

	public void setNumberOfDay(int numberOfDay) {
		this.numberOfDay = numberOfDay;
	}

	public LeaveRecordDTO getLeaveRecordDTO() {
		return leaveRecordDTO;
	}

	public void setLeaveRecordDTO(LeaveRecordDTO leaveRecordDTO) {
		this.leaveRecordDTO = leaveRecordDTO;
	}

	public String getLeaveRequestRefrenceId() {
		return leaveRequestRefrenceId;
	}

	public void setLeaveRequestRefrenceId(String leaveRequestRefrenceId) {
		this.leaveRequestRefrenceId = leaveRequestRefrenceId;
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
	 * @return the fileUploadDTO
	 */
	public FileUploadDTO getFileUploadDTO() {
		return fileUploadDTO;
	}

	/**
	 * @param fileUploadDTO
	 *            the fileUploadDTO to set
	 */
	public void setFileUploadDTO(FileUploadDTO fileUploadDTO) {
		this.fileUploadDTO = fileUploadDTO;
	}

}
