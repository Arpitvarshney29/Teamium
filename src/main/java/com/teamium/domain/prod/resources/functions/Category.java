package com.teamium.domain.prod.resources.functions;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.teamium.domain.AbstractXmlEntity;
import com.teamium.domain.TeamiumConstants;

/**
 * Class which represents function categories
 * @author JS
 *
 */
@Embeddable
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Category extends AbstractXmlEntity{

	/**
	 * Generated ID
	 */
	private static final long serialVersionUID = 1914979979199549699L;
	
	/**
	 * The category number
	 */
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	@Transient
	private Double chargePercent;

	/**
	 * @return the chargePercent
	 */
	public Double getChargePercent() {
		return chargePercent;
	}

	/**
	 * @param chargePercent the chargePercent to set
	 */
	public void setChargePercent(Double chargePercent) {
		this.chargePercent = chargePercent;
	}

}
