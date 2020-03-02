package com.teamium.domain.prod.projects.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;

@Entity
@Table(name = "t_expenses_detail")
public class ExpensesDetail extends AbstractEntity{
	
	/**
	 * Auto generated value.
	 */
	private static final long serialVersionUID = -4312165736220167846L;

	@Id
	@Column(name = "c_idexpensesdetail", insertable = false, updatable = false)
	@TableGenerator(name = "idExpensesDetail_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "expensesDetail_idExpensesDetail_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idExpensesDetail_seq")
	private Long id;
	
	@Column(name = "c_type")
	private String type;

	@Column(name = "c_budget")
	private Float budget;

	@Column(name = "c_amount")
	private Float amount;

	@Column(name = "c_personal")
	private Float personal;

	@Column(name = "c_item")
	private Integer item;

	public ExpensesDetail() {

	}

	public ExpensesDetail(String type, Float budget, Float amount, Float personal, Integer item) {
		this.type = type;
		this.budget = budget;
		this.amount = amount;
		this.personal = personal;
		this.item = item;
	}

	@Override
	public Long getId() {
		
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id=id;

	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the budget
	 */
	public Float getBudget() {
		return budget;
	}

	/**
	 * @param budget
	 *            the budget to set
	 */
	public void setBudget(Float budget) {
		this.budget = budget;
	}

	/**
	 * @return the amount
	 */
	public Float getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(Float amount) {
		this.amount = amount;
	}

	/**
	 * @return the personal
	 */
	public Float getPersonal() {
		return personal;
	}

	/**
	 * @param personal
	 *            the personal to set
	 */
	public void setPersonal(Float personal) {
		this.personal = personal;
	}

	/**
	 * @return the item
	 */
	public Integer getItem() {
		return item;
	}

	/**
	 * @param item
	 *            the item to set
	 */
	public void setItem(Integer item) {
		this.item = item;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((budget == null) ? 0 : budget.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		result = prime * result + ((personal == null) ? 0 : personal.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExpensesDetail other = (ExpensesDetail) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (budget == null) {
			if (other.budget != null)
				return false;
		} else if (!budget.equals(other.budget))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		if (personal == null) {
			if (other.personal != null)
				return false;
		} else if (!personal.equals(other.personal))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
