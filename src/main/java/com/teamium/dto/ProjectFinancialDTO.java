package com.teamium.dto;

import java.util.HashMap;
import java.util.Map;

import com.teamium.domain.prod.projects.Quotation;

public class ProjectFinancialDTO {
	private String title;
	private String projectStatus;
	private String financialStatus;
	private String managedBy;
	private String origin;
	private String comapny;
	private String currency;
	private String language;
	private String internalReference;
	private Map<String, Float> budgetMap;
	private Float totalBudget;
	private Float totalBudgetMargin;
	private Float totalActual;
	private Float totalOutsource;
	private Float totalActualMargin;
	private Map<String, Float> actualMap;
	private Map<String, Float> outSourceMap;
	private Float totalOutsourceMargin;
	private Map<String, Float> byPhaseActualMap;
	private Map<String, Float> byPhaseBudgetMap;
	private Map<String, Float> byFunctionBudgetMap;
	private Map<String, Float> byFunctionActualMap;
	private Map<String, FinancialGraphDataDTO> financialGraphData;
	private Map<String, FinancialGraphDataDTO> byPhaseData;
	private Map<String, FinancialGraphDataDTO> byFunctionData;
	private Map<String, FinancialGraphDataDTO> total;

	private Map<String, Float> byFunctionOutSourceMap;
	private Map<String, Float> byPhaseoutSourceMap;

	public ProjectFinancialDTO() {

	}

	public ProjectFinancialDTO(Quotation record, Float totalBudget, Float totalActual, Float totalOutsource,
			Map<String, Float> budgetMap, Map<String, Float> actualMap, Map<String, Float> outSourceMap,
			Map<String, Float> byPhaseActualMap, Map<String, Float> byPhaseBudgetMap,
			Map<String, Float> byFunctionBudgetMap, Map<String, Float> byFunctionActualMap) {

		this.projectStatus = record.getStatus() != null ? record.getStatus().getKey() : "";
		this.financialStatus = record.getFinancialStatus() != null ? record.getFinancialStatus() : "";
		this.managedBy = record.getFollower() != null
				? record.getFollower().getFirstName() + " " + record.getFollower().getName()
				: "";
		this.origin = record.getOrigin() != null ? record.getOrigin() : "";
		this.comapny = record.getEntity() != null ? record.getEntity().getName() : "";
		this.currency = record.getCurrency() != null ? record.getCurrency().getCurrencyCode() : "";
		this.language = record.getLanguage() != null ? record.getLanguage().getKey() : "";
		this.internalReference = record.getReferenceInternal() != null ? record.getReferenceInternal() : "";
		this.title = record.getTitle();
		this.totalBudget = totalBudget;
		this.totalActual = totalActual;
		this.totalOutsource = totalOutsource;

		this.budgetMap = budgetMap != null ? budgetMap : new HashMap<>();
		this.actualMap = actualMap != null ? actualMap : new HashMap<>();
		this.outSourceMap = outSourceMap != null ? outSourceMap : new HashMap<>();
		this.byPhaseActualMap = byPhaseActualMap != null ? byPhaseActualMap : new HashMap<>();
		this.byPhaseBudgetMap = byPhaseBudgetMap != null ? byPhaseBudgetMap : new HashMap<>();
		this.byFunctionBudgetMap = byFunctionBudgetMap != null ? byFunctionBudgetMap : new HashMap<>();
		this.byFunctionActualMap = byFunctionActualMap != null ? byFunctionActualMap : new HashMap<>();

	}

	public ProjectFinancialDTO(Map<String, Float> budgetMap, Float totalBudget, Float totalMargin) {

		this.budgetMap = budgetMap != null ? budgetMap : new HashMap<>();
		this.totalBudget = totalBudget;
		this.totalBudgetMargin = totalMargin;

	}

	public ProjectFinancialDTO(Quotation record) {

		this.projectStatus = record.getStatus() != null ? record.getStatus().getKey() : "";
		this.financialStatus = record.getFinancialStatus() != null ? record.getFinancialStatus() : "";
		this.managedBy = record.getFollower() != null
				? record.getFollower().getFirstName() + " " + record.getFollower().getName()
				: "";
		this.origin = record.getOrigin() != null ? record.getOrigin() : "";
		this.comapny = record.getEntity() != null ? record.getEntity().getName() : "";
		this.currency = record.getCurrency() != null ? record.getCurrency().getCurrencyCode() : "";
		this.language = record.getLanguage() != null ? record.getLanguage().getKey() : "";
		this.internalReference = record.getReferenceInternal() != null ? record.getReferenceInternal() : "";
		this.title = record.getTitle();

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
	 * @return projectStatus
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
	 * @return financialStatus
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
	 * @return managedBy
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
	 * @return origin
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
	 * @return comapny
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
	 * @return currency
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
	 * @return language
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
	 * @return internalReference
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
	public Map<String, Float> getBudgetMap() {
		return budgetMap;
	}

	/**
	 * 
	 * @param budgetMap
	 */
	public void setBudgetMap(Map<String, Float> budgetMap) {
		this.budgetMap = budgetMap;
	}

	/**
	 * 
	 * @return
	 */
	public Float getTotalBudget() {
		return totalBudget;
	}

	/**
	 * 
	 * @param totalBudget
	 */
	public void setTotalBudget(Float totalBudget) {
		this.totalBudget = totalBudget;
	}

	/**
	 * 
	 * @return
	 */
	public Float getTotalActual() {
		return totalActual;
	}

	/**
	 * 
	 * @param totalActual
	 */
	public void setTotalActual(Float totalActual) {
		this.totalActual = totalActual;
	}

	/**
	 * 
	 * @return
	 */
	public Float getTotalOutsource() {
		return totalOutsource;
	}

	/**
	 * 
	 * @param totalOutsource
	 */
	public void setTotalOutsource(Float totalOutsource) {
		this.totalOutsource = totalOutsource;
	}

	/**
	 * 
	 */

	public Float getTotalBudgetMargin() {
		return totalBudgetMargin;
	}

	/**
	 * 
	 * @param totalBudgetMargin
	 */
	public void setTotalBudgetMargin(Float totalBudgetMargin) {
		this.totalBudgetMargin = totalBudgetMargin;
	}

	/**
	 * 
	 * @return
	 */
	public Float getTotalActualMargin() {
		return totalActualMargin;
	}

	/**
	 * 
	 * @param totalActualMargin
	 */
	public void setTotalActualMargin(Float totalActualMargin) {
		this.totalActualMargin = totalActualMargin;
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, Float> getActualMap() {
		return actualMap;
	}

	/**
	 * 
	 * @param actualMap
	 */
	public void setActualMap(Map<String, Float> actualMap) {
		this.actualMap = actualMap;
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, Float> getOutSourceMap() {
		return outSourceMap;
	}

	/**
	 * 
	 * @param outSourceMap
	 */
	public void setOutSourceMap(Map<String, Float> outSourceMap) {
		this.outSourceMap = outSourceMap;
	}

	/**
	 * 
	 * @return
	 */
	public Float getTotalOutsourceMargin() {
		return totalOutsourceMargin;
	}

	/**
	 * 
	 * @param totalOutsourceMargin
	 */
	public void setTotalOutsourceMargin(Float totalOutsourceMargin) {
		this.totalOutsourceMargin = totalOutsourceMargin;
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, Float> getByPhaseBudgetMap() {
		return byPhaseBudgetMap;
	}

	/**
	 * 
	 * @param byPhaseBudgetMap
	 */
	public void setByPhaseBudgetMap(Map<String, Float> byPhaseBudgetMap) {
		this.byPhaseBudgetMap = byPhaseBudgetMap;
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, Float> getByPhaseActualMap() {
		return byPhaseActualMap;
	}

	/**
	 * 
	 * @param byPhaseActualMap
	 */
	public void setByPhaseActualMap(Map<String, Float> byPhaseActualMap) {
		this.byPhaseActualMap = byPhaseActualMap;
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, Float> getByFunctionBudgetMap() {
		return byFunctionBudgetMap;
	}

	/**
	 * 
	 * @param byFunctionBudgetMap
	 */
	public void setByFunctionBudgetMap(Map<String, Float> byFunctionBudgetMap) {
		this.byFunctionBudgetMap = byFunctionBudgetMap;
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, Float> getByFunctionActualMap() {
		return byFunctionActualMap;
	}

	/**
	 * 
	 * @param byFunctionActualMap
	 */
	public void setByFunctionActualMap(Map<String, Float> byFunctionActualMap) {
		this.byFunctionActualMap = byFunctionActualMap;
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
	public Map<String, Float> getByFunctionOutSourceMap() {
		return byFunctionOutSourceMap;
	}

	/**
	 * 
	 * @param byFunctionOutSourceMap
	 */
	public void setByFunctionOutSourceMap(Map<String, Float> byFunctionOutSourceMap) {
		this.byFunctionOutSourceMap = byFunctionOutSourceMap;
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, Float> getByPhaseoutSourceMap() {
		return byPhaseoutSourceMap;
	}

	/**
	 * 
	 * @param byPhaseoutSourceMap
	 */
	public void setByPhaseoutSourceMap(Map<String, Float> byPhaseoutSourceMap) {
		this.byPhaseoutSourceMap = byPhaseoutSourceMap;
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
