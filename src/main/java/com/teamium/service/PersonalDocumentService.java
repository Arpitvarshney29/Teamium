package com.teamium.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.teamium.domain.DocumentType;
import com.teamium.domain.PersonalDocument;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.dto.PersonalDocumentDTO;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.DocumentTypeRepository;
import com.teamium.repository.PersonalDocumentRepository;

/**
 * A service class implementation for Booking Controller
 *
 */
@Service
public class PersonalDocumentService {

	private PersonalDocumentRepository personalDocumentRepository;
	private DocumentTypeRepository documentTypeRepository;
	private AuthenticationService authenticationService;
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	public PersonalDocumentService(PersonalDocumentRepository personalDocumentRepository,
			DocumentTypeRepository documentTypeRepository, AuthenticationService authenticationService) {
		this.personalDocumentRepository = personalDocumentRepository;
		this.documentTypeRepository = documentTypeRepository;
		this.authenticationService = authenticationService;
	}

	/**
	 * To save/update the personal document
	 * 
	 * @param documentDTO
	 * @return PersonalDocument
	 */
	public PersonalDocument saveOrUpdateDocument(PersonalDocumentDTO documentDTO, StaffMember documentPerson) {
		logger.info("Inside saveOrUpdateDocument() :: save/update personalDocument:  " + documentDTO);
		PersonalDocument document;
		if (documentDTO.getId() != null) {
			document = validateDocument(documentDTO.getId());
			document = documentDTO.getPersonalDocument(document);

		} else {
			String documentTypeString = documentDTO.getType();
			if (StringUtils.isBlank(documentTypeString)) {
				logger.error("Invalid document type ");
				throw new UnprocessableEntityException("Invalid document type ");
			}
			documentTypeString = documentTypeString.trim();
			DocumentType documentType = documentTypeRepository.findByType(documentTypeString);
			if (documentType == null) {
				logger.info("Document-type not found");
				throw new NotFoundException("Document-type not found");
			}
			PersonalDocument savedDocument = personalDocumentRepository.findByDocumentPersonAndType(documentPerson,
					documentType);
			if (savedDocument != null) {
				logger.error(documentTypeString + "  already exists ");
				throw new UnprocessableEntityException(documentTypeString + " already exists ");
			}
			document = documentDTO.getPersonalDocument();

			document.setType(documentType);
		}
		document.setDocumentPerson(documentPerson);
		document = personalDocumentRepository.save(document);
		logger.info("Document has updated successfully ");
		logger.info("Returning from saveOrUpdateDocument() .");
		return document;
	}

	/**
	 * To save/update the personal document
	 * 
	 * @param documentDTO
	 * @return PersonalDocumentDTO
	 */
	public PersonalDocumentDTO saveOrUpdateDocument(PersonalDocumentDTO documentDTO) {
		StaffMember loggedInUser = authenticationService.getAuthenticatedUser();
		return new PersonalDocumentDTO(saveOrUpdateDocument(documentDTO, loggedInUser));
	}

	/**
	 * To Validate PersonalDocment by documentId
	 * 
	 * @param documentId the documentId
	 * 
	 * @return the PersonalDocument
	 */
	private PersonalDocument validateDocument(Long documentId) {
		logger.info("Inside validateDocument() :: validating document with id: " + documentId);
		if (documentId == null) {
			logger.error("Document id is null");
			throw new NotFoundException("Document id can not be null");
		}
		PersonalDocument document = personalDocumentRepository.findOne(documentId);
		if (document == null) {
			logger.error("Document not found ");
			throw new UnprocessableEntityException("Document not found");
		}
		logger.info("Returning from validateDocument() .");
		return document;
	}

	/**
	 * To find Document by documentId
	 * 
	 * @param documentId the documentId
	 * 
	 * @return the PersonalDocumentDTO
	 */
	public PersonalDocumentDTO findDocument(Long documentId) {
		logger.info("inside findDocument() :: finding document with id: " + documentId);
		PersonalDocument document = validateDocument(documentId);
		PersonalDocumentDTO documentDTO = new PersonalDocumentDTO(document);
		logger.info("Returning from findDocument() .");
		return documentDTO;
	}

	/**
	 * To delete Document by documentId
	 * 
	 * @param documentId the documentId
	 * 
	 */
	public void deleteDocument(Long documentId) {
		logger.info("inside deleteDocument() :: deleting document with id: " + documentId);
		PersonalDocument p = this.validateDocument(documentId);
		p.setDocumentPerson(null);
		p.setType(null);
		personalDocumentRepository.save(p);
		personalDocumentRepository.delete(p);
		logger.info("Returning from deleteDocument() .");

	}

	/**
	 * To get document type
	 * 
	 * @param documentTypeString the documentTypeString
	 * 
	 * @return DocumentType
	 */
	public DocumentType getDocumentType(String documentTypeString) {
		logger.info("Inside getDocumentType() :: ");
		if (StringUtils.isBlank(documentTypeString)) {
			logger.error("Invalid document type");
			throw new UnprocessableEntityException("Invalid document type");
		}
		documentTypeString = documentTypeString.trim();
		DocumentType documentType = documentTypeRepository.findByType(documentTypeString);
		if (documentType == null) {
			logger.info("Document-type not found");
			throw new NotFoundException("Document-type not found");
		}
		logger.info("Returning from getDocumentType ");
		return documentType;
	}

}
