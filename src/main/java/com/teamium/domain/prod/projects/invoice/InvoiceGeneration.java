package com.teamium.domain.prod.projects.invoice;

import java.util.Calendar;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.XmlEntity;

@Entity
@Table(name = "t_invoice_generation")
public class InvoiceGeneration {

	/**
	 * Header ID
	 */
	@Id
	@Column(name = "c_idinvoice_generation")
	@TableGenerator(name = "idInvoice_generation_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "invoice_generation_idinvoice_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idInvoice_generation_seq")
	private Long id;

	@AttributeOverride(name = "key", column = @Column(name = "c_invoice_Type"))
	private XmlEntity invoiceType;

	@Column(name = "c_user_from")
	private Calendar userFromDate;

	@Column(name = "c_user_to")
	private Calendar userToDate;

	@ManyToOne
	@JoinColumn(name = "c_idinvoice")
	private Invoice invoice;

	@Column(name = "c_invoice_percent")
	private Float invoicePercent;

	@Column(name = "c_invoice_amount")
	private Float invoicedAmount;

	@Column(name = "c_invoice_date")
	private Calendar invoiceDate;

	@Column(name = "c_invoice_pdf_path")
	private String invoicePdfPath;

	/**
	 * 
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 
	 * @return
	 */
	public Calendar getUserFromDate() {
		return userFromDate;
	}

	/**
	 * 
	 * @param userFromDate
	 */
	public void setUserFromDate(Calendar userFromDate) {
		this.userFromDate = userFromDate;
	}

	/**
	 * 
	 * @return
	 */
	public Calendar getUserToDate() {
		return userToDate;
	}

	/**
	 * 
	 * @param userToDate
	 */
	public void setUserToDate(Calendar userToDate) {
		this.userToDate = userToDate;
	}

	/**
	 * 
	 * @return
	 */
	public Invoice getInvoice() {
		return invoice;
	}

	/**
	 * 
	 * @param invoice
	 */
	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	/**
	 * 
	 * @return
	 */
	public XmlEntity getInvoiceType() {
		return invoiceType;
	}

	/**
	 * 
	 * @param invoiceType
	 */
	public void setInvoiceType(XmlEntity invoiceType) {
		this.invoiceType = invoiceType;
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
	public Calendar getInvoiceDate() {
		return invoiceDate;
	}

	/**
	 * 
	 * @param invoiceDate
	 */
	public void setInvoiceDate(Calendar invoiceDate) {
		this.invoiceDate = invoiceDate;
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
