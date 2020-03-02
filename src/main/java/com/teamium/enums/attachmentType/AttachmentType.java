package com.teamium.enums.attachmentType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.teamium.exception.UnprocessableEntityException;

/**
 * Enum class for various attachment-types
 * 
 * @author Teamium
 *
 */
public class AttachmentType {

	/**
	 * The Enum of available EquipmentType.
	 */
	public enum EquipmentAttachmentTypeName {

		Technical_Specification("Technical Specification"), User_Manual("User Manual");

		private String equipmentAttachmentTypeName;

		private EquipmentAttachmentTypeName(String equipmentAttachmentTypeName) {
			this.equipmentAttachmentTypeName = equipmentAttachmentTypeName;
		}

		/**
		 * Gets the equipmentAttachmentTypeName value.
		 *
		 * @return the equipmentAttachmentTypeName value
		 */
		public String getEquipmentAttachmentNameString() {
			return this.equipmentAttachmentTypeName;
		}

		/**
		 * Validate the equipment-attachment-type value.
		 * 
		 * @param value
		 * @return EquipmentAttachmentTypeName
		 */
		public static EquipmentAttachmentTypeName getEnum(String value) {
			for (EquipmentAttachmentTypeName v : values()) {
				if (v.getEquipmentAttachmentNameString().equalsIgnoreCase(value)) {
					return v;
				}
			}
			throw new UnprocessableEntityException("Invalid AttachmentType Name.");
		}

		/**
		 * Get the list of equipment-attachment type.
		 * 
		 * @return List of equipment-attachment type.
		 */
		public static List<String> getAllAttachmentType() {
			return Arrays.asList(values()).stream()
					.map(eqAttachmentTypeName -> eqAttachmentTypeName.getEquipmentAttachmentNameString())
					.sorted((attachment1, attachment2) -> attachment1.toLowerCase()
							.compareTo(attachment2.toLowerCase()))
					.collect(Collectors.toList());
		}
	}

	/**
	 * The Enum of available StaffType.
	 */
	public enum StaffAttachmentTypeName {

		Technical_Specification("Technical Specification"), User_Manual("User Manual");

		private String staffAttachmentTypeName;

		private StaffAttachmentTypeName(String staffAttachmentTypeName) {
			this.staffAttachmentTypeName = staffAttachmentTypeName;
		}

		/**
		 * Gets the staffAttachmentTypeName value.
		 *
		 * @return the staffAttachmentTypeName value
		 */
		public String getStaffAttachmentTypeNameString() {
			return this.staffAttachmentTypeName;
		}

		/**
		 * Validate the staff-attachment-type value.
		 * 
		 * @param value
		 * @return StaffAttachmentTypeName
		 */
		public static StaffAttachmentTypeName getEnum(String value) {
			for (StaffAttachmentTypeName v : values()) {
				if (v.getStaffAttachmentTypeNameString().equalsIgnoreCase(value)) {
					return v;
				}
			}
			throw new UnprocessableEntityException("Invalid AttachmentType Name.");
		}

		/**
		 * Get the list of staff-attachment type.
		 * 
		 * @return List of staff-attachment type.
		 */
		public static List<String> getAllAttachmentType() {
			return Arrays.asList(values()).stream()
					.map(staffAttachmentTypeName -> staffAttachmentTypeName.getStaffAttachmentTypeNameString())
					.sorted((attachment1, attachment2) -> attachment1.toLowerCase()
							.compareTo(attachment2.toLowerCase()))
					.collect(Collectors.toList());
		}
	}

}
