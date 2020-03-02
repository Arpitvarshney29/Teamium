package com.teamium.dto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.projects.invoice.Invoice;

public class InvoiceDTO extends RecordDTO {

	private String invoiceNumber;
	private long projectId;
	private long contactId;
	private String startDate;
	private String endDate;
	private String location;
	private String orgnization;
	private String comment;
	private int percentage;
	private boolean finalInvoice;

	private String title;
	private String projectStatus;
	private String financialStatus;
	private String managedBy;
	private String origin;
	private String comapny;
	private String currency;
	private String language;
	private String internalReference;

	private List<BookingDTO> bookingDTOs;

	public InvoiceDTO() {
	}

	public InvoiceDTO(String title, String projectStatus, String financialStatus, String managedBy, String origin,
			String comapny, String currency, String language, String internalReference) {
		this.title = title;
		this.projectStatus = projectStatus;
		this.financialStatus = financialStatus;
		this.managedBy = managedBy;
		this.origin = origin;
		this.comapny = comapny;
		this.currency = currency;
		this.language = language;
		this.internalReference = internalReference;
	}

	public InvoiceDTO(Record record) {
		super(record);
	}

	/**
	 * @return the invoiceNumber
	 */
	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	/**
	 * @param invoiceNumber
	 *            the invoiceNumber to set
	 */
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	/**
	 * 
	 * @param invoiceNumber
	 */
	public InvoiceDTO(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	/**
	 * 
	 * @param invoice
	 */
	public InvoiceDTO(Invoice invoice) {
		super(invoice);
		this.invoiceNumber = invoice.getInvoiceNumber();
	}

	/**
	 * 
	 * @param invoice
	 * @param invoiceDTO
	 * @return
	 */
	public Invoice getInvoice(Invoice invoice, InvoiceDTO invoiceDTO) {
		setInvoiceDetail(invoice, invoiceDTO);
		return invoice;
	}

	/**
	 * 
	 * @param invoice
	 * @param invoiceDTO
	 */
	public void setInvoiceDetail(Invoice invoice, InvoiceDTO invoiceDTO) {
		invoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
		invoiceDTO.setRecordDetail(invoice, invoiceDTO);
	}

	/**
	 * 
	 * @return
	 */
	public long getProjectId() {
		return projectId;
	}

	/**
	 * 
	 * @param projectId
	 */
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	/**
	 * 
	 * @return
	 */
	public long getContactId() {
		return contactId;
	}

	/**
	 * 
	 * @param contactId
	 */
	public void setContactId(long contactId) {
		this.contactId = contactId;
	}

	/**
	 * 
	 * @return
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * 
	 * @param location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * 
	 * @return
	 */
	public String getOrgnization() {
		return orgnization;
	}

	/**
	 * 
	 * @param orgnization
	 */
	public void setOrgnization(String orgnization) {
		this.orgnization = orgnization;
	}

	/**
	 * 
	 * @return
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * 
	 * @param comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * 
	 * @return
	 */
	public int getPercentage() {
		return percentage;
	}

	/**
	 * 
	 * @param percentage
	 */
	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

	/**
	 * 
	 * @return
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * 
	 * @param startDate
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * 
	 * @return
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * 
	 * @param endDate
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isFinalInvoice() {
		return finalInvoice;
	}

	/**
	 * 
	 * @param finalInvoice
	 */
	public void setFinalInvoice(boolean finalInvoice) {
		this.finalInvoice = finalInvoice;
	}

	/**
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 
	 * @return
	 */
	public String getProjectStatus() {
		return projectStatus;
	}

	/**
	 * 
	 * @param projectStatus
	 */
	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	/**
	 * 
	 */
	public String getFinancialStatus() {
		return financialStatus;
	}

	/**
	 * 
	 */
	public void setFinancialStatus(String financialStatus) {
		this.financialStatus = financialStatus;
	}

	/**
	 * 
	 * @return
	 */
	public String getManagedBy() {
		return managedBy;
	}

	/**
	 * 
	 * @param managedBy
	 */
	public void setManagedBy(String managedBy) {
		this.managedBy = managedBy;
	}

	/**
	 * 
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * 
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	/**
	 * 
	 * @return
	 */
	public String getComapny() {
		return comapny;
	}

	/**
	 * 
	 * @param comapny
	 */
	public void setComapny(String comapny) {
		this.comapny = comapny;
	}

	/**
	 * 
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * 
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * 
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * 
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * 
	 * @return
	 */
	public String getInternalReference() {
		return internalReference;
	}

	/**
	 * 
	 * @param internalReference
	 */
	public void setInternalReference(String internalReference) {
		this.internalReference = internalReference;
	}

	/**
	 * 
	 * @return
	 */
	public List<BookingDTO> getBookingDTOs() {
		return bookingDTOs;
	}

	/**
	 * 
	 * @param bookingDTOs
	 */
	public void setBookingDTOs(List<BookingDTO> bookingDTOs) {
		this.bookingDTOs = bookingDTOs;
	}

}
