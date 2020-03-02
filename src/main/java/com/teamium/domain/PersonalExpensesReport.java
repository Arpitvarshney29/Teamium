package com.teamium.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.teamium.domain.prod.projects.Project;
import com.teamium.domain.prod.resources.staff.StaffMember;

@Entity
@Table(name = "t_personal_expenses_report",uniqueConstraints=
@UniqueConstraint(columnNames={"c_idrecord", "c_idperson"}))
public class PersonalExpensesReport {
	/**
	 * PersonalExpensesReport ID
	 */
	@Id
	@Column(name = "c_personal_expenses_report_id", insertable = false, updatable = false)
	@TableGenerator(name = "idPersonalExpensesReport_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "expensesReport_idPersonalExpensesReport_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idPersonalExpensesReport_seq")
	private Long id;

	@OneToOne
	@JoinColumn(name = "c_idrecord")
	@NotNull
	private Project project;

	@OneToOne
	@JoinColumn(name = "c_idperson")
	@NotNull
	private StaffMember staffMember;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "c_personal_expenses_report_id")
	private Set<ExpensesItem> expensesItems = new HashSet<>();

	@Column(name = "c_date")
	private Calendar date;

	@Column(name = "c_status")
	private String status;

	@Column(name = "c_total_personal_expenses")
	private Double totalPersonalExpenses;

	@Column(name = "c_total_company_card_expenses")
	private Double totalCompanyCardExpenses;

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
	 * @return the staffMember
	 */
	public StaffMember getStaffMember() {
		return staffMember;
	}

	/**
	 * @param staffMember the staffMember to set
	 */
	public void setStaffMember(StaffMember staffMember) {
		this.staffMember = staffMember;
	}

	/**
	 * @return the expensesItems
	 */
	public Set<ExpensesItem> getExpensesItems() {
		return expensesItems;
	}

	/**
	 * @param expensesItems the expensesItems to set
	 */
	public void setExpensesItems(Set<ExpensesItem> expensesItems) {
		this.expensesItems = expensesItems;
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
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the totalPersonalExpenses
	 */
	public Double getTotalPersonalExpenses() {
		return totalPersonalExpenses;
	}

	/**
	 * @param totalPersonalExpenses the totalPersonalExpenses to set
	 */
	public void setTotalPersonalExpenses(Double totalPersonalExpenses) {
		this.totalPersonalExpenses = totalPersonalExpenses;
	}

	/**
	 * @return the totalCompanyCardExpenses
	 */
	public Double getTotalCompanyCardExpenses() {
		return totalCompanyCardExpenses;
	}

	/**
	 * @param totalCompanyCardExpenses the totalCompanyCardExpenses to set
	 */
	public void setTotalCompanyCardExpenses(Double totalCompanyCardExpenses) {
		this.totalCompanyCardExpenses = totalCompanyCardExpenses;
	}

	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PersonalExpensesReport [id=" + id + ", project=" + project + ", staffMember=" + staffMember
				+ ", expensesItems=" + expensesItems + ", date=" + date + ", status=" + status
				+ ", totalPersonalExpenses=" + totalPersonalExpenses + ", totalCompanyCardExpenses="
				+ totalCompanyCardExpenses + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((staffMember == null) ? 0 : staffMember.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonalExpensesReport other = (PersonalExpensesReport) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (staffMember == null) {
			if (other.staffMember != null)
				return false;
		} else if (!staffMember.equals(other.staffMember))
			return false;
		return true;
	}

}
