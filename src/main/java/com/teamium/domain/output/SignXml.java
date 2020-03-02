package com.teamium.domain.output;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Xml representation for Sign
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SignXml {

	/**
	 * The id of person who has to sign either humanResource, vendor or parttime etc.
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private String id;

	/**
	 * The sign Base64 value
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private String value;
	
	/**
	 * The first name of person who has to sign.
	 */
	
	@XmlElement(namespace = XmlOutput.XMLNS)
	private String firstName;
	
	/**
	 * The last name of person who has to sign.
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private String lastName;

	/**
	 * Marshal the values in xml file
	 * 
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param value
	 */
	public void marshallar(String id, String value, String firstName, String lastName) {
		this.id = id;
		this.value = value;
		this.firstName = firstName;
		this.lastName = lastName;
		
	}
}
