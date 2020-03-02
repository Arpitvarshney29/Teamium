package com.teamium.enums;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.teamium.exception.UnprocessableEntityException;

public enum RateUnitType {
	DAY("Day"), HOUR("Hour"), MINUTE("Minute"), UNITY("Unity"), WEEK("Week"), SPECIAL("Special");
	private String type;

	/**
	 * @param type
	 */
	private RateUnitType(String type) {
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
	 * @param value
	 *            the String enum value
	 * 
	 * @return the RateUnitType
	 */
	public static RateUnitType getEnum(String value) {
		for (RateUnitType v : values()) {
			if (v.getType().equalsIgnoreCase(value)) {
				return v;
			}
		}
		throw new UnprocessableEntityException("Invalid unit / basis");
	}

	/**
	 * To get rate unit type list
	 * 
	 * @return rate unit type list
	 */
	public static List<String> getRateUnitTypes() {
		return Stream.of(values()).map(v -> v.getType())
				.collect(Collectors.toList());
	}

}
