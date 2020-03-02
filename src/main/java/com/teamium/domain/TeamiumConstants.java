/**
 * 
 */
package com.teamium.domain;

/**
 * Centralize all constants used by teamium's modules
 * 
 * @author sraybaud - NovaRem
 *
 */
public interface TeamiumConstants {
	/**
	 * Persistence unit name
	 */
	public static final String PERSISTENCE_UNIT = "Teamium";

	/**
	 * Logger name
	 */
	public static final String LOGGER = "com.teamium.domain";

	/**
	 * XML namespace
	 */
	public static final String XMLNS = "http://www.softvallee.com/teamium/xml";

	/**
	 * Name of table which store the sequence to generate id of entities
	 */
	public static final String TeamiumSequenceTable = "t_teamium_seq_store";

	/**
	 * Name of table which store the sequence to generate id of entities
	 */
	public static final String TeamiumSequencePkColumnName = "teamium_seq_id";

	/**
	 * Name of table which store the sequence to generate id of entities
	 */
	public static final String TeamiumSequenceColumnValue = "teamium_seq_value";

	/**
	 * Name of the teamium security domain
	 */
	public static final String SECURITY_DOMAIN = "teamium";

	/**
	 * Constants for spreadsheet upload.
	 */

	public static final String STAFF_LAST_NAME = "Last Name";
	public static final String STAFF_FISRT_NAME = "First name";
	public static final String STAFF_NAME_PREFIX = "Prefix";
	public static final String STAFF_JOB_TITLE = "Job title";
	public static final String STAFF_IS_FREELANCE = "Freelance";
	public static final String STAFF_EMPLOYEE_CODE = "EMP Code";
	public static final String STAFF_FUNCTION = "Function";
	public static final String STAFF_EXPERTISE = "Expertise";
	public static final String STAFF_LANGUAGE = "Language";
	public static final String STAFF_GROUP = "Group";
	public static final String STAFF_ADDRESS = "Address";
	public static final String STAFF_ZIP_OR_POSTAL_CODE = "Zip/Postal code";
	public static final String STAFF_CITY = "City";
	public static final String STAFF_COUNTRY = "Country";
	public static final String STAFF_EMAIL = "Email";
	public static final String STAFF_GOOGLECAL = "GoogleCal";
	public static final String STAFF_SKYPE_ID = "Skype";
	public static final String STAFF_MOBILE_PHONE = "Mobile Phone";
	public static final String STAFF_PHONE = "Phone";
	public static final String STAFF_SOCIAL_SECURITY_NUMBER = "Social security number";
	public static final String STAFF_GENDER = "Gender";
	public static final String STAFF_BIRTH_DATE = "Birth date";
	public static final String STAFF_PLACE_OF_BIRTH = "Place of birth";
	public static final String STAFF_DPT = "Dpt";
	public static final String STAFF_COUNTRY_OF_BIRTH = "Country of birth";
	public static final String STAFF_CITIZENSHIP = "Citizenship";
	public static final String STAFF_PRESS_CARD_NUMBER = "Press card No";
	public static final String STAFF_TALENT_REGISTRATION = "Talent registration";
	public static final String STAFF_ROUTING_AND_ACCOUNT = "Routing# and Account#";
	public static final String STAFF_BIC = "BIC";
	public static final String STAFF_BANK_ADDRESS = "Bank address";
	public static final String STAFF_BENEFICIARY = "Beneficiary";
	public static final String STAFF_MARRIED_NAME = "Married name";
	public static final String STAFF_BIRTH_PROVINCE = "Birth province";
	public static final String STAFF_CONTRACT = "Contract";
	public static final String STAFF_PERFIX_MR = "M";
	public static final String STAFF_PERFIX_MS = "Ms";
	public static final String STAFF_PERFIX_MISS = "Miss";
	public static final String EQUIPMENT_NAME = "Equipment name";
	public static final String EQUIPMENT_TYPE = "Type";
	public static final String EQUIPMENT_SERIAL_NUMBER = "Serial number";
	public static final String EQUIPMENT_MODEL = "Model";
	public static final String EQUIPMENT_BRAND = "Brand";
	public static final String EQUIPMENT_REFRENCE = "Reference";
	public static final String EQUIPMENT_LOCATION = "Location";
	public static final String EQUIPMENT_DESCRIPTION = "Description";
	public static final String EQUIPMENT_LOCATION_DETAILS = "Location details";
	public static final String IMPORT_WARNING = "Some error occurs.Please see the logs.";
	public static final Integer ORDER_STATUS_LIMIT = 1;

	/**
	 * User's signature file name
	 */
	public static final String USER_SIGNATURE_IMAGE_NAME = "signature.txt";

	/**
	 * Teamium User Roles
	 */
	public static final String ROLE_ADMINISTRATOR = "administrator";
	public static final String ROLE_SUPPORT = "support";
	public static final String ROLE_PRODUCTION = "production";
	public static final String ROLE_PROGRAM = "program";
	public static final String ROLE_ACCOUNTANT = "accountant";
	public static final String ROLE_HUMAN_RESOURCE = "humanResource";
	public static final String ROLE_PARTTIME = "parttime";
	public static final String ROLE_SALES = "seller";
	public static final String ROLE_SALES_MGR = "sellerMgr";
	public static final String ROLE_PRODUCTION_MGR = "productionMgr";

	/**
	 * Function Types
	 */
	public static final String STAFF_FUNCTION_TYPE = "Personnel";
	public static final String EQUIPMENT_FUNCTION_TYPE = "Equipment";
	public static final String PROCESS_FUNCTION_TYPE = "Services";
	public static final String SUPPLY_FUNCTION_TYPE = "Supplier";
	public static final String DEFAULT_FUNCTION_TYPE = "function";
	public static final String RIGHT_FUNCTION_TYPE = "Copyright";
	public static final String FOLDER_FUNCTION_TYPE = "folder";
	public static final String RATE_TYPE_STANDARD = "Standard";
	public static final String RATE_TYPE_CLIENT = "Client";
	public static final String RATE_TYPE_VENDOR = "Vendor";
	public static final String RATE_TYPE_CLIENT_OR_VENDOR = "Client/Vendor";

	/**
	 * Maximum levels of parent for a function
	 */
	public static final int FUNCTION_FOLDER_MAX_LEVEL = 2;

}
