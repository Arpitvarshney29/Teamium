/**
 * 
 */
package com.teamium.domain.prod.resources.contacts;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.teamium.domain.ProfileType;



/**
 * Describe a customer type of profile
 * @author sraybaud- NovaRem
 */
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerProfileType extends ProfileType{

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -3085061291845069671L;}
