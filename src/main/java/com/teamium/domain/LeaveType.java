package com.teamium.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "t_leave_type")
public class LeaveType {

	/**
	 * LabourRule ID
	 */
	@Id
	@Column(name = "c_leave_type_id", insertable = false, updatable = false)
	@TableGenerator(name = "idLevaeType_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "leavetype_idleavetype_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idLevaeType_seq")
	private Long leaveTypeId;

	/**
	 * Name of the Leave
	 */
	@Column(name = "c_type")
	private String type;

	/**
	 * Active of the Leave
	 */
	@Column(name = "c_active")
	private boolean active;

	public LeaveType() {

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

}
