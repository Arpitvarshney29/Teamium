/**
 * 
 */
package com.teamium.domain.prod.resources.equipments;

import java.util.Calendar;
import java.util.Currency;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.resources.suppliers.Supplier;

/**
 * @author slopes
 *
 */
@Entity
@Table(name="t_maintenance_contract")
@NamedQueries({
	@NamedQuery(name=MaintenanceContract.QUERY_findByDateRangeByType, query="SELECT mc FROM MaintenanceContract mc WHERE mc.start >= ?1 AND mc.end <= ?2 AND mc.type = ?3"),
	@NamedQuery(name=MaintenanceContract.QUERY_findExpiredByDateRangeByType, query="SELECT mc FROM MaintenanceContract mc WHERE mc.start >= ?1 AND mc.end <= ?2 AND mc.end < CURRENT_DATE AND mc.type = ?3"),
})
public class MaintenanceContract extends AbstractEntity {
	/**
	 * The generated serial UID
	 */
	private static final long serialVersionUID = 3394257723175072899L;
	
	/**
	 * Find maintenance contract between the date range given in parameter
	 * @param 1 The start date
	 * @param 2 The end date
	 * @param 3 The type
	 */
	public static final String QUERY_findByDateRangeByType = "find_maintenance_contract_by_date_range_by_type";
	
	/**
	 * Find maintenance contract between the date range given in parameter
	 * @param 1 The start date
	 * @param 2 The end date
	 * @param 3 The type
	 */
	public static final String QUERY_findExpiredByDateRangeByType = "find_expired_maintenance_contract_by_date_range_by_type";
	/**
	 * The id
	 */
	@Id
	@Column(name="c_id", insertable=false, updatable=false)
	@TableGenerator(name = "idMaintenance_Contract_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "maintenance_contract", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idMaintenance_Contract_seq")
	private Long id;
	
	/**
	 * The start date
	 */
	@Column(name="c_start")
	@Temporal(TemporalType.DATE)
	private Calendar start;
	
	/**
	 * The end date
	 */
	@Column(name="c_end")
	@Temporal(TemporalType.DATE)
	private Calendar end;
	
	/**
	 * The equipment information type
	 */
	@Embedded
	@AttributeOverride(name="key", column=@Column(name="c_type"))
	private XmlEntity type;
	
	/**
	 * The amount
	 */
	@Column(name="c_amount")
	private Float amount;
	
	/**
	 * The currency
	 */
	@Column(name="c_currency")
	private String currency;
	
	/**
	 * The supplier
	 */
	@ManyToOne
	@JoinColumn(name="c_supplier")
	private Supplier supplier;

	/**
	 * @return The id
	 */
	@Override
	public Long getId() {
		return this.id;
	}

	/**
	 * @param id The id to set
	 */
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the start
	 */
	public Calendar getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(Calendar start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public Calendar getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(Calendar end) {
		this.end = end;
	}

	/**
	 * @return the type
	 */
	public XmlEntity getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(XmlEntity type) {
		this.type = type;
	}

	/**
	 * @return the amount
	 */
	public Float getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Float amount) {
		this.amount = amount;
	}

	/**
	 * @return the currency
	 */
	public Currency getCurrency() {
		if(currency != null && !currency.isEmpty()){
			return Currency.getInstance(currency);
		}else{
			return null;
		}
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(Currency currency) {
		if(currency != null){
			this.currency = currency.getCurrencyCode();
		}else{
			this.currency = null;
		}
	}

	/**
	 * @return the supplier
	 */
	public Supplier getSupplier() {
		return supplier;
	}

	/**
	 * @param supplier the supplier to set
	 */
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	
	

}
