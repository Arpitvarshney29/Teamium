package com.teamium.dto;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.Vat;
import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.resources.SaleEntity;
import com.teamium.domain.prod.resources.equipments.EquipmentFunction;
import com.teamium.domain.prod.resources.functions.Rate;
import com.teamium.domain.prod.resources.functions.RatedFunction;
import com.teamium.dto.prod.resources.functions.RateDTO;

/**
 * Line wrapper class
 * 
 * @author Teamium
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class LineDTO {

	private Long id;
	private RecordDTO record;
	private RatedFunctionDTO function;
	private Float qtyTotalUsed;

	private Float qtyTotalSold;
	private Float unitPrice;
	private Float floorUnitPrice;
	private Float unitCost;
	private String persistentCurrency;
	private Float discountRate;

	private Float totalPrice;

	private Float totalCost;
	private Boolean disabled = Boolean.FALSE;
	private String comment;
	private Float occurrenceCount = 1f;
	private Float qtyUsedPerOc;
	private Float qtySoldPerOc;
	private Boolean syncQty = Boolean.FALSE;
	private Float freeQuantity;
	private Float extraCost;
	private Float extraPrice;
	private SaleEntityDTO saleEntity;
	private boolean faulty;
	private RateDTO rate;
	private List<ExtraLineDTO> extras = new ArrayList<ExtraLineDTO>();
	private Vat vat;

	private float totalLocalCost;
	private float totalLocalPrice;
	private float totalWithTax;
	private float discountAmount;
	private float vatAmount;
	private float vatRate;
	private float totalPriceWithOccurenceCount;
	private float totalCostWithOccurenceCount;

	private boolean qtyChange = false;
	private boolean dateChange = false;
	private String unitPriceBasis;
	private boolean applyDateToAll = false;
	private String supplierName;
	private float totalPaidAmount;
	private float invoiceAmount;
	private float invoiceTax;
	private float invoiceDiscount;
	private float invoiceLineAmount;

	public LineDTO() {

	}

	public LineDTO(Line line) {
		this.id = line.getId();
		if (line.getFunction() != null) {
			this.function = new RatedFunctionDTO(line.getFunction());
		}
		this.floorUnitPrice = line.getFloorUnitPrice() != null ? line.getFloorUnitPrice() : 0f;
		this.persistentCurrency = line.getCurrency() == null ? null : line.getCurrency().getCurrencyCode();
		this.disabled = line.getDisabled() == null ? null : line.getDisabled();
		this.comment = line.getComment();
		this.extraCost = line.getExtraCost() != null ? line.getExtraCost() : 0f;
		this.extraPrice = line.getExtraPrice() != null ? line.getExtraPrice() : 0f;
		this.syncQty = line.getSyncQty() == null ? Boolean.FALSE : line.getSyncQty();
		this.freeQuantity = line.getFreeQuantity() != null ? line.getFreeQuantity() : 0f;
		if (line.getSaleEntity() != null) {
			this.saleEntity = new SaleEntityDTO(line.getSaleEntity());
		}
		this.faulty = line.isFaulty();
		if (line.getRate() != null) {
			this.rate = new RateDTO(line.getRate());
		}
		if (line.getExtras() != null && !line.getExtras().isEmpty()) {
			this.extras = line.getExtras().stream().map(extraEntity -> new ExtraLineDTO(extraEntity))
					.collect(Collectors.toList());
		}

		this.occurrenceCount = line.getOccurrenceCount() == null ? 1f : line.getOccurrenceCount();

		this.qtyTotalUsed = line.getQtyTotalUsed() != null ? line.getQtyTotalUsed() : 0f;
		this.qtyUsedPerOc = line.getQtyUsedPerOc() != null ? line.getQtyUsedPerOc() : 0f;
		this.unitCost = line.getUnitCost() != null ? line.getUnitCost() : 0f;
		this.totalCost = line.getTotalCost() != null ? line.getTotalCost() : 0f;
		this.totalLocalCost = line.getTotalLocalCost() != null ? line.getTotalLocalCost() : 0f;
		this.totalCostWithOccurenceCount = line.getTotalCostWithOccurenceCount() != null
				? line.getTotalCostWithOccurenceCount()
				: 0f;

		this.qtyTotalSold = line.getQtyTotalSold() != null ? line.getQtyTotalSold() : 0f;
		this.qtySoldPerOc = line.getQtySoldPerOc() != null ? line.getQtySoldPerOc() : 0f;
		this.unitPrice = line.getUnitPrice() != null ? line.getUnitPrice() : 0f;
		this.totalPrice = line.getTotalPrice() != null ? line.getTotalPrice() : 0f;
		this.totalLocalPrice = line.getTotalLocalPrice() != null ? line.getTotalLocalPrice() : 0f;
		this.totalPriceWithOccurenceCount = this.totalLocalPrice * this.occurrenceCount;
		this.totalPriceWithOccurenceCount = line.getTotalPriceWithOccurenceCount() != null
				? line.getTotalPriceWithOccurenceCount()
				: 0f;

		this.totalWithTax = line.getTotalWithTax() != null ? line.getTotalWithTax() : 0f;

		this.vat = line.getVat();
		if (this.vat != null) {
			this.vatRate = this.vat.getRate();
		}
		this.vatAmount = this.totalLocalPrice
				* (this.getVat() != null && this.getVat().getRate() != null ? this.getVat().getRate() : 0) / 100;

		this.discountRate = line.getDiscountRate() != null ? line.getDiscountRate() : 0f;
		this.discountAmount = (this.totalPrice * this.discountRate) / 100;

		if (line.getUnitUsed() != null) {
			this.unitPriceBasis = line.getUnitUsed().getKey();
		}
	}

	public Line getLine(Line line, LineDTO lineDTO) {
		setLineDetail(line, lineDTO);
		return line;
	}

	public void setLineDetail(Line line, LineDTO lineDTO) {
		line.setId(lineDTO.getId());
		RatedFunctionDTO functionDTO = lineDTO.getFunction();
		if (functionDTO != null) {
			RatedFunction fun = functionDTO.getRatedFunction(new EquipmentFunction());
			line.setFunction(fun);
		}
		line.setQtyTotalUsed(lineDTO.getQtyTotalUsed());
		line.setQtyTotalSold(lineDTO.getQtyTotalSold());
		line.setUnitPrice(lineDTO.getUnitPrice());
		line.setFloorUnitPrice(lineDTO.getFloorUnitPrice());
		line.setUnitCost(lineDTO.getUnitCost());
		String persistentCurrency = lineDTO.getPersistentCurrency();
		if (persistentCurrency != null && !persistentCurrency.isEmpty()) {
			line.setCurrency(Currency.getInstance(persistentCurrency));
		}
		line.setDiscountRate(lineDTO.getDiscountRate());

		line.setDisabled(lineDTO.getDisabled() == null ? null : lineDTO.getDisabled());
		line.setComment(lineDTO.getComment());
		line.setOccurrenceCount(lineDTO.getOccurrenceCount() == null ? 1f : lineDTO.getOccurrenceCount());
		line.setQtyUsedPerOc(lineDTO.getQtyUsedPerOc());
		line.setQtySoldPerOc(lineDTO.getQtySoldPerOc());
		line.setSyncQty(lineDTO.getSyncQty() == null ? Boolean.TRUE : lineDTO.getSyncQty());
		line.setFreeQuantity(lineDTO.getFreeQuantity());
		line.setExtraPrice(lineDTO.getExtraPrice());
		line.setExtraCost(lineDTO.getExtraCost());
		SaleEntityDTO saleEntityDTO = lineDTO.getSaleEntity();
		if (saleEntityDTO != null) {
			SaleEntity saleEntity = saleEntityDTO.getSaleEntityDetails(new SaleEntity());
			line.setSaleEntity(saleEntity);
		}
		line.setFaulty(lineDTO.isFaulty());
		if (this.rate != null) {
			line.setRate(this.rate.getRate(new Rate()));
		}

		line.setVat(lineDTO.getVat());
	}

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
	 * @return the recordDTO
	 */
	public RecordDTO getRecord() {
		return record;
	}

	/**
	 * @param recordDTO
	 *            the recordDTO to set
	 */
	public void setRecord(RecordDTO record) {
		this.record = record;
	}

	/**
	 * @return the function
	 */
	public RatedFunctionDTO getFunction() {
		return function;
	}

	/**
	 * @param function
	 *            the function to set
	 */
	public void setFunction(RatedFunctionDTO function) {
		this.function = function;
	}

	/**
	 * @return the qtyTotalUsed
	 */
	public Float getQtyTotalUsed() {
		return qtyTotalUsed;
	}

	/**
	 * @param qtyTotalUsed
	 *            the qtyTotalUsed to set
	 */
	public void setQtyTotalUsed(Float qtyTotalUsed) {
		this.qtyTotalUsed = qtyTotalUsed;
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
		return unitCost;
	}

	/**
	 * @param unitCost
	 *            the unitCost to set
	 */
	public void setUnitCost(Float unitCost) {
		this.unitCost = unitCost;
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
	 * @return the discountRate
	 */
	public Float getDiscountRate() {
		return discountRate;
	}

	/**
	 * @param discountRate
	 *            the discountRate to set
	 */
	public void setDiscountRate(Float discountRate) {
		this.discountRate = discountRate;
	}

	/**
	 * @return the totalLocalPrice
	 */
	public float getTotalLocalPrice() {
		return totalLocalPrice;
	}

	/**
	 * @param totalLocalPrice
	 *            the totalLocalPrice to set
	 */
	public void setTotalLocalPrice(float totalLocalPrice) {
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
	 * @return the totalLocalCost
	 */
	public float getTotalLocalCost() {
		return totalLocalCost;
	}

	/**
	 * @param totalLocalCost
	 *            the totalLocalCost to set
	 */
	public void setTotalLocalCost(float totalLocalCost) {
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
	 * @return the saleEntityDTO
	 */
	public SaleEntityDTO getSaleEntity() {
		return saleEntity;
	}

	/**
	 * @param saleEntityDTO
	 *            the saleEntityDTO to set
	 */
	public void setSaleEntity(SaleEntityDTO saleEntity) {
		this.saleEntity = saleEntity;
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
	 * @return the rate
	 */
	public RateDTO getRate() {
		return rate;
	}

	/**
	 * @param rate
	 *            the rate to set
	 */
	public void setRate(RateDTO rate) {
		this.rate = rate;
	}

	/**
	 * @return the extras
	 */
	public List<ExtraLineDTO> getExtras() {
		return extras;
	}

	/**
	 * @param extras
	 *            the extras to set
	 */
	public void setExtras(List<ExtraLineDTO> extras) {
		this.extras = extras;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LineDTO [id=" + id + ", record=" + record + ", function=" + function + ", qtyTotalUsed=" + qtyTotalUsed
				+ ", qtyTotalSold=" + qtyTotalSold + ", unitPrice=" + unitPrice + ", floorUnitPrice=" + floorUnitPrice
				+ ", unitCost=" + unitCost + ", persistentCurrency=" + persistentCurrency + ", discountRate="
				+ discountRate + ", totalPrice=" + totalPrice + ", totalCost=" + totalCost + ", disabled=" + disabled
				+ ", comment=" + comment + ", occurrenceCount=" + occurrenceCount + ", qtyUsedPerOc=" + qtyUsedPerOc
				+ ", qtySoldPerOc=" + qtySoldPerOc + ", syncQty=" + syncQty + ", freeQuantity=" + freeQuantity
				+ ", extraCost=" + extraCost + ", extraPrice=" + extraPrice + ", saleEntity=" + saleEntity + ", faulty="
				+ faulty + ", rate=" + rate + ", extras=" + extras + ", vat=" + vat + ", totalLocalCost="
				+ totalLocalCost + ", totalLocalPrice=" + totalLocalPrice + ", totalWithTax=" + totalWithTax
				+ ", discountAmount=" + discountAmount + ", vatAmount=" + vatAmount + ", vatRate=" + vatRate
				+ ", totalPriceWithOccurenceCount=" + totalPriceWithOccurenceCount + ", totalCostWithOccurenceCount="
				+ totalCostWithOccurenceCount + ", qtyChange=" + qtyChange + ", dateChange=" + dateChange
				+ ", unitPriceBasis=" + unitPriceBasis + ", applyDateToAll=" + applyDateToAll + "]";
	}

	/**
	 * @return the totalWithTax
	 */
	public float getTotalWithTax() {
		return totalWithTax;
	}

	/**
	 * @param totalWithTax
	 *            the totalWithTax to set
	 */
	public void setTotalWithTax(float totalWithTax) {
		this.totalWithTax = totalWithTax;
	}

	/**
	 * @return the totalPriceWithOccurenceCount
	 */
	public float getTotalPriceWithOccurenceCount() {
		return totalPriceWithOccurenceCount;
	}

	/**
	 * @param totalPriceWithOccurenceCount
	 *            the totalPriceWithOccurenceCount to set
	 */
	public void setTotalPriceWithOccurenceCount(float totalPriceWithOccurenceCount) {
		this.totalPriceWithOccurenceCount = totalPriceWithOccurenceCount;
	}

	/**
	 * @return the totalCostWithOccurenceCount
	 */
	public float getTotalCostWithOccurenceCount() {
		return totalCostWithOccurenceCount;
	}

	/**
	 * @param totalCostWithOccurenceCount
	 *            the totalCostWithOccurenceCount to set
	 */
	public void setTotalCostWithOccurenceCount(float totalCostWithOccurenceCount) {
		this.totalCostWithOccurenceCount = totalCostWithOccurenceCount;
	}

	/**
	 * @return the vatAmount
	 */
	public float getVatAmount() {
		return vatAmount;
	}

	/**
	 * @param vatAmount
	 *            the vatAmount to set
	 */
	public void setVatAmount(float vatAmount) {
		this.vatAmount = vatAmount;
	}

	/**
	 * @return the vatRate
	 */
	public float getVatRate() {
		return vatRate;
	}

	/**
	 * @param vatRate
	 *            the vatRate to set
	 */
	public void setVatRate(float vatRate) {
		this.vatRate = vatRate;
	}

	/**
	 * @return the qtyChange
	 */
	public boolean isQtyChange() {
		return qtyChange;
	}

	/**
	 * @param qtyChange
	 *            the qtyChange to set
	 */
	public void setQtyChange(boolean qtyChange) {
		this.qtyChange = qtyChange;
	}

	/**
	 * @return the dateChange
	 */
	public boolean isDateChange() {
		return dateChange;
	}

	/**
	 * @param dateChange
	 *            the dateChange to set
	 */
	public void setDateChange(boolean dateChange) {
		this.dateChange = dateChange;
	}

	/**
	 * @return the unitPriceBasis
	 */
	public String getUnitPriceBasis() {
		return unitPriceBasis;
	}

	/**
	 * @param unitPriceBasis
	 *            the unitPriceBasis to set
	 */
	public void setUnitPriceBasis(String unitPriceBasis) {
		this.unitPriceBasis = unitPriceBasis;
	}

	/**
	 * @return the discountAmount
	 */
	public float getDiscountAmount() {
		return discountAmount;
	}

	/**
	 * @param discountAmount
	 *            the discountAmount to set
	 */
	public void setDiscountAmount(float discountAmount) {
		this.discountAmount = discountAmount;
	}

	/**
	 * @return the applyDateToAll
	 */
	public boolean isApplyDateToAll() {
		return applyDateToAll;
	}

	/**
	 * @param applyDateToAll
	 *            the applyDateToAll to set
	 */
	public void setApplyDateToAll(boolean applyDateToAll) {
		this.applyDateToAll = applyDateToAll;
	}

	/**
	 * @return the supplierName
	 */
	public String getSupplierName() {
		return supplierName;
	}

	/**
	 * @param supplierName
	 *            the supplierName to set
	 */
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	/**
	 * 
	 * @return
	 */
	public float getTotalPaidAmount() {
		return totalPaidAmount;
	}

	/**
	 * 
	 * @param totalPaidAmount
	 */
	public void setTotalPaidAmount(float totalPaidAmount) {
		this.totalPaidAmount = totalPaidAmount;
	}

	/**
	 * 
	 * @return
	 */
	public float getInvoiceAmount() {
		return invoiceAmount;
	}

	/**
	 * 
	 * @param invoiceAmount
	 */
	public void setInvoiceAmount(float invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	/**
	 * 
	 * @return
	 */
	public float getInvoiceTax() {
		return invoiceTax;
	}

	/**
	 * 
	 * @param invoiceTax
	 */
	public void setInvoiceTax(float invoiceTax) {
		this.invoiceTax = invoiceTax;
	}

	/**
	 * 
	 * @return
	 */
	public float getInvoiceDiscount() {
		return invoiceDiscount;
	}

	/**
	 * 
	 * @param invoiceDiscount
	 */
	public void setInvoiceDiscount(float invoiceDiscount) {
		this.invoiceDiscount = invoiceDiscount;
	}

	/**
	 * 
	 * @return
	 */
	public float getInvoiceLineAmount() {
		return invoiceLineAmount;
	}

	/**
	 * 
	 * @param invoiceLineAmount
	 */
	public void setInvoiceLineAmount(float invoiceLineAmount) {
		this.invoiceLineAmount = invoiceLineAmount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LineDTO other = (LineDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
