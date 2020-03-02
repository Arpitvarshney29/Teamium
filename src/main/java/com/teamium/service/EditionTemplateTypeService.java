package com.teamium.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamium.domain.EditionTemplateType;
import com.teamium.exception.BadRequestException;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnauthorizedException;
import com.teamium.repository.EditionTemplateTypeRepository;

@Service
public class EditionTemplateTypeService {

	private EditionTemplateTypeRepository editionTemplateTypeRepository;
	private AuthenticationService authenticationService;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public EditionTemplateTypeService(EditionTemplateTypeRepository editionTemplateTypeRepository,
			AuthenticationService authenticationService) {
		this.editionTemplateTypeRepository = editionTemplateTypeRepository;
		this.authenticationService = authenticationService;
	}

	/**
	 * To save or update EditionTemplateType
	 * 
	 * @param editionTemplateType
	 * @return EditionTemplateType
	 */
	public EditionTemplateType saveOrUpdate(EditionTemplateType editionTemplateType) {
		logger.info(" Inside EditionTemplateTypeService: saveAndUpdate editionTemplateType: "
				+ editionTemplateType.toString());
		if (!authenticationService.isAdmin()) {
			logger.warn("Invalid role");
			throw new UnauthorizedException("Invalid role");
		}

		if (StringUtils.isBlank(editionTemplateType.getTemplateName())) {
			logger.warn("Template name can not be empty.");
			throw new BadRequestException("Template name can not be empty.");
		}
		EditionTemplateType editionTemplateByName = editionTemplateTypeRepository
				.findByTemplateNameIgnoreCase(editionTemplateType.getTemplateName());

		EditionTemplateType editionTemplate = new EditionTemplateType();
		if (editionTemplateType.getId() != null) {
			editionTemplate = findById(editionTemplateType.getId());
			if (editionTemplateByName != null && editionTemplateByName.getId() != editionTemplate.getId()) {
				logger.error("Template with given name already exist.");
				throw new BadRequestException("Template with given name already exist.");
			}
			editionTemplate.setTemplateName(editionTemplateType.getTemplateName());
		} else {
			if (editionTemplateByName != null) {
				logger.error("Template with given name already exist.");
				throw new BadRequestException("Template with given name already exist.");
			}
			editionTemplate.setTemplateName(editionTemplateType.getTemplateName());
		}
		logger.info("Returning after successfully saving EditionTemplateType");
		return editionTemplateTypeRepository.save(editionTemplate);

	}

	/**
	 * To get list of EditionTemplateType
	 * 
	 * @return
	 */
	public List<EditionTemplateType> findAllEditionTemplateTypes() {
		logger.info(" Inside EditionTemplateTypeService: findAllEditionTemplateTypes");
		return editionTemplateTypeRepository.findAll();
	}

	/**
	 * To get EditionTemplateType by id.
	 * 
	 * @param id
	 * @return EditionTemplateType
	 */
	public EditionTemplateType findById(long id) {
		logger.info(" Inside EditionTemplateTypeService: findById:" + id);
		EditionTemplateType editionTemplateType = editionTemplateTypeRepository.findOne(id);
		if (editionTemplateType == null) {
			logger.warn("Invalid EditionTemplateType");
			throw new NotFoundException("Invalid EditionTemplateType ");
		}
		logger.info("Returning after successfully geting EditionTemplateType");
		return editionTemplateType;
	}

	/**
	 * To get EditionTemplateType by templateName.
	 * 
	 * @param id
	 * @return EditionTemplateType
	 */
	public EditionTemplateType findByTemplateNameIgnoreCase(String templateName) {
		logger.info(" Inside EditionTemplateTypeService: findByTemplateName:" + templateName);
		EditionTemplateType editionTemplateType = editionTemplateTypeRepository.findByTemplateNameIgnoreCase(templateName);
		if (editionTemplateType == null) {
			logger.warn("Invalid EditionTemplateType");
			throw new NotFoundException("Invalid EditionTemplateType ");
		}
		logger.info("Returning after successfully geting EditionTemplateType");
		return editionTemplateType;
	}

	/**
	 * To delete EditionTemplateType by id.
	 * 
	 * @param id
	 */
	public void deleteEditionTemplateTypeById(long id) {
		logger.info(" Inside EditionTemplateTypeService: deleteEditionTemplateTypeById:" + id);
		if (!authenticationService.isAdmin()) {
			logger.warn("Invalid role");
			throw new UnauthorizedException("Invalid role");
		}
		findById(id);
		editionTemplateTypeRepository.delete(id);
		logger.info("Successfully deleted EditionTemplateType having id:" + id);

	}

}
