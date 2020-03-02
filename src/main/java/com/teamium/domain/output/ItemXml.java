package com.teamium.domain.output;

import java.util.Currency;
import java.util.Locale;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.teamium.domain.AbstractXmlEntity;

/**
 * An Xml item output
 * @author sraybaud - NovaRem
 * @version TEAM-43
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemXml {
	/**
	 * Key
	 */
	@XmlAttribute
	private String key;

	/**
	 * Label
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String label;	
	
	/**
	 * @param obj the object to marshal
	 */
	public void marshal(AbstractXmlEntity obj){
		key=obj.getKey();
		label = obj.getLabel();		
	}
	
	/**
	 * @param obj the object to marshal
	 */
	public void marshal(Locale obj){
		key=obj.getCountry();
		label = obj.getDisplayCountry();
	}
	
	/**
	 * @param obj the object to marshal
	 */
	public void marshal(Currency obj){
		if(obj == null)
			obj=Currency.getInstance(Locale.FRANCE);
		key=obj.getCurrencyCode();
		label = obj.getSymbol();
	}
}
