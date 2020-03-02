/**
 * 
 */
package com.teamium.domain.prod.projects.order;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.teamium.domain.prod.projects.invoice.LineWrapper;

/**
 * Describes a project line
 * @author sraybaud - NovaRem
 *
 */
@Entity
@DiscriminatorValue("order")
public class OrderLine extends LineWrapper  {
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 8154730952926159520L;

	/**
	 * @return
	 * @see com.teamium.domain.prod.Line#getUnitPrice()
	 */
	@Override
	public Float getUnitPrice() {
		if(super.getBaseUnitPrice()==null && this.getItem()!=null){
			super.setUnitPrice(this.getItem().getUnitCost());
		}
		return super.getUnitPrice();
	}
	
	/**
	* Update unit price
	*/
	public void setUnitPrice(Float unitPrice){
		super.setUnitPrice(unitPrice);
		this.getItem().setUnitCost(unitPrice);
	}
	
	/**
	 * @return
	 * @see com.teamium.domain.prod.Line#getQtyTotalUsed()
	 */
	@Override
	public Float getQtyTotalUsed() {
		if(super.getQtyTotalUsed()==null && this.getItem()!=null){
			super.setQtyTotalUsed(this.getItem().getQtyTotalUsed());
		}
		return super.getQtyTotalUsed();
	}
	
	/**
	 * get line comment
	 * 
	 */
	@Override
	public String getComment() {
		return super.getComment();
	}
	
}
