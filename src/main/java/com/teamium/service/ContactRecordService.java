package com.teamium.service;

import com.teamium.domain.ContactRecord;
import com.teamium.dto.ContactRecordDTO;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.ContactRecordRepository;

public class ContactRecordService {

	private ContactRecordRepository contactRecordRepository;

	/**
	 * @param contactRecordRepository
	 */
	public ContactRecordService(ContactRecordRepository contactRecordRepository) {
		this.contactRecordRepository = contactRecordRepository;
	}
	
	public ContactRecord validateContactRecord(ContactRecordDTO contactRecordDTO) {
		if(contactRecordDTO.getContactId()==null) {
			throw new UnprocessableEntityException("Contact Id is required");
		}
		ContactRecord contactRecord=new ContactRecord();
		Long recordContactId=contactRecordDTO.getId();
		long personId=contactRecordDTO.getContactId();
		Long recordId=contactRecordDTO.getRecordId();
		if(recordContactId!=null) {
			contactRecord=contactRecordRepository.findOne(recordContactId);
			if(contactRecord==null) {
				throw new UnprocessableEntityException("Invalid contactRecord Id ");
			}
		}else if(recordId !=null) {
			
		}
		return null;
	}

}
