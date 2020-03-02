/**
 * 
 */
package com.teamium.domain.prod.resources.contacts;

import java.io.Serializable;
import java.text.NumberFormat;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;

/**
 * Describes a customer rating policy
 * @author sraybaud- NovaRem
 */
@MappedSuperclass
@Embeddable
public class RatePolicy implements Serializable{

	/**
	 * Class uid
	 */
	private static final long serialVersionUID = 1928594231287681630L;
	
	/**
	 * Customer rating policy type
	 */
	@Embedded
	@AttributeOverride(name="key", column=@Column(name="c_rating_type"))
	private RatePolicyType type;
	
	
	/**
	 * Special discount
	 */
	@Column(name="c_rating_discount")
	private Float discount;	

	/**
	 * Return the rating policy tpe
	 * @return the type
	 */
	public RatePolicyType getType() {
		return type;
	}

	/**
	 * Set the rating policy type
	 * @param type the type to set
	 */
	public void setType(RatePolicyType type) {
		this.type = type;
	}

	
	/**
	 * @return the discount
	 */
	public Float getDiscount() {
		return discount;
	}

	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(Float discount) {
		this.discount = discount;
	}
	
	/**
	 * Return the current objet as a string expression
	 * @return the string
	 */
	@Override
	public String toString(){
		return this.getClass().getSimpleName()+(discount!=null? NumberFormat.getPercentInstance().format(discount.doubleValue()) : "");
	}

	
}
