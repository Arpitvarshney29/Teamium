package com.teamium.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Wrapper class for Project(Booking) by status and download spreadsheet data
 * 
 * @author Teamium
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ProjectByStatusDTO {

	private String status;
	private int count;
	private List<QuotationDTO> projects;
	private List<WidgetDataDTO> widgetDataDTOs;
	private List<ATACarnetReportDTO> carnetReportDTOs;

	// fields for spreadsheet download
	private byte[] spreadsheetFile;
	private String period;
	private String timestamp;
	private String totalWithoutTax;
	private String actualComparison;
	private Map<String, Integer> functionPerUses;
	private List<ReportDTO> reports;
	private String nominalHour;

	public ProjectByStatusDTO() {

	}

	public ProjectByStatusDTO(int count, List<ATACarnetReportDTO> carnetReportDTOs, byte[] spreadsheetFile) {
		this.count = count;
		this.carnetReportDTOs = carnetReportDTOs;
		this.spreadsheetFile = spreadsheetFile;
	}

	public ProjectByStatusDTO(int count, List<WidgetDataDTO> widgetDataDTOs, String period, String timestamp,
			byte[] spreadsheetFile, boolean isCheck) {
		this.count = count;
		this.widgetDataDTOs = widgetDataDTOs;
		this.period = period;
		this.timestamp = timestamp;
		this.spreadsheetFile = spreadsheetFile;
	}

	public ProjectByStatusDTO(Map<String, Integer> functionPerUses, String period, String timestamp,
			byte[] spreadsheetFile) {
		this.functionPerUses = functionPerUses;
		this.period = period;
		this.timestamp = timestamp;
		this.spreadsheetFile = spreadsheetFile;
	}

	public ProjectByStatusDTO(int count, List<QuotationDTO> projects, String period, String timestamp,
			byte[] spreadsheetFile) {
		this.count = count;
		this.projects = projects;
		this.period = period;
		this.timestamp = timestamp;
		this.spreadsheetFile = spreadsheetFile;
	}

	public ProjectByStatusDTO(int count, List<QuotationDTO> projects, String period, String timestamp) {
		this.count = count;
		this.projects = projects;
		this.period = period;
		this.timestamp = timestamp;
	}

	public ProjectByStatusDTO(String totalWithoutTax) {
		this.totalWithoutTax = totalWithoutTax;
	}

	public ProjectByStatusDTO(String actualComparison, int count, List<QuotationDTO> projects, Boolean comparison) {
		this.actualComparison = actualComparison;
		this.count = count;
		this.projects = projects;
	}

	public ProjectByStatusDTO(String status, int count, List<QuotationDTO> projects) {
		this.status = status;
		this.count = count;
		this.projects = projects;
	}

	public ProjectByStatusDTO(String actualComparison, int count, List<QuotationDTO> projects, byte[] spreadsheetFile) {
		this.actualComparison = actualComparison;
		this.count = count;
		this.projects = projects;
		this.spreadsheetFile = spreadsheetFile;
	}

	public ProjectByStatusDTO(String status, int count, List<QuotationDTO> projects, byte[] spreadsheetFile,
			String totalWithoutTax) {
		this.status = status;
		this.count = count;
		this.projects = projects;
		this.spreadsheetFile = spreadsheetFile;
		this.totalWithoutTax = totalWithoutTax;
	}

	public ProjectByStatusDTO(String status, int count, List<QuotationDTO> projects, byte[] spreadsheetFile,
			String period, String timestamp) {
		this.status = status;
		this.count = count;
		this.projects = projects;
		this.spreadsheetFile = spreadsheetFile;
		this.period = period;
		this.timestamp = timestamp;
	}

	public ProjectByStatusDTO(int size, String status, String timestamp, List<ReportDTO> reports,
			byte[] spreadsheetFile) {
		this.count = size;
		this.status = status;
		this.timestamp = timestamp;
		this.reports = reports;
		this.spreadsheetFile = spreadsheetFile;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the projects
	 */
	public List<QuotationDTO> getProjects() {
		if (this.projects == null) {
			this.projects = new ArrayList<QuotationDTO>();
		}
		return projects;
	}

	/**
	 * @param projects
	 *            the projects to set
	 */
	public void setProjects(List<QuotationDTO> projects) {
		this.projects = projects;
	}

	/**
	 * @return the spreadsheetFile
	 */
	public byte[] getSpreadsheetFile() {
		return spreadsheetFile;
	}

	/**
	 * @param spreadsheetFile
	 *            the spreadsheetFile to set
	 */
	public void setSpreadsheetFile(byte[] spreadsheetFile) {
		this.spreadsheetFile = spreadsheetFile;
	}

	/**
	 * @return the period
	 */
	public String getPeriod() {
		return period;
	}

	/**
	 * @param period
	 *            the period to set
	 */
	public void setPeriod(String period) {
		this.period = period;
	}

	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the totalWithoutTax
	 */
	public String getTotalWithoutTax() {
		return totalWithoutTax;
	}

	/**
	 * @param totalWithoutTax
	 *            the totalWithoutTax to set
	 */
	public void setTotalWithoutTax(String totalWithoutTax) {
		this.totalWithoutTax = totalWithoutTax;
	}

	/**
	 * @return the actualComparison
	 */
	public String getActualComparison() {
		return actualComparison;
	}

	/**
	 * @param actualComparison
	 *            the actualComparison to set
	 */
	public void setActualComparison(String actualComparison) {
		this.actualComparison = actualComparison;
	}

	/**
	 * @return the functionPerUses
	 */
	public Map<String, Integer> getFunctionPerUses() {
		return functionPerUses;
	}

	/**
	 * @param functionPerUses
	 *            the functionPerUses to set
	 */
	public void setFunctionPerUses(Map<String, Integer> functionPerUses) {
		this.functionPerUses = functionPerUses;
	}

	/**
	 * 
	 * @return
	 */
	public List<WidgetDataDTO> getWidgetDataDTOs() {
		return widgetDataDTOs;
	}

	/**
	 * 
	 * @param widgetDataDTOs
	 */
	public void setWidgetDataDTOs(List<WidgetDataDTO> widgetDataDTOs) {
		this.widgetDataDTOs = widgetDataDTOs;
	}

	/**
	 * 
	 * @return
	 */
	public List<ATACarnetReportDTO> getCarnetReportDTOs() {
		return carnetReportDTOs;
	}
	
	/**
	 * 
	 * @param carnetReportDTOs
	 */
	public void setCarnetReportDTOs(List<ATACarnetReportDTO> carnetReportDTOs) {
		this.carnetReportDTOs = carnetReportDTOs;
	}

	/**
	 * 
	 * @return
	 */
	public List<ReportDTO> getReports() {
		return reports;
	}

	/**
	 * @param reports
	 */
	public void setReports(List<ReportDTO> reports) {
		this.reports = reports;
	}

	/**
	 * 
	 * @return
	 */
	public String getNominalHour() {
		return nominalHour;
	}

	/**
	 * 
	 * @param nominalHour
	 */
	public void setNominalHour(String nominalHour) {
		this.nominalHour = nominalHour;
	}

}
