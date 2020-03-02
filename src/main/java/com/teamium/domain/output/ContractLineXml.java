package com.teamium.domain.output;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.teamium.domain.output.adapters.CalendarDateAdapter;
import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.resources.staff.contract.ContractLine;

/**
 * The xml representation of a contractLine
 * @author JS
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ContractLineXml {

	/**
	 * The start period
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	@XmlJavaTypeAdapter(CalendarDateAdapter.class)
	private Calendar startPeriod ;
	
	/**
	 * The end period
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	@XmlJavaTypeAdapter(CalendarDateAdapter.class)
	private Calendar endPeriod;
	
	/**
	 * The due quantity
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private Integer dueQuantity;
	
	/**
	 * the bookings
	 */
	@XmlElement(namespace=XmlOutput.XMLNS,name="booking")
	@XmlElementWrapper(namespace=XmlOutput.XMLNS)
	private List<BookingXml> bookings;
	
	/**
	 * marshal the object into an xml entity
	 */
	public void marshal(ContractLine obj){
		if(obj != null){
			this.startPeriod = obj.getStartPeriod();
			this.endPeriod = obj.getEndPeriod();
			this.dueQuantity = obj.getDueQuantity();
			bookings = new ArrayList<BookingXml>();
			for(Booking b : obj.getBookings()){
				BookingXml xml = new BookingXml();
				xml.marshal(b);
				bookings.add(xml);
			}
		}
	}
	
	
}
