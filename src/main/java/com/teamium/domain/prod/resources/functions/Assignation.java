package com.teamium.domain.prod.resources.functions;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.teamium.domain.AbstractXmlEntity;

@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Assignation extends AbstractXmlEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -86135783354328740L;
	
	public static final Assignation INTERNAL = new Assignation().setInitKey("internal");
	public static final Assignation FREELANCE = new Assignation().setInitKey("freelance");
	public static final Assignation EXTERNAL = new Assignation().setInitKey("external");
	
	/**
	 * Used to initialize static Assignation
	 * @param key
	 * @return
	 */
	private Assignation setInitKey(String key){
		super.setKey(key);
		return this;
	}
	

	
	
}
