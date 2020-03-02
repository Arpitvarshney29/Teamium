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
import java.util.Base64;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.teamium.config.PropConfig;
import com.teamium.constants.Constants;
import com.teamium.domain.AbstractXmlEntity;
import com.teamium.domain.Address;
import com.teamium.domain.LabourRule;
import com.teamium.domain.PersonalDocument;
import com.teamium.domain.Role;
import com.teamium.domain.Role.RoleName;
import com.teamium.domain.Skill;
import com.teamium.domain.TeamiumException;
import com.teamium.domain.UserBooking;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.projects.Project;
import com.teamium.domain.prod.resources.ResourceFunction;
import com.teamium.domain.prod.resources.functions.Function;
import com.teamium.domain.prod.resources.functions.units.RateUnit;
import com.teamium.domain.prod.resources.staff.ResourceRate;
import com.teamium.domain.prod.resources.staff.StaffEmail;
import com.teamium.domain.prod.resources.staff.StaffFunction;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.domain.prod.resources.staff.StaffMemberIdentity;
import com.teamium.domain.prod.resources.staff.StaffMemberSkill;
import com.teamium.domain.prod.resources.staff.StaffResource;
import com.teamium.domain.prod.resources.staff.StaffTelephone;
import com.teamium.domain.prod.resources.staff.UserSetting;
import com.teamium.domain.prod.resources.staff.contract.EntertainmentContractSetting;
import com.teamium.domain.prod.upload.log.ImportFor;
import com.teamium.domain.prod.upload.log.SpreadsheetUploadLog;
import com.teamium.dto.BookingDTO;
import com.teamium.dto.ContractSettingDTO;
import com.teamium.dto.CurrencyDTO;
import com.teamium.dto.DigitalSignatureByteDTO;
import com.teamium.dto.FunctionDTO;
import com.teamium.dto.LabourRuleDTO;
import com.teamium.dto.PersonalDocumentDTO;
import com.teamium.dto.ReportDTO;
import com.teamium.dto.ResourceRateDTO;
import com.teamium.dto.SpreadsheetMessageDTO;
import com.teamium.dto.StaffDropDownDTO;
import com.teamium.dto.StaffEmailDTO;
import com.teamium.dto.StaffMemberDTO;
import com.teamium.dto.StaffMemberSkillDTO;
import com.teamium.dto.StaffTelephoneDTO;
import com.teamium.dto.TimeReportDTO;
import com.teamium.dto.UserSettingDTO;
import com.teamium.dto.WeekDurationDTO;
import com.teamium.enums.ContractType;
import com.teamium.enums.Gender;
import com.teamium.enums.RateUnitType;
import com.teamium.enums.SocialConvention;
import com.teamium.enums.project.Language;
import com.teamium.enums.project.TelephoneType;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.BookingRepository;
import com.teamium.repository.LabourRuleRepository;
import com.teamium.repository.ResourceRateRepository;
import com.teamium.repository.RoleRepository;
import com.teamium.repository.SpreadsheetUploadLogRepository;
import com.teamium.repository.StaffMemberRepository;
import com.teamium.repository.UserBookinRepository;
import com.teamium.service.prod.resources.functions.FunctionService;
import com.teamium.utils.CommonUtil;
import com.teamium.utils.CountryUtil;
import com.teamium.utils.FileReader;
import com.teamium.utils.FileWriter;

/**
 * 
 * A Service class implementation for Staff(Personnel)
 * 
 * @author Teamium
 *
 */
@Service
public class StaffMemberService {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private StaffMemberRepository staffMemberRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	private PersonalDocumentService personalDocumentService;
	private FunctionService functionService;
	private DocumentTypeService documentTypeService;
	private UnavailabilityService unavailabilityService;
	private UnavailabilityEventService unavailabilityEventService;
	private AuthenticationService authenticationService;
	private PropConfig propConfig;
	private StaffResourceService staffResourceService;
	private UserService userService;
	private ResourceRateRepository resourceRateRepository;
	private SpreadsheetUploadLogRepository spreadsheetUploadLogRepository;
	private LabourRuleRepository labourRuleRepository;
	private GroupService groupService;
	private CurrencyService currencyService;
	private SkillService skillService;
	private StaffMemberSkillService staffMemberSkillService;
	private TimeZoneService timeZoneService;
	private UserBookingService userBookingService;
	private UserBookinRepository userBookingRepository;
	private BookingRepository bookingRepository;

	@Autowired
	@Lazy
	private BookingService bookingService;

	@Autowired
	@Lazy
	private LabourRuleService labourRuleService;

	@Autowired
	public StaffMemberService(StaffMemberRepository staffMemberRepository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder, PersonalDocumentService personalDocumentService,
			FunctionService functionService, DocumentTypeService documentTypeService,
			UnavailabilityService unavailabilityService, UnavailabilityEventService unavailabilityEventService,
			AuthenticationService authenticationService, PropConfig propConfig,
			StaffResourceService staffResourceService, UserService userService,
			ResourceRateRepository resourceRateRepository, LabourRuleRepository labourRuleRepository,
			SpreadsheetUploadLogRepository spreadsheetUploadLogRepository, @Lazy GroupService groupService,
			CurrencyService currencyService, SkillService skillService, StaffMemberSkillService staffMemberSkillService,
			TimeZoneService timeZoneService, @Lazy UserBookingService userBookingService,
			UserBookinRepository userBookingRepository, BookingRepository bookingRepository) {
		this.staffMemberRepository = staffMemberRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.personalDocumentService = personalDocumentService;
		this.functionService = functionService;
		this.documentTypeService = documentTypeService;
		this.unavailabilityService = unavailabilityService;
		this.unavailabilityEventService = unavailabilityEventService;
		this.authenticationService = authenticationService;
		this.propConfig = propConfig;
		this.staffResourceService = staffResourceService;
		this.userService = userService;
		this.resourceRateRepository = resourceRateRepository;
		this.spreadsheetUploadLogRepository = spreadsheetUploadLogRepository;
		this.labourRuleRepository = labourRuleRepository;
		this.groupService = groupService;
		this.currencyService = currencyService;
		this.skillService = skillService;
		this.staffMemberSkillService = staffMemberSkillService;
		this.timeZoneService = timeZoneService;
		this.userBookingService = userBookingService;
		this.userBookingRepository = userBookingRepository;
		this.bookingRepository = bookingRepository;
	}

	/**
	 * To save or update staff member.
	 * 
	 * @param staffMemberDTO;
	 * @return
	 * @throws Exception
	 */
	public StaffMemberDTO saveOrUpdate(StaffMemberDTO staffMemberDTO) throws Exception {
		logger.info("Inside StaffMemberService:saveOrUpdate(),To save and update staff member:"
				+ staffMemberDTO.toString());
		if (!authenticationService.isHumanResource()) {
			logger.warn("A invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to save or update staff.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}

		// Set default currency to functions having rates with no currency.
		staffMemberDTO.getResource().getFunctions().stream()
				.forEach(resourceFunctionDTO -> resourceFunctionDTO.getRates().stream().forEach(rate -> {
					if (StringUtils.isBlank(rate.getCurrency()))
						rate.setCurrency(currencyService.getDefaultCurrency().getCode());
				}));

		StaffMember staffMember = null;
		StaffResource resource = new StaffResource();

		staffMemberDTO.setRole(validateRoles(staffMemberDTO.getRole()));

		ContractSettingDTO contractSettingDTO = staffMemberDTO.getContractSettingDTO();
		if (contractSettingDTO != null) {
			validateContractSetting(contractSettingDTO);
		}

		// method to validate the mandatory fields
		validateStaff(staffMemberDTO);

		if (staffMemberDTO.getId() != null) {
			staffMember = validateStaffMember(staffMemberDTO.getId());
			staffMemberDTO.getStaffMember(staffMember);
		} else {
			staffMember = staffMemberDTO.getStaffMember();
			if (staffMemberDTO.getUserSettingDTO() != null) {
				if (staffMemberDTO.getUserSettingDTO().getPassword() != null) {
					staffMember.getUserSetting().setPassword(
							passwordEncoder.encode(staffMemberDTO.getUserSettingDTO().getPassword()).trim());
				}

			}
		}
		if (staffMemberDTO.getLabourRuleId() != null) {
			LabourRule labourRule = this.labourRuleService.getLabourRuleById(staffMemberDTO.getLabourRuleId());
			staffMember.setLabourRule(labourRule);
		}
		if (staffMember.getResource() == null) {
			resource.setStaffMember(staffMember);
		}

		// New implementation for Resource-Function
		Set<ResourceFunction> resourceFunctions = staffMember.getResource().getFunctions();
		StaffResource staffResource = staffMember.getResource();
		AtomicBoolean isRemove = new AtomicBoolean(false);

		if (staffMemberDTO.getResource() != null && staffMemberDTO.getResource().getFunctions() != null
				&& !staffMemberDTO.getResource().getFunctions().isEmpty()) {

			// removing function not present in dto
			if (resourceFunctions != null && !resourceFunctions.isEmpty()) {
				resourceFunctions.removeIf(resFun -> {
					isRemove.set(true);
					staffMemberDTO.getResource().getFunctions().forEach(dto -> {
						if (dto.getId() != null && dto.getId().longValue() == resFun.getId().longValue()) {
							isRemove.set(false);
							return;
						}
					});
					return isRemove.get();
				});
			}
			// assigning new function or updating existing resource-function
			staffMemberDTO.getResource().getFunctions().forEach(data -> {
				List<ResourceRate> rates = data.getRates() == null ? new ArrayList<ResourceRate>()
						: data.getRates().stream().map(r -> r.getResourceRateDetails(new ResourceRate()))
								.collect(Collectors.toList());

				if (!staffMemberDTO.isFreelance() && rates.size() > 0) {
					logger.info(" Only Freelancer can have rate card");
					throw new UnprocessableEntityException("Only Freelancer can have rate card");
				}
				logger.info("adding rate card for staff ::" + data.getRates());
				if (data.getRates() != null && !data.getRates().isEmpty()) {
					data.getRates().stream().collect(Collectors.groupingBy(ResourceRateDTO::getBasis))
							.forEach((b, l) -> {
								if (l.size() > 1) {
									logger.info("duplicate rate for " + b + ".");
									throw new UnprocessableEntityException("duplicate rate for " + b + ".");
								}
							});
				}
				if (data.getId() == null) {
					logger.info(" Updateing function : " + data.getId());
					ResourceFunction resourceFunction = staffResource.assignFunction(functionService
							.getFunctionByIdAndDiscriminator(StaffFunction.class, data.getFunction().getId()));
					resourceFunction.setRating(data.getRating());
					resourceFunction.setRates(rates);
					resourceFunction.setCreatedOn(Calendar.getInstance());
					resourceFunction.setPrimaryFunction(data.isPrimaryFunction());
					resourceFunction.setRateSelected(data.isRateSelected());
				} else {
					logger.info(" Adding functions :: " + resourceFunctions);
					resourceFunctions.forEach(resFun -> {
						if (resFun.getId() != null && data.getId().longValue() == resFun.getId().longValue()) {
							resFun.setRating(data.getRating());
							resFun.setRates(rates);
							resFun.setPrimaryFunction(data.isPrimaryFunction());
							resFun.setRateSelected(data.isRateSelected());
						}
					});
				}
			});

		} else {
			if (staffMember.getId() != null && staffMember.getId() != 0) {
				logger.info("Atleast one function must be present and should be primary.");
				throw new UnprocessableEntityException("Atleast one function must be present and should be primary.");
			}
		}

		// validating primary resource-function
		Set<ResourceFunction> resFunctions = staffMember.getResource().getFunctions();
		if (resFunctions != null && !resFunctions.isEmpty()) {
			if (resFunctions.size() == 1) {
				resFunctions.stream().forEach(resFun -> resFun.setPrimaryFunction(true));
			}
			List<ResourceFunction> countPrimaryFunctions = resFunctions.stream()
					.filter(resFun -> resFun.isPrimaryFunction() == true).collect(Collectors.toList());
			if (countPrimaryFunctions != null && !countPrimaryFunctions.isEmpty()) {
				if (countPrimaryFunctions.size() > 1) {
					logger.info("Only one function can be primary.");
					throw new UnprocessableEntityException("Only one function can be primary.");
				}
			} else {
				logger.info("Atleast one function must be primary");
				throw new UnprocessableEntityException("Atleast one function must be primary");
			}
		} else {
			logger.info("Atleast one function must be assigned on staff.");
			throw new UnprocessableEntityException("Atleast one function must be assigned on staff.");
		}

		logger.info("adding skills.");
		// validating skills
		if (staffMember.getId() == null) {
			Set<StaffMemberSkillDTO> staffSkills = staffMemberDTO.getSkills();
			if (staffSkills != null && !staffSkills.isEmpty()) {
				staffSkills.stream().forEach(skill -> {
					if (skill.getId() != null && skill.getId() != 0) {
						throw new UnprocessableEntityException("Skill already exist.");
					}
				});
			}
		}

		// assign staff-skills on personnel
		Set<StaffMemberSkill> dbSkills = staffMember.getSkills();
		Set<StaffMemberSkillDTO> skills = staffMemberDTO.getSkills();
		if (skills != null && !skills.isEmpty()) {
			staffMember.setSkills(skills.stream().map(dto -> {

				// removing skills not present in dto
				if (dbSkills != null && !dbSkills.isEmpty()) {
					List<Long> idsToDelete = new ArrayList<>();
					dbSkills.removeIf(dt -> {
						isRemove.set(true);
						skills.forEach(dt1 -> {
							if (dt1.getId() != null && dt1.getId().longValue() == dt.getId().longValue()) {
								isRemove.set(false);
								return;
							}
						});
						if (isRemove.get())
							idsToDelete.add(dt.getId());
						return isRemove.get();
					});
					// Deleting touples from staffMemberSkill.
					idsToDelete.stream().forEach(x -> staffMemberSkillService.removeStaffMemberSkillById(x));
				}

				if (StringUtils.isBlank(dto.getDomain())) {
					logger.info("Please select valid expertise-type.");
					throw new UnprocessableEntityException("Please select valid expertise-type.");
				}
				Skill domain = skillService.findBySkillName(dto.getDomain());
				StaffMemberSkill entity = dto.getStaffMemberSkill(new StaffMemberSkill());
				if (dto.getId() != null) {
					Optional<StaffMemberSkill> list = dbSkills.stream().filter(skill -> skill.getId() == dto.getId())
							.findFirst();
					if (list.isPresent()) {
						entity = list.get();
					}
				}

				if (entity.getId() != null && !domain.getName().equalsIgnoreCase(entity.getSkill().getName())) {
					logger.info("Not allowed to update this current expertise-type.");
					throw new UnprocessableEntityException("Not allowed to update this current expertise-type.");
				} else if (entity.getId() == null) {
					entity.setSkill(domain);
				}

				dbSkills.stream().forEach(skill -> {
					if (dto.getId() == null && skill.getSkill().getName().equalsIgnoreCase(domain.getName())) {
						logger.info("Expertise-type with this name already exists.");
						throw new UnprocessableEntityException("Expertise-type with this name already exists.");
					}
				});

				entity.setRating(dto.getRating());

				return entity;
			}).collect(Collectors.toSet()));
		}

		UserSettingDTO userSettingDTO = staffMemberDTO.getUserSettingDTO();

		if (userSettingDTO == null) {
			logger.info("Please provide user setting");
			throw new UnprocessableEntityException("Please provide user setting");
		} else {
			if (staffMemberDTO.getId() != null) {
				if (staffMember != null && staffMember.getUserSetting() != null
						&& !StringUtils.isBlank(staffMember.getUserSetting().getLogin())) {
					if (!staffMember.getUserSetting().getLogin().equals(userSettingDTO.getLogin())) {
						userService.validateAssignedLogin(staffMemberDTO);
					}
				}
			} else {
				userService.validateAssignedLogin(staffMemberDTO);
				userService.validatePassword(staffMemberDTO);
			}
		}

		if (staffMemberDTO.getUserSettingDTO() != null) {
			if (!StringUtils.isBlank(staffMemberDTO.getUserSettingDTO().getPassword())) {
				staffMember.getUserSetting()
						.setPassword(passwordEncoder.encode(staffMemberDTO.getUserSettingDTO().getPassword().trim()));
			}
		}

		Long staffId = staffMember.getId();
		// validate staff-emails
		this.validateStaffEmail(staffMember, staffMemberDTO, isRemove, staffId);

		if (staffMemberDTO.getWorkPercentage() < 0 || staffMemberDTO.getWorkPercentage() > 100) {
			logger.info("Work percentage must be between 0 to 100");
			throw new UnprocessableEntityException("Work percentage must be 0 to 100");
		}
		if (StringUtils.isNoneBlank(staffMemberDTO.getHiringDate())) {
			try {
				staffMember.setHiringDate(DateTime.parse(staffMemberDTO.getHiringDate()).toDate());
			} catch (IllegalFieldValueException e) {
				logger.info("Invalid Date format for hiring date : " + e.getMessage());
				throw new UnprocessableEntityException("Invalid Date format for hiring date");
			}
		}

		// validate staff-telephones
		this.validateStaffTelephone(staffMember, staffMemberDTO, isRemove, staffId);

		Set<StaffResource> staffResources = this.staffResourceService
				.validateGroupNameAndGetGroupList(staffMemberDTO.getGroupName());

		staffMember.getResource().setStaffGroups(staffResources);

		// validate perosnnel-document
		staffMember.setDocuments(this.validateDocuments(staffMemberDTO.getDocuments(), staffMember));
		staffMember.setAvailable(staffMemberDTO.isAvailable());
		StaffMember staff = this.staffMemberRepository.save(staffMember);
		staffMember = staff;

		StaffMemberDTO staffDto = new StaffMemberDTO(staff);

		this.enableOrDisableStaffMember(staff.getId(), staffMemberDTO.isAvailable());
		staffDto.setAvailable(staffMemberDTO.isAvailable());
		this.addStaffDocumentColor(staffDto.getDocuments());

		logger.info("Successfully returning StaffMemberDTO after saveOrUpdate");
		return staffDto;

	}

	/**
	 * To get list of all staff members.
	 * 
	 * @return list of StaffMemberDTO
	 */
	public List<StaffMemberDTO> findStaffMembers() {
		logger.info("Inside StaffMemberService:findStaffMembers ,To find StaffMembers");
		List<StaffMemberDTO> staffMembers = this.staffMemberRepository.findAll().stream().map(sf -> {
			StaffMemberDTO staffMemberDTO = new StaffMemberDTO(sf, Boolean.TRUE);
			// staffMemberDTO.setAvailable(this.unavailabilityEventService.isAvailable(sf.getResource()));
			staffMemberDTO.setAvailable(sf.isAvailable());
			addStaffDocumentColor(staffMemberDTO.getDocuments());
			return staffMemberDTO;
		}).collect(Collectors.toList());
		logger.info("Returning successfully from findStaffMembers,found staffMembers count:" + staffMembers.size());
		return staffMembers;
	}

	/**
	 * Find time report by staffmember id
	 * 
	 * @param staffMemberid
	 * @param startDate
	 * @param endDate
	 * @param type
	 * 
	 * @return TimeReportDTO
	 */
	public TimeReportDTO findTimeReportById(long staffMemberid, Date startDate, Date endDate, String type) {
		logger.info("Inside StaffMemberService :: findTimeReportById ," + staffMemberid);
		StaffMember staffMember = validateStaffMember(staffMemberid);
		String fullName = "";
		if (!StringUtils.isBlank(staffMember.getFirstName())) {
			fullName = staffMember.getFirstName() + " ";
		}
		if (!StringUtils.isBlank(staffMember.getName())) {
			fullName = fullName + staffMember.getName();
		}
		TimeReportDTO timeReportDTO = new TimeReportDTO();
		timeReportDTO.setEmployeeCode(staffMember.getEmployeeCode());
		timeReportDTO.setStaffName(fullName.trim());
		timeReportDTO.setActivityRate(staffMember.getWorkPercentage());
		switch (type) {

		case Constants.TIME_REPORT_BY_WEEK:
			if (staffMember.getLabourRule() != null) {
				if (staffMember.getLabourRule().getAverageWeeklyDuration() != null) {
					if (staffMember.getWorkPercentage() != 0)
						timeReportDTO.setNominalHour((staffMember.getLabourRule().getAverageWeeklyDuration()
								* staffMember.getWorkPercentage()) / 100);

				}
			}

			List<ReportDTO> timeReportDto = new ArrayList<>();

			List<WeekDurationDTO> totalWeekDuration = userBookingService.findWeeklyWorkingDurationForUser(staffMemberid,
					startDate, endDate, type);
			for (WeekDurationDTO week : totalWeekDuration) {
				week.getWeekNumber();
				week.getTotalDuration();
				float decimal = (float) week.getTotalDuration() / (60);
				float balance = (float) decimal - timeReportDTO.getNominalHour();
				timeReportDto.add(new ReportDTO(week.getWeekNumber(), decimal, balance));
			}
			timeReportDTO.setReport(timeReportDto);
			this.logger.info("Successfully return findTimeReportById :: " + timeReportDTO);
			return timeReportDTO;

		case Constants.ANNUAL_PERSONNEL_TIME_REPORT:
			if (staffMember.getLabourRule() != null) {
				if (staffMember.getLabourRule().getAverageWeeklyDuration() != null) {
					if (staffMember.getWorkPercentage() != 0)
						timeReportDTO.setNominalHour((staffMember.getLabourRule().getWorkingDurationPerMonth()
								* staffMember.getWorkPercentage()) / 100);
				}
			}
			List<ReportDTO> annualReportDto = new ArrayList<>();

			List<WeekDurationDTO> totalMonthDuration = userBookingService
					.findWeeklyWorkingDurationForUser(staffMemberid, startDate, endDate, type);
			// ReportDTO reportDtos=new ReportDTO();
			if (totalMonthDuration != null) {
				for (WeekDurationDTO month : totalMonthDuration) {
					float decimal = (float) month.getTotalDuration() / (60);
					float balance = (float) decimal - timeReportDTO.getNominalHour();
					annualReportDto.add(new ReportDTO(month.getMonthName(), decimal, balance, month.isSick()));

				}
				timeReportDTO.setReport(annualReportDto);
				this.logger.info(
						"Successfully return annualPersonalTimeReport by findTimeReportById  :: " + timeReportDTO);
				return timeReportDTO;
			}

		case Constants.WEEKLY_PERSONNEL_TIME_REPORT:
			if (staffMember.getLabourRule() != null) {
				if (staffMember.getLabourRule().getAverageWeeklyDuration() != null) {
					if (staffMember.getWorkPercentage() != 0
							&& staffMember.getLabourRule().getAverageWeeklyDuration() != null)
						timeReportDTO.setNominalHour((staffMember.getLabourRule().getAverageWeeklyDuration()
								* staffMember.getWorkPercentage()) / 100);
				}
			}

			DateTime finalStartDate = new DateTime(startDate).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
					.withMillisOfSecond(0);
			DateTime finalEndDate = new DateTime(endDate).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(0)
					.withMillisOfSecond(0);
			List<UserBooking> userBookings = userBookingRepository.findUserBookingByDate(staffMemberid,
					finalStartDate.toDate(), finalEndDate.toDate());

			List<ReportDTO> reportDTOs = new ArrayList<>();
			userBookings.stream().forEach(dt -> {
				ReportDTO reportDto = new ReportDTO();
				DateTime start = new DateTime(dt.getUserStartTime());
				DateTime end = new DateTime(dt.getUserEndTime());
				Booking booking = null;
				Project project = null;
				if (dt.getBookingId() != null) {
					booking = bookingService.findBooking(dt.getBookingId());
					if (booking != null && booking.getRecord() != null && booking.getRecord() instanceof Project) {
						project = (Project) booking.getRecord();
					}
				}

				if (start.withTime(0, 0, 0, 0).compareTo(end.withTime(0, 0, 0, 0)) == 0) {
					float decimal = (float) Minutes.minutesBetween(start, end).getMinutes() / 60;
					reportDto.setSickHour(dt.getSick());
					if (dt.getSick() == true) {
						reportDto.setSickHourDuration(decimal);
					} else {
						reportDto.setWorkedHour(decimal);
					}

					reportDto.setDateString(start.toString());
					if (booking != null) {
						reportDto.setFunctionName(booking.getFunction().getQualifiedName());
						if (project != null) {
							reportDto.setProjectName(project.getTitle());
						}
					} else
						reportDto.setDescription(dt.getName());
					reportDTOs.add(reportDto);
				} else {
					int value = finalStartDate.compareTo(start.withTime(0, 0, 0, 0));

					if (value == 1) {
						start = finalStartDate.withTime(0, 0, 0, 0);
					}
					while (true) {

						if (start.isAfter(finalEndDate)) {
							break;
						}

						ReportDTO reportDTO = new ReportDTO();
						if (booking != null) {
							if (booking.getFunction() != null) {
								reportDTO.setFunctionName(booking.getFunction().getQualifiedName());
							}
							if (project != null) {
								reportDTO.setProjectName(project.getTitle());
							}
						} else
							reportDTO.setDescription(dt.getName());
						// reportDto1.setDateString(start.toString());
						reportDTO.setSickHour(dt.getSick());

						if (start.withTime(0, 0, 0, 0).compareTo(end.withTime(0, 0, 0, 0)) == 0) {
							float decimal = (float) Minutes.minutesBetween(start, end).getMinutes() / 60;
							reportDTO.setWorkedHour(decimal);
							reportDTO.setDateString(start.toString());
							reportDTOs.add(reportDTO);
							break;
						} else if (start.withTime(0, 0, 0, 0).compareTo(finalEndDate.withTime(0, 0, 0, 0)) == 0) {
							float decimal = (float) Minutes.minutesBetween(start, finalEndDate).getMinutes() / 60;
							reportDTO.setWorkedHour(decimal);
							reportDTO.setDateString(start.toString());
							reportDTOs.add(reportDTO);
							break;
						}
						float decimal = (float) Minutes.minutesBetween(start, start.withTime(23, 59, 0, 0)).getMinutes()
								/ 60;
						reportDTO.setWorkedHour(decimal);
						reportDTO.setDateString(start.toString());
						reportDTOs.add(reportDTO);
						start = start.plusDays(1).withTime(0, 0, 0, 0);
					}
				}

			});
			reportDTOs.sort(Comparator.comparing(ReportDTO::getDateString));
			timeReportDTO.setReport(reportDTOs);
			this.logger.info("Successfully return annualPersonalTimeReport by findTimeReportById  :: " + timeReportDTO);
			return timeReportDTO;
		default:
			return null;
		}

	}

	/**
	 * To get staff member by id.
	 * 
	 * @param staffMemberId
	 * @return StaffMemberDTO
	 */
	public StaffMemberDTO findStaffById(long id) {
		logger.info("Inside StaffMemberService:findStaffById ,To find StaffMember by id:" + id);
		StaffMember staffMember = validateStaffMember(id);
		StaffMemberDTO staffMemberDTO = new StaffMemberDTO(staffMember);
		addStaffDocumentColor(staffMemberDTO.getDocuments());
		// staffMemberDTO.setAvailable(this.unavailabilityEventService.isAvailable(staffMember.getResource()));

		// get recent bookings of this staff
		List<BookingDTO> recentBooking = bookingService.getRecentBookings(staffMember.getResource().getId());
		staffMemberDTO.setRecentBooking(recentBooking);
		logger.info("Retunrning successfully after getting staffMember" + staffMember.toString());
		return staffMemberDTO;
	}

	// public void findTimeReportByWeek(long id)
	// {
	// logger.info("Inside StaffMemberService:findStaffById ,To find StaffMember by
	// id:" + id);
	// staff
	// }
	/**
	 * To delete a staff member.
	 * 
	 * @param staffMemberId
	 */
	public void deleteStaffMember(long staffMemberId) {
		logger.info("Inside StaffMemberService:deleteStaffMember ,To delete StaffMember by id:" + staffMemberId);
		// User user = authenticationService.getAuthenticatedUser();
		// authenticationService.validateUserSystemAdmin(user);
		StaffMember staffMember = validateStaffMember(staffMemberId);
		this.staffMemberRepository.delete(staffMember);
		logger.info("Returning successfully after deleting the staff member.");
	}

	/**
	 * To validate StaffMember existance.
	 * 
	 * @param staffMemberId the staffMemberId.
	 * 
	 * @return the StaffMember object.
	 */
	public StaffMember validateStaffMember(long staffMemberId) {
		logger.info("To validate StaffMember.");
		StaffMember staffMember = this.staffMemberRepository.findOne(staffMemberId);
		if (staffMember == null) {
			logger.error("StaffMember not found");
			throw new NotFoundException("StaffMember not found.");
		}
		logger.info("Returning StaffMember after validating StaffMember.");
		return staffMember;
	}

	/**
	 * To validate roles.
	 * 
	 * @param role
	 * @return list of roles.
	 */
	private List<Role> validateRoles(List<Role> role) {
		List<Role> roles = null;
		if (role != null && !role.isEmpty()) {
			roles = new ArrayList<Role>();
			for (Role roleList : role) {
				Role dbRole = this.roleRepository.findByRoleNameAndActive(roleList.getRoleName(), Boolean.TRUE);
				if (dbRole == null) {
					throw new NotFoundException("Role not found with name { " + roleList.getRoleName() + " }.");
				}
				roles.add(dbRole);
			}
		}
		return roles;
	}

	/**
	 * To get staff drop down data.
	 * 
	 * @return the StaffDropDownDTO
	 */
	public StaffDropDownDTO getStaffDropDownData() {
		logger.info("Inside  getStaffDropDownData() :: finding staff dropdown data");
		List<FunctionDTO> functions = functionService.getRatedStaffFunctions();
		List<String> skills = skillService.getSkillNames();
		List<String> languages = Language.getLanguages();
		List<String> roles = RoleName.getRoles();
		List<String> telephoneTypes = TelephoneType.getTelephoneTypes();
		List<String> documentTypes = documentTypeService.getAllDocumentTypeName();
		List<String> locations = staffMemberRepository.getLocations();
		List<String> rateUnitsList = RateUnitType.getRateUnitTypes();
		List<String> countries = CountryUtil.getInstance().getCountryNames();
		List<CurrencyDTO> currencyList = currencyService.getAllCurrencyByActive(Boolean.TRUE);
		List<String> contractTypeList = ContractType.getContractTypes();
		List<String> socialConventionTypeList = SocialConvention.getConventionTypes();
		List<String> groups = this.staffResourceService.getAllGroups().stream().map(dto -> {
			return dto.getName();
		}).filter(name -> name != null).collect(Collectors.toList());

		StaffDropDownDTO staffDropDown = new StaffDropDownDTO(languages, skills, documentTypes, functions, roles,
				telephoneTypes);
		staffDropDown.setLabourRuleDTOs(this.labourRuleService.getLabourRuleRepository().findAll().stream().map(la -> {
			LabourRuleDTO labourRuleDTO = new LabourRuleDTO();
			labourRuleDTO.setLabourRuleId(la.getLabourRuleId());
			labourRuleDTO.setLabourRuleName(la.getLabourRuleName());
			return labourRuleDTO;
		}).collect(Collectors.toList()));

		staffDropDown.setLocations(locations);
		staffDropDown.setCurrencyList(currencyList);
		staffDropDown.setRateUnitsList(rateUnitsList);
		staffDropDown.setCountries(countries);

		staffDropDown.setContractTypes(contractTypeList);
		staffDropDown.setSocialConventionTypes(socialConventionTypeList);
		staffDropDown.setGroups(groups);

		Math.floor(this.resourceRateRepository.getMaxValue());
		staffDropDown.setMaxRate((int) Math.ceil(this.resourceRateRepository.getMaxValue()));
		staffDropDown.setMinRate((int) Math.floor(this.resourceRateRepository.getMinValue()));

		logger.info("Returning from  getStaffDropDownData()");
		return staffDropDown;
	}

	/**
	 * Enable Or Disable Equipment.
	 * 
	 * @param equipmentId
	 * @param isAvailable
	 */
	public void enableOrDisableStaffMember(long staffMemberId, boolean isAvailable) throws Exception {
		this.logger.info("Inside StaffMember : enableOrDisableStaffMember");
		this.logger.info("staffMemberId : " + staffMemberId);
		this.logger.info("isAvailable : " + isAvailable);

		if (!authenticationService.isHumanResource()) {
			logger.warn("A invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get equipment drop-down list.");

			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}

		StaffMember staffMember = validateStaffMember(staffMemberId);
		if (staffMember.getResource().getId() == null) {
			throw new UnprocessableEntityException(
					"Resource is not available for StaffMember : " + staffMember.getId());
		}
		this.unavailabilityService.setAvailable(staffMember.getResource(), isAvailable);

		this.logger.info("Returning from EquipmentService : availableOrUnavailableEquipment");
	}

	/**
	 * Method to get logged-in-user
	 * 
	 * @return StaffMemberDTO object
	 */
	public StaffMemberDTO getLoggedInUser() {
		logger.info("Inside StaffMemberService::getLoggedInUser(), To get logged-in authenticated user details.");
		StaffMember loggedInUser = authenticationService.getAuthenticatedUser();
		if (loggedInUser == null) {
			logger.info("Staff is not authenticated.");
			throw new UnprocessableEntityException("Staff is not authenticated.");
		}
		StaffMemberDTO staffMemberDTO = new StaffMemberDTO(loggedInUser);
		logger.info("Returning after getting logged-in authenticated user details.");
		return staffMemberDTO;
	}

	/**
	 * add expiration color in document
	 */
	private void addStaffDocumentColor(Set<PersonalDocumentDTO> documents) {
		logger.info(" Inside addMilestoneMilestoneColor(): ");
		if (documents == null) {
			return;
		}
		for (PersonalDocumentDTO document : documents) {
			int remainingDays = document.getRemainingDays();
			if (remainingDays < 0) {
				document.setExpirationColor("RED");
			} else if (remainingDays > propConfig.getStaffDocumentExpiration()) {
				document.setExpirationColor("#119f32");
			} else {
				document.setExpirationColor("ORANGE");
			}
		}
		logger.info(" Returning from addMilestoneMilestoneColor(): ");
	}

	private void validateContractSetting(ContractSettingDTO contractSettingDTO) {
		logger.info(" Inside validateContractSetting(): ");
		if (contractSettingDTO != null) {
			if (ContractType.getEnum(contractSettingDTO.getContractType()) == null) {
				logger.info("Please provide valid contract setting type.");
				throw new UnprocessableEntityException("Please provide valid contract setting type.");
			}
			// if (contractSettingDTO.getConvention() != null
			// && SocialConvention.getEnum(contractSettingDTO.getConvention()) == null) {
			// logger.info("Please provide valid labour agreement.");
			// throw new UnprocessableEntityException("Please provide valid labour
			// agreement.");
			// }
		}
		logger.info(" Returning from validateContractSetting(): ");
	}

	/**
	 * Method to validate StaffMemberDTO
	 * 
	 * @param staffMemberDTO the staffMemberDTO
	 */
	private void validateStaff(StaffMemberDTO staffMemberDTO) {
		logger.info("Inside StaffMemberService::validateStaff, To validate StaffMember details.");

		// first name
		if (StringUtils.isBlank(staffMemberDTO.getFirstName()) || !staffMemberDTO.getFirstName()
				.matches("^(?=.{1,30}$)[a-zA-ZÀ-ŸØ-öø-ÿ]+(\\s[a-zA-ZÀ-ŸØ-öø-ÿ]{1,})*$")) {
			logger.info("Please enter valid first name.");
			throw new UnprocessableEntityException("Please enter valid first name.");
		}
		// last name
		if (StringUtils.isBlank(staffMemberDTO.getLastName()) || !staffMemberDTO.getLastName()
				.matches("^(?=.{1,30}$)[a-zA-ZÀ-ŸØ-öø-ÿ]+(\\s[a-zA-ZÀ-ŸØ-öø-ÿ]{1,})*$")) {
			logger.info("Please enter valid last name.");
			throw new UnprocessableEntityException("Please enter valid last name.");
		}
		// employee code
		if (StringUtils.isBlank(staffMemberDTO.getEmployeeCode())
				|| staffMemberDTO.getEmployeeCode().matches("^([0]){1,30}$")) {
			logger.info("Please enter valid employee code.");
			throw new UnprocessableEntityException("Please enter valid employee code.");
		}

		if (!staffMemberDTO.getEmployeeCode()
				.matches("^([a-zA-Z0-9#*()-_+])?([a-zA-Z0-9]+)*([a-zA-Z0-9#*()-_+]){1,30}$")
				|| staffMemberDTO.getEmployeeCode().length() > 30) {
			logger.info("Please enter valid employee code.");
			throw new UnprocessableEntityException("Please enter valid employee code.");
		}

		// validating if employeeCode already exists
		StaffMember dbUser = staffMemberRepository.findByEmployeeCode(staffMemberDTO.getEmployeeCode());
		StaffMember previousEmployeeCodeUser = null;
		if (staffMemberDTO.getId() != null) {
			previousEmployeeCodeUser = staffMemberRepository.findOne(staffMemberDTO.getId());
		}
		if (dbUser != null && staffMemberDTO.getId() != null
				&& dbUser.getId().longValue() != staffMemberDTO.getId().longValue()) {
			logger.info("Employee code already exist.");
			throw new UnprocessableEntityException("Employee code already exist.");
		}
//		if (previousEmployeeCodeUser != null && !StringUtils.isBlank(previousEmployeeCodeUser.getEmployeeCode())) {
//			if (previousEmployeeCodeUser != null
//					&& !previousEmployeeCodeUser.getEmployeeCode().equals(staffMemberDTO.getEmployeeCode())) {
//				logger.info("Not allowed to update existing employee code.");
//				throw new UnprocessableEntityException("Not allowed to update existing employee code.");
//			}
//		}

		// //job title
		// if (!staffMemberDTO.getJobTitle().matches("^[a-zA-Z]{1,30}$")) {
		// logger.info("Please enter valid job title.");
		// throw new UnprocessableEntityException("Please enter valid job title.");
		// }

		// adderss
		// if (staffMemberDTO.getAddress() == null) {
		// logger.info("Please enter valid address.");
		// throw new UnprocessableEntityException("Please enter valid address.");
		// }
		// if (StringUtils.isBlank(staffMemberDTO.getAddress().getLine1()) ||
		// staffMemberDTO.getAddress().getLine1().matches("^[a-zA-Z0-9!@#$%^&*()-_+]{1,50}$"))
		// {
		// logger.info("Please enter valid employee code.");
		// throw new UnprocessableEntityException("Please enter valid employee code.");
		// }
		// if (StringUtils.isBlank(staffMemberDTO.getAddress().getCity()) ||
		// staffMemberDTO.getAddress().getCity().matches("^([a-zA-Z0-9]{1,})([a-zA-Z0-9]){1,50}$"))
		// {
		// logger.info("Please enter valid employee code.");
		// throw new UnprocessableEntityException("Please enter valid employee code.");
		// }
		// if (StringUtils.isBlank(staffMemberDTO.getAddress().getCountry()) ||
		// staffMemberDTO.getAddress().getCountry().matches("^([a-zA-Z0-9]{1,})([a-zA-Z0-9]){1,50}$"))
		// {
		// logger.info("Please enter valid employee code.");
		// throw new UnprocessableEntityException("Please enter valid employee code.");
		// }

		logger.info("Returning after validating staff-member details.");
	}

	/**
	 * To validate documents
	 * 
	 * @param documents
	 * 
	 * @return PersonalDocument list
	 */
	private Set<PersonalDocument> validateDocuments(Set<PersonalDocumentDTO> documents, StaffMember staffMember) {
		logger.info("Inside StaffMemberService :: validateDocuments(), To validate documets.");
		Set<PersonalDocumentDTO> dtoDocuments = documents;
		documents.stream().collect(Collectors.groupingBy(PersonalDocumentDTO::getType)).forEach((t, val) -> {

			if (!StringUtils.isBlank(t)) {
				com.teamium.domain.DocumentType documentTypeEntity = documentTypeService.findDocumentTypeByType(t);
				if (documentTypeEntity == null) {
					logger.info("Please provide valid document-type : " + t);
					throw new UnprocessableEntityException("Please provide valid document-type");
				}
			} else {
				logger.info("Please provide valid document-type");
				throw new UnprocessableEntityException("Please provide valid document-type");
			}
			if (val.size() > 1) {
				logger.error("Duplicate document" + t);
				throw new UnprocessableEntityException("Duplicate document : " + t);
			}
		});
		Set<PersonalDocument> docs = documents.stream().map(doc -> {
			PersonalDocument document = doc.getPersonalDocument();
			document.setType(personalDocumentService.getDocumentType(doc.getType()));
			return document;
		}).collect(Collectors.toSet());

		Set<PersonalDocument> dbDocuments = staffMember.getDocuments();
		if (dbDocuments != null && !dbDocuments.isEmpty()) {
			List<Long> dbList = dbDocuments.stream().map(ent -> ent.getId()).collect(Collectors.toList());
			if (dtoDocuments == null || dtoDocuments.isEmpty()) {
				dbList.stream().forEach(ent -> {
					personalDocumentService.deleteDocument(ent);
				});
			} else {
				List<Long> dtoList = documents.stream().filter(ent -> ent.getId() != null).map(ent -> ent.getId())
						.collect(Collectors.toList());
				dbList.stream().forEach(ent -> {
					if (!dtoList.contains(ent)) {
						personalDocumentService.deleteDocument(ent);
					}
				});
			}
		}

		docs.size();
		logger.info("Returning after validating staff documents .");
		return docs;
	}

	/**
	 * To upload spreadsheet data for staff
	 * 
	 * @param file
	 * 
	 * @return SpreadsheetMessage wrapper object
	 * 
	 * @throws InvalidFormatException
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

		String logDetails = "";
		Date currentDate = new Date();
		String dateString = new SimpleDateFormat("yyyyMMddHHmm").format(currentDate);
		Calendar calander = Calendar.getInstance();
		calander.setTime(currentDate);

		SpreadsheetUploadLog spreadsheetUploadLog = new SpreadsheetUploadLog();

		boolean hasInvalidHeader = false;
		boolean hasError = false;

		String fileExtention = null;

		String relativePath = null;
		String absolutePath = null;
		URL url = null;
		String logFileName = null;

		if (file != null) {
			logDetails = "Upload started.";
			String fileName = file.getOriginalFilename();
			logger.info("Importing " + fileName);

			// List<AbstractXmlEntity> genderXmlEntitys =
			// xmlEntityBean.getXmlEntityItems("Gender");
			// List<AbstractXmlEntity> courtesyXmlEntitys =
			// xmlEntityBean.getXmlEntityItems("Courtesy");

			// fileExtention = fileName.substring(fileName.lastIndexOf('.') +
			// 1).toUpperCase();

			fileExtention = FilenameUtils.getExtension(file.getOriginalFilename());
			Map<String, Integer> indexValue = new HashMap<String, Integer>();
			Map<String, String> numbers = new HashMap<>();
			AtomicInteger index = new AtomicInteger(0);
			int rowNumber = 0;
			int corruptedRowNumber = 0;

			String extension = fileExtention.toUpperCase();
			switch (extension) {

			case "XLSX":
			case "XLS":
				logger.info("Inside here in case XLS/XSLX for staff-member");
				Workbook workbook = WorkbookFactory.create(file.getInputStream());
				Workbook errorWorkbook = new XSSFWorkbook();
				Sheet sheet = errorWorkbook.createSheet(
						StringUtils.capitalize(Constants.STAFF_STRING) + " " + Constants.INVALID_SPREADSHEET_NAME);
				Sheet spreadsheet = workbook.getSheetAt(0);

				for (Cell cell : spreadsheet.getRow(0)) {

					switch (getValue(cell).trim()) {

					case Constants.STAFF_EMP_CODE:
						indexValue.put(Constants.STAFF_EMP_CODE, index.getAndIncrement());
						continue;
					case Constants.STAFF_LAST_NAME:
						indexValue.put(Constants.STAFF_LAST_NAME, index.getAndIncrement());
						continue;
					case Constants.STAFF_FISRT_NAME:
						indexValue.put(Constants.STAFF_FISRT_NAME, index.getAndIncrement());
						continue;
					case Constants.STAFF_JOB_TITLE:
						indexValue.put(Constants.STAFF_JOB_TITLE, index.getAndIncrement());
						continue;

					case Constants.STAFF_NAME_PREFIX:
						indexValue.put(Constants.STAFF_NAME_PREFIX, index.getAndIncrement());
						continue;

					case Constants.STAFF_PRIMARY_FUNCTION:
						indexValue.put(Constants.STAFF_PRIMARY_FUNCTION, index.getAndIncrement());
						continue;
					case Constants.STAFF_SECONDARY_FUNCTION:
						indexValue.put(Constants.STAFF_SECONDARY_FUNCTION, index.getAndIncrement());
						continue;

					case Constants.STAFF_EXPERTISE:
						indexValue.put(Constants.STAFF_EXPERTISE, index.getAndIncrement());
						continue;
					case Constants.STAFF_LANGUAGE:
						indexValue.put(Constants.STAFF_LANGUAGE, index.getAndIncrement());
						continue;
					case Constants.STAFF_GROUP:
						indexValue.put(Constants.STAFF_GROUP, index.getAndIncrement());
						continue;

					case Constants.STAFF_ADDRESS:
						indexValue.put(Constants.STAFF_ADDRESS, index.getAndIncrement());
						continue;
					case Constants.STAFF_ZIP_OR_POSTAL_CODE:
						indexValue.put(Constants.STAFF_ZIP_OR_POSTAL_CODE, index.getAndIncrement());
						continue;
					case Constants.STAFF_CITY:
						indexValue.put(Constants.STAFF_CITY, index.getAndIncrement());
						continue;
					case Constants.STAFF_STATE:
						indexValue.put(Constants.STAFF_STATE, index.getAndIncrement());
						continue;
					case Constants.STAFF_COUNTRY:
						indexValue.put(Constants.STAFF_COUNTRY, index.getAndIncrement());
						continue;

					case Constants.STAFF_PRIMARY_EMAIL:
						indexValue.put(Constants.STAFF_PRIMARY_EMAIL, index.getAndIncrement());
						continue;
					case Constants.STAFF_SECONDARY_EMAIL:
						indexValue.put(Constants.STAFF_SECONDARY_EMAIL, index.getAndIncrement());
						continue;

					case Constants.STAFF_PRIMARY_PHONE:
						indexValue.put(Constants.STAFF_PRIMARY_PHONE, index.getAndIncrement());
						continue;
					case Constants.STAFF_SECONDARY_PHONE:
						indexValue.put(Constants.STAFF_SECONDARY_PHONE, index.getAndIncrement());
						continue;

					case Constants.STAFF_SOCIAL_SECURITY_NUMBER:
						indexValue.put(Constants.STAFF_SOCIAL_SECURITY_NUMBER, index.getAndIncrement());
						continue;
					case Constants.STAFF_GENDER:
						indexValue.put(Constants.STAFF_GENDER, index.getAndIncrement());
						continue;
					case Constants.STAFF_BIRTH_DATE:
						indexValue.put(Constants.STAFF_BIRTH_DATE, index.getAndIncrement());
						continue;
					case Constants.STAFF_PLACE_OF_BIRTH:
						indexValue.put(Constants.STAFF_PLACE_OF_BIRTH, index.getAndIncrement());
						continue;
					case Constants.STAFF_DPT:
						indexValue.put(Constants.STAFF_DPT, index.getAndIncrement());
						continue;
					case Constants.STAFF_COUNTRY_OF_BIRTH:
						indexValue.put(Constants.STAFF_COUNTRY_OF_BIRTH, index.getAndIncrement());
						continue;
					case Constants.STAFF_CITIZENSHIP:
						indexValue.put(Constants.STAFF_CITIZENSHIP, index.getAndIncrement());
						continue;
					case Constants.STAFF_PRESS_CARD_NUMBER:
						indexValue.put(Constants.STAFF_PRESS_CARD_NUMBER, index.getAndIncrement());
						continue;
					case Constants.STAFF_TALENT_REGISTRATION:
						indexValue.put(Constants.STAFF_TALENT_REGISTRATION, index.getAndIncrement());
						continue;
					case Constants.STAFF_ROUTING_AND_ACCOUNT:
						indexValue.put(Constants.STAFF_ROUTING_AND_ACCOUNT, index.getAndIncrement());
						continue;
					case Constants.STAFF_BIC:
						indexValue.put(Constants.STAFF_BIC, index.getAndIncrement());
						continue;
					case Constants.STAFF_BANK_ADDRESS:
						indexValue.put(Constants.STAFF_BANK_ADDRESS, index.getAndIncrement());
						continue;
					case Constants.STAFF_BENEFICIARY:
						indexValue.put(Constants.STAFF_BENEFICIARY, index.getAndIncrement());
						continue;
					case Constants.STAFF_MARRIED_NAME:
						indexValue.put(Constants.STAFF_MARRIED_NAME, index.getAndIncrement());
						continue;
					case Constants.STAFF_BIRTH_PROVINCE:
						indexValue.put(Constants.STAFF_BIRTH_PROVINCE, index.getAndIncrement());
						continue;
					case Constants.STAFF_CONTRACT:
						indexValue.put(Constants.STAFF_CONTRACT, index.getAndIncrement());
						continue;
					case Constants.STAFF_LABOR_AGREEMENT:
						indexValue.put(Constants.STAFF_LABOR_AGREEMENT, index.getAndIncrement());
						continue;

					}
					index.getAndIncrement();
				}

				addXlsRow(sheet, spreadsheet.getRow(0), 0, Constants.ERROR_FIELD);
				spreadsheet.removeRow(spreadsheet.getRow(0));

				if (indexValue.size() >= Constants.HEADERS) {

					for (Row row : spreadsheet) {

						hasError = false;
						++rowNumber;
						if (!StringUtils.isBlank(getValue(row.getCell(0)))
								&& indexValue.get(Constants.STAFF_EMP_CODE) != null
								&& !getValue(row.getCell(indexValue.get(Constants.STAFF_EMP_CODE))).isEmpty()) {

							String employeeCode = getValue(row.getCell(indexValue.get(Constants.STAFF_EMP_CODE)));
							if (!StringUtils.isBlank(employeeCode)) {
								employeeCode = employeeCode.trim();
								StaffMember validateStaffEmpCode = staffMemberRepository
										.findByEmployeeCode(employeeCode);
								if (validateStaffEmpCode != null) {
									// make a log for duplicate employee-code
									hasError = true;
									logDetails = "Duplicate employee-code. EmployeeCode : " + employeeCode
											+ " is duplicate on row number : " + rowNumber;
									++corruptedRowNumber;
									addXlsRow(sheet, row, corruptedRowNumber, Constants.STAFF_EMP_CODE + " : "
											+ employeeCode + " is duplicate, employee-code");
									continue;
								}
							}

							String firstName = getValue(row.getCell(indexValue.get(Constants.STAFF_FISRT_NAME)));
							String lastName = getValue(row.getCell(indexValue.get(Constants.STAFF_LAST_NAME)));
							String courtesy = getValue(row.getCell(indexValue.get(Constants.STAFF_NAME_PREFIX)));

							// Initializing StaffMember object and setting values in entity class
							StaffMember staffMember = new StaffMember();
							staffMember.setId(null);
							StaffResource resource = new StaffResource();
							resource.setStaffMember(staffMember);

							staffMember.setEmployeeCode(employeeCode);
							staffMember.setFirstName(firstName);
							staffMember.setName(lastName);
							staffMember.setFunction(getValue(row.getCell(indexValue.get(Constants.STAFF_JOB_TITLE))));

							if (!StringUtils.isBlank(courtesy)) {
								courtesy = courtesy.trim();
								AbstractXmlEntity courtesyXmlEntity = new XmlEntity();
								if (courtesy.equalsIgnoreCase("Mr")) {
									courtesyXmlEntity.setKey(Constants.STAFF_PERFIX_MR);
									staffMember.setCourtesy((XmlEntity) courtesyXmlEntity);
								} else if (courtesy.equalsIgnoreCase("Ms")) {
									courtesyXmlEntity.setKey(Constants.STAFF_PERFIX_MS);
									staffMember.setCourtesy((XmlEntity) courtesyXmlEntity);
								} else if (courtesy.equalsIgnoreCase("Miss")) {
									courtesyXmlEntity.setKey(Constants.STAFF_PERFIX_MISS);
									staffMember.setCourtesy((XmlEntity) courtesyXmlEntity);
								}
							}

							// primary function
							String primaryFunctionName = getValue(
									row.getCell(indexValue.get(Constants.STAFF_PRIMARY_FUNCTION)));
							if (!StringUtils.isBlank(primaryFunctionName)) {
								primaryFunctionName = primaryFunctionName.trim();

								Function fun = functionService.findFunctionByValueIgnoreCase(primaryFunctionName);
								if (fun == null || (fun != null && !(fun instanceof StaffFunction))) {
									// make a log for wrong primary function
									hasError = true;
									logDetails = "Invalid primary function. Function : " + primaryFunctionName
											+ " not found by name on row number : " + rowNumber;
									++corruptedRowNumber;
									addXlsRow(sheet, row, corruptedRowNumber,
											Constants.STAFF_PRIMARY_FUNCTION + " : " + primaryFunctionName);
									continue;
								}
								ResourceFunction primaryFunction = staffMember.getResource().assignFunction(fun);
								primaryFunction.setRating(0);
								primaryFunction.setRates(null);
								primaryFunction.setCreatedOn(Calendar.getInstance());
								primaryFunction.setPrimaryFunction(Boolean.TRUE);
							}

							// secondary functions
							String secondaryFunctionString = getValue(
									row.getCell(indexValue.get(Constants.STAFF_SECONDARY_FUNCTION)));
							if (!StringUtils.isBlank(secondaryFunctionString)) {
								String[] splitFunctions = secondaryFunctionString.split(",");
								for (String functionName : splitFunctions) {
									if (!StringUtils.isBlank(functionName)) {
										functionName = functionName.trim();

										Function fun = functionService.findFunctionByValueIgnoreCase(functionName);
										if (fun == null || (fun != null && !(fun instanceof StaffFunction))) {
											// make a log for wrong secondary function
											hasError = true;
											logDetails = "Invalid secondary function. Function : " + functionName
													+ " not found by name on row number : " + rowNumber;
											++corruptedRowNumber;
											addXlsRow(sheet, row, corruptedRowNumber,
													Constants.STAFF_SECONDARY_FUNCTION + " : " + functionName);
											break;
										}
										ResourceFunction secondaryFunction = staffMember.getResource()
												.assignFunction(fun);
										secondaryFunction.setRating(0);
										secondaryFunction.setRates(null);
										secondaryFunction.setCreatedOn(Calendar.getInstance());
										secondaryFunction.setPrimaryFunction(Boolean.FALSE);
									}
								}
								if (hasError) {
									continue;
								}
							}

							// skills/expertise
							Set<StaffMemberSkill> entitySkills = new HashSet<StaffMemberSkill>();
							String skillsString = getValue(row.getCell(indexValue.get(Constants.STAFF_EXPERTISE)));
							if (!StringUtils.isBlank(skillsString)) {
								String[] splitSkills = skillsString.split(",");

								for (String skillName : splitSkills) {
									if (!StringUtils.isBlank(skillName)) {
										skillName = skillName.trim();

										Skill domain = skillService.findBySkillName(skillName);
										if (domain != null) {
											StaffMemberSkill entity = new StaffMemberSkill();
											entity.setId(null);
											entity.setSkill(domain);
											entity.setRating(0);
											entitySkills.add(entity);
										} else {
											// make a log for invalid skill/expertise
											hasError = true;
											logDetails = "Invalid skill . Skill : " + skillName
													+ " not found by name on row number : " + rowNumber;
											++corruptedRowNumber;
											addXlsRow(sheet, row, corruptedRowNumber,
													Constants.STAFF_EXPERTISE + " : " + skillName);
											break;
										}
									}
								}
								if (hasError) {
									continue;
								}
								staffMember.setSkills(entitySkills);
							}

							// languages
							Set<XmlEntity> entityLanguages = new HashSet<XmlEntity>();
							String languageString = getValue(row.getCell(indexValue.get(Constants.STAFF_LANGUAGE)));
							if (!StringUtils.isBlank(languageString)) {
								String[] splitLanguages = languageString.split(",");
								List<String> languageList = Language.getLanguages();

								for (String languageName : splitLanguages) {
									if (!StringUtils.isBlank(languageName)) {
										languageName = languageName.trim();
										if (languageList.contains(languageName)) {
											String language = Language.getEnum(languageName).getLanguage();
											XmlEntity entity = new XmlEntity();
											entity.setKey(language);
											entityLanguages.add(entity);
										} else {
											// make a log for invalid language
											hasError = true;
											logDetails = "Invalid language . Language : " + languageName
													+ " not found by on row number : " + rowNumber;
											++corruptedRowNumber;
											addXlsRow(sheet, row, corruptedRowNumber,
													Constants.STAFF_LANGUAGE + " : " + languageName);
											break;
										}
									}
								}
								if (hasError) {
									continue;
								}
								staffMember.setLanguages(entityLanguages);
							}

							// not setting groups

							// address,zipcode,city,country
							Address address = new Address();

							String zipString = getValue(
									row.getCell(indexValue.get(Constants.STAFF_ZIP_OR_POSTAL_CODE)));
							if (!StringUtils.isBlank(zipString)) {
								zipString = zipString.trim();
								BigDecimal zipCode = new BigDecimal(getIntValueAsString(zipString));
								address.setZipcode(zipCode.toPlainString());
							}

							address.setLine1(getValue(row.getCell(indexValue.get(Constants.STAFF_ADDRESS))));
							address.setCity(getValue(row.getCell(indexValue.get(Constants.STAFF_CITY))));
							address.setState(getValue(row.getCell(indexValue.get(Constants.STAFF_STATE))));
							if (!StringUtils.isBlank(getValue(row.getCell(indexValue.get(Constants.STAFF_COUNTRY))))) {
								try {
									address.setCountry(CountryUtil.getInstance().getCountry(
											getValue(row.getCell(indexValue.get(Constants.STAFF_COUNTRY))).trim())
											.getCode());
								} catch (Exception e) {
									hasError = true;
									logDetails = "Invalid Country : "
											+ getValue(row.getCell(indexValue.get(Constants.STAFF_COUNTRY)))
											+ " on row number : " + rowNumber;
									++corruptedRowNumber;
									addXlsRow(sheet, row, corruptedRowNumber,
											Constants.STAFF_COUNTRY + " is invalid, country : "
													+ getValue(row.getCell(indexValue.get(Constants.STAFF_COUNTRY))));
								}
							}

							staffMember.setAddress(address);

							// user-setting
							UserSetting userSetting = new UserSetting();

							// emails
							Set<StaffEmail> entityEmails = new HashSet<StaffEmail>();

							// primary-email
							String primaryEmailString = getValue(
									row.getCell(indexValue.get(Constants.STAFF_PRIMARY_EMAIL)));
							if (!StringUtils.isBlank(primaryEmailString)) {
								primaryEmailString = primaryEmailString.trim();
								// check email is valid as per regex and length
								if (StringUtils.isBlank(primaryEmailString)
										|| !CommonUtil.isEmailValid(primaryEmailString)
										|| primaryEmailString.length() > 65) {
									// make log for invalid primary-email
									hasError = true;
									logDetails = "Invalid primary-email . Email : " + primaryEmailString
											+ " on row number : " + rowNumber;
									++corruptedRowNumber;
									addXlsRow(sheet, row, corruptedRowNumber, Constants.STAFF_PRIMARY_EMAIL
											+ " is invalid, primary-email : " + primaryEmailString);
									continue;
								}
								// checking already existing email with another existing-user
								StaffMember checkStaffWithEmail = staffMemberRepository.findByEmail(primaryEmailString);
								if (checkStaffWithEmail != null) {
									// make log for duplicate primary-email
									hasError = true;
									logDetails = "Invalid primary-email . Email : " + primaryEmailString
											+ " is duplicate on row number : " + rowNumber;
									++corruptedRowNumber;
									addXlsRow(sheet, row, corruptedRowNumber, Constants.STAFF_PRIMARY_EMAIL + " : "
											+ primaryEmailString + " is duplicate, primary-email");
									continue;
								}

								StaffEmail email = new StaffEmail();
								email.setId(null);
								email.setEmail(primaryEmailString);
								email.setPrimaryEmail(Boolean.TRUE);
								entityEmails.add(email);
							}

							// secondary-emails
							String secondaryEmailString = getValue(
									row.getCell(indexValue.get(Constants.STAFF_SECONDARY_EMAIL)));
							if (!StringUtils.isBlank(secondaryEmailString)) {
								String[] splitSecondaryEmails = secondaryEmailString.split(",");

								for (String secondaryEmail : splitSecondaryEmails) {
									if (!StringUtils.isBlank(secondaryEmail)) {
										secondaryEmail = secondaryEmail.trim();
										// check email is valid as per regex and length
										if (StringUtils.isBlank(secondaryEmail)
												|| !CommonUtil.isEmailValid(secondaryEmail)
												|| secondaryEmail.length() > 65) {
											// make log for invalid secondary-email
											hasError = true;
											logDetails = "Invalid secondary-email . Email : " + secondaryEmail
													+ " on row number : " + rowNumber;
											++corruptedRowNumber;
											addXlsRow(sheet, row, corruptedRowNumber, Constants.STAFF_SECONDARY_EMAIL
													+ " : " + secondaryEmail + " is invalid, secondary-email");
											break;
										}
										// checking already existing email with another existing-user
										StaffMember checkStaffWithEmail = staffMemberRepository
												.findByEmail(secondaryEmail);
										if (checkStaffWithEmail != null) {
											// make log for duplicate primary-email
											hasError = true;
											logDetails = "Invalid secondary-email . Email : " + secondaryEmail
													+ " is duplicate on row number : " + rowNumber;
											++corruptedRowNumber;
											addXlsRow(sheet, row, corruptedRowNumber,
													Constants.STAFF_PRIMARY_EMAIL + " is duplicate, secondary-email");
											break;
										}

										StaffEmail email = new StaffEmail();
										email.setId(null);
										email.setEmail(secondaryEmail);
										email.setPrimaryEmail(Boolean.FALSE);
										entityEmails.add(email);
									}
								}
								if (hasError) {
									continue;
								}
							}

							// setting all emails
							userSetting.setEmails(entityEmails);

							// telephones
							Set<StaffTelephone> entityTelephones = new HashSet<StaffTelephone>();

							// primary-telephone
							String primaryTelephoneString = getValue(
									row.getCell(indexValue.get(Constants.STAFF_PRIMARY_PHONE)));
							if (!StringUtils.isBlank(primaryTelephoneString)) {
								primaryTelephoneString = primaryTelephoneString.trim();
								String tele = primaryTelephoneString;
								if (tele.split("-").length == 2) {
									if (CommonUtil.isTelephoneCodeNotValid(tele.split("-")[0])) {
										// make log for invalid primary-telephone
										hasError = true;
										logDetails = "Invalid primary-telephone . Telephone : " + primaryTelephoneString
												+ " on row number : " + rowNumber;
										++corruptedRowNumber;
										addXlsRow(sheet, row, corruptedRowNumber, Constants.STAFF_PRIMARY_PHONE + " : "
												+ primaryTelephoneString + " is invalid, primary-phone");
										continue;
									}
									if (!CommonUtil.isTelephoneNumberValid(primaryTelephoneString)) {
										// make log for invalid primary-telephone
										hasError = true;
										logDetails = "Invalid primary-telephone . Telephone : " + primaryTelephoneString
												+ " on row number : " + rowNumber;
										++corruptedRowNumber;
										addXlsRow(sheet, row, corruptedRowNumber, Constants.STAFF_PRIMARY_PHONE + " : "
												+ primaryTelephoneString + " is invalid, primary-phone");
										continue;
									}
									if (tele.split("-")[0].length() > 8 || tele.split("-")[1].length() < 6
											|| tele.split("-")[1].length() > 10 || tele.length() > 17
											|| tele.length() < 10) {
										// make log for invalid primary-telephone
										hasError = true;
										logDetails = "Invalid primary-telephone . Telephone : " + primaryTelephoneString
												+ " on row number : " + rowNumber;
										++corruptedRowNumber;
										addXlsRow(sheet, row, corruptedRowNumber, Constants.STAFF_PRIMARY_PHONE + " : "
												+ primaryTelephoneString + " is invalid, primary-phone");
										continue;
									}
								} else if (tele.split("-").length >= 2) {
									// make log for invalid primary-telephone
									hasError = true;
									logDetails = "Invalid primary-telephone . Telephone : " + primaryTelephoneString
											+ " on row number : " + rowNumber;
									++corruptedRowNumber;
									addXlsRow(sheet, row, corruptedRowNumber, Constants.STAFF_PRIMARY_PHONE + " : "
											+ primaryTelephoneString + " is invalid, primary-phone");
									continue;
								} else {
									if (!CommonUtil.isMobileNumberValid(primaryTelephoneString)) {
										// make log for invalid primary-telephone
										hasError = true;
										logDetails = "Invalid primary-telephone . Telephone : " + primaryTelephoneString
												+ " on row number : " + rowNumber;
										++corruptedRowNumber;
										addXlsRow(sheet, row, corruptedRowNumber, Constants.STAFF_PRIMARY_PHONE + " : "
												+ primaryTelephoneString + " is invalid, primary-phone");
										continue;
									}
								}

								// checking already existing telephone with another existing-user
								List<StaffMember> checkStaffWithTelephone = staffMemberRepository
										.findByTelephone(primaryTelephoneString);
								if (checkStaffWithTelephone != null && !checkStaffWithTelephone.isEmpty()) {
									// make log for invalid primary-telephone
									hasError = true;
									logDetails = "Invalid primary-telephone . Telephone : " + primaryTelephoneString
											+ " is duplicate on row number : " + rowNumber;
									++corruptedRowNumber;
									addXlsRow(sheet, row, corruptedRowNumber, Constants.STAFF_PRIMARY_PHONE + " : "
											+ primaryTelephoneString + " is duplicate, primary-phone");
									continue;
								}

								StaffTelephone telephone = new StaffTelephone();
								telephone.setId(null);
								telephone.setTelephone(new BigDecimal(primaryTelephoneString).toPlainString());
								telephone.setPrimaryTelephone(Boolean.TRUE);
								entityTelephones.add(telephone);
							}

							// secondary-telephones
							String secondaryTelephoneString = getValue(
									row.getCell(indexValue.get(Constants.STAFF_SECONDARY_PHONE)));
							if (!StringUtils.isBlank(secondaryTelephoneString)) {
								String[] splitSecondaryTelephones = secondaryTelephoneString.split(",");

								for (String secondaryTelephone : splitSecondaryTelephones) {
									if (!StringUtils.isBlank(secondaryTelephone)) {
										secondaryTelephone = secondaryTelephone.trim();
										String tele = secondaryTelephone;
										if (tele.split("-").length == 2) {
											if (CommonUtil.isTelephoneCodeNotValid(tele.split("-")[0])) {
												// make log for invalid secondary-telephone
												hasError = true;
												logDetails = "Invalid secondary-telephone . Telephone : "
														+ secondaryTelephone + " on row number : " + rowNumber;
												++corruptedRowNumber;
												addXlsRow(sheet, row, corruptedRowNumber,
														Constants.STAFF_SECONDARY_PHONE + " : " + secondaryTelephone
																+ " is invalid, secondary-phone");
												break;
											}
											if (!CommonUtil.isTelephoneNumberValid(secondaryTelephone)) {
												// make log for invalid secondary-telephone
												hasError = true;
												logDetails = "Invalid secondary-telephone . Telephone : "
														+ secondaryTelephone + " on row number : " + rowNumber;
												++corruptedRowNumber;
												addXlsRow(sheet, row, corruptedRowNumber,
														Constants.STAFF_SECONDARY_PHONE + " : " + secondaryTelephone
																+ " is invalid, secondary-phone");
												break;
											}
											if (tele.split("-")[0].length() > 8 || tele.split("-")[1].length() < 6
													|| tele.split("-")[1].length() > 10 || tele.length() > 17
													|| tele.length() < 10) {
												// make log for invalid secondary-telephone
												hasError = true;
												logDetails = "Invalid secondary-telephone . Telephone : "
														+ secondaryTelephone + " on row number : " + rowNumber;
												++corruptedRowNumber;
												addXlsRow(sheet, row, corruptedRowNumber,
														Constants.STAFF_SECONDARY_PHONE + " : " + secondaryTelephone
																+ " is invalid, secondary-phone");
												break;
											}
										} else if (tele.split("-").length >= 2) {
											// make log for invalid secondary-telephone
											hasError = true;
											logDetails = "Invalid secondary-telephone . Telephone : "
													+ secondaryTelephone + " on row number : " + rowNumber;
											++corruptedRowNumber;
											addXlsRow(sheet, row, corruptedRowNumber, Constants.STAFF_SECONDARY_PHONE
													+ " : " + secondaryTelephone + " is invalid, secondary-phone");
											break;
										} else {
											if (!CommonUtil.isMobileNumberValid(secondaryTelephone)) {
												// make log for invalid primary-telephone
												hasError = true;
												logDetails = "Invalid secondary-telephone . Telephone : "
														+ secondaryTelephone + " on row number : " + rowNumber;
												++corruptedRowNumber;
												addXlsRow(sheet, row, corruptedRowNumber,
														Constants.STAFF_SECONDARY_PHONE + " : " + secondaryTelephone
																+ " is invalid, secondary-phone");
												break;
											}
										}

										// checking already existing telephone with another existing-user
										List<StaffMember> checkStaffWithTelephone = staffMemberRepository
												.findByTelephone(secondaryTelephone);
										if (checkStaffWithTelephone != null && !checkStaffWithTelephone.isEmpty()) {
											// make log for invalid primary-telephone
											hasError = true;
											logDetails = "Invalid secondary-telephone . Telephone : "
													+ secondaryTelephone + " is duplicate on row number : " + rowNumber;
											++corruptedRowNumber;
											addXlsRow(sheet, row, corruptedRowNumber, Constants.STAFF_SECONDARY_PHONE
													+ " : " + secondaryTelephone + " is duplicate, secondary-phone");
											break;
										}

										StaffTelephone telephone = new StaffTelephone();
										telephone.setId(null);
										telephone.setTelephone(new BigDecimal(secondaryTelephone).toPlainString());
										telephone.setPrimaryTelephone(Boolean.TRUE);
										entityTelephones.add(telephone);
									}
								}
								if (hasError) {
									continue;
								}
							}

							// set secondary-emails
							userSetting.setTelephones(entityTelephones);

							// set user-setting in staffmember
							staffMember.setUserSetting(userSetting);

							// staff-identity
							StaffMemberIdentity staffMemberIdentity = new StaffMemberIdentity();

							staffMemberIdentity.setHealthInsuranceNumber(
									getValue(row.getCell(indexValue.get(Constants.STAFF_SOCIAL_SECURITY_NUMBER))));

							String gender = getValue(row.getCell(indexValue.get(Constants.STAFF_GENDER)));
							if (!StringUtils.isBlank(gender)) {
								gender = gender.trim();
								AtomicReference<String> atomicReference = new AtomicReference<String>(gender);
								if (Gender.getGenderTypes().stream()
										.anyMatch(g -> g.equalsIgnoreCase(atomicReference.get()))) {
									AbstractXmlEntity genderXmlEntity = new XmlEntity();
									genderXmlEntity.setKey(Gender.getEnum(gender).getGender());
									staffMemberIdentity.setGender((XmlEntity) genderXmlEntity);
								} else {
									// make a log for invalid-gender
									hasError = true;
									logDetails = "Invalid gender. Gender : " + gender + " on row number : " + rowNumber;
									++corruptedRowNumber;
									addXlsRow(sheet, row, corruptedRowNumber, Constants.STAFF_GENDER + " : " + gender);
									continue;
								}
							}

							try {
								Date javaDate = DateUtil.getJavaDate((Double.parseDouble(
										getValue(row.getCell(indexValue.get(Constants.STAFF_BIRTH_DATE))).trim())));
								Calendar dateOfBirth = Calendar.getInstance();
								dateOfBirth.setTime(javaDate);
								staffMemberIdentity.setDateOfBirth(dateOfBirth);

							} catch (Exception e) {
								hasError = true;
								logDetails = "Invalid date format for date of birth in row no:" + rowNumber;
								++corruptedRowNumber;
								addXlsRow(sheet, row, corruptedRowNumber, Constants.STAFF_BIRTH_DATE);
								continue;
							}

							staffMemberIdentity.setBirthPlace(
									getValue(row.getCell(indexValue.get(Constants.STAFF_PLACE_OF_BIRTH))));

							staffMemberIdentity
									.setBirthState(getValue(row.getCell(indexValue.get(Constants.STAFF_DPT))));

							staffMemberIdentity.setPersistentbirthCountry(
									getValue(row.getCell(indexValue.get(Constants.STAFF_COUNTRY_OF_BIRTH))).trim());
							staffMemberIdentity.setBirthCountry(new Locale(Locale.ENGLISH.getLanguage(),
									staffMemberIdentity.getPersistentbirthCountry()));

							String citizenship = getValue(row.getCell(indexValue.get(Constants.STAFF_CITIZENSHIP)));
							if (!StringUtils.isBlank(citizenship)) {
								citizenship = citizenship.trim();
								XmlEntity xmlEntityNationality = new XmlEntity();
								xmlEntityNationality
										.setKey(CountryUtil.getInstance().getCountry(citizenship).getName());
								staffMemberIdentity.setNationality(xmlEntityNationality);
							}

							staffMemberIdentity.setNumCardPress(getIntValueAsString(
									getValue(row.getCell(indexValue.get(Constants.STAFF_PRESS_CARD_NUMBER)))));
							staffMemberIdentity.setNumCotisationSpectacle(getIntValueAsString(
									getValue(row.getCell(indexValue.get(Constants.STAFF_TALENT_REGISTRATION)))));
							staffMemberIdentity.setNumIBAN(
									getValue(row.getCell(indexValue.get(Constants.STAFF_ROUTING_AND_ACCOUNT))));

							staffMemberIdentity.setNumBic(getValue(row.getCell(indexValue.get(Constants.STAFF_BIC))));

							staffMemberIdentity.setDomiciliation(
									getValue(row.getCell(indexValue.get(Constants.STAFF_BANK_ADDRESS))));

							staffMemberIdentity.setBeneficiaire(
									getValue(row.getCell(indexValue.get(Constants.STAFF_BENEFICIARY))));

							staffMemberIdentity.setMarriedName(
									getValue(row.getCell(indexValue.get(Constants.STAFF_MARRIED_NAME))));

							staffMemberIdentity.setBirthProvince(
									getValue(row.getCell(indexValue.get(Constants.STAFF_BIRTH_PROVINCE))));

							String contractString = getValue(row.getCell(indexValue.get(Constants.STAFF_CONTRACT)));
							if (!StringUtils.isBlank(contractString)) {
								contractString = contractString.trim();
								List<String> contractList = ContractType.getContractTypes();
								if (contractList.contains(contractString)) {

									String contract = ContractType.getEnum(contractString).getContractType();
									EntertainmentContractSetting contractSetting = new EntertainmentContractSetting();

									contractSetting.setId(null);

									com.teamium.domain.prod.resources.staff.contract.ContractType type = new com.teamium.domain.prod.resources.staff.contract.ContractType();
									type.setKey(contract);
									contractSetting.setType(type);

									com.teamium.domain.prod.resources.staff.contract.SocialConvention socialConvention = new com.teamium.domain.prod.resources.staff.contract.SocialConvention();
									socialConvention.setKey("");
									contractSetting.setConvention(socialConvention);

									RateUnit defaultRate = new RateUnit();
									defaultRate.setKey("H");
									contractSetting.setDefaultRate(defaultRate);

									Calendar c = Calendar.getInstance();
									c.set(Calendar.HOUR_OF_DAY, 19);
									c.set(Calendar.MINUTE, 0);
									contractSetting.setDayEnd(c.getTime());

									c.set(Calendar.HOUR_OF_DAY, 10);
									c.set(Calendar.MINUTE, 0);
									contractSetting.setDayStart(c.getTime());

									staffMember.setContractSetting(contractSetting);
								} else {
									// make log for contract-setting
									hasError = true;
									logDetails = "Invalid contract on row number : " + rowNumber;
									++corruptedRowNumber;
									addXlsRow(sheet, row, corruptedRowNumber, Constants.STAFF_CONTRACT);
									continue;
								}
							}

							staffMember.setIdentity(staffMemberIdentity);

							String labourAgreement = getValue(
									row.getCell(indexValue.get(Constants.STAFF_LABOR_AGREEMENT)));
							if (!StringUtils.isBlank(labourAgreement)) {
								labourAgreement = labourAgreement.trim();
								LabourRule rule = labourRuleRepository.findLabourByName(labourAgreement);
								if (rule == null) {
									// make log for invalid labour-rule
									hasError = true;
									logDetails = "Invalid labour-agreement on row number : " + rowNumber;
									++corruptedRowNumber;
									addXlsRow(sheet, row, corruptedRowNumber,
											Constants.STAFF_LABOR_AGREEMENT + " not found by name , Labour-agreement");
									continue;
								}

								staffMember.setLabourRule(rule);
							}

							if (!hasError) {
								logger.info(
										"No error in spreadsheet row. Uploading it in database row. : " + rowNumber);
								System.out.println("staffMember => " + staffMember);
								staffMemberRepository.save(staffMember);
							}

						} else {
							for (Cell cell : row) {
								if (!StringUtils.isBlank(getValue(cell))) {
									logDetails = "Not importing data from row no:" + rowNumber
											+ "  because employee code of staff memeber not exist in given row.";
									++corruptedRowNumber;
									addXlsRow(sheet, row, corruptedRowNumber, Constants.STAFF_EMP_CODE);
									break;
								}
							}
							break;
						}
					}
				} else {
					hasInvalidHeader = true;
					logDetails = "Invalid Header List";
				}
				if (corruptedRowNumber > 0) {
					hasError = true;
					String resourcePath = propConfig.getTeamiumResourcesPath();
					logFileName = FilenameUtils.removeExtension(file.getOriginalFilename()) + "_" + dateString
							+ ".xlsx";

					relativePath = Constants.SPREADSHEET_STRING + "/" + Constants.STAFF_STRING + "/"
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

				logDetails = "Error! , Invalid file format. Please provide xlsx/xls/csv format.";
				logger.info("Spredsheet not uploaded for staff. Found invalid format. File found with extention: "
						+ fileExtention);
				throw new UnprocessableEntityException(
						"Spredsheet not uploaded for staff. Found invalid format. File found with extention: "
								+ fileExtention);
			}
		} else {
			logger.info("File is null. Please upload valid file");
			throw new UnprocessableEntityException("File is null. Please upload valid file");
		}

		if (hasInvalidHeader) {
			// spreadsheet has invalid headers
			hasError = true;
			logDetails = " Spreadsheet has invalid headers, Upload failed.";
		}
		try {
			spreadsheetUploadLog = new SpreadsheetUploadLog(logDetails, calander,
					loggedInUser.getUserSetting().getLogin(), ImportFor.STAFF, fileExtention, hasError,
					loggedInUser.getId(), file.getOriginalFilename(), absolutePath);
			spreadsheetUploadLog.setUrl(url);
			logger.info("Saving spreadsheet log in database.");
			spreadsheetUploadLog = spreadsheetUploadLogRepository.save(spreadsheetUploadLog);
			logger.info("Successfully saved spreadsheet log in database with id : " + spreadsheetUploadLog.getId());
		} catch (Exception e) {
			logger.info("Exception in saving log in database");
			e.printStackTrace();
		}
		logger.info("Uploaded spredsheet for staff.");
		if (hasError) {
			SpreadsheetMessageDTO message = new SpreadsheetMessageDTO("Imported staff spreadsheet contains error data",
					Boolean.TRUE);
			return message;
		} else if (hasInvalidHeader) {
			SpreadsheetMessageDTO message = new SpreadsheetMessageDTO(logDetails, Boolean.TRUE);
			return message;
		} else {
			SpreadsheetMessageDTO message = new SpreadsheetMessageDTO("Successfully imported staff spreadsheet",
					Boolean.FALSE);
			return message;
		}
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
		// return String.valueOf(data.getNumericCellValue());

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

	public StaffMember findById(long id) {
		StaffMember staffMember = staffMemberRepository.findOne(id);
		if (staffMember == null) {
			throw new NotFoundException("Invalid staff id.");
		}
		return staffMember;
	}

	/**
	 * To get spreadsheet log by logged-in-user
	 * 
	 * @return list of spreadsheet-logs
	 */
	public List<SpreadsheetUploadLog> getSpreadsheetUploadLogByLoggedInUser() {
		logger.info("Inside StaffMemberService :: findAllSpreadsheetUploadLog(), To get  ");
		StaffMember loggedInUser = authenticationService.getAuthenticatedUser();
		if (loggedInUser == null) {
			logger.info("Invalid staff");
			throw new NotFoundException("Invalid staff");
		}
		return spreadsheetUploadLogRepository.getSpreadsheetLogByStaff(loggedInUser.getId());
	}

	/**
	 * To get count of available staffMember
	 * 
	 * @param available
	 * 
	 * @return count of available staffmember
	 */
	public Long findCountByAvailable(boolean available) {
		logger.info("Inside StaffMemberService :: findCountByAvailable(available : " + available + ")");
		return staffMemberRepository.findCountByAvailable(available);
	}

	/**
	 * To edit profile of current login staff
	 * 
	 * @param staffMemberDTO
	 * @param id
	 * @param request
	 * 
	 * @return StaffMember wrapper object
	 * @throws IOException
	 */
	public StaffMemberDTO editProfile(StaffMemberDTO staffMemberDTO, long id, HttpServletRequest request)
			throws IOException {
		logger.info(
				"Inside StaffMemberService :: editProfile(StaffMemberDTO staffMemberDTO, long id), To edit profile on id: "
						+ id + " and  staffMemberDTO: " + staffMemberDTO);
		StaffMember currentLoginUser = this.authenticationService.getAuthenticatedUser();
		if (currentLoginUser.getId() != id) {
			logger.error("Invalid Authorization");
			throw new UnprocessableEntityException("Invalid Authorization");
		}
		StaffMember staffMember = findById(id);
		staffMemberDTO.setEmployeeCode(staffMember.getEmployeeCode());

		// method to validate the mandatory fields
		validateStaff(staffMemberDTO);
		staffMember.setFirstName(staffMemberDTO.getFirstName());
		staffMember.setName(staffMemberDTO.getLastName());

		if (StringUtils.isBlank(staffMemberDTO.getUiLanguage())) {
			staffMember.setUiLanguage("English");
		} else {
			staffMember.setUiLanguage(staffMemberDTO.getUiLanguage());
		}
		if (staffMemberDTO.isRemovePhoto()) {
			staffMember.setPhoto(null);
		}

		staffMemberDTO.getUserSettingDTO().setLogin(staffMember.getUserSetting().getLogin());
		validateMyAccount(staffMemberDTO, staffMember);
		this.staffMemberRepository.save(staffMember);
		validatePassword(staffMemberDTO, request);

		StaffMemberDTO memberDTO = new StaffMemberDTO(staffMember);
		memberDTO.getUserSettingDTO().setDigitalSignatureByteCode(getDigitalSignatureByteCode(id).getSignatureByte());
		logger.info("Returning after getiing staffMember and edit profile");
		return memberDTO;
	}

	/**
	 * To get digital signature byte code of staff
	 * 
	 * @param staffId
	 * @param absolutefilePath
	 * 
	 * @return signatureByte
	 * 
	 * @throws IOException
	 */
	public DigitalSignatureByteDTO getDigitalSignatureByteCode(long id) throws IOException {
		logger.info("Inside StaffMemberService :: getDigitalSignatureByteCode(), To digital signature byte code  ");

		StaffMember currentLoginUser = this.authenticationService.getAuthenticatedUser();
		if (currentLoginUser.getId() != id) {
			logger.error("Invalid Authorization");
			throw new UnprocessableEntityException("Invalid Authorization");
		}

		StaffMember staffMember = findById(id);
		String absolutefilePath = staffMember.getUserSetting().getSignaturePath();
		byte[] signatureByte = new byte[0];
		if (!StringUtils.isBlank(absolutefilePath)) {
			signatureByte = FileReader.getEncodedFileByteArray(absolutefilePath);
		}
		DigitalSignatureByteDTO signatureDTO = new DigitalSignatureByteDTO(id, signatureByte,
				staffMember.getUserSetting().getEditable(), absolutefilePath);
		logger.info("Return after getting digital signature byte code");
		return signatureDTO;

	}

	/**
	 * To save digital signature on staff
	 * 
	 * @param signatureByte
	 * @param staffId
	 * 
	 * @return absolutePath
	 */
	public DigitalSignatureByteDTO setDigitalSignatureUrl(DigitalSignatureByteDTO signatureByteDTO) {
		logger.info("Inside StaffMemberService :: setDigitalSignatureUrl(), To save digital signature on staff "
				+ signatureByteDTO);

		if (signatureByteDTO == null) {
			logger.info("Please provide request body");
			throw new UnprocessableEntityException("Please provide request body");
		} else if (signatureByteDTO.getSignatureByte() == null) {
			logger.info("Please provide digital signature byte code ");
			throw new UnprocessableEntityException("Please provide digital signature byte code ");
		}
		StaffMember currentLoginUser = this.authenticationService.getAuthenticatedUser();
		if (currentLoginUser.getId() != signatureByteDTO.getStaffId()) {
			logger.error("Invalid Authorization");
			throw new UnprocessableEntityException("Invalid Authorization");
		}

		StaffMember staffMember = findById(signatureByteDTO.getStaffId());

		String url = propConfig.getTeamiumResourcesPath() + "/" + Constants.PERSON_SIGNATURE_DIR + ""
				+ signatureByteDTO.getStaffId();
		String fileName = "Signature.txt";
		String absolutePath = url + "/" + fileName;
		byte[] signature = Base64.getDecoder().decode(signatureByteDTO.getSignatureByte());

		try {
			FileWriter.saveFile(signature, url, fileName);
			logger.info("Successflly save file and url is: " + absolutePath);
			staffMember.getUserSetting().setSignaturePath(absolutePath);
			staffMember.getUserSetting().setEditable(signatureByteDTO.isEditable());
			this.staffMemberRepository.save(staffMember);

			logger.info("Return DigitalSignatureByteDTO");
			return new DigitalSignatureByteDTO(signatureByteDTO.getStaffId(), Base64.getEncoder().encode(signature),
					signatureByteDTO.isEditable(), absolutePath);

		} catch (Exception e) {
			logger.error("File not write : " + e);
			throw new UnprocessableEntityException("File not write : " + e);
		}

	}

	/**
	 * To validate password
	 * 
	 * @param staffMemberDTO
	 * @param request
	 */
	public void validatePassword(StaffMemberDTO staffMemberDTO, HttpServletRequest request) {
		logger.info(
				"Inside StaffMemberService :: validatePassword(StaffMemberDTO staffMemberDTO, HttpServletRequest request), to validate password and update password");
		if (staffMemberDTO.getUserSettingDTO() != null
				&& (!StringUtils.isBlank(staffMemberDTO.getUserSettingDTO().getPassword())
						|| !StringUtils.isBlank(staffMemberDTO.getUserSettingDTO().getConfirmPassword())
						|| !StringUtils.isBlank(staffMemberDTO.getUserSettingDTO().getExistingPassword()))) {
			if (StringUtils.isBlank(staffMemberDTO.getUserSettingDTO().getPassword())) {
				logger.error("Please provide valid password");
				throw new UnprocessableEntityException("Please provide valid password");
			}
			if (StringUtils.isBlank(staffMemberDTO.getUserSettingDTO().getConfirmPassword())) {
				logger.error("Please provide valid confirm password");
				throw new UnprocessableEntityException("Please provide valid confirm password");
			}
			if (StringUtils.isBlank(staffMemberDTO.getUserSettingDTO().getExistingPassword())) {
				logger.error("Please provide valid existing password");
				throw new UnprocessableEntityException("Please provide valid existing password");
			}

			this.userService.updatePassword(staffMemberDTO, request);
		}
		logger.info("Returning after validate and update password");
	}

	/**
	 * validate method for edit profile
	 * 
	 * @param staffMemberDTO
	 * @param staffMember
	 * @param request
	 */
	public void validateMyAccount(StaffMemberDTO staffMemberDTO, StaffMember staffMember) {
		logger.info(
				"Inside StaffMemberService :: validateMyAccount(StaffMemberDTO staffMemberDTO, StaffMember staffMember, HttpServletRequest request), To validate Staff ");
		Long staffId = staffMember.getId();
		AtomicBoolean isRemove = new AtomicBoolean(false);

		List<Long> emailIds = staffMemberDTO.getUserSettingDTO().getEmails().stream().map(id -> id.getId())
				.collect(Collectors.toList());
		Long emailSize = staffMember.getUserSetting().getEmails().stream().filter(m -> emailIds.contains(m.getId()))
				.count();
		if (emailIds.size() != emailSize || emailIds.size() != staffMember.getUserSetting().getEmails().size()) {
			logger.error("Please provide valid email");
			throw new UnprocessableEntityException("Please provide valid emails");
		}
		staffMember.getUserSetting().getEmails().stream().forEach(mail -> {
			staffMemberDTO.getUserSettingDTO().getEmails().stream().forEach(dtoMail -> {
				if (dtoMail.getId() == mail.getId()) {
					dtoMail.setEmail(mail.getEmail());
				}
			});
		});

		List<Long> telephoneIds = staffMemberDTO.getUserSettingDTO().getTelephones().stream().map(id -> id.getId())
				.collect(Collectors.toList());
		Long telNumberSize = staffMember.getUserSetting().getTelephones().stream()
				.filter(s -> telephoneIds.contains(s.getId())).count();
		if (telephoneIds.size() != telNumberSize
				|| telephoneIds.size() != staffMember.getUserSetting().getTelephones().size()) {
			logger.error("Please provide all telephone numbers");
			throw new UnprocessableEntityException("Please provide all telephone numbers");
		}

		staffMember.getUserSetting().getTelephones().stream().forEach(number -> {
			staffMemberDTO.getUserSettingDTO().getTelephones().stream().forEach(dtoNumber -> {
				if (number.getId() == dtoNumber.getId()) {
					dtoNumber.setTelephone(number.getTelephone());
				}
			});
		});

		validateStaffEmail(staffMember, staffMemberDTO, isRemove, staffId);
		validateStaffTelephone(staffMember, staffMemberDTO, isRemove, staffId);

		if (staffMemberDTO.getAddress() != null) {
			staffMemberDTO.getAddress().setId(staffMember.getAddress().getId());
			if (!StringUtils.isBlank(staffMemberDTO.getAddress().getLine1())) {
				staffMember.getAddress().setLine1(staffMemberDTO.getAddress().getLine1());
			}
			if (!StringUtils.isBlank(staffMemberDTO.getAddress().getLine2())) {
				staffMember.getAddress().setLine2(staffMemberDTO.getAddress().getLine2());
			}
			if (!StringUtils.isBlank(staffMemberDTO.getAddress().getZipcode())) {
				staffMember.getAddress().setZipcode(staffMemberDTO.getAddress().getZipcode());
			}
			if (!StringUtils.isBlank(staffMemberDTO.getAddress().getState())) {
				staffMember.getAddress().setState(staffMemberDTO.getAddress().getState());
			}
			if (!StringUtils.isBlank(staffMemberDTO.getAddress().getCity())) {
				staffMember.getAddress().setCity(staffMemberDTO.getAddress().getCity());
			}
			if (!StringUtils.isBlank(staffMemberDTO.getAddress().getCountry())) {

				staffMember.getAddress().setCountry(
						CountryUtil.getInstance().getCountry(staffMemberDTO.getAddress().getCountry()).getCode());
			}
		}

		if (staffMemberDTO.getUserSettingDTO() != null
				&& !StringUtils.isBlank(staffMemberDTO.getUserSettingDTO().getTimezone())) {
			com.teamium.domain.TimeZone timeZone = this.timeZoneService
					.getTimeZoneBYName(staffMemberDTO.getUserSettingDTO().getTimezone());
			if (timeZone == null) {
				logger.error(
						"Not found TimeZone on this zone name : " + staffMemberDTO.getUserSettingDTO().getTimezone());
				throw new NotFoundException(
						"Not found TimeZone on this zone name : " + staffMemberDTO.getUserSettingDTO().getTimezone());
			}
			staffMember.getUserSetting().setTimezone(timeZone.getZoneName());
		}

		if (staffMemberDTO.getUserSettingDTO() != null
				&& staffMemberDTO.getUserSettingDTO().getDigitalSignatureByteCode() != null) {
			// String url = propConfig.getTeamiumResourcesPath() + "/" +
			// Constants.PERSON_SIGNATURE_DIR + "" + staffId;
			// String fileName = "Signature.txt";
			// String absolutePath = url + "/" + fileName;
			// byte[] signature =
			// staffMemberDTO.getUserSettingDTO().getDigitalSignatureByteCode();
			// try {
			// FileWriter.saveFile(signature, url, fileName);
			// logger.info("Successflly save file and url is: " + absolutePath);
			// staffMember.getUserSetting().setSignaturePath(absolutePath);
			//
			// } catch (Exception e) {
			// logger.error("File not write : " + e);
			// throw new UnprocessableEntityException("File not write : " + e);
			// }

			// staffMember.getUserSetting().setSignaturePath(
			// setDigitalSignatureUrl(staffMemberDTO.getUserSettingDTO().getDigitalSignatureByteCode(),
			// staffId));
		}
		logger.info("Returning after validate my account");
	}

	/**
	 * validate staff email
	 * 
	 * @param staffMember
	 * @param staffMemberDTO
	 * @param isRemove
	 * @param staffId
	 */
	public void validateStaffEmail(StaffMember staffMember, StaffMemberDTO staffMemberDTO, AtomicBoolean isRemove,
			Long staffId) {
		logger.info(
				"Inside StaffMemberService :: validateStaffEmail(StaffMember staffMember, StaffMemberDTO staffMemberDTO, AtomicBoolean isRemove,Long staffId), To validate Staff email ");
		Set<StaffEmail> dbStaffEmails = staffMember.getUserSetting() == null ? new HashSet<StaffEmail>()
				: staffMember.getUserSetting().getEmails();
		Set<StaffEmailDTO> staffEmails = staffMemberDTO.getUserSettingDTO().getEmails();

		if (staffEmails != null && !staffEmails.isEmpty()) {
			staffMember.getUserSetting().setEmails(staffEmails.stream().map(dto -> {

				// removing staff-mails not present in dto
				if (dbStaffEmails != null && !dbStaffEmails.isEmpty()) {
					dbStaffEmails.removeIf(dt -> {
						isRemove.set(true);
						staffEmails.forEach(dt1 -> {
							if (dt1.getId() != null && dt1.getId().longValue() == dt.getId().longValue()) {
								isRemove.set(false);
								return;
							}
						});
						return isRemove.get();
					});
				}

				// check email is valid as per regex and length
				if (StringUtils.isBlank(dto.getEmail()) || !CommonUtil.isEmailValid(dto.getEmail())
						|| dto.getEmail().length() > 65) {
					logger.error("Please enter a valid email.");
					throw new UnprocessableEntityException("Please enter a valid email.");
				}

				// checking already existing email with another existing-user
				StaffMember checkStaffWithEmail = staffMemberRepository.findByEmail(dto.getEmail());
				if (staffId == null && checkStaffWithEmail != null) {
					logger.info("Email already exists.");
					throw new UnprocessableEntityException("Email already exists.");
				}

				int idStaff = staffId == null ? 0 : staffId.intValue();
				int idCheckStaffWithEmail = checkStaffWithEmail == null ? 0 : checkStaffWithEmail.getId().intValue();

				if (staffId != null && checkStaffWithEmail != null && idStaff != idCheckStaffWithEmail) {
					logger.info("Email already exists.");
					throw new UnprocessableEntityException("Email already exists.");
				}

				StaffEmail entity = dto.getStaffEmail(new StaffEmail());
				if (dto.getId() != null) {
					Optional<StaffEmail> list = dbStaffEmails.stream()
							.filter(userEmail -> userEmail.getId().longValue() == dto.getId().longValue()).findFirst();
					if (list.isPresent()) {
						entity = list.get();
					}
				}

				if (!StringUtils.isBlank(entity.getEmail()) && !dto.getEmail().equalsIgnoreCase(entity.getEmail())
						&& entity.getId() != null) {
					logger.info("Not allowed to update this current email.");
					throw new UnprocessableEntityException("Not allowed to update this current email.");
				} else if (entity.getId() == null && dto.getEmail().equalsIgnoreCase(entity.getEmail())) {
					logger.info("Email already exists.");
					throw new UnprocessableEntityException("Email already exists.");
				} else if (entity.getId() == null) {
					entity.setEmail(dto.getEmail());
				}

				dbStaffEmails.stream().forEach(emailEntity -> {
					if (dto.getId() == null && emailEntity.getEmail().equalsIgnoreCase(dto.getEmail())) {
						logger.info("Email already exists.");
						throw new UnprocessableEntityException("Email already exists.");
					}
				});

				entity.setPrimaryEmail(dto.isPrimaryEmail());
				entity.setEmail(dto.getEmail());
				return entity;

			}).collect(Collectors.toSet()));

		} else {
			logger.info("Atleast one primary email should pe present.");
			throw new UnprocessableEntityException("Atleast one primary email should pe present.");
		}

		// checking the size of emails list and validations
		Set<StaffEmail> mails = staffMember.getUserSetting().getEmails();
		if (mails != null && !mails.isEmpty()) {

			if (mails.size() == 1) {
				mails.stream().forEach(mail -> mail.setPrimaryEmail(true));
			}

			Set<StaffEmail> countPrimaryEmails = mails.stream().filter(mail -> mail.isPrimaryEmail() == true)
					.collect(Collectors.toSet());
			if (countPrimaryEmails != null && !countPrimaryEmails.isEmpty()) {
				if (countPrimaryEmails.size() > 1) {
					logger.info("Only one email can be primary.");
					throw new UnprocessableEntityException("Only one email can be primary.");
				}
			} else {
				logger.info("Atleast one email must be primary");
				throw new UnprocessableEntityException("Atleast one email must be primary");
			}
		} else {
			logger.info("Atleast one email must be assigned on staff.");
			throw new UnprocessableEntityException("Atleast one email must be assigned on staff.");
		}
		logger.info("Returning after validate staff emails");
	}

	/**
	 * validate staff telephone
	 * 
	 * @param staffMember
	 * @param staffMemberDTO
	 * @param isRemove
	 * @param staffId
	 */
	public void validateStaffTelephone(StaffMember staffMember, StaffMemberDTO staffMemberDTO, AtomicBoolean isRemove,
			Long staffId) {
		logger.info(
				"Inside StaffMemberService :: validateStaffTelephone(StaffMember staffMember, StaffMemberDTO staffMemberDTO, AtomicBoolean isRemove,\n"
						+ "			Long staffId), to validate staff telephone");
		if (staffMemberDTO.getUserSettingDTO() != null) {
			logger.info("Adding user setting :" + staffMemberDTO.getUserSettingDTO());
			Set<StaffTelephone> dbStaffTelephones = staffMember.getUserSetting() == null ? new HashSet<>()
					: staffMember.getUserSetting().getTelephones();
			Set<StaffTelephoneDTO> staffTelephones = staffMemberDTO.getUserSettingDTO().getTelephones();

			if (staffTelephones != null && !staffTelephones.isEmpty()) {
				staffMember.getUserSetting().setTelephones(staffTelephones.stream().map(dto -> {

					// removing staff-telephones not present in dto
					if (dbStaffTelephones != null && !dbStaffTelephones.isEmpty()) {
						dbStaffTelephones.removeIf(dt -> {
							isRemove.set(true);
							staffTelephones.forEach(dt1 -> {
								if (dt1.getId() != null && dt1.getId().longValue() == dt.getId().longValue()) {
									isRemove.set(false);
									return;
								}
							});
							return isRemove.get();
						});
					}

					if (StringUtils.isBlank(dto.getTelephone())) {
						logger.error("Please enter a valid telephone.");
						throw new UnprocessableEntityException("Please enter a valid telephone.");
					}
					String tele = dto.getTelephone();

					if (tele.split("-").length == 2) {

						if (CommonUtil.isTelephoneCodeNotValid(tele.split("-")[0])) {
							logger.error("Please enter a valid telephone.");
							throw new UnprocessableEntityException("Please enter a valid telephone.");
						}
						if (!CommonUtil.isTelephoneNumberValid(dto.getTelephone())) {
							logger.error("Please enter a valid telephone.");
							throw new UnprocessableEntityException("Please enter a valid telephone.");
						}
						if (tele.split("-")[0].length() > 8 || tele.split("-")[1].length() < 6
								|| tele.split("-")[1].length() > 10 || tele.length() > 17 || tele.length() < 10) {
							logger.error("Please enter a valid telephone.");
							throw new UnprocessableEntityException("Please enter a valid telephone.");
						}

					} else if (tele.split("-").length >= 2) {

						logger.error("Please enter a valid telephone.");
						throw new UnprocessableEntityException("Please enter a valid telephone.");

					} else {
						if (!CommonUtil.isMobileNumberValid(dto.getTelephone())) {
							logger.error("Please enter a valid telephone.");
							throw new UnprocessableEntityException("Please enter a valid telephone.");
						}
					}

					// checking already existing telephone with another existing-user
					List<StaffMember> checkStaffWithTelephone = staffMemberRepository
							.findByTelephone(dto.getTelephone());

					// if (staffId == null && checkStaffWithTelephone != null) {
					// logger.info("Telephone already exists.");
					// throw new UnprocessableEntityException("Telephone already exists.");
					// }

					int idStaff = staffId == null ? 0 : staffId.intValue();

					if (checkStaffWithTelephone != null && !checkStaffWithTelephone.isEmpty()) {
						checkStaffWithTelephone.stream().forEach(telephoneUser -> {

							int idCheckStaffWithTelephone = telephoneUser == null ? 0
									: telephoneUser.getId().intValue();

							// if (staffId != null && telephoneUser != null) {
							// logger.info("Telephone already exists.");
							// throw new UnprocessableEntityException("Telephone already exists.");
							// }

							// if (staffId != null && telephoneUser != null && idStaff !=
							// idCheckStaffWithTelephone) {
							// logger.info("Telephone already exists.");
							// throw new UnprocessableEntityException("Telephone already exists.");
							// }

						});
					}

					StaffTelephone entity = dto.getStaffTelephone(new StaffTelephone());
					if (dto.getId() != null) {
						Optional<StaffTelephone> list = dbStaffTelephones.stream()
								.filter(userTelephone -> userTelephone.getId().longValue() == dto.getId().longValue())
								.findFirst();
						if (list.isPresent()) {
							entity = list.get();
						}
					}

					if (!StringUtils.isBlank(entity.getTelephone())
							&& !dto.getTelephone().equalsIgnoreCase(entity.getTelephone()) && entity.getId() != null) {
						logger.info("Not allowed to update this current telephone.");
						throw new UnprocessableEntityException("Not allowed to update this current telephone.");
					} else if (entity.getId() == null && dto.getTelephone().equalsIgnoreCase(entity.getTelephone())) {
						logger.info("Telephone already exists.");
						throw new UnprocessableEntityException("Telephone already exists.");
					} else if (entity.getId() == null) {
						entity.setTelephone(dto.getTelephone());
					}

					dbStaffTelephones.stream().forEach(telephoneEntity -> {
						if (dto.getId() == null
								&& telephoneEntity.getTelephone().equalsIgnoreCase(dto.getTelephone())) {
							logger.info("Telephone already exists.");
							throw new UnprocessableEntityException("Telephone already exists.");
						}
					});

					entity.setPrimaryTelephone(dto.isPrimaryTelephone());
					entity.setTelephone(dto.getTelephone());
					return entity;

				}).collect(Collectors.toSet()));

			} else {
				logger.info("Atleast one primary telephone should pe present.");
				throw new UnprocessableEntityException("Atleast one primary telephone should pe present.");
			}
		} else {
			logger.info("Atleast one primary telephone should pe present.");
			throw new UnprocessableEntityException("Atleast one primary telephone should pe present.");
		}

		// checking the size of telephones list and validations
		Set<StaffTelephone> telephones = staffMember.getUserSetting().getTelephones();
		if (telephones != null && !telephones.isEmpty()) {

			if (telephones.size() == 1) {
				telephones.stream().forEach(tel -> tel.setPrimaryTelephone(true));
			}

			Set<StaffTelephone> countPrimaryTelephones = telephones.stream()
					.filter(tel -> tel.isPrimaryTelephone() == true).collect(Collectors.toSet());
			if (countPrimaryTelephones != null && !countPrimaryTelephones.isEmpty()) {
				if (countPrimaryTelephones.size() > 1) {
					logger.info("Only one telephone can be primary.");
					throw new UnprocessableEntityException("Only one telephone can be primary.");
				}
			} else {
				logger.info("Atleast one telephone must be primary");
				throw new UnprocessableEntityException("Atleast one telephone must be primary");
			}
		} else {
			logger.info("Atleast one telephone must be assigned on staff.");
			throw new UnprocessableEntityException("Atleast one telephone must be assigned on staff.");
		}
		logger.info("Returning after validate staff telephone");
	}

	/**
	 * To get list of all available staff members.
	 * 
	 * @return list of available StaffMemberDTO
	 */
	public List<StaffMemberDTO> getAvailableStaffMembers() {
		logger.info("Inside StaffMemberService :: getAvailableStaffMembers(), To find available staff-members");
		List<StaffMemberDTO> staffMembers = this.staffMemberRepository.findAll().stream()
				.filter(staff -> staff.isAvailable()).map(sf -> {
					StaffMemberDTO staffMemberDTO = new StaffMemberDTO(sf, Boolean.TRUE);
					return staffMemberDTO;
				}).collect(Collectors.toList());
		logger.info("Returning successfully from findStaffMembers,found staffMembers count:" + staffMembers.size());
		return staffMembers;
	}

}
