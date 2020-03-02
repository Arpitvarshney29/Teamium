package com.teamium.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teamium.dto.BookingConflictDTO;
import com.teamium.dto.BookingDTO;
import com.teamium.dto.DashboardDataDTO;
import com.teamium.dto.DashboardFunctionalWidgetData;
import com.teamium.dto.DateRangeDTO;
import com.teamium.dto.ProjectByStatusDTO;
import com.teamium.dto.WidgetDataDTO;
import com.teamium.service.DashboardService;

/**
 * <p>
 * Handles operations related to dashboard widgets
 * </p>
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

	private DashboardService dashboardService;

	/**
	 * @param dashboardService
	 */
	public DashboardController(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}

	/**
	 * To get project volume
	 * 
	 * Service URL: /dashboard/booking-volume method: GET
	 * 
	 * @return project-volume
	 */
	@GetMapping("/booking-volume")
	List<ProjectByStatusDTO> getProjectVolume(@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) {
		return dashboardService.getProjectVolume(startDate, endDate);
	}

	/**
	 * To export project volume spreadsheet
	 * 
	 * Service URL: /dashboard/booking-volume/export method: GET
	 * 
	 * @return project-volume spreadsheet
	 * 
	 * @throws IOException
	 */
	@GetMapping("/booking-volume/export")
	ProjectByStatusDTO exportProjectVolumeSpreadsheet(@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "status", required = true) String status) throws IOException {
		return dashboardService.exportProjectVolumeSpreadsheet(startDate, endDate, status);
	}

	/**
	 * To get project(booking) revenue
	 * 
	 * Service URL: /dashboard/project-revenue method: GET
	 * 
	 * @return project-revenue data
	 * 
	 * @throws IOException
	 */
	@GetMapping("/project-revenue")
	Map<String, Float> getProjectRevenue(@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) throws IOException {
		return dashboardService.getProjectRevenue(startDate, endDate);
	}

	/**
	 * To export project revenue spreadsheet
	 * 
	 * Service URL: /dashboard/project-revenue/export method: GET
	 * 
	 * @return project-volume spreadsheet
	 * 
	 * @throws IOException
	 */
	@GetMapping("/project-revenue/export")
	ProjectByStatusDTO exportProjectRevenueSpreadsheet(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) throws IOException {
		return dashboardService.exportProjectRevenueSpreadsheet(startDate, endDate);
	}

	/**
	 * To get project actual vs budget comparison
	 * 
	 * Service URL: /dashboard/actual-comparison method: GET.
	 * 
	 * @return list of project actual comparison
	 * 
	 * @throws IOException
	 */
	@GetMapping("/actual-comparison")
	List<ProjectByStatusDTO> getProjectActualVsBudget(@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) throws IOException {
		return dashboardService.getProjectActualVsBudget(startDate, endDate);
	}

	/**
	 * To export project actual vs budget spreadsheet
	 * 
	 * Service URL: /dashboard/actual-comparison/export method: GET
	 * 
	 * @return project actual comparison spreadsheet
	 * 
	 * @throws IOException
	 */
	@GetMapping("/actual-comparison/export")
	ProjectByStatusDTO exportProjectActualVsBudgetSpreadsheet(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "actualComparison", required = true) String actualComparison) throws IOException {
		return dashboardService.exportProjectActualVsBudgetSpreadsheet(startDate, endDate, actualComparison);
	}

	/**
	 * To get project budgeting
	 * 
	 * Service URL: /dashboard/project-budgeting method: GET.
	 * 
	 * @return list of project-budgeting
	 */
	@GetMapping("/project-budgeting")
	Map<String, Float> getProjectBudgeting(@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) {
		return dashboardService.getProjectBudgeting(startDate, endDate);
	}

	/**
	 * To export project budgeting spreadsheet
	 * 
	 * Service URL: /dashboard/project-budgeting/export method: GET.
	 * 
	 * @return project budgeting spreadsheet
	 */
	@GetMapping("/project-budgeting/export")
	ProjectByStatusDTO exportProjectBudgetingSpreadsheet(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) throws IOException {
		return dashboardService.exportProjectBudgetingSpreadsheet(startDate, endDate);
	}

	/**
	 * To get project actual
	 * 
	 * Service URL: /dashboard/project-actual method: GET.
	 * 
	 * @return the project-actual data
	 */
	@GetMapping("/project-actual")
	Map<String, Float> getProjectActual(@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) {
		return dashboardService.getProjectActual(startDate, endDate);
	}

	/**
	 * To get project actual spreadsheet
	 * 
	 * Service URL: /dashboard/project-actual/export method: GET.
	 * 
	 * @return the project-actual spreadsheet
	 * 
	 * @throws IOException
	 */
	@GetMapping("/project-actual/export")
	ProjectByStatusDTO exportProjectActualSpreadsheet(@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) throws IOException {
		return dashboardService.exportProjectActualSpreadsheet(startDate, endDate);
	}

	/**
	 * To get project actual spreadsheet
	 * 
	 * Service URL: /dashboard/project-bussiness-function method: GET.
	 * 
	 * @return the project business function data
	 * 
	 * @throws IOException
	 */
	@GetMapping("/project-bussiness-function")
	Map<String, Integer> getProjectBussinessFunctionByStartDateAndEndDate(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "resultCount", required = false) Integer resultCount) {
		return dashboardService.getProjectBussinessFunctionByStartDateAndEndDate(startDate, endDate, resultCount);
	}

	/**
	 * To export project bussiness function spreadsheet
	 * 
	 * Service URL: /dashboard/project-bussiness-function/export method: GET.
	 * 
	 * @return project bussiness function spreadsheet
	 * 
	 * @throws IOException
	 */
	@GetMapping("/project-bussiness-function/export")
	ProjectByStatusDTO exportProjectBussinessFunctionByStartDateAndEndDateSpreadsheet(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "resultCount", required = false) Integer resultCount) throws IOException {
		return dashboardService.exportProjectBussinessFunctionByStartDateAndEndDateSpreadsheet(startDate, endDate, resultCount);
	}

	/**
	 * To get functional uses and graph data for widget equipment and personnel
	 * 
	 * Service URL: /dashboard/functional-uses method: GET.
	 * 
	 * @return graph data for widget Equipment and personnel
	 * 
	 */
	@GetMapping("/functional-uses")
	DashboardFunctionalWidgetData getFunctionalUsesBystartDateAndendDate(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "functionType", required = true) String functionType) {
		return dashboardService.getGraphDataForWidgetEquipment(startDate, endDate, functionType);
	}

	/**
	 * To export widget equipment and personnel spreadsheet
	 * 
	 * Service URL: /dashboard/functional-uses/export method: GET.
	 * 
	 * @return widget equipment and personnel spreadsheet
	 * 
	 * @throws FileNotFoundException
	 */
	@GetMapping("/functional-uses/export")
	ProjectByStatusDTO exportWidgetPersonnalAndEquipmentSpreadSheet(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "functionType", required = true) String functionType) throws FileNotFoundException {
		return dashboardService.exportWidgetPersonnalAndEquipmentSpreadSheet(startDate, endDate, functionType);
	}

	/**
	 * To get booking conflicts in a date-range
	 * 
	 * Service URL: /dashboard/booking-conflict method: GET.
	 * 
	 * @return list of booking-conflicts
	 */
	@GetMapping("/booking-conflict")
	DashboardDataDTO getBookingConflicts(@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "all", required = true) boolean all) {
		return dashboardService.getBookingConflicts(startDate, endDate, all);
	}

	/**
	 * To export week time report spreadsheet
	 * 
	 * Service URL: /dashboard/week/time-report/export method: GET.
	 * 
	 * @return week time report spreadsheet
	 * 
	 * @throws FileNotFoundException
	 */
	@GetMapping("/week/time-report/export")
	ProjectByStatusDTO exportWeekTimeReportSpreadsheet(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "staffid", required = true) long staffMemberId) throws IOException {
		return dashboardService.exportWeekTimeReportSpreadsheet(startDate, endDate, staffMemberId);

	}

	/**
	 * To export annual personnel time report spreadsheet
	 * 
	 * Service URL: /dashboard/annual-personnel/time-report/export method: GET.
	 * 
	 * @return annual personnel time report spreadsheet
	 * 
	 * @throws FileNotFoundException
	 */
	@GetMapping("/annual-personnel/time-report/export")
	ProjectByStatusDTO exportAnnualPersonnelTimeReport(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "staffid", required = true) long staffMemberId) throws IOException {
		return dashboardService.exportAnnualPersonnelTimeReport(startDate, endDate, staffMemberId);

	}

	/**
	 * To export weekly personnel time report spreadsheet
	 * 
	 * Service URL: /dashboard/weekly-personnel/time-report/export method: GET.
	 * 
	 * @return annual personnel time report spreadsheet
	 * 
	 * @throws FileNotFoundException
	 */
	@GetMapping("/weekly-personnel/time-report/export")
	ProjectByStatusDTO exportWeeklyPersonnelTimeReport(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "staffid", required = true) long staffMemberId) throws IOException {
		return dashboardService.exportWeeklyPersonnelTimeReport(startDate, endDate, staffMemberId);

	}

	/**
	 * To get all Unscheduled booking
	 * 
	 * Service URL: /dashboard/unscheduled-booking method: GET.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param all
	 * 
	 * @return Unscheduled bookings
	 */
	@GetMapping("/unscheduled-booking")
	DashboardDataDTO getUnscheduledBooking(@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "all", required = true) boolean all) {
		return dashboardService.getUnscheduledBooking(startDate, endDate, all);
	}

}
