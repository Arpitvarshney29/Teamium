
package com.teamium.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teamium.domain.prod.projects.Program;
import com.teamium.domain.prod.projects.Project;
import com.teamium.domain.prod.projects.Quotation;
import com.teamium.dto.BookingDTO;
import com.teamium.dto.LayoutDTO;
import com.teamium.dto.ProgramDTO;
import com.teamium.dto.ProgramSchedulerDTO;
import com.teamium.dto.ProjectDTO;
import com.teamium.dto.QuotationDTO;
import com.teamium.dto.RecordDTO;
import com.teamium.service.EditionService;
import com.teamium.service.RecordService;

@RestController
@RequestMapping(value = "/show")
public class ProgramController {

	private RecordService recordService;

	private EditionService editionService;

	/**
	 * @param recordService
	 */
	@Autowired
	public ProgramController(RecordService recordService, EditionService editionService) {
		this.recordService = recordService;
		this.editionService = editionService;
	}

	/**
	 * To save and update Program-Show (previously Program)
	 * 
	 * Service URL:/show method: POST
	 * 
	 * @param programDTO the programDTO object.
	 * 
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST)
	ProgramDTO saveOrUpdateProgramShow(@RequestBody ProgramDTO programDTO) throws Exception {
		return (ProgramDTO) recordService.saveOrUpdateRecord(programDTO, ProgramDTO.class);
	}

	/**
	 * Method to get Program-Show by id
	 * 
	 * Service URL:/show/{programId} method: GET
	 * 
	 * @param programId the programId
	 * 
	 * @return the ProgramDTO
	 */
	@RequestMapping(value = "/{programId}", method = RequestMethod.GET)
	ProgramDTO findProgramShowById(@PathVariable long programId) {
		return (ProgramDTO) recordService.findRecordById(Program.class, programId);
	}

	/**
	 * Method to get all Programs-Shows by status
	 * 
	 * Service URL : /show/status, method: GET
	 * 
	 * @return a HashMap related to status and program-shows
	 */
	@RequestMapping(value = "/status", method = RequestMethod.GET)
	Map<String, List<RecordDTO>> findProgramShowByStatus() {
		return recordService.findAllRecordsByStatus(Program.class);
	}

	/**
	 * Method to get recent viewed Programs-Shows
	 * 
	 * Service URL : /show/recent/{count} method: GET
	 * 
	 * @param recent viewed program-shows count
	 * 
	 * @return List of recent viewed Program-Shows
	 */
	@RequestMapping(value = "/recent/{count}", method = RequestMethod.GET)
	List<RecordDTO> getRecentViewedProgramShows(@PathVariable long count) {
		return recordService.getRecentViewedRecords(Program.class, count);
	}

	/**
	 * Method to save a budget-line on program-show
	 * 
	 * Service URL : show/line/{showId}, method: POST
	 * 
	 * @return Program-Show
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/line/{showId}", method = RequestMethod.POST)
	ProgramDTO saveBudgetLine(@PathVariable long showId, @RequestBody BookingDTO bookingDTO) throws Exception {
		return (ProgramDTO) recordService.saveProgramLine(showId, bookingDTO, Program.class);
	}

	/**
	 * Method to delete budget-line on program-show
	 * 
	 * Service URL : show/line/{lineId}, method: DELETE
	 * 
	 * @param showId     the showId
	 * 
	 * @param bookingDTO the bookingDTO
	 * 
	 * @return Program-Show
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/line/{lineId}", method = RequestMethod.DELETE)
	ProgramDTO deleteBudgetLine(@PathVariable long lineId) throws Exception {
		return (ProgramDTO) recordService.deleteBudgetLine(lineId, Program.class);
	}

	/**
	 * Method to get create all sessions
	 * 
	 * Service URL : /show/session/all/{programId} method: POST
	 * 
	 * @param programId
	 * 
	 * @return Program wrapper object
	 */
	@RequestMapping(value = "/session/all/{programId}", method = RequestMethod.POST)
	ProgramDTO createAllSessions(@PathVariable long programId) {
		return recordService.createAllSessions(programId);
	}

	/**
	 * Method to get create a single session
	 * 
	 * Service URL : /show/session/{programId} method: POST
	 * 
	 * @param programId
	 * 
	 * @return Program wrapper object
	 */
	@RequestMapping(value = "/session/{programId}", method = RequestMethod.POST)
	ProgramDTO createSingleSession(@PathVariable long programId) {
		return recordService.createSingleSession(programId);
	}

	/**
	 * Get of all project related to program id.
	 * 
	 * @param programId
	 * @return object of ProgramSchedulerDTO.
	 */
	@RequestMapping(value = "/event/{programId}", method = RequestMethod.GET)
	ProgramSchedulerDTO getAllProgramEvent(@PathVariable long programId) {
		return recordService.getAllProgramEvent(programId);
	}

	/**
	 * Method to apply date on all budget-lines
	 * 
	 * @param budgetId   the budgetId
	 * 
	 * @param bookingDTO the bookingDTO
	 * 
	 * @return program object
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/apply-date-to-all/{programId}", method = RequestMethod.POST)
	ProgramDTO applyDateToAllBudgetLines(@PathVariable long programId, @RequestBody BookingDTO bookingDTO)
			throws Exception {
		return (ProgramDTO) recordService.applyDateToAllBudgetLines(programId, bookingDTO, Program.class);
	}

	/**
	 * Method to delete program-show by Id
	 * 
	 * Service URL:/show/{programId} method: DELETE
	 * 
	 * @param programId the programId
	 */
	@RequestMapping(value = "/{programId}", method = RequestMethod.DELETE)
	void deleteBudgetById(@PathVariable long programId) {
		recordService.deleteRecordById(Program.class, programId);
	}

	/**
	 * Method to get pdf by id
	 * 
	 * Service URL:/show/budget/pdf method: GET
	 * 
	 * @param budgetId  the budgetId
	 * 
	 * @param contactId the contactId
	 */
	@RequestMapping(value = "/budget/pdf", method = RequestMethod.GET)
	QuotationDTO getBudgetPdf(@RequestParam long showId, @RequestParam long contactId,
			@RequestParam(value = "", required = false) String comment) {
		return editionService.getShowBudgetPdf(showId, contactId,comment);
	}
	/**
	 * Method to save or Update layout-template
	 * 
	 * Service URL : /show/template/{projectId} method : POST
	 * 
	 * @param layoutDTO
	 * 
	 * @return the booking-program
	 * 
	 * @throws Exception
	 */
	@PostMapping(value = "/template/{programId}")
	ProgramDTO createTemplate(@PathVariable long programId, @RequestBody LayoutDTO layoutDTO) throws Exception {
		return (ProgramDTO) recordService.saveOrUpdateTemplate(programId, layoutDTO, Program.class);
	}
	
	/**
	 * Method to apply the template on show-budget
	 * 
	 * Service URL : /budget/apply/template/{programId}/{templateId} method : POST
	 * 
	 * @param programId
	 * 
	 * @param templateId
	 * 
	 * @return the show-budget object
	 * 
	 * @throws Exception
	 */
	@PostMapping(value = "/apply/template/{programId}/{templateId}")
	ProgramDTO applyTemplate(@PathVariable long programId, @PathVariable long templateId) throws Exception {
		return (ProgramDTO) recordService.applyTemplate(programId, templateId, Program.class);
	}

}
