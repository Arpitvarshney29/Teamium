package com.teamium.dto;

import com.teamium.domain.TimeZone;

public class TimeZoneDTO {

	private Long id;
	private String offset;
	private String zoneName;
	private boolean isCheck;

	public TimeZoneDTO() {
		super();
	}

	public TimeZoneDTO(TimeZone timeZone) {
		this.id = timeZone.getId();
		this.offset = timeZone.getOffset();
		this.zoneName = timeZone.getZoneName();
		this.isCheck = timeZone.isCheck();
	}

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

}
