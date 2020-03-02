package com.teamium.enums;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.teamium.exception.UnprocessableEntityException;

/**
 * <p>
 * An enum class for AccountancyType
 * </p>
 *
 */
public enum AccountancyType {
	SALES_ACCOUNTING_CODE("Sales accounting code"), SALES_ANALYTICAL_COST_CENTER(
			"Sales analytical cost center"), SALES_ANALYTICAL_ACCOUNTING_CODE(
					"Sales analytical accounting code"), PROCUREMENT_ACCOUNT_COST(
							"Procurement account code"), PROCUREMENT_ANALYTICAL_COST_CENTER(
									"Procurement analytical cost center"), ASSET("Asset");
	// CAPITAL_COST("capital_cost"), ANALYTICAL_PURCHASE("analytic_purchase"),
	// PURCHASE("purchase"), SALE("sale"),
	// ANALYTIC_SALE_CENTER("analytic_sale_center"),
	// ANALYTIC_SALE_NATURE("analytic_sale_nature");

	private String accountancyType;

	/**
	 * @param accountancyType
	 */
	private AccountancyType(String accountancyType) {
		this.accountancyType = accountancyType;
	}

	/**
	 * @return the accountancyType
	 */
	public String getAccountancyType() {
		return accountancyType;
	}

	/**
	 * Method to get the enum
	 * 
	 * @param value
	 *            the String enum value
	 * 
	 * @return the AccountancyType
	 */
	public static AccountancyType getEnum(String value) {
		for (AccountancyType v : values()) {
			if (v.getAccountancyType().equalsIgnoreCase(value)) {
				return v;
			}
		}
		throw new UnprocessableEntityException("Invalid accounting code type ");
	}

	/**
	 * To get AccountancyType list
	 * 
	 * @return AccountancyType list
	 */
	public static List<String> getAccountancyTypes() {
		return Stream
				.of(values()).map(v -> v.getAccountancyType()).sorted((accountancyType1,
						accountancyType2) -> accountancyType1.toLowerCase().compareTo(accountancyType2.toLowerCase()))
				.collect(Collectors.toList());
	}

}
