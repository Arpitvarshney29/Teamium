package com.teamium.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.Document;
import com.teamium.domain.ProfileChoice;
import com.teamium.domain.Vat;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.resources.contacts.Customer;

/**
 * Wrapper class for Customer
 * 
 * @author Teamium
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CustomerDTO extends CompanyDTO {
	private List<ChannelDTO> channels;
	private List<DirectorDTO> directors;
	private String tax;
	private String type;
	private String profile;
	private int totalProjects;

	public CustomerDTO() {
	}

	public CustomerDTO(String name, Document logo, AddressDTO address, String companyNumber, String vatNumber,
			String accountNumber, String comments, Map<String, String> numbers) {
		super(name, logo, address, companyNumber, vatNumber, accountNumber, comments, numbers, address, comments);
	}

	public CustomerDTO(Customer customer) {
		super(customer);
		if (customer.getChannels() != null) {
			this.channels = customer.getChannels().stream().map(c -> new ChannelDTO(c)).collect(Collectors.toList());
		}
		if (customer.getDirectors() != null) {
			this.directors = customer.getDirectors().stream().map(d -> new DirectorDTO(d)).collect(Collectors.toList());
		}
		if (customer.getVatRates() != null) {
			customer.getVatRates().forEach(f -> {

				if (StringUtils.isNoneBlank(f.getKey())) {
					this.tax = f.getKey();
				}
			});
		}
		if (customer.getType() != null) {
			this.type = customer.getType().getKey();
		}

		customer.getProfiles().forEach(pc -> {
			if (StringUtils.isNoneBlank(pc.getKey())) {
				this.profile = pc.getKey();
			}
		});

	}

	/**
	 * To get entity object from DTO
	 * 
	 * @param customer
	 * @return Customer
	 */
	@JsonIgnore
	public Customer getCustomer(Customer customer) {
		super.getCompanyDetails(customer);
//		if (channels != null) {
//			customer.setChannels(channels.stream().map(c -> c.getChannel()).collect(Collectors.toList()));
//		}
//		if (directors != null) {
//			customer.setDirectors(directors.stream().map(d -> d.getDirector()).distinct().collect(Collectors.toList()));
//		}
		if (StringUtils.isNoneBlank(tax)) {
			Vat vat = new Vat();
			vat.setKey(tax);
			customer.addVatRate(vat);
		}
		if (StringUtils.isNoneBlank(type)) {
			XmlEntity tp = new XmlEntity();
			tp.setKey(type);
			customer.setType(tp);
		}
		if (StringUtils.isNoneBlank(profile)) {
			ProfileChoice profileChoice = new ProfileChoice();
			profileChoice.setKey(profile);
			List<ProfileChoice> choice = new ArrayList<ProfileChoice>();
			choice.add(profileChoice);
			customer.setProfiles(choice);
		}
		return customer;
	}

	/**
	 * To get entity object from DTO
	 *
	 * @return Customer
	 */
	@JsonIgnore
	public Customer getCustomer() {
		Customer customer = new Customer();
		return getCustomer(customer);
	}

	/**
	 * @return the directors
	 */
	public List<DirectorDTO> getDirectors() {
		return directors;
	}

	/**
	 * @param directors the directors to set
	 */
	public void setDirectors(List<DirectorDTO> directors) {
		this.directors = directors;
	}

	/**
	 * @return the channels
	 */
	public List<ChannelDTO> getChannels() {
		return channels;
	}

	/**
	 * @param channels the channels to set
	 */
	public void setChannels(List<ChannelDTO> channels) {
		this.channels = channels;
	}

	/**
	 * @return the tax
	 */
	public String getTax() {
		return tax;
	}

	/**
	 * @param tax the tax to set
	 */
	public void setTax(String tax) {
		this.tax = tax;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the profile
	 */
	public String getProfile() {
		return profile;
	}

	/**
	 * @param profile the profile to set
	 */
	public void setProfile(String profile) {
		this.profile = profile;
	}

	/**
	 * @return the totalProjects
	 */
	public int getTotalProjects() {
		return totalProjects;
	}

	/**
	 * @param totalProjects the totalProjects to set
	 */
	public void setTotalProjects(int totalProjects) {
		this.totalProjects = totalProjects;
	}

	
}
