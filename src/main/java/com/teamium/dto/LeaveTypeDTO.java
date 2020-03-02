package com.teamium.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.LeaveType;

@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class LeaveTypeDTO {
	private Long leaveTypeId;
	private String type;
	private boolean active;

	public LeaveTypeDTO() {

	}

	public LeaveTypeDTO(LeaveType leaveType) {
		this.leaveTypeId = leaveType.getLeaveTypeId();
		this.type = leaveType.getType();
		this.active = leaveType.isActive();
	}

	public LeaveType getLeaveTypeEntity(LeaveType leaveType) {
		leaveType.setType(this.type);
		leaveType.setActive(Boolean.TRUE);
		return leaveType;
	}

	public Long getLeaveTypeId() {
		return leaveTypeId;
	}

	public void setLeaveTypeId(Long leaveTypeId) {
		this.leaveTypeId = leaveTypeId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((leaveTypeId == null) ? 0 : leaveTypeId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LeaveTypeDTO other = (LeaveTypeDTO) obj;
		if (leaveTypeId == null) {
			if (other.leaveTypeId != null)
				return false;
		} else if (!leaveTypeId.equals(other.leaveTypeId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LeaveTypeDTO [leaveTypeId=" + leaveTypeId + ", type=" + type + ", active=" + active + "]";
	}

}
