package com.teamium.controller;

import java.io.IOException;
import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teamium.domain.prod.projects.Project;
import com.teamium.dto.ATACarnetReportDTO;
import com.teamium.dto.BookingDTO;
import com.teamium.dto.CallsheetDTO;
import com.teamium.dto.FinancialDataDTO;
import com.teamium.dto.LayoutDTO;
import com.teamium.dto.ProjectByStatusDTO;
import com.teamium.dto.ProjectDTO;
import com.teamium.dto.ProjectFinancialDTO;
import com.teamium.dto.QuotationDTO;
import com.teamium.dto.RecordDTO;
import com.teamium.service.BookingService;
import com.teamium.service.EditionService;
import com.teamium.service.RecordService;


@RestController
@RequestMapping(value = "/project")
public class ProjectController {

	private RecordService recordService;
	private BookingService bookingService;
	@Autowired
	private EditionService edtionService;

	/**
	 * @param recordService
	 * @param bookingService
	 */
	@Autowired
	public ProjectController(RecordService recordService, BookingService bookingService) {
		this.recordService = recordService;
		this.bookingService = bookingService;
	}

	/**
	 * To save and update Project
	 * 
	 * Service URL:/project method: POST
	 * 
	 * @param projectDTO
	 *            the ProjectDTO object.
	 * 
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST)
	ProjectDTO saveOrUpdateProject(@RequestBody ProjectDTO projectDTO) throws Exception {
		return (ProjectDTO) recordService.saveOrUpdateRecord(projectDTO, ProjectDTO.class);
	}

	/**
	 * Method to get Projects
	 * 
	 * Service URL:/project method: GET
	 * 
	 * @return List of Projects
	 */
	@RequestMapping(method = RequestMethod.GET)
	List<RecordDTO> getProjects() {
		return recordService.getRecords(Project.class);
	}


	
	
	/**
	 * Method to get Project by id
	 * 
	 * Service URL:/project/{projectId} method: GET
	 * 
	 * @param projectId
	 *            the projectId
	 * 
	 * @return the ProjectDTO
	 */
	@RequestMapping(value = "/{projectId}", method = RequestMethod.GET)
	ProjectDTO findProjectById(@PathVariable long projectId) {
		return (ProjectDTO) recordService.findRecordById(Project.class, projectId);
	}

	/**
	 * Method to delete Project by Id
	 * 
	 * Service URL:/project/{projectId} method: DELETE
	 * 
	 * @param projectId
	 *            the projectId
	 */
	@RequestMapping(value = "/{projectId}", method = RequestMethod.DELETE)
	void deleteProjectById(@PathVariable long projectId) {
		recordService.deleteRecordById(Project.class, projectId);
	}

	/**
	 * To add or get project bookings
	 * 
	 * Service URL: /project/bookings method: POST.
	 * 
	 * @return the list of BookingDTO
	 */
	@PostMapping("/booking")
	ProjectDTO addBookingEventByProject(@RequestBody ProjectDTO projectDTO) {
		return bookingService.assignBookingEventByProject(projectDTO);
	}

	/**
	 * To get project by quotation
	 * 
	 * Service URL: /project/booking/{quotationId} method: Get.
	 * 
	 * @return the ProjectDTO
	 */
	@GetMapping("/booking/{quotationId}")
	ProjectDTO getProjectByQuotation(@PathVariable long quotationId) {
		return recordService.getProjectByQuotationId(quotationId);
	}

	/**
	 * Method to get recent viewed records
	 * 
	 * Service URL : /budget/recent/{count} method: GET
	 * 
	 * @param count
	 *            the count
	 * 
	 * @return List of recent viewed budgets
	 */
	@RequestMapping(value = "/recent/{count}", method = RequestMethod.GET)
	List<RecordDTO> getRecentViewedBudgets(@PathVariable long count) {
		return recordService.getRecentViewedRecords(Project.class, count);
	}

	/**
	 * Method to basic info of projects
	 * 
	 * Service URL : /info method: GET
	 * 
	 * @return List of projects
	 */
	@GetMapping("/info")
	List<ProjectDTO> getProjectBaiscDetails() {
		return recordService.getProjectsBasicDetails();
	}

	/**
	 * Method to basic info of projects
	 * 
	 * Service URL : project/budget-id/{projectId} method: GET
	 * 
	 * @return budget id
	 */
	@GetMapping("/budget-id/{projectId}")
	public long getBudgetIdByProject(@PathVariable long projectId) {
		return recordService.getBudgetIdByProject(projectId);
	}

	/**
	 * Method to apply date on all booking-lines
	 * 
	 * @param budgetId
	 *            the budgetId
	 * 
	 * @param bookingDTO
	 *            the bookingDTO
	 * 
	 * @return booking
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/apply-date-to-all/{bookingId}", method = RequestMethod.POST)
	ProjectDTO applyDateToAllBudgetLines(@PathVariable long bookingId, @RequestBody BookingDTO bookingDTO)
			throws Exception {
		return (ProjectDTO) recordService.applyDateToAllBudgetLines(bookingId, bookingDTO, Project.class);
	}

	/**
	 * Method to save a booking-line
	 * 
	 * Service URL : project/line/{projectId}, method: POST
	 * 
	 * @return Booking-Project
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/line/{projectId}", method = RequestMethod.POST)
	ProjectDTO saveBudgetLine(@PathVariable long projectId, @RequestBody BookingDTO bookingDTO) throws Exception {
		return (ProjectDTO) recordService.saveBudgetLine(projectId, bookingDTO, Project.class);
	}

	/**
	 * Method to delete booking line
	 * 
	 * Service URL : project/line/{lineId}, method: DELETE
	 * 
	 * @param budgetId
	 *            the budgetId
	 * 
	 * @return Booking-Project
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/line/{lineId}", method = RequestMethod.DELETE)
	ProjectDTO deleteBudgetLine(@PathVariable long lineId) throws Exception {
		return (ProjectDTO) recordService.deleteBudgetLine(lineId, Project.class);
	}

	/**
	 * Method to save layout-template
	 * 
	 * Service URL : /project/template/{projectId} method : POST
	 * 
	 * @param layoutDTO
	 * 
	 * @return the booking-project
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/template/{projectId}", method = RequestMethod.POST)
	ProjectDTO createTemplate(@PathVariable long projectId, @RequestBody LayoutDTO layoutDTO) throws Exception {
		return (ProjectDTO) recordService.saveOrUpdateTemplate(projectId, layoutDTO, Project.class);
	}

	/**
	 * Method to apply the template on booking-project
	 * 
	 * Service URL : /project/apply/template/{projectId}/{templateId} method : POST
	 * 
	 * @param projectId
	 * @param templateId
	 * 
	 * @return the booking-project object
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/apply/template/{projectId}/{templateId}", method = RequestMethod.POST)
	ProjectDTO applyTemplate(@PathVariable long projectId, @PathVariable long templateId) throws Exception {
		return (ProjectDTO) recordService.applyTemplate(projectId, templateId, Project.class);
	}

	/**
	 * Method to get the unscheduled bookings on project
	 * 
	 * Service URL : /project/unschedule-bookings/{projectId}/ method : GET
	 * 
	 * @param projectId
	 * 
	 * @return list of unscheduled bookings
	 */
	@RequestMapping(value = "/unschedule-bookings/{projectId}", method = RequestMethod.GET)
	List<BookingDTO> getUnscheduledBookings(@PathVariable long projectId) {
		return recordService.getUnscheduledBookings(Project.class, projectId);
	}

	/**
	 * Method to get Project procurement details by projectId
	 * 
	 * Service URL:/project/procurment/{projectId} method: GET
	 * 
	 * @param projectId
	 *            the projectId
	 * 
	 * @return the ProjectDTO
	 */
	@RequestMapping(value = "/procurment/{projectId}", method = RequestMethod.GET)
	ProjectDTO getProcurmentByProjectId(@PathVariable long projectId) {
		return (ProjectDTO) recordService.getProcurmentByProjectId(projectId, Project.class);
	}

	/**
	 * Method to get callsheet pdf by project id
	 * 
	 * Service URL:/project/pdf/callsheet/ method: GET
	 * 
	 * @param projectId
	 * @param contactId
	 * @param orgnization
	 * @param location
	 * @param comment
	 * @return QuotationDTO
	 */
	@RequestMapping(value = "/pdf/callsheet", method = RequestMethod.GET)
	QuotationDTO getBudgetCallsheetPdf(@RequestParam long projectId,
			@RequestParam(value = "", required = false) long contactId,
			@RequestParam(value = "", required = false) String orgnization,
			@RequestParam(value = "", required = false) String location,
			@RequestParam(value = "", required = false) String comment) {
		return edtionService.getProjectCallSheetPdf(projectId, contactId, orgnization, comment, location);
	}

	/**
	 * To generate callsheet pdf
	 * 
	 * @param callsheet
	 * @return document information
	 */
	@RequestMapping(value = "/pdf/packing", method = RequestMethod.POST)
	QuotationDTO generatePackingPdf(@RequestBody CallsheetDTO callsheet) {
 		return edtionService.getProjectPackingPdf(callsheet);
	}

	/**
	 * To generate callsheet pdf.
	 * 
	 * Service URL:/project/pdf/callsheet method: POST
	 * 
	 * @param callsheet
	 * 
	 * @return document information
	 */
	@RequestMapping(value = "/pdf/callsheet", method = RequestMethod.POST)
	QuotationDTO generateCallsheetPdf(@RequestBody CallsheetDTO callsheet) {
		return edtionService.getProjectCallSheetPdf(callsheet);
	}

	/**
	 * Method to get packing list pdf by id
	 * 
	 * Service URL:/project/pdf/production method: GET
	 * 
	 * @param budgetId
	 *            the projectId
	 * 
	 * @param contactId
	 *            the contactId
	 */
	@RequestMapping(value = "/pdf/production", method = RequestMethod.GET)
	QuotationDTO getProductionPdf(@RequestParam long projectId, @RequestParam long contactId,
			@RequestParam(value = "", required = false) String comments) {
		return edtionService.getProductionStatementPdf(projectId, contactId, comments);
	}

	/**
	 * Method to get all budgets by status
	 * 
	 * Service URL : /budget/status method: DELETE
	 * 
	 * @return a HashMap related to status and records
	 */
	@RequestMapping(value = "/progress", method = RequestMethod.GET)
	List<RecordDTO> getRecordsByTodoORProgress() {
		return recordService.getRecordsByTodoORProgress();
	}

	/**
	 * Method to get package pdf
	 * 
	 * Service URL : /budget/pdf/package method: POST
	 * 
	 * @param equipmentPackage
	 * 
	 * @return the budget wrapper object
	 */
	@RequestMapping(value = "/pdf/package", method = RequestMethod.POST)
	QuotationDTO generatePackagePdf(@RequestBody CallsheetDTO equipmentPackage) {
 		return edtionService.getProjectPackagePdf(equipmentPackage);
	}


	/**
	 * To export ATA carnet spreadsheet
	 * 
	 * Service URL : /project/ata-report/export/{projectId} method: GET
	 * 
	 * @param projectId
	 * @param sheetLanguage
	 * 
	 * @return ProjectByStatusDTO
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/ata-report/export/{projectId}", method = RequestMethod.GET)
	ProjectByStatusDTO getATACarnetReportExport(@PathVariable long projectId,
			@RequestParam(value = "sheetLanguage", required = true) String sheetLanguage)
			throws IOException, ParseException {
		return recordService.getATACarnetReportExport(projectId, sheetLanguage);
	}

	/**
	 * To get ATA carnet spreadsheet data
	 * 
	 * Service URL : /project/ata-report/{projectId} method: GET
	 * 
	 * @param projectId
	 * 
	 * @return ProjectByStatusDTO
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/ata-report/{projectId}", method = RequestMethod.GET)
	List<ATACarnetReportDTO> getATACarnetReportData(@PathVariable long projectId) throws IOException, ParseException {
		return recordService.getATACarnetReportData(projectId);
	}
	
	/**
	 * To get Project Financial detail
	 * 
	 * Service URL : /project/financial/{quotationId} method: GET
	 * 
	 * @param quotationId
	 * 
	 * @return FinancialDataDTO
	 */
	@RequestMapping(value = "/financial/{quotationId}", method = RequestMethod.GET)
	FinancialDataDTO getProjectFinancialDetail(@PathVariable long quotationId) {
		return recordService.getProjectFinancialDetail(quotationId);
	}

}

