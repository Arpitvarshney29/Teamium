package com.teamium.domain.utils;

/**
 * Interface for converting numbers to letters
 * @author JS
 *
 */
public interface NumberToLetter {

	public static final String CLASSPATH = "com.teamium.domain.utils.NumberToLetter_";
	
	/**
	 * The convert method
	 * @param number - the number to convert
	 * @return  the number in letters
	 */
	public String convert(long number);
	
}
