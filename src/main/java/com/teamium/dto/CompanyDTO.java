package com.teamium.dto;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.Address;
import com.teamium.domain.BankInformation;
import com.teamium.domain.Company;
import com.teamium.domain.Document;
import com.teamium.domain.Person;
import com.teamium.domain.prod.resources.accountancy.AccountancyNumber;
import com.teamium.domain.prod.resources.equipments.Attachment;
import com.teamium.dto.prod.resources.functions.RateDTO;
import com.teamium.enums.project.Language;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;

/**
 * DTO Class for Company
 * 
 * @author Nishant Chauhan
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CompanyDTO extends AbstractDTO {

	private String name;
	private Document logo;
	private AddressDTO address;
	private String companyNumber;
	private String vatNumber;
	private String accountNumber;
	private String comments;
	private String website;
	// private Map<String, String> numbers;
	private String number;
	private BankInformation bank;
	private AddressDTO billingAddress;
	private PersonDTO mainContact;
	private List<PersonDTO> contacts = new ArrayList<PersonDTO>();
	private String currency;
	private String language;
	private List<RateDTO> rateCard;
	private Set<AccountancyNumberDTO> accoutingCode = new HashSet<AccountancyNumberDTO>();
	private Set<Attachment> attachments = new HashSet<Attachment>();
	private List<QuotationDTO> projects = new ArrayList<>();
	private boolean isSameBillingAddress;
	private String domain;

	private String header;
	private String footer;

	public CompanyDTO() {
		super();
	}

	public CompanyDTO(String name, Document logo, AddressDTO address, String companyNumber, String vatNumber,
			String accountNumber, String comments, Map<String, String> numbers, AddressDTO billingAddress,
			String website) {
		this.name = name;
		this.logo = logo;
		this.address = address;
		this.companyNumber = companyNumber;
		this.vatNumber = vatNumber;
		this.accountNumber = accountNumber;
		this.comments = comments;
		// this.numbers = numbers;
		this.billingAddress = billingAddress;
		this.website = website;
	}

	public CompanyDTO(Company company) {
		super(company.getId(), company.getVersion());
		this.name = company.getName();
		this.logo = company.getLogo();
		Address companyAddress = company.getAddress();
		if (companyAddress != null)
			address = new AddressDTO(companyAddress);
		this.companyNumber = company.getCompanyNumber();
		this.vatNumber = company.getVatNumber();
		this.accountNumber = company.getAccountNumber();
		this.comments = company.getComments();
		if (company.getNumbers() != null) {
			this.number = company.getNumbers().get("phone");
		}

		this.bank = company.getBank();
		if (company.getBillingAddress() != null) {
			this.billingAddress = new AddressDTO(company.getBillingAddress());
		}
		this.website = company.getWebsite();

		if (company.getContacts().size() > 0) {
			this.contacts = company.getContacts().stream().map(contact -> new PersonDTO(contact))
					.collect(Collectors.toList());
		}
		if (company.getMainContact() != null) {
			PersonDTO p = new PersonDTO(company.getMainContact());
			p.setMain(true);
			this.contacts.add(p);
			this.mainContact = p;
		}
		if (company.getCurrency() != null) {
			this.currency = company.getCurrency().getCurrencyCode();
		}
		if (company.getMainContact() != null) {
			this.mainContact = new PersonDTO(company.getMainContact());
			mainContact.setMain(true);
			// this.contacts.add(mainContact);
		}
		if (company.getAccountancyNumbers() != null) {
			this.accoutingCode = company.getAccountancyNumbers().stream().map(acc -> new AccountancyNumberDTO(acc))
					.collect(Collectors.toSet());
		}
		this.accountNumber = company.getAccountNumber();

		this.language = company.getLanguage();
		this.attachments = company.getAttachments();
		this.domain = company.getDomian();
		this.header=company.getHeader();
		this.footer=company.getFooter();
		
	}

	/**
	 * Get Company details from DTO
	 * 
	 * @param company
	 * @return Company
	 */
	@JsonIgnore
	public Company getCompanyDetails(Company company) {
		company.setId(this.getId());
		company.setVersion(this.getVersion());
		if (StringUtils.isBlank(name)) {
			throw new UnprocessableEntityException("Invalid name");
		}
		company.setName(name);
		if (logo != null) {
			company.setLogo(logo);
		}
		if (address != null) {
			company.setAddress(address.getAddress(company.getAddress()));
		}
		// company.setNumbers(numbers);
		Map<String, String> numbers = new HashMap<String, String>();
		numbers.put("phone", number);
		company.setNumbers(numbers);
		company.setVatNumber(vatNumber);
		company.setBank(bank);
		company.setAccountNumber(accountNumber);
		company.setCompanyNumber(companyNumber);
		company.setComments(comments);
		company.setWebsite(website);
		// company.setLanguage(language);
		if (billingAddress != null) {
			company.setBillingAddress(billingAddress.getAddress(company.getBillingAddress()));
		}
//		if (mainContact != null) {
//			company.setMainContact(mainContact.getPerson(new Person()));
//		}else {

//		}
		// main contact should be removed if no contact is main
		company.setMainContact(null);
		if (contacts != null) {
			List<Person> contactsList = new LinkedList<Person>();
			if (contacts.size() == 1) {
				company.setMainContact(contacts.get(0).getPerson(new Person()));
			} else {
				for (PersonDTO contact : contacts) {
					if (contact.isMain()) {
						company.setMainContact(contact.getPerson(new Person()));
					} else {
						contactsList.add(contact.getPerson(new Person()));
					}
				}
			}
			company.setContacts(contactsList);
//			company.setContacts(contacts.stream().map(con -> con.getPerson(new Person())).collect(Collectors.toList()));
		}
		if (language != null) {
			company.setLanguage(Language.getEnum(language).getLanguage());
		}
		if (currency != null) {

			try {
				company.setCurrency(Currency.getInstance(currency));
			} catch (Exception e) {
			}
		}
		if (this.accoutingCode != null) {
			this.accoutingCode.stream().collect(Collectors.groupingBy(AccountancyNumberDTO::getType))
					.forEach((key, value) -> {
						if (value.size() > 1) {
							throw new NotFoundException("Duplicate Accounting type " + key);
						}
					});
			;
			ArrayList<AccountancyNumber> functionAccNo = new ArrayList<AccountancyNumber>();
			this.accoutingCode.forEach(acc -> functionAccNo.add(acc.getAccountancyNumber(new AccountancyNumber())));
			company.setAccountancyNumbers(functionAccNo);
		}

		if (company.getAttachments() != null && !company.getAttachments().isEmpty()) {
			this.attachments = company.getAttachments();
		}
		company.setHeader(header);
		company.setFooter(footer);
		return company;
	}

	/**
	 * Get Company details from DTO
	 * 
	 * @return Company
	 */
	@JsonIgnore
	public Company getCompanyDetails() {
		return this.getCompanyDetails(new Company());
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
	 * @return the address
	 */
	public AddressDTO getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(AddressDTO address) {
		this.address = address;
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
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the bank
	 */
	public BankInformation getBank() {
		return bank;
	}

	/**
	 * @param bank the bank to set
	 */
	public void setBank(BankInformation bank) {
		this.bank = bank;
	}

	/**
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}

	/**
	 * @param website the website to set
	 */
	public void setWebsite(String website) {
		this.website = website;
	}

	/**
	 * @return the billingAddress
	 */
	public AddressDTO getBillingAddress() {
		return billingAddress;
	}

	/**
	 * @param billingAddress the billingAddress to set
	 */
	public void setBillingAddress(AddressDTO billingAddress) {
		this.billingAddress = billingAddress;
	}

	/**
	 * @return the mainContact
	 */
	public PersonDTO getMainContact() {
		return mainContact;
	}

	/**
	 * @param mainContact the mainContact to set
	 * @param mainContact the mainContact to set
	 */
	public void setMainContact(PersonDTO mainContact) {
		this.mainContact = mainContact;
	}

	/**
	 * @return the contacts
	 */
	public List<PersonDTO> getContacts() {
		return contacts;
	}

	/**
	 * @param contacts the contacts to set
	 * @param contacts the contacts to set
	 */
	public void setContacts(List<PersonDTO> contacts) {
		this.contacts = contacts;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the rateCard
	 */
	public List<RateDTO> getRateCard() {
		return rateCard;
	}

	/**
	 * @param rateCard the rateCard to set
	 */
	public void setRateCard(List<RateDTO> rateCard) {
		this.rateCard = rateCard;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CompanyDTO [name=" + name + ", logo=" + logo + ", address=" + address + ", companyNumber="
				+ companyNumber + ", vatNumber=" + vatNumber + ", accountNumber=" + accountNumber + ", comments="
				+ comments + ", website=" + website + ", numbers=" + number + ", bank=" + bank + ", billingAddress="
				+ billingAddress + ", mainContact=" + mainContact + ", contacts=" + contacts + ", currency=" + currency
				+ ", language=" + language + "]";
	}

	/**
	 * @return the accoutingCode
	 */
	public Set<AccountancyNumberDTO> getAccoutingCode() {
		return accoutingCode;
	}

	/**
	 * @param accoutingCode the accoutingCode to set
	 */
	public void setAccoutingCode(Set<AccountancyNumberDTO> accoutingCode) {
		this.accoutingCode = accoutingCode;
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
	 * @return the projects
	 */
	public List<QuotationDTO> getProjects() {
		return projects;
	}

	/**
	 * @param projects the projects to set
	 */
	public void setProjects(List<QuotationDTO> projects) {
		this.projects = projects;
	}

	/**
	 * @return the isSameBillingAddress
	 */
	public boolean isSameBillingAddress() {
		if(this.address!=null) {
			return this.address.equals(this.billingAddress);
		}
		return false;
	}

	/**
	 * @param isSameBillingAddress the isSameBillingAddress to set
	 */
	public void setSameBillingAddress(boolean isSameBillingAddress) {
		if(this.address!=null) {
			this.isSameBillingAddress =  this.address.equals(this.billingAddress);
		}
		
		
	}

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
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

}
