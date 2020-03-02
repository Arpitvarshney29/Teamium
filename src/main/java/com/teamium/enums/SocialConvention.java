package com.teamium.enums;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.teamium.exception.UnprocessableEntityException;

public enum SocialConvention {

	TECHNICIAN("Technician"), JOURNALIST("Journalist"), TVBROADCASTER("TV Broadcaster");
	
	private String conventionType;

	/**
	 * @param conventionType
	 */
	private SocialConvention(String conventionType) {
		this.conventionType = conventionType;
	}

	/**
	 * @return the conventionType
	 */
	public String getConventionType() {
		return conventionType;
	}

	/**
	 * Method to get the conventionType enum
	 * 
	 * @param value
	 *            the String enum value
	 * 
	 * @return the convention
	 */
	public static SocialConvention getEnum(String value) {
		for (SocialConvention v : values()) {
			if (v.getConventionType().equalsIgnoreCase(value)) {
				return v;
			}
		}
		throw new UnprocessableEntityException("Invalid convention type ");
	}

	/**
	 * To get list of convention type
	 * 
	 * @return convention type list
	 */
	public static List<String> getConventionTypes() {
		return Stream.of(values()).map(reel -> reel.getConventionType())
				.sorted((socialConvention1, socialConvention2) -> socialConvention1.toLowerCase()
						.compareTo(socialConvention2.toLowerCase()))
				.collect(Collectors.toList());
	}

}
