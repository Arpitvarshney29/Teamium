/**
 * 
 */
package com.teamium.domain.prod.projects.invoice;


import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.teamium.domain.prod.DueDate;

/**
 * Describes a due date for a project
 * @author sraybaud
 *
 */
@Entity
@DiscriminatorValue("invoice")
public class InvoiceDueDate extends DueDate {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6896139586803349479L;

	/**
	 * Payment date
	 */
	@Column(name="c_invoice_paymentdate")
	private Calendar paymentDate;
	
	/**
	 * Due amount
	 */
	@Column(name="c_invoice_amount")
	private Float amount;
	
	/**
	 * Invoice rate
	 */
	@Column(name="c_invoice_rate")
	private Float rate;

	/**
	 * @return the paymentDate
	 */
	public Calendar getPaymentDate() {
		return paymentDate;
	}

	/**
	 * @param paymentDate the paymentDate to set
	 */
	public void setPaymentDate(Calendar paymentDate) {
		this.paymentDate = paymentDate;
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
		if(this.amount!=null && this.getRecord()!=null){
			Float basis = this.getRecord().getTotalNetPriceIFees();
			if(basis!=null && Math.abs(basis.doubleValue()) > 0) this.rate = this.amount / basis;
			else this.rate=null;
		}else{
			this.rate=null;
		}
	}

	/**
	 * @return the rate
	 */
	public Float getRate() {
		return rate;
	}

	/**
	 * @param rate the rate to set
	 */
	public void setRate(Float rate) {
		this.rate = rate;
		if(this.rate!=null && this.getRecord()!=null){
			Float basis = this.getRecord().getTotalNetPriceIFees();
			if(basis!=null) this.amount = this.rate * basis;
			else this.amount=null;
		}
	}
	
	
	
}
