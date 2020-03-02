package com.teamium.enums;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.teamium.exception.UnprocessableEntityException;

/**
 * <p>
 * An enum class for ReelType
 * </p>
 *
 */
public enum ReelType {

	REEL("Reel"), PHOTO("Photo"), RESUME("Resume"), BIO("Bio"), PRESS("Press Release");

	private String reelType;

	/**
	 * @param reelType
	 */
	private ReelType(String reelType) {
		this.reelType = reelType;
	}

	/**
	 * @return the reelType
	 */
	public String getReelType() {
		return reelType;
	}

	/**
	 * Method to get the enum
	 * 
	 * @param value the String enum value
	 * 
	 * @return the ReelType
	 */
	public static ReelType getEnum(String value) {
		for (ReelType v : values()) {
			if (v.getReelType().equalsIgnoreCase(value)) {
				return v;
			}
		}
		throw new UnprocessableEntityException("Invalid reel type ");
	}

	/**
	 * To get list of reel type
	 * 
	 * @return reel type list
	 */
	public static List<String> getReelTypes() {
		return Stream.of(values()).map(reel -> reel.getReelType()).sorted().collect(Collectors.toList());
	}

}
