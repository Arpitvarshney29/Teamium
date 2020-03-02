package com.teamium.domain.prod.resources.staff.contract;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.teamium.domain.AbstractXmlEntity;
import com.teamium.domain.TeamiumConstants;

/**
 * ContractEdition
 * @author JS
 *
 */
@Embeddable
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ContractEdition extends AbstractXmlEntity{

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -2332324551033745970L;
	
	/**
	 * Edition key
	 */
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	@Transient
	private String editionKey;

	/**
	 * 
	 * @return
	 */
	public String getEditionKey() {
		return editionKey;
	}

	/**
	 * 
	 */
	public void setEditionKey(String editionKey) {
		this.editionKey = editionKey;
	}

}
