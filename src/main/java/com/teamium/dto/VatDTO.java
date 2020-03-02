package com.teamium.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.Vat;

@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class VatDTO {

	private Float rate;

	public VatDTO() {

	}

	public VatDTO(Vat vat) {
		this.rate = vat.getRate();
	}

	public Vat getVat(Vat vat, VatDTO vatDTO) {
		setVatDetail(vat, vatDTO);
		return vat;
	}

	public void setVatDetail(Vat vat, VatDTO vatDTO) {
		vat.setRate(vatDTO.getRate());
	}

	/**
	 * @return the rate
	 */
	public Float getRate() {
		return rate;
	}

	/**
	 * @param rate
	 *            the rate to set
	 */
	public void setRate(Float rate) {
		this.rate = rate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VatDTO [rate=" + rate + "]";
	}

}
