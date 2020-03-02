package com.teamium.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibm.icu.text.DateFormat;
import com.teamium.domain.prod.resources.staff.contract.ContractSetting;
import com.teamium.domain.prod.resources.staff.contract.ContractType;
import com.teamium.domain.prod.resources.staff.contract.EntertainmentContractSetting;
import com.teamium.domain.prod.resources.staff.contract.SocialConvention;

public class ContractSettingDTO {

	/**
	 * Contract settings ID
	 */
	private Long contractId;

	/**
	 * Personal ID in company
	 */
	private Long employeeID;

	/**
	 * Contract type
	 */
	private String contractType;

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
	 * The social convention/ The collective labour agreement
	 */
	private String convention;

	public ContractSettingDTO() {
	}

	public ContractSettingDTO(ContractSetting contractSetting) {
		this.contractId = contractSetting.getId();
		this.employeeID = contractSetting.getEmployeeID();
		this.contractType = contractSetting.getType() != null ? contractSetting.getType().getKey() : null;
		EntertainmentContractSetting entertainmentContractSetting = (EntertainmentContractSetting) contractSetting;
		if (entertainmentContractSetting != null) {
			
			this.dayStart = entertainmentContractSetting.getDayStart();
			this.dayEnd = entertainmentContractSetting.getDayEnd();
			
			this.defaultRate = entertainmentContractSetting.getDefaultRate() != null
					? entertainmentContractSetting.getDefaultRate().getKey()
					: null;
			this.convention = entertainmentContractSetting.getConvention() != null
					? entertainmentContractSetting.getConvention().getKey()
					: null;
		}
	}

	public ContractSettingDTO(Long contractId, Long employeeID, ContractType type) {
		this.contractId = contractId;
		this.employeeID = employeeID;
		this.contractType = type != null ? type.getKey() : null;
	}

	/**
	 * @return the contractId
	 */
	public Long getContractId() {
		return contractId;
	}

	/**
	 * @param contractId
	 *            the contractId to set
	 */
	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	/**
	 * @return the employeeID
	 */
	public Long getEmployeeID() {
		return employeeID;
	}

	/**
	 * @param employeeID
	 *            the employeeID to set
	 */
	public void setEmployeeID(Long employeeID) {
		this.employeeID = employeeID;
	}

	/**
	 * @return the contractType
	 */
	public String getContractType() {
		return contractType;
	}

	/**
	 * @param contractType
	 *            the contractType to set
	 */
	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	/**
	 * @return the dayStart
	 */
	public Date getDayStart() {
		return dayStart;
	}

	/**
	 * @param dayStart
	 *            the dayStart to set
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
	 * @param dayEnd
	 *            the dayEnd to set
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
	 * @param defaultRate
	 *            the defaultRate to set
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
	 * @param convention
	 *            the convention to set
	 */
	public void setConvention(String convention) {
		this.convention = convention;
	}

	@SuppressWarnings("deprecation")
	@JsonIgnore
	public ContractSetting getContractSetting() {
		EntertainmentContractSetting entertainmentContractSetting = new EntertainmentContractSetting();

		ContractType contractType = new ContractType();
		contractType.setKey(this.getContractType());
		entertainmentContractSetting.setType(contractType);

		SocialConvention socialConvention = new SocialConvention();
		socialConvention.setKey(this.getConvention());
		entertainmentContractSetting.setConvention(socialConvention);
		entertainmentContractSetting.setDayStart(this.getDayStart());
		entertainmentContractSetting.setDayEnd(this.getDayEnd());
		entertainmentContractSetting.setEmployeeID(this.getEmployeeID());

		return entertainmentContractSetting;
	}
}
