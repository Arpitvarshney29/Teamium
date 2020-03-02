package com.teamium.domain.output;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * An XML rated amount
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RatedAmountXml {

	/**
	 * Type
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private Float rate;
	
	/**
	 * Amount
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private Float amount;
	
	/**
	 * @return the rate
	 */
	Float getRate() {
		return rate;
	}

	/**
	 * @param rate the rate to set
	 */
	void setRate(Float rate) {
		this.rate = rate;
	}

	/**
	 * @return the amount
	 */
	Float getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	void setAmount(Float amount) {
		this.amount = amount;
	}

	/**
	 * @param rate the rate to marshal
	 * @param amount the amount to marshal
	 */
	public void marshal(Float rate, Float amount){
		this.rate=rate;
		this.amount=amount;
	}
	
}
