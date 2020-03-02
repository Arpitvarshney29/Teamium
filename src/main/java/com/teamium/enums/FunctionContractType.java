package com.teamium.enums;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.teamium.exception.UnprocessableEntityException;

public enum FunctionContractType {

	ANIMATOR("Animator"), AUTEUR_ERITURE("Auteur Ecriture"), AUTEUR_REAL("Auteur Real"), JOURNALIST("Journalist"),
	PART_TIME("Part Time");

	private String contractType;

	/**
	 * @param contractType
	 */
	private FunctionContractType(String contractType) {
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
	 * @param value the String enum value
	 * 
	 * @return the FunctionContractType
	 */
	public FunctionContractType getEnum(String value) {
		for (FunctionContractType v : values()) {
			if (v.getContractType().equalsIgnoreCase(value)) {
				return v;
			}
		}
		throw new UnprocessableEntityException("Invalid contract type ");
	}

	/**
	 * To get list of FunctionContractType
	 * 
	 * @return FunctionContractType list
	 */
	public static List<String> geAssignationTypes() {
		return Stream.of(values()).map(v -> v.getContractType()).sorted().collect(Collectors.toList());
	}

}
