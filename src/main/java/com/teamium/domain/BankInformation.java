/**
 * 
 */
package com.teamium.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Describe bank information for company
 * @author sraybaud
 *
 */
@Embeddable
public class BankInformation implements Serializable {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = -4196195952140636989L;

	/**
	 * IBAN
	 */
	@Column(name="c_bank_iban")
	private String iban;
	
	/**
	 * Swift
	 */
	@Column(name="c_bank_swift")
	private String swift;

	/**
	 * @return the iban
	 */
	public String getIban() {
		return iban;
	}

	/**
	 * @param iban the iban to set
	 */
	public void setIban(String iban) {
		this.iban = iban;
	}

	/**
	 * @return the swift
	 */
	public String getSwift() {
		return swift;
	}

	/**
	 * @param swift the swift to set
	 */
	public void setSwift(String swift) {
		this.swift = swift;
	}
	
	/**
	 * Return the string representation of the bank information
	 * @return the string
	 */
	@Override
	public String toString(){
		return swift+" "+iban;
	}

}
