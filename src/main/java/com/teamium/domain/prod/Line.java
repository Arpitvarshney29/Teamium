/**
 * 
 */
package com.teamium.domain.prod;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.teamium.domain.Company;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.TeamiumPersistenceException;
import com.teamium.domain.Vat;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.resources.functions.Assignation;
import com.teamium.domain.prod.resources.functions.ExtraLine;
import com.teamium.domain.prod.resources.functions.Rate;
import com.teamium.domain.prod.resources.functions.RatedFunction;
import com.teamium.domain.prod.resources.functions.units.RateUnit;

/**
 * Describes a project line
 * 
 * @author sraybaud - NovaRem
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "c_discriminator")
@DiscriminatorValue(value = Line.DISCRIMINATOR)
@Table(name = "t_record_line")
@NamedQueries({ @NamedQuery(name = Line.QUERY_deleteFromRecord, query = "DELETE FROM Line l WHERE l.record = ?1"),
		@NamedQuery(name = Line.QUERY_findByIds, query = "SELECT l FROM Line l WHERE l.id IN (?1)"),
		@NamedQuery(name = Line.QUERY_findToInvoiceByProjectByFunctionType, query = "SELECT l FROM Line l WHERE l.record = ?1 AND EXISTS(SELECT f FROM Function f WHERE TYPE(f) = ?2 AND f = l.function) AND NOT EXISTS (SELECT il FROM LineWrapper il WHERE il.item = l AND EXISTS(SELECT i FROM Invoice i WHERE i.source = ?1 AND il.record = i AND TYPE(i) = Invoice))"),
		@NamedQuery(name = Line.QUERY_findToInvoiceByProject, query = "SELECT l FROM Line l WHERE l.record = ?1 AND NOT EXISTS (SELECT il FROM LineWrapper il WHERE il.item = l AND EXISTS(SELECT i FROM Invoice i WHERE i.source = ?1 AND il.record = i AND TYPE(i) = Invoice))") })
public class Line implements Cloneable, Comparable<Line> {

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -8070983611788985496L;

	/**
	 * Timstamp format for toString
	 */
	protected static final DateFormat TSF = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

	/**
	 * Entity discriminator
	 */
	public static final String DISCRIMINATOR = "line";

	/**
	 * Name of the query deleting all lines from a given record
	 * 
	 * @param 1
	 *            the record from which all lines must be deleted
	 * @return affected rows
	 */
	public static final String QUERY_deleteFromRecord = "deleteLineFromRecord";

	/**
	 * Name of the query returning all the line which apply the fee in parameter
	 * from the record in parameter
	 * 
	 * @param ?1
	 *            The record
	 * @return ?2 The fee
	 * @throws TeamiumPersistenceException
	 */
	public static final String QUERY_findLineHavingFeeFromRecord = "findLineHavingFeeFromRecord";

	/**
	 * Name of the query retrieving lines corresponding to a given list of ids
	 * 
	 * @param 1
	 *            the ids list of the lines to retrieve
	 * @return the list of lines
	 */
	public static final String QUERY_findByIds = "findLineByIds";

	/**
	 * Native query string changing type of the given line
	 * 
	 * @param 1
	 *            the discriminator value to set, that must be EXCLUSIVELY the
	 *            simple class name of the entity, transformed to lower case
	 * @param 2
	 *            id id of the given line
	 * @return the result of the update
	 */
	public static final String QUERY_nativeUpdateType = "UPDATE t_record_line SET c_version = c_version+1, c_discriminator = ?1 WHERE c_idline = ?2";

	/**
	 * Name of the query retrieving bookings to invoice, belonging to the given
	 * project with the given function and which have not been invoiced yet
	 * 
	 * @param 1
	 *            the given project
	 * @param 2
	 *            the given function
	 * @return the bookings list
	 */
	public static final String QUERY_findToInvoiceByProjectByFunctionType = "findLineToInvoiceByProjectByFunctionType";

	/**
	 * Name of the query retrieving all bookings to invoice, belonging to the given
	 * project
	 * 
	 * @param 1
	 *            the given project
	 * @return the bookings list
	 */
	public static final String QUERY_findToInvoiceByProject = "findLineToInvoiceByProject";

	/**
	 * Execute native query for transform a line into a booking
	 * 
	 * @param 1
	 *            id of the line
	 */
	public static final String QUERY_nativeUpdateBooking = "UPDATE t_record_line SET c_version = c_version+1, c_discriminator = 'booking' WHERE c_idline = ?1";

	/**
	 * Line ID
	 */
	@Id
	@Column(name = "c_idline", insertable = false, updatable = false)
	@TableGenerator(name = "idLine_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "record_line_idline_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idLine_seq")
	private Long id;

	/**
	 * The linked project
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "c_idrecord")
	private Record record;

	/**
	 * Function
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "c_idfunction")
	private RatedFunction function;

	/**
	 * Assigned fees
	 * 
	 * @see com.teamium.domain.prod.project.ProjectFee
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "t_line_fee", joinColumns = @JoinColumn(name = "c_idline"))
	@AttributeOverrides({ @AttributeOverride(name = "key", column = @Column(name = "c_fee")) })
	private Set<XmlEntity> appliedFees;

	/**
	 * Quantity
	 */
	@Column(name = "c_quantity")
	private Float qtyTotalUsed;

	/**
	 * Quantity
	 */
	@Column(name = "c_qtytotalsold")
	private Float qtyTotalSold;

	/**
	 * Cost unit
	 */
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "key", column = @Column(name = "c_unit")),
			@AttributeOverride(name = "calendarConstant", column = @Column(name = "c_unit_calendarconstant")) })
	private RateUnit unitUsed;

	/**
	 * Sell unit
	 */
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "key", column = @Column(name = "c_unit_sale")),
			@AttributeOverride(name = "calendarConstant", column = @Column(name = "c_unit_sale_calendarconstant")) })
	private RateUnit unitSold;

	/**
	 * Sell unit price
	 */
	@Column(name = "c_unitprice")
	private Float unitPrice;

	/**
	 * Sell floor unit price
	 */
	@Column(name = "c_floorprice")
	private Float floorUnitPrice;

	/**
	 * Unit cost
	 */
	@Column(name = "c_unitcost")
	private Float unitCost;

	/**
	 * Currency
	 */
	@Column(name = "c_currency")
	private String persistentCurrency;
	@Transient
	private Currency currency;

	/**
	 * Discount rate
	 */
	@Column(name = "c_discount")
	private Float discountRate;

	/**
	 * Total local price
	 */
	@Column(name = "c_totallocalprice")
	private Float totalLocalPrice;

	/**
	 * Total converted price
	 */
	@Column(name = "c_totalprice")
	private Float totalPrice;

	/**
	 * TVA rate
	 */
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "key", column = @Column(name = "c_vat_code")),
			@AttributeOverride(name = "rate", column = @Column(name = "c_vat_rate")) })
	private Vat vat;

	/**
	 * Total local cost
	 */
	@Column(name = "c_totallocalcost")
	private Float totalLocalCost;

	/**
	 * Total converted cost
	 */
	@Column(name = "c_totalcost")
	private Float totalCost;

	/**
	 * Flag if the line is disabled (true)
	 */
	@Column(name = "c_disabled")
	private Boolean disabled;

	/**
	 * Comment for the line
	 */
	@Column(name = "c_comment")
	private String comment;

	/**
	 * Occurence count for the quantity
	 */
	@Column(name = "c_occurrence_count")
	private Float occurrenceCount = 1f;

	/**
	 * Occurence size for the quantity
	 */
	@Column(name = "c_occurrence_size")
	private Float qtyUsedPerOc;

	/**
	 * Sale quantity
	 */
	@Column(name = "c_quantity_sale")
	private Float qtySoldPerOc;

	/**
	 * Flag if the sale quantity and consumed quantity are sync
	 */
	@Column(name = "c_syncqty")
	private Boolean syncQty = Boolean.FALSE;

	/**
	 * Extras whit impact on cost or/and price for the line
	 */
	@OneToMany(mappedBy = "line", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
	private List<ExtraLine> extras;

	/**
	 * Free quantity whit impact only on the period. Define by occurence.
	 */
	@Column(name = "c_freequantity")
	private Float freeQuantity;

	/**
	 * Global extra cost. Define by occurence.
	 */
	@Column(name = "c_extracost")
	private Float extraCost;

	/**
	 * Global extra price. Define by occurence.
	 */
	@Column(name = "c_extraprice")
	private Float extraPrice;

	/**
	 * The assignation
	 */
	@Embedded
	@AttributeOverride(name = "key", column = @Column(name = "c_assignation"))
	private Assignation assignation;

	/**
	 * The rate
	 */
	@ManyToOne
	@JoinColumn(name = "c_rate")
	private Rate rate;

	/**
	 * Sale entity for the line
	 */
	@ManyToOne
	@JoinColumn(name = "c_saleentity")
	private Company saleEntity;

	/**
	 * Is line added to intervention
	 * 
	 */
	@Transient
	private boolean faulty;

	/**
	 * local-cost multiplied by occurence count
	 * 
	 * totalCostWithOccurenceCount = totalLocalCost * occurrenceCount
	 */
	@Column(name = "c_total_cost_with_occurence_count")
	private Float totalCostWithOccurenceCount;

	/**
	 * local-price multiplied by occurence count
	 * 
	 * totalPriceWithOccurenceCount = totalLocalPrice * occurrenceCount
	 */
	@Column(name = "c_total_price_with_occurence_count")
	private Float totalPriceWithOccurenceCount;

	/**
	 * (local-price plus vat-amount) multiplied by occurrenceCount
	 * 
	 * totalWithTax = totalLocalPrice + vatAmount) * occurrenceCount
	 */
	@Column(name = "c_total_with_tax")
	private Float totalWithTax;

	/**
	 * total paid amount
	 */
	@Column(name = "c_total_paid_amount")
	private Float totalPaidAmount;

	/**
	 * total paid amount of days
	 * 
	 */
	@Column(name = "c_invoice_days")
	private Long invoiceDay;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the record
	 */
	public Record getRecord() {
		return record;
	}

	/**
	 * @param record
	 *            the record to set
	 */
	public void setRecord(Record record) {
		this.record = record;
	}

	/**
	 * @return the function
	 */
	public RatedFunction getFunction() {
		return function;
	}

	/**
	 * @param function
	 *            the function to set
	 */
	public void setFunction(RatedFunction function) {
		this.function = function;
	}

	/**
	 * @return the appliedFees
	 */
	public Set<XmlEntity> getAppliedFees() {
		if (appliedFees == null && function != null) {
			appliedFees = function.getAppliedFees();
		}
		return appliedFees;
	}

	/**
	 * @return the quantity
	 */
	public Float getQtyTotalUsed() {
		return qtyTotalUsed;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQtyTotalUsed(Float qtyTotalUsed) {
		this.qtyTotalUsed = qtyTotalUsed;
	}

	/**
	 * @return the unit
	 */
	public RateUnit getUnitUsed() {
		return unitUsed;
	}

	/**
	 * @return the unit
	 */
	public RateUnit getUnitUsedRaw() {
		return unitUsed;
	}

	/**
	 * @param unit
	 *            the unit to set
	 */
	public void setUnitUsed(RateUnit unitUsed) {
		this.unitUsed = unitUsed;
	}

	/**
	 * @return the unitSale
	 */
	public RateUnit getUnitSold() {
		return unitSold;
	}

	/**
	 * @param unitSale
	 *            the unitSale to set
	 */
	public void setUnitSold(RateUnit unitSold) {
		this.unitSold = unitSold;
	}

	/**
	 * Warning don't use if you don't want break process
	 * 
	 * @param unitSale
	 *            the unitSale to set
	 */
	public void setUnitSoldRaw(RateUnit unitSold) {
		this.unitSold = unitSold;
	}

	/**
	 * @return the unitPrice
	 */
	public Float getUnitPrice() {
		return unitPrice;
	}

	/**
	 * @param unitPrice
	 *            the unitPrice to set
	 */
	public void setUnitPrice(Float unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * @return the floorUnitPrice
	 */
	public Float getFloorUnitPrice() {
		return floorUnitPrice;
	}

	/**
	 * @param floorUnitPrice
	 *            the floorUnitPrice to set
	 */
	public void setFloorUnitPrice(Float floorUnitPrice) {
		this.floorUnitPrice = floorUnitPrice;
	}

	/**
	 * @return the unitCost
	 */
	public Float getUnitCost() {
		return unitCost == null ? 0 : unitCost;
	}

	/**
	 * @param unitCost
	 *            the unitCost to set
	 */
	public void setUnitCost(Float unitCost) {
		this.unitCost = unitCost;
	}

	/**
	 * @return the currency
	 */
	public Currency getCurrency() {
		if (this.currency == null && this.persistentCurrency != null) {
			this.setCurrency(Currency.getInstance(this.persistentCurrency));
		}
		return currency;
	}

	/**
	 * @param currency
	 *            the currency to set
	 */
	public void setCurrency(Currency currency) {
		this.currency = currency;
		this.persistentCurrency = this.getCurrency() == null ? null : currency.getCurrencyCode();
	}

	/**
	 * @return the discount
	 */
	public Float getDiscountRate() {
		return discountRate;
	}

	/**
	 * @param discountRate
	 *            the discount rate to set
	 */
	public void setDiscountRate(Float discountRate) {
		this.discountRate = discountRate;
	}

	/**
	 * @return the total price given in the local currency
	 */
	private Float getTotalLocalPriceBeforeDiscount() {
		if (this.getDisabled())
			return null;
		Float extraPrice = 0F;
		for (ExtraLine extra : this.getExtras()) {
			extraPrice += extra.getExtraTotalPrice() != null ? extra.getExtraTotalPrice().floatValue() : 0F;
		}
		return this.getQtyTotalSold() == null ? null
				: this.getQtyTotalSold() * (this.unitPrice == null ? 0 : (this.unitPrice + extraPrice))
						+ (this.extraPrice != null ? this.extraPrice : 0);
	}

	//
	// /**
	// * @return the total price given in the local currency
	// */
	// public Float getTotalLocalPrice() {
	// Float total = this.getTotalLocalPriceBeforeDiscount();
	// this.totalLocalPrice = total == null ? null : Math.round((total -
	// this.getDiscount()) * 100f) / 100f;
	// return totalLocalPrice;
	// }
	//
	// /**
	// * @return the total price per oc in the local currency
	// */
	// public Float getTotalLocalPricePerOc() {
	// if (this.getDisabled())
	// return null;
	// Float extraPrice = 0F;
	// for (ExtraLine extra : this.getExtras()) {
	// extraPrice += extra.getExtraTotalPrice() != null ?
	// extra.getExtraTotalPrice().floatValue() : 0F;
	// }
	// return this.getQtySoldPerOc() == null ? null
	// : this.getQtySoldPerOc() * (this.unitPrice == null ? 0 : (this.unitPrice +
	// extraPrice));
	// }
	//
	// /**
	// * @return the total price whitout all calcul Note : can be desync
	// */
	// public Float getNoCalTotalLocalPrice() {
	// return totalLocalPrice;
	// }
	//
	// /**
	// * Floor price alert
	// */
	// public boolean getFloorPriceAlert() {
	// if (this.floorUnitPrice != null) {
	// if (Boolean.TRUE.equals(this.getDisabled()))
	// return false;
	// if (this.getQtySoldPerOc() == null)
	// return false;
	// if (this.unitPrice == null)
	// return true;
	// if (this.getQtySoldPerOc() * this.floorUnitPrice > this.getQtySoldPerOc() *
	// this.unitPrice
	// * (1 - (this.discountRate != null ? this.discountRate : 0f)))
	// return true;
	// }
	// return false;
	// }
	//
	// /**
	// * @return the discount amount
	// */
	public Float getDiscount() {
		Float total = this.getTotalLocalPriceBeforeDiscount();
		if (this.discountRate != null && total != null)
			return total * this.discountRate;
		if (this.totalPrice != null && this.discountRate != null) {
			return ((this.totalPrice * this.discountRate) / 100);
		}
		return 0f;
	}
	//
	// /**
	// * @return the totalPrice
	// */
	// public Float getTotalPrice() {
	// try {
	// Float localPrice = this.getTotalLocalPrice();
	// Float rate = this.getCurrency() == null || this.record.getExchangeRates() ==
	// null ? null
	// : this.record.getExchangeRates().get(this.getCurrency());
	// this.totalPrice = localPrice == null || rate == null ? localPrice
	// : Math.round(localPrice * rate * 100f) / 100f;
	// } catch (NullPointerException e) {
	// this.totalPrice = this.totalLocalPrice;
	// }
	// return totalPrice;
	// }
	//
	// /**
	// * @return the tva rate to apply
	// */
	// public Vat getVat() {
	// if (this.vat == null && this.function != null)
	// this.vat = this.function.getVat();
	// return this.vat;
	// }
	//
	// /**
	// * param vat the vat rate to apply
	// */
	// public void setVat(Vat vat) {
	// this.vat = vat;
	// }
	//
	// /**
	// * @return the total cost given in the local currency
	// */
	// public Float getTotalLocalCost() {
	// if (this.getDisabled())
	// this.totalLocalCost = null;
	// else {
	// Float extraCost = 0F;
	// for (ExtraLine extra : this.getExtras()) {
	// extraCost += extra.getExtraTotal() != null ?
	// extra.getExtraTotal().floatValue() : 0F;
	// }
	// this.totalLocalCost = this.qtyTotalUsed == null ? null
	// : Math.round(((this.qtyTotalUsed * (this.unitCost == null ? 0 : this.unitCost
	// + extraCost))
	// + (this.extraCost != null ? this.extraCost : 0)) * 100f) / 100f;
	// }
	// return this.totalLocalCost;
	// }
	//
	// /**
	// * @return the total cost given in the local currency per oc
	// */
	// public Float getTotalLocalCostPerOc() {
	// if (this.getDisabled())
	// return null;
	// else {
	// Float extraCost = 0F;
	// for (ExtraLine extra : this.getExtras()) {
	// extraCost += extra.getExtraTotal() != null ?
	// extra.getExtraTotal().floatValue() : 0F;
	// }
	// return this.qtyUsedPerOc == null ? null
	// : Math.round((this.qtyUsedPerOc * (this.unitCost == null ? 0 : this.unitCost
	// + extraCost)) * 100f)
	// / 100f;
	// }
	// }
	//
	// /**
	// * @return the totalCost
	// */
	// public Float getTotalCost() {
	// try {
	// Float localCost = this.getTotalLocalCost();
	// Float rate = this.getCurrency() == null || this.record.getExchangeRates() ==
	// null ? null
	// : this.record.getExchangeRates().get(this.getCurrency());
	// this.totalCost = localCost == null || rate == null ? localCost :
	// Math.round(localCost * rate * 100f) / 100f;
	// } catch (NullPointerException e) {
	// this.totalCost = this.totalLocalCost;
	// }
	// return totalCost;
	// }
	//
	// /**
	// * @return true if the line is disabled
	// */
	// public Boolean getDisabled() {
	// if (disabled == null) {
	// disabled = false;
	// }
	// return disabled;
	// }
	//
	// /**
	// *
	// * @param disabled
	// * set true or false to make the line disabled
	// */
	// public void setDisabled(Boolean disabled) {
	// this.disabled = disabled;
	// }
	//
	// /**
	// * Return the comment for the line
	// *
	// * @return
	// */
	// public String getComment() {
	// return comment;
	// }
	//
	// /**
	// * Update the comment for the line
	// *
	// * @param comment
	// */
	// public void setComment(String comment) {
	// this.comment = comment;
	// }
	//
	// /**
	// * @return the occurenceCount
	// */
	// public Float getOccurrenceCount() {
	// return occurrenceCount;
	// }
	//
	// /**
	// * Update quantity
	// *
	// * @param occurenceCount
	// * the occurenceCount to set
	// */
	// public void setOccurrenceCount(Float occurrenceCount) {
	// if ((occurrenceCount == null && this.rate != null) || (this.rate != null &&
	// occurrenceCount != null
	// && this.rate.getBaseMin() != null && occurrenceCount <
	// this.rate.getBaseMin())) {
	// this.occurrenceCount = this.rate.getBaseMin() != null ? new
	// Float(this.rate.getBaseMin()) : null;
	// } else {
	// this.occurrenceCount = occurrenceCount;
	// }
	// this.updateQuantityUsed();
	// this.updateQuantitySold();
	// }
	//
	// /**
	// * @return the quantity used per occurence
	// */
	// public Float getQtyUsedPerOc() {
	// return qtyUsedPerOc;
	// }
	//
	// /**
	// * Update quantity
	// *
	// * @param qtyUsedPerOc
	// * the quantity per occurence to set
	// */
	// public void setQtyUsedPerOc(Float qtyUsedPerOc) {
	// this.qtyUsedPerOc = qtyUsedPerOc;
	// if (this.qtyUsedPerOc != null && this.unitUsed != null &&
	// this.unitUsed.getCalendarConstant() != null) {
	// this.qtyUsedPerOc = new Float(this.qtyUsedPerOc.intValue());
	// }
	// this.updateQuantityUsed();
	// }
	//
	// /**
	// * @return the extras
	// */
	// public List<ExtraLine> getExtras() {
	// if (extras == null)
	// extras = new ArrayList<ExtraLine>();
	// return extras;
	// }
	//
	// /**
	// * @param extras
	// * the extras to set
	// */
	// public void setExtras(List<ExtraLine> extras) {
	// this.extras = extras;
	// }
	//
	// /**
	// * Update the quantity whit the current value of occurence size & count
	// */
	// private void updateQuantityUsed() {
	// if (this.getOccurrenceCount() != null && this.getQtyUsedPerOc() != null) {
	// this.qtyTotalUsed = this.getOccurrenceCount() * this.getQtyUsedPerOc();
	// } else {
	// this.qtyTotalUsed = 0f;
	// }
	// }
	//
	// /**
	// * Update the quantity whit the current value of occurence size & count
	// */
	// private void updateQuantitySold() {
	// if (this.getOccurrenceCount() != null && this.getQtySoldPerOc() != null) {
	// this.qtyTotalSold = this.getOccurrenceCount() * this.getQtySoldPerOc();
	// } else {
	// this.qtyTotalSold = 0f;
	// }
	// }
	//
	// /**
	// * Summarize prices into dedicated attributes before update, for permiting
	// * search
	// */
	// @PrePersist
	// @PreUpdate
	// private void beforeUpdate() {
	// this.getTotalPrice();
	// this.getTotalCost();
	// }
	//
	// /**
	// * @return the saleQuantity
	// */
	// public Float getQtySoldPerOc() {
	// return qtySoldPerOc;
	// }
	//
	// /**
	// * @param saleQuantity
	// * the saleQuantity to set
	// */
	// public void setQtySoldPerOc(Float qtySoldPerOc) {
	// Float oldQty = this.qtySoldPerOc;
	// this.qtySoldPerOc = qtySoldPerOc;
	// // Refresh total qty
	// this.updateQuantitySold();
	// // Update qty used
	// if (this.qtySoldPerOc != null && this.syncQty) {
	// // Updated by rate values
	// if (this.rate != null && this.rate.getQuantityCost() != null &&
	// this.rate.getQuantitySale() != null) {
	// this.qtyUsedPerOc = this.qtySoldPerOc * this.rate.getQuantityCost() /
	// this.rate.getQuantitySale();
	// }
	// // Update by old qty and qty used per oc
	// else if (this.qtyUsedPerOc != null && oldQty != null) {
	// this.qtyUsedPerOc = (this.qtySoldPerOc * this.qtyUsedPerOc) / oldQty;
	// }
	// // Round if unit used is temporal
	// if (this.qtyUsedPerOc != null && this.unitUsed != null &&
	// this.unitUsed.getCalendarConstant() != null) {
	// this.qtyUsedPerOc = new Float(this.qtyUsedPerOc.intValue());
	// }
	// // Refresh total qty
	// this.updateQuantityUsed();
	// }
	// }
	//
	// /**
	// * Raw setter, don't use if you don't want break process on qty
	// */
	// public void setQtySoldPerOcRaw(Float qtySoldPerOc) {
	// this.qtySoldPerOc = qtySoldPerOc;
	// }
	//
	// /**
	// * @return the syncQty
	// */
	// public Boolean getSyncQty() {
	// if (syncQty == null) {
	// syncQty = Boolean.TRUE;
	// }
	// return syncQty;
	// }
	//
	// /**
	// * @param syncQty
	// * the syncQty to set
	// */
	// public void setSyncQty(Boolean syncQty) {
	// this.syncQty = syncQty;
	// }
	//
	// /**
	// * @return the freeQuantity
	// */
	// public Float getFreeQuantity() {
	// return freeQuantity;
	// }
	//
	// /**
	// * @param freeQuantity
	// * the freeQuantity to set
	// */
	// public void setFreeQuantity(Float freeQuantity) {
	// this.freeQuantity = freeQuantity;
	// }
	//
	// /**
	// * @return the extraCost
	// */
	// public Float getExtraCost() {
	// return extraCost;
	// }
	//
	// /**
	// * @param extraCost
	// * the extraCost to set
	// */
	// public void setExtraCost(Float extraCost) {
	// this.extraCost = extraCost;
	// }
	//
	// /**
	// * @return the extraPrice
	// */
	// public Float getExtraPrice() {
	// return extraPrice;
	// }
	//
	// /**
	// * @param extraPrice
	// * the extraPrice to set
	// */
	// public void setExtraPrice(Float extraPrice) {
	// this.extraPrice = extraPrice;
	// }
	//
	// /**
	// * @return the assignation
	// */
	// public Assignation getAssignation() {
	// return assignation;
	// }
	//
	// /**
	// * @param assignation
	// * the assignation to set
	// */
	// public void setAssignation(Assignation assignation) {
	// this.assignation = assignation;
	// }
	//
	// /**
	// * @return the rate
	// */
	// public Rate getRate() {
	// return rate;
	// }
	//
	// /**
	// * @param rate
	// * the rate to set
	// */
	// public void setRate(Rate rate) {
	// if (rate != null && (this.rate == null || !this.rate.equals(rate))) {
	// // General
	// this.setCurrency(rate.getCurrency());
	// this.saleEntity = rate.getEntity();
	// this.assignation = rate.getAssignation();
	//
	// // Sold
	// this.unitUsed = rate.getUnit();
	// this.qtySoldPerOc = rate.getBaseMin() != null ? new Float(rate.getBaseMin())
	// : null;
	// this.unitPrice = rate.getUnitPrice();
	//
	// // Useds
	// this.unitSold = rate.getUnitSale();
	// this.unitCost = rate.getUnitCost();
	// }
	// this.rate = rate;
	// }
	//
	// /**
	// * Manually method to set rate
	// *
	// */
	// public void setLineRate(Rate rate) {
	// this.rate = rate;
	// }
	//
	// /**
	// * @return the saleEntity
	// */
	// public SaleEntity getSaleEntity() {
	// return saleEntity;
	// }
	//
	// /**
	// * @param saleEntity
	// * the saleEntity to set
	// */
	// public void setSaleEntity(SaleEntity saleEntity) {
	// this.saleEntity = saleEntity;
	// }
	//
	// /**
	// * @return the qtyTotalSold
	// */
	// public Float getQtyTotalSold() {
	// if (qtyTotalSold == null && qtySoldPerOc != null && occurrenceCount != null)
	// {
	// qtyTotalSold = qtySoldPerOc * occurrenceCount;
	// }
	// return qtyTotalSold;
	// }
	//
	// /**
	// * @param qtyTotalSold
	// * the qtyTotalSold to set
	// */
	// public void setQtyTotalSold(Float qtyTotalSold) {
	// this.qtyTotalSold = qtyTotalSold;
	// }
	//
	// /**
	// * @return amount of vat
	// */
	// public Float getAmountVat() {
	// if (this.getTotalLocalPrice() != null) {
	// return this.getTotalLocalPrice()
	// * (this.getVat() != null && this.getVat().getRate() != null ?
	// this.getVat().getRate() : 0);
	// } else {
	// return null;
	// }
	// }
	//
	// /**
	// * @return total whit tax
	// */
	// public Float getTotalWithTax() {
	// if (this.getTotalLocalPrice() != null) {
	// return this.getTotalLocalPrice() * (1 + (this.getVat() != null ?
	// this.getVat().getRate() : 0));
	// } else {
	// return null;
	// }
	// }

	/**
	 * Compare the current object with the given one
	 * 
	 * @param other
	 *            the other to compare
	 * @return -1 if the current is previous, 0 if both are equal, +1 if the given
	 *         is previous
	 */
	@Override
	public int compareTo(Line o) {
		if (o == null)
			return 1;
		if (this.function == null) {
			if (o.function != null) {
				return -1;
			}
		} else {
			if (o.function != null) {
				int compare = this.function.compareTo(o.function);
				if (compare != 0)
					return compare;
			} else {
				return 1;
			}
		}

		if (this.id == null) {
			if (o.id != null) {
				return -1;
			}
		} else {
			if (o.id != null) {
				int compare = this.id.compareTo(o.id);
				if (compare != 0)
					return compare;
			} else {
				return 1;
			}
		}
		return 0;
	}

	/**
	 * Clone the current object
	 * 
	 * @return the clone
	 * @throws CloneNotSupportedException
	 */
	@Override
	public Line clone() throws CloneNotSupportedException {
		Line clone = null;
		RatedFunction function = this.function;
		this.function = null;
		Long id = this.id;
		this.id = null;
		Record record = this.record;
		this.record = null;
		RateUnit unit = this.unitUsed;
		this.unitUsed = null;
		// List<ExtraLine> extras = this.getExtras();
		// this.extras = null;
		Company saleEntity = this.saleEntity;
		this.saleEntity = null;
		Rate rate = this.rate;
		this.rate = null;
		String comment = this.getComment();
		this.comment = null;
		try {
			clone = (Line) super.clone();
			clone.function = function;
			clone.record = record;
			clone.unitUsed = unit;
			clone.comment = comment;
			if (this.getAppliedFees() != null) {
				clone.appliedFees = new HashSet<XmlEntity>(this.appliedFees);
			}
			if (this.getExtras() != null) {
				clone.extras = new ArrayList<ExtraLine>(this.getExtras().size());
				for (ExtraLine el : extras) {
					ExtraLine cloneEl = el.clone();
					cloneEl.setLine(clone);
					clone.extras.add(cloneEl);
				}
			}
			clone.saleEntity = saleEntity;
			clone.rate = rate;
		} catch (CloneNotSupportedException e) {
			throw e;
		} finally {
			this.function = function;
			this.id = id;
			this.record = record;
			this.unitUsed = unit;
			this.saleEntity = saleEntity;
			this.rate = rate;
			this.comment = comment;
		}
		return clone;
	}

	/**
	 * Copy information of current line into the given one
	 * 
	 * @param copy
	 *            the copy to make
	 * @return the made copy
	 */
	public Line copy(Line copy) {
		if (copy != null) {
			copy.comment = this.comment;
			copy.currency = this.getCurrency();
			copy.disabled = disabled;
			copy.discountRate = this.discountRate != null ? this.discountRate.floatValue() : null;
			copy.function = this.function;
			copy.persistentCurrency = this.persistentCurrency != null ? this.persistentCurrency.toString() + "" : null;
			copy.qtyTotalUsed = this.qtyTotalUsed != null ? this.qtyTotalUsed.floatValue() : null;
			copy.record = this.record;
			copy.totalCost = this.totalCost != null ? this.totalCost.floatValue() : null;
			copy.totalLocalCost = this.totalLocalCost != null ? this.totalLocalCost.floatValue() : null;
			copy.totalPrice = this.totalPrice != null ? this.totalPrice.floatValue() : null;
			copy.unitUsed = this.unitUsed;
			copy.unitCost = this.unitCost != null ? this.unitCost.floatValue() : null;
			copy.unitPrice = this.unitPrice != null ? this.unitPrice.floatValue() : null;
			copy.vat = this.vat;
			copy.occurrenceCount = this.occurrenceCount;
			copy.qtyUsedPerOc = this.qtyUsedPerOc;
			copy.qtySoldPerOc = this.qtySoldPerOc;
			copy.syncQty = this.syncQty;
			copy.assignation = this.assignation;
			copy.rate = this.rate;
			copy.saleEntity = this.saleEntity;
		}
		return copy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Line [id=" + id + ", record=" + record + ", function=" + function + ", appliedFees=" + appliedFees
				+ ", qtyTotalUsed=" + qtyTotalUsed + ", qtyTotalSold=" + qtyTotalSold + ", unitUsed=" + unitUsed
				+ ", unitSold=" + unitSold + ", unitPrice=" + unitPrice + ", floorUnitPrice=" + floorUnitPrice
				+ ", unitCost=" + unitCost + ", persistentCurrency=" + persistentCurrency + ", currency=" + currency
				+ ", discountRate=" + discountRate + ", totalLocalPrice=" + totalLocalPrice + ", totalPrice="
				+ totalPrice + ", vat=" + vat + ", totalLocalCost=" + totalLocalCost + ", totalCost=" + totalCost
				+ ", disabled=" + disabled + ", comment=" + comment + ", occurrenceCount=" + occurrenceCount
				+ ", qtyUsedPerOc=" + qtyUsedPerOc + ", qtySoldPerOc=" + qtySoldPerOc + ", syncQty=" + syncQty
				+ ", extras=" + extras + ", freeQuantity=" + freeQuantity + ", extraCost=" + extraCost + ", extraPrice="
				+ extraPrice + ", assignation=" + assignation + ", rate=" + rate + ", saleEntity=" + saleEntity
				+ ", faulty=" + faulty + "]";
	}

	/**
	 * @return the faulty
	 */
	public boolean isFaulty() {
		return faulty;
	}

	/**
	 * @param faulty
	 *            the faulty to set
	 */
	public void setFaulty(boolean faulty) {
		this.faulty = faulty;
	}

	/**
	 * @return the qtyTotalSold
	 */
	public Float getQtyTotalSold() {
		return qtyTotalSold;
	}

	/**
	 * @param qtyTotalSold
	 *            the qtyTotalSold to set
	 */
	public void setQtyTotalSold(Float qtyTotalSold) {
		this.qtyTotalSold = qtyTotalSold;
	}

	/**
	 * @return the persistentCurrency
	 */
	public String getPersistentCurrency() {
		return persistentCurrency;
	}

	/**
	 * @param persistentCurrency
	 *            the persistentCurrency to set
	 */
	public void setPersistentCurrency(String persistentCurrency) {
		this.persistentCurrency = persistentCurrency;
	}

	/**
	 * @return the totalLocalPrice
	 */
	public Float getTotalLocalPrice() {
		if (totalLocalPrice == null) {
			totalLocalPrice = 0f;
		}
		return totalLocalPrice;
	}

	/**
	 * @param totalLocalPrice
	 *            the totalLocalPrice to set
	 */
	public void setTotalLocalPrice(Float totalLocalPrice) {
		this.totalLocalPrice = totalLocalPrice;
	}

	/**
	 * @return the totalPrice
	 */
	public Float getTotalPrice() {
		return totalPrice;
	}

	/**
	 * @param totalPrice
	 *            the totalPrice to set
	 */
	public void setTotalPrice(Float totalPrice) {
		this.totalPrice = totalPrice;
	}

	/**
	 * @return the vat
	 */
	public Vat getVat() {
		return vat;
	}

	/**
	 * @param vat
	 *            the vat to set
	 */
	public void setVat(Vat vat) {
		this.vat = vat;
	}

	/**
	 * @return the totalLocalCost
	 */
	public Float getTotalLocalCost() {
		if (totalLocalCost == null) {
			totalLocalCost = 0.0f;
		}
		return totalLocalCost;
	}

	/**
	 * @param totalLocalCost
	 *            the totalLocalCost to set
	 */
	public void setTotalLocalCost(Float totalLocalCost) {
		this.totalLocalCost = totalLocalCost;
	}

	/**
	 * @return the totalCost
	 */
	public Float getTotalCost() {
		return totalCost;
	}

	/**
	 * @param totalCost
	 *            the totalCost to set
	 */
	public void setTotalCost(Float totalCost) {
		this.totalCost = totalCost;
	}

	/**
	 * @return the disabled
	 */
	public Boolean getDisabled() {
		if (disabled == null) {
			disabled = false;
		}
		return disabled;
	}

	/**
	 * @param disabled
	 *            the disabled to set
	 */
	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the occurrenceCount
	 */
	public Float getOccurrenceCount() {
		return occurrenceCount;
	}

	/**
	 * @param occurrenceCount
	 *            the occurrenceCount to set
	 */
	public void setOccurrenceCount(Float occurrenceCount) {
		this.occurrenceCount = occurrenceCount;
	}

	/**
	 * @return the qtyUsedPerOc
	 */
	public Float getQtyUsedPerOc() {
		if (qtyUsedPerOc == null) {
			qtyUsedPerOc = 0.0f;
		}
		return qtyUsedPerOc;
	}

	/**
	 * @param qtyUsedPerOc
	 *            the qtyUsedPerOc to set
	 */
	public void setQtyUsedPerOc(Float qtyUsedPerOc) {
		this.qtyUsedPerOc = qtyUsedPerOc;
	}

	/**
	 * @return the qtySoldPerOc
	 */
	public Float getQtySoldPerOc() {
		return qtySoldPerOc;
	}

	/**
	 * @param qtySoldPerOc
	 *            the qtySoldPerOc to set
	 */
	public void setQtySoldPerOc(Float qtySoldPerOc) {
		this.qtySoldPerOc = qtySoldPerOc;
	}

	/**
	 * @return the syncQty
	 */
	public Boolean getSyncQty() {
		if (syncQty == null) {
			syncQty = Boolean.TRUE;
		}
		return syncQty;
	}

	/**
	 * @param syncQty
	 *            the syncQty to set
	 */
	public void setSyncQty(Boolean syncQty) {
		this.syncQty = syncQty;
	}

	/**
	 * @return the extras
	 */
	public List<ExtraLine> getExtras() {
		if (extras == null) {
			extras = new ArrayList<ExtraLine>();
		}
		return extras;
	}

	/**
	 * @param extras
	 *            the extras to set
	 */
	public void setExtras(List<ExtraLine> extras) {
		this.extras = extras;
	}

	/**
	 * @return the freeQuantity
	 */
	public Float getFreeQuantity() {
		return freeQuantity;
	}

	/**
	 * @param freeQuantity
	 *            the freeQuantity to set
	 */
	public void setFreeQuantity(Float freeQuantity) {
		this.freeQuantity = freeQuantity;
	}

	/**
	 * @return the extraCost
	 */
	public Float getExtraCost() {
		return extraCost;
	}

	/**
	 * @param extraCost
	 *            the extraCost to set
	 */
	public void setExtraCost(Float extraCost) {
		this.extraCost = extraCost;
	}

	/**
	 * @return the extraPrice
	 */
	public Float getExtraPrice() {
		return extraPrice;
	}

	/**
	 * @param extraPrice
	 *            the extraPrice to set
	 */
	public void setExtraPrice(Float extraPrice) {
		this.extraPrice = extraPrice;
	}

	/**
	 * @return the assignation
	 */
	public Assignation getAssignation() {
		return assignation;
	}

	/**
	 * @param assignation
	 *            the assignation to set
	 */
	public void setAssignation(Assignation assignation) {
		this.assignation = assignation;
	}

	/**
	 * @return the rate
	 */
	public Rate getRate() {
		return rate;
	}

	/**
	 * @param rate
	 *            the rate to set
	 */
	public void setRate(Rate rate) {
		if (rate != null && (this.rate == null || !this.rate.equals(rate))) {
			// General
			this.setCurrency(rate.getCurrency());
			this.saleEntity = rate.getEntity();
			this.assignation = rate.getAssignation();

			this.unitUsed = rate.getUnit();
			this.qtyUsedPerOc = rate.getQuantityCost();

			this.qtySoldPerOc = rate.getQuantitySale();
			this.unitPrice = rate.getUnitPrice();

			this.unitSold = rate.getUnitSale();
			this.unitCost = rate.getUnitCost();
		}
		this.rate = rate;
	}

	public void setLineRate(Rate rate) {
		this.rate = rate;
	}

	/**
	 * @return the saleEntity
	 */
	public Company getSaleEntity() {
		return saleEntity;
	}

	/**
	 * @param saleEntity
	 *            the saleEntity to set
	 */
	public void setSaleEntity(Company saleEntity) {
		this.saleEntity = saleEntity;
	}

	/**
	 * @param appliedFees
	 *            the appliedFees to set
	 */
	public void setAppliedFees(Set<XmlEntity> appliedFees) {
		this.appliedFees = appliedFees;
	}

	/**
	 * Raw setter, don't use if you don't want break process on qty
	 */
	public void setQtySoldPerOcRaw(Float qtySoldPerOc) {
		this.qtySoldPerOc = qtySoldPerOc;
	}

	/**
	 * @return the totalCostWithOccurenceCount
	 */
	public Float getTotalCostWithOccurenceCount() {
		return totalCostWithOccurenceCount;
	}

	/**
	 * @param totalCostWithOccurenceCount
	 *            the totalCostWithOccurenceCount to set
	 */
	public void setTotalCostWithOccurenceCount(Float totalCostWithOccurenceCount) {
		this.totalCostWithOccurenceCount = totalCostWithOccurenceCount;
	}

	/**
	 * @return the totalPriceWithOccurenceCount
	 */
	public Float getTotalPriceWithOccurenceCount() {
		return totalPriceWithOccurenceCount;
	}

	/**
	 * @param totalPriceWithOccurenceCount
	 *            the totalPriceWithOccurenceCount to set
	 */
	public void setTotalPriceWithOccurenceCount(Float totalPriceWithOccurenceCount) {
		this.totalPriceWithOccurenceCount = totalPriceWithOccurenceCount;
	}

	/**
	 * @return the totalWithTax
	 */
	public Float getTotalWithTax() {
		return totalWithTax;
	}

	/**
	 * @param totalWithTax
	 *            the totalWithTax to set
	 */
	public void setTotalWithTax(Float totalWithTax) {
		this.totalWithTax = totalWithTax;
	}

	@PrePersist
	@PreUpdate
	private void beforeUpdate() {
		this.getTotalPrice();
		this.getTotalCost();
	}

	public String getUnitString() {
		return unitUsed == null ? null : unitUsed.getKey();
	}

	public float getVatAmount() {
		if (this.totalLocalPrice != null && this.getVat() != null && this.getVat().getRate() != null) {
			return this.totalLocalPrice
					* (this.getVat() != null && this.getVat().getRate() != null ? this.getVat().getRate() : 0) / 100;
		}
		return 0f;
	}

	/**
	 * 
	 * @return
	 */
	public Float getTotalPaidAmount() {
		return totalPaidAmount;
	}

	/**
	 * 
	 * @param totalPaidAmount
	 */
	public void setTotalPaidAmount(Float totalPaidAmount) {
		this.totalPaidAmount = totalPaidAmount;
	}

	/**
	 * 
	 * @return
	 */
	public Long getInvoiceDay() {
		return invoiceDay;
	}

	/**
	 * 
	 * @param invoiceDay
	 */
	public void setInvoiceDay(Long invoiceDay) {
		this.invoiceDay = invoiceDay;
	}

}
