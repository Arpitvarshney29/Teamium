package com.teamium.domain.output;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.teamium.domain.output.adapters.CalendarDatetimeAdapter;
import com.teamium.domain.prod.projects.AbstractProject;
import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.projects.order.WorkAndTravelOrder;

/**
 * The xml representation of a booking
 * 
 * @author Sébastien Raybaud
 * @version TEAM-43
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BookingXml {

	@XmlAttribute
	private Long originId;

	@XmlAttribute
	private Long id;

	/**
	 * The from date
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	@XmlJavaTypeAdapter(CalendarDatetimeAdapter.class)
	private Calendar from;

	/**
	 * The to date
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	@XmlJavaTypeAdapter(CalendarDatetimeAdapter.class)
	private Calendar to;

	/**
	 * The linked project
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private String linkedProject;

	/**
	 * The asked quantity
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private Float quantity;

	/**
	 * The asked unit
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private ItemXml unit;

	/**
	 * Staff resource name
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private ContactXml staff;

	/**
	 * The resource name
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private ResourceXml resource;

	/**
	 * Comments about the booking
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private String comment;

	

	/**
	 * marshal an entity into an xml entity
	 * 
	 * @param obj
	 */
	public void marshal(Booking obj) {
		if (obj != null) {
			this.id = obj.getId();
			if (obj.getOrigin() != null)
				this.originId = obj.getOrigin();
			this.from = obj.getFrom();
			this.to = obj.getTo();
			if (obj.getUnitUsed() != null) {
				unit = new ItemXml();
				unit.marshal(obj.getUnitUsed());
			}
			if (obj.getRecord() instanceof AbstractProject) {
				this.linkedProject = ((AbstractProject) obj.getRecord()).getTitle();
			}
			quantity = obj.getQtyTotalUsed();
			if (obj.getResource() != null) {
				this.resource = new ResourceXml();
				this.resource.marshal(obj);
			}
			
			this.comment = obj.getComment();

		}
	}

}
