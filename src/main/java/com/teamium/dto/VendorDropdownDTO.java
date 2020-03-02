package com.teamium.dto;

import java.util.List;


/**
 * Wrapper class for VendorDropdown
 * 
 * @author Hansraj
 *
 */
public class VendorDropdownDTO {

	List<CurrencyDTO> currencyList;
	List<String> languagesList;
	List<String> countryList;
	List<String> accountingCodes;
	List<String> formats;
	List<String> basis;
	List<String>  domain;
	List<String> cities;
	
	/**
	 * @param currencyList
	 * @param languagesList
	 * @param countryList
	 * @param accountingCodes
	 * @param formats
	 * @param basis
	 * @param domain
	 * @param cities
	 */
	public VendorDropdownDTO(List<CurrencyDTO> currencyList, List<String> languagesList, List<String> countryList,
			List<String> accountingCodes, List<String> formats, List<String> basis, List<String> domain,
			List<String> cities) {
		this.currencyList = currencyList;
		this.languagesList = languagesList;
		this.countryList = countryList;
		this.accountingCodes = accountingCodes;
		this.formats = formats;
		this.basis = basis;
		this.domain = domain;
		this.cities = cities;
	}
	
	

	public VendorDropdownDTO() {
		
	}


	/**
	 * @return the currencyList
	 */
	public List<CurrencyDTO> getCurrencyList() {
		return currencyList;
	}
	/**
	 * @param currencyList the currencyList to set
	 */
	public void setCurrencyList(List<CurrencyDTO> currencyList) {
		this.currencyList = currencyList;
	}
	/**
	 * @return the languagesList
	 */
	public List<String> getLanguagesList() {
		return languagesList;
	}
	/**
	 * @param languagesList the languagesList to set
	 */
	public void setLanguagesList(List<String> languagesList) {
		this.languagesList = languagesList;
	}
	/**
	 * @return the countryList
	 */
	public List<String> getCountryList() {
		return countryList;
	}
	/**
	 * @param countryList the countryList to set
	 */
	public void setCountryList(List<String> countryList) {
		this.countryList = countryList;
	}
	/**
	 * @return the accountingCodes
	 */
	public List<String> getAccountingCodes() {
		return accountingCodes;
	}
	/**
	 * @param accountingCodes the accountingCodes to set
	 */
	public void setAccountingCodes(List<String> accountingCodes) {
		this.accountingCodes = accountingCodes;
	}
	/**
	 * @return the formats
	 */
	public List<String> getFormats() {
		return formats;
	}
	/**
	 * @param formats the formats to set
	 */
	public void setFormats(List<String> formats) {
		this.formats = formats;
	}
	/**
	 * @return the basis
	 */
	public List<String> getBasis() {
		return basis;
	}
	/**
	 * @param basis the basis to set
	 */
	public void setBasis(List<String> basis) {
		this.basis = basis;
	}
	/**
	 * @return the domain
	 */
	public List<String> getDomain() {
		return domain;
	}
	/**
	 * @param domain the domain to set
	 */
	public void setDomain(List<String> domain) {
		this.domain = domain;
	}
	/**
	 * @return the cities
	 */
	public List<String> getCities() {
		return cities;
	}
	/**
	 * @param cities the cities to set
	 */
	public void setCities(List<String> cities) {
		this.cities = cities;
	}
	
}
