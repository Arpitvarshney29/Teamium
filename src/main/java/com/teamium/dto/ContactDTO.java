package com.teamium.dto;

import com.teamium.domain.Person;
import com.teamium.domain.prod.resources.contacts.Contact;

/**
 * DTO Class for Contract Entity
 * 
 * @author wittybrains
 *
 */
public class ContactDTO extends PersonDTO {

	public ContactDTO() {
		super();
	}

	public ContactDTO(Person entity) {
		super(entity);
	}

	public ContactDTO(Person entity, boolean isForList) {
		super(entity, isForList);
	}

	/**
	 * Get Contract Object for Current DTO
	 * 
	 * @param contract
	 * @return Contract
	 */
	public Contact getContact(Contact contract) {
		super.getPerson(contract);
		return contract;
	}

}
