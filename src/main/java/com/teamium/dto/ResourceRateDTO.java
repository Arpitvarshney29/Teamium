package com.teamium.dto;

import java.util.Currency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teamium.domain.prod.resources.functions.units.RateUnit;
import com.teamium.domain.prod.resources.staff.ResourceRate;
import com.teamium.enums.RateUnitType;
import com.teamium.exception.UnprocessableEntityException;

/**
 * DTO Class for ResourceRate Entity
 * 
 * @author wittybrains
 *
 */
public class ResourceRateDTO extends AbstractDTO {
	private Float value;
	private String currency;
	private String basis;
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	public ResourceRateDTO() {
	}

	public ResourceRateDTO(ResourceRate entity) {
		super(entity);
		this.value = entity.getValue();
		if (entity.getCurrency() != null) {
			this.currency = entity.getCurrency().getCurrencyCode();
		}
		if (entity.getRateUnit() != null) {
			this.basis = entity.getRateUnit().getKey();
		}

	}

	@JsonIgnore
	public ResourceRate getResourceRateDetails(ResourceRate resourceRate) {
		if (currency == null) {
			logger.info("Please enter valid currency for rate.");
			throw new UnprocessableEntityException("Please enter valid currency for rate.");
		}
		if (basis == null) {
			logger.info("Please enter valid basis for rate.");
			throw new UnprocessableEntityException("Please enter valid basis for rate.");
		}
		if (value == null) {
			logger.info("Please enter valid value for rate.");
			throw new UnprocessableEntityException("Please enter valid value for rate.");
		}
		resourceRate.setValue(value);

		try {
			resourceRate.setCurrency(Currency.getInstance(currency));
		} catch (IllegalArgumentException e) {
			logger.info("Please enter valid currency for rate :: "+e);
			throw new UnprocessableEntityException("Please enter valid currency for rate.");
		}
		RateUnit unit = new RateUnit();
		unit.setKey(RateUnitType.getEnum(basis).getType());
		resourceRate.setRateUnit(unit);
		super.getAbstractEntityDetails(resourceRate);
		return resourceRate;

	}

	/**
	 * @return the value
	 */
	public Float getValue() {
		return value;
	}

	/**
	 * @param value the value to set
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
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the basis
	 */
	public String getBasis() {
		return basis;
	}

	/**
	 * @param basis the basis to set
	 */
	public void setBasis(String basis) {
		this.basis = basis;
	}

}
