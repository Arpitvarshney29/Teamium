package com.teamium.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamium.dto.PersonalExpensesReportDTO;
import com.teamium.dto.ProjectDTO;
import com.teamium.service.EditionService;
import com.teamium.service.PersonalExpensesReportService;

@RestController
@RequestMapping("perosnal-expenses-report")
public class PerosnalExpensesReportController {
	
	@Autowired
	private EditionService edtionService;

	private PersonalExpensesReportService personalExpensesReportService;

	@Autowired
	public PerosnalExpensesReportController(PersonalExpensesReportService personalExpensesReportService) {
		this.personalExpensesReportService = personalExpensesReportService;
	}

	/**
	 * To get save or update PersonalExpensesReportDTO.
	 * 
	 * Service URL: /expenses-report method: POST.
	 * 
	 * @param personalExpensesReportDTO
	 * @return PersonalExpensesReportDTO
	 */
	@PostMapping
	PersonalExpensesReportDTO saveOrUpdateExpensesReport(
			@RequestBody PersonalExpensesReportDTO personalExpensesReportDTO) {
		return personalExpensesReportService.saveOrUpdateExpensesReport(personalExpensesReportDTO);
	}

	/**
	 * To get list of PersonalExpensesReportDTO.
	 * 
	 * Service URL: /expenses-report method: GET.
	 * 
	 * @return List<PersonalExpensesReportDTO>
	 */
	@GetMapping
	List<PersonalExpensesReportDTO> getPersonalExpensesReports() {
		return personalExpensesReportService.getAllExpensesReports();
	}

	/**
	 * To delete PersonalExpensesReport by id.
	 * 
	 * Service URL: /expenses-report/{id} method: DELETE.
	 * 
	 * @param id
	 */
	@DeleteMapping("/{id}")
	void deletePersonalExpensesReport(@PathVariable("id") long id) {
		personalExpensesReportService.deleteExpensesReportById(id);
	}

	/**
	 * To get PersonalExpensesReport by id.
	 * 
	 * Service URL: /expenses-report/{id} method: GET.
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	PersonalExpensesReportDTO getPersonalExpensesReportById(@PathVariable("id") long id) {
		return personalExpensesReportService.getExpensesReportById(id);
	}

	/**
	 * To get list of PersonalExpensesReport by staff id.
	 * 
	 * Service URL: /expenses-report/by-staff-member/{id} method: GET.
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/by-staff-member/{id}")
	List<PersonalExpensesReportDTO> getPersonalExpensesReportByStaffMember(@PathVariable("id") long id) {
		return personalExpensesReportService.getExpensesReportByStaffMember(id);
	}

	/**
	 * To get list of projects for personal-expenses-reports.
	 * 
	 * Service URL: /expenses-report/projects method: GET.
	 * 
	 * @return
	 */
	@GetMapping("/projects/by-staff-member/{staffMemberId}")
	Set<ProjectDTO> getProjectsForExpensesReport(@PathVariable("staffMemberId") long staffMemberId) {
		return personalExpensesReportService.getProjectsForExpensesReportByStaffMember(staffMemberId);
	}

	/**
	 * To get list of projects for personal-expenses-reports.
	 * 
	 * Service URL: /expenses-report/change-status method: POST.
	 * 
	 * @param personalExpensesReportDTO
	 * @return
	 */
	@PostMapping("/change-status")
	PersonalExpensesReportDTO changePersonalExpenseReportStatus(
			@RequestBody PersonalExpensesReportDTO personalExpensesReportDTO) {
		return personalExpensesReportService.changePersonalExpenseReportStatus(personalExpensesReportDTO);
	}

	/**
	 * To get list of projects for personal-expenses-reports.
	 * 
	 * Service URL: /expenses-report/change-status method: POST.
	 * 
	 * @param personalExpensesReportDTO
	 * @return
	 */
	@PostMapping("/by-staff/between")
	List<PersonalExpensesReportDTO> findAllExpensesReportsForStaffBetween(
			@RequestBody PersonalExpensesReportDTO personalExpensesReportDTO) {
		return personalExpensesReportService.findAllExpensesReportsForStaffBetween(personalExpensesReportDTO);
	}

	/**
	 * To get list of projects for personal-expenses-reports.
	 * 
	 * Service URL: /expenses-report/change-status method: POST.
	 * 
	 * @param personalExpensesReportDTO
	 * @return
	 */
	@GetMapping("/pdf/expensereport/{personnelExpenseReportId}")
	public Map<String, String> genratePersonalExpensesReport(@PathVariable long personnelExpenseReportId) {
		return edtionService.getPersonalExpensesReport(personnelExpenseReportId);
	}

}
