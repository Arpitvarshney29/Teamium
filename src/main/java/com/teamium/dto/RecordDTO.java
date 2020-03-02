package com.teamium.dto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.Company;
import com.teamium.domain.Vat;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.DueDate;
import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.RecordFee;
import com.teamium.domain.prod.RecordInformation;
import com.teamium.domain.prod.resources.equipments.Attachment;
import com.teamium.domain.prod.resources.staff.StaffMember;

/**
 * Record Wrapper class
 * 
 * @author Teamium
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class RecordDTO {

	private Long id;
	private CompanyDTO company;
	private Calendar date;
	private String numObjet;
	private StaffMemberDTO follower;
	private String status;
	private Set<DueDateDTO> dueDates = new HashSet<DueDateDTO>();
	private CompanyDTO saleEntity;
	private String currency;
	private String language;
	private Locale localeLanguage;
	private List<BookingDTO> lines = new ArrayList<BookingDTO>();
	private Map<String, Float> persistentExchangeRates = new HashMap<String, Float>();
	private Set<FeeDTO> fees = new HashSet<FeeDTO>();
	private Float totalPrice;
	private Float discount;
	private Float discountRate;
	private Float totalNetPrice;
	private List<Vat> vatRates = new ArrayList<Vat>();
	private Float totalPriceIVAT;
	private Float totalCost;
	private Float margin;
	private RecordInformationDTO information;
	private String paymentTerms;
	private RecordDTO source;
	private String referenceInternal;
	private String referenceExternal;
	private String country;
	private String city;
	private List<ContactRecordDTO> contacts = new ArrayList<ContactRecordDTO>();
	private Calendar purchaseOrderDate;
	private String financialStatus;
	private Set<PersonDTO> recordContacts = new HashSet<PersonDTO>();
	private Set<Attachment> attachments = new HashSet<Attachment>();
	private Set<ChannelFormatDTO> channelFormat = new HashSet<ChannelFormatDTO>();
	private String recordDiscriminator;
	private String recordDate;
	private LayoutDTO templateLayout;
	private String templateTitle;
	private String dueDateForListView;
	private String pdfUrl;
	private Float procurementTax;
	private String origin;

	public RecordDTO() {
	}

	public RecordDTO(Record record) {
		if (record != null) {
			this.id = record.getId();
			Company company = record.getCompany();
			if (company != null) {
				this.company = new CompanyDTO(record.getCompany());
			}

			this.date = record.getDate();
			if (record.getDate() != null) {
				this.recordDate = new DateTime(record.getDate().getTime()).withZone(DateTimeZone.UTC).toString();
			}
			this.numObjet = record.getNumObjet();
			if (record.getCurrency() != null) {
				this.currency = record.getCurrency().getCurrencyCode();
			}

			StaffMember follower = record.getFollower();
			if (follower != null) {
				this.follower = new StaffMemberDTO(follower, null);
			}

			this.status = record.getStatus() == null ? null : record.getStatus().getKey();

			Company saleEntity = record.getEntity();
			if (saleEntity != null) {
				this.saleEntity = new CompanyDTO(saleEntity);
			}
			this.currency = record.getCurrency() == null ? null : record.getCurrency().getCurrencyCode();

			this.language = record.getLanguage() == null ? null : record.getLanguage().getKey();
			this.localeLanguage = record.getLocaleLanguage();

			List<Line> lines = record.getLines();
			if (lines != null && !lines.isEmpty()) {
				this.lines = lines.stream().map(entity -> new BookingDTO(entity)).collect(Collectors.toList());
			}

			Set<DueDate> dueDate = record.getDueDates();
			if (dueDate != null && !dueDate.isEmpty()) {
				this.dueDates = dueDate.stream().map(entity -> new DueDateDTO(entity)).collect(Collectors.toSet());
			}

			if (record.getExchangeRates() != null) {
				for (Currency c : record.getExchangeRates().keySet()) {
					this.persistentExchangeRates.put(c.getCurrencyCode(), record.getExchangeRates().get(c));
				}
			}

			Set<RecordFee> recordFees = record.getFees();
			if (recordFees != null && !recordFees.isEmpty()) {
				this.fees = recordFees.stream().map(recordFee -> new FeeDTO(recordFee)).collect(Collectors.toSet());
			}

			this.discount = record.getDiscount();
			this.discountRate = record.getDiscountRate();
			this.totalNetPrice = record.getTotalNetPrice();
			this.vatRates = record.getVatRates();

			this.margin = record.getMargin();

			if (record.getInformation() != null) {
				this.information = new RecordInformationDTO(record.getInformation());
			}

			this.paymentTerms = record.getPaymentTerms();

			Record source = record.getSource();
			if (source != null) {
				source.setSource(null);
				this.source = new RecordDTO(source);
			}

			this.referenceInternal = record.getReferenceInternal();
			this.referenceExternal = record.getReferenceExternal();
			this.country = record.getCountry() == null ? null : record.getCountry().getKey();
			this.city = record.getCity();

			this.purchaseOrderDate = record.getPurchaseOrderDate();
			this.financialStatus = record.getFinancialStatus();

			// updating each time the prices
			this.totalCost = record.getCalcTotalCost();
			this.totalPriceIVAT = record.getCalcTotalPrice();
			this.totalPrice = record.getCalcTotalPriceIVAT();

			if (record.getRecordContacts() != null && !record.getRecordContacts().isEmpty()) {
				this.recordContacts = record.getRecordContacts().stream().filter(dt -> dt != null)
						.map(contact -> new ContactDTO(contact)).collect(Collectors.toSet());
			}

			if (record.getAttachments() != null && !record.getAttachments().isEmpty()) {
				this.attachments = record.getAttachments();
			}

			if (record.getChannelFormat() != null && !record.getChannelFormat().isEmpty()) {
				this.channelFormat = record.getChannelFormat().stream().map(entity -> new ChannelFormatDTO(entity))
						.collect(Collectors.toSet());
			}

			this.paymentTerms = record.getPaymentTerms();
			this.procurementTax = record.getProcurementTax();
			this.origin = record.getOrigin();
		}
	}

	public RecordDTO(Record record, Boolean forSpreadsheet) {
		if (record != null) {
			this.setId(record.getId());
			this.setTotalPriceIVAT(record.getTotalPriceIVAT());
		}
	}

	public RecordDTO(Long id, Calendar date, String numObjet, String currency,
			Map<String, Float> persistentExchangeRates, Float totalPrice, Float discount, Float discountRate,
			Float totalNetPrice, Float totalPriceIVAT, Float totalCost, String paymentTerms, String referenceInternal,
			String referenceExternal, String city) {
		this.id = id;
		this.date = date;
		if (this.date != null) {
			this.recordDate = new DateTime(this.date.getTime()).withZone(DateTimeZone.UTC).toString();
		}
		this.numObjet = numObjet;
		this.currency = currency;
		this.persistentExchangeRates = persistentExchangeRates;
		this.totalPrice = totalPrice;
		this.discount = discount;
		this.discountRate = discountRate;
		this.totalNetPrice = totalNetPrice;
		this.totalPriceIVAT = totalPriceIVAT;
		this.totalCost = totalCost;
		this.paymentTerms = paymentTerms;
		this.referenceInternal = referenceInternal;
		this.referenceExternal = referenceExternal;
		this.city = city;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the date
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * @return the recordDate
	 */
	public String getRecordDate() {
		return recordDate;
	}

	/**
	 * @param recordDate the recordDate to set
	 */
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 * @return the numObjet
	 */
	public String getNumObjet() {
		return numObjet;
	}

	/**
	 * @param numObjet the numObjet to set
	 */
	public void setNumObjet(String numObjet) {
		this.numObjet = numObjet;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the persistentExchangeRates
	 */
	public Map<String, Float> getPersistentExchangeRates() {
		return persistentExchangeRates;
	}

	/**
	 * @param persistentExchangeRates the persistentExchangeRates to set
	 */
	public void setPersistentExchangeRates(Map<String, Float> persistentExchangeRates) {
		this.persistentExchangeRates = persistentExchangeRates;
	}

	/**
	 * @return the totalPrice
	 */
	public Float getTotalPrice() {
		return totalPrice;
	}

	/**
	 * @param totalPrice the totalPrice to set
	 */
	public void setTotalPrice(Float totalPrice) {
		this.totalPrice = totalPrice;
	}

	/**
	 * @return the discount
	 */
	public Float getDiscount() {
		return discount;
	}

	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	/**
	 * @return the discountRate
	 */
	public Float getDiscountRate() {
		return discountRate;
	}

	/**
	 * @param discountRate the discountRate to set
	 */
	public void setDiscountRate(Float discountRate) {
		this.discountRate = discountRate;
	}

	/**
	 * @return the totalNetPrice
	 */
	public Float getTotalNetPrice() {
		return totalNetPrice;
	}

	/**
	 * @param totalNetPrice the totalNetPrice to set
	 */
	public void setTotalNetPrice(Float totalNetPrice) {
		this.totalNetPrice = totalNetPrice;
	}

	/**
	 * @return the totalPriceIVAT
	 */
	public Float getTotalPriceIVAT() {
		if (totalPriceIVAT == null) {
			this.totalPriceIVAT = 0f;
		}
		return totalPriceIVAT;
	}

	/**
	 * @param totalPriceIVAT the totalPriceIVAT to set
	 */
	public void setTotalPriceIVAT(Float totalPriceIVAT) {
		this.totalPriceIVAT = totalPriceIVAT;
	}

	/**
	 * @return the totalCost
	 */
	public Float getTotalCost() {
		return totalCost;
	}

	/**
	 * @param totalCost the totalCost to set
	 */
	public void setTotalCost(Float totalCost) {
		this.totalCost = totalCost;
	}

	/**
	 * @return the paymentTerms
	 */
	public String getPaymentTerms() {
		return paymentTerms;
	}

	/**
	 * @param paymentTerms the paymentTerms to set
	 */
	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	/**
	 * @return the referenceInternal
	 */
	public String getReferenceInternal() {
		return referenceInternal;
	}

	/**
	 * @param referenceInternal the referenceInternal to set
	 */
	public void setReferenceInternal(String referenceInternal) {
		this.referenceInternal = referenceInternal;
	}

	/**
	 * @return the referenceExternal
	 */
	public String getReferenceExternal() {
		return referenceExternal;
	}

	/**
	 * @param referenceExternal the referenceExternal to set
	 */
	public void setReferenceExternal(String referenceExternal) {
		this.referenceExternal = referenceExternal;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the margin
	 */
	public Float getMargin() {
		return margin;
	}

	/**
	 * @param margin the margin to set
	 */
	public void setMargin(Float margin) {
		this.margin = margin;
	}

	/**
	 * @return the companyDTO
	 */
	public CompanyDTO getCompany() {
		return company;
	}

	/**
	 * @param companyDTO the companyDTO to set
	 */
	public void setCompany(CompanyDTO company) {
		this.company = company;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the saleEntity
	 */
	public CompanyDTO getSaleEntity() {
		return saleEntity;
	}

	/**
	 * @param saleEntity the saleEntity to set
	 */
	public void setSaleEntity(CompanyDTO saleEntity) {
		this.saleEntity = saleEntity;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the localeLanguage
	 */
	public Locale getLocaleLanguage() {
		return localeLanguage;
	}

	/**
	 * @param localeLanguage the localeLanguage to set
	 */
	public void setLocaleLanguage(Locale localeLanguage) {
		this.localeLanguage = localeLanguage;
	}

	// /**
	// * @return the fees
	// */
	// public Set<RecordFee> getFees() {
	// return fees;
	// }
	//
	// /**
	// * @param fees
	// * the fees to set
	// */
	// public void setFees(Set<RecordFee> fees) {
	// this.fees = fees;
	// }

	/**
	 * @return the vatRates
	 */
	public List<Vat> getVatRates() {
		return vatRates;
	}

	/**
	 * @param vatRates the vatRates to set
	 */
	public void setVatRates(List<Vat> vatRates) {
		this.vatRates = vatRates;
	}

	/**
	 * @return the source
	 */
	public RecordDTO getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(RecordDTO source) {
		this.source = source;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the contacts
	 */
	public List<ContactRecordDTO> getContacts() {
		return contacts;
	}

	/**
	 * @param contacts the contacts to set
	 */
	public void setContacts(List<ContactRecordDTO> contacts) {
		this.contacts = contacts;
	}

	/**
	 * @return the dueDates
	 */
	public Set<DueDateDTO> getDueDates() {
		return dueDates;
	}

	/**
	 * @param dueDates the dueDates to set
	 */
	public void setDueDates(Set<DueDateDTO> dueDates) {
		this.dueDates = dueDates;
	}

	/**
	 * @return the lines
	 */
	public List<BookingDTO> getLines() {
		return lines;
	}

	/**
	 * @param lines the lines to set
	 */
	public void setLines(List<BookingDTO> lines) {
		this.lines = lines;
	}

	/**
	 * @return the fees
	 */
	public Set<FeeDTO> getFees() {
		return fees;
	}

	/**
	 * @param fees the fees to set
	 */
	public void setFees(Set<FeeDTO> fees) {
		this.fees = fees;
	}

	/**
	 * @return the purchaseOrderDate
	 */
	public Calendar getPurchaseOrderDate() {
		return purchaseOrderDate;
	}

	/**
	 * @param purchaseOrderDate the purchaseOrderDate to set
	 */
	public void setPurchaseOrderDate(Calendar purchaseOrderDate) {
		this.purchaseOrderDate = purchaseOrderDate;
	}

	/**
	 * @return the information
	 */
	public RecordInformationDTO getInformation() {
		return information;
	}

	/**
	 * @param information the information to set
	 */
	public void setInformation(RecordInformationDTO information) {
		this.information = information;
	}

	/**
	 * @return the financialStatus
	 */
	public String getFinancialStatus() {
		return financialStatus;
	}

	/**
	 * @param financialStatus the financialStatus to set
	 */
	public void setFinancialStatus(String financialStatus) {
		this.financialStatus = financialStatus;
	}

	/**
	 * @return the follower
	 */
	public StaffMemberDTO getFollower() {
		return follower;
	}

	/**
	 * @param follower the follower to set
	 */
	public void setFollower(StaffMemberDTO follower) {
		this.follower = follower;
	}

	/**
	 * @return the recordContacts
	 */
	public Set<PersonDTO> getRecordContacts() {
		return recordContacts;
	}

	/**
	 * @param recordContacts the recordContacts to set
	 */
	public void setRecordContacts(HashSet<PersonDTO> recordContacts) {
		this.recordContacts = recordContacts;
	}

	/**
	 * @return the attachments
	 */
	public Set<Attachment> getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}

	/**
	 * @return the channelFormat
	 */
	public Set<ChannelFormatDTO> getChannelFormat() {
		return channelFormat;
	}

	/**
	 * @param channelFormat the channelFormat to set
	 */
	public void setChannelFormat(Set<ChannelFormatDTO> channelFormat) {
		this.channelFormat = channelFormat;
	}

	/**
	 * @return the recordDiscriminator
	 */
	public String getRecordDiscriminator() {
		return recordDiscriminator;
	}

	/**
	 * @param recordDiscriminator the recordDiscriminator to set
	 */
	public void setRecordDiscriminator(String recordDiscriminator) {
		this.recordDiscriminator = recordDiscriminator;
	}

	/**
	 * @return the templateLayout
	 */
	public LayoutDTO getTemplateLayout() {
		return templateLayout;
	}

	/**
	 * @param templateLayout the templateLayout to set
	 */
	public void setTemplateLayout(LayoutDTO templateLayout) {
		this.templateLayout = templateLayout;
	}

	/**
	 * @return the templateTitle
	 */
	public String getTemplateTitle() {
		return templateTitle;
	}

	/**
	 * @param templateTitle the templateTitle to set
	 */
	public void setTemplateTitle(String templateTitle) {
		this.templateTitle = templateTitle;
	}

	/**
	 * Method to get record
	 * 
	 * @param record    the record
	 * 
	 * @param recordDTO the recordDTO
	 * 
	 * @return record
	 */
	public Record getRecord(Record record, RecordDTO recordDTO) {
		setRecordDetail(record, recordDTO);
		return record;
	}

	/**
	 * To set record details
	 * 
	 * @param record    the record
	 * 
	 * @param recordDTO the recordDTO
	 */
	public void setRecordDetail(Record record, RecordDTO recordDTO) {
		record.setId(recordDTO.getId());
		record.setOrigin(recordDTO.getOrigin());

		if (recordDTO.getDate() != null) {
			Calendar dateInstance = recordDTO.getDate();
			dateInstance.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
			record.setDate(dateInstance);
		}

		if (recordDTO.getRecordDate() != null) {
			DateTime start = DateTime.parse(recordDTO.getRecordDate()).withZone(DateTimeZone.forID("Asia/Calcutta"));
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(start.getMillis());
			record.setDate(cal);
		}
		record.setNumObjet(recordDTO.getNumObjet());

		List<Vat> vat = recordDTO.getVatRates();
		if (vat != null && !vat.isEmpty()) {
			record.setVatRate(vat);
		}

		if (!StringUtils.isBlank(recordDTO.getCurrency())) {
			record.setCurrency(Currency.getInstance(recordDTO.getCurrency()));
		}

		if (!StringUtils.isBlank(recordDTO.getLanguage())) {
			XmlEntity languageEntity = new XmlEntity();
			languageEntity.setKey(recordDTO.getLanguage());
			record.setLanguage(languageEntity);
		}

		record.setLocaleLanguage(recordDTO.getLocaleLanguage() == null ? null : recordDTO.getLocaleLanguage());

		if (recordDTO.getPersistentExchangeRates() != null) {
			for (String c : this.getPersistentExchangeRates().keySet()) {
				recordDTO.persistentExchangeRates.put(c, recordDTO.getPersistentExchangeRates().get(c));
			}
		}

		record.setDiscount(recordDTO.getDiscount());
		record.setDiscountRate(recordDTO.getDiscountRate());
		record.setPaymentTerms(recordDTO.getPaymentTerms());
		record.setReferenceInternal(recordDTO.getReferenceInternal());
		record.setReferenceExternal(recordDTO.getReferenceExternal());
		record.setCity(recordDTO.getCity());

		if (!StringUtils.isBlank(recordDTO.getCountry())) {
			XmlEntity countryEntity = new XmlEntity();
			countryEntity.setKey(recordDTO.getCountry());
			record.setCountry(countryEntity);
		}
		
		if (recordDTO.getInformation() != null) {
			record.setInformation(recordDTO.getInformation().getRecordInformation(new RecordInformation(),
					recordDTO.getInformation()));
		}

		record.setPurchaseOrderDate(recordDTO.getPurchaseOrderDate());
//		record.setFinancialStatus(recordDTO.getFinancialStatus());

		if (recordDTO.getFees() != null && !recordDTO.getFees().isEmpty()) {

		}

		record.setPaymentTerms(recordDTO.getPaymentTerms());
		record.setProcurementTax(recordDTO.getProcurementTax());
	}

	/**
	 * @return the dueDateForListView
	 */
	public String getDueDateForListView() {

		return this.dueDateForListView;
	}

	/**
	 * @param dueDateForListView the dueDateForListView to set
	 */
	public void setDueDateForListView(String dueDateForListView) {
		this.dueDateForListView = dueDateForListView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RecordDTO [id=" + id + ", company=" + company + ", date=" + date + ", numObjet=" + numObjet
				+ ", follower=" + follower + ", status=" + status + ", dueDates=" + dueDates + ", saleEntity="
				+ saleEntity + ", currency=" + currency + ", language=" + language + ", localeLanguage="
				+ localeLanguage + ", lines=" + lines + ", persistentExchangeRates=" + persistentExchangeRates
				+ ", fees=" + fees + ", totalPrice=" + totalPrice + ", discount=" + discount + ", discountRate="
				+ discountRate + ", totalNetPrice=" + totalNetPrice + ", vatRates=" + vatRates + ", totalPriceIVAT="
				+ totalPriceIVAT + ", totalCost=" + totalCost + ", margin=" + margin + ", paymentTerms=" + paymentTerms
				+ ", source=" + source + ", referenceInternal=" + referenceInternal + ", referenceExternal="
				+ referenceExternal + ", country=" + country + ", city=" + city + ", contacts=" + contacts
				+ ", purchaseOrderDate=" + purchaseOrderDate + "]";
	}

	/**
	 * @return the pdfUrl
	 */
	public String getPdfUrl() {
		return pdfUrl;
	}

	/**
	 * @param pdfUrl the pdfUrl to set
	 */
	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
	}

	/**
	 * @return the procurementTax
	 */
	public Float getProcurementTax() {
		return procurementTax;
	}

	/**
	 * @param procurementTax the procurementTax to set
	 */
	public void setProcurementTax(Float procurementTax) {
		this.procurementTax = procurementTax;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecordDTO other = (RecordDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * @return the origin
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

}
