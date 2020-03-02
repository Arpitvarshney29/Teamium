package com.teamium.domain.prod;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.teamium.domain.ChannelFormat;
import com.teamium.domain.Company;
import com.teamium.domain.ContactRecord;
import com.teamium.domain.MilestoneType;
import com.teamium.domain.Person;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.Vat;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.output.XmlOutputEntity;
import com.teamium.domain.prod.projects.Program;
import com.teamium.domain.prod.projects.Quotation;
import com.teamium.domain.prod.projects.order.Order;
import com.teamium.domain.prod.resources.SaleEntity;
import com.teamium.domain.prod.resources.contacts.Contact;
import com.teamium.domain.prod.resources.contacts.Customer;
import com.teamium.domain.prod.resources.equipments.Attachment;
import com.teamium.domain.prod.resources.functions.Function;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.enums.ProjectStatus.ProjectStatusName;

/**
 * Describe the abstract layer of a production project
 *
 * @author Teamium
 * 
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_record")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "c_discriminator", discriminatorType = DiscriminatorType.STRING)
@NamedQueries({
		@NamedQuery(name = Record.QUERY_findAllByType, query = "SELECT r FROM Record r WHERE TYPE(r) = ?1 ORDER BY r.date DESC, r.id DESC"),
		@NamedQuery(name = Record.QUERY_countAllByType, query = "SELECT COUNT(r) FROM Record r WHERE TYPE(r) = ?1"),
		@NamedQuery(name = Record.QUERY_findByIds, query = "SELECT r FROM Record r WHERE r.id IN (?1) ORDER BY r.date DESC, r.id DESC"),
		@NamedQuery(name = Record.QUERY_findBySource, query = "SELECT r FROM Record r WHERE r.source = ?1 AND TYPE(r) = ?2"),
		@NamedQuery(name = Record.QUERY_findByCompany, query = "SELECT r FROM Record r WHERE r.company = ?1 AND TYPE(r) = ?2"),
		@NamedQuery(name = Record.QUERY_findSource, query = "SELECT s FROM Record s WHERE EXISTS(SELECT r FROM Record r WHERE r = ?1 AND r.source = s)"),
		@NamedQuery(name = Record.QUERY_findByStatus, query = "SELECT r FROM Record r WHERE r.status IN(?1) AND TYPE(r) = ?2"),
		@NamedQuery(name = Record.QUERY_completeCityWhitCountry, query = "SELECT DISTINCT r.city FROM Record r WHERE r.country = ?1 AND LOWER(r.city) LIKE ?2"), })
public abstract class Record implements Cloneable, XmlOutputEntity {

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 7146308281386143748L;

	/**
	 * Entity alias used in queries
	 */
	public static final String QUERY_ENTITY_ALIAS = "r";

	/**
	 * Name of the query counting all records in persistence unit matching the given
	 * type
	 * 
	 * @param type the type of record to count
	 * @return count of projects
	 */
	public static final String QUERY_countAllByType = "countAllRecordByType";

	/**
	 * Name of the query retrieving all records matching the given type from
	 * persistence unit
	 * 
	 * @param type the type of records to retrieve
	 * @return the list of all projects
	 */
	public static final String QUERY_findAllByType = "findAllRecordByType";

	/**
	 * Query string retrieving all entities found in the persistence unit, that may
	 * be completed by an OrderByClause.getClause() to sort entities
	 * 
	 * @param 1 the list of ids to match
	 * @param 2 the type of record to find
	 * @return the list of matching entities
	 */
	public static final String QUERY_findOrderedAllByType = "SELECT r FROM Record r WHERE TYPE(r) = ?1";

	/**
	 * Query string changing type of the given record
	 * 
	 * @param 1 the discriminator value to set, that must be EXCLUSIVELY the simple
	 *        class name of the entity, transformed to lower case
	 * @param 2 id id of the targeted record
	 * @return the result of the update
	 */
	public static final String QUERY_nativeUpdateType = "UPDATE t_record SET c_version = c_version+1, c_discriminator = ?1 WHERE c_idrecord = ?2";

	/**
	 * Name of the query retrieving entities matching the given list of ids
	 * 
	 * @param 1 the ids to match
	 * @return the matching entities
	 */
	public static final String QUERY_findByIds = "findRecordByIds";

	/**
	 * String of the query retrieving the ids of the given type of record matching
	 * one of the given ids,that may be completed by OrderByClause.getClause()
	 * 
	 * @param 1 the ids the match
	 * @param 2 the type of persons to retrieve
	 * @return the list of matching ids
	 */
	public static final String QUERY_findByOrderedIds = "SELECT r.id FROM Record r WHERE r.id IN (?1) AND TYPE(r) = ?2";

	/**
	 * Name of the query retrieving records transformed from the given source
	 * 
	 * @param 1 the source of the project
	 * @param 2 the type of the records to retrieve
	 * @return the generated project
	 */
	public static final String QUERY_findBySource = "findRecordBySource";

	/**
	 * Name of the query retrieving records matching the given type, targeting the
	 * given
	 * 
	 * @param 1 the tageted company
	 * @param 2 the type of the records to retrieve
	 * @return the generated project
	 */
	public static final String QUERY_findByCompany = "findRecordByCompany";

	/**
	 * Name of the query retrieving the source record of the given record
	 * 
	 * @param 1 the given record
	 * @return the source record
	 */
	public static final String QUERY_findSource = "findSourceRecord";

	/**
	 * Name of the query retrieving the source record for the given status
	 * 
	 * @param 1 the given status
	 * @return the source record
	 */
	public static final String QUERY_findByStatus = "findByStatus";

	/**
	 * Name of the query retrieving the city for the given keyword and the given
	 * country
	 * 
	 * @param 1 the given keyword
	 * @return the list of city
	 */
	public static final String QUERY_completeCityWhitCountry = "record_completeCityWhitCountry";

	/**
	 * DateFormat for toString
	 */
	protected static final DateFormat DF = DateFormat.getDateInstance(DateFormat.SHORT);

	/**
	 * Header ID
	 */
	@Id
	@Column(name = "c_idrecord", insertable = false, updatable = false)
	@TableGenerator(name = "idRecord_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "record_idrecord_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idRecord_seq")
	private Long id;

	/**
	 * The targeted company
	 */
	@ManyToOne
	@JoinColumn(name = "c_idcompany")
	private Company company;

	/**
	 * Date of record
	 */
	@Column(name = "c_recorddate")
	@Temporal(TemporalType.DATE)
	private Calendar date;

	@Column(name = "c_pdf")
	private String pdf;

	/**
	 * Num Objet (Assedic)
	 */
	@Column(name = "c_numobjet")
	private String numObjet;

	/**
	 * The project manager
	 */
	@ManyToOne
	@JoinColumn(name = "c_idfollower")
	private StaffMember follower;

	/**
	 * Project Status
	 * 
	 * @see com.teamium.domain.prod.projects.ProjectStatus
	 */
	@Embedded
	@AttributeOverride(name = "key", column = @Column(name = "c_status"))
	private XmlEntity status;

	/**
	 * Due dates
	 */
	@OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
	private Set<DueDate> dueDates;

	/**
	 * The sale entity
	 */
	@ManyToOne
	@JoinColumn(name = "c_identity")
	private Company entity;

	/**
	 * The currency
	 */
	@Column(name = "c_currency")
	private String currency;

	/**
	 * UI language
	 * 
	 * @see com.teamium.domain.prod.LanguageGeneral
	 */
	@Embedded
	@AttributeOverride(name = "key", column = @Column(name = "c_language"))
	private XmlEntity language;
	@Transient
	private Locale localeLanguage;

	/**
	 * Lines of the case
	 */
	@OneToMany(mappedBy = "record", fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = Line.class)
	protected List<Line> lines;

	/**
	 * Currency rates
	 */
	@ElementCollection
	@MapKeyColumn(name = "c_currency")
	@Column(name = "c_rate")
	@CollectionTable(name = "t_record_currency", joinColumns = @JoinColumn(name = "c_idrecord"))
	private Map<String, Float> persistentExchangeRates;
	@Transient
	private Map<Currency, Float> exchangeRates;

	/**
	 * Applied fees
	 */
	@OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
	private Set<RecordFee> fees;

	/**
	 * The total price
	 */
	@Column(name = "c_totalprice")
	private Float totalPrice;

	/**
	 * The discount rate
	 */
	@XmlTransient
	@Column(name = "c_discount_amount")
	private Float discount;

	/**
	 * The discount rate
	 */
	@Column(name = "c_discount_rate")
	private Float discountRate;

	/**
	 * The net price
	 */
	@Column(name = "c_totalnetprice")
	private Float totalNetPrice;

	/**
	 * Applied VAT Rates
	 */
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "t_record_vat", joinColumns = @JoinColumn(name = "c_idrecord"))
	@AttributeOverrides({ @AttributeOverride(name = "key", column = @Column(name = "c_code")),
			@AttributeOverride(name = "rate", column = @Column(name = "c_rate")) })
	private List<Vat> vatRates;

	/**
	 * The total price, VAT included
	 */
	@Column(name = "c_totalpriceivat")
	private Float totalPriceIVAT;

	/**
	 * The total cost of the project
	 */
	@Column(name = "c_totalcost")
	private Float totalCost;

	/**
	 * The margin of the project
	 */
	@Column(name = "c_margin")
	private Float margin;

	/**
	 * Information about the project
	 */
	@Embedded
	private RecordInformation information;

	/**
	 * Payment terms
	 */
	@Column(name = "c_payment_terms")
	private String paymentTerms;

	/**
	 * The source case permits to keep full steps of transformation of cases
	 * (ballpark to quotation, quotation to project, project to invoice, project to
	 * order, or any to layout etc.)
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "c_idsource")
	private Record source;

	/**
	 * Reference internal
	 */
	@Column(name = "c_reference_internal")
	private String referenceInternal;

	/**
	 * Reference external
	 */
	@Column(name = "c_reference_external")
	private String referenceExternal;

	/**
	 * Country
	 * 
	 * @see com.teamium.domain.CountryISO
	 */
	@Embedded
	@AttributeOverride(name = "key", column = @Column(name = "c_country"))
	private XmlEntity country;

	/**
	 * City
	 */
	@Column(name = "c_city")
	private String city;

	/**
	 * The list of contact
	 */
	@OneToMany
	@JoinColumn(name = "c_idrecord")
	private List<ContactRecord> contacts;

	@OneToOne
	@JoinColumn(name = "c_sign_person_hr")
	private StaffMember signPersonHR;

	@OneToOne
	@JoinColumn(name = "c_sign_person_parttime")
	private StaffMember signPersonFreelancer;

	/**
	 * Purchase order date
	 */
	@Column(name = "c_purchase_order_date")
	@Temporal(TemporalType.DATE)
	private Calendar purchaseOrderDate;

	/**
	 * record-contacts
	 */
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "t_record_contact", joinColumns = @JoinColumn(name = "c_idrecord"), inverseJoinColumns = @JoinColumn(name = "c_idperson"))
	private Set<Person> recordContacts = new HashSet<Person>();

	/**
	 * Financial status
	 */
	@Column(name = "c_financial_status")
	private String financialStatus;

	/**
	 * Financial status
	 */
	@Column(name = "c_origin")
	private String origin;

	/**
	 * Attachments
	 */
	@OneToMany(cascade = { CascadeType.ALL })
	@JoinColumn(name = "c_idrecord")
	private Set<Attachment> attachments = new HashSet<Attachment>();

	/**
	 * Channel-Formats
	 */
	@OneToMany(cascade = { CascadeType.ALL })
	@JoinColumn(name = "c_idrecord")
	private Set<ChannelFormat> channelFormat = new HashSet<ChannelFormat>();

	@Column(name = "c_procurement_tax")
	private Float procurementTax;

	@Transient
	private Long newlySavedLine;

	@Column(name = "c_program_id")
	private Long programId;
	
	@Column(name = "c_updated_on")
	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar modifiedDate;
	
	@Column(name = "c_modified_by")
	@LastModifiedBy
	private Long modifiedBy;

	/**
	 * @return the signPersonHR
	 */
	public StaffMember getSignPersonHR() {
		return signPersonHR;
	}

	/**
	 * @param signPersonHR the signPersonHR to set
	 */
	public void setSignPersonHR(StaffMember signPersonHR) {
		this.signPersonHR = signPersonHR;
	}

	/**
	 * @return the signPersonFreelancer
	 */
	public StaffMember getSignPersonFreelancer() {
		return signPersonFreelancer;
	}

	/**
	 * @param signPersonFreelancer the signPersonFreelancer to set
	 */
	public void setSignPersonFreelancer(StaffMember signPersonFreelancer) {
		this.signPersonFreelancer = signPersonFreelancer;
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
	 * @return the targeted company
	 */
	public Company getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(Company company) {
		this.company = company;
		if (this.company != null) {
			if (this.currency == null)
				this.setCurrency(this.company.getCurrency());
			if (this.language == null)
				this.setLocaleLanguage(this.company.getLocaleLanguage());
			if ((this.vatRates == null || this.vatRates.isEmpty()) && company.getClass().equals(Customer.class))
				vatRates = new ArrayList<Vat>(((Customer) company).getVatRates());

		}
	}

	/**
	 * @return the date
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * @param quoteDate the quoteDate to set
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 * @return the follower
	 */
	public StaffMember getFollower() {
		return follower;
	}

	/**
	 * @param follower the follower to set
	 */
	public void setFollower(StaffMember follower) {
		this.follower = follower;
	}

	/**
	 * @return the dueDates
	 */
	public Set<DueDate> getDueDates() {
		if (this.dueDates == null)
			this.dueDates = new HashSet<DueDate>();
		return dueDates;
	}

	/**
	 * @param dueDates the dueDates to set
	 */
	public void setDueDates(Set<DueDate> dueDates) {
		this.dueDates = dueDates;
	}

	/**
	 * Clear the duedate by setting null ( can avoid lazy )
	 */
	public void clearDueDate() {
		this.dueDates = null;
	}

	/**
	 * Add the given type of due date to the project
	 * 
	 * @return true if success, else returns false
	 */
	public boolean addDueDate(MilestoneType type) {
		if (type != null) {
			DueDate date = new DueDate();
			date.setType(type);
			date.setRecord(this);
			return this.getDueDates().add(date);
		} else {
			return false;
		}
	}

	/**
	 * @return the lines
	 */
	public List<Line> getLines() {
		if (this.lines == null)
			this.lines = new ArrayList<Line>();
		return lines;
	}

	/**
	 * Update the lines
	 */
	public void setLines(List<Line> lines) {
		this.lines = lines;
	}

	/**
	 * Add the given line to the record
	 * 
	 * @param line the line to add
	 * @return true if success, else returns false
	 */
	public boolean addLine(Line line) {
		return this.addLineTemp(line);
	}

	/**
	 * TODO Review Contrer le add line de l'abstract project
	 */
	private boolean addLineTemp(Line line) {
		if (line == null)
			return false;
		Record former = line.getRecord();
		line.setRecord(this);
		boolean success = this.getLines().add(line);
		if (!success)
			line.setRecord(former);
		else {
			Collections.sort(this.lines);
			this.updateFees();
		}
		return success;
	}

	/**
	 * TODO Review Ajouter pour contrer le abstract project n'ajoutant qu'une ligne
	 * par fonction
	 */
	public boolean addBooking(Line line) {
		return this.addLineTemp(line);
	}

	/**
	 * Remove the given line from the project
	 * 
	 * @param line the line to remove
	 * @return true if success, else returns false
	 */
	public boolean removeLine(Line line) {
		return this.getLines().remove(line);
	}

	/**
	 * @return the currency
	 */
	public Currency getCurrency() {
		if (currency == null)
			return null;
		else
			return Currency.getInstance(currency);
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(Currency currency) {
		if (currency == null)
			this.currency = null;
		else
			this.currency = currency.getCurrencyCode();
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
	 * @return the entity
	 */
	public Company getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(Company entity) {
		this.entity = entity;
	}

	/**
	 * @return the language
	 */
	public XmlEntity getLanguage() {
		return this.language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(XmlEntity language) {
		this.language = language;
	}

	/**
	 * @return the language
	 */
	public Locale getLocaleLanguage() {
		if (this.localeLanguage == null && this.language != null)
			this.localeLanguage = new Locale(this.language.getKey());
		return this.localeLanguage;
	}

	/**
	 * @param language the language to set
	 */
	public void setLocaleLanguage(Locale language) {
		this.localeLanguage = language;
		if (this.localeLanguage == null)
			this.language = null;
		else {
			XmlEntity xml = new XmlEntity();
			String languageString = StringUtils.capitalize(language.getLanguage());
			xml.setKey(languageString);
			if (!xml.equals(this.language))
				this.language = xml;

		}
	}

	/**
	 * @return the conversionRates
	 */
	public Map<Currency, Float> getExchangeRates() {
		if (this.persistentExchangeRates == null)
			this.persistentExchangeRates = new HashMap<String, Float>();
		if (this.exchangeRates == null) {
			this.exchangeRates = new HashMap<Currency, Float>();
			for (String key : this.persistentExchangeRates.keySet()) {
				Currency currency = Currency.getInstance(key);
				if (currency != null)
					this.exchangeRates.put(currency, this.persistentExchangeRates.get(key));
			}
		}
		return exchangeRates;
	}

	/**
	 * Clear the exchange rates by setting null ( can avoid lazy )
	 */
	public void clearExchangeRates() {
		this.persistentExchangeRates = null;
		this.exchangeRates = null;
	}

	/**
	 * Put the given currency exchange rate into the map
	 */
	public void setExchangeRates(Map<Currency, Float> exchangeRates) {
		this.exchangeRates = exchangeRates;
		if (exchangeRates == null || exchangeRates.isEmpty())
			this.persistentExchangeRates = null;
		else {
			this.persistentExchangeRates.clear();
			for (Currency c : this.exchangeRates.keySet()) {
				this.persistentExchangeRates.put(c.getCurrencyCode(), this.exchangeRates.get(c));
			}
		}

	}

	/**
	 * Put the given currency exchange rate into the map
	 * 
	 * @param currency the currency to exchange
	 * @param rate     the exchange rate
	 */
	public void addExchangeRate(Currency currency, Float rate) {
		if (currency != null) {
			// ADD DO : rate can't be null for saving persistent
			if (rate == null)
				rate = (float) 1.0;
			this.getExchangeRates().put(currency, rate);
			this.persistentExchangeRates.put(currency.getCurrencyCode(), rate);
		}
	}

	/**
	 * Remove the given currency rate conversion from the map
	 * 
	 * @param currency the currency to remove
	 */
	public void removeExchangeRate(Currency currency) {
		if (currency != null) {
			this.getExchangeRates().remove(currency);
			this.persistentExchangeRates.remove(currency.getCurrencyCode());
		}
	}

	/**
	 * @return the status
	 */
	public XmlEntity getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(XmlEntity status) {
		this.status = status;
	}

	/**
	 * @return the fees
	 */
	public Set<RecordFee> getFees() {
		if (this.fees == null)
			this.fees = new TreeSet<RecordFee>();
		return fees;
	}

	/**
	 * @param fees the fees to set
	 */
	public void setFees(Set<RecordFee> fees) {
		this.fees = fees;
	}

	/**
	 * Add the given type of fee to the project
	 * 
	 * @param type the type of fee to add
	 * @return true if success, else returns false
	 */
	public RecordFee addFee(XmlEntity type) {
		if (type == null)
			return null;
		else {
			RecordFee fee = new RecordFee();
			fee.setType(type);
			fee.setRecord(this);
			if (this.getFees().add(fee)) {
				return fee;
			} else {
				return null;
			}
		}
	}

	/**
	 * Return the fee matching the given type
	 * 
	 * @param type the type of fee to retrieve
	 * @return the matching fee
	 */
	public RecordFee getFee(XmlEntity type) {
		try {
			for (RecordFee fee : this.getFees()) {
				if (type.equals(fee.getType())) {
					return fee;
				}
			}
		} catch (NullPointerException e) {
		}
		return null;
	}

	/**
	 * Update fees amount
	 */
	protected void updateFees() {
		Map<XmlEntity, RecordFee> fees = new HashMap<XmlEntity, RecordFee>();
		for (RecordFee fee : this.getFees()) {
			if (fee.getType() != null)
				fees.put(fee.getType(), fee);
		}
		for (Line line : this.getLines()) {
			if (line.getFunction() != null) {
				for (XmlEntity appliedFee : line.getAppliedFees()) {
					if (fees.get(appliedFee) == null) {
						RecordFee fee = this.addFee(appliedFee);
						if (fee != null)
							fees.put(fee.getType(), fee);
					}
				}
			}
		}
		for (RecordFee fee : this.getFees()) {
			fee.updateAmount();
		}
	}

	/**
	 * Remove the given fee from the project
	 * 
	 * @param fee the fee to remove
	 * @return true if success, else returns false
	 */
	public boolean removeFee(RecordFee fee) {
		return this.getFees().remove(fee);
	}

	/**
	 * @return the discount
	 */
	public Float getDiscountRate() {
		return discountRate;
	}

	/**
	 * @param discount the discount to set
	 */
	public void setDiscountRate(Float discount) {
		this.discountRate = discount;
		if (this.discountRate != null && this.getTotalPrice() != null) {
			this.discount = Math.round(this.discountRate * this.getTotalPrice() * 100f) / 100f;
		} else {
			this.discount = null;
		}
	}

	/**
	 * Sums and returns the total price for the given type of function
	 * 
	 * @param type the type of functions to sum
	 * @return the subtotal for the given type of function
	 */
	public Float getSubPrice(Class<? extends Function> type) {
		Float total = 0f;
		if (type != null) {
			for (Line line : this.getLines()) {
				boolean include = false;
				include = line.getFunction() != null && type.equals(line.getFunction().getClass());
				if (include) {
					total += (line.getTotalPrice() != null ? line.getTotalPrice() : 0f);
				}
			}
		}
		return total;
	}

	/**
	 * Sums and returns the total cost for the given type of function
	 * 
	 * @param type the type of functions to sum
	 * @return the subtotal for the given type of function
	 */
	public Float getSubCost(Class<? extends Function> type) {
		Float total = 0f;
		if (type != null) {
			for (Line line : this.getLines()) {
				boolean include = false;
				include = line.getFunction() != null && type.equals(line.getFunction().getClass());
				if (include)
					total += (line.getTotalCost() != null ? line.getTotalCost() : 0f);
			}
		}
		return total;
	}

	/**
	 * @return the totalPrice
	 */
	public Float getTotalPrice() {
		if (totalPrice == null)
			return 0f;
		return this.totalPrice;
	}

	public void setTotalPrice(Float totalPrice) {
		this.totalPrice = totalPrice;
	}

	/**
	 * For all lines totalPriceWithOccurenceCount
	 * 
	 * @return the totalPrice
	 */
	public Float getCalcTotalPrice() {
		if (this.getLines().isEmpty())
			this.totalPrice = null;
		else {
			this.totalPrice = 0f;
			for (Line line : this.getLines()) {
				if (!line.getDisabled()) {
					Float price = line.getTotalPriceWithOccurenceCount();
					if (price != null)
						this.totalPrice += price;
				}
			}
		}
		if (totalPrice == null)
			return 0f;
		return this.totalPrice;
	}

	// /**
	// * @return the totalPrice
	// */
	// public Float getCalcTotalPrice() {
	// if (this.getLines().isEmpty())
	// this.totalPrice = null;
	// else {
	// this.totalPrice = 0f;
	// for (Line line : this.getLines()) {
	// if (!line.getDisabled()) {
	// Float price = line.getTotalPrice();
	// if (price != null)
	// this.totalPrice += price;
	// }
	// }
	// }
	// if (totalPrice == null)
	// return 0f;
	// return this.totalPrice;
	// }

	/**
	 * @return the discount applied on the total price
	 */
	public Float getDiscount() {
		return this.discount;
	}

	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(Float discount) {
		this.discount = discount;
		if (this.discount != null && this.getTotalPrice() != null) {
			this.discountRate = this.discount / this.getTotalPrice();
		} else {
			this.discountRate = null;
		}
	}

	/**
	 * Summarizes all obtained discounts, including discounts on specific lines
	 * 
	 * @return the global discount
	 */
	public Float getGlobalDiscount() {
		Float total = this.getDiscount() == null ? 0f : this.getDiscount();
		for (Line line : this.getLines()) {
			total += line.getDiscount();
		}
		return total;
	}

	/**
	 * @return the totalNetPrice
	 */
	public Float getTotalNetPrice() {
		Float totalPrice = this.getTotalPrice();
		if (totalPrice == null) {
			this.totalNetPrice = null;
		} else {
			Float discount = this.getDiscount();
			this.totalNetPrice = totalPrice - (discount != null ? discount : 0f);
		}
		return totalNetPrice;
	}

	/**
	 * @return the total price including fees
	 */
	public Float getTotalNetPriceIFees() {
		Float total = this.getTotalNetPrice();
		if (total == null)
			total = 0f;
		for (RecordFee fee : this.getFees()) {
			total += fee.getAmount() != null ? fee.getAmount() : 0f;
		}
		return total;
	}

	/**
	 * Returns the VAT rates to apply
	 * 
	 * @return the vat list
	 */
	public List<Vat> getVatRates() {
		if (this.vatRates == null)
			this.vatRates = new ArrayList<Vat>();
		return this.vatRates;
	}

	/**
	 * Add the given VAT rate to the record
	 * 
	 * @param rate the vat rate to add
	 * @return true if success, else returns false
	 */
	public boolean addVatRate(Vat rate) {
		for (Vat vat : this.getVatRates()) {
			if (vat.equals(rate))
				return false;
		}
		return this.getVatRates().add(rate);
	}

	public void setVatRate(List<Vat> vatRates) {
		this.vatRates = vatRates;
	}

	/**
	 * Remove the given VAT rate from the record
	 * 
	 * @param rate the VAT rate to remove
	 * @return true if success, else returns false
	 */
	public boolean removeVatRate(Vat rate) {
		return this.getVatRates().remove(rate);
	}

	/**
	 * Returns the VAT amount for the given rate
	 * 
	 * @param rate the rate to sum
	 * @return the calculated amount
	 */
	public Float getVatAmount(Vat rate) {
		Float amount = 0f;
		int index = this.getVatRates().indexOf(rate);
		if (index >= 0) {
			rate = this.getVatRates().get(index);
			if (rate.getRate() != null) {
				Float basis = 0f;
				for (Line line : this.getLines()) {
					if (rate.equals(line.getVat())) {
						basis += (line.getTotalPrice() != null ? line.getTotalPrice() : 0f);
					}
					amount = basis * rate.getRate();
				}
				for (Fee fee : this.getFees()) {
					amount += fee.getVatAmount(rate);
				}
			}
		}
		return amount;
	}

	/**
	 * @return the totalPriceIVAT
	 */
	public Float getTotalPriceIVAT() {
		if (totalPriceIVAT == null) {
			return 0f;
		}
		return this.totalPriceIVAT;
	}

	/**
	 * @return calculate totalPriceIVAT
	 */
	public Float getCalcTotalPriceIVAT() {
		// Float price = this.getTotalNetPriceIFees();
		// this.totalPriceIVAT = price;
		// if (price != null) {
		// Float amountVat = 0f;
		// for (Vat rate : this.getVatRates()) {
		// Float vat = this.getVatAmount(rate);
		// if (vat != null)
		// amountVat += vat;
		// }
		// amountVat = amountVat
		// * (this.getDiscount() != null ? (1 - (this.getDiscount() / (price +
		// this.getDiscount()))) : 1);
		// price = price + amountVat;
		// price = Math.round(price * 100f) / 100f;
		// }
		// this.totalPriceIVAT = price;

		return this.totalPriceIVAT;
	}

	public void setCalcTotalPriceIVAT(Float totalPriceIVAT) {
		this.totalPriceIVAT = totalPriceIVAT;
	}

	public void setTotalNetPrice(Float totalNetPrice) {
		this.totalNetPrice = totalNetPrice;
	}

	/**
	 * @return the total cost
	 */
	public Float getTotalCost() {
		if (totalCost == null)
			return 0f;
		return this.totalCost;
	}

	public void setTotalCost() {
	}

	/**
	 * @return calculate total cost
	 */
	public Float getCalcTotalCost() {
		this.totalCost = 0f;
		for (Line line : this.getLines()) {
			if (!line.getDisabled()) {
				this.totalCost += (line.getTotalCost() != null ? line.getTotalCost() : 0f);
			}
		}
		return this.totalCost;
	}

	/**
	 * @return the margin
	 */
	public Float getMargin() {
		Float cost = this.getTotalCost();
		Float price = this.getTotalNetPrice();
		if (cost != null && price != null) {
			this.margin = price - cost;
		} else {
			this.margin = null;
		}
		return this.margin;
	}

	/**
	 * @return calculate margin
	 */
	public Float getMarginRate() {
		Float margin = this.getMargin();
		Float price = this.getTotalNetPrice();
		if (margin != null && price != null) {
			Integer rate = Math.round(margin / price * 10000);
			return rate.floatValue() / 10000f;
		}
		return null;
	}

	/**
	 * @return the information
	 */
	public RecordInformation getInformation() {
		if (information == null)
			information = new RecordInformation();
		return information;
	}

	/**
	 * @param information the information to set
	 */
	public void setInformation(RecordInformation information) {
		this.information = information;
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
	 * @return the source
	 */
	public Record getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(Record source) {
		this.source = source;
	}

	/**
	 * Before update operations
	 */
	@PreUpdate
	@PrePersist
	protected void beforeUpdate() {
		// Securing the foreign key for project lines
		for (Line line : this.getLines()) {
			line.setRecord(this);
		}

		// Calculate all prices
		// this.updateAmounts();
	}

	/**
	 * Calculate in cascade all prices, costs, taxes and fees
	 */
	public void updateAmounts() {
		this.updateFees();
		this.getCalcTotalCost();
		this.getCalcTotalPrice();
		this.getCalcTotalPriceIVAT();
	}

	/**
	 * Clone the current object
	 * 
	 * @return the clone
	 * @throws CloneNotSupportedException
	 */
	@Override
	public Record clone() throws CloneNotSupportedException {
		Record clone = null;
		Company company = this.company;
		this.company = null;
		Set<DueDate> dueDates = this.getDueDates();
		this.dueDates = null;
		Map<Currency, Float> exchangeRates = this.getExchangeRates();
		this.exchangeRates = null;
		Set<RecordFee> fees = this.getFees();
		this.fees = null;
		StaffMember follower = this.follower;
		this.follower = null;
		Long id = this.id;
		this.id = null;
		List<Line> lines = this.getLines();
		Map<String, Float> persistentExchangeRates = this.persistentExchangeRates;
		this.persistentExchangeRates = null;
		XmlEntity status = this.status;
		this.status = null;
		Record source = this.source;
		this.source = null;
		List<Vat> vatRates = this.getVatRates();
		this.vatRates = null;
		RecordInformation recordInformation = this.information;
		this.information = null;
		List<ContactRecord> contacts = this.getContacts();

		StaffMember signPersonHR = this.signPersonHR;
		StaffMember signPersonFreelancer = this.signPersonFreelancer;

		Calendar poDate = this.getPurchaseOrderDate();
		this.purchaseOrderDate = null;

		String finStatus = this.financialStatus;
		this.financialStatus = null;

		String ori = this.origin;
		this.origin = null;
		// Set<Attachment> attachments = this.attachments;
		// this.attachments = null;

		try {
			clone = (Record) super.clone();
			for (DueDate date : dueDates) {
				DueDate clonedDate = date.clone();
				clonedDate.setRecord(clone);
				clone.getDueDates().add(clonedDate);
			}
			for (Currency key : exchangeRates.keySet()) {
				clone.addExchangeRate(key, exchangeRates.get(key));
			}
			for (RecordFee fee : fees) {
				RecordFee clonedFee = fee.clone();
				clonedFee.setRecord(clone);
				clone.getFees().add(clonedFee);
			}
			clone.follower = follower;
			clone.lines = new ArrayList<Line>(this.getLines().size());
			for (Line line : lines) {
				Line clonedLine = line.clone();
				clonedLine.setRecord(clone);
				clone.lines.add(clonedLine);
			}
			clone.company = company;
			clone.status = null;
			clone.vatRates = new ArrayList<Vat>(vatRates.size());
			for (Vat vat : vatRates) {
				Vat cloneVat = vat.clone();
				clone.vatRates.add(cloneVat);
			}
			clone.information = (RecordInformation) recordInformation.clone();
			// Special management for contacts
			clone.contacts = null;

			clone.signPersonHR = signPersonHR;
			clone.signPersonFreelancer = signPersonFreelancer;
			clone.purchaseOrderDate = purchaseOrderDate;
			clone.financialStatus = finStatus;
			clone.origin = ori;
			// clone.attachments = attachments;
		} catch (CloneNotSupportedException e) {
			throw e;
		} finally {
			this.company = company;
			this.dueDates = dueDates;
			this.exchangeRates = exchangeRates;
			this.fees = fees;
			this.follower = follower;
			this.id = id;
			this.lines = lines;
			this.persistentExchangeRates = persistentExchangeRates;
			this.status = status;
			this.source = source;
			this.vatRates = vatRates;
			this.information = recordInformation;
			this.contacts = contacts;
			this.signPersonHR = signPersonHR;
			this.signPersonFreelancer = signPersonFreelancer;
			this.purchaseOrderDate = poDate;
			this.financialStatus = finStatus;
			this.origin = ori;
			// this.attachments = attachments;
		}
		return clone;
	}

	/**
	 * Customized clone method
	 * 
	 * @return Record
	 * 
	 * @throws CloneNotSupportedException
	 */
	public Quotation getCloneOfRecord(Program record) throws CloneNotSupportedException {
		Quotation clone = new Quotation();

		clone.setId(null);
		clone.setDate(record.getStart());
		clone.setChannel(record.getChannel());
		record.setChannel(null);
		clone.setProductionUnit(record.getProdUnit());
		record.setProdUnit(null);
		clone.setAgency(record.getAgency());
		record.setAgency(null);
		clone.setCategory(record.getCategory());
		clone.setDirector(record.getDirector());
		record.setDirector(null);
		clone.setInputs(record.getInputs());
		record.setInputs(null);
		clone.setOutputs(record.getOutputs());
		record.setOutputs(null);
		clone.setCompany(record.getCompany());
		clone.setExchangeRates(record.getExchangeRates());
		record.setExchangeRates(null);
		clone.setFees(record.getFees());
		record.setFees(null);
		clone.setFollower(record.getFollower());
//		record.setFollower(null);
		clone.setEntity(record.getEntity());

		XmlEntity status = new XmlEntity();
		status.setKey(ProjectStatusName.TO_DO.getProjectStatusNameString());
		clone.setStatus(status);

		clone.setVatRate(record.getVatRates());
		record.setVatRate(null);
		clone.setContacts(record.getContacts());
		record.setContacts(null);
		clone.setSignPersonHR(record.getSignPersonHR());
		record.setSignPersonHR(null);
		clone.setSignPersonFreelancer(record.getSignPersonFreelancer());
		record.setSignPersonFreelancer(null);
		clone.setPurchaseOrderDate(record.getPurchaseOrderDate());
		record.setPurchaseOrderDate(null);
		clone.setFinancialStatus(null);
		clone.setCurrency(record.getCurrency());
		clone.setLanguage(record.getLanguage());
		clone.setProgram(record);
		clone.setProgramId(record.getId());
		clone.setCity(record.getCity());
		clone.setCountry(record.getCountry());

		Set<Person> recordContacts = record.getRecordContacts();
		Set<Person> contacts = null;
		if (recordContacts != null && !recordContacts.isEmpty()) {
			contacts = new HashSet<Person>();
			List<Person> list = recordContacts.stream().collect(Collectors.toList());
			for (int k = 0; k < list.size(); k++) {
				Person extra = list.get(k);
				contacts.add(extra);
			}
			clone.setRecordContacts(contacts);
		}

		clone.setReferenceInternal(record.getReferenceInternal());
		clone.setReferenceExternal(record.getReferenceExternal());

		if (record.getInformation() != null) {
			clone.setInformation(copyRecordInformation(record.getInformation()));
		}
		if (record.getDueDates() != null) {
			clone.setDueDates(copyDueDates(record.getDueDates()));
			clone.getDueDates().stream().forEach(dueDate -> dueDate.setRecord(clone));
		}
		clone.setChannelFormat(copyChannelDetails(record.getChannelFormat()));
		clone.setAttachments(copyAttachments(record.getAttachments()));

		clone.setLines(null);
		clone.setTheme(record.getTheme());

		return clone;
	}

	/**
	 * To copy copy channel formats.
	 * 
	 * @param channelFormats the channelFormats
	 * 
	 * @return
	 */
	protected Set<ChannelFormat> copyChannelDetails(Set<ChannelFormat> channelFormats) {
		Set<ChannelFormat> channelFormatsForProject = channelFormats.stream()
				.map(cf -> new ChannelFormat(cf.getChannel(), cf.getFormat())).collect(Collectors.toSet());
		return channelFormatsForProject;
	}

	/**
	 * To copy copy Attachment.
	 * 
	 * @param channelFormats the channelFormats
	 * 
	 * @return set of Attachment
	 */
	protected Set<Attachment> copyAttachments(Set<Attachment> attachments) {
		Set<Attachment> attachmentsForProject = attachments.stream().map(at -> new Attachment(at))
				.collect(Collectors.toSet());
		return attachmentsForProject;
	}

	/**
	 * To copy recordInformation
	 * 
	 * @param budgetRecordInformation the budgetRecordInformation
	 * 
	 * @return RecordInformation instance
	 */
	protected RecordInformation copyRecordInformation(RecordInformation budgetRecordInformation) {
		RecordInformation recordInformation = new RecordInformation();
		recordInformation.setComment(budgetRecordInformation.getComment());
		recordInformation.setLength(budgetRecordInformation.getLength());
		recordInformation.setVersion(budgetRecordInformation.getVersion());
		return recordInformation;
	}

	/**
	 * To copy due-dates
	 * 
	 * @param duedates the duedates
	 * 
	 * @return
	 */
	protected Set<DueDate> copyDueDates(Set<DueDate> dueDates) {
		Set<DueDate> dueDatesForProject = dueDates.stream().map(at -> new DueDate(at)).collect(Collectors.toSet());
		return dueDatesForProject;
	}

	/**
	 * Returns the edition root path for this kind of entitt from main edition root
	 * 
	 * @return the relative path from main edition root
	 */
	public String getOutputPath() {
		return this.getClass().getSimpleName();
	}

	/**
	 * Returns the output locale
	 * 
	 * @return the locale
	 */
	public Locale getOutputLocale() {
		return this.getLocaleLanguage();
	}

	/**
	 * Returns the output header entity
	 * 
	 * @returns the sale entity
	 */

	public Company getOutputHeaderEntity() {
		return this.entity;
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
	 * @return the country
	 */
	public XmlEntity getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(XmlEntity country) {
		this.country = country;
	}

	/**
	 * @return the contacts
	 */
	public List<ContactRecord> getContacts() {
		if (this.contacts == null)
			this.contacts = new ArrayList<ContactRecord>();
		return contacts;
	}

	/**
	 * Clear contact by setting null ( can avoid lazy )
	 */
	public void clearContacts() {
		this.contacts = null;
	}

	/**
	 * @param contacts the contacts to set
	 */
	public void setContacts(List<ContactRecord> contacts) {
		this.contacts = contacts;
	}

	/**
	 * @return the purchaseOrderDate
	 */
	public Calendar getPurchaseOrderDate() {
		return purchaseOrderDate;
	}

	/**
	 * @param purchaseOrderDate the purchaseOrderDate
	 */
	public void setPurchaseOrderDate(Calendar purchaseOrderDate) {
		this.purchaseOrderDate = purchaseOrderDate;
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
	 * @return the recordContacts
	 */
	public Set<Person> getRecordContacts() {
		return recordContacts;
	}

	/**
	 * @param recordContacts the recordContacts to set
	 */
	public void setRecordContacts(Set<Person> recordContacts) {
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
	public Set<ChannelFormat> getChannelFormat() {
		return channelFormat;
	}

	/**
	 * @param channelFormat the channelFormat to set
	 */
	public void setChannelFormat(Set<ChannelFormat> channelFormat) {
		this.channelFormat = channelFormat;
	}

	/**
	 * @return the pdf
	 */
	public String getPdf() {
		return pdf;
	}

	/**
	 * @param pdf the pdf to set
	 */
	public void setPdf(String pdf) {
		this.pdf = pdf;
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

	/**
	 * Alternate method for updating amounts
	 * 
	 */
	public void updateRecordAmounts() {
		this.updateFeesRecord();
		this.getCalcTotalCost();
		this.getCalcTotalPrice();
		this.getCalcTotalPriceIVATRecord();
	}

	protected void updateFeesRecord() {
		Map<XmlEntity, RecordFee> fees = new HashMap<XmlEntity, RecordFee>();
		for (RecordFee fee : this.getFees()) {
			if (fee.getType() != null)
				fees.put(fee.getType(), fee);
		}
		for (Line line : this.getLines()) {
			if (line.getFunction() != null) {
				for (XmlEntity appliedFee : line.getAppliedFees()) {
					if (fees.get(appliedFee) == null) {
						RecordFee fee = this.addFee(appliedFee);
						if (fee != null)
							fees.put(fee.getType(), fee);
					}
				}
			}
		}
		for (RecordFee fee : this.getFees()) {
			fee.updateAmount();
		}
	}

	public Float getCalcTotalPriceIVATRecord() {
		Float price = this.getTotalNetPriceIFees();
		this.totalPriceIVAT = price;
		if (price != null) {
			Float amountVat = 0f;
			for (Vat rate : this.getVatRates()) {
				Float vat = this.getVatAmount(rate);
				if (vat != null)
					amountVat += vat;
			}
			amountVat = amountVat
					* (this.getDiscount() != null ? (1 - (this.getDiscount() / (price + this.getDiscount()))) : 1);
			price = price + amountVat;
			price = Math.round(price * 100f) / 100f;
		}
		this.totalPriceIVAT = price;
		return this.totalPriceIVAT;
	}

	/**
	 * @return the newlySavedLine
	 */
	public Long getNewlySavedLine() {
		return newlySavedLine;
	}

	/**
	 * @param newlySavedLine the newlySavedLine to set
	 */
	public void setNewlySavedLine(Long newlySavedLine) {
		this.newlySavedLine = newlySavedLine;
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

	/**
	 * To update tax amounts
	 * 
	 * @param calcTotalPrice
	 */
	public void updateTaxes(Float calcTotalPrice) {
		this.setTotalPrice(calcTotalPrice);
		this.setTotalNetPrice(calcTotalPrice);
		this.setCalcTotalPriceIVAT(calcTotalPrice);
	}

	/**
	 * @return the programId
	 */
	public Long getProgramId() {
		return programId;
	}

	/**
	 * @param programId the programId to set
	 */
	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	/**
	 * @return the modifiedDate
	 */
	public Calendar getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(Calendar modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * @return the modifiedBy
	 */
	public Long getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

}
