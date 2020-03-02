package com.teamium.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.FunctionKeyword;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.Theme;
import com.teamium.domain.Vat;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.resources.accountancy.AccountancyNumber;
import com.teamium.domain.prod.resources.equipments.EquipmentFunction;
import com.teamium.domain.prod.resources.functions.DefaultResource;
import com.teamium.domain.prod.resources.functions.Function;
import com.teamium.domain.prod.resources.functions.ProcessFunction;
import com.teamium.domain.prod.resources.functions.RatedFunction;
import com.teamium.domain.prod.resources.functions.RightFunction;
import com.teamium.domain.prod.resources.functions.units.RateUnit;
import com.teamium.domain.prod.resources.staff.StaffFunction;
import com.teamium.domain.prod.resources.suppliers.SupplyFunction;
import com.teamium.dto.prod.resources.functions.RateDTO;
import com.teamium.enums.RateUnitType;
import com.teamium.enums.AssignationType.AssignationType;
import com.teamium.exception.NotFoundException;
import com.teamium.utils.FunctionUtil;

/**
 * Wrapper class for Function
 * 
 * @author Teamium
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class FunctionDTO extends AbstractDTO {

	private Integer starRating;
	private Integer position;
	private String value;
	private String defaultCurrency;
	private String qualifiedName;
	private String theme;
	private Integer display;
	private String contract;
	private String origine;
	private FunctionDTO parent;
	private Set<XmlEntity> fees = new HashSet<XmlEntity>();
	private String group;
	private Vat vat;
	private String keyword;
	private Set<AccountancyNumberDTO> accoutingCode = new HashSet<AccountancyNumberDTO>();
	private String basis;
	private Boolean archived = Boolean.FALSE;
	private String description;
	private ResourceDTO<ResourceDTO<?>> defaultResource;
	private String type;
	private SupplierDTO supplierDTO;
	private List<FunctionDTO> childFunctions = new ArrayList<FunctionDTO>();
	private FunctionKeyword functionKeyword;
	private List<RateDTO> rates = new ArrayList<RateDTO>();

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	public FunctionDTO() {
		super();
	}

	public FunctionDTO(Function entity) {
		super(entity);
		this.position = entity.getPosition();
		this.value = entity.getValue();
		if (entity.getDefaultCurrency() != null) {
			this.defaultCurrency = entity.getDefaultCurrency().getCurrencyCode();
		}
		this.qualifiedName = entity.getQualifiedName();
		this.display = entity.getDisplayNumber();

		this.contract = entity.getContractEdition();

		if (entity.getDefaultAssignation() != null) {
			this.origine = entity.getDefaultAssignation();
		}
		if (entity.getChildFunctions() != null && entity.getChildFunctions().size() > 0) {
			this.childFunctions = entity.getChildFunctions().stream().filter(fun -> fun != null).map(fun -> {
				fun.setParent(null);
				return new FunctionDTO(fun);
			}).collect(Collectors.toList());
		}
		if (entity.getParent() != null)
			this.parent = new FunctionDTO(entity.getParent());
		this.fees = entity.getAppliedFees();
		this.group = entity.getGroup();
		this.vat = entity.getVat();
		this.keyword = entity.getKeyword();

		this.accoutingCode = entity.getAccountancyNumbers().stream().map(acc -> new AccountancyNumberDTO(acc))
				.collect(Collectors.toSet());
		if (entity.getDefaultUnit() != null) {
			this.basis = entity.getDefaultUnit().getKey();
		}
		if (entity.getTheme() != null) {
			this.theme = entity.getTheme().getKey();
		}

		this.archived = entity.getArchived();
		type = FunctionUtil.getFunctionType(entity);
		this.description = entity.getDescription();
		FunctionKeyword funkeyword = entity.getFunctionKeyword();
		if (funkeyword != null) {
			this.functionKeyword = funkeyword;
		}
	}

	public FunctionDTO(RatedFunction entity) {
		this((Function) entity);
		this.description = entity.getDescription();
		// DefaultResource defResource = entity.getDefaultResource();
		// if (defResource != null) {
		// this.defaultResource = new ResourceDTO<>(defResource);
		// }
	}

	/**
	 * Get Function Entity from DTO
	 * 
	 * @param function
	 *            the function
	 * 
	 * @return Function object
	 */
	@JsonIgnore
	public Function getFunction(Function function) {
		super.getAbstractEntityDetails(function);
		function.setPosition(this.position);
		if (StringUtils.isBlank(this.value)) {
			logger.error("Value can not be blank ");
			throw new NotFoundException("Value can not be blank");
		}
		// if (!StringUtils.isAlphanumeric(this.value)) {
		// logger.error("Value can not contain special charater ");
		// throw new NotFoundException("Value can not contain special charater ");
		// }
		function.setValue(this.value);
		function.setDefaultCurrency(this.defaultCurrency);
		function.setQualifiedName(this.qualifiedName);
		function.setDisplayNumber(this.display);

		function.setContractEdition(this.contract);
		AssignationType assignationType;
		if (StringUtils.isBlank(this.origine)) {
			assignationType = AssignationType.INTERNAL;
		} else {
			assignationType = AssignationType.getEnum(this.origine);
		}
		function.setDefaultAssignation(assignationType.getAssignType());
		// function.setParent(parent.getFunction(null));
		function.setAppliedFees(this.fees);
		function.setGroup(this.group);
		function.setVat(this.vat);
		function.setKeyword(this.keyword);
		if (this.accoutingCode != null && !this.accoutingCode.isEmpty()) {
			this.accoutingCode.stream().collect(Collectors.groupingBy(AccountancyNumberDTO::getType))
					.forEach((key, value) -> {
						if (value.size() > 1) {
							throw new NotFoundException("Duplicate Accounting type " + key);
						}
					});
			;
			ArrayList<AccountancyNumber> functionAccNo = new ArrayList<AccountancyNumber>();
			this.accoutingCode.forEach(acc -> functionAccNo.add(acc.getAccountancyNumber(new AccountancyNumber())));
			function.setAccountancyNumbers(functionAccNo);
		}
		if (this.type != null && !this.type.equalsIgnoreCase(TeamiumConstants.FOLDER_FUNCTION_TYPE)) {
			RateUnit rateUnit = new RateUnit();
			rateUnit.setKey(RateUnitType.getEnum(basis).getType());
			function.setDefaultUnit(rateUnit);
		}
		if (this.theme != null) {
			Theme tm = new Theme();
			tm.setKey(this.theme);
			function.setTheme(tm);
		}
		function.setArchived(this.archived);
		function.setFunctionKeyword(this.getFunctionKeyword());
		return function;
	}

	/**
	 * Get Function Entity from DTO
	 * 
	 * @return Function
	 */
	@JsonIgnore
	public Function getFunction() {
		return this.getFunction(new Function());
	}

	/**
	 * Get RatedFunction Entity from DTO
	 * 
	 * @return RatedFunction
	 */
	@JsonIgnore
	public RatedFunction getRatedFunction(RatedFunction ratedFunction) {
		ratedFunction.setDescription(description);
		if (this.getId() != null) {
			ratedFunction.setId(this.getId());
			ratedFunction.setVersion(this.getVersion());
		}
		getFunction(ratedFunction);
		return ratedFunction;
	}

	/**
	 * Get StaffFunction Entity from DTO
	 * 
	 * @return RatedFunction
	 */
	@JsonIgnore
	public StaffFunction getStaffFunction() {
		StaffFunction staffFunction = new StaffFunction();
		getStaffFunction(staffFunction);
		return staffFunction;
	}

	/**
	 * Get StaffFunction Entity from DTO
	 * 
	 * @param staffFunction
	 * @return StaffFunction
	 */
	@JsonIgnore
	public StaffFunction getStaffFunction(StaffFunction staffFunction) {
		getRatedFunction(staffFunction);
		return staffFunction;
	}

	/**
	 * Get EquipmentFunction Entity from DTO
	 * 
	 * @return EquipmentFunction
	 */
	@JsonIgnore
	public EquipmentFunction getEquipmnetFunction() {
		EquipmentFunction equipmentFunction = new EquipmentFunction();
		return getEquipmnetFunction(equipmentFunction);
	}

	/**
	 * Get EquipmentFunction Entity from DTO
	 * 
	 * @param equipmentFunction
	 * @return EquipmentFunction
	 */
	@JsonIgnore
	public EquipmentFunction getEquipmnetFunction(EquipmentFunction equipmentFunction) {
		getRatedFunction(equipmentFunction);
		return equipmentFunction;
	}

	/**
	 * Get SupplyFunction Entity from DTO
	 * 
	 * @return SupplyFunction
	 */
	@JsonIgnore
	public SupplyFunction getSupplyFunction() {
		SupplyFunction supplyFunction = new SupplyFunction();
		return getSupplyFunction(supplyFunction);
	}

	@JsonIgnore
	public SupplyFunction getSupplyFunction(SupplyFunction supplyFunction) {
		if (supplierDTO != null) {
			supplyFunction.setSupplier(supplierDTO.getSupplier());
		}
		getRatedFunction(supplyFunction);
		return supplyFunction;
	}

	/**
	 * Get RightFunction Entity from DTO
	 * 
	 * @return RightFunction
	 */
	@JsonIgnore
	public RightFunction getRightFunction() {
		RightFunction rightFunction = new RightFunction();
		getRightFunction(rightFunction);
		return rightFunction;
	}

	/**
	 * Get RightFunction Entity from DTO
	 * 
	 * @param rightFunction
	 * @return RightFunction
	 */
	@JsonIgnore
	public RightFunction getRightFunction(RightFunction rightFunction) {
		getRatedFunction(rightFunction);
		return rightFunction;
	}

	/**
	 * Get ProcessFunction Entity from DTO
	 * 
	 * @return ProcessFunction
	 */
	@JsonIgnore
	public ProcessFunction getProcessFunction() {
		ProcessFunction processFunction = new ProcessFunction();
		getProcessFunction(processFunction);
		return processFunction;
	}

	/**
	 * Get ProcessFunction Entity from DTO
	 * 
	 * @param processFunction
	 * @return ProcessFunction
	 */
	@JsonIgnore
	public ProcessFunction getProcessFunction(ProcessFunction processFunction) {
		getRatedFunction(processFunction);
		return processFunction;
	}

	/**
	 * @return the position
	 */
	public Integer getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(Integer position) {
		this.position = position;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the defaultCurrency
	 */
	public String getDefaultCurrency() {
		return defaultCurrency;
	}

	/**
	 * @param defaultCurrency
	 *            the defaultCurrency to set
	 */
	public void setDefaultCurrency(String defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}

	/**
	 * @return the qualifiedName
	 */
	public String getQualifiedName() {
		return qualifiedName;
	}

	/**
	 * @param qualifiedName
	 *            the qualifiedName to set
	 */
	public void setQualifiedName(String qualifiedName) {
		this.qualifiedName = qualifiedName;
	}

	/**
	 * @return the display
	 */
	public Integer getDisplay() {
		return display;
	}

	/**
	 * @param display
	 *            the display to set
	 */
	public void setDisplay(Integer display) {
		this.display = display;
	}

	/**
	 * @return the origine
	 */
	public String getOrigine() {
		return origine;
	}

	/**
	 * @param origine
	 *            the origine to set
	 */
	public void setOrigine(String origine) {
		this.origine = origine;
	}

	/**
	 * @return the parent
	 */
	public FunctionDTO getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(FunctionDTO parent) {
		this.parent = parent;
	}

	/**
	 * @return the fees
	 */
	public Set<XmlEntity> getFees() {
		return fees;
	}

	/**
	 * @param fees
	 *            the fees to set
	 */
	public void setFees(Set<XmlEntity> fees) {
		this.fees = fees;
	}

	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group
	 *            the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
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
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * @param keyword
	 *            the keyword to set
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	/**
	 * @return the accoutingCode
	 */
	public Set<AccountancyNumberDTO> getAccoutingCode() {
		return accoutingCode;
	}

	/**
	 * @param accoutingCode
	 *            the accoutingCode to set
	 */
	public void setAccoutingCode(Set<AccountancyNumberDTO> accoutingCode) {
		this.accoutingCode = accoutingCode;
	}

	/**
	 * @return the basis
	 */
	public String getBasis() {
		return basis;
	}

	/**
	 * @param basis
	 *            the basis to set
	 */
	public void setBasis(String basis) {
		this.basis = basis;
	}

	/**
	 * @return the archived
	 */
	public Boolean getArchived() {
		return archived;
	}

	/**
	 * @param archived
	 *            the archived to set
	 */
	public void setArchived(Boolean archived) {
		this.archived = archived;
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
	 * @return the defaultResource
	 */
	public ResourceDTO<ResourceDTO<?>> getDefaultResource() {
		return defaultResource;
	}

	/**
	 * @param defaultResource
	 *            the defaultResource to set
	 */
	public void setDefaultResource(ResourceDTO<ResourceDTO<?>> defaultResource) {
		this.defaultResource = defaultResource;
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
	 * @return the supplierDTO
	 */
	public SupplierDTO getSupplierDTO() {
		return supplierDTO;
	}

	/**
	 * @param supplierDTO
	 *            the supplierDTO to set
	 */
	public void setSupplierDTO(SupplierDTO supplierDTO) {
		this.supplierDTO = supplierDTO;
	}

	/**
	 * @return the contract
	 */
	public String getContract() {
		return contract;
	}

	/**
	 * @param contract
	 *            the contract to set
	 */
	public void setContract(String contract) {
		this.contract = contract;
	}

	/**
	 * @return the childFunctions
	 */
	public List<FunctionDTO> getChildFunctions() {
		return childFunctions;
	}

	/**
	 * @param childFunctions
	 *            the childFunctions to set
	 */
	public void setChildFunctions(List<FunctionDTO> childFunctions) {
		this.childFunctions = childFunctions;
	}

	/**
	 * @return the functionKeyword
	 */
	public FunctionKeyword getFunctionKeyword() {
		return functionKeyword;
	}

	/**
	 * @param functionKeyword
	 *            the functionKeyword to set
	 */
	public void setFunctionKeyword(FunctionKeyword functionKeyword) {
		this.functionKeyword = functionKeyword;
	}

	/**
	 * @return the starRating
	 */
	public Integer getStarRating() {
		return starRating;
	}

	/**
	 * @param starRating
	 *            the starRating to set
	 */
	public void setStarRating(Integer starRating) {
		this.starRating = starRating;
	}

	/**
	 * @return the rates
	 */
	public List<RateDTO> getRates() {
		return rates;
	}

	/**
	 * @param rates
	 *            the rates to set
	 */
	public void setRates(List<RateDTO> rates) {
		this.rates = rates;
	}

	/**
	 * @return the theme
	 */
	public String getTheme() {
		return theme;
	}

	/**
	 * @param theme
	 *            the theme to set
	 */
	public void setTheme(String theme) {
		this.theme = theme;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FunctionDTO [starRating=" + starRating + ", position=" + position + ", value=" + value
				+ ", defaultCurrency=" + defaultCurrency + ", qualifiedName=" + qualifiedName + ", theme=" + theme
				+ ", display=" + display + ", contract=" + contract + ", origine=" + origine + ", parent=" + parent
				+ ", fees=" + fees + ", group=" + group + ", vat=" + vat + ", keyword=" + keyword
				+ ", accountancyNumbers=" + accoutingCode + ", basis=" + basis + ", archived=" + archived
				+ ", description=" + description + ", defaultResource=" + defaultResource + ", type=" + type
				+ ", supplierDTO=" + supplierDTO + ", childFunctions=" + childFunctions + ", functionKeyword="
				+ functionKeyword + "]";
	}

}
