/**
 * 
 */
package com.teamium.domain.output;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.teamium.domain.ContactRecord;
import com.teamium.domain.Vat;
import com.teamium.domain.output.adapters.CalendarDateAdapter;
import com.teamium.domain.prod.DueDate;
import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.RecordFee;
import com.teamium.domain.prod.projects.BallPark;
import com.teamium.domain.prod.projects.Brief;
import com.teamium.domain.prod.projects.Project;
import com.teamium.domain.prod.projects.Quotation;

/**
 * A project xml output
 * 
 * @author sraybaud
 * @version TEAM-43
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RecordXml implements OutputEntityRoot<Record> {

	/**
	 * Record ID
	 */
	@XmlAttribute
	private Long id;

	/**
	 * Record number
	 */
	@XmlElement(name = "recordNumber", namespace = XmlOutput.XMLNS)
	private String number;

	/**
	 * Record date
	 */
	@XmlElement(name = "recordDate", namespace = XmlOutput.XMLNS)
	@XmlJavaTypeAdapter(CalendarDateAdapter.class)
	private Calendar recordDate;
	
	/**
	 * purchaseOrderDate
	 */
	@XmlElement(name = "purchaseOrderDate", namespace = XmlOutput.XMLNS)
	@XmlJavaTypeAdapter(CalendarDateAdapter.class)
	private Calendar purchaseOrderDate;

	/**
	 * The contact
	 */
	@XmlElementWrapper(namespace = XmlOutput.XMLNS, name = "contacts")
	@XmlElement(namespace = XmlOutput.XMLNS, name = "contact")
	private List<ContactXml> contacts;

	/**
	 * Sale entity
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private CompanyXml saleEntity;

	/**
	 * targeted company
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private CompanyXml company;

	/**
	 * Due dates
	 */
	@XmlElementWrapper(name = "dueDates", namespace = XmlOutput.XMLNS)
	@XmlElement(name = "dueDate", namespace = XmlOutput.XMLNS)
	private List<DueDateXml> dueDates;

	/**
	 * Lines
	 */
	@XmlElementWrapper(name = "lines", namespace = XmlOutput.XMLNS)
	@XmlElement(name = "line", namespace = XmlOutput.XMLNS)
	private List<LineXml> lines;

	/**
	 * Summaries
	 */
	@XmlElementWrapper(name = "summaries", namespace = XmlOutput.XMLNS)
	@XmlElement(name = "summary", namespace = XmlOutput.XMLNS)
	private List<LineXml> summaries;

	/**
	 * Currency
	 */
	@XmlElement(name = "currency", namespace = XmlOutput.XMLNS)
	private ItemXml currency;

	/**
	 * Total amount
	 */
	@XmlElement(name = "amount", namespace = XmlOutput.XMLNS)
	private Float totalAmount;

	/**
	 * Total amount
	 */
	@XmlElement(name = "discount", namespace = XmlOutput.XMLNS)
	private RatedAmountXml discount;

	/**
	 * Total amount
	 */
	@XmlElement(name = "fee", namespace = XmlOutput.XMLNS)
	private List<FeeXml> fees;

	/**
	 * VAT
	 */
	@XmlElement(name = "vat", namespace = XmlOutput.XMLNS)
	private List<FeeXml> vat;

	/**
	 * VAT
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private Float amountIVAT;

	/**
	 * Format
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private String format;

	/**
	 * Version
	 */
	@XmlAttribute
	private String version;

	/**
	 * Length
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private String length;

	/**
	 * Brief
	 */
	@XmlElementWrapper(name = "briefs", namespace = XmlOutput.XMLNS)
	@XmlElement(name = "brief", namespace = XmlOutput.XMLNS)
	private List<BriefXml> briefs;

	/**
	 * Description
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private String description;

	/**
	 * Source ID
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private String source;

	/**
	 * Source ID
	 */
	@XmlElement(namespace = XmlOutput.XMLNS)
	private String sourceTitle;

	@XmlElement(namespace = XmlOutput.XMLNS)
	private String paymentTerms;

	@XmlElement(namespace = XmlOutput.XMLNS)
	private String country;

	@XmlElement(namespace = XmlOutput.XMLNS)
	private String city;

	@XmlElementWrapper(name = "signs", namespace = XmlOutput.XMLNS)
	@XmlElement(name = "sign", namespace = XmlOutput.XMLNS)
	private List<SignXml> signs;

	
	/**
	 * @param obj
	 *            the object to marshal
	 */
	@Override
	public void marshal(Record obj) {
		if (obj != null) {
			this.id = obj.getId();
			this.number = obj.getNumObjet();
			this.recordDate = obj.getDate();
			this.purchaseOrderDate = obj.getPurchaseOrderDate();
			this.description = obj.getInformation().getComment();
			this.paymentTerms = obj.getPaymentTerms();
			this.country = obj.getCountry() == null ? null : obj.getCountry().getKey();
			this.city = obj.getCity();
			if (obj.getEntity() != null) {
				this.saleEntity = new CompanyXml();
				this.saleEntity.marshal(obj.getEntity());
				this.saleEntity.setContact(obj.getFollower());
			} else {
				this.saleEntity = null;
			}
			if (obj.getCompany() != null) {
				this.company = new CompanyXml();
				this.company.marshal(obj.getCompany());
				this.company.setContact(obj.getContacts().isEmpty() ? null : obj.getContacts().get(0).getPerson());
			} else {
				this.company = null;
			}
			this.dueDates = new ArrayList<DueDateXml>();
			for (DueDate date : obj.getDueDates()) {
				DueDateXml d = new DueDateXml();
				d.marshal(date);
				this.dueDates.add(d);
			}
			if (obj.getContacts() != null) {
				this.contacts = new ArrayList<ContactXml>();
				for (ContactRecord cr : obj.getContacts()) {
					ContactXml c = new ContactXml();
					c.marshal(cr);
					this.contacts.add(c);
				}
			}

			if (obj.getInformation() != null && obj.getInformation().getFormat() != null)
				this.format = obj.getInformation().getFormat().getKey();

			this.lines = new ArrayList<LineXml>();
			HashMap<FunctionXml, List<LineXml>> tempSummaries = new HashMap<FunctionXml, List<LineXml>>();
			// Sort lines
			Collections.sort(obj.getLines());
			for (Line line : obj.getLines()) {
				if (!(Boolean.TRUE.equals(line.getDisabled()))) {
					LineXml l = new LineXml();
					l.marshal(line);
					this.lines.add(l);
					if (l.getFunctions() != null) {
						for (FunctionXml function : l.getFunctions()) {
							List<LineXml> summary = tempSummaries.get(function);
							if (summary == null) {
								summary = new ArrayList<LineXml>();
								tempSummaries.put(function, summary);
							}
							summary.add(l);
						}
					}
				}
			}
			// Generating summaries by function
			this.summaries = new ArrayList<LineXml>();
			List<LineXml> mergedLines = new ArrayList<LineXml>();
			List<FunctionXml> listF = new ArrayList<FunctionXml>(tempSummaries.keySet());
			Collections.sort(listF);
			for (FunctionXml f : listF) {
				if (tempSummaries.get(f) != null) {
					LineXml finalLine = null;
					for (LineXml line : tempSummaries.get(f)) {
						if (finalLine == null)
							finalLine = new LineXml(line);
						else
							finalLine.merge(line);
					}
					finalLine.removeOtherFunction(f);
					mergedLines.add(finalLine);
				}
			}
			this.summaries.addAll(mergedLines);
			Collections.sort(this.summaries);

			if (obj.getCurrency() != null) {
				this.currency = new ItemXml();
				this.currency.marshal(obj.getCurrency());
			}
			this.totalAmount = obj.getTotalNetPrice();
			if (obj.getDiscountRate() != null) {
				this.discount = new RatedAmountXml();
				this.discount.marshal(obj.getDiscountRate(), obj.getDiscount());
			}
			this.fees = new ArrayList<FeeXml>();
			for (RecordFee fee : obj.getFees()) {
				if (fee.getRate() != null) {
					FeeXml f = new FeeXml();
					f.marshal(fee.getType(), fee.getRate(), fee.getAmount());
					this.fees.add(f);
				}
			}
			this.vat = new ArrayList<FeeXml>();
			for (Vat vat : obj.getVatRates()) {
				if (vat.getRate() != null) {
					FeeXml v = new FeeXml();
					v.marshal(vat, vat.getRate(), obj.getVatAmount(vat));
					this.vat.add(v);
				}
			}
			this.amountIVAT = obj.getTotalPriceIVAT();
			this.version = obj.getInformation().getVersion();
			this.length = obj.getInformation().getLength();
			if (obj.getSource() != null) {
				this.source = obj.getSource().getId().toString();
				try {
					this.sourceTitle = ((Project) obj.getSource()).getTitle();
				} catch (Exception e) {
				}
				;
			}

			if (obj instanceof Quotation) {
				this.briefs = new ArrayList<BriefXml>();
				for (Brief b : obj.getInformation().getBriefs()) {
					BriefXml xml = new BriefXml();
					xml.marshal(b);
					briefs.add(xml);
				}
			}

			signs = new ArrayList<>();
			SignXml signXml = null;
			if (obj instanceof BallPark) {
				signXml = new SignXml();
				if (obj.getSignPersonHR() != null) {
					signXml.marshallar("humanResource", " ", obj.getSignPersonHR().getFirstName(),  obj.getSignPersonHR().getName());
					signs.add(signXml);
				} 
//				else {
//					signXml.marshallar("humanResource", " ", " ");
//				}
//				signs.add(signXml);
				
				signXml = new SignXml();
				if (obj.getSignPersonFreelancer() != null) {
					signXml.marshallar("parttime", " ", obj.getSignPersonFreelancer().getFirstName(), obj.getSignPersonFreelancer().getName());
					signs.add(signXml);
				}
//				else {
//					signXml.marshallar("humanResource", " ", " ");
//				}
//				signs.add(signXml);
			}

		}
	}
}
