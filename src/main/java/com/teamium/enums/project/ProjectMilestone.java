package com.teamium.enums.project;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.teamium.exception.UnprocessableEntityException;

/**
 * <p>
 * An enum  for ProjectMilestone .
 * </p>
 *
 */
public enum ProjectMilestone {

	CALL_TIME("CallTime"), GMT("GMT"), PRODUCTION("Production"), POSTPROD("Postprod"), MIX("Mix"),
	NOTIFICATION("Notification"), VALIDATION("Validation"), DELIVERY("Delivery"), ON_AIR("OnAir");
	private String type;

	/**
	 * @param type
	 */
	private ProjectMilestone(String type) {
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
	 * @return the ProjectMilestone
	 */
	public static ProjectMilestone getEnum(String value) {
		for (ProjectMilestone v : values()) {
			if (v.getType().equalsIgnoreCase(value)) {
				return v;
			}
		}
		throw new UnprocessableEntityException("Invalid Milestone type.");
	}

	/**
	 * To get list of milestone types
	 * 
	 * @return ProjectMilestones type list
	 */
	public static List<String> getMilestoneTypes() {
		return Stream.of(values()).map(reel -> reel.getType()).sorted().collect(Collectors.toList());
	}

}
