
package com.teamium.controller;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teamium.domain.prod.projects.Quotation;
import com.teamium.dto.BookingDTO;
import com.teamium.dto.LayoutDTO;
import com.teamium.dto.ProjectDTO;
import com.teamium.dto.ProjectDropDownDTO;
import com.teamium.dto.ProjectSchedulerDTO;
import com.teamium.dto.QuotationDTO;
import com.teamium.dto.RecordDTO;
import com.teamium.service.EditionService;
import com.teamium.service.RecordService;

/**
 * A controller class to create Project-Budget
 * 
 * @author Teamium
 *
 */
@RestController
@RequestMapping(value = "/budget")
public class ProjectBudgetController {

	private RecordService recordService;

	@Autowired
	private EditionService edtionService;

	@Autowired
	public ProjectBudgetController(RecordService recordService) {
		this.recordService = recordService;
	}

	/**
	 * To save and update Project-Budget (previously Quotation)
	 * 
	 * Service URL:/budget method: POST
	 * 
	 * @param quotationDTO the quotationDTO object.
	 * 
	 * @return the budget-quotation object
	 * 
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST)
	QuotationDTO saveOrUpdateBudget(@RequestBody QuotationDTO quotationDTO) throws Exception {
		return (QuotationDTO) recordService.saveOrUpdateRecord(quotationDTO, QuotationDTO.class);
	}

	/**
	 * Method to get Project-Budgets
	 * 
	 * Service URL:/budget method: GET
	 * 
	 * @return List of Budget-Quotations
	 */
	@RequestMapping(method = RequestMethod.GET)
	List<RecordDTO> getBudgets() {
		return recordService.getRecords(Quotation.class);
	}

	/**
	 * Method to get Budget by id
	 * 
	 * Service URL:/budget/{budgetId} method: GET
	 * 
	 * @param budgetId the budgetId
	 * 
	 * @return the QuotationDTO
	 */
	@RequestMapping(value = "/{budgetId}", method = RequestMethod.GET)
	QuotationDTO findBudgetById(@PathVariable long budgetId) {
		return (QuotationDTO) recordService.findRecordById(Quotation.class, budgetId);
	}

	/**
	 * Method to delete Quotation by Id
	 * 
	 * Service URL:/budget/{budgetId} method: DELETE
	 * 
	 * @param budgetId the budgetId
	 */
	@RequestMapping(value = "/{budgetId}", method = RequestMethod.DELETE)
	void deleteBudgetById(@PathVariable long budgetId) {
		recordService.deleteRecordById(Quotation.class, budgetId);
	}

	/**
	 * Method to get all budgets by status
	 * 
	 * Service URL : /budget/status method: DELETE
	 * 
	 * @return a HashMap related to status and records
	 */
	@RequestMapping(value = "/status", method = RequestMethod.GET)
	Map<String, List<RecordDTO>> findBudgetByStatus() {
		return recordService.findAllRecordsByStatus(Quotation.class);
	}

	/**
	 * Method to get recent viewed records
	 * 
	 * Service URL : /budget/recent/{count} method: GET
	 * 
	 * @param count the count
	 * 
	 * @return List of recent viewed budgets
	 */
	@RequestMapping(value = "/recent/{count}", method = RequestMethod.GET)
	List<RecordDTO> getRecentViewedBudgets(@PathVariable long count) {
		return recordService.getRecentViewedRecords(Quotation.class, count);
	}

	/**
	 * To get project drop-down data
	 * 
	 * Service URL:/budget/dropdown , method: GET.
	 * 
	 * @return ProjectDropDownDTO object
	 * 
	 */
	@GetMapping("/dropdown")
	public ProjectDropDownDTO getProjectDropdown() {
		return recordService.getProjectDropdown();
	}

	/**
	 * To change project status.
	 * 
	 * Service URL:/budget/change/status , method: Post.
	 * 
	 * @param projectDTO
	 */
	@PostMapping("/change/status")
	void changeRecordSatus(@RequestBody ProjectDTO projectDTO) {
		recordService.changeProjectStatus(projectDTO.getId(), projectDTO.getStatus());
	}

	/**
	 * Method to apply date on all budget-lines
	 * 
	 * @param budgetId   the budgetId
	 * 
	 * @param bookingDTO the bookingDTO
	 * 
	 * @return budget
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/apply-date-to-all/{budgetId}", method = RequestMethod.POST)
	QuotationDTO applyDateToAllBudgetLines(@PathVariable long budgetId, @RequestBody BookingDTO bookingDTO)
			throws Exception {
		return (QuotationDTO) recordService.applyDateToAllBudgetLines(budgetId, bookingDTO, Quotation.class);
	}

	/**
	 * Method to save a budget-line
	 * 
	 * Service URL : budget/line/{budgetId}, method: POST
	 * 
	 * @return Budget-Quotation
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/line/{budgetId}", method = RequestMethod.POST)
	QuotationDTO saveBudgetLine(@PathVariable long budgetId, @RequestBody BookingDTO bookingDTO) throws Exception {
		return (QuotationDTO) recordService.saveBudgetLine(budgetId, bookingDTO, Quotation.class);
	}

	/**
	 * Method to delete budget line
	 * 
	 * Service URL : budget/line/{lineId}, method: DELETE
	 * 
	 * @param budgetId   the budgetId
	 * 
	 * @param bookingDTO the bookingDTO
	 * 
	 * @return Budget-Quotation
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/line/{lineId}", method = RequestMethod.DELETE)
	QuotationDTO deleteBudgetLine(@PathVariable long lineId) throws Exception {
		return (QuotationDTO) recordService.deleteBudgetLine(lineId, Quotation.class);
	}

	/**
	 * Method to save layout-template
	 * 
	 * Service URL : /budget/template/{budgetId} method : POST
	 * 
	 * @param layoutDTO
	 * 
	 * @return the budget-quotation
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/template/{budgetId}", method = RequestMethod.POST)
	QuotationDTO createTemplate(@PathVariable long budgetId, @RequestBody LayoutDTO layoutDTO) throws Exception {
		return (QuotationDTO) recordService.saveOrUpdateTemplate(budgetId, layoutDTO, Quotation.class);
	}

	/**
	 * Method to apply the template on budget
	 * 
	 * Service URL : /budget/apply/template/{projectId}/{templateId} method : POST
	 * 
	 * @param budgetId
	 * @param templateId
	 * 
	 * @return the budget object
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/apply/template/{budgetId}/{templateId}", method = RequestMethod.POST)
	QuotationDTO applyTemplate(@PathVariable long budgetId, @PathVariable long templateId) throws Exception {
		return (QuotationDTO) recordService.applyTemplate(budgetId, templateId, Quotation.class);
	}

	/**
	 * Method to get all project event.
	 * 
	 * @return list of all project event.
	 */
	@GetMapping(value = "/event")
	List<ProjectSchedulerDTO> getScheduledProject() {
		return recordService.getScheduledProject();
	}

	/**
	 * save budget from project scheduler.
	 * 
	 * @param quotationDTO
	 * @return object of ProjectSchedulerDTO.
	 * @throws MalformedURLException
	 */
	@PostMapping(value = "/event")
	ProjectSchedulerDTO saveBugdetFromEvent(@RequestBody QuotationDTO quotationDTO) throws MalformedURLException {
		return recordService.saveBugdetFromEvent(quotationDTO);
	}

	/**
	 * Method to get pdf by id
	 * 
	 * Service URL:/budget/pdf method: GET
	 * 
	 * @param budgetId  the budgetId
	 * 
	 * @param contactId the contactId
	 */
	@RequestMapping(value = "/pdf", method = RequestMethod.GET)
	QuotationDTO getBudgetPdf(@RequestParam long budgetId, @RequestParam long contactId,
			@RequestParam(value = "", required = false) String terms) {
		return edtionService.getBudgetPdf(budgetId, contactId, terms);
	}

	/**
	 * Method to get recently updated bookings within specified time period in
	 * minutes. Discriminator should be 'project'(mandatory) and minutes is not
	 * mandatory. By default bookings updated within 60 minutes are given.
	 * 
	 * Service URL:/recently-updated method: GET
	 * 
	 * @return List of bookings
	 */
	@GetMapping(value = "/recently-updated")
	List<RecordDTO> getRecentlyUpdatedRecords(
			@RequestParam(value = "discriminator", required = true) String discriminator,
			@RequestParam(value = "minutes", required = false) Integer minutes) {
		return recordService.getRecentlyUpdatedRecords(discriminator, minutes);
	}

}
