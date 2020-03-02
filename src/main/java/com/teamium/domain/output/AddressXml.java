package com.teamium.domain.output;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.teamium.domain.Address;

/**
 * An XML address output
 * @author sraybaud - NovaRem
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AddressXml {
	/**
	 * First line address
	 */
	@XmlElement(name="line", namespace=XmlOutput.XMLNS)
	private List<String> lines;

	/**
	 * ZIP Code
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String zipcode;
	
	/**
	 * City
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String city;
	
	/**
	 * Region/State
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String state;
	
	/**
	 * Country
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private ItemXml country;

	/**
	 * @param obj the object to marshal
	 */
	public void marshal(Address obj){
		this.lines = new ArrayList<String>();
		if(obj.getLine1()!=null) this.lines.add(obj.getLine1());
		if(obj.getLine2()!=null) this.lines.add(obj.getLine2());
		this.zipcode = obj.getZipcode();
		this.city = obj.getCity();
		this.state = obj.getState();
		if(obj.getCountry()!=null){
			this.country = new ItemXml();
//			this.country.marshal(obj.getCountry());
		}else{
			this.country=null;
		}
	}		
}
