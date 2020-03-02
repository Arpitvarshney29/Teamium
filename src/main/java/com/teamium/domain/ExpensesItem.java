package com.teamium.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.teamium.dto.ExpensesItemDTO;
import com.teamium.exception.UnprocessableEntityException;

@Entity
@Table(name = "t_expenses_item")
public class ExpensesItem {

	/**
	 * ExpensesItem ID
	 */
	@Id
	@Column(name = "c_expenses_item_id", insertable = false, updatable = false)
	@TableGenerator(name = "idExpensesItem_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "expensesItem_idExpensesItem_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idExpensesItem_seq")
	private Long id;

	@Column(name = "c_name")
	@NotNull
	private String name;

	@Column(name = "c_date")
	@NotNull
	private Calendar date;

	@Column(name = "c_company_card_expense")
	private Double companyCardExpense;

	@Column(name = "c_personal_expense")
	private Double personalExpense;

	public ExpensesItem() {

	}

	public ExpensesItem(ExpensesItemDTO expensesItemDTO) {
		if (expensesItemDTO.getId() != null) {
			this.id = expensesItemDTO.getId();
		}
		if(StringUtils.isBlank(expensesItemDTO.getName())) {
			throw new UnprocessableEntityException("Expenses item's name can't be null.");
		}
		this.name = expensesItemDTO.getName();
		if (expensesItemDTO.getDate() != null) {
			DateTime date = DateTime.parse(expensesItemDTO.getDate()).withZone(DateTimeZone.forID("Asia/Calcutta"));
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(date.getMillis());
			this.setDate(cal);
		}else {
			throw new UnprocessableEntityException("Expenses item's date can't be null.");
		}
		this.companyCardExpense = expensesItemDTO.getCompanyCardExpense();
		this.personalExpense = expensesItemDTO.getPersonalExpense();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ExpensesItem [id=" + id + ", name=" + name + ", date=" + date + ", companyCardExpense="
				+ companyCardExpense + ", personalExpense=" + personalExpense + "]";
	}

}
