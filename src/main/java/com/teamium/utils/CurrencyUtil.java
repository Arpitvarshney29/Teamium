package com.teamium.utils;

import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.teamium.dto.CurrencyDTO;
import com.teamium.exception.UnprocessableEntityException;

/**
 * The utility class to manage Currency
 * 
 * @author Teamium
 *
 */
public class CurrencyUtil {

	private static CurrencyUtil instance = null;

	private Map<String, String> currency = new HashMap<String, String>();
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private CurrencyUtil(Map<String, String> currency) {
		this.currency = currency;
	}

	/**
	 * To get Instance of currencyUtil
	 * 
	 * @return currencyUtil
	 */
	public static synchronized CurrencyUtil getInstance() {
		if (instance == null) {
			Map<String, String> currencyInstance = getAvailableCurrencies();
			instance = new CurrencyUtil(currencyInstance);
		}
		return instance;
	}

	/**
	 * To get list of countries names
	 * 
	 * @return list of currency names
	 */
	public List<String> getCurrencyNames() {
		return currency.values().stream()
				.sorted((currency1, currency2) -> currency1.toLowerCase().compareTo(currency2.toLowerCase()))
				.collect(Collectors.toList());
	}

	/**
	 * To get currency by name.
	 * 
	 * @param name.
	 * 
	 * @return the currency.
	 */
	public CurrencyDTO getCurrency(String name) {
		logger.info("Inside CurrencyUtil :: getCurrency().");
		if (StringUtils.isBlank(name)) {
			logger.info("Please enter valid currency name.");
			throw new UnprocessableEntityException("Please enter valid currency name.");
		}
		name = name.trim();
		for (Entry<String, String> entry : currency.entrySet()) {
			if (entry.getValue().equalsIgnoreCase(name)) {
				logger.info("Returning from getcurrency().");
				return new CurrencyDTO(entry.getKey(), entry.getValue());
			}
		}
		logger.info("Invalid currency : " + name);
		throw new UnprocessableEntityException("Invalid currency");
	}

	/**
	 * To get currency name by code.
	 * 
	 * @param code.
	 * 
	 * @return the currency name.
	 */
	public String getNameByCode(String code) {
		return currency.get(code);
	}

	/**
	 * Get the currencies code from the available locales information.
	 *
	 * @return a map of currencies code.
	 */
	public static Map<String, String> getAvailableCurrencies() {
		Locale[] locales = Locale.getAvailableLocales();
		Map<String, String> currencies = new TreeMap<String, String>();
		for (Locale locale : locales) {
			try {
				currencies.put(Currency.getInstance(locale).getCurrencyCode(),
						Currency.getInstance(locale).getSymbol());
			} catch (Exception e) {

			}
		}
		return currencies;
	}

}
