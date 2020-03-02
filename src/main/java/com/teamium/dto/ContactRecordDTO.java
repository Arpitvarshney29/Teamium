package com.teamium.dto;

import java.net.MalformedURLException;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.ContactRecord;
import com.teamium.domain.ContactRecordId;
import com.teamium.domain.Person;
import com.teamium.domain.XmlEntity;

@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ContactRecordDTO {

//	private ContactRecordId id;
//	private XmlEntity role;
	private PersonDTO personDTO;
	private Long contactId;
	private Long recordId;
	private String role;
	private Long id;

	public ContactRecordDTO() {

	}

	public ContactRecordDTO(ContactRecord contactRecord) {
//		this.setRole(contactRecord.getRole());
		Person person = contactRecord.getPerson();
		if (person != null) {
			this.setPersonDTO(new PersonDTO(person));
		}
	}

	public ContactRecordDTO( XmlEntity role, PersonDTO personDTO) {
//		this.id = id;
//		this.role = role;
		this.personDTO = personDTO;
	}

	public ContactRecord getContactRecord(ContactRecord contactRecord, ContactRecordDTO contactRecordDTO)
			throws MalformedURLException {
		
		setContactRecordDetail(contactRecord, contactRecordDTO);
		return contactRecord;
	}

	public void setContactRecordDetail(ContactRecord contactRecord, ContactRecordDTO contactRecordDTO)
			throws MalformedURLException {
	
		PersonDTO personDTO = contactRecordDTO.getPersonDTO();
		if (personDTO != null) {
			contactRecord.setPerson(personDTO.getPerson(new Person(), personDTO));
		}
	}

	/**
	 * @return the personDTO
	 */
	public PersonDTO getPersonDTO() {
		return personDTO;
	}

	/**
	 * @param personDTO
	 *            the personDTO to set
	 */
	public void setPersonDTO(PersonDTO personDTO) {
		this.personDTO = personDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ContactRecordDTO [" + ", personDTO=" + personDTO + "]";
	}

	/**
	 * @return the contactId
	 */
	public Long getContactId() {
		return contactId;
	}

	/**
	 * @param contactId the contactId to set
	 */
	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}

	/**
	 * @return the recordId
	 */
	public Long getRecordId() {
		return recordId;
	}

	/**
	 * @param recordId the recordId to set
	 */
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	
	
	
	
	

}
