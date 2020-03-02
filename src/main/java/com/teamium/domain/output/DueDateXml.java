package com.teamium.domain.output;

import java.util.Calendar;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.teamium.domain.MilestoneType;
import com.teamium.domain.output.adapters.CalendarDateAdapter;
import com.teamium.domain.prod.DueDate;
import com.teamium.domain.prod.projects.invoice.InvoiceDueDate;

/**
 * An XML DueDate output
 * 
 * @author sraybaud - NovaRem
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DueDateXml {

	/**
	 * Type
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private MilestoneType type;

	/**
	 * Date
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	@XmlJavaTypeAdapter(CalendarDateAdapter.class)
	private Calendar date;

	/**
	 * Date
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	@XmlJavaTypeAdapter(CalendarDateAdapter.class)
	private Calendar paymentDate;

	/**
	 * Due amount
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private Float amount;

	/**
	 * Invoice rate
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private Float rate;

	/**
	 * @param obj the object to marshal
	 */
	public void marshal(DueDate obj) {
		this.date = obj.getDate();
		if (obj instanceof InvoiceDueDate) {
			this.amount = ((InvoiceDueDate) obj).getAmount();
			this.rate = ((InvoiceDueDate) obj).getRate();
			this.paymentDate = ((InvoiceDueDate) obj).getPaymentDate();
		}
		if (obj.getType() != null) {
			this.type = obj.getType();
		} else {
			this.type = null;
		}
	}
}
