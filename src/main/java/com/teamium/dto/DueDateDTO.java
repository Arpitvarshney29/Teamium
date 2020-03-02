package com.teamium.dto;

import java.util.Calendar;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.AbstractEntity;
import com.teamium.domain.MilestoneType;
import com.teamium.domain.prod.DueDate;

@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class DueDateDTO {

	private Long id;
	private String type;
	private Calendar date;
	private RecordDTO recordDTO;
	private String expirationColor;
	private String dueDate;

	public DueDateDTO() {
	}

	public DueDateDTO(AbstractEntity entity) {
	}

	public DueDateDTO(DueDate dueDate) {
		this.id = dueDate.getId();
		MilestoneType typeEntity = dueDate.getType();
		if (typeEntity != null) {
			this.type = typeEntity.getName();
		}
		this.date = dueDate.getDate();
		DateTime dt = new DateTime(dueDate.getDate().getTime()).withZone(DateTimeZone.UTC);
		this.dueDate = dt.toString();
		int remainingDays = this.getRemainingDays();
		if (remainingDays < 0) {
			this.expirationColor = "RED";
		} else if (remainingDays > 30) {
			this.expirationColor = "GREEN";
		} else {
			this.expirationColor = "ORANGE";
		}

	}

	public DueDateDTO(String type, Calendar date, RecordDTO recordDTO) {
		this.type = type;
		this.date = date;
		this.recordDTO = recordDTO;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the date
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 * @return the recordDTO
	 */
	public RecordDTO getRecordDTO() {
		return recordDTO;
	}

	/**
	 * @param recordDTO the recordDTO to set
	 */
	public void setRecordDTO(RecordDTO recordDTO) {
		this.recordDTO = recordDTO;
	}

	/**
	 * @return the expirationColor
	 */
	public String getExpirationColor() {
		return expirationColor;
	}

	/**
	 * @param expirationColor the expirationColor to set
	 */
	public void setExpirationColor(String expirationColor) {
		this.expirationColor = expirationColor;
	}

	/**
	 * @return the dueDate
	 */
	public String getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * method to get due-date
	 * 
	 * @param dueDate    the dueDate
	 * 
	 * @param dueDateDTO the dueDateDTO
	 * 
	 * @return DueDate object
	 */
	public DueDate getDueDate(DueDate dueDate, DueDateDTO dueDateDTO) {
		setDueDateDetail(dueDate, dueDateDTO);
		return dueDate;
	}

	/**
	 * Method to set due-date
	 * 
	 * @param dueDate    the dueDate
	 * 
	 * @param dueDateDTO the dueDateDTO
	 */
	public void setDueDateDetail(DueDate dueDate, DueDateDTO dueDateDTO) {
		dueDate.setId(dueDateDTO.getId());
		dueDate.setDate(dueDateDTO.getDate());
	}

	public DueDate setDueDateDetail(DueDate dueDate, Calendar dateTime) {
		this.setDate(dateTime);
		this.setDueDateDetail(dueDate, this);
		return dueDate;
	}

	/**
	 * Method to get remaining days for due-date to expire
	 * 
	 * @return count of days to expire
	 */
	public int getRemainingDays() {
		LocalDate currentDate = LocalDate.now();
		TimeZone tz = null;
		DateTimeZone jodaTz = null;
		DateTime dateTime = null;
		if (date != null) {
			tz = date.getTimeZone();
			jodaTz = DateTimeZone.forID(tz.getID());
			dateTime = new DateTime(date.getTimeInMillis(), jodaTz);
			return Days.daysBetween(currentDate, dateTime.toLocalDate()).getDays();
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DueDateDTO [id=" + id + ", type=" + type + ", date=" + date + ", expirationColor=" + expirationColor
				+ "]";
	}

}
