package com.teamium.domain.prod;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class EmbeddedRate implements Serializable{

	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 3463243817277045052L;

	/**
	 * The minimum base unit
	 */
	private Integer baseMin;
	
	/**
	 * The number of minimum additional unit
	 */
	private Integer extraSlot;
	
	/**
	 * The cost of extra unit
	 */
	private Float extraCost;
	
	/**
	 * The price of extra unit
	 */
	private Float extraPrice;
	
	/**
	 * the unit price
	 */
	private Float unitPrice;
	
	/**
	 * the unit cost
	 */
	private Float unitCost;
	
	/**
	 * Boolean indicating if the rate is processing
	 */
	@Transient
	boolean processing = false;

	/**
	 * @return the baseMin
	 */
	public Integer getBaseMin() {
		return baseMin;
	}

	/**
	 * @return the extraSlot
	 */
	public Integer getExtraSlot() {
		return extraSlot;
	}


	/**
	 * @return the extraCost
	 */
	public Float getExtraCost() {
		return extraCost;
	}



	/**
	 * @return the extraPrice
	 */
	public Float getExtraPrice() {
		return extraPrice;
	}



	/**
	 * @return the unitPrice
	 */
	public Float getUnitPrice() {
		return unitPrice;
	}



	/**
	 * @return the unitCost
	 */
	public Float getUnitCost() {
		return unitCost;
	}



	/**
	 * @param baseMin the baseMin to set
	 */
	public void setBaseMin(Integer baseMin) {
		if(baseMin == null)
			this.baseMin = 0;
		else
			this.baseMin = baseMin;
	}

	/**
	 * @param extraSlot the extraSlot to set
	 */
	public void setExtraSlot(Integer extraSlot) {
		if(extraSlot == null)
			this.extraSlot = 0;
		else
			this.extraSlot = extraSlot;
	}

	/**
	 * @param extraCost the extraCost to set
	 */
	public void setExtraCost(Float extraCost) {
		if(extraCost == null)
			this.extraCost = 0F;
		else
			this.extraCost = extraCost;
	}

	/**
	 * @param extraPrice the extraPrice to set
	 */
	public void setExtraPrice(Float extraPrice) {
		if(extraPrice == null)
			this.extraPrice = 0F;
		else
			this.extraPrice = extraPrice;
	}

	/**
	 * @param unitPrice the unitPrice to set
	 */
	public void setUnitPrice(Float unitPrice) {
		if(unitPrice == null)
			this.unitPrice = 0F;
		else
			this.unitPrice = unitPrice;
	}

	/**
	 * @param unitCost the unitCost to set
	 */
	public void setUnitCost(Float unitCost) {
		if(unitCost == null)
			unitCost = 0F;
		else
			this.unitCost = unitCost;
	}

	/**
	 * Calculate the price for a line 
	 * @param line - the line to calculate
	 */
	public void calculate(Line line) {
		if(!processing)
		{
			processing = true;
			Float temp = baseMin - line.getQtyTotalUsed();
			if(temp > 0)
			{
				//TODO REVIEW
				if(extraSlot > 0)
					temp = temp % extraSlot == 0 ? temp/extraSlot : (temp/extraSlot) + 1;
				Float newUnitPrice = (baseMin*unitPrice + temp*extraPrice) / (baseMin+temp);
				Float newUnitCost = (baseMin*unitCost + temp*extraCost) / (baseMin+temp);
				line.setUnitCost(newUnitCost);
				line.setUnitPrice(newUnitPrice);
				if(extraSlot > 0)
					line.setQtyTotalUsed(baseMin + temp*extraSlot);
				else
					line.setQtyTotalUsed(baseMin + temp);
			}
			else{
				if(line.getQtyTotalUsed() < baseMin)
				{
					line.setQtyTotalUsed(new Float(this.baseMin));
				}
				if(this.unitPrice != null && this.unitPrice.equals(0)){
					line.setUnitPrice(this.unitPrice);
				}
				if(this.unitCost != null && this.unitCost.equals(0)){
					line.setUnitCost(this.unitCost);
				}
			}
		}
		else
			processing = false;
		
	}



}
