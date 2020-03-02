package com.teamium.domain.prod;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.Vat;
import com.teamium.domain.XmlEntity;

/**
 * Abstract superclass for fees
 * @author sraybaud @version TEAM-18
 *
 */
@MappedSuperclass
public abstract class Fee extends AbstractEntity implements Cloneable,Comparable<RecordFee>{

	/**
	 * Generated OID
	 */
	private static final long serialVersionUID = 2909396810436862849L;

	public abstract Long getId();

	public abstract void setId(Long id);

	/**
	 * The project
	 */
	@ManyToOne
	@JoinColumn(name="c_idrecord")
	private Record record;

	/**
	 * Type of fee
	 * @see com.teamium.domain.prod.projects.ProjectFee
	 */
	@Embedded
	@AttributeOverride(name="key", column=@Column(name="c_feetype"))
	private XmlEntity type;

	/**
	 * Applied rate
	 */
	@Column(name="c_rate")
	private Float rate;

	/**
	 * Amount
	 */
	@Column(name="c_amount")
	private Float amount;


	/**
	 * @return the record
	 */
	public Record getRecord() {
		return record;
	}

	/**
	 * @param record the record to set
	 */
	public void setRecord(Record record) {
		this.record = record;
	}

	/**
	 * @return the type
	 */
	public XmlEntity getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(XmlEntity type) {
		this.type = type;
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
		this.updateAmount();
	}

	/**
	 * @return the amount
	 */
	public Float getAmount() {
		return amount;
	}

	/**
	 * 
	 */

	/**
	 * Compare the current object with the given one
	 * @param other the other to compare
	 * @return -1 if the current is previous, 0 if both are equal, +1 if the given is previous
	 */
	@Override
	public int compareTo(RecordFee o) {
		if(o==null) return 1;
		if(this.type==null){
			if(o.getType()!=null){
				return -1;
			}
		}else{
			int compare = this.type.compareTo(o.getType());
			if(compare !=0) return compare;
		}
		return 0;
	}	


	/**
	 * Clone the current object
	 * @return the clone
	 * @throws CloneNotSupportedException 
	 */
	@Override
	public RecordFee clone() throws CloneNotSupportedException{
		RecordFee clone = null;
		Long id=this.getId();
		this.setId(null);
		Record record = this.record;
		this.record=null;
		XmlEntity type = this.type;
		this.type=type;
		try{
			clone = (RecordFee) super.clone();
			clone.setRecord(record);
			clone.setType(type);
		}catch(CloneNotSupportedException e){
			throw e;
		}finally{
			this.setId(id);
			this.record=record;
			this.type=type;
		}
		return clone;
	}

	/**
	 * Update fee amount
	 */
	/*
	public void updateAmount(){
		if(this.rate!=null && this.record!=null){
			this.amount=0f;
			for(Line line : this.record.getLines()){
				Float basis=0f;
				if(line.getFunction()!=null && line.getAppliedFees().contains(this.type)){
					basis+=line.getTotalPrice()!=null? line.getTotalPrice() : 0f;
				}
				this.calculate(basis);
			}
		}else{
			this.amount=null;
		}
	}*/
	public void updateAmount(){
		if(this.rate!=null && this.record!=null){
			this.amount=0f;
			for(Line line : this.record.getLines()){
				Float basis=0f;
				if(line.getFunction()!=null && line.getAppliedFees().contains(this.type)){
					basis+=line.getTotalPrice()!=null? line.getTotalPrice() : 0f;
					/* FEE with TVA
					if(line.getVat() == null)
						basis+=line.getTotalPrice()!=null? line.getTotalPrice() : 0f;
					else
						basis+=line.getTotalPrice()!=null? line.getTotalPrice() * (1 + line.getVat().getRate()) : 0f;
					*/
				}
				this.calculate(basis);
			}
		}else{
			this.amount=null;
		}
	}

	/**
	 * Calculate fee amount regarding to the current rate and the given basis
	 * @param basis the basis
	 */
	public void calculate(Float basis){
		if(this.rate!=null && basis!=null){
			this.amount+=Math.round(basis*this.rate*100f)/100f;
		}else{
			this.amount=null;
		}
	}
	
	/**
	 * Return vat amount for the given vat
	 * 
	 */
	public Float getVatAmount(Vat vat){
		Float amount = 0f;
		
		if(vat != null){
			for(Line line : this.record.getLines()){
				if(line.getAppliedFees().contains(this.type) && line.getRecord() != null && line.getVat() != null && line.getVat().equals(vat) && this.rate != null && line.getTotalPrice() != null){
					amount+=line.getTotalPrice() * this.rate * vat.getRate();
				}
			}
		}
		
		return amount;
	}

}
