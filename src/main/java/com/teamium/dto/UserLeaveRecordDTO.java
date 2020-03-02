package com.teamium.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.UserLeaveRecord;
import com.teamium.enums.LeaveRequestStatus;

@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UserLeaveRecordDTO {

	private Long staffMemberId;
	private Long userLeaveRecordId;
	private List<LeaveRecordDTO> leaveRecordDTOs = new ArrayList<>();
	private List<LeaveRecordDTO> availableLeaveRecordDTOs = new ArrayList<>();
	private List<LeaveRequestDTO> leaveRequestDTOs = new ArrayList<>();
	private List<String> leaveStatus = new ArrayList<String>();
	private LeaveRecordDTO changeLeaveRecordDTO;
	private LeaveRequestDTO changeLeaveRequestDTO;

	public UserLeaveRecordDTO() {

	}

	public UserLeaveRecordDTO(UserLeaveRecord userLeaveRecord) {
		this.userLeaveRecordId = userLeaveRecord.getUserLeaveRecordId();
		this.leaveRecordDTOs = userLeaveRecord.getLeaveRecords().stream().map(LeaveRecordDTO::new)
				.sorted((dt,dt1)->dt.getLeaveRecordId().compareTo(dt1.getLeaveRecordId())).collect(Collectors.toList());
		this.leaveRequestDTOs = userLeaveRecord.getLeaveRequests().stream().map(LeaveRequestDTO::new)
				.sorted((dt,dt1)->dt.getLeaveRequestId().compareTo(dt1.getLeaveRequestId())).collect(Collectors.toList());
		this.leaveStatus = LeaveRequestStatus.getLeaveStatus();
	}

	public UserLeaveRecord getUserLeaveRecordEntity(UserLeaveRecord userLeaveRecord) {
		return userLeaveRecord;
	}

	public Long getUserLeaveRecordId() {
		return userLeaveRecordId;
	}

	public void setUserLeaveRecordId(Long userLeaveRecordId) {
		this.userLeaveRecordId = userLeaveRecordId;
	}

	public List<LeaveRecordDTO> getLeaveRecordDTOs() {
		return leaveRecordDTOs;
	}

	public void setLeaveRecordDTOs(List<LeaveRecordDTO> leaveRecordDTOs) {
		this.leaveRecordDTOs = leaveRecordDTOs;
	}

	public Long getStaffMemberId() {
		return staffMemberId;
	}

	public void setStaffMemberId(Long staffMemberId) {
		this.staffMemberId = staffMemberId;
	}

	public List<LeaveRecordDTO> getAvailableLeaveRecordDTOs() {
		return availableLeaveRecordDTOs;
	}

	public void setAvailableLeaveRecordDTOs(List<LeaveRecordDTO> availableLeaveRecordDTOs) {
		this.availableLeaveRecordDTOs = availableLeaveRecordDTOs;
	}

	public LeaveRecordDTO getChangeLeaveRecordDTO() {
		return changeLeaveRecordDTO;
	}

	public void setChangeLeaveRecordDTO(LeaveRecordDTO changeLeaveRecordDTO) {
		this.changeLeaveRecordDTO = changeLeaveRecordDTO;
	}

	public List<LeaveRequestDTO> getLeaveRequestDTOs() {
		return leaveRequestDTOs;
	}

	public void setLeaveRequestDTOs(List<LeaveRequestDTO> leaveRequestDTOs) {
		this.leaveRequestDTOs = leaveRequestDTOs;
	}

	public LeaveRequestDTO getChangeLeaveRequestDTO() {
		return changeLeaveRequestDTO;
	}

	public void setChangeLeaveRequestDTO(LeaveRequestDTO changeLeaveRequestDTO) {
		this.changeLeaveRequestDTO = changeLeaveRequestDTO;
	}

	public List<String> getLeaveStatus() {
		return leaveStatus;
	}

	public void setLeaveStatus(List<String> leaveStatus) {
		this.leaveStatus = leaveStatus;
	}

	@Override
	public String toString() {
		return "UserLeaveRecordDTO [staffMemberId=" + staffMemberId + ", userLeaveRecordId=" + userLeaveRecordId
				+ ", leaveRecordDTOs=" + leaveRecordDTOs + ", availableLeaveRecordDTOs=" + availableLeaveRecordDTOs
				+ ", leaveRequestDTOs=" + leaveRequestDTOs + ", changeLeaveRecordDTO=" + changeLeaveRecordDTO
				+ ", changeLeaveRequestDTO=" + changeLeaveRequestDTO + "]";
	}

}
