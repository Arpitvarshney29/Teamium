package com.teamium.dto;

import java.util.Map;

public class FinancialDataDTO {

	private String title;
	private String projectStatus;
	private String financialStatus;
	private String managedBy;
	private String origin;
	private String comapny;
	private String currency;
	private String language;
	private String internalReference;
	private Map<String, FinancialGraphDataDTO> financialGraphData;
	private Map<String, FinancialGraphDataDTO> byPhaseData;
	private Map<String, FinancialGraphDataDTO> byFunctionData;
	private Map<String, FinancialGraphDataDTO> total;

	public FinancialDataDTO() {

	}

	public FinancialDataDTO(String title, String projectStatus, String financialStatus, String managedBy, String origin,
			String comapny, String currency, String language, String internalReference,
			Map<String, FinancialGraphDataDTO> financialGraphData, Map<String, FinancialGraphDataDTO> byPhaseData,
			Map<String, FinancialGraphDataDTO> byFunctionData, Map<String, FinancialGraphDataDTO> total) {

		this.title = title;
		this.projectStatus = projectStatus;
		this.financialStatus = financialStatus;
		this.managedBy = managedBy;
		this.origin = origin;
		this.comapny = comapny;
		this.currency = currency;
		this.language = language;
		this.internalReference = internalReference;
		this.financialGraphData = financialGraphData;
		this.byPhaseData = byPhaseData;
		this.byFunctionData = byFunctionData;
		this.total = total;
	}

	/**
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 
	 * @return
	 */
	public String getProjectStatus() {
		return projectStatus;
	}

	/**
	 * 
	 * @param projectStatus
	 */
	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	/**
	 * 
	 * @return
	 */
	public String getFinancialStatus() {
		return financialStatus;
	}

	/**
	 * 
	 * @param financialStatus
	 */
	public void setFinancialStatus(String financialStatus) {
		this.financialStatus = financialStatus;
	}

	/**
	 * 
	 * @return
	 */
	public String getManagedBy() {
		return managedBy;
	}

	/**
	 * 
	 * @param managedBy
	 */
	public void setManagedBy(String managedBy) {
		this.managedBy = managedBy;
	}

	/**
	 * 
	 * @return
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * 
	 * @param origin
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	/**
	 * 
	 * @return
	 */
	public String getComapny() {
		return comapny;
	}

	/**
	 * 
	 * @param comapny
	 */
	public void setComapny(String comapny) {
		this.comapny = comapny;
	}

	/**
	 * 
	 * @return
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * 
	 * @param currency
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * 
	 * @return
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * 
	 * @param language
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * 
	 * @return
	 */
	public String getInternalReference() {
		return internalReference;
	}

	/**
	 * 
	 * @param internalReference
	 */
	public void setInternalReference(String internalReference) {
		this.internalReference = internalReference;
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, FinancialGraphDataDTO> getFinancialGraphData() {
		return financialGraphData;
	}

	/**
	 * 
	 * @param financialGraphData
	 */
	public void setFinancialGraphData(Map<String, FinancialGraphDataDTO> financialGraphData) {
		this.financialGraphData = financialGraphData;
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, FinancialGraphDataDTO> getByPhaseData() {
		return byPhaseData;
	}

	/**
	 * 
	 * @param byPhaseData
	 */
	public void setByPhaseData(Map<String, FinancialGraphDataDTO> byPhaseData) {
		this.byPhaseData = byPhaseData;
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, FinancialGraphDataDTO> getByFunctionData() {
		return byFunctionData;
	}

	/**
	 * 
	 * @param byFunctionData
	 */
	public void setByFunctionData(Map<String, FinancialGraphDataDTO> byFunctionData) {
		this.byFunctionData = byFunctionData;
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, FinancialGraphDataDTO> getTotal() {
		return total;
	}

	/**
	 * 
	 * @param total
	 */
	public void setTotal(Map<String, FinancialGraphDataDTO> total) {
		this.total = total;
	}

}
