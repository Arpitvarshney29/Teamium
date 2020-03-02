/**
 * 
 */
package com.teamium.domain;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


/**
 * Describe a type owning a dedicated class
 * @author sraybaud- NovaRem
 */
@XmlAccessorType(XmlAccessType.FIELD)
@MappedSuperclass
public class ClassType extends AbstractXmlEntity{

	/**
	 * Class uid
	 */
	private static final long serialVersionUID = 8415698249587414153L;
	
	/**
	 * Customer rating policy type
	 */
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	@Transient
	private String className;

	/**
	 * Return an instance of of the current rating policy
	 * @return the created instance
	 * @throws TeamiumPersistenceException classType_create_failed
	 */
	public Object newInstance() throws TeamiumPersistenceException{
		Object o = null;
		try{
			o = Class.forName(className).newInstance();
			return o;
		}
		catch(Exception e){
			throw new TeamiumPersistenceException("classType_create_failed",e);
		}
	}
}
