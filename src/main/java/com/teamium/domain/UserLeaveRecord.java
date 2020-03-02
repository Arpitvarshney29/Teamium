package com.teamium.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "t_user_leave_record")
public class UserLeaveRecord {

	/**
	 * LabourRecord ID
	 */
	@Id
	@Column(name = "c_user_leave_record_id", insertable = false, updatable = false)
	@TableGenerator(name = "idUserLeaveRecord_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "userleaverecord_iduserleaverecord_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idUserLeaveRecord_seq")
	private Long userLeaveRecordId;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "leave_record_id")
	private List<LeaveRecord> leaveRecords = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "leave_request_id")
	private List<LeaveRequest> leaveRequests = new ArrayList<>();

	public UserLeaveRecord() {

	}

	public Long getUserLeaveRecordId() {
		return userLeaveRecordId;
	}

	public void setUserLeaveRecordId(Long userLeaveRecordId) {
		this.userLeaveRecordId = userLeaveRecordId;
	}

	public List<LeaveRecord> getLeaveRecords() {
		return leaveRecords;
	}

	public void setLeaveRecords(List<LeaveRecord> leaveRecords) {
		this.leaveRecords = leaveRecords;
	}

	public List<LeaveRequest> getLeaveRequests() {
		return leaveRequests;
	}

	public void setLeaveRequests(List<LeaveRequest> leaveRequests) {
		this.leaveRequests = leaveRequests;
	}

}
