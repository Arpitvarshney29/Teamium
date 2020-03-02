package com.teamium.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamium.config.PropConfig;
import com.teamium.constants.Constants;
import com.teamium.domain.Address;
import com.teamium.domain.Company;
import com.teamium.domain.EditionTemplateType;
import com.teamium.domain.Person;
import com.teamium.domain.PersonalExpensesReport;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.RecordInformation;
import com.teamium.domain.prod.docsign.DocumentGeneration;
import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.projects.Program;
import com.teamium.domain.prod.projects.Project;
import com.teamium.domain.prod.projects.Quotation;
import com.teamium.domain.prod.projects.invoice.Invoice;
import com.teamium.domain.prod.projects.invoice.InvoiceGeneration;
import com.teamium.domain.prod.projects.order.Order;
import com.teamium.domain.prod.resources.equipments.Equipment;
import com.teamium.domain.prod.resources.equipments.EquipmentFunction;
import com.teamium.domain.prod.resources.equipments.EquipmentResource;
import com.teamium.domain.prod.resources.staff.StaffFunction;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.domain.prod.resources.staff.StaffResource;
import com.teamium.dto.CallsheetDTO;
import com.teamium.dto.DateRangeDTO;
import com.teamium.dto.EquipmentDTO;
import com.teamium.dto.InvoiceDTO;
import com.teamium.dto.InvoiceGenerationDTO;
import com.teamium.dto.PersonalExpensesReportDTO;
import com.teamium.dto.QuotationDTO;
import com.teamium.dto.RecordDTO;
import com.teamium.dto.SignatureHistoryDTO;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.BookingRepository;
import com.teamium.repository.PersonRepository;
import com.teamium.utils.CommonUtil;
import com.teamium.utils.CountryUtil;
import com.teamium.utils.EditionPdfUtil;
import com.teamium.utils.FunctionUtil;

/**
 * A service class implementation for Edition
 * 
 * @author Hansraj
 *
 */
@Service
public class EditionService {

	public static final String PACKING_LIST = "packing_list";
	public static final String PACKAGE_LIST = "package_list";
	public static final String INVOICE_PROGRESS = "invoice_progress";
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private RecordService recordService;
	private PersonalExpensesReportService personalExpensesReportService;
	private DocumentGenerationService documentService;
	private PropConfig propConfig;
	private EditionPdfUtil edtionPdfUtil;
	private AuthenticationService authenticationService;
	private PersonRepository<Person> personRepository;
	private BookingRepository bookingRepository;

	private DigitalSignatureService digitalSignatureService;
	private EditionTemplateTypeService editionTemplateTypeService;

	@Autowired
	private CurrencyService currencyService;

	private PackageService packageService;
	@Autowired
	private InvoiceService invoiceService;

	/**
	 * @param recordService
	 * @param documentService
	 * @param propConfig
	 * @param edtionPdfUtil
	 * @param authenticationService
	 * @param personRepository
	 */
	public EditionService(RecordService recordService, DocumentGenerationService documentService, PropConfig propConfig,
			EditionPdfUtil edtionPdfUtil, AuthenticationService authenticationService,
			PersonRepository<Person> personRepository, PersonalExpensesReportService personalExpensesReportService,
			PackageService packageService, DigitalSignatureService digitalSignatureService,
			EditionTemplateTypeService editionTemplateTypeService, BookingRepository bookingRepository,
			InvoiceService invoiceService) {
		this.recordService = recordService;
		this.documentService = documentService;
		this.propConfig = propConfig;
		this.edtionPdfUtil = edtionPdfUtil;
		this.authenticationService = authenticationService;
		this.personRepository = personRepository;
		this.personalExpensesReportService = personalExpensesReportService;
		this.digitalSignatureService = digitalSignatureService;
		this.editionTemplateTypeService = editionTemplateTypeService;
		this.packageService = packageService;
		this.bookingRepository = bookingRepository;

	}

	/**
	 * To get project pdf
	 * 
	 * @param budgetId
	 * 
	 * @param contactId
	 * 
	 * @return pdf URL
	 */
	public RecordDTO generateOrderPdf(long orderId, long contactId) {
		logger.info("Inside EditionService :: getOrderPdf( orderId" + orderId + " : contactId " + contactId + " ).");
		Order order = (Order) recordService.validateRecordExistence(Order.class, orderId);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("budget", order);
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		if (order.getDate() != null) {
			data.put("date", format.format(order.getDate().getTime()));
		}
		if (order.getPurchaseOrderDate() != null) {
			data.put("deliveryDate", format.format(order.getPurchaseOrderDate().getTime()));
		}
		data.put("deliveryLocation", "location");
		Record source;
		StringBuilder address = new StringBuilder("");
		if (order.getSource() != null) {
			logger.info("finding order source ");
			source = order.getSource();
			logger.info("Order source is :" + source);
			if (source != null) {
				String t = recordService.findProjectTitleId(source.getId());
				data.put("title", t);
				Company company = source.getEntity();
				logger.info("source company is: " + company);
				if (company != null) {
					data.put("header", company.getHeader() == null ? " " : company.getHeader());
					data.put("footer", company.getFooter() == null ? " " : company.getFooter());
					data.put("logoUrl",
							company.getLogo() != null && company.getLogo().getUrl() != null ? company.getLogo().getUrl()
									: " ");

				}
			}

		}

		if (order.getCompany() != null && order.getCompany().getAddress() != null) {
			Address addressObj = order.getCompany().getAddress();
			address.append(addressObj.getLine1() == null ? "" : addressObj.getLine1() + " ");
			address.append(addressObj.getCity() == null ? "" : addressObj.getCity() + " ");
			address.append(addressObj.getState() == null ? "" : addressObj.getState() + " ");
			address.append(addressObj.getCountry() == null ? ""
					: " " + CountryUtil.getInstance().getNameByCode(addressObj.getCountry()));
			if (StringUtils.isNoneBlank(addressObj.getZipcode())) {
				address.append(" Zipcode : " + addressObj.getZipcode());
			}
		}
		Person person = personRepository.findOne(contactId);
		if ((person == null) /* || !(budget.getContacts().contains(person)) */) {
			logger.info("Invalid contact " + contactId);
			throw new UnprocessableEntityException("Invalid contact " + contactId);
		}
		if (order.getCompany() != null && order.getCompany() != null) {
			if (!(person.equals(order.getCompany().getMainContact())
					|| order.getCompany().getContacts().contains(person))) {
				logger.info("Invalid contact " + contactId);
				throw new UnprocessableEntityException("Invalid contact " + contactId);
			}
		}
		StringBuilder contact = new StringBuilder("");
		if (StringUtils.isNoneBlank(person.getFirstName())) {
			contact.append(person.getFirstName() + " ");
		}
		if (StringUtils.isNoneBlank(person.getName())) {
			contact.append(person.getName());
		}
		data.put("contact", contact.toString());
		data.put("address", address.toString());
		data.put("pdfTittle", "PURCHASE ORDER");
		data.put("company", order.getEntity());

		// double totalVat = order.getLines().stream().mapToDouble(l -> l.getVatAmount()
		// * l.getOccurrenceCount()).sum();

		double totalDiscount = order.getLines().stream().mapToDouble(l -> l.getDiscount() * l.getOccurrenceCount())
				.sum();
		double subTotal = order.getLines().stream()
				.mapToDouble(l -> l.getUnitPrice() * l.getOccurrenceCount() * l.getQtySoldPerOc()).sum();
		double totalAfterDiscount = subTotal - totalDiscount;
		float taxRate = order.getProcurementTax() == null ? 0 : order.getProcurementTax();
		double totalVat = (taxRate * totalAfterDiscount) / 100;
		double totalAfterTax = totalAfterDiscount + totalVat;
		RecordInformation information = order.getInformation();
		if (information != null) {
			data.put("comment", information.getComment());
		}
		data.put("terms", order.getPaymentTerms());
		data.put("taxRate", (int) taxRate);
		data.put("totalAfterTax", String.format("%.2f", totalAfterTax));
		data.put("totalVat", String.format("%.2f", totalVat));
		data.put("subTotal", String.format("%.2f", subTotal));
		String currency = "US$";
		if (order.getCurrency() != null)
			currency = order.getCurrency().getCurrencyCode();
		data.put("currencySymbol", currency);
		data.put("totalDiscount", totalDiscount);

		// For attaching signature in an edition.
		data = setSignatureList("Project Purchase Order", order.getSource().getId(), data);

		String pdfUrl = null;
		try {
			String path = propConfig.getTeamiumResourcesPath() + Constants.EDITION_PROJECT_ORDER_PATH;
			pdfUrl = edtionPdfUtil.createPdf("project_purchase_order", data, path);
			StaffMember createdBy = authenticationService.getAuthenticatedUser();
			DocumentGeneration document = new DocumentGeneration();
			document.setDocumentPath(pdfUrl);
			document.setCreatedBy(createdBy);
			document = documentService.saveOrUpdateDocument(document);
			pdfUrl = propConfig.getAppBaseURL() + Constants.EDITION_PROJECT_ORDER_PATH + pdfUrl;
		} catch (Exception e) {
			logger.info("error while creating pdf" + e.getMessage());
			throw new UnprocessableEntityException("error while creating pdf");
		}
		RecordDTO dto = new RecordDTO();
		dto.setId(order.getId());
		dto.setPdfUrl(pdfUrl);
		logger.info("Returning from getBudgetPdf");
		return dto;
	}

	/**
	 * To generate callsheet pdf
	 * 
	 * @param projectId
	 * @param contactId
	 * @param orgnization
	 * @param comments
	 * @param location
	 * 
	 * @return QuotationDTO
	 */
	public Map<String, String> getPersonalExpensesReport(long personnelExpenseReportId) {
		logger.info("Inside editionservice :: getPersonalExpensesReport(personnelExpenseReportId : "
				+ personnelExpenseReportId + ")");
		String fileName = null;
		PersonalExpensesReportDTO personalExpensesReportDto = personalExpensesReportService
				.getExpensesReportById(personnelExpenseReportId);
		PersonalExpensesReport personalExpensesReport = personalExpensesReportService
				.findById(personnelExpenseReportId);
		Project project = (Project) recordService.validateRecordExistence(Project.class,
				personalExpensesReport.getProject().getId());

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", personalExpensesReportDto.getId());
		data.put("Expensereport", personalExpensesReportDto.getExpensesItems());
		data.put("staff", personalExpensesReportDto.getStaffMember());
		data.put("project", personalExpensesReportDto.getProject());
		data.put("totalPersonalExpenses", personalExpensesReportDto.getTotalPersonalExpenses());
		data.put("totalCompanyCardExpenses", personalExpensesReportDto.getTotalCompanyCardExpenses());
		data.put("pdfTitle", "EXPENSE REPORT");
		data.put("company", project.getEntity());

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String projectDate = sdf.format(new Date(personalExpensesReportDto.getDate().getTimeInMillis()));
		data.put("projectDate", projectDate);
		String templateName = "expense_report";
		String currency = "US $";
		data.put("currencySymbol", currency);

		Project projects = personalExpensesReport.getProject();
		if (projects != null) {
			Company company = projects.getCompany();
			if (company != null) {
				String header = company.getHeader();
				String footer = company.getFooter();
				data.put("header", header);
				data.put("footer", footer);
			}
		}

		String path = propConfig.getTeamiumResourcesPath() + Constants.EDITION_PROJECT_EXPENSE_REPORT;
		try {
			fileName = edtionPdfUtil.createPdf(templateName, data, path);
			// budget.setPdf(fileName);
			// recordRepository.save(budget);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("error while creating pdf" + e.getMessage());
			throw new UnprocessableEntityException("error while creating pdf");
		}

		Map<String, String> report = new LinkedHashMap<>();
		try {
			report.put("expenseReport",
					Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(new File(path, fileName))));
		} catch (IOException e) {
		}

		return report;
	}

	public QuotationDTO getProjectCallSheetPdf(long projectId, long contactId, String orgnization, String comments,
			String location) {
		logger.info("Inside EditionService :: getBudgetPdf( projectId : " + projectId + " , contactId : " + contactId
				+ ", comment " + comments + " ).");
		Project project = (Project) recordService.validateRecordExistence(Project.class, projectId);
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, StaffMember> staffLines = this.getStaffLines(project.getLines());
		Map<String, Equipment> equipmentLines = this.getEquipmentLines(project.getLines());
		String channels = project.getChannelFormat().stream()
				.map(cf -> cf.getChannel() == null ? "" : cf.getChannel().getName()).collect(Collectors.joining(","));
		data.put("budget", project);
		data.put("channels", channels);
		data.put("staffLines", staffLines);
		data.put("equipmentLines", equipmentLines);
		data.put("pdfTittle", "CALL SHEET");
		data.put("orgnization", orgnization);
		data.put("comments", comments);
		data.put("location", location);

		// For attaching signature in an edition.
		data = setSignatureList("Project Booking CallSheet", projectId, data);

		StringBuilder address = new StringBuilder("");
		if (project.getCompany() != null && project.getCompany().getAddress() != null) {
			Address addressObj = project.getCompany().getAddress();
			address.append(addressObj.getLine1() == null ? "" : addressObj.getLine1() + " ");
			address.append(addressObj.getCity() == null ? "" : addressObj.getCity() + " ");
			address.append(addressObj.getCountry() == null ? ""
					: CountryUtil.getInstance().getNameByCode(addressObj.getCountry()));
			if (StringUtils.isNoneBlank(addressObj.getZipcode())) {
				address.append(" , Zipcode - " + addressObj.getZipcode());
			}
		}
		validateRecordContact(project, contactId);
		data.put("address", address.toString());
		String path = propConfig.getTeamiumResourcesPath() + Constants.EDITION_PROJECT_CALLSHEET_PATH;
		String pdfUrl = createGenericProjectPDF(project, contactId, data, "call_sheet", path);
		DocumentGeneration document = new DocumentGeneration();
		document.setDocumentPath(pdfUrl);
		document.setCreatedBy(authenticationService.getAuthenticatedUser());
		document = documentService.saveOrUpdateDocument(document);
		pdfUrl = propConfig.getAppBaseURL() + Constants.EDITION_PROJECT_CALLSHEET_PATH + pdfUrl;
		QuotationDTO dto = new QuotationDTO();
		dto.setId(project.getId());
		dto.setPdfUrl(pdfUrl);
		logger.info("Returning from getProjectCallSheetPdf()");
		return dto;
	}

	/**
	 * To get budget pdf
	 * 
	 * @param budgetId
	 * @param contactId
	 * 
	 * @return pdf URL
	 */
	public QuotationDTO getBudgetPdf(long budgetId, long contactId, String terms) {
		logger.info("Inside EditionService :: getBudgetPdf( budgetId" + budgetId + ": contact Id " + contactId
				+ ": terms " + terms + " ).");
		Quotation budget = (Quotation) recordService.validateRecordExistence(Quotation.class, budgetId);
		// if (budget.getFinancialStatus() ==null ||
		// !budget.getFinancialStatus().equalsIgnoreCase("Approved")) {
		if (budget.getPdf() != null) {
			String[] splitedPath = budget.getPdf().split("/");
			File fileToDeletePath = new File(getEditionPath() + splitedPath[splitedPath.length - 1]);
			if (fileToDeletePath.exists()) {
				fileToDeletePath.delete();
			}
		}
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, List<Line>> lines = budget.getLines().stream().filter(l -> l.getFunction() != null)
				.collect(Collectors.groupingBy(l -> l.getFunction().getParent() == null ? "Others"
						: l.getFunction().getParent().getQualifiedName()));
		data.put("budget", budget);
		data.put("lines", lines);
		data.put("terms", terms);
		data.put("pdfTittle", "BUDGET");
		String currencySymbol = "US $";
		// if (budget.getCurrency() != null)
		// currency = budget.getCurrency().getCurrencyCode();
		// String currencySymbol = currencyService.getCurrencySymbolByCode(currency);
		data.put("currencySymbol", currencySymbol);

		// For attaching signature in an edition.
		data = setSignatureList("Project Budget Quotation", budgetId, data);

		String pdfUrl = createGenericProjectPDF(budget, contactId, data, "budget", getEditionPath());
		QuotationDTO dto = new QuotationDTO();
		dto.setId(budget.getId());
		dto.setPdfUrl(propConfig.getAppBaseURL() + Constants.EDITION_BDGET_PATH + pdfUrl);
		logger.info("Returning from getBudgetPdf()");
		return dto;
	}

	/**
	 * To show budget pdf
	 * 
	 * @param programId
	 * @param contactId
	 * 
	 * @return pdf URL
	 */
	public QuotationDTO getShowBudgetPdf(long programId, long contactId, String comment) {
		logger.info("Inside EditionService :: getShowBudgetPdf( programId " + programId + ": contact Id" + contactId
				+ ": comment " + comment + " ).");
		Program budget = (Program) recordService.validateRecordExistence(Program.class, programId);
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, List<Line>> lines = budget.getLines().stream().filter(l -> l.getFunction() != null)
				.collect(Collectors.groupingBy(l -> l.getFunction().getParent() == null ? "Others"
						: l.getFunction().getParent().getQualifiedName()));
		Map<String, Double> subotals = createAllSubTotals(lines);
		data.put("budget", budget);
		data.put("subotals", subotals);
		data.put("lines", lines);
		data.put("season", budget.getSeason());
		int duration = budget.getMinuteDuration() == null ? 0 : budget.getMinuteDuration().intValue();
		data.put("minuteDuration", duration % 60);
		data.put("hourDuration", duration / 60);
		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
		if (budget.getStart() != null) {
			data.put("startDate", format1.format(budget.getStart().getTime()));
		}
		if (StringUtils.isNoneBlank(comment)) {
			data.put("comment", comment);
		}
		data.put("pdfTittle", "SHOW BUDGET");
		String currencySymbol = "US $";
		// if (budget.getCurrency() != null)
		// currency = budget.getCurrency().getCurrencyCode();
		// String currencySymbol = currencyService.getCurrencySymbolByCode(currency);
		data.put("currencySymbol", currencySymbol);

		// For attaching signature in an edition.
		data = setSignatureList("Show Budget Quotation", programId, data);

		String pdfUrl = createGenericProjectPDF(budget, contactId, data, "show_budget",
				propConfig.getTeamiumResourcesPath() + Constants.EDITION_SHOW_BDGET_PATH);
		QuotationDTO dto = new QuotationDTO();
		dto.setId(budget.getId());
		dto.setPdfUrl(propConfig.getAppBaseURL() + Constants.EDITION_SHOW_BDGET_PATH + pdfUrl);
		logger.info("Returning from getShowBudgetPdf()");
		return dto;
	}

	/**
	 * To create sub total
	 * 
	 * @param lines
	 * @return map of sub total
	 */

	private Map<String, Double> createAllSubTotals(Map<String, List<Line>> lines) {
		logger.info("Inside EditionService::createAllSubTotals()");
		Map<String, Double> subTotal = new HashMap<String, Double>();
		for (Entry<String, List<Line>> entry : lines.entrySet()) {
			double sum = entry.getValue().stream()
					.filter(l -> l != null && l.getUnitPrice() != null && l.getOccurrenceCount() != null
							&& l.getQtySoldPerOc() != null)
					.map(l -> new Double(l.getUnitPrice() * l.getOccurrenceCount() * l.getQtySoldPerOc()))
					.mapToDouble(Double::doubleValue).sum();
			subTotal.put(entry.getKey(), sum);
		}
		logger.info("Returning from createAllSubTotals()");
		return subTotal;
	}

	/**
	 * To generate budget pdf
	 * 
	 * @param budget
	 * @param data
	 * @param path
	 *
	 * @return pdf URL
	 */
	public String createGenericProjectPDF(Record budget, long contactId, Map<String, Object> data, String templateName,
			String path) {
		logger.info("Inside EditionService::createProjectPDF() creating project pdf");
		String fileName = null;
		Person person = personRepository.findOne(contactId);
		if ((person == null) /* || !(budget.getContacts().contains(person)) */) {
			logger.info("Invalid contact " + contactId);
			throw new UnprocessableEntityException("Invalid contact " + contactId);
		}
		StringBuilder contact = new StringBuilder("");
		if (StringUtils.isNoneBlank(person.getFirstName())) {
			contact.append(person.getFirstName() + " ");
		}
		if (StringUtils.isNoneBlank(person.getName())) {
			contact.append(person.getName());
		}
		data.put("contact", contact.toString());
		double totalVat = budget.getLines().stream().mapToDouble(l -> l.getVatAmount() * l.getOccurrenceCount()).sum();

		double subTotal = budget.getLines().stream()
				.filter(l -> l != null && l.getUnitPrice() != null && l.getOccurrenceCount() != null
						&& l.getQtySoldPerOc() != null)
				.mapToDouble(l -> l.getUnitPrice() * l.getOccurrenceCount() * l.getQtySoldPerOc()).sum();

		double totalDiscount = budget.getLines().stream().mapToDouble(l -> l.getDiscount() * l.getOccurrenceCount())
				.sum();
		double totalAfterTax = subTotal - totalDiscount + totalVat;
		Company company = budget.getEntity();

		if (company != null) {
			data.put("header", company.getHeader() == null ? "" : company.getHeader());
			data.put("footer", company.getFooter() == null ? "" : company.getFooter());
		}

		data.put("totalAfterTax", String.format("%.2f", totalAfterTax));
		data.put("company", budget.getEntity());
		data.put("totalVat", String.format("%.2f", totalVat));
		data.put("subTotal", String.format("%.2f", subTotal));
		data.put("totalDiscount", String.format("%.2f", totalDiscount));
		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
		if (budget.getDate() != null) {
			data.put("date", format1.format(budget.getDate().getTime()));
		}

		try {
			fileName = edtionPdfUtil.createPdf(templateName, data, path);
			budget.setPdf(fileName);
			// recordRepository.save(budget);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("error while creating pdf" + e.getMessage());
			throw new UnprocessableEntityException("error while creating pdf");
		}
		logger.info("Returning from createProjectPDF()");
		return fileName;
	}

	/**
	 * To get staff lines
	 * 
	 * @param lines
	 * @return personnel related to line function
	 */
	private Map<String, StaffMember> getStaffLines(List<Line> lines) {
		logger.info("Inside EditionService:: getStaffLines()");
		Map<String, StaffMember> staffLine = new TreeMap<String, StaffMember>((first, second) -> 1);

		lines.stream().forEach(l -> {

			if (l.getFunction() != null) {
				String type = FunctionUtil.getFunctionType(l.getFunction());
				if (type.equalsIgnoreCase(TeamiumConstants.STAFF_FUNCTION_TYPE)) {
					if (l instanceof Booking) {
						Booking booking = (Booking) l;
						if (booking.getResource() instanceof StaffResource) {
							StaffResource resource = (StaffResource) booking.getResource();
							if (resource.getEntity() != null) {
								StaffMember member = resource.getEntity();
								staffLine.put(l.getFunction().getValue(), member);
							}
						}

					}

				}

			}
		});
		logger.info("Returning from getStaffLines() ");
		return staffLine;
	}

	/**
	 * To get staff lines
	 * 
	 * @param lines
	 * @return equipments related to line function
	 */
	private Map<String, Equipment> getEquipmentLines(List<Line> lines) {
		logger.info("Inside EditionService::getEquipmentLines()");
		Map<String, Equipment> equipmentLine = new TreeMap<String, Equipment>((first, second) -> 1);

		lines.stream().forEach(l -> {

			if (l.getFunction() != null) {
				String type = FunctionUtil.getFunctionType(l.getFunction());
				if (type.equalsIgnoreCase(TeamiumConstants.EQUIPMENT_FUNCTION_TYPE)) {
					if (l instanceof Booking) {
						Booking booking = (Booking) l;
						if (booking.getResource() instanceof EquipmentResource) {
							EquipmentResource resource = (EquipmentResource) booking.getResource();
							if (resource.getEntity() != null) {
								Equipment equipment = resource.getEntity();
								equipment.setDescription(l.getComment());
								equipmentLine.put(l.getFunction().getValue(), equipment);
							}
						}

					}

				}

			}
		});
		logger.info("Returning from getEquipmentLines()");
		return equipmentLine;
	}

	/**
	 * To get project pdf
	 * 
	 * @param budgetId
	 * 
	 * @param contactId
	 * 
	 * @return pdf URL
	 */
	public QuotationDTO getPackingListPdf(long projectId, long contactId, String comment, String location) {
		logger.info(
				"Inside EditionService :: getPackingListPdf( projectId:" + projectId + "contactId:" + contactId + ").");
		Project project = (Project) recordService.validateRecordExistence(Project.class, projectId);
		Map<String, Object> data = new HashMap<String, Object>();
		// Map<String, StaffMember> staffLines = this.getStaffLines(project.getLines());
		Map<String, Equipment> equipmentLines = this.getEquipmentLines(project.getLines());
		String channels = project.getChannelFormat().stream()
				.map(cf -> cf.getChannel() == null ? "" : cf.getChannel().getName()).collect(Collectors.joining(","));
		data.put("budget", project);
		// data.put("company", project.getEntity());
		data.put("channels", channels);
		data.put("location", location);
		data.put("comments", comment);
		data.put("equipmentLines", equipmentLines);
		data.put("pdfTittle", "PACKING LIST");
		validateRecordContact(project, contactId);
		String path = propConfig.getTeamiumResourcesPath() + Constants.EDITION_PROJECT_PACKING_LIST_PATH;
		DocumentGeneration document = new DocumentGeneration();
		StringBuffer address = new StringBuffer("");
		if (project.getCompany() != null && project.getCompany().getAddress() != null) {
			Address addressObj = project.getCompany().getAddress();
			address.append(addressObj.getLine1() == null ? "" : addressObj.getLine1() + " ");
			address.append(addressObj.getCity() == null ? "" : addressObj.getCity() + " ");
			address.append(addressObj.getCountry() == null ? ""
					: CountryUtil.getInstance().getNameByCode(addressObj.getCountry()));
			if (StringUtils.isNoneBlank(addressObj.getZipcode())) {
				address.append(" , Zipcode - " + addressObj.getZipcode());
			}
		}
		data.put("address", address.toString());
		document.setCreatedBy(authenticationService.getAuthenticatedUser());

		// For attaching signature in an edition.
		data = setSignatureList("Project Booking Packing List", projectId, data);

		String pdfUrl = createGenericProjectPDF(project, contactId, data, PACKING_LIST, path);
		document = documentService.saveOrUpdateDocument(document);
		pdfUrl = propConfig.getAppBaseURL() + Constants.EDITION_PROJECT_PACKING_LIST_PATH + pdfUrl;
		QuotationDTO dto = new QuotationDTO();
		dto.setId(project.getId());
		dto.setPdfUrl(pdfUrl);
		logger.info("Returning from getPackingListPdf()");
		return dto;
	}

	/**
	 * To get budget pdf
	 * 
	 * @param projectId
	 * @param contactId
	 * 
	 * @return pdf URL
	 */
	public QuotationDTO getProductionStatementPdf(long projectId, long contactId, String comments) {
		logger.info("Inside RecordService :: getBudgetPdf( " + projectId + "contactId: " + contactId + " ).");
		Quotation budget = (Quotation) recordService.validateRecordExistence(Project.class, projectId);
		// if (budget.getFinancialStatus() ==null ||
		// !budget.getFinancialStatus().equalsIgnoreCase("Approved")) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, List<Line>> lines = budget.getLines().stream().filter(l -> l.getFunction() != null)
				.collect(Collectors.groupingBy(l -> l.getFunction().getParent() == null ? "Others"
						: l.getFunction().getParent().getQualifiedName()));
		data.put("budget", budget);

		data.put("lines", lines);
		data.put("comments", comments);
		data.put("pdfTittle", "PRODUCTION STATEMENT");
		validateRecordContact(budget, contactId);
		StringBuffer address = new StringBuffer("");
		if (budget.getCompany() != null && budget.getCompany().getAddress() != null) {
			Address addressObj = budget.getCompany().getAddress();
			address.append(addressObj.getLine1() == null ? "" : addressObj.getLine1() + " ");
			address.append(addressObj.getCity() == null ? "" : addressObj.getCity() + " ");
			address.append(addressObj.getCountry() == null ? ""
					: CountryUtil.getInstance().getNameByCode(addressObj.getCountry()));
			if (StringUtils.isNoneBlank(addressObj.getZipcode())) {
				address.append(" , Zipcode - " + addressObj.getZipcode());
			}
		}
		Map<String, Double> subtotals = createAllSubTotals(lines);

		data.put("subotals", subtotals);
		Double totalPrice = 0.0;
		Double subTotal;
		for (Map.Entry<String, Double> totalEntrySet : subtotals.entrySet()) {

			subTotal = totalEntrySet.getValue();
			if (subTotal == null) {
				subTotal = 0.0;
			}
			totalPrice = totalPrice + subTotal;
			data.put("totalprice", totalPrice);
		}

		data.put("address", address.toString());
		String currency = "US $";
		// if (budget.getCurrency() != null)
		// currency = budget.getCurrency().getCurrencyCode();
		// String currencySymbol = currencyService.getCurrencySymbolByCode(currency);
		data.put("currencySymbol", currency);

		// For attaching signature in an edition.
		data = setSignatureList("Project Booking Production Statement", projectId, data);

		String pdfUrl = createGenericProjectPDF(budget, contactId, data, "production_statement",
				propConfig.getTeamiumResourcesPath() + Constants.EDITION_PROJECT_PRODUCTION_STATEMENT_PATH);
		QuotationDTO dto = new QuotationDTO();
		dto.setId(budget.getId());
		dto.setPdfUrl(propConfig.getAppBaseURL() + Constants.EDITION_PROJECT_PRODUCTION_STATEMENT_PATH + pdfUrl);
		logger.info("Returning from getBudgetPdf");
		return dto;
	}

	private String getEditionPath() {
		return propConfig.getTeamiumResourcesPath() + Constants.EDITION_BDGET_PATH;
	}

	/**
	 * To Generate Callsheet pdf
	 * 
	 * @param callsheet
	 * @return document information
	 */
	public QuotationDTO getProjectCallSheetPdf(CallsheetDTO callsheet) {
		logger.info("Inside getProjectCallSheetPdf()");
		if (callsheet.getContactId() == 0) {
			logger.info("Invalid contact " + callsheet.getContactId());
			throw new UnprocessableEntityException("Invalid contact " + callsheet.getContactId());
		}
		/*
		 * Replace open tags
		 */
		String orgnization = callsheet.getOrgnization();
		if (StringUtils.isNotBlank(orgnization)) {
			orgnization = orgnization.replaceAll("<br>", "<br/>");
			orgnization = orgnization.replaceAll("<hr>", "<hr/>");
		}
		QuotationDTO documentDetails = this.getProjectCallSheetPdf(callsheet.getProjectId(), callsheet.getContactId(),
				orgnization, callsheet.getComment(), callsheet.getLocation());
		logger.info("Returning from getProjectCallSheetPdf()");
		return documentDetails;
	}

	/**
	 * To Generate packing list pdf
	 * 
	 * @param packing
	 * 
	 * @return document information
	 */
	public QuotationDTO getProjectPackingPdf(CallsheetDTO packing) {
		logger.info("Inside getProjectPackingPdf()");
		if (packing.getContactId() == 0) {
			logger.info("Invalid contact " + packing.getContactId());
			throw new UnprocessableEntityException("Invalid contact " + packing.getContactId());
		}
		QuotationDTO documentDetails = this.getPackingListPdf(packing.getProjectId(), packing.getContactId(),
				packing.getComment(), packing.getLocation());
		logger.info("Returning from getProjectPackingPdf()");
		return documentDetails;
	}

	/**
	 * To Generate package list pdf.
	 * 
	 * @param packOfPacking
	 * 
	 * @return document information.
	 */
	public QuotationDTO getProjectPackagePdf(CallsheetDTO packOfPacking) {
		logger.info("Inside getProjectPackagePdf()");
		if (packOfPacking.getContactId() == 0) {
			logger.info("Invalid contact " + packOfPacking.getContactId());
			throw new UnprocessableEntityException("Invalid contact id " + packOfPacking.getContactId());
		}
		QuotationDTO documentDetails = this.getPackageListPdf(packOfPacking.getProjectId(),
				packOfPacking.getContactId(), packOfPacking.getComment(), packOfPacking.getLocation());
		logger.info("Returning from getProjectPackagePdf()");
		return documentDetails;
	}

	/**
	 * To validate record contact
	 * 
	 * @param record
	 * @param contactId
	 * @return Person
	 */
	private Person validateRecordContact(Record record, long contactId) {
		Person person = personRepository.findOne(contactId);
		if ((person == null) /* || !(budget.getContacts().contains(person)) */) {
			logger.info("Invalid contact " + contactId);
			throw new UnprocessableEntityException("Invalid contact " + contactId);
		}
		if (record.getCompany() != null && record.getCompany() != null) {
			if (!(person.equals(record.getCompany().getMainContact())
					|| record.getCompany().getContacts().contains(person))) {
				logger.info("Invalid contact " + contactId);
				throw new UnprocessableEntityException("Invalid contact " + contactId);
			}
		}
		return person;
	}

	/**
	 * For attaching signature in an edition.
	 * 
	 * @param templateName
	 * @param recordId
	 * @param data
	 * @return Map<String, Object> data to set context for thymeleaf.
	 */
	public Map<String, Object> setSignatureList(String templateName, Long recordId, Map<String, Object> data) {
		logger.info("Inside EditionService :: setSignatureList(" + templateName + "," + recordId + "," + data + ")");
		EditionTemplateType editionTemplateType = editionTemplateTypeService.findByTemplateNameIgnoreCase(templateName);
		if (editionTemplateType == null) {
			logger.error("Invalid template name.");
			throw new NotFoundException("Invalid template name.");
		}
		List<SignatureHistoryDTO> allSignatures = digitalSignatureService.getAllSignatures(editionTemplateType,
				recordId);
		Map<Long, String> signatureImages = new LinkedHashMap<>();
		allSignatures.stream().forEach(signature -> {
			String image = "";
			if (!StringUtils.isBlank(signature.getRecipient().getUserSettingDTO().getSignaturePath())) {
				try {
					image = "data:image/png;base64," + Base64.getEncoder().encodeToString(Files
							.readAllBytes(Paths.get(signature.getRecipient().getUserSettingDTO().getSignaturePath())));
				} catch (IOException e) {
				}
			}
			signatureImages.put(signature.getId(), image);
		});
		data.put("signatureList", allSignatures);
		data.put("signatureImageList", signatureImages);
		logger.info("Returning from EditionService :: setSignatureList()");
		return data;
	}

	/**
	 * To get project pdf
	 * 
	 * @param budgetId
	 * 
	 * @param contactId
	 * 
	 * @return pdf URL
	 */
	public QuotationDTO getPackageListPdf(long projectId, long contactId, String comment, String location) {
		logger.info(
				"Inside EditionService :: getPackingListPdf( projectId:" + projectId + "contactId " + contactId + ".");

		Project project = (Project) recordService.validateRecordExistence(Project.class, projectId);
		Map<String, Object> data = new HashMap<String, Object>();
		Map<Equipment, List<EquipmentDTO>> equipmentPackage = this.getEquipmentPackageList(project.getLines());
		String channels = project.getChannelFormat().stream()
				.map(cf -> cf.getChannel() == null ? "" : cf.getChannel().getName()).collect(Collectors.joining(","));
		project.getLines();
		data.put("budget", project);
		data.put("channels", channels);
		data.put("location", location);
		data.put("comments", comment);
		data.put("equipmentPack", equipmentPackage);
		data.put("pdfTittle", "PACKING LIST");
		validateRecordContact(project, contactId);
		String path = propConfig.getTeamiumResourcesPath() + Constants.EDITION_PROJECT_PACKAGE_LIST_PATH;
		DocumentGeneration document = new DocumentGeneration();
		StringBuffer address = new StringBuffer("");
		document.setCreatedBy(authenticationService.getAuthenticatedUser());
		String pdfUrl = createGenericProjectPDF(project, contactId, data, PACKAGE_LIST, path);
		document = documentService.saveOrUpdateDocument(document);
		pdfUrl = propConfig.getAppBaseURL() + Constants.EDITION_PROJECT_PACKAGE_LIST_PATH + pdfUrl;
		QuotationDTO dto = new QuotationDTO();
		dto.setId(project.getId());
		dto.setPdfUrl(pdfUrl);
		logger.info("Inside EditionService::Returning from getPackingListPdf()");
		return dto;
	}

	/**
	 * To get staff lines for package.
	 * 
	 * @param lines
	 * @return equipments related to line function.
	 */
	private Map<Equipment, List<EquipmentDTO>> getEquipmentPackageList(List<Line> lines) {
		logger.info("Inside EditionService::Inside getEquipmentLines() ");
		Map<Equipment, List<EquipmentDTO>> equipmentLine = new TreeMap<Equipment, List<EquipmentDTO>>(
				(first, second) -> 1);

		lines.stream().forEach(l -> {

			if (l.getFunction() != null) {
				String type = FunctionUtil.getFunctionType(l.getFunction());
				if (type.equalsIgnoreCase(TeamiumConstants.EQUIPMENT_FUNCTION_TYPE)) {
					if (l instanceof Booking) {
						Booking booking = (Booking) l;
						if (booking.getResource() instanceof EquipmentResource) {
							EquipmentResource resource = (EquipmentResource) booking.getResource();
							List<EquipmentDTO> equipmentList = new ArrayList<>();
							if (resource.getId() != null) {
								List<EquipmentDTO> equipmentResource = this.packageService
										.findEquipmentForSelectedPackage(resource.getId());
								if (equipmentResource != null && !equipmentResource.isEmpty()) {
									equipmentResource.stream().forEach(equ -> {
										EquipmentDTO euipment = new EquipmentDTO();
										euipment.setName(equ.getName());
										euipment.setSerialNumber(equ.getSerialNumber());
										euipment.setQuantityForBookingLine((l.getOccurrenceCount()).intValue());
										equipmentList.add(euipment);
									});
									equipmentLine.put(resource.getEntity(), equipmentList);
								} else {
									EquipmentDTO euipment = new EquipmentDTO();
									euipment.setName("");
									euipment.setSerialNumber("");
									equipmentResource.add(euipment);
									equipmentLine.put(resource.getEntity(), equipmentResource);
								}

							}

						}

					}

				}

			}
		});
		logger.info("Inside EditionService :: Returning from getEquipmentPackageList()");
		return equipmentLine;
	}

	/**
	 * 
	 * @param budget
	 * @param contactId
	 * @param data
	 * @param templateName
	 * @param path
	 * @param percentage
	 * @param linesofProjectAndBudget
	 * @return String
	 */
	private String createInvoicePDF(Record budget, long contactId, Map<String, Object> data, String templateName,
			String path, InvoiceGenerationDTO invoiceGenerationDTO) {
		logger.info("Inside EditionService::createProjectPDF() creating project pdf");
		String fileName = null;
		Person person = personRepository.findOne(contactId);
		if ((person == null) /* || !(budget.getContacts().contains(person)) */) {
			logger.info("Invalid contact " + contactId);
			throw new UnprocessableEntityException("Invalid contact " + contactId);
		}
		StringBuilder contact = new StringBuilder("");
		if (StringUtils.isNoneBlank(person.getFirstName())) {
			contact.append(person.getFirstName() + " ");
		}
		if (StringUtils.isNoneBlank(person.getName())) {
			contact.append(person.getName());
		}
		data.put("contact", contact.toString());

		Company company = budget.getEntity();

		if (company != null) {
			data.put("header", company.getHeader() == null ? "" : company.getHeader());
			data.put("footer", company.getFooter() == null ? "" : company.getFooter());
		}

		Float subTotal = (float) budget.getLines().stream().mapToDouble(cost -> cost.getTotalLocalPrice()).sum();
		double previousPaidAmount = this.invoiceService.getTotalPaidAmountPreviousInvoices(budget.getId());
		Float dueAmount = (float) (budget.getCalcTotalPriceIVAT()
				- (previousPaidAmount + invoiceGenerationDTO.getInvoicedAmount()));
		data.put("company", budget.getEntity());

		data.put("totalVat", String.format("%.2f", invoiceGenerationDTO.getTotalTax()));
		data.put("subTotal", String.format("%.2f", subTotal));
		data.put("invoicedAmount", String.format("%.2f", invoiceGenerationDTO.getInvoicedAmount()));
		data.put("paidAmount", String.format("%.2f", previousPaidAmount));
		data.put("invoiceBalance", String.format("%.2f", dueAmount));
		data.put("date", invoiceGenerationDTO.getInvoiceDate());
		if (invoiceGenerationDTO.getInvoicePercent() != null) {
			data.put("forInvoice", invoiceGenerationDTO.getInvoicePercent() + "% Invoice");
		} else if (invoiceGenerationDTO.getFromDate() != null && invoiceGenerationDTO.getToDate() != null) {
			data.put("forInvoice",
					"Invoice from " + invoiceGenerationDTO.getFromDate() + " to " + invoiceGenerationDTO.getToDate());
		} else {
			data.put("forInvoice", "Final invoice");
		}

		// data.put("totalDiscount", String.format("%.2f", invoiceTotalDiscount));
		//
		// data.put("invoiceProgress", String.format("%.2f", invoiceProgress));
		// data.put("invoiceTotal", String.format("%.2f", invoiceTotal));

		// data.put("percentage", percentage);

		try {
			fileName = edtionPdfUtil.createPdfWithoutPreviousDelete(templateName, data, path);
			budget.setPdf(fileName);
			// recordRepository.save(budget);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("error while creating pdf" + e.getMessage());
			throw new UnprocessableEntityException("error while creating pdf");
		}
		logger.info("Returning from createProjectPDF()");
		return fileName;
	}

	/**
	 * 
	 * @param invoiceGenerationDTO
	 */
	public InvoiceGeneration createInvoicePDF(InvoiceGenerationDTO invoiceGenerationDTO) {
		long projectId = invoiceGenerationDTO.getProjectId();
		Project record = (Project) this.recordService.validateRecordExistence(Project.class, projectId);

		Map<String, List<Line>> recordMap = record.getLines().stream().filter(l -> l.getFunction() != null)
				.collect(Collectors.groupingBy(t -> (t.getFunction() instanceof EquipmentFunction) ? "Equipment"
						: t.getFunction() instanceof StaffFunction ? "Personnel" : "Services/Others"));

		System.out.println("ID==> " + invoiceGenerationDTO.getInvoiceGenerationId());
		InvoiceGeneration invoiceGeneration = this.invoiceService
				.findInvoiceGenerationById(invoiceGenerationDTO.getInvoiceGenerationId());
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("budget", record);
		data.put("invoice Id", invoiceGenerationDTO.getInvoiceGenerationId());
		data.put("line", recordMap);
		data.put("invoice type", invoiceGenerationDTO.getInvoiceType());
		String path = propConfig.getTeamiumResourcesPath() + Constants.EDITION_PROJECT_INVOICE_PATH + projectId + "/"
				+ invoiceGenerationDTO.getInvoiceGenerationId();
		String pdfUrl = createInvoicePDF(record, invoiceGenerationDTO.getContactId(), data, Constants.INVOICE_STRING,
				path, invoiceGenerationDTO);
		invoiceGeneration.setInvoicePdfPath(pdfUrl);
		invoiceGeneration = this.invoiceService.saveOrUpdateInvoice(invoiceGeneration);
		return invoiceGeneration;
	}

}
