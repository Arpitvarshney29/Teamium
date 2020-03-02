package com.teamium.dto;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.Fee;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.projects.invoice.Invoice;

@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class FeeDTO extends AbstractDTO {

	private Long id;
	private RecordDTO record;
	private String type;
	private Float rate;
	private Float amount;

	public FeeDTO() {
		super();
	}

	public FeeDTO(Fee fee) {
		super(fee);
		this.id = fee.getId();
		this.type = fee.getType() == null ? null : fee.getType().getKey();
		this.rate = fee.getRate();
		this.amount = fee.getAmount();
	}

	/**
	 * method to get fee
	 * 
	 * @param fee
	 *            the fee
	 * 
	 * @param feeDTO
	 *            the feeDTO
	 * 
	 * @return Fee object
	 */
	public Fee getFee(Fee fee, FeeDTO feeDTO) {
		setFeeDetail(fee, feeDTO);
		return fee;
	}

	/**
	 * set fee details
	 * 
	 * @param fee
	 *            the fee
	 * 
	 * @param feeDTO
	 *            the feeDTO
	 */
	public void setFeeDetail(Fee fee, FeeDTO feeDTO) {
		fee.setId(feeDTO.getId());
		fee.setVersion(feeDTO.getVersion());
		RecordDTO recordDTO = feeDTO.getRecord();
		if (recordDTO != null) {
			Record record = recordDTO.getRecord(new Invoice(), recordDTO);
			fee.setRecord(record);
		}
		if (!StringUtils.isBlank(feeDTO.getType())) {
			XmlEntity typeEntity = new XmlEntity();
			typeEntity.setKey(feeDTO.getType());
			fee.setType(typeEntity);
		}
		fee.setRate(feeDTO.getRate());
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the record
	 */
	public RecordDTO getRecord() {
		return record;
	}

	/**
	 * @param record
	 *            the record to set
	 */
	public void setRecord(RecordDTO record) {
		this.record = record;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
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

	/**
	 * @return the amount
	 */
	public Float getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(Float amount) {
		this.amount = amount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FeeDTO [id=" + id + ", record=" + record + ", type=" + type + ", rate=" + rate + ", amount=" + amount
				+ "]";
	}

}
