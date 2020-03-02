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

import com.teamium.constants.Constants;
import com.teamium.domain.Channel;
import com.teamium.domain.Company;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.resources.SaleEntity;
import com.teamium.domain.prod.resources.contacts.Customer;
import com.teamium.domain.prod.resources.suppliers.Supplier;
import com.teamium.dto.ChannelDTO;
import com.teamium.dto.CompanyDTO;
import com.teamium.dto.CustomerDTO;
import com.teamium.dto.SaleEntityDTO;
import com.teamium.dto.SupplierDTO;
import com.teamium.dto.prod.resources.functions.RateDTO;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.CompanyRepository;
import com.teamium.service.prod.resources.functions.RateService;

/**
 * A service class implementation for company controller
 *
 */

@Service
public class CompanyService {

	private CompanyRepository companyRepository;
	private AuthenticationService authenticationService;

	@Autowired
	@Lazy
	private RecordService recordService;

	@Autowired
	@Lazy
	private RateService rateService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public CompanyService(CompanyRepository companyRepository, AuthenticationService authenticationService,
			FormatService formatService) {
		this.companyRepository = companyRepository;
		this.authenticationService = authenticationService;

	}

	/**
	 * To get list of companies
	 * 
	 * @return list of CompanyDTO
	 */
	public List<CompanyDTO> getCompanies() {
		logger.info("Inside getAllcompaniesDTO().");
		List<CompanyDTO> companies = companyRepository.findCompanies();

		logger.info(companies.size() + " Companies found ");
		logger.info("Returning from getAllcompaniesDTO()");
		return companies;
	}

	/**
	 * To get company by companyId .
	 * 
	 * @param companyId the companyId.
	 * 
	 * @return the CompanyDTO.
	 */
	public CompanyDTO getCompany(Long companyId) {
		logger.info("Inside getCompany() :: getting company with id: " + companyId);
		CompanyDTO companyDto = new CompanyDTO(findCompanyById(companyId));
		logger.info("Returning from getCompany() .");
		return companyDto;
	}

	/**
	 * To find company by id.
	 * 
	 * @param companyId the companyId.
	 * 
	 * @return the Company.
	 */
	public Company findCompanyById(Long companyId) {
		logger.info("Inside  findCompanyById()  :: finding company with id " + companyId);
		if (companyId == null) {
			logger.error("Company id is null");
			throw new UnprocessableEntityException("Company id can not be null");
		}
		Company company = companyRepository.findOne(companyId);
		if (company == null) {
			logger.error("Invalid company id.");
			throw new NotFoundException("Invalid company id.");
		}
		logger.info("Returning from findCompanyById() .");
		return company;
	}

	/**
	 * To delete company.
	 * 
	 * @param companyId the companyId.
	 */
	public void deleteCompany(Long companyId) {
		logger.info("Inside deleteCompany() :: deleting company with id: " + companyId);
		this.findCompanyById(companyId);

		// check if company is applied on any project or not
		List<Record> recordsByCompany = recordService.getRecordByCompany(companyId);
		if (recordsByCompany != null && !recordsByCompany.isEmpty()) {
			logger.info("Cannot delete company as it has been assigned on record");
			throw new UnprocessableEntityException("Cannot delete company as it has been assigned on record");
		}

		List<RateDTO> rateByCompany = rateService.findRatesByCompany(companyId);
		if (rateByCompany != null && !rateByCompany.isEmpty()) {
			logger.info("Cannot delete company as it has been assigned on rate-card");
			throw new UnprocessableEntityException("Cannot delete company as it has been assigned on rate-card");
		}

		List<RateDTO> rateBySaleEntity = rateService.findRatesBySaleEntity(companyId);
		if (rateBySaleEntity != null && !rateBySaleEntity.isEmpty()) {
			logger.info("Cannot delete company as it has been assigned on rate-card");
			throw new UnprocessableEntityException("Cannot delete company as it has been assigned on rate-card");
		}
		companyRepository.delete(companyId);
		logger.info("Returning from deleteCompany() .");
	}

	/**
	 * To save the company.
	 * 
	 * @param companyDTO the companyDTO object.
	 * @return
	 */
	public CompanyDTO saveOrUpdateCompany(CompanyDTO companyDTO) {
		logger.info("Inside saveOrUpdateCompany() :: save/update company : " + companyDTO);
		Company company;
		if (companyDTO.getId() == null) {
			company = companyDTO.getCompanyDetails(new Company());
		} else {
			company = companyRepository.getOne(companyDTO.getId());
			if (company == null) {
				logger.error("Invalid company id : " + companyDTO.getId());
				throw new NotFoundException("Invalid company id : " + companyDTO.getId());
			}
			company = companyDTO.getCompanyDetails(company);
		}
		company = companyRepository.save(company);
		logger.info("Returning from saveOrUpdateCompany() .");
		return new CompanyDTO(company);
	}

	/**
	 * Method to get Companies
	 * 
	 * @param discriminator the discriminator
	 * 
	 * @return list of Companies
	 */
	public List<CompanyDTO> getAllCompany(Class<?> discriminator) {
		logger.info("Inside ChannelService :: getAllChannels() :");
		List<CompanyDTO> companies = companyRepository.getCompanies(discriminator).stream()
				.map(channel -> new CompanyDTO(channel)).collect(Collectors.toList());
		logger.info("Returning after getting all channels");
		return companies;
	}

	/**
	 * Method to get company by id and discriminator
	 * 
	 * @param discriminator
	 * @param companyId
	 * @return company object
	 */
	public CompanyDTO findCompanyDTOById(Class<?> discriminator, long companyId) {
		logger.info("Inside CompanyService :: findCompanyById() , To find company by id : " + companyId);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to find company by id.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		Company company = companyRepository.getCompanyById(discriminator, companyId);

		logger.info("Returning after finding company by id.");

		if (discriminator.equals(Company.class)) {
			if (company == null) {
				throw new NotFoundException("Company not found.");
			}
			CompanyDTO response = new CompanyDTO(company);
			return response;

		} else if (discriminator.equals(Channel.class)) {
			if (company == null) {
				throw new NotFoundException("Channel not found.");
			}
			ChannelDTO response = new ChannelDTO(company, "forChannel");
			return response;

		} else if (discriminator.equals(Customer.class)) {
			if (company == null) {
				throw new NotFoundException("Customer not found.");
			}
			CustomerDTO response = new CustomerDTO((Customer) company);
			return response;

		} else if (discriminator.equals(SaleEntity.class)) {
			if (company == null) {
				throw new NotFoundException("Sale entity not found.");
			}
			SaleEntityDTO response = new SaleEntityDTO((SaleEntity) company);
			return response;

		} else if (discriminator.equals(Supplier.class)) {
			if (company == null) {
				throw new NotFoundException("Supplier not found.");
			}
			SupplierDTO response = new SupplierDTO((Supplier) company);
			return response;
		}
		return null;
	}

	/**
	 * To get company
	 * 
	 * @param companyId
	 * @param discriminator
	 * 
	 * @return company object
	 */
	public Company findCompany(Class<?> discriminator, Long companyId) {
		logger.info("Inside CompanyService :: findCompany()  :: finding company with id " + companyId);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to find company by id.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		if (companyId == null) {
			logger.error("Company id is null");
			throw new NotFoundException("Company id can not be null");
		}
		Company company = companyRepository.getCompanyById(discriminator, companyId);
		if (company == null) {
			logger.error("Invalid company id.");
			throw new NotFoundException("Invalid company id.");
		}
		logger.info("Returning from findCompany().");
		return company;
	}

	/**
	 * Method to get companies by discriminator
	 * 
	 * @param discriminator
	 * 
	 * @return list of all companies by discriminator
	 */
	public List<Company> getAllCompanyByDiscriminator(Class<?> discriminator) {
		logger.info("Inside ChannelService :: getAllCompanyByDiscriminator() : discriminator : " + discriminator);
		List<Company> companies = companyRepository.getCompanies(discriminator);
		logger.info("Returning after getting all companies by discriminator");
		return companies;
	}

	/**
	 * Find all companyDTO by the given currency code.
	 * 
	 * @param currencyCode
	 * @return List of companyDTO.
	 */
	public List<CompanyDTO> getAllCompanyByCurrencyCode(String currencyCode) {
		logger.info("Inside CompanyService :: getAllCompanyByCurrencyCode(" + currencyCode + ")");
		if (StringUtils.isBlank(currencyCode)) {
			logger.error("Currency code should not be blank.");
			throw new UnprocessableEntityException("Currency code should not be blank.");
		}
		List<CompanyDTO> companyDTOs = new ArrayList<>();
		companyRepository.findByCurrencyIgnoreCase(currencyCode).stream()
				.forEach(company -> companyDTOs.add(new CompanyDTO(company)));
		logger.info("Returning from CompanyService :: getAllCompanyByCurrencyCode()");
		return companyDTOs;
	}

}
