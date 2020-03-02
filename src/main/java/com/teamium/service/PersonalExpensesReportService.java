package com.teamium.service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.teamium.domain.ExpensesItem;
import com.teamium.domain.PersonalExpensesReport;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.projects.Project;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.dto.PersonalExpensesReportDTO;
import com.teamium.dto.ProjectDTO;
import com.teamium.enums.PersonalExpensesStatus;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.PersonalExpensesReportRepository;

/**
 * To handle all operations on PersonalExpensesReport
 * 
 * @author Nishant Chauhan
 *
 */
@Service
public class PersonalExpensesReportService {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	private PersonalExpensesReportRepository personalExpensesReportRepository;
	private AuthenticationService authenticationService;
	private RecordService recordService;
	private BookingService bookingService;
	private StaffMemberService staffMemberService;

	@Autowired
	public PersonalExpensesReportService(PersonalExpensesReportRepository personalExpensesReportRepository,
			AuthenticationService authenticationService, RecordService recordService,
			StaffMemberService staffMemberService, BookingService bookingService) {
		this.personalExpensesReportRepository = personalExpensesReportRepository;
		this.authenticationService = authenticationService;
		this.recordService = recordService;
		this.staffMemberService = staffMemberService;
		this.bookingService = bookingService;
	}

	/**
	 * To save or update personalExpensesReportDTO
	 * 
	 * @param personalExpensesReportDTO
	 * @return PersonalExpensesReportDTO
	 */
	public PersonalExpensesReportDTO saveOrUpdateExpensesReport(PersonalExpensesReportDTO personalExpensesReportDTO) {
		logger.info("Inside PersonalExpensesReportService :: saveOrUpdateExpensesReport(), To save or update :  "
				+ " : " + personalExpensesReportDTO);
		PersonalExpensesReport personalExpensesReportFromDb = null;

		if (personalExpensesReportDTO.getId() != null) {
			personalExpensesReportFromDb = findById(personalExpensesReportDTO.getId());
		}
		PersonalExpensesReport personalExpensesReport = validateExpensesReport(personalExpensesReportDTO);
		if (personalExpensesReportFromDb != null) {
			personalExpensesReport.setStatus(personalExpensesReportFromDb.getStatus());
		}

		try {
			personalExpensesReport = personalExpensesReportRepository.save(personalExpensesReport);
		} catch (DataIntegrityViolationException d) {
			logger.error("Expenses report already created for selected project and given personnel.");
			throw new UnprocessableEntityException(
					"Expenses report already created for selected project and given personnel.");
		}
		logger.info("Returning after saving PersonalExpensesReport.");
		return new PersonalExpensesReportDTO(personalExpensesReport);

	}

	/**
	 * To validate PersonalExpensesReportDTO
	 * 
	 * @param personalExpensesReportDTO
	 * @return PersonalExpensesReport
	 */
	private PersonalExpensesReport validateExpensesReport(PersonalExpensesReportDTO personalExpensesReportDTO) {
		logger.info("Inside PersonalExpensesReportService :: validateExpensesReport(), To validate :  " + " : "
				+ personalExpensesReportDTO);
		PersonalExpensesReport personalExpensesReport = new PersonalExpensesReport();
		if (personalExpensesReportDTO.getId() != null) {
			personalExpensesReport.setId(personalExpensesReportDTO.getId());
		} else {
			personalExpensesReport.setStatus(
					PersonalExpensesStatus.PersonalExpensesStatusName.CREATED.getPersonalExpensesStatusName());
		}
		StaffMember staffMember = null;
		Project project = null;
		if (personalExpensesReportDTO.getStaffMember() != null) {
			staffMember = personalExpensesReportDTO.getStaffMember().getId() != null
					? staffMemberService.findById(personalExpensesReportDTO.getStaffMember().getId())
					: null;
			if (staffMember == null) {
				logger.error("Invalid staff member id.");
				throw new UnprocessableEntityException("Invalid staff member id.");
			}

		} else {
			logger.error("StaffMember can not be null.");
			throw new UnprocessableEntityException("StaffMember can not be null.");

		}

		if (personalExpensesReportDTO.getProject() != null) {
			project = personalExpensesReportDTO.getProject().getId() != null
					? recordService.findProjectById(personalExpensesReportDTO.getProject().getId())
					: null;
			if (project == null) {
				logger.error("Invalid project id.");
				throw new UnprocessableEntityException("Invalid project id.");
			}

		} else {
			logger.error("Please provide a project.");
			throw new UnprocessableEntityException("Please provide a project.");

		}
		personalExpensesReport.setStaffMember(staffMember);
		personalExpensesReport.setProject(project);
		Set<ExpensesItem> expensesItems = personalExpensesReportDTO.getExpensesItems().stream()
				.map(dto -> new ExpensesItem(dto)).collect(Collectors.toSet());
		personalExpensesReport.setExpensesItems(expensesItems);

		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
		personalExpensesReport.setDate(cal);

		personalExpensesReport.setTotalCompanyCardExpenses(personalExpensesReportDTO.getExpensesItems().stream()
				.mapToDouble(item -> item.getCompanyCardExpense()).sum());
		personalExpensesReport.setTotalPersonalExpenses(personalExpensesReportDTO.getExpensesItems().stream()
				.mapToDouble(item -> item.getPersonalExpense()).sum());
		logger.error("Returning after validating PersonalExpensesReportDTO");
		return personalExpensesReport;

	}

	/**
	 * to get PersonalExpensesReport by id.
	 * 
	 * @param id
	 * @return
	 */
	public PersonalExpensesReport findById(long id) {
		logger.info("Inside PersonalExpensesReportService :: findById(" + id + ")");
		PersonalExpensesReport personalExpensesReport = personalExpensesReportRepository.findOne(id);
		if (personalExpensesReport == null) {
			logger.info("Inside PersonalExpensesReportService :: findById(" + id + ")");
			throw new NotFoundException("Invalid PersonalExpensesReport id.");
		}
		logger.info("Returning after geting PersonalExpensesReport.");
		return personalExpensesReport;
	}

	/**
	 * to get All Personal Expenses Reports.
	 * 
	 * @return List<PersonalExpensesReportDTO>
	 */
	public List<PersonalExpensesReportDTO> getAllExpensesReports() {
		logger.info("Inside PersonalExpensesReportService :: getAllExpensesReports()");
		return personalExpensesReportRepository.findAllExpensesReports();
	}

	/**
	 * To delete PersonalExpensesReport by id.
	 * 
	 * @param id
	 */
	public void deleteExpensesReportById(long id) {
		logger.info("Inside PersonalExpensesReportService :: deleteExpensesReportById(" + id + ")");
		findById(id);
		personalExpensesReportRepository.delete(id);
		logger.info("Returning after deleting PersonalExpensesReport.");
	}

	/**
	 * To get PersonalExpensesReportDTO by id.
	 * 
	 * @param id
	 * @return PersonalExpensesReportDTO
	 */
	public PersonalExpensesReportDTO getExpensesReportById(long id) {
		logger.info("Inside PersonalExpensesReportService :: getExpensesReportById(" + id + ")");
		PersonalExpensesReport personalExpensesReport = findById(id);
		logger.info("Returning after geting PersonalExpensesReport.");
		return new PersonalExpensesReportDTO(personalExpensesReport);
	}

	/**
	 * To get list of PersonalExpensesReportDTOs by staffmember id.
	 * 
	 * @param id
	 * @return List<PersonalExpensesReportDTO>
	 */
	public List<PersonalExpensesReportDTO> getExpensesReportByStaffMember(long id) {
		logger.info("Inside PersonalExpensesReportService :: getExpensesReportByStaffMember(" + id + ")");
		StaffMember staffMember = staffMemberService.findById(id);
		if (staffMember == null) {
			logger.error("Invalid staffmember.");
			throw new NotFoundException("Invalid staffmember.");
		}
		logger.info("Returning after geting PersonalExpensesReport by staffmember id.");
		return personalExpensesReportRepository.findByStaffMember(staffMember);
	}

	/**
	 * To get list of projects for expensesReport.
	 * 
	 * @return ProjectDTO
	 */
	public Set<ProjectDTO> getProjectsForExpensesReportByStaffMember(long staffMemberId) {
		logger.info("Inside PersonalExpensesReportService :: getProjectsForExpensesReport()");
		StaffMember staffMember = staffMemberService.findById(staffMemberId);
		if (staffMember == null) {
			logger.error("Invalid staffmember.");
			throw new NotFoundException("Invalid staffmember.");
		}
		List<String> projectStatus = Arrays.asList("To Do", "In Progress", "Done");

		List<Record> record = bookingService.findProjectsByBookingByResources(staffMember.getResource());
		Set<ProjectDTO> projectDTOs = record.stream().filter(p -> p instanceof Project)
				.map(project -> new ProjectDTO((Project) project, true)).filter(p -> {
					if (p.getFinancialStatus() != null && p.getFinancialStatus().equalsIgnoreCase("Approved")
							&& projectStatus.contains(p.getStatus())) {
						return true;
					} else {
						return false;
					}
				}).collect(Collectors.toSet());

		return projectDTOs;
	}

	/**
	 * To change status of PersonalExpenseReport
	 * 
	 * @param personalExpensesReportDTO
	 * @return PersonalExpensesReportDTO
	 */
	public PersonalExpensesReportDTO changePersonalExpenseReportStatus(
			PersonalExpensesReportDTO personalExpensesReportDTO) {
		logger.info("Inside PersonalExpensesReportService :: changePersonalExpenseReportStatus(), To change status :  "
				+ " : " + personalExpensesReportDTO);
		PersonalExpensesReport personalExpensesReportFromDb = null;

		if (personalExpensesReportDTO.getId() != null) {
			personalExpensesReportFromDb = findById(personalExpensesReportDTO.getId());
		} else {
			logger.error("Please provide personal expense report id.");
			throw new UnprocessableEntityException("Please provide personal expense report id.");
		}

		if (this.authenticationService.isManager()) {
			PersonalExpensesStatus.PersonalExpensesStatusName.getEnum(personalExpensesReportDTO.getStatus());
			personalExpensesReportFromDb.setStatus(personalExpensesReportDTO.getStatus());
			return new PersonalExpensesReportDTO(personalExpensesReportRepository.save(personalExpensesReportFromDb));
		} else {
			logger.error("You are not allowed to change status.");
			throw new UnprocessableEntityException("You are not allowed to change status.");
		}

	}

	/**
	 * To get Expenses report betwwen dates
	 * 
	 * @param personalExpensesReportDTO
	 * @return
	 */
	public List<PersonalExpensesReportDTO> findAllExpensesReportsForStaffBetween(
			PersonalExpensesReportDTO personalExpensesReportDTO) {
		if (personalExpensesReportDTO.getStaffMember() == null) {
			logger.error(
					"Inside PersonalExpensesReportService :: findAllExpensesReportsForStaffBetween:Invalid staffmember.");
			throw new NotFoundException("Invalid staffmember.");
		}
		logger.info("Inside PersonalExpensesReportService :: findAllExpensesReportsForStaffBetween("
				+ personalExpensesReportDTO.getStaffMember().getId() + ")");
		StaffMember staffMember = staffMemberService.findById(personalExpensesReportDTO.getStaffMember().getId());
		if (staffMember == null) {
			logger.error("Invalid staffmember.");
			throw new NotFoundException("Invalid staffmember.");
		}
		return personalExpensesReportRepository.findAllExpensesReportsBetween(personalExpensesReportDTO.getFrom(),
				personalExpensesReportDTO.getTo(), staffMember);
	}

}
