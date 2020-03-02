package com.teamium.domain.output;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.teamium.domain.prod.Line;

/**
 * An XML local pricing output
 * @author sraybaud - NovaRem
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class LocalPricingXml {

	/**
	 * Currency
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private ItemXml currency;
	
	/**
	 * Unit price
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private Float unitPrice;
	
	/**
	 * Unit Cost
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private Float unitCost;
	
	/**
	 * discount
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private RatedAmountXml discount;
	
	/**
	 * Total amount
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private Float subTotal;
	
	/**
	 * @return the currency
	 */
	ItemXml getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	void setCurrency(ItemXml currency) {
		this.currency = currency;
	}

	/**
	 * @return the unitPrice
	 */
	Float getUnitPrice() {
		return unitPrice;
	}

	/**
	 * @param unitPrice the unitPrice to set
	 */
	void setUnitPrice(Float unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * @return the discount
	 */
	RatedAmountXml getDiscount() {
		return discount;
	}

	/**
	 * @param discount the discount to set
	 */
	void setDiscount(RatedAmountXml discount) {
		this.discount = discount;
	}

	/**
	 * @return the subTotal
	 */
	Float getSubTotal() {
		return subTotal;
	}

	/**
	 * @param subTotal the subTotal to set
	 */
	void setSubTotal(Float subTotal) {
		this.subTotal = subTotal;
	}

	/**
	 * @param obj the object to marshal
	 */
	public void marshal(Line obj){
		if(obj.getCurrency()!=null){
			this.currency = new ItemXml();
			this.currency.marshal(obj.getCurrency());
		}
		this.unitPrice=obj.getUnitPrice();
		this.unitCost=obj.getUnitCost();
		if(obj.getDiscountRate()!=null || obj.getDiscount()!=null){
			this.discount = new RatedAmountXml();
			this.discount.marshal(obj.getDiscountRate(), obj.getDiscount());
		}
		this.subTotal=obj.getTotalLocalPrice();
	}
}
