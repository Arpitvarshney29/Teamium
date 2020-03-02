package com.teamium.enums;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.teamium.exception.UnprocessableEntityException;

public enum ContractType {

	FREELANCE("Freelance"), FULLTIMEPERMANENT("Full Time Permanent"), PARTIMEPERMANENT(
			"Part Time Permanent"), FIXEDTERM("Fixed Term"), INTERN("Intern");

	private String contractType;

	/**
	 * @param contractType
	 */
	private ContractType(String contractType) {
		this.contractType = contractType;
	}

	/**
	 * @return the contractType
	 */
	public String getContractType() {
		return contractType;
	}

	/**
	 * Method to get the enum
	 * 
	 * @param value
	 *            the String enum value
	 * 
	 * @return the contractType
	 */
	public static ContractType getEnum(String value) {
		for (ContractType v : values()) {
			if (v.getContractType().equalsIgnoreCase(value)) {
				return v;
			}
		}
		throw new UnprocessableEntityException("Invalid contract type ");
	}

	/**
	 * To get contract type list
	 * 
	 * @return contract type list
	 */
	public static List<String> getContractTypes() {
		return Stream.of(values()).map(v -> v.getContractType()).sorted(
				(contractType1, contractType2) -> contractType1.toLowerCase().compareTo(contractType2.toLowerCase()))
				.collect(Collectors.toList());
	}

}
