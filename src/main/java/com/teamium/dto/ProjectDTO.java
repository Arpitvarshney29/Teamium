package com.teamium.dto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.prod.DueDate;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.projects.Project;
import com.teamium.domain.prod.projects.Quotation;
import com.teamium.utils.CommonUtil;

/**
 * Project Wrapper class
 * 
 * @author Teamium
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ProjectDTO extends QuotationDTO {

	private List<InOutDTO> inOuts = new ArrayList<InOutDTO>();
	private long pendingBookings = 0;
	private List<BookingDTO> externalLinesForOrder = new ArrayList<>();
	private List<SupplierDTO> vendorListForOrder = new ArrayList<>();
	private ContractDTO contractDTO;
	private Long newlyBookingId;

	public ProjectDTO() {

	}
	
	public ProjectDTO(Project project) {
		super(project);
		if (project.getContract() != null) {
			contractDTO = new ContractDTO(project.getContract());
		}
	}

	public ProjectDTO(long id, String category, String title) {
		this.setId(id);
		this.setCategory(category);
		this.setTitle(title);
	}

	public ProjectDTO(Project project, Boolean forExpenseDropdown) {
		this.setId(project.getId());
		this.setCategory(project.getCategory() != null ? project.getCategory().getName() : "");
		this.setTitle(project.getTitle());
		this.setStatus(project.getStatus() != null ? project.getStatus().getKey() : "");
		this.setFinancialStatus(project.getFinancialStatus());
		this.setStartDate(project.getStartDate());
		this.setEndDate(project.getEndDate());
		this.setDate(project.getDate());
		if (project.getSource() != null) {
			this.setSource(new RecordDTO(project.getSource(), Boolean.TRUE));
		}
	}

	public ProjectDTO(long id, String category, String title, String status) {
		this.setId(id);
		this.setCategory(category);
		this.setTitle(title);
		this.setStatus(status);
	}

	public ProjectDTO(Project project, String recordDiscriminator) {
		if (project != null) {
			this.setId(project.getId());
			this.setCategory(project.getCategory() != null ? project.getCategory().getName() : "");
			this.setTitle(project.getTitle());
			this.setStatus(project.getStatus() != null ? project.getStatus().getKey() : "");
			this.setRecordDiscriminator(recordDiscriminator);
			if (project.getCompany() != null) {
				this.setCompany(new CompanyDTO(project.getCompany()));
			}
			this.setCity(project.getCity());
			this.setCountry(project.getCountry() != null ? project.getCountry().getKey() : "");

			Optional<DueDate> optional = project.getDueDates().stream()
					.filter(d -> d.getType() != null && d.getType().getName().equalsIgnoreCase("delivery")).findFirst();
			if (optional.isPresent()) {
				DueDate dueDate = optional.get();
				this.setDueDateForListView(CommonUtil.getStringFromCalendar(dueDate.getDate(), "dd/MM/yyyy"));
			}
			if (project.getTheme() != null) {
				this.setTheme(project.getTheme().getKey());
			}
			if (project.getContract() != null) {
				contractDTO = new ContractDTO(project.getContract());
			}
			if (project.getSource() != null) {
				this.setSource(new RecordDTO(project.getSource(), Boolean.TRUE));
			}
			this.setRecordDiscriminator(recordDiscriminator);
			this.setBookingTotalIVAT(project.getTotalPriceIVAT());
		}
	}

	public ProjectDTO(long id, String category, String title, String status, String recordDiscriminator,
			String templateTitle, String theme) {
		this.setId(id);
		this.setCategory(category);
		this.setTitle(title);
		this.setStatus(status);
		this.setRecordDiscriminator(recordDiscriminator);
		this.setTemplateTitle(templateTitle);
		this.setTheme(theme);

	}

	public ProjectDTO(Project project, String recordDiscriminator, long pendingBooking, String templateTitle) {
		if (project != null) {
			this.setId(project.getSource() != null ? project.getSource().getId() : 0l);
			this.setCategory(project.getCategory() != null ? project.getCategory().getName() : "");
			this.setTitle(project.getTitle());
			this.setStatus(project.getStatus() != null ? project.getStatus().getKey() : "");
			this.setRecordDiscriminator(recordDiscriminator);
			if (project.getCompany() != null) {
				this.setCompany(new CompanyDTO(project.getCompany()));
			}
			this.setCity(project.getCity());
			this.setCountry(project.getCountry() != null ? project.getCountry().getKey() : "");
			this.setPendingBookings(pendingBooking);
			this.setTemplateTitle(templateTitle);

			if (project.getDueDates() != null && !project.getDueDates().isEmpty()) {
				Optional<DueDate> optional = project.getDueDates().stream()
						.filter(d -> d.getType() != null && d.getType().getName().equalsIgnoreCase("delivery"))
						.findFirst();
				if (optional.isPresent()) {
					DueDate dueDate = optional.get();
					this.setDueDateForListView(CommonUtil.getStringFromCalendar(dueDate.getDate(), "dd/MM/yyyy"));
				}
			}
			if (project.getTheme() != null) {
				this.setTheme(project.getTheme().getKey());
			}
			if (project.getContract() != null) {
				contractDTO = new ContractDTO(project.getContract());
			}
		}
	}

	public ProjectDTO(long id, String category, String title, String status, String recordDiscriminator) {
		this.setId(id);
		this.setCategory(category);
		this.setTitle(title);
		this.setStatus(status);
		this.setRecordDiscriminator(recordDiscriminator);
	}

	public ProjectDTO(long id, String category, String title, String status, String recordDiscriminator,
			long pendingBooking, String templateTitle) {
		this.setId(id);
		this.setCategory(category);
		this.setTitle(title);
		this.setStatus(status);
		this.setRecordDiscriminator(recordDiscriminator);
		this.setPendingBookings(pendingBooking);
		this.setTemplateTitle(templateTitle);
	}

	public ProjectDTO(Quotation qotation) {
		super(qotation);
	}

	public ProjectDTO(Calendar startDate, Calendar endDate, Integer minuteDuration, String idPad) {
		super(startDate, endDate, minuteDuration, idPad);
	}

	/**
	 * @return the inOuts
	 */
	public List<InOutDTO> getInOuts() {
		return inOuts;
	}

	/**
	 * @param inOuts
	 *            the inOuts to set
	 */
	public void setInOuts(List<InOutDTO> inOuts) {
		this.inOuts = inOuts;
	}

	/**
	 * @return the pendingBookings
	 */
	public long getPendingBookings() {
		return pendingBookings;
	}

	/**
	 * @param pendingBookings
	 *            the pendingBookings to set
	 */
	public void setPendingBookings(long pendingBookings) {
		this.pendingBookings = pendingBookings;
	}

	public void setProjectDetail(Project project, ProjectDTO projectDTO) {
		projectDTO.setQuotationDetail(project, projectDTO);
	}

	@JsonIgnore
	public Project getProject(Project project, ProjectDTO projectDTO) {
		projectDTO.setQuotationDetail(project, projectDTO);
		return project;
	}

	@JsonIgnore
	public Project getProject(Project project) {
		this.setProjectDetail(project, this);
		return project;
	}

	/**
	 * @return the vendorListForOrder
	 */
	public List<SupplierDTO> getVendorListForOrder() {
		return vendorListForOrder;
	}

	/**
	 * @param vendorListForOrder
	 *            the vendorListForOrder to set
	 */
	public void setVendorListForOrder(List<SupplierDTO> vendorListForOrder) {
		this.vendorListForOrder = vendorListForOrder;
	}

	/**
	 * @return the externalLinesForOrder
	 */
	public List<BookingDTO> getExternalLinesForOrder() {
		return externalLinesForOrder;
	}

	/**
	 * @param externalLinesForOrder
	 *            the externalLinesForOrder to set
	 */
	public void setExternalLinesForOrder(List<BookingDTO> externalLinesForOrder) {
		this.externalLinesForOrder = externalLinesForOrder;
	}

	/**
	 * @return the contractDTO
	 */
	public ContractDTO getContractDTO() {
		return contractDTO;
	}

	/**
	 * @param contractDTO
	 *            the contractDTO to set
	 */
	public void setContractDTO(ContractDTO contractDTO) {
		this.contractDTO = contractDTO;
	}

	/**
	 * @return the newlyBookingId
	 */
	public Long getNewlyBookingId() {
		return newlyBookingId;
	}

	/**
	 * @param newlyBookingId
	 *            the newlyBookingId to set
	 */
	public void setNewlyBookingId(Long newlyBookingId) {
		this.newlyBookingId = newlyBookingId;
	}

}
