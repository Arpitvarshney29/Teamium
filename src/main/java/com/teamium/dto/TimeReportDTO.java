package com.teamium.dto;

import java.util.List;

public class TimeReportDTO {
	private String employeeCode;
	private String staffName;
	private long nominalHour;
	private byte activityRate;
	private List<ReportDTO> report;
	private String totalWorkedHours;
	private String totalNominalHours;
	private String balance;
	private long id;
	private String totalSickHours;
	private String overTimeHours;

	public TimeReportDTO() {

	}

	/**
	 * 
	 * @return
	 */
	public String getEmployeeCode() {
		return employeeCode;
	}

	/**
	 * 
	 * @param employeeCode
	 */
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	/**
	 * 
	 * @return
	 */
	public String getStaffName() {
		return staffName;
	}

	/**
	 * 
	 * @param staffName
	 */
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	/**
	 * 
	 * @return
	 */
	public long getNominalHour() {
		return nominalHour;
	}

	/**
	 * 
	 * @param nominalHour
	 */
	public void setNominalHour(long nominalHour) {
		this.nominalHour = nominalHour;
	}

	/**
	 * 
	 * @return
	 */
	public List<ReportDTO> getReport() {
		return report;
	}

	/**
	 * 
	 * @param report
	 */
	public void setReport(List<ReportDTO> report) {
		this.report = report;
	}

	/**
	 * 
	 * @return
	 */
	public String getTotalWorkedHours() {
		return totalWorkedHours;
	}

	/**
	 * 
	 * @param totalWorkedHours
	 */
	public void setTotalWorkedHours(String totalWorkedHours) {
		this.totalWorkedHours = totalWorkedHours;
	}

	/**
	 * 
	 * @return
	 */
	public String getTotalNominalHours() {
		return totalNominalHours;
	}

	/**
	 * 
	 * @param totalNominalHours
	 */
	public void setTotalNominalHours(String totalNominalHours) {
		this.totalNominalHours = totalNominalHours;
	}

	/**
	 * 
	 * @return
	 */
	public byte getActivityRate() {
		return activityRate;
	}

	/**
	 * 
	 * @param activityRate
	 */
	public void setActivityRate(byte activityRate) {
		this.activityRate = activityRate;
	}

	/**
	 * 
	 * @return
	 */
	public String getBalance() {
		return balance;
	}

	/**
	 * 
	 * @param balance
	 */
	public void setBalance(String balance) {
		this.balance = balance;
	}

	/**
	 * 
	 * @return
	 */
	public long getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 
	 * @return
	 */
	public String getTotalSickHours() {
		return totalSickHours;
	}

	/**
	 * 
	 * @param totalSickHours
	 */
	public void setTotalSickHours(String totalSickHours) {
		this.totalSickHours = totalSickHours;
	}

	/**
	 * 
	 * @return
	 */
	public String getOverTimeHours() {
		return overTimeHours;
	}

	/**
	 * 
	 * @param overTimeHours
	 */
	public void setOverTimeHours(String overTimeHours) {
		this.overTimeHours = overTimeHours;
	}

}
