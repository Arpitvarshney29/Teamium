package com.teamium.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "t_holiday")
public class Holiday {

	/**
	 * Id
	 */
	@Id
	@Column(name = "c_idholiday", insertable = false, updatable = false)
	@TableGenerator(name = "idHoliday_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "hoilday_idholiday_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idHoliday_seq")
	private Long id;

	/**
	 * Holiday Name
	 */
	@Column(name = "c_holiday_name")
	private String holidayName;

	/**
	 * Holiday Date
	 */
	@Column(name = "c_holiday_date")
	@Temporal(TemporalType.DATE)
	private Date holidayDate;

	public Holiday() {
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
	public Date getHolidayDate() {
		return holidayDate;
	}

	/**
	 * @param holidayDate
	 *            the holidayDate to set
	 */
	public void setHolidayDate(Date holidayDate) {
		this.holidayDate = holidayDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Holiday [id=" + id + ", holidayName=" + holidayName + ", holidayDate=" + holidayDate + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Holiday other = (Holiday) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
