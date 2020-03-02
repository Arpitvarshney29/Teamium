package com.teamium.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.teamium.domain.ExpensesItem;

public class ExpensesItemDTO {
	private Long id;
	private String name;
	private String date;
	private Double companyCardExpense;
	private Double personalExpense;

	public ExpensesItemDTO() {

	}

	public ExpensesItemDTO(ExpensesItem expensesItem) {
		this.id = expensesItem.getId();
		this.name = expensesItem.getName();
		if (expensesItem.getDate() != null) {
			DateTime dt = new DateTime(expensesItem.getDate().getTime()).withZone(DateTimeZone.UTC);
			this.date = dt.toString();
		}
		this.companyCardExpense = expensesItem.getCompanyCardExpense();
		this.personalExpense = expensesItem.getPersonalExpense();
	}
	
	public ExpensesItemDTO(ExpensesItem expensesItem,boolean isCheck) {
		this.id = expensesItem.getId();
		this.name = expensesItem.getName();
		if (expensesItem.getDate() != null) {
			SimpleDateFormat sd = new SimpleDateFormat("MM/dd/yyyy");
			String  dt=sd.format(new Date(expensesItem.getDate().getTimeInMillis()));
			this.date = dt.toString();
		}
		
		this.companyCardExpense = expensesItem.getCompanyCardExpense();
		this.personalExpense = expensesItem.getPersonalExpense();
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the companyCardExpense
	 */
	public Double getCompanyCardExpense() {
		return companyCardExpense;
	}

	/**
	 * @param companyCardExpense the companyCardExpense to set
	 */
	public void setCompanyCardExpense(Double companyCardExpense) {
		this.companyCardExpense = companyCardExpense;
	}

	/**
	 * @return the personalExpense
	 */
	public Double getPersonalExpense() {
		return personalExpense;
	}

	/**
	 * @param personalExpense the personalExpense to set
	 */
	public void setPersonalExpense(Double personalExpense) {
		this.personalExpense = personalExpense;
	}

}
