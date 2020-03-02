package com.teamium.enums;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.teamium.exception.UnprocessableEntityException;

public enum Gender {

	MALE("Male"), FEMALE("Female"), THIRD("Third");

	private String gender;

	/**
	 * @param gender
	 */
	private Gender(String conventionType) {
		this.gender = conventionType;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * Method to get the gender enum
	 * 
	 * @param value
	 *            the String enum value
	 * 
	 * @return the gender
	 */
	public static Gender getEnum(String value) {
		for (Gender v : values()) {
			if (v.getGender().equalsIgnoreCase(value)) {
				return v;
			}
		}
		throw new UnprocessableEntityException("Invalid gender type ");
	}

	/**
	 * To get list of gender type
	 * 
	 * @return gender type list
	 */
	public static List<String> getGenderTypes() {
		return Stream.of(values()).map(reel -> reel.getGender()).collect(Collectors.toList());
	}

}
