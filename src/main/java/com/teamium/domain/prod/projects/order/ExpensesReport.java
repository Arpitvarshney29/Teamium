package com.teamium.domain.prod.projects.order;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.prod.Line;

@Entity
@Table(name = "t_expenses_report")
@NamedQueries({ @NamedQuery(name = ExpensesReport.QUERY_deleteExpensesReport, query = "DELETE FROM ExpensesReport er WHERE er=?1")})
public class ExpensesReport extends AbstractEntity {

	/**
	 * Auto generated value.
	 */
	private static final long serialVersionUID = 6646094920530135868L;

	/**
	 * Name of the query deleting all lines from a given record
	 * 
	 * @param 1
	 *            the record from which all lines must be deleted
	 * @return affected rows
	 */
	public static final String QUERY_deleteExpensesReport = "deleteExpenseReportFromLine";

	@Id
	@Column(name = "c_idexpensesreport", insertable = false, updatable = false)
	@TableGenerator(name = "idExpensesReport_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "expensesReport_idExpensesReport_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idExpensesReport_seq")
	private Long id;

	@Override
	public Long getId() {
		
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id=id;
		
	}
	public static enum Status{
		CREATED, IN_PROGRESS, DONE
	}

	@Column(name = "c_status")
	@Enumerated(EnumType.STRING)
	private  Status status;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name="c_expenses_report_id")
	private Set<ExpensesDetail> expensesDetails = new HashSet<>();

	public ExpensesReport(){
		
	}
	
	
	public ExpensesReport(Status status, Set<ExpensesDetail> expensesDetails) {
		this.status = status;
		this.expensesDetails = expensesDetails;
	}
	
	public ExpensesReport(Status status) {
		this.status = status;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @return the expensesDetails
	 */
	public Set<ExpensesDetail> getExpensesDetails() {
		return expensesDetails;
	}

	/**
	 * @param expensesDetails the expensesDetails to set
	 */
	public void setExpensesDetails(Set<ExpensesDetail> expensesDetails) {
		this.expensesDetails = expensesDetails;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((expensesDetails == null) ? 0 : expensesDetails.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		ExpensesReport other = (ExpensesReport) obj;
		if (expensesDetails == null) {
			if (other.expensesDetails != null)
				return false;
		} else if (!expensesDetails.equals(other.expensesDetails))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (status != other.status)
			return false;
		return true;
	}
	

}
