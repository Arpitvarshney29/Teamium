package com.teamium.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamium.domain.FunctionKeys;
import com.teamium.domain.FunctionKeyword;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnauthorizedException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.FunctionKeysRepository;
import com.teamium.repository.FunctionKeywordRepository;
import com.teamium.domain.prod.resources.ResourceInformation;
import com.teamium.repository.ResourceInformationRepository;
import com.teamium.service.prod.resources.functions.FunctionService;

/**
 * To perform operations on functionKeyword.
 * 
 * @author Himanshu
 *
 */
@Service
public class FunctionKeywordService {

	private FunctionKeywordRepository functionKeywordRepository;
	private AuthenticationService authenticationService;
	private FunctionService functionService;
	private FunctionKeysRepository functionKeysRepository;
	private ResourceInformationRepository resourceInformationRepository;
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * @param functionKeywordRepository
	 * @param authenticationService
	 */
	@Autowired
	public FunctionKeywordService(FunctionKeywordRepository functionKeywordRepository,
			AuthenticationService authenticationService, FunctionService functionService,
			FunctionKeysRepository functionKeysRepository,
			ResourceInformationRepository resourceInformationRepository) {
		this.functionKeywordRepository = functionKeywordRepository;
		this.authenticationService = authenticationService;
		this.functionService = functionService;
		this.functionKeysRepository = functionKeysRepository;
		this.resourceInformationRepository = resourceInformationRepository;
	}

	/**
	 * To add functionKeyword.
	 * 
	 * @param functionKeyword
	 * @return functionKeyword
	 */
	public FunctionKeyword addFunctionKeyword(FunctionKeyword functionKeyword) {
		logger.info("Inside FunctionKeywordService :: addFunctionKeyword(), To add keywords: " + functionKeyword);

		if (!authenticationService.isAdmin()) {
			logger.error("An invalid user: " + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to add keywords.");
			throw new UnauthorizedException("You do not have authority to add keywords.");
		}

		functionKeyword = validateFunctionKeyword(functionKeyword);
		logger.info("Returning from FunctionKeywordService :: addFunctionKeyword()");
		return functionKeywordRepository.save(functionKeyword);
	}

	/**
	 * To validate a functionKeyword.
	 * 
	 * @param functionKeyword
	 * @return functionKeyword
	 */
	private FunctionKeyword validateFunctionKeyword(FunctionKeyword functionKeyword) {
		logger.info(
				"Inside FunctionKeywordService :: validateFunctionKeyword(), To validate keywords: " + functionKeyword);
		if (functionKeyword.getId() != null && functionKeywordRepository.findOne(functionKeyword.getId()) == null) {
			logger.error("Invalid keyword id given.");
			throw new NotFoundException("Invalid keyword id given.");
		}

		if (StringUtils.isBlank(functionKeyword.getKeyword())) {
			logger.error("Please provide a valid keyword.");
			throw new UnprocessableEntityException("Please provide a valid keyword.");
		}

		// Remove extra spaces.
		functionKeyword.setKeyword(functionKeyword.getKeyword().trim());

		if (functionKeyword.getKeyword().matches("[0-9]+")) {
			logger.error("Invalid keyword.");
			throw new UnprocessableEntityException("Invalid keyword.");
		}

		if (functionKeyword.getKeysList().stream().anyMatch(key -> StringUtils.isBlank(key.getKeyValue()))) {
			logger.error("The keys in this keyword should not be empty.");
			throw new UnprocessableEntityException("The keys in this keyword should not be empty.");
		}

		// Remove extra spaces from keys.
		functionKeyword.getKeysList().stream().forEach(key -> key.setKeyValue(key.getKeyValue().trim()));

		if (functionKeyword.getKeysList().stream().anyMatch(key -> key.getKeyValue().matches("[0-9]+"))) {
			logger.error("The keys in this keyword should not only contain numbers.");
			throw new UnprocessableEntityException("The keys in this keyword should not only contain numbers.");
		}

		FunctionKeyword functionKeywordInDb = functionKeywordRepository
				.findByKeywordIgnoreCase(functionKeyword.getKeyword());
		if (functionKeywordInDb != null && !(functionKeyword.getId() != null
				&& (functionKeyword.getId().longValue() == functionKeywordInDb.getId().longValue()))) {
			logger.error("Keyword already exists.");
			throw new UnprocessableEntityException("Keyword already exists.");
		}

		List<String> keyValues = functionKeyword.getKeysList().stream().map(key -> key.getKeyValue())
				.collect(Collectors.toList());
		if (IntStream.range(0, keyValues.size())
				.filter(st1 -> IntStream.range(st1 + 1, keyValues.size())
						.filter(st2 -> keyValues.get(st1).equalsIgnoreCase(keyValues.get(st2))).findFirst()
						.orElse(-1) > -1)
				.findFirst().orElse(-1) > -1) {
			logger.error("Duplicate keys should not be present in keywords.");
			throw new UnprocessableEntityException("Duplicate keys should not be present in keywords.");
		}

		if (functionKeyword.getId() != null) {
			if (functionKeyword.getKeysList().stream().filter(key -> key.getId() != null)
					.anyMatch(key -> functionKeysRepository.findOne(key.getId()) == null)) {
				logger.error("Provided key id is invalid.");
				throw new UnprocessableEntityException("Provided key id is invalid.");
			}
		} else {
			if (functionKeyword.getKeysList().stream().anyMatch(key -> key.getId() != null)) {
				logger.error("key id should not be present in a new keyword.");
				throw new UnprocessableEntityException("key id should not be present in a new keyword.");
			}
		}

		// check if key assigned on function on equipment
		if (functionKeyword.getId() != null && functionKeyword.getId() != 0) {
			FunctionKeyword dbKeyword = functionKeywordRepository.findOne(functionKeyword.getId());
			if (dbKeyword != null) {
				// converting
				List<Long> dbKeysIds = dbKeyword.getKeysList().stream().map(key -> key.getId())
						.collect(Collectors.toList());
				List<Long> dtoKeysIds = functionKeyword.getKeysList().stream().filter(key -> key.getId() != null)
						.map(key -> key.getId()).collect(Collectors.toList());

				dbKeysIds.stream().forEach(id -> {

					if (!dtoKeysIds.contains(id)) {

						Optional<FunctionKeys> isFoundKey = dbKeyword.getKeysList().stream()
								.filter(entity -> entity.getId().longValue() == id.longValue()).findFirst();

						if (isFoundKey.isPresent()) {
							List<ResourceInformation> resourceInfo = resourceInformationRepository
									.findByKeywordValueAndkeyValue(dbKeyword.getKeyword(),
											isFoundKey.get().getKeyValue());
							if (resourceInfo != null && !resourceInfo.isEmpty()) {
								logger.info("Cannot deassign key : " + isFoundKey.get().getKeyValue()
										+ " as this key has been assigned on equipment");
								throw new UnprocessableEntityException(
										"Cannot deassign key : " + isFoundKey.get().getKeyValue()
												+ " as this key has been assigned on equipment");
							}
						} else {
							logger.info("Key not found on this keyword");
							throw new UnprocessableEntityException("Key not found on this keyword");
						}
					}
				});

			}

		}

		logger.info("Returning from FunctionKeywordService :: validateFunctionKeyword()");
		return functionKeyword;
	}

	/**
	 * To get all functionKeywords.
	 * 
	 * @return list of FunctionKeywords
	 */
	public List<FunctionKeyword> getFunctionKeywords() {
		logger.info("Inside FunctionKeywordService :: getFunctionKeywords(), To get keywords.");
		return functionKeywordRepository.findAll().stream()
				.sorted((x, y) -> x.getKeyword().compareToIgnoreCase(y.getKeyword())).collect(Collectors.toList());
	}

	/**
	 * To get functionKeyword by id.
	 * 
	 * @param id
	 * @return FunctionKeyword
	 */
	public FunctionKeyword getFunctionKeyword(Long id) {
		logger.info("Inside FunctionKeywordService :: getFunctionKeyword(), To get keyword with id : " + id);

		FunctionKeyword functionKeyword = functionKeywordRepository.findOne(id);
		if (functionKeyword == null) {
			logger.error("Keyword id is not valid.");
			throw new NotFoundException("Keyword id is not valid.");
		}

		logger.info("Returning from FunctionKeywordService :: getFunctionKeyword()");
		return functionKeyword;
	}

	/**
	 * To delete a functionKeyword by id.
	 * 
	 * @param id
	 */
	public void removeFunctionKeywordById(Long id) {
		logger.info("Inside FunctionKeywordService :: removeFunctionKeywordById(), To remove keyword with id : " + id);

		if (!authenticationService.isAdmin()) {
			logger.error("You do not have authority to remove kewords.");
			throw new UnauthorizedException("You do not have authority to remove kewords.");
		}

		FunctionKeyword functionKeyword = functionKeywordRepository.findOne(id);
		if (functionKeyword == null) {
			logger.error("Keyword id is not valid.");
			throw new NotFoundException("Keyword id is not valid.");
		}

		if (functionService.findByFunctionKeywordId(id).size() > 0) {
			logger.error("The keyword is asigned to a function(s).");
			throw new NotFoundException("The keyword is asigned to a function(s).");
		}

		// Deleting keys.
		functionKeysRepository.delete(functionKeyword.getKeysList());

		logger.info("Returning from FunctionKeywordService :: removeFunctionKeywordById()");
		functionKeywordRepository.delete(id);

	}
}
