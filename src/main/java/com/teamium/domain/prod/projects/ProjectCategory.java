/**
 * 
 */
package com.teamium.domain.prod.projects;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;

import com.teamium.domain.AbstractXmlEntity;

/**
 * @author slopes
 *
 */
@Embeddable
public class ProjectCategory extends AbstractXmlEntity {
	/**
	 * The serial ID
	 */
	private static final long serialVersionUID = 5676508655567845122L;
	/**
	 * The object number
	 */
	@XmlAttribute
	@Transient
	private String objectNumber;

	/**
	 * @return the objectNumber
	 */
	public String getObjectNumber() {
		return objectNumber;
	}

	/**
	 * @param objectNumber the objectNumber to set
	 */
	public void setObjectNumber(String objectNumber) {
		this.objectNumber = objectNumber;
	}

}
