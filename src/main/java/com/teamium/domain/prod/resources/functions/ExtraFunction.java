package com.teamium.domain.prod.resources.functions;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;

/**
 * An extra rate
 * May be an reduction or a rising
 * @author JS
 *
 */
@Entity
@Table(name="t_extra")
public class ExtraFunction extends Extra{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2773510873847838499L;
	
	@ManyToOne
	@JoinColumn(name="c_idrate")
	private Rate rate;
	
	/**
	 * @return the rate
	 */
	public Rate getRate() {
		return rate;
	}

	/**
	 * @param rate the rate to set
	 */
	public void setRate(Rate rate) {
		this.rate = rate;
	}
	
	/**
	 * @param percent the percent to set
	 */
	@Override
	public void setPercent(Double percent) {
		if(percent != null){
			this.percent = percent;
			basis = null;
			extraTotal = percent * rate.getUnitCost() * (quantity == null ? 0 : quantity );
		}
	}

	/**
	 * @param quantity the quantity to set
	 */
	@Override
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
		extraTotal = percent != null ? percent * rate.getUnitCost() * this.getQuantity() : basis != null ? basis * this.getQuantity() : 0D;
		extraTotalPrice = percent != null ? percentPrice * rate.getUnitPrice() * this.getQuantity() : basisPrice != null ? basisPrice * this.getQuantity() : 0D;
	}
	
	/**
	 * @param percent the percent to set
	 */
	@Override
	public void setPercentPrice(Double percent) {
		if(percent != null){
			this.percentPrice = percent;
			basisPrice = null;
			extraTotal = percent * rate.getUnitPrice() * this.getQuantity();
		}
	}

	
}
