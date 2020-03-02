/**
 * 
 */
package com.teamium.domain.prod.resources;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.prod.resources.functions.Function;
import com.teamium.domain.prod.resources.functions.units.RateUnit;
import com.teamium.domain.prod.resources.staff.ResourceRate;
import com.teamium.domain.prod.resources.staff.contract.ContractEdition;

/**
 * Describe a staff member function rating
 * 
 * @author sraybaud - NovaRem
 *
 */
@Entity
@Table(name = "t_resource_function")
public class ResourceFunction extends AbstractEntity implements Comparable<ResourceFunction> {
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -7211402274438036064L;

	/**
	 * Job ID
	 */
	@Id
	@Column(name = "c_idresourcefunction", insertable = false, updatable = false)
	@TableGenerator(name = "idResourceFunction_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "resource_function_idresourcefunction_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idResourceFunction_seq")
	private Long id;

	/**
	 * Skilled function
	 */
	@ManyToOne
	@JoinColumn(name = "c_idfunction")
	private Function function;

	/**
	 * Job rating
	 */
	@Column(name = "c_rating")
	private Integer rating;

	/**
	 * The rates fot this function
	 */
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REMOVE,
			CascadeType.DETACH, CascadeType.MERGE })
	@JoinColumn(name = "c_idresourcefunction")
	private List<ResourceRate> rates;

	/**
	 * The contract edition
	 */

	@Column(name = "c_contractedition")
	private String contractEdition;

	/**
	 * resource-function created on date time
	 */
	@Column(name = "c_date_created")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar createdOn;

	/**
	 * resource-function is primary-function or not
	 */
	@Column(name = "c_primary_function")
	private boolean primaryFunction;

	/**
	 * Selected Rate
	 */
	@Column(name = "c_rate_selected")
	private Boolean rateSelected;

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
	 * @return the name
	 */
	public Function getFunction() {
		return function;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setFunction(Function name) {
		this.function = name;
	}

	/**
	 * @return the rating
	 */
	public Integer getRating() {
		return rating;
	}

	/**
	 * @param rating
	 *            the rating to set
	 */
	public void setRating(Integer rating) {
		this.rating = rating;
	}

	/**
	 * @return the rates
	 */
	public List<ResourceRate> getRates() {
		if (rates == null)
			rates = new ArrayList<ResourceRate>();
		return rates;
	}

	/**
	 * @param rates
	 *            the rates to set
	 */
	public void setRates(List<ResourceRate> rates) {
		this.rates = rates;
	}

	/**
	 * @return the contractEdition
	 */
	public String getContractEdition() {
		if (contractEdition == null)
			return this.getFunction().getRootFunction().getContractEdition();
		return this.contractEdition;
	}

	/**
	 * @param contractEdition
	 *            the contractEdition to set
	 */
	public void setContractEdition(String contractEdition) {
		this.contractEdition = contractEdition;
	}

	/**
	 * @return the createdOn
	 */
	public Calendar getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn
	 *            the createdOn to set
	 */
	public void setCreatedOn(Calendar createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the primaryFunction
	 */
	public boolean isPrimaryFunction() {
		return primaryFunction;
	}

	/**
	 * @param primaryFunction
	 *            the primaryFunction to set
	 */
	public void setPrimaryFunction(boolean primaryFunction) {
		this.primaryFunction = primaryFunction;
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
	public int compareTo(ResourceFunction o) {
		if (o == null)
			return 1;
		if (this.function == null) {
			if (o.function != null) {
				return -1;
			}
		}

		else {
			int compare = this.function.compareTo(o.function);
			if (compare != 0)
				return compare;
		}
		return 0;
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
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourceFunction other = (ResourceFunction) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (function == null) {
			if (other.function != null)
				return false;
		} else if (!function.equals(other.function))
			return false;
		if (rating == null) {
			if (other.rating != null)
				return false;
		} else if (!rating.equals(other.rating))
			return false;
		return true;
	}

	/**
	 * Return the cost for a unit
	 * 
	 * @param r
	 * @return Float - null if no rate found
	 */
	public Float getUnitCost(RateUnit rUnit) {
		if (rUnit != null) {
			ResourceRate rRate = null;
			for (ResourceRate r : rates) {
				if (r != null && r.getRateUnit() != null) {
					if (r.getRateUnit().equals(rUnit)) {
						rRate = r;
						break;
					}
				}
			}
			if (rRate == null)
				return null;
			return rRate.getValue().floatValue();
		}

		return null;
	}

	/**
	 * @return the rateSelected
	 */
	public Boolean getRateSelected() {
		return rateSelected;
	}

	/**
	 * @param rateSelected
	 *            the rateSelected to set
	 */
	public void setRateSelected(Boolean rateSelected) {
		this.rateSelected = rateSelected;
	}

}
