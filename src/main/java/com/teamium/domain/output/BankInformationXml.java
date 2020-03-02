package com.teamium.domain.output;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.teamium.domain.BankInformation;


/**
 * An XML bank information output
 * @author sraybaud - NovaRem
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BankInformationXml {
	/**
	 * Bank name
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String name;
	
	/**
	 * Bank address
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private AddressXml address;

	/**
	 * IBAN
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String iban;
	
	/**
	 * Swift
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String swift;

	/**
	 * @param obj the object to marshal
	 */
	public void marshal(BankInformation obj){
		this.iban=obj.getIban();
		this.swift=obj.getSwift();
	}	
}
