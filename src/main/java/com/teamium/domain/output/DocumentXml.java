package com.teamium.domain.output;

import java.net.URL;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.teamium.domain.Document;

/**
 * An XML document output
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentXml{
	/**
	 * Document id
	 */
	@XmlAttribute
	private Long id;
	
	/**
	 * Document name
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String name;
	
	/**
	 * Document url
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String url;
	
	/**
	 * Document path
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String path;
	
	
	/**
	 * @param obj the obj to marshal
	 */
	public void marshal(Document obj){
		this.name=obj.getName();
		this.path = obj.getPath();
		try{
			try{
				this.url=new URL(obj.getPath()).toString();
			}
			catch(Exception e){
				this.url=new URL(Document.DOCUMENT_ROOT_URL+obj.getPath()).toString();
			}
		}
		catch(Exception e){
			this.id=obj.getId();
		}
	}
	
}
