package com.teamium.service.prod.resources.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamium.domain.Company;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.prod.resources.contacts.Customer;
import com.teamium.domain.prod.resources.functions.ExtraFunction;
import com.teamium.domain.prod.resources.functions.Function;
import com.teamium.domain.prod.resources.functions.Rate;
import com.teamium.domain.prod.resources.functions.RateCard;
import com.teamium.domain.prod.resources.functions.units.RateUnit;
import com.teamium.domain.prod.resources.suppliers.Supplier;
import com.teamium.dto.CompanyDTO;
import com.teamium.dto.CurrencyDTO;
import com.teamium.dto.FunctionDTO;
import com.teamium.dto.RateDropdownDTO;
import com.teamium.dto.prod.resources.functions.RateDTO;
import com.teamium.enums.RateUnitType;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.BookingRepository;
import com.teamium.repository.ExtraRepository;
import com.teamium.repository.FunctionRepository;
import com.teamium.repository.prod.resources.functions.RateRepository;
import com.teamium.service.CompanyService;
import com.teamium.service.CurrencyService;

/**
 * A service class implementation for Rate Controller
 *
 */
@Service
public class RateService {

	private RateRepository rateRepository;
	private CompanyService companyService;
	private FunctionService functionService;
	private FunctionRepository functionRepository;
	private BookingRepository bookingRepository;
	private ExtraRepository extraRepository;
	private CurrencyService currencyService;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * @param rateRepository
	 * @param companyService
	 * @param functionService
	 * @param functionRepository
	 * @param bookingRepository
	 * @param extraRepository
	 */
	public RateService(RateRepository rateRepository, CompanyService companyService, FunctionService functionService,
			FunctionRepository functionRepository, BookingRepository bookingRepository, ExtraRepository extraRepository,
			CurrencyService currencyService) {
		this.rateRepository = rateRepository;
		this.companyService = companyService;
		this.functionService = functionService;
		this.functionRepository = functionRepository;
		this.bookingRepository = bookingRepository;
		this.extraRepository = extraRepository;
		this.currencyService = currencyService;
	}

	/**
	 * To get list of rates
	 * 
	 * @return list of RateDTO
	 */
	public List<RateDTO> getRates() {
		logger.info("Inside getRates() .");
		List<RateDTO> rates = rateRepository.findAll().stream().map(r -> {
			RateDTO rateD = new RateDTO(r);
			rateD.setType(this.getRateType(r));
			return rateD;
		}).collect(Collectors.toList());
//		List<RateDTO> rates = rateRepository.getRates();
		logger.info(rates.size() + " rates found ");
		logger.info("Returning from getRates() .");
		return rates;
	}

	/**
	 * To get rate details.
	 * 
	 * @param rateIt the rateIt.
	 * 
	 * @return the RateDTO.
	 */
	public RateDTO getRate(long rateId) {
		logger.info("Inside getRate() : ");
		Rate rate = valideRate(rateId);
		RateDTO rateDto = new RateDTO(rate);
		rateDto.setType(this.getRateType(rate));
		logger.info("Returning from getRate() .");
		return rateDto;
	}

	/**
	 * To delete rate.
	 * 
	 * @param rateId the rateId.
	 */
	public void deleteRate(long rateId) {
		logger.info("Inside deleteRate() :: deleting rate with id : " + rateId);
		Rate rate = rateRepository.findOne(rateId);
		if (rate == null) {
			logger.error("Invalid rate id");
			throw new NotFoundException("Invalid rate id ");
		}
		if (bookingRepository.getLineCountByRate(rateId) > 0) {
			logger.error("Can not Delete this Rate :rate has assigned to line");
			throw new UnprocessableEntityException("Can not Delete this Rate :rate has assigned to line");
		}
		rateRepository.delete(rateId);
		logger.info("Returning from deleteRate() . ");
	}

	/**
	 * To save the rate.
	 * 
	 * @param supplierDTO the supplierDTO object.
	 * @return RateDTO
	 */
	@Transactional
	public RateDTO saveOrUpdateRate(RateDTO rateDTO) {
		logger.info("Inside saveOrUpdateSupplier saveOrUpdateRate() :: save/update : " + rateDTO);
		Rate rate;
		if (StringUtils.isBlank(rateDTO.getBasis())) {
			logger.info("Invalid rate basis");
			throw new UnprocessableEntityException("Invalid rate basis");
		}
		if (rateDTO.getEntity() == null || rateDTO.getEntity().getId() == null) {
			logger.info("Invalid company");
			throw new UnprocessableEntityException("Invalid company");
		}
		if (StringUtils.isBlank(rateDTO.getCurrency())) {
			rateDTO.setCurrency(currencyService.getDefaultCurrency().getCode());
		}
		/* Client or vendor rate card */
		if (rateDTO.getType() != null && (rateDTO.getType().equalsIgnoreCase(TeamiumConstants.RATE_TYPE_CLIENT)
				|| rateDTO.getType().equalsIgnoreCase(TeamiumConstants.RATE_TYPE_VENDOR))) {
			rate = validateCompanyRate(rateDTO);
			rate = rateRepository.save(rate);
			RateDTO savedRate = new RateDTO(rate);
			savedRate.setType(rateDTO.getType());
			return savedRate;
		}
		rate = rateDTO.getId() != null ? validateUpdate(rateDTO) : validateCreate(rateDTO);
//		if (rateDTO.getFunctionId() != null) {
//			Function function = functionService.getFunctionById(rateDTO.getFunctionId());
//			validateRateUnit(function, rate.getUnit());
//			rate.setFunction(function);
//		}
		rate = rateRepository.save(rate);
		if (rate.getExtra() == null) {
			List<ExtraFunction> ex = extraRepository.findByRate(rate);
			ex.forEach(e -> {
				extraRepository.delete(e);
			});
		}
		logger.info("Returning from saveOrUpdateRate() .");
		return new RateDTO(rate);
	}

	/**
	 * To save the company rate.
	 * 
	 * @param supplierDTO the supplierDTO object.
	 * @return RateDTO
	 */
	private Rate validateCompanyRate(RateDTO rateDTO) {
		logger.info("Inside saveCompanyRate saveOrUpdateRate() :: save/update : " + rateDTO);
		if (rateDTO.getCompany() == null || rateDTO.getCompany().getId() == null) {
			logger.error("Invalid client/vendor id");
			throw new NotFoundException("Invalid client/vendor id");
		}
		Long companyId = rateDTO.getCompany().getId();
		Long entityId = rateDTO.getEntity().getId();
		Company company = companyService.findCompanyById(companyId);
		Company entity = companyService.findCompany(Company.class, entityId);

		Rate rate;
		// updating rate card
		if (rateDTO.getId() != null) {
			logger.info("Updating company rate card");
			rate = valideRate(rateDTO.getId());
			if (rate.getCompany() != null) {
				if (!rate.getCompany().getId().equals(companyId)) {
					logger.error("Company can not be change");
					throw new UnprocessableEntityException("Company can not be change");
				}
			}
			rate = rateDTO.getRate(rate);
			return rate;
		} else

			// add new company rate
			logger.info("Adding new Company rate");
//		List<Rate> companyRates = rateRepository.findByCompanyIdAndUnit(companyId, rateDTO.getBasis());
//		if (companyRates.size() > 0) {
//			logger.error("Duplicate Rate basis for same company ");
//			throw new UnprocessableEntityException("Duplicate Rate basis for same company ");
//		}
		rate = rateDTO.getRateCard();
		// Add a default function to company
		// currently function is require to create a rate
		Function fun = null;
		if (rateDTO.getFunctionId() != null) {
			fun = functionService.getFunctionById(rateDTO.getFunctionId());
		}
		if (fun == null) {
			fun = new Function();
			fun = functionRepository.save(fun);
		}
		rate.setFunction(fun);
		rate.setCompany(company);
		rate.setEntity(entity);
		logger.info("Returning from saveCompanyRate() . ");
		return rate;

	}

	/**
	 * To validate RateDTO for creation
	 * 
	 * @param rateDTO
	 * @return Rate
	 */

	private Rate validateCreate(RateDTO rateDTO) {
		logger.info("Inside  validateCreate()  :: validating to create rate : " + rateDTO);
		Rate rate = rateDTO.getRate();
//		Company company = null;

//			if (rateDTO.getFunctionId() == null) {
//				FunctionDTO functionDto = new FunctionDTO();
//				functionDto.setBasis(rateDTO.getBasis());
//				functionDto.setValue(rateDTO.getLabel());
//				Vat v = new Vat();
//				v.setKey("0");
//				functionDto.setVat(v);
//				Function fun = functionService.saveOrUpdateFunction(TeamiumConstants.STAFF_FUNCTION_TYPE, functionDto);
//				rate.setFunction(fun);
//			}
//		}

		Function function = functionService.getFunctionById(rateDTO.getFunctionId());
		rate.setFunction(function);
		Company entity = companyService.findCompany(Company.class, rateDTO.getEntity().getId());
		rate.setEntity(entity);
//		if (rateDTO.getCompanyId() != null) {
//			Company company = companyService.findCompanyById(rateDTO.getCompanyId());
//			rate.setCompany(company);
//		}
//		validateRateUnit(function, rate.getUnit());
		logger.info("Returning from validateCreate() . ");
		return rate;
	}

	/**
	 * To validate RateDTO for updation
	 * 
	 * @param rateDTO
	 * @return Rate
	 */

	private Rate validateUpdate(RateDTO rateDTO) {
		logger.info("Inside  validateUpdate()  :: validating to update rate: " + rateDTO);
		Rate rate = valideRate(rateDTO.getId());
//		if (rateDTO.getCompanyId() != null) {
//			rate.setCompany(companyService.findCompanyById(rateDTO.getCompanyId()));
//		}
		// changing previously saved rate basis

		if (rateDTO.getFunctionId() != null && !rate.getFunction().getId().equals(rateDTO.getFunctionId())) {
			logger.error("Function can not be changed ");
			throw new NotFoundException("Function can not be changed");
		}
		if (rate.getUnit() != null && !rate.getUnit().getKey().equals(rateDTO.getBasis())) {
//			validateRateUnit(rate.getFunction(), rate.getUnit());
		}
		rate = rateDTO.getRate(rate);
		logger.info("Returning from  validateUpdate()  .");
		return rate;
	}

	/**
	 * To validate Rate existence.
	 * 
	 * @param rateId the rate-id.
	 * 
	 * @return the Rate object.
	 */
	private Rate valideRate(long rateId) {
		logger.info("Inside  valideRate()  :: validating rate with id : " + rateId);
		Rate rate = rateRepository.findOne(rateId);
		if (rate == null) {
			logger.error("Invalid rate id.");
			throw new NotFoundException("Invalid rate id.");
		}
		logger.info("Returning from  valideRate()  . ");
		return rate;
	}

	/**
	 * To get list of rates by companyId.
	 * 
	 * @param companyId the companyId.
	 * 
	 * @return list of RateDTO.
	 */
	public List<RateDTO> findRatesByCompany(long companyId) {
		logger.info("Inside  findRatesByCompany() :: finding rates associated with company id : " + companyId);
		List<RateDTO> rates = rateRepository.findByCompanyId(companyId);
		logger.info(rates.size() + " rates found ");
		logger.info("Returning from findRatesByCompany() .");
		return rates;
	}

	/**
	 * To get list of rates by functionId.
	 * 
	 * @param functionId the functionId.
	 * 
	 * @return list of RateDTO.
	 */
	public List<RateDTO> findRateByFunction(long functionId) {
		logger.info("Inside  findRateByFunction() :: finding rates associated with function id : " + functionId);
		List<RateDTO> rates = rateRepository.findByFunctionId(functionId);
		logger.info(rates.size() + " rates found ");
		logger.info("Returning from findRateByFunction() .");
		return rates;
	}

	/**
	 * To validate RateUnit.
	 * 
	 * @param Function the function.
	 * 
	 * @param RateUnit the unit.
	 * 
	 * @return the RateUnit object.
	 */
	private void validateRateUnit(Function function, RateUnit unit) {
		logger.info("Inside  validateRateUnit() :: validating rate unit  : " + unit);

//			List<Rate> savedRates = rateRepository.findRateByFunction(function.getId());
//			// if (savedRates.size() == 0) {
//			// return function.getDefaultUnit();
//			// }
//			savedRates.forEach(r -> {
//				if (r.getUnit() != null && r.getUnit().getKey().equals(unit.getKey())) {
//					logger.info("duplicate rate on" + unit.getKey() + " basis for functon " + function.getId());
//					throw new UnprocessableEntityException(
//							"duplicate rate on " + unit.getKey() + " basis for functon " + function.getId());
//				}
//			});

		if (rateRepository.countRateByFunctionAndUnit(function.getId(), unit.getKey()) > 0) {
			logger.info("duplicate rate on" + unit.getKey() + " basis for functon " + function.getId());
			throw new UnprocessableEntityException(
					"duplicate rate on " + unit.getKey() + " basis for functon " + function.getId());
		}

		logger.info("Returning from validateRateUnit() .");
	}

	/**
	 * To get list of rates by functionId and customerId.
	 * 
	 * @param functionId the functionId.
	 * 
	 * @param companyId  the companyId.
	 * 
	 * @return list of Rate.
	 */
	public List<Rate> findRatesByFunctionAndCompany(long functionId, long companyId) {
		logger.info("Inside  findRatesByFunctionAndCompany() :: finding rates associated with function and company : ");
		List<Rate> rates = rateRepository.findRateByFunctionAndCompany(functionId, companyId);
		logger.info(rates.size() + " rates found ");
		logger.info("Returning from findRatesByFunctionAndCompany() .");
		return rates;
	}

	/**
	 * To get rate drop down data.
	 * 
	 * @return
	 * 
	 * @return the RateDropdownDTO
	 */
	public RateDropdownDTO getRateDropdowns() {
		logger.info("inside getRateDropdowns() :");
		List<CurrencyDTO> currencies = currencyService.getAllCurrencyByActive(Boolean.TRUE);
		List<String> basis = RateUnitType.getRateUnitTypes();
		List<CompanyDTO> companies = companyService.getAllCompany(Company.class);
		List<CompanyDTO> clients = companyService.getAllCompany(Customer.class);
		List<CompanyDTO> suppliers = companyService.getAllCompany(Supplier.class);
		List<FunctionDTO> functions = functionService.getFunctions();
		RateDropdownDTO dropdown = new RateDropdownDTO(currencies, basis, companies, clients, suppliers, functions);
		logger.info("returning from getRateDropdowns() :");
		return dropdown;
	}

	/**
	 * To get rate Type.
	 * 
	 * @return the type of rate
	 */
	private String getRateType(Rate rate) {
		logger.info("inside getRateType() :");
		if (!(rate instanceof RateCard)) {
			return TeamiumConstants.RATE_TYPE_STANDARD;
		}
		if (rate.getCompany() != null) {
			if (rate.getCompany() instanceof Customer) {
				return TeamiumConstants.RATE_TYPE_CLIENT;
			}
			if (rate.getCompany() instanceof Supplier) {
				return TeamiumConstants.RATE_TYPE_VENDOR;
			}
		}
		logger.info("retuning from getRateType() :");
		return TeamiumConstants.RATE_TYPE_CLIENT_OR_VENDOR;
	}

	/**
	 * To get list of rates by saleEntityId.
	 * 
	 * @param companyId the saleEntityId.
	 * 
	 * @return list of RateDTO.
	 */
	public List<RateDTO> findRatesBySaleEntity(long saleEntityId) {
		logger.info(
				"Inside  findRatesBySaleEntity() :: finding rates associated with sale-entity id : " + saleEntityId);
		List<RateDTO> rates = rateRepository.findRateBySaleEntityId(saleEntityId);
		logger.info(rates.size() + " rates found ");
		logger.info("Returning from findRatesBySaleEntity().");
		return rates;
	}

	/**
	 * Find all Rate by the given currency code.
	 * 
	 * @param currencyCode
	 * @return list of rateDTO.
	 */
	public List<RateDTO> getAllRateByCurrencyCode(String currencyCode) {
		logger.info("Inside RateService :: getAllRateByCurrencyCode(" + currencyCode + ")");
		if (StringUtils.isBlank(currencyCode)) {
			logger.error("Currency code should not be blank.");
			throw new UnprocessableEntityException("Currency code should not be blank.");
		}
		List<RateDTO> rateDTOs = new ArrayList<>();
		rateRepository.findByCurrencyIgnoreCase(currencyCode).stream().forEach(rate -> rateDTOs.add(new RateDTO(rate)));
		logger.info("Returning from RateService :: getAllRateByCurrencyCode()");
		return rateDTOs;
	}
}
