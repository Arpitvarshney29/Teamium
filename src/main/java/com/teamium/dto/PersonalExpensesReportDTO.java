package com.teamium.dto;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import com.teamium.domain.PersonalExpensesReport;
import com.teamium.domain.prod.projects.Project;

public class PersonalExpensesReportDTO {

	private Long id;

	private ProjectDTO project;

	private StaffMemberDTO staffMember;

	private Set<ExpensesItemDTO> expensesItems = new HashSet<>();

	private Calendar date;

	private String status;

	private Double totalPersonalExpenses;

	private Double totalCompanyCardExpenses;

	private Calendar from;

	private Calendar to;

	public PersonalExpensesReportDTO() {

	}

	public PersonalExpensesReportDTO(PersonalExpensesReport personalExpensesReport) {

		this.id = personalExpensesReport.getId();
		Project project = personalExpensesReport.getProject();
		if (project != null) {
			this.project = new ProjectDTO(project.getId(),
					project.getCategory() != null ? project.getCategory().getName() : "", project.getTitle());
		}

		this.staffMember = new StaffMemberDTO(personalExpensesReport.getStaffMember(), "", true);
		this.expensesItems = personalExpensesReport.getExpensesItems().stream()
				.map(item -> new ExpensesItemDTO(item, true)).collect(Collectors.toSet());
		this.date = personalExpensesReport.getDate();
		this.date.setTimeZone(TimeZone.getTimeZone("UTC"));
		this.status = personalExpensesReport.getStatus();
		this.totalPersonalExpenses = personalExpensesReport.getTotalPersonalExpenses();
		this.totalCompanyCardExpenses = personalExpensesReport.getTotalCompanyCardExpenses();
	}
//	public PersonalExpensesReportDTO(PersonalExpensesReport personalExpensesReport,boolean ischeck) {
//
//		this.id = personalExpensesReport.getId();
//		Project project = personalExpensesReport.getProject();
//		if (project != null) {
//			this.project = new ProjectDTO(project.getId(), project.getCompany()!=null ? project.getCompany().getHeader():"",
//					project.getCategory() != null ? project.getCategory().getName() : "", project.getTitle(),true);
//		}
//
//		this.staffMember = new StaffMemberDTO(personalExpensesReport.getStaffMember(),"", true);
//		this.expensesItems = personalExpensesReport.getExpensesItems().stream().map(item->new ExpensesItemDTO(item,true)).collect(Collectors.toSet());
//		this.date = personalExpensesReport.getDate();
//		this.date.setTimeZone(TimeZone.getTimeZone("UTC"));
//		this.status = personalExpensesReport.getStatus();
//		this.totalPersonalExpenses = personalExpensesReport.getTotalPersonalExpenses();
//		this.totalCompanyCardExpenses = personalExpensesReport.getTotalCompanyCardExpenses();
//	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the project
	 */
	public ProjectDTO getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(ProjectDTO project) {
		this.project = project;
	}

	/**
	 * @return the staffMember
	 */
	public StaffMemberDTO getStaffMember() {
		return staffMember;
	}

	/**
	 * @param staffMember the staffMember to set
	 */
	public void setStaffMember(StaffMemberDTO staffMember) {
		this.staffMember = staffMember;
	}

	/**
	 * @return the expensesItems
	 */
	public Set<ExpensesItemDTO> getExpensesItems() {
		return expensesItems;
	}

	/**
	 * @param expensesItems the expensesItems to set
	 */
	public void setExpensesItems(Set<ExpensesItemDTO> expensesItems) {
		this.expensesItems = expensesItems;
	}

	/**
	 * @return the date
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the totalPersonalExpenses
	 */
	public Double getTotalPersonalExpenses() {
		return totalPersonalExpenses;
	}

	/**
	 * @param totalPersonalExpenses the totalPersonalExpenses to set
	 */
	public void setTotalPersonalExpenses(Double totalPersonalExpenses) {
		this.totalPersonalExpenses = totalPersonalExpenses;
	}

	/**
	 * @return the totalCompanyCardExpenses
	 */
	public Double getTotalCompanyCardExpenses() {
		return totalCompanyCardExpenses;
	}

	/**
	 * @param totalCompanyCardExpenses the totalCompanyCardExpenses to set
	 */
	public void setTotalCompanyCardExpenses(Double totalCompanyCardExpenses) {
		this.totalCompanyCardExpenses = totalCompanyCardExpenses;
	}

	/**
	 * @return the from
	 */
	public Calendar getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(Calendar from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public Calendar getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(Calendar to) {
		this.to = to;
	}

}
