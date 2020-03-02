package com.teamium.domain.output;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.teamium.domain.Company;
import com.teamium.domain.Person;

/**
 * An XML company output
 * @author sraybaud
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CompanyXml {
	
	/**
	 * Company ID
	 */
	@XmlAttribute
	private Long id;
	
	/**
	 * Company name
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String name;
	
	/**
	 * Company address
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private AddressXml address;
	
	/**
	 * Billing address
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private AddressXml billingAddress;
	
	/**
	 * Comments
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String comments;
	
	/**
	 * Main contact
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private ContactXml contact;
	
	/**
	 * Numbers
	 */
	@XmlElementWrapper(name="numbers", namespace=XmlOutput.XMLNS)
	@XmlElement(name="number", namespace=XmlOutput.XMLNS)
	private List<NumberXml> numbers;
	
	/**
	 * Web site
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String website;
	
	/**
	 * Company logo url
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private DocumentXml logo;	
	
	/**
	 * type (SA, SARL etc.)
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String type;
	
	/**
	 * Capital
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private Float capital;

	/**
	 * registration number
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String registrationNumber;
	
	/**
	 * VAT number
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String vatNumber;
	
	/**
	 * Account number
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String accountNumber;
	
	/**
	 * Bank information
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private BankInformationXml bank;
	
	/**
	 * ICS
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String ICS;
	
	/**
	 * @param contact the contact to set
	 */
	public void setContact(Person contact) {
		if(contact!=null){
			this.contact = new ContactXml();
			this.contact.marshal(contact);
		}else{
			this.contact=null;
		}
	}
	/**
	 * @param obj the object to marshal
	 */
	public void marshal(Company obj){
		this.id=obj.getId();
		this.name=obj.getName();
		if(obj.getBillingAddress()!=null){
			this.billingAddress=new AddressXml();
			this.billingAddress.marshal(obj.getBillingAddress());
		}
		if(obj.getAddress()!=null){
			this.address=new AddressXml();
			this.address.marshal(obj.getAddress());
			if(this.billingAddress == null)
			{
				this.billingAddress=new AddressXml();
				this.billingAddress.marshal(obj.getAddress());
			}
		}
		if(obj.getMainContact()!=null){
			this.contact = new ContactXml();
			this.contact.marshal(obj.getMainContact());
		}
		if(obj.getComments() != null)
			this.comments = obj.getComments();
		
		this.numbers = new ArrayList<NumberXml>();
		for(String key : obj.getNumbers().keySet()){
			NumberXml number = new NumberXml();
			number.marshal(key, obj.getNumbers().get(key));
			this.numbers.add(number);
		}
		this.website=obj.getWebsite();
		if(obj.getLogo()!=null){
			this.logo = new DocumentXml();
			this.logo.marshal(obj.getLogo());
		}
		this.type=null;//TO DO
		this.capital=null;//TO DO
		this.registrationNumber=obj.getCompanyNumber();
		this.vatNumber=obj.getVatNumber();
		this.accountNumber=obj.getAccountNumber();
		this.ICS = obj.getICSnumber();
		if(obj.getBank()!=null){
			this.bank = new BankInformationXml();
			this.bank.marshal(obj.getBank());
		}
	}	
}
