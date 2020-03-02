package com.teamium.domain.output;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.teamium.domain.output.adapters.CalendarDateAdapter;
import com.teamium.domain.prod.projects.AbstractProject;
import com.teamium.domain.prod.projects.invoice.Invoice;

/**
 * Xml representation of invoice
 * @author JS
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class InvoiceXml extends RecordXml{

	/**
	 * Invoice number
	 */
	@XmlAttribute
	private String idInvoice;
	
	/**
	 * Invoice date
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	@XmlJavaTypeAdapter(CalendarDateAdapter.class)
	private Calendar date;
	
	/**
	 * The source project
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private ProjectXml source;
	
	/**
	 * Marshal method
	 * @param obj
	 */
	public void marshal(Invoice obj){
		super.marshal(obj);
		idInvoice = obj.getId().toString();
		date = Calendar.getInstance();
		source = new ProjectXml();
		source.marshal((AbstractProject)obj.getSource());
	}
	
}
