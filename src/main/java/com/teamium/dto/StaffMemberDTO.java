package com.teamium.dto;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.Address;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.resources.ResourceFunction;
import com.teamium.domain.prod.resources.contacts.Contact;
import com.teamium.domain.prod.resources.equipments.Attachment;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.domain.prod.resources.staff.StaffMemberIdentity;
import com.teamium.domain.prod.resources.staff.StaffMemberSkill;
import com.teamium.domain.prod.resources.staff.contract.ContractSetting;
import com.teamium.domain.prod.resources.staff.contract.EntertainmentContractSetting;
import com.teamium.enums.ContractType;
import com.teamium.enums.project.Language;

/**
 * DTO for StaffMember Entity
 * 
 * @author Teamium
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class StaffMemberDTO extends ContactDTO {

	private AddressDTO address;
	private Set<StaffMemberSkillDTO> skills;
	private Set<String> languages;
	private Set<Locale> localeLanguages;
	private Set<XmlEntity> specialties;
	private ContactDTO agent;
	private String employeeCode;
	private String comments;
	private String location;
	private UserSettingDTO userSettingDTO;
	private Set<Attachment> attachments = new HashSet<>();
	private StaffResourceDTO resource;
	private ContractSettingDTO contractSettingDTO;
	private boolean freelance = false;
	private StaffMemberIdentityDTO staffMemberIdentityDTO;
	private Set<FunctionDTO> functions;
	private Set<String> groupName = new HashSet<>();
	private boolean available = true;
	private boolean marketplace = false;
	private Long labourRuleId;
	private Set<GroupDTO> groupDTOs = new HashSet<>();
	private FunctionDTO mainFunction;
	private String groupNames;
	private String hiringDate;
	private byte workPercentage;
	private List<BookingDTO> recentBooking;
	private String uiLanguage;

	public StaffMemberDTO() {
		super();
	}

	public StaffMemberDTO(StaffMember entity, boolean isForList) {
		super(entity, isForList);
		if (isForList) {
			Address staffAddress = entity.getAddress();
			if (staffAddress != null) {
				this.location = staffAddress.getCity();
			}
			if (entity.getLanguages() != null) {
				languages = entity.getLanguages().stream().map(lang -> lang.getKey()).collect(Collectors.toSet());
			}
			Set<StaffMemberSkill> skills = entity.getSkills();
			if (skills != null && !skills.isEmpty()) {
				this.skills = skills.stream().map(domain -> new StaffMemberSkillDTO(domain))
						.collect(Collectors.toSet());
			}
			if (entity.getResource() != null) {
				this.resource = new StaffResourceDTO(entity.getResource());
			}
			this.marketplace = entity.isFromMarketPlace();
			ContractSetting contractSetting = entity.getContractSetting();
			if (contractSetting != null) {
				if (entity.getContractSetting() instanceof EntertainmentContractSetting) {
					if (entity.getContractSetting().getType().getKey()
							.equalsIgnoreCase(ContractType.FREELANCE.toString())) {
						this.freelance = true;
					}
				}
			}
			if (entity.getUserSetting() != null) {
				this.userSettingDTO = new UserSettingDTO(entity.getUserSetting());
			}
			if (entity.getLabourRule() != null) {
				this.labourRuleId = entity.getLabourRule().getLabourRuleId();
			}
			this.groupNames = StringUtils
					.join(entity.getGroups().stream().map(group -> group.getName()).collect(Collectors.toList()), ',');
			if (StringUtils.isBlank(groupNames)) {
				this.groupNames = null;
			}
			if (entity.getResource() != null) {
				ResourceFunction resourceFunction = entity.getResource().getFunctions().stream()
						.filter(fun -> fun.isPrimaryFunction()).findFirst().orElse(null);
				if (resourceFunction != null) {
					this.mainFunction = new FunctionDTO();
					this.mainFunction.setQualifiedName(resourceFunction.getFunction().getQualifiedName());
					this.mainFunction.setValue(resourceFunction.getFunction().getValue());
				}
			}
			this.available = entity.isAvailable();
		}
	}

	public StaffMemberDTO(StaffMember entity) {
		super(entity);
		Address staffAddress = entity.getAddress();
		if (staffAddress != null) {
			address = new AddressDTO(staffAddress);
			this.location = address.getCity();
		}

		StaffMemberIdentity staffMemberIdentity = entity.getIdentity();
		if (staffMemberIdentity != null) {
			this.staffMemberIdentityDTO = new StaffMemberIdentityDTO(staffMemberIdentity);
		}

		Set<StaffMemberSkill> skills = entity.getSkills();
		if (skills != null && !skills.isEmpty()) {
			this.skills = skills.stream().map(domain -> new StaffMemberSkillDTO(domain)).collect(Collectors.toSet());
		}
		if (entity.getLanguages() != null) {
			languages = entity.getLanguages().stream().map(lang -> lang.getKey()).collect(Collectors.toSet());
		}
		localeLanguages = entity.getLocaleLanguages();
		specialties = entity.getSpecialties();
		this.userSettingDTO = new UserSettingDTO(entity.getUserSetting());
		Contact staffAgent = entity.getAgent();
		if (staffAgent != null) {
			agent = new ContactDTO(staffAgent);
		}
		comments = entity.getComments();
		if (entity.getAttachments() != null && entity.getAttachments().size() > 0) {
			this.attachments = entity.getAttachments();
		}
		if (entity.getResource() != null) {
			this.resource = new StaffResourceDTO(entity.getResource());
		}
		ContractSetting contractSetting = entity.getContractSetting();
		if (contractSetting != null) {
			this.contractSettingDTO = new ContractSettingDTO(contractSetting);
			if (entity.getContractSetting() instanceof EntertainmentContractSetting) {
				if (entity.getContractSetting().getType().getKey()
						.equalsIgnoreCase(ContractType.FREELANCE.toString())) {
					this.freelance = true;
				}
			}
		}

		if (entity.getRole() != null && !entity.getRole().isEmpty()) {
			this.setRole(entity.getRole());
		}

		this.marketplace = entity.isFromMarketPlace();
		this.employeeCode = entity.getEmployeeCode();
		this.setJobTitle(entity.getFunction());
		if (entity.getLabourRule() != null) {
			this.labourRuleId = entity.getLabourRule().getLabourRuleId();
		}
		this.groupDTOs = entity.getGroups().stream().map(group -> new GroupDTO(group, "forStaffView"))
				.collect(Collectors.toSet());
		this.hiringDate = new org.joda.time.LocalDate(entity.getHiringDate()).toString();
		this.workPercentage = entity.getWorkPercentage();
		this.uiLanguage = entity.getUiLanguage();
		this.available = entity.isAvailable();

	}

	public StaffMemberDTO(StaffMember staffMember, String forDigitalSignature) {
		this.setId(staffMember.getId());
		this.setFirstName(staffMember.getFirstName());
		this.setLastName(staffMember.getName());
		this.setRole(staffMember.getRole());
		this.setUserSettingDTO(new UserSettingDTO(staffMember.getUserSetting()));
		if (staffMember.getResource() != null) {
			this.resource = new StaffResourceDTO();
			this.resource.setId(staffMember.getResource().getId());
			ResourceFunction resourceFunction = staffMember.getResource().getFunctions().stream()
					.filter(fun -> fun.isPrimaryFunction()).findFirst().orElse(null);
			if (resourceFunction != null) {
				this.mainFunction = new FunctionDTO();
				this.mainFunction.setId(resourceFunction.getFunction().getId());
			}
		}
	}

	public StaffMemberDTO(StaffMember staffMember, String brief, Boolean forExpenseReport) {
		this.setId(staffMember.getId());
		this.setFirstName(staffMember.getFirstName());
		this.setLastName(staffMember.getName());

	}
	
	public StaffMemberDTO getStaffWithoutContractSettingDTO(StaffMember staffMember) {
		StaffMemberDTO staffMemberDTO = new StaffMemberDTO(staffMember);
		staffMemberDTO.setContractSettingDTO(null);
		return staffMemberDTO;
	}

	/**
	 * @return the address
	 */
	public AddressDTO getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(AddressDTO address) {
		this.address = address;
	}

	/**
	 * Given skills will be alphabetically sorted.
	 * 
	 * @return the skills
	 */
	public Set<StaffMemberSkillDTO> getSkills() {
		if (skills != null) {
			List<StaffMemberSkillDTO> alphabeticallySortedSkillDTOs = skills.stream()
					.sorted((x, y) -> x.getDomain().compareToIgnoreCase(y.getDomain())).collect(Collectors.toList());
			skills = new LinkedHashSet<>(alphabeticallySortedSkillDTOs);
		}
		return skills;
	}

	/**
	 * @param skills the skills to set
	 */
	public void setSkills(Set<StaffMemberSkillDTO> skills) {
		this.skills = skills;
	}

	/**
	 * @return the languages
	 */
	public Set<String> getLanguages() {
		return languages;
	}

	/**
	 * @param languages the languages to set
	 */
	public void setLanguages(Set<String> languages) {
		this.languages = languages;
	}

	/**
	 * @return the localeLanguages
	 */
	public Set<Locale> getLocaleLanguages() {
		return localeLanguages;
	}

	/**
	 * @param localeLanguages the localeLanguages to set
	 */
	public void setLocaleLanguages(Set<Locale> localeLanguages) {
		this.localeLanguages = localeLanguages;
	}

	/**
	 * @return the specialties
	 */
	public Set<XmlEntity> getSpecialties() {
		return specialties;
	}

	/**
	 * @param specialties the specialties to set
	 */
	public void setSpecialties(Set<XmlEntity> specialties) {
		this.specialties = specialties;
	}

	/**
	 * @return the agent
	 */
	public ContactDTO getAgent() {
		return agent;
	}

	/**
	 * @param agent the agent to set
	 */
	public void setAgent(ContactDTO agent) {
		this.agent = agent;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * Get StaffMember entity from DTO
	 * 
	 * @param staffMember
	 * @return StaffMember
	 */

	@JsonIgnore
	public StaffMember getStaffMember(StaffMember staffMember) {
		if (this.getAddress() != null) {
			staffMember.setAddress(this.getAddress().getAddress(staffMember.getAddress()));
		}
		if (this.getStaffMemberIdentityDTO() != null) {
			staffMember.setIdentity(this.getStaffMemberIdentityDTO().getStaffMemberIdentity());
		}

		staffMember.setName(this.getLastName());
		staffMember.setFirstName(this.getFirstName());
		staffMember.setNumbers(this.getNumbers());
		staffMember.setUserSetting(this.getUserSettingDTO() == null ? null
				: this.getUserSettingDTO().getUserSetting(staffMember.getUserSetting()));
		super.getContact(staffMember);
		if (languages != null) {
			staffMember.setLanguages(languages.stream().map(lang -> {
				String langValue = Language.getEnum(lang).getLanguage();
				XmlEntity langs = new XmlEntity();
				langs.setKey(langValue);
				return langs;
			}).collect(Collectors.toSet()));
		}
		staffMember.setContractSetting(
				this.contractSettingDTO == null ? null : this.getContractSettingDTO().getContractSetting());
		staffMember.setFromMarketPlace(this.marketplace);
		staffMember.setEmployeeCode(this.employeeCode);
		staffMember.setFunction(this.getJobTitle());
		staffMember.setComments(this.comments);
		if (workPercentage >= 0 && workPercentage <= 100) {
			staffMember.setWorkPercentage(workPercentage);
		}
		return staffMember;
	}

	/**
	 * @return the userSettingDTO
	 */
	public UserSettingDTO getUserSettingDTO() {
		return userSettingDTO;
	}

	/**
	 * @param userSettingDTO the userSettingDTO to set
	 */
	public void setUserSettingDTO(UserSettingDTO userSettingDTO) {
		this.userSettingDTO = userSettingDTO;
	}

	/**
	 * Get StaffMember entity from DTO
	 * 
	 * @return StaffMember
	 */

	@JsonIgnore
	public StaffMember getStaffMember() {
		return this.getStaffMember(new StaffMember());
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
	 * @return the resource
	 */
	public StaffResourceDTO getResource() {
		return resource;
	}

	/**
	 * @param resource the resource to set
	 */
	public void setResource(StaffResourceDTO resource) {
		this.resource = resource;
	}

	/**
	 * @return the contractSettingDTO
	 */
	public ContractSettingDTO getContractSettingDTO() {
		return contractSettingDTO;
	}

	/**
	 * @param contractSettingDTO the contractSettingDTO to set
	 */
	public void setContractSettingDTO(ContractSettingDTO contractSettingDTO) {
		this.contractSettingDTO = contractSettingDTO;
	}

	/**
	 * @return the staffMemberIdentityDTO
	 */
	public StaffMemberIdentityDTO getStaffMemberIdentityDTO() {
		return staffMemberIdentityDTO;
	}

	/**
	 * @param staffMemberIdentityDTO the staffMemberIdentityDTO to set
	 */
	public void setStaffMemberIdentityDTO(StaffMemberIdentityDTO staffMemberIdentityDTO) {
		this.staffMemberIdentityDTO = staffMemberIdentityDTO;
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
	 * @return the freelance
	 */
	public boolean isFreelance() {
		return freelance;
	}

	/**
	 * @param freelance the freelance to set
	 */
	public void setFreelance(boolean freelance) {
		this.freelance = freelance;
	}

	/**
	 * @return the groupName
	 */
	public Set<String> getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(Set<String> groupName) {
		this.groupName = groupName;
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
	 * @return the employeeCode
	 */
	public String getEmployeeCode() {
		return employeeCode;
	}

	/**
	 * @param employeeCode the employeeCode to set
	 */
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	/**
	 * @return the labourRuleId
	 */
	public Long getLabourRuleId() {
		return labourRuleId;
	}

	/**
	 * @param labourRuleId the labourRuleId to set
	 */
	public void setLabourRuleId(Long labourRuleId) {
		this.labourRuleId = labourRuleId;
	}

	/**
	 * @return the groupDTOs
	 */
	public Set<GroupDTO> getGroupDTOs() {
		return groupDTOs;
	}

	/**
	 * @param groupDTOs the groupDTOs to set
	 */
	public void setGroupDTOs(Set<GroupDTO> groupDTOs) {
		this.groupDTOs = groupDTOs;
	}

	/**
	 * @return the mainFunction
	 */
	public FunctionDTO getMainFunction() {
		return mainFunction;
	}

	/**
	 * @param mainFunction the mainFunction to set
	 */
	public void setMainFunction(FunctionDTO mainFunction) {
		this.mainFunction = mainFunction;
	}

	/**
	 * @return the groupNames
	 */
	public String getGroupNames() {
		return groupNames;
	}

	/**
	 * @param groupNames the groupNames to set
	 */
	public void setGroupNames(String groupNames) {
		this.groupNames = groupNames;
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
	 * @param workPercentage the workPercentage to set
	 */
	public void setWorkPercentage(byte workPercentage) {
		this.workPercentage = workPercentage;
	}

	/**
	 * @return the hiringDate
	 */
	public String getHiringDate() {
		return hiringDate;
	}

	/**
	 * @param hiringDate the hiringDate to set
	 */
	public void setHiringDate(String hiringDate) {
		this.hiringDate = hiringDate;
	}

	/**
	 * @return the workPercentage
	 */
	public byte getWorkPercentage() {
		return workPercentage;
	}

	/**
	 * 
	 * @return the uiLanguage
	 */
	public String getUiLanguage() {
		return uiLanguage;
	}

	/**
	 * 
	 * @param uiLanguage the uiLanguage to set
	 */
	public void setUiLanguage(String uiLanguage) {
		this.uiLanguage = uiLanguage;
	}

}
