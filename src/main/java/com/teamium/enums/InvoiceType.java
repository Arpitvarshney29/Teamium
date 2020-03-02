package com.teamium.enums;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.teamium.exception.UnprocessableEntityException;

public enum InvoiceType {

	FINAL("Final"), PROGRESS("Progress"), PERIOD("Period");

	private String invoiceType;

	/**
	 * @param gender
	 */
	private InvoiceType(String conventionType) {
		this.invoiceType = conventionType;
	}

	/**
	 * 
	 * @return
	 */

	public String getInvoiceType() {
		return invoiceType;
	}

	/**
	 * 
	 * @param invoiceType
	 */
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	/**
	 * Method to get the invoice type enum
	 * 
	 * @param value the String enum value
	 * 
	 * @return the gender
	 */
	public static InvoiceType getEnum(String value) {
		for (InvoiceType v : values()) {
			if (v.getInvoiceType().equalsIgnoreCase(value)) {
				return v;
			}
		}
		throw new UnprocessableEntityException("Invalid invoice type ");
	}

	/**
	 * To get list of gender type
	 * 
	 * @return gender type list
	 */
	public static List<String> getInvoiceTypes() {
		return Stream.of(values()).map(reel -> reel.getInvoiceType()).collect(Collectors.toList());
	}
}
