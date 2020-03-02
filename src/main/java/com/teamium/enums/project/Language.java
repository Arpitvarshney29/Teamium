package com.teamium.enums.project;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.teamium.exception.UnprocessableEntityException;

/**
 * <p>
 * An enum class for Language .
 * </p>
 *
 */
public enum Language {
	French("French"), English("English"), Spanish("Spanish"), Italian("Italian"), German("German"), Chinese(
			"Chinese"), Russian("Russian"), Arabian("Arabian");
	private String language;

	/**
	 * @param language
	 */
	private Language(String language) {
		this.language = language;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language
	 *            the language to set
	 * @return Language
	 */
	public static Language getEnum(String value) {
		for (Language v : values()) {
			if (v.getLanguage().equalsIgnoreCase(value)) {
				return v;
			}
		}
		throw new UnprocessableEntityException("Invalid Language :" + value);
	}

	/**
	 * Get the list of languages .
	 * 
	 * @return list of language
	 */
	public static List<String> getLanguages() {
		return Stream.of(values()).map(v -> v.getLanguage()).sorted((lang1, lang2) -> lang1.toLowerCase().compareTo(lang2.toLowerCase()))
				.collect(Collectors.toList());
	}

}
