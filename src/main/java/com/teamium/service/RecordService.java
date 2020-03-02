package com.teamium.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamium.config.PropConfig;
import com.teamium.constants.Constants;
import com.teamium.domain.Category;
import com.teamium.domain.Channel;
import com.teamium.domain.ChannelFormat;
import com.teamium.domain.Company;
import com.teamium.domain.ContactRecord;
import com.teamium.domain.CurrencySymbol;
import com.teamium.domain.Format;
import com.teamium.domain.MilestoneType;
import com.teamium.domain.Person;
import com.teamium.domain.ProductionUnit;
import com.teamium.domain.RecordHistory;
import com.teamium.domain.Role.RoleName;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.Theme;
import com.teamium.domain.Vat;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.DueDate;
import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.RecordFee;
import com.teamium.domain.prod.RecordInformation;
import com.teamium.domain.prod.projects.AbstractProject;
import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.projects.BookingEvent;
import com.teamium.domain.prod.projects.Layout;
import com.teamium.domain.prod.projects.Program;
import com.teamium.domain.prod.projects.Project;
import com.teamium.domain.prod.projects.Quotation;
import com.teamium.domain.prod.projects.intervention.Intervention;
import com.teamium.domain.prod.projects.order.Order;
import com.teamium.domain.prod.projects.order.OrderLine;
import com.teamium.domain.prod.projects.order.SupplyResource;
import com.teamium.domain.prod.resources.Resource;
import com.teamium.domain.prod.resources.ResourceFunction;
import com.teamium.domain.prod.resources.contacts.Customer;
import com.teamium.domain.prod.resources.equipments.Equipment;
import com.teamium.domain.prod.resources.equipments.EquipmentResource;
import com.teamium.domain.prod.resources.functions.DefaultResource;
import com.teamium.domain.prod.resources.functions.ExtraLine;
import com.teamium.domain.prod.resources.functions.Function;
import com.teamium.domain.prod.resources.functions.Rate;
import com.teamium.domain.prod.resources.functions.RateCard;
import com.teamium.domain.prod.resources.functions.RatedFunction;
import com.teamium.domain.prod.resources.functions.units.RateUnit;
import com.teamium.domain.prod.resources.staff.StaffFunction;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.domain.prod.resources.staff.StaffResource;
import com.teamium.domain.prod.resources.staff.contract.Contract;
import com.teamium.domain.prod.resources.staff.contract.ContractLine;
import com.teamium.domain.prod.resources.staff.contract.ContractSetting;
import com.teamium.domain.prod.resources.staff.contract.EntertainmentContractSetting;
import com.teamium.dto.ATACarnetReportDTO;
import com.teamium.dto.AbstractProjectDTO;
import com.teamium.dto.AvailablilityDTO;
import com.teamium.dto.BookingDTO;
import com.teamium.dto.ChannelDTO;
import com.teamium.dto.ChannelFormatDTO;
import com.teamium.dto.CompanyDTO;
import com.teamium.dto.CurrencyDTO;
import com.teamium.dto.CustomerDTO;
import com.teamium.dto.DueDateDTO;
import com.teamium.dto.ExtraLineDTO;
import com.teamium.dto.FinancialDataDTO;
import com.teamium.dto.FinancialGraphDataDTO;
import com.teamium.dto.FunctionDTO;
import com.teamium.dto.InterventionDTO;
import com.teamium.dto.LayoutDTO;
import com.teamium.dto.LineDTO;
import com.teamium.dto.OrderDTO;
import com.teamium.dto.PersonDTO;
import com.teamium.dto.ProductionUnitDTO;
import com.teamium.dto.ProgramDTO;
import com.teamium.dto.ProgramEventDTO;
import com.teamium.dto.ProgramSchedulerDTO;
import com.teamium.dto.ProjectByStatusDTO;
import com.teamium.dto.ProjectDTO;
import com.teamium.dto.ProjectDropDownDTO;
import com.teamium.dto.ProjectFinancialDTO;
import com.teamium.dto.ProjectSchedulerDTO;
import com.teamium.dto.QuotationDTO;
import com.teamium.dto.RatedFunctionDTO;
import com.teamium.dto.RecordDTO;
import com.teamium.dto.RecordInformationDTO;
import com.teamium.dto.ResourceDTO;
import com.teamium.dto.SpreadsheetDataDTO;
import com.teamium.dto.StaffMemberDTO;
import com.teamium.dto.SupplierDTO;
import com.teamium.enums.ContractType;
import com.teamium.enums.ProjectStatus.ProjectFinancialStatusName;
import com.teamium.enums.ProjectStatus.ProjectStatusName;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.BookingRepository;
import com.teamium.repository.ChannelFormatRepository;
import com.teamium.repository.CompanyRepository;
import com.teamium.repository.ContractLineRepositiory;
import com.teamium.repository.FunctionRepository;
import com.teamium.repository.PersonRepository;
import com.teamium.repository.ProductionUnitRepository;
import com.teamium.repository.RecordHistoryRepository;
import com.teamium.repository.RecordRepository;
import com.teamium.repository.StaffMemberRepository;
import com.teamium.service.prod.resources.functions.RateService;
import com.teamium.service.prod.resources.suppliers.SupplierService;
import com.teamium.utils.CommonUtil;
import com.teamium.utils.EditionPdfUtil;

/**
 * A service class implementation for Budget, Booking, Order, Show controller
 * 
 * @author Teamium
 *
 */
@Service
public class RecordService {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private RecordRepository<Project> recordRepository;

	private CompanyRepository companyRepository;
	private AuthenticationService authenticationService;
	private ProductionUnitRepository productionUnitRepository;
	private FunctionRepository functionRepository;
	private StaffMemberRepository staffMemberRepository;
	private RecordHistoryRepository recordHistoryService;
	private CustomerService customerService;
	private ChannelService channelService;
	private PersonRepository<StaffMember> personRepository;
	private RateService rateService;
	private AvailabilityService availabilityService;
	private ResourceService resourceService;
	private BookingRepository bookingRepository;
	private EditionPdfUtil edtionPdfUtil;
	private PropConfig propConfig;
	private ContractLineRepositiory contractLineRepositiory;
	private FormatService formatService;
	private CategoryService categoryService;
	private MilestoneTypeService milestoneTypeService;
	private CurrencyService currencyService;
	private ChannelFormatRepository channelFormatRepository;
	private DueDateService dueDateService;

	@Autowired
	@Lazy
	private SupplierService supplierService;

	@Autowired
	@Lazy
	private CompanyService companyService;

	@Autowired
	@Lazy
	private BookingService bookingService;

	@Autowired
	@Lazy
	private EventService eventService;

	@Autowired
	@Lazy
	private EquipmentService equipmentService;

	@Autowired
	public RecordService(AuthenticationService authenticationService, RecordRepository<Project> recordRepository,
			CompanyRepository companyRepository, ProductionUnitRepository productionUnitRepository,
			FunctionRepository functionRepository, StaffMemberRepository staffMemberRepository,
			RecordHistoryRepository recordHistoryService, CustomerService customerService,
			ChannelService channelService, PersonRepository<StaffMember> personRepository, RateService rateService,
			AvailabilityService availabilityService, ResourceService resourceService,
			BookingRepository bookingRepository, EditionPdfUtil edtionPdfUtil, PropConfig propConfig,
			ContractLineRepositiory contractLineRepositiory, FormatService formatService,
			CategoryService categoryService, MilestoneTypeService milestoneTypeService, CurrencyService currencyService,
			ChannelFormatRepository channelFormatRepository, DueDateService dueDateService) {
		this.authenticationService = authenticationService;
		this.recordRepository = recordRepository;
		this.companyRepository = companyRepository;
		this.productionUnitRepository = productionUnitRepository;
		this.functionRepository = functionRepository;
		this.staffMemberRepository = staffMemberRepository;
		this.recordHistoryService = recordHistoryService;
		this.customerService = customerService;
		this.channelService = channelService;
		this.personRepository = personRepository;
		this.rateService = rateService;
		this.availabilityService = availabilityService;
		this.resourceService = resourceService;
		this.bookingRepository = bookingRepository;
		this.propConfig = propConfig;
		this.edtionPdfUtil = edtionPdfUtil;
		this.contractLineRepositiory = contractLineRepositiory;
		this.formatService = formatService;
		this.categoryService = categoryService;
		this.milestoneTypeService = milestoneTypeService;
		this.currencyService = currencyService;
		this.channelFormatRepository = channelFormatRepository;
		this.dueDateService = dueDateService;

	}

	/**
	 * Method to save or update Record
	 * 
	 * @param recordDTO the recordDTO
	 * 
	 * @throws MalformedURLException
	 */
	@Transactional
	public RecordDTO saveOrUpdateRecord(RecordDTO recordDTO, Class<?> discriminator) throws MalformedURLException {
		logger.info("Inside RecordService :: saveOrUpdateRecord(), To save or update :  "
				+ discriminator.getSimpleName() + ", record object : " + recordDTO);
		StaffMember loggedInUser = authenticationService.getAuthenticatedUser();
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to save or update record.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		// Setting default currency if empty.
		if (StringUtils.isBlank(recordDTO.getCurrency())) {
			recordDTO.setCurrency(currencyService.getDefaultCurrency().getCode());
		}
		Record record = null;
		Record dbRecord = null;
		XmlEntity statusEntity = new XmlEntity();

		String statusInProgress = ProjectStatusName.IN_PROGRESS.getProjectStatusNameString();
		String statusToDo = ProjectStatusName.TO_DO.getProjectStatusNameString();
		String statusDone = ProjectStatusName.DONE.getProjectStatusNameString();

		String statusApproved = ProjectFinancialStatusName.APPROVED.getProjectFinancialStatusNameString();
		String statusRejected = ProjectFinancialStatusName.REJECTED.getProjectFinancialStatusNameString();
		String statusRevised = ProjectFinancialStatusName.REVISED.getProjectFinancialStatusNameString();

		if (discriminator.equals(QuotationDTO.class)) {
			logger.info("Record to be saved is an instance of Quotation/Budget.");
			QuotationDTO quotationDTO = (QuotationDTO) recordDTO;
			if (recordDTO.getId() != null && recordDTO.getId() != 0) {
				record = this.validateRecordExistence(Quotation.class, recordDTO.getId());

				if (record.getStatus() != null && record.getStatus().getKey().equalsIgnoreCase(statusDone)) {
					// if status is "Done", cannot edit anything on budget
					QuotationDTO responseDTO = new QuotationDTO((Quotation) record);
					responseDTO.setRecordDiscriminator(Constants.BUDGET_STRING);
					return responseDTO;
				}

				String status = recordDTO.getStatus();
				if (recordDTO.getFollower() != null && recordDTO.getFollower().getId() != null) {
					StaffMember sf = staffMemberRepository.findOne(recordDTO.getFollower().getId());
					if (sf != null) {
						record.setFollower(sf);
					}
				} else if (record.getFollower() != null) {
					record.setFollower(null);
				}

				if (!StringUtils.isBlank(status)) {

					if ((status.equalsIgnoreCase(statusToDo) || status.equalsIgnoreCase(statusInProgress))
							&& !StringUtils.isBlank(recordDTO.getFinancialStatus())
							&& recordDTO.getFinancialStatus().equalsIgnoreCase(statusApproved)) {
						List<Line> dblines = record.getLines();
						validateRecord(record, Quotation.class, recordDTO);
						ProjectStatusName.getEnum(recordDTO.getStatus());
						statusEntity.setKey(recordDTO.getStatus());
						record.setStatus(statusEntity);
						if (!StringUtils.isBlank(recordDTO.getFinancialStatus())) {
							ProjectFinancialStatusName.getEnum(recordDTO.getFinancialStatus());
							record.setFinancialStatus(recordDTO.getFinancialStatus());
						}
						record.setLines(dblines);
					}
					if ((status.equalsIgnoreCase(statusToDo) || status.equalsIgnoreCase(statusInProgress))
							&& (StringUtils.isBlank(recordDTO.getFinancialStatus())
									|| (!StringUtils.isBlank(recordDTO.getFinancialStatus())
											&& recordDTO.getFinancialStatus().equalsIgnoreCase(statusRevised)))) {
						validateRecord(record, Quotation.class, recordDTO);
						ProjectStatusName.getEnum(recordDTO.getStatus());
						statusEntity.setKey(recordDTO.getStatus());
						record.setStatus(statusEntity);
						if (!StringUtils.isBlank(recordDTO.getFinancialStatus())) {
							ProjectFinancialStatusName.getEnum(recordDTO.getFinancialStatus());
							record.setFinancialStatus(recordDTO.getFinancialStatus());
						}

					}
				}

				record = quotationDTO.getQuotation((Quotation) record, (QuotationDTO) recordDTO);
			} else {
				record = quotationDTO.getQuotation(new Quotation(), (QuotationDTO) recordDTO);
				validateRecord(record, Quotation.class, recordDTO);
				if (recordDTO.getFollower() != null && recordDTO.getFollower().getId() != null) {
					StaffMember sf = staffMemberRepository.findOne(recordDTO.getFollower().getId());
					if (sf != null) {
						record.setFollower(sf);
					}
				} else if (record.getFollower() != null) {
					record.setFollower(null);
				}
				record.updateAmounts();
				record.setCalcTotalPriceIVAT(0f);
				record.setTotalPrice(0f);
				record.setTotalNetPrice(0f);
			}
			logger.info("Saving the quotation/budget.");

			// validate category
			record = this.validateProjectCategoryAndCurrency(recordDTO, record);
			record = this.updateStartAndEndTime(record);
			record = recordRepository.save(record);
			saveLoggedInUserRecordHistory(loggedInUser, record, Quotation.class);
			dbRecord = this.validateRecordContacts(record, recordDTO.getRecordContacts(), record);
			QuotationDTO responseDTO = new QuotationDTO((Quotation) dbRecord);
			responseDTO.setRecordDiscriminator(Constants.BUDGET_STRING);

			// check if budget has a booking to update
			if (responseDTO.getId() != null) {
				Project booking = recordRepository.getProjectByQuotationId(Project.class, responseDTO.getId());
				if (booking != null) {

					String bookingStatus = booking.getStatus() != null ? booking.getStatus().getKey() : "";
					if (!booking.getTitle().equalsIgnoreCase(responseDTO.getTitle())
							|| !bookingStatus.equalsIgnoreCase(responseDTO.getStatus())
							|| !(!StringUtils.isBlank(booking.getFinancialStatus()) && booking.getFinancialStatus()
									.equalsIgnoreCase(responseDTO.getFinancialStatus()))) {

						String status = responseDTO.getStatus();
						if (!StringUtils.isBlank(status)) {
							statusEntity.setKey(status);
							booking.setStatus(statusEntity);
						}
						booking.setFinancialStatus(responseDTO.getFinancialStatus());
						booking.setTitle(responseDTO.getTitle());

						// save changes in project-booking as per changes in title and status of
						// budget-quotation
						recordRepository.save(booking);
					}
				}
			}
			return responseDTO;

		} else if (discriminator.equals(ProjectDTO.class)) {
			logger.info("Record to be saved is an instance of Project.");
			ProjectDTO projectDTO = (ProjectDTO) recordDTO;
			if (recordDTO.getId() != null && recordDTO.getId() != 0) {
				record = validateRecordExistence(Project.class, recordDTO.getId());

				if (record.getStatus() != null && record.getStatus().getKey().equalsIgnoreCase(statusDone)) {
					// if status is "Done", cannot edit anything on booking
					ProjectDTO responseDTO = new ProjectDTO((Project) record);
					responseDTO.setRecordDiscriminator(Constants.BOOKING_STRING.toString());
					return responseDTO;
				}
				record = projectDTO.getProject((Project) record);
				if (record instanceof Project) {
					Project proj = (Project) record;
					proj.setTitle(projectDTO.getTitle());
				}

			} else {
				logger.info("Cannot create project without its budget");
				throw new UnprocessableEntityException("Cannot create project without its budget");
			}
			validateRecord(record, Project.class, recordDTO);
			if (recordDTO.getFollower() != null && recordDTO.getFollower().getId() != null) {
				StaffMember sf = staffMemberRepository.findOne(recordDTO.getFollower().getId());
				if (sf != null) {
					record.setFollower(sf);
				}
			} else if (record.getFollower() != null) {
				record.setFollower(null);
			}

			// if booking status is "Done", set also budget status "Done"
			if (record.getStatus() != null && record.getStatus().getKey().equalsIgnoreCase(statusDone)
					&& record.getSource() != null) {

				// check pending bookings
				List<Line> bookings = record.getLines();
				long count = 0;
				if (bookings != null && !bookings.isEmpty()) {
					count = bookings.stream().filter(line -> {
						if (line instanceof Booking) {
							Booking b = (Booking) line;
							if (b != null && b.getResource() instanceof DefaultResource) {
								return true;
							}
						}
						return false;
					}).collect(Collectors.counting());
				}
				if (count > 0) {
					logger.info("Pending bookings can not be done.");
					throw new UnprocessableEntityException("Pending bookings can not be done");
				}

				Record quotationBudget = record.getSource();
				if (quotationBudget.getStatus() != null
						&& !quotationBudget.getStatus().getKey().equalsIgnoreCase(statusDone)) {
					statusEntity.setKey(statusDone);
					quotationBudget.setStatus(statusEntity);
					recordRepository.save(quotationBudget);
				}
			}

			record.updateAmounts();
			// validate category
			record = this.validateProjectCategoryAndCurrency(recordDTO, record);
			record = this.updateStartAndEndTime(record);
			record.setTotalPrice(record.getCalcTotalPrice());
			record.setTotalPrice(record.getCalcTotalPrice());
			record.setCalcTotalPriceIVAT(record.getCalcTotalPrice());
			record = recordRepository.save(record);

			saveLoggedInUserRecordHistory(loggedInUser, record, Project.class);
			logger.info("Validate record contacts");
			dbRecord = this.validateRecordContacts(record, recordDTO.getRecordContacts(), record);
			Program program = null;
			// check its program and its all child sessions if "Done"
			if (dbRecord instanceof Project) {
				Project projectInstance = (Project) dbRecord;
				program = projectInstance.getProgram();
				if (program != null && program.getLinkedRecords() != null && !program.getLinkedRecords().isEmpty()) {
					int countDoneSessions = recordRepository.findSessionsCountByProgram(program.getId());
					if (countDoneSessions == program.getLinkedRecords().size()) {
						program.getStatus().setKey(statusDone);
						logger.info("All child sessions are Done, so program will be Done : programId : "
								+ program.getId() + " , on booking , bookingId : " + dbRecord.getId());
						recordRepository.save(program);
					}
				}
			}

			ProjectDTO responseDTO = new ProjectDTO((Project) dbRecord);
			if (program != null) {
				responseDTO.setProgram(new ProgramDTO(program));
			}
			responseDTO.setRecordDiscriminator(Constants.BOOKING_STRING.toString());
			return responseDTO;

		} else if (discriminator.equals(InterventionDTO.class)) {
			logger.info("Record to be saved is an instance of Intervention.");
			Intervention intervention = null;
			if (recordDTO.getId() != null && recordDTO.getId() != 0) {
				intervention = (Intervention) validateRecordExistence(Intervention.class, recordDTO.getId());
			} else {
				intervention = new Intervention();
			}
			InterventionDTO interventionDTO = (InterventionDTO) recordDTO;
			record = interventionDTO.getIntervention(intervention, interventionDTO);
			logger.info("Returning after saving the intervention.");
			record = recordRepository.save(record);
			saveLoggedInUserRecordHistory(loggedInUser, record, Intervention.class);

			if (record.getId() != null) {
				record = this.validateRecordExistence(Project.class, record.getId());
			}
			return new InterventionDTO((Intervention) record);

		} else if (discriminator.equals(ProgramDTO.class)) {

			logger.info("Record to be saved is an instance of Program/Show.");
			ProgramDTO programDTO = (ProgramDTO) recordDTO;

			if (recordDTO.getId() != null && recordDTO.getId() != 0) {
				record = (Program) validateRecordExistence(Program.class, recordDTO.getId());
				// check if booking exists for this particular budget

				String status = recordDTO.getStatus();
				if (recordDTO.getFollower() != null && recordDTO.getFollower().getId() != null) {
					StaffMember sf = staffMemberRepository.findOne(recordDTO.getFollower().getId());
					if (sf != null) {
						record.setFollower(sf);
					}
				} else if (record.getFollower() != null) {
					record.setFollower(null);
				}

				if (!StringUtils.isBlank(status)) {

					if ((status.equalsIgnoreCase(statusToDo) || status.equalsIgnoreCase(statusInProgress))
							&& !StringUtils.isBlank(recordDTO.getFinancialStatus())
							&& recordDTO.getFinancialStatus().equalsIgnoreCase(statusApproved)) {
						// freeze all details and lines & cannot edit show detail and lines
						List<Line> dblines = record.getLines();
						validateRecord(record, Program.class, recordDTO);
						ProjectStatusName.getEnum(recordDTO.getStatus());
						statusEntity.setKey(recordDTO.getStatus());
						record.setStatus(statusEntity);
						if (!StringUtils.isBlank(recordDTO.getFinancialStatus())) {
							ProjectFinancialStatusName.getEnum(recordDTO.getFinancialStatus());
							record.setFinancialStatus(recordDTO.getFinancialStatus());
						}
						record.setLines(dblines);
					}

					if ((status.equalsIgnoreCase(statusToDo) || status.equalsIgnoreCase(statusInProgress))
							&& (StringUtils.isBlank(recordDTO.getFinancialStatus())
									|| (!StringUtils.isBlank(recordDTO.getFinancialStatus())
											&& recordDTO.getFinancialStatus().equalsIgnoreCase(statusRevised)))) {
						// can edit show detail if null or revised
						record = programDTO.getProgram((Program) record);
						validateRecord(record, Program.class, recordDTO);
						ProjectStatusName.getEnum(recordDTO.getStatus());
						statusEntity.setKey(recordDTO.getStatus());
						record.setStatus(statusEntity);
						if (!StringUtils.isBlank(recordDTO.getFinancialStatus())) {
							ProjectFinancialStatusName.getEnum(recordDTO.getFinancialStatus());
							record.setFinancialStatus(recordDTO.getFinancialStatus());
						}
					}

					if ((status.equalsIgnoreCase(statusToDo) || status.equalsIgnoreCase(statusInProgress))
							&& (!StringUtils.isBlank(recordDTO.getFinancialStatus())
									&& recordDTO.getFinancialStatus().equalsIgnoreCase(statusRejected))) {
						record = programDTO.getProgram((Program) record);
						validateRecord(record, Program.class, recordDTO);
						ProjectStatusName.getEnum(recordDTO.getStatus());
						statusEntity.setKey(recordDTO.getStatus());
						record.setStatus(statusEntity);
						if (!StringUtils.isBlank(recordDTO.getFinancialStatus())) {
							ProjectFinancialStatusName.getEnum(recordDTO.getFinancialStatus());
							record.setFinancialStatus(recordDTO.getFinancialStatus());
						}
					}

				} else {
					record = programDTO.getProgram((Program) record);
				}
			} else {
				// means program is new
				record = programDTO.getProgram(new Program());
				if ((record.getLines() == null || record.getLines().isEmpty())
						&& (!StringUtils.isBlank(record.getFinancialStatus())
								&& record.getFinancialStatus().equalsIgnoreCase(statusApproved))) {
					logger.error("Cannot approve the show as no lines are added yet");
					throw new UnprocessableEntityException("Cannot approve the show as no lines are added yet");
				}
				validateRecord(record, Program.class, recordDTO);
			}

			record.updateAmounts();
			record = this.validateProjectCategoryAndCurrency(programDTO, record);
			record.setTotalPrice(record.getCalcTotalPrice());
			record.setTotalPrice(record.getCalcTotalPrice());
			record.setCalcTotalPriceIVAT(record.getCalcTotalPrice());

			if (programDTO.getFollower() != null && programDTO.getFollower().getId() != null) {
				StaffMember sf = staffMemberRepository.findOne(programDTO.getFollower().getId());
				if (sf != null) {
					record.setFollower(sf);
				} else {
					logger.error("Invalid follower id.");
					throw new UnprocessableEntityException("Invalid follower id.");
				}
			}

			record = recordRepository.save(record);

			saveLoggedInUserRecordHistory(loggedInUser, record, Program.class);
			dbRecord = this.validateRecordContacts(record, recordDTO.getRecordContacts(), record);

			ProgramDTO responseDTO = new ProgramDTO((Program) dbRecord);
			responseDTO.setRecordDiscriminator(Constants.SHOWS_STRING.toString());
			this.addAvailableResources(responseDTO);
			return responseDTO;

		}
		logger.info("Returning after saving the record.");
		return null;
	}

	/**
	 * Method to validate category and currency
	 * 
	 * @param record
	 * 
	 * @return the record object
	 */
	private Record validateProjectCategoryAndCurrency(RecordDTO recordDTO, Record record) {
		logger.info("Inside RecordService :: validateCategory(), To validate record category");
		if (!(record instanceof Program)) {
			AbstractProject abstractProject = (AbstractProject) record;
			AbstractProjectDTO abstractProjectDTO = (AbstractProjectDTO) recordDTO;
			String category = abstractProjectDTO.getCategory();
			if (!StringUtils.isBlank(category)) {
				Category categoryEntity = categoryService.findCategoryByName(category);
				if (category == null) {
					logger.info("Category not found with name : " + category);
					throw new NotFoundException("Category not found");
				}
				abstractProject.setCategory(categoryEntity);
			} else {
				abstractProject.setCategory(null);
			}
		}
		Currency currencyEntity = record.getCurrency();
		if (currencyEntity != null) {
			CurrencySymbol currency = currencyService.findCurrencyByCode(currencyEntity.getCurrencyCode());
			if (currency == null) {
				logger.info("Currency not found with code : " + currencyEntity.getCurrencyCode());
				throw new NotFoundException("Currency not found");
			}
		}
		return record;
	}

	/**
	 * Method to validate records-contacts
	 * 
	 * @param recordContacts
	 * @param record
	 * 
	 * @return the record
	 */
	private Record validateRecordContacts(Record previousRecord, Set<PersonDTO> recordContacts, Record record) {
		logger.info("Inside RecordService :: validateRecordContacts()");
		Set<Person> contacts = null;
		Record dbRecord = null;

		Company currentClient = record.getCompany();
		Company previousClient = previousRecord.getCompany();

		if (previousClient == null && currentClient != null) {
			Person mainContact = record.getCompany().getMainContact();
			List<Long> contactsId = record.getCompany().getContacts().stream().map(entity -> entity.getId())
					.collect(Collectors.toList());
			if (mainContact != null) {
				contactsId.add(mainContact.getId());
			}
			if (recordContacts != null && !recordContacts.isEmpty()) {
				contacts = new HashSet<Person>();
				for (PersonDTO staff : recordContacts) {
					Person person = this.personRepository.findOne(staff.getId().longValue());
					if (person == null) {
						logger.info("Contact not found with id + " + staff.getId());
						throw new NotFoundException("Contact not found with id { " + staff.getId() + " }.");
					}
					if (!contactsId.contains(person.getId())) {
						String firstName = StringUtils.isBlank(person.getFirstName()) ? "" : person.getFirstName();
						String lastName = StringUtils.isBlank(person.getName()) ? "" : person.getName();
						logger.info("Contact with name :" + firstName + " " + lastName
								+ " is not a contact of client : " + record.getCompany().getName());
						throw new UnprocessableEntityException("Contact with name :" + firstName + " " + lastName
								+ " is not a contact of client : " + record.getCompany().getName());
					}
					contacts.add(person);
				}
			}
			record.setRecordContacts(contacts);

		} else {
			if (currentClient == null) {
				// clear all contacts if current company is null
				record.getRecordContacts().clear();

			} else if (previousClient.getId().longValue() != currentClient.getId().longValue()) {
				// clear all contacts if clients are not same on record
				record.getContacts().clear();
				if (recordContacts != null && !recordContacts.isEmpty()) {
					Person mainContact = record.getCompany().getMainContact();
					List<Long> contactsId = record.getCompany().getContacts().stream().map(entity -> entity.getId())
							.collect(Collectors.toList());
					if (mainContact != null) {
						contactsId.add(mainContact.getId());
					}
					contacts = new HashSet<Person>();
					for (PersonDTO staff : recordContacts) {
						Person person = this.personRepository.findOne(staff.getId().longValue());
						if (person == null) {
							logger.info("Contact not found with id + " + staff.getId());
							throw new NotFoundException("Contact not found with id { " + staff.getId() + " }.");
						}
						if (!contactsId.contains(person.getId())) {
							String firstName = StringUtils.isBlank(person.getFirstName()) ? "" : person.getFirstName();
							String lastName = StringUtils.isBlank(person.getName()) ? "" : person.getName();
							logger.info("Contact with name :" + firstName + " " + lastName
									+ " is not a contact of client : " + record.getCompany().getName());
							throw new UnprocessableEntityException("Contact with name :" + firstName + " " + lastName
									+ " is not a contact of client : " + record.getCompany().getName());
						}
						contacts.add(person);
					}
				}

			} else {
				if (recordContacts != null && !recordContacts.isEmpty()) {
					Person mainContact = record.getCompany().getMainContact();
					List<Long> contactsId = record.getCompany().getContacts().stream().map(entity -> entity.getId())
							.collect(Collectors.toList());
					if (mainContact != null) {
						contactsId.add(mainContact.getId());
					}
					contacts = new HashSet<Person>();
					for (PersonDTO staff : recordContacts) {
						Person person = this.personRepository.findOne(staff.getId().longValue());
						if (person == null) {
							logger.info("Contact not found with id + " + staff.getId());
							throw new NotFoundException("Contact not found with id { " + staff.getId() + " }.");
						}
						if (!contactsId.contains(person.getId())) {
							String firstName = StringUtils.isBlank(person.getFirstName()) ? "" : person.getFirstName();
							String lastName = StringUtils.isBlank(person.getName()) ? "" : person.getName();
							logger.info("Contact with name :" + firstName + " " + lastName
									+ " is not a contact of client : " + record.getCompany().getName());
							throw new UnprocessableEntityException("Contact with name :" + firstName + " " + lastName
									+ " is not a contact of client : " + record.getCompany().getName());
						}
						contacts.add(person);
					}
				}
				record.setRecordContacts(contacts);
			}
		}

		try {
			dbRecord = this.recordRepository.save(record);
		} catch (Exception e) {
			logger.info("Failed saving record contacts");
			throw new UnprocessableEntityException("Failed saving record contacts");
		}
		logger.info("Returning after saving record-contacts on record");
		return dbRecord;
	}

	/**
	 * Method to save record history
	 * 
	 * @param loggedInUser the loggedInUser
	 * 
	 * @param record       the record
	 */
	private void saveLoggedInUserRecordHistory(StaffMember loggedInUser, Record record, Class<?> discriminator) {
		logger.info("Inside saveLoggedInUserRecordHistory() : loggedInUser id = " + loggedInUser.getId() + ""
				+ ", record id = " + record.getId() + ", discriminator : " + discriminator);
		RecordHistory history = null;
		if (record.getId() != null && record.getId() != 0) {
			Set<RecordHistory> recordsHistory = loggedInUser.getRecordHistory();
			Optional<RecordHistory> rec = recordsHistory.stream().filter(dt -> dt.getRecord().getId() == record.getId())
					.findFirst();
			try {
				history = rec.get();
			} catch (Exception e) {
				history = new RecordHistory();
			}
			history.setDateViewed(Calendar.getInstance());
			history.setRecordDiscriminator(discriminator.getSimpleName().toLowerCase());
			history.setRecord(record);
			recordsHistory.add(history);
			loggedInUser.setRecordHistory(recordsHistory);
			staffMemberRepository.save(loggedInUser);
			logger.info("Successfully save staff record history.");
		}
		logger.info("Returning after saving record history.");
	}

	/**
	 * Method to validate record
	 * 
	 * @param record the record
	 */
	private Record validateRecord(Record record, Class<?> discriminator, RecordDTO recordDTO) {
		logger.info("Inside RecordService :: validateRecord(), To validate record : " + recordDTO);

		AbstractProject project = (AbstractProject) record;
		AbstractProjectDTO projectDTO = (AbstractProjectDTO) recordDTO;
		AtomicBoolean isRemove = new AtomicBoolean(false);

		// validate record-title
		if (StringUtils.isBlank(projectDTO.getTitle())) {
			logger.info("Please provide a valid project title");
			throw new UnprocessableEntityException("Please provide a valid project title");
		}

		// assigning sale-entity on record
		CompanyDTO client = recordDTO.getCompany();
		if (client != null && client.getId() != null) {
			Company company = companyRepository.getCompanyById(Customer.class, client.getId());
			record.setCompany(company);
		} else {
			record.setCompany(null);
		}

		// validate company on project
		CompanyDTO saleEntity = recordDTO.getSaleEntity();
		if (saleEntity != null) {
			if (saleEntity.getId() == null) {
				logger.info("Invalid company id");
				throw new UnprocessableEntityException("Invalid company id");
			}
			Company company = companyRepository.getCompanyById(Company.class, saleEntity.getId());
			if (company == null) {
				logger.info("Company not found with id : " + saleEntity.getId());
				throw new UnprocessableEntityException("Company not found");
			}
			record.setEntity(company);
		} else {
			// check if only a single company is present on this SmartERP
			List<Company> companies = companyService.getAllCompanyByDiscriminator(Company.class);
			if (companies == null || companies.isEmpty()) {
				logger.info("Company not created yet. Please create atleast one company from configuration");
				throw new UnprocessableEntityException(
						"Company not created yet. Please create atleast one company from configuration");
			} else if (companies.size() == 1) {
				// automatically assign company on project
				record.setEntity(companies.get(0));
			} else {
				logger.info("Please assign company");
				throw new UnprocessableEntityException("Please assign company");
			}
		}

		if (discriminator.equals(Project.class) || discriminator.equals(Quotation.class)) {
			logger.info("Validating record instance of quotation or project");

			QuotationDTO instanceDTO = (QuotationDTO) recordDTO;
			Quotation instanceEntity = (Quotation) project;

			// assigning production-unit
			ProductionUnitDTO prodUnit = instanceDTO.getProductionUnit();
			if (prodUnit != null && prodUnit.getId() != null) {
				ProductionUnit productionEntity = productionUnitRepository.findOne(prodUnit.getId());
				instanceEntity.setProductionUnit(productionEntity);
			} else {
				instanceEntity.setProductionUnit(null);
			}

			// assigning channel
			ChannelDTO channel = instanceDTO.getChannel();
			if (channel != null && channel.getId() != null) {
				Channel channelEntity = (Channel) companyRepository.getCompanyById(Channel.class, channel.getId());
				instanceEntity.setChannel(channelEntity);
			} else {
				instanceEntity.setChannel(null);
			}

			// assigning program to abstract-project
			// setting theme on budget and booking from program-show
			ProgramDTO programDTO = projectDTO.getProgram();
			if (programDTO != null) {
				Program entity = (Program) this.validateRecordExistence(Program.class, programDTO.getId());
				if (entity != null) {
					project.setProgram(entity);
					project.setTheme(entity.getTheme());
					project.setProgramId(entity.getId());
				} else {
					project.setProgram(null);
					project.setProgramId(null);
				}
			} else {
				project.setProgram(null);
				project.setProgramId(null);
				if (StringUtils.isBlank(projectDTO.getTheme())) {
					logger.info("Please provide valid theme");
					throw new UnprocessableEntityException("Please provide valid theme");
				} else {
					// setting the theme if program is null
					Theme themeEntity = new Theme();
					themeEntity.setKey(projectDTO.getTheme());
					project.setTheme(themeEntity);
				}
			}

		} else if (discriminator.equals(Program.class)) {
			// checking a valid theme on record
			if (StringUtils.isBlank(projectDTO.getTheme())) {
				logger.info("Please provide valid theme");
				throw new UnprocessableEntityException("Please provide valid theme");
			}
			ProgramDTO dto = (ProgramDTO) projectDTO;
			if (dto.getYear() != null && ((dto.getYear() > 9999) || (dto.getYear() < 1000))) {
				logger.error("Year should be of 4 digit");
				throw new UnprocessableEntityException("Year should be of 4 digit");
			}
		}

		// assigning customer-agency
		CustomerDTO agencyDTO = projectDTO.getAgency();
		if (agencyDTO != null) {
			Customer entity = (Customer) companyRepository.getCompanyById(Customer.class, agencyDTO.getId());
			project.setAgency(entity);
		} else {
			project.setAgency(null);
		}

		List<Line> dbLines = project.getLines();
		List<BookingDTO> linesList = recordDTO.getLines() != null ? recordDTO.getLines() : new ArrayList<BookingDTO>();

		if (linesList != null && !linesList.isEmpty()) {
			record.setLines(linesList.stream().map(dto -> {

				// removing lines not present in dto
				if (dbLines != null && !dbLines.isEmpty()) {
					dbLines.removeIf(dt -> {
						isRemove.set(true);
						linesList.forEach(dt1 -> {
							if (dt1.getId() != null && dt.getId() != null && dt1.getId().equals(dt.getId())) {
								isRemove.set(false);
								return;
							}
						});
						return isRemove.get();
					});
				}

				if (dto.getFrom() == null || dto.getTo() == null) {
					logger.info("Please provide valid budget-line date : " + dto.getId());
					throw new UnprocessableEntityException("Please provide valid budget-line date");
				}

				Line entity = validateLineCalculation(record, dto, discriminator);
				return entity;

			}).collect(Collectors.toList()));

		} else {
			// remove all lines from project if line-dto is null or empty
			if (record.getLines() != null) {
				record.getLines().clear();
			}
		}

		// setting due-dates/milestones
		Set<DueDate> dbDueDates = record.getDueDates();
		Set<DueDateDTO> dueDatesList = recordDTO.getDueDates();

		if (dueDatesList != null && !dueDatesList.isEmpty()) {

			record.setDueDates(dueDatesList.stream().map(dto -> {

				// removing due-dates not present in dto
				if (dbDueDates != null && !dbDueDates.isEmpty()) {
					dbDueDates.removeIf(dt -> {
						isRemove.set(true);
						dueDatesList.forEach(dt1 -> {
							if (dt1.getId() != null && dt.getId() != null
									&& dt1.getId().longValue() == dt.getId().longValue()) {
								isRemove.set(false);
								return;
							}
						});
						if (isRemove.get()) {
							dt.setRecord(null);
							dt.setType(null);
							this.dueDateService.unassignDueDate(dt.getId());
						}
						return isRemove.get();
					});
				}

				// check the existing line
				DueDate entity = null;
				try {
					entity = dto.setDueDateDetail(new DueDate(),
							authenticationService.getCalanderTime(dto.getDueDate()));
				} catch (Exception e) {
				}
				if (dto.getId() != null) {
					Optional<DueDate> list = dbDueDates.stream()
							.filter(dueDate1 -> dueDate1.getId().longValue() == dto.getId().longValue()).findFirst();
					if (list.isPresent()) {
						entity = list.get();
					}
				}
				entity.setId(dto.getId());
				entity.setRecord(record);
				entity.setDate(authenticationService.getCalanderTime(dto.getDueDate()));
				if (!StringUtils.isBlank(dto.getType())) {
					MilestoneType milestoneEntity = milestoneTypeService
							.findMilestoneTypeByNameAndDiscriminator(dto.getType(), Constants.PROJECT_STRING);
					if (milestoneEntity == null) {
						logger.info("Please provide valid milestone name : " + dto.getType());
						throw new UnprocessableEntityException("Please provide valid milestone name");
					}
					entity.setType(milestoneEntity);
				} else {
					logger.info("Please provide valid milestone name");
					throw new UnprocessableEntityException("Please provide valid milestone name");
				}

				return entity;
			}).collect(Collectors.toSet()));

		} else {
			// remove all milestones/due-dates from project if due-dates is null or empty
			if (record.getDueDates() != null && !record.getDueDates().isEmpty()) {
				record.getDueDates().stream().forEach(due -> due.setRecord(null));
				record.getDueDates().clear();
			}
		}

		// setting channel-formats
		Set<ChannelFormat> dbChannelFormat = project.getChannelFormat();
		Set<ChannelFormatDTO> channelFormatsList = recordDTO.getChannelFormat();

		if (channelFormatsList != null && !channelFormatsList.isEmpty()) {
			record.setChannelFormat(channelFormatsList.stream().map(dto -> {

				// removing channel-Formats not present in dto
				if (dbChannelFormat != null && !dbChannelFormat.isEmpty()) {
					dbChannelFormat.removeIf(dt -> {
						isRemove.set(true);
						channelFormatsList.forEach(dt1 -> {
							if (dt1.getId() != null && dt.getId() != null
									&& dt1.getId().longValue() == dt.getId().longValue()) {
								isRemove.set(false);
								return;
							}
						});
						return isRemove.get();
					});
				}

				// check the existing channel-format
				ChannelFormat entity = new ChannelFormat();
				try {
					entity = dto.getChannelFormat(entity);
				} catch (Exception e) {
				}
				if (dto.getId() != null) {
					Optional<ChannelFormat> list = dbChannelFormat.stream()
							.filter(format1 -> format1.getId().longValue() == dto.getId().longValue()).findFirst();
					if (list.isPresent()) {
						entity = list.get();
					}
				}
				entity.setId(dto.getId());
				if (dto.getChannel() == null || dto.getChannel().getId() == null) {
					logger.info("Please provide valid channel.");
					throw new NotFoundException("Please provide valid channel.");
				}
				Channel channel = channelService.findChannelById(dto.getChannel().getId());
				if (channel == null) {
					logger.info("Channel not found with id : " + dto.getChannel().getId());
					throw new NotFoundException("Channel not found");
				}
				entity.setChannel(channel);
				Set<Long> formatIds = channel.getFormats().stream().map(form -> form.getId())
						.collect(Collectors.toSet());
				if (!StringUtils.isBlank(dto.getFormat())) {
					Format formatEntity = formatService.findFormatByName(dto.getFormat());
					if (formatEntity == null) {
						logger.info("Please provide valid channel format : " + dto.getFormat());
						throw new UnprocessableEntityException("Please provide valid channel format");
					}
					if (!formatIds.contains(formatEntity.getId())) {
						logger.info("The selected format : " + dto.getFormat() + ", is not a format of channel : "
								+ channel.getName());
						throw new UnprocessableEntityException("The selected format : " + dto.getFormat()
								+ ", is not a format of channel : " + channel.getName());
					}
					entity.setFormat(formatEntity);
				} else {
					logger.info("Please provide valid channel format : " + dto.getFormat());
					throw new UnprocessableEntityException("Please provide valid channel format");
				}

				return entity;
			}).collect(Collectors.toSet()));

		} else {
			// remove all
			if (record.getChannelFormat() != null) {
				record.getChannelFormat().clear();
			}
		}

		if (record.getId() == null || record.getId() == 0) {

			// Quotation quotation = (Quotation) record;
			// List<Promo> promos = quotation.getPromoList();
			// if (promos != null && !promos.isEmpty()) {
			// promos.stream().forEach(promo -> {
			// if (promo.getId() != null && promo.getId() != 0) {
			// logger.info("Promo already exist : id : " + promo.getId());
			// throw new UnprocessableEntityException("Promo already exist");
			// }
			// });
			// }

			Set<DueDate> dueDates = record.getDueDates();
			if (dueDates != null && !dueDates.isEmpty()) {
				dueDates.stream().forEach(dueDate -> {
					if (dueDate.getId() != null && dueDate.getId() != 0) {
						logger.info("Due Date already exist : id : " + dueDate.getId());
						throw new UnprocessableEntityException("Due Date already exist");
					}
				});
			}
			Set<RecordFee> fees = record.getFees();
			if (fees != null && !fees.isEmpty()) {
				fees.stream().forEach(fee -> {
					if (fee.getId() != null && fee.getId() != 0) {
						logger.info("Fee already exist : id : " + fee.getId());
						throw new UnprocessableEntityException("Fee already exist");
					}
				});
			}

			List<Line> lines = record.getLines();
			if (lines != null && !lines.isEmpty()) {
				lines.stream().forEach(line -> {
					if (line.getId() != null && line.getId() != 0) {
						logger.info("Line already exist : id : " + line.getId());
						throw new UnprocessableEntityException("Line already exist");
					}
				});
			}
			List<ContactRecord> contacts = record.getContacts();
			if (contacts != null && !contacts.isEmpty()) {
				contacts.stream().forEach(contact -> {
					if (contact.getRecord() != null && contact.getRecord() != 0) {
						logger.info("Contact already exist");
						throw new UnprocessableEntityException("Contact already exist");
					}
				});
			}
		}

		// validate project status
		if (StringUtils.isBlank(recordDTO.getStatus())) {
			logger.info("Invalid project status : " + recordDTO.getStatus());
			throw new UnprocessableEntityException("Invalid project status");
		}
		ProjectStatusName.getEnum(recordDTO.getStatus());

		// validate project financial status
		if (!StringUtils.isBlank(recordDTO.getFinancialStatus())) {
			ProjectFinancialStatusName.getEnum(recordDTO.getFinancialStatus());
		}

		// validate record-information
		if (recordDTO.getInformation() != null) {
			if (!StringUtils.isBlank(recordDTO.getInformation().getFormat())) {
				if (!CommonUtil.getProjectFormats().contains(recordDTO.getInformation().getFormat())) {
					logger.info("Invalid information format : " + recordDTO.getInformation().getFormat());
					throw new UnprocessableEntityException("Invalid format.");
				}
			}
		}

		logger.info("Returning after validating Record.");
		return record;
	}

	/**
	 * Method to get records
	 * 
	 * @return list of records
	 */
	public List<RecordDTO> getRecords(Class<?> discriminator) {
		logger.info("Inside getRecords()");
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get all records.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		List<RecordDTO> records = recordRepository.getRecords(discriminator).stream().map(record -> {
			if (discriminator.equals(Project.class)) {
				Project entity = (Project) record;
				return new ProjectDTO(entity, Constants.BOOKING_STRING.toString());

			} else if (discriminator.equals(Quotation.class)) {
				Quotation entity = (Quotation) record;
				return new QuotationDTO(entity, Constants.BUDGET_STRING.toString());

			} else if (discriminator.equals(Order.class)) {
				Order entity = (Order) record;
				return new OrderDTO(entity, Constants.ORDER_STRING.toString());
			}
			return null;
		}).collect(Collectors.toList());
		logger.info("Returning after getting records.");
		return records;
	}

	/**
	 * Method to get Record by Id
	 * 
	 * @param recordId the recordId
	 * 
	 * @return the recordDTO
	 */
	public RecordDTO findRecordById(Class<?> discriminator, long recordId) {
		logger.info("Inside findRecordById() " + recordId);
		StaffMember loggedInUser = authenticationService.getAuthenticatedUser();
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to find record by id.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		Record record = this.validateRecordExistence(discriminator, recordId);
		logger.info("Returning after finding record by id.");
		if (discriminator.equals(Project.class)) {
			ProjectDTO project = new ProjectDTO((Project) record);
			project.setRecordDiscriminator(Constants.BOOKING_STRING.toString());
			// Add available resource for each line
			this.addAvailableResources(project);

			// adding template if exists from this booking-project
			Record layout = recordRepository.getLayoutByProjectId(Layout.class, project.getId());
			if (layout != null) {
				if (layout instanceof Layout) {
					Layout template = (Layout) layout;
					LayoutDTO dto = new LayoutDTO(template, true);
					project.setTemplateLayout(dto);
				} else {
					logger.info("Fetched template is not an instance of layout");
					throw new UnprocessableEntityException("Fetched template is not an instance of layout");
				}
			}
			return project;

		} else if (discriminator.equals(Quotation.class)) {
			saveLoggedInUserRecordHistory(loggedInUser, record, Quotation.class);
			QuotationDTO quotation = new QuotationDTO((Quotation) record);
			quotation.setRecordDiscriminator(Constants.BUDGET_STRING.toString());
			// Add available resource for each line
			this.addAvailableResources(quotation);

			// adding template if exists from this budget
			Record layout = recordRepository.getLayoutByProjectId(Layout.class, quotation.getId());
			if (layout != null) {
				if (layout instanceof Layout) {
					Layout template = (Layout) layout;
					LayoutDTO dto = new LayoutDTO(template, true);
					quotation.setTemplateLayout(dto);
				} else {
					logger.info("Fetched template is not an instance of layout");
					throw new UnprocessableEntityException("Fetched template is not an instance of layout");
				}
			}

			return quotation;

		} else if (discriminator.equals(Program.class)) {
			saveLoggedInUserRecordHistory(loggedInUser, record, Program.class);
			ProgramDTO program = new ProgramDTO((Program) record);
			program.setRecordDiscriminator(Constants.SHOWS_STRING.toString());
			// Add available resource for each line
			this.addAvailableResources(program);

			// adding template if exists for this budget
			Record layout = recordRepository.getLayoutByProjectId(Layout.class, program.getId());
			if (layout != null) {
				if (layout instanceof Layout) {
					Layout template = (Layout) layout;
					LayoutDTO dto = new LayoutDTO(template, true);
					program.setTemplateLayout(dto);
				} else {
					logger.info("Fetched template is not an instance of layout");
					throw new UnprocessableEntityException("Fetched template is not an instance of layout");
				}
			}

			return program;

		} else if (discriminator.equals(Order.class)) {
			// saveLoggedInUserRecordHistory(loggedInUser, record, Order.class);
			OrderDTO order = new OrderDTO((Order) record);
			order.setRecordDiscriminator(Constants.ORDER_STRING.toString());
			return order;
		}
		logger.info("Returning after finding record.");
		return null;
	}

	/**
	 * Method to delete Record by Id
	 * 
	 * @param recordId the recordId
	 */
	@Transactional
	public void deleteRecordById(Class<?> discriminator, long recordId) {
		logger.info("Inside RecordService :: deleteRecordById() " + recordId);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to delete record.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		// check if quotation exists
		Record project = this.validateRecordExistence(discriminator, recordId);

		// check if budget has a booking
		if (discriminator.equals(Quotation.class)) {
			logger.info("Deleting record is an instance of Program");
			RecordDTO record = findRecordById(discriminator, recordId);
			if (record.getSource() != null) {
				ProjectDTO booking = this.getProjectByQuotationId(recordId);
				if (booking != null) {
					logger.info("Can't delete budget because it has been already booked : " + booking);
					throw new UnprocessableEntityException("Can't delete budget because it has been already booked");
				}
			}
		}

		// check if show has sessions
		if (discriminator.equals(Program.class)) {
			logger.info("Deleting record is an instance of Program");
			if (project instanceof Program) {
				Program program = (Program) project;

				XmlEntity statusEntity = program.getStatus();
				String financialStatus = program.getFinancialStatus();
				if (statusEntity == null) {
					logger.info("Invalid status");
					throw new UnprocessableEntityException("Invalid status");
				}
				String status = statusEntity.getKey();

				String statusToDo = ProjectStatusName.TO_DO.getProjectStatusNameString();
				String statusInProgress = ProjectStatusName.IN_PROGRESS.getProjectStatusNameString();

				if (!(status.equalsIgnoreCase(statusToDo) || status.equalsIgnoreCase(statusInProgress))) {
					logger.info("Cannot delete show due to invalid status");
					throw new UnprocessableEntityException("Cannot delete show due to invalid status");
				}
				if (!StringUtils.isBlank(financialStatus)) {
					logger.info("Cannot delete show due to financial status : " + financialStatus);
					throw new UnprocessableEntityException(
							"Cannot delete show due to financial status : " + financialStatus);
				}

				if (program.getLinkedRecords() != null && !program.getLinkedRecords().isEmpty()) {
					logger.info("Show cannot be deleted because its session has been created");
					throw new UnprocessableEntityException(
							"Show cannot be deleted because its session has been created");
				}
			} else {
				logger.info("Record found is not an instance of Program");
				throw new UnprocessableEntityException("Record found is not an instance of Program");
			}
		}

		if (discriminator.equals(Project.class)) {
			logger.info("Deleting record is an instance of Project");
			// Getting orders by the given project id.
			List<Record> orders = recordRepository.findRecordByProjectId(Order.class, recordId);
			if (orders != null && !orders.isEmpty()) {
				logger.info("Cannot delete booking because order(s) exists for this booking.");
				throw new UnprocessableEntityException(
						"Cannot delete booking because order(s) exists for this booking.");
			}
		}

		// delete its history
		try {
			recordHistoryService.deleteRecordHistoryByRecordId(recordId);
		} catch (Exception e) {
		}
		// if (recHistory != null) {
		// //recHistory.setRecord(null);
		// recordHistoryService.delete(recHistory);
		// }
		recordRepository.delete(recordId);
		logger.info("Returning after deleting record");
	}

	/**
	 * To validate record existence.
	 * 
	 * @param recordId the recordId.
	 * 
	 * @return the Record object.
	 */
	public Record validateRecordExistence(Class<?> discriminator, long recordId) {
		logger.info("Inside RecordService :: validateRecordExistence() " + recordId);
		Record record = recordRepository.getRecordById(discriminator, recordId);
		if (record == null) {
			logger.warn(discriminator.getSimpleName() + " not found with given id : " + recordId);
			throw new NotFoundException(discriminator.getSimpleName() + " not found");
		}
		Program show = null;
		if (record.getProgramId() != null) {
			Record showInstance = recordRepository.findOne(record.getProgramId());
			if (showInstance != null && showInstance instanceof Program) {
				show = (Program) showInstance;
			}
		}
		if (discriminator.equals(Project.class)) {
			Project project = (Project) record;
			project.setProgram(show);
			return project;
		} else if (discriminator.equals(Quotation.class)) {
			Quotation quotation = (Quotation) record;
			quotation.setProgram(show);
			return quotation;
		} else if (discriminator.equals(Program.class)) {
			Program program = (Program) record;
			program.setProgram(show);
			return program;
		}
		logger.info("Returning after validating Project.");
		return record;
	}

	/**
	 * Method to get records by status(To Do, In Progress, Done)
	 * 
	 * @param discriminator(Budget, Project, or Program)
	 * 
	 * @return list of records by status
	 */
	public Map<String, List<RecordDTO>> findAllRecordsByStatus(Class<?> discriminator) {
		logger.info("Inside RecordService :: findAllRecordsByStatus() : discriminator : " + discriminator);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get records by status.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		Map<String, List<RecordDTO>> recordsByStatus = new HashMap<String, List<RecordDTO>>();

		if (discriminator.equals(Quotation.class)) {
			logger.info("Getting records by status : discriminator : " + discriminator);
			List<RecordDTO> recordsToDoAndDone = recordRepository.getRecordsToDoAndDone(discriminator).stream()
					.map(record -> {
						Layout template = null;
						String templateName = null;
						if (discriminator.equals(Project.class)) {
							Project entity = (Project) record;
							template = recordRepository.getLayoutsByDiscriminatorAndSource(Layout.class,
									entity.getId());
							if (template != null) {
								templateName = template.getTitle();
							}
							return new ProjectDTO(entity.getId(),
									entity.getCategory() == null ? null : entity.getCategory().getName(),
									entity.getTitle(), entity.getStatus() == null ? null : entity.getStatus().getKey(),
									Constants.BOOKING_STRING.toString(), templateName,
									entity.getTheme() == null ? null : entity.getTheme().getKey());
						} else if (discriminator.equals(Quotation.class)) {
							Quotation entity = (Quotation) record;
							template = recordRepository.getLayoutsByDiscriminatorAndSource(Layout.class,
									entity.getId());
							if (template != null) {
								templateName = template.getTitle();
							}
							return new QuotationDTO(entity, Constants.BUDGET_STRING.toString(), templateName);
						}
						return null;
					}).collect(Collectors.toList());

			List<RecordDTO> recordInProgress = recordRepository.getRecordsByProgress(Project.class).stream()
					.map(rec -> {
						Project entity = (Project) rec;
						Layout template = null;
						String templateName = null;
						template = recordRepository.getLayoutsByDiscriminatorAndSource(Layout.class, entity.getId());
						if (template != null) {
							templateName = template.getTitle();
						}
						List<Line> bookings = entity.getLines();
						long count = 0;
						if (bookings != null && !bookings.isEmpty()) {
							count = bookings.stream().filter(line -> {
								if (line instanceof Booking) {
									Booking b = (Booking) line;
									if (b != null && b.getResource() instanceof DefaultResource) {
										return true;
									}
								}
								return false;
							}).collect(Collectors.counting());
						}
						return new ProjectDTO(entity, Constants.BOOKING_STRING.toString(), count, templateName);
					}).collect(Collectors.toList());

			recordsByStatus = recordsToDoAndDone.stream().map(dto -> dto).filter(dto -> dto.getStatus() != null)
					.collect(Collectors.groupingBy(RecordDTO::getStatus));

			recordsByStatus.put(ProjectStatusName.IN_PROGRESS.getProjectStatusNameString(), recordInProgress);

		} else if (discriminator.equals(Program.class)) {
			logger.info("Getting records by status : discriminator : " + discriminator);
			List<RecordDTO> programsList = recordRepository.getRecords(discriminator).stream().map(record -> {
				if (discriminator.equals(Program.class)) {
					Program entity = (Program) record;
					long countPendingBookingProjects = 0;
					List<AbstractProject> linkedRecords = entity.getLinkedRecords();
					if (linkedRecords != null && !linkedRecords.isEmpty()) {

						countPendingBookingProjects = linkedRecords.stream().filter(linkRec -> {

							if (linkRec instanceof Project) {
								Project bookingProject = (Project) linkRec;
								List<Line> bookings = bookingProject.getLines();
								long count = 0;
								String status = ProjectStatusName.IN_PROGRESS.getProjectStatusNameString();
								String financialStatus = ProjectFinancialStatusName.APPROVED
										.getProjectFinancialStatusNameString();
								XmlEntity statusEntity = entity.getStatus();

								if (statusEntity != null && statusEntity != null
										&& statusEntity.getKey().equalsIgnoreCase(status)
										&& !StringUtils.isBlank(bookingProject.getFinancialStatus())
										&& bookingProject.getFinancialStatus().equalsIgnoreCase(financialStatus)
										&& bookings != null && !bookings.isEmpty()) {

									count = bookings.stream().filter(line -> {
										if (line instanceof Booking) {
											Booking b = (Booking) line;
											if (b != null && b.getResource() instanceof DefaultResource) {
												return true;
											}
										}
										return false;
									}).collect(Collectors.counting());

								}

								return true;
							}
							return false;

						}).collect(Collectors.counting());

					}

					return new ProgramDTO(entity, entity.getTitle(),
							entity.getStatus() == null ? null : entity.getStatus().getKey(),
							Constants.SHOWS_STRING.toString(), countPendingBookingProjects);
				}
				return null;
			}).collect(Collectors.toList());

			recordsByStatus = programsList.stream().map(dto -> dto).filter(dto -> dto.getStatus() != null)
					.collect(Collectors.groupingBy(RecordDTO::getStatus));
		}

		logger.info("Returning after getting records : discriminator" + discriminator + " by status");
		return recordsByStatus;
	}

	/**
	 * Method to get list of recent viewed records(Budget, Booking, or Program)
	 * 
	 * @param discriminator
	 * 
	 * @return list of recent viewed records
	 */
	public List<RecordDTO> getRecentViewedRecords(Class<?> discriminator, long count) {
		logger.info("Inside RecordService :: getRecentViewedProjects() : discriminator : " + discriminator);
		StaffMember loggedInUser = authenticationService.getAuthenticatedUser();
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get records history.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}

		List<RecordHistory> recordsHistory = new ArrayList<RecordHistory>();
		recordsHistory = loggedInUser.getRecordHistory().stream()
				.filter(history -> history.getRecordDiscriminator().toLowerCase()
						.equalsIgnoreCase(discriminator.getSimpleName().toLowerCase()))
				.sorted((o1, o2) -> -1 * o1.getDateViewed().compareTo(o2.getDateViewed())).limit(count == 0 ? 4 : count)
				.collect(Collectors.toList());

		List<RecordDTO> recentViewedProjects = new ArrayList<RecordDTO>();
		recentViewedProjects = recordsHistory.stream().map(history -> {
			if (discriminator.equals(Project.class)) {
				Project entity = (Project) this.validateRecordExistence(discriminator, history.getRecord().getId());
				ProjectDTO dto = new ProjectDTO(entity, Constants.BOOKING_STRING.toString());
				return dto;
			} else if (discriminator.equals(Quotation.class)) {

				Quotation entity = (Quotation) this.validateRecordExistence(discriminator, history.getRecord().getId());
				QuotationDTO dto = new QuotationDTO(entity, Constants.BUDGET_STRING.toString());
				return dto;

			} else if (discriminator.equals(Program.class)) {

				Program entity = (Program) this.validateRecordExistence(discriminator, history.getRecord().getId());
				ProgramDTO dto = new ProgramDTO(entity, entity.getTitle(),
						entity.getStatus() == null ? null : entity.getStatus().getKey(),
						Constants.SHOWS_STRING.toString(),
						entity.getTheme() == null ? null : entity.getTheme().getKey());
				return dto;
			}
			return null;
		}).collect(Collectors.toList());

		logger.info("Returning after getting record history.");
		return recentViewedProjects;
	}

	/**
	 * To get project drop down data.
	 * 
	 * @return the FunctionDropDownDTO
	 */
	public ProjectDropDownDTO getProjectDropdown() {
		logger.info("Inside RecordService :: getProjectDropdown():");

		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get project drop-down.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}

		List<String> milestones = milestoneTypeService.getAllMilestoneTypeName(Constants.PROJECT_STRING);
		List<String> categories = categoryService.getAllCategoryName();
		List<CustomerDTO> clients = customerService.getCustomers();
		List<ChannelDTO> channels = channelService.getChannels();
		List<String> formats = formatService.getAllFormatsName();
		List<ProgramDTO> programs = recordRepository.getRecordsShowByStatus(Program.class).stream()
				.map(program -> new ProgramDTO((Program) program)).collect(Collectors.toList());

		// List<SaleEntityDTO> saleEntities =
		// companyRepository.getCompanies(SaleEntity.class).stream()
		// .map(entity -> new SaleEntityDTO((SaleEntity)
		// entity)).collect(Collectors.toList());
		List<CompanyDTO> saleEntities = companyService.getAllCompany(Company.class);
		// getting functions by type
		Map<String, List<FunctionDTO>> functionsByType = functionRepository.findAll().parallelStream()
				.map(entity -> new FunctionDTO(entity)).filter(dto -> dto.getType() != null)
				.collect(Collectors.groupingBy(FunctionDTO::getType));

		List<StaffMemberDTO> followers = staffMemberRepository.findAllSalesStaffMember(RoleName.Sales.toString())
				.stream().map(sales -> new StaffMemberDTO(sales)).collect(Collectors.toList());

		List<String> unitBasis = CommonUtil.getProjectUnitBasis();
		List<CurrencyDTO> currencyList = currencyService.getAllCurrencyByActive(Boolean.TRUE);

		List<String> projectStatus = CommonUtil.getProjectStatus();
		List<String> projectFinancialStatus = CommonUtil.getProjectFinancialStatus();

		List<LayoutDTO> templates = recordRepository.getLayouts(Layout.class).stream()
				.map(layout -> new LayoutDTO((Layout) layout)).collect(Collectors.toList());

		ProjectDropDownDTO dropDownDTO = new ProjectDropDownDTO(milestones, categories, clients, channels, formats,
				programs, saleEntities, functionsByType, followers, unitBasis, currencyList, projectStatus,
				projectFinancialStatus, templates);

		logger.info("Returning from getProjectDropdown():");
		return dropDownDTO;
	}

	/**
	 * Method to applying rates on Line
	 * 
	 * @param line
	 * @param record
	 * 
	 * @return the Line
	 * 
	 * @throws UnprocessableEntityException
	 */
	@SuppressWarnings("unchecked")
	public Line applyRate(Line line, Record record) throws UnprocessableEntityException {
		try {
			logger.info("Inside RecordService :: applyRate(), To apply rate on Line.");
			logger.info("Line + " + line.toString());
			logger.info("Record , id : " + record.getId());

			Rate appliedRate = null;
			List<Rate> rates = new ArrayList<Rate>();
			Boolean newRateFound = line.getUnitUsed() == null;
			Customer productor = null;

			if (record instanceof Order) {
				logger.info("Applying rate on order line");
				rates = rateService.findRatesByFunctionAndCompany(line.getFunction().getId(),
						record.getCompany().getId());
				if (rates != null && !rates.isEmpty()) {
					appliedRate = rates.get(0);

					line.setRate(appliedRate);
					line.setCurrency(appliedRate.getCurrency());
					line.setUnitSold(appliedRate.getUnitSale());
					line.setUnitUsed(appliedRate.getUnit());
					line.setUnitCost(appliedRate.getUnitCost());
					line.setUnitPrice(appliedRate.getUnitPrice());
					line.setQtySoldPerOc(appliedRate.getQuantitySale());
					line.setQtyTotalUsed(appliedRate.getQuantityCost());
					line.setQtyUsedPerOc(appliedRate.getQuantityCost());
					line.setFreeQuantity(appliedRate.getQuantityFree());
					line.setSaleEntity(appliedRate.getEntity());

					line.setAssignation(appliedRate.getAssignation());
					line.setFloorUnitPrice(appliedRate.getFloorUnitPrice());
				}
				logger.info("Successfully applied rate on order line");
				return line;
			}

			try {
				productor = customerService.findCustomerById(record.getCompany().getId());
			} catch (Exception e) {
			}
			RateUnit defaultUnit = line.getUnitUsed() != null ? line.getUnitUsed()
					: line.getFunction().getDefaultUnit();
			Currency defaultCurrency = line.getCurrency() != null ? line.getCurrency() : record.getCurrency();
			Company defaultEntity = line.getRecord().getEntity(); // Project must have a defaultEntity

			// Strategy to fetch the best rate to apply
			if (productor != null && productor.getRatingPolicy() != null) {

				rates = rateService.findRatesByFunctionAndCompany(record.getCompany().getId(),
						line.getFunction().getId());
				logger.info("Performing query " + RateCard.QUERY_findByFunctionByCompany + " with function="
						+ line.getFunction() + " and productor=" + productor + " : " + rates.size() + " rate(s) found");
			}

			if (rates.isEmpty()) {
				// 2. Elsewhere fetching standard rates
				logger.info("Getting rates from function with corresponding function " + line.getFunction().getId());
				rates = rateService.findRateByFunction(line.getFunction().getId()).stream()
						.map(rateEntity -> rateEntity.getRate(new Rate())).collect(Collectors.toList());
			}

			for (Rate rate : rates) {
				if (appliedRate == null) {
					// If no rate selected, the current rate is selected by default
					appliedRate = rate;
				} else {

					// Trying to select the rate for the project sale entity, else the default rate
					// entity
					Company bestEntity = record.getEntity();
					if (bestEntity != null && !bestEntity.equals(appliedRate.getEntity())
							&& bestEntity.equals(rate.getEntity())) {
						appliedRate = rate;
						newRateFound = true;
					} else {
						bestEntity = defaultEntity;
						if (bestEntity != null && !bestEntity.equals(appliedRate.getEntity())
								&& bestEntity.equals(rate.getEntity())) {
							appliedRate = rate;
							newRateFound = true;
						} else {
							// Trying to select the rate matching the default unit
							if (defaultUnit != null && !defaultUnit.getKey().equals(appliedRate.getUnit().getKey())
									&& defaultUnit.getKey().equals(rate.getUnit().getKey())) {
								appliedRate = rate;
								newRateFound = true;
							} else {
								// Trying to select rate matching the project currency, else the default rate
								// currency
								Currency bestCurrency = record.getCurrency();
								if (bestCurrency != null && !bestCurrency.equals(appliedRate.getCurrency())
										&& bestCurrency.equals(rate.getCurrency())) {
									appliedRate = rate;
									newRateFound = true;
								} else {
									bestCurrency = defaultCurrency;
									if (bestCurrency != null && !bestCurrency.equals(appliedRate.getCurrency())
											&& bestCurrency.equals(rate.getCurrency())) {
										appliedRate = rate;
										newRateFound = true;
									}
								}
							}
						}
					}
				}
			}

			if (appliedRate != null && (newRateFound || (line.getUnitUsed() != null
					&& line.getUnitUsed().getKey().equals(appliedRate.getUnit().getKey())))) {
				// Reload applied rate calendar constant

				// If a available rate has been found, we applied it as default to the line
				line.setCurrency(appliedRate.getCurrency());
				line.setUnitSold(appliedRate.getUnitSale());
				line.setUnitUsed(appliedRate.getUnit());
				line.setUnitCost(appliedRate.getUnitCost());
				line.setUnitPrice(appliedRate.getUnitPrice());
				line.setFloorUnitPrice(appliedRate.getFloorUnitPrice());
				line.setAssignation(appliedRate.getAssignation());
				line.setSaleEntity(appliedRate.getEntity());
				line.setRate(appliedRate);
				line.setQtySoldPerOc(appliedRate.getQuantitySale());
				line.setQtyTotalUsed(appliedRate.getQuantityCost());
				line.setQtyUsedPerOc(appliedRate.getQuantityCost());
				line.setFreeQuantity(appliedRate.getQuantityFree());

				// extraService.create(line, appliedRate.getExtras());

				if (productor != null && productor.getRatingPolicy() != null
						&& productor.getRatingPolicy().getDiscount() != null) {
					line.setDiscountRate(productor.getRatingPolicy().getDiscount());
				}

				this.logger.info("Rate applied for budget line : " + line + " , applied Rate : " + appliedRate);
				return line;

			} else {
				// Else, we only set the default currency of the project
				line.setRate(null);
				line.setCurrency(record.getCurrency());
				// check if rate-card has been applied or not
				if (line.getRate() == null) {
					line.setSyncQty(Boolean.TRUE);
				}
				// set default unit-basis as "Day"
				RateUnit unitUsed = new RateUnit();
				unitUsed.setKey("Day");
				line.setUnitUsed(unitUsed);

				logger.info("No parameterized rate to apply to " + line + " , simply set line currency to "
						+ line.getCurrency());
				return line;
			}
		} catch (Exception e) {
			logger.info("Failed to apply a rate to" + line);
			logger.info("Failure due to : " + e);
			throw new UnprocessableEntityException("Applied Rate failed on budget line");
		}
	}

	/**
	 * Method to validate Extra-Line on a budget-line
	 * 
	 * @param line
	 * @param lineDTO
	 * @param dblines
	 * 
	 * @return booking-line object
	 */
	public Line validateExtraLine(Line line, LineDTO lineDTO, List<Line> dblines) {
		logger.info("Inside RecordService :: validateExtraLine(), To validate extra-line");
		List<ExtraLine> dbExtraLines = new ArrayList<ExtraLine>();
		if (dblines != null && !dblines.isEmpty()) {
			dblines.stream().forEach(dbLineEntity -> {
				if (dbLineEntity.getId() != null && lineDTO.getId() != null
						&& dbLineEntity.getId().longValue() == lineDTO.getId().longValue()) {
					dbExtraLines.addAll(dbLineEntity.getExtras());
					return;
				}
			});
		}

		List<ExtraLineDTO> extraLinesList = lineDTO.getExtras();
		if (extraLinesList != null && !extraLinesList.isEmpty()) {

			line.setExtras(extraLinesList.stream().map(dto -> {

				// removing extras not present in dto
				if (dbExtraLines != null && !dbExtraLines.isEmpty()) {
					dbExtraLines.stream().forEach(thisEntity -> {
						if (dto.getId() != null && thisEntity.getId().longValue() != dto.getId().longValue()) {
							thisEntity.setLine(null);
							line.getExtras().remove(thisEntity);
						}
					});
				}
				// check the existing extra
				ExtraLine entity = new ExtraLine();
				try {
					entity = dto.getExtraLine(entity);
				} catch (Exception e) {
				}
				if (dto.getId() != null) {
					Optional<ExtraLine> list = dbExtraLines.stream()
							.filter(extra1 -> extra1.getId().longValue() == dto.getId().longValue()).findFirst();
					if (list.isPresent()) {
						entity = list.get();
					}
				}
				entity.setId(dto.getId());
				entity.setVersion(dto.getVersion());
				entity.setLine(line);

				entity.setLabel(dto.getLabel());
				entity.setExtraTotal(dto.getCost());
				entity.setExtraTotalPrice(dto.getPrice());

				return entity;

			}).collect(Collectors.toList()));

		} else {
			if (line.getId() != null) {
				logger.info("Deleting all etxras from line " + line.getId());
				line.getExtras().stream().forEach(extra -> extra.setLine(null));
				line.setExtras(null);
				dbExtraLines.stream().forEach(entity -> entity.setLine(null));
				logger.info("Returning after deleting all etxras from line " + line.getId());
			}
		}

		logger.info("Returning after validating extra-line on line : " + line);
		return line;
	}

	/**
	 * To add available resources to project-booking lines
	 * 
	 * @param record the record
	 */
	public void addAvailableResources(RecordDTO record) {
		logger.info("Inside RecordService :: addAvailableResources(), To add available resources.");
		for (BookingDTO booking : record.getLines()) {
			if (booking.getFunction() == null || booking.getFrom() == null || booking.getTo() == null) {
				continue;
			}
			AvailablilityDTO availablilityDTO = new AvailablilityDTO(booking.getFunction().getId(), booking.getFrom(),
					booking.getTo());
			booking.setAvailableResources(
					availabilityService.getAavailableResourceByFunctionAndBetween(availablilityDTO));
		}
		logger.info("Returning after adding available resources.");
	}

	/**
	 * To get project by quotation id
	 * 
	 * @param quotationId
	 * 
	 * @return ProjectDTO
	 */
	public ProjectDTO getProjectByQuotationId(long quotationId) {
		logger.info(
				"Inside BookingService, getProjectByQuotationId():: Finding project by quotation associated with id "
						+ quotationId);
		Project savedProject = recordRepository.getProjectByQuotationId(Project.class, quotationId);
		if (savedProject == null) {
			return null;
		}
		ProjectDTO recordDTO = (ProjectDTO) this.findRecordById(Project.class, savedProject.getId());
		logger.info("Returning from BookingService,getProjectByQuotationId()::");
		return recordDTO;
	}

	/**
	 * To get basic deatils of projects
	 * 
	 * @return list of ProjectDTO
	 */
	public List<ProjectDTO> getProjectsBasicDetails() {
		logger.info("Inside BookingService,getProjectsBasicDetails() :: ");
		List<Record> records = recordRepository.getRecords(Project.class);
		List<ProjectDTO> projects = records.stream().map(r -> {
			Project pr = (Project) r;
			return new ProjectDTO(pr.getId(), pr.getCategory() == null ? null : pr.getCategory().getName(),
					pr.getTitle(), pr.getStatus() == null ? null : pr.getStatus().getKey());
		}).collect(Collectors.toList());
		logger.info("Returning from BookingService,getProjectsBasicDetails() :: ");
		return projects;
	}

	/**
	 * To find budget id by project
	 * 
	 * @param projectId
	 * 
	 * @return budget id
	 */
	public long getBudgetIdByProject(long projectId) {
		logger.info("Inside RecordService :: getBudgetIdByProject(), To find budget id by project");
		Project project = (Project) this.validateRecordExistence(Project.class, projectId);
		return project.getSource() == null ? 0 : project.getSource().getId();
	}

	/**
	 * To change project status
	 * 
	 * @param id
	 * @param status
	 */
	public void changeProjectStatus(long id, String status) {
		logger.info("Inside RecordService :: changeProjectStatus(), id:", +id + ",status:" + status);
		Record record = recordRepository.findOne(id);
		if (record == null) {
			logger.info("Invalid Project Id : " + id);
			throw new NotFoundException("Record not found.");
		}
		ProjectStatusName.getEnum(status);
		record.getStatus().setKey(status);
		recordRepository.save(record);
	}

	/**
	 * Method to save apply date to all lines
	 * 
	 * @param discriminator the discriminator
	 * 
	 * @param budgetId      the budgetId
	 * 
	 * @param bookingDTO    the bookingDTO
	 * 
	 * @return the record instance
	 */
	public RecordDTO applyDateToAllBudgetLines(long budgetId, BookingDTO bookingDTO, Class<?> discriminator) {
		logger.info("Inside RecordService :: applyDateToAllBudgetLines(), for discriminator : " + discriminator
				+ "budgetId : ", +budgetId + ", budget-line : " + bookingDTO);
		this.findRecordById(discriminator, budgetId);
		Record record = validateRecordExistence(discriminator, budgetId);
		if (record == null) {
			logger.info("Invalid Record Id : " + budgetId);
			throw new NotFoundException("Record not found");
		}

		// check if budget has a booking
		if (discriminator.equals(Quotation.class)) {
			ProjectDTO booking = this.getProjectByQuotationId(budgetId);
			if (booking != null) {
				logger.info("Can't update budget-line because it has been already booked : " + booking);
				throw new UnprocessableEntityException("Can't update budget-line because it has been already booked");
			}
		}

		if (bookingDTO.getId() == null) {
			logger.info("The budget-line is not yet saved. Please save it first. : " + bookingDTO);
			throw new UnprocessableEntityException("The budget-line is not yet saved. Please save it first");
		}

		List<Line> dbLines = record.getLines();
		AtomicBoolean lineFound = new AtomicBoolean(false);
		Calendar applyFrom = bookingDTO.getFrom();
		Calendar applyTo = bookingDTO.getTo();
		Optional<Line> thisOptionalLine = dbLines.stream()
				.filter(line -> line.getId().longValue() == bookingDTO.getId().longValue()).findFirst();
		if (thisOptionalLine.isPresent()) {
			lineFound.set(true);
		}
		if (!lineFound.get()) {
			logger.info("The budget-line is not yet saved on record. Please save it first. : " + bookingDTO);
			throw new UnprocessableEntityException("The budget-line is not yet saved on record. Please save it first");
		}
		if (applyFrom == null || applyTo == null) {
			logger.info("Please provide valid date.");
			throw new UnprocessableEntityException("Please provide valid date.");
		}

		if (dbLines != null && !dbLines.isEmpty()) {
			record.setLines(dbLines.stream().map(dto -> {

				logger.info("Budget Line, id : " + dto.getId());

				Booking bookingEntity = new Booking();
				// check the existing line
				Line entity = dto;

				if (dto.getId() != null && bookingEntity instanceof Line) {
					bookingEntity = (Booking) entity;
				}

				if (dto.getSyncQty() != null && dto.getSyncQty().equals(Boolean.TRUE)) {
					// if budget-line is sync is manual ON, then don't update the date and other
					// calculations
					return dto;
				}
				if (dto.getDisabled().equals(Boolean.TRUE)) {
					// if budget-line is disabled, then don't update the date and other calculations
					return dto;
				}

				Line thisLine = null;
				// thisLine is same as dto corresponding stream line
				if (lineFound.get()) {
					thisLine = thisOptionalLine.get();
				}
				if (thisLine.getId().longValue() == dto.getId().longValue()) {

					logger.info("Applying date on Line : " + thisLine.getId());
					// thisLine is same as dto corresponding stream line
					bookingEntity.setFrom(applyFrom);
					bookingEntity.setTo(applyTo);

					// function
					RatedFunctionDTO fun = bookingDTO.getFunction();
					Function funEntity = null;
					if (fun != null) {
						funEntity = functionRepository.findOne(fun.getId());
						if (funEntity == null) {
							logger.info("Function not found with id : " + fun.getId());
							throw new NotFoundException("Function not found with id : " + fun.getId());
						}
					} else {
						logger.info("Please provide valid function");
						throw new NotFoundException("Please provide valid function");
					}

					// adding resource on booking-line if present
					if (bookingDTO.getResource() != null && bookingDTO.getResource().getId() != null) {
						Resource<?> resource = this.resourceService.findResource(bookingEntity.getResource().getId());
						if (resource == null) {
							logger.info("Resource not found : " + bookingEntity.getResource());
							throw new NotFoundException("Resource not found");
						}
						ResourceFunction validateFunction = resource.getFunction(funEntity);
						if (validateFunction == null) {
							logger.info("Invalid Resource");
							throw new UnprocessableEntityException("Invalid Resource");
						}
						bookingEntity.setResource(resource);
					} else {
						bookingEntity.setResource(null);
					}

					entity = (Line) bookingEntity;
					entity.setFunction((RatedFunction) funEntity);
					entity.setId(bookingDTO.getId());
					if (!StringUtils.isBlank(dto.getPersistentCurrency())) {
						Currency currency = Currency.getInstance(dto.getPersistentCurrency());
						entity.setCurrency(currency);
					} else {
						entity.setCurrency(Currency.getInstance(currencyService.getDefaultCurrency().getCode()));
					}
					entity.setRecord(record);
					entity.setComment(bookingDTO.getComment());
					entity.setOccurrenceCount(bookingDTO.getOccurrenceCount());
					entity.setDisabled(bookingDTO.getDisabled() != null ? bookingDTO.getDisabled() : Boolean.FALSE);
					entity.setFaulty(bookingDTO.isFaulty());
					entity.setSyncQty(bookingDTO.getSyncQty());
					entity.setDiscountRate(bookingDTO.getDiscountRate());
					// setting vat
					Vat vat = null;
					if (entity.getVat() != null) {
						vat = entity.getVat();
						Float key = bookingDTO.getVatRate();
						vat.setKey(key == null ? "" : key.toString());
						vat.setRate(key);
						entity.setVat(vat);
					} else {
						vat = new Vat();
						Float key = bookingDTO.getVatRate();
						vat.setKey(key == null ? "" : key.toString());
						vat.setRate(key);
						entity.setVat(vat);
					}
					// applying extras on line
					entity = validateExtraLine(entity, bookingDTO, record.getLines());
					if (bookingDTO.getRate() != null) {
						Rate rate = bookingDTO.getRate().getRate(new Rate());
						bookingEntity.setRate(rate);
					}

					LocalDate fromTime = new LocalDate(applyFrom);
					LocalDate toTime = new LocalDate(applyTo);
					long daysDiff = Days.daysBetween(fromTime, toTime).getDays();

					Float addedQtySold = daysDiff + 1f;
					bookingEntity.setQtySoldPerOc(addedQtySold); // biliable qty
					bookingEntity.setQtyUsedPerOc(addedQtySold); // bookable qty

					entity = this.calculateAndUpdateBudgetLineCosts(entity);

					return entity;
				}

				entity = (Line) bookingEntity;

				entity.setId(dto.getId());
				Long fun = bookingEntity.getFunction() != null ? bookingEntity.getFunction().getId() : null;
				Function funEntity = null;
				if (fun != null) {
					funEntity = functionRepository.findOne(fun);
					entity.setFunction((RatedFunction) funEntity);
				}
				if (!StringUtils.isBlank(dto.getPersistentCurrency())) {
					Currency currency = Currency.getInstance(dto.getPersistentCurrency());
					entity.setCurrency(currency);
				} else {
					entity.setCurrency(Currency.getInstance(currencyService.getDefaultCurrency().getCode()));
				}
				entity.setRecord(record);
				entity.setComment(dto.getComment());
				entity.setOccurrenceCount(dto.getOccurrenceCount());
				entity.setDisabled(dto.getDisabled() != null ? dto.getDisabled() : Boolean.FALSE);
				entity.setFaulty(dto.isFaulty());
				entity.setSyncQty(dto.getSyncQty());
				entity.setDiscountRate(dto.getDiscountRate());

				// setting vat
				entity.setVat(dto.getVat());
				entity.setExtras(dto.getExtras());
				if (dto.getRate() != null) {
					bookingEntity.setRate(dto.getRate());
				}

				LocalDate fromTime = new LocalDate(applyFrom);
				LocalDate toTime = new LocalDate(applyTo);
				long daysDiff = Days.daysBetween(fromTime, toTime).getDays();

				Float addedQtySold = daysDiff + 1f;
				bookingEntity.setQtySoldPerOc(addedQtySold); // biliable qty
				bookingEntity.setQtyUsedPerOc(addedQtySold); // bookable qty
				bookingEntity.setFrom(applyFrom);
				bookingEntity.setTo(applyTo);

				entity = this.calculateAndUpdateBudgetLineCosts(entity);

				return entity;
			}).collect(Collectors.toList()));
		}
		logger.info("Saving record with applying dates to all budget-lines on record : " + record.getId());
		Record thisRec = null;
		if (discriminator.equals(Project.class)) {
			Project project = (Project) record;
			project.setStartDate(applyFrom);
			project.setEndDate(applyTo);
			project.setCalcTotalPriceIVAT(project.getCalcTotalPrice());
			project.setTotalPrice(project.getCalcTotalPrice());
			project.setTotalNetPrice(project.getCalcTotalPrice());
			thisRec = recordRepository.save(project);

			ProjectDTO responseDTO = new ProjectDTO((Project) thisRec);
			responseDTO.setRecordDiscriminator(Constants.BOOKING_STRING.toString());
			this.addAvailableResources(responseDTO);
			logger.info("Returning booking instance after applying dates to all lines.");
			return responseDTO;

		} else if (discriminator.equals(Quotation.class)) {
			Quotation quotation = (Quotation) record;
			quotation.setStartDate(applyFrom);
			quotation.setEndDate(applyTo);
			quotation.setCalcTotalPriceIVAT(quotation.getCalcTotalPrice());
			quotation.setTotalPrice(quotation.getCalcTotalPrice());
			quotation.setTotalNetPrice(quotation.getCalcTotalPrice());
			thisRec = recordRepository.save(quotation);

			QuotationDTO responseDTO = new QuotationDTO((Quotation) thisRec);
			responseDTO.setRecordDiscriminator(Constants.BUDGET_STRING.toString());
			this.addAvailableResources(responseDTO);
			logger.info("Returning budget instance after applying dates to all lines.");
			return responseDTO;

		} else if (discriminator.equals(Program.class)) {
			record.setCalcTotalPriceIVAT(record.getCalcTotalPrice());
			record.setTotalPrice(record.getCalcTotalPrice());
			record.setTotalNetPrice(record.getCalcTotalPrice());
			thisRec = recordRepository.save(record);

			ProgramDTO responseDTO = new ProgramDTO((Program) thisRec);
			responseDTO.setRecordDiscriminator(Constants.SHOWS_STRING.toString());
			this.addAvailableResources(responseDTO);
			logger.info("Returning show instance after applying dates to all lines.");
			return responseDTO;
		}

		return null;
	}

	/**
	 * Method to save budget-line on record
	 * 
	 * @param recordId
	 * @param dto
	 * @param discriminator
	 * 
	 * @return record object
	 */
	@Transactional
	public RecordDTO saveBudgetLine(long recordId, BookingDTO dto, Class<?> discriminator) {
		logger.info("Inside RecordService :: saveBudgetLine(), To save a budget-line on : recordId : " + recordId
				+ ", Line : " + dto + ", for discriminator : " + discriminator);
		this.findRecordById(discriminator, recordId);
		Record record = this.validateRecordExistence(discriminator, recordId);
		if (record == null) {
			logger.info("Invalid Record Id : " + recordId);
			throw new NotFoundException("Record not found");
		}

		String done = ProjectStatusName.DONE.getProjectStatusNameString();
		XmlEntity entityStatus = ((AbstractProject) record).getStatus();
		String status = entityStatus == null ? null : entityStatus.getKey();
		if (StringUtils.isBlank(status) || (!StringUtils.isBlank(status) && status.equalsIgnoreCase(done))) {
			logger.info("Cannot add more lines as status is " + status);
			throw new UnprocessableEntityException("Cannot add more lines as status is invalid");
		}

		String approved = ProjectFinancialStatusName.APPROVED.getProjectFinancialStatusNameString();
		String rejected = ProjectFinancialStatusName.REJECTED.getProjectFinancialStatusNameString();
		String financialStatus = record.getFinancialStatus();

		if (discriminator.equals(Project.class)) {
			if (!StringUtils.isBlank(financialStatus) && (financialStatus.equalsIgnoreCase(rejected))) {
				logger.info("Cannot add more lines as financial status is " + financialStatus);
				throw new UnprocessableEntityException(
						"Cannot add more lines as financial status is " + financialStatus);
			}
		} else {
			if (!StringUtils.isBlank(financialStatus)
					&& (financialStatus.equalsIgnoreCase(approved) || financialStatus.equalsIgnoreCase(rejected))) {
				logger.info("Cannot add more lines as financial status is " + financialStatus);
				throw new UnprocessableEntityException(
						"Cannot add more lines as financial status is " + financialStatus);
			}
		}

		if (dto.getId() != null && dto.getDisabled() != null && dto.getDisabled().equals(Boolean.TRUE)) {
			// means line is disabled, and needs no updation and calculations

			Line thisDisabledEntity = null;
			Calendar endDate = null;
			Optional<Line> list = record.getLines().stream()
					.filter(line1 -> line1.getId().longValue() == dto.getId().longValue()).findFirst();
			if (list.isPresent()) {
				thisDisabledEntity = list.get();
				thisDisabledEntity.setDisabled(Boolean.TRUE);
				int index = record.getLines().indexOf(list.get());
				if (thisDisabledEntity instanceof Booking) {
					Booking booking = (Booking) thisDisabledEntity;
					endDate = booking.getTo();
				}
				record.getLines().set(index, thisDisabledEntity);
			}
			Record updatedRecord = recordRepository.save(record);
			updatedRecord.updateAmounts();
			updatedRecord.setTotalPrice(updatedRecord.getCalcTotalPrice());
			updatedRecord.setTotalNetPrice(updatedRecord.getCalcTotalPrice());
			updatedRecord.setCalcTotalPriceIVAT(updatedRecord.getCalcTotalPrice());

			// line is disabled, means no calculation is needed on that particular line.
			if (discriminator.equals(Quotation.class)) {
				Quotation quotation = (Quotation) updatedRecord;
				quotation.setEndDate(endDate);
				updatedRecord = recordRepository.save(quotation);

				QuotationDTO response = new QuotationDTO((Quotation) updatedRecord);
				this.addAvailableResources(response);
				response.setRecordDiscriminator(Constants.BUDGET_STRING.toString());
				return response;
			} else if (discriminator.equals(Project.class)) {
				Project project = (Project) updatedRecord;
				project.setEndDate(endDate);
				updatedRecord = recordRepository.save(project);

				ProjectDTO response = new ProjectDTO((Project) updatedRecord);
				this.addAvailableResources(response);
				response.setRecordDiscriminator(Constants.BOOKING_STRING.toString());
				return response;
			} else if (discriminator.equals(Program.class)) {
				updatedRecord = recordRepository.save(updatedRecord);

				ProgramDTO response = new ProgramDTO((Program) updatedRecord);
				this.addAvailableResources(response);
				response.setRecordDiscriminator(Constants.SHOWS_STRING.toString());
				return response;
			}
		}

		// only calculate if line is not disabled
		Line dbLine = null;
		Line entity = null;

		if (discriminator.equals(Project.class)) {

			// save line for booking-project
			Record instanceRecord = null;

			if (dto.getOccurrenceCount() == null || dto.getOccurrenceCount().intValue() <= 1) {
				// for single occurrence-count
				dto.setOccurrenceCount(1f);
				entity = this.validateLineCalculation(record, dto, discriminator);
				Record updatedRecord = bookingService.saveOrUpdateEventFromBookingLine(entity);
				instanceRecord = recordRepository.save(updatedRecord);
				Record recordInstance = this.updateStartAndEndTime(instanceRecord);
				recordInstance.updateAmounts();
				recordInstance.updateTaxes(updatedRecord.getCalcTotalPrice());
				instanceRecord = recordRepository.save(recordInstance);
				instanceRecord.setNewlySavedLine(updatedRecord.getNewlySavedLine());
			} else {
				// for multiple occurrence-count
				Long newlySavedLine = null;
				Record updatedRecord = null;
				for (int i = 0; i < dto.getOccurrenceCount().intValue(); i++) {
					entity = this.validateLineCalculation(record, dto, discriminator);
					updatedRecord = bookingService.saveOrUpdateEventFromBookingLine(entity);
					updatedRecord.updateAmounts();
					newlySavedLine = updatedRecord.getNewlySavedLine();
				}
				instanceRecord = this.updateStartAndEndTime(updatedRecord);
				instanceRecord.updateAmounts();
				instanceRecord.updateTaxes(updatedRecord.getCalcTotalPrice());
				instanceRecord = recordRepository.save(instanceRecord);
				instanceRecord.setNewlySavedLine(newlySavedLine);
			}

			ProjectDTO response = new ProjectDTO((Project) instanceRecord);
			response.setNewlyBookingId(instanceRecord.getNewlySavedLine());
			this.addAvailableResources(response);
			response.setRecordDiscriminator(Constants.BOOKING_STRING.toString());
			return response;

		} else if (discriminator.equals(Quotation.class)) {

			if (dto.getOccurrenceCount() == null || dto.getOccurrenceCount().intValue() <= 1) {
				dto.setOccurrenceCount(1f);
			}
			// save line for budget-quotation
			entity = validateLineCalculation(record, dto, discriminator);

			if (record.getLines() != null && !record.getLines().isEmpty()) {
				if (dto.getId() != null) {
					Optional<Line> list = record.getLines().stream()
							.filter(line1 -> line1.getId().longValue() == dto.getId().longValue()).findFirst();
					if (list.isPresent()) {
						dbLine = list.get();
						int index = record.getLines().indexOf(dbLine);
						record.getLines().set(index, entity);
					}
				} else {
					record.addLine(entity);
				}
			} else {
				record.addLine(entity);
			}
			record = this.updateStartAndEndTime(record);
			record.updateAmounts();
			record.setTotalPrice(record.getCalcTotalPrice());
			record.setCalcTotalPriceIVAT(record.getCalcTotalPrice());
			record.setCalcTotalPriceIVAT(record.getCalcTotalPrice());
			Record updatedRecord = recordRepository.save(record);

			QuotationDTO budgetQuotation = new QuotationDTO(
					(Quotation) recordRepository.findOne(updatedRecord.getId()));
			this.addAvailableResources(budgetQuotation);
			budgetQuotation.setRecordDiscriminator(Constants.BUDGET_STRING.toString());
			return budgetQuotation;

		} else if (discriminator.equals(Program.class)) {
			// save line for program-show
			XmlEntity statusEntity = null;
			if (dto.getOccurrenceCount() == null || dto.getOccurrenceCount().intValue() <= 1) {
				dto.setOccurrenceCount(1f);
			}
			entity = validateLineCalculation(record, dto, discriminator);

			if (record.getLines() != null && !record.getLines().isEmpty()) {
				if (dto.getId() != null) {
					Optional<Line> list = record.getLines().stream()
							.filter(line1 -> line1.getId().longValue() == dto.getId().longValue()).findFirst();
					if (list.isPresent()) {
						dbLine = list.get();
						int index = record.getLines().indexOf(dbLine);
						record.getLines().set(index, entity);
					}
				} else {
					record.addLine(entity);
				}

				// if status is "To Do", make program-show status as "In Progress"
				statusEntity = record.getStatus();
				String inProgressStatus = ProjectStatusName.IN_PROGRESS.getProjectStatusNameString();
				String toDoStatus = ProjectStatusName.TO_DO.getProjectStatusNameString();
				if (statusEntity != null && statusEntity.getKey().equalsIgnoreCase(toDoStatus)) {
					statusEntity.setKey(inProgressStatus);
					record.setStatus(statusEntity);
				}
			} else {
				record.addLine(entity);

				// make program status as "In Progress" if only a single line is present
				String inProgressStatus = ProjectStatusName.IN_PROGRESS.getProjectStatusNameString();
				statusEntity = new XmlEntity();
				statusEntity.setKey(inProgressStatus);
				record.setStatus(statusEntity);
			}
			record.updateAmounts();
			record.setTotalPrice(record.getCalcTotalPrice());
			record.setTotalNetPrice(record.getCalcTotalPrice());
			record.setCalcTotalPriceIVAT(record.getCalcTotalPrice());
			Record updatedRecord = recordRepository.save(record);
			updatedRecord.updateAmounts();

			ProgramDTO programShow = new ProgramDTO((Program) recordRepository.findOne(updatedRecord.getId()));
			programShow.setRecordDiscriminator(Constants.SHOWS_STRING.toString());
			this.addAvailableResources(programShow);
			return programShow;
		}
		return null;
	}

	/**
	 * Method to update start-time and end-time of record
	 * 
	 * @param record
	 * 
	 * @return the record instance object
	 */
	public Record updateStartAndEndTime(Record record) {
		logger.info("Inside RecordService :: updateStartAndEndTime(), record : " + record);
		List<BookingDTO> bookingLines = record.getLines().stream().map(line -> new BookingDTO(line))
				.collect(Collectors.toList());
		if (bookingLines != null && !bookingLines.isEmpty()) {

			Calendar calStart = Calendar.getInstance();
			Calendar calEnd = calStart;

			Optional<BookingDTO> first = bookingLines.stream()
					.sorted((o1, o2) -> o1.getStart().compareTo(o2.getStart())).findFirst();
			if (first.isPresent()) {
				DateTime start = DateTime.parse(first.get().getStart()).withZone(DateTimeZone.forID("Asia/Calcutta"));
				calStart = Calendar.getInstance();
				calStart.setTimeInMillis(start.getMillis());
			}
			Optional<BookingDTO> last = bookingLines.stream()
					.sorted((o1, o2) -> -1 * o1.getEnd().compareTo(o2.getEnd())).findFirst();
			if (last.isPresent()) {
				DateTime end = DateTime.parse(last.get().getEnd()).withZone(DateTimeZone.forID("Asia/Calcutta"));
				calEnd.setTimeInMillis(end.getMillis());
			}
			if (record instanceof Project) {
				Project project = (Project) record;
				project.setStartDate(calStart);
				project.setEndDate(calEnd);
				return project;
			} else if (record instanceof Quotation) {
				Quotation quotation = (Quotation) record;
				quotation.setStartDate(calStart);
				quotation.setEndDate(calEnd);
				return quotation;
			}
		}
		return record;
	}

	/**
	 * Method to delete booking-line
	 * 
	 * @param lineId        the lineId
	 * 
	 * @param discriminator the record discriminator
	 * 
	 * @return record object
	 */
	@Transactional
	public RecordDTO deleteBudgetLine(long lineId, Class<?> discriminator) {
		logger.info("Inside RecordService :: deleteBudgetLine(), To delete a budget-line id : " + lineId
				+ ", for discriminator : " + discriminator);

		Booking booking = bookingService.findBooking(lineId);
		if (booking.getRecord() == null) {
			logger.info("Record not found for this booking");
			throw new UnprocessableEntityException("Record not found for this booking");
		}
		Record record = this.validateRecordExistence(discriminator, booking.getRecord().getId());

		if (booking != null && booking.getId() != null) {
			BookingEvent event = booking.getBookingEvent();
			if (event != null && event.getId() != null) {
				logger.info("Deleting event from booking , eventId : " + event.getId());
				event.setOrigin(null);
				eventService.deleteEvent(event.getId());
			}
			// cannot delete booking-line if resource been assigned on it
			if (booking.getResource() != null && !(booking.getResource() instanceof DefaultResource
					|| booking.getResource() instanceof SupplyResource)) {
				logger.info("Cannot delete booking-line as the resource already been assigned on it");
				throw new UnprocessableEntityException(
						"Cannot delete booking-line as the resource already been assigned on it");
			}
			// cannot delete booking-line if order has been created for this line
			if (bookingRepository.findOrderLineByBooking(OrderLine.class, booking.getId()) != null) {
				logger.error("Cannot delete booking-line as an order has already been created for it.");
				throw new UnprocessableEntityException(
						"Cannot delete booking-line as an order has already been created for it.");
			}
			bookingService.deleteBookingLine(booking);
		}
		record.removeLine(booking);
		Record updatedRecord = record;
		updatedRecord.updateAmounts();
		Record savedRecord = this.updateStartAndEndTime(updatedRecord);
		savedRecord.updateTaxes(savedRecord.getCalcTotalPrice());
		updatedRecord = recordRepository.save(savedRecord);

		if (discriminator.equals(Project.class)) {
			ProjectDTO response = new ProjectDTO(
					(Project) this.validateRecordExistence(Project.class, updatedRecord.getId()));
			response.setRecordDiscriminator(Constants.BOOKING_STRING.toString());
			this.addAvailableResources(response);
			logger.info("Returning booking instance after deleting booking-line from booking-project.");
			return response;
		} else if (discriminator.equals(Quotation.class)) {
			QuotationDTO response = new QuotationDTO(
					(Quotation) this.validateRecordExistence(Quotation.class, updatedRecord.getId()));
			response.setRecordDiscriminator(Constants.BUDGET_STRING.toString());
			this.addAvailableResources(response);
			logger.info("Returning budget instance after deleting booking-line from budget-quotation.");
			return response;
		} else if (discriminator.equals(Program.class)) {
			ProgramDTO response = new ProgramDTO((Program) updatedRecord);
			response.setRecordDiscriminator(Constants.SHOWS_STRING.toString());
			this.addAvailableResources(response);
			logger.info("Returning budget instance after deleting booking-line from program-show.");
			return response;
		}
		return null;
	}

	/**
	 * Method to validate budget-line calculation
	 * 
	 * @param record
	 * @param dto
	 * @param discriminator
	 * 
	 * @return budget-Line object
	 */
	public Line validateLineCalculation(Record record, BookingDTO dto, Class<?> discriminator) {
		logger.info("Inside RecordService :: validateLineCalculation(), To calculate a budget-line on : recordId : "
				+ record.getId(), ", Line : " + dto + ", for discriminator : " + discriminator);

		Booking bookingEntity = new Booking();
		// check the existing line
		Line entity = new Line();
		try {
			entity = dto.getLine(entity, dto);
		} catch (Exception e) {
		}
		if (dto.getId() != null) {
			Optional<Line> list = record.getLines().stream()
					.filter(line1 -> line1.getId().longValue() == dto.getId().longValue()).findFirst();
			if (list.isPresent()) {
				entity = list.get();
			}
		}

		if (dto.getId() != null && entity instanceof Booking) {
			bookingEntity = (Booking) entity;
		}

		// db date
		Calendar dbFrom = bookingEntity.getFrom();
		Calendar dbTo = bookingEntity.getTo();
		DateTime start = null;
		DateTime to = null;

		if (!StringUtils.isBlank(dto.getStart()) && !StringUtils.isBlank(dto.getEnd())) {
			start = DateTime.parse(dto.getStart()).withZone(DateTimeZone.forID("Asia/Calcutta"));
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(start.getMillis());
			bookingEntity.setFrom(cal);

			to = DateTime.parse(dto.getEnd()).withZone(DateTimeZone.forID("Asia/Calcutta"));
			Calendar endCal = Calendar.getInstance();
			endCal.setTimeInMillis(to.getMillis());
			bookingEntity.setTo(endCal);
		} else {
			logger.info("Please provide valid start and end date");
			throw new UnprocessableEntityException("Please provide valid start and end date");
		}

		// function
		RatedFunctionDTO fun = dto.getFunction();
		Function funEntity = null;
		if (fun != null) {
			funEntity = functionRepository.findOne(fun.getId());
			if (funEntity == null) {
				logger.info("Function not found with id : " + fun.getId());
				throw new NotFoundException("Function not found with id : " + fun.getId());
			}
		} else {
			logger.info("Please provide valid function");
			throw new NotFoundException("Please provide valid function");
		}

		// adding resource on booking-line if present
		if (dto.getResource() != null && dto.getResource().getId() != null) {
			Resource<?> resource = this.resourceService.findResource(dto.getResource().getId());
			if (resource == null) {
				logger.info("Resource not found : " + dto.getResource());
				throw new NotFoundException("Resource not found");
			}
			if (!(resource instanceof SupplyResource)) {
				ResourceFunction validateFunction = resource.getFunction(funEntity);
				if (validateFunction == null) {
					logger.info("Invalid Resource");
					throw new UnprocessableEntityException("Invalid Resource");
				}
			}
			bookingEntity.setResource(resource);
		} else {
			bookingEntity.setResource(null);
		}

		entity = (Line) bookingEntity;
		entity.setFunction((RatedFunction) funEntity);
		entity.setId(dto.getId());
		if (!StringUtils.isBlank(dto.getPersistentCurrency())) {
			Currency currency = Currency.getInstance(dto.getPersistentCurrency());
			entity.setCurrency(currency);
		} else {
			entity.setCurrency(Currency.getInstance(currencyService.getDefaultCurrency().getCode()));
		}
		entity.setRecord(record);
		entity.setComment(dto.getComment());

		if (discriminator.equals(Project.class)) {
			// at booking, now each line can only have one quantity
			entity.setOccurrenceCount(1f);
		} else if (discriminator.equals(Quotation.class)) {
			entity.setOccurrenceCount(dto.getOccurrenceCount());
		} else if (discriminator.equals(Program.class)) {
			entity.setOccurrenceCount(dto.getOccurrenceCount());
		}

		entity.setDisabled(dto.getDisabled() != null ? dto.getDisabled() : Boolean.FALSE);
		entity.setFaulty(dto.isFaulty());
		entity.setSyncQty(dto.getSyncQty());
		entity.setDiscountRate(dto.getDiscountRate());

		// setting vat
		Vat vat = null;
		if (entity.getVat() != null) {
			vat = entity.getVat();
			Float key = dto.getVatRate();
			vat.setKey(key == null ? "" : key.toString());
			vat.setRate(key);
			entity.setVat(vat);
		} else {
			vat = new Vat();
			Float key = dto.getVatRate();
			vat.setKey(key == null ? "" : key.toString());
			vat.setRate(key);
			entity.setVat(vat);
		}

		// applying extras on line
		entity = validateExtraLine(entity, dto, record.getLines());

		if (dto.getId() == null) {
			entity = this.applyRate(entity, record);
			if (!discriminator.equals(Program.class)) {
				long daysDiff = Days.daysBetween(start.toLocalDate(), to.toLocalDate()).getDays();
				long timeDiff = Hours.hoursBetween(start.toLocalDateTime(), to.toLocalDateTime()).getHours();
				if (daysDiff <= 0) {
					if (daysDiff == 0 && timeDiff < 0) {
						logger.info("End time should be greater than start time");
						throw new UnprocessableEntityException("End time should be greater than start time");
					} else if (daysDiff == 0 && timeDiff == 0) {
						logger.info("Booking should not be less than 1 hour");
						throw new UnprocessableEntityException("Booking should not be less than 1 hour");
					} else if (daysDiff < 0) {
						logger.info("End Date cannot be before Start Date.");
						throw new UnprocessableEntityException("End Date cannot be before Start Date.");
					}
				}
				Float addedQtySold = daysDiff + 1f;
				bookingEntity.setQtySoldPerOc(addedQtySold); // biliable qty
				bookingEntity.setQtyUsedPerOc(addedQtySold); // bookable qty
			}

			// update the calculations here for a new line added.
			// set TotalLocalPrice with added extras-cost and deducting discount

			entity = this.calculateAndUpdateBudgetLineCosts(entity);

		} else if (dto.getId() != null && dto.getSyncQty() != null && dto.getSyncQty().equals(Boolean.FALSE)) {
			// manual update : OFF, then apply RateCard
			// qty change : disabled
			// date change : enable, then both billable and bookable get changed

			if (dto.getRate() == null) {
				logger.info("Please provide rate card");
				throw new UnprocessableEntityException("Please provide rate card");
			}
			if (dto.getRate() != null) {
				Rate rate = dto.getRate().getRate(new Rate());
				bookingEntity.setRate(rate);
			}

			// startDate = startDate - bookable unit
			if (!discriminator.equals(Program.class)) {
				long daysDiff = Days.daysBetween(start.toLocalDate(), to.toLocalDate()).getDays();
				long timeDiff = Hours.hoursBetween(start.toLocalDateTime(), to.toLocalDateTime()).getHours();
				if (daysDiff <= 0) {
					if (daysDiff == 0 && timeDiff < 0) {
						logger.info("End time should be greater than start time");
						throw new UnprocessableEntityException("End time should be greater than start time");
					} else if (daysDiff == 0 && timeDiff == 0) {
						logger.info("Booking should not be less than 1 hour");
						throw new UnprocessableEntityException("Booking should not be less than 1 hour");
					} else if (daysDiff < 0) {
						logger.info("End Date cannot be before Start Date.");
						throw new UnprocessableEntityException("End Date cannot be before Start Date.");
					}
				}
				Float addedQtySold = daysDiff + 1f;
				bookingEntity.setQtySoldPerOc(addedQtySold); // biliable qty
				bookingEntity.setQtyUsedPerOc(addedQtySold); // bookable qty
			}

			entity = this.calculateAndUpdateBudgetLineCosts(entity);

		} else if (dto.getId() != null && dto.getSyncQty() != null && dto.getSyncQty().equals(Boolean.TRUE)) {

			// manual update : ON,
			// you can either apply RateCard, or manually update
			// quantity, prices, dates
			// discount, vat/tax
			// manually update line cost if line-id exists

			// disable rate-card
			// start-date : disabled, it can't changed from calendar
			// if due-date change, then only bookable change

			if (!StringUtils.isBlank(dto.getPersistentCurrency())) {
				entity.setCurrency(Currency.getInstance(dto.getPersistentCurrency()));
			} else {
				entity.setCurrency(Currency.getInstance(currencyService.getDefaultCurrency().getCode()));
			}

			bookingEntity.setRate(null);
			if (!StringUtils.isBlank(dto.getUnitPriceBasis())) {
				if (!CommonUtil.getProjectUnitBasis().contains(dto.getUnitPriceBasis())) {
					logger.info("Invalid basis");
					throw new UnprocessableEntityException("Invalid basis");
				}
				RateUnit unitUsed = new RateUnit();
				unitUsed.setKey(dto.getUnitPriceBasis());
				bookingEntity.setUnitUsed(unitUsed);
			} else {
				// apply DAY basis as default
				RateUnit unitUsed = new RateUnit();
				unitUsed.setKey("Day");
				bookingEntity.setUnitUsed(unitUsed);
			}

			entity.setUnitCost(dto.getUnitCost());
			entity.setUnitPrice(dto.getUnitPrice());
			entity.setFloorUnitPrice(dto.getFloorUnitPrice());
			entity.setDiscountRate(dto.getDiscountRate());

			if (dto.isDateChange()) {
				// if date change event
				long daysDiff = Days.daysBetween(start.toLocalDate(), to.toLocalDate()).getDays();
				long timeDiff = Hours.hoursBetween(start.toLocalDateTime(), to.toLocalDateTime()).getHours();
				if (daysDiff <= 0) {
					if (daysDiff == 0 && timeDiff < 0) {
						logger.info("End time should be greater than start time");
						throw new UnprocessableEntityException("End time should be greater than start time");
					} else if (daysDiff == 0 && timeDiff == 0) {
						logger.info("Booking should not be less than 1 hour");
						throw new UnprocessableEntityException("Booking should not be less than 1 hour");
					} else if (daysDiff < 0) {
						logger.info("End Date cannot be before Start Date.");
						throw new UnprocessableEntityException("End Date cannot be before Start Date.");
					}
				}
				Float addedQtySold = daysDiff + 1f;
				bookingEntity.setQtyUsedPerOc(addedQtySold); // only bookable change if calendar due-date
				// changes
				bookingEntity.setQtySoldPerOc(dto.getQtySoldPerOc()); // billiable can change

			} else if (dto.isQtyChange()) {
				// if qty change event
				Calendar toCal = dbTo;
				Calendar fromNew = dbTo;
				int bookableQtySld = dto.getQtyUsedPerOc() != null ? dto.getQtyUsedPerOc().intValue() : 0;
				bookableQtySld = bookableQtySld - 1;
				fromNew.add(Calendar.DATE, -bookableQtySld);
				bookingEntity.setFrom(fromNew);
				bookingEntity.setQtySoldPerOc(dto.getQtySoldPerOc());
				bookingEntity.setQtyUsedPerOc(dto.getQtyUsedPerOc());

			} else {
				// may be only qtySold has been changed, but it will not affect the dates.
				bookingEntity.setQtySoldPerOc(dto.getQtySoldPerOc());
			}
			entity = this.calculateAndUpdateBudgetLineCosts(entity);
		}
		return entity;
	}

	/**
	 * Method to calculate and update budget-line cost
	 * 
	 * @param entity the budget-line entity
	 * 
	 * @return budget-line object
	 */
	public Line calculateAndUpdateBudgetLineCosts(Line entity) {
		logger.info(
				"Inside RecordService :: calculateAndUpdateBudgetLineCosts(), To calculate and update budget-line cost");
		logger.info(
				"Budget-line to update cost, line : " + entity.getId() + ", on record : " + entity.getRecord().getId());

		// set TotalLocalPrice with added extras-cost and deducting discount
		Float extraPrice = 0f;
		if (entity.getExtras() != null && !entity.getExtras().isEmpty()) {
			for (ExtraLine extra : entity.getExtras()) {
				extraPrice += extra.getExtraTotalPrice() != null ? extra.getExtraTotalPrice().floatValue() : 0F;
			}
		}
		float qtySold = entity.getQtySoldPerOc() == null ? 0f : entity.getQtySoldPerOc();
		float unitPrice = entity.getUnitPrice() == null ? 0f : entity.getUnitPrice();
		float totalPriceWithExtra = qtySold * unitPrice + extraPrice;
		float discRate = entity.getDiscountRate() == null ? 0f : entity.getDiscountRate();
		float totalLocalPrice = totalPriceWithExtra - (totalPriceWithExtra * discRate) / 100;

		entity.setExtraPrice(extraPrice);
		entity.setTotalPrice(totalPriceWithExtra);
		entity.setTotalLocalPrice(totalLocalPrice);
		entity.setTotalPriceWithOccurenceCount(entity.getTotalLocalPrice() * entity.getOccurrenceCount());
		float vatAmount = totalLocalPrice
				* (entity.getVat() != null && entity.getVat().getRate() != null ? entity.getVat().getRate() : 0) / 100;
		float totalWithTax = (totalLocalPrice + vatAmount) * entity.getOccurrenceCount();
		entity.setTotalWithTax(totalWithTax);

		// setting totalLocalCost with added extras-cost
		Float extraCost = 0f;
		if (entity.getExtras() != null && !entity.getExtras().isEmpty()) {
			for (ExtraLine extra : entity.getExtras()) {
				extraCost += extra.getExtraTotal() != null ? extra.getExtraTotal().floatValue() : 0F;
			}
		}
		float qtyUsed = entity.getQtyUsedPerOc() == null ? 0f : entity.getQtyUsedPerOc();
		float unitCost = entity.getUnitCost() == null ? 0f : entity.getUnitCost();
		float totalLocalCost = qtyUsed * unitCost + extraCost;

		entity.setExtraCost(extraCost);
		entity.setTotalCost(totalLocalCost);
		entity.setTotalLocalCost(totalLocalCost);
		entity.setTotalCostWithOccurenceCount(entity.getTotalLocalCost() * entity.getOccurrenceCount());

		logger.info("Returning after calculating and updating budget-line cost.");
		return entity;
	}

	/**
	 * To get total project count by company
	 * 
	 * @param companyId
	 * @return
	 */
	public int getTotalCompanyProjects(long companyId) {
		return this.recordRepository.findProjectCountByCompany(companyId);
	}

	/**
	 * To create all sessions(Budgets) from a program-show
	 * 
	 * @param programId the programId
	 * 
	 * @return Program wrapper object
	 */
	public ProgramDTO createAllSessions(long programId) {
		logger.info("Inside RecordService :: createAllSessions, To make all sessions for program Id : " + programId);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to create shows-sessions.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		Program program = (Program) this.validateRecordExistence(Program.class, programId);

		if (program.isSessioned()) {
			logger.info("Sessions haa been already created for this program-show : programId : " + program.getId());
			throw new UnprocessableEntityException("Sessions has been already created for this show");
		}

		this.validateProgramSession(program);

		if (program != null && program.getNbEpisodes() != null && program.getNbEpisodes().intValue() >= 0
				&& program.getNbSessions() != null && program.getNbSessions().intValue() >= 0) {

			int size = program.getLinkedRecords().size();
			if (size >= program.getNbEpisodes()) {
				logger.warn("All episodes have already been created for this program-show : id = " + program.getId());
				logger.warn("Program episodes full for program-show : id = " + program.getId());
				throw new UnprocessableEntityException("All episodes have already been created for this program-show");
			}
			List<Line> programLines = program.getLines();
			String programTitle = program.getTitle();
			Integer sessionsToCreate = program.getNbSessions() - program.getLinkedRecords().size();
			String title = "";
			while (sessionsToCreate > 0) {

				// if (size < 9)
				// title = programTitle + " 00" + (size + 1);
				// if (size >= 9 && size < 99)
				// title = programTitle + " 0" + (size + 1);
				// if (size >= 99)
				// title = programTitle + " " + (size + 1);

				title = programTitle + " Ep " + (size + 1);
				Quotation q = (Quotation) this.transformProgram(program, title, programLines, Quotation.class);
				logger.info("Session-Budget successfully created : budget id : " + q.getId());
				sessionsToCreate--;
				size++;
				title = "";
			}
			logger.info("Successfully created a single session for program : id = " + program.getId());
			program.setSessioned(true);
			program = recordRepository.save(program);
			logger.info("Successfully made sessions");
		} else {
			logger.info("Failed to make sessions for program : " + program.getId());
			logger.info("Failed due to invalid program sessions.");
			throw new UnprocessableEntityException("Invalid program sessions");
		}

		logger.info("Returning after creating all sessions.");
		ProgramDTO response = new ProgramDTO(program);
		this.addAvailableResources(response);
		response.setRecordDiscriminator(Constants.SHOWS_STRING.toString());
		return response;
	}

	/**
	 * Method to validate program-show session details
	 * 
	 * @param program
	 */
	private void validateProgramSession(Program program) {
		logger.info("Inside RecordService :: validateProgramSession(), To validate program-show session.");

		String toDoStatus = ProjectStatusName.TO_DO.getProjectStatusNameString();
		String inProgressStatus = ProjectStatusName.IN_PROGRESS.getProjectStatusNameString();
		String doneStatus = ProjectStatusName.DONE.getProjectStatusNameString();

		String rejectedStatus = ProjectFinancialStatusName.REJECTED.getProjectFinancialStatusNameString();
		String approvedStatus = ProjectFinancialStatusName.APPROVED.getProjectFinancialStatusNameString();
		String revisedStatus = ProjectFinancialStatusName.REVISED.getProjectFinancialStatusNameString();

		if (program == null) {
			logger.info("Invalid Program");
			throw new UnprocessableEntityException("Invalid Program");
		}
		if (program.getLines() == null && (program.getLines() != null && program.getLines().isEmpty())) {
			logger.info("Please insert atleast one program show line");
			throw new UnprocessableEntityException("Please insert atleast one program show line");
		}
		if (program.getNbEpisodes() == null || program.getNbEpisodes() <= 0) {
			logger.info("Please provide valid episodes.");
			throw new UnprocessableEntityException("Please provide valid episodes.");
		}
		if (program.getNbSessions() == null || program.getNbSessions() <= 0) {
			logger.info("Please provide valid sessions.");
			throw new UnprocessableEntityException("Please provide valid sessions.");
		}

		// validate status and financial status
		if (program.getStatus() == null) {
			logger.info("Cannot create session due to invalid status");
			throw new UnprocessableEntityException("Cannot create session due to invalid status");
		}
		String status = program.getStatus().getKey();
		String financialStatus = program.getFinancialStatus();

		if ((status.equalsIgnoreCase(toDoStatus) || status.equalsIgnoreCase(inProgressStatus))
				&& StringUtils.isBlank(financialStatus)) {
			logger.info("Cannot create projects without show approval");
			throw new UnprocessableEntityException("Cannot create projects without show approval");
		}
		if ((status.equalsIgnoreCase(toDoStatus) || status.equalsIgnoreCase(inProgressStatus))
				&& !StringUtils.isBlank(financialStatus) && financialStatus.equalsIgnoreCase(rejectedStatus)) {
			logger.info("Cannot create projects as show is rejected");
			throw new UnprocessableEntityException("Cannot create projects as show is rejected");
		}
		if ((status.equalsIgnoreCase(toDoStatus) || status.equalsIgnoreCase(inProgressStatus))
				&& !StringUtils.isBlank(financialStatus) && financialStatus.equalsIgnoreCase(revisedStatus)) {
			logger.info("Cannot create projects, requires show approval");
			throw new UnprocessableEntityException("Cannot create projects, requires show approval");
		}

		if (program.getTheme() == null) {
			logger.info("Please provide theme");
			throw new UnprocessableEntityException("Please provide theme");
		}

		logger.info("Successfully validated program-session.");
	}

	/**
	 * To transform Program-Show to Budget(Quotation)
	 * 
	 * @param program
	 * @param title
	 * @param programLines
	 * @param type
	 * 
	 * @return the Program-Show wrapper object
	 * 
	 * @throws UnprocessableEntityException
	 */
	public Record transformProgram(Program program, String title, List<Line> programLines, Class<? extends Record> type)
			throws UnprocessableEntityException {
		logger.info("Inside RecordService :: transformProgram(), To transform program to budget-quotation.");
		Record copy = null;
		try {
			copy = program.getCloneOfProgram(title, program);
			// ((Program) copy).setLinkedRecords(null);
			if (copy.getLines() != null) {
				copy.getLines().clear();
			}
			copy = this.transform(copy, programLines, type);
			return copy;
		} catch (Exception e) {
			String message = "An unexpected error occurs while transforming " + program.getId() + " to " + type;
			logger.error(message, e);
			throw new UnprocessableEntityException("Budget session transform failed");
		}
	}

	/**
	 * To set budget-lines on newly created budget
	 * 
	 * @param record
	 * @param programLines
	 * @param type
	 * 
	 * @return the newly created budget from program-show
	 * 
	 * @throws UnprocessableEntityException
	 */
	protected Record transform(Record record, List<Line> programLines, Class<? extends Record> type)
			throws UnprocessableEntityException {
		logger.info(
				"Inside RecordService :: transform(), To transform program-show to budget and set budget-lines on newly created budget.");
		Record former = null;
		try {
			if (type == null) {
				logger.info("No type provided to transform program-show to new record session : " + record);
				throw new UnprocessableEntityException(
						"No type provided to transform program-show to new record session");
			}

			List<Line> lines = programLines;
			if (lines != null && !lines.isEmpty()) {
				lines = programLines.stream().map(bookingLine -> {
					Line entity = null;
					// entity = this.validateLineCalculation(record, new BookingDTO(bookingLine),
					// type);
					if (bookingLine instanceof Booking) {
						Booking book = (Booking) bookingLine;
						// entity = this.cloneLine((Quotation) record, book);
						entity = this.cloneLine(record, book);
					}
					return entity;
				}).collect(Collectors.toList());

				record.setLines(lines);

			} else {
				if (record.getLines() != null && record.getLines().isEmpty()) {
					record.getLines().clear();
				}
			}
			record.updateAmounts();
			record.setTotalPrice(record.getCalcTotalPrice());
			record.setCalcTotalPriceIVAT(record.getCalcTotalPrice());
			former = (Record) recordRepository.save(record);
			former.updateAmounts();
			if (former != null && former instanceof Quotation) {
				logger.info("Newly created budget with an id : " + former.getId()
						+ " has been successfully transformed from program with id : " + record.getId());
			} else {
				logger.warn(former + " can't be transformed to " + type);
				throw new UnprocessableEntityException("Program-Show instance session can't be transformed to Budget");
			}

			return former;
		} catch (Exception e) {
			String message = "An unexpected error occurs while transforming " + record + " to " + type;
			logger.error(message, e);
			throw new UnprocessableEntityException("Budget session transform failed");
		}
	}

	/**
	 * Method to make a clone of budget-line
	 * 
	 * @param budget
	 * @param savedBooking
	 * 
	 * @return the budget-line object
	 */
	// protected Booking cloneLine(Quotation budget, Booking savedBooking) {
	protected Booking cloneLine(Record budget, Booking savedBooking) {
		logger.info("Inside RecordService :: cloneLine(), To make clone of lines");
		try {
			Booking booking = savedBooking.cloneWhitoutEvent();
			if (booking.getFunction() != null && savedBooking.getResource() != null) {

				if (savedBooking.getResource() == null) {
					DefaultResource resource = new DefaultResource();
					if (booking.getFunction().getDefaultResource() != null) {
						resource = booking.getFunction().getDefaultResource();
					}
					booking.setResource(resource);
				} else {
					DefaultResource resource = new DefaultResource();
					if (booking.getFunction().getDefaultResource() != null) {
						resource = booking.getFunction().getDefaultResource();
					}
					booking.setResource(resource);
				}

				// booking.setId(null);
				booking.setRecord(budget);

				// need discussion on the quantity of budget-line over the session(budget)
				booking.setOccurrenceCount(savedBooking.getOccurrenceCount());

				if (savedBooking.getExtras() != null) {
					List<ExtraLine> extraList = new ArrayList<ExtraLine>();
					List<ExtraLine> dbExtraLine = savedBooking.getExtras();
					Booking bookEntity = booking;
					for (int k = 0; k < dbExtraLine.size(); k++) {
						ExtraLine extra = dbExtraLine.get(k);

						ExtraLine lineExtra = new ExtraLine();
						lineExtra.setId(null);
						lineExtra.setLine(bookEntity);
						lineExtra.setLabel(extra.getLabel());
						lineExtra.setExtraTotal(extra.getExtraTotal());
						lineExtra.setExtraTotalPrice(extra.getExtraTotalPrice());

						extraList.add(lineExtra);
					}
					booking.setExtras(extraList);
				}

				booking.setQtySoldPerOc(savedBooking.getQtySoldPerOc());
				booking.setQtyUsedPerOc(savedBooking.getQtyUsedPerOc());
				booking.setDiscountRate(savedBooking.getDiscountRate());
				booking.setVat(savedBooking.getVat());

				if (booking instanceof Line) {
					booking = (Booking) this.calculateAndUpdateBudgetLineCosts(booking);
				}

				return booking;
			}
		} catch (CloneNotSupportedException e) {
			logger.info("Failure in making clone of booking-lines from show-lines on Record : record-id : "
					+ budget.getId());
			logger.info("Failure in making clone of booking-lines from show-lines due to : " + e.getStackTrace());
			throw new UnprocessableEntityException("Failure in making booking-lines from show-lines");
		} catch (Exception e) {
			logger.info("Failure in making booking-lines from show-lines on Record : recordId : " + budget.getId());
			logger.info("Failure in making booking-lines from show-lines due to : " + e.getStackTrace());
			throw new UnprocessableEntityException("Failure in making booking-lines from show-lines");
		}
		return null;
	}

	/**
	 * Method to create a single session Budget for the Program-Show
	 * 
	 * @param programId the programId to set
	 * 
	 * @return the program-show wrapper object
	 */
	public ProgramDTO createSingleSession(long programId) {
		logger.info(
				"Inside RecordService :: createSingleSession, To make a single session for program Id : " + programId);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to create shows-sessions.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		Program program = (Program) this.validateRecordExistence(Program.class, programId);

		if (program.isSessioned()) {
			logger.warn("Session have been created for this program-show : programId : " + program.getId());
			throw new UnprocessableEntityException("Session has been already created for this show");
		}

		validateProgramSession(program);

		if (program != null && program.getNbEpisodes() != null && program.getNbEpisodes().intValue() >= 0
				&& program.getNbSessions() != null && program.getNbSessions().intValue() >= 0) {

			int size = program.getLinkedRecords().size();
			if (size >= program.getNbEpisodes()) {
				logger.warn("All episodes have already been created for this program-show : id = " + program.getId());
				logger.warn("Program episodes full for program-show : id = " + program.getId());
				throw new UnprocessableEntityException("All episodes have already been created for this program-show");
			}
			List<Line> programLines = program.getLines();
			// String title = program.getTitle() + " " + (program.getLinkedRecords().size()
			// + 1);
			String title = program.getTitle();
			Quotation q = (Quotation) this.transformProgram(program, title, programLines, Quotation.class);
			logger.info("Session-Budget successfully created : budget id : " + q.getId());
			logger.info("Successfully created a single session for program : id = " + program.getId());

			program.setSessioned(true);
			program = recordRepository.save(program);
			logger.info("Successfully made sessions");
		} else {
			logger.info("Failed to make session for program : " + program.getId());
			logger.info("Failed due to invalid program sessions.");
			throw new UnprocessableEntityException("Invalid program sessions");
		}

		logger.info("Returning after creating a single session.");

		ProgramDTO response = new ProgramDTO(program);
		this.addAvailableResources(response);
		response.setRecordDiscriminator(Constants.SHOWS_STRING.toString());
		return response;
	}

	/**
	 * Method to save line on program-show
	 * 
	 * @param recordId
	 * @param dto
	 * @param discriminator
	 * 
	 * @return the Program-Show object
	 */
	public ProgramDTO saveProgramLine(long recordId, BookingDTO dto, Class<?> discriminator) {
		logger.info("Inside RecordService :: saveProgramLine(), To save a program-line on : recordId : " + recordId
				+ ", Line : " + dto + ", for discriminator : " + discriminator);
		Program program = null;
		Record record = validateRecordExistence(discriminator, recordId);
		if (record == null) {
			logger.info("Invalid Record Id : " + recordId);
			throw new NotFoundException("Record not found");
		} else {
			if (record instanceof Program) {
				program = (Program) record;
			}
		}

		if (program == null) {
			logger.warn("Cannot cast record into Program");
			throw new UnprocessableEntityException("Cannot cast record into Program");
		}

		String statusInProgress = ProjectStatusName.IN_PROGRESS.getProjectStatusNameString();
		String statusToDo = ProjectStatusName.TO_DO.getProjectStatusNameString();
		String statusDone = ProjectStatusName.DONE.getProjectStatusNameString();

		String statusApproved = ProjectFinancialStatusName.APPROVED.getProjectFinancialStatusNameString();
		String statusRejected = ProjectFinancialStatusName.REJECTED.getProjectFinancialStatusNameString();
		String statusRevised = ProjectFinancialStatusName.REVISED.getProjectFinancialStatusNameString();

		XmlEntity statusEntity = program.getStatus();
		if (statusEntity == null) {
			logger.warn("Please provide show status");
			throw new UnprocessableEntityException("Please provide show status");
		}
		String programStatus = statusEntity.getKey();
		String financialStatus = program.getFinancialStatus();

		// ((To Do or In Progress))
		if (programStatus.equalsIgnoreCase(statusToDo) || programStatus.equalsIgnoreCase(statusInProgress)) {

			if ((StringUtils.isBlank(financialStatus))
					|| (!StringUtils.isBlank(financialStatus) && financialStatus.equalsIgnoreCase(statusRevised))) {
				return (ProgramDTO) this.saveBudgetLine(recordId, dto, discriminator);

			} else if (!StringUtils.isBlank(financialStatus) && financialStatus.equalsIgnoreCase(statusRejected)) {
				logger.warn("Cannot add or edit lines as status is rejected");
				throw new UnprocessableEntityException("Cannot add or edit lines as status is Rejected");

			} else if (!StringUtils.isBlank(financialStatus) && financialStatus.equalsIgnoreCase(statusApproved)) {
				logger.warn("Cannot add or edit lines as status is approved");
				throw new UnprocessableEntityException("Cannot add or edit lines as status is Approved");
			}

		} else if (programStatus.equalsIgnoreCase(statusDone) && !StringUtils.isBlank(financialStatus)
				&& financialStatus.equalsIgnoreCase(statusApproved)) {
			logger.warn("Cannot add or edit lines as status is Done and Approved");
			throw new UnprocessableEntityException("Cannot add or edit lines as status is Done and Approved");

		} else if (programStatus.equalsIgnoreCase("Archived") && !StringUtils.isBlank(financialStatus)
				&& financialStatus.equalsIgnoreCase(statusApproved)) {
			logger.warn("Cannot add or edit lines as status is Archived and Approved");
			throw new UnprocessableEntityException("Cannot add or edit lines as status is Archived and Approved");
		}
		logger.info("Returning after validating a program-line.");
		return null;
	}

	/**
	 * To get Budgets by company id.
	 * 
	 * @param id
	 * 
	 * @return List of Budgets
	 */
	public List<QuotationDTO> getBudgetByCompany(long id) {
		logger.info("Inside RecordService :: getBudgetByCompany()");
		List<Record> records = this.recordRepository.findRecordByCompany(id, Quotation.class);
		return records.stream().map(r -> {
			Quotation q = (Quotation) r;
			return new QuotationDTO(q.getId(), " ", q.getTitle());
		}).collect(Collectors.toList());
	}

	/**
	 *
	 * Method to apply project-template on budget or booking
	 * 
	 * @param budgetId
	 * @param recordDTO
	 * @param discriminator
	 * 
	 * @return the record children wrapper object
	 * 
	 * @throws MalformedURLException
	 */
	public RecordDTO saveOrUpdateTemplate(long budgetId, RecordDTO recordDTO, Class<?> discriminator)
			throws MalformedURLException {
		logger.info("Inside RecordService :: saveOrUpdateTemplate(), To save template on "
				+ discriminator.getSimpleName() + ", with recordId : " + budgetId);
		StaffMember loggedInUser = authenticationService.getAuthenticatedUser();
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + loggedInUser.getFirstName() + " tried to save template.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}

		Record parentRecord = validateRecordExistence(discriminator, budgetId);
		Layout template = null;
		Record existingLayout = null;
		LayoutDTO layoutDTO = (LayoutDTO) recordDTO;

		XmlEntity statusEntity = parentRecord.getStatus();
		if (statusEntity == null) {
			logger.info("Invalid status");
			throw new UnprocessableEntityException("Invalid status");
		}

		// if (StringUtils.isBlank(parentRecord.getFinancialStatus())) {
		// logger.info("Invalid financial status.");
		// throw new UnprocessableEntityException("Invalid financial status.");
		// }

		List<Line> recordLines = parentRecord.getLines();
		if (recordLines == null || recordLines.isEmpty()) {
			logger.info(discriminator.getSimpleName() + " must have atleast one line event.");
			throw new UnprocessableEntityException("Record must have atleast one line event.");
		}

		String statusInProgress = ProjectStatusName.IN_PROGRESS.getProjectStatusNameString();
		String statusToDo = ProjectStatusName.TO_DO.getProjectStatusNameString();
		String statusRevised = ProjectFinancialStatusName.REVISED.getProjectFinancialStatusNameString();

		String status = statusEntity.getKey();
		String financialStatus = parentRecord.getFinancialStatus();

		if ((status.equalsIgnoreCase(statusToDo) || status.equalsIgnoreCase(statusInProgress)) && (StringUtils
				.isBlank(financialStatus)
				|| (!StringUtils.isBlank(financialStatus) && financialStatus.equalsIgnoreCase(statusRevised)))) {

			if (layoutDTO.getId() != null && layoutDTO.getId() != 0) {
				template = (Layout) validateRecordExistence(Layout.class, recordDTO.getId());

				if (template == null) {
					logger.info("Record found is not an instance of Layout");
					throw new UnprocessableEntityException("Record found is not an instance of Layout");
				}

				existingLayout = recordRepository.getLayoutByProjectId(Layout.class, parentRecord.getId());
				if (existingLayout != null && !existingLayout.getId().equals(template.getId())) {
					logger.info("Template already created from this record");
					throw new UnprocessableEntityException("Template already created from this record");
				}
				if (StringUtils.isBlank(layoutDTO.getTitle())) {
					logger.info("Please provide a valid project template title");
					throw new UnprocessableEntityException("Please provide a valid project template title");
				}
				if (!StringUtils.isBlank(template.getTitle()) && !template.getTitle().equals(layoutDTO.getTitle())) {
					logger.info("You cannot change the title of template");
					throw new UnprocessableEntityException("You cannot change the title of template");
				}

			} else {
				// making a new template

				existingLayout = recordRepository.getLayoutByProjectId(Layout.class, parentRecord.getId());
				if (existingLayout != null) {
					logger.info("Template already created from this record");
					throw new UnprocessableEntityException("Template already created from this record");
				}
				if (StringUtils.isBlank(layoutDTO.getTitle())) {
					logger.info("Please provide a valid project template title");
					throw new UnprocessableEntityException("Please provide a valid project template title");
				}
				if (recordRepository.getLayoutByTitle(Layout.class, layoutDTO.getTitle()) != null) {
					logger.info("Template with this title already exists : title : " + layoutDTO.getTitle());
					throw new UnprocessableEntityException("Template with this title already exists");
				}

				template = layoutDTO.getLayout(new Layout(), layoutDTO);

				// setting lines
				List<Line> lines = new ArrayList<Line>();
				for (Line bookingLine : recordLines) {
					Line entity = null;
					if (bookingLine instanceof Booking) {
						Booking book = (Booking) bookingLine;
						entity = this.cloneLine(template, book);
						lines.add(entity);
					}
				}
				template.setLines(lines);
			}
		} else {
			logger.info("Invalid status or financial status");
			throw new UnprocessableEntityException("Invalid status or financial status");
		}

		// setting the source of template
		template.setSource(parentRecord);
		template.updateAmounts();
		template.setTotalPrice(template.getCalcTotalPrice());
		template.setTotalPrice(template.getCalcTotalPrice());
		template.setCalcTotalPriceIVAT(template.getCalcTotalPrice());
		template = recordRepository.save(template);

		saveLoggedInUserRecordHistory(loggedInUser, parentRecord, discriminator);

		logger.info("Successfully saved template with title : " + template.getTitle() + " & templateId : "
				+ template.getId() + ", on record with id : " + parentRecord.getId());
		if (discriminator.equals(Quotation.class)) {
			QuotationDTO response = new QuotationDTO((Quotation) parentRecord);
			response.setRecordDiscriminator(Constants.BUDGET_STRING.toString());
			this.addAvailableResources(response);
			if (template != null) {
				LayoutDTO dto = new LayoutDTO(template, true);
				response.setTemplateLayout(dto);
			}
			return response;
		} else if (discriminator.equals(Project.class)) {
			ProjectDTO response = new ProjectDTO((Project) parentRecord);
			response.setRecordDiscriminator(Constants.BOOKING_STRING.toString());
			this.addAvailableResources(response);
			if (template != null) {
				LayoutDTO dto = new LayoutDTO(template, true);
				response.setTemplateLayout(dto);
			}
			return response;
		} else if (discriminator.equals(Program.class)) {
			ProgramDTO response = new ProgramDTO((Program) parentRecord);
			response.setRecordDiscriminator(Constants.BUDGET_STRING.toString());
			this.addAvailableResources(response);
			if (template != null) {
				LayoutDTO dto = new LayoutDTO(template, true);
				response.setTemplateLayout(dto);
			}
			return response;
		}
		return null;
	}

	/**
	 * Method to apply template on budget-quotation or booking-project
	 * 
	 * @param budgetId
	 * @param templateId
	 * @param discriminator
	 * 
	 * @return record(budget-quotation or booking-project)
	 * 
	 * @throws MalformedURLException
	 */
	public RecordDTO applyTemplate(long budgetId, long templateId, Class<?> discriminator)
			throws MalformedURLException {
		logger.info("Inside RecordService :: saveOrUpdateTemplate(), To save template on "
				+ discriminator.getSimpleName() + ", with recordId : " + budgetId);
		StaffMember loggedInUser = authenticationService.getAuthenticatedUser();
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + loggedInUser.getFirstName() + " tried to save template.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}

		Record record = this.validateRecordExistence(discriminator, budgetId);
		Record template = this.validateRecordExistence(Layout.class, templateId);

		if (discriminator.equals(Quotation.class)) {
			Project booking = recordRepository.getProjectByQuotationId(Project.class, budgetId);
			if (booking != null) {
				logger.info("Booking has been already done, so cannot apply template");
				throw new UnprocessableEntityException("Booking has been already done, so cannot apply template");
			}
		}

		XmlEntity statusEntity = record.getStatus();
		if (statusEntity == null) {
			logger.info("Please provide valid status");
			throw new UnprocessableEntityException("Please provide valid status");
		}
		List<Line> templateLines = template.getLines();
		if (templateLines == null || templateLines.isEmpty()) {
			logger.info("Atleast one line must be present on template : id : " + template.getId());
			throw new UnprocessableEntityException("Atleast one line must be present on template");
		}
		String status = statusEntity.getKey();
		String financialStatus = record.getFinancialStatus();

		String statusInProgress = ProjectStatusName.IN_PROGRESS.getProjectStatusNameString();
		String statusToDo = ProjectStatusName.TO_DO.getProjectStatusNameString();
		String statusRevised = ProjectFinancialStatusName.REVISED.getProjectFinancialStatusNameString();

		if ((status.equalsIgnoreCase(statusToDo) || status.equalsIgnoreCase(statusInProgress)) && (StringUtils
				.isBlank(financialStatus)
				|| (!StringUtils.isBlank(financialStatus) && financialStatus.equalsIgnoreCase(statusRevised)))) {

			for (Line bookingLine : templateLines) {
				Line entity = null;
				if (bookingLine instanceof Booking) {
					Booking book = (Booking) bookingLine;
					entity = this.cloneLine(template, book);
					record.addLine(entity);
				}
			}

		} else {
			logger.info("Invalid status or financial status");
			throw new UnprocessableEntityException("Invalid status or financial status");
		}

		record.updateAmounts();
		record = this.updateStartAndEndTime(record);
		record.setTotalPrice(record.getCalcTotalPrice());
		record.setTotalPrice(record.getCalcTotalPrice());
		record.setCalcTotalPriceIVAT(record.getCalcTotalPrice());
		record = recordRepository.save(record);
		saveLoggedInUserRecordHistory(loggedInUser, record, discriminator);

		logger.info("Successfully applied template with templateId : " + template.getId() + ", on record with id : "
				+ record.getId());

		Record existingLayout = recordRepository.getLayoutByProjectId(Layout.class, record.getId());
		Layout layout = null;
		if (existingLayout instanceof Layout) {
			layout = (Layout) existingLayout;
		}

		if (discriminator.equals(Quotation.class)) {
			QuotationDTO response = new QuotationDTO((Quotation) record);
			response.setRecordDiscriminator(Constants.BUDGET_STRING.toString());
			this.addAvailableResources(response);
			if (layout != null) {
				LayoutDTO dto = new LayoutDTO(layout, true);
				response.setTemplateLayout(dto);
			}
			return response;
		} else if (discriminator.equals(Project.class)) {
			ProjectDTO response = new ProjectDTO((Project) record);
			response.setRecordDiscriminator(Constants.BOOKING_STRING.toString());
			this.addAvailableResources(response);
			if (layout != null) {
				LayoutDTO dto = new LayoutDTO(layout, true);
				response.setTemplateLayout(dto);
			}
			return response;
		} else if (discriminator.equals(Program.class)) {
			ProgramDTO response = new ProgramDTO((Program) record);
			response.setRecordDiscriminator(Constants.BUDGET_STRING.toString());
			this.addAvailableResources(response);
			if (layout != null) {
				LayoutDTO dto = new LayoutDTO(layout, true);
				response.setTemplateLayout(dto);
			}
			return response;
		}
		return null;
	}

	/**
	 * 
	 * Method to get all ProjectSchedulerDTO.
	 * 
	 * @return List of ProjectSchedulerDTO.
	 */
	public List<ProjectSchedulerDTO> getScheduledProject() {
		logger.info("Inside RecordService :: getScheduledProject()");
		List<Record> projects = recordRepository.getRecords(Project.class);

		List<Long> budgetId = projects.stream().filter(dt -> dt.getSource() != null).map(dt -> dt.getSource().getId())
				.sorted((b1, b2) -> b1.compareTo(b2)).collect(Collectors.toList());

		List<Record> quotataions = recordRepository.getRecords(Quotation.class);

		quotataions.forEach(dt -> {
			if (dt instanceof Quotation) {
				if (!budgetId.contains(dt.getId())) {
					projects.add(dt);
				}
			}
		});
		List<ProjectSchedulerDTO> projectSchedulerDTOs = projects.stream().map(ProjectSchedulerDTO::new)
				.collect(Collectors.toList());

		// projectSchedulerDTOs.forEach(System.out::println);
		logger.info("Successfully return from getScheduledProject()");
		return projectSchedulerDTOs;
	}

	/**
	 * Save budget from Project Scheduler.
	 * 
	 * @param quotationDTO
	 * 
	 * @return object of ProjectSchedulerDTO.
	 * 
	 * @throws MalformedURLException
	 */
	public ProjectSchedulerDTO saveBugdetFromEvent(QuotationDTO quotationDTO) throws MalformedURLException {
		logger.info("Inside RecordService :: saveBugdetFromEvent( " + quotationDTO + " ).");
		RecordDTO recordDTO = this.saveOrUpdateRecord(quotationDTO, QuotationDTO.class);
		Record record = recordRepository.findOne(recordDTO.getId());
		logger.info("Successfully saved Record :: " + record);
		return new ProjectSchedulerDTO(record);
	}

	/**
	 * Method to get list of program event.
	 * 
	 * @param programId
	 * 
	 * @return object of ProgramSchedulerDTO.
	 */
	public ProgramSchedulerDTO getAllProgramEvent(long programId) {
		logger.info("Inside RecordService :: getAllProgramEvent( " + programId + " ).");
		Record record = recordRepository.getRecordById(Program.class, programId);
		if (record == null || !(record instanceof Program)) {
			logger.info("Invalid Program Id : " + programId);
			throw new NotFoundException("Program not found");
		}
		final Program program = (Program) record;

		List<ResourceDTO<?>> resourceDTOs = new ArrayList<>();
		List<ProgramEventDTO> programEventDTOs = new ArrayList<>();
		if (program.getLinkedRecords() != null && !program.getLinkedRecords().isEmpty()) {
			program.getLinkedRecords().parallelStream().filter(pr -> pr instanceof Project).map(pr -> (Project) pr)
					.forEach(project -> {
						resourceDTOs.add(new ResourceDTO<>(project.getId(), project.getTitle()));

						if (project.getLines() != null && !project.getLines().isEmpty()) {

							List<Booking> bookings = project.getLines().stream().filter(line -> line instanceof Booking)
									.map(line -> (Booking) line)
									.filter(booking -> booking.getFrom() != null && booking.getTo() != null)
									.collect(Collectors.toList());

							logger.info("bookings size => " + bookings.size());
							if (!bookings.isEmpty()) {

								Date bookingStartDate = bookings.stream()
										.sorted((b1, b2) -> b1.getFrom().getTime().before(b2.getFrom().getTime()) ? -1
												: 1)
										.map(booking -> booking.getFrom().getTime()).findFirst().orElse(null);

								Date bookingEndDate = bookings.stream()
										.sorted((b1, b2) -> b1.getTo().getTime().after(b2.getTo().getTime()) ? -1 : 1)
										.map(booking -> booking.getTo().getTime()).findFirst().orElse(null);

								logger.info("bookingStartDate => " + bookingStartDate + " bookingEndDate => "
										+ bookingEndDate);

								if (bookingStartDate != null && bookingEndDate != null) {
									programEventDTOs.add(new ProgramEventDTO(project.getId(), project.getId(),
											new DateTime(bookingStartDate).withZone(DateTimeZone.UTC).toString(),
											new DateTime(bookingEndDate).withZone(DateTimeZone.UTC).toString(),
											project.getTitle(),
											program.getTheme() != null ? program.getTheme().getKey() : "green"));

								}
							}
						}

					});
		}
		logger.info("Successfully return from getAllProgramEvent");
		ProgramSchedulerDTO programSchedulerDTO = new ProgramSchedulerDTO(resourceDTOs.stream().sorted((re1, re2) -> {
			if (re1 == null || re1.getName() == null) {
				return -1;
			}
			if (re2 == null || re2.getName() == null) {
				return 1;
			}
			return re1.getName().compareTo(re2.getName());
		}).collect(Collectors.toList()), programEventDTOs);
		return programSchedulerDTO;
	}

	/**
	 * Method to place an order
	 * 
	 * @param orderDTO
	 */
	public OrderDTO placeOrder(OrderDTO orderDTO) {
		logger.info("Inside RecordService :: placeOrder(), To place an order");
		Project project = null;
		Company supplier = null;
		long quotationId = 0;
		if (orderDTO == null) {
			logger.info("Please provide valid data");
			throw new UnprocessableEntityException("Please provide valid data");
		}
		List<BookingDTO> bookings = orderDTO.getLines();
		if (bookings != null && !bookings.isEmpty()) {
			try {
				project = (Project) this.validateRecordExistence(Project.class, orderDTO.getProjectId());
				if (project == null) {
					logger.warn("Project booking not found");
					throw new UnprocessableEntityException("Project booking not found");
				}
				quotationId = project.getSource().getId();
			} catch (Exception e) {
				logger.warn("Project booking not found");
				throw new UnprocessableEntityException("Project booking not found");
			}
			if (orderDTO.getSupplierId() != 0) {
				supplier = companyRepository.findOne(orderDTO.getSupplierId());
				if (supplier == null) {
					logger.warn("Supplier not found");
					throw new UnprocessableEntityException("Supplier not found");
				}
			} else {
				logger.info("No supplier selected to make order");
				throw new UnprocessableEntityException("No supplier selected to make order");
			}

			Order order = new Order();
			Record orderRecord = this.getRecordByProjectAndSupplier(orderDTO.getProjectId(), orderDTO.getSupplierId());
			if (orderRecord != null) {
				order = (Order) orderRecord;
			} else {
				logger.info("Placing new order");
				order.setCurrency(project.getCurrency());
				order.setDate(Calendar.getInstance());
				order.setCompany(supplier);
				SupplyResource resource = new SupplyResource();
				if (order.getResource() == null) {
					resource.setOrder(order);
				}
			}

			float totalPrice = 0f;
			for (BookingDTO dto : bookings) {
				OrderLine line = this.createOrderLine(dto, order);
				order.addBooking(line);

				if (!line.getDisabled()) {
					Float price = line.getTotalPriceWithOccurenceCount();
					if (price != null)
						totalPrice += price;
				}
				order.setTotalPrice(totalPrice);
				order.setTotalNetPrice(totalPrice);
				order.setCalcTotalPriceIVAT(totalPrice);

				// order.updateAmounts();
			}

			order.setSource(project);
			order = recordRepository.save(order);
			order.updateAmounts();
			Order newOrder = order;
			order.getLines().stream().forEach(lineEntity -> {
				OrderLine orderLine = (OrderLine) lineEntity;
				Booking booking = orderLine.getItem();
				booking.setResource(newOrder.getResource());
				bookingRepository.save(booking);
			});
			return new OrderDTO(order);
		} else {
			logger.info("No booking-lines selected to make order");
			throw new UnprocessableEntityException("No booking-lines selected to make order");
		}
	}

	/**
	 * Method to create order-Line
	 * 
	 * @param dto
	 * @param order
	 * 
	 * @return the orderLine object
	 */
	private OrderLine createOrderLine(BookingDTO dto, Record order) {
		logger.info("Inside RecordService :: createOrderLine(), To create order-line");

		Booking bookingSource = bookingRepository.findOne(dto.getId());
		if (bookingSource == null) {
			logger.info("Booking-Line not found");
			throw new UnprocessableEntityException("Booking-Line not found");
		}

		if (bookingSource.getResource() != null && bookingSource.getResource() instanceof Resource
				&& !(bookingSource.getResource() instanceof DefaultResource)) {
			logger.error("There is already a resource in this booking line.");
			throw new UnprocessableEntityException("There is already a resource in this booking line.");
		}
		// Basis and current null,empty check is done as default currency is not
		// implemented yet.
		if (bookingSource.getUnitUsed() == null || StringUtils.isBlank(bookingSource.getUnitUsed().getKey())) {
			logger.error("Please set basis before ordering.");
			throw new UnprocessableEntityException("Please set basis before ordering.");

		}

		if (bookingSource.getFunction() != null && bookingSource.getFunction() instanceof StaffFunction) {
			logger.error("Staff function is not allowed for vendor order.");
			throw new UnprocessableEntityException("Staff function is not allowed for vendor order.");
		}

		OrderLine entity = new OrderLine();

		entity.setId(null);
		entity.setFrom(new DateTime(dto.getFrom()).withZone(DateTimeZone.forID("Asia/Calcutta")).toGregorianCalendar());
		entity.setTo(new DateTime(dto.getTo()).withZone(DateTimeZone.forID("Asia/Calcutta")).toGregorianCalendar());
		RatedFunctionDTO fun = dto.getFunction();
		Function funEntity = null;
		if (fun != null) {
			funEntity = functionRepository.findOne(fun.getId());
			entity.setFunction((RatedFunction) funEntity);
		}
		entity.setResource(null);
		if (!StringUtils.isBlank(dto.getPersistentCurrency())) {
			Currency currency = Currency.getInstance(dto.getPersistentCurrency());
			entity.setCurrency(currency);
		} else {
			entity.setCurrency(Currency.getInstance(currencyService.getDefaultCurrency().getCode()));
		}
		entity.setRecord(order);
		entity.setComment(dto.getComment());
		entity.setOccurrenceCount(1f);
		entity.setDisabled(dto.getDisabled() != null ? dto.getDisabled() : Boolean.FALSE);
		entity.setFaulty(dto.isFaulty());
		entity.setSyncQty(dto.getSyncQty());
		entity.setDiscountRate(dto.getDiscountRate());

		if (bookingRepository.findOrderLineByBooking(OrderLine.class, bookingSource.getId()) != null) {
			logger.error("There is already an order for this booking line.");
			throw new UnprocessableEntityException("There is already an order for this booking line.");
		}
		entity.setItem(bookingSource);

		entity = (OrderLine) this.applyRate(entity, order);
		if (entity.getRate() == null) {
			// set the dto default rate details on order-line
			entity.setCurrency(bookingSource.getCurrency());
			entity.setUnitSold(bookingSource.getUnitSold());
			entity.setUnitUsed(bookingSource.getUnitUsed());
			entity.setUnitCost(bookingSource.getUnitCost());
			entity.setUnitPrice(bookingSource.getUnitPrice());
			entity.setQtySoldPerOc(bookingSource.getQtySoldPerOc());
			entity.setQtyTotalUsed(bookingSource.getQtyTotalUsed());
			entity.setQtyUsedPerOc(bookingSource.getQtyUsedPerOc());
			entity.setFreeQuantity(bookingSource.getFreeQuantity());
			entity.setSaleEntity(bookingSource.getSaleEntity());
		}
		entity = (OrderLine) this.calculateAndUpdateBudgetLineCosts(entity);

		entity.update();

		logger.info("Returning after creating order-line");
		return entity;
	}

	/**
	 * Method to get unscheduled bookings
	 * 
	 * @param discriminator
	 * @param projectId
	 * 
	 * @return list of unscheduled bookings
	 */
	public List<BookingDTO> getUnscheduledBookings(Class<?> discriminator, long projectId) {
		logger.info(
				"Inside RecordService :: getUnscheduledBookings(), To get unscheduled booking for id : " + projectId);
		Record project = this.validateRecordExistence(discriminator, projectId);
		if (project == null) {
			logger.warn("Project not found with given id : " + projectId);
			throw new NotFoundException("Project not found");
		}
		List<Order> orders = recordRepository.findRecordByProjectId(Order.class, projectId).stream().map(order -> {
			if (order instanceof Order) {
				Order entity = (Order) order;
				return entity;
			}
			return null;
		}).collect(Collectors.toList());

		List<BookingDTO> bookings = new ArrayList<BookingDTO>();

		if (orders == null || orders.isEmpty()) {

			bookings = bookingRepository.getBookingsByRecord(projectId).stream().filter(book -> {
				if (book.getResource() == null) {
					return true;
				} else if (book.getResource() != null && book.getResource() instanceof DefaultResource) {
					return true;
				}
				return false;
			}).map(book -> new BookingDTO(book)).collect(Collectors.toList());
			logger.info("Successfully return unscheduled booking");
			return filterStaffFunction(bookings);

		} else if (orders != null && !orders.isEmpty()) {

			List<Long> listSourceLines = new ArrayList<Long>();

			for (Order order : orders) {

				if (order.getLines() != null && !order.getLines().isEmpty()) {
					List<BookingDTO> bookingLines = order.getLines().stream().filter(line -> {
						if (line instanceof OrderLine) {
							OrderLine orderLine = (OrderLine) line;
							if (orderLine.getItem() != null) {
								listSourceLines.add(orderLine.getItem().getId());
								return true;
							}
						}
						return false;
					}).map(line -> new BookingDTO((Booking) line, Boolean.TRUE, order.getCompany().getName()))
							.collect(Collectors.toList());

					bookings.addAll(bookingLines);
				}
			}

			// unscheduled bookings
			List<BookingDTO> unscheduledBookings = bookingRepository.getBookingsByRecord(projectId).stream()
					.filter(book -> {
						if (book.getResource() == null) {
							return true;
						} else if (book.getResource() != null && book.getResource() instanceof DefaultResource) {
							return true;
						}
						return false;
					}).map(book -> new BookingDTO(book)).collect(Collectors.toList());

			// adding the unscheduled-list and ordered list
			for (BookingDTO book : unscheduledBookings) {
				if (!listSourceLines.contains(book.getId())) {
					bookings.add(book);
				}
			}

		}
		return filterStaffFunction(bookings);

	}

	/**
	 * Method to get orders related to project-booking
	 * 
	 * @param discriminator
	 * @param projectId
	 * 
	 * @return list of orders related to project
	 */
	public List<OrderDTO> getOrdersByProjectId(Class<?> discriminator, long projectId) {
		logger.info("Inside RecordService :: getOrdersByProjectId(), To get orders by project");
		this.validateRecordExistence(discriminator, projectId);
		List<OrderDTO> orders = recordRepository.findRecordByProjectId(Order.class, projectId).stream()
				.map(order -> new OrderDTO((Order) order)).collect(Collectors.toList());
		logger.info("Successfully returning orders from project");
		return orders;
	}

	/**
	 * To get record by project and supplier
	 * 
	 * @param projectId
	 * @param supplierId
	 * 
	 * @return the order
	 */
	public Record getRecordByProjectAndSupplier(long projectId, long supplierId) {
		logger.info("Inside RecordService :: getRecordByProjectAndSupplier()");
		Record order = recordRepository.getRecordByProjectAndSupplier(Order.class, projectId, supplierId);
		return order;
	}

	/**
	 * To get vendors for procurement-orders
	 * 
	 * @param projectId
	 * 
	 * @return list of procurement vendors
	 */
	public List<SupplierDTO> getVendorsForProcurment(long projectId) {
		List<SupplierDTO> supplierDTOs = supplierService.getSuppliers();
		List<OrderDTO> orderDTOs = this.getOrdersByProjectId(Project.class, projectId);
		for (SupplierDTO supplierDTO : supplierDTOs) {
			orderDTOs.stream().forEach(order -> {
				if (order.getCompany().getId() == supplierDTO.getId()) {
					supplierDTO.setOrderDTO(order);
					;
				}
			});
		}
		List<SupplierDTO> list = supplierDTOs.stream().sorted((o1, o2) -> o1.getId().compareTo(o2.getId()))
				.collect(Collectors.toList());
		return list;
	}

	/**
	 * To get procurement by project id
	 * 
	 * @param recordId
	 * 
	 * @return the project wrapper object
	 */
	public ProjectDTO getProcurmentByProjectId(long recordId, Class<?> discriminator) {
		logger.info("Inside RecordService :: getProcurmentByProjectId()");
		ProjectDTO projectDTO = (ProjectDTO) findRecordById(discriminator, recordId);
		projectDTO.setExternalLinesForOrder(this.getUnscheduledBookings(Project.class, recordId));
		projectDTO.setVendorListForOrder(getVendorsForProcurment(recordId));
		logger.info("Successfully returning after getting project-procurment");
		return projectDTO;
	}

	/**
	 * To update an order
	 * 
	 * @param recordDTO
	 * @param discriminator
	 * 
	 * @return order wrapper object
	 */
	public OrderDTO updateOrder(RecordDTO recordDTO, Class<?> discriminator) {
		logger.info("Inside RecordService :: updateOrder(), To update a :  " + discriminator.getSimpleName()
				+ ", record object : " + recordDTO);
		StaffMember loggedInUser = authenticationService.getAuthenticatedUser();
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + loggedInUser.getFirstName() + " tried to save or update record.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		Record record = null;
		XmlEntity statusEntity = new XmlEntity();

		if (discriminator.equals(OrderDTO.class)) {
			logger.info("Record to be saved is an instance of Order.");

			if (recordDTO.getId() != null && recordDTO.getId() != 0) {
				record = (Order) validateRecordExistence(Order.class, recordDTO.getId());
				if (record.getSource() == null) {
					logger.info("Order not linked to any booking project");
					throw new UnprocessableEntityException("Order not linked to any booking project");
				}

				List<Line> dbLines = record.getLines();
				List<BookingDTO> dtoLines = recordDTO.getLines();

				if (dbLines != null && !dbLines.isEmpty() && dtoLines != null && !dtoLines.isEmpty()) {
					if (dbLines.size() != dtoLines.size()) {
						logger.info("Cannot add or delete order-lines");
						throw new UnprocessableEntityException("Cannot add or delete order-lines");
					}

					record.getLines().stream().forEach(line -> {
						BookingDTO dto = null;
						Long lineId = line.getId();
						Optional<BookingDTO> bookingDTO = dtoLines.stream()
								.filter(dt -> dt.getId() != null && dt.getId().longValue() == lineId.longValue())
								.findFirst();
						if (bookingDTO.isPresent()) {
							dto = bookingDTO.get();
						} else {
							logger.info("Order-Line not found with line-id : " + line.getId());
							throw new UnprocessableEntityException("Order-Line not found");
						}

						line.setDiscountRate(dto.getDiscountRate());
						Float unitPrice = dto.getUnitPrice();
						if (unitPrice == null || (unitPrice != null && unitPrice.longValue() <= 0)) {
							logger.info("Invalid price value on line with id : " + line.getId());
							throw new UnprocessableEntityException("Invalid price value");
						}
						line.setUnitPrice(unitPrice);
						line = this.calculateAndUpdateBudgetLineCosts(line);
						logger.info("successfully updated line calculation");
					});
					record.updateAmounts();
				} else {
					logger.info("Please enter order-lines");
					throw new UnprocessableEntityException("Please enter order-lines");
				}

				RecordInformationDTO information = recordDTO.getInformation();
				if (information != null && !StringUtils.isBlank(information.getComment())) {
					RecordInformation recordInformation = record.getInformation();
					if (recordInformation == null) {
						recordInformation = new RecordInformation();
						recordInformation.setComment(information.getComment());
						record.setInformation(recordInformation);
					} else {
						record.getInformation().setComment(information.getComment());
					}
				} else if (record.getInformation() != null) {
					record.getInformation().setComment(null);
					record.setInformation(null);
				}

				if (!StringUtils.isBlank(recordDTO.getStatus())) {
					statusEntity = new XmlEntity();
					statusEntity.setKey(recordDTO.getStatus());
					record.setStatus(statusEntity);
				}

				// adding tax
				if (recordDTO.getProcurementTax() != null && recordDTO.getProcurementTax().floatValue() > 0f) {
					float tax = recordDTO.getProcurementTax().floatValue();
					record.setProcurementTax(tax);
					float totalPriceIvat = record.getTotalPrice() != null ? record.getTotalPrice().floatValue() : 0f;
					float totalAmt = totalPriceIvat;
					float finalAmt = totalAmt + ((tax * totalAmt) / 100);

					record.setTotalNetPrice(finalAmt);
					record.setCalcTotalPriceIVAT(finalAmt);
				}

				logger.info("saving order for project, id : " + record.getSource().getId());
				record = recordRepository.save(record);
				// record.updateAmounts();
				logger.info("retrning saved order with id : " + record.getId());

				return new OrderDTO((Order) record);

			} else {
				logger.info("Order not found");
				throw new UnprocessableEntityException("Order not found");
			}
		}
		return null;
	}

	/**
	 * To get project/booking by id
	 * 
	 * @param id the id
	 * 
	 * @return project/booking
	 */
	public Project findProjectById(long id) {
		logger.info("Inside BookingService,findProjectById():: Finding project associated with id ");
		Project project = (Project) recordRepository.getRecordById(Project.class, id);
		logger.info("Returning from BookingService,findProjectById():: ");
		return project;
	}

	/**
	 * Create Contract For Project.
	 * 
	 * @param projectId
	 * @param bookingId
	 */
	public void createContract(Long projectId, Long bookingId) {
		logger.info("Inside createContract( " + projectId + " )");
		Booking booking = bookingService.findBooking(bookingId);
		Project project = this.findProjectById(projectId);
		if (project == null) {
			logger.info("Please enter a valid project Id.");
			throw new NotFoundException("Please enter a valid project Id.");
		}
		Contract contract = project.getContract();
		StaffMember staffMember = (StaffMember) booking.getResource().getEntity();
		boolean isDeleted = false;
		ContractSetting contractSetting = staffMember.getContractSetting();
		if (contractSetting != null) {
			if (staffMember.getContractSetting() instanceof EntertainmentContractSetting) {
				if (staffMember.getContractSetting().getType().getKey()
						.equalsIgnoreCase(ContractType.FREELANCE.toString())) {
					if (contract == null) {
						contract = new Contract();
						project.setContract(contract);
					}

					List<ContractLine> contractLines = contract.getLines();

					ContractLine contractLine = contractLines.stream()
							.filter(dt -> dt.getBookings() != null && dt.getBookings().get(0) != null
									&& dt.getBookings().get(0).getId().equals(booking.getId()))
							.findFirst().orElse(new ContractLine());

					if (contractLine.getId() == null) {
						contractLine.addBooking(booking);
						contractLine.setContract(contract);
						contractLines.add(contractLine);
						recordRepository.save(project);
					} else {
						contractLineRepositiory.save(contractLine);
					}
				} else {
					isDeleted = true;
				}
			} else {
				isDeleted = true;
			}
		} else if (contract != null) {
			isDeleted = true;
		}
		if (isDeleted && contract != null) {
			ContractLine contractLine = contract.getLines().stream().filter(dt -> dt.getBookings() != null
					&& dt.getBookings().get(0) != null && dt.getBookings().get(0).getId().equals(booking.getId()))
					.findFirst().orElse(null);

			if (contractLine != null) {
				contractLineRepositiory.delete(contractLine);
				System.out.println("Deleted Successfully");
			}

		}

		logger.info("Successfully return from createContract");
	}

	public List<RecordDTO> getRecordsByTodoORProgress() {
		List<RecordDTO> recordDtoInTododOrProgress = recordRepository.getRecordsByTodoORProgress(Project.class).stream()
				.filter(dt -> dt instanceof Project).map(dt -> (Project) dt).map(dt -> {
					ProjectDTO projectDTO = new ProjectDTO();
					projectDTO.setId(dt.getId());
					projectDTO.setTitle(dt.getTitle());
					return projectDTO;
				}).collect(Collectors.toList());
		return recordDtoInTododOrProgress;
	}

	/**
	 * To find project title by id
	 * 
	 * @param projectId
	 * 
	 * @return the project-title string
	 */
	public String findProjectTitleId(long projectId) {
		logger.info("Inside findProjectTitleId( " + projectId + " )");
		return recordRepository.findProjectTitleId(projectId);
	}

	/**
	 * To get list of records by milestoneType
	 * 
	 * @param milestoneType
	 * 
	 * @return list of records
	 */
	public List<Record> getRecordByMilestoneType(MilestoneType milestoneType) {
		logger.info("Inside RecordService :: getRecordByMilestoneType(), To get list of records by milestone-type : "
				+ milestoneType);
		List<Record> recordsByMilestone = recordRepository.findRecordByDueDate(milestoneType);
		logger.info("Returning after getting list of records by milestone-type");
		return recordsByMilestone;
	}

	/**
	 * To get list of records by category
	 * 
	 * @param category
	 * 
	 * @return list of records
	 */
	public List<Record> getRecordByCategory(Category category) {
		logger.info("Inside RecordService :: getRecordByCategory(), To get list of records by category : " + category);
		List<Record> recordsByCategory = recordRepository.findRecordByCategory(category);
		logger.info("Returning after getting list of records by category");
		return recordsByCategory;
	}

	/**
	 * To get project volume by date-range
	 * 
	 * @param discriminator
	 * @param start
	 * @param end
	 * 
	 * @return list of ProjectByStatus
	 */
	public List<ProjectByStatusDTO> getProjectByDateRange(Class<?> discriminator, Calendar start, Calendar end) {
		logger.info("Inside RecordService :: getProjectByDateRange() : discriminator : " + discriminator);
		List<ProjectByStatusDTO> list = new ArrayList<ProjectByStatusDTO>();
		Map<String, List<QuotationDTO>> recordsByStatus = new HashMap<String, List<QuotationDTO>>();

		List<QuotationDTO> recordsToDo = recordRepository.findRecordsByToDoAndDateRange(Quotation.class, start, end)
				.stream().map(record -> {
					Quotation entity = (Quotation) record;
					return new QuotationDTO(entity, Boolean.TRUE);
				}).collect(Collectors.toList());

		List<QuotationDTO> recordsInProgressAndDone = recordRepository
				.findRecordsByInProgressAndDoneAndDateRange(Project.class, start, end).stream().map(record -> {
					Project entity = (Project) record;
					return new ProjectDTO(entity, Boolean.TRUE);
				}).collect(Collectors.toList());

		recordsByStatus = recordsInProgressAndDone.stream().map(dto -> dto).filter(dto -> dto.getStatus() != null)
				.collect(Collectors.groupingBy(QuotationDTO::getStatus));
		recordsByStatus.put(ProjectStatusName.TO_DO.getProjectStatusNameString(), recordsToDo);

		for (Map.Entry<String, List<QuotationDTO>> entry : recordsByStatus.entrySet()) {
			ProjectByStatusDTO status = new ProjectByStatusDTO(entry.getKey(), entry.getValue().size(),
					entry.getValue());
			list.add(status);
		}

		logger.info("Returning after getting records : discriminator" + discriminator + " by status and date-range");
		return list;
	}

	/**
	 * Method to get record by status and date-range
	 * 
	 * @param discriminator
	 * @param start
	 * @param end
	 * @param status
	 * 
	 * @return list of records
	 */
	public List<QuotationDTO> getProjectByDateRangeAndStatus(Class<?> discriminator, Calendar start, Calendar end,
			String status) {
		logger.info("Inside RecordService :: getProjectByDateRange() : discriminator : " + discriminator);

		List<QuotationDTO> recordsByStatus = recordRepository
				.getRecordsByDateRangeAndStatus(discriminator, start, end, status).stream().map(record -> {
					Quotation entity = (Quotation) record;
					return new QuotationDTO(entity, Boolean.TRUE);
				}).collect(Collectors.toList());
		logger.info("Returning after getting records : discriminator" + discriminator + " by status and date-range");
		return recordsByStatus;
	}

	/**
	 * Method to update and persist record
	 * 
	 * @param record
	 * 
	 * @return the record object
	 */
	public Record updateAndPersistRecord(Record record) {
		logger.info("Inside RecordService :: updateAndPersistRecord(), record : " + record);
		return recordRepository.save(record);
	}

	/**
	 * Method to get project revenue
	 * 
	 * @param discriminator
	 * @param start
	 * @param end
	 * @param financialStatus
	 * 
	 * @return project revenue
	 */
	public Float getRevenueByDateRange(Class<?> discriminator, Calendar start, Calendar end, String financialStatus) {
		logger.info("Inside RecordService :: getProjectRevenue() : discriminator : " + discriminator
				+ ", financialStatus : " + financialStatus);
		Float revenue = null;
		if (!StringUtils.isBlank(financialStatus)) {
			revenue = recordRepository.getProjectRevenueByDateRangeAndStatus(discriminator, start, end,
					financialStatus);
		} else {
			revenue = recordRepository.getProjectRevenueByDateRange(discriminator, start, end);
		}
		logger.info("Returning after getting revenue");
		return revenue == null ? 0f : revenue;
	}

	/**
	 * Method to get list of projects by revenue
	 * 
	 * @param discriminator
	 * @param start
	 * @param end
	 * @param financialStatus
	 * 
	 * @return list of records
	 */
	public List<QuotationDTO> getRecordRevenueByDateRange(Class<?> discriminator, Calendar start, Calendar end,
			String financialStatus) {
		logger.info("Inside RecordService :: getProjectRevenue() : discriminator : " + discriminator
				+ ", financialStatus : " + financialStatus);
		if (!StringUtils.isBlank(financialStatus)) {
			return recordRepository.getProjectListByDateRangeAndStatus(discriminator, start, end, financialStatus)
					.stream().map(rec -> new QuotationDTO((Quotation) rec, Boolean.TRUE)).collect(Collectors.toList());
		} else {
			return recordRepository.findProjectVolume(discriminator, start, end).stream()
					.map(rec -> new QuotationDTO((Quotation) rec, Boolean.TRUE)).collect(Collectors.toList());
		}
	}

	/**
	 * Method to get list of records
	 * 
	 * @param discriminator
	 * @param start
	 * @param end
	 * @param financialStatus
	 * 
	 * @return list of records
	 */
	public List<Record> getRecordByDateRangeAndFinancialStatus(Class<?> discriminator, Calendar start, Calendar end,
			String financialStatus) {
		logger.info("Inside RecordService :: getRecordByDateRangeAndFinancialStatus() : discriminator : "
				+ discriminator + ", financialStatus : " + financialStatus);
		return recordRepository.getProjectListByDateRangeAndStatus(discriminator, start, end, financialStatus);
	}

	/**
	 * Method to get project actual vs budget comparison
	 * 
	 * @param discriminator
	 * @param start
	 * @param end
	 * @param financialStatus
	 * 
	 * @return list of projects
	 */
	public List<ProjectByStatusDTO> getProjectActualVsBudgetByDateRangeAndFinancialStatus(Class<?> discriminator,
			Calendar start, Calendar end, String financialStatus) {
		logger.info("Inside RecordService :: getProjectActualVsBudgetByDateRangeAndFinancialStatus() : discriminator : "
				+ discriminator);

		List<ProjectByStatusDTO> list = new ArrayList<ProjectByStatusDTO>();
		Map<String, List<QuotationDTO>> recordsByStatus = this.getProjectByActualComparison(discriminator, start, end,
				financialStatus);
		for (Map.Entry<String, List<QuotationDTO>> entry : recordsByStatus.entrySet()) {
			ProjectByStatusDTO status = new ProjectByStatusDTO(entry.getKey(), entry.getValue().size(),
					entry.getValue(), Boolean.TRUE);
			list.add(status);
		}
		logger.info("Returning after getting project actual vs budget : discriminator" + discriminator
				+ " by financialStatus and date-range");
		return list;
	}

	/**
	 * Method to get projects by comparison
	 * 
	 * @param discriminator
	 * @param start
	 * @param end
	 * @param financialStatus
	 * 
	 * @return map of projects by comparison
	 */
	public Map<String, List<QuotationDTO>> getProjectByActualComparison(Class<?> discriminator, Calendar start,
			Calendar end, String financialStatus) {
		logger.info("Inside RecordService :: getProjectByActualComparison() : discriminator : " + discriminator);
		Map<String, List<QuotationDTO>> recordsByStatus = new HashMap<String, List<QuotationDTO>>();
		List<QuotationDTO> projectComparisonList = recordRepository
				.getProjectListByDateRangeAndStatus(discriminator, start, end, financialStatus).stream()
				.filter(rec -> rec.getSource() != null).map(record -> {
					Project entity = (Project) record;
					return new QuotationDTO(Boolean.TRUE, entity);
				}).collect(Collectors.toList());

		recordsByStatus = projectComparisonList.stream().collect(Collectors.groupingBy(QuotationDTO::getComparison));
		logger.info("Returning after getting projects by comparison : discriminator" + discriminator
				+ " by financialStatus and date-range");
		return recordsByStatus;
	}

	/**
	 * Method to get record by date-range
	 * 
	 * @param discriminator
	 * @param start
	 * @param end
	 * 
	 * @return list of quotations
	 */
	public List<Record> getRecordByDateRange(Class<?> discriminator, Calendar start, Calendar end) {
		logger.info("Inside RecordService :: getRecordByDateRange(start : " + start + ", end : " + end + ")");
		return recordRepository.getRecordsByDateRange(discriminator, start, end);
	}

	/**
	 * Filtering out bookings with staff function.
	 * 
	 * @param bookings
	 * @return bookings
	 */
	private List<BookingDTO> filterStaffFunction(List<BookingDTO> bookings) {
		logger.info("Inside RecordService :: filterStaffFunction()");
		List<BookingDTO> dtos = new ArrayList<>();
		dtos = bookings.stream().filter(
				booking -> !booking.getFunction().getType().equalsIgnoreCase(TeamiumConstants.STAFF_FUNCTION_TYPE))
				.collect(Collectors.toList());
		logger.info("Returning from RecordService :: filterStaffFunction()");
		return dtos;
	}

	/**
	 * To create workbook for ATA carnet spreadsheet
	 * 
	 * @param sheetName
	 * @param sheetLanguage
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public Workbook createSheetAndAddRowHeader(String sheetName, String sheetLanguage)
			throws IOException, ParseException {

		logger.info("Inside RecordService :: createSheetAndAddRowHeader(sheetName : " + sheetName
				+ "), to create sheet and row header" + "Sheet langusge " + sheetLanguage);
		if (StringUtils.isBlank(sheetName)) {
			logger.info("Invalid sheet name");
			throw new UnprocessableEntityException("Invalid sheet name");
		}
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(StringUtils.capitalize(sheetName));
		sheet.setColumnWidth(0, 5000);
		sheet.setColumnWidth(1, 14000);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 5000);
		sheet.setColumnWidth(4, 5000);
		sheet.setColumnWidth(5, 5000);
		sheet.setColumnWidth(6, 7000);

		Font font = workbook.createFont();

		XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.LEFT);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);

		XSSFCellStyle rowDefaultStyle = style;
		XSSFCellStyle styleWrap = rowDefaultStyle;

		for (int rowNumber = 0; rowNumber <= 7; rowNumber++) {
			Row newRow = sheet.createRow(rowNumber);
			for (int col = 0; col < 8; col++) {
				newRow.createCell(col);
			}
		}

		JSONObject jsonObject = this.convertfileToJsonObject(sheetLanguage);
		// merge two rows
		for (int col = 0; col < 7; col++) {
			sheet.addMergedRegion(new CellRangeAddress(0, 2, col, col));
			sheet.addMergedRegion(new CellRangeAddress(3, 6, col, col));
		}

		for (int rowNumber = 0; rowNumber <= 7; rowNumber++) {
			font.setBold(true);
			styleWrap.setFont(font);
			styleWrap.setWrapText(true);
			styleWrap.setAlignment(HorizontalAlignment.CENTER);
			styleWrap.setVerticalAlignment(VerticalAlignment.CENTER);
			if (rowNumber == 0) {
				sheet.getRow(rowNumber).getCell(0)
						.setCellValue(jsonObject.get("A.T.A.").toString() + " " + jsonObject.get("CARNET").toString());
				sheet.getRow(rowNumber).getCell(1).setCellValue(jsonObject.get("GENERAL LIST").toString());
				sheet.getRow(rowNumber).getCell(6)
						.setCellValue(jsonObject.get("CARNET").toString() + " " + jsonObject.get("A.T.A.").toString());

			}
			if (rowNumber == 3) {
				sheet.getRow(rowNumber).getCell(0).setCellValue(jsonObject.get("Item No.").toString());
				sheet.getRow(rowNumber).getCell(1).setCellValue(jsonObject.get("Tread description").toString());
				sheet.getRow(rowNumber).getCell(2).setCellValue(jsonObject.get("Pieces").toString());
				sheet.getRow(rowNumber).getCell(3).setCellValue(jsonObject.get("Weight").toString());
				sheet.getRow(rowNumber).getCell(4).setCellValue(jsonObject.get("Value").toString());
				sheet.getRow(rowNumber).getCell(5).setCellValue(jsonObject.get("Country").toString());
				sheet.getRow(rowNumber).getCell(6).setCellValue(
						jsonObject.get("Customs").toString() + " \n" + jsonObject.get("Identification").toString());
			}

			if (rowNumber == 7) {
				for (int col = 0; col < 7; col++) {
					sheet.getRow(rowNumber).getCell(col).setCellValue(col + 1);
				}
			}
			styleWrap.setVerticalAlignment(VerticalAlignment.CENTER);
			sheet.getRow(rowNumber).setRowStyle(styleWrap);
		}

		logger.info("Return after creating workbook");
		return workbook;

	}

	/**
	 * Method to add data in spread sheet
	 * 
	 * @param workbook
	 * @param spreadsheetData
	 * @param rowNumber
	 * @param sheetLanguage
	 * 
	 * @return spreadsheet byteArray
	 * @throws IOException
	 * @throws ParseException
	 */
	private byte[] addData(Workbook workbook, List<SpreadsheetDataDTO> spreadsheetData, int rowNumber,
			String sheetLanguage, ATACarnetReportDTO dto) throws IOException, ParseException {
		logger.info("Inside RecordService :: addData(), To add data in spreadsheet");
		Sheet sheet = workbook.getSheetAt(0);
		int rowCount = rowNumber;
		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		style.setAlignment(HorizontalAlignment.LEFT);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		XSSFCellStyle rowDefaultStyle = style;
		// styleWrap.setWrapText(true);

		Font font = workbook.createFont();
		JSONObject jsonObject = this.convertfileToJsonObject(sheetLanguage);

		for (SpreadsheetDataDTO record : spreadsheetData) {

			Row newRow = sheet.createRow(rowCount);
			List<String> data = record.getData();
			int size = data.size();

			for (int colNumber = 0; colNumber < size; colNumber++) {
				newRow.createCell(colNumber).setCellValue(data.get(colNumber));
				if (colNumber != 1) {
					this.setCellfontSize(workbook, rowCount, colNumber, false);
				}
			}
			rowCount++;
		}

		int rowNum = rowNumber + spreadsheetData.size();

		for (int row = rowNum; row < rowNum + 4; row++) {
			Row newRow = sheet.createRow(row);
			for (int col = 0; col < 8; col++) {
				newRow.createCell(col);

			}
			if (row == rowNum) {
				font.setBold(true);
				rowDefaultStyle.setWrapText(false);
				rowDefaultStyle.setFont(font);
				sheet.getRow(row).getCell(0).setCellValue(jsonObject.get("Total").toString());
				sheet.getRow(row).getCell(2).setCellValue(dto.getNumberOfPiece());
				sheet.getRow(row).getCell(3).setCellValue(dto.getTotalWeight());
				sheet.getRow(row).getCell(4).setCellValue(dto.getTotalValue());
				sheet.getRow(row).getCell(0).setCellStyle(rowDefaultStyle);
				sheet.getRow(row).getCell(1).setCellStyle(rowDefaultStyle);
				sheet.getRow(row).getCell(5).setCellStyle(rowDefaultStyle);
				sheet.getRow(row).getCell(6).setCellStyle(rowDefaultStyle);
				this.setCellfontSize(workbook, row, 2, true);
				this.setCellfontSize(workbook, row, 3, true);
				this.setCellfontSize(workbook, row, 4, true);
			} else if (row == rowNum + 2) {
				sheet.getRow(row).getCell(0).setCellValue(jsonObject.get("Star1").toString());

			} else if (row == rowNum + 3) {
				sheet.getRow(row).getCell(0).setCellValue(jsonObject.get("Star2").toString());

			}
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			workbook.write(bos);
		} catch (IOException e) {
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
			}
		}

		return bos.toByteArray();

	}

	/**
	 * To set cell style
	 * 
	 * @param workbook
	 * @param rowCount
	 * @param colNumber
	 * 
	 * @return cell
	 */
	private Cell setCellfontSize(Workbook workbook, int rowCount, int colNumber, boolean isFontBold) {
		logger.info("Inside RecordService :: setCellfontSize(), To cell font alignment");
		Cell cell = workbook.getSheetAt(0).getRow(rowCount).getCell(colNumber);
		XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);

		if (isFontBold) {
			Font font = workbook.createFont();
			font.setBold(true);
			cellStyle.setFont(font);
		}

		cell.setCellStyle(cellStyle);
		logger.info("Returning from setCellfontSize()");
		return cell;
	}

	/**
	 * Method to convert JSON file into JSON object
	 * 
	 * @param sheetLanguage
	 * 
	 * @return JSONObject
	 */
	public JSONObject convertfileToJsonObject(String sheetLanguage) {
		logger.info("Inside RecordService :: convertfileToJsonObject(), To convert JSON file into JSON object");
		JSONParser parser = new JSONParser();
		Object obj = new Object();
		File file = null;
		FileReader fileReader = null;

		try {
			if (sheetLanguage.equalsIgnoreCase(Constants.FRANCH_LABEL)) {
				file = new ClassPathResource("/static/json/ATAFrench.json").getFile();
			} else if (sheetLanguage.equalsIgnoreCase(Constants.ENGLISH_LABEL)) {
				file = new ClassPathResource("/static/json/ATAEnglish.json").getFile();
			}

		} catch (Exception e) {
			logger.info("File not found");
			throw new UnprocessableEntityException("File not found");
		}
		try {
			fileReader = new FileReader(file);
		} catch (Exception e) {
			logger.info("File not read  " + file.getPath());
			throw new UnprocessableEntityException("File not read  " + file.getPath());
		}
		try {
			obj = parser.parse(fileReader);
		} catch (Exception e) {
			logger.info("File not parse ");
			throw new UnprocessableEntityException("File not parse ");
		}
		JSONObject jsonObject = (JSONObject) obj;
		logger.info("Returning after converting JSON file into JSON object");
		return jsonObject;
	}

	/**
	 * Method to get ATA carnet report data
	 * 
	 * @param projectId
	 * 
	 * @return ataCarnetReportDTOs
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<ATACarnetReportDTO> getATACarnetReportData(long projectId) throws IOException, ParseException {
		logger.info("Inside RecordService :: getATACarnetReportData, To get all equipment on booking on project id "
				+ projectId);
		Record record = this.validateRecordExistence(Project.class, projectId);
		Project project = (Project) record;
		List<ATACarnetReportDTO> carnetReportDTOs = new ArrayList<>();
		project.getLines().stream().forEach(t -> {
			if (((Booking) t).getResource() instanceof EquipmentResource) {

				long equipmentId = ((Equipment) ((Booking) t).getResource().getEntity()).getId();
				Equipment equipment = equipmentService.findEquipment(equipmentId);
				ATACarnetReportDTO reportDTO = validateEquipments(equipment);
				carnetReportDTOs.add(reportDTO);
			}
		});
		Map<Long, List<ATACarnetReportDTO>> groupByPriceMap = carnetReportDTOs.stream()
				.collect(Collectors.groupingBy(ATACarnetReportDTO::getEquipmentId));

		List<ATACarnetReportDTO> ataCarnetReportDTOs = new ArrayList<>();
		for (Map.Entry<Long, List<ATACarnetReportDTO>> entry : groupByPriceMap.entrySet()) {
			ATACarnetReportDTO reportDTO = new ATACarnetReportDTO();
			reportDTO.setNumberOfPiece(entry.getValue().stream().mapToInt(t -> t.getNumberOfPiece()).sum());
			reportDTO.setTotalValue(entry.getValue().stream().mapToDouble(t -> t.getValue()).sum());
			reportDTO.setDescription(entry.getValue().stream().findFirst().get().getDescription());
			reportDTO.setTotalWeight((float) entry.getValue().stream().mapToDouble(t -> t.getWeight()).sum());
			reportDTO.setCountryOfOrigin(entry.getValue().stream().findFirst().get().getCountryOfOrigin());
			reportDTO.setEquipmentId(entry.getKey());
			ataCarnetReportDTOs.add(reportDTO);
		}
		logger.info("Returning afetr getting data ATA carnet report data");
		return ataCarnetReportDTOs;

	}

	/**
	 * To validate equipment
	 * 
	 * @param equipment
	 * 
	 * @return reportDTO
	 */
	public ATACarnetReportDTO validateEquipments(Equipment equipment) {
		logger.info("Inside RecordService ::validateEquipments() ,To validate Equipment");
		ATACarnetReportDTO reportDTO = new ATACarnetReportDTO();
		String description = StringUtils.isBlank(equipment.getReference()) ? "" : "/" + equipment.getReference();
		String model = equipment.getModel();
		if (StringUtils.isBlank(equipment.getModel()) && StringUtils.isBlank(equipment.getReference())) {
			reportDTO.setDescription("");
		} else {
			if (!StringUtils.isBlank(equipment.getModel())) {
				description = StringUtils.isBlank(equipment.getReference()) ? "" : "/" + equipment.getReference();
				description = model + description;
			}
			reportDTO.setDescription(description);
		}

		if (equipment.getSpecsOrign() == null) {
			reportDTO.setCountryOfOrigin("");
		} else {
			reportDTO.setCountryOfOrigin(equipment.getSpecsOrign());
		}

		if (equipment.getAta() == null) {
			reportDTO.setValue(0.0);
		} else {
			reportDTO.setValue(equipment.getAta());
		}

		if (equipment.getSpecsWeight() == null) {
			reportDTO.setWeight(0.0f);
		} else {
			try {
				reportDTO.setWeight(Float.valueOf(equipment.getSpecsWeight()));
			} catch (Exception e) {
				reportDTO.setWeight(0.0f);
			}
		}
		reportDTO.setEquipmentId(equipment.getId());
		reportDTO.setNumberOfPiece(1);
		logger.info("Returning after valiadte Equipment ");
		return reportDTO;
	}

	/**
	 * Method to get ATA carnet report export
	 * 
	 * @param projectId
	 * @param sheetLanguage
	 * 
	 * @return statusDTO
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	public ProjectByStatusDTO getATACarnetReportExport(long projectId, String sheetLanguage)
			throws IOException, ParseException {
		logger.info("Inside RecordService :: getATACarnetReportData, To export ATA carnet report spread sheet ");
		List<ATACarnetReportDTO> carnetReportDTOs = getATACarnetReportData(projectId);
		Workbook workbook = this.createSheetAndAddRowHeader(Constants.ATA_STRING, sheetLanguage);

		AtomicInteger count = new AtomicInteger(1);
		List<SpreadsheetDataDTO> spreadsheetData = carnetReportDTOs.stream().map(record -> {
			List<String> list = Arrays.asList(String.valueOf(count.get()), record.getDescription(),
					String.valueOf(record.getNumberOfPiece()), record.getTotalWeight().toString(),
					record.getTotalValue().toString(), record.getCountryOfOrigin(), " ");
			count.getAndIncrement();
			return new SpreadsheetDataDTO(list);
		}).collect(Collectors.toList());

		int totalNumberOfPiece = carnetReportDTOs.stream().mapToInt(t -> t.getNumberOfPiece()).sum();
		Float totalWeight = (float) carnetReportDTOs.stream().mapToDouble(t -> t.getTotalWeight()).sum();
		Double totalValue = carnetReportDTOs.stream().mapToDouble(t -> t.getTotalValue()).sum();

		ATACarnetReportDTO dto = new ATACarnetReportDTO();
		dto.setNumberOfPiece(totalNumberOfPiece);
		dto.setTotalValue(totalValue);
		dto.setTotalWeight(totalWeight);
		logger.info("Importing ATA carnet report spreadsheet");
		byte[] spreadsheetByteArray = this.addData(workbook, spreadsheetData, 8, sheetLanguage, dto);

		ProjectByStatusDTO statusDTO = new ProjectByStatusDTO(carnetReportDTOs.size(), carnetReportDTOs,
				spreadsheetByteArray);
		statusDTO.setTimestamp(CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()));
		logger.info("Returning after exorting ATA carnet spreadsheet");
		return statusDTO;
	}

	/**
	 * To get records by company
	 * 
	 * @param companyId
	 * 
	 * @return list of records by company
	 */
	public List<Record> getRecordByCompany(Long companyId) {
		logger.info("Inside RecordService :: getRecordByCompany( companyId : " + companyId
				+ "), To get records by company");
		return recordRepository.getRecordsByCompany(companyId);
	}

	/**
	 * Method to get recently updated bookings within specified time period in
	 * minutes. Discriminator should be 'project'(mandatory) and minutes is not
	 * mandatory. By default bookings updated within 60 minutes are given.
	 * 
	 * @param discriminator
	 * @param minutes
	 * 
	 * @return list of recordDTO
	 */
	public List<RecordDTO> getRecentlyUpdatedRecords(String discriminator, Integer minutes) {
		logger.info("Inside RecordService :: getRecentlyUpdatedRecords(discriminator : " + discriminator
				+ ", minutes : " + minutes + ")");
		List<RecordDTO> recordDTOs = new ArrayList<RecordDTO>();
		if (StringUtils.isBlank(discriminator)) {
			logger.error("Please provide valid discriminator.");
			throw new UnprocessableEntityException("Please provide valid discriminator.");
		}
		if (minutes == null || minutes.intValue() == 0) {
			minutes = 60;
		}

		switch (discriminator.toLowerCase()) {
		case Constants.PROJECT_STRING:
			recordRepository
					.getRecordsByDateTimeRange(Project.class,
							DateTime.now().minusMinutes(minutes).toGregorianCalendar())
					.stream().forEach(record -> recordDTOs.add(new ProjectDTO((Project) record, true)));
			break;

		default:
			logger.error("Please provide valid discriminator.");
			throw new UnprocessableEntityException("Please provide valid discriminator.");
		}
		logger.info("Returning from RecordService :: getRecentlyUpdatedRecords()");
		return recordDTOs;
	}

	/**
	 * Find all records by the given currency code.
	 * 
	 * @param currencyCode
	 * @return List of recordDTO.
	 */
	public List<RecordDTO> getAllRecordByCurrencyCode(String currencyCode) {
		logger.info("Inside RecordService :: getAllRecordByCurrency(" + currencyCode + ")");
		if (StringUtils.isBlank(currencyCode)) {
			logger.error("Currency code should not be blank.");
			throw new UnprocessableEntityException("Currency code should not be blank.");
		}
		List<RecordDTO> recordDTOs = new ArrayList<>();
		recordRepository.findByCurrencyIgnoreCase(currencyCode).stream()
				.forEach(record -> recordDTOs.add(new RecordDTO(record)));
		logger.info("Returning from RecordService :: getAllRecordByCurrency()");
		return recordDTOs;
	}

	/**
	 * To save record
	 * 
	 * @param record
	 * 
	 * @return record
	 */
	public Record SaveRecord(Record record) {
		logger.info("Inside RecordService :: SaveRecord(" + record + ")");
		return recordRepository.save(record);
	}
	
	/** To get Project Financial detail
	 * 
	 * @param quotationId
	 * 
	 * @return financialDTO
	 */
	public FinancialDataDTO getProjectFinancialDetail(long quotationId) {
		logger.info(
				"Inside RecordService :: getProjectFinancialDetail(), To get Project Financial detail " + quotationId);

		ProjectFinancialDTO financialDTO;
		Project savedProject = recordRepository.getProjectByQuotationId(Project.class, quotationId);

		if (savedProject == null) {
			// get budget basic detail
			financialDTO = setValueInBudgetMap(quotationId);
		} else {
			financialDTO = setValueInActualMap(savedProject);
		}
		logger.info("Returning from RecordService :: getProjectFinancialDetail()");
		return new FinancialDataDTO(financialDTO.getTitle(), financialDTO.getProjectStatus(),
				financialDTO.getFinancialStatus(), financialDTO.getManagedBy(), financialDTO.getOrigin(),
				financialDTO.getComapny(), financialDTO.getCurrency(), financialDTO.getLanguage(),
				financialDTO.getInternalReference(), financialDTO.getFinancialGraphData(),
				financialDTO.getByPhaseData(), financialDTO.getByFunctionData(), financialDTO.getTotal());
	}

	/**
	 * To set value in budget Map for Project Financial
	 * 
	 * @param quotationId
	 * 
	 * @return financialDTO
	 */
	private ProjectFinancialDTO setValueInBudgetMap(long quotationId) {
		logger.info("Inside RecordService :: setValueInBudgetMap(), To set value in budget Map, quotationId : "
				+ quotationId);
		List<Float> margin = new ArrayList<>();
		Quotation record = (Quotation) validateRecordExistence(Quotation.class, quotationId);
		Map<String, Float> budgetMap = new HashMap<>();
		Map<String, Float> byPhaseBudgetMap = new HashMap<>();
		Map<String, Float> byFunctionBudgetMap = new HashMap<>();

		for (Line r : record.getLines()) {
			if (!(((Booking) r).getResource() instanceof DefaultResource)) {
				Float totalPrice = r.getTotalLocalPrice();
				budgetMap.put(r.getFunction().getClass().getSimpleName(),
						budgetMap.containsKey(r.getFunction().getClass().getSimpleName())
								? budgetMap.get(r.getFunction().getClass().getSimpleName()) + totalPrice
								: totalPrice);
				margin.add(totalPrice - r.getTotalCost());

				byFunctionBudgetMap.put(r.getFunction().getQualifiedName(),
						byFunctionBudgetMap.containsKey(r.getFunction().getQualifiedName())
								? byFunctionBudgetMap.get(r.getFunction().getQualifiedName()) + totalPrice
								: totalPrice);

				if (r.getFunction().getParent() != null) {

					byPhaseBudgetMap.put(r.getFunction().getParent().getQualifiedName(),
							byPhaseBudgetMap.containsKey(r.getFunction().getParent().getQualifiedName())
									? byPhaseBudgetMap.get(r.getFunction().getParent().getQualifiedName()) + totalPrice
									: totalPrice);
				}
			}
		}

		Float totalBudget = (float) budgetMap.values().stream().mapToDouble(Float::floatValue).sum();
		Float totalMargin = (float) margin.stream().mapToDouble(Float::floatValue).sum();

		ProjectFinancialDTO financialDTO = new ProjectFinancialDTO(budgetMap, totalBudget, totalMargin);
		financialDTO.setByPhaseBudgetMap(byPhaseBudgetMap);
		financialDTO.setByFunctionBudgetMap(byFunctionBudgetMap);
		setProjectDetail(financialDTO, record);
		getProjectFinancialGraphData(financialDTO, true);
		logger.info("Returning from RecordService :: setValueInBudgetMap()");
		return financialDTO;

	}

	/**
	 * To set value in actual Map for Project Financial
	 * 
	 * @param savedProject
	 * 
	 * @return financialDTO
	 */
	public ProjectFinancialDTO setValueInActualMap(Project savedProject) {
		logger.info("Inside RecordService :: setValueInActualMap(), To set value in actual Map " + savedProject);
		ProjectFinancialDTO financialDTO = new ProjectFinancialDTO();
		List<Float> margin = new ArrayList<>();
		List<Float> outSourceMargin = new ArrayList<>();
		Map<String, Float> budgetMap = new HashMap<>();
		Map<String, Float> actualMap = new HashMap<>();
		Map<String, Float> outSourceMap = new HashMap<>();
		Map<String, Float> byPhaseBudgetMap = new HashMap<>();
		Map<String, Float> byPhaseActualMap = new HashMap<>();
		Map<String, Float> byFunctionBudgetMap = new HashMap<>();
		Map<String, Float> byFunctionActualMap = new HashMap<>();
		Map<String, Float> byFunctionOutSourceMap = new HashMap<>();
		Map<String, Float> byPhaseoutSourceMap = new HashMap<>();

		logger.info("Set value for budget map");
		for (Line r : savedProject.getSource().getLines()) {
			if (!(((Booking) r).getResource() instanceof DefaultResource)) {
				Float totalPrice = r.getTotalLocalPrice();
				budgetMap.put(r.getFunction().getClass().getSimpleName(),
						budgetMap.containsKey(r.getFunction().getClass().getSimpleName())
								? budgetMap.get(r.getFunction().getClass().getSimpleName()) + totalPrice
								: totalPrice);
				margin.add(totalPrice - r.getTotalCost());

				byFunctionBudgetMap.put(r.getFunction().getQualifiedName(),
						byFunctionBudgetMap.containsKey(r.getFunction().getQualifiedName())
								? byFunctionBudgetMap.get(r.getFunction().getQualifiedName()) + totalPrice
								: totalPrice);

				if (r.getFunction().getParent() != null) {
					byPhaseBudgetMap.put(r.getFunction().getParent().getQualifiedName(),
							byPhaseBudgetMap.containsKey(r.getFunction().getParent().getQualifiedName())
									? byPhaseBudgetMap.get(r.getFunction().getParent().getQualifiedName()) + totalPrice
									: totalPrice);
				}
			}
		}

		Float totalBudget = (float) budgetMap.values().stream().mapToDouble(Float::floatValue).sum();
		Float totalBudgetMargin = (float) margin.stream().mapToDouble(Float::floatValue).sum();
		margin.clear();
		logger.info("Set value for actual map");
		for (Line r : savedProject.getLines()) {

			if (!(((Booking) r).getResource() instanceof DefaultResource)) {
				Float totalPrice = r.getTotalLocalPrice();
				actualMap.put(r.getFunction().getClass().getSimpleName(),
						actualMap.containsKey(r.getFunction().getClass().getSimpleName())
								? actualMap.get(r.getFunction().getClass().getSimpleName()) + totalPrice
								: totalPrice);
				margin.add(totalPrice - r.getTotalCost());
				if (r.getFunction().getParent() != null) {
					byPhaseActualMap.put(r.getFunction().getParent().getQualifiedName(),
							byPhaseActualMap.containsKey(r.getFunction().getParent().getQualifiedName())
									? byPhaseActualMap.get(r.getFunction().getParent().getQualifiedName()) + totalPrice
									: totalPrice);
				}

				byFunctionActualMap.put(r.getFunction().getQualifiedName(),
						byFunctionActualMap.containsKey(r.getFunction().getQualifiedName())
								? byFunctionActualMap.get(r.getFunction().getQualifiedName()) + totalPrice
								: totalPrice);

				if (((Booking) r).getResource() instanceof StaffResource
						&& isResourceFreelance((StaffMember) ((Booking) r).getResource().getEntity())) {

					outSourceMap.put((r.getFunction().getClass().getSimpleName()),
							outSourceMap.containsKey((r.getFunction().getClass().getSimpleName()))
									? outSourceMap.get(r.getFunction().getClass().getSimpleName()) + totalPrice
									: totalPrice);
					outSourceMargin.add(totalPrice - r.getTotalCost());

					byFunctionOutSourceMap.put(r.getFunction().getQualifiedName(),
							byFunctionOutSourceMap.containsKey(r.getFunction().getQualifiedName())
									? byFunctionOutSourceMap.get(r.getFunction().getQualifiedName()) + totalPrice
									: totalPrice);

					if (r.getFunction().getParent() != null) {
						byPhaseoutSourceMap.put(r.getFunction().getParent().getQualifiedName(),
								byPhaseoutSourceMap.containsKey(r.getFunction().getParent().getQualifiedName())
										? byPhaseoutSourceMap.get(r.getFunction().getParent().getQualifiedName())
												+ totalPrice
										: totalPrice);
					}
				}

			}
		}

		logger.info("Set value for outsource map");
		List<Record> orderRecords = recordRepository.findRecordByProjectId(Order.class, savedProject.getId());
		if (orderRecords.size() == 1) {
			Record record = orderRecords.stream().findFirst().get();
			if (record != null) {
				for (Line r : record.getLines()) {
					Float totalPrice = r.getTotalLocalPrice();
					outSourceMap.put((r.getFunction().getClass().getSimpleName()),
							outSourceMap.containsKey((r.getFunction().getClass().getSimpleName()))
									? outSourceMap.get(r.getFunction().getClass().getSimpleName()) + totalPrice
									: totalPrice);
					outSourceMargin.add(totalPrice - r.getTotalCost());

					byFunctionOutSourceMap.put(r.getFunction().getParent().getQualifiedName(),
							byFunctionOutSourceMap.containsKey(r.getFunction().getParent().getQualifiedName())
									? byFunctionOutSourceMap.get(r.getFunction().getParent().getQualifiedName())
											+ totalPrice
									: totalPrice);

					if (r.getFunction().getParent() != null) {
						byPhaseoutSourceMap.put(r.getFunction().getParent().getQualifiedName(),
								byPhaseoutSourceMap.containsKey(r.getFunction().getParent().getQualifiedName())
										? byPhaseoutSourceMap.get(r.getFunction().getParent().getQualifiedName())
												+ totalPrice
										: totalPrice);
					}

				}
			}
		}

		Float totalActual = (float) actualMap.values().stream().mapToDouble(Float::floatValue).sum();
		Float totalActualMargin = (float) margin.stream().mapToDouble(Float::floatValue).sum();

		Float totalOutSourceMargin = (float) outSourceMargin.stream().mapToDouble(Float::floatValue).sum();
		Float totalOutSource = (float) outSourceMap.values().stream().mapToDouble(Float::floatValue).sum();

		financialDTO = new ProjectFinancialDTO(savedProject, totalBudget, totalActual, totalOutSource, budgetMap,
				actualMap, outSourceMap, byPhaseActualMap, byPhaseBudgetMap, byFunctionBudgetMap, byFunctionActualMap);

		financialDTO.setByFunctionOutSourceMap(byFunctionOutSourceMap);
		financialDTO.setByPhaseoutSourceMap(byPhaseoutSourceMap);
		financialDTO.setTotalBudgetMargin((float) (Math.round(totalBudgetMargin * 100.0) / 100.0));
		financialDTO.setTotalActualMargin((float) (Math.round(totalActualMargin * 100.0) / 100.0));
		financialDTO.setTotalOutsourceMargin((float) (Math.round(totalOutSourceMargin * 100.0) / 100.0));
		getProjectFinancialGraphData(financialDTO, false);
		logger.info("Returning from RecordService :: setValueInActualMap()");
		return financialDTO;

	}

	/**
	 * To get project financial Graph data
	 * 
	 * @param financialDTO
	 */
	public void getProjectFinancialGraphData(ProjectFinancialDTO financialDTO, boolean isOnlyForBudget) {
		logger.info("Inside RecordService :: getProjectFinancialGraphData , To get project financial Graph data"
				+ financialDTO);

		Set<String> graphKey = new HashSet<>();
		Set<String> functionKey = new HashSet<>();
		Set<String> phaseKey = new HashSet<>();
		Map<String, FinancialGraphDataDTO> financialGraphData = new HashMap<>();
		Map<String, FinancialGraphDataDTO> byPhase = new HashMap<>();
		Map<String, FinancialGraphDataDTO> byFunction = new HashMap<>();
		Map<String, FinancialGraphDataDTO> total = new HashMap<>();

		if (isOnlyForBudget) {
			for (String value : financialDTO.getBudgetMap().keySet()) {
				graphKey.add(value);
			}

			graphKey.stream().forEach(entity -> {
				FinancialGraphDataDTO graphDataDTO = new FinancialGraphDataDTO();
				graphDataDTO.setBudget(financialDTO.getBudgetMap().get(entity));
				financialGraphData.put((entity.substring(0, entity.indexOf("Function"))), graphDataDTO);
			});
			financialDTO.setFinancialGraphData(financialGraphData);

			for (String value : financialDTO.getByFunctionBudgetMap().keySet()) {
				functionKey.add(value);
			}

			functionKey.stream().forEach(entity -> {
				FinancialGraphDataDTO byFunctionDataDTO = new FinancialGraphDataDTO();
				byFunctionDataDTO.setBudget(financialDTO.getByFunctionBudgetMap().get(entity));
				byFunction.put(entity, byFunctionDataDTO);
			});
			financialDTO.setByFunctionData(byFunction);

			for (String value : financialDTO.getByPhaseBudgetMap().keySet()) {
				phaseKey.add(value);
			}
			phaseKey.stream().forEach(entity -> {
				FinancialGraphDataDTO byPhaseDataDTO = new FinancialGraphDataDTO();
				byPhaseDataDTO.setBudget(financialDTO.getByPhaseBudgetMap().get(entity));
				byPhase.put(entity, byPhaseDataDTO);
			});
			financialDTO.setByPhaseData(byPhase);

			total.put("Total", new FinancialGraphDataDTO(financialDTO.getTotalBudget()));
			total.put("Margin", new FinancialGraphDataDTO(financialDTO.getTotalBudgetMargin()));
			financialDTO.setTotal(total);

		} else {
			for (String value : financialDTO.getActualMap().keySet()) {
				graphKey.add(value);
			}

			for (String value : financialDTO.getBudgetMap().keySet()) {
				graphKey.add(value);
			}

			for (String value : financialDTO.getOutSourceMap().keySet()) {
				graphKey.add(value);
			}

			graphKey.stream().forEach(entity -> {
				FinancialGraphDataDTO graphDataDTO = new FinancialGraphDataDTO(financialDTO.getActualMap().get(entity),
						financialDTO.getBudgetMap().get(entity), financialDTO.getOutSourceMap().get(entity));
				financialGraphData.put((entity.substring(0, entity.indexOf("Function"))), graphDataDTO);

			});
			financialDTO.setFinancialGraphData(financialGraphData);

			for (String value : financialDTO.getByFunctionActualMap().keySet()) {
				functionKey.add(value);
			}
			for (String value : financialDTO.getByFunctionBudgetMap().keySet()) {
				functionKey.add(value);
			}
			for (String value : financialDTO.getByFunctionOutSourceMap().keySet()) {
				functionKey.add(value);
			}

			functionKey.stream().forEach(entity -> {
				FinancialGraphDataDTO byFunctionDataDTO = new FinancialGraphDataDTO(
						financialDTO.getByFunctionActualMap().get(entity),
						financialDTO.getByFunctionBudgetMap().get(entity),
						financialDTO.getByFunctionOutSourceMap().get(entity));
				byFunction.put(entity, byFunctionDataDTO);
			});
			financialDTO.setByFunctionData(byFunction);

			for (String value : financialDTO.getByPhaseActualMap().keySet()) {
				phaseKey.add(value);
			}
			for (String value : financialDTO.getByPhaseBudgetMap().keySet()) {
				phaseKey.add(value);
			}
			for (String value : financialDTO.getByPhaseoutSourceMap().keySet()) {
				functionKey.add(value);
			}
			phaseKey.stream().forEach(entity -> {
				FinancialGraphDataDTO byPhaseDataDTO = new FinancialGraphDataDTO(
						financialDTO.getByPhaseActualMap().get(entity), financialDTO.getByPhaseBudgetMap().get(entity),
						financialDTO.getByPhaseoutSourceMap().get(entity));
				byPhase.put(entity, byPhaseDataDTO);
			});
			financialDTO.setByPhaseData(byPhase);

			total.put("Total", new FinancialGraphDataDTO(financialDTO.getTotalActual(), financialDTO.getTotalBudget(),
					financialDTO.getTotalOutsource()));
			total.put("Margin", new FinancialGraphDataDTO(financialDTO.getTotalActualMargin(),
					financialDTO.getTotalBudgetMargin(), financialDTO.getTotalOutsource()));
			financialDTO.setTotal(total);
		}
		logger.info(
				"Returning from RecordService ::getProjectFinancialGraphData , To successfully get project financial Graph data");
	}

	/**
	 * To get if staff resource is Freelance or not.
	 * 
	 * @param staffMember
	 * @return
	 */
	private boolean isResourceFreelance(StaffMember staffMember) {
		logger.info("Inside RecordService :: isResourceFreelance");
		ContractSetting contractSetting = staffMember.getContractSetting();
		boolean isFreelance = false;
		if (contractSetting != null) {
			if (staffMember.getContractSetting() instanceof EntertainmentContractSetting) {
				if (staffMember.getContractSetting().getType().getKey()
						.equalsIgnoreCase(ContractType.FREELANCE.toString())) {
					isFreelance = true;
				}
			}
		}
		logger.info("Returning from RecordService :: isResourceFreelance" + isFreelance);
		return isFreelance;
	}

	/**
	 * To set project basic detail
	 * 
	 * @param financialDTO
	 * @param record
	 */
	private void setProjectDetail(ProjectFinancialDTO financialDTO, Quotation record) {
		logger.info("Inside RecordService :: setProjectDetail(), To set vproject basic detail ");

		financialDTO.setProjectStatus(record.getStatus() != null ? record.getStatus().getKey() : "");
		financialDTO.setFinancialStatus(record.getFinancialStatus() != null ? record.getFinancialStatus() : "");
		financialDTO.setManagedBy(record.getFollower() != null
				? record.getFollower().getFirstName() + " " + record.getFollower().getName()
				: "");
		financialDTO.setOrigin(record.getOrigin() != null ? record.getOrigin() : "");
		financialDTO.setComapny(record.getEntity() != null ? record.getEntity().getName() : "");
		financialDTO.setCurrency(record.getCurrency() != null ? record.getCurrency().getCurrencyCode() : "");
		financialDTO.setLanguage(record.getLanguage() != null ? record.getLanguage().getKey() : "");
		financialDTO.setInternalReference(record.getReferenceInternal() != null ? record.getReferenceInternal() : "");
		financialDTO.setTitle(record.getTitle());
		logger.info("Returning from RecordService :: setProjectDetail(), To set vproject basic detail");
	}

	/**
	 * To find a record by discriminator and recordId.
	 * 
	 * @param discriminator
	 * @param recordId
	 * @return Record
	 */
	public Record getRecordById(Class<?> discriminator, long recordId) {
		return recordRepository.getRecordById(discriminator, recordId);
	}

}
