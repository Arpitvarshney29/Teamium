package com.teamium.enums.AssignationType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.teamium.exception.UnprocessableEntityException;

/**
 * <p>
 * An enum class for AssignationType either INTERNAL, EXTERNAL or FREELANCE.
 * </p>
 *
 */
public enum AssignationType {
	INTERNAL("internal"), EXTERNAL("external"), FREELANCE("freelance");
	private String assignType;

	/**
	 * Parameterized FormatType constructor
	 * 
	 * @param assignType the assignType
	 */

	private AssignationType(String assignType) {
		this.assignType = assignType;
	}

	/**
	 * @return the assignType
	 */
	public String getAssignType() {
		return assignType;
	}

	/**
	 * @param assignType the assignType to set
	 */
	public void setAssignType(String assignType) {
		this.assignType = assignType;
	}

	/**
	 * Method to get the enum
	 * 
	 * @param value the String enum value
	 * 
	 * @return the AssignationType
	 */
	public static AssignationType getEnum(String value) {
		for (AssignationType v : values()) {
			if (v.getAssignType().equalsIgnoreCase(value)) {
				return v;
			}
		}
		throw new UnprocessableEntityException("Invalid origine type ");
	}

	/**
	 * To get list of AssignationType
	 * 
	 * @return AssignationType list
	 */
	public static List<String> geAssignationTypes() {
		return Stream.of(values()).map(v -> v.getAssignType()).sorted().collect(Collectors.toList());
	}

}
