
package com.teamium.domain.prod.resources.equipments;

import java.util.ArrayList;
import java.util.Calendar;
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
import javax.persistence.DiscriminatorType;
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
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.Document;
import com.teamium.domain.Milestone;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.resources.ResourceEntity;
import com.teamium.domain.prod.resources.ResourceFunction;
import com.teamium.domain.prod.resources.SaleEntity;
import com.teamium.domain.prod.resources.functions.Function;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.domain.prod.resources.suppliers.Supplier;

/**
 * Describe an equipment used for resource planning.
 * 
 * @author sraybaud - NovaRem
 *
 */
@Entity
@Table(name = "t_equipment")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "c_discriminator", discriminatorType = DiscriminatorType.STRING)
@NamedQueries({ @NamedQuery(name = Equipment.QUERY_countAll, query = "SELECT COUNT(e) FROM Equipment e"),
		@NamedQuery(name = Equipment.QUERY_findAll, query = "SELECT e FROM Equipment e ORDER BY e.name"),
		@NamedQuery(name = Equipment.QUERY_findByIds, query = "SELECT e FROM Equipment e WHERE e.id IN (?1)"),
		@NamedQuery(name = Equipment.QUERY_findByMaintenanceContract, query = "SELECT e FROM Equipment e WHERE ?1 MEMBER OF e.contracts"),
		@NamedQuery(name = Equipment.QUERY_findBrandByType, query = "SELECT e.brand FROM Equipment e WHERE e.type = ?1 GROUP BY e.brand"),
		@NamedQuery(name = Equipment.QUERY_findModelByTypeByBrand, query = "SELECT e.model FROM Equipment e WHERE e.type = ?1 AND e.brand = ?2 GROUP BY e.model"),
		@NamedQuery(name = Equipment.QUERY_findReferenceByTypeByBrandByModel, query = "SELECT e.reference FROM Equipment e WHERE e.type = ?1 AND e.brand = ?2 AND e.model = ?3 GROUP BY e.reference") })
public class Equipment extends AbstractEntity implements ResourceEntity {

	/**
	 * Class uid
	 */
	private static final long serialVersionUID = -9051876576455835958L;

	/**
	 * The entity alias used in queries
	 */
	public static final String QUERY_ENTITY_ALIAS = "e";

	/**
	 * Name of the query counting all records for the current entity
	 * 
	 * @return the current entity total count
	 */
	public static final String QUERY_countAll = "countAllEquipment";

	/**
	 * Name of the query retrieving all the current entity instances from the
	 * persistence context
	 * 
	 * @return the equipment list
	 */
	public static final String QUERY_findAll = "QUERY_findAllEquipment";

	/**
	 * Query string for retrieving all equipements, that may be ordered by
	 * OrderByClause.getClause()
	 * 
	 * @return the list of all contracts
	 */
	public static final String QUERY_findAllOrdered = "SELECT e FROM Equipment e";

	/**
	 * Name of the query retrieving the entity instances matching the given list of
	 * ids
	 * 
	 * @param ids
	 *            the ids of the entities to retrieve
	 * @return the equipment list
	 */
	public static final String QUERY_findByIds = "QUERY_findEquipmentByIds";

	/**
	 * Query string to retrieve the equipments matching the given list of ids, that
	 * may be ordered by OrderByClause.getClause()
	 * 
	 * @param 1
	 *            the ids to search
	 * @return the list of matching equipments
	 */
	public static final String QUERY_findOrderedByIds = "SELECT e.id FROM Equipment e WHERE e.id IN (?1)";

	/**
	 * Query string to retrieve the equipment matching the maintenance contract
	 * given in parameter
	 * 
	 * @param 1
	 *            the maintenance contract to search
	 * @return the equipment
	 */
	public static final String QUERY_findByMaintenanceContract = "QUERY_findEquipmentByContract";

	/**
	 * Query string to retrieve all the brands matching the type of equipment given
	 * in parameter
	 * 
	 * @since 7.2
	 * @param 1
	 *            the type of equipment to search
	 * @return the sorted list of brands for the given type
	 */
	public static final String QUERY_findBrandByType = "QUERY_findEquipmentBrandByType";

	/**
	 * Query string to retrieve all the models matching the type and the brand of
	 * equipment given in parameter
	 * 
	 * @since 7.2
	 * @param 1
	 *            the type of equipment to search
	 * @param 2
	 *            the brand of equipment to search
	 * @return the sorted list of models for the given type and brand
	 */
	public static final String QUERY_findModelByTypeByBrand = "QUERY_findEquipmentModelByTypeByBrand";

	/**
	 * Query string to retrieve all the references matching the type, the brand and
	 * the model of equipment given in parameter
	 * 
	 * @since 7.2
	 * @param 1
	 *            the type of equipment to search
	 * @param 2
	 *            the brand of equipment to search
	 * @param 3
	 *            the model of equipment to search
	 * @return the sorted list of references for the given type, brand and model
	 */
	public static final String QUERY_findReferenceByTypeByBrandByModel = "QUERY_findEquipmentReferenceByTypeByBrandByModel";

	/**
	 * Query string changing type of the current equipment
	 * 
	 * @param type
	 *            the type to set
	 * @param id
	 *            id of the targeted equipment
	 * @return the result of the update
	 */
	public static final String QUERY_nativeUpdateType = "UPDATE t_equipment SET c_version = c_version + 1, c_discriminator = ?1 WHERE c_idequipment = ?2";

	/**
	 * The equipment ID
	 */
	@Id
	@Column(name = "c_idequipment", insertable = false, updatable = false)
	@TableGenerator(name = "idEquipment_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "equipment_idequipment_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idEquipment_seq")
	private Long id;

	/**
	 * The equipment name
	 */
	@Column(name = "c_name")
	private String name;

	/**
	 * The type of equipment
	 * 
	 * @see com.teamium.domain.prod.resources.equipment.EquipmentType
	 */
	@Column(name = "c_equipmenttype")
	private String type;

	/**
	 * The equipment description
	 */
	@Column(name = "c_description")
	private String description;

	/**
	 * The brand of the equipment
	 * 
	 * @since 7.2
	 */
	@Column(name = "c_brand")
	private String brand;

	/**
	 * The brand of the equipment
	 * 
	 * @since 7.2
	 */
	@Column(name = "c_model")
	private String model;

	/**
	 * The brand of the equipment
	 * 
	 * @since 7.2
	 */
	@Column(name = "c_reference")
	private String reference;

	/**
	 * The serial number of the equipment
	 * 
	 * @since 7.2
	 */
	@Column(name = "c_serialnumber")
	private String serialNumber;

	/**
	 * The equipment's location
	 * 
	 * @since 7.2
	 */

	@Column(name = "c_location")
	private String location;

	/**
	 * The location details
	 * 
	 * @since 7.2
	 */
	@Column(name = "c_locationdetails")
	private String locationDetails;

	/**
	 * The equipment photo
	 */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "c_idphoto")
	private Document photo;

	/**
	 * The faceted resource corresponding to the equipment resource
	 */
	@OneToOne(mappedBy = "entity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private EquipmentResource resource;

	/**
	 * @since v6
	 * @author slopes The valuation of the equipment
	 */
	@Column(name = "c_value")
	private Float value;

	/**
	 * @since v6
	 * @author slopes The currency
	 */
	@Column(name = "c_currency")
	private String currency;

	/**
	 * The amortization array
	 */
	@OneToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH,
			CascadeType.REMOVE }, mappedBy = "equipment")
	private List<Amortization> amortizationArray;

	/**
	 * The maintenance contracts
	 */
	@OneToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH,
			CascadeType.REMOVE })
	@JoinColumn(name = "c_equipment")
	private List<MaintenanceContract> contracts;

	/**
	 * @since v6
	 * @author slopes The supplier
	 */
	@ManyToOne
	@JoinColumn(name = "c_supplier")
	private Supplier supplier;

	/**
	 * List of sale entities
	 */
	@ManyToOne
	@JoinColumn(name = "c_idSaleEntity")
	private SaleEntity saleEntity;

	/**
	 * Export ATA boolean
	 */
	@Transient
	private Boolean isExportAta;

	@Column(name = "c_creation")
	@Temporal(TemporalType.DATE)
	private Calendar creation;

	@JoinColumn(name = "c_created_by", referencedColumnName = "c_idperson")
	@OneToOne(fetch = FetchType.LAZY)
	private StaffMember createdBy;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "c_updated_by", referencedColumnName = "c_idperson")
	private StaffMember updatedBy;

	@Column(name = "c_updation")
	@Temporal(TemporalType.DATE)
	private Calendar updation;

	@Column(name = "c_specs_consumption")
	private String specsConsumption;

	@Column(name = "c_specs_weight")
	private String specsWeight;

	@Column(name = "c_specs_size")
	private String specsSize;

	@Column(name = "c_specs_orign")
	private String specsOrign;

	@Column(name = "c_specs_ip")
	private String specsIp;

	@OneToMany(cascade = { CascadeType.ALL })
	@JoinColumn(name = "c_idequipment")
	private Set<Attachment> attachments = new HashSet<>();

	@Column(name = "c_value_purchage")
	private Double purchase;

	@Column(name = "c_value_insurance")
	private Double insurance;

	@Column(name = "c_value_ata")
	private Double ata;

	@ElementCollection
	@CollectionTable(name = "t_equipment_milestone", joinColumns = @JoinColumn(name = "equipment_id"))
	private Set<Milestone> milestones;

	@Column(name = "c_format")
	private String format;

	@Column(name = "c_from_market_place")
	private boolean fromMarketPlace;
	
	@Column(name = "c_available")
	private boolean available = true;
	
	

	/**
	 * 
	 * 
	 * 
	 * /**
	 * 
	 * @return the id
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * Set the entity id
	 * 
	 * @param the
	 *            id to set
	 */
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the brand
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * @param brand
	 *            the brand to set
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model
	 *            the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference
	 *            the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * @param serialNumber
	 *            the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the locationDetails
	 */
	public String getLocationDetails() {
		return locationDetails;
	}

	/**
	 * @param locationDetails
	 *            the locationDetails to set
	 */
	public void setLocationDetails(String locationDetails) {
		this.locationDetails = locationDetails;
	}

	/**
	 * @return the photo
	 */
	public Document getPhoto() {
		return photo;
	}

	/**
	 * @param photo
	 *            the photo to set
	 */
	public void setPhoto(Document photo) {
		this.photo = photo;
	}

	/**
	 * Returns the faceted resource
	 * 
	 * @return the faceted resource
	 */
	public EquipmentResource getResource() {
		if (this.resource == null) {
			this.resource = new EquipmentResource();
			this.resource.setEquipment(this);
		}
		return this.resource;
	}

	/**
	 * @return the assigned function
	 */
	public Set<ResourceFunction> getFunctions() {
		return this.getResource().getFunctions();
	}

	/**
	 * Return the function matching the given name
	 * 
	 * @param name
	 *            the name to match
	 * @return the matching function
	 */
	public ResourceFunction getFunction(Function name) {
		return this.getResource().getFunction(name);
	}

	/**
	 * Assign the given function to the current equipment
	 * 
	 * @param function
	 *            the function to assign
	 * @return the created resource if function, else returns null
	 */
	public ResourceFunction assignFunction(Function function) {
		return this.getResource().assignFunction(function);
	}

	/**
	 * Remove the given assignment from the current equipment
	 * 
	 * @return function the function assignment to remove
	 * @return true if success, else returns false
	 */
	public boolean unassignFunction(ResourceFunction function) {
		return this.getResource().unassignFunction(function);
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the value
	 */
	public Float getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Float value) {
		this.value = value;
		if (this.getAmortizationArray() != null) {
			for (Amortization amort : this.getAmortizationArray()) {
				amort.refreshValue();
			}
		}
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
	 * @param currency
	 *            the currency to set
	 */
	public void setCurrency(Currency currency) {
		if (currency == null)
			this.currency = null;
		else
			this.currency = currency.getCurrencyCode();
	}

	/**
	 * @return the amortizationArray
	 */
	public List<Amortization> getAmortizationArray() {
		if (amortizationArray == null) {
			amortizationArray = new ArrayList<Amortization>();
		}
		return amortizationArray;
	}

	/**
	 * @param amortizationArray
	 *            the amortizationArray to set
	 */
	public void setAmortizationArray(List<Amortization> amortizationArray) {
		this.amortizationArray = amortizationArray;
	}

	/**
	 * @return the contracts
	 */
	public List<MaintenanceContract> getContracts() {
		if (contracts == null) {
			contracts = new ArrayList<MaintenanceContract>();
		}
		return contracts;
	}

	/**
	 * @param contracts
	 *            the contracts to set
	 */
	public void setContracts(List<MaintenanceContract> contracts) {
		this.contracts = contracts;
	}

	/**
	 * @return the supplier
	 */
	public Supplier getSupplier() {
		return supplier;
	}

	/**
	 * @param supplier
	 *            the supplier to set
	 */
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	/**
	 * Add amortization to the amortization array
	 * 
	 * @param amortization
	 *            The amortization
	 * @return True if the amortization is correctly added
	 */
	public boolean addAmortization(Amortization amortization) {
		if (amortizationArray == null) {
			amortizationArray = new ArrayList<Amortization>();
		}
		return this.amortizationArray.add(amortization);
	}

	/**
	 * Remove amortization to the amortization array
	 * 
	 * @param amortization
	 *            The amortization
	 * @return True if the amortization is correctly removed
	 */
	public boolean removeAmortization(Amortization amortization) {
		if (amortizationArray == null) {
			amortizationArray = new ArrayList<Amortization>();
		}
		return this.amortizationArray.remove(amortization);
	}

	/**
	 * Add contract to the contracts
	 * 
	 * @param contract
	 *            The contract
	 * @return True if the contract is correctly added
	 */
	public boolean addContract(MaintenanceContract contract) {
		return this.getContracts().add(contract);
	}

	/**
	 * Remove contract to the contracts
	 * 
	 * @param contract
	 *            The contract
	 * @return True if the contract is correctly removed
	 */
	public boolean removeContract(MaintenanceContract contract) {
		return this.getContracts().remove(contract);
	}

	/**
	 * Total percentage of amortization
	 * 
	 * @return
	 */
	public Float getTotalAmortizationPercent() {
		Float total = 0F;
		if (this.amortizationArray != null) {
			for (Amortization am : this.amortizationArray) {
				if (am.getPercent() != null) {
					total += am.getPercent();
				}
			}
		}

		return total;
	}

	/**
	 * @param currency
	 *            the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the saleEntity
	 */
	public SaleEntity getSaleEntity() {
		return saleEntity;
	}

	/**
	 * @param saleEntity
	 *            the saleEntity to set
	 */
	public void setSaleEntity(SaleEntity saleEntity) {
		this.saleEntity = saleEntity;
	}

	/**
	 * @return the exportAta
	 */
	public Boolean getIsExportAta() {
		if (this.isExportAta == null) {
			if (this.ata == null)
				this.isExportAta = false;
			else
				this.isExportAta = true;
		}

		return isExportAta;
	}

	/**
	 * @param exportAta
	 *            the exportAta to set
	 */
	public void setIsExportAta(Boolean isExportAta) {
		this.isExportAta = isExportAta;
	}

	/**
	 * @return the specsConsumption
	 */
	public String getSpecsConsumption() {
		return specsConsumption;
	}

	/**
	 * @param specsConsumption
	 *            the specsConsumption to set
	 */
	public void setSpecsConsumption(String specsConsumption) {
		this.specsConsumption = specsConsumption;
	}

	/**
	 * @return the specsWeight
	 */
	public String getSpecsWeight() {
		return specsWeight;
	}

	/**
	 * @param specsWeight
	 *            the specsWeight to set
	 */
	public void setSpecsWeight(String specsWeight) {
		this.specsWeight = specsWeight;
	}

	/**
	 * @return the specsSize
	 */
	public String getSpecsSize() {
		return specsSize;
	}

	/**
	 * @param specsSize
	 *            the specsSize to set
	 */
	public void setSpecsSize(String specsSize) {
		this.specsSize = specsSize;
	}

	/**
	 * @return the specsOrign
	 */
	public String getSpecsOrign() {
		return specsOrign;
	}

	/**
	 * @param specsOrign
	 *            the specsOrign to set
	 */
	public void setSpecsOrign(String specsOrign) {
		this.specsOrign = specsOrign;
	}

	/**
	 * @return the specsIp
	 */
	public String getSpecsIp() {
		return specsIp;
	}

	/**
	 * @param specsIp
	 *            the specsIp to set
	 */
	public void setSpecsIp(String specsIp) {
		this.specsIp = specsIp;
	}

	/**
	 * @return the createdBy
	 */
	public StaffMember getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(StaffMember createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the updatedBy
	 */
	public StaffMember getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy
	 *            the updatedBy to set
	 */
	public void setUpdatedBy(StaffMember updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * @return the updation
	 */
	public Calendar getUpdation() {
		return updation;
	}

	/**
	 * @param updation
	 *            the updation to set
	 */
	public void setUpdation(Calendar updation) {
		this.updation = updation;
	}

	/**
	 * @return the creation
	 */
	public Calendar getCreation() {
		return creation;
	}

	/**
	 * @param creation
	 *            the creation to set
	 */
	public void setCreation(Calendar creation) {
		this.creation = creation;
	}

	/**
	 * @return the attachments
	 */
	public Set<Attachment> getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments
	 *            the attachments to set
	 */
	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}

	/**
	 * @return the purchase
	 */
	public Double getPurchase() {
		return purchase;
	}

	/**
	 * @param purchage
	 *            the purchage to set
	 */
	public void setPurchase(Double purchase) {
		this.purchase = purchase;
	}

	/**
	 * @return the insurance
	 */
	public Double getInsurance() {
		return insurance;
	}

	/**
	 * @param insurance
	 *            the insurance to set
	 */
	public void setInsurance(Double insurance) {
		this.insurance = insurance;
	}

	/**
	 * @return the ata
	 */
	public Double getAta() {
		return ata;
	}

	/**
	 * @param ata
	 *            the ata to set
	 */
	public void setAta(Double ata) {
		this.ata = ata;
	}

	/**
	 * @return the milestones
	 */
	public Set<Milestone> getMilestones() {
		return milestones;
	}

	/**
	 * @param milestones
	 *            the milestones to set
	 */
	public void setMilestones(Set<Milestone> milestones) {
		this.milestones = milestones;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format
	 *            the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @return the fromMarketPlace
	 */
	public boolean isFromMarketPlace() {
		return fromMarketPlace;
	}

	/**
	 * @param fromMarketPlace
	 *            the fromMarketPlace to set
	 */
	public void setFromMarketPlace(boolean fromMarketPlace) {
		this.fromMarketPlace = fromMarketPlace;
	}

	/**
	 * @return the available
	 */
	public boolean isAvailable() {
		return available;
	}

	/**
	 * @param available the available to set
	 */
	public void setAvailable(boolean available) {
		this.available = available;
	}

	/**
	 * Finalize the name ordering before persisting
	 */
	@PrePersist
	@PreUpdate
	private void beforeUpdate() {
		if (this.getResource() != null) {
			this.getResource().setName(this.getName());
		}
	}

}
