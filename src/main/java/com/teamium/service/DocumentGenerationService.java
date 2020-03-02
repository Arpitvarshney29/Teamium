package com.teamium.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.teamium.domain.prod.docsign.DocumentGeneration;
import com.teamium.repository.DocumentGenerationRepository;

/**
 * A service class implementation for Document generation
 * 
 * @author Hansraj
 *
 */
@Service
public class DocumentGenerationService {

	private DocumentGenerationRepository documentRepository;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * @param documentRepository
	 */
	public DocumentGenerationService(DocumentGenerationRepository documentRepository) {
		this.documentRepository = documentRepository;
	}

	/**
	 * To save or update document
	 * 
	 * @param document
	 * @return DocumentGeneration
	 */
	public DocumentGeneration saveOrUpdateDocument(DocumentGeneration document) {
		logger.info("Inside saveOrUpdateDocument :: save/update document : " + document);
		DocumentGeneration savedDocument = documentRepository.save(document);
		logger.info("Returning from saveOrUpdateDocument :: document has saved/updated successfuly");
		return savedDocument;
	}

}
