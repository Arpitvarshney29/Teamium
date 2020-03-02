package com.teamium.domain;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
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
import javax.persistence.MapKeyClass;
import javax.persistence.MapKeyColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.teamium.domain.prod.resources.accountancy.AccountancyNumber;
import com.teamium.domain.prod.resources.equipments.Attachment;
import com.teamium.domain.prod.resources.staff.StaffMember;

/**
 * Describe a company
 * 
 * @author sraybaud
 * @version TEAM-18
 *
 */
@Entity
@Table(name = "t_company")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "c_discriminator", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("company")
@NamedQueries({
		@NamedQuery(name = Company.QUERY_findByIdsByType, query = "SELECT c FROM Company c WHERE c.id IN (?1) AND TYPE(c) = ?2 ORDER BY c.name"),
		@NamedQuery(name = Company.QUERY_findAllByType, query = "SELECT c FROM Company c WHERE TYPE(c) = ?1 ORDER BY c.name"),
		@NamedQuery(name = Company.QUERY_countAllByType, query = "SELECT COUNT(c) FROM Company c WHERE TYPE(c) = ?1"),
		@NamedQuery(name = Company.QUERY_findLikeKeyword, query = "SELECT c FROM Company c WHERE LOWER(c.name) LIKE ?1 ORDER BY c.name"),
		@NamedQuery(name = Company.QUERY_findLikeKeywordByType, query = "SELECT c FROM Company c WHERE LOWER(c.name) LIKE ?1 AND TYPE(c) = ?2 ORDER BY c.name"),
		@NamedQuery(name = Company.QUERY_findContacts, query = "SELECT co FROM Company c, IN(c.contacts) co WHERE c = ?1 ORDER BY co.name, co.firstName"),
		@NamedQuery(name = Company.QUERY_findByTypeByActionFollowerByActionDate, query = "SELECT DISTINCT c FROM Company c, IN(c.contacts) ct WHERE TYPE(c) = ?1 AND EXISTS (SELECT a FROM Action a WHERE a.targetPerson = ct AND a.follower = ?2 AND a.date = ?3) ORDER BY c.name ASC"),
		@NamedQuery(name = Company.QUERY_findByTypeByActionFollowerByActionDateInActionStatus, query = "SELECT DISTINCT c FROM Company c, IN(c.contacts) ct WHERE TYPE(c) = ?1 AND EXISTS (SELECT a FROM Action a WHERE a.targetPerson = ct AND a.follower = ?2 AND a.date = ?3 AND a.status IN (?4)) ORDER BY c.name ASC") })
public class Company extends AbstractEntity implements Comparable<Company> {

	/**
	 * Class uid
	 */
	private static final long serialVersionUID = -6410346778279085132L;

	/**
	 * Entity alias used in queries
	 */
	public static final String QUERY_ENTITY_ALIAS = "c";

	/**
	 * Name of the query retrieving the count of companies matching the given type
	 * 
	 * @param 1 the type of company
	 * @return the total count of this type of companies
	 */
	public static final String QUERY_countAllByType = "countAllCompanyByType";

	/**
	 * Name of the query retrieving all companies matching the given type
	 * 
	 * @param 1 the type of company
	 * @return the list of all companies with the given type
	 */
	public static final String QUERY_findAllByType = "findAllCompanyByType";

	/**
	 * String of the query retrieving all companies with the given type, that may be
	 * completed by OrderByClause.getClause()
	 * 
	 * @param 1 the given type of companies to retrieve
	 * @return the list of matching companies
	 */
	public static final String QUERY_findOrderedAllByType = "SELECT c FROM Company c WHERE TYPE(c) = ?1";

	/**
	 * Name of the query retrieving the companies matching the given list of ids
	 * 
	 * @param 1 the ids to match
	 * @param 2 the given type of companies to retriev
	 * @return thelist of matching companies
	 */
	public static final String QUERY_findByIdsByType = "findCompanyByIdsByType";

	/**
	 * String of the query retrieving the ids of the given type of company matching
	 * one of the given ids,that may be completed by OrderByClause.getClause()
	 * 
	 * @param 1 the ids the match
	 * @param 2 the type of persons to retrieve
	 * @return the list of matching ids
	 */
	public static final String QUERY_findOrderedByIdsByType = "SELECT c.id FROM Company c WHERE c.id IN (?1) AND TYPE(c) = ?2 ";

	/**
	 * Name of the query retrieving companies which the name's matching the given
	 * keyword
	 * 
	 * @param 1 the given keyword in lower case
	 * @return the matching companies
	 */
	public static final String QUERY_findLikeKeyword = "findCompanyLikeKeyword";

	/**
	 * Name of the query retrieving companies from given types, which the name's
	 * matching the given keyword
	 * 
	 * @param 1 the given keyword in lower case
	 * @param 2 the given types sequence
	 * @return the matching companies
	 */
	public static final String QUERY_findLikeKeywordByType = "findCompanyLikeKeywordByType";

	/**
	 * Name of the query retrieving contacts from a given company
	 * 
	 * @param 1 the given company
	 * @return the matching contacts
	 */
	public static final String QUERY_findContacts = "findPersonByCompany";

	/**
	 * Name of the query retrieving companies of the given type, having actions on
	 * one of their contacts followed by the given staff member, on the given date
	 * 
	 * @param 1 the type of company
	 * @param 2 the given follower of action
	 * @param 3 the given date of action
	 */
	public static final String QUERY_findByTypeByActionFollowerByActionDate = "findCompanyByTypeByActionFollowerByActionDate";

	/**
	 * Name of the query retrieving companies of the given type, having actions on
	 * one of their contacts followed by the given staff member, on the given date,
	 * with a status belonging to the given list
	 * 
	 * @param 1 the type of company
	 * @param 2 the given follower of action
	 * @param 3 the given date of action
	 * @param 4 the given list of matching action status
	 */
	public static final String QUERY_findByTypeByActionFollowerByActionDateInActionStatus = "findCompanyByTypeByActionFollowerByActionDateInActionStatus";

	/**
	 * Company ID
	 */
	@Id
	@Column(name = "c_idcompany", insertable = false, updatable = false)
	@TableGenerator(name = "idCompany_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "company_idcompany_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idCompany_seq")
	private Long id;

	/**
	 * Company name
	 */
	@Column(name = "c_name")
	private String name;

	/**
	 * Company logo
	 */
	@OneToOne(targetEntity = Document.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "c_idlogo")
	private Document logo;

	/**
	 * Profiling of the company
	 */
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "t_company_profile", joinColumns = @JoinColumn(name = "c_idcompany"))
	@AttributeOverrides({ @AttributeOverride(name = "key", column = @Column(name = "c_profile")),
			@AttributeOverride(name = "value", column = @Column(name = "c_value")) })
	private List<ProfileChoice> profiles;

	/**
	 * Main contact
	 */
	@OneToOne(targetEntity = Person.class, fetch = FetchType.EAGER, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "c_idmaincontact")
	private Person mainContact;

	/**
	 * All contacts working in the company
	 */
	@OneToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "c_idcompany")
	@OrderBy("name ASC, firstName ASC")
	private List<Person> contacts;

	/**
	 * Contact numbers
	 */
	@ElementCollection(targetClass = java.lang.String.class)
	@MapKeyClass(java.lang.String.class)
	@MapKeyColumn(name = "c_name")
	@Column(name = "c_value")
	@CollectionTable(name = "t_company_number", joinColumns = @JoinColumn(name = "c_idcompany"))
	private Map<String, String> numbers;

	/**
	 * Company address
	 */
	@OneToOne(targetEntity = Address.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "c_idaddress")
	private Address address;

	/**
	 * Billing address
	 */
	@OneToOne(targetEntity = Address.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "c_idbillingaddress")
	private Address billingAddress;

	/**
	 * Company number
	 */
	@Column(name = "c_registrationnumber")
	private String companyNumber;

	/**
	 * VAT number
	 */
	@Column(name = "c_vatnumber")
	private String vatNumber;

	/**
	 * Account number
	 */
	@Column(name = "c_accountnumber")
	private String accountNumber;

	/**
	 * Comments
	 */
	@Column(name = "c_comments")
	private String comments;

	/**
	 * Web site
	 */
	@Column(name = "c_website")
	private String website;

	/**
	 * Bank information
	 */
	@Embedded
	private BankInformation bank;

	/**
	 * Follower
	 */
	@ManyToOne
	@JoinColumn(name = "c_idfollower")
	private StaffMember follower;

	/**
	 * The currency
	 */
	@Column(name = "c_currency")
	private String currency;

	/**
	 * UI language
	 * 
	 * @see com.teamium.domain.LanguageGeneral
	 */

	@Column(name = "c_language")
	private String language;

	@Transient
	private Locale localeLanguage;

	/**
	 * The ICS Number
	 */
	@Column(name = "c_ics")
	private String ICSnumber;

	/**
	 * The accountancy numbers
	 */
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "t_company_accountancy_number", joinColumns = {
			@JoinColumn(name = "c_company") }, inverseJoinColumns = { @JoinColumn(name = "c_accountancy_number") })
	private List<AccountancyNumber> accountancyNumbers;

	/**
	 * Attachments
	 */
	@OneToMany(cascade = { CascadeType.ALL })
	@JoinColumn(name = "c_idcompany")
	private Set<Attachment> attachments = new HashSet<Attachment>();

	@Column(name = "c_domain")
	private String domian;

	@Column(name = "c_header")
	private String header;

	@Column(name = "c_footer")
	private String footer;

	/**
	 * Channel-Formats
	 */
	@OneToMany(cascade = { CascadeType.ALL })
	@JoinColumn(name = "c_idcompany")
	private Set<ChannelFormat> channelFormat = new HashSet<ChannelFormat>();

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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the profiles
	 */
	public List<ProfileChoice> getProfiles() {
		if (this.profiles == null)
			this.profiles = new ArrayList<ProfileChoice>();
		return this.profiles;
	}

	/**
	 * @param profiles the profiles to set
	 */
	public void setProfiles(List<ProfileChoice> profiles) {
		this.profiles = profiles;
	}

	/**
	 * @return the mainContact
	 */
	public Person getMainContact() {
		return mainContact;
	}

	/**
	 * @param mainContact the mainContact to set
	 */
	public void setMainContact(Person mainContact) {
		this.mainContact = mainContact;
	}

	/**
	 * @return the logo
	 */
	public Document getLogo() {
		return logo;
	}

	/**
	 * @param logo the logo to set
	 */
	public void setLogo(Document logo) {
		this.logo = logo;
	}

	/**
	 * @return the contacts
	 */
	public List<Person> getContacts() {
		if (this.contacts == null)
			this.contacts = new ArrayList<Person>();
		return contacts;
	}

	/**
	 * Add the given contact to the current company
	 * 
	 * @param person the contact to add
	 * @return true if added, else returns false
	 */
	public boolean addContact(Person person) {
		if (person != null && !this.getContacts().contains(person)) {
			return this.getContacts().add(person);
		}
		return false;
	}

	/**
	 * Remove the given contact from the current company
	 * 
	 * @param person the contact to remove
	 * @return true if removed, else returns false
	 */
	public boolean removeContact(Person person) {
		return this.getContacts().remove(person);
	}

	/**
	 * @return the numbers
	 */
	public Map<String, String> getNumbers() {
		if (this.numbers == null)
			this.numbers = new Hashtable<String, String>();
		return numbers;
	}

	/**
	 * @param numbers the numbers to set
	 */
	public void setNumbers(Map<String, String> numbers) {
		this.numbers = numbers;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		if (this.address == null)
			this.address = new Address();
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * By default, the billing address is the same that the generic address
	 * 
	 * @return the billingAddress
	 */
	public Address getBillingAddress() {
//		if (this.billingAddress == null)
//			return this.getAddress();
		return billingAddress;
	}

	/**
	 * @param billingAddress the billingAddress to set
	 */
	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	/**
	 * @return the companyNumber
	 */
	public String getCompanyNumber() {
		return companyNumber;
	}

	/**
	 * @param companyNumber the companyNumber to set
	 */
	public void setCompanyNumber(String companyNumber) {
		this.companyNumber = companyNumber;
	}

	/**
	 * @return the vatNumber
	 */
	public String getVatNumber() {
		return vatNumber;
	}

	/**
	 * @param vatNumber the vatNumber to set
	 */
	public void setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;
	}

	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the web site
	 */
	public String getWebsite() {
		return website;
	}

	/**
	 * @param webSite to set
	 */
	public void setWebsite(String website) {
		this.website = website;
	}

	/**
	 * @return the bank
	 */
	public BankInformation getBank() {
		if (bank == null)
			bank = new BankInformation();
		return bank;
	}

	/**
	 * @param bank the bank to set
	 */
	public void setBank(BankInformation bank) {
		this.bank = bank;
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
	 * @return the language
	 */
	public Locale getLocaleLanguage() {
		if (this.localeLanguage == null && this.language != null)
			this.localeLanguage = new Locale(this.language);
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
			this.language = language.getLanguage();

		}
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

	public String getICSnumber() {
		if (ICSnumber == null)
			ICSnumber = new String();
		return ICSnumber;
	}

	public void setICSnumber(String iCSnumber) {
		ICSnumber = iCSnumber;
	}

	/**
	 * @return the accountancyNumbers
	 */
	public List<AccountancyNumber> getAccountancyNumbers() {
		if (accountancyNumbers == null) {
			accountancyNumbers = new ArrayList<AccountancyNumber>();
		}
		return accountancyNumbers;
	}

	/**
	 * @param accountancyNumbers the accountancyNumbers to set
	 */
	public void setAccountancyNumbers(List<AccountancyNumber> accountancyNumbers) {
		this.accountancyNumbers = accountancyNumbers;
	}

	/**
	 * Return true if the given accountancy number is added
	 */
	public Boolean addAccountancyNumber(AbstractXmlEntity type) {
		if (type != null) {
			if (this.accountancyNumbers == null) {
				this.accountancyNumbers = new ArrayList<AccountancyNumber>();
			}
			AccountancyNumber an = new AccountancyNumber();
			an.setType((XmlEntity) type);
			for (AccountancyNumber anI : this.accountancyNumbers) {
				if (anI.getType().equals(type)) {
					return false;
				}
			}
			return this.accountancyNumbers.add(an);
		}

		return null;
	}

	/**
	 * Return true if the given accountancy number is added
	 */
	public Boolean createAccountancyNumber() {
		if (this.accountancyNumbers == null) {
			this.accountancyNumbers = new ArrayList<AccountancyNumber>();
		}
		return this.accountancyNumbers.add(new AccountancyNumber());
	}

	/**
	 * Return true if the given accountancy number is removed
	 */
	public Boolean removeAccountancyNumber(AccountancyNumber accountancyNumber) {
		if (accountancyNumber != null && this.accountancyNumbers != null) {
			return this.accountancyNumbers.remove(accountancyNumber);
		}

		return null;
	}

	/**
	 * Return the accountancy number matching the accountancy type given in
	 * parameter
	 * 
	 * @param accountancyType The accountancy type
	 * @return The accountancy number
	 */
	public AccountancyNumber getAccountancyNumber(XmlEntity accountancyType) {
		Iterator<AccountancyNumber> itAccountancyNumber = this.getAccountancyNumbers().iterator();
		AccountancyNumber accountancyNumber = null;
		// Search accountancy number matching the accountancy type given in parameter
		while (itAccountancyNumber.hasNext() && accountancyNumber == null) {
			AccountancyNumber localeAccountancyNumber = itAccountancyNumber.next();
			if (localeAccountancyNumber.getType().equals(accountancyType)) {
				accountancyNumber = localeAccountancyNumber;
			}
		}
		return accountancyNumber;
	}

	/**
	 * Compare the current object with the given one
	 * 
	 * @param other the other to compare
	 * @return -1 if the current is previous, 0 if both are equal, +1 if the given
	 *         is previous
	 */
	@Override
	public int compareTo(Company o) {
		if (o == null)
			return 1;
		if (this.name == null) {
			if (o.name != null) {
				return -1;
			}
		} else {
			int compare = Collator.getInstance().compare(this.name, o.name);
			if (compare != 0)
				return compare;
		}
		if (this.id == null) {
			if (o.id != null) {
				return -1;
			}
		} else {
			int compare = this.id.compareTo(o.id);
			if (compare != 0)
				return compare;
		}
		return 0;
	}

	/**
	 * @param contacts the contacts to set
	 */
	public void setContacts(List<Person> contacts) {
		this.contacts = contacts;
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
	 * Return the string expression of the current object
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		return super.toString() + " " + this.name;
	}

	/**
	 * @return the domian
	 */
	public String getDomian() {
		return domian;
	}

	/**
	 * @param domian the domian to set
	 */
	public void setDomian(String domian) {
		this.domian = domian;
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
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * @return the footer
	 */
	public String getFooter() {
		return footer;
	}

	/**
	 * @param footer the footer to set
	 */
	public void setFooter(String footer) {
		this.footer = footer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((contacts == null) ? 0 : contacts.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Company other = (Company) obj;
		if (contacts == null) {
			if (other.contacts != null)
				return false;
		} else if (!contacts.equals(other.contacts))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
