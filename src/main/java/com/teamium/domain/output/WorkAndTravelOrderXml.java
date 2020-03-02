package com.teamium.domain.output;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.teamium.domain.prod.projects.order.WorkAndTravelOrder;

@XmlAccessorType(XmlAccessType.FIELD)
public class WorkAndTravelOrderXml {
	@XmlAttribute
	private Long id;

	@XmlElement(namespace = XmlOutput.XMLNS)
	private String comment;

	@XmlElement(namespace = XmlOutput.XMLNS)
	private String mediaId;

	@XmlElementWrapper(name = "orderForms", namespace = XmlOutput.XMLNS)
	@XmlElement(name = "orderForm", namespace = XmlOutput.XMLNS)
	private List<OrderFormXml> orderForms;

	/**
	 * @param obj the object to marshal
	 */
	public void marshal(WorkAndTravelOrder obj) {
		this.id = obj.getId();
		this.mediaId = obj.getMediaId();
		this.comment = obj.getComment();
		this.orderForms = new ArrayList<>();
//		if (obj.getOrderForm() != null) {
//			for (OrderForm orderForm : obj.getOrderForm()) {
//				if (obj.getSelectedOrderType() == null || orderForm.getFormType().equals(obj.getSelectedOrderType())) {
//					OrderFormXml orderFormXml = new OrderFormXml();
//					orderFormXml.marshal(orderForm);
//					this.orderForms.add(orderFormXml);
//				}
//			}
//		}
	}
}
