package com.teamium.dto;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.AbstractEntity;
import com.teamium.domain.Document;
import com.teamium.domain.Person;
import com.teamium.domain.Role;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.resources.staff.StaffEmail;
import com.teamium.domain.prod.resources.staff.StaffTelephone;

/**
 * DTO Class for Person Entity
 * 
 * @author wittybrains
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class PersonDTO extends AbstractDTO {

	private XmlEntity courtesy;
	private String lastName;
	private String firstName;
	private String jobTitle; // same as Person entity function
	private DocumentDTO photo;
	private Map<String, String> numbers;
	private List<Role> role;
	private Set<PersonalDocumentDTO> documents;
	private boolean main;
	private String fullName;
	private boolean removePhoto = false;
	private UserSettingDTO userSettingDTO;
	private String directorEmail;
	private String directorTelephone;

	public PersonDTO() {
	}

	public PersonDTO(AbstractEntity abstractEntity) {
		super(abstractEntity);
	}

	public PersonDTO(Person entity, boolean isForList) {
		super(entity);
		this.lastName = entity.getName();
		this.firstName = entity.getFirstName();
		Document document = entity.getPhoto();
		if (document != null) {
			this.photo = new DocumentDTO(document);
		}
		this.fullName = this.firstName + " " + this.lastName;
	}

	public PersonDTO(Person entity) {
		// super(entity);
		if (entity != null) {
			this.setId(entity.getId());
			this.setVersion(entity.getVersion());
			this.courtesy = entity.getCourtesy();
			this.lastName = entity.getName();
			this.firstName = entity.getFirstName();
			this.jobTitle = entity.getFunction();
			Document document = entity.getPhoto();
			if (document != null) {
				this.photo = new DocumentDTO(document);
			}
			this.numbers = entity.getNumbers();
			this.role = entity.getRole();
			if (entity.getDocuments() != null) {
				this.documents = entity.getDocuments().stream().map(doc -> new PersonalDocumentDTO(doc))
						.collect(Collectors.toSet());
			}
			if (entity != null) {
				this.userSettingDTO = new UserSettingDTO(entity.getUserSetting(), true);
			}
			this.fullName = this.firstName + " " + this.lastName;
		}
	}

	public PersonDTO(Person entity, String forDirector) {
		// super(entity);
		if (entity != null) {
			this.setId(entity.getId());
			this.lastName = entity.getName();
			this.firstName = entity.getFirstName();
			if (entity.getUserSetting() != null) {
				Set<StaffEmail> staffEmails = entity.getUserSetting().getEmails();

				Set<StaffTelephone> staffTelephones = entity.getUserSetting().getTelephones();


				if (staffEmails.size() == 1) {
					StaffEmail email = staffEmails.stream().findFirst().get();
					this.directorEmail = email.getEmail();
				}
				if (staffTelephones.size() == 1) {
					StaffTelephone telephone = staffTelephones.stream().findFirst().get();
					this.directorTelephone = telephone.getTelephone();
				}

			}
		}
	}

	public PersonDTO(XmlEntity courtesy, String lastName, String firstName, String function, DocumentDTO photo,
			Map<String, String> numbers, List<Role> role) {
		this.courtesy = courtesy;
		this.lastName = lastName;
		this.firstName = firstName;
		this.jobTitle = function;
		this.photo = photo;
		this.numbers = numbers;
		this.role = role;
	}

	/**
	 * Get Person Object for Person DTO
	 * 
	 * @param person
	 * @return Person
	 */
	public Person getPerson(Person person) {
		super.getAbstractEntityDetails(person);
		person.setCourtesy(courtesy);
		person.setFirstName(firstName);
		person.setName(lastName);
		if (this.removePhoto) {
			person.setPhoto(null);
		}
		// if (this.photo != null) {
		// person.setPhoto(this.photo.getDocument());
		// } else {
		// person.setPhoto(null);
		// }

		person.setNumbers(numbers);
		person.setRole(role);
		person.setFunction(this.jobTitle);
		return person;
	}

	public Person getPerson(Person person, PersonDTO personDTO) throws MalformedURLException {
		setPersonDetail(person, personDTO);
		return person;
	}

	public void setPersonDetail(Person person, PersonDTO personDTO) throws MalformedURLException {
		person.setId(personDTO.getId());
		person.setVersion(personDTO.getVersion());
		person.setCourtesy(personDTO.getCourtesy());
		person.setName(personDTO.getLastName());
		person.setFirstName(personDTO.getFirstName());
		person.setFunction(personDTO.getJobTitle());
		DocumentDTO document = personDTO.getPhoto();
		if (document != null) {
			person.setPhoto(document.getDocument());
		}
		person.setNumbers(personDTO.getNumbers());
		person.setRole(personDTO.getRole());
	}

	/**
	 * @return the courtesy
	 */
	public XmlEntity getCourtesy() {
		return courtesy;
	}

	/**
	 * @param courtesy the courtesy to set
	 */
	public void setCourtesy(XmlEntity courtesy) {
		this.courtesy = courtesy;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the jobTitle
	 */
	public String getJobTitle() {
		return jobTitle;
	}

	/**
	 * @param jobTitle the jobTitle to set
	 */
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
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
	 * @return the role
	 */
	public List<Role> getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(List<Role> role) {
		this.role = role;
	}

	/**
	 * @return the numbers
	 */
	public Map<String, String> getNumbers() {
		return numbers;
	}

	/**
	 * @param numbers the numbers to set
	 */
	public void setNumbers(Map<String, String> numbers) {
		this.numbers = numbers;
	}

	/**
	 * @return the documents
	 */
	public Set<PersonalDocumentDTO> getDocuments() {
		return documents;
	}

	/**
	 * @param documents the documents to set
	 */
	public void setDocuments(Set<PersonalDocumentDTO> documents) {
		this.documents = documents;
	}

	/**
	 * @return the main
	 */
	public boolean isMain() {
		return main;
	}

	/**
	 * @param main the main to set
	 */
	public void setMain(boolean main) {
		this.main = main;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
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

	public UserSettingDTO getUserSettingDTO() {
		return userSettingDTO;
	}

	public void setUserSettingDTO(UserSettingDTO userSettingDTO) {
		this.userSettingDTO = userSettingDTO;
	}

	public String getDirectorEmail() {
		return directorEmail;
	}

	public void setDirectorEmail(String directorEmail) {
		this.directorEmail = directorEmail;
	}

	public String getDirectorTelephone() {
		return directorTelephone;
	}

	public void setDirectorTelephone(String directorTelephone) {
		this.directorTelephone = directorTelephone;
	}

}
