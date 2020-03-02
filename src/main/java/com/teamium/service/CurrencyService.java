package com.teamium.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamium.constants.Constants;
import com.teamium.domain.CurrencySymbol;
import com.teamium.dto.CurrencyDTO;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.CurrencySymbolRepository;
import com.teamium.repository.LineRepository;
import com.teamium.service.prod.resources.functions.RateService;

/**
 * Service to make changes to currencies.
 * 
 * @author Avinash
 *
 */
@Service
public class CurrencyService {

	private CurrencySymbolRepository currencySymbolRepository;
	private AuthenticationService authenticationService;
	private LineRepository lineRepository;
	private CompanyService companyService;
	private RecordService recordService;
	private StaffMemberService staffMemberService;
	private RateService rateService;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * @param currencySymbolRepository
	 * @param authenticationService
	 * @param lineRepository
	 */
	@Autowired
	public CurrencyService(CurrencySymbolRepository currencySymbolRepository,
			AuthenticationService authenticationService, LineRepository lineRepository,
			@Lazy CompanyService companyService, @Lazy RecordService recordService,
			@Lazy StaffMemberService staffMemberService, @Lazy RateService rateService) {
		this.currencySymbolRepository = currencySymbolRepository;
		this.authenticationService = authenticationService;
		this.lineRepository = lineRepository;
		this.companyService = companyService;
		this.recordService = recordService;
		this.staffMemberService = staffMemberService;
		this.rateService = rateService;
	}

	/**
	 * Method to save or update currency.
	 * 
	 * @param currencyDTO
	 * 
	 * @return currency wrapper object
	 */
	@Transactional
	public CurrencyDTO saveOrUpdateCurrency(CurrencyDTO currencyDTO) {
		logger.info("Inside CurrencyService :: saveOrUpdateCurrency() , To save currency " + currencyDTO);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to save/update currency.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		CurrencySymbol currency = null;
		if (currencyDTO.getId() == null) {
			logger.error("New currency can't be created");
			throw new UnprocessableEntityException("New currency can't be created");
		} else {
			currency = this.findCurrencyById(currencyDTO.getId());
		}
		final String code = currency.getCode();
		if (!StringUtils.isBlank(currencyDTO.getCode())) {
			CurrencySymbol savedCurrency = currencySymbolRepository.findByCodeIgnoreCase(currencyDTO.getCode());
			if (savedCurrency == null) {
				logger.error("Currency code(" + currencyDTO.getCode() + ") is invalid");
				throw new UnprocessableEntityException("Currency code(" + currencyDTO.getCode() + ") is invalid");
			} else if (currency.getId().longValue() != savedCurrency.getId().longValue()) {
				logger.info("Currency already exist with code " + currencyDTO.getCode());
				throw new UnprocessableEntityException("Currency already exist with code " + currencyDTO.getCode());
			}
		}

		if (!StringUtils.isBlank(currencyDTO.getCode())
				&& !StringUtils.equalsIgnoreCase(currency.getCode(), currencyDTO.getCode())) {
			logger.error("Currency code(" + currencyDTO.getCode() + ") can't be changed");
			throw new UnprocessableEntityException("Currency code(" + currencyDTO.getCode() + ") can't be changed");
		}

		if (!StringUtils.isBlank(currencyDTO.getSymbol())
				&& !StringUtils.equalsIgnoreCase(currency.getSymbol(), currencyDTO.getSymbol())) {
			logger.error("Currency symbol(" + currencyDTO.getSymbol() + ") can't be changed");
			throw new UnprocessableEntityException("Currency symbol(" + currencyDTO.getSymbol() + ") can't be changed");
		}

		if (currencyDTO.isDefaultCurrency() && !currencyDTO.isActive()) {
			logger.error("Default currency(" + code + ") can not be deactived");
			throw new UnprocessableEntityException("Default currency(" + code + ") can not be deactived");
		}

		if (currency.isDefaultCurrency() && !currencyDTO.isDefaultCurrency()) {
			logger.error("Default currency(" + code + ") can not be unset. Set any other currency to be default.");
			throw new UnprocessableEntityException(
					"Default currency(" + code + ") can not be unset. Set any other currency to be default.");
		}

		if (!currencyDTO.isActive()) {
			// Check if used in staff member.
			staffMemberService.findStaffMembers().stream().forEach(staffMemberDTO -> {
				staffMemberDTO.getResource().getFunctions().stream().forEach(resourceFunctionDTO -> {
					if (resourceFunctionDTO.getRates() != null && resourceFunctionDTO.getRates().stream()
							.anyMatch(rate -> StringUtils.equalsIgnoreCase(rate.getCurrency(), code))) {
						logger.error("This currency(" + code + ") can't be deactivated as it is used in personnel(s)");
						throw new UnprocessableEntityException(
								"This currency(" + code + ") can't be deactivated as it is used in personnel(s)");
					}
				});
			});

			// Check if used in booking line or order line.
			if (lineRepository.findByPersistentCurrencyIgnoreCase(code).size() != 0) {
				logger.error(
						"This currency(" + code + ") can't be deactivated as it is used in booking or order line(s)");
				throw new UnprocessableEntityException(
						"This currency(" + code + ") can't be deactivated as it is used in booking or order line(s)");
			}

			// Check if used in Rate.
			if (rateService.getAllRateByCurrencyCode(code).size() != 0) {
				logger.error("This currency(" + code + ") can't be deactivated as it is used in rate card(s)");
				throw new UnprocessableEntityException(
						"This currency(" + code + ") can't be deactivated as it is used in rate card(s)");
			}

			// Check if used in company, customer, salesentity, supplier.
			if (companyService.getAllCompanyByCurrencyCode(code).size() != 0) {
				logger.error("This currency(" + code + ") can't be deactivated as it is used in company(s)");
				throw new UnprocessableEntityException(
						"This currency(" + code + ") can't be deactivated as it is used in company(s)");
			}

			// Check if used in Records.
			if (recordService.getAllRecordByCurrencyCode(code).size() != 0) {
				logger.error("This currency(" + code + ") can't be deactivated as it is used in record(s)");
				throw new UnprocessableEntityException(
						"This currency(" + code + ") can't be deactivated as it is used in record(s)");
			}
		}
		if (currencyDTO.isDefaultCurrency()) {
			CurrencySymbol defaultCurrency = currencySymbolRepository.getDefaultCurrency();
			if (defaultCurrency != null) {
				defaultCurrency.setDefaultCurrency(false);
				currencySymbolRepository.save(defaultCurrency);
			}
		}
		currency = currencyDTO.getCurrency(currency);
		currency = currencySymbolRepository.save(currency);
		logger.info("Returning from saving currency() :: saveOrUpdateCurrency() ");
		return new CurrencyDTO(currency);
	}

	/**
	 * Method to validate currency
	 * 
	 * @param currencyDTO
	 */
	private void validateCurrency(CurrencyDTO currencyDTO) {
		logger.info(
				"Inside CurrencyService :: validateCurrency() , To validate currency wrapper object :" + currencyDTO);
		if (StringUtils.isBlank(currencyDTO.getCode())) {
			logger.info("Please provide valid currency code");
			throw new UnprocessableEntityException("Please provide valid currency code");
		}
		if (StringUtils.isBlank(currencyDTO.getSymbol())) {
			logger.info("Please provide valid currency symbol");
			throw new UnprocessableEntityException("Please provide valid currency symbol");
		}
		logger.info("Returning from  validate() ::");
	}

	/**
	 * To find Currency by Id
	 * 
	 * @param id
	 * 
	 * @return CurrencySymbol
	 */
	private CurrencySymbol findCurrencyById(long id) {
		logger.info("Inside CurrencyService :: findCurrencyById(), To find currency by id : " + id);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to find currency by id.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		CurrencySymbol currency = currencySymbolRepository.findOne(id);
		if (currency == null) {
			logger.info("Currency not found with id : " + id);
			throw new NotFoundException("Currency not found");
		}
		// To check if default currency present.
		getDefaultCurrency();
		logger.info("Returning from findCurrencyById()");
		return currency;
	}

	/**
	 * Method to get currency by id.
	 * 
	 * @param id
	 * 
	 * @return currency wrapper object
	 */
	public CurrencyDTO getCurrencyById(long id) {
		logger.info("Inside CurrencyService :: getCurrencyById(), id : " + id);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get currency by id.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		return new CurrencyDTO(this.findCurrencyById(id));
	}

	/**
	 * To find currency by code
	 * 
	 * @param code
	 * 
	 * @return currency object
	 */
	public CurrencySymbol findCurrencyByCode(String code) {
		logger.info("Inside CurrencyService :: findCurrencyByCode(), To find currency by currency-code : " + code);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to find currency by currency-code.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		// To check if default currency present.
		getDefaultCurrency();
		return currencySymbolRepository.findByCodeIgnoreCase(code);
	}

	/**
	 * To get currency symbol by code
	 * 
	 * @param code
	 * 
	 * @return symbol string
	 */
	public String getCurrencySymbolByCode(String code) {
		logger.info("Inside CurrencyService :: getCurrencySymbolByCode() , To get currency symbol by code : " + code);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get currency-symbol by currency-code.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		CurrencySymbol currency = this.findCurrencyByCode(code);
		logger.info("Returning from  getCurrencySymbolByCode() ::");
		return currency == null ? null : currency.getSymbol();
	}

	/**
	 * To get all currency.
	 * 
	 * @return list of all currency
	 */
	public List<CurrencyDTO> getAllCurrency() {
		logger.info("Inside CurrencyService :: getAllCurrency(), To get all currency");
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get currency-symbol by currency-code.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		// To check if default currency present.
		getDefaultCurrency();
		return currencySymbolRepository.getAllCurrency();
	}

	/**
	 * To get currency list by status.
	 * 
	 * @param active
	 * 
	 * @return list of currency by status
	 */
	public List<CurrencyDTO> getAllCurrencyByActive(boolean active) {
		logger.info(
				"Inside CurrencyService :: getAllCurrencyByActive(), To get all currency by active status : " + active);
		// To check if default currency present.
		getDefaultCurrency();
		return currencySymbolRepository.getAllCurrencyByActive(active);
	}

	/**
	 * To get currency-codes string list by status
	 * 
	 * @param active
	 * 
	 * @return list of currency-codes by status
	 */
	public List<String> getAllCurrencyCodeStringByActive(boolean active) {
		logger.info(
				"Inside CurrencyService :: getAllCurrencyCodeStringByActive(), To get all currency codes by active status : "
						+ active);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get all currency-codes by active status.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		// To check if default currency present.
		getDefaultCurrency();
		return currencySymbolRepository.getAllCurrencyCodeByActive(active);
	}

	/**
	 * Method to update currency-list
	 * 
	 * @param currencyDTOList
	 * 
	 * @return all currency list
	 */
	@Transactional
	public List<CurrencyDTO> updateCurrencyList(List<CurrencyDTO> currencyDTOList) {
		logger.info("Inside CurrencyService :: updateCurrencyList(), To update currency-list: ");
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to update currency-list.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		if (currencyDTOList == null || currencyDTOList.isEmpty()) {
			logger.info("Please provide valid data");
			throw new UnprocessableEntityException("Please provide valid data");
		}
		currencyDTOList.stream().forEach(currencyDTO -> {
			if (currencyDTO.getId() == null) {
				logger.error("Id should present in every currency.");
				throw new UnprocessableEntityException("Id should present in every currency.");
			}
			findCurrencyById(currencyDTO.getId());
			if (!currencyDTO.isActive()) {
				logger.error("Only active currencies should present.");
				throw new UnprocessableEntityException("Only active currencies should present.");
			}
		});
		List<CurrencyDTO> defaultCurrency = currencyDTOList.stream()
				.filter(currencyDTO -> currencyDTO.isDefaultCurrency()).collect(Collectors.toList());
		if (defaultCurrency.size() != 1) {
			logger.error("One currency should present as default currency");
			throw new UnprocessableEntityException("One currency should present as default currency");
		}
		// Putting default currency in first position.
		int indexOf = currencyDTOList.indexOf(defaultCurrency.get(0));
		currencyDTOList.set(indexOf, currencyDTOList.set(0, defaultCurrency.get(0)));

		List<CurrencyDTO> currencyToDisableList = new ArrayList<>();
		currencySymbolRepository
				.findByIdNotIn(
						currencyDTOList.stream().map(currencyDTO -> currencyDTO.getId()).collect(Collectors.toList()))
				.stream().forEach(currencySymbol -> {
					CurrencyDTO currencyDTO = new CurrencyDTO(currencySymbol);
					currencyDTO.setActive(false);
					currencyDTO.setDefaultCurrency(false);
					currencyToDisableList.add(currencyDTO);
				});
		currencyDTOList.stream().forEach(currencyDTO -> {
			saveOrUpdateCurrency(currencyDTO);
		});
		currencyToDisableList.stream().forEach(currencyDTO -> {
			saveOrUpdateCurrency(currencyDTO);
		});

		logger.info("Successfully updated currency-list");
		return currencySymbolRepository.getAllCurrency();
	}

	/**
	 * Method to get the default currency.
	 * 
	 * @return The default currencyDTO.
	 */
	public CurrencyDTO getDefaultCurrency() {
		logger.info("Inside CurrencyService :: getDefaultCurrency(), To get default currency");
		CurrencySymbol defaultCurrency = currencySymbolRepository.getDefaultCurrency();
		if (defaultCurrency == null) {
			logger.error("No default currency found");
			throw new UnprocessableEntityException("No default currency found");
		}
		logger.info("Returning from CurrencyService :: getDefaultCurrency()");
		return new CurrencyDTO(defaultCurrency);
	}
}
