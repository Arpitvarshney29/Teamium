package com.teamium.enums;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.teamium.exception.UnprocessableEntityException;

/**
 * <p>
 * An enum class for DocumentType
 * </p>
 *
 */
public enum DocumentType {
	PASSPORT("Passport"), ID("ID"), DRIVING_LICENCE("Driving Licence"), PRESS_CARD("Press Card");
	private String documentType;

	/**
	 * @param documentType
	 */
	private DocumentType(String documentType) {
		this.documentType = documentType;
	}

	/**
	 * @return the documentType
	 */
	public String getDocumentType() {
		return documentType;
	}

	/**
	 * Method to get the enum
	 * 
	 * @param value
	 *            the String enum value
	 * 
	 * @return the DocumentType
	 */
	public static DocumentType getEnum(String value) {
		for (DocumentType v : values()) {
			if (v.getDocumentType().equalsIgnoreCase(value)) {
				return v;
			}
		}
		throw new UnprocessableEntityException("Invalid document type ");
	}

	/**
	 * To get document type list
	 * 
	 * @return document type list
	 */
	public static List<String> getDocumentTypes() {
		return Stream.of(values()).map(v -> v.getDocumentType())
				.sorted((doc1, doc2) -> doc1.toLowerCase().compareTo(doc2.toLowerCase())).collect(Collectors.toList());
	}

}
