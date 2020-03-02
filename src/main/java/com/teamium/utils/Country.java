package com.teamium.utils;

/**
 * The country class
 * 
 * @author Hansraj
 *
 */
public class Country {

	private String code;
	private String name;

	/**
	 * @param code
	 * @param name
	 */
	public Country(String code, String name) {
		this.code = code;
		this.name = name;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
