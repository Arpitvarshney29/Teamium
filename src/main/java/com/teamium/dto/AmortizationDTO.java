package com.teamium.dto;

import java.util.Calendar;

import com.teamium.domain.prod.resources.equipments.Amortization;


/**
 * DTO for Amortization Entity
 * 
 * @author wittybrains
 *
 */
public class AmortizationDTO extends AbstractDTO {
	private Calendar date;
	private Float percent;
	private Float value;

	public AmortizationDTO() {
		super();

	}

	public AmortizationDTO(Amortization amortization) {
		super(amortization.getId(), amortization.getVersion());
		this.date = amortization.getDate();
		this.percent = amortization.getPercent();
		this.value = amortization.getValue();
	}
	
	/**
	 * Get Amortization Entity from AmortizationDTO
	 * @param amortization
	 * @return Amortization
	 */
	public Amortization getAmortizationDetails(Amortization amortization) {
		amortization.setDate(date);
		amortization.setPercent(percent);
		amortization.setValue(value);
		super.getAbstractEntityDetails(amortization);
		return amortization;
	}

	/**
	 * @return the date
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 * @return the percent
	 */
	public Float getPercent() {
		return percent;
	}

	/**
	 * @param percent
	 *            the percent to set
	 */
	public void setPercent(Float percent) {
		this.percent = percent;
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

}
