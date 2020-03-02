/**
 * 
 */
package com.teamium.domain.prod.resources.contacts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PostLoad;

import com.teamium.domain.Channel;
import com.teamium.domain.Company;
import com.teamium.domain.ProductionUnit;
import com.teamium.domain.TeamiumException;
import com.teamium.domain.Vat;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.resources.Director;

/**
 * Describe a business contact
 * 
 * @author sraybaud - NovaRem
 *
 */
@Entity
@DiscriminatorValue("customer")
@NamedQueries({
		@NamedQuery(name = Customer.QUERY_findChannels, query = "SELECT DISTINCT ch FROM Customer c, IN(c.channels) ch WHERE c = ?1"),
		@NamedQuery(name = Customer.QUERY_findByType, query = "SELECT c FROM Customer c WHERE c.type = ?1"),
		@NamedQuery(name = Customer.QUERY_findDirectors, query = "SELECT d FROM Customer c, IN(c.directors) d WHERE c = ?1"),
		@NamedQuery(name = Customer.QUERY_findProductionUnit, query = "SELECT pu FROM Customer c, IN(c.productionUnits) pu WHERE c = ?1") })
public class Customer extends Company {

	/**
	 * Class uid
	 */
	private static final long serialVersionUID = -7637422730971511144L;

	/**
	 * Name of the query retrieving the channel list for the given customer
	 * 
	 * @param 1 the given customer
	 * @return the channel list
	 */
	public static final String QUERY_findChannels = "findChannelByCustomer";

	/**
	 * Name of the query retrieving the channel list for the given customer
	 * 
	 * @param 1 the given customer
	 * @return the channel list
	 */
	public static final String QUERY_findProductionUnit = "findProductionUnitByCustomer";

	/**
	 * Name of the query retrieving the customers matching the given type
	 * 
	 * @param 1 the given type
	 * @return the matching customers
	 */
	public static final String QUERY_findByType = "findCustomerByType";

	/**
	 * Name of the query retrieving all directors associated with the given customer
	 * 
	 * @param 1 the given customer
	 * @return the matching directors
	 */
	public static final String QUERY_findDirectors = "findDirectorsByCustomer";

	/**
	 * Type de contact
	 * 
	 * @see com.teamium.domain.prod.resources.CustomerType
	 */
	@Embedded
	@AttributeOverride(name = "key", column = @Column(name = "c_customer_type"))
	private XmlEntity type;

	/**
	 * Rating policy applied to the customer
	 */
	@Embedded
	private RatePolicy ratingPolicy;

	/**
	 * Directors with which the customer is working
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "t_customer_director", joinColumns = @JoinColumn(name = "c_idcustomer"), inverseJoinColumns = @JoinColumn(name = "c_iddirector"))
	@OrderBy("name ASC, firstName ASC")
	private List<Director> directors;

	/**
	 * Production units defined by the customer
	 */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "t_customer_produnit", joinColumns = @JoinColumn(name = "c_idcustomer"), inverseJoinColumns = @JoinColumn(name = "c_idprodunit"))
	@OrderBy("name ASC")
	private List<ProductionUnit> productionUnits;

	/**
	 * Channels managed by
	 */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "t_customer_channel", joinColumns = @JoinColumn(name = "c_idcustomer"), inverseJoinColumns = @JoinColumn(name = "c_idchannel"))
	@OrderBy("name ASC")
	private List<Channel> channels;

	/**
	 * Applied VAT Rates
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "t_company_vat", joinColumns = @JoinColumn(name = "c_idcompany"))
	@AttributeOverrides({ @AttributeOverride(name = "key", column = @Column(name = "c_code")),
			@AttributeOverride(name = "rate", column = @Column(name = "c_rate")) })
	private Set<Vat> vatRates;

	/**
	 * @return the type
	 */
	public XmlEntity getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(XmlEntity type) {
		this.type = type;
	}

	/**
	 * @return the ratingPolicy
	 */
	public RatePolicy getRatingPolicy() {
		return ratingPolicy;
	}

	/**
	 * @param ratingPolicy the ratingPolicy to set
	 */
	public void setRatingPolicy(RatePolicy ratingPolicy) {
		this.ratingPolicy = ratingPolicy;
	}

	/**
	 * @return the directors
	 */
	public List<Director> getDirectors() {
		if (this.directors == null)
			this.directors = new ArrayList<Director>();
		return directors;
	}

	/**
	 * Add the given director to the current customer
	 * 
	 * @param director the director to add
	 * @return true if added, else returns false
	 */
	public boolean addDirector(Director director) {
		if (!this.directors.contains(director) && director != null) {
			return this.getDirectors().add(director);
		} else {
			return false;
		}
	}

	/**
	 * Remove the given director from the current customer
	 * 
	 * @param director the director to remove
	 * @return true if removed, else returns false
	 */
	public boolean removeDirector(Director director) {
		return this.getDirectors().remove(director);
	}

	/**
	 * @return the productionUnits
	 */
	public List<ProductionUnit> getProductionUnits() {
		if (this.productionUnits == null)
			this.productionUnits = new ArrayList<ProductionUnit>();
		return productionUnits;
	}

	/**
	 * @param productionUnits the productionUnits to set
	 */
	public void setProductionUnits(List<ProductionUnit> productionUnits) {
		this.productionUnits = productionUnits;
	}

	/**
	 * @return the channels
	 */
	public List<Channel> getChannels() {
		if (this.channels == null)
			this.channels = new ArrayList<Channel>();
		return channels;
	}

	/**
	 * @param channels the channels to set
	 */
	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}

	public Set<Vat> getVatRates() {
		if (vatRates == null)
			vatRates = new HashSet<Vat>();
		return vatRates;
	}

	public void addVatRate(Vat vat) {
//		if(vatRates == null)
		vatRates = new HashSet<Vat>();
		vatRates.add(vat);
	}

	/**
	 * @param directors the directors to set
	 */
	public void setDirectors(List<Director> directors) {
		this.directors = directors;
	}

	/**
	 * Initializes entity
	 */
	@PostLoad
	private void initialize() {
		if (this.ratingPolicy != null && this.ratingPolicy.getType() != null) {
			try {
				RatePolicy generic = this.ratingPolicy.getType().newInstance();
				if (!this.ratingPolicy.getClass().equals(generic.getClass())) {
					generic.setDiscount(this.ratingPolicy.getDiscount());
					this.ratingPolicy = generic;
				}
			} catch (TeamiumException e) {
			}
		}
	}
}
