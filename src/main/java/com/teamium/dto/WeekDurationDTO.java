package com.teamium.dto;

import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class WeekDurationDTO {
	private DateTime userStartTime;
	private DateTime userEndTime;
	private int weekNumber;
	private long totalDuration;
	private String monthName;
	private boolean isSick;

	public WeekDurationDTO() {
	}

	public WeekDurationDTO(DateTime userStartTime, DateTime userEndTime, boolean isSick) {
		this.userStartTime = userStartTime;
		this.userEndTime = userEndTime;
		this.isSick = isSick;
	}

	public WeekDurationDTO(int weekNumber, long totalDuration) {
		this.weekNumber = weekNumber;
		this.totalDuration = totalDuration;
	}

	public WeekDurationDTO(String monthName, long totalDuration) {
		this.monthName = monthName;
		this.totalDuration = totalDuration;
		this.userStartTime = userStartTime;
		this.userEndTime = userEndTime;
	}

	public WeekDurationDTO(DateTime startDate, float totalDuration2) {
		// TODO Auto-generated constructor stub
	}

	public DateTime getUserStartTime() {
		return userStartTime;
	}

	public void setUserStartTime(DateTime userStartTime) {
		this.userStartTime = userStartTime;
	}

	public DateTime getUserEndTime() {
		return userEndTime;
	}

	public void setUserEndTime(DateTime userEndTime) {
		this.userEndTime = userEndTime;
	}

	public int getWeekNumber() {
		return weekNumber;
	}

	public void setWeekNumber(int weekNumber) {
		this.weekNumber = weekNumber;
	}

	public long getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(long totalDuration) {
		this.totalDuration = totalDuration;
	}

	/**
	 * @return the monthName
	 */
	public String getMonthName() {
		return monthName;
	}

	/**
	 * @param monthName
	 *            the monthName to set
	 */
	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}

	/**
	 * 
	 * @return
	 */

	public boolean isSick() {
		return isSick;
	}

	/**
	 * 
	 * @param isSick
	 */
	public void setSick(boolean isSick) {
		this.isSick = isSick;
	}

	@Override
	public String toString() {
		return "WeekDurationDTO [userStartTime=" + userStartTime + ", userEndTime=" + userEndTime + ", weekNumber="
				+ weekNumber + ", totalDuration=" + totalDuration + "]";
	}

}