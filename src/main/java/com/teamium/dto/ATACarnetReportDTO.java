package com.teamium.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author wittybrains
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ATACarnetReportDTO {

	private long equipmentId;
	private String description;
	private int numberOfPiece;
	private Float weight;
	private String countryOfOrigin;
	private Double value;

	private Float totalWeight;
	private Double totalValue;

	public ATACarnetReportDTO() {

	}

	/**
	 * 
	 * @return
	 */
	public long getEquipmentId() {
		return equipmentId;
	}

	/**
	 * 
	 * @param equipmentId
	 */
	public void setEquipmentId(long equipmentId) {
		this.equipmentId = equipmentId;
	}

	/**
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public int getNumberOfPiece() {
		return numberOfPiece;
	}

	/**
	 * 
	 * @param numberOfPiece
	 */
	public void setNumberOfPiece(int numberOfPiece) {
		this.numberOfPiece = numberOfPiece;
	}

	/**
	 * 
	 * @return
	 */
	public Float getWeight() {
		return weight;
	}

	/**
	 * 
	 * @param weight
	 */
	public void setWeight(Float weight) {
		this.weight = weight;
	}

	/**
	 * 
	 * @return
	 */
	public String getCountryOfOrigin() {
		return countryOfOrigin;
	}

	/**
	 * 
	 * @param countryOfOrigin
	 */
	public void setCountryOfOrigin(String countryOfOrigin) {
		this.countryOfOrigin = countryOfOrigin;
	}

	/**
	 * 
	 * @return
	 */
	public Double getValue() {
		return value;
	}

	/**
	 * 
	 * @param value
	 */
	public void setValue(Double value) {
		this.value = value;
	}

	/**
	 * 
	 * @return
	 */
	public Float getTotalWeight() {
		return totalWeight;
	}

	/**
	 * 
	 * @param totalWeight
	 */
	public void setTotalWeight(Float totalWeight) {
		this.totalWeight = totalWeight;
	}

	/**
	 * 
	 * @return
	 */
	public Double getTotalValue() {
		return totalValue;
	}

	/**
	 * 
	 * @param totalValue
	 */
	public void setTotalValue(Double totalValue) {
		this.totalValue = totalValue;
	}

}
