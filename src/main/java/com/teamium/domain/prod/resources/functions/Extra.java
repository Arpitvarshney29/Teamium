package com.teamium.domain.prod.resources.functions;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.TableGenerator;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class Extra extends AbstractEntity implements Serializable,Cloneable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8673025993753113058L;
	
	/**
	 * Extra ID
	 */
	@Id
	@Column(name="c_id", insertable=false, updatable=false)
	@TableGenerator( name = "idExtra_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "function_idExtra_seq", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idExtra_seq")
	private Long id;
	
	@Column(name="c_label")
	protected String label;
	
	@Column(name="c_basis")
	protected Double basis;
	
	@Column(name="c_percent")
	protected Double percent;
	
	@Column(name="c_basis_price")
	protected Double basisPrice;
	
	@Column(name="c_percent_price")
	protected Double percentPrice;
	
	@Column(name="c_quantity")
	protected Integer quantity;
	
	@Column(name="c_total")
	protected Double extraTotal;
	
	@Column(name="c_total_price")
	protected Double extraTotalPrice;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the basis
	 */
	public Double getBasis() {
		return basis;
	}

	/**
	 * @param basis the basis to set
	 */
	public void setBasis(Double basis) {
		if(basis != null){
			this.basis = basis;
			percent = null;
			extraTotal = basis * this.getQuantity();
		}
	}

	/**
	 * @return the percent
	 */
	public Double getPercent() {
		return percent;
	}

	/**
	 * @param percent the percent to set
	 */
	public void setPercent(Double percent) {
		if(percent != null){
			this.percent = percent;
		}
	}

	/**
	 * @return the quantity
	 */
	public Integer getQuantity() {
		return quantity != null ? quantity : 0;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the extraTotal
	 */
	public Double getExtraTotal() {
		return extraTotal;
	}

	/**
	 * @param extraTotal the extraTotal to set
	 */
	public void setExtraTotal(Double extraTotal) {
		this.extraTotal = extraTotal;
	}
	
	/**
	 * @return the basisPrice
	 */
	public Double getBasisPrice() {
		return basisPrice;
	}

	/**
	 * @param basisPrice the basisPrice to set
	 */
	public void setBasisPrice(Double basisPrice) {
		if(basisPrice != null){
			this.basisPrice = basisPrice;
			percentPrice = null;
			extraTotalPrice = basisPrice * this.getQuantity();
		}
	}

	/**
	 * @return the percentPrice
	 */
	public Double getPercentPrice() {
		return percentPrice;
	}

	/**
	 * @param percentPrice the percentPrice to set
	 */
	public void setPercentPrice(Double percentPrice) {
		this.percentPrice = percentPrice;
	}

	/**
	 * @return the extraTotalPrice
	 */
	public Double getExtraTotalPrice() {
		return extraTotalPrice;
	}

	/**
	 * @param extraTotalPrice the extraTotalPrice to set
	 */
	public void setExtraTotalPrice(Double extraTotalPrice) {
		this.extraTotalPrice = extraTotalPrice;
	}

	/**
	 * 
	 */
	public Extra clone(Class<? extends Extra> c){
		Extra clone;
		try {
			clone = (Extra) c.newInstance();
			clone.basis = ((Extra)this).basis;
			clone.percent = ((Extra)this).percent;
			clone.quantity = ((Extra)this).quantity;
			clone.extraTotal = ((Extra)this).extraTotal;
			clone.basisPrice = ((Extra)this).basisPrice;
			clone.percentPrice = ((Extra)this).percentPrice;
			clone.extraTotalPrice = ((Extra)this).extraTotalPrice;
			clone.label = ((Extra)this).label;
			return clone;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
		
	}

}
