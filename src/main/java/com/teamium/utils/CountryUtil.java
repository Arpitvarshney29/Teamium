package com.teamium.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.teamium.exception.UnprocessableEntityException;

/**
 * The utility class to manage country
 * 
 * @author Hansraj
 *
 */
public class CountryUtil {

	private static CountryUtil instance = null;

	private Map<String, String> country = new HashMap<String, String>();
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private CountryUtil(HashMap<String, String> country) {
		this.country = country;
	}

	/**
	 * To get Instance of CountryUtil
	 * 
	 * @return CountryUtil
	 */
	public static synchronized CountryUtil getInstance() {
		if (instance == null) {
			HashMap<String, String> country = new HashMap<String, String>();
			for (String code : Locale.getISOCountries()) {
				Locale locale = new Locale("en", code);
				country.put(code, locale.getDisplayCountry());
			}
			instance = new CountryUtil(country);
		}
		return instance;
	}

	/**
	 * To get list of countries names
	 * 
	 * @return list of country names
	 */
	public List<String> getCountryNames() {
		return country.values().stream()
				.sorted((country1, country2) -> country1.toLowerCase().compareTo(country2.toLowerCase()))
				.collect(Collectors.toList());
	}

	/**
	 * To get set of countries
	 * 
	 * @return Get set of countries
	 */
	public Set<Country> getCountries() {
		logger.info("Inside CountryUtil::getCountries()");
		Set<Country> countries = new HashSet<>();
		for (Entry<String, String> entry : country.entrySet()) {
			countries.add(new Country(entry.getKey(), entry.getValue()));
		}
		logger.info(countries.size() + " : countries found");
		logger.info("Retuning from getCountries() .");
		return countries;
	}

	/**
	 * To get country by name.
	 * 
	 * @param name.
	 * 
	 * @return the Country.
	 */
	public Country getCountry(String name) {
		logger.info("Inside CountryUtil::getCountry().");
		if (name == null) {
			logger.info("Please enter valid country name.");
			throw new UnprocessableEntityException("Please enter valid country name.");
		}
		name = name.trim();
		for (Entry<String, String> entry : country.entrySet()) {
			if (entry.getValue().equalsIgnoreCase(name)) {
				logger.info("Returning from getCountry().");
				return new Country(entry.getKey(), entry.getValue());
			}
		}
		logger.info("Invalid country : " + name);
		throw new UnprocessableEntityException("Invalid Country");
	}

	/**
	 * To get country name by code.
	 * 
	 * @param code.
	 * 
	 * @return the country name.
	 */
	public String getNameByCode(String code) {
		return country.get(code);
	}

}
