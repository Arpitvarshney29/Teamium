package com.teamium.domain.prod.projects.invoice;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.teamium.domain.MilestoneType;
import com.teamium.domain.Vat;
import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.projects.Booking;

/**
 * Describe the abstract layer of a production project
 * 
 * @author sraybaud
 *
 */
@Entity
@DiscriminatorValue("invoice")
@NamedQueries({
		@NamedQuery(name = Invoice.QUERY_findByProject, query = "SELECT i FROM Invoice i WHERE i.source = ?1 ORDER BY i.date"),
		@NamedQuery(name = Invoice.QUERY_findByIds, query = "SELECT i FROM Invoice i WHERE i.id IN (?1) ORDER BY i.date, i.id"), })
public class Invoice extends Record implements Comparable<Invoice> {

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -3771828342483620701L;

	/**
	 * Name of the query retrieving all invoices for the given project
	 * 
	 * @param 1
	 *            the given project
	 * @return the invoices list
	 */
	public static final String QUERY_findByProject = "findInvoiceByProject";

	/**
	 * Name of the query retrieving entities matching the given list of ids
	 * 
	 * @param 1
	 *            the ids to match
	 * @return the matching entities
	 */
	public static final String QUERY_findByIds = "findInvoiceByIds";

	/**
	 * Invoice number
	 */
	@Column(name = "c_invoicenumber")
	private String invoiceNumber;

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
	 * Add the given type of due date to the project
	 * 
	 * @return true if success, else returns false
	 */
	@Override
	public boolean addDueDate(MilestoneType type) {
		if (type != null) {
			InvoiceDueDate date = new InvoiceDueDate();
			date.setType(type);
			date.setRecord(this);
			return this.getDueDates().add(date);
		} else {
			return false;
		}
	}

	/**
	 * Enwrappe the given line to the invoice
	 * 
	 * @param line
	 *            the line to invoice
	 * @return the linewrapper if operation succeeds, else returns null
	 */
	@Override
	public boolean addLine(Line line) {
		if (line != null && this.getSource() != null) {
			for (Line current : this.getLines()) {
				if (line.equals(((LineWrapper) current).getItem())) {
					return false;
				}
			}
			LineWrapper lw = new LineWrapper();
			lw.setItem((Booking) line);
			lw.setRecord(this);
			return this.getLines().add(lw);
		}
		return false;
	}

	/**
	 * Compare the current object with the given one
	 * 
	 * @param other
	 *            the other to compare
	 * @return -1 if the current is previous, 0 if both are equal, +1 if the given
	 *         is previous
	 */
	@Override
	public int compareTo(Invoice o) {
		if (o == null)
			return 1;
		if (this.invoiceNumber == null) {
			if (o.invoiceNumber != null) {
				return -1;
			}
		} else {
			int compare = this.invoiceNumber.compareTo(o.invoiceNumber);
			if (compare != 0)
				return compare;
		}
		return 0;
	}

	/**
	 * Returns the string expression of a project
	 * 
	 * @return the text
	 */
	@Override
	public String toString() {
		return super.toString() + this.invoiceNumber + " "
				+ (this.getDate() != null ? DF.format(this.getDate().getTime()) : "");
	}

	/**
	 * Returns the VAT rates to apply
	 * 
	 * @return the vat list
	 */
	@Override
	public List<Vat> getVatRates() {
		if (this.getSource() == null || this.getSource().getVatRates() == null)
			return new ArrayList<Vat>();
		return this.getSource().getVatRates();
	}

}
