package com.teamium.domain.prod.resources.functions;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.teamium.domain.prod.Line;

@Entity
@Table(name="t_line_extra")
public class ExtraLine extends Extra {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2097285245425648889L;

	@ManyToOne
	@JoinColumn(name="c_idline")
	private Line line;
	
	/**
	 * @param percent the percent to set
	 */
	@Override
	public void setPercent(Double percent) {
		if(percent != null){
			this.percent = percent;
			basis = null;
			extraTotal = percent * line.getUnitCost() * this.getQuantity();
		}else{
			this.percent = null;
			this.extraTotal = null;
		}
	}

	/**
	 * @param quantity the quantity to set
	 */
	@Override
	public void setQuantity(Integer quantity) {
		if(quantity != null){
			this.quantity = quantity;
			extraTotal = percent != null ? percent * line.getUnitCost() * this.getQuantity() : basis != null ? basis * this.getQuantity() : 0D;
			extraTotalPrice = percentPrice != null ? percentPrice * line.getUnitPrice() * this.getQuantity() : basisPrice != null ? basisPrice * this.getQuantity() : 0D;
		}else{
			this.quantity = null;
			this.extraTotal = null;
			this.extraTotalPrice = null;
		}
	}
	
	/**
	 * @param percent the percent to set
	 */
	@Override
	public void setPercentPrice(Double percent) {
		if(percent != null){
			this.percentPrice = percent;
			basisPrice = null;
			extraTotalPrice = percent * line.getUnitPrice() * this.getQuantity();
		}else{
			this.percentPrice = null;
			this.extraTotalPrice = null;
		}
	}
	

	/**
	 * @return the line
	 */
	public Line getLine() {
		return line;
	}

	/**
	 * @param line the line to set
	 */
	public void setLine(Line line) {
		this.line = line;
	}
	
	/**
	 * Clone the current object
	 * @return the clone
	 * @throws CloneNotSupportedException
	 */
	@Override
	public ExtraLine clone() throws CloneNotSupportedException{
		return (ExtraLine) super.clone();
	}
}
