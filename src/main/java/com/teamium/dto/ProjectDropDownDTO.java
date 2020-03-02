package com.teamium.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.Format;

/**
 * Wrapper class for Project Drop-Down List
 * 
 * @author Teamium
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ProjectDropDownDTO {

	private List<String> milestones;
	private List<String> categories;
	private List<CustomerDTO> clients;
	private List<ChannelDTO> channels;
	private List<String> formats;
	private List<StaffMemberDTO> followers = new ArrayList<StaffMemberDTO>();
	private List<ProgramDTO> programs = new ArrayList<ProgramDTO>();
	private List<CompanyDTO> saleEntities = new ArrayList<CompanyDTO>();
	private Map<String, List<FunctionDTO>> functionsByType = new HashMap<String, List<FunctionDTO>>();
	private List<String> unitBasis;
	private List<CurrencyDTO> currencyList;
	private List<String> projectStatusList;
	private List<String> projectFinancialStatusList;
	private List<LayoutDTO> templateList;

	/**
	 * @param milestones
	 * @param categories
	 * @param clients
	 */
	public ProjectDropDownDTO(List<String> milestones, List<String> categories, List<CustomerDTO> clients) {
		this.milestones = milestones;
		this.categories = categories;
		this.clients = clients;
	}

	public ProjectDropDownDTO(List<String> milestones, List<String> categories, List<CustomerDTO> clients,
			List<ChannelDTO> channels, List<String> formats, List<ProgramDTO> programs,
			List<CompanyDTO> saleEntities, Map<String, List<FunctionDTO>> functionsByType,
			List<StaffMemberDTO> followers, List<String> unitBasis, List<CurrencyDTO> currencyList,
			List<String> projectStatusList, List<String> projectFinancialStatusList, List<LayoutDTO> templates) {
		this.milestones = milestones;
		this.categories = categories;
		this.clients = clients;
		this.channels = channels;
		this.formats = formats;
		this.programs = programs;
		this.saleEntities = saleEntities;
		this.functionsByType = functionsByType;
		this.followers = followers;
		this.unitBasis = unitBasis;
		this.currencyList = currencyList;
		this.projectStatusList = projectStatusList;
		this.projectFinancialStatusList = projectFinancialStatusList;
		this.templateList = templates;
	}

	/**
	 * @return the milestones
	 */
	public List<String> getMilestones() {
		return milestones;
	}

	/**
	 * @param milestones the milestones to set
	 */
	public void setMilestones(List<String> milestones) {
		this.milestones = milestones;
	}

	/**
	 * @return the categories
	 */
	public List<String> getCategories() {
		return categories;
	}

	/**
	 * @param categories the categories to set
	 */
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	/**
	 * @return the clients
	 */
	public List<CustomerDTO> getClients() {
		return clients;
	}

	/**
	 * @param clients the clients to set
	 */
	public void setClients(List<CustomerDTO> clients) {
		this.clients = clients;
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
	 * @return the programs
	 */
	public List<ProgramDTO> getPrograms() {
		return programs;
	}

	/**
	 * @param programs the programs to set
	 */
	public void setPrograms(List<ProgramDTO> programs) {
		this.programs = programs;
	}

	

	/**
	 * @return the saleEntities
	 */
	public List<CompanyDTO> getSaleEntities() {
		return saleEntities;
	}

	/**
	 * @param saleEntities the saleEntities to set
	 */
	public void setSaleEntities(List<CompanyDTO> saleEntities) {
		this.saleEntities = saleEntities;
	}

	/**
	 * @return the functionsByType
	 */
	public Map<String, List<FunctionDTO>> getFunctionsByType() {
		return functionsByType;
	}

	/**
	 * @param functionsByType the functionsByType to set
	 */
	public void setFunctionsByType(Map<String, List<FunctionDTO>> functionsByType) {
		this.functionsByType = functionsByType;
	}

	/**
	 * @return the followers
	 */
	public List<StaffMemberDTO> getFollowers() {
		return followers;
	}

	/**
	 * @param followers the followers to set
	 */
	public void setFollowers(List<StaffMemberDTO> followers) {
		this.followers = followers;
	}

	/**
	 * @return the unitBasis
	 */
	public List<String> getUnitBasis() {
		return unitBasis;
	}

	/**
	 * @param unitBasis the unitBasis to set
	 */
	public void setUnitBasis(List<String> unitBasis) {
		this.unitBasis = unitBasis;
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
	 * @return the projectStatusList
	 */
	public List<String> getProjectStatusList() {
		return projectStatusList;
	}

	/**
	 * @param projectStatusList the projectStatusList to set
	 */
	public void setProjectStatusList(List<String> projectStatusList) {
		this.projectStatusList = projectStatusList;
	}

	/**
	 * @return the projectFinancialStatusList
	 */
	public List<String> getProjectFinancialStatusList() {
		return projectFinancialStatusList;
	}

	/**
	 * @param projectFinancialStatusList the projectFinancialStatusList to set
	 */
	public void setProjectFinancialStatusList(List<String> projectFinancialStatusList) {
		this.projectFinancialStatusList = projectFinancialStatusList;
	}

	/**
	 * @return the templateList
	 */
	public List<LayoutDTO> getTemplateList() {
		return templateList;
	}

	/**
	 * @param templateList the templateList to set
	 */
	public void setTemplateList(List<LayoutDTO> templateList) {
		this.templateList = templateList;
	}

}
