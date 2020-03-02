/**
 * 
 */
package com.teamium.domain.prod.resources.staff.contract;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.output.XmlOutputEntity;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.resources.SaleEntity;
import com.teamium.domain.prod.resources.contacts.Contact;
import com.teamium.domain.prod.resources.functions.Function;
import com.teamium.domain.prod.resources.functions.units.RateUnit;
import com.teamium.domain.prod.resources.staff.StaffMember;

/**
 * Describe a contract
 * 
 * @author sraybaud - NovaRem
 *
 */
@Entity
@Table(name = "t_contract")
@NamedQueries(value = {
		@NamedQuery(name = Contract.QUERY_findByIds, query = "SELECT c FROM Contract c WHERE c.id IN (?1) ORDER BY c.id"),
		@NamedQuery(name = Contract.QUERY_findAll, query = "SELECT c FROM Contract c ORDER BY c.id"),
		@NamedQuery(name = Contract.QUERY_countAll, query = "SELECT COUNT(c) FROM Contract c"),
		@NamedQuery(name = Contract.QUERY_findUnprintedContractsForStaffMemberForFunction, query = "SELECT c FROM Contract c WHERE c.staffMember = ?1 AND c.printedDate IS NULL AND c.function = ?2 AND c.unit = ?3"),
		@NamedQuery(name = Contract.QUERY_findByBooking, query = "SELECT c FROM Contract c WHERE EXISTS (SELECT cl FROM ContractLine cl WHERE cl.contract.id = c.id AND ?1 MEMBER OF cl.bookings)"),
		@NamedQuery(name = Contract.QUERY_findByStaffMember, query = "SELECT c FROM Contract c WHERE c.staffMember = ?1"),
		@NamedQuery(name = Contract.QUERY_findContractWithoutDue, query = "SELECT c FROM Contract c WHERE (c.dueSent = false OR c.dueSent IS NULL) AND c.signDate IS NOT NULL") })
public class Contract extends AbstractEntity implements XmlOutputEntity {

	/**
	 * The entity alias used in queries
	 */
	public static final String QUERY_ENTITY_ALIAS = "c";

	/**
	 *  
	 */
	public static final String QUERY_findContractWithoutDue = "findContractWithoutDue";

	/**
	 * Query for returning contracts for a staffMember
	 * 
	 * @param 1
	 *            the staffMember
	 */
	public static final String QUERY_findByStaffMember = "findContractForStaffMember";

	/**
	 * Name of the query retrieving contracts of a given staff member, for a given
	 * function with a given unit
	 * 
	 * @param 1
	 *            the staffMember
	 * @param 2
	 *            the staff member function
	 * @param 3
	 *            the unit
	 * @return the list of matching contracts
	 */
	public static final String QUERY_findUnprintedContractsForStaffMemberForFunction = "findContractForStaffMemberForFunction";

	/**
	 * Name of the query retrieving the contracts matching the given list of ids
	 * 
	 * @param 1
	 *            the ids to search
	 * @return the list of matching contracts
	 */
	public static final String QUERY_findByIds = "findContractByIds";

	/**
	 * Query string to retrieve the contracts matching the given list of ids, that
	 * may be ordered by OrderByClause.getClause()
	 * 
	 * @param 1
	 *            the ids to search
	 * @return the list of matching contracts
	 */
	public static final String QUERY_findOrderedByIds = "SELECT c.id FROM Contract c WHERE c.id IN (?1)";

	/**
	 * Name of the query retrieving all the contracts persisted in the persistence
	 * unit
	 * 
	 * @return the list of all contracts
	 */
	public static final String QUERY_findAll = "findAllContract";

	/**
	 * Query string for retrieving all contracts, that may be ordered by
	 * OrderByClause.getClause()
	 * 
	 * @return the list of all contracts
	 */
	public static final String QUERY_findAllOrdered = "SELECT c FROM Contract c";

	/**
	 * Name of the query counting all contracts in the persistence unit
	 * 
	 * @return the total count of contracts
	 */
	public static final String QUERY_countAll = "countAllContract";

	/**
	 * Query for finding a contract line by a booking 1 : the researched booking
	 */
	public static final String QUERY_findByBooking = "findContractLineByBooking";

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -3747242423645322510L;

	/**
	 * ID de l'entité
	 */
	@Id
	@Column(name = "c_idcontract", insertable = false, updatable = false)
	@TableGenerator(name = "idContract_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "contract_idcontract_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idContract_seq")
	private Long id;

	/**
	 * L'employeur
	 */
	@ManyToOne(cascade = { CascadeType.MERGE })
	@JoinColumn(name = "c_idsaleentity")
	private SaleEntity employer;

	/**
	 * Le staff member concerné par le contrat
	 */
	@ManyToOne
	@JoinColumn(name = "c_idstaffmember")
	private StaffMember staffMember;

	/**
	 * La fonction concernée par le contrat
	 */
	@ManyToOne
	@JoinColumn(name = "c_idfunction")
	private Function function;

	/**
	 * L'unité du contrat
	 */
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "key", column = @Column(name = "c_unit")),
			@AttributeOverride(name = "calendarConstant", column = @Column(name = "c_unit_calendarconstant")), })
	private RateUnit unit;

	/**
	 * Le salaire par unité du contrat
	 */
	@Column(name = "c_rate")
	private Float rate;

	/**
	 * La date d'embauche
	 */
	@Column(name = "c_hiredate")
	private Calendar hireDate;

	/**
	 * La date de fin du contrat
	 */
	@Column(name = "c_enddate")
	private Calendar endDate;

	/**
	 * Total du contrat
	 */
	@Column(name = "c_contracttotal")
	private Float contractTotal;

	/**
	 * The saleEntity representative
	 */
	@ManyToOne
	@JoinColumn(name = "c_sale_representative")
	private Contact saleRepresentative;

	/**
	 * @param contractTotal
	 *            the contractTotal to set
	 */
	public void setContractTotal(Float contractTotal) {
		this.contractTotal = contractTotal;
	}

	/**
	 * La convention collective ratachée au contrat
	 */
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "key", column = @Column(name = "c_convention_key")),
			@AttributeOverride(name = "threshold", column = @Column(name = "c_convention_threshold")),
			@AttributeOverride(name = "majoration", column = @Column(name = "c_convention_majoration")),
			@AttributeOverride(name = "interpige", column = @Column(name = "c_convention_interpige")),
			@AttributeOverride(name = "nightMajoration", column = @Column(name = "c_convention_nightmajoration")),
			@AttributeOverride(name = "stringNightRanges", column = @Column(name = "c_convention_nightranges")),
			@AttributeOverride(name = "chargePercent", column = @Column(name = "c_convention_chargepercent")),
			@AttributeOverride(name = "stringHolidaysRanges", column = @Column(name = "c_convention_holidays")),
			@AttributeOverride(name = "sundayMajoration", column = @Column(name = "c_convention_sundaymajoration")) })
	private SocialConvention convention;

	/**
	 * L'edition liée au contrat
	 */
	@Embedded
	@AttributeOverride(name = "key", column = @Column(name = "c_contract_type"))
	private ContractEdition contractEdition;

	/**
	 * Les lignes du contrat
	 */
	@OneToMany(mappedBy = "contract", cascade = CascadeType.ALL)
	private List<ContractLine> lines;

	/**
	 * La date d'impression du contrat
	 */
	@Column(name = "c_printeddate")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar printedDate;

	/**
	 * La date de signature du contrat
	 */
	@Column(name = "c_signdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar signDate;

	/**
	 * The break hour
	 */
	@Column(name = "c_breakhour_start")
	@Temporal(TemporalType.TIME)
	private Date breakHourStart;

	/**
	 * The break hour
	 */
	@Column(name = "c_breakhour_end")
	@Temporal(TemporalType.TIME)
	private Date breakHourEnd;

	/**
	 * The description
	 */
	@Column(name = "c_description")
	private String description;

	/**
	 * N° ASSEDIC
	 */
	@Column(name = "c_numassedic")
	private String numAssedic;

	/**
	 * Coefficient
	 */
	@Column(name = "c_coefficiant")
	private Integer coefficiant;

	/**
	 * Operation
	 */
	@Column(name = "c_operation")
	private String operation;

	/**
	 * Locale for editions
	 */
	@Transient
	private Locale locale;

	/**
	 * The staff category
	 */
	@Embedded
	@AttributeOverride(name = "key", column = @Column(name = "c_staffcategory"))
	private com.teamium.domain.XmlEntity staffCategory;

	/**
	 * The linked record
	 */
	@ManyToOne
	@JoinColumn(name = "c_linkedrecord")
	private Record linkedRecord;

	/**
	 * Exported to DUE
	 */
	@Column(name = "c_exported")
	private Boolean dueSent;

	/**
	 * Return the ID
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * Set the ID
	 */
	@Override
	public void setId(Long id) {
		this.id = id;

	}

	/**
	 * Return true if the contract is a draft
	 */
	public Boolean isDraft() {
		return this.getStaffMember() == null || this.getUnit() == null || this.getFunction() == null
				|| this.getConvention() == null;
	}

	/**
	 * @return the employer
	 */
	public SaleEntity getEmployer() {
		return employer;
	}

	/**
	 * @param employer
	 *            the employer to set
	 */
	public void setEmployer(SaleEntity employer) {
		this.employer = employer;
	}

	/**
	 * @return the staffMember
	 */
	public StaffMember getStaffMember() {
		return staffMember;
	}

	/**
	 * @param staffMember
	 *            the staffMember to set
	 */
	public void setStaffMember(StaffMember staffMember) {
		this.staffMember = staffMember;
	}

	/**
	 * @return the function
	 */
	public Function getFunction() {
		return function;
	}

	/**
	 * @param function
	 *            the function to set
	 */
	public void setFunction(Function function) {
		this.function = function;
	}

	public com.teamium.domain.XmlEntity getStaffCategory() {
		return staffCategory;
	}

	public void setStaffCategory(com.teamium.domain.XmlEntity staffCategory) {
		this.staffCategory = staffCategory;
	}

	/**
	 * @return the unit
	 */
	public RateUnit getUnit() {
		return unit;
	}

	/**
	 * @param unit
	 *            the unit to set
	 */
	public void setUnit(RateUnit unit) {
		this.unit = unit;
	}

	public Contact getSaleRepresentative() {
		return saleRepresentative;
	}

	public void setSaleRepresentative(Contact saleRepresentative) {
		this.saleRepresentative = saleRepresentative;
	}

	/**
	 * @return the rate
	 */
	public Float getRate() {
		if (rate == null)
			rate = 0F;
		return rate;
	}

	/**
	 * @param rate
	 *            the rate to set
	 */
	public void setRate(Float rate) {
		this.rate = rate;
	}

	/**
	 * @return the dueSent
	 */
	public Boolean isDueSent() {
		if (dueSent == null)
			dueSent = false;
		return dueSent;
	}

	/**
	 * @param dueSent
	 *            the dueSent to set
	 */
	public void setDueSent(boolean dueSent) {
		this.dueSent = dueSent;
	}

	/**
	 * @return the hireDate
	 */
	public Calendar getHireDate() {

		Calendar dateToSet = null;

		if (hireDate == null) {
			List<Calendar> list = new ArrayList<Calendar>();

			for (ContractLine l : lines)
				list.add(l.getStartPeriod());

			if (!list.isEmpty())
				dateToSet = list.get(0);

			for (Calendar c : list)
				dateToSet = c.before(dateToSet) ? c : dateToSet;
		}
		if (dateToSet != null) {
			hireDate = Calendar.getInstance();
			hireDate.setTime(dateToSet.getTime());
		}
		return hireDate;
	}

	/**
	 * @param hireDate
	 *            the hireDate to set
	 */
	public void setHireDate(Calendar hireDate) {

		this.hireDate = hireDate;
	}

	/**
	 * @return the endDate
	 */
	public Calendar getEndDate() {

		Calendar dateToSet = null;

		if (endDate == null) {
			List<Calendar> list = new ArrayList<Calendar>();

			for (ContractLine l : lines)
				list.add(l.getEndPeriod());

			if (!list.isEmpty())
				dateToSet = list.get(0);

			for (Calendar c : list)
				dateToSet = c.after(dateToSet) ? c : dateToSet;
		}
		if (dateToSet != null) {
			endDate = Calendar.getInstance();
			endDate.setTime(dateToSet.getTime());
		}
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the convention
	 */
	public SocialConvention getConvention() {
		return convention;
	}

	/**
	 * @param convention
	 *            the convention to set
	 * @throws CloneNotSupportedException
	 */
	public void setConvention(SocialConvention convention) throws CloneNotSupportedException {
		if (convention != null)
			convention = convention.clone();
		this.convention = convention;
	}

	/**
	 * @return the contractEdition
	 */
	public ContractEdition getContractEdition() {
		return contractEdition;
	}

	/**
	 * @param contractEdition
	 *            the contractEdition to set
	 */
	public void setContractEdition(ContractEdition contractEdition) {
		this.contractEdition = contractEdition;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNumAssedic() {
		if ((numAssedic == null || numAssedic.isEmpty()) && linkedRecord != null)
			numAssedic = linkedRecord.getNumObjet();
		return numAssedic;
	}

	public void setNumAssedic(String numAssedic) {
		this.numAssedic = numAssedic;
	}

	public Integer getCoefficiant() {
		return coefficiant;
	}

	public void setCoefficiant(Integer coefficiant) {
		this.coefficiant = coefficiant;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * @return the lines
	 */
	public List<ContractLine> getLines() {
		if (this.lines == null)
			this.lines = new ArrayList<ContractLine>();
		return this.lines;
	}

	/**
	 * Add the given line to the current contract
	 * 
	 * @param line
	 *            the line to add
	 * @return true if success, else returns false
	 */
	public boolean addLine(ContractLine line) {
		if (line == null)
			return false;
		else {
			Contract former = line.getContract();
			if (this.getLines().contains(line))
				return false;
			line.setContract(this);
			if (this.getLines().add(line)) {
				return true;
			} else {
				line.setContract(former);
				return false;
			}
		}
	}

	/**
	 * Remove the given line from the contract
	 * 
	 * @param the
	 *            line to remove
	 */
	public boolean removeLine(ContractLine line) {
		return this.getLines().remove(line);
	}

	/**
	 * @return the printedDate
	 */
	public Calendar getPrintedDate() {
		return printedDate;
	}

	/**
	 * @param printedDate
	 *            the printedDate to set
	 */
	public void setPrintedDate(Calendar printedDate) {
		this.printedDate = printedDate;
	}

	/**
	 * @return the signDate
	 */
	public Calendar getSignDate() {
		return signDate;
	}

	/**
	 * @param signDate
	 *            the signDate to set
	 */
	public void setSignDate(Calendar signDate) {
		this.signDate = signDate;
	}

	/**
	 * @return the breakHourStart
	 */
	public Date getBreakHourStart() {
		return breakHourStart;
	}

	/**
	 * @param breakHourStart
	 *            the breakHourStart to set
	 */
	public void setBreakHourStart(Date breakHourStart) {
		this.breakHourStart = breakHourStart;
	}

	/**
	 * @return the breakHourEnd
	 */
	public Date getBreakHourEnd() {
		return breakHourEnd;
	}

	/**
	 * @param breakHourEnd
	 *            the breakHourEnd to set
	 */
	public void setBreakHourEnd(Date breakHourEnd) {
		this.breakHourEnd = breakHourEnd;
	}

	/**
	 * @return the linkedRecord
	 */
	public Record getLinkedRecord() {
		return linkedRecord;
	}

	/**
	 * @param linkedRecord
	 *            the linkedRecord to set
	 */
	public void setLinkedRecord(Record linkedRecord) {
		this.linkedRecord = linkedRecord;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * Recalculate all the attributes
	 */
	public void calculateFull() {
		for (ContractLine l : lines)
			l.calculate();
		this.calculatePartial();

	}

	/**
	 * Recalculate only the values for the contract
	 */
	public void calculatePartial() {
		endDate = null;
		hireDate = null;
		this.getEndDate();
		this.getHireDate();
	}

	/**
	 * Return the total remuneration for the contract
	 * 
	 * @return
	 */
	public Float getContractTotal() {
		if (contractTotal == null)
			contractTotal = new Float(0F);
		contractTotal = 0F;
		for (ContractLine l : lines)
			contractTotal += l.getLineCost();
		return contractTotal;
	}

	/**
	 * Get total extra cost
	 * 
	 * @return Float
	 */
	public Float getContractExtra() {
		Float total = 0F;
		for (ContractLine l : lines)
			total += l.getExtraCost();
		return total;
	}

	/**
	 * get total of dueQuantity
	 * 
	 * @return Integer
	 */
	public Integer getTotalDueQuantity() {
		Integer total = 0;
		for (ContractLine l : lines)
			total += l.getDueQuantity();
		return total;
	}

	/**
	 * get total of EffectiveQuantity
	 * 
	 * @return Integer
	 */
	public Integer getTotalEffectiveQuantity() {
		Integer total = 0;
		for (ContractLine l : lines)
			total += l.getEffectiveQuantity();
		return total;
	}

	/**
	 * get total of extraQuantity
	 * 
	 * @return Integer
	 */
	public Integer getTotalExtraQuantity() {
		Integer total = 0;
		for (ContractLine l : lines)
			total += l.getExtraQuantity();
		return total;
	}

	/**
	 * get total of nightQuantity
	 * 
	 * @return Integer
	 */
	public Integer getTotalNightQuantity() {
		Integer total = 0;
		for (ContractLine l : lines)
			total += l.getNightQuantity();
		return total;
	}

	/**
	 * recalculate a line only
	 * 
	 * @param line
	 *            - The line to re-calculate
	 */
	public void calculateLine(ContractLine line) {
		line.calculate();
		this.calculatePartial();
	}

	/**
	 * Set the contract to validated status
	 */
	public boolean validate() {

		boolean valid = true;

		for (ContractLine cl : lines) {
			for (Booking b : cl.getBookings()) {
				b.setContractStatus(ContractStatus.PRINTED);
				if (b.getFrom() == null) {
					valid = false;
					break;
				}
				if (b.getTo() == null) {
					valid = false;
					break;
				}
			}
			if (!valid)
				break;
		}
		if (valid) {
			this.setPrintedDate(Calendar.getInstance());
			return true;
		} else {
			for (ContractLine cl : lines) {
				for (Booking b : cl.getBookings()) {
					b.setContractStatus(ContractStatus.MADE);
				}
			}
			return false;
		}
	}

	/**
	 * Fill the real date in the lines for the contract
	 */
	public void fillRealDate() {
		for (ContractLine cl : lines) {
			boolean modified = false;
			for (Booking b : cl.getBookings()) {
				if (b.getFrom() == null) {
					b.setFrom(b.getFrom());
					modified = true;
				}
				if (b.getTo() == null) {
					b.setTo(b.getTo());
					modified = true;
				}
			}
			if (modified)
				this.calculateLine(cl);
		}
	}

	/**
	 * Set the contract to unvalidated status
	 */
	public void unvalidate() {
		for (ContractLine cl : lines) {
			for (Booking b : cl.getBookings()) {
				b.setContractStatus(ContractStatus.MADE);
			}

		}
		this.setPrintedDate(null);
	}

	/**
	 * Set the contract to print status
	 */
	public void sign(Calendar date) {
		this.setSignDate(date);
		for (ContractLine cl : lines) {
			for (Booking b : cl.getBookings())
				b.setContractStatus(ContractStatus.SIGNED);
		}
	}

	/**
	 * Set the contract back to print status
	 */
	public void unsign() {
		for (ContractLine cl : lines) {
			for (Booking b : cl.getBookings()) {
				b.setContractStatus(ContractStatus.PRINTED);
			}
		}
		this.setSignDate(null);
	}

	/**
	 * Returns the output header entity
	 * 
	 * @return the sale entity
	 */
	@Override
	public SaleEntity getOutputHeaderEntity() {
		return this.employer;
	}

	/**
	 * Returns the output localization
	 * 
	 * @returns the locale
	 */
	@Override
	public Locale getOutputLocale() {
		return null;
	}

	/**
	 * Returns the edition group of the current xml output entity
	 * 
	 * @author sraybaud
	 * @return the edition group (can be null)
	 */
	@Override
	public String getOutputPath() {
		String path = this.getClass().getSimpleName();
		return path;
	}

	/**
	 * Method called before remove
	 */
	@PreRemove
	public void preRemove() {
		for (ContractLine cl : lines) {
			for (Booking b : cl.getBookings())
				b.setContractStatus(ContractStatus.NOT_MADE);
		}
	}

	/**
	 * Remove a booking from a cotract line
	 * 
	 * @param b
	 *            - the booking
	 * @param cl
	 *            - the contract Line
	 */
	public void removeBookingFromLine(Booking b, ContractLine cl) {
		cl.removeBooking(b);
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
		result = prime * result + ((function == null) ? 0 : function.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
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
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contract other = (Contract) obj;
		if (function == null) {
			if (other.function != null)
				return false;
		} else if (!function.equals(other.function))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		return true;
	}

}
