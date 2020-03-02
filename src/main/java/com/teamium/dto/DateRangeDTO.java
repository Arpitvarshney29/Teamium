package com.teamium.dto;

import java.util.Calendar;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.AbstractEntity;

/**
 * Wrapper class for Date-Range
 * 
 * @author Teamium
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class DateRangeDTO {

	private String start;
	private String end;
	private String status;

	private Calendar startDate;
	private Calendar endDate;
	private boolean spreadsheet;
	private boolean previousMonthData;
	private String actualComparison;
	private long duration;

	public DateRangeDTO() {

	}

	public DateRangeDTO(Calendar startDate, Calendar endDate, boolean spreadsheet) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.spreadsheet = spreadsheet;
	}

	public DateRangeDTO(Calendar startDate, Calendar endDate, String status, boolean spreadsheet) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.spreadsheet = spreadsheet;
	}

	/**
	 * @return the start
	 */
	public String getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(String start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public String getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(String end) {
		this.end = end;
	}

	/**
	 * @return the startDate
	 */
	public Calendar getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Calendar getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the spreadsheet
	 */
	public boolean isSpreadsheet() {
		return spreadsheet;
	}

	/**
	 * @param spreadsheet the spreadsheet to set
	 */
	public void setSpreadsheet(boolean spreadsheet) {
		this.spreadsheet = spreadsheet;
	}

	/**
	 * @return the previousMonthData
	 */
	public boolean isPreviousMonthData() {
		return previousMonthData;
	}

	/**
	 * @param previousMonthData the previousMonthData to set
	 */
	public void setPreviousMonthData(boolean previousMonthData) {
		this.previousMonthData = previousMonthData;
	}

	/**
	 * @return the actualComparison
	 */
	public String getActualComparison() {
		return actualComparison;
	}

	/**
	 * @param actualComparison the actualComparison to set
	 */
	public void setActualComparison(String actualComparison) {
		this.actualComparison = actualComparison;
	}

	/**
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DateRangeDTO [start=" + start + ", end=" + end + ", status=" + status + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", spreadsheet=" + spreadsheet + ", previousMonthData=" + previousMonthData
				+ ", actualComparison=" + actualComparison + "]";
	}

}
