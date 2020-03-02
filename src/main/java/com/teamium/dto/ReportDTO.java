package com.teamium.dto;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;

import org.joda.time.DateTime;

public class ReportDTO {
	private int week;
	private String monthName;
	private float workedHour;
	private float sickHourDuration;
	private float balance;
	private String projectName;
	private String functionName;
	private String description;
	private boolean sickHour;
	private String dateString;

	public ReportDTO() {

	}

	public ReportDTO(int week, float workedHour, float balance) {
		this.week = week;
		setWorkedHour(workedHour);
		setBalance(balance);
	}

	public ReportDTO(String monthName, float totalDuration, float balance, boolean sickHour) {

		this.monthName = monthName;
		this.workedHour = totalDuration;
		this.balance = balance;
		this.sickHour = sickHour;
	}

	/**
	 * 
	 * @return
	 */
	public int getWeek() {
		return week;
	}

	/**
	 * 
	 * @param week
	 */
	public void setWeek(int week) {
		this.week = week;
	}

	/**
	 * 
	 * @return
	 */
	public float getWorkedHour() {
		return workedHour;
	}

	/**
	 * 
	 * @param workedHour
	 */
	public void setWorkedHour(float workedHour) {
		this.workedHour = new BigDecimal(workedHour).setScale(1,BigDecimal.ROUND_CEILING).floatValue();

	}

	/**
	 * 
	 * @param balance
	 */
	public void setBalance(float balance) {
		this.balance = new BigDecimal(balance).setScale(1,BigDecimal.ROUND_CEILING).floatValue();
	}

	/**
	 * 
	 * @return
	 */
	public float getBalance() {
		return balance;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isSickHour() {
		return sickHour;
	}

	/**
	 * 
	 * @param sickHour
	 */
	public void setSickHour(boolean sickHour) {
		this.sickHour = sickHour;
	}

	/**
	 * 
	 * @return
	 */
	public String getMonthName() {
		return monthName;
	}

	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}

	/**
	 * 
	 * @return
	 */
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * 
	 * @return
	 */
	public String getFunctionName() {
		return functionName;
	}

	/**
	 * 
	 * @param functionName
	 */
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	/**
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return
	 */
	public String getDateString() {
		return dateString;
	}

	/**
	 * 
	 * @param dateString
	 */
	public void setDateString(String dateString) {
		DateTime d = new DateTime(dateString);
		this.dateString = new SimpleDateFormat("dd/MM/yyyy").format(d.toDate());

	}

	/**
	 * 
	 * @return
	 */
	public float getSickHourDuration() {
		return sickHourDuration;
	}

	/**
	 * 
	 * @param sickHourDuration
	 */
	public void setSickHourDuration(float sickHourDuration) {
		this.sickHourDuration = sickHourDuration;
	}

}
