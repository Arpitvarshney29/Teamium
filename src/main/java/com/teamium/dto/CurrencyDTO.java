package com.teamium.dto;

import java.util.Currency;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.CurrencySymbol;

/**
 * 
 * Wrapper class for Currency
 *
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CurrencyDTO {

	private Long id;
	private String code;
	private String symbol;
	private boolean active;
	private boolean defaultCurrency;

	public CurrencyDTO() {
	}

	/**
	 * To make a wrapper object from Currency(java.util) object
	 * 
	 * @param obj the Currency object
	 */
	public CurrencyDTO(Currency obj) {
		if (obj != null) {
			this.code = obj.getCurrencyCode();
			this.symbol = obj.getSymbol();
		}
	}

	public CurrencyDTO(CurrencySymbol entity) {
		if (entity != null) {
			this.id = entity.getId();
			this.code = entity.getCode();
			this.symbol = entity.getSymbol();
			this.active = entity.isActive();
			this.defaultCurrency = entity.isDefaultCurrency();
		}
	}

	/**
	 * @param code
	 * @param symbol
	 */
	public CurrencyDTO(String code, String symbol) {
		this.code = code;
		this.symbol = symbol;
	}

	/**
	 * @param code
	 * @param symbol
	 * @param active
	 */
	public CurrencyDTO(String code, String symbol, boolean active, boolean defaultCurrency) {
		this.code = code;
		this.symbol = symbol;
		this.active = active;
		this.defaultCurrency = defaultCurrency;
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
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the defaultCurrency
	 */
	public boolean isDefaultCurrency() {
		return defaultCurrency;
	}

	/**
	 * @param defaultCurrency the defaultCurrency to set
	 */
	public void setDefaultCurrency(boolean defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}

	/**
	 * To get entity object by dto
	 * 
	 * @param entity
	 * 
	 * @return updated entity with wrapper fields
	 */
	@JsonIgnore
	public CurrencySymbol getCurrency(CurrencySymbol entity) {
		entity.setId(this.getId());
		if(!StringUtils.isBlank(this.getSymbol()))
		entity.setSymbol(this.getSymbol());
		if(!StringUtils.isBlank(this.getCode()))
		entity.setCode(this.getCode().toUpperCase());
		entity.setActive(this.isActive());
		entity.setDefaultCurrency(this.isDefaultCurrency());
		return entity;
	}

}
