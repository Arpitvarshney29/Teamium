/**
 * 
 */
package com.teamium.domain.prod.projects.invoice;

import java.util.Calendar;
import java.util.Currency;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.teamium.domain.Vat;
import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.resources.functions.RatedFunction;
import com.teamium.domain.prod.resources.functions.units.RateUnit;

/**
 * Describes a project line
 * @author sraybaud - NovaRem
 * @version 5.0.1 : add method update()
 */
@Entity
@DiscriminatorValue("wrapper")
public class LineWrapper extends Booking  {

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -8070983611788985496L;
	
	/**
	 * The linked project
	 */
	@ManyToOne
	@JoinColumn(name="c_idwrappedline")
	private Booking item;
	
	/**
	 * @return the invoiced item
	 */
	public Booking getItem() {
		return item;
	}
	/**
	 * @param item the item to invoice
	 */
	public void setItem(Booking item) {
		this.item = item;
	}

	/**
	 * @return
	 * @see com.teamium.domain.prod.Line#getFunction()
	 */
	@Override
	public RatedFunction getFunction() {
		if(super.getFunction()==null && item!=null){
			super.setFunction(item.getFunction());
		}
		return super.getFunction();
	}

	/**
	 * @return
	 * @see com.teamium.domain.prod.Line#getFrom()
	 */
	@Override
	public Calendar getFrom() {
		if(super.getFrom()==null && item!=null && item.getFrom()!=null){
			super.setFrom((Calendar) item.getFrom().clone());
		}
		return super.getFrom();
	}

	/**
	 * @return
	 * @see com.teamium.domain.prod.Line#getTo()
	 */
	@Override
	public Calendar getTo() {
		if(super.getTo()==null && item!=null && item.getTo()!=null){
			super.setTo((Calendar) item.getTo().clone());
		}
		return super.getTo();
	}
	
	/**
	 * @return the saleQuantity
	 */
	public Float getQtySoldPerOc() {
		if(super.getQtySoldPerOc()==null && item!=null){
			super.setQtySoldPerOcRaw(item.getQtySoldPerOc());
		}
		return super.getQtySoldPerOc();
	}

	/**
	 * @return
	 * @see com.teamium.domain.prod.Line#getQtyTotalUsed()
	 */
	@Override
	public Float getQtyTotalUsed() {
		if(this.getSyncQty()){
			if(super.getQtyTotalUsed()==null && item!=null){
				super.setQtyTotalUsed(item.getQtyTotalUsed());
			}
		}else{
			if(super.getQtySoldPerOc()==null && item!=null){
				super.setQtyTotalUsed(item.getQtySoldPerOc());
			}
		}
		return super.getQtyTotalUsed();
	}
	
	/**
	 * @return
	 * @see com.teamium.domain.prod.Line#getUnitUsed()
	 */
	@Override
	public RateUnit getUnitUsed() {
		if(super.getUnitUsed()==null && item!=null){
			super.setUnitUsed(item.getUnitUsed());
		}
		return super.getUnitUsed();
	}
	
	/**
	 * @return
	 * @see com.teamium.domain.prod.Line#getUnitUsed()
	 */
	@Override
	public RateUnit getUnitSold() {
		if(super.getUnitSold()==null && item!=null){
			super.setUnitSoldRaw(item.getUnitSold());
		}
		return super.getUnitSold();
	}

	/**
	 * @return
	 * @see com.teamium.domain.prod.Line#getUnitPrice()
	 */
	@Override
	public Float getUnitPrice() {
		if(super.getUnitPrice()==null && item!=null){
			super.setUnitPrice(item.getUnitPrice());
		}
		return super.getUnitPrice();
	}

	/**
	 * @return
	 * @see com.teamium.domain.prod.Line#getCurrency()
	 */
	@Override
	public Currency getCurrency() {
		if(super.getCurrency()==null && item!=null){
			super.setCurrency(item.getCurrency());
		}
		return super.getCurrency();
	}

	/**
	 * @return
	 * @see com.teamium.domain.prod.Line#getDiscountRate()
	 */
	@Override
	public Float getDiscountRate() {
		if(super.getDiscountRate()==null && this.item!=null){
			super.setDiscountRate(item.getDiscountRate());
		}
		return super.getDiscountRate();
	}

	/**
	 * @return
	 * @see com.teamium.domain.prod.Line#getVat()
	 */
	@Override
	public Vat getVat() {
		if(super.getVat()==null && this.item!=null){
			super.setVat(item.getVat());
		}
		return super.getVat();
	}
	
	/**
	* Return the basic value of unit price ( can be null )
	*/
	public Float getBaseUnitPrice(){
		return super.getUnitPrice();
	}
	
	/**
	 * @return the syncQty
	 */
	public Boolean getSyncQty() {
		if(super.getSyncQty() == null && this.item != null){
			super.setSyncQty(item.getSyncQty());
		}
		return super.getSyncQty();
	}

	/**
	 * @return
	 * @throws CloneNotSupportedException
	 * @see com.teamium.domain.prod.Line#clone()
	 */
	@Override
	public LineWrapper clone() throws CloneNotSupportedException {
		LineWrapper clone=null;
		Booking item = this.item;
		this.item=null;
		try{
			clone = (LineWrapper) super.clone();
			clone.item=item;
		}
		catch(CloneNotSupportedException e){
			this.item=item;
		}
		return clone;
	}
	
	/**
	 * Update all properties whit call on getter
	 */
	public void update(){
		this.getCurrency();
		this.getDiscountRate();
		this.getFrom();
		this.getFunction();
		this.getItem();
		this.getQtyTotalUsed();
		this.getQtyTotalSold();
		this.getTo();
		this.getUnitUsed();
		this.getUnitSold();
		this.getUnitPrice();
		this.getVat();
//		this.getTotalLocalPricePerOc();
		this.getTotalLocalPrice();
		this.getTotalCost();
		this.getTotalPrice();
		this.getComment();
	}
	
	/**
	 * get line comment
	 * 
	 */
	@Override
	public String getComment() {
		super.setComment(item.getComment());
		return super.getComment();
	}
}
