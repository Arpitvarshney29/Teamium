package com.teamium.enums.project;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.teamium.exception.UnprocessableEntityException;

/**
 * <p>
 * An enum class for ExpertiseType .
 * </p>
 *
 */
public enum ExpertiseType {

	TWO_D("2D"), THREE_D("3D"), TWO_D_THREE_D("2D/3D"), GENERALIST("Generalist"), MAYA("MAYA"), AUTHOR(
			"Author"), ANIMATOR("Animator"), NUKE("Nuke"), FX("FX"), PHOTOSHOP("Photoshop"), SCRIPT("Script"), AFTER_FX(
					"After FX"), RENDERING("Rendering"), MATTE_PAINT("Matte Paint"), MC("MC"), SOUND_EDITOR(
							"Sound Editor"), MODELER("Modeler"), FCP("FCP"), PRINT("Print"), RENDER("Render");
	private String expertiseType;

	/**
	 * Parameterized ExpertiseType constructor
	 * 
	 * @param expertiseType
	 *            the expertiseType
	 */

	private ExpertiseType(String expertiseType) {
		this.expertiseType = expertiseType;
	}

	/**
	 * @return the expertiseType
	 */
	public String getExpertiseType() {
		return expertiseType;
	}

	/**
	 * Validate the ExpertiseType type value.
	 * 
	 * @param value
	 * @return ExpertiseType
	 */
	public static ExpertiseType getEnum(String value) {
		for (ExpertiseType v : values()) {
			if (v.getExpertiseType().equalsIgnoreCase(value)) {
				return v;
			}
		}
		throw new UnprocessableEntityException("Invalid ExpertiseType Name");
	}

	/**
	 * To get list of expertiseType
	 * 
	 * @return list of ExpertiseType
	 */
	public static List<String> getExpertiseTypes() {
		return Stream.of(values()).map(expert -> expert.getExpertiseType())
				.sorted((skill1, skill2) -> skill1.toLowerCase().compareTo(skill2.toLowerCase()))
				.collect(Collectors.toList());
	}

}
