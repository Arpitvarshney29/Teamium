/**
 * 
 */
package com.teamium.domain;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlElement;

/**
 * The VAT part of a project
 * 
 * @author sraybaud
 *
 */
@Embeddable
public class Vat extends AbstractXmlEntity implements Cloneable {
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 659814129119579782L;

	/**
	 * The VAT rate
	 */
	@XmlElement(namespace = TeamiumConstants.XMLNS)
	private Float rate;

	/**
	 * Returns the rate
	 * 
	 * @return the rate
	 */
	public Float getRate() {
		return this.rate;
	}

	/**
	 * The rate to set
	 * 
	 * @param rate
	 *            the rate to set
	 */
	public void setRate(Float rate) {
		this.rate = rate;
	}

	/**
	 * Clone the current object
	 * 
	 * @return the clone
	 * @throws CloneNotSupportedException
	 */
	@Override
	public Vat clone() throws CloneNotSupportedException {
		return (Vat) super.clone();
	}
}
