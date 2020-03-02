/**
 * 
 */
package com.teamium.domain.prod.projects;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.teamium.domain.AbstractXmlEntity;
import com.teamium.domain.XmlEntity;

/**
 * @author slopes
 *
 */
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class CombinationType extends AbstractXmlEntity {

	/**
	 * The generated serial UID
	 */
	private static final long serialVersionUID = 6753669637509266294L;
	
	/**
	 * The address used to invoke the EJB to manage the updating and deleting process
	 */
	@XmlAttribute
	@Transient
	private String jndi;

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

}
