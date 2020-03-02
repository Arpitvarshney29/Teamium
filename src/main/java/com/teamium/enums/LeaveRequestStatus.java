package com.teamium.enums;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.teamium.exception.UnprocessableEntityException;

public enum LeaveRequestStatus {

	REQUEST("Request"), APPROVED("Approved"), REJECTED("Rejected");

	private String status;

	/**
	 * @param status
	 */
	private LeaveRequestStatus(String conventionType) {
		this.status = conventionType;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Method to get the status enum
	 * 
	 * @param value
	 *            the String enum value
	 * 
	 * @return the status
	 */
	public static LeaveRequestStatus getEnum(String value) {
		for (LeaveRequestStatus v : values()) {
			if (v.getStatus().equalsIgnoreCase(value)) {
				return v;
			}
		}
		throw new UnprocessableEntityException("Invalid Leave Status type ");
	}

	/**
	 * To get list of status type
	 * 
	 * @return status type list
	 */
	public static List<String> getLeaveStatus() {
		return Stream.of(values()).map(leave -> leave.getStatus()).collect(Collectors.toList());
	}

}
