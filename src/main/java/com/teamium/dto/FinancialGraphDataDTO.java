package com.teamium.dto;

public class FinancialGraphDataDTO {
	private Float actual;
	private Float budget;
	private Float outsource;

	public FinancialGraphDataDTO() {

	}

	public FinancialGraphDataDTO(Float actual, Float budget, Float outsource) {
		this.actual = actual;
		this.budget = budget;
		this.outsource = outsource;
	}

	public FinancialGraphDataDTO(Float budget) {
		this.budget = budget;
	}

	/**
	 * 
	 * @return
	 */
	public Float getActual() {
		return actual;
	}

	/**
	 * 
	 * @param actual
	 */
	public void setActual(Float actual) {
		this.actual = actual;
	}

	/**
	 * 
	 * @return
	 */
	public Float getBudget() {
		return budget;
	}

	/**
	 * 
	 * @param budget
	 */
	public void setBudget(Float budget) {
		this.budget = budget;
	}

	/**
	 * 
	 * @return
	 */
	public Float getOutsource() {
		return outsource;
	}

	/**
	 * 
	 * @param outsource
	 */
	public void setOutsource(Float outsource) {
		this.outsource = outsource;
	}

}
