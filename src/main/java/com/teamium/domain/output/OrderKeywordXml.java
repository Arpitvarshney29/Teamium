package com.teamium.domain.output;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.teamium.domain.prod.projects.order.OrderKeyword;

@XmlAccessorType(XmlAccessType.FIELD)
public class OrderKeywordXml {
	/**
	 * Id
	 */
	@XmlAttribute
	private Long id;
	/**
	 * Keyword
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private String keyword;
	/**
	 * Keyword Value
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private String keywordValue;

	/**
	 * @param obj
	 *            the object to marshal
	 */
	public void marshal(OrderKeyword obj) {
		this.id = obj.getId();
		this.keyword = obj.getKeyword();
		this.keywordValue = obj.getKeywordValue();
	}
}
