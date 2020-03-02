package com.teamium.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.teamium.exception.UnprocessableEntityException;

public class PersonalExpensesStatus {
	/**
	 * The Enum of available PersonalExpensesStatusName.
	 */
	public enum PersonalExpensesStatusName {

		CREATED("Created"), SUBMITTED("Submitted"), APPROVED("Approved"), REVISED("Revised");

		private String personalExpensesStatusTypeName;

		private PersonalExpensesStatusName(String personalExpensesStatusTypeName) {
			this.personalExpensesStatusTypeName = personalExpensesStatusTypeName;
		}

		/**
		 * Gets the personalExpensesStatusTypeName value.
		 *
		 * @return the personalExpensesStatusTypeName value
		 */
		public String getPersonalExpensesStatusName() {
			return this.personalExpensesStatusTypeName;
		}

		/**
		 * Method to validate the -personal-expenses-status-type value.
		 * 
		 * @param value
		 * @return personalExpensesStatusTypeName
		 */
		public static PersonalExpensesStatusName getEnum(String value) {
			for (PersonalExpensesStatusName v : values()) {
				if (v.getPersonalExpensesStatusName().equalsIgnoreCase(value)) {
					return v;
				}
			}
			throw new UnprocessableEntityException("Invalid personal expenses status.");
		}

		/**
		 * Method to get the list of personal-expenses-status-type.
		 * 
		 * @return the list of personal-expenses-status-type.
		 */
		public static List<String> getAllPersonalExpensesStatusTypeName() {
			return Arrays.asList(values()).stream()
					.map(eqAttachmentTypeName -> eqAttachmentTypeName.getPersonalExpensesStatusName())
					.sorted((status1, status2) -> status1.toLowerCase().compareTo(status2.toLowerCase()))
					.collect(Collectors.toList());
		}
	}

}
