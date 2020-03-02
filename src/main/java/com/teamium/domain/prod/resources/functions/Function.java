/**
 * 
 */
package com.teamium.domain.prod.resources.functions;

import java.util.Comparator;
import java.util.Currency;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.AbstractXmlEntity;
import com.teamium.domain.FunctionKeyword;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.Theme;
import com.teamium.domain.Vat;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.resources.accountancy.AccountancyEntity;
import com.teamium.domain.prod.resources.accountancy.AccountancyNumber;
import com.teamium.domain.prod.resources.functions.units.RateUnit;

/**
 * Describe an sorted function classification
 * 
 * @author sraybaud - NovaRem
 *
 */
@Entity
@Table(name = "t_function")
@DiscriminatorColumn(name = "c_discriminator", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("function")
@NamedQueries({
		@NamedQuery(name = Function.QUERY_findAll, query = "SELECT n FROM Function n WHERE n.archived != TRUE ORDER BY n.value ASC"),
		@NamedQuery(name = Function.QUERY_findRootNames, query = "SELECT n FROM Function n WHERE n.parent IS NULL AND n.archived != TRUE ORDER BY n.value ASC"),
		@NamedQuery(name = Function.QUERY_findNamesByParent, query = "SELECT n FROM Function n WHERE n.parent = ?1 AND n.archived != TRUE ORDER BY n.value ASC"),
		@NamedQuery(name = Function.QUERY_findNamesByParents, query = "SELECT n FROM Function n WHERE n.parent IN (?1) AND n.archived != TRUE ORDER BY n.value ASC"),
		@NamedQuery(name = Function.QUERY_findNamesLikeValue, query = "SELECT n FROM Function n WHERE LOWER(n.value) LIKE ?1 AND n.archived != TRUE ORDER BY n.value ASC"),
		@NamedQuery(name = Function.QUERY_findGroups, query = "SELECT DISTINCT f.group FROM Function f WHERE f.group IS NOT NULL AND f.archived != TRUE ORDER BY f.group"),
		@NamedQuery(name = Function.QUERY_findByGroup, query = "SELECT f FROM Function f WHERE f.group = ?1 AND f.archived != TRUE"),
		@NamedQuery(name = Function.QUERY_findByResource, query = "SELECT f FROM Function f WHERE f.archived != TRUE AND EXISTS (SELECT r FROM Resource r, IN(r.functions) ff WHERE r = ?1 AND ff.function = f)") })
public class Function extends AbstractEntity implements Comparable<Function>, AccountancyEntity {
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -3424269981338754077L;

	/**
	 * Name of the query returning all root names (excepting SupplyFunctionName)
	 * 
	 * @return the list of function's names
	 */
	public static final String QUERY_findRootNames = "findRootName";

	public static final String QUERY_findAll = "findAllFunction";

	/**
	 * Name of the query returning child names of a given parent name
	 * 
	 * @param 1
	 *            name the parent function name
	 * @return the children function names
	 */
	public static final String QUERY_findNamesByParent = "findFunctionNameByParent";

	/**
	 * Name of the query returning children names of a given list of parent names
	 * 
	 * @param 1
	 *            names the list of parent names
	 * @return the children function names
	 */
	public static final String QUERY_findNamesByParents = "findFunctionNameByParents";

	/**
	 * Name of the query returning function name matching a given lower case keyword
	 * 
	 * @param 1
	 *            given lower case keyword to match
	 * @return the list of matching function names
	 */
	public static final String QUERY_findNamesLikeValue = "findFunctionNameLikeValue";

	/**
	 * Name of the query returning function groups
	 * 
	 * @return the list of matching groups
	 */
	public static final String QUERY_findGroups = "findFunctionGroups";

	/**
	 * Name of the query returning functions matching a given group
	 * 
	 * @param 1
	 *            the group name to match
	 * @return the list of matching functions
	 */
	public static final String QUERY_findByGroup = "findFunctionByGroup";

	/**
	 * Name of the query returning functions available for the given resource
	 * 
	 * @param 1
	 *            the resource to match
	 * @return the list of matching functions
	 */
	public static final String QUERY_findByResource = "findFunctionByResource";

	/**
	 * Function ID
	 */
	@Id
	@Column(name = "c_idfunction", insertable = false, updatable = false)
	@TableGenerator(name = "idFunction_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "function_idfunction_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idFunction_seq")
	private Long id;

	/**
	 * Function position
	 */
	@Column(name = "c_pos")
	private Integer position;

	/**
	 * Function name
	 */
	@Column(name = "c_name")
	private String value;

	/**
	 * Rate currency
	 */
	@Column(name = "c_currency")
	private String defaultCurrency;

	/**
	 * Function qualified name
	 */
	@Column(name = "c_qualifiedname")
	private String qualifiedName;

	/**
	 * UI Theme
	 */
	@Embedded
	@AttributeOverride(name = "key", column = @Column(name = "c_theme"))
	private Theme theme;

	/**
	 * Display number
	 */
	@Column(name = "c_displaynumber")
	private Integer displayNumber;

	/**
	 * The contract edition for this function
	 */

	@Column(name = "c_contractedition")
	private String contractEdition;

	/**
	 * The default assignation
	 */

	@Column(name = "c_assignation")
	private String defaultAssignation;

	/**
	 * Parent level
	 */
	@ManyToOne
	@JoinColumn(name = "c_idparent")
	private Function parent;
	
	/**
	 * Function description
	 */
	@Column(name = "c_description")
	private String description;

	/**
	 * Assigned fees
	 * 
	 * @see com.teamium.domain.prod.project.ProjectFee
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "t_function_fee", joinColumns = @JoinColumn(name = "c_idfunction"))
	@AttributeOverrides({ @AttributeOverride(name = "key", column = @Column(name = "c_fee")) })
	private Set<XmlEntity> appliedFees;

	/**
	 * The function's group for planning filtering
	 */
	@Column(name = "c_functiongroup")
	private String group;

	/**
	 * The VAT rate to apply
	 */
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "key", column = @Column(name = "c_vat_code")),
			@AttributeOverride(name = "rate", column = @Column(name = "c_vat_rate")) })
	private Vat vat;

	/**
	 * The keyword used to generate informations
	 */
	@Column(name = "c_keyword")
	private String keyword;

	/**
	 * The accountancy numbers
	 */
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "t_function_accountancy_number", joinColumns = {
			@JoinColumn(name = "c_function") }, inverseJoinColumns = { @JoinColumn(name = "c_accountancy_number") })
	private Set<AccountancyNumber> accountancyNumbers;

	/**
	 * Default Unit
	 */
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "key", column = @Column(name = "c_defaultunit")),
			@AttributeOverride(name = "calendarConstant", column = @Column(name = "c_defaultunit_calendarconstant")) })
	private RateUnit defaultUnit;

	/**
	 * Flag for an archived entity.
	 */
	@Column(name = "c_archived")
	private Boolean archived = Boolean.FALSE;

	@Transient
	private List<Function> childFunctions;

	@ManyToOne
	@JoinColumn(name = "c_idfunctionkeyword")
	private FunctionKeyword functionKeyword;
	
	/**
	 * True if current function is folder
	 */
	@Column(name="c_folder")
	private boolean folder=false;

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
	 * @return the number
	 */
	public Integer getPosition() {
		if (this.position == null) {
			if (parent == null)
				this.setPosition(0);
			else
				this.setPosition(this.parent.getPosition() + 1);
		}
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
	 *            the name to set
	 */
	public void setValue(String value) {
		this.value = value;
		this.beforeUpdate();
	}

	/**
	 * @return the defaultUnit
	 */
	public RateUnit getDefaultUnit() {
		return defaultUnit;
	}

	/**
	 * @param defaultUnit
	 *            the defaultUnit to set
	 */
	public void setDefaultUnit(RateUnit defaultUnit) {
		this.defaultUnit = defaultUnit;
	}

	/**
	 * @return the parent
	 */
	public Function getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(Function parent) {
		this.parent = parent;
		if (this.parent == null)
			this.setPosition(0);
		else
			this.setPosition(this.parent.getPosition() + 1);
		this.beforeUpdate();
	}

	/**
	 * @return the Theme
	 */
	public Theme getTheme() {
		if (this.theme == null && this.parent != null) {
			return this.parent.getTheme();
		} else {
			return theme;
		}
	}

	/**
	 * @param Theme
	 *            the Theme to set
	 */
	public void setTheme(Theme theme) {
		this.theme = theme;
	}

	/**
	 * @return the displayNumber
	 */
	public Integer getDisplayNumber() {
		if (displayNumber == null && this.getParent() != null)
			return this.getParent().getDisplayNumber();
		if (displayNumber == null)
			displayNumber = 0;
		return displayNumber;
	}

	/**
	 * @param displayNumber
	 *            the displayNumber to set
	 */
	public void setDisplayNumber(Integer displayNumber) {
		this.displayNumber = displayNumber;
	}

	/**
	 * Get the first available theme in the tree, if there are no available theme,
	 * return new Theme
	 * 
	 * @return Theme
	 */
	public Theme getAvailableTheme() {
		if (this.position.equals(new Integer(0))) {
			return (this.theme == null) ? new Theme() : theme; // TODO Renvoyer
																// un theme par
																// défaut au
																// lieu d'un new
																// thème
		} else
			return this.theme == null ? parent.getAvailableTheme() : theme;
	}

	/**
	 * Returns fees to apply, including parents fees
	 * 
	 * @return the fees to apply for the given function
	 */
	public Set<XmlEntity> getAppliedFees() {
		Set<XmlEntity> fees = new HashSet<XmlEntity>();
		if (this.appliedFees == null) {
			this.appliedFees = new HashSet<XmlEntity>();
		}
		fees.addAll(this.appliedFees);
		if (this.parent != null)
			fees.addAll(this.parent.getAppliedFees());
		return fees;
	}

	/**
	 * Set the fees to apply to the current function NB : Each fee already assigned
	 * to parent function is removed from current set of fees to apply
	 * 
	 * @param fees
	 *            the fees to apply
	 */
	public void setAppliedFees(Set<XmlEntity> fees) {
		if (this.appliedFees == null)
			this.appliedFees = new HashSet<XmlEntity>();
		this.appliedFees.clear();
		if (fees != null) {
			for (XmlEntity fee : fees) {
				if (this.parent != null) {
					if (this.parent.getAppliedFees().contains(fee)) {
						this.appliedFees.remove(fee);
					} else {
						this.appliedFees.add(fee);
					}
				} else {
					this.appliedFees.add(fee);
				}
			}
		}
	}

	/**
	 * @return the defautlAssignation
	 */
	public String getDefaultAssignation() {
		if (this.defaultAssignation == null && this.getParent() != null)
			return this.getParent().getDefaultAssignation();
		return defaultAssignation;
	}

	/**
	 * @param defautlAssignation
	 *            the defautlAssignation to set
	 */
	public void setDefaultAssignation(String defautlAssignation) {
		this.defaultAssignation = defautlAssignation;
	}

	/**
	 * Returns the function's group.
	 * 
	 * @return group
	 */
	public String getGroup() {
		if (this.group == null && parent != null) {
			return parent.getGroup();
		} else {
			return this.group;
		}
	}

	/**
	 * Set the group for the given function
	 * 
	 * @param group
	 *            the group to set
	 */
	public void setGroup(String group) {
		if (group != null && parent != null && group.equals(parent.getGroup())) {
			this.group = null;
		} else {
			this.group = group;
		}
	}

	/**
	 * Returns the VAT rate to apply for the current function
	 * 
	 * @return the VAT rate
	 */
	public Vat getVat() {
		return this.vat;
	}

	/**
	 * Sets the VAT rate to apply
	 * 
	 * @param vat
	 *            the VAT rate to set
	 */
	public void setVat(Vat vat) {
		this.vat = vat;
	}

	/**
	 * @return the defaultCurrency
	 */
	public Currency getDefaultCurrency() {
		if (defaultCurrency == null && this.getParent() != null)
			return this.getParent().getDefaultCurrency();
		if (defaultCurrency == null)
			return null;
		return Currency.getInstance(defaultCurrency);
	}

	/**
	 * @param defaultCurrency
	 *            the defaultCurrency to set
	 */
	public void setDefaultCurrency(Currency defaultCurrency) {
		this.defaultCurrency = defaultCurrency.getCurrencyCode();
	}

	/**
	 * @param defaultCurrency
	 *            the defaultCurrency to set
	 */
	public void setDefaultCurrency(String defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}

	/**
	 * @return the contractEdition
	 */
	public String getContractEdition() {
		if (contractEdition == null && this.getParent() != null)
			return this.getParent().getContractEdition();
		return contractEdition;
	}

	/**
	 * @param contractEdition
	 *            the contractEdition to set
	 */
	public void setContractEdition(String contractEdition) {
		this.contractEdition = contractEdition;
	}

	/**
	 * Return the hashcode of the object
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		return result;
	}

	/**
	 * Return the fully qualified name of a function
	 */
	public String getQualifiedName() {
		if (this.qualifiedName == null) {
			this.beforeUpdate();
		}
		return this.qualifiedName;
	}

	/**
	 * Do nothing
	 */
	public void setQualifiedName(String qualifiedName) {
	}

	/**
	 * Return the root function
	 */
	public Function getRootFunction() {
		if (this.getParent() == null)
			return this;
		else
			return this.getParent().getRootFunction();

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
	 * The keyword of the current instance and its parents
	 * 
	 * @return
	 */
	public Set<String> getKeywords() {
		Set<String> keywords = new TreeSet<String>();
		Function function = this;
		while (function != null && function.getKeyword() != null) {
			keywords.add(function.getKeyword());
			function = function.getParent();
		}
		return keywords;
	}

	/**
	 * @return the accountancyNumbers
	 */
	public List<AccountancyNumber> getAccountancyNumbers() {
		if (accountancyNumbers == null) {
			accountancyNumbers = new HashSet<AccountancyNumber>();
		}
		return new ArrayList<AccountancyNumber>(accountancyNumbers);
	}

	/**
	 * Return all accountancy number
	 */
	public List<AccountancyNumber> getAllAccountancyNumbers() {
		List<AccountancyNumber> accountancyNumbers = new ArrayList<AccountancyNumber>();
		List<XmlEntity> types = new ArrayList<XmlEntity>();

		for (AccountancyNumber an : this.getAccountancyNumbers()) {
			types.add(an.getType());
			accountancyNumbers.add(an);
		}

		Function parent = this.getParent();
		while (parent != null) {
			for (AccountancyNumber an : parent.getAccountancyNumbers()) {
				if (an.getType() != null && !types.contains(an.getType())) {
					accountancyNumbers.add(an);
				}
			}
			parent = parent.getParent();
		}

		return accountancyNumbers;
	}

	/**
	 * @param accountancyNumbers
	 *            the accountancyNumbers to set
	 */
	public void setAccountancyNumbers(List<AccountancyNumber> accountancyNumbers) {
		if (accountancyNumbers != null) {

			this.accountancyNumbers = new HashSet<AccountancyNumber>(accountancyNumbers);
		} else {
			this.accountancyNumbers = null;
		}
	}

	/**
	 * Return the accountancy number matching the accountancy type given in
	 * parameter
	 * 
	 * @param accountancyType
	 *            The accountancy type
	 * @return The accountancy number
	 */
	public AccountancyNumber getAccountancyNumber(XmlEntity accountancyType) {
		Iterator<AccountancyNumber> itAccountancyNumber = this.getAccountancyNumbers().iterator();
		AccountancyNumber accountancyNumber = null;
		// Search accountancy number matching the accountancy type given in
		// parameter
		while (itAccountancyNumber.hasNext() && accountancyNumber == null) {
			AccountancyNumber localeAccountancyNumber = itAccountancyNumber.next();
			if (localeAccountancyNumber.getType().equals(accountancyType)) {
				accountancyNumber = localeAccountancyNumber;
			}
		}
		// Search in parents if any accountancy number is found
		if (accountancyNumber == null && this.getParent() != null) {
			accountancyNumber = this.getParent().getAccountancyNumber(accountancyType);
		}
		return accountancyNumber;
	}

	/**
	 * Return true if the given accountancy number is added
	 */
	public Boolean addAccountancyNumber(AbstractXmlEntity type) {
		if (type != null) {
			if (this.accountancyNumbers == null) {
				this.accountancyNumbers = new HashSet<AccountancyNumber>();
			}
			AccountancyNumber an = new AccountancyNumber();
			an.setType((XmlEntity) type);
			for (AccountancyNumber anI : this.accountancyNumbers) {
				if (anI.getType().equals(type)) {
					return false;
				}
			}
			return this.accountancyNumbers.add(an);
		}

		return null;
	}

	/**
	 * Return true if the given accountancy number is added
	 */
	public Boolean createAccountancyNumber() {
		if (this.accountancyNumbers == null) {
			this.accountancyNumbers = new HashSet<AccountancyNumber>();
		}
		return this.accountancyNumbers.add(new AccountancyNumber());
	}

	/**
	 * Return true if the given accountancy number is removed
	 */
	public Boolean removeAccountancyNumber(AccountancyNumber accountancyNumber) {
		if (accountancyNumber != null && this.accountancyNumbers != null) {
			return this.accountancyNumbers.remove(accountancyNumber);
		}

		return null;
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
	 * Update the qualifiedName
	 */
	@PrePersist
	@PreUpdate
	public void beforeUpdate() {
		this.position = null;
		this.getPosition();
		this.qualifiedName = "";
		if (this.parent != null) {
			this.qualifiedName = this.parent.getQualifiedName() + ">";
		}
		this.qualifiedName += this.value;
	}

	/**
	 * Test if the current object is equal with the given object
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Function other = (Function) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		return true;
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
	public int compareTo(Function o) {
		int compare = 0;
		if (o == null)
			return 1;

		// Parent
		if (compare == 0) {
			if (this.getParent() != null) {
				if (o.getParent() != null) {
					compare = this.getParent().compareTo(o.getParent());
				} else {
					compare = this.getParent().compareTo(o);
					if (compare == 0) {
						return 1;
					}
				}
			} else {
				if (o.getParent() != null) {
					compare = this.compareTo(o.getParent());
					if (compare == 0) {
						return -1;
					}
				}
			}
		}
		// Display number
		if (compare == 0) {
			if (this.getDisplayNumber() != null) {
				if (o.getDisplayNumber() == null) {
					compare = -1;
				} else {
					compare = this.getDisplayNumber().compareTo(o.getDisplayNumber());
				}
			} else {
				if (o.getDisplayNumber() == null) {
					compare = 1;
				}
			}
		}
		// Value
		if (compare == 0) {
			if (this.getValue() != null) {
				if (o.getValue() == null) {
					compare = -1;
				} else {
					compare = this.getValue().toLowerCase().compareTo(o.getValue().toLowerCase());
				}
			} else {
				if (o.getQualifiedName() == null) {
					compare = 1;
				}
			}
		}

		// Id
		if (compare == 0) {
			if (this.id != null) {
				if (o.id == null) {
					compare = -1;
				} else {
					compare = this.id.compareTo(o.id);
				}
			} else {
				if (o.id == null) {
					compare = 1;
				}
			}
		}
		return compare;
	}

	/**
	 * Comparator for qualified name only En java 8 : return
	 * Comparator.comparing(Function::getQualifiedName().toLowerCase()).thenComparing(Function::getId())
	 */
	public static final Comparator<Function> COMPARATOR_QUALIFIEDNAME = new Comparator<Function>() {
		@Override
		public int compare(Function f1, Function o) {
			int compare = 0;

			if (o == null) {
				return -1;
			}

			// Qualified name
			if (compare == 0) {
				if (f1.getQualifiedName() != null) {
					if (o.getQualifiedName() == null) {
						compare = -1;
					} else {
						compare = f1.getQualifiedName().toLowerCase().compareTo(o.getQualifiedName().toLowerCase());
					}
				} else {
					if (o.getQualifiedName() == null) {
						compare = 1;
					}
				}
			}

			// Id
			if (compare == 0) {
				if (f1.id != null) {
					if (o.id == null) {
						compare = -1;
					} else {
						compare = f1.id.compareTo(o.id);
					}
				} else {
					if (o.id == null) {
						compare = 1;
					}
				}
			}

			return compare;
		}
	};

	/**
	 * Return a textual description of the current object
	 * 
	 * @return the textual description
	 */
	@Override
	public String toString() {
		String name = "";
		Function current = this;
		while (current != null) {
			name = current.value + (!name.isEmpty() ? ">" + name : "");
			current = current.getParent();
		}
		return super.toString() + name;
	}

	/**
	 * @return the childFunctions
	 */
	public List<Function> getChildFunctions() {
		return childFunctions;
	}

	/**
	 * @param childFunctions
	 *            the childFunctions to set
	 */
	public void setChildFunctions(List<Function> childFunctions) {
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
	 * @return the folder
	 */
	public boolean isFolder() {
		return folder;
	}

	/**
	 * @param folder the folder to set
	 */
	public void setFolder(boolean folder) {
		this.folder = folder;
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
	
	
	
	

}
