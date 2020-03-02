package com.teamium.service.prod.resources.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.teamium.domain.FunctionKeyword;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.prod.resources.equipments.EquipmentFunction;
import com.teamium.domain.prod.resources.functions.DefaultResource;
import com.teamium.domain.prod.resources.functions.Function;
import com.teamium.domain.prod.resources.functions.ProcessFunction;
import com.teamium.domain.prod.resources.functions.RatedFunction;
import com.teamium.domain.prod.resources.functions.RightFunction;
import com.teamium.domain.prod.resources.staff.StaffFunction;
import com.teamium.domain.prod.resources.suppliers.SupplyFunction;
import com.teamium.dto.FunctionDTO;
import com.teamium.dto.FunctionDropDownDTO;
import com.teamium.enums.AccountancyType;
import com.teamium.enums.FunctionContractType;
import com.teamium.enums.RateUnitType;
import com.teamium.enums.AssignationType.AssignationType;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.EquipmentFunctionRepository;
import com.teamium.repository.FunctionKeywordRepository;
import com.teamium.repository.FunctionRepository;
import com.teamium.repository.LineRepository;
import com.teamium.repository.ProcessFunctionRepository;
import com.teamium.repository.ResourceFunctionRepository;
import com.teamium.repository.RightFunctionRepository;
import com.teamium.repository.StaffFunctionRepository;
import com.teamium.repository.SupplyFunctionRepository;
import com.teamium.service.ResourceService;
import com.teamium.utils.FunctionUtil;

/**
 * A service class implementation for Function Controller
 *
 */
@Service
public class FunctionService {

	/**
	 * Owned Repository
	 */
	private FunctionKeywordRepository functionKeywordRepository;
	private FunctionRepository functionRepository;
	private StaffFunctionRepository staffFunctionRepository;
	private EquipmentFunctionRepository equipmentFunctionRepository;
	private RightFunctionRepository rightFunctionRepository;
	private SupplyFunctionRepository supplyFunctionRepository;
	private ProcessFunctionRepository processFunctionRepository;
	private ResourceFunctionRepository resourceFunctionRepository;
	private LineRepository lineRepositor;

	@Autowired
	@Lazy
	private ResourceService resourceService;

	@Autowired
	@Lazy
	private RateService rateService;

	/**
	 * logger
	 */
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	public FunctionService(FunctionRepository functionRepository, StaffFunctionRepository staffFunctionRepository,
			EquipmentFunctionRepository equipmentFunctionRepository, RightFunctionRepository rightFunctionRepository,
			SupplyFunctionRepository supplyFunctionRepository, ProcessFunctionRepository processFunctionRepository,
			FunctionKeywordRepository functionKeywordRepository, ResourceFunctionRepository resourceFunctionRepository,
			LineRepository lineRepositor) {
		this.functionRepository = functionRepository;
		this.staffFunctionRepository = staffFunctionRepository;
		this.equipmentFunctionRepository = equipmentFunctionRepository;
		this.rightFunctionRepository = rightFunctionRepository;
		this.supplyFunctionRepository = supplyFunctionRepository;
		this.processFunctionRepository = processFunctionRepository;
		this.functionKeywordRepository = functionKeywordRepository;
		this.resourceFunctionRepository = resourceFunctionRepository;
		this.lineRepositor = lineRepositor;
	}

	/**
	 * To save and update Function.
	 * 
	 * @param functionDTO the FunctionDTO object.
	 */
	public FunctionDTO saveOrUpdateFunctionAndFolder(FunctionDTO functionDTO) {
		logger.info("Inside saveOrUpdateFunctionAndFolder() :: save/update:  " + functionDTO);
		String functionType = functionDTO.getType();
		if (StringUtils.isBlank(functionType)) {
			logger.error("Please enter a valid function type.");
			throw new UnprocessableEntityException("Please enter a valid function type.");
		}

//		Accent word allowed in function name.
		if (StringUtils.isBlank(functionDTO.getValue())
				|| !functionDTO.getValue().matches("^(?=.{1,30}$)[a-zA-ZÀ-ŸØ-öø-ÿ]+(\\s[a-zA-ZÀ-ŸØ-öø-ÿ]{1,})*$")) {
			logger.info("Please provide valid function name.");
			throw new UnprocessableEntityException("Please provide valid function name.");
		}

		// save folder
		if (functionType.equalsIgnoreCase(TeamiumConstants.FOLDER_FUNCTION_TYPE)) {
			logger.info("Saving folder :: " + functionDTO);
			Function folder = validateDefaultFunction(functionDTO);

			Function validateFunctionName = functionRepository.findByValueIgnoreCase(functionDTO.getValue());
			if (folder.getId() == null && validateFunctionName != null) {
				logger.info("Function/Folder name already exists.");
				throw new UnprocessableEntityException("Function/Folder name already exists.");
			}
			if (folder.getId() != null && validateFunctionName != null
					&& folder.getId().longValue() != validateFunctionName.getId().longValue()) {
				logger.info("Function/Folder name already exists.");
				throw new UnprocessableEntityException("Function/Folder name already exists.");
			}

			folder.setFolder(true);
			folder.setParent(validatedParent(functionDTO));
			folder = functionRepository.save(folder);
			FunctionDTO savedFunction = new FunctionDTO(folder);
			logger.info("Folder saved successfully .");
			return savedFunction;
		}

		// for a function, not for folder
		RatedFunction function = this.saveOrUpdateFunction(functionType, functionDTO);
		FunctionDTO savedFunction = new FunctionDTO(function);
		if (function.getDefaultResource() == null) {
			DefaultResource resource = new DefaultResource();
			resource.setName(function.getValue());

			resource.setFunction(function);
			resource = resourceService.addDefaultResource(resource);

		}
		logger.info("Returning from saveOrUpdate() .");
		return savedFunction;
	}

	/**
	 * To validate the Default Function
	 * 
	 * @param functionDTO
	 * @return RightFunction
	 */
	private Function validateDefaultFunction(FunctionDTO functionDTO) {
		logger.info("Inside validateDefaultFunction() :: validating function:  " + functionDTO);
		if (functionDTO.getId() == null) {
			return functionDTO.getFunction();
		}
		Function function = validateFunction(functionDTO.getId());

		function = functionDTO.getFunction(function);
		FunctionKeyword funKeyword = functionDTO.getFunctionKeyword();
		if (funKeyword != null && funKeyword.getId() != null) {
			function.setFunctionKeyword(functionKeywordRepository.findOne(funKeyword.getId()));
		}
		logger.info("Returning from validateDefaultFunction() .");
		return function;

	}

	/**
	 * To validate the Right Function
	 * 
	 * @param functionDTO
	 * @return RightFunction
	 */
	private RightFunction validateRightFunction(FunctionDTO functionDTO) {
		logger.info("Inside validateRightFunction() :: validating right function:  " + functionDTO);
		if (functionDTO.getId() == null) {
			return functionDTO.getRightFunction();
		}
		RightFunction rightFunction = rightFunctionRepository.findOne(functionDTO.getId());
		if (rightFunction == null) {
			logger.error("Function not found ");
			throw new NotFoundException("Function not found");
		}
		rightFunction = functionDTO.getRightFunction(rightFunction);
		FunctionKeyword funKeyword = functionDTO.getFunctionKeyword();
		if (funKeyword != null) {
			rightFunction.setFunctionKeyword(functionKeywordRepository.findOne(funKeyword.getId()));
		}
		logger.info("Returning from validateRightFunction() .");
		return rightFunction;
	}

	/**
	 * To validate The Supply Function
	 * 
	 * @param functionDTO
	 * @return SupplyFunction
	 */
	private SupplyFunction validateSupplyFunction(FunctionDTO functionDTO) {
		logger.info("Inside validateSupplyFunction() :: validating supply function:  " + functionDTO);
		if (functionDTO.getId() == null) {
			return functionDTO.getSupplyFunction();
		}
		SupplyFunction supplyFunction = supplyFunctionRepository.findOne(functionDTO.getId());
		if (supplyFunction == null) {
			logger.error("Function not found ");
			throw new NotFoundException("Function not found");
		}
		supplyFunction = functionDTO.getSupplyFunction(supplyFunction);
		FunctionKeyword funKeyword = functionDTO.getFunctionKeyword();
		if (funKeyword != null) {
			supplyFunction.setFunctionKeyword(functionKeywordRepository.findOne(funKeyword.getId()));
		}
		logger.info("Returning from validateSupplyFunction() .");
		return supplyFunction;
	}

	/**
	 * To validate the Equipment Function
	 * 
	 * @param functionDTO
	 * @return EquipmentFunction
	 */
	private EquipmentFunction validateEquipmentFunction(FunctionDTO functionDTO) {
		logger.info("Inside validateEquipmentFunction() :: validating equipment function:  " + functionDTO);
		if (functionDTO.getId() == null) {
			return functionDTO.getEquipmnetFunction();
		}
		EquipmentFunction equipmentFunction = equipmentFunctionRepository.findOne(functionDTO.getId());
		if (equipmentFunction == null) {
			logger.error("Function not found ");
			throw new NotFoundException("Function not found");
		}
		equipmentFunction = functionDTO.getEquipmnetFunction(equipmentFunction);
		FunctionKeyword funKeyword = functionDTO.getFunctionKeyword();
		if (funKeyword != null) {
			equipmentFunction.setFunctionKeyword(functionKeywordRepository.findOne(funKeyword.getId()));
		}
		logger.info("Returning from validateEquipmentFunction() .");
		return equipmentFunction;
	}

	/**
	 * To validate the Staff Function
	 * 
	 * @param functionDTO
	 * @return StaffFunction
	 */
	private StaffFunction validateStaffFunction(FunctionDTO functionDTO) {
		logger.info("Inside validateStaffFunction() :: validating staff function:  " + functionDTO);
		if (functionDTO.getId() == null) {
			return functionDTO.getStaffFunction();
		}
		StaffFunction staffFunction = staffFunctionRepository.findOne(functionDTO.getId());
		if (staffFunction == null) {
			logger.error("Function not found ");
			throw new NotFoundException("Function not found");
		}
		staffFunction = functionDTO.getStaffFunction(staffFunction);
		FunctionKeyword funKeyword = functionDTO.getFunctionKeyword();
		if (funKeyword != null) {
			staffFunction.setFunctionKeyword(functionKeywordRepository.findOne(funKeyword.getId()));
		}
		logger.info("Returning from validateStaffFunction() .");
		return staffFunction;
	}

	/**
	 * To validate the Process Function
	 * 
	 * @param functionDTO
	 * @return RatedFunction
	 */
	private ProcessFunction validateProcessFunction(FunctionDTO functionDTO) {
		logger.info("Inside validateProcessFunction() :: validating staff function:  " + functionDTO);
		if (functionDTO.getId() == null) {
			return functionDTO.getProcessFunction();
		}
		ProcessFunction processFunction = processFunctionRepository.findOne(functionDTO.getId());
		if (processFunction == null) {
			logger.error("Function not found ");
			throw new NotFoundException("Function not found");
		}
		processFunction = functionDTO.getProcessFunction(processFunction);
		logger.info("Returning from validateProcessFunction() .");
		return processFunction;
	}

	/**
	 * To get all functions.
	 * 
	 * @return FunctionDTO list
	 */
	public List<FunctionDTO> getFunctions() {
		logger.info("Inside getFunctions() :");
		List<FunctionDTO> functions = functionRepository.getFunctions();
		logger.info(functions.size() + " functions found ");
		logger.info("Returning from getFunctions() .");
		return functions;

	}

	/**
	 * To delete function .
	 * 
	 * @param functionId the functionId.
	 */
	public void deleteFunction(Long functionId) {
		logger.info("Inside deleteFunction() :: deleting function with id " + functionId);
		Function function = validateFunction(functionId);
		if (isFunctionAssigned(functionId)) {
			logger.error("Can not delete the Function :: Function is used to resource ");
			throw new UnprocessableEntityException(
					"Can not delete the Function :: Function is used to resource or booking.");
		}
		deleteChildFunctions(function);
		functionRepository.delete(functionId);
		logger.info("Function has deleted successfully ");
		logger.info("Returning from deleteFunction() .");
	}

	/**
	 * To delete all child functions of a function.
	 * 
	 * @param function the function.
	 */
	private void deleteChildFunctions(Function function) {
		logger.info("Inside deleteChildFunctions() :: deleting child  functions for function : " + function);
		List<Function> childFunctions = findChildFunctions(function);
		if (childFunctions != null && childFunctions.size() > 0) {
			childFunctions.forEach(fun -> {
				if (isFunctionAssigned(fun.getId())) {
					logger.error("Can not delete the Function :: Function is used to resource ");
					throw new UnprocessableEntityException(
							"Can not delete the Function : Function is assigned to resource or booking.");
				}
				deleteChildFunctions(fun);
				logger.info("deleting function " + fun + " by parent ");
				functionRepository.delete(fun);

			});
		}
		logger.info("Returning from deleteChildFunctions() .");
	}

	/**
	 * To find Function by functionId
	 * 
	 * @param functionId the functionId
	 * 
	 * @return the FunctionDTO
	 */
	public FunctionDTO findFunction(Long functionId) {
		logger.info("inside findFunction() :: finding function with id: " + functionId);
		Function function = validateFunction(functionId);
		FunctionDTO functionDTO = new FunctionDTO(function);
		logger.info("Returning from findFunction() .");
		return functionDTO;
	}

	/**
	 * To Validate Function by functionId
	 * 
	 * @param functionId the functionId
	 * 
	 * @return the Function
	 */
	public Function validateFunction(Long functionId) {
		logger.info("inside validateFunction() :: validating function with id: " + functionId);
		if (functionId == null) {
			logger.error("function id is null");
			throw new NotFoundException("function id can not be null");
		}
		Function function = functionRepository.findOne(functionId);
		if (function == null) {
			logger.error("Function not found ");
			throw new NotFoundException("Function not found");
		}
		logger.info("Returning from validateFunction() .");
		return function;
	}

	/**
	 * To get root function list with their sub function
	 * 
	 * @return the FunctionDTO list
	 */
	public List<FunctionDTO> getRootFunctions() {
		logger.info("inside getRootFunctions() : ");
		List<Function> functions = functionRepository.findAll();
		List<Function> rootFunctions = functions.stream().filter(f -> f.getParent() == null)
				.collect(Collectors.toList());
		rootFunctions.forEach(fun -> {
			fun.setChildFunctions(findChildFunctions(fun));
		});
		List<FunctionDTO> functionsDTO = rootFunctions.stream().filter(f -> f != null).map(f -> new FunctionDTO(f))
				.collect(Collectors.toList());
		logger.info("Returning from getRootFunctions() .");
		return functionsDTO;
	}

	/**
	 * To find child functions associated with a function
	 * 
	 * @param function the function
	 * 
	 * @return the function list
	 */
	public List<Function> findChildFunctions(Function function) {
		logger.info("inside getRootFunctions() : ");
		if (function == null) {
			return null;
		}
		List<Function> childFunctions = functionRepository.getChildFunctions(function.getId());
		childFunctions.forEach(f -> {
			f.setChildFunctions(findChildFunctions(f));
		});
		function.setChildFunctions(childFunctions);
		logger.info("Returning from getRootFunctions() .");
		return childFunctions;
	}

	/**
	 * To validate a parent function
	 * 
	 * @param functionDTO the functionDTO
	 * 
	 * @return the function
	 */
	private Function validatedParent(FunctionDTO functionDTO) {
		logger.info("inside validatedParent() :: validating function :  " + functionDTO);
		if (functionDTO.getParent() != null && functionDTO.getParent().getId() != null) {

			Function parentFunction = functionRepository.findOne(functionDTO.getParent().getId());
			if (!parentFunction.isFolder()) {
				logger.error(" Parent should be a folder .");
				throw new UnprocessableEntityException(" Parent should be a folder .");
			}
			if (!functionDTO.getType().equalsIgnoreCase(TeamiumConstants.FOLDER_FUNCTION_TYPE)) {
				return parentFunction;
			}
			if (functionDTO.getId() != null && functionDTO.getId().equals(functionDTO.getParent().getId())) {
				logger.error("Function can not be parent of itself.");
				throw new UnprocessableEntityException("Function can not be parent of itself.");
			}
			if (getFunctionLevels(0, parentFunction) >= TeamiumConstants.FUNCTION_FOLDER_MAX_LEVEL) {
				logger.error("Maximum level of parent function has exceeded");
				throw new UnprocessableEntityException("Maximum level of parent function has exceeded .");
			}
			if (isCircularParent(parentFunction, functionDTO.getId())) {
				logger.error("can not create circular function .");
				throw new UnprocessableEntityException("can not create circular function .");
			}
			logger.info("Returning from validatedParent() .");
			return parentFunction;
		}
		return null;

	}

	/**
	 * To check circular parent for function
	 * 
	 * @param parentFunction
	 * @param functionId
	 * @return true if circular
	 */

	private boolean isCircularParent(Function parentFunction, Long functionId) {
		if (parentFunction == null) {
			return false;
		}
		if (parentFunction.getId().equals(functionId)) {
			return true;
		}
		return isCircularParent(parentFunction.getParent(), functionId);
	}

	/**
	 * To count levels of parent function
	 * 
	 * @param startLevel the startLevel
	 * 
	 * @param function   the function
	 * 
	 * @return levels
	 */
	private int getFunctionLevels(int startLevel, Function function) {
		logger.info("inside getFunctionLevels() :: currentLeve " + startLevel + " : function " + function);
		if (function == null) {
			return startLevel;
		}
		if (function.getParent() == null) {
			return ++startLevel;
		}
		logger.info("Returning from getFunctionLevels() .");
		return getFunctionLevels(startLevel, function.getParent()) + 1;
	}

	/**
	 * Find function by id.
	 * 
	 * @param functionId
	 * @return
	 */
	public Function getFunctionById(Long functionId) {
		logger.info("inside getFunctionById() :: finding function with id: " + functionId);
		if (functionId == null) {
			logger.error("function id is null");
			throw new NotFoundException("function id can not be null");
		}
		Function function = functionRepository.findOne(functionId);
		if (function == null) {
			logger.error("Function not found ");
			throw new NotFoundException("Function not found");
		}
		return function;
	}

	/**
	 * To get function by discriminator and functionId
	 * 
	 * @param functionId    the functionId
	 * 
	 * @param discriminator the discriminator
	 * 
	 * @return Function object
	 */
	public Function getFunctionByIdAndDiscriminator(Class<?> discriminator, Long functionId) {
		logger.info("Inside getFunctionByIdAndDiscriminator() :: finding function with id: " + functionId
				+ "and discriminator : " + discriminator);
		if (functionId == null) {
			logger.error("Function id is null");
			throw new NotFoundException("Function id can not be null");
		}
		Function function = functionRepository.getFunctionByIdAndDiscriminator(discriminator, functionId);
		if (function == null) {
			logger.error("Function not found ");
			throw new NotFoundException("Function not found");
		}
		return function;
	}

	/**
	 * To get Functions by discriminator
	 * 
	 * @param discriminator the discriminator
	 * 
	 * @return lit of functions
	 */
	public List<Function> getFunctionsByDiscrimination(Class<?> discriminator) {
		logger.info("Inside getFunctionsByDiscrimination() :: finding function with discriminator : " + discriminator);
		return functionRepository.getFunctionByAndDiscriminator(discriminator);
	}

	/**
	 * To get FunctionDTOs by discriminator
	 * 
	 * @param discriminator the discriminator
	 * 
	 * @return list of FunctionsDTO
	 */
	public List<FunctionDTO> getFunctionDTOsByDiscrimination(Class<?> discriminator) {
		logger.info("Inside getFunctionsByDiscrimination() :: finding function with discriminator : " + discriminator);
		return functionRepository.getFunctionDTOsByDiscrimination(discriminator);
	}

	/**
	 * To get get all rated staff functions
	 * 
	 * @return list of RatedFunctionDTO
	 */
	public List<FunctionDTO> getRatedStaffFunctions() {
		logger.info("Inside getRatedStaffFunctions() :: finding rated staff function: ");
		List<FunctionDTO> staffFunctions = functionRepository.getFunctionDTOsByDiscrimination(StaffFunction.class);
		List<FunctionDTO> functionDtos = staffFunctions.stream().map(fun -> {
			fun.setDefaultResource(null);
			fun.setRates(rateService.findRateByFunction(fun.getId()));
			return fun;
		}).collect(Collectors.toList());
		logger.info(functionDtos.size() + " rated staff found ");
		logger.info("Returning from getRatedStaffFunctions() .");
		return functionDtos;
	}

	/**
	 * To get list of accounting code
	 * 
	 * @return list of accounting code
	 */
	public List<String> getAccountingCodes() {
		logger.info("inside getAccountingCodes() :");
		return AccountancyType.getAccountancyTypes();
	}

	/**
	 * To get function drop down data.
	 * 
	 * @return the FunctionDropDownDTO
	 */
	public FunctionDropDownDTO getFunctionDropdown() {
		logger.info("inside getFunctionDropdown() :");
		List<String> functionTypes = getFunctionTypes();
		List<String> basis = RateUnitType.getRateUnitTypes();
		List<String> origines = AssignationType.geAssignationTypes();
		List<String> contracts = FunctionContractType.geAssignationTypes();
		List<String> accountingCodes = getAccountingCodes();
		List<String> keywords = functionKeywordRepository.findAll().stream().map(k -> k.getKeyword()).sorted()
				.collect(Collectors.toList());
		FunctionDropDownDTO dropdown = new FunctionDropDownDTO(functionTypes, basis, origines, contracts,
				accountingCodes);
		dropdown.setKeywords(keywords);
		logger.info("Returning from getFunctionDropdown() .");
		return dropdown;
	}

	/**
	 * To get list of functions types
	 * 
	 * @return list of functions types
	 */
	private List<String> getFunctionTypes() {
		logger.info("inside getFunctionTypes() :");
		List<String> functionTypes = new ArrayList<String>();
		functionTypes.add(TeamiumConstants.STAFF_FUNCTION_TYPE);
		functionTypes.add(TeamiumConstants.EQUIPMENT_FUNCTION_TYPE);
		functionTypes.add(TeamiumConstants.SUPPLY_FUNCTION_TYPE);
		functionTypes.add(TeamiumConstants.PROCESS_FUNCTION_TYPE);
		functionTypes.add(TeamiumConstants.RIGHT_FUNCTION_TYPE);
		functionTypes = functionTypes.stream().sorted().collect(Collectors.toList());
		logger.info("Returning from getFunctionTypes() .");
		return functionTypes;
	}

	/**
	 * To check if function assigned to any resource
	 * 
	 * @return true if function assigned
	 */
	private boolean isFunctionAssigned(long functionId) {
		boolean isAssigned = resourceFunctionRepository.getAssignedFunction(functionId).size() > 1;
		if (isAssigned) {
			return isAssigned;
		}
		return lineRepositor.getLinesRelatedToFunction(functionId).size() > 0;
	}

	/**
	 * To save and update Function.
	 * 
	 * @param functionDTO the FunctionDTO object.
	 */
	public RatedFunction saveOrUpdateFunction(String functionType, FunctionDTO functionDTO) {
		logger.info("Inside saveOrUpdateFunction() :: save/update:  " + functionDTO);
		String oldType = null;
		if (functionDTO.getId() != null) {
			Function function = validateFunction(functionDTO.getId());
			oldType = FunctionUtil.getFunctionType(function);
			// can not update function type if it is folder or assigned to any resource
			if (isFunctionAssigned(function.getId()) && !functionType.equalsIgnoreCase(oldType)) {
				logger.error("Can not change this function type. ");
				throw new UnprocessableEntityException("Can not change this function type. ");
			}
			// can not change the folder type
			if (oldType.equalsIgnoreCase(TeamiumConstants.FOLDER_FUNCTION_TYPE)
					&& !functionType.equalsIgnoreCase(oldType)) {
				logger.error("Can not change this folder type. ");
				throw new UnprocessableEntityException("Can not change this folder type. ");
			}

			// validate if name(value) of function already exists or not
			Function validateFunctionName = functionRepository.findByValueIgnoreCase(functionDTO.getValue());
			if (function.getId() != null && validateFunctionName != null
					&& function.getId().longValue() != validateFunctionName.getId().longValue()) {
				logger.info("Function/Folder name already exists.");
				throw new UnprocessableEntityException("Function/Folder name already exists.");
			}
			if (function.getId() == null && validateFunctionName != null) {
				logger.info("Function/Folder name already exists.");
				throw new UnprocessableEntityException("Function/Folder name already exists.");
			}

		}
		RatedFunction function = validateRatedFunction(functionDTO, functionType, oldType);
		logger.info("Returning from saveOrUpdateFunction() .");
		return functionRepository.save(function);
	}

	/**
	 * To validate the Rated Function
	 * 
	 * @param functionDTO
	 * @param functionType
	 * @param oldType
	 * @return RatedFunction
	 */
	private RatedFunction validateRatedFunction(FunctionDTO functionDTO, String functionType, String oldType) {
		logger.info("Inside validateRatedFunction() :: updating : " + functionDTO);
		RatedFunction ratedFunction;
		if (oldType != null) {
			logger.info(" Validating to update function :  " + functionDTO);
			RatedFunction savedFunction = getSavedFunction(oldType, functionDTO.getId());
			if (functionType.equalsIgnoreCase(oldType)) {
				ratedFunction = savedFunction;
			} else {
				ratedFunction = changeFunctionType(savedFunction, functionType);
				functionRepository.delete(functionDTO.getId());
			}
		} else {
			logger.info(" Validating to save new function :  " + functionDTO);
			ratedFunction = getNewFunction(functionType);
		}
		ratedFunction = functionDTO.getRatedFunction(ratedFunction);
		// Validating keyword(FunctionKeyword).
		if (!StringUtils.isBlank(ratedFunction.getKeyword())) {
			FunctionKeyword functionKeyword = functionKeywordRepository
					.findByKeywordIgnoreCase(ratedFunction.getKeyword());
			if (functionKeyword == null) {
				logger.error("Provided keyword is not valid.");
				throw new NotFoundException("Provided keyword is not valid.");
			}
			ratedFunction.setFunctionKeyword(functionKeyword);
		} else {
			ratedFunction.setKeyword(null);
			ratedFunction.setFunctionKeyword(null);
		}
		FunctionKeyword funKeyword = functionDTO.getFunctionKeyword();
		if (funKeyword != null) {
			logger.info("Adding keywords to function :  " + funKeyword);
			ratedFunction.setFunctionKeyword(functionKeywordRepository.findOne(funKeyword.getId()));
		}
		if (ratedFunction.getVat() == null) {
			logger.error("Vat is required ");
			throw new NotFoundException("Vat is required ");
		}
		ratedFunction.setParent(validatedParent(functionDTO));
		logger.info("Returning from validateRatedFunction() .");
		return ratedFunction;
	}

	/**
	 * To change the function type
	 * 
	 * @param savedRatedFunction
	 * @param the                new function type
	 * @return RatedFunction
	 */
	private RatedFunction changeFunctionType(RatedFunction savedRatedFunction, String type) {
		logger.info("Inside changeFunctionType() ::" + type);
		RatedFunction ratedFunction;
		switch (type) {
		case TeamiumConstants.STAFF_FUNCTION_TYPE:
			ratedFunction = new StaffFunction();
			break;
		case TeamiumConstants.EQUIPMENT_FUNCTION_TYPE:
			ratedFunction = new EquipmentFunction();
			break;
		case TeamiumConstants.SUPPLY_FUNCTION_TYPE:
			ratedFunction = new SupplyFunction();
			break;
		case TeamiumConstants.PROCESS_FUNCTION_TYPE:
			ratedFunction = new ProcessFunction();
			break;
		case TeamiumConstants.RIGHT_FUNCTION_TYPE:
			ratedFunction = new RightFunction();
			break;
		default:
			logger.error("Please enter a valid function type.");
			throw new UnprocessableEntityException("Please enter a valid function type.");
		}
		logger.info("Returning from changeFunctionType() .");
		RatedFunction function = new FunctionDTO(savedRatedFunction).getRatedFunction(ratedFunction);
		function.setId(ratedFunction.getId());
		function.setVersion(ratedFunction.getVersion());
		return function;
	}

	/**
	 * To get savedFunction
	 * 
	 * @param type
	 * @param id
	 * @return RatedFunction
	 */
	public RatedFunction getSavedFunction(String type, Long id) {
		logger.info("Inside getSavedFunction() :: finding " + type + " : with id " + id);
		RatedFunction ratedFunction = null;
		switch (type) {
		case TeamiumConstants.STAFF_FUNCTION_TYPE:
			ratedFunction = staffFunctionRepository.findOne(id);
			break;
		case TeamiumConstants.EQUIPMENT_FUNCTION_TYPE:
			ratedFunction = equipmentFunctionRepository.findOne(id);
			break;
		case TeamiumConstants.SUPPLY_FUNCTION_TYPE:
			ratedFunction = supplyFunctionRepository.findOne(id);
			break;
		case TeamiumConstants.PROCESS_FUNCTION_TYPE:
			ratedFunction = processFunctionRepository.findOne(id);
			break;
		case TeamiumConstants.RIGHT_FUNCTION_TYPE:
			ratedFunction = rightFunctionRepository.findOne(id);
			break;
		default:
			logger.error("Please enter a valid function type.");
			throw new UnprocessableEntityException("Please enter a valid function type.");
		}
		return ratedFunction;
	}

	/**
	 * To get new function with specific type
	 * 
	 * @param type
	 * @return RatedFunction
	 */
	public RatedFunction getNewFunction(String type) {
		logger.info("Inside getNewFunction() ::" + type);
		switch (type) {
		case TeamiumConstants.STAFF_FUNCTION_TYPE:
			return new StaffFunction();
		case TeamiumConstants.EQUIPMENT_FUNCTION_TYPE:
			return new EquipmentFunction();
		case TeamiumConstants.SUPPLY_FUNCTION_TYPE:
			return new SupplyFunction();
		case TeamiumConstants.PROCESS_FUNCTION_TYPE:
			return new ProcessFunction();
		case TeamiumConstants.RIGHT_FUNCTION_TYPE:
			return new RightFunction();
		default:
			logger.error("Please enter a valid function type.");
			throw new UnprocessableEntityException("Please enter a valid function type.");
		}
	}

	/**
	 * To find all ratedFunction
	 * 
	 * 
	 * @return the FunctionDTO
	 */
	public List<RatedFunction> findAllRatedFunctions() {
		logger.info("inside findAllRatedFunctions() :: finding all rated functions : ");
		List<RatedFunction> ratedFunctions = functionRepository.getAllRatedFunctions();
		logger.info("Returning from findAllRatedFunctions() .");
		return ratedFunctions;
	}

	/**
	 * To get function by functionId
	 * 
	 * @param functionId the functionId
	 * 
	 * @return Function object
	 */
	public RatedFunction getRatedFunctionById(long functionId) {
		logger.info("Inside getRatedFunctionById() :: finding rated function with id: " + functionId);
		RatedFunction function = functionRepository.getRatedFunctionById(functionId);
		if (function == null) {
			logger.error("Function not found ");
			throw new NotFoundException("Function not found");
		}
		return function;
	}

	/**
	 * Method to get function by name
	 * 
	 * @param name the value
	 * 
	 * @return the function object
	 */
	public Function findFunctionByValueIgnoreCase(String name) {
		logger.info("Inside FunctionService :: findFunctionByValueIgnoreCase(), To get function by function-name");
		return functionRepository.findByValueIgnoreCase(name);
	}

	/**
	 * To get all functions having functionKeyword with id given.
	 * 
	 * @param id
	 * @return List of Function.
	 */
	public List<Function> findByFunctionKeywordId(Long id) {
		return functionRepository.findByFunctionKeywordId(id);
	}

}
