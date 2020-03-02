package com.teamium.domain.output;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.teamium.domain.AbstractXmlEntity;
import com.teamium.domain.XmlEntity;

/**
 * An XML Number output
 * @author sraybaud - NovaRem
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class NumberXml {

	/**
	 * Type
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private ItemXml type;
	
	/**
	 * Date
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String value;
	
	/**
	 * @param type the type of number
	 * @param value the assigned value
	 */
	public void marshal(AbstractXmlEntity type, String value){
		this.value=value;
		if(type!=null){
			this.type = new ItemXml();
			this.type.marshal(type);
		}else{
			this.type=null;
		}
	}	
	
	/**
	 * @param key the key
	 * @param value the value
	 */
	public void marshal(String key, String value){
		XmlEntity type = null;
		if(key!=null){
			type = new XmlEntity();
			type.setKey(key);
		}
		this.marshal(type, value);
	}	
}
