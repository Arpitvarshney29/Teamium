/**
 * 
 */
package com.teamium.domain.output;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.teamium.domain.Category;
import com.teamium.domain.output.adapters.CalendarDatetimeAdapter;
import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.projects.AbstractProject;
import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.projects.Project;
import com.teamium.domain.prod.projects.Quotation;

/**
 * Map in xml information specific to an abastract project
 * 
 * @author sraybaud
 * @version TEAM-43
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ProjectXml extends RecordXml {

	/**
	 * Production unit
	 */
	private String productionUnit;

	/**
	 * Channel
	 */
	private String channel;

	/**
	 * Agency
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private CompanyXml agency;

	/**
	 * Final customer
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private String finalCustomer;

	/**
	 * Director
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private ContactXml director;

	/**
	 * Project start
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	@XmlJavaTypeAdapter(CalendarDatetimeAdapter.class)
	private Calendar from;

	/**
	 * External reference
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private String externalReference;

	/**
	 * Internal reference
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private String internalReference;

	/**
	 * Brief
	 */
	@XmlElementWrapper(name = "bookings", namespace = XmlOutput.XMLNS)
	@XmlElement(name = "booking", namespace = XmlOutput.XMLNS)
	private List<BookingXml> bookings;

	/**
	 * Project end
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	@XmlJavaTypeAdapter(CalendarDatetimeAdapter.class)
	private Calendar to;

	/**
	 * Title
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private String title;

	/**
	 * The category
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private Category category;

	/**
	 * The subcategory
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private String subCategory;

	/**
	 * Linked Document
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private DocumentXml document;

	/**
	 * The program
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private String program;

	/**
	 * Project address
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private String prodAddress;

	/**
	 * Project description
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private String prodDescription;

	/**
	 * Project description
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private String paymentTerms;

	/**
	 * available functions
	 */
	@XmlElementWrapper(namespace = XmlOutput.XMLNS, name = "availableFunctions")
	@XmlElement(namespace = XmlOutput.XMLNS, name = "availableFunction")
	private List<String> availableFunctions;

	/**
	 * @param obj
	 *            the object to marshal
	 */
	public void marshal(AbstractProject obj) {
		if (obj != null) {
			super.marshal(obj);
			if (obj != null) {
				Record record = (Record) obj;
				availableFunctions = new ArrayList<String>();
				for (Line line : record.getLines()) {
					if (!(Boolean.TRUE.equals(line.getDisabled()))) {
						if (line.getFunction() != null
								&& !availableFunctions.contains(line.getFunction().getParent().getQualifiedName())) {
							availableFunctions.add(line.getFunction().getParent().getQualifiedName());
						}
					}
				}
			}
			if (obj.getAgency() != null) {
				this.agency = new CompanyXml();
				this.agency.marshal(obj.getAgency());
			}
			this.finalCustomer = obj.getClient();
			if (obj.getDirector() != null) {
				this.director = new ContactXml();
				this.director.marshal(obj.getDirector());
			}
			if (obj instanceof Quotation) {
				Quotation q = (Quotation) obj;
				this.from = q.getStartDate();
				this.to = q.getEndDate();
				if (q.getProgram() != null) {
					this.program = q.getProgram().getTitle();
				}
				if (q.getProductionUnit() != null) {
					this.productionUnit = q.getProductionUnit().getName();
				}
				if (q.getChannel() != null) {
					this.channel = q.getChannel().getName();
				}
				this.externalReference = q.getReferenceExternal();
				this.internalReference = q.getReferenceInternal();
			}
			if (obj.getTitle() != null)
				this.title = obj.getTitle();
			if (obj.getCategory() != null) {
				this.category = obj.getCategory();
			}
			this.subCategory = obj.getSubCategory();
			if (obj.getProdAddress() != null)
				this.prodAddress = obj.getProdAddress();
			if (obj.getProdOrganisation() != null)
				this.prodDescription = obj.getProdOrganisation();
			if (obj.getProgram() != null && obj.getProgram().getDocument() != null) {
				this.document = new DocumentXml();
				this.document.marshal(obj.getProgram().getDocument());
			}
			if (obj.getPaymentTerms() != null) {
				this.paymentTerms = obj.getPaymentTerms();
			}
			bookings = new ArrayList<BookingXml>();
			if (obj instanceof Project) {
				for (Line l : obj.getLines()) {
					if (l instanceof Booking) {
						Booking booking = (Booking) l;
						if (obj.getCurrentBookingId() == null || (obj.getCurrentBookingId().equals(booking.getId()))) {
//							if (obj.getCurrentBookingId() != null) {
//								if (booking.getWorkOrder() != null) {
//									booking.getWorkOrder().setSelectedOrderType(obj.getSelectedOrderType());
//								}
//								if (booking.getTravelOrder() != null) {
//									booking.getTravelOrder().setSelectedOrderType(obj.getSelectedOrderType());
//								}
//							}
							BookingXml b = new BookingXml();
							b.marshal(booking);
							bookings.add(b);
						}
					}
				}
			}
		}
	}

}
