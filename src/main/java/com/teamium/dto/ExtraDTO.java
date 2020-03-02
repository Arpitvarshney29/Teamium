package com.teamium.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.prod.resources.functions.Extra;
import com.teamium.domain.prod.resources.functions.ExtraFunction;

/**
 * 
 * Wrapper class for Extra
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ExtraDTO extends AbstractDTO {

	private Long id;
	private String label;

	// private double basis;
	// private double basisPrice;
	// private double percent;
	// private double percentPrice;
	// private int quantity;

	private double cost;
	private double price;

	public ExtraDTO() {
		super();
	}

	public ExtraDTO(Extra extra) {
		super(extra);
		this.id = extra.getId();
		this.setVersion(extra.getVersion());
		this.label = extra.getLabel();

		// this.basis = extra.getBasis() == null ? 0 : extra.getBasis();
		// this.basisPrice = extra.getBasisPrice() == null ? 0 : extra.getBasisPrice();

		// this.percent = extra.getPercent() == null ? 0 : extra.getPercent() * 100;
		// this.percentPrice = extra.getPercentPrice() == null ? 0 :
		// extra.getPercentPrice() * 100;
		// this.quantity = extra.getQuantity() == null ? 0 :
		// extra.getQuantity().intValue();

		this.cost = extra.getExtraTotal() == null ? 0 : extra.getExtraTotal();
		this.price = extra.getExtraTotalPrice() == null ? 0 : extra.getExtraTotalPrice();
	}

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

	// /**
	// * @return the basis
	// */
	// public double getBasis() {
	// return basis;
	// }
	//
	// /**
	// * @param basis
	// * the basis to set
	// */
	// public void setBasis(double basis) {
	// this.basis = basis;
	// }
	//
	// /**
	// * @return the basisPrice
	// */
	// public double getBasisPrice() {
	// return basisPrice;
	// }
	//
	// /**
	// * @param basisPrice
	// * the basisPrice to set
	// */
	// public void setBasisPrice(double basisPrice) {
	// this.basisPrice = basisPrice;
	// }

	// /**
	// * @return the percent
	// */
	// public double getPercent() {
	// return percent;
	// }
	//
	// /**
	// * @param percent
	// * the percent to set
	// */
	// public void setPercent(double percent) {
	// this.percent = percent;
	// }
	//
	// /**
	// * @return the percentPrice
	// */
	// public double getPercentPrice() {
	// return percentPrice;
	// }
	//
	// /**
	// * @param percentPrice
	// * the percentPrice to set
	// */
	// public void setPercentPrice(double percentPrice) {
	// this.percentPrice = percentPrice;
	// }
	//
	// /**
	// * @return the quantity
	// */
	// public int getQuantity() {
	// return quantity;
	// }
	//
	// /**
	// * @param quantity
	// * the quantity to set
	// */
	// public void setQuantity(int quantity) {
	// this.quantity = quantity;
	// }

	/**
	 * @return the extraTotal
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * @param extraTotal the extraTotal to set
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}

	/**
	 * @return the extraTotalPrice
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param extraTotalPrice the extraTotalPrice to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * Method to get extra
	 * 
	 * @param extra the extra
	 * 
	 * @return extra object
	 */
	@JsonIgnore
	public Extra getExtra(Extra extra) {
		extra.setId(this.id);
		extra.setVersion(this.getVersion());
		extra.setLabel(this.label);
		// extra.setBasis(this.basis);
		// extra.setBasisPrice(this.basisPrice);
		// extra.setPercent(this.percent);
		// extra.setPercentPrice(this.percentPrice);
		// extra.setQuantity(this.quantity);

		extra.setExtraTotal(this.cost);
		extra.setExtraTotalPrice(this.price);
		return extra;
	}

	/**
	 * To get entity from DTO
	 * 
	 * @param extra
	 * @return extra Function
	 */
	@JsonIgnore
	public ExtraFunction getExtraFunction(ExtraFunction extra) {
		this.getExtra(extra);
		return extra;
	}

	/**
	 * To get entity from DTO
	 * 
	 * @param extra
	 * @return extra Function
	 */
	@JsonIgnore
	public ExtraFunction getExtra() {
		return this.getExtraFunction(new ExtraFunction());
	}
}
