/**
 * 
 */
package com.teamium.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.AttributeOverride;
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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

import com.teamium.domain.prod.resources.equipments.Attachment;
import com.teamium.domain.prod.resources.staff.UserSetting;

/**
 * Describe a generic individual person
 * 
 * @author sraybaud
 * @version TEAM-18
 *
 */
/**
 * @author TeamiumNishant
 *
 */
@Entity
@Table(name = "t_person", uniqueConstraints = @UniqueConstraint(columnNames = { "c_user_email" }))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "c_discriminator", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("person")
@NamedQueries({
		@NamedQuery(name = Person.QUERY_countAllByType, query = "SELECT COUNT(p) FROM Person p WHERE TYPE(p) = ?1"),
		@NamedQuery(name = Person.QUERY_findAllByType, query = "SELECT p FROM Person p WHERE TYPE(p) = ?1 ORDER BY p.name ASC, p.firstName ASC"),
		@NamedQuery(name = Person.QUERY_findByIdsByType, query = "SELECT p FROM Person p WHERE p.id IN (?1) AND TYPE(p) = ?2 ORDER BY p.name ASC, p.firstName ASC"), })
public class Person extends AbstractEntity implements Comparable<Person> {

	/**
	 * The entity alias used in queries
	 */
	public static final String QUERY_ENTITY_ALIAS = "p";

	/**
	 * Name of the query counting all persons matching a given type
	 * 
	 * @param 1
	 *            the type of person to count
	 * @return the count of all found persons with this type
	 */
	public static final String QUERY_countAllByType = "countAllPersonByType";

	/**
	 * Name of the query retrieving all persons with the given type
	 * 
	 * @param 1
	 *            the type of person to retrieve
	 * @return the list of all persons with the given type
	 */
	public static final String QUERY_findAllByType = "findAllPersonByType";
	/**
	 * Name of the query retrieving the persons matching one of the given ids
	 * 
	 * @param 1
	 *            the ids the match
	 * @return the list of matching persons
	 */
	public static final String QUERY_findByIdsByType = "findPersonByIdsByType";

	/**
	 * Name of the query retrieving all persons with the given type, that may be
	 * completed by OrderByClause.getClause()
	 * 
	 * @param 1
	 *            the type of persons to retrieve
	 * @return the list of all persons with the given type
	 */
	public static final String QUERY_findAllOrderedByType = "SELECT p FROM Person p WHERE TYPE(p) = ?1";

	/**
	 * String of the query retrieving the ids of the given type of persons matching
	 * one of the given ids,that may be completed by OrderByClause.getClause()
	 * 
	 * @param 1
	 *            the ids the match
	 * @param 2
	 *            the type of persons to retrieve
	 * @return the list of matching ids
	 */
	public static final String QUERY_findOrderedByIdsByType = "SELECT p.id FROM Person p WHERE p.id IN (?1) AND TYPE(p) = ?2";

	/**
	 * Nom de la requête retournant les contacts d'un client
	 * 
	 * @param customer
	 *            le client dont les contacts sont recherchés
	 * @return le client correspondant
	 */
	public static final String QUERY_findPersonsByCompany = "findPersonsByCompany";

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 5845480911736111359L;

	/**
	 * Person ID
	 */
	@Id
	@Column(name = "c_idperson", insertable = false, updatable = false)
	@TableGenerator(name = "idPerson_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "person_idperson_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idPerson_seq")
	private Long id;

	/**
	 * Courtesy
	 * 
	 * @see com.teamium.domain.Courtesy
	 */
	@Embedded
	@AttributeOverride(name = "key", column = @Column(name = "c_courtesy"))
	private XmlEntity courtesy;

	/**
	 * Contact name
	 */
	@Column(name = "c_lastname")
	private String name;

	/**
	 * Contact first name
	 */
	@Column(name = "c_firstname")
	private String firstName;

	/**
	 * Contact function in his company
	 */
	@Column(name = "c_function")
	private String function;

	/**
	 * Photo
	 */
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "c_idphoto")
	private Document photo;

	/**
	 * Contact numbers
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@MapKeyColumn(name = "c_name")
	@Column(name = "c_value")
	@CollectionTable(name = "t_person_number", joinColumns = @JoinColumn(name = "c_idperson"))
	private Map<String, String> numbers;

	/**
	 * Role
	 * 
	 * @see com.teamium.domain.Role
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "t_user_role", joinColumns = @JoinColumn(name = "c_idperson"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> role = new ArrayList<Role>();

	/**
	 * List of personal document of
	 */

	@OneToMany(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
	@JoinColumn(name="c_idperson")
	private Set<PersonalDocument> documents = new HashSet<PersonalDocument>();

	@Column(name = "c_first_time_login")

	private boolean firstTimeLogin = true;

	@OneToMany(cascade = { CascadeType.ALL })
	@JoinColumn(name = "c_idperson")
	private Set<Attachment> attachments = new HashSet<>();

	/**
	 * record history
	 */
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "c_idperson", nullable = true)
	private Set<RecordHistory> recordHistory = new HashSet<RecordHistory>();

	/**
	 * password-recovery
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
	private UserPasswordRecovery userPasswordRecovery;
	
	@Column(name = "c_available")
	private boolean available = true;
	
	/**
	 * User settings
	 */
	@Embedded
	private UserSetting userSetting;

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
	 * @return the courtesy
	 */
	public XmlEntity getCourtesy() {
		return courtesy;
	}

	/**
	 * @param courtesy
	 *            the courtesy to set
	 */
	public void setCourtesy(XmlEntity courtesy) {
		this.courtesy = courtesy;
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
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
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
	 * @return the function
	 */
	public String getFunction() {
		return function;
	}

	/**
	 * @param function
	 *            the function to set
	 */
	public void setFunction(String function) {
		this.function = function;
	}

	/**
	 * @return the numbers
	 */
	public Map<String, String> getNumbers() {
		if (this.numbers == null)
			this.numbers = new Hashtable<String, String>();
		return numbers;
	}

	/**
	 * @param numbers
	 *            the numbers to set
	 */
	public void setNumbers(Map<String, String> numbers) {
		this.numbers = numbers;
	}

	/**
	 * @return the role
	 */
	public List<Role> getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(List<Role> role) {
		this.role = role;
	}

	/**
	 * @return the userPasswordRecovery
	 */
	public UserPasswordRecovery getUserPasswordRecovery() {
		return userPasswordRecovery;
	}

	/**
	 * @param userPasswordRecovery
	 *            the userPasswordRecovery to set
	 */
	public void setUserPasswordRecovery(UserPasswordRecovery userPasswordRecovery) {
		this.userPasswordRecovery = userPasswordRecovery;
	}

	/**
	 * Compare the current objet with the given one
	 * 
	 * @param other
	 *            the other to compare
	 * @return -1 if the current is previous, 0 if both are equal, +1 if the given
	 *         is previous
	 */
	@Override
	public int compareTo(Person o) {
		if (o == null)
			return 1;
		if (this.name == null) {
			if (o.name != null) {
				return -1;
			}
		} else {
			int compare = this.name.compareTo(o.name);
			if (compare != 0)
				return compare;
		}
		if (this.firstName == null) {
			if (o.firstName != null) {
				return -1;
			}
		} else {
			int compare = this.firstName.compareTo(o.firstName);
			if (compare != 0)
				return compare;
		}
		if (this.id == null) {
			if (o.id != null) {
				return -1;
			}
		} else {
			int compare = this.id.compareTo(o.id);
			if (compare != 0)
				return compare;
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
		return super.toString() + " " + this.name + " " + this.firstName;
	}

	/**
	 * Return an instance a clone of the person
	 */
	@Override
	public Person clone() throws CloneNotSupportedException {
		Person clone = null;
		Long id = this.id;
		this.id = null;
		Document photo = this.photo;
		XmlEntity courtesy = this.courtesy;
		this.courtesy = null;
		this.photo = null;
		Map<String, String> numbers = this.numbers;
		this.numbers = null;
		try {
			clone = (Person) super.clone();
			clone.setCourtesy(courtesy);
			clone.setPhoto(photo);
			clone.setNumbers(new HashMap<String, String>(numbers.size()));
			for (Entry<String, String> number : numbers.entrySet()) {
				clone.numbers.put(number.getKey(), number.getValue());
			}
			return clone;
		} catch (CloneNotSupportedException e) {
			throw e;
		} finally {
			this.id = id;
			this.photo = photo;
			this.numbers = numbers;
			this.courtesy = courtesy;
		}

	}

	/**
	 * @return the documents
	 */
	public Set<PersonalDocument> getDocuments() {
		return documents;
	}

	/**
	 * @param documents
	 *            the documents to set
	 */
	public void setDocuments(Set<PersonalDocument> documents) {
		this.documents = documents;
	}

	/**
	 * @return the firstTimeLogin
	 */
	public boolean isFirstTimeLogin() {
		return firstTimeLogin;
	}

	/**
	 * @param firstTimeLogin
	 *            the firstTimeLogin to set
	 */
	public void setFirstTimeLogin(boolean firstTimeLogin) {
		this.firstTimeLogin = firstTimeLogin;
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
	 * @return the recordHistory
	 */
	public Set<RecordHistory> getRecordHistory() {
		return recordHistory;
	}

	/**
	 * @param recordHistory
	 *            the recordHistory to set
	 */
	public void setRecordHistory(Set<RecordHistory> recordHistory) {
		this.recordHistory = recordHistory;
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

	public UserSetting getUserSetting() {
		return userSetting;
	}

	public void setUserSetting(UserSetting userSetting) {
		this.userSetting = userSetting;
	}
	

}
