package com.teamium.enums.project;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.teamium.exception.UnprocessableEntityException;

/**
 * <p>
 * An enum for project CategoryType.
 * </p>
 *
 */
public enum CategoryType {
	PRODUCTION("Production"), TRANSMISSION("Transmission"), POSTPROD("Postprod"), COMMERCIAL("Commercial"),
	PROGRAM("Program"), TEASER("Teaser"), INFORMERCIAL("Infomercial");

	private String type;

	/**
	 * @param type
	 */
	private CategoryType(String type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Method to get the enum
	 * 
	 * @param value the String enum value
	 * 
	 * @return the CategoryType
	 */
	public static CategoryType getEnum(String value) {
		for (CategoryType v : values()) {
			if (v.getType().equalsIgnoreCase(value)) {
				return v;
			}
		}
		throw new UnprocessableEntityException("Invalid Category.");
	}

	
	/**
	 * To get list of categories types
	 * 
	 * @return categories type list
	 */
	public static List<String> getCategoryTypes() {
		return Stream.of(values()).map(reel -> reel.getType()).sorted().collect(Collectors.toList());
	}

}
