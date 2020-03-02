package com.teamium.dto;

import java.util.ArrayList;
import java.util.List;

public class RateDropdownDTO {

	List<CurrencyDTO> currencies = new ArrayList<>();
	List<String> basis = new ArrayList<String>();
	List<CompanyDTO> companies = new ArrayList<CompanyDTO>();
	List<CompanyDTO> clients = new ArrayList<CompanyDTO>();
	List<CompanyDTO> suppliers = new ArrayList<CompanyDTO>();
	List<FunctionDTO> functions = new ArrayList<FunctionDTO>();

	/**
	 * @param currencyList
	 * @param basis
	 * @param companies
	 * @param clients
	 * @param suppliers
	 * @param functions
	 */
	public RateDropdownDTO(List<CurrencyDTO> currencyList, List<String> basis, List<CompanyDTO> companies,
			List<CompanyDTO> clients, List<CompanyDTO> suppliers, List<FunctionDTO> functions) {
		this.currencies = currencyList;
		this.basis = basis;
		this.companies = companies;
		this.clients = clients;
		this.suppliers = suppliers;
		this.functions = functions;
	}

	/**
	 * @return the currencyList
	 */
	public List<CurrencyDTO> getCurrencyList() {
		return currencies;
	}

	/**
	 * @param currencyList the currencyList to set
	 */
	public void setCurrencyList(List<CurrencyDTO> currencyList) {
		this.currencies = currencyList;
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
	 * @return the companies
	 */
	public List<CompanyDTO> getCompanies() {
		return companies;
	}

	/**
	 * @param companies the companies to set
	 */
	public void setCompanies(List<CompanyDTO> companies) {
		this.companies = companies;
	}

	/**
	 * @return the clients
	 */
	public List<CompanyDTO> getClients() {
		return clients;
	}

	/**
	 * @param clients the clients to set
	 */
	public void setClients(List<CompanyDTO> clients) {
		this.clients = clients;
	}

	/**
	 * @return the suppliers
	 */
	public List<CompanyDTO> getSuppliers() {
		return suppliers;
	}

	/**
	 * @param suppliers the suppliers to set
	 */
	public void setSuppliers(List<CompanyDTO> suppliers) {
		this.suppliers = suppliers;
	}

	/**
	 * @return the functions
	 */
	public List<FunctionDTO> getFunctions() {
		return functions;
	}

	/**
	 * @param functions the functions to set
	 */
	public void setFunctions(List<FunctionDTO> functions) {
		this.functions = functions;
	}

}
