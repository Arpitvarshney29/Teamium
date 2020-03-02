/**
 * 
 */
package com.teamium.domain.output;

import java.util.Locale;

import com.teamium.domain.Company;


/**
 * Spécifies an entity which can me marshalled to editable entity
 * @author JS
 *
 */
public interface XmlOutputEntity{
	
	/**
	 * Returns the output header entity
	 * @return the sale entity
	 */
	public Company getOutputHeaderEntity();
	
	/**
	 * Returns the output localization
	 * @returns the locale
	 */
	public Locale getOutputLocale();
	
	/**
	 * Returns the path of output editions
	 * @returns the edition group
	 */
	public String getOutputPath();
}
