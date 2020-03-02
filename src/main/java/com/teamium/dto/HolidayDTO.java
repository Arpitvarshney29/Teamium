package com.teamium.dto;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.Holiday;

@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class HolidayDTO {
	private Long id;

	private Long laborRuleId;
	/**
	 * Holiday Name
	 */
	private String holidayName;

	/**
	 * Holiday Date
	 */
	private String holidayDate;

	public HolidayDTO() {
	}

	public HolidayDTO(Holiday holiday) {
		this.id = holiday.getId();
		this.holidayName = holiday.getHolidayName();
		if (holiday.getHolidayDate() != null) {
			this.holidayDate = new DateTime(holiday.getHolidayDate()).withZone(DateTimeZone.UTC).toString();
		}
	}

	/**
	 * Set value into entity from DTO.
	 * 
	 * @param holiday
	 * @return Entity of Holiday.
	 */
	public Holiday getHolidayEntity(Holiday holiday) {
		holiday.setHolidayName(this.holidayName);
		if (this.holidayDate != null) {
			holiday.setHolidayDate(
					DateTime.parse(this.holidayDate).withZone(DateTimeZone.forID("Asia/Calcutta")).toDate());
		}
		return holiday;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the holidayName
	 */
	public String getHolidayName() {
		return holidayName;
	}

	/**
	 * @param holidayName
	 *            the holidayName to set
	 */
	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}

	/**
	 * @return the holidayDate
	 */
	public String getHolidayDate() {
		return holidayDate;
	}

	/**
	 * @param holidayDate
	 *            the holidayDate to set
	 */
	public void setHolidayDate(String holidayDate) {
		this.holidayDate = holidayDate;
	}

	/**
	 * @return the laborRuleId
	 */
	public Long getLaborRuleId() {
		return laborRuleId;
	}

	/**
	 * @param laborRuleId
	 *            the laborRuleId to set
	 */
	public void setLaborRuleId(Long laborRuleId) {
		this.laborRuleId = laborRuleId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HolidayDTO [id=" + id + ", laborRuleId=" + laborRuleId + ", holidayName=" + holidayName
				+ ", holidayDate=" + holidayDate + "]";
	}

}
