package com.teamium.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.teamium.config.PropConfig;
import com.teamium.constants.Constants;
import com.teamium.domain.Address;
import com.teamium.domain.Channel;
import com.teamium.domain.Company;
import com.teamium.domain.Person;
import com.teamium.domain.TeamiumException;
import com.teamium.domain.Vat;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.resources.contacts.Customer;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.domain.prod.upload.log.ImportFor;
import com.teamium.domain.prod.upload.log.SpreadsheetUploadLog;
import com.teamium.dto.ChannelDTO;
import com.teamium.dto.CurrencyDTO;
import com.teamium.dto.CustomerDTO;
import com.teamium.dto.CustomerDropdownDTO;
import com.teamium.dto.QuotationDTO;
import com.teamium.dto.SpreadsheetMessageDTO;
import com.teamium.dto.prod.resources.functions.RateDTO;
import com.teamium.enums.AccountancyType;
import com.teamium.enums.CustomerDomainType;
import com.teamium.enums.RateUnitType;
import com.teamium.enums.project.Language;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.CustomerRepository;
import com.teamium.repository.PersonRepository;
import com.teamium.repository.SpreadsheetUploadLogRepository;
import com.teamium.service.prod.resources.functions.RateService;
import com.teamium.utils.CommonUtil;
import com.teamium.utils.CountryUtil;

/**
 * A service class implementation for customer controller
 *
 */

@Service
public class CustomerService {

	private CustomerRepository customerRepository;
	private RateService rateService;
	private ChannelService channelService;

	private PersonRepository<Person> personReposiotry;
	@Autowired
	@Lazy
	private RecordService recordService;

	private PropConfig propConfig;

	private SpreadsheetUploadLogRepository spreadsheetUploadLogRepository;
	private AuthenticationService authenticationService;
	private CurrencyService currencyService;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * @param customerRepository
	 * @param rateService
	 * @param channelService
	 * @param personReposiotry
	 * @param propConfig
	 * @param spreadsheetUploadLogRepository
	 * @param authenticationService
	 */
	public CustomerService(CustomerRepository customerRepository, RateService rateService,
			ChannelService channelService, PersonRepository<Person> personReposiotry, PropConfig propConfig,
			SpreadsheetUploadLogRepository spreadsheetUploadLogRepository, AuthenticationService authenticationService,
			CurrencyService currencyService) {
		this.customerRepository = customerRepository;
		this.rateService = rateService;
		this.channelService = channelService;
		this.personReposiotry = personReposiotry;
		this.propConfig = propConfig;
		this.spreadsheetUploadLogRepository = spreadsheetUploadLogRepository;
		this.authenticationService = authenticationService;
		this.currencyService = currencyService;
	}

	/**
	 * To get list of customers
	 * 
	 * @return list of CustomerDTO
	 */
	public List<CustomerDTO> getCustomers() {

		logger.info("Inside getCompanies().");
		List<CustomerDTO> customers = customerRepository.findAll().stream().map(cus -> {
			CustomerDTO customerD = new CustomerDTO(cus);
			customerD.setRateCard(rateService.findRatesByCompany(cus.getId()));
			customerD.setTotalProjects(recordService.getTotalCompanyProjects(cus.getId()));
			customerD.setProjects(recordService.getBudgetByCompany(customerD.getId()));
			return customerD;
		}).collect(Collectors.toList());
		logger.info(customers.size() + " Customers found ");
		logger.info("Returning from getCustomers()");
		return customers;
	}

	/**
	 * To get customer by customerId .
	 * 
	 * @param customerId the customerId.
	 * 
	 * @return the CustomerDTO.
	 */
	public CustomerDTO getCustomer(Long customerId) {
		logger.info("Inside getCustomer() :: getting customer with id: " + customerId);
		CustomerDTO customerDto = new CustomerDTO(findCustomerById(customerId));
		List<RateDTO> rates = rateService.findRatesByCompany(customerId);
		customerDto.setRateCard(rates);
		logger.info("Returning from getCustomer() .");
		return customerDto;
	}

	/**
	 * To find customer by id.
	 * 
	 * @param customerId the customerId.
	 * 
	 * @return the Customer.
	 */
	public Customer findCustomerById(long customerId) {
		logger.info("Inside  findCustomerById()  :: finding customer with id " + customerId);
		Customer customer = customerRepository.findOne(customerId);
		if (customer == null) {
			logger.error("Invalid customer id.");
			throw new NotFoundException("Invalid customer id.");
		}
		logger.info("Returning from findCustomerById() .");
		return customer;
	}

	/**
	 * To delete customer.
	 * 
	 * @param customerId the customerId.
	 */
	public void deleteCustomer(long customerId) {
		logger.info("Inside deleteCustomer() :: deleting customer with id: " + customerId);
		findCustomerById(customerId);
		try {
			customerRepository.delete(customerId);
		} catch (DataIntegrityViolationException ex) {
			logger.error("Can't delete this client :: " + ex.getMessage());
			throw new UnprocessableEntityException("Can't delete this client ::  ");
		}

		logger.info("Returning from deleteCustomer().");
	}

	/**
	 * To save the CustomerDTO.
	 * 
	 * @param DTO the DTO object.
	 * @return
	 */
	public CustomerDTO saveOrUpdateCustomer(CustomerDTO customerDTO) {
		logger.info("Inside saveOrUpdateCustomer() :: save/update customer : " + customerDTO);
		Customer customer;

		// validate the name of the client, the name must be unique
		if (StringUtils.isBlank(customerDTO.getName())) {
			logger.info("Please provide valid name");
			throw new UnprocessableEntityException("Please provide valid name");
		}
		Customer validateCustomerName = customerRepository.findByNameIgnoreCase(customerDTO.getName());
		if (customerDTO.getId() == null && validateCustomerName != null) {
			logger.info("Client name already exists.");
			throw new UnprocessableEntityException("Client name already exists");
		}
		if (customerDTO.getId() != null && validateCustomerName != null
				&& customerDTO.getId().longValue() != validateCustomerName.getId().longValue()) {
			logger.info("Client name already exists.");
			throw new UnprocessableEntityException("Client name already exists");
		}
		if (StringUtils.isBlank(customerDTO.getCurrency())) {
			customerDTO.setCurrency(currencyService.getDefaultCurrency().getCode());
		}
		if (customerDTO.getId() == null) {
			customer = customerDTO.getCustomer();
		} else {
			customer = findCustomerById(customerDTO.getId());
			customer = customerDTO.getCustomer(customer);
		}
		if (customerDTO.getChannels() != null) {
			List<Channel> channels = customerDTO.getChannels().stream().filter(c -> c.getId() != null)
					.map(c -> channelService.findChannelById(c.getId())).collect(Collectors.toList());
			customer.setChannels(channels);
		}

		customer = customerRepository.save(customer);

		logger.info("Returning from saveOrUpdateCustomer() .");
		return new CustomerDTO(customer);
	}

	/**
	 * To get customer drop down data.
	 * 
	 * @return the CustomerDropdownDTO
	 */
	public CustomerDropdownDTO getCustomerDropdowns() {
		logger.info("inside getCustomerDropdowns() :");
		Map<Long, List<QuotationDTO>> projects = new HashMap<Long, List<QuotationDTO>>();
//		for(Customer customer:customerRepository.findAll()) {
//			projects.put(customer.getId(), recordService.getProjectInforByCompany(customer.getId()));
//		}

		List<CurrencyDTO> currencyList = currencyService.getAllCurrencyByActive(Boolean.TRUE);
		List<String> languagesList = Language.getLanguages();
		List<String> formats = CommonUtil.getProjectFormats();
		List<ChannelDTO> channels = channelService.getChannels();
		List<String> basis = RateUnitType.getRateUnitTypes();
		List<String> countryList = CountryUtil.getInstance().getCountryNames();
		List<String> accountingCodes = AccountancyType.getAccountancyTypes();
		List<String> cities = customerRepository.getClientCities();
		CustomerDropdownDTO dropdown = new CustomerDropdownDTO(currencyList, languagesList, channels, countryList,
				accountingCodes, formats, CustomerDomainType.getDomainTypes());
		dropdown.setBasis(basis);
//		dropdown.setProjects(projects);
		dropdown.setCities(cities);
		logger.info("Returning from getCustomerDropdowns() .");
		return dropdown;
	}

	/**
	 * To upload spreadsheet data for staff
	 * 
	 * @return
	 * 
	 * @throws InvalidFormatException
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws TeamiumException
	 */
	public SpreadsheetMessageDTO upload(MultipartFile file) throws InvalidFormatException, IOException {
		logger.info("Inside upload: going to import data for staff by spreadsheet.");

		StaffMember loggedInUser = authenticationService.getAuthenticatedUser();
		if (!authenticationService.isHumanResource()) {
			logger.warn("A invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to import staff spreadsheet.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}

		Date currentDate = new Date();
		String dateString = new SimpleDateFormat("yyyyMMddHHmm").format(currentDate);
		Calendar calander = Calendar.getInstance();
		calander.setTime(currentDate);

		SpreadsheetUploadLog spreadsheetUploadLog = new SpreadsheetUploadLog();

		boolean hasError = false;

		String fileExtention = null;

		String relativePath = "";
		String absolutePath = "";
		URL url = null;
		String logFileName = null;

		if (file == null) {
			logger.info("Invalid file");
			throw new NotFoundException("No File founds");
		}
		StringBuffer logDetails = new StringBuffer("Upload Started :: ");
		String fileName = file.getOriginalFilename();
		logger.info("Importing " + fileName);

		fileExtention = FilenameUtils.getExtension(file.getOriginalFilename());

		Map<String, String> numbers = new HashMap<>();
		AtomicInteger index = new AtomicInteger(0);
		Map<String, Integer> indexValue = new HashMap<String, Integer>();
		Map<String, Integer> contactIndexMap = new HashMap<String, Integer>();
		int rowNumber = 0;
		int corruptedRowNumber = 0;

		String extension = fileExtention.toUpperCase();
		switch (extension) {

		case "XLSX":
		case "XLS":
			Workbook workbook = WorkbookFactory.create(file.getInputStream());
			Workbook errorWorkbook = new XSSFWorkbook();
			Sheet sheet = errorWorkbook.createSheet("client " + Constants.INVALID_SPREADSHEET_NAME);
			Sheet contactSheet = errorWorkbook.createSheet("contact " + Constants.INVALID_SPREADSHEET_NAME);
			Sheet spreadsheet = workbook.getSheetAt(0);
			Sheet contactSpreadsheet = workbook.getSheetAt(1);
			index = createXLHeaders(spreadsheet, indexValue, index);
			createXLHeaders(contactSpreadsheet, contactIndexMap, new AtomicInteger(0));
			addXlsRow(sheet, spreadsheet.getRow(0), 0, Constants.ERROR_FIELD);
			addXlsRow(contactSheet, contactSpreadsheet.getRow(0), 0, Constants.ERROR_FIELD);
			spreadsheet.removeRow(spreadsheet.getRow(0));
			contactSpreadsheet.removeRow(contactSpreadsheet.getRow(0));
			Map<String, List<Person>> contacts = new HashMap<String, List<Person>>();
			if (contactIndexMap.size() < Constants.CONTACT_HEADERS) {
				logger.info("Invalid contact headers");
				throw new NotFoundException("Invalid contact headers");
			}
			contacts = createContactsData(contactSpreadsheet, contactIndexMap, contacts);

			for (Row row : spreadsheet) {
				hasError = false;
				++rowNumber;
				if (!StringUtils.isBlank(getValue(row.getCell(0))) && indexValue.get(Constants.CLIENT_NAME) != null
						&& !getValue(row.getCell(indexValue.get(Constants.CLIENT_NAME))).isEmpty()) {

					String companyName = getValue(row.getCell(indexValue.get(Constants.CLIENT_NAME)));
					if (!StringUtils.isBlank(companyName)) {
						Company savedCompany = customerRepository.findByNameIgnoreCase(companyName);
						if (savedCompany != null) {
							// make a log for duplicate employee-code
							hasError = true;
							logDetails.append("Duplicate comany Name : " + companyName
									+ " is duplicate on row number : " + rowNumber);
							++corruptedRowNumber;
							addXlsRow(sheet, row, corruptedRowNumber,
									Constants.CLIENT_NAME + " : " + companyName + " is duplicate, company-name");
							continue;
						}
					}

					String category = getValue(row.getCell(indexValue.get(Constants.COMPANY_CATEGORY)));
					String telephone = getValue(row.getCell(indexValue.get(Constants.COMPANY_TELEPHONE)));
					String addressLine = getValue(row.getCell(indexValue.get(Constants.COMPANY_ADDRESS_LINE)));
					String addressCity = getValue(row.getCell(indexValue.get(Constants.LOCATION)));
					String addressCountry = getValue(row.getCell(indexValue.get(Constants.COMPANY_COUNTRY)));
					String language = getValue(row.getCell(indexValue.get(Constants.COMPANY_LANGUAGE)));
					String addressZip = getValue(row.getCell(indexValue.get(Constants.COMPANY_ADDRESS_ZIP)));
					String billingAddressLine = getValue(
							row.getCell(indexValue.get(Constants.COMPANY_BILLING_ADDRESS)));
					String billingAddressCity = getValue(row.getCell(indexValue.get(Constants.COMPANY_BILLING_CITY)));
					String billingAddressCountry = getValue(
							row.getCell(indexValue.get(Constants.COMPANY_BILLING_COUNTRY)));
					String billingAddressZip = getValue(row.getCell(indexValue.get(Constants.COMPANY_BILLING_ZIP)));
					String taxNumber = getValue(row.getCell(indexValue.get(Constants.COMPANY_TAX)));
					String stateTax = getValue(row.getCell(indexValue.get(Constants.COMPANY_STATE_TAX)));
					String taxPercent = getValue(row.getCell(indexValue.get(Constants.COMPANY_TAX_PERCENT)));
					String web = getValue(row.getCell(indexValue.get(Constants.COMPANY_WEB)));
					String currency = getValue(row.getCell(indexValue.get(Constants.COMPANY_CURRENCY)));
					String accountNumber = getValue(row.getCell(indexValue.get(Constants.COMPANY_ACCOUNT_NO)));
					String comments = getValue(row.getCell(indexValue.get(Constants.COMPANY_DESCRIPTION)));
					String channels = getValue(row.getCell(indexValue.get(Constants.COMPANY_CHANNEL)));
					List<Channel> channelSet = new ArrayList<Channel>();
					String[] channelsArray = channels.split(",");
					for (String channel : channelsArray) {
						if (StringUtils.isBlank(channels)) {
							continue;
						}
						Channel currentChannel = channelService.findChannelByName(channel);
						if (currentChannel == null) {
							hasError = true;
							logDetails.append("Invalid channel Name : " + channel + " " + rowNumber);
							++corruptedRowNumber;
							addXlsRow(sheet, row, corruptedRowNumber,
									Constants.COMPANY_CHANNEL + " : " + channel + " No channel found :");
						} else {
							channelSet.add(currentChannel);
						}

					}
					// Initializing C object and setting values in entity class
					Customer customer = new Customer();
					customer.setName(companyName);
					Address address = new Address();
					Address billingAddress = new Address();
					address.setLine1(addressLine);
					address.setCity(addressCity);
					if (StringUtils.isNoneBlank(addressCountry)) {
						try {
							address.setCountry(CountryUtil.getInstance().getCountry(addressCountry).getCode());
						} catch (Exception e1) {
							hasError = true;
							logDetails.append("Invalid country Name : " + addressCountry + " " + rowNumber);
							++corruptedRowNumber;
							addXlsRow(sheet, row, corruptedRowNumber,
									Constants.COMPANY_COUNTRY + " : " + addressCountry + " not found :");
						}
					}
					address.setZipcode(addressZip);
					billingAddress.setLine1(billingAddressLine);
					billingAddress.setCity(billingAddressCity);
					if (StringUtils.isNoneBlank(billingAddressCountry)) {
						try {
							billingAddress
									.setCountry(CountryUtil.getInstance().getCountry(billingAddressCountry).getCode());
						} catch (Exception e1) {
							hasError = true;
							logDetails.append("Invalid billing address country Name : " + billingAddressCountry + " "
									+ rowNumber);
							++corruptedRowNumber;
							addXlsRow(sheet, row, corruptedRowNumber,
									Constants.COMPANY_BILLING_COUNTRY + " : " + billingAddressCountry + " not found :");
						}
					}
					billingAddress.setZipcode(billingAddressZip);
					customer.setId(null);
					customer.setAddress(address);
					customer.setBillingAddress(billingAddress);
					customer.setVatNumber(taxNumber);
					if (StringUtils.isNoneBlank(language)) {
						try {
							customer.setLanguage(Language.getEnum(language).getLanguage());
						} catch (Exception e1) {
							hasError = true;
							logDetails.append("Invalid language : " + language + " " + rowNumber);
							++corruptedRowNumber;
							addXlsRow(sheet, row, corruptedRowNumber,
									Constants.COMPANY_LANGUAGE + " : " + language + " not found :");
						}
					}
					if (!StringUtils.isBlank(category)) {
						try {
							XmlEntity tp = new XmlEntity();
							tp.setKey(CustomerDomainType.getEnum(category).getDomainType());
							customer.setType(tp);
						} catch (Exception e1) {
							hasError = true;
							logDetails.append("Invalid category : " + category + " " + rowNumber);
							++corruptedRowNumber;
							addXlsRow(sheet, row, corruptedRowNumber,
									Constants.COMPANY_CATEGORY + " : " + category + " not found :");
						}
					}
//					if (CustomerDomainType.hasEnum(category)) {
//						XmlEntity tp = new XmlEntity();
//						tp.setKey(category);
//						customer.setType(tp);
//					}
					customer.setWebsite(web);
					customer.setChannels(channelSet);
					try {
						if (!StringUtils.isBlank(currency)) {
							customer.setCurrency(Currency.getInstance(currency));
							if (!currencyService.getAllCurrencyCodeStringByActive(true).contains(currency)) {
								hasError = true;
								logDetails.append("Currency : " + currency + " is not active " + rowNumber);
								++corruptedRowNumber;
								addXlsRow(sheet, row, corruptedRowNumber,
										Constants.COMPANY_CURRENCY + " : " + currency + " is not active :");
							}
						} else {
							customer.setCurrency(Currency.getInstance(currencyService.getDefaultCurrency().getCode()));
						}
					} catch (Exception e) {
						hasError = true;
						logDetails.append("Invalid currency : " + currency + " " + rowNumber);
						++corruptedRowNumber;
						addXlsRow(sheet, row, corruptedRowNumber,
								Constants.COMPANY_CURRENCY + " : " + currency + " not found :");
					}
					numbers.put("phone", telephone);
					customer.setNumbers(numbers);
					customer.setCompanyNumber(stateTax);
					if (StringUtils.isNoneBlank(taxPercent)) {
						Vat vat = new Vat();
						vat.setKey(taxPercent);
						customer.addVatRate(vat);
					}
					if (StringUtils.isNoneBlank(accountNumber)) {
						customer.setAccountNumber(accountNumber);
					}
					customer.setComments(comments);
					customer.setContacts(contacts.remove(companyName));
					if (!hasError) {
						logger.info("No error in spreadsheet row. Uploading it in database row. : " + rowNumber);
//								System.out.println("customer => " + customer);
						try {
							customer = customerRepository.save(customer);
						} catch (Exception ex) {
							System.out.println(ex.getMessage());
						}
					}
				} else {
					for (Cell cell : row) {
						if (!StringUtils.isBlank(getValue(cell))) {
							hasError = true;
							logDetails.append("Client name not found so skipping the row: " + rowNumber);
							++corruptedRowNumber;
							addXlsRow(sheet, row, corruptedRowNumber,
									"Client name not found so skipping the row: " + rowNumber);
							break;
						}
					}
					break;
				}

			}
			int contactErrorNumber = 1;
			for (Entry<String, List<Person>> e : contacts.entrySet()) {
				for (Person p : e.getValue()) {
					Row cRow = contactSheet.createRow(contactErrorNumber);
					logDetails.append(" :: Client  : " + e.getKey()
							+ " has incorrect data Or does not exist in client sheet :: for contact "
							+ p.getFirstName());
					cRow.createCell(contactIndexMap.get(Constants.CLIENT_NAME)).setCellValue(e.getKey());
					cRow.createCell(contactIndexMap.get(Constants.CONTACT_FIRST_NAME)).setCellValue(p.getFirstName());
					cRow.createCell(contactIndexMap.get(Constants.CONTACT_LAST_NAME)).setCellValue(p.getName());
					cRow.createCell(contactIndexMap.get(Constants.CONTACT_JOB_TITTLE)).setCellValue(p.getFunction());
					cRow.createCell(contactIndexMap.get(Constants.CONTACT_TELEPHONE))
							.setCellValue(p.getNumbers() == null ? null : p.getNumbers().get("telephone"));
					cRow.createCell(contactIndexMap.get(Constants.CONTACT_MOBILE))
							.setCellValue(p.getNumbers() == null ? null : p.getNumbers().get("mobile"));
					cRow.createCell(contactIndexMap.get(Constants.CONTACT_EMAIL))
							.setCellValue(p.getNumbers() == null ? null : p.getNumbers().get("email"));
					cRow.createCell(cRow.getLastCellNum()).setCellValue("Invalid client Name/ data");
					contactErrorNumber++;
				}
			}

			if (corruptedRowNumber > 0) {
				hasError = true;
				String resourcePath = propConfig.getTeamiumResourcesPath();
				logFileName = FilenameUtils.removeExtension(file.getOriginalFilename()) + "_" + dateString + ".xlsx";

				relativePath = Constants.SPREADSHEET_STRING + "/" + Constants.CONTACT_CLIENT.toLowerCase() + "/"
						+ loggedInUser.getId() + "/";
				absolutePath = resourcePath + "/" + relativePath + logFileName;
				String urlString = propConfig.getAppBaseURL() + "/" + relativePath;

				File pathStored = new File(resourcePath + "/" + relativePath);
				if (!pathStored.exists()) {
					pathStored.mkdirs();
				}

				File filePath = new File(resourcePath + "/" + relativePath, logFileName);
				FileOutputStream fos = new FileOutputStream(filePath);
				errorWorkbook.write(fos);
				fos.close();

				url = CommonUtil.validateURL(urlString + logFileName);
			}
			break;

		case "CSV":
			// List<String[]> errorRecords = new LinkedList<String[]>();
			// try (Reader reader = new BufferedReader(new InputStreamReader(new
			// FileInputStream(file), "UTF-8"));
			// CSVReader csvReader = new CSVReaderBuilder(reader).build();) {
			//
			// List<String[]> records = csvReader.readAll();
			// for (String cell : records.get(0)) {
			// switch (cell.trim()) {
			// case Constants.STAFF_LAST_NAME:
			// indexValue.put(Constants.STAFF_LAST_NAME, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_FISRT_NAME:
			// indexValue.put(Constants.STAFF_FISRT_NAME, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_NAME_PREFIX:
			// indexValue.put(Constants.STAFF_NAME_PREFIX, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_JOB_TITLE:
			// indexValue.put(Constants.STAFF_JOB_TITLE, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_IS_FREELANCE:
			// indexValue.put(Constants.STAFF_IS_FREELANCE, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_FUNCTION:
			// indexValue.put(Constants.STAFF_FUNCTION, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_EXPERTISE:
			// indexValue.put(Constants.STAFF_EXPERTISE, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_LANGUAGE:
			// indexValue.put(Constants.STAFF_LANGUAGE, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_GROUP:
			// indexValue.put(Constants.STAFF_GROUP, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_ADDRESS:
			// indexValue.put(Constants.STAFF_ADDRESS, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_ZIP_OR_POSTAL_CODE:
			// indexValue.put(Constants.STAFF_ZIP_OR_POSTAL_CODE, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_CITY:
			// indexValue.put(Constants.STAFF_CITY, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_COUNTRY:
			// indexValue.put(Constants.STAFF_COUNTRY, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_EMAIL:
			// indexValue.put(Constants.STAFF_EMAIL, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_GOOGLECAL:
			// indexValue.put(Constants.STAFF_GOOGLECAL, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_MOBILE_PHONE:
			// indexValue.put(Constants.STAFF_MOBILE_PHONE, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_PHONE:
			// indexValue.put(Constants.STAFF_PHONE, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_SKYPE_ID:
			// indexValue.put(Constants.STAFF_SKYPE_ID, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_SOCIAL_SECURITY_NUMBER:
			// indexValue.put(Constants.STAFF_SOCIAL_SECURITY_NUMBER,
			// index.getAndIncrement());
			// continue;
			// case Constants.STAFF_GENDER:
			// indexValue.put(Constants.STAFF_GENDER, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_BIRTH_DATE:
			// indexValue.put(Constants.STAFF_BIRTH_DATE, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_PLACE_OF_BIRTH:
			// indexValue.put(Constants.STAFF_PLACE_OF_BIRTH, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_DPT:
			// indexValue.put(Constants.STAFF_DPT, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_COUNTRY_OF_BIRTH:
			// indexValue.put(Constants.STAFF_COUNTRY_OF_BIRTH, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_CITIZENSHIP:
			// indexValue.put(Constants.STAFF_CITIZENSHIP, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_PRESS_CARD_NUMBER:
			// indexValue.put(Constants.STAFF_PRESS_CARD_NUMBER, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_TALENT_REGISTRATION:
			// indexValue.put(Constants.STAFF_TALENT_REGISTRATION, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_ROUTING_AND_ACCOUNT:
			// indexValue.put(Constants.STAFF_ROUTING_AND_ACCOUNT, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_BIC:
			// indexValue.put(Constants.STAFF_BIC, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_BANK_ADDRESS:
			// indexValue.put(Constants.STAFF_BANK_ADDRESS, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_BENEFICIARY:
			// indexValue.put(Constants.STAFF_BENEFICIARY, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_MARRIED_NAME:
			// indexValue.put(Constants.STAFF_MARRIED_NAME, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_BIRTH_PROVINCE:
			// indexValue.put(Constants.STAFF_BIRTH_PROVINCE, index.getAndIncrement());
			// continue;
			// case Constants.STAFF_CONTRACT:
			// indexValue.put(Constants.STAFF_CONTRACT, index.getAndIncrement());
			// continue;
			// }
			// index.getAndIncrement();
			// }
			//
			// records.remove(0);
			// if (indexValue.size() >= HEADERS) {
			// for (String[] record : records) {
			// ++rowNumber;
			// if (indexValue.get(Constants.STAFF_FISRT_NAME) != null
			// && !record[indexValue.get(Constants.STAFF_FISRT_NAME)].isEmpty()) {
			//
			// String firstName = record[indexValue.get(Constants.STAFF_FISRT_NAME)];
			// String lastName = record[indexValue.get(Constants.STAFF_LAST_NAME)];
			// StaffMember staffMember = new StaffMember();
			// String courtesy = record[indexValue.get(Constants.STAFF_NAME_PREFIX)];
			// AbstractXmlEntity courtesyXmlEntity = new XmlEntity();
			// courtesyXmlEntity
			// .setKey(courtesy.equalsIgnoreCase("Mr") ? Constants.STAFF_PERFIX_MR
			// : (courtesy.equalsIgnoreCase("Ms") ? Constants.STAFF_PERFIX_MS
			// : Constants.STAFF_PERFIX_MISS));
			//
			// int courtesyXmlEntitysIndex = courtesyXmlEntitys.indexOf(courtesyXmlEntity);
			// if (courtesyXmlEntitysIndex > -1) {
			// staffMember
			// .setCourtesy((XmlEntity) courtesyXmlEntitys.get(courtesyXmlEntitysIndex));
			//
			// }
			// staffMember.setFirstName(firstName);
			// staffMember.setName(lastName);
			// List<String> phones = getContactNumbers(
			// record[indexValue.get(Constants.STAFF_PHONE)]);
			// phones.forEach(number -> {
			// numbers.put(number, "phone");
			// });
			// List<String> emails = getContactNumbers(
			// record[indexValue.get(Constants.STAFF_EMAIL)]);
			// emails.forEach(email -> {
			// numbers.put(email, "email");
			// });
			// List<String> mobiles = getContactNumbers(
			// record[indexValue.get(Constants.STAFF_MOBILE_PHONE)]);
			// mobiles.forEach(mobile -> {
			// numbers.put(mobile, "mobile");
			// });
			// List<String> googleCals = getContactNumbers(
			// record[indexValue.get(Constants.STAFF_GOOGLECAL)]);
			// googleCals.forEach(cal -> {
			// numbers.put(cal, "googleCal");
			// });
			// List<String> skypes = getContactNumbers(
			// record[indexValue.get(Constants.STAFF_SKYPE_ID)]);
			// skypes.forEach(skype -> {
			// numbers.put(skype, "skype");
			// });
			// staffMember.setNumbers(numbers);
			// StaffMemberIdentity staffMemberIdentity = new StaffMemberIdentity();
			// Address address = new Address();
			// String gender = record[indexValue.get(Constants.STAFF_GENDER)];
			// AbstractXmlEntity genderXmlEntity = new XmlEntity();
			// genderXmlEntity.setKey(gender.equalsIgnoreCase("male") ? "M" : "F");
			// int genderXmlEntitysIndex = genderXmlEntitys.indexOf(genderXmlEntity);
			// if (genderXmlEntitysIndex > -1) {
			// staffMemberIdentity
			// .setGender((XmlEntity) genderXmlEntitys.get(genderXmlEntitysIndex));
			//
			// }
			// staffMemberIdentity
			// .setBeneficiaire(record[indexValue.get(Constants.STAFF_BENEFICIARY)]);
			// staffMemberIdentity
			// .setBirthPlace(record[indexValue.get(Constants.STAFF_PLACE_OF_BIRTH)]);
			// staffMemberIdentity.setBirthProvince(
			// record[indexValue.get(Constants.STAFF_BIRTH_PROVINCE)]);
			// staffMemberIdentity.setNumCardPress(getIntValueAsString(
			// record[indexValue.get(Constants.STAFF_PRESS_CARD_NUMBER)]));
			// staffMemberIdentity
			// .setMarriedName(record[indexValue.get(Constants.STAFF_MARRIED_NAME)]);
			// staffMemberIdentity.setNumBic(record[indexValue.get(Constants.STAFF_BIC)]);
			// staffMemberIdentity.setHealthInsuranceNumber(
			// record[indexValue.get(Constants.STAFF_SOCIAL_SECURITY_NUMBER)]);
			// staffMemberIdentity
			// .setDomiciliation(record[indexValue.get(Constants.STAFF_BANK_ADDRESS)]);
			// staffMemberIdentity
			// .setNumIBAN(record[indexValue.get(Constants.STAFF_ROUTING_AND_ACCOUNT)]);
			// staffMemberIdentity.setBirthState(record[indexValue.get(Constants.STAFF_DPT)]);
			// String zipCode = null;
			// try {
			// String dob = record[indexValue.get(Constants.STAFF_BIRTH_DATE)];
			// Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dob);
			// Calendar dateOfBirth = Calendar.getInstance();
			// dateOfBirth.setTime(date);
			// staffMemberIdentity.setDateOfBirth(dateOfBirth);
			//
			// } catch (Exception e) {
			// logger
			// .warn("Invalid date format for date of birth in row no:" + rowNumber);
			// logDetails += "\n" + "Invalid date format for date of birth in row no:" +
			// rowNumber;
			// errorRecords.add(record);
			// }
			// staffMember.setFunction(record[indexValue.get(Constants.STAFF_JOB_TITLE)]);
			// zipCode = getIntValueAsString(
			// record[indexValue.get(Constants.STAFF_ZIP_OR_POSTAL_CODE)]);
			// address.setZipcode(zipCode);
			// address.setLine1(record[indexValue.get(Constants.STAFF_ADDRESS)]);
			// address.setCity(record[indexValue.get(Constants.STAFF_CITY)]);
			// staffMember.setAddress(address);
			// staffMember.setIdentity(staffMemberIdentity);
			//
			// this.staffMemberRepository.save(staffMember);
			//
			// } else {
			// logDetails += "\n" + "Not importing data from row no:" + rowNumber
			// + " because first name of staff memeber not exist in given row.Please provide
			// data with first name of staff member.";
			// errorRecords.add(record);
			// }
			//
			// }
			// } else {
			// logDetails += "\n Invalid Header List";
			//
			// }
			// }
			// if (!errorRecords.isEmpty()) {
			// hasError = true;
			// File directory =
			// appService.getFile(appService.getProperty("staff.spreadsheet.path"));
			// if (!directory.exists())
			// directory.mkdirs();
			// File filePath = new File(directory, SPREADSHEET_FILE_NAME + dateString +
			// ".csv");
			// CSVWriter writer = new CSVWriter(new FileWriter(filePath));
			// writer.writeAll(errorRecords);
			// writer.close();
			// }
			break;

		default:
			logDetails.append("Error! , Invalid file format. Please provide xlsx/xls/csv format.");
			logger.info("Spredsheet not uploaded for staff. Found invalid fortmat. File found with extention: "
					+ fileExtention);
			throw new UnprocessableEntityException(
					"Spredsheet not uploaded for staff. Found invalid fortmat. File found with extention: "
							+ fileExtention);
		}

		try {
			spreadsheetUploadLog = new SpreadsheetUploadLog(logDetails.toString(), calander,
					loggedInUser.getUserSetting().getLogin(), ImportFor.CLIENT, fileExtention, hasError,
					loggedInUser.getId(), file.getOriginalFilename(), absolutePath);
			spreadsheetUploadLog.setUrl(url);
			logger.info("Saving spreadsheet log in database.");
			spreadsheetUploadLog = spreadsheetUploadLogRepository.save(spreadsheetUploadLog);
			logger.info("successfully saved spreadsheet log in database with id : " + spreadsheetUploadLog.getId());

		} catch (Exception e) {
			logger.info("Exception in saving log in database");
			e.printStackTrace();
		}

		logger.info("Uploaded spredsheet for client.");
		if (hasError) {
			return new SpreadsheetMessageDTO("Imported client spreadsheet contains error data", true);
		}
		return new SpreadsheetMessageDTO("Successfully imported client spreadsheet", false);

	}

	/**
	 * To create contact Data
	 * 
	 * @param spreadsheet
	 * @param headerPositions
	 * @param contacts
	 * @return contact data
	 */
	private Map<String, List<Person>> createContactsData(Sheet spreadsheet, Map<String, Integer> headerPositions,
			Map<String, List<Person>> contacts) {
		logger.info("Inside createContactsData() :: creating contact data ");
		for (Row row : spreadsheet) {
			Person person = new Person();
			String companyName = null;
			companyName = getValue(row.getCell(headerPositions.get(Constants.CLIENT_NAME)));
			String firstName = getValue(row.getCell(headerPositions.get(Constants.CONTACT_FIRST_NAME)));
			String lastName = getValue(row.getCell(headerPositions.get(Constants.CONTACT_LAST_NAME)));
			String jobTittle = getValue(row.getCell(headerPositions.get(Constants.CONTACT_JOB_TITTLE)));
			String telephone = getValue(row.getCell(headerPositions.get(Constants.CONTACT_TELEPHONE)));
			String mobile = getValue(row.getCell(headerPositions.get(Constants.CONTACT_MOBILE)));
			String email = getValue(row.getCell(headerPositions.get(Constants.CONTACT_EMAIL)));
			person.setFirstName(firstName);
			person.setName(lastName);
			person.setFunction(jobTittle);
			Map<String, String> numbers = new HashMap<String, String>();
			numbers.put("email", email);
			numbers.put("telephone", telephone);
			numbers.put("mobile", mobile);
			person.setNumbers(numbers);
			if (!StringUtils.isBlank(companyName)) {
				// if no contact has added to company
				if (contacts.get(companyName) == null) {
					List<Person> cont = new LinkedList<Person>();
					cont.add(person);
					contacts.put(companyName, cont);
					// add person to same company's contact
				} else {
					List<Person> conts = contacts.get(companyName);
					conts.add(person);
					contacts.put(companyName, conts);
				}
			}

		}
		logger.info("Returning from createContactsData() :: ");
		return contacts;
	}

	/**
	 * 
	 * @param spreadsheet
	 * @param indexValueMap
	 * @param index
	 * @return totalHeaders
	 */
	private AtomicInteger createXLHeaders(Sheet spreadsheet, Map<String, Integer> indexValueMap, AtomicInteger index) {
		logger.info("Inside createXLHeaders :: creating XL headers for Client ");

		for (Cell cell : spreadsheet.getRow(0)) {

			switch (getValue(cell).trim()) {

			case Constants.STAFF_EMP_CODE:
				indexValueMap.put(Constants.STAFF_EMP_CODE, index.getAndIncrement());
				continue;

			case Constants.COMPANY_CATEGORY:
				indexValueMap.put(Constants.COMPANY_CATEGORY, index.getAndIncrement());
				continue;

			case Constants.COMPANY_TELEPHONE:
				indexValueMap.put(Constants.COMPANY_TELEPHONE, index.getAndIncrement());
				continue;

			case Constants.COMPANY_ADDRESS_LINE:
				indexValueMap.put(Constants.COMPANY_ADDRESS_LINE, index.getAndIncrement());
				continue;

			case Constants.COMPANY_ADDRESS_ZIP:
				indexValueMap.put(Constants.COMPANY_ADDRESS_ZIP, index.getAndIncrement());
				continue;

			case Constants.COMPANY_COUNTRY:
				indexValueMap.put(Constants.COMPANY_COUNTRY, index.getAndIncrement());
				continue;

			case Constants.COMPANY_ADDRESS_CITY:
				indexValueMap.put(Constants.COMPANY_ADDRESS_CITY, index.getAndIncrement());
				continue;

			case Constants.COMPANY_BILLING_COUNTRY:
				indexValueMap.put(Constants.COMPANY_BILLING_COUNTRY, index.getAndIncrement());
				continue;

			case Constants.COMPANY_TAX:
				indexValueMap.put(Constants.COMPANY_TAX, index.getAndIncrement());
				continue;

			case Constants.COMPANY_STATE_TAX:
				indexValueMap.put(Constants.COMPANY_STATE_TAX, index.getAndIncrement());
				continue;

			case Constants.COMPANY_WEB:
				indexValueMap.put(Constants.COMPANY_WEB, index.getAndIncrement());
				continue;

			case Constants.COMPANY_DESCRIPTION:
				indexValueMap.put(Constants.COMPANY_DESCRIPTION, index.getAndIncrement());
				continue;

			case Constants.COMPANY_BILLING_ADDRESS:
				indexValueMap.put(Constants.COMPANY_BILLING_ADDRESS, index.getAndIncrement());
				continue;

			case Constants.COMPANY_BILLING_ZIP:
				indexValueMap.put(Constants.COMPANY_BILLING_ZIP, index.getAndIncrement());
				continue;

			case Constants.COMPANY_BILLING_CITY:
				indexValueMap.put(Constants.COMPANY_BILLING_CITY, index.getAndIncrement());
				continue;

			case Constants.COMPANY_LANGUAGE:
				indexValueMap.put(Constants.COMPANY_LANGUAGE, index.getAndIncrement());
				continue;

			case Constants.COMPANY_CURRENCY:
				indexValueMap.put(Constants.COMPANY_CURRENCY, index.getAndIncrement());
				continue;

			case Constants.COMPANY_ACCOUNT_NO:
				indexValueMap.put(Constants.COMPANY_ACCOUNT_NO, index.getAndIncrement());
				continue;

			case Constants.COMPANY_TAX_PERCENT:
				indexValueMap.put(Constants.COMPANY_TAX_PERCENT, index.getAndIncrement());
				continue;

			case Constants.COMPANY_CHANNEL:
				indexValueMap.put(Constants.COMPANY_CHANNEL, index.getAndIncrement());
				continue;

			case Constants.CONTACT_FIRST_NAME:
				indexValueMap.put(Constants.CONTACT_FIRST_NAME, index.getAndIncrement());
				continue;

			case Constants.CONTACT_LAST_NAME:
				indexValueMap.put(Constants.CONTACT_LAST_NAME, index.getAndIncrement());
				continue;

			case Constants.CONTACT_JOB_TITTLE:
				indexValueMap.put(Constants.CONTACT_JOB_TITTLE, index.getAndIncrement());
				continue;

			case Constants.CONTACT_MOBILE:
				indexValueMap.put(Constants.CONTACT_MOBILE, index.getAndIncrement());
				continue;

			case Constants.CONTACT_EMAIL:
				indexValueMap.put(Constants.CONTACT_EMAIL, index.getAndIncrement());
				continue;

			case Constants.STAFF_PLACE_OF_BIRTH:
				indexValueMap.put(Constants.STAFF_PLACE_OF_BIRTH, index.getAndIncrement());
				continue;

			case Constants.STAFF_DPT:
				indexValueMap.put(Constants.STAFF_DPT, index.getAndIncrement());
				continue;
			case Constants.STAFF_COUNTRY_OF_BIRTH:
				indexValueMap.put(Constants.STAFF_COUNTRY_OF_BIRTH, index.getAndIncrement());
				continue;

			case Constants.STAFF_CITIZENSHIP:
				indexValueMap.put(Constants.STAFF_CITIZENSHIP, index.getAndIncrement());
				continue;

			case Constants.STAFF_PRESS_CARD_NUMBER:
				indexValueMap.put(Constants.STAFF_PRESS_CARD_NUMBER, index.getAndIncrement());
				continue;
			case Constants.STAFF_TALENT_REGISTRATION:
				indexValueMap.put(Constants.STAFF_TALENT_REGISTRATION, index.getAndIncrement());
				continue;

			case Constants.STAFF_ROUTING_AND_ACCOUNT:
				indexValueMap.put(Constants.STAFF_ROUTING_AND_ACCOUNT, index.getAndIncrement());
				continue;

			case Constants.STAFF_BIC:
				indexValueMap.put(Constants.STAFF_BIC, index.getAndIncrement());
				continue;

			case Constants.STAFF_BANK_ADDRESS:
				indexValueMap.put(Constants.STAFF_BANK_ADDRESS, index.getAndIncrement());
				continue;
			case Constants.STAFF_BENEFICIARY:
				indexValueMap.put(Constants.STAFF_BENEFICIARY, index.getAndIncrement());
				continue;

			case Constants.STAFF_MARRIED_NAME:
				indexValueMap.put(Constants.STAFF_MARRIED_NAME, index.getAndIncrement());
				continue;

			case Constants.STAFF_BIRTH_PROVINCE:
				indexValueMap.put(Constants.STAFF_BIRTH_PROVINCE, index.getAndIncrement());
				continue;

			case Constants.STAFF_CONTRACT:
				indexValueMap.put(Constants.STAFF_CONTRACT, index.getAndIncrement());
				continue;

			case Constants.CLIENT_NAME:
				indexValueMap.put(Constants.CLIENT_NAME, index.getAndIncrement());
				continue;
			case Constants.LOCATION:
				indexValueMap.put(Constants.LOCATION, index.getAndIncrement());
				continue;

			case Constants.STAFF_LABOR_AGREEMENT:
				indexValueMap.put(Constants.STAFF_LABOR_AGREEMENT, index.getAndIncrement());
				continue;

			}
			index.getAndIncrement();
		}
		logger.info("Returnign from createXLHeaders :: " + index.get() + " created XL headers for client ");
		return index;
	}

	/**
	 * To get type specific data from sheet.
	 * 
	 * @param data
	 * @return value of cell as String
	 */
	@SuppressWarnings("deprecation")
	private static String getValue(Cell data) {
		if (data == null || data.getCellTypeEnum() == null)
			return "";
		switch (data.getCellTypeEnum()) {
		case STRING:
			return data.getStringCellValue();

		case NUMERIC:
			return new BigDecimal(data.getNumericCellValue()).toPlainString();

		case FORMULA:
			data.setCellType(data.getCachedFormulaResultTypeEnum());
			return getValue(data);

		}
		return "";
	}

	/**
	 * To get integer value from double as string
	 * 
	 * @param s
	 * @return number as string
	 */
	public String getIntValueAsString(String s) {
		Double number = null;
		try {
			number = Double.parseDouble(s);
		} catch (Exception e) {
			return "";
		}
		return number.intValue() + "";

	}

	/**
	 * Add new corrupted Row to ErrorSheet
	 * 
	 * @param sheet
	 * @param row
	 * @param rowNumber
	 */
	private void addXlsRow(Sheet sheet, Row row, int rowNumber, String errorField) {
		Row errorRow = sheet.createRow(rowNumber);
		int columnIndex = 0;
		for (Cell c : row) {
			columnIndex = c.getColumnIndex();
			errorRow.createCell(c.getColumnIndex()).setCellValue(getValue(c));
		}
		if (errorField != null) {
			errorRow.createCell(++columnIndex).setCellValue(errorField + " has invalid data");
		}
	}

	/**
	 * To get list of numbers from comma separated list
	 * 
	 * @param s
	 * @return list of string from a string
	 */
	public List<String> getContactNumbers(String s) {
		return Arrays.asList(s.split(","));
	}

}
