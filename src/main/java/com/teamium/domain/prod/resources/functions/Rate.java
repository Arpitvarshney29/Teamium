/**
 * 
 */
package com.teamium.domain.prod.resources.functions;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.Company;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.prod.resources.SaleEntity;
import com.teamium.domain.prod.resources.functions.units.RateUnit;

/**
 * Describe a function standard rate
 * 
 * @author sraybaud - NovaRem
 *
 */
@Entity
@Table(name = "t_function_rate")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "c_discriminator")
@DiscriminatorValue("standard")
@NamedQueries({
		@NamedQuery(name = Rate.QUERY_findStandardByFunction, query = "SELECT r FROM Rate r WHERE r.function = ?1 AND TYPE(r) = Rate AND r.archived != TRUE"),
		@NamedQuery(name = Rate.QUERY_findSupplyRatesByCompany, query = "SELECT r FROM Rate r WHERE r.company = ?1 AND r.archived != TRUE"),
		@NamedQuery(name = Rate.QUERY_findSupplyRatesByFunctionName, query = "SELECT r FROM Rate r WHERE r.function = ?2 AND r.company = ?1 AND r.archived != TRUE"),
		@NamedQuery(name = Rate.QUERY_findAllRates, query = "SELECT r FROM Rate r WHERE TYPE(r) = Rate AND r.archived != TRUE") })
public class Rate extends AbstractEntity implements Comparable<Rate> {
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 3285567328302077829L;

	/**
	 * Name of the query returning standard rates only for a given function
	 * 
	 * @param the given function
	 * @return the matching standard rates
	 */
	public static final String QUERY_findStandardByFunction = "findStandardRateByFunction";

	/**
	 * return the rates for a function and a supplier
	 * 
	 * @param 1 - The supplier
	 * @param 2 - The function
	 */
	public static final String QUERY_findSupplyRatesByFunctionName = "findSupplyRateByFunction";

	/**
	 * return the rates for a supplier
	 * 
	 * @param 1 - The supplier
	 * @param 2 - The function
	 */
	public static final String QUERY_findSupplyRatesByCompany = "findSupplyRateByCompany";

	/**
	 * Return all the rates
	 */
	public static final String QUERY_findAllRates = "findAllRates";

	/**
	 * Return all the rates
	 */
	public static final String QUERY_find = "SELECT r FROM Rate r WHERE r.archived != TRUE";

	/**
	 * Rate ID
	 */
	@Id
	@Column(name = "c_idrate", insertable = false, updatable = false)
	@TableGenerator(name = "idRate_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "function_rate_idrate_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idRate_seq")
	private Long id;

	/**
	 * The linked function
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "c_idfunction")
	private Function function;

	@Column(name = "c_label")
	private String label;

	/**
	 * The minimum base unit
	 */
	@Column(name = "c_basismin")
	private Float baseMin;

	/**
	 * Rate unit
	 */
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "key", column = @Column(name = "c_unit")),
			@AttributeOverride(name = "calendarConstant", column = @Column(name = "c_unit_calendarconstant")), })
	private RateUnit unit;

	/**
	 * Rate currency
	 */
	@Column(name = "c_currency")
	private String currency;

	/**
	 * unit sale price per basis min
	 */
	@Column(name = "c_price")
	private Float unitPrice;

	/**
	 * floor price per basis
	 */
	@Column(name = "c_floorprice")
	private Float floorUnitPrice;

	/**
	 * unit cost
	 */
	@Column(name = "c_cost")
	private Float unitCost;

	/**
	 * Sales entity for which the rate is dedicated
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "c_identity")
	private Company entity;

	/**
	 * Company for which the rate is dedicated
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "c_idcompany")
	private Company company;

	/**
	 * Assignation
	 */
	@Embedded
	@AttributeOverride(name = "key", column = @Column(name = "c_assignation"))
	private Assignation assignation;

	@OneToOne(mappedBy = "rate", cascade = CascadeType.ALL)
	private ExtraFunction extra;

	/**
	 * Quantity cost
	 */
	@Column(name = "c_qty_cost")
	private Float quantityCost;

	/**
	 * Quantity sale
	 */
	@Column(name = "c_qty_sale")
	private Float quantitySale;

	/**
	 * Unit sale
	 */
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "key", column = @Column(name = "c_unit_sale")),
			@AttributeOverride(name = "calendarConstant", column = @Column(name = "c_unit_sale_calendarconstant")) })
	private RateUnit unitSale;

	/**
	 * Free quantity
	 */
	@Column(name = "c_qty_free")
	private Float quantityFree;
	
	@Column(name = "c_code")
	private String code;
	
	@Column(name = "c_title")
	private String title;

	/**
	 * Archived entity
	 */
	@Column(name = "c_archived")
	private Boolean archived = Boolean.FALSE;

	/**
	 * @return the associated company
	 */
	public Company getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(Company customer) {
		this.company = customer;
	}

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
	 * @return the unit
	 */
	public RateUnit getUnit() {
		return unit;
	}

	/**
	 * @return the function
	 */
	public Function getFunction() {
		return function;
	}

	/**
	 * @param function the function to set
	 */
	public void setFunction(Function function) {
		this.function = function;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(RateUnit unit) {
		this.unit = unit;
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
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the currency
	 */
	public Currency getCurrency() {
		if (currency == null)
			return null;
		else
			return Currency.getInstance(currency);
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(Currency currency) {
		if (currency == null)
			this.currency = null;
		else
			this.currency = currency.getCurrencyCode();
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
	 * @param unitCoast the unitCost to set
	 */
	public void setUnitCost(Float unitCost) {
		this.unitCost = unitCost;
	}

	/**
	 * @return the entity
	 */
	public Company getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(Company entity) {
		this.entity = entity;
	}

	/**
	 * @return the assignation
	 */
	public Assignation getAssignation() {
		return assignation;
	}

	/**
	 * @param assignation the assignation to set
	 */
	public void setAssignation(Assignation assignation) {
		this.assignation = assignation;
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
	 * @return the unitSale
	 */
	public RateUnit getUnitSale() {
		return unitSale;
	}

	/**
	 * @param unitSale the unitSale to set
	 */
	public void setUnitSale(RateUnit unitSale) {
		this.unitSale = unitSale;
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
	 * // * @return the extras //
	 */
//	public List<ExtraFunction> getExtras() {
//		if (extras == null)
//			extras = new ArrayList<ExtraFunction>();
//		return extras;
//	}
//
//	/**
//	 * @param extras the extras to set
//	 */
//	public void setExtras(List<ExtraFunction> extras) {
//		this.extras = extras;
//	}

	/**
	 * @return the extra
	 */
	public ExtraFunction getExtra() {
		return extra;
	}

	/**
	 * @param extra the extra to set
	 */
	public void setExtra(ExtraFunction extra) {
		this.extra = extra;
		if (extra != null) {
			this.extra.setRate(this);
		}
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
	 * Compare the current object with the given one
	 * 
	 * @param other the other to compare
	 * @return -1 if the current is previous, 0 if both are equal, +1 if the given
	 *         is previous
	 */
	@Override
	public int compareTo(Rate o) {
		if (o == null)
			return 1;
		if (this.id == null) {
			if (o.id != null) {
				return -1;
			}
		} else {
			int compare = this.id.compareTo(o.id);
			if (compare != 0)
				return compare;
		}
		if (this.entity == null) {
			if (o.entity != null) {
				return -1;
			}
		} else {
			int compare = this.entity.compareTo(o.entity);
			if (compare != 0)
				return compare;
		}
		if (this.unit == null) {
			if (o.unit != null) {
				return -1;
			}
		} else {
			int compare = this.unit.compareTo(o.unit);
			if (compare != 0)
				return compare;
		}
		if (this.currency == null) {
			if (o.currency != null) {
				return -1;
			}
		} else {
			if (o.currency == null) {
				return 1;
			} else {
				int compare = this.currency.compareTo(o.currency);
				if (compare != 0)
					return compare;
			}
		}
		return 0;
	}

	/**
	 * Return the string expression of the current object
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		return super.toString() + " " + this.unitPrice
				+ (this.getCurrency() != null ? this.getCurrency().getSymbol() : "") + "/"
				+ (this.unit != null ? this.unit.getKey() : "?") + " as " + entity;
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
