package com.teamium.dto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teamium.domain.Milestone;
import com.teamium.domain.prod.resources.SaleEntity;
import com.teamium.domain.prod.resources.equipments.Amortization;
import com.teamium.domain.prod.resources.equipments.Ata;
import com.teamium.domain.prod.resources.equipments.Equipment;
import com.teamium.domain.prod.resources.equipments.EquipmentResource;
import com.teamium.domain.prod.resources.equipments.Attachment;
import com.teamium.domain.prod.resources.equipments.MaintenanceContract;
import com.teamium.domain.prod.resources.functions.Function;
import com.teamium.domain.prod.resources.suppliers.Supplier;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EquipmentDTO extends AbstractDTO {

	/**
	 * The equipment name
	 */

	private String name;

	private String type;

	private String description;

	private String brand;

	private String model;

	private String reference;

	private String serialNumber;

	private String location;

	private DocumentDTO photo;

	private String locationDetails;

	private EquipmentResourceDTO resource;

	private Float value;

	private String currency;

	private List<MaintenanceContractDTO> contracts = new ArrayList<>();

	private SupplierDTO supplier;

	private Calendar creation;

	private List<AmortizationDTO> amortizationList;

	private SaleEntityDTO saleEntity;

	private Boolean isExportAta;
	private String createdBy;

	private String updatedBy;
	private Calendar updation;

	private Set<FunctionDTO> functions;

	private Set<Long> unassignedResourceFunctionIds = new HashSet<>();

	private String specsConsumption;

	private String specsWeight;

	private String specsSize;

	private String specsOrign;

	private String specsIp;

	private Set<Long> unassignedAmortizationIds = new HashSet<>();
	
	private int quantityForBookingLine = 1;


	private Set<Attachment> attachments = new HashSet<>();
	private Double ata;
	private Double purchase;
	private Double insurance;
	private Set<Milestone> milestones;
	private String format;
	private boolean available = true;
	private boolean marketplace = false;
	private List<BookingDTO> recentBooking;
	private boolean removePhoto = false;

	public EquipmentDTO() {

	}

	public EquipmentDTO(Equipment equipment, boolean isForList) {
		if (isForList) {
			this.setId(equipment.getId());
			this.name = equipment.getName();
			this.brand = equipment.getBrand();
			this.model = equipment.getModel();
			this.serialNumber = equipment.getSerialNumber();
			this.location = equipment.getLocation() != null ? equipment.getLocation() : "";
			if (equipment.getPhoto() != null) {
				this.photo = new DocumentDTO(equipment.getPhoto());
			}
			this.description = equipment.getDescription() != null ? equipment.getDescription() : "";
			if (equipment.getResource() != null) {
				this.resource = new EquipmentResourceDTO(equipment.getResource());
			}
		}
	}

	public EquipmentDTO(Equipment equipment) {

		this.setId(equipment.getId());
		this.name = equipment.getName();
		this.type = equipment.getType() != null ? equipment.getType() : "";
		this.description = equipment.getDescription();
		this.brand = equipment.getBrand();
		this.model = equipment.getModel();
		this.reference = equipment.getReference();
		this.serialNumber = equipment.getSerialNumber();
		this.location = equipment.getLocation() != null ? equipment.getLocation() : "";
		this.locationDetails = equipment.getLocationDetails();
		this.value = equipment.getValue();
		this.creation = equipment.getCreation();
		ata = equipment.getAta();
		purchase = equipment.getPurchase();
		insurance = equipment.getInsurance();
		this.format = equipment.getFormat();
		this.milestones = equipment.getMilestones();
		if (equipment.getCurrency() != null) {
			this.currency = equipment.getCurrency().getCurrencyCode();
		}
		this.setVersion(equipment.getVersion());
		this.isExportAta = equipment.getIsExportAta();
		Supplier supplier = equipment.getSupplier();
		if (supplier != null) {
			this.setSupplier(new SupplierDTO(equipment.getSupplier()));
		}

		SaleEntity saleEntity = equipment.getSaleEntity();
		if (saleEntity != null) {
			this.setSaleEntity(new SaleEntityDTO(saleEntity));
		}
		List<Amortization> amortization = equipment.getAmortizationArray();
		amortizationList = new LinkedList<AmortizationDTO>();
		if (amortization != null && amortization.size() > 0) {
			for (Amortization amor : amortization) {
				if (amor != null)
					amortizationList.add(new AmortizationDTO(amor));
			}

		}

		this.setContracts(equipment.getContracts().stream().map(contract -> new MaintenanceContractDTO(contract))
				.collect(Collectors.toList()));
		if (equipment.getResource() != null) {
			this.resource = new EquipmentResourceDTO(equipment.getResource());
		}
		if (equipment.getPhoto() != null) {
			this.photo = new DocumentDTO(equipment.getPhoto());
		}
		this.createdBy = equipment.getCreatedBy() != null ? equipment.getCreatedBy().getFirstName() : "";
		this.updatedBy = equipment.getUpdatedBy() != null ? equipment.getUpdatedBy().getFirstName() : "";

		// setting specs
		this.specsConsumption = equipment.getSpecsConsumption();
		this.specsOrign = equipment.getSpecsOrign();
		this.specsSize = equipment.getSpecsSize();
		this.specsWeight = equipment.getSpecsWeight();
		this.specsIp = equipment.getSpecsIp();
		if (equipment.getAttachments() != null && equipment.getAttachments().size() > 0) {
			this.attachments = equipment.getAttachments();
		}
		this.marketplace = equipment.isFromMarketPlace();
		this.available = equipment.isAvailable();
				
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the brand
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * @param brand the brand to set
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
	 * @param model the model to set
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
	 * @param reference the reference to set
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
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the locationDetails
	 */
	public String getLocationDetails() {
		return locationDetails;
	}

	/**
	 * @param locationDetails the locationDetails to set
	 */
	public void setLocationDetails(String locationDetails) {
		this.locationDetails = locationDetails;
	}

	/**
	 * @return the value
	 */
	public Float getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Float value) {
		this.value = value;
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
	 * @return the creation
	 */
	public Calendar getCreation() {
		return creation;
	}

	/**
	 * @param creation the creation to set
	 */
	public void setCreation(Calendar creation) {
		this.creation = creation;
	}

	/**
	 * @return the isExportAta
	 */
	public Boolean getIsExportAta() {
		return isExportAta;
	}

	/**
	 * @param isExportAta the isExportAta to set
	 */
	public void setIsExportAta(Boolean isExportAta) {
		this.isExportAta = isExportAta;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the supplier
	 */
	public SupplierDTO getSupplier() {
		return supplier;
	}

	/**
	 * @return the photo
	 */
	public DocumentDTO getPhoto() {
		return photo;
	}

	/**
	 * @param photo the photo to set
	 */
	public void setPhoto(DocumentDTO photo) {
		this.photo = photo;
	}

	/**
	 * @param supplier the supplier to set
	 */
	public void setSupplier(SupplierDTO supplier) {
		this.supplier = supplier;
	}

	/**
	 * Get Equipment Object of Current EqupmentDTO
	 *
	 * @return Equipment
	 */
	// @JsonIgnore
	// public Equipment getCurrentEquipment() {
	// Equipment equipment = new Equipment();
	// equipment.setId(this.getId());
	// equipment.setName(this.getName());
	// equipment.setCurrency(this.getCurrency());
	// equipment.setBrand(this.getBrand());
	// equipment.setCreation(this.getCreation());
	// equipment.setLocationDetails(this.getLocationDetails());
	// equipment.setDescription(this.getDescription());
	// equipment.setSerialNumber(serialNumber);
	// equipment.setReference(reference);
	// equipment.setValue(value);
	// equipment.setVersion(this.getVersion());
	// equipment.setPhoto(photo);
	// equipment.setContracts(contracts.stream().map(cont ->
	// cont.getMaintenanceContract(new MaintenanceContract()))
	// .collect(Collectors.toList()));
	// equipment.setCurrency(this.getCurrency());
	// amortizationList.stream().filter(amor -> amor != null)
	// .map(amor -> amor.getAmortizationDetails(new
	// Amortization())).collect(Collectors.toList());
	// return equipment;
	// }

	/**
	 * Get Equipment Entity from EqupmentDTO
	 *
	 * @return Equipment
	 */
	@JsonIgnore
	public Equipment getEquipment(Equipment equipment) {
		super.getAbstractEntityDetails(equipment);
		equipment.setName(this.getName());
		equipment.setCurrency(this.getCurrency());
		equipment.setBrand(this.getBrand());
		equipment.setCreation(this.getCreation());
		equipment.setLocationDetails(this.getLocationDetails());
		equipment.setDescription(this.getDescription());
		equipment.setSerialNumber(this.serialNumber);
		equipment.setReference(this.reference);
		equipment.setValue(this.value);
		equipment.setModel(this.model);
		equipment.setLocationDetails(this.locationDetails);
		equipment.setVersion(this.getVersion());
		equipment.setAta(ata);
		equipment.setInsurance(insurance);
		equipment.setPurchase(purchase);
		equipment.setMilestones(milestones);
		equipment.setFormat(format);

		if (this.removePhoto) {
			equipment.setPhoto(null);
		}

		// if (photo != null) {
		// equipment.setPhoto(photo.getDocument());
		// } else {
		// equipment.setPhoto(null);
		// }

		equipment.setLocation(this.location);
		equipment.setType(this.type);
		equipment.setContracts(contracts.stream().map(cont -> cont.getMaintenanceContract(new MaintenanceContract()))
				.collect(Collectors.toList()));
		equipment.setCurrency(this.getCurrency());
		if (this.saleEntity != null) {
			equipment.setSaleEntity(this.saleEntity.getSaleEntity());
		}

		if (this.supplier != null) {
			equipment.setSupplier(this.supplier.getSupplier());
		}

		// setting specs
		equipment.setSpecsConsumption(this.specsConsumption);
		equipment.setSpecsOrign(this.specsOrign);
		equipment.setSpecsSize(this.specsSize);
		equipment.setSpecsWeight(this.specsWeight);
		equipment.setSpecsIp(this.specsIp);

		// adding Amortization
		List<AmortizationDTO> amortizationDTOs = this.amortizationList;
		if (amortizationDTOs != null && !amortizationDTOs.isEmpty()) {
			equipment.setAmortizationArray(amortizationDTOs.stream().map(line -> {
				Amortization entity = line.getAmortizationDetails(new Amortization());
				entity.setEquipment(equipment);
				return entity;
			}).collect(Collectors.toList()));
		}

		equipment.setFromMarketPlace(this.marketplace);
		return equipment;
	}

	@JsonIgnore
	public Equipment getEquipment() {
		return this.getEquipment(new Equipment());
	}

	/**
	 * @return the saleEntity
	 */
	public SaleEntityDTO getSaleEntity() {
		return saleEntity;
	}

	/**
	 * @param saleEntity the saleEntity to set
	 */
	public void setSaleEntity(SaleEntityDTO saleEntity) {
		this.saleEntity = saleEntity;
	}

	/**
	 * @return the amortizationList
	 */
	public List<AmortizationDTO> getAmortizationList() {
		return amortizationList;
	}

	/**
	 * @param amortizationList the amortizationList to set
	 */
	public void setAmortizationList(List<AmortizationDTO> amortizationList) {
		this.amortizationList = amortizationList;
	}

	/**
	 * @return the contracts
	 */
	public List<MaintenanceContractDTO> getContracts() {
		return contracts;
	}

	/**
	 * @param contracts the contracts to set
	 */
	public void setContracts(List<MaintenanceContractDTO> contracts) {
		this.contracts = contracts;
	}

	/**
	 * @return the resource
	 */
	public EquipmentResourceDTO getResource() {
		return resource;
	}

	/**
	 * @param resource the resource to set
	 */
	public void setResource(EquipmentResourceDTO resource) {
		this.resource = resource;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the updatedBy
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * @return the updation
	 */
	public Calendar getUpdation() {
		return updation;
	}

	/**
	 * @param updation the updation to set
	 */
	public void setUpdation(Calendar updation) {
		this.updation = updation;
	}

	/**
	 * @return the functions
	 */
	public Set<FunctionDTO> getFunctions() {
		return functions;
	}

	/**
	 * @param functions the functions to set
	 */
	public void setFunctions(Set<FunctionDTO> functions) {
		this.functions = functions;
	}

	/**
	 * @return the unassignedResourceFunctionIds
	 */
	public Set<Long> getUnassignedResourceFunctionIds() {
		return unassignedResourceFunctionIds;
	}

	/**
	 * @param unassignedResourceFunctionIds the unassignedResourceFunctionIds to set
	 */
	public void setUnassignedResourceFunctionIds(Set<Long> unassignedResourceFunctionIds) {
		this.unassignedResourceFunctionIds = unassignedResourceFunctionIds;
	}

	/**
	 * @return the specsConsumption
	 */
	public String getSpecsConsumption() {
		return specsConsumption;
	}

	/**
	 * @param specsConsumption the specsConsumption to set
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
	 * @param specsWeight the specsWeight to set
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
	 * @param specsSize the specsSize to set
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
	 * @param specsOrign the specsOrign to set
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
	 * @param specsIp the specsIp to set
	 */
	public void setSpecsIp(String specsIp) {
		this.specsIp = specsIp;
	}

	/**
	 * @return the unassignedAmortizationIds
	 */
	public Set<Long> getUnassignedAmortizationIds() {
		return unassignedAmortizationIds;
	}

	/**
	 * @param unassignedAmortizationIds the unassignedAmortizationIds to set
	 */
	public void setUnassignedAmortizationIds(Set<Long> unassignedAmortizationIds) {
		this.unassignedAmortizationIds = unassignedAmortizationIds;
	}

	/**
	 * @return the attachments
	 */
	public Set<Attachment> getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EquipmentDTO [name=" + name + ", type=" + type + ", description=" + description + ", brand=" + brand
				+ ", model=" + model + ", reference=" + reference + ", serialNumber=" + serialNumber + ", location="
				+ location + ", photo=" + photo + ", locationDetails=" + locationDetails + ", resource=" + resource
				+ ", value=" + value + ", currency=" + currency + ", contracts=" + contracts + ", supplier=" + supplier
				+ ", creation=" + creation + ", amortizationList=" + amortizationList + ", saleEntity=" + saleEntity
				+ ", ata=" + ata + ", isExportAta=" + isExportAta + "]";
	}

	/**
	 * @return the ata
	 */
	public Double getAta() {
		return ata;
	}

	/**
	 * @param ata the ata to set
	 */
	public void setAta(Double ata) {
		this.ata = ata;
	}

	/**
	 * @return the purchase
	 */
	public Double getPurchase() {
		return purchase;
	}

	/**
	 * @param purchage the purchage to set
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
	 * @param insurance the insurance to set
	 */
	public void setInsurance(Double insurance) {
		this.insurance = insurance;
	}

	/**
	 * @return the milestones
	 */
	public Set<Milestone> getMilestones() {
		return milestones;
	}

	/**
	 * @param milestones the milestones to set
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
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
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
	 * @return the marketplace
	 */
	public boolean isMarketplace() {
		return marketplace;
	}

	/**
	 * @param marketplace the marketplace to set
	 */
	public void setMarketplace(boolean marketplace) {
		this.marketplace = marketplace;
	}

	/**
	 * @return the recentBooking
	 */
	public List<BookingDTO> getRecentBooking() {
		return recentBooking;
	}

	/**
	 * @param recentBooking the recentBooking to set
	 */
	public void setRecentBooking(List<BookingDTO> recentBooking) {
		this.recentBooking = recentBooking;
	}

	/**
	 * @return the removePhoto
	 */
	public boolean isRemovePhoto() {
		return removePhoto;
	}

	/**
	 * @param removePhoto the removePhoto to set
	 */
	public void setRemovePhoto(boolean removePhoto) {
		this.removePhoto = removePhoto;
	}

	public int getQuantityForBookingLine() {
		return quantityForBookingLine;
	}

	public void setQuantityForBookingLine(int quantityForBookingLine) {
		this.quantityForBookingLine = quantityForBookingLine;
	}

	
}
