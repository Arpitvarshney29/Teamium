package com.teamium.domain.prod.resources.staff;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("staff")
public class StaffRate extends ResourceRate{

	/**
	 * Generated OID
	 */
	private static final long serialVersionUID = 3804838611874202318L;
	
	@Column(name="c_chargepercent")
	private Float chargePercent;

	/**
	 * @return the chargePercent
	 */
	public Float getChargePercent() {
		return chargePercent;
	}

	/**
	 * @param chargePercent the chargePercent to set
	 */
	public void setChargePercent(Float chargePercent) {
		this.chargePercent = chargePercent;
	}
	
	
	
}
