package com.teamium.dto;

public class LineCostDTO {
	private Float lineCost;
	private Float discount;
	private Float vat;

	public LineCostDTO() {
	}

	public LineCostDTO(Float lineCost, Float discount, Float vat) {

		this.lineCost = lineCost;
		this.discount = discount;
		this.vat = vat;
	}

	/**
	 * 
	 * @return
	 */
	public Float getLineCost() {
		return lineCost;
	}

	/**
	 * 
	 * @param lineCost
	 */
	public void setLineCost(Float lineCost) {
		this.lineCost = lineCost;
	}

	/**
	 * 
	 * @return
	 */
	public Float getDiscount() {
		return discount;
	}

	/**
	 * 
	 * @param discount
	 */
	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	/**
	 * 
	 * @return
	 */
	public Float getVat() {
		return vat;
	}

	/**
	 * 
	 * @param vat
	 */
	public void setVat(Float vat) {
		this.vat = vat;
	}

}
