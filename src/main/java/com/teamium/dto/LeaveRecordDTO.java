package com.teamium.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.LeaveRecord;

@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class LeaveRecordDTO {

	private Long leaveRecordId;
	private LeaveTypeDTO leaveTypeDTO;
	private int debitedLeave;
	private int creditLeave;
	private int remainBalance;

	public LeaveRecordDTO() {

	}

	public LeaveRecordDTO(LeaveRecord leaveRecord) {
		this.leaveRecordId = leaveRecord.getLeaveRecordId();
		this.debitedLeave = leaveRecord.getDebitedLeave();
		this.creditLeave = leaveRecord.getCreditLeave();
		if (leaveRecord.getLeaveType() != null) {
			this.leaveTypeDTO = new LeaveTypeDTO(leaveRecord.getLeaveType());
		}
		this.remainBalance = this.creditLeave - this.debitedLeave;
	}

	public LeaveRecordDTO(LeaveTypeDTO leaveTypeDTO) {
		this.leaveTypeDTO = leaveTypeDTO;
	}

	public LeaveRecord getLeaveRecordEntity(LeaveRecord leaveRecord) {
		leaveRecord.setCreditLeave(this.creditLeave);
		leaveRecord.setDebitedLeave(this.debitedLeave);
		return leaveRecord;
	}

	public Long getLeaveRecordId() {
		return leaveRecordId;
	}

	public void setLeaveRecordId(Long leaveRecordId) {
		this.leaveRecordId = leaveRecordId;
	}

	public LeaveTypeDTO getLeaveTypeDTO() {
		return leaveTypeDTO;
	}

	public void setLeaveTypeDTO(LeaveTypeDTO leaveTypeDTO) {
		this.leaveTypeDTO = leaveTypeDTO;
	}

	public int getDebitedLeave() {
		return debitedLeave;
	}

	public void setDebitedLeave(int debitedLeave) {
		this.debitedLeave = debitedLeave;
	}

	public int getCreditLeave() {
		return creditLeave;
	}

	public void setCreditLeave(int creditLeave) {
		this.creditLeave = creditLeave;
	}

	public int getRemainBalance() {
		return remainBalance;
	}

	public void setRemainBalance(int remainBalance) {
		this.remainBalance = remainBalance;
	}

	@Override
	public String toString() {
		return "LeaveRecordDTO [leaveRecordId=" + leaveRecordId + ", leaveTypeDTO=" + leaveTypeDTO + ", debitedLeave="
				+ debitedLeave + ", creditLeave=" + creditLeave + ", remainBalance=" + remainBalance + "]";
	}

}
