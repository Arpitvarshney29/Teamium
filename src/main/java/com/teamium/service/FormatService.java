package com.teamium.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.teamium.constants.Constants;
import com.teamium.domain.Channel;
import com.teamium.domain.Format;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.FormatRepository;

/**
 * Service to manage format configuration
 * 
 * @author Teamium
 *
 */
@Service
public class FormatService {

	private AuthenticationService authenticationService;
	private FormatRepository formatRepository;
	
	@Autowired
	@Lazy
	private ChannelService channelService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public FormatService(FormatRepository formatRepository, AuthenticationService authenticationService) {
		this.formatRepository = formatRepository;
		this.authenticationService = authenticationService;
	}

	/**
	 * To save or update format
	 * 
	 * @param format
	 * 
	 * @return format object
	 */
	public Format saveOrUpdateFormat(Format format) {
		logger.info(" Inside FormatService:: saveOrUpdateFormat() , To save or update format , format : " + format);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to save/update format.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		if (format.getId() != null && format.getId() != 0) {
			this.getFormatById(format.getId());
		}
		if (StringUtils.isBlank(format.getName())) {
			logger.info("Please provide a valid format");
			throw new UnprocessableEntityException("Please provide a valid format");
		}

		Format validateFormat = formatRepository.findByNameIgnoreCase(format.getName());
		if (format.getId() == null && validateFormat != null) {
			logger.info("Format already exist with given name : " + format.getName());
			throw new UnprocessableEntityException("Format already exist with given name");
		}
		if (format.getId() != null && validateFormat != null
				&& (format.getId().longValue() != validateFormat.getId().longValue())) {
			logger.info("Format already exist with given name : " + format.getName());
			throw new UnprocessableEntityException("Format already exist with given name");
		}
		logger.info("Saving format with name : " + format.getName());
		return formatRepository.save(format);
	}

	/**
	 * Method to get format by id
	 * 
	 * @param id
	 * 
	 * @return the format object
	 */
	public Format getFormatById(Long id) {
		logger.info(" Inside FormatService:: getFormatById() , To get format by id, formatId : " + id);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get format by id.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		Format format = formatRepository.findOne(id);
		if (format == null) {
			logger.info("Format not found with id : " + id);
			throw new NotFoundException("Format not found");
		}
		return format;
	}

	/**
	 * To get list of formats.
	 * 
	 * @return list of formats
	 */
	public List<Format> getAllFormats() {
		logger.info(" Inside FormatService:: getAllFormats() , To get list of all formats");
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get formats.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		return formatRepository.getAllFormats();
	}

	/**
	 * Method to get all formats name
	 * 
	 * @return list of formats name
	 */
	public List<String> getAllFormatsName() {
		logger.info(" Inside FormatService:: getAllFormatsName() , To get list of all formats name");
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get formats name.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		return formatRepository.getAllFormatsName();
	}

	/**
	 * To delete format by id.
	 * 
	 * @param id
	 */
	public void deleteFormat(long id) {
		logger.info(" Inside FormatService:: deleteFormat() , To delete format by id : " + id);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to delete format.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		Format format = this.getFormatById(id);
		List<Channel> list = channelService.getChannelByFormat(format);
		if (list != null && !list.isEmpty()) {
			logger.info("Cannot delete format as it is already assigned on channel");
			throw new UnprocessableEntityException("Cannot delete format as it is already assigned on channel");
		}
		formatRepository.delete(id);
		logger.info("Successfully deleted");
	}

	/**
	 * Method to get Format by name by IgnoreCase
	 * 
	 * @param name
	 * 
	 * @return the format object
	 */
	public Format findFormatByName(String name) {
		logger.info("Inside FormatService :: findFormatByName(), To get format by name : " + name);
		Format format = formatRepository.findByNameIgnoreCase(name);
		logger.info("Returning after finding format by name");
		return format;
	}

}
