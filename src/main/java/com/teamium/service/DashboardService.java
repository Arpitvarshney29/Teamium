package com.teamium.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.Weeks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.AtomicDouble;
import com.teamium.constants.Constants;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.projects.AbstractProject;
import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.projects.Project;
import com.teamium.domain.prod.projects.Quotation;
import com.teamium.domain.prod.projects.order.OrderLine;
import com.teamium.domain.prod.resources.equipments.EquipmentResource;
import com.teamium.domain.prod.resources.functions.DefaultResource;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.domain.prod.resources.staff.StaffResource;
import com.teamium.domain.prod.resources.staff.contract.ContractSetting;
import com.teamium.domain.prod.resources.staff.contract.EntertainmentContractSetting;
import com.teamium.dto.BookingConflictDTO;
import com.teamium.dto.BookingEventDTO;
import com.teamium.dto.DashboardDataDTO;
import com.teamium.dto.DashboardFunctionalWidgetData;
import com.teamium.dto.DateRangeDTO;
import com.teamium.dto.EventDTO;
import com.teamium.dto.ProjectByStatusDTO;
import com.teamium.dto.QuotationDTO;
import com.teamium.dto.RecordDTO;
import com.teamium.dto.ReportDTO;
import com.teamium.dto.SpreadsheetDataDTO;
import com.teamium.dto.TimeReportDTO;
import com.teamium.dto.WidgetDataDTO;
import com.teamium.enums.ContractType;
import com.teamium.enums.ProjectStatus.ProjectFinancialStatusName;
import com.teamium.enums.ProjectStatus.ProjectStatusName;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.BookingRepository;
import com.teamium.repository.OrderLineService;
import com.teamium.utils.CommonUtil;

/**
 * A service class implementation to manage dashboard widgets and spreadsheet
 * imports
 * 
 * @author Teamium
 *
 */
@Service
public class DashboardService {

	private AuthenticationService authenticationService;
	private RecordService recordService;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private BookingService bookingService;
	private EquipmentService equipmentService;
	private StaffMemberService staffMemberService;
	private OrderLineService orderLineService;
	private BookingRepository bookingRepository;

	@Autowired
	@Lazy
	private EventService eventService;

	@Autowired
	public DashboardService(AuthenticationService authenticationService, RecordService recordService,
			BookingService bookingService, EquipmentService equipmentService, OrderLineService orderLineService,
			StaffMemberService staffMemberService, BookingRepository bookingRepository) {
		this.authenticationService = authenticationService;
		this.recordService = recordService;
		this.bookingService = bookingService;
		this.equipmentService = equipmentService;
		this.orderLineService = orderLineService;
		this.staffMemberService = staffMemberService;
		this.bookingRepository = bookingRepository;
	}

	/**
	 * Method to get project volume in a particular date-range
	 * 
	 * @param start
	 *            the start date
	 * @param end
	 *            the end date
	 * 
	 * @return the list of project volume
	 */
	public List<ProjectByStatusDTO> getProjectVolume(String start, String end) {
		logger.info("Inside DashboardService :: getProjectVolume(start : " + start + ", end : " + end + ")");
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user : " + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get project volume.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		DateRangeDTO range = CommonUtil.validateDateRange(start, end);
		logger.info("Returning project volume");
		return recordService.getProjectByDateRange(Project.class, range.getStartDate(), range.getEndDate());
	}

	/**
	 * Method to export project volume spreadsheet
	 * 
	 * @param start
	 * @param end
	 * @param status
	 * 
	 * @return the ProjectByStatusDTO object
	 * 
	 * @throws FileNotFoundException
	 */
	public ProjectByStatusDTO exportProjectVolumeSpreadsheet(String start, String end, String status)
			throws FileNotFoundException {
		logger.info(
				"Inside DashboardService :: exportProjectVolumeSpreadsheet(start : " + start + ", end : " + end + ")");
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user : " + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get project volume spreadsheet.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		DateRangeDTO range = CommonUtil.validateDateRange(start, end);

		range.setStatus(this.validateProjectStatus(status));
		ProjectByStatusDTO statusDTO = null;
		List<QuotationDTO> projects = null;

		if (ProjectStatusName.TO_DO.getProjectStatusNameString().equalsIgnoreCase(range.getStatus())) {
			projects = this.recordService.getProjectByDateRangeAndStatus(Quotation.class, range.getStartDate(),
					range.getEndDate(), range.getStatus());
		} else {
			projects = this.recordService.getProjectByDateRangeAndStatus(Project.class, range.getStartDate(),
					range.getEndDate(), range.getStatus());
		}

		List<String> header = Arrays.asList(Constants.PROJECT_ID, Constants.PROJECT_TITLE, Constants.PROJECT_DATE,
				Constants.PROJECT_STATUS);
		Workbook workbook = this.createSheetAndAddRowHeader(Constants.PROJECT_VOLUME,
				CommonUtil.datePattern(Constants.DATE_PATTERN, range.getStartDate()) + " - "
						+ CommonUtil.datePattern(Constants.DATE_PATTERN, range.getEndDate()),
				CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()), header);

		List<SpreadsheetDataDTO> spreadsheetData = projects.stream().map(record -> {
			List<String> list = Arrays.asList(new Long(record.getId()).toString(), record.getTitle(),
					CommonUtil.datePattern(Constants.DATE_PATTERN, record.getDate()), record.getStatus());
			return new SpreadsheetDataDTO(list);
		}).collect(Collectors.toList());

		byte[] spreadsheetByteArray = this.addData(workbook, spreadsheetData, 3);

		statusDTO = new ProjectByStatusDTO(range.getStatus(), projects.size(), projects);
		statusDTO.setTimestamp(CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()));
		statusDTO.setPeriod(CommonUtil.datePattern(Constants.DATE_PATTERN, range.getStartDate()) + " - "
				+ CommonUtil.datePattern(Constants.DATE_PATTERN, range.getEndDate()));
		statusDTO.setSpreadsheetFile(spreadsheetByteArray);
		return statusDTO;
	}

	/**
	 * Method to validate project-status
	 * 
	 * @param status
	 * 
	 * @return the project status
	 */
	private String validateProjectStatus(String status) {
		logger.info("Inside DashboardService :: validateProjectStatus(status : " + status
				+ "), To validate project-status");
		if (StringUtils.isBlank(status)) {
			logger.info("Please provide valid project status");
			throw new UnprocessableEntityException("Please provide valid project status");
		}
		ProjectStatusName statusName = ProjectStatusName.getEnum(status);
		return statusName.getProjectStatusNameString();
	}

	/**
	 * To create workbook and add row header
	 * 
	 * @param sheetName
	 * @param period
	 * @param timestamp
	 * @param header
	 * 
	 * @return the workbook object
	 */
	private Workbook createSheetAndAddRowHeader(String sheetName, String period, String timestamp,
			List<String> header) {
		logger.info("Inside DashboardService :: createSheetAndAddRowHeader(sheetName : " + sheetName
				+ "), to create sheet and row header");

		if (StringUtils.isBlank(sheetName)) {
			logger.info("Invalid sheet name");
			throw new UnprocessableEntityException("Invalid sheet name");
		}
		Workbook workbook = new XSSFWorkbook();
		boolean isColFive = false;
		Sheet sheet = workbook.createSheet(StringUtils.capitalize(sheetName));

		if (sheetName.equalsIgnoreCase(Constants.PROJECT_BUSINESS_FUNCTION)) {
			sheet.setColumnWidth(0, 22000);
			sheet.setColumnWidth(1, 14000);
		} else if (sheetName.equalsIgnoreCase(Constants.EQUIPMENT_STRING)
				|| sheetName.equalsIgnoreCase(Constants.PERSONNEL_STRING)) {
			sheet.setColumnWidth(0, 6000);
			sheet.setColumnWidth(1, 6000);
			sheet.setColumnWidth(2, 6000);
			sheet.setColumnWidth(3, 10000);
			sheet.setColumnWidth(4, 6000);
			sheet.setColumnWidth(5, 6000);
			sheet.setColumnWidth(6, 6000);
		} else {
			sheet.setColumnWidth(0, 5000);
			sheet.setColumnWidth(1, 16000);
			sheet.setColumnWidth(2, 6000);
			sheet.setColumnWidth(3, 6000);
			if (header.size() == 5) {
				isColFive = true;
				sheet.setColumnWidth(4, 6000);
			}
		}

		Font font = workbook.createFont();
		font.setBold(true);
		font.setColor(IndexedColors.WHITE.getIndex());

		XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.LEFT);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);

		XSSFColor color = new XSSFColor(new Color(47, 111, 152));
		style.setFillForegroundColor(color);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		for (int rowNumber = 0; rowNumber <= 2; rowNumber++) {
			Row newRow = sheet.createRow(rowNumber);
			for (int col = 0; col < header.size(); col++) {
				newRow.createCell(col);
			}

			if (rowNumber == 0) {
				if (sheetName.equalsIgnoreCase(Constants.PROJECT_BUSINESS_FUNCTION)) {
					newRow.getCell(0).setCellValue(Constants.PERIOD + " : " + period);
					newRow.getCell(1).setCellValue(Constants.EXPORTED + " : " + timestamp);
				} else if (sheetName.equalsIgnoreCase(Constants.EQUIPMENT_STRING)
						|| sheetName.equalsIgnoreCase(Constants.PERSONNEL_STRING)) {
					sheet.addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, 0, 3));
					sheet.addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, 4, 6));
					newRow.getCell(0).setCellValue(Constants.PERIOD + " : " + period);
					newRow.getCell(4).setCellValue(Constants.EXPORTED + " : " + timestamp);
				} else {
					sheet.addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, 0, 1));
					if (isColFive) {
						sheet.addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, 2, 4));
					} else {
						sheet.addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, 2, 3));
					}
					newRow.getCell(0).setCellValue(Constants.PERIOD + " : " + period);
					newRow.getCell(2).setCellValue(Constants.EXPORTED + " : " + timestamp);
				}

			} else if (rowNumber == 1) {
				for (int col = 0; col < header.size(); col++) {
					newRow.getCell(col).setCellValue(Constants.EMPTY_STRING);
				}
			} else if (rowNumber == 2) {
				for (int col = 0; col < header.size(); col++) {
					newRow.getCell(col).setCellValue(header.get(col));
				}
			}
			style.setFont(font);
			newRow.getCell(0).setCellStyle(style);
			newRow.getCell(1).setCellStyle(style);
			if (!sheetName.equalsIgnoreCase(Constants.PROJECT_BUSINESS_FUNCTION)) {
				newRow.getCell(2).setCellStyle(style);
				newRow.getCell(3).setCellStyle(style);
				if (isColFive) {
					newRow.getCell(4).setCellStyle(style);
				}
				if (sheetName.equalsIgnoreCase(Constants.EQUIPMENT_STRING)
						|| sheetName.equalsIgnoreCase(Constants.PERSONNEL_STRING)) {
					newRow.getCell(4).setCellStyle(style);
					newRow.getCell(5).setCellStyle(style);
					newRow.getCell(6).setCellStyle(style);
				}
			}
		}
		return workbook;
	}

	/**
	 * Method to add data in spreadsheet
	 * 
	 * @param workbook
	 * @param spreadsheetData
	 * @param rowNumber
	 * 
	 * @return spreadsheet byteArray
	 * @throws FileNotFoundException
	 */
	private byte[] addData(Workbook workbook, List<SpreadsheetDataDTO> spreadsheetData, int rowNumber)
			throws FileNotFoundException {
		logger.info("Inside DashboardService :: addData(), To add data in spreadsheet");
		Sheet sheet = workbook.getSheetAt(0);
		int rowCount = rowNumber;
		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		style.setAlignment(HorizontalAlignment.LEFT);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		XSSFCellStyle styleWrap = style;
		XSSFCellStyle rowDefaultStyle = style;
		styleWrap.setWrapText(true);

		if ((sheet.getSheetName().equalsIgnoreCase(Constants.EQUIPMENT_STRING)
				|| sheet.getSheetName().equalsIgnoreCase(Constants.PERSONNEL_STRING))) {
			rowCount = 3;
		}

		for (SpreadsheetDataDTO record : spreadsheetData) {

			Row newRow = sheet.createRow(rowCount);
			List<String> data = record.getData();
			int size = data.size();
			if (workbook.getSheetAt(0).getSheetName().equals(Constants.PROJECT_ACTUAL_STRING)) {
				size = size - 1;
			}

			for (int colNumber = 0; colNumber < size; colNumber++) {
				if (colNumber == 0) {
					if (sheet.getSheetName().equalsIgnoreCase(Constants.PROJECT_BUSINESS_FUNCTION)) {
						newRow.createCell(colNumber).setCellValue(data.get(colNumber));
						newRow.getCell(colNumber).setCellStyle(styleWrap);
					} else if (sheet.getSheetName().equalsIgnoreCase(Constants.EQUIPMENT_STRING)
							|| sheet.getSheetName().equalsIgnoreCase(Constants.PERSONNEL_STRING)) {
						newRow.createCell(colNumber).setCellValue(data.get(colNumber));
						newRow.getCell(colNumber).setCellStyle(styleWrap);
					} else {
						Long id = Long.valueOf(data.get(colNumber));
						newRow.createCell(colNumber).setCellValue(id);
						newRow.getCell(colNumber).setCellStyle(styleWrap);
					}
				} else {

					if ((sheet.getSheetName().equalsIgnoreCase(Constants.PROJECT_REVENUE)
							|| sheet.getSheetName().equalsIgnoreCase(Constants.PROJECT_ACTUAL_VS_BUDGET))
							&& colNumber > 2) {
						newRow.createCell(colNumber).setCellValue("$ " + data.get(colNumber));
						newRow.getCell(colNumber).setCellStyle(styleWrap);
					} else if ((sheet.getSheetName().equalsIgnoreCase(Constants.PROJECT_ACTUAL_STRING))
							&& colNumber > 2) {
						newRow.createCell(colNumber).setCellValue("$ " + data.get(colNumber));
						newRow.getCell(colNumber).setCellStyle(styleWrap);
						if (colNumber == 3) {
							XSSFCellStyle cellStyle = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
							cellStyle.setAlignment(HorizontalAlignment.LEFT);
							cellStyle.setBorderBottom(BorderStyle.THIN);
							cellStyle.setBorderTop(BorderStyle.THIN);
							cellStyle.setBorderRight(BorderStyle.THIN);
							cellStyle.setBorderLeft(BorderStyle.THIN);

							Font font = workbook.createFont();
							font.setFontHeightInPoints((short) 10);

							if (data.contains(Constants.COMPARISION_TAG_GRATER)) {
								font.setColor(IndexedColors.RED.getIndex());
								cellStyle.setFont(font);
							} else if (data.contains(Constants.COMPARISION_TAG_LOWER)) {
								font.setColor(IndexedColors.GREEN.getIndex());
								cellStyle.setFont(font);
							} else {
								font.setColor(IndexedColors.BLACK.getIndex());
								cellStyle.setFont(font);
							}
							newRow.getCell(colNumber).setCellStyle(cellStyle);

						}
					} else if ((sheet.getSheetName().equalsIgnoreCase(Constants.PROJECT_BUDGETING)) && colNumber == 4) {
						newRow.createCell(colNumber).setCellValue("$ " + data.get(colNumber));
						newRow.getCell(colNumber).setCellStyle(styleWrap);
					} else if (sheet.getSheetName().equalsIgnoreCase(Constants.PROJECT_BUSINESS_FUNCTION)) {
						Integer numberOfDays = Integer.valueOf(data.get(colNumber));
						newRow.createCell(colNumber).setCellValue(numberOfDays);
						newRow.getCell(colNumber).setCellStyle(styleWrap);
					} else {
						newRow.createCell(colNumber).setCellValue(data.get(colNumber));
						newRow.getCell(colNumber).setCellStyle(styleWrap);
					}

					if ((sheet.getSheetName().equalsIgnoreCase(Constants.EQUIPMENT_STRING)
							|| sheet.getSheetName().equalsIgnoreCase(Constants.PERSONNEL_STRING)) && colNumber == 4) {
						XSSFCellStyle cellStyle = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
						newRow.createCell(colNumber).setCellValue(data.get(colNumber));
						newRow.getCell(colNumber).setCellStyle(cellStyle);
						if (data.get(colNumber).equalsIgnoreCase(Constants.ORDER_TYPE_EXTERNAL)) {
							this.setCellBackgroundColorAndBorder(workbook, 85, 218, 225, rowCount, colNumber);
						} else if (data.get(colNumber).equalsIgnoreCase(Constants.STAFF_IS_FREELANCE)) {
							this.setCellBackgroundColorAndBorder(workbook, 153, 231, 254, rowCount, colNumber);
						} else {
							if (sheet.getSheetName().equalsIgnoreCase(Constants.EQUIPMENT_STRING)) {
								if (data.get(colNumber).equalsIgnoreCase(Constants.ORDER_TYPE_INTERNAL)) {
									this.setCellBackgroundColorAndBorder(workbook, 123, 114, 233, rowCount, colNumber);
								}
							} else {
								if (data.get(colNumber).equalsIgnoreCase(Constants.ORDER_TYPE_INTERNAL)) {
									this.setCellBackgroundColorAndBorder(workbook, 78, 176, 249, rowCount, colNumber);
								}
							}
						}

					}
				}

				// newRow.getCell(colNumber).setCellStyle(styleWrap);
			}
			rowCount++;
		}

		// set default row-color on row one
		Row rowOne = workbook.getSheetAt(0).getRow(1);
		XSSFColor color = new XSSFColor(new Color(255, 255, 255));
		rowDefaultStyle.setFillForegroundColor(color);
		for (Cell cell : rowOne) {
			cell.setCellStyle(rowDefaultStyle);
		}
		if ((sheet.getSheetName().equalsIgnoreCase(Constants.EQUIPMENT_STRING))) {
			rowOne.createCell(0).setCellValue(Constants.INTERNAL_INVENTORY);
			rowOne.createCell(1).setCellValue(rowNumber - 3);
			this.setCellBackgroundColorAndBorder(workbook, 143, 197, 109, 1, 0);
			this.setCellBackgroundColorAndBorder(workbook, 143, 197, 109, 1, 1);
		}
		if ((sheet.getSheetName().equalsIgnoreCase(Constants.PERSONNEL_STRING))) {
			rowOne.createCell(0).setCellValue(Constants.HEADCOUNT);
			rowOne.createCell(1).setCellValue(rowNumber - 3);
			this.setCellBackgroundColorAndBorder(workbook, 94, 190, 204, 1, 0);
			this.setCellBackgroundColorAndBorder(workbook, 94, 190, 204, 1, 1);
		}

		// writing file into byte-array

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

		logger.info("Returning from addData()");
		return bos.toByteArray();
	}

	/**
	 * create cell style background color and border
	 * 
	 * @param workbook
	 * @param r
	 * @param g
	 * @param b
	 * @param rowCount
	 * @param colNumber
	 * @return Cell
	 */
	private Cell setCellBackgroundColorAndBorder(Workbook workbook, int r, int g, int b, int rowCount, int colNumber) {
		logger.info("Inside DashboardService :: setCellBackgroundColorAndBorder");
		Cell cell = workbook.getSheetAt(0).getRow(rowCount).getCell(colNumber);
		XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);

		XSSFColor color = new XSSFColor(new Color(r, g, b));
		cellStyle.setFillForegroundColor(color);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cell.setCellStyle(cellStyle);
		logger.info("Returning from after set cell background color and border ");
		return cell;
	}

	/**
	 * Method to get project revenue
	 * 
	 * @param start
	 *            the start date
	 * @param end
	 *            the end date
	 * 
	 * @return project revenue
	 */
	public Map<String, Float> getProjectRevenue(String start, String end) {
		logger.info("Inside DashboardService :: getProjectRevenue(start : " + start + ", end : " + end + ")");
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get project revenue.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		DateRangeDTO range = CommonUtil.validateDateRange(start, end);
		Map<String, Float> revenue = new HashMap<String, Float>();
		if (range.isPreviousMonthData()) {
			logger.info("Get current and previous month revenue");
			float currentRevenue = recordService.getRevenueByDateRange(Project.class, range.getStartDate(),
					range.getEndDate(), ProjectFinancialStatusName.APPROVED.getProjectFinancialStatusNameString());

			List<Calendar> previousMonthDates = this.getPreviousMonthDates(range.getStartDate(), range.getEndDate());

			float previousMonthRevenue = recordService.getRevenueByDateRange(Project.class, previousMonthDates.get(0),
					previousMonthDates.get(1),
					ProjectFinancialStatusName.APPROVED.getProjectFinancialStatusNameString());

			revenue.put(Constants.CURRENT_MONTH_DATA, currentRevenue);
			revenue.put(Constants.PREVIOUS_MONTH_DATA, previousMonthRevenue);
		} else {
			logger.info("Get only current month revenue");
			float currentRevenue = (Float) recordService.getRevenueByDateRange(Project.class, range.getStartDate(),
					range.getEndDate(), ProjectFinancialStatusName.APPROVED.getProjectFinancialStatusNameString());
			revenue.put(Constants.CURRENT_MONTH_DATA, currentRevenue);
		}
		logger.info("Returning after getting project revenue");
		return revenue;
	}

	/**
	 * Method to export project revenue spreadsheet
	 * 
	 * @param start
	 * @param end
	 * 
	 * @return the ProjectByStatus wrapper object
	 * 
	 * @throws FileNotFoundException
	 */
	public ProjectByStatusDTO exportProjectRevenueSpreadsheet(String start, String end) throws FileNotFoundException {
		logger.info(
				"Inside DashboardService :: exportProjectRevenueSpreadsheet(start : " + start + ", end : " + end + ")");
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user : " + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get project revenue spreadsheet.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		ProjectByStatusDTO statusDTO = null;
		DateRangeDTO range = CommonUtil.validateDateRange(start, end);
		logger.info("Importing revenue spreadsheet");
		float currentRevenue = recordService.getRevenueByDateRange(Project.class, range.getStartDate(),
				range.getEndDate(), ProjectFinancialStatusName.APPROVED.getProjectFinancialStatusNameString());

		List<QuotationDTO> projects = recordService.getRecordRevenueByDateRange(Project.class, range.getStartDate(),
				range.getEndDate(), ProjectFinancialStatusName.APPROVED.getProjectFinancialStatusNameString());

		List<String> header = Arrays.asList(Constants.PROJECT_ID, Constants.PROJECT_TITLE, Constants.PROJECT_DATE,
				Constants.PROJECT_TOTAL_WITHOUT_TAX);
		Workbook workbook = this.createSheetAndAddRowHeader(Constants.PROJECT_REVENUE,
				CommonUtil.datePattern(Constants.DATE_PATTERN, range.getStartDate()) + " - "
						+ CommonUtil.datePattern(Constants.DATE_PATTERN, range.getEndDate()),
				CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()), header);

		List<SpreadsheetDataDTO> spreadsheetData = projects.stream().map(record -> {
			List<String> list = Arrays.asList(new Long(record.getId()).toString(), record.getTitle(),
					CommonUtil.datePattern(Constants.DATE_PATTERN, record.getDate()),
					CommonUtil.formatValue(record.getTotalPriceIVAT()));
			return new SpreadsheetDataDTO(list);
		}).collect(Collectors.toList());

		byte[] spreadsheetByteArray = this.addData(workbook, spreadsheetData, 3);

		statusDTO = new ProjectByStatusDTO(range.getStatus(), projects.size(), projects, spreadsheetByteArray,
				CommonUtil.formatValue(currentRevenue));
		statusDTO.setTimestamp(CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()));
		statusDTO.setPeriod(CommonUtil.datePattern(Constants.DATE_PATTERN, range.getStartDate()) + " - "
				+ CommonUtil.datePattern(Constants.DATE_PATTERN, range.getEndDate()));
		logger.info("Returning after importing revenue spreadsheet");
		return statusDTO;
	}

	/**
	 * Method to get previous month start and end date
	 * 
	 * @param startDateIntance
	 * @param endDateInstance
	 * 
	 * @return the previous month start and end dates
	 */
	private List<Calendar> getPreviousMonthDates(Calendar startDateIntance, Calendar endDateInstance) {
		logger.info("Inside DashboardService :: getPreviousMonthDates(startDate : " + startDateIntance + " , endDate : "
				+ endDateInstance + " ), to get previous month dates");
		List<Calendar> previousMonthDates = new ArrayList<Calendar>();
		if (startDateIntance != null && endDateInstance != null) {
			Calendar previousMonthStartDate = startDateIntance;
			Calendar previousMonthEndDate = endDateInstance;

			if (previousMonthStartDate.get(Calendar.MONTH) == 0) {
				// means current month is January, set previous month to December and preceding
				// year
				previousMonthStartDate.add(Calendar.YEAR, -1);
				previousMonthStartDate.set(Calendar.MONTH, Calendar.DECEMBER);

				previousMonthEndDate.add(Calendar.YEAR, -1);
				previousMonthEndDate.set(Calendar.MONTH, Calendar.DECEMBER);

			} else {
				previousMonthStartDate.add(Calendar.MONTH, -1);
				previousMonthEndDate.add(Calendar.MONTH, -1);
			}
			previousMonthDates = Arrays.asList(previousMonthStartDate, previousMonthEndDate);
		} else {
			logger.info("Invalid start and end date");
			throw new UnprocessableEntityException("Invalid start and end date");
		}
		return previousMonthDates;
	}

	/**
	 * Method to get project actual vs budget/spreadsheet
	 * 
	 * @param dateRange
	 * 
	 * @return project actual vs budget data
	 * 
	 * @throws IOException
	 */
	public List<ProjectByStatusDTO> getProjectActualVsBudget(String start, String end) throws IOException {
		logger.info("Inside DashboardService :: getProjectActualVsBudget(start : " + start + ", end : " + end + ")");
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get project actual vs budget comparison.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		DateRangeDTO range = CommonUtil.validateDateRange(start, end);
		return recordService.getProjectActualVsBudgetByDateRangeAndFinancialStatus(Project.class, range.getStartDate(),
				range.getEndDate(), ProjectFinancialStatusName.APPROVED.getProjectFinancialStatusNameString());
	}

	/**
	 * Method to export project actual vs budget comparison spreadsheet
	 * 
	 * @param start
	 * @param end
	 * @param comparison
	 * 
	 * @return the ProjectByStatus wrapper object
	 * 
	 * @throws IOException
	 */
	public ProjectByStatusDTO exportProjectActualVsBudgetSpreadsheet(String start, String end, String comparison)
			throws IOException {
		logger.info("Inside DashboardService :: exportProjectActualVsBudgetSpreadsheet(start : " + start + ", end : "
				+ end + ", comparison :" + comparison + ")");
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get project actual vs budget comparison spreadsheet.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		DateRangeDTO range = CommonUtil.validateDateRange(start, end);
		logger.info("Importing project actual vs budget spreadsheet");
		String comparisonString = this.validateProjectActual(comparison);
		range.setActualComparison(comparisonString);

		Map<String, List<QuotationDTO>> actualComparison = recordService.getProjectByActualComparison(Project.class,
				range.getStartDate(), range.getEndDate(),
				ProjectFinancialStatusName.APPROVED.getProjectFinancialStatusNameString());

		List<QuotationDTO> projects = actualComparison.get(comparisonString);
		if (projects == null || projects.isEmpty()) {
			projects = new ArrayList<QuotationDTO>();
		}

		List<String> header = Arrays.asList(Constants.PROJECT_ID, Constants.PROJECT_TITLE, Constants.PROJECT_DATE,
				Constants.PROJECT_ACTUAL, StringUtils.capitalize(Constants.BUDGET_STRING));

		Workbook workbook = this.createSheetAndAddRowHeader(Constants.PROJECT_ACTUAL_VS_BUDGET,
				CommonUtil.datePattern(Constants.DATE_PATTERN, range.getStartDate()) + " - "
						+ CommonUtil.datePattern(Constants.DATE_PATTERN, range.getEndDate()),
				CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()), header);

		List<SpreadsheetDataDTO> spreadsheetData = projects.stream().map(record -> {
			List<String> list = Arrays.asList(new Long(record.getId()).toString(), record.getTitle(),
					CommonUtil.datePattern(Constants.DATE_PATTERN, record.getDate()),
					CommonUtil.formatValue(record.getTotalPriceIVAT()),
					CommonUtil.formatValue(record.getSource().getTotalPriceIVAT()));
			return new SpreadsheetDataDTO(list);
		}).collect(Collectors.toList());

		byte[] spreadsheetByteArray = this.addData(workbook, spreadsheetData, 3);

		ProjectByStatusDTO statusDTO = new ProjectByStatusDTO(range.getActualComparison(), projects.size(), projects,
				spreadsheetByteArray);
		statusDTO.setTimestamp(CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()));
		statusDTO.setPeriod(CommonUtil.datePattern(Constants.DATE_PATTERN, range.getStartDate()) + " - "
				+ CommonUtil.datePattern(Constants.DATE_PATTERN, range.getEndDate()));
		logger.info("Returning after importing project actual vs budget spreadsheet");
		return statusDTO;
	}

	/**
	 * Method to validate actual comparison
	 * 
	 * @param comparisonString
	 * 
	 * @return the actual comparison string
	 */
	private String validateProjectActual(String comparisonString) {
		logger.info("Inside DashboardService :: validateProjectActual(comparisonString : " + comparisonString
				+ "), To validate project-actual comparison");
		if (StringUtils.isBlank(comparisonString)) {
			logger.info("Please provide valid project actual comparison");
			throw new UnprocessableEntityException("Please provide valid project actual comparison");
		}
		if (comparisonString.equalsIgnoreCase(Constants.PROJECT_ACTUAL_ON_BUDGET)) {
			return Constants.PROJECT_ACTUAL_ON_BUDGET;
		} else if (comparisonString.equalsIgnoreCase(Constants.PROJECT_ACTUAL_EXCEED)) {
			return Constants.PROJECT_ACTUAL_EXCEED;
		} else if (comparisonString.equalsIgnoreCase(Constants.PROJECT_ACTUAL_LOWER)) {
			return Constants.PROJECT_ACTUAL_LOWER;
		} else {
			logger.info("Please provide valid project actual comparison");
			throw new UnprocessableEntityException("Please provide valid project actual comparison");
		}
	}

	/**
	 * Method to get project budgeting
	 *
	 * @param start
	 * @param end
	 * 
	 * @return the project budgeting data
	 */
	public Map<String, Float> getProjectBudgeting(String start, String end) {
		logger.info("Inside DashboardService :: getProjectBudgeting(start : " + start + ", end : " + end + ")");
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get project budgeting.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		DateRangeDTO range = CommonUtil.validateDateRange(start, end);
		Map<String, Float> revenue = new HashMap<String, Float>();
		if (range.isPreviousMonthData()) {
			logger.info("Get current and previous month project budgeting");
			float currentProjectBudgeting = recordService.getRevenueByDateRange(Quotation.class, range.getStartDate(),
					range.getEndDate(), null);

			List<Calendar> previousMonthDates = this.getPreviousMonthDates(range.getStartDate(), range.getEndDate());

			float previousMonthProjectBudgeting = recordService.getRevenueByDateRange(Quotation.class,
					previousMonthDates.get(0), previousMonthDates.get(1), null);

			revenue.put(Constants.CURRENT_MONTH_DATA, currentProjectBudgeting);
			revenue.put(Constants.PREVIOUS_MONTH_DATA, previousMonthProjectBudgeting);
		} else {
			logger.info("Get only current month project budgeting");
			float currentProjectBudgeting = recordService.getRevenueByDateRange(Quotation.class, range.getStartDate(),
					range.getEndDate(), null);
			revenue.put(Constants.CURRENT_MONTH_DATA, currentProjectBudgeting);
		}
		logger.info("Returning after getting project budgeting");
		return revenue;
	}

	/**
	 * Method to get project actual
	 * 
	 * @param start
	 * @param end
	 * 
	 * @return the project actual data
	 */
	public Map<String, Float> getMostUsedBusinessFunction(String start, String end) {
		logger.info("Inside DashboardService :: getProjectActual(start : " + start + ", end : " + end + ")");
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get project actual.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		DateRangeDTO range = CommonUtil.validateDateRange(start, end);
		List<QuotationDTO> budgetsList = this.compareBudgetWithBooking(range);
		logger.info("Calculating project actual");
		Map<String, Float> projectActual = new HashMap<String, Float>();
		Float actualTotal = (float) budgetsList.stream().mapToDouble(t -> t.getBookingTotalIVAT()).sum();
		Float budgetTotal = (float) budgetsList.stream().mapToDouble(t -> t.getTotalPriceIVAT()).sum();
		projectActual.put(Constants.PROJECT_ACTUAL, actualTotal);
		projectActual.put(StringUtils.capitalize(Constants.BUDGET_STRING), budgetTotal);
		logger.info("Returning after getting project actual");
		return projectActual;
	}

	/**
	 * Method to get project actual
	 * 
	 * @param start
	 * @param end
	 * 
	 * @return the project actual data
	 */
	public Map<String, Float> getProjectActual(String start, String end) {
		logger.info("Inside DashboardService :: getProjectActual(start : " + start + ", end : " + end + ")");
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get project actual.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		DateRangeDTO range = CommonUtil.validateDateRange(start, end);
		List<QuotationDTO> budgetsList = this.compareBudgetWithBooking(range);
		logger.info("Calculating project actual");
		Map<String, Float> projectActual = new HashMap<String, Float>();
		Float actualTotal = (float) budgetsList.stream().mapToDouble(t -> t.getBookingTotalIVAT()).sum();
		Float budgetTotal = (float) budgetsList.stream().mapToDouble(t -> t.getTotalPriceIVAT()).sum();
		projectActual.put(Constants.PROJECT_ACTUAL, actualTotal);
		projectActual.put(StringUtils.capitalize(Constants.BUDGET_STRING), budgetTotal);
		logger.info("Returning after getting project actual");
		return projectActual;
	}

	/**
	 * Method to compare each budget with its booking
	 * 
	 * @param range
	 * 
	 * @return list of budgets
	 */
	private List<QuotationDTO> compareBudgetWithBooking(DateRangeDTO range) {
		logger.info("Inside DashboardService :: compareBudgetWithBooking()");
		List<QuotationDTO> budgetsList = new ArrayList<QuotationDTO>();
		List<Record> records = this.recordService.getRecordByDateRange(Quotation.class, range.getStartDate(),
				range.getEndDate());

		for (Record record : records) {
			QuotationDTO entity = new QuotationDTO((Quotation) record, Boolean.TRUE);
			for (RecordDTO booking : this.recordService.getRecords(Project.class)) {
				RecordDTO source = booking.getSource();
				if (source != null && record.getId().longValue() == source.getId().longValue()) {
					entity.setBookingTotalIVAT(((QuotationDTO) booking).getBookingTotalIVAT());
				}
			}
			entity.setComparisonTag(
					(entity.getTotalPriceIVAT() < entity.getBookingTotalIVAT()) ? Constants.COMPARISION_TAG_GRATER
							: (entity.getTotalPriceIVAT() > entity.getBookingTotalIVAT())
									? Constants.COMPARISION_TAG_LOWER
									: Constants.COMPARISION_TAG_EQUAL);
			budgetsList.add(entity);
		}
		logger.info("Returning quotation list after comparing each budget with its booking");
		return budgetsList;
	}

	/**
	 * Method to export project budgeting spreadsheet
	 * 
	 * @param start
	 * @param end
	 * 
	 * @return the ProjectByStatus wrapper object
	 * 
	 * @throws IOException
	 */
	public ProjectByStatusDTO exportProjectBudgetingSpreadsheet(String start, String end) throws IOException {
		logger.info("Inside DashboardService :: exportProjectBudgetingSpreadsheet(start : " + start + ", end : " + end
				+ ")");
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get project budgeting spreadsheet.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		DateRangeDTO range = CommonUtil.validateDateRange(start, end);
		logger.info("Importing project budgeting spreadsheet");

		List<QuotationDTO> budgets = recordService.getRecordRevenueByDateRange(Quotation.class, range.getStartDate(),
				range.getEndDate(), null);
		List<String> header = Arrays.asList(Constants.PROJECT_ID, Constants.PROJECT_TITLE,
				Constants.PROJECT_FINANCIAL_STATUS, Constants.PROJECT_DATE, Constants.PROJECT_TOTAL_WITHOUT_TAX);

		Workbook workbook = this.createSheetAndAddRowHeader(Constants.PROJECT_BUDGETING,
				CommonUtil.datePattern(Constants.DATE_PATTERN, range.getStartDate()) + " - "
						+ CommonUtil.datePattern(Constants.DATE_PATTERN, range.getEndDate()),
				CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()), header);

		List<SpreadsheetDataDTO> spreadsheetData = budgets.stream().map(record -> {
			List<String> list = Arrays.asList(new Long(record.getId()).toString(), record.getTitle(),
					record.getFinancialStatus(), CommonUtil.datePattern(Constants.DATE_PATTERN, record.getDate()),
					CommonUtil.formatValue(record.getTotalPriceIVAT()));
			return new SpreadsheetDataDTO(list);
		}).collect(Collectors.toList());

		byte[] spreadsheetByteArray = this.addData(workbook, spreadsheetData, 3);

		ProjectByStatusDTO statusDTO = new ProjectByStatusDTO(range.getActualComparison(), budgets.size(), budgets,
				spreadsheetByteArray);
		statusDTO.setTimestamp(CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()));
		statusDTO.setPeriod(CommonUtil.datePattern(Constants.DATE_PATTERN, range.getStartDate()) + " - "
				+ CommonUtil.datePattern(Constants.DATE_PATTERN, range.getEndDate()));
		logger.info("Returning after importing project actual vs budget spreadsheet");
		return statusDTO;
	}

	/**
	 * Method to export project actual spreadsheet
	 * 
	 * @param start
	 * @param end
	 * 
	 * @return the ProjectByStatus wrapper object
	 * 
	 * @throws IOException
	 */
	public ProjectByStatusDTO exportProjectActualSpreadsheet(String start, String end) throws IOException {
		logger.info(
				"Inside DashboardService :: exportProjectActualSpreadsheet(start : " + start + ", end : " + end + ")");
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get project actual spreadsheet.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		DateRangeDTO range = CommonUtil.validateDateRange(start, end);
		List<QuotationDTO> budgetsList = this.compareBudgetWithBooking(range);

		List<String> header = Arrays.asList(Constants.PROJECT_ID, Constants.PROJECT_TITLE, Constants.PROJECT_DATE,
				Constants.PROJECT_ACTUAL, StringUtils.capitalize(Constants.BUDGET_STRING));
		Workbook workbook = this.createSheetAndAddRowHeader(Constants.PROJECT_ACTUAL_STRING,
				CommonUtil.datePattern(Constants.DATE_PATTERN, range.getStartDate()) + " - "
						+ CommonUtil.datePattern(Constants.DATE_PATTERN, range.getEndDate()),
				CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()), header);

		List<SpreadsheetDataDTO> spreadsheetData = budgetsList.stream().map(record -> {
			List<String> list = Arrays.asList(new Long(record.getId()).toString(), record.getTitle(),
					CommonUtil.datePattern(Constants.DATE_PATTERN, record.getDate()),
					CommonUtil.formatValue(((QuotationDTO) record).getBookingTotalIVAT()),
					CommonUtil.formatValue(record.getTotalPriceIVAT()), record.getComparisonTag());
			return new SpreadsheetDataDTO(list);
		}).collect(Collectors.toList());

		logger.info("Importing project actual spreadsheet");
		byte[] spreadsheetByteArray = this.addData(workbook, spreadsheetData, 3);

		ProjectByStatusDTO statusDTO = new ProjectByStatusDTO(budgetsList.size(), budgetsList, range.getStatus(),
				CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()).toString(),
				spreadsheetByteArray);
		statusDTO.setTimestamp(CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()));
		statusDTO.setPeriod(CommonUtil.datePattern(Constants.DATE_PATTERN, range.getStartDate()) + " - "
				+ CommonUtil.datePattern(Constants.DATE_PATTERN, range.getEndDate()));
		logger.info("Returning after importing actual spreadsheet");
		return statusDTO;
	}

	/**
	 * To get functions by their uses.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param resultCount
	 * 
	 * @return the project business function data
	 */
	public Map<String, Integer> getProjectBussinessFunctionByStartDateAndEndDate(String startDate, String endDate,
			Integer resultCount) {
		logger.info("Inside DashboardService :: getProjectBussinessFunctionByStartDateAndEndDate(start : " + startDate
				+ ", end : " + endDate + ", resultCount : " + resultCount + ")");
		DateRangeDTO dateRangeDTO = CommonUtil.validateDateRange(startDate, endDate);
		return this.getProjectBussinessFunctionByStartDateAndEndDateAlgo(dateRangeDTO, resultCount);
	}

	/**
	 * Method to get functions by their uses
	 * 
	 * @param dateRangeDTO
	 * @param resultCount
	 * 
	 * @return the project business function data
	 */
	public Map<String, Integer> getProjectBussinessFunctionByStartDateAndEndDateAlgo(DateRangeDTO dateRangeDTO,
			Integer resultCount) {
		logger.info("Inside DashboardService :: getProjectBussinessFunctionByStartDateAndEndDateAlgo(dateRangeDTO : "
				+ dateRangeDTO + ", resultCount : " + resultCount + ")");
		if (resultCount == null || resultCount == 0) {
			logger.info("Setting default result-count to 5");
			resultCount = 5;
		}
		List<Booking> bookings = bookingService.findByFunctionAndStartDateAndEndDate(null, dateRangeDTO.getStartDate(),
				dateRangeDTO.getEndDate());

		// calculating function per uses count
		Map<String, Integer> functionByBookingCount = getMapedFunctionalUses(dateRangeDTO, bookings);

		logger.info("Returning after geting ProjectBussinessFunctionByStartDateAndEnd().");
		return functionByBookingCount.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.limit(resultCount)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

	}

	/**
	 * Method to export project business function spreadsheet
	 * 
	 * @param startDate
	 * @param endDate
	 * @param resultCount
	 * 
	 * @return ProjectByStatusDTO wrapper object
	 * 
	 * @throws IOException
	 */
	public ProjectByStatusDTO exportProjectBussinessFunctionByStartDateAndEndDateSpreadsheet(String startDate,
			String endDate, Integer resultCount) throws IOException {
		logger.info("Inside DashboardService :: exportProjectBussinessFunctionByStartDateAndEndDateSpreadsheet(start : "
				+ startDate + ", end : " + endDate + ", resultCount : " + resultCount + ")");
		DateRangeDTO range = CommonUtil.validateDateRange(startDate, endDate);
		Map<String, Integer> functionPerUses = this.getProjectBussinessFunctionByStartDateAndEndDateAlgo(range,
				resultCount);
		List<String> header = Arrays.asList(Constants.STAFF_FUNCTION, Constants.NUMBER_OF_DAYS);

		Workbook workbook = this.createSheetAndAddRowHeader(Constants.PROJECT_BUSINESS_FUNCTION,
				CommonUtil.datePattern(Constants.DATE_PATTERN, range.getStartDate()) + " - "
						+ CommonUtil.datePattern(Constants.DATE_PATTERN, range.getEndDate()),
				CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()), header);

		List<SpreadsheetDataDTO> spreadsheetData = functionPerUses.entrySet().stream().map(entry -> {
			List<String> list = Arrays.asList(entry.getKey(), new Integer(entry.getValue()).toString());
			return new SpreadsheetDataDTO(list);
		}).collect(Collectors.toList());
		logger.info("Exporting project business function spreadsheet");
		byte[] spreadsheetByteArray = this.addData(workbook, spreadsheetData, 3);

		ProjectByStatusDTO statusDTO = new ProjectByStatusDTO(functionPerUses,
				CommonUtil.datePattern(Constants.DATE_PATTERN, range.getStartDate()) + " - "
						+ CommonUtil.datePattern(Constants.DATE_PATTERN, range.getEndDate()),
				CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()), spreadsheetByteArray);
		logger.info("Returning after exporting project business function spreadsheet");
		return statusDTO;
	}

	/**
	 * To get graph data for equipment or staff widget.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param resultCount
	 * @param functionType
	 * @return DashboardFunctionalWidgetData
	 */
	public DashboardFunctionalWidgetData getGraphDataForWidgetEquipment(String startDate, String endDate,
			String functionType) {
		logger.info("Inside DashboardService :: getGraphDataForWidgetEquipment(start : " + startDate + ", end : "
				+ endDate + ", functionType : " + functionType + ")");
		DateRangeDTO range = CommonUtil.validateDateRange(startDate, endDate);
		List<Booking> bookings = new ArrayList<Booking>();
		List<OrderLine> externalBookings = new ArrayList<OrderLine>();
		DashboardFunctionalWidgetData dashboardFunctionalWidgetData = new DashboardFunctionalWidgetData();
		long totalInventoryCount = 0;
		AtomicLong totalExternalUsesCount = new AtomicLong();
		AtomicDouble totalOutsourcedAmount = new AtomicDouble();
		AtomicLong totalInternalUsesCount = new AtomicLong();

		if (functionType.equals("EquipmentFunction")) {
			logger.info("Inside Equipment Function");
			totalInventoryCount = equipmentService.findCountByAvailable(true) * range.getDuration();
			bookings = bookingService.findByFunctionAndResourceAndStartDateAndEndDate(functionType,
					range.getStartDate(), range.getEndDate());
			externalBookings = orderLineService.findExternalBookingByFunctionAndResourceAndStartDateAndEndDate(
					functionType, range.getStartDate(), range.getEndDate());
			// getting data for internal bookings
			for (Booking booking : bookings) {
				long duration = getBookingDuration(range, booking);
				totalInternalUsesCount.addAndGet(duration);
			}

			// getting data for external bookings
			for (OrderLine orderLine : externalBookings) {
				long duration = getBookingDuration(range, orderLine);
				totalExternalUsesCount.addAndGet(duration);
				totalOutsourcedAmount.addAndGet(duration * orderLine.getUnitCost());
			}

		} else if (functionType.equalsIgnoreCase("StaffFunction")) {
			logger.info("Inside Staff Function");
			totalInventoryCount = staffMemberService.findCountByAvailable(true) * range.getDuration();
			bookings = bookingService.findByFunctionAndResourceAndStartDateAndEndDate(functionType,
					range.getStartDate(), range.getEndDate());

			bookings.stream().filter(t -> t.getFunction() != null).forEach(t -> {
				long duration = getBookingDuration(range, t);
				StaffMember staffMember = (StaffMember) t.getResource().getEntity();
				if (!isResourceFreelance(staffMember)) {
					totalInternalUsesCount.addAndGet(duration);
				} else {
					totalExternalUsesCount.addAndGet(duration);
					totalOutsourcedAmount.addAndGet(duration * t.getUnitCost());
				}
			});

		}
		dashboardFunctionalWidgetData = new DashboardFunctionalWidgetData(totalInventoryCount,
				totalExternalUsesCount.get(), totalInternalUsesCount.get(), totalOutsourcedAmount.get());
		logger.info("Returning from DashboardService :getGraphDataForWidgetEquipment");
		return dashboardFunctionalWidgetData;
	}

	/**
	 * To get if staff resource is Freelance or not.
	 * 
	 * @param staffMember
	 * @return
	 */
	private boolean isResourceFreelance(StaffMember staffMember) {
		logger.info("Inside DashboardService:isResourceFreelance");
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
		logger.info("Returning from DashboardService:isResourceFreelance" + isFreelance);
		return isFreelance;
	}

	/**
	 * Method to export equipment or staff widget Spreadsheet.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param functionType
	 * @return ProjectByStatusDTO
	 * @throws FileNotFoundException
	 */
	public ProjectByStatusDTO exportWidgetPersonnalAndEquipmentSpreadSheet(String startDate, String endDate,
			String functionType) throws FileNotFoundException {

		logger.info("Inside DashboardService :: exportWidgetPersonnalAndEquipmentSpreadSheet(start : " + startDate
				+ ", end : " + endDate + ", functionType : " + functionType + ")");

		if (!functionType.equals("EquipmentFunction") && !functionType.equals("StaffFunction")) {
			return new ProjectByStatusDTO();
		}
		long totalInventoryCount = 0;
		DateRangeDTO range = CommonUtil.validateDateRange(startDate, endDate);

		logger.info("start date ==> " + range.getStartDate() + "  End date ==> " + range.getEndDate());
		List<Booking> booking = bookingService.findByFunctionAndResourceAndStartDateAndEndDate(functionType,
				range.getStartDate(), range.getEndDate());
		List<OrderLine> externalBookings = orderLineService
				.findExternalBookingByFunctionAndResourceAndStartDateAndEndDate(functionType, range.getStartDate(),
						range.getEndDate());

		List<String> header = null;
		Workbook workbook = null;
		List<WidgetDataDTO> widgetDataDTOs = new ArrayList<>();
		if (functionType.equals("StaffFunction")) {
			totalInventoryCount = staffMemberService.findCountByAvailable(true) * range.getDuration();
			header = Arrays.asList(Constants.STAFF_FUNCTION, Constants.PERSONNEL_STRING, Constants.PROJECT_ID,
					Constants.PROJECT_TITLE, Constants.EQUIPMENT_TYPE, Constants.PROJECT_DATE,
					Constants.NUMBER_OF_DAYS);
			workbook = this.createSheetAndAddRowHeader(StringUtils.capitalize(Constants.PERSONNEL_STRING),
					CommonUtil.datePattern(Constants.DATE_PATTERN, range.getStartDate()) + " - "
							+ CommonUtil.datePattern(Constants.DATE_PATTERN, range.getEndDate()),
					CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()), header);
			booking.stream().forEach(t -> {
				long duration = getBookingDuration(range, t);
				WidgetDataDTO widgetDataDTO = new WidgetDataDTO();
				widgetDataDTO.setFunction(t.getFunction().getQualifiedName());
				widgetDataDTO.setProjectTeamId(t.getRecord().getId().toString());
				widgetDataDTO.setProjectname(((AbstractProject) t.getRecord()).getTitle());

				widgetDataDTO.setNumberOfDays("" + duration);
				widgetDataDTO.setProjectDate(CommonUtil.datePattern(Constants.DATE_PATTERN, (t.getRecord()).getDate()));
				StaffResource resource = (StaffResource) t.getResource();
				widgetDataDTO.setResource(resource.getEntity().getFirstName() + " " + resource.getEntity().getName());
				widgetDataDTO
						.setType(isResourceFreelance((StaffMember) resource.getEntity()) ? Constants.STAFF_IS_FREELANCE
								: Constants.ORDER_TYPE_INTERNAL);
				widgetDataDTOs.add(widgetDataDTO);
			});
		}
		if (functionType.equals("EquipmentFunction")) {
			totalInventoryCount = equipmentService.findCountByAvailable(true) * range.getDuration();
			header = Arrays.asList(Constants.STAFF_FUNCTION, StringUtils.capitalize(Constants.EQUIPMENT_STRING),
					Constants.PROJECT_ID, Constants.PROJECT_TITLE, Constants.EQUIPMENT_TYPE, Constants.PROJECT_DATE,
					Constants.NUMBER_OF_DAYS);
			workbook = this.createSheetAndAddRowHeader(StringUtils.capitalize(Constants.EQUIPMENT_STRING),
					CommonUtil.datePattern(Constants.DATE_PATTERN, range.getStartDate()) + " - "
							+ CommonUtil.datePattern(Constants.DATE_PATTERN, range.getEndDate()),
					CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()), header);

			booking.stream().forEach(t -> {

				WidgetDataDTO widgetDataDTO = new WidgetDataDTO();
				long duration = getBookingDuration(range, t);
				widgetDataDTO.setFunction(t.getFunction().getQualifiedName());
				widgetDataDTO.setProjectTeamId(t.getRecord().getId().toString());
				widgetDataDTO.setProjectname(((AbstractProject) t.getRecord()).getTitle());
				widgetDataDTO.setType(Constants.ORDER_TYPE_INTERNAL);
				widgetDataDTO.setNumberOfDays("" + duration);
				widgetDataDTO.setProjectDate(CommonUtil.datePattern(Constants.DATE_PATTERN, (t.getRecord()).getDate()));
				if (functionType.equals("StaffFunction")) {
					StaffResource resource = (StaffResource) t.getResource();
					widgetDataDTO
							.setResource(resource.getEntity().getFirstName() + " " + resource.getEntity().getName());
				} else {
					widgetDataDTO.setResource(t.getResource().getName());
				}
				widgetDataDTOs.add(widgetDataDTO);
			});

			externalBookings.stream().forEach(t -> {
				WidgetDataDTO widgetDataDTO = new WidgetDataDTO();
				long duration = getBookingDuration(range, t);
				widgetDataDTO.setFunction(t.getFunction().getQualifiedName());
				widgetDataDTO.setResource(t.getResource().getName());
				widgetDataDTO.setProjectTeamId(t.getRecord().getId().toString());
				widgetDataDTO.setProjectname(((AbstractProject) t.getRecord().getSource()).getTitle());
				widgetDataDTO.setType(Constants.ORDER_TYPE_EXTERNAL);
				widgetDataDTO.setNumberOfDays("" + duration);
				widgetDataDTO.setProjectDate(CommonUtil.datePattern(Constants.DATE_PATTERN, (t.getRecord()).getDate()));
				String vendorName = t.getRecord().getCompany().getName();
				widgetDataDTO.setResource(vendorName);
				widgetDataDTOs.add(widgetDataDTO);

			});

		}

		List<SpreadsheetDataDTO> spreadsheetData = widgetDataDTOs.stream().map(record -> {
			List<String> list = Arrays.asList(record.getFunction(), record.getResource(), record.getProjectTeamId(),
					record.getProjectname(), record.getType(), record.getProjectDate(), record.getNumberOfDays());
			return new SpreadsheetDataDTO(list);
		}).collect(Collectors.toList());

		logger.info("Importing widget equpiment spreadsheet");
		byte[] spreadsheetByteArray = this.addData(workbook, spreadsheetData, (int) totalInventoryCount + 3);

		ProjectByStatusDTO statusDTO = new ProjectByStatusDTO(widgetDataDTOs.size(), widgetDataDTOs, range.getStatus(),
				CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()).toString(),
				spreadsheetByteArray, true);
		statusDTO.setTimestamp(CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()));
		statusDTO.setPeriod(CommonUtil.datePattern(Constants.DATE_PATTERN, range.getStartDate()) + " - "
				+ CommonUtil.datePattern(Constants.DATE_PATTERN, range.getEndDate()));
		logger.info("Returning from DashboardService :exportWidgetPersonnalAndEquipmentSpreadSheet");
		return statusDTO;

	}

	/**
	 * To Calculate number of working hour for an user on weekly bases.
	 * 
	 * @param userBooking
	 * @param filterLogic
	 * @param calculationLogic
	 * @return time of particular week.
	 */
	private int calculateTime(List<Booking> userBooking, Predicate<Booking> filterLogic,
			Function<Booking, Integer> calculationLogic) {
		logger.info("Importing DashboardService: calculateTime");
		AtomicInteger totalMinutes = new AtomicInteger();
		userBooking.stream().filter(filterLogic).collect(Collectors.toList()).forEach(dt -> {
			totalMinutes.addAndGet(calculationLogic.apply(dt));
		});
		logger.info("Returning from DashboardService: calculateTime");
		return totalMinutes.get();
	}

	/**
	 * Algorithm to get ResourceUsedDays that takes list as argument.
	 * 
	 * @param dateRangeDTO
	 * @param list
	 * @return
	 */
	public int calculateResourceUsedDays(DateRangeDTO dateRangeDTO, List<Booking> list) {
		logger.info("Importing DashboardService: calculateResourceUsedDays");
		return calculateTime(list, dt -> true, booking -> {
			int start = booking.getFrom().getTime().compareTo(dateRangeDTO.getStartDate().getTime());
			int end = dateRangeDTO.getEndDate().getTime().compareTo(booking.getTo().getTime());
			if (start > -1 && end > -1) {
				int value = (int) CommonUtil.dayDifference(booking.getFrom(), booking.getTo());
				return value + 1;
			} else if (start == 1) {
				int value = (int) CommonUtil.dayDifference(booking.getFrom(), dateRangeDTO.getEndDate());
				return value + 1;
			} else if (start == -1 && end != -1) {
				int value = (int) CommonUtil.dayDifference(dateRangeDTO.getStartDate(), booking.getTo());
				return value + 1;
			}
			int value = (int) dateRangeDTO.getDuration();
			return value;
		});
	}

	/**
	 * To get Uses per function maped data.
	 * 
	 * @param dateRangeDTO
	 * @param bookingDTOs
	 * @return Map<String, Integer>
	 */
	private Map<String, Integer> getMapedFunctionalUses(DateRangeDTO dateRangeDTO, List<Booking> bookings) {
		logger.info("Importing DashboardService: getMapedFunctionalUses");
		Map<String, Integer> functionByBookingCount = bookings.stream()
				.collect(Collectors.groupingBy(booking -> booking.getFunction().getQualifiedName())).entrySet().stream()
				.collect(Collectors.toMap(e -> (String) e.getKey(),
						e -> calculateResourceUsedDays(dateRangeDTO, e.getValue())));
		logger.info("Returning From DashboardService: getMapedFunctionalUses");
		return functionByBookingCount;
	}

	/**
	 * Algorithm to get ResourceUsedDays that takes booking object argument.
	 * 
	 * @param dateRangeDTO
	 * @param booking
	 * @return
	 */
	public long getBookingDuration(DateRangeDTO dateRangeDTO, Booking booking) {
		logger.info(
				"Importing DashboardService: getBookingDuration(dateRangeDTO:" + dateRangeDTO + ",booking" + booking);
		int start = booking.getFrom().getTime().compareTo(dateRangeDTO.getStartDate().getTime());
		int end = dateRangeDTO.getEndDate().getTime().compareTo(booking.getTo().getTime());
		if (start > -1 && end > -1) {
			return CommonUtil.dayDifference(booking.getFrom(), booking.getTo()) + 1;
		} else if (start == 1) {
			return CommonUtil.dayDifference(booking.getFrom(), dateRangeDTO.getEndDate()) + 1;
		} else if (start == -1 && end != -1) {
			return CommonUtil.dayDifference(dateRangeDTO.getStartDate(), booking.getTo()) + 1;
		}
		logger.info("Returning From DashboardService: getBookingDuration");
		return dateRangeDTO.getDuration();

	}

	/**
	 * Method to get booking-events by date-range
	 * 
	 * @param startDate
	 * @param endDate
	 * 
	 * @return list of booking-conflicts
	 */
	public DashboardDataDTO getBookingConflicts(String startDate, String endDate, boolean all) {
		logger.info("Inside DashboardService :: getBookingConflicts(start : " + startDate + ", end : " + endDate + ")");
		DateRangeDTO range = CommonUtil.validateDateRange(startDate, endDate);
		List<BookingEventDTO> events = eventService
				.getBookingEventsWithinDateRange(range.getStartDate(), range.getEndDate()).stream().filter(entity -> {
					Record record = entity.getOrigin().getRecord();
					if (record.getStatus() != null && record.getStatus().getKey()
							.equalsIgnoreCase(ProjectStatusName.IN_PROGRESS.getProjectStatusNameString())) {
						return true;
					}
					return false;
				}).map(entity -> new BookingEventDTO(entity, Boolean.TRUE)).collect(Collectors.toList());

		Integer increment = 1;
		List<BookingConflictDTO> conflicts = new ArrayList<BookingConflictDTO>();

		// Generate conflict
		for (BookingEventDTO eventEntity : events) {
			Boolean find = Boolean.FALSE;

			for (BookingConflictDTO conflict : conflicts) {

				if ((eventEntity.getResource() == conflict.getResourceId())
						&& (!eventEntity.getFrom().after(conflict.getEnd()))
						&& (!eventEntity.getTo().before(conflict.getStart()) || eventEntity.getTo() == null)) {

					conflict.getEvents().add(eventEntity);
					if (conflict.getStart().after(eventEntity.getFrom())) {
						conflict.setStart(eventEntity.getFrom());
					}
					if (conflict.getEnd().before(eventEntity.getTo())) {
						conflict.setEnd(eventEntity.getTo());
					}
					find = Boolean.TRUE;
					break;
				}
			}
			if (!find) {
				BookingConflictDTO conflictEntity = new BookingConflictDTO(increment, eventEntity);
				conflictEntity.setPeriod(CommonUtil.datePattern(Constants.DATE_PATTERN, range.getStartDate()) + " - "
						+ CommonUtil.datePattern(Constants.DATE_PATTERN, range.getEndDate()));
				increment += 1;
				conflictEntity.getEvents().add(eventEntity);
				conflicts.add(conflictEntity);
			}
		}

		// getting conflict start time for each event
		conflicts.stream().forEach(conflict -> {

			conflict.getEvents().forEach(bookingEvent -> {

				DateTime startTime = bookingEvent.getStart();
				for (EventDTO eventdto : conflict.getEvents()) {

					if (bookingEvent.getId().longValue() == eventdto.getId().longValue()) {
						continue;
					}
					int compareStartDate = bookingEvent.getStart().compareTo(eventdto.getStart());
					if (compareStartDate >= 0) {
						startTime = bookingEvent.getStart();
						break;
					}
					compareStartDate = startTime.compareTo(eventdto.getStart());
					if (compareStartDate > -1) {
						startTime = eventdto.getStart();
					} else if (bookingEvent.getStart().equals(startTime)) {
						startTime = eventdto.getStart();
					}
				}
				// bookingEvent.setStart(startTime);
				bookingEvent.setConflictStart(
						CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, startTime.toGregorianCalendar()));
			});
		});

		List<BookingConflictDTO> bookingConflicts = conflicts.stream()
				.sorted((o1, o2) -> -1 * o1.getStart().compareTo(o2.getStart())).collect(Collectors.toList());

		DashboardDataDTO dashboardDataDTO = new DashboardDataDTO();
		dashboardDataDTO.setSize(bookingConflicts.size());
		if (all) {
			dashboardDataDTO.setBookingConflicts(bookingConflicts);
		} else {
			bookingConflicts.stream().forEach(booking -> booking.setEvents(null));
			dashboardDataDTO.setBookingConflicts(bookingConflicts.stream().limit(3).collect(Collectors.toList()));
		}
		logger.info("Returning after getting conflicts");
		return dashboardDataDTO;
	}

	/**
	 * To create workbook of time report spreadsheet
	 * 
	 * @param sheetName
	 * @param period
	 * @param timestamp
	 * @param header
	 * @param lastRowCount
	 * @param subHeader
	 * @return Workbook
	 */
	private Workbook createSpreadSheetAndAddRowHeader(String sheetName, String period, String timestamp,
			List<String> header, int lastRowCount, List<String> subHeader) {
		logger.info("Inside DashboardService :: createSpreadSheetAndAddRowHeader(sheetName : " + sheetName
				+ "), to create sheet and row header");

		if (StringUtils.isBlank(sheetName)) {
			logger.info("Invalid sheet name");
			throw new UnprocessableEntityException("Invalid sheet name");
		}
		Workbook workbook = new XSSFWorkbook();
		Sheet weekSheet = workbook.createSheet(StringUtils.capitalize(sheetName));

		if (sheetName.equalsIgnoreCase(Constants.TIME_REPORT_BY_WEEK)) {
			weekSheet.setColumnWidth(0, 10000);
			weekSheet.setColumnWidth(1, 6000);
			weekSheet.setColumnWidth(2, 6000);
			weekSheet.setColumnWidth(3, 6000);
			weekSheet.setColumnWidth(4, 6000);
			weekSheet.setColumnWidth(5, 6000);
		} else if (sheetName.equalsIgnoreCase(Constants.ANNUAL_PERSONNEL_TIME_REPORT)) {
			weekSheet.setColumnWidth(0, 6000);
			weekSheet.setColumnWidth(1, 6000);
			weekSheet.setColumnWidth(2, 6000);
			weekSheet.setColumnWidth(3, 6000);
		} else if (sheetName.equalsIgnoreCase(Constants.WEEKLY_PERSONNEL_TIME_REPORT)) {
			weekSheet.setColumnWidth(0, 6000);
			weekSheet.setColumnWidth(1, 6000);
			weekSheet.setColumnWidth(2, 6000);
			weekSheet.setColumnWidth(3, 10000);
			weekSheet.setColumnWidth(4, 10000);
			weekSheet.setColumnWidth(5, 10000);
		}

		Font font = workbook.createFont();
		font.setBold(true);
		font.setColor(IndexedColors.WHITE.getIndex());

		XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.LEFT);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);

		XSSFColor color = new XSSFColor(new Color(47, 111, 152));
		style.setFillForegroundColor(color);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		for (int rowNumber = 0; rowNumber <= 2; rowNumber++) {
			Row newRow = weekSheet.createRow(rowNumber);
			int colSize = header.size();
			for (int col = 0; col < colSize; col++) {
				newRow.createCell(col);
			}

			if (rowNumber == 0) {
				if (sheetName.equalsIgnoreCase(Constants.TIME_REPORT_BY_WEEK)) {
					weekSheet.addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, 0, 2));
					weekSheet.addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, 3, 5));
					newRow.getCell(0).setCellValue(Constants.PERIOD + " : " + period);
					newRow.getCell(3).setCellValue(Constants.EXPORTED + " : " + timestamp);
				} else if (sheetName.equalsIgnoreCase(Constants.ANNUAL_PERSONNEL_TIME_REPORT)) {
					weekSheet.addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, 0, 1));
					weekSheet.addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, 2, 3));
					newRow.getCell(0).setCellValue(Constants.PERIOD + " : " + period);
					newRow.getCell(2).setCellValue(Constants.EXPORTED + " : " + timestamp);
				} else if (sheetName.equalsIgnoreCase(Constants.WEEKLY_PERSONNEL_TIME_REPORT)) {
					weekSheet.addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, 0, 3));
					weekSheet.addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, 4, 5));
					newRow.getCell(0).setCellValue(Constants.PERIOD + " : " + period);
					newRow.getCell(4).setCellValue(Constants.EXPORTED + " : " + timestamp);
				}

			} else if (rowNumber == 1) {
				for (int col = 0; col < header.size(); col++) {
					newRow.getCell(col).setCellValue(Constants.EMPTY_STRING);
				}
			} else if (rowNumber == 2) {
				for (int col = 0; col < header.size(); col++) {
					newRow.getCell(col).setCellValue(header.get(col));
				}
			}
			style.setFont(font);
			newRow.getCell(0).setCellStyle(style);
			newRow.getCell(1).setCellStyle(style);
			newRow.getCell(2).setCellStyle(style);
			newRow.getCell(3).setCellStyle(style);

			if (sheetName.equalsIgnoreCase(Constants.TIME_REPORT_BY_WEEK)
					|| sheetName.equalsIgnoreCase(Constants.WEEKLY_PERSONNEL_TIME_REPORT)) {
				newRow.getCell(4).setCellStyle(style);
				newRow.getCell(5).setCellStyle(style);
			}

		}

		if (sheetName.equalsIgnoreCase(Constants.ANNUAL_PERSONNEL_TIME_REPORT)
				|| sheetName.equalsIgnoreCase(Constants.WEEKLY_PERSONNEL_TIME_REPORT)) {
			for (int row = 3; row < 5; row++) {
				Row subHeadderRow = weekSheet.createRow(row);
				int columnSize = subHeader.size();

				if (row == 3) {
					subHeadderRow.createCell(0);
					subHeadderRow.createCell(1);
					subHeadderRow.createCell(2);
					subHeadderRow.createCell(3);
					this.setCellColorAndFont(workbook, 255, 255, 0, row, 0, true, false);
					if (sheetName.equalsIgnoreCase(Constants.WEEKLY_PERSONNEL_TIME_REPORT)) {
						this.setCellColorAndFont(workbook, 161, 202, 232, row, 3, true, false);
					}

				} else {
					for (int col = 0; col < columnSize; col++) {
						subHeadderRow.createCell(col);
						subHeadderRow.getCell(col).setCellValue(subHeader.get(col));
						subHeadderRow.getCell(col).setCellStyle(style);
					}
				}
			}
		}

		Row lastRow = weekSheet.createRow(lastRowCount);
		if (sheetName.equalsIgnoreCase(Constants.TIME_REPORT_BY_WEEK)
				|| sheetName.equalsIgnoreCase(Constants.ANNUAL_PERSONNEL_TIME_REPORT)
				|| sheetName.equalsIgnoreCase(Constants.WEEKLY_PERSONNEL_TIME_REPORT)) {
			// Row lastRow = weekSheet.createRow(lastRowCount);
			for (int col = 0; col < header.size(); col++) {
				lastRow.createCell(col);
				lastRow.getCell(col).setCellStyle(style);

			}
		}
		if (sheetName.equalsIgnoreCase(Constants.TIME_REPORT_BY_WEEK)) {
			lastRow.getCell(1).setCellValue(Constants.TOTAL_STRING);
		} else if (sheetName.equalsIgnoreCase(Constants.ANNUAL_PERSONNEL_TIME_REPORT)) {
			for (int row = 2; row < 3; row++) {
				Row lastTwoRow = weekSheet.createRow(lastRowCount + row);

				for (int col = 0; col < 2; col++) {
					lastTwoRow.createCell(col);
					lastTwoRow.getCell(col).setCellStyle(style);
				}
				if (row == 2) {
					lastTwoRow.getCell(0).setCellValue(Constants.OVERTIME_HOURS);
					this.setCellColorAndFont(workbook, 19, 159, 172, lastRowCount + row, 1, false, true);
				} else {
					// change in row < 4
					// lastTwoRow.getCell(0).setCellValue(Constants.SICK_LEAVE_DAYS);
					// this.setCellColorAndFont(workbook, 19, 159, 172, lastRowCount + row, 1,
					// false);

				}

			}
		} else if (sheetName.equalsIgnoreCase(Constants.WEEKLY_PERSONNEL_TIME_REPORT)) {
			for (int row = 2; row < 4; row++) {
				Row lastTwoRow = weekSheet.createRow(lastRowCount + row);
				for (int col = 0; col < 2; col++) {
					lastTwoRow.createCell(col);
					lastTwoRow.getCell(col).setCellStyle(style);
				}
				if (row == 2) {
					lastTwoRow.getCell(0).setCellValue(Constants.BALANCE_STRING);
				} else if (row == 3) {
					lastTwoRow.getCell(0).setCellValue(Constants.OVERTIME_HOURS);
				}

			}
		}

		return workbook;
	}

	/**
	 * Set cell background color
	 * 
	 * @param workbook
	 * @param r
	 * @param g
	 * @param b
	 * @param rowCount
	 * @param colNumber
	 * 
	 * @return Cell
	 */
	private Cell setCellBackgroundColor(Workbook workbook, int r, int g, int b, int rowCount, int colNumber) {
		logger.info("Inside DashboardService :: setCellBackgroundColor() , to set cell background color ");
		Cell cell = workbook.getSheetAt(0).getRow(rowCount).getCell(colNumber);
		XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
		XSSFColor color = new XSSFColor(new Color(r, g, b));
		cellStyle.setFillForegroundColor(color);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cell.setCellStyle(cellStyle);
		logger.info("Returning from setCellBackgroundColor()");
		return cell;
	}

	/**
	 * Add data in time report spreadsheet
	 * 
	 * @param workbook
	 * @param spreadsheetData
	 * @param rowNumber
	 * @return byte code of spreadsheet
	 * @throws FileNotFoundException
	 */
	private byte[] addDataWeekTimeReport(Workbook workbook, List<SpreadsheetDataDTO> spreadsheetData, int rowNumber,
			TimeReportDTO timeReportDTO) throws FileNotFoundException {
		logger.info("Inside DashboardService :: addDataWeekTimeReport(), To add data in spreadsheet");
		Sheet sheet = workbook.getSheetAt(0);
		int rowCount = rowNumber;
		XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		style.setAlignment(HorizontalAlignment.LEFT);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		XSSFCellStyle styleWrap = style;
		XSSFCellStyle rowDefaultStyle = style;
		styleWrap.setWrapText(true);
		for (SpreadsheetDataDTO record : spreadsheetData) {

			Row newRow = sheet.createRow(rowCount);
			List<String> data = record.getData();
			int size = data.size();
			for (int colNumber = 0; colNumber < size; colNumber++) {
				newRow.createCell(colNumber).setCellValue(data.get(colNumber));
				if (sheet.getSheetName().equalsIgnoreCase(Constants.TIME_REPORT_BY_WEEK)) {
					if (colNumber == 2 || colNumber == 3 || colNumber == 5) {
						this.setCellBackgroundColor(workbook, 222, 236, 239, rowCount, colNumber);
					} else if (colNumber == 4) {
						this.setCellBackgroundColor(workbook, 144, 184, 206, rowCount, colNumber);
					} else {
						newRow.getCell(colNumber).setCellStyle(styleWrap);
					}
				} else if (sheet.getSheetName().equalsIgnoreCase(Constants.WEEKLY_PERSONNEL_TIME_REPORT)) {
					if (colNumber == 0 || colNumber == 1 || colNumber == 2) {
						this.setCellBackgroundColor(workbook, 145, 182, 208, rowCount, colNumber);
					}
				} else {
					newRow.getCell(colNumber).setCellStyle(styleWrap);
				}

			}
			rowCount++;
		}

		// set total value on last row

		if (sheet.getSheetName().equalsIgnoreCase(Constants.TIME_REPORT_BY_WEEK)) {
			int lastRow = rowNumber + spreadsheetData.size();
			Row rowLast = workbook.getSheetAt(0).getRow(lastRow);
			rowLast.getCell(3).setCellValue(timeReportDTO.getTotalNominalHours());
			rowLast.getCell(4).setCellValue(timeReportDTO.getTotalWorkedHours());
			rowLast.getCell(5).setCellValue(timeReportDTO.getBalance());
		} else if (sheet.getSheetName().equalsIgnoreCase(Constants.ANNUAL_PERSONNEL_TIME_REPORT)) {
			int lastRow = rowNumber + spreadsheetData.size();
			Row rowLast = workbook.getSheetAt(0).getRow(lastRow);
			rowLast.getCell(1).setCellValue(timeReportDTO.getTotalNominalHours());
			rowLast.getCell(2).setCellValue(timeReportDTO.getTotalWorkedHours());
			rowLast.getCell(3).setCellValue(timeReportDTO.getBalance());

			// set data for overtime hours
			lastRow = lastRow + 2;
			rowLast = workbook.getSheetAt(0).getRow(lastRow);
			rowLast.getCell(1).setCellValue(timeReportDTO.getOverTimeHours());

			// set data for sick leave days
			// lastRow = lastRow + 1;
			// rowLast = workbook.getSheetAt(0).getRow(lastRow);
			// rowLast.getCell(1).setCellValue(timeReportDTO.getBalance());

		} else if (sheet.getSheetName().equalsIgnoreCase(Constants.WEEKLY_PERSONNEL_TIME_REPORT)) {
			int lastRow = rowNumber + spreadsheetData.size();
			Row rowLast = workbook.getSheetAt(0).getRow(lastRow);

			rowLast.getCell(1).setCellValue(timeReportDTO.getTotalWorkedHours());
			rowLast.getCell(2).setCellValue(timeReportDTO.getTotalSickHours());

			// set data for overtime
			lastRow = lastRow + 2;
			rowLast = workbook.getSheetAt(0).getRow(lastRow);
			rowLast.getCell(1).setCellValue(timeReportDTO.getBalance());
			this.setCellColorAndFont(workbook, 255, 192, 0, lastRow, 0, true, true);
			this.setCellColorAndFont(workbook, 255, 192, 0, lastRow, 1, true, true);

			// set data for balance
			lastRow = lastRow + 1;
			rowLast = workbook.getSheetAt(0).getRow(lastRow);
			rowLast.getCell(1).setCellValue(timeReportDTO.getOverTimeHours());
			this.setCellColorAndFont(workbook, 255, 192, 0, lastRow, 0, true, true);
			this.setCellColorAndFont(workbook, 255, 192, 0, lastRow, 1, true, true);

			// set background color of row 4
			this.setCellColorAndFont(workbook, 0, 150, 163, 4, 0, false, true);
			this.setCellColorAndFont(workbook, 0, 150, 163, 4, 1, false, true);
			this.setCellColorAndFont(workbook, 0, 150, 163, 4, 2, false, true);
			this.setCellColorAndFont(workbook, 173, 185, 202, 4, 3, false, true);
			this.setCellColorAndFont(workbook, 173, 185, 202, 4, 4, false, true);
			this.setCellColorAndFont(workbook, 173, 185, 202, 4, 5, false, true);
		}

		// set default row-color on row one
		Row rowOne = workbook.getSheetAt(0).getRow(1);
		XSSFColor color = new XSSFColor(new Color(255, 255, 255));
		rowDefaultStyle.setFillForegroundColor(color);
		for (Cell cell : rowOne) {
			cell.setCellStyle(rowDefaultStyle);
		}

		if (sheet.getSheetName().equalsIgnoreCase(Constants.TIME_REPORT_BY_WEEK)) {
			this.setCellColorAndFont(workbook, 19, 159, 172, 2, 4, false, true);
		} else if (sheet.getSheetName().equalsIgnoreCase(Constants.ANNUAL_PERSONNEL_TIME_REPORT)) {
			this.setCellColorAndFont(workbook, 19, 159, 172, 4, 2, false, true);
			Row row = workbook.getSheetAt(0).getRow(3);
			row.getCell(0).setCellValue(timeReportDTO.getStaffName());
			row.getCell(1).setCellValue(new Long(timeReportDTO.getId()).toString());
			row.getCell(3).setCellValue(String.valueOf(timeReportDTO.getActivityRate()) + "%");
		} else if (sheet.getSheetName().equalsIgnoreCase(Constants.WEEKLY_PERSONNEL_TIME_REPORT)) {
			this.setCellColorAndFont(workbook, 19, 159, 172, 4, 2, false, true);
			Row row = workbook.getSheetAt(0).getRow(3);
			row.getCell(0).setCellValue(timeReportDTO.getStaffName());
			row.getCell(1).setCellValue(new Long(timeReportDTO.getId()).toString());
			row.getCell(2).setCellValue(String.valueOf(timeReportDTO.getTotalNominalHours()));
			row.getCell(3).setCellValue(timeReportDTO.getActivityRate() + "%");
		}

		// writing file into byte-array

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
		logger.info("Returning from addDataWeekTimeReport()");

		return bos.toByteArray();
	}

	/**
	 * Set cell color and font size
	 * 
	 * @param workbook
	 * @param r
	 * @param g
	 * @param b
	 * @param rowCount
	 * @param colNumber
	 * @param isFontBlack
	 * 
	 * @return Cell
	 */
	private Cell setCellColorAndFont(Workbook workbook, int r, int g, int b, int rowCount, int colNumber,
			boolean isFontBlack, boolean isFontBold) {
		logger.info("Inside DashboardService :: setCellColorAndFont() , to set cell background color and font style");
		Cell cell = workbook.getSheetAt(0).getRow(rowCount).getCell(colNumber);
		XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
		XSSFColor color = new XSSFColor(new Color(r, g, b));

		cellStyle.setFillForegroundColor(color);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		Font font = workbook.createFont();
		if (isFontBlack) {
			font.setColor(IndexedColors.BLACK.getIndex());
		} else {
			font.setColor(IndexedColors.WHITE.getIndex());
		}
		if (isFontBold) {
			font.setBold(true);
		} else {
			font.setBold(false);
			font.setFontHeightInPoints((short) 10);
		}
		cellStyle.setFont(font);
		cell.setCellStyle(cellStyle);
		logger.info("Returning from setCellColorAndFont()");
		return cell;

	}

	/**
	 * To export Week Time Report Spreadsheet
	 * 
	 * @param start
	 * @param end
	 * 
	 * @return ProjectByStatusDTO
	 * 
	 */
	public ProjectByStatusDTO exportWeekTimeReportSpreadsheet(String start, String end, long staffMemberId)
			throws IOException {
		logger.info(
				"Inside DashboardService :: exportWeekTimeReportSpreadsheet(start : " + start + ", end : " + end + ")");
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get week time report spreadsheet.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		DateRangeDTO range = CommonUtil.validateDateRange(start, end);
		Date startDate = range.getStartDate().getTime();
		Date endDate = range.getEndDate().getTime();
		TimeReportDTO timeReportDTO = this.staffMemberService.findTimeReportById(staffMemberId, startDate, endDate,
				Constants.TIME_REPORT_BY_WEEK);
		List<ReportDTO> reports = timeReportDTO.getReport();
		int lastRowCount = reports.size() + 3;
		List<String> header = Arrays.asList(Constants.PERSONNEL_STRING, Constants.PERSONNEL_ID, Constants.WEEK_STRING,
				Constants.NOMINAL_HOUR, Constants.WORKED_HOURS, Constants.BALANCE_STRING);

		List<String> subHeader = null;
		Workbook workbook = this.createSpreadSheetAndAddRowHeader(Constants.TIME_REPORT_BY_WEEK,
				CommonUtil.datePattern(Constants.DATE_PATTERN, range.getStartDate()) + " - "
						+ CommonUtil.datePattern(Constants.DATE_PATTERN, range.getEndDate()),
				CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()), header, lastRowCount,
				subHeader);

		double totalWorkedHours = reports.stream().mapToDouble(i -> i.getWorkedHour()).sum();
		long totalNominalHours = timeReportDTO.getNominalHour() * reports.size();

		timeReportDTO.setTotalNominalHours(String.valueOf(totalNominalHours));
		timeReportDTO.setTotalWorkedHours(String.valueOf(totalWorkedHours));
		timeReportDTO.setBalance(String.valueOf(totalWorkedHours - (double) totalNominalHours));
		timeReportDTO.setId(staffMemberId);

		List<SpreadsheetDataDTO> spreadsheetData = reports.stream().map(record -> {
			List<String> list = Arrays.asList(timeReportDTO.getStaffName(), new Long(timeReportDTO.getId()).toString(),
					String.valueOf(record.getWeek()), new Long(timeReportDTO.getNominalHour()).toString(),
					String.valueOf(record.getWorkedHour()), String.valueOf(record.getBalance()));
			return new SpreadsheetDataDTO(list);
		}).collect(Collectors.toList());

		logger.info("Importing week time report spreadsheet");
		byte[] spreadsheetByteArray = this.addDataWeekTimeReport(workbook, spreadsheetData, 3, timeReportDTO);

		ProjectByStatusDTO statusDTO = new ProjectByStatusDTO(reports.size(), range.getStatus(),
				CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()).toString(), reports,
				spreadsheetByteArray);
		statusDTO.setTimestamp(CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()));
		statusDTO.setPeriod(CommonUtil.datePattern(Constants.DATE_PATTERN, range.getStartDate()) + " - "
				+ CommonUtil.datePattern(Constants.DATE_PATTERN, range.getEndDate()));
		statusDTO.setNominalHour(new Long(timeReportDTO.getNominalHour()).toString());
		logger.info("Returning after importing week time report spreadsheet");
		return statusDTO;
	}

	/**
	 * To export Annual Personnel Time Report
	 * 
	 * @param start
	 * @param end
	 * 
	 * @return ProjectByStatusDTO
	 */
	public ProjectByStatusDTO exportAnnualPersonnelTimeReport(String start, String end, long staffMemberId)
			throws IOException {
		logger.info(
				"Inside DashboardService :: exportAnnualPersonnelTimeReport(start : " + start + ", end : " + end + ")");
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get annual personnel time report spreadsheet.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		DateRangeDTO range = CommonUtil.validateDateRange(start, end);
		Date startDate = range.getStartDate().getTime();
		Date endDate = range.getEndDate().getTime();
		TimeReportDTO timeReportDTO = this.staffMemberService.findTimeReportById(staffMemberId, startDate, endDate,
				Constants.ANNUAL_PERSONNEL_TIME_REPORT);
		List<ReportDTO> reports = timeReportDTO.getReport();
		int topFiveRow = 5;
		int lastRowCount = reports.size() + topFiveRow;
		List<String> header = Arrays.asList(Constants.PERSONNEL_STRING, Constants.PERSONNEL_ID, " ",
				Constants.ACTIVITY_RATE);

		List<String> subHeader = Arrays.asList(Constants.MONTH_STRING, Constants.NOMINAL_HOUR, Constants.WORKED_HOURS,
				Constants.BALANCE_STRING);

		Workbook workbook = this.createSpreadSheetAndAddRowHeader(Constants.ANNUAL_PERSONNEL_TIME_REPORT,
				CommonUtil.datePattern(Constants.DATE_PATTERN, range.getStartDate()) + " - "
						+ CommonUtil.datePattern(Constants.DATE_PATTERN, range.getEndDate()),
				CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()), header, lastRowCount,
				subHeader);

		double totalWorkedHours = reports.stream().mapToDouble(i -> i.getWorkedHour()).sum();
		long totalNominalHours = timeReportDTO.getNominalHour() * reports.size();
		timeReportDTO.setBalance(String.valueOf(totalWorkedHours - (double) totalNominalHours));
		timeReportDTO.setTotalNominalHours(String.valueOf(totalNominalHours));
		timeReportDTO.setTotalWorkedHours(String.valueOf(totalWorkedHours));
		timeReportDTO.setId(staffMemberId);

		if ((totalWorkedHours - (double) totalNominalHours) > 0) {
			timeReportDTO.setOverTimeHours(String.valueOf(totalWorkedHours - (double) totalNominalHours));
		} else {
			timeReportDTO.setOverTimeHours("0.0");
		}

		List<SpreadsheetDataDTO> spreadsheetData = reports.stream().map(record -> {
			List<String> list = Arrays.asList(record.getMonthName(),
					new Long(timeReportDTO.getNominalHour()).toString(), String.valueOf(record.getWorkedHour()),
					String.valueOf(record.getBalance()));
			return new SpreadsheetDataDTO(list);
		}).collect(Collectors.toList());

		logger.info("Importing annual personnel time report spreadsheet");
		byte[] spreadsheetByteArray = this.addDataWeekTimeReport(workbook, spreadsheetData, 5, timeReportDTO);

		ProjectByStatusDTO statusDTO = new ProjectByStatusDTO(reports.size(), range.getStatus(),
				CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()).toString(), reports,
				spreadsheetByteArray);
		statusDTO.setTimestamp(CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()));
		statusDTO.setPeriod(CommonUtil.datePattern(Constants.DATE_PATTERN, range.getStartDate()) + " - "
				+ CommonUtil.datePattern(Constants.DATE_PATTERN, range.getEndDate()));
		statusDTO.setNominalHour(new Long(timeReportDTO.getNominalHour()).toString());
		logger.info("Returning after importing annual personnel time report spreadsheet");
		return statusDTO;
	}

	/**
	 * To export Annual Personnel Time Report
	 * 
	 * @param start
	 * @param end
	 * @return ProjectByStatusDTO
	 * @throws IOException
	 */
	public ProjectByStatusDTO exportWeeklyPersonnelTimeReport(String start, String end, long staffMemberId)
			throws IOException {
		logger.info(
				"Inside DashboardService :: exportWeeklyPersonnelTimeReport(start : " + start + ", end : " + end + ")");
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get weekly personnel time report spreadsheet.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		DateRangeDTO range = CommonUtil.validateDateRange(start, end);
		Date startDate = range.getStartDate().getTime();
		Date endDate = range.getEndDate().getTime();
		TimeReportDTO timeReportDTO = this.staffMemberService.findTimeReportById(staffMemberId, startDate, endDate,
				Constants.WEEKLY_PERSONNEL_TIME_REPORT);
		List<ReportDTO> reports = timeReportDTO.getReport();
		int topFiveRow = 5;
		int lastRowCount = reports.size() + topFiveRow;
		List<String> header = Arrays.asList(Constants.PERSONNEL_STRING, Constants.PERSONNEL_ID, Constants.NOMINAL_HOUR,
				Constants.ACTIVITY_RATE, " ", " ");

		List<String> subHeader = Arrays.asList(Constants.DATE_STRING, Constants.WORKED_HOURS, Constants.SICK_HOURS,
				StringUtils.capitalize(Constants.PROJECT_STRING), Constants.STAFF_FUNCTION, Constants.PERSONNEL_STRING);

		Workbook workbook = this.createSpreadSheetAndAddRowHeader(Constants.WEEKLY_PERSONNEL_TIME_REPORT,
				CommonUtil.datePattern(Constants.DATE_PATTERN, range.getStartDate()) + " - "
						+ CommonUtil.datePattern(Constants.DATE_PATTERN, range.getEndDate()),
				CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()), header, lastRowCount,
				subHeader);

		int weeks = Weeks.weeksBetween((new DateTime(startDate)).dayOfWeek().withMinimumValue().minusDays(1),
				new DateTime(endDate).dayOfWeek().withMaximumValue().plusDays(1)).getWeeks();

		double totalWorkedHours = reports.stream().mapToDouble(i -> i.getWorkedHour()).sum();
		double totalSickHours = reports.stream().mapToDouble(i -> i.getSickHourDuration()).sum();
		long totalNominalHours = timeReportDTO.getNominalHour() * weeks;
		timeReportDTO.setTotalWorkedHours(String.valueOf(totalWorkedHours));
		timeReportDTO.setTotalNominalHours(String.valueOf(totalNominalHours));
		timeReportDTO.setBalance(String.valueOf(totalWorkedHours - (double) totalNominalHours));
		timeReportDTO.setTotalSickHours(String.valueOf(totalSickHours));
		timeReportDTO.setId(staffMemberId);
		if ((totalWorkedHours - (double) totalNominalHours) > 0) {
			timeReportDTO.setOverTimeHours(String.valueOf(totalWorkedHours - (double) totalNominalHours));
		} else {
			timeReportDTO.setOverTimeHours("0.0");
		}

		List<SpreadsheetDataDTO> spreadsheetData = reports.stream().map(record -> {
			List<String> list = Arrays.asList(record.getDateString(), String.valueOf(record.getWorkedHour()),
					String.valueOf(record.getSickHourDuration()), record.getProjectName(), record.getFunctionName(),
					record.getDescription());
			return new SpreadsheetDataDTO(list);
		}).collect(Collectors.toList());

		logger.info("Importing weekly personnel time report spreadsheet");
		byte[] spreadsheetByteArray = this.addDataWeekTimeReport(workbook, spreadsheetData, 5, timeReportDTO);

		ProjectByStatusDTO statusDTO = new ProjectByStatusDTO(reports.size(), range.getStatus(),
				CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()).toString(), reports,
				spreadsheetByteArray);
		statusDTO.setTimestamp(CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, Calendar.getInstance()));
		statusDTO.setPeriod(CommonUtil.datePattern(Constants.DATE_PATTERN, range.getStartDate()) + " - "
				+ CommonUtil.datePattern(Constants.DATE_PATTERN, range.getEndDate()));
		statusDTO.setNominalHour(String.valueOf(totalNominalHours));
		logger.info("Returning after importing weekly personnel time report spreadsheet");
		return statusDTO;
	}

	/**
	 * To get all Unscheduled bookings
	 * 
	 * @param startDate
	 * @param endDate
	 * 
	 * @return widgetDataDTOs
	 */
	public DashboardDataDTO getUnscheduledBooking(String startDate, String endDate, boolean all) {
		logger.info("Inside DashboardService :: getUnscheduledBooking(start : " + startDate + ", end : " + endDate
				+ ") , To get all Unscheduled bookings");
		DateRangeDTO range = CommonUtil.validateDateRange(startDate, endDate);

		List<Booking> bookings = bookingRepository.findByProjectAndStartDateAndEndDate(range.getStartDate(),
				range.getEndDate(), Project.class);
		bookings = bookings.stream().filter(b -> b.getResource() instanceof DefaultResource)
				.collect(Collectors.toList());

		List<WidgetDataDTO> widgetDataDTOs = new ArrayList<>();
		bookings.stream().forEach(t -> {
			WidgetDataDTO widgetDataDTO = new WidgetDataDTO();
			widgetDataDTO.setFunction(t.getFunction().getQualifiedName());
			widgetDataDTO.setProjectTeamId(t.getRecord().getId().toString());
			widgetDataDTO.setProjectname(((AbstractProject) t.getRecord()).getTitle());
			widgetDataDTO.setProjectDate(CommonUtil.datePattern(Constants.DATE_PATTERN, (t.getRecord()).getDate()));
			widgetDataDTO.setStartTime(CommonUtil.datePattern(Constants.TIMESTAMP_PATTERN, t.getFrom()));

			widgetDataDTOs.add(widgetDataDTO);
		});

		DashboardDataDTO dashboardDataDTO = new DashboardDataDTO();
		dashboardDataDTO.setSize(widgetDataDTOs.size());
		if (all) {
			dashboardDataDTO.setWidgetDataDTOs(widgetDataDTOs);
		} else {
			dashboardDataDTO.setWidgetDataDTOs(widgetDataDTOs.stream().limit(3).collect(Collectors.toList()));
		}

		logger.info("Returning after getting all Unscheduled booking ");
		return dashboardDataDTO;
	}

}
