package com.teamium.dto;

import java.util.ArrayList;
import java.util.List;

public class RecurrenceDTO {

	private String startDate;
	private String endDate;
	List<String> days = new ArrayList<>();
	private Integer plusWeeks = 1;
	private Long cloneEventId;

	public RecurrenceDTO() {

	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<String> getDays() {
		return days;
	}

	public void setDays(List<String> days) {
		this.days = days;
	}

	public Integer getPlusWeeks() {
		return plusWeeks;
	}

	public void setPlusWeeks(Integer plusWeeks) {
		this.plusWeeks = plusWeeks;
	}

	public Long getCloneEventId() {
		return cloneEventId;
	}

	public void setCloneEventId(Long cloneEventId) {
		this.cloneEventId = cloneEventId;
	}

	@Override
	public String toString() {
		return "RecurrenceDTO [startDate=" + startDate + ", endDate=" + endDate + ", days=" + days + ", plusWeeks="
				+ plusWeeks + ", cloneEventId=" + cloneEventId + "]";
	}

}
