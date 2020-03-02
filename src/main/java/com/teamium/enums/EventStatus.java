package com.teamium.enums;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.teamium.exception.UnprocessableEntityException;

public enum EventStatus {

	REQUESTED("Requested"), CONFIRMED("Confirmed"), AWAITING_CONFIRMATION("Awaiting Confirmation"), ON_HOLD(
			"On Hold"), PENCILED("Penciled");

	private String eventStatus;

	/**
	 * @param gender
	 */
	private EventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	/**
	 * @return the eventStatus
	 */
	public String getEventStatus() {
		return eventStatus;
	}

	/**
	 * Method to get the gender enum
	 * 
	 * @param value
	 *            the String enum value
	 * 
	 * @return the gender
	 */
	public static EventStatus getEnum(String value) {
		for (EventStatus v : values()) {
			if (v.getEventStatus().equalsIgnoreCase(value)) {
				return v;
			}
		}
		throw new UnprocessableEntityException("Invalid Event status type");
	}

	/**
	 * To get list of gender type
	 * 
	 * @return gender type list
	 */
	public static List<String> getAllEventStatus() {
		return Stream.of(values()).map(status -> status.getEventStatus()).collect(Collectors.toList());
	}
}
