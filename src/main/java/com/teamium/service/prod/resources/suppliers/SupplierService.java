package com.teamium.service.prod.resources.suppliers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.teamium.config.PropConfig;
import com.teamium.constants.Constants;
import com.teamium.domain.Address;
import com.teamium.domain.Company;
import com.teamium.domain.Person;
import com.teamium.domain.TeamiumException;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.domain.prod.resources.suppliers.Supplier;
import com.teamium.domain.prod.upload.log.ImportFor;
import com.teamium.domain.prod.upload.log.SpreadsheetUploadLog;
import com.teamium.dto.AddressDTO;
import com.teamium.dto.CurrencyDTO;
import com.teamium.dto.SpreadsheetMessageDTO;
import com.teamium.dto.SupplierDTO;
import com.teamium.dto.VendorDropdownDTO;
import com.teamium.dto.prod.resources.functions.RateDTO;
import com.teamium.enums.AccountancyType;
import com.teamium.enums.CustomerDomainType;
import com.teamium.enums.RateUnitType;
import com.teamium.enums.project.Language;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.SpreadsheetUploadLogRepository;
import com.teamium.repository.prod.resources.suppliers.SupplierRepository;
import com.teamium.service.AuthenticationService;
import com.teamium.service.CurrencyService;
import com.teamium.service.RecordService;
import com.teamium.service.prod.resources.functions.RateService;
import com.teamium.utils.CommonUtil;
import com.teamium.utils.CountryUtil;

/**
 * A service class implementation for Supplier Controller
 *
 */

@Service
public class SupplierService {

	private SupplierRepository supplierRepository;
	private RateService rateService;

	private AuthenticationService authenticationService;
	private PropConfig propConfig;
	private SpreadsheetUploadLogRepository spreadsheetUploadLogRepository;
	private CurrencyService currencyService;

	@Autowired
	private RecordService recordService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * @param supplierRepository
	 * @param rateService
	 * @param authenticationService
	 * @param propConfig
	 * @param spreadsheetUploadLogRepository
	 */
	public SupplierService(SupplierRepository supplierRepository, RateService rateService,
			AuthenticationService authenticationService, PropConfig propConfig,
			SpreadsheetUploadLogRepository spreadsheetUploadLogRepository, CurrencyService currencyService) {
		this.supplierRepository = supplierRepository;
		this.rateService = rateService;

		this.authenticationService = authenticationService;
		this.propConfig = propConfig;
		this.spreadsheetUploadLogRepository = spreadsheetUploadLogRepository;
		this.currencyService = currencyService;
	}

	/**
	 * To get list of suppliers
	 * 
	 * @return list of SupplierDTO
	 */
	public List<SupplierDTO> getSuppliers() {
		logger.info("inside getSuppliers() :");
		List<Supplier> suppliers = supplierRepository.getSuppliers();
		List<SupplierDTO> suppliersDTO = suppliers.stream().map(s -> {
			SupplierDTO supplier = new SupplierDTO(s);

			supplier.setRateCard(rateService.findRatesByCompany(s.getId()));
			return supplier;
		}).collect(Collectors.toList());
		logger.info(suppliers.size() + " suppliers found ");
		logger.info("Returning from getSuppliers() .");
		return suppliersDTO;
	}

	/**
	 * To get supplier details.
	 * 
	 * @param supplierId the supplierId.
	 * 
	 * @return the SupplierDTO.
	 */
	public SupplierDTO getSupplier(Long supplierId) {
		logger.info("Inside getSupplier() :: finding supplier with id : " + supplierId);
		if (supplierId == null) {
			logger.error("Supplier id is null");
			throw new NotFoundException("Supplier id can not be null");
		}

		Supplier supplier = supplierRepository.findOne(supplierId);
		if (supplier == null) {
			logger.error("Invalid supplier id.");
			throw new NotFoundException("Invalid supplier id.");
		}
		SupplierDTO supplierDto = new SupplierDTO(supplier);
		List<RateDTO> rates = rateService.findRatesByCompany(supplierId);
		supplierDto.setRateCard(rates);
		logger.info("Returning from getSupplier() .");

		return supplierDto;
	}

	/**
	 * To delete supplier.
	 * 
	 * @param supplierId the supplierId.
	 */
	public void deleteSupplier(Long supplierId) {
		logger.info("Inside deleteSupplier()  :: deleting supplier with id : " + supplierId);
		if (supplierId == null) {
			logger.error("Supplier id is null");
			throw new NotFoundException("Supplier id can not be null");
		}
		Supplier supplier = supplierRepository.findOne(supplierId);
		if (supplier == null) {
			logger.error("Invalid supplier id");
			throw new NotFoundException("Invalid supplier id ");
		}
		supplierRepository.delete(supplierId);
		logger.info("Returning from deleteSupplier() .");
	}

	/**
	 * To save the supplier.
	 * 
	 * @param supplierDTO the supplierDTO object.
	 */
	public SupplierDTO saveOrUpdateSupplier(SupplierDTO supplierDTO) {
		logger.info("Inside saveOrUpdateSupplier() :: save/update " + supplierDTO);
		Supplier supplier;
		Long supplierId = supplierDTO.getId();
		if (StringUtils.isBlank(supplierDTO.getCurrency())) {
			supplierDTO.setCurrency(currencyService.getDefaultCurrency().getCode());
		}
		if (supplierId == null) {
			supplier = validateSupplier(supplierDTO);
		} else {
			supplier = supplierRepository.getOne(supplierId);
			if (supplier == null) {
				logger.error("Invalid supplier id : " + supplierDTO.getId());
				throw new NotFoundException("Invalid supplier id : " + supplierDTO.getId());
			}
			Supplier validateSupplierName = supplierRepository.findByNameIgnoreCase(supplierDTO.getName());
			if (validateSupplierName != null
					&& supplier.getId().longValue() != validateSupplierName.getId().longValue()) {
				logger.info("Vendor name already exists.");
				throw new UnprocessableEntityException("Vendor name already exists");
			}
			supplier = supplierDTO.getSupplier(supplier);
		}
		if (!StringUtils.isBlank(supplierDTO.getDomain())) {
			CustomerDomainType.getEnum(supplierDTO.getDomain());
		}

		// validate the name of the supplier, the name must be unique
		Supplier validateSupplierName = supplierRepository.findByNameIgnoreCase(supplierDTO.getName());
		if (supplier.getId() == null && validateSupplierName != null) {
			logger.info("Vendor name already exists.");
			throw new UnprocessableEntityException("Vendor name already exists");
		}
		if (supplier.getId() != null && validateSupplierName != null
				&& supplier.getId().longValue() != validateSupplierName.getId().longValue()) {
			logger.info("Vendor name already exists.");
			throw new UnprocessableEntityException("Vendor name already exists");
		}

		supplier.setDomian(supplierDTO.getDomain());
		supplier = supplierRepository.save(supplier);

		if (supplierDTO.getRateCard() != null && supplierDTO.getRateCard().size() > 0) {
			supplierDTO.getRateCard().forEach(rate -> {
				rate.setCompanyId(supplierId);
				rateService.saveOrUpdateRate(rate);
			});
		}
		logger.info("Returning from saveOrUpdateSupplier() .");
		return new SupplierDTO(supplier);
	}

	/**
	 * To validate the supplier.
	 * 
	 * @param supplierDTO the Supplier object.
	 */
	private Supplier validateSupplier(SupplierDTO supplierDTO) {
		logger.info("Inside validateSupplier() :: validating supplier : " + supplierDTO);
		if (StringUtils.isBlank(supplierDTO.getName())) {
			logger.error("Please enter a valid name.");
			throw new UnprocessableEntityException("Please enter a valid name.");
		}
		Company savedCompany = supplierRepository.findByNameIgnoreCase(supplierDTO.getName());
		if (savedCompany != null) {
			logger.error("Vendor name already exits : " + supplierDTO.getName());
			throw new UnprocessableEntityException("Vendor name already exits");
		}
		validateAddress(supplierDTO.getAddress());
		Supplier supplier = supplierDTO.getSupplier();
		logger.info("Returning from validateSupplier().");
		return supplier;
	}

	/**
	 * To validate the Address.
	 */
	private void validateAddress(AddressDTO addressDTO) {
		logger.info("Returning validateAddress() :: validating address : " + addressDTO);
		if (addressDTO == null) {
			logger.error("Please enter a valid address.");
			throw new UnprocessableEntityException("Please enter a valid address.");
		}
		if (addressDTO.getId() != null) {
			logger.error("Please enter a new address.");
			throw new UnprocessableEntityException("Please enter a new address.");
		}
		logger.info("Returning fron validateAddress() .");
	}

	public Supplier findById(long supplierId) {
		Supplier supplier = supplierRepository.findOne(supplierId);
		if (supplier == null) {
			logger.error("Invalid supplier id");
			throw new NotFoundException("Invalid supplier id ");
		}
		return supplier;
	}

	/**
	 * To get customer drop down data.
	 * 
	 * @return the VendorDropdownDTO
	 */
	public VendorDropdownDTO getVendorDropdowns() {
		logger.info("inside getCustomerDropdowns() :");
		List<CurrencyDTO> currencyList = currencyService.getAllCurrencyByActive(Boolean.TRUE);
		List<String> languagesList = Language.getLanguages();
		List<String> formats = CommonUtil.getProjectFormats();
		List<String> basis = RateUnitType.getRateUnitTypes();
		List<String> countryList = CountryUtil.getInstance().getCountryNames();
		List<String> accountingCodes = AccountancyType.getAccountancyTypes();

		VendorDropdownDTO dropdown = new VendorDropdownDTO(currencyList, languagesList, countryList, accountingCodes,
				formats, basis, CustomerDomainType.getDomainTypes(), supplierRepository.getSuppliersCities());

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

		StringBuffer logDetails = new StringBuffer("");
		Date currentDate = new Date();
		String dateString = new SimpleDateFormat("yyyyMMddHHmm").format(currentDate);
		Calendar calander = Calendar.getInstance();
		calander.setTime(currentDate);

		SpreadsheetUploadLog spreadsheetUploadLog = new SpreadsheetUploadLog();

		boolean hasInvalidHeader = false;
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
		logDetails.append("Upload started :: ");
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
			logDetails.append("Uploading  XLSX : ");
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
				throw new UnprocessableEntityException("Invalid contact headers");
			}
			contacts = createContactsData(contactSpreadsheet, contactIndexMap, contacts);
			if (indexValue.size() < Constants.VEDOR_HEADERS) {
				logger.info("Invalid vendor headers");
				throw new UnprocessableEntityException("Invalid vendor headers");
			}

			for (Row row : spreadsheet) {
				hasError = false;
				++rowNumber;
				if (!StringUtils.isBlank(getValue(row.getCell(0))) && indexValue.get(Constants.VENDO_NAME) != null
						&& !getValue(row.getCell(indexValue.get(Constants.VENDO_NAME))).isEmpty()) {

					String companyName = getValue(row.getCell(indexValue.get(Constants.VENDO_NAME)));
					if (!StringUtils.isBlank(companyName)) {
						Company savedCompany = supplierRepository.findByNameIgnoreCase(companyName);
						if (savedCompany != null) {
							// make a log for duplicate employee-code
							hasError = true;
							logDetails.append("Duplicate comany Name : " + companyName
									+ " is duplicate on row number : " + rowNumber);
							++corruptedRowNumber;
							addXlsRow(sheet, row, corruptedRowNumber,
									Constants.VENDO_NAME + " : " + companyName + " is duplicate, company-name");
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
					String billingAddressCountry = getValue(
							row.getCell(indexValue.get(Constants.COMPANY_BILLING_COUNTRY)));
					String billingAddressLine = getValue(
							row.getCell(indexValue.get(Constants.COMPANY_BILLING_ADDRESS)));
					String billingAddressCity = getValue(row.getCell(indexValue.get(Constants.COMPANY_BILLING_ZIP)));
					String billingAddressZip = getValue(row.getCell(indexValue.get(Constants.COMPANY_BILLING_ZIP)));
					String taxNumber = getValue(row.getCell(indexValue.get(Constants.COMPANY_TAX)));
					String stateTax = getValue(row.getCell(indexValue.get(Constants.COMPANY_STATE_TAX)));
					String web = getValue(row.getCell(indexValue.get(Constants.COMPANY_WEB)));
					String currency = getValue(row.getCell(indexValue.get(Constants.COMPANY_CURRENCY)));
					String comments = getValue(row.getCell(indexValue.get(Constants.COMPANY_DESCRIPTION)));
					String accountNumber = getValue(row.getCell(indexValue.get(Constants.COMPANY_ACCOUNT_NO)));

//							String billingAddressZip = getValue(row.getCell(indexValue.get(Constants.companycou)));
					// Initializing C object and setting values in entity class
					Supplier supplier = new Supplier();
					supplier.setName(companyName);
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
					supplier.setId(null);
					supplier.setAddress(address);
					supplier.setBillingAddress(billingAddress);
					supplier.setVatNumber(taxNumber);
					XmlEntity tp = new XmlEntity();
					tp.setKey(category);
					supplier.setWebsite(web);
					if (StringUtils.isNoneBlank(language)) {
						try {
							supplier.setLanguage(Language.getEnum(language).getLanguage());
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
							supplier.setDomian(CustomerDomainType.getEnum(category).getDomainType());
						} catch (Exception e1) {
							hasError = true;
							logDetails.append("Invalid category : " + category + " " + rowNumber);
							++corruptedRowNumber;
							addXlsRow(sheet, row, corruptedRowNumber,
									Constants.COMPANY_CATEGORY + " : " + category + " not found :");
						}
					}
					try {
						if (!StringUtils.isBlank(currency)) {
							supplier.setCurrency(Currency.getInstance(currency));
							if (!currencyService.getAllCurrencyCodeStringByActive(true).contains(currency)) {
								hasError = true;
								logDetails.append("Currency : " + currency + " is not active " + rowNumber);
								++corruptedRowNumber;
								addXlsRow(sheet, row, corruptedRowNumber,
										Constants.COMPANY_CURRENCY + " : " + currency + " is not active :");
							}
						} else {
							supplier.setCurrency(Currency.getInstance(currencyService.getDefaultCurrency().getCode()));
						}
					} catch (Exception e) {
						hasError = true;
						logDetails.append("Invalid currency : " + currency + " " + rowNumber);
						++corruptedRowNumber;
						addXlsRow(sheet, row, corruptedRowNumber,
								Constants.COMPANY_CURRENCY + " : " + currency + " not found :");
					}

					numbers.put("phone", telephone);
					supplier.setNumbers(numbers);
					supplier.setCompanyNumber(stateTax);
					supplier.setComments(comments);
					companyName = companyName.trim().toLowerCase();
					supplier.setContacts(contacts.remove(companyName));
					if (StringUtils.isNoneBlank(accountNumber)) {
						supplier.setAccountNumber(accountNumber);
					}
					if (!hasError) {

//								System.out.println("customer => " + customer);
						try {
							supplier = supplierRepository.save(supplier);
							logger.info("No error in spreadsheet row. Uploading it in database row. : " + rowNumber);
							logDetails.append("A new  vendor added with id :: " + supplier.getId());
						} catch (Exception ex) {
							System.out.println(ex.getMessage());
						}
					} else {
						logger.info("Skipping the row :: " + rowNumber);
						logDetails.append(" \n  vendor with Name" + supplier.getName() + " :: \n");
					}
				} else {
					for (Cell cell : row) {
						if (!StringUtils.isBlank(getValue(cell))) {
							hasError = true;
							logDetails.append("Vendor name not found so skipping the row: " + rowNumber);
							++corruptedRowNumber;
							addXlsRow(sheet, row, corruptedRowNumber, "Vendor name not found so skipping the row: " + rowNumber);
							break;
						}
					}
					break;
				}
			}

			int contactErrorNumber = 1;
			// the contact which has not saved
			for (Entry<String, List<Person>> e : contacts.entrySet()) {
				for (Person p : e.getValue()) {
					Row cRow = contactSheet.createRow(contactErrorNumber);
					logDetails.append(" :: Vendor  : " + e.getKey()
							+ " has incorrect data Or does not exist in vendor sheet :: for contact "
							+ p.getFirstName());
					cRow.createCell(contactIndexMap.get(Constants.VENDO_NAME)).setCellValue(e.getKey());
					cRow.createCell(contactIndexMap.get(Constants.CONTACT_FIRST_NAME)).setCellValue(p.getFirstName());
					cRow.createCell(contactIndexMap.get(Constants.CONTACT_LAST_NAME)).setCellValue(p.getName());
					cRow.createCell(contactIndexMap.get(Constants.CONTACT_JOB_TITTLE)).setCellValue(p.getFunction());
					cRow.createCell(contactIndexMap.get(Constants.CONTACT_TELEPHONE))
							.setCellValue(p.getNumbers() == null ? null : p.getNumbers().get("telephone"));
					cRow.createCell(contactIndexMap.get(Constants.CONTACT_MOBILE))
							.setCellValue(p.getNumbers() == null ? null : p.getNumbers().get("mobile"));
					cRow.createCell(contactIndexMap.get(Constants.CONTACT_EMAIL))
							.setCellValue(p.getNumbers() == null ? null : p.getNumbers().get("email"));
					cRow.createCell(cRow.getLastCellNum()).setCellValue(" :: Invalid Vendor Name/ data");
					contactErrorNumber++;
				}
			}

			if (corruptedRowNumber > 0) {
				hasError = true;
				String resourcePath = propConfig.getTeamiumResourcesPath();
				logFileName = FilenameUtils.removeExtension(file.getOriginalFilename()) + "_" + dateString + ".xlsx";

				relativePath = Constants.SPREADSHEET_STRING + "/" + Constants.CONTACT_VENDOR.toLowerCase() + "/"
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
		}

		try {
			spreadsheetUploadLog = new SpreadsheetUploadLog(logDetails.toString(), calander,
					loggedInUser.getUserSetting().getLogin(), ImportFor.VENDOR, fileExtention, hasError,
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
			return new SpreadsheetMessageDTO("Imported vendor spreadsheet contains error data", true);
		}
		return new SpreadsheetMessageDTO("Successfully imported vendor spreadsheet", false);

	}

//	protected void handleChannelFormat(Supplier supplier,SupplierDTO supplierDTO){
//		AtomicBoolean isRemove = new AtomicBoolean(false);
//		// setting channel-formats
//		Set<ChannelFormat> dbChannelFormat = supplier.getChannelFormat();
//		Set<ChannelFormatDTO> channelFormatsList = supplierDTO.getChannelFormat();
//
//		if (channelFormatsList != null && !channelFormatsList.isEmpty()) {
//			supplier.setChannelFormat(channelFormatsList.stream().map(dto -> {
//
//				// removing channel-Formats not present in dto
//				if (dbChannelFormat != null && !dbChannelFormat.isEmpty()) {
//					dbChannelFormat.removeIf(dt -> {
//						isRemove.set(true);
//						channelFormatsList.forEach(dt1 -> {
//							if (dt1.getId() != null && dt.getId() != null
//									&& dt1.getId().longValue() == dt.getId().longValue()) {
//								isRemove.set(false);
//								return;
//							}
//						});
//						return isRemove.get();
//					});
//				}
//
//				// check the existing channel-format
//				ChannelFormat entity = new ChannelFormat();
//				try {
//					entity = dto.getChannelFormat(entity);
//				} catch (Exception e) {
//				}
//				if (dto.getId() != null) {
//					Optional<ChannelFormat> list = dbChannelFormat.stream()
//							.filter(format1 -> format1.getId().longValue() == dto.getId().longValue()).findFirst();
//					if (list.isPresent()) {
//						entity = list.get();
//					}
//				}
//				entity.setId(dto.getId());
//				if (dto.getChannel() == null || dto.getChannel().getId() == null) {
//					logger.info("Please provide valid channel.");
//					throw new NotFoundException("Please provide valid channel.");
//				}
//				Channel channel = channelService.findChannelById(dto.getChannel().getId());
//				if (channel == null) {
//					logger.info("Channel not found with id : " + dto.getChannel().getId());
//					throw new NotFoundException("Channel not found");
//				}
//				entity.setChannel(channel);
//
//				if (!StringUtils.isBlank(dto.getFormat())) {
//					boolean contain = CommonUtil.getProjectFormats().contains(dto.getFormat());
//					if (!contain) {
//						logger.info("Please provide valid channel format : " + dto.getFormat());
//						throw new UnprocessableEntityException("Please provide valid channel format");
//					}
//					entity.setFormat(dto.getFormat());
//				} else {
//					logger.info("Please provide valid channel format : " + dto.getFormat());
//					throw new UnprocessableEntityException("Please provide valid channel format");
//				}
//
//				return entity;
//			}).collect(Collectors.toSet()));
//
//		} else {
//			// remove all
//			if (record.getChannelFormat() != null) {
//				record.getChannelFormat().clear();
//			}
//		}
//
//	}

	/**
	 * 
	 * @param spreadsheet
	 * @param indexValueMap
	 * @param index
	 * @return totalHeaders
	 */
	private AtomicInteger createXLHeaders(Sheet spreadsheet, Map<String, Integer> indexValueMap, AtomicInteger index) {
		logger.info("Inside createXLHeaders :: creating XL headers for vendor ");
		for (Cell cell : spreadsheet.getRow(0)) {

			switch (getValue(cell).trim()) {

			case Constants.STAFF_EMP_CODE:
				indexValueMap.put(Constants.STAFF_EMP_CODE, index.getAndIncrement());
				continue;

			case Constants.VENDO_NAME:
				indexValueMap.put(Constants.VENDO_NAME, index.getAndIncrement());
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

			case Constants.COMPANY_TAX:
				indexValueMap.put(Constants.COMPANY_TAX, index.getAndIncrement());
				continue;

			case Constants.COMPANY_ACCOUNT_NO:
				indexValueMap.put(Constants.COMPANY_ACCOUNT_NO, index.getAndIncrement());
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

			case Constants.COMPANY_BILLING_COUNTRY:
				indexValueMap.put(Constants.COMPANY_BILLING_COUNTRY, index.getAndIncrement());
				continue;

			case Constants.LOCATION:
				indexValueMap.put(Constants.LOCATION, index.getAndIncrement());
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

			case Constants.CONTACT_CLIENT:
				indexValueMap.put(Constants.CONTACT_CLIENT, index.getAndIncrement());
				continue;

			case Constants.CLIENT_NAME:
				indexValueMap.put(Constants.CLIENT_NAME, index.getAndIncrement());
				continue;
			case Constants.STAFF_LABOR_AGREEMENT:
				indexValueMap.put(Constants.STAFF_LABOR_AGREEMENT, index.getAndIncrement());
				continue;

			}
			index.getAndIncrement();
		}
		logger.info("Returnign from createXLHeaders :: " + index.get() + " created XL headers for vendor ");
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
//			return String.valueOf(data.getNumericCellValue());

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

	private Map<String, List<Person>> createContactsData(Sheet spreadsheet, Map<String, Integer> headerPositions,
			Map<String, List<Person>> contacts) {
		logger.info("Inside createContactsData() :: creating contact data ");
		for (Row row : spreadsheet) {
			Person person = new Person();
			String companyName = null;
			companyName = getValue(row.getCell(headerPositions.get(Constants.VENDO_NAME)));
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
			companyName = companyName.trim().toLowerCase();
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

}
