package com.teamium.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Wrapper class for Staff DropDown
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StaffDropDownDTO {

	private List<String> languages;
	private List<String> skills;
	private List<String> documentTypes;
	private List<FunctionDTO> functions;
	private List<String> roles;
	private List<String> telephoneTypes;
	private List<String> locations;
	private List<String> rateUnitsList;
	private List<CurrencyDTO> currencyList;
	private List<String> countries;
	private List<String> contractTypes;
	private List<String> socialConventionTypes;
	private List<String> groups;
	private int maxRate;
	private int MinRate;
	private List<LabourRuleDTO> labourRuleDTOs = new ArrayList<>();

	public StaffDropDownDTO() {
	}

	/**
	 * @param languages
	 * @param skills
	 * @param documentTypes
	 * @param functions
	 * @param roles
	 * @param telephoneTypes
	 */
	public StaffDropDownDTO(List<String> languages, List<String> skills, List<String> documentTypes,
			List<FunctionDTO> functions, List<String> roles, List<String> telephoneTypes) {
		this.languages = languages;
		this.skills = skills;
		this.documentTypes = documentTypes;
		this.functions = functions;
		this.roles = roles;
		this.telephoneTypes = telephoneTypes;
	}

	/**
	 * @return the languages
	 */
	public List<String> getLanguages() {
		return languages;
	}

	/**
	 * @param languages
	 *            the languages to set
	 */
	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}

	/**
	 * @return the skills
	 */
	public List<String> getSkills() {
		return skills;
	}

	/**
	 * @param skills
	 *            the skills to set
	 */
	public void setSkills(List<String> skills) {
		this.skills = skills;
	}

	/**
	 * @return the documentTypes
	 */
	public List<String> getDocumentTypes() {
		return documentTypes;
	}

	/**
	 * @param documentTypes
	 *            the documentTypes to set
	 */
	public void setDocumentTypes(List<String> documentTypes) {
		this.documentTypes = documentTypes;
	}

	/**
	 * @return the functions
	 */
	public List<FunctionDTO> getFunctions() {
		return functions;
	}

	/**
	 * @param functions
	 *            the functions to set
	 */
	public void setFunctions(List<FunctionDTO> functions) {
		this.functions = functions;
	}

	/**
	 * @return the roles
	 */
	public List<String> getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	/**
	 * @return the telephoneTypes
	 */
	public List<String> getTelephoneTypes() {
		return telephoneTypes;
	}

	/**
	 * @param telephoneTypes
	 *            the telephoneTypes to set
	 */
	public void setTelephoneTypes(List<String> telephoneTypes) {
		this.telephoneTypes = telephoneTypes;
	}

	/**
	 * @return the locations
	 */
	public List<String> getLocations() {
		return locations;
	}

	/**
	 * @param locations
	 *            the locations to set
	 */
	public void setLocations(List<String> locations) {
		this.locations = locations;
	}

	/**
	 * @return the rateUnitsList
	 */
	public List<String> getRateUnitsList() {
		return rateUnitsList;
	}

	/**
	 * @param rateUnitsList
	 *            the rateUnitsList to set
	 */
	public void setRateUnitsList(List<String> rateUnitsList) {
		this.rateUnitsList = rateUnitsList;
	}

	/**
	 * @return the currencyList
	 */
	public List<CurrencyDTO> getCurrencyList() {
		return currencyList;
	}

	/**
	 * @param currencyList
	 *            the currencyList to set
	 */
	public void setCurrencyList(List<CurrencyDTO> currencyList) {
		this.currencyList = currencyList;
	}

	/**
	 * @return the countries
	 */
	public List<String> getCountries() {
		return countries;
	}

	/**
	 * @param countries
	 *            the countries to set
	 */
	public void setCountries(List<String> countries) {
		this.countries = countries;
	}

	/**
	 * @return the contractTypes
	 */
	public List<String> getContractTypes() {
		return contractTypes;
	}

	/**
	 * @param contractTypes
	 *            the contractTypes to set
	 */
	public void setContractTypes(List<String> contractTypes) {
		this.contractTypes = contractTypes;
	}

	/**
	 * @return the socialConventionTypes
	 */
	public List<String> getSocialConventionTypes() {
		return socialConventionTypes;
	}

	/**
	 * @param socialConventionTypes
	 *            the socialConventionTypes to set
	 */
	public void setSocialConventionTypes(List<String> socialConventionTypes) {
		this.socialConventionTypes = socialConventionTypes;
	}

	/**
	 * @return the groups
	 */
	public List<String> getGroups() {
		return groups;
	}

	/**
	 * @param groups
	 *            the groups to set
	 */
	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	/**
	 * @return the maxRate
	 */
	public int getMaxRate() {
		return maxRate;
	}

	/**
	 * @param maxRate
	 *            the maxRate to set
	 */
	public void setMaxRate(int maxRate) {
		this.maxRate = maxRate;
	}

	/**
	 * @return the minRate
	 */
	public int getMinRate() {
		return MinRate;
	}

	/**
	 * @param minRate
	 *            the minRate to set
	 */
	public void setMinRate(int minRate) {
		this.MinRate = minRate;
	}

	/**
	 * @return the labourRuleDTOs
	 */
	public List<LabourRuleDTO> getLabourRuleDTOs() {
		return labourRuleDTOs;
	}

	/**
	 * @param labourRuleDTOs
	 *            the labourRuleDTOs to set
	 */
	public void setLabourRuleDTOs(List<LabourRuleDTO> labourRuleDTOs) {
		this.labourRuleDTOs = labourRuleDTOs;
	}

}