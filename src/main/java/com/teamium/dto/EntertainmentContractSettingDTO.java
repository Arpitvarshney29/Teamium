package com.teamium.dto;

import java.util.Date;

public class EntertainmentContractSettingDTO extends ContractSettingDTO {

	
	/**
	 * Start day time
	 */
	private Date dayStart;
	
	/**
	 * End day Time
	 */
	private Date dayEnd;
	
	private String defaultRate;
	
	/**
	 * The social convention
	 */
	private String convention;

	/**
	 * @return the dayStart
	 */
	public Date getDayStart() {
		return dayStart;
	}

	/**
	 * @param dayStart the dayStart to set
	 */
	public void setDayStart(Date dayStart) {
		this.dayStart = dayStart;
	}

	/**
	 * @return the dayEnd
	 */
	public Date getDayEnd() {
		return dayEnd;
	}

	/**
	 * @param dayEnd the dayEnd to set
	 */
	public void setDayEnd(Date dayEnd) {
		this.dayEnd = dayEnd;
	}

	/**
	 * @return the defaultRate
	 */
	public String getDefaultRate() {
		return defaultRate;
	}

	/**
	 * @param defaultRate the defaultRate to set
	 */
	public void setDefaultRate(String defaultRate) {
		this.defaultRate = defaultRate;
	}

	/**
	 * @return the convention
	 */
	public String getConvention() {
		return convention;
	}

	/**
	 * @param convention the convention to set
	 */
	public void setConvention(String convention) {
		this.convention = convention;
	}
	
}
