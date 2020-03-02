package com.teamium.dto;

import java.util.Calendar;
import java.util.Currency;

import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.resources.equipments.MaintenanceContract;

/**
 * DTO Class for MaintenanceContract Entity
 * 
 * @author wittybrains
 *
 */

public class MaintenanceContractDTO extends AbstractDTO {

	private Calendar start;
	private Calendar end;
	private XmlEntity type;
	private Float amount;
	private Currency currency;
	private SupplierDTO supplier;

	public MaintenanceContractDTO() {
		super();
	}

	public MaintenanceContractDTO(MaintenanceContract entity) {
		super(entity);
		this.start = entity.getStart();
		this.end = entity.getEnd();
		this.type = entity.getType();
		this.amount = entity.getAmount();
		this.currency = entity.getCurrency();
	}

	/**
	 * Get MaintenanceContract object from DTO
	 * 
	 * @param maintenanceContract
	 * @return MaintenanceContract
	 */
	public MaintenanceContract getMaintenanceContract(MaintenanceContract maintenanceContract) {
		maintenanceContract.setStart(start);
		maintenanceContract.setEnd(end);
		maintenanceContract.setType(type);
		maintenanceContract.setAmount(amount);
		maintenanceContract.setCurrency(currency);
		return maintenanceContract;
	}

	/**
	 * @return the start
	 */
	public Calendar getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(Calendar start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public Calendar getEnd() {
		return end;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public void setEnd(Calendar end) {
		this.end = end;
	}

	/**
	 * @return the type
	 */
	public XmlEntity getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(XmlEntity type) {
		this.type = type;
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

	/**
	 * @return the currency
	 */
	public Currency getCurrency() {
		return currency;
	}

	/**
	 * @param currency
	 *            the currency to set
	 */
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	/**
	 * @return the supplier
	 */
	public SupplierDTO getSupplier() {
		return supplier;
	}

	/**
	 * @param supplier
	 *            the supplier to set
	 */
	public void setSupplier(SupplierDTO supplier) {
		this.supplier = supplier;
	}

}
