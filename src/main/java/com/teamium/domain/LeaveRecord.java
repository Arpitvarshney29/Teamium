package com.teamium.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "t_leave_record")
public class LeaveRecord {

	/**
	 * LabourRecord ID
	 */
	@Id
	@Column(name = "c_leave_record_id", insertable = false, updatable = false)
	@TableGenerator(name = "idLeaveRecord_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "leaverecord_idleaverecord_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idLeaveRecord_seq")
	private Long leaveRecordId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "c_leave_type_id")
	private LeaveType leaveType;

	/**
	 * Debited Leave.
	 */
	@Column(name = "c_debit_leave")
	private int debitedLeave;

	/**
	 * Credit Leave.
	 */
	@Column(name = "c_credit_leave")
	private int creditLeave;

	public LeaveRecord() {

	}

	public Long getLeaveRecordId() {
		return leaveRecordId;
	}

	public void setLeaveRecordId(Long leaveRecordId) {
		this.leaveRecordId = leaveRecordId;
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

	public LeaveType getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}

}
