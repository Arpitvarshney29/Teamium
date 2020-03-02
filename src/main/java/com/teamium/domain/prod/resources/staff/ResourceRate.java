package com.teamium.domain.prod.resources.staff;

import java.util.Currency;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.prod.resources.functions.units.RateUnit;

@Entity
@Table(name="t_staff_rate")
@DiscriminatorColumn(name="c_discriminator",discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue("resource")
public class ResourceRate extends AbstractEntity{
	
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 8996506893040027890L;

	@Id
	@Column(name="c_idstaffrate", insertable=false, updatable=false)
	@TableGenerator( name = "idStaffRate_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "staff_rate_idstaffrate_seq", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idStaffRate_seq")
	private Long id;
	
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="key", column=@Column(name="c_rateunit")),
		@AttributeOverride(name="calendarConstant", column=@Column(name="c_rateunit_calendarconstant"))
	})
	private RateUnit rateUnit;
	
	@Column(name="c_value")
	private Float value;
	
	/**
	 * The currency
	 */
	@Column(name="c_currency")
	private String currency;
	

	/**
	 * @return the rateunit
	 */
	public RateUnit getRateUnit() {
		return rateUnit;
	}

	/**
	 * @param rateunit the rateunit to set
	 */
	public void setRateUnit(RateUnit rateunit) {
		this.rateUnit = rateunit;
	}

	/**
	 * @return the value
	 */
	public Float getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Float value) {
		this.value = value;
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
	 * @return the currency
	 */
	public Currency getCurrency() {
		if(currency==null) return null;
		else return Currency.getInstance(currency);
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(Currency currency) {
		if(currency==null) this.currency = null;
		else this.currency = currency.getCurrencyCode();
	}
	

}
