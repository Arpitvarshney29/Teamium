package com.teamium.enums.project;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.teamium.exception.UnprocessableEntityException;

/**
 * <p>
 * An enum class for TelephoneType .
 * </p>
 *
 */
public enum TelephoneType {

	EMAIL("Email"), GOOGLE_CAL("GoogleCal"), MOBILE_PHONE("Mobile Phone"), PHONE("Phone"), SKYPE("Skype");
	private String telephoneType;

	/**
	 * @return the telephoneType
	 */
	public String getTelephoneType() {
		return telephoneType;
	}

	/**
	 * @param telephoneType
	 */
	private TelephoneType(String telephoneType) {
		this.telephoneType = telephoneType;
	}

	/**
	 * Validate the TelephoneType type value.
	 * 
	 * @param value
	 * @return TelephoneType
	 */
	public static TelephoneType setEnum(String value) {
		for (TelephoneType v : values()) {
			if (v.getTelephoneType().equalsIgnoreCase(value)) {
				return v;
			}
		}
		throw new UnprocessableEntityException("Invalid TelephoneType Name");
	}

	/**
	 * To get list of TelephoneType
	 * 
	 * @return list of TelephoneType
	 */
	public static List<String> getTelephoneTypes() {
		return Stream.of(values()).map(v -> v.getTelephoneType())
				.sorted((tel1, tel2) -> tel1.toLowerCase().compareTo(tel2.toLowerCase())).collect(Collectors.toList());
	}

}
