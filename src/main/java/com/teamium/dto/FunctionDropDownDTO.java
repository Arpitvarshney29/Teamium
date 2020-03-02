package com.teamium.dto;

import java.util.List;

/**
 * Wrapper class for Function DropDown
 *
 */
public class FunctionDropDownDTO {

	List<String> functionTypes;
	List<String> basis;
	List<String> origines;
	List<String> contracts;
	List<String> accountingCodes;
	List<String> keywords;

	public FunctionDropDownDTO() {
	}

	/**
	 * @param functionTypes
	 * @param basis
	 * @param origines
	 * @param contracts
	 * @param accountingCodes
	 */
	public FunctionDropDownDTO(List<String> functionTypes, List<String> basis, List<String> origines,
			List<String> contracts, List<String> accountingCodes) {
		super();
		this.functionTypes = functionTypes;
		this.basis = basis;
		this.origines = origines;
		this.contracts = contracts;
		this.accountingCodes = accountingCodes;
	}

	/**
	 * @return the functionTypes
	 */
	public List<String> getFunctionTypes() {
		return functionTypes;
	}

	/**
	 * @param functionTypes the functionTypes to set
	 */
	public void setFunctionTypes(List<String> functionTypes) {
		this.functionTypes = functionTypes;
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
	 * @return the origines
	 */
	public List<String> getOrigines() {
		return origines;
	}

	/**
	 * @param origines the origines to set
	 */
	public void setOrigines(List<String> origines) {
		this.origines = origines;
	}

	/**
	 * @return the contracts
	 */
	public List<String> getContracts() {
		return contracts;
	}

	/**
	 * @param contracts the contracts to set
	 */
	public void setContracts(List<String> contracts) {
		this.contracts = contracts;
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
	 * @return the keywords
	 */
	public List<String> getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

}
