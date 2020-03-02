package com.teamium.domain.output;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.teamium.domain.AbstractXmlEntity;

/**
 * An XML fee output
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class FeeXml extends RatedAmountXml {

	/**
	 * Type
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private ItemXml type;

	/**
	 * @param type
	 *            the type of fee
	 * @param rate
	 *            the rate
	 * @param amount
	 *            the amount
	 */
	public void marshal(AbstractXmlEntity type, Float rate, Float amount) {
		if (type != null) {
			this.type = new ItemXml();
			this.type.marshal(type);
		}
		this.marshal(rate, amount);
	}

}
