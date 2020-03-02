package com.teamium.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.constants.Constants;
import com.teamium.domain.prod.projects.invoice.InvoiceGeneration;
import com.teamium.utils.CommonUtil;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class InvoiceGenerationDTO {

	private Long invoiceGenerationId;
	private String fromDate;
	private String toDate;
	private String invoiceType;
	private int invoiceDay;
	private Float invoicePercent;
	private Float invoicedAmount;
	private long projectId;
	private long contactId;
	private float totalTax;
	private float totalDiscount;
	private String invoiceDate;
	private List<LineDTO> lineDTOs = new ArrayList<>();
	private String invoicePdfPath;

	public InvoiceGenerationDTO() {

	}

	public InvoiceGenerationDTO(InvoiceGeneration invoiceGeneration) {
		if (invoiceGeneration != null) {
			this.invoiceGenerationId = invoiceGeneration.getId();
			this.invoiceType = invoiceGeneration.getInvoiceType() != null ? invoiceGeneration.getInvoiceType().getKey()
					: "";

			if (invoiceGeneration.getInvoicePercent() != null) {
				this.invoicePercent = invoiceGeneration.getInvoicePercent();
			}
			if (invoiceGeneration.getUserFromDate() != null) {
				this.fromDate = CommonUtil.datePattern(Constants.DATE_PATTERN, invoiceGeneration.getUserFromDate());
			}

			if (invoiceGeneration.getUserToDate() != null) {
				this.toDate = CommonUtil.datePattern(Constants.DATE_PATTERN, invoiceGeneration.getUserToDate());
			}
			this.invoicedAmount = invoiceGeneration.getInvoicedAmount();
			this.invoiceDate = CommonUtil.datePattern(Constants.DATE_PATTERN, invoiceGeneration.getInvoiceDate());
		}

	}

	public InvoiceGenerationDTO(Long invoiceId, String invoiceType, Float invoicePercent, Float invoicedAmount,
			long projectId) {
		this.invoiceGenerationId = invoiceId;
		this.invoiceType = invoiceType;
		this.invoicePercent = invoicePercent;
		this.invoicedAmount = invoicedAmount;
		this.projectId = projectId;

	}

	public InvoiceGenerationDTO(InvoiceGeneration invoiceGeneration, boolean forAllInvoice) {
		this.invoiceGenerationId = invoiceGeneration.getId();
		this.invoiceType = invoiceGeneration.getInvoiceType() != null ? invoiceGeneration.getInvoiceType().getKey()
				: "";
		this.invoicePdfPath = invoiceGeneration.getInvoicePdfPath() != null ? invoiceGeneration.getInvoicePdfPath()
				: "";
		this.invoicedAmount = invoiceGeneration.getInvoicedAmount();
		this.invoiceDate = CommonUtil.datePattern(Constants.DATE_PATTERN, invoiceGeneration.getInvoiceDate());
	}

	/**
	 * 
	 * @return
	 */
	public Long getInvoiceGenerationId() {
		return invoiceGenerationId;
	}

	/**
	 * 
	 * @param invoiceGenerationId
	 */
	public void setInvoiceGenerationId(Long invoiceGenerationId) {
		this.invoiceGenerationId = invoiceGenerationId;
	}

	/**
	 * 
	 * @return
	 */
	public String getFromDate() {
		return fromDate;
	}

	/**
	 * 
	 * @param fromDate
	 */
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * 
	 * @return
	 */
	public String getToDate() {
		return toDate;
	}

	/**
	 * 
	 * @param toDate
	 */
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	/**
	 * 
	 * @return
	 */
	public String getInvoiceType() {
		return invoiceType;
	}

	/**
	 * 
	 * @param invoiceType
	 */
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	/**
	 * 
	 * @return
	 */
	public int getInvoiceDay() {
		return invoiceDay;
	}

	/**
	 * 
	 * @param invoiceDay
	 */
	public void setInvoiceDay(int invoiceDay) {
		this.invoiceDay = invoiceDay;
	}

	/**
	 * 
	 * @return
	 */
	public Float getInvoicePercent() {
		return invoicePercent;
	}

	/**
	 * 
	 * @param invoicePercent
	 */
	public void setInvoicePercent(Float invoicePercent) {
		this.invoicePercent = invoicePercent;
	}

	/**
	 * 
	 * @return
	 */
	public Float getInvoicedAmount() {
		return invoicedAmount;
	}

	/**
	 * 
	 * @param invoicedAmount
	 */
	public void setInvoicedAmount(Float invoicedAmount) {
		this.invoicedAmount = invoicedAmount;
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
	public float getTotalTax() {
		return totalTax;
	}

	/**
	 * 
	 * @param totalTax
	 */
	public void setTotalTax(float totalTax) {
		this.totalTax = totalTax;
	}

	/**
	 * 
	 * @return
	 */
	public float getTotalDiscount() {
		return totalDiscount;
	}

	/**
	 * 
	 * @param totalDiscount
	 */
	public void setTotalDiscount(float totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	/**
	 * 
	 * @return
	 */
	public String getInvoiceDate() {
		return invoiceDate;
	}

	/**
	 * 
	 * @param invoiceDate
	 */
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	/**
	 * 
	 * @return
	 */
	public List<LineDTO> getLineDTOs() {
		return lineDTOs;
	}

	/**
	 * 
	 * @param lineDTOs
	 */
	public void setLineDTOs(List<LineDTO> lineDTOs) {
		this.lineDTOs = lineDTOs;
	}

	/**
	 * 
	 * @return
	 */
	public String getInvoicePdfPath() {
		return invoicePdfPath;
	}

	/**
	 * 
	 * @param invoicePdfPath
	 */
	public void setInvoicePdfPath(String invoicePdfPath) {
		this.invoicePdfPath = invoicePdfPath;
	}

}
