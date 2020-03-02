package com.teamium.dto;

import java.util.Currency;

import com.teamium.domain.prod.resources.equipments.Ata;

/**
 * DTO Class for Ata Entity
 * 
 * @author wittybrains
 *
 */
public class AtaDTO {
	/*
	 * The weight
	 */
	private Float weight;

	/*
	 * The value
	 */
	private Float value;

	/*
	 * The currency
	 */
	private String currency;

	/*
	 * The volume
	 */
	private Float volume;

	public AtaDTO() {
	}

	public AtaDTO(Float weight, Float value, String currency, Float volume) {
		this.weight = weight;
		this.value = value;
		this.currency = currency;
		this.volume = volume;
	}

	public AtaDTO(Ata ata) {
		this.weight = ata.getWeight();
		this.value = ata.getValue();
		Currency currecy = ata.getCurrency();
		if (currecy != null)
			this.currency = currecy.getCurrencyCode();
		this.volume = ata.getVolume();
	}



	/**
	 * @return the weight
	 */
	public Float getWeight() {
		return weight;
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(Float weight) {
		this.weight = weight;
	}

	/**
	 * @return the value
	 */
	public Float getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Float value) {
		this.value = value;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency
	 *            the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the volume
	 */
	public Float getVolume() {
		return volume;
	}

	/**
	 * @param volume
	 *            the volume to set
	 */
	public void setVolume(Float volume) {
		this.volume = volume;
	}

}
