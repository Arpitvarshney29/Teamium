package com.teamium.dto.prod.resources.functions;

import java.util.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.teamium.domain.prod.resources.functions.Rate;
import com.teamium.domain.prod.resources.functions.RateCard;
import com.teamium.domain.prod.resources.functions.units.RateUnit;
import com.teamium.dto.AbstractDTO;
import com.teamium.dto.CompanyDTO;
import com.teamium.dto.ExtraDTO;
import com.teamium.dto.FunctionDTO;
import com.teamium.dto.SaleEntityDTO;
import com.teamium.enums.RateUnitType;
import com.teamium.exception.UnprocessableEntityException;

/**
 * Wrapper class for Rate Entity
 *
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RateDTO extends AbstractDTO {

	private Long functionId;
	private String label;
	private Float baseMin;
	private String currency;
	private Float unitPrice;
	private Float floorUnitPrice;
	private Float unitCost;
	private SaleEntityDTO entity;
	private Long companyId;
	private CompanyDTO company;

	private Float quantityCost;
	private Float quantitySale;
	private Float quantityFree;
	private Boolean archived = Boolean.FALSE;
	private String basis;
	private ExtraDTO extra;
	private FunctionDTO functionDetails;
	private String type;
	private String code;
	private String title;
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	public RateDTO() {
		super();
	}

	public RateDTO(Rate rate) {
		super(rate);
		this.setId(rate.getId());
		this.setVersion(rate.getVersion());
		label = rate.getLabel();
		baseMin = rate.getBaseMin() != null ? rate.getBaseMin() : 0f;
		if (rate.getCurrency() != null) {
			currency = rate.getCurrency().getCurrencyCode();
		}
		unitPrice = rate.getUnitPrice();
		floorUnitPrice = rate.getFloorUnitPrice();
		unitCost = rate.getUnitCost();
		if (rate.getEntity() != null) {
			entity = new SaleEntityDTO(rate.getEntity());
		}
		quantityCost = rate.getQuantityCost();
		quantitySale = rate.getQuantitySale();
		quantityFree = rate.getQuantityFree();
		archived = rate.getArchived();
		this.unitCost = rate.getUnitCost();
		if (rate.getUnit() != null) {
			this.basis = rate.getUnit().getKey();
		}
		if (rate.getFunction() != null) {
			this.functionId = rate.getFunction().getId();
			this.functionDetails = new FunctionDTO(rate.getFunction());
		}

		if (rate.getCompany() != null) {
			this.company = new CompanyDTO(rate.getCompany());
		}
		if (rate.getExtra() != null) {
			this.extra = new ExtraDTO(rate.getExtra());
		}
		this.title=rate.getTitle();
		this.code=rate.getCode();
//		this.extras = rate.getExtras().stream().map(e -> new ExtraDTO(e)).collect(Collectors.toList());
	}

	/**
	 * Get Rate details from DTO
	 * 
	 * @param Rate
	 * @return Rate
	 */
	@JsonIgnore
	public Rate getRate(Rate rate) {
		rate.setId(this.getId());
		rate.setVersion(this.getVersion());
		try {
			currency = Currency.getInstance(currency).getCurrencyCode();
		} catch (Exception e) {
			logger.info("Please enter valid currency for rate.");
			throw new UnprocessableEntityException("Please enter valid currency for rate.");
		}

		if (currency == null) {
			logger.info("Please enter valid currency for rate.");
			throw new UnprocessableEntityException("Please enter valid currency for rate.");
		}
		RateUnit rateUnit = new RateUnit();
		rateUnit.setKey(RateUnitType.getEnum(basis).getType());
		rate.setUnit(rateUnit);
		if (unitPrice == null) {
			logger.info("Please enter valid unitPrice for rate.");
			throw new UnprocessableEntityException("Please enter valid unitPrice for rate.");
		}
		rate.setLabel(label);
		rate.setBaseMin(baseMin);
		rate.setCurrency(currency);
		rate.setUnitPrice(unitPrice);
		rate.setFloorUnitPrice(floorUnitPrice);
		rate.setUnitCost(unitCost);
		rate.setQuantityCost(quantityCost);
		rate.setQuantitySale(quantitySale);
		rate.setQuantityFree(quantityFree);
		rate.setArchived(archived);
		if (this.extra != null) {
			rate.setExtra(this.extra.getExtra());
//			rate.getExtras().clear();
//			rate.setExtras(extras.stream().map(e -> e.getExtra()).collect(Collectors.toList()));
		} else {
			rate.setExtra(null);
		}
		rate.setTitle(title);
		rate.setCode(code);
		return rate;
	}

	/**
	 * Get Rate details from DTO
	 * 
	 * @return Rate
	 */
	@JsonIgnore
	public Rate getRate() {
		return this.getRate(new Rate());
	}

	/**
	 * Get Rate details from DTO
	 * 
	 * @return RateCard
	 */
	@JsonIgnore
	public RateCard getRateCard() {
		RateCard rate = new RateCard();
		this.getRate(rate);
		return rate;
	}

	/**
	 * @return the functionId
	 */
	public Long getFunctionId() {
		return functionId;
	}

	/**
	 * @param functionId the functionId to set
	 */
	public void setFunctionId(Long functionId) {
		this.functionId = functionId;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the baseMin
	 */
	public Float getBaseMin() {
		return baseMin;
	}

	/**
	 * @param baseMin the baseMin to set
	 */
	public void setBaseMin(Float baseMin) {
		this.baseMin = baseMin;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the unitPrice
	 */
	public Float getUnitPrice() {
		return unitPrice;
	}

	/**
	 * @param unitPrice the unitPrice to set
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
	 * @param floorUnitPrice the floorUnitPrice to set
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
	 * @param unitCost the unitCost to set
	 */
	public void setUnitCost(Float unitCost) {
		this.unitCost = unitCost;
	}

	/**
	 * @return the entity
	 */
	public SaleEntityDTO getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(SaleEntityDTO entity) {
		this.entity = entity;
	}

	/**
	 * @return the quantityCost
	 */
	public Float getQuantityCost() {
		return quantityCost;
	}

	/**
	 * @param quantityCost the quantityCost to set
	 */
	public void setQuantityCost(Float quantityCost) {
		this.quantityCost = quantityCost;
	}

	/**
	 * @return the quantitySale
	 */
	public Float getQuantitySale() {
		return quantitySale;
	}

	/**
	 * @param quantitySale the quantitySale to set
	 */
	public void setQuantitySale(Float quantitySale) {
		this.quantitySale = quantitySale;
	}

	/**
	 * @return the quantityFree
	 */
	public Float getQuantityFree() {
		return quantityFree;
	}

	/**
	 * @param quantityFree the quantityFree to set
	 */
	public void setQuantityFree(Float quantityFree) {
		this.quantityFree = quantityFree;
	}

	/**
	 * @return the archived
	 */
	public Boolean getArchived() {
		return archived;
	}

	/**
	 * @param archived the archived to set
	 */
	public void setArchived(Boolean archived) {
		this.archived = archived;
	}

	/**
	 * @return the companyId
	 */
	public Long getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	/**
	 * @return the basis
	 */
	public String getBasis() {
		return basis;
	}

	/**
	 * @param basis the basis to set
	 */
	public void setBasis(String basis) {
		this.basis = basis;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RateDTO [functionId=" + functionId + ", label=" + label + ", baseMin=" + baseMin + ", currency="
				+ currency + ", unitPrice=" + unitPrice + ", floorUnitPrice=" + floorUnitPrice + ", unitCost="
				+ unitCost + ", entity=" + entity + ", CompanyId=" + companyId + ", quantityCost=" + quantityCost
				+ ", quantitySale=" + quantitySale + ", quantityFree=" + quantityFree + ", archived=" + archived
				+ ", basis=" + basis + ", logger=" + logger + ", getId()=" + getId() + ", getVersion()=" + getVersion()
				+ ", toString()=" + super.toString() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ "]";
	}

	/**
	 * @return the extra
	 */
	public ExtraDTO getExtra() {
		return extra;
	}

	/**
	 * @param extra the extra to set
	 */
	public void setExtra(ExtraDTO extra) {
		this.extra = extra;
	}

	/**
	 * @return the functionDetails
	 */
	public FunctionDTO getFunctionDetails() {
		return functionDetails;
	}

	/**
	 * @param functionDetails the functionDetails to set
	 */
	public void setFunctionDetails(FunctionDTO functionDetails) {
		this.functionDetails = functionDetails;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the company
	 */
	public CompanyDTO getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(CompanyDTO company) {
		this.company = company;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	

}
