package com.teamium.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.teamium.exception.UnprocessableEntityException;

/**
 * Enum class for various Project-Status
 * 
 * @author Teamium
 *
 */
public class ProjectStatus {

	/**
	 * The Enum of available ProjectStatusName.
	 */
	public enum ProjectStatusName {

		TO_DO("To Do"), IN_PROGRESS("In Progress"), DONE("Done");

		private String projectStatusTypeName;

		private ProjectStatusName(String projectStatusTypeName) {
			this.projectStatusTypeName = projectStatusTypeName;
		}

		/**
		 * Gets the projectStatusTypeName value.
		 *
		 * @return the projectStatusTypeName value
		 */
		public String getProjectStatusNameString() {
			return this.projectStatusTypeName;
		}

		/**
		 * Method to validate the project-status-type value.
		 * 
		 * @param value
		 * @return ProjectStatusTypeName
		 */
		public static ProjectStatusName getEnum(String value) {
			for (ProjectStatusName v : values()) {
				if (v.getProjectStatusNameString().equalsIgnoreCase(value)) {
					return v;
				}
			}
			throw new UnprocessableEntityException("Invalid project status.");
		}

		/**
		 * Method to get the list of project-status type.
		 * 
		 * @return the list of project-status type.
		 */
		public static List<String> getAllProjectStatusTypeName() {
			return Arrays.asList(values()).stream()
					.map(eqAttachmentTypeName -> eqAttachmentTypeName.getProjectStatusNameString())
					.sorted((status1, status2) -> status1.toLowerCase().compareTo(status2.toLowerCase()))
					.collect(Collectors.toList());
		}
	}
	

	/**
	 * The Enum of available Project-Financial-Status.
	 */
	public enum ProjectFinancialStatusName {

		APPROVED("Approved"), REJECTED("Rejected"), REVISED("Revised");

		private String projectFinancialStatusName;

		private ProjectFinancialStatusName(String projectFinancialStatusName) {
			this.projectFinancialStatusName = projectFinancialStatusName;
		}

		/**
		 * Gets the projectFinancialStatusName value.
		 *
		 * @return the projectFinancialStatusName value
		 */
		public String getProjectFinancialStatusNameString() {
			return this.projectFinancialStatusName;
		}

		/**
		 * Validate the staff-attachment-type value.
		 * 
		 * @param value
		 * @return StaffAttachmentTypeName
		 */
		public static ProjectFinancialStatusName getEnum(String value) {
			for (ProjectFinancialStatusName v : values()) {
				if (v.getProjectFinancialStatusNameString().equalsIgnoreCase(value)) {
					return v;
				}
			}
			throw new UnprocessableEntityException("Invalid project financial status.");
		}

		/**
		 * Get the list of staff-attachment type.
		 * 
		 * @return List of staff-attachment type.
		 */
		public static List<String> getAllProjectFinancialStatusTypeName() {
			return Arrays.asList(values()).stream().map(
					projectFinancialStatusName -> projectFinancialStatusName.getProjectFinancialStatusNameString())
					.sorted((financialStatus1, financialStatus2) -> financialStatus1.toLowerCase()
							.compareTo(financialStatus2.toLowerCase()))
					.collect(Collectors.toList());
		}
	}

}
