package com.teamium.domain.output;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.teamium.domain.prod.projects.Brief;

/**
 * The xml representation of a booking
 * @author JS
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BriefXml {

	/**
	 * brief title
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String title;
	
	/**
	 * brief content
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String description;
	
	
	public void marshal(Brief obj){
		this.title = obj.getBriefType().getLabel();
		this.description = obj.getDescription();
	}
	
}
