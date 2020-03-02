package com.teamium.domain.prod.resources.equipments;

import java.io.Serializable;
import java.util.Currency;

import javax.persistence.Embeddable;

import com.teamium.dto.AtaDTO;

/**
 * 
 * @author mgenevier
 *
 */
@Embeddable
public class Ata implements Serializable {

	/**
	 * Class uid
	 */
	private static final long serialVersionUID = 1575827520416399558L;

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

	public Ata() {

	}

	public Ata(AtaDTO ataDTO) {
		this.weight = ataDTO.getWeight();
		this.value = ataDTO.getValue();
		this.currency = ataDTO.getCurrency();
		this.volume = ataDTO.getVolume();
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
	public Currency getCurrency() {
		if (currency == null)
			return null;
		else
			return Currency.getInstance(currency);
	}

	/**
	 * @param currency
	 *            the currency to set
	 */
	public void setCurrency(Currency currency) {
		if (currency == null)
			this.currency = null;
		else
			this.currency = currency.getCurrencyCode();
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
