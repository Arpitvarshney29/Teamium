package com.teamium.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "t_timezone")
public class TimeZone {

	@Id
	@Column(name = "c_idtimezone", insertable = false, updatable = false)
	@TableGenerator(name = "idTimezone_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "idtimezone_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idTimezone_seq")
	private Long id;

	@Column(name = "c_offset")
	private String offset;

	@Column(name = "c_zone_name")
	private String zoneName;

	@Column(name = "c_ischeck")
	private boolean isCheck;

	/**
	 * 
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 
	 * @return
	 */
	public String getOffset() {
		return offset;
	}

	/**
	 * 
	 * @param offset
	 */
	public void setOffset(String offset) {
		this.offset = offset;
	}

	/**
	 * 
	 * @return
	 */
	public String getZoneName() {
		return zoneName;
	}

	/**
	 * 
	 * @param zoneName
	 */
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isCheck() {
		return isCheck;
	}

	/**
	 * 
	 * @param isCheck
	 */
	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	@Override
	public String toString() {
		return "TimeZone [id=" + id + ", offset=" + offset + ", zoneName=" + zoneName + ", isCheck=" + isCheck + "]";
	}

}
