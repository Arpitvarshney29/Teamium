package com.teamium.domain.output;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class OrderFormXml {
	/**
	 * Id
	 */
	@XmlAttribute
	private Long id;
	/**
	 * Form Type
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private String formType;
	/**
	 * Order Type
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private String orderType;

	@XmlElementWrapper(name = "orderkeywords", namespace = XmlOutput.XMLNS)
	@XmlElement(name = "orderkeyword", namespace = XmlOutput.XMLNS)
	private List<OrderKeywordXml> orderKeywords;

//	/**
//	 * @param obj
//	 *            the object to marshal
//	 */
//	public void marshal(OrderForm obj) {
//		this.id = obj.getId();
//		this.formType = obj.getFormType();
//		this.orderType = obj.getOrderType();
//		this.orderKeywords = new ArrayList<OrderKeywordXml>();
//		if (obj.getKewords() != null) {
//			for (OrderKeyword orderKeyword : obj.getKewords()) {
//				OrderKeywordXml orderKeywordXml = new OrderKeywordXml();
//				orderKeywordXml.marshal(orderKeyword);
//				orderKeywords.add(orderKeywordXml);
//			}
//		}
//	}
}
