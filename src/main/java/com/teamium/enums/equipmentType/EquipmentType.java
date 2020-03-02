package com.teamium.enums.equipmentType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.teamium.exception.UnprocessableEntityException;

public class EquipmentType {

	/**
	 * The Enum of available EquipmentType.
	 */
	public enum EquipmentTypeName {

		HW("HW"), SW("SW"), Infrastructure("Infrastructure"), Truck("Truck");

		private String equipmentTypeName;

		private EquipmentTypeName(String equipmentTypeName) {
			this.equipmentTypeName = equipmentTypeName;
		}

		/**
		 * Gets the equipment Type value.
		 *
		 * @return the equipmentType value
		 */
		public String getEquipmentNameString() {
			return this.equipmentTypeName;
		}

		/**
		 * Validate equipment type value.
		 * 
		 * @param value
		 * @return EquipmentTypeName.
		 */
		public static EquipmentTypeName getEnum(String value) {
			for (EquipmentTypeName v : values()) {
				if (v.getEquipmentNameString().equalsIgnoreCase(value)) {
					return v;
				}
			}
			throw new UnprocessableEntityException("Invalid EquipmentType Name.");
		}

		/**
		 * Get all Equipment type value.
		 * 
		 * @return list of Equipment type value.
		 */
		public static List<String> getAllEquipmentType() {
			return Arrays.asList(values()).stream().map(equipmentTypeName -> equipmentTypeName.getEquipmentNameString())
					.sorted((type1, type2) -> type1.toLowerCase().compareTo(type2.toLowerCase()))
					.collect(Collectors.toList());
		}
	}
}
