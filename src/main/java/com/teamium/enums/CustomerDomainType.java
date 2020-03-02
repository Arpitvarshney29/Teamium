package com.teamium.enums;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.teamium.exception.UnprocessableEntityException;

public enum CustomerDomainType {
	
	AGENCY("AGENCY"), FEDERAL("FEDERAL"), MOBILE_UNIT("MOBILE UNIT"), RADIO("RADIO"),SECURITY("SECURITY"),STUDIO("STUDIO"),TELEVISION("TELEVISION");
	
	private String domainType;

	/**
	 * @param documentType
	 */
	private CustomerDomainType(String domainType) {
		this.domainType = domainType;
	}


	/**
	 * @return the domainType
	 */
	public String getDomainType() {
		return domainType;
	}



	/**
	 * Method to get the enum
	 * 
	 * @param value
	 *            the String enum value
	 * 
	 * @return the DocumentType
	 */
	public static CustomerDomainType getEnum(String value) {
		for (CustomerDomainType v : values()) {
			if (v.getDomainType().equalsIgnoreCase(value)) {
				return v;
			}
		}
		throw new UnprocessableEntityException("Invalid category " + value);
	}
	
	
	/**
	 * Method check if enum has value or not
	 * 
	 * @param value
	 *            the String enum value
	 * 
	 * @return the DocumentType
	 */
	public static boolean hasEnum(String value) {
		for (CustomerDomainType v : values()) {
			if (v.getDomainType().equalsIgnoreCase(value)) {
				return true;
			}
		}
		return false;
	}


	/**
	 * To get document type list
	 * 
	 * @return document type list
	 */
	public static List<String> getDomainTypes() {
		return Stream.of(values()).map(v -> v.getDomainType())
				.sorted((doc1, doc2) -> doc1.toLowerCase().compareTo(doc2.toLowerCase())).collect(Collectors.toList());
	}
}
