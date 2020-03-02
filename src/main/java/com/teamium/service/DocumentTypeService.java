package com.teamium.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.teamium.constants.Constants;
import com.teamium.domain.DocumentType;
import com.teamium.domain.PersonalDocument;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.DocumentTypeRepository;
import com.teamium.repository.PersonalDocumentRepository;

/**
 * A service class implementation for Configuration Controller for document-type
 * 
 * @author Teamium
 *
 */
@Service
public class DocumentTypeService {

	private DocumentTypeRepository documentTypeRepository;
	private AuthenticationService authenticationService;

	@Autowired
	@Lazy
	private PersonalDocumentRepository personalDocumentRepository;

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * @param documentTypeRepository
	 */
	public DocumentTypeService(DocumentTypeRepository documentTypeRepository,
			AuthenticationService authenticationService) {
		this.documentTypeRepository = documentTypeRepository;
		this.authenticationService = authenticationService;
	}

	/**
	 * To save or update format
	 * 
	 * @param format
	 * 
	 * @return format object
	 */
	public DocumentType saveOrUpdateDocumentType(DocumentType documentType) {
		logger.info(
				" Inside FormatService:: saveOrUpdateDocumentType() , To save or update documentType , documentType : "
						+ documentType);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to save/update document-type.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		if (documentType.getId() != null && documentType.getId() != 0) {
			this.getDocumentTypeById(documentType.getId());
		}
		String type = documentType.getType();
		if (StringUtils.isBlank(type)) {
			logger.info("Please provide a valid document name");
			throw new UnprocessableEntityException("Please provide a valid document name");
		}
		documentType.setType(type.trim());
		DocumentType validateDocumentType = documentTypeRepository.findByTypeIgnoreCase(documentType.getType());
		if (documentType.getId() == null && validateDocumentType != null) {
			logger.info("Document-type already exist with given name : " + documentType.getType());
			throw new UnprocessableEntityException("Document-type already exist with given name");
		}
		if (documentType.getId() != null && validateDocumentType != null
				&& (documentType.getId().longValue() != validateDocumentType.getId().longValue())) {
			logger.info("Document-type already exist with given name : " + documentType.getType());
			throw new UnprocessableEntityException("Document-type already exist with given name");
		}
		logger.info("Saving document-type with name : " + documentType.getType());
		return documentTypeRepository.save(documentType);
	}

	/**
	 * Method to get document-type by id
	 * 
	 * @param id
	 * 
	 * @return the document-type object
	 */
	public DocumentType getDocumentTypeById(Long documentTypeId) {
		logger.info(" Inside FormatService:: getDocumentTypeById() , To get document-type by id, documentTypeId : "
				+ documentTypeId);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get document-type by id.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		DocumentType documentType = documentTypeRepository.findOne(documentTypeId);
		if (documentType == null) {
			logger.info("Document-Type not found with id : " + documentTypeId);
			throw new NotFoundException("Document-Type not found");
		}
		return documentType;
	}

	/**
	 * To get list of document-types.
	 * 
	 * @return list of document-types
	 */
	public List<DocumentType> getAllDocumentType() {
		logger.info(" Inside FormatService:: getAllDocumentType() , To get list of all document-types");
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get document-types.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		return documentTypeRepository.getAllDocumentType();
	}

	/**
	 * Method to get all document-type name
	 * 
	 * @return list of document-type name
	 */
	public List<String> getAllDocumentTypeName() {
		logger.info(" Inside FormatService:: getAllDocumentTypeName() , To get list of all document-type name");
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get document-type name.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		return documentTypeRepository.getAllDocumentTypeName();
	}

	/**
	 * To delete document-type by documentTypeId.
	 * 
	 * @param documentTypeId
	 */
	public void deleteDocumentType(long documentTypeId) {
		logger.info("Inside FormatService:: deleteDocumentType() , To delete document-type by id : " + documentTypeId);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to delete document-type.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		DocumentType type = this.getDocumentTypeById(documentTypeId);
		List<PersonalDocument> list = personalDocumentRepository.findByTypeAndPerson(type);
		if (list != null && !list.isEmpty()) {
			logger.info("Cannot delete document as it is already assigned as personal document on Personnel");
			throw new UnprocessableEntityException(
					"Cannot delete document as it is already assigned as personal document on Personnel");
		}
		documentTypeRepository.delete(documentTypeId);
		logger.info("Successfully deleted document-type with id : " + documentTypeId);;
	}

	/**
	 * Method to get Format by name by IgnoreCase
	 * 
	 * @param name
	 * 
	 * @return the format object
	 */
	public DocumentType findDocumentTypeByType(String type) {
		logger.info("Inside FormatService :: findDocumentTypeByType(), To get document-type by type : " + type);
		DocumentType documentType = documentTypeRepository.findByTypeIgnoreCase(type);
		logger.info("Returning after finding document-type by type");
		return documentType;
	}

}
