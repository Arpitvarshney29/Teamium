
package com.teamium.domain.prod.resources.staff;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.teamium.domain.Address;
import com.teamium.domain.BankInformation;
import com.teamium.domain.Group;
import com.teamium.domain.LabourRule;
import com.teamium.domain.Milestone;
import com.teamium.domain.TimeZone;
import com.teamium.domain.UserLeaveRecord;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.resources.ResourceEntity;
import com.teamium.domain.prod.resources.ResourceFunction;
import com.teamium.domain.prod.resources.SaleEntity;
import com.teamium.domain.prod.resources.contacts.Contact;
import com.teamium.domain.prod.resources.functions.Function;
import com.teamium.domain.prod.resources.staff.contract.ContractSetting;

/**
 * Describe a staff member used in resource planning
 * 
 * @author sraybaud - NovaRem
 *
 */
@Entity
@DiscriminatorValue(StaffMember.TYPE)
@NamedQueries({
		@NamedQuery(name = StaffMember.QUERY_findByLogin, query = "SELECT sm FROM StaffMember sm WHERE sm.userSetting.login = ?1"),
		@NamedQuery(name = StaffMember.QUERY_findByRole, query = "SELECT sm FROM StaffMember sm, IN (sm.userSetting.roles) r WHERE r = ?1 ORDER BY sm.name"),
		@NamedQuery(name = StaffMember.QUERY_findEligibleToContractByContractSetting, query = "SELECT DISTINCT sm FROM StaffMember sm WHERE EXISTS(SELECT b FROM Booking b WHERE EXISTS(SELECT s FROM StaffResource s WHERE s.entity = sm AND s = b.resource) AND b.contractStatus = ?1)  AND sm.contractSetting.type = ?2"),
		@NamedQuery(name = StaffMember.QUERY_findByEmployeeId, query = "SELECT sm FROM StaffMember sm, IN(sm.staffEmployeeId) se WHERE se.employeeId = ?1"),
		@NamedQuery(name = StaffMember.QUERY_findLikeKeyword, query = "SELECT sm FROM StaffMember sm WHERE LOWER(sm.firstName) LIKE ?1 OR LOWER(sm.name) LIKE ?1 ORDER BY sm.name, sm.firstName"),
		@NamedQuery(name = StaffMember.QUERY_findByLoginAndPassword, query = "SELECT sm FROM StaffMember sm WHERE sm.userSetting.login = ?1 and sm.userSetting.password = ?2") })

public class StaffMember extends Contact implements ResourceEntity {

	/**
	 * Class uid
	 */
	private static final long serialVersionUID = 6766310834225135539L;

	/**
	 * The discriminator type of a staff member
	 */
	public static final String TYPE = "staff";

	/**
	 * Name of the query retrieving a staff member list matching a given role
	 * 
	 * @param 1 the role to match
	 * @return the list of staff members found
	 */
	public static final String QUERY_findByRole = "findStaffMemberByRole";

	/**
	 * Name of the query retrieving a staff member from its login
	 * 
	 * @param 1 the staff member login
	 * @return the matching staff member
	 */
	public static final String QUERY_findByLogin = "findStaffMemberByLogin";

	public static final String QUERY_findAll = "findStaffMember";

	/**
	 * Name of the query retrieving a staff member from its employee id
	 * 
	 * @param 1 the staff member employee id
	 * @return the matching staff member
	 */
	public static final String QUERY_findByEmployeeId = "findStaffMemberByEmployeeId";

	/**
	 * Name of the query that return entertainment staff that are eligible to a
	 * contract
	 * 
	 * @param 1 : The contract status
	 * @param 2 : The contract setting
	 */
	public static final String QUERY_findEligibleToContractByContractSetting = "findEligibleStaffMember";

	/**
	 * Name of the query retrieving staff members, which the first-name or name is
	 * matching the given keyword
	 * 
	 * @param 1 the given keyword in lower case
	 * @return the matching program
	 */
	public static final String QUERY_findLikeKeyword = "findStaffMembersLikeKeyword";

	public static final String QUERY_findByLoginAndPassword = "findByLoginAndPassword";

	/**
	 * The faceted resource corresponding to the staff member
	 */
	@OneToOne(mappedBy = "entity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private StaffResource resource;

	/**
	 * Skills
	 */
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "c_idstaffmember")
	private Set<StaffMemberSkill> skills;

	/**
	 * Languages
	 */
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "t_staff_language", joinColumns = @JoinColumn(name = "c_idstaffmember"))
	@AttributeOverrides({ @AttributeOverride(name = "key", column = @Column(name = "c_language")) })
	private Set<XmlEntity> languages;
	@Transient
	private Set<Locale> localeLanguages;

	/**
	 * Book
	 */
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "t_staff_book", joinColumns = @JoinColumn(name = "c_idstaffmember"), inverseJoinColumns = @JoinColumn(name = "c_iddocument"))
	private List<StaffMemberBook> book;

	/**
	 * Specialties
	 */
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "t_staff_specialty", joinColumns = @JoinColumn(name = "c_idstaffmember"))
	@AttributeOverrides({ @AttributeOverride(name = "key", column = @Column(name = "c_specialty")) })
	private Set<XmlEntity> specialties;

	/**
	 * Representative agent
	 */
	@ManyToOne
	@JoinColumn(name = "c_idagent")
	private Contact agent;

	/**
	 * Staff member personal address
	 */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "c_idaddress")
	private Address address;

	/**
	 * Identity information
	 */
	@Embedded
	private StaffMemberIdentity identity;

	/**
	 * Bank informtion
	 */
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "swift", column = @Column(name = "c_bank_swift")),
			@AttributeOverride(name = "iban", column = @Column(name = "c_bank_iban")), })
	private BankInformation bank;

	/**
	 * Supervisor
	 */
	@ManyToOne
	@JoinColumn(name = "c_idsupervisor")
	private StaffMember supervisor;

	/**
	 * Contract settings
	 */
	@OneToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "c_idcontractsetting")
	private ContractSetting contractSetting;

	/**
	 * User settings
	 */
	@Embedded
	private UserSetting userSetting;

	/**
	 * Comments
	 */
	@Column(name = "c_staff_comments")
	private String comments;

	/**
	 * Employee ID
	 */
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "c_staffid")
	private List<StaffEmployeeID> staffEmployeeId;

	@Column(name = "c_google_cal")
	private Boolean googleSynchronized;

	@Column(name = "c_emp_code")
	private String employeeCode;

	@ElementCollection
	@CollectionTable(name = "t_staff_milestone", joinColumns = @JoinColumn(name = "c_idstaff"))
	private Set<Milestone> milestones;

	@Column(name = "c_from_market_place")
	private boolean fromMarketPlace;

	@Column(name = "c_hiring_date")
	@Temporal(TemporalType.DATE)
	private Date hiringDate;

	@Column(name = "c_work_percentage")
	private byte workPercentage;

	/**
	 * L'employeur
	 */
	@OneToOne
	@JoinColumn(name = "c_labour_rule")
	private LabourRule labourRule;

	/**
	 * SignatureRecipient
	 * 
	 * @see com.teamium.domain.StaffMember
	 */
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "groupMembers")
	private Set<Group> groups = new HashSet<>();

	/**
	 * Staff member personal User Leave Record
	 */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "c_user_leave_record_id")
	private UserLeaveRecord userLeaveRecord;

	/**
	 * uilangauge
	 */
	@Column(name = "c_ui_language")
	private String uiLanguage;

	/**
	 * @return the book
	 */
	public List<StaffMemberBook> getBook() {
		if (this.book == null)
			this.book = new ArrayList<StaffMemberBook>();
		return book;
	}

	/**
	 * Add the given book document to the current staff member
	 * 
	 * @param the book document to add
	 * @return true if success, else returns false
	 */
	public boolean addToBook(StaffMemberBook doc) {
		if (doc == null)
			return false;
		else
			return this.getBook().add(doc);
	}

	/**
	 * Add the given book document to the current staff member
	 * 
	 * @param the book document to remove
	 * @return true if success, else returns false
	 */
	public boolean removeFromBook(StaffMemberBook doc) {
		return this.getBook().remove(doc);
	}

	/**
	 * @return the agent
	 */
	public Contact getAgent() {
		return agent;
	}

	/**
	 * @param agent the agent to set
	 */
	public void setAgent(Contact agent) {
		this.agent = agent;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		if (this.address == null)
			this.address = new Address();
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * @return the identity
	 */
	public StaffMemberIdentity getIdentity() {
		if (this.identity == null)
			this.identity = new StaffMemberIdentity();
		return identity;
	}

	/**
	 * @param identity the identity to set
	 */
	public void setIdentity(StaffMemberIdentity identity) {
		this.identity = identity;
	}

	/**
	 * @return the bank
	 */
	public BankInformation getBank() {
		if (bank == null)
			this.bank = new BankInformation();
		return bank;
	}

	/**
	 * @param bank the bank to set
	 */
	public void setBank(BankInformation bank) {
		this.bank = bank;
	}

	/**
	 * @return the skills
	 */
	public Set<StaffMemberSkill> getSkills() {
		if (this.skills == null)
			this.skills = new TreeSet<StaffMemberSkill>();
		return skills;
	}

	/**
	 * @param skills the skills to set
	 */
	public void setSkills(Set<StaffMemberSkill> skills) {
		this.skills = skills;
	}

	/**
	 * @return the languages
	 */
	public Set<XmlEntity> getLanguages() {
		if (this.languages == null)
			this.languages = new TreeSet<XmlEntity>();
		return this.languages;
	}

	/**
	 * @param languages the languages to set
	 */
	public void setLanguages(Set<XmlEntity> languages) {
		this.languages = languages;
		this.localeLanguages = null;
	}

	/**
	 * @return the languages
	 */
	public Set<Locale> getLocaleLanguages() {
		if (this.localeLanguages == null) {
			this.localeLanguages = new HashSet<Locale>();
			for (XmlEntity language : this.getLanguages()) {
				this.localeLanguages.add(new Locale(language.getKey()));
			}
		}
		return localeLanguages;
	}

	/**
	 * @param languages the languages to set
	 */
	public void setLocaleLanguages(Set<Locale> languages) {
		this.getLanguages().clear();
		this.localeLanguages = languages;
		for (Locale locale : this.getLocaleLanguages()) {
			XmlEntity language = new XmlEntity();
			language.setKey(locale.getLanguage());
			this.getLanguages().add(language);
		}
	}

	/**
	 * @return the supervisor
	 */
	public StaffMember getSupervisor() {
		return supervisor;
	}

	/**
	 * @param supervisor the supervisor to set
	 */
	public void setSupervisor(StaffMember supervisor) {
		if (supervisor != null && !supervisor.getUserSetting().isInRole(UserSetting.ROLE_SUPERVISOR))
			throw new IllegalArgumentException(
					"Trying to pass the non habilited " + supervisor + " as supervisor of " + this);
		this.supervisor = supervisor;
	}

	/**
	 * @return the contractSetting
	 */
	public ContractSetting getContractSetting() {
		return contractSetting;
	}

	/**
	 * @param contractSetting the contractSetting to set
	 */
	public void setContractSetting(ContractSetting contractSetting) {
		this.contractSetting = contractSetting;
	}

	/**
	 * @return the userSetting
	 */
	public UserSetting getUserSetting() {
		if (this.userSetting == null)
			this.userSetting = new UserSetting();
		return userSetting;
	}

	/**
	 * @param userSetting the userSetting to set
	 */
	public void setUserSetting(UserSetting userSetting) {
		this.userSetting = userSetting;
	}

	/**
	 * Add the given document to the staff member's book
	 * 
	 * @param the document to add
	 * @return true if success, else returns false;
	 */
	public boolean addBook(StaffMemberBook book) {
		return this.getBook().add(book);
	}

	/**
	 * Remove the given document from the staff member's book
	 * 
	 * @param the document to remove
	 * @return true if success, else returns false;
	 */
	public boolean removeBook(StaffMemberBook book) {
		return this.getBook().remove(book);
	}

	/**
	 * Returns the faceted resource
	 * 
	 * @return the faceted resource
	 */
	public StaffResource getResource() {
		if (this.resource == null) {
			this.resource = new StaffResource();
			this.resource.setStaffMember(this);
		}
		return this.resource;
	}

	/**
	 * @return the jobs
	 */
	public Set<ResourceFunction> getFunctions() {
		return this.getResource().getFunctions();
	}

	/**
	 * Return the function matching the given name
	 * 
	 * @param name the name to match
	 * @return the matching function
	 */
	public ResourceFunction getFunction(Function name) {
		return this.getResource().getFunction(name);
	}

	/**
	 * Add the given job to the current staff member
	 * 
	 * @param function the function to add
	 * @retun the assigned resource if success, else returns false
	 */
	public ResourceFunction assignFunction(Function function) {
		return this.getResource().assignFunction(function);
	}

	/**
	 * Remove the given job from the current staff member
	 * 
	 * @return job the job to remove
	 * @retun true if success, else returns false
	 */
	public boolean unassignFunction(ResourceFunction function) {
		return this.getResource().unassignFunction(function);
	}

	/**
	 * Remove the given skill to the staff member
	 * 
	 * @param skill the skill to remove
	 * @return true if success, false if it already exists
	 */
	public boolean removeSkill(StaffMemberSkill skill) {
		return this.getSkills().remove(skill);
	}

	/**
	 * @return the specialties
	 */
	public Set<XmlEntity> getSpecialties() {
		if (this.specialties == null)
			this.specialties = new TreeSet<XmlEntity>();
		return specialties;
	}

	/**
	 * @param specialties the specialties to set
	 */
	public void setSpecialties(Set<XmlEntity> specialties) {
		this.specialties = specialties;
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
	 * @return the staffEmployeeId
	 */
	public List<StaffEmployeeID> getStaffEmployeeId() {
		if (staffEmployeeId == null)
			staffEmployeeId = new ArrayList<StaffEmployeeID>();
		return staffEmployeeId;
	}

	/**
	 * @param staffEmployeeId the staffEmployeeId to set
	 */
	public void setStaffEmployeeId(List<StaffEmployeeID> staffEmployeeId) {
		this.staffEmployeeId = staffEmployeeId;
	}

	/**
	 * Return the employeeID for this saleEntity
	 * 
	 * @param s
	 * @return
	 */
	public Long getEmployeeIdForSaleEntity(SaleEntity s) {
		Long res = null;
		if (s != null) {
			for (StaffEmployeeID id : this.getStaffEmployeeId()) {
				// Added to fix the null pointer exception on null sale entity
				// for an employee
				if (id.getEntity() != null && id.getEntity().equals(s)) {
					res = id.getEmployeeId();
					break;
				}
			}
		}
		if (res == null && this.contractSetting != null) {
			res = this.getContractSetting().getEmployeeID();
		}
		return res;
	}

	public Boolean getGoogleSynchronized() {
		if (googleSynchronized == null)
			googleSynchronized = false;
		return googleSynchronized;
	}

	public void setGoogleSynchronized(Boolean googleSynchronized) {
		this.googleSynchronized = googleSynchronized;
	}

	/**
	 * Check if the user have the role given in parameter
	 */
	public boolean isUserInRole(String role) {
		Boolean isInRole = Boolean.FALSE;

		if (role != null) {
			for (String r : this.getUserSetting().getRoles()) {
				if (r.equals(role)) {
					isInRole = Boolean.TRUE;
					break;
				}
			}
		}

		return isInRole;
	}

	/**
	 * Update resource name before update
	 */
	@PrePersist
	@PreUpdate
	private void beforeUpdate() {
		if (this.getResource() != null && this.getName() != null) {
			this.getResource().setName(this.getFirstName() + " " + this.getName());
		}
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public void addGroup(Group group) {
		this.groups.add(group);
		group.getGroupMembers().add(this);
	}

	public void removeGroup(Group group) {
		this.groups.remove(group);
		group.getGroupMembers().remove(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		return result;
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
	 * @return the fromMarketPlace
	 */
	public boolean isFromMarketPlace() {
		return fromMarketPlace;
	}

	/**
	 * @param fromMarketPlace the fromMarketPlace to set
	 */
	public void setFromMarketPlace(boolean fromMarketPlace) {
		this.fromMarketPlace = fromMarketPlace;
	}

	/**
	 * @return the labourRule
	 */
	public LabourRule getLabourRule() {
		return labourRule;
	}

	/**
	 * @param labourRule the labourRule to set
	 */
	public void setLabourRule(LabourRule labourRule) {
		this.labourRule = labourRule;
	}

	/**
	 * @return the groups
	 */
	public Set<Group> getGroups() {
		return groups;
	}

	/**
	 * @return the hiringDate
	 */
	public Date getHiringDate() {
		return hiringDate;
	}

	/**
	 * @param hiringDate the hiringDate to set
	 */
	public void setHiringDate(Date hiringDate) {
		this.hiringDate = hiringDate;
	}

	/**
	 * @return the workPercentage
	 */
	public byte getWorkPercentage() {
		return workPercentage;
	}

	/**
	 * @param workPercentage the workPercentage to set
	 */
	public void setWorkPercentage(byte workPercentage) {
		this.workPercentage = workPercentage;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	/**
	 * @return the userLeaveRecord
	 */
	public UserLeaveRecord getUserLeaveRecord() {
		if (userLeaveRecord == null) {
			userLeaveRecord = new UserLeaveRecord();
		}
		return userLeaveRecord;
	}

	/**
	 * @param userLeaveRecord the userLeaveRecord to set
	 */
	public void setUserLeaveRecord(UserLeaveRecord userLeaveRecord) {
		this.userLeaveRecord = userLeaveRecord;
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
		StaffMember other = (StaffMember) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StaffMember [resource=" + resource + ", skills=" + skills + ", languages=" + languages
				+ ", localeLanguages=" + localeLanguages + ", book=" + book + ", specialties=" + specialties
				+ ", agent=" + agent + ", address=" + address + ", identity=" + identity + ", bank=" + bank
				+ ", supervisor=" + supervisor + ", contractSetting=" + contractSetting + ", userSetting=" + userSetting
				+ ", comments=" + comments + ", staffEmployeeId=" + staffEmployeeId + ", googleSynchronized="
				+ googleSynchronized + ", employeeCode=" + employeeCode + ", milestones=" + milestones
				+ ", fromMarketPlace=" + fromMarketPlace + ", labourRule=" + labourRule + "]";
	}

}
