/**
 * 
 */
package com.teamium.domain.prod.resources.suppliers;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.teamium.domain.ProfileType;



/**
 * Describe a supplier type of profile
 * @author sraybaud- NovaRem
 */
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
public class SupplierProfileType extends ProfileType{

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -9185142261343317569L;	
}
