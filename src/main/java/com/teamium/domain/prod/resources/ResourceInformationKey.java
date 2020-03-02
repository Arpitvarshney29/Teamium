/**
 * 
 */
package com.teamium.domain.prod.resources;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.teamium.domain.AbstractXmlEntity;

/**
 * @author slopes
 * Keyword used to generate information in equipment
 *
 */
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ResourceInformationKey extends AbstractXmlEntity {

	/**
	 * The generated serial UID
	 */
	private static final long serialVersionUID = -3916614968935903604L;
	
	/**
	 * The keyword
	 */
	@XmlAttribute
	@Column(name="c_keyword")
	private String keyword;

	/**
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * @param keyword the keyword to set
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
