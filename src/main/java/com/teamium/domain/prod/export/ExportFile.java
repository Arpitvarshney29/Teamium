/**
 * 
 */
package com.teamium.domain.prod.export;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.teamium.domain.AbstractXmlEntity;

/**
 * @author slopes
 * Represent an element 
 *
 */
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ExportFile extends AbstractXmlEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 346449857055404676L;
	/**
	 * The JNDI address used to retrieve the EJB in the custom module
	 */
	@XmlAttribute
	private String jndi;
	/**
	 * The file name of generated file
	 */
	@XmlAttribute
	private String generatedFileName;
	/**
	 * @return the jndi
	 */
	public String getJndi() {
		return jndi;
	}
	/**
	 * @param jndi the jndi to set
	 */
	public void setJndi(String jndi) {
		this.jndi = jndi;
	}
	/**
	 * @return the fileName
	 */
	public String getGeneratedFileName() {
		return generatedFileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setGeneratedFileName(String generatedFileName) {
		this.generatedFileName = generatedFileName;
	}
}
