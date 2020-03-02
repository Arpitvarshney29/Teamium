package com.teamium.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.teamium.config.PropConfig;
import com.teamium.constants.Constants;
import com.teamium.domain.FunctionKeys;
import com.teamium.domain.FunctionKeyword;
import com.teamium.domain.Milestone;
import com.teamium.domain.MilestoneType;
import com.teamium.domain.prod.resources.ResourceFunction;
import com.teamium.domain.prod.resources.ResourceInformation;
import com.teamium.domain.prod.resources.equipments.Equipment;
import com.teamium.domain.prod.resources.equipments.EquipmentFunction;
import com.teamium.domain.prod.resources.equipments.EquipmentResource;
import com.teamium.domain.prod.resources.functions.Function;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.domain.prod.upload.log.ImportFor;
import com.teamium.domain.prod.upload.log.SpreadsheetUploadLog;
import com.teamium.dto.BookingDTO;
import com.teamium.dto.EquipmentDTO;
import com.teamium.dto.EquipmentDropDownDTO;
import com.teamium.dto.ResourceInformationDTO;
import com.teamium.dto.SpreadsheetMessageDTO;
import com.teamium.enums.attachmentType.AttachmentType.EquipmentAttachmentTypeName;
import com.teamium.enums.equipmentType.EquipmentType.EquipmentTypeName;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.EquipmentRepository;
import com.teamium.repository.FunctionKeywordRepository;
import com.teamium.repository.SpreadsheetUploadLogRepository;
import com.teamium.service.prod.resources.functions.FunctionService;
import com.teamium.service.prod.resources.suppliers.SupplierService;
import com.teamium.utils.CommonUtil;

/**
 * A service class implementation for Equipment Controller
 *
 */
@Service
public class EquipmentService {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private EquipmentRepository equipmentRepository;
	private SupplierService supplierService;
	private FunctionService functionService;
	private PropConfig propConfig;
	private AuthenticationService authenticationService;
	private FunctionKeywordRepository functionKeywordRepository;
	private UnavailabilityService unavailabilityService;
	private UnavailabilityEventService unavailabilityEventService;
	private SpreadsheetUploadLogRepository spreadsheetUploadLogRepository;
	private MilestoneTypeService milestoneTypeService;

	@Autowired
	@Lazy
	private BookingService bookingService;

	@Autowired
	public EquipmentService(EquipmentRepository equipmentRepository, SupplierService supplierService,
			PropConfig propConfig, FunctionService functionService, AuthenticationService authenticationService,
			FunctionKeywordRepository functionKeywordRepository, UnavailabilityService unavailabilityService,
			UnavailabilityEventService unavailabilityEventService,
			SpreadsheetUploadLogRepository spreadsheetUploadLogRepository, MilestoneTypeService milestoneTypeService) {
		this.equipmentRepository = equipmentRepository;
		this.supplierService = supplierService;
		this.propConfig = propConfig;
		this.functionService = functionService;
		this.authenticationService = authenticationService;
		this.functionKeywordRepository = functionKeywordRepository;
		this.unavailabilityService = unavailabilityService;
		this.unavailabilityEventService = unavailabilityEventService;
		this.spreadsheetUploadLogRepository = spreadsheetUploadLogRepository;
		this.milestoneTypeService = milestoneTypeService;
	}

	/**
	 * Returns list of all equipments.
	 * 
	 * @return EquipmentDTOs
	 */
	public List<EquipmentDTO> findEquipments() {
		logger.info(" Inside EquipmentService,getEquipmentList :: Finding All Equipments");
		List<EquipmentDTO> equipments = equipmentRepository.findAll().stream().map(eq -> {
			EquipmentDTO equipmentDTO = new EquipmentDTO(eq);
			addMilestoneMilestoneColor(eq.getMilestones());
//			equipmentDTO.setAvailable(this.unavailabilityEventService.isAvailable(eq.getResource()));
			equipmentDTO.setAvailable(eq.isAvailable());
			return equipmentDTO;
		}).collect(Collectors.toList());

		logger.info(" Returning from EquipmentService,getEquipmentList: found " + equipments.size());
		return equipments;
	}

	/**
	 * add expiration color in milestone
	 */
	private void addMilestoneMilestoneColor(Set<Milestone> milestones) {
		logger.info(" Inside EquipmentService,addMilestoneMilestoneColor ");
		if (milestones == null) {
			return;
		}
		for (Milestone milestone : milestones) {
			int remainingDays = milestone.getRemainingDays();
			if (remainingDays < 0) {
				// red color
				milestone.setExpirationColor("RED");
			} else if (remainingDays > propConfig.getEquipmentMilestoneExpiration()) {
				// green color
				milestone.setExpirationColor("#119f32");
			} else {
				// orange color
				milestone.setExpirationColor("ORANGE");
			}
		}
		logger.info(" Returning from EquipmentService,addMilestoneMilestoneColor: ");
	}

	/**
	 * To save or update equipment
	 * 
	 * @param equipmentDTO
	 * @return
	 * @throws Exception
	 */
	public EquipmentDTO saveOrUpdate(EquipmentDTO equipmentDTO) throws Exception {
		if (!authenticationService.isEquipmentManager()) {
			logger.warn("A invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to save or update to equipment.");

			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}

		logger.info(" Inside EquipmentService: saveAndUpdate equipmentDTO: " + equipmentDTO.toString());
		Equipment equipment = null;
		EquipmentResource resource = new EquipmentResource();

		validateEquipmentDTO(equipmentDTO);
		List<Equipment> equipments = equipmentRepository.findByNameContainingIgnoreCase(equipmentDTO.getName());
		if (equipmentDTO.getId() != null) {// updation
			if (equipments.size() > 1) {
				logger.error("Equipment with name already exists.");
				throw new UnprocessableEntityException("Equipment with name already exists.");
			}
			equipment = validateEquipment(equipmentDTO.getId());
		} else {// creating new
			if (equipments.size() != 0) {
				logger.error("Equipment with name already exists.");
				throw new UnprocessableEntityException("Equipment with name already exists.");
			}
			equipment = equipmentDTO.getEquipment();
		}
		equipment = equipmentDTO.getEquipment(equipment);
		if (equipment.getResource() == null) {
			resource.setEquipment(equipment);
		}
		if (equipment.getId() == null) {// adding user who add new equipment
			equipment.setCreation(Calendar.getInstance());
			equipment.setCreatedBy(authenticationService.getAuthenticatedUser());
		} else {// adding user who updates the existing user
			equipment.setUpdation(Calendar.getInstance());
			equipment.setUpdatedBy(authenticationService.getAuthenticatedUser());
		}

		if (equipmentDTO.getSupplier() != null) {
			equipment.setSupplier(supplierService.findById(equipmentDTO.getSupplier().getId()));
		}

		// remove amortization functions
		if (equipmentDTO.getUnassignedAmortizationIds() != null
				&& !equipmentDTO.getUnassignedAmortizationIds().isEmpty()) {
			for (Long amortizationId : equipmentDTO.getUnassignedAmortizationIds()) {
				equipment.getAmortizationArray().removeIf(dt1 -> {
					return amortizationId.longValue() == dt1.getId().longValue();
				});
			}
		}

		// New implementation for Resource-Function
		Set<ResourceFunction> resourceFunctions = equipment.getResource().getFunctions();
		EquipmentResource equipmentResource = equipment.getResource();
		AtomicBoolean isRemove = new AtomicBoolean(false);

		if (equipmentDTO.getResource().getFunctions() != null && !equipmentDTO.getResource().getFunctions().isEmpty()) {

			// removing function not present in dto
			if (resourceFunctions != null && !resourceFunctions.isEmpty()) {
				resourceFunctions.removeIf(resFun -> {
					isRemove.set(true);
					equipmentDTO.getResource().getFunctions().forEach(dto -> {
						if (dto.getId() != null && dto.getId().longValue() == resFun.getId().longValue()) {
							isRemove.set(false);
							return;
						}
					});
					return isRemove.get();
				});
			}

			// assigning new function or updating existing resource-function
			equipmentDTO.getResource().getFunctions().forEach(data -> {
				if (data.getId() == null) {
					ResourceFunction resourceFunction = equipmentResource.assignFunction(functionService
							.getFunctionByIdAndDiscriminator(EquipmentFunction.class, data.getFunction().getId()));

					resourceFunction.setRating(data.getRating());
				} else {
					resourceFunctions.forEach(dto -> {
						if (dto.getId() != null && data.getId().longValue() == dto.getId().longValue()) {
							dto.setRating(data.getRating());
						}
					});
				}
			});
		} else {
			// removing all functions
			equipment.getResource().getFunctions().clear();
		}
		addMilestoneMilestoneColor(equipment.getMilestones());
		logger.info("Returning from EquipmentService,saveAndUpdate: Successfully saved ");
		equipment.setAvailable(equipmentDTO.isAvailable());
		Equipment persistedEquipment = equipmentRepository.save(equipment);
		EquipmentResource resourceEntity = persistedEquipment.getResource();

		// resource-informations
		persistedEquipment.getResource().getInformations().stream().forEach(entity -> entity.setResource(null));
		List<ResourceInformationDTO> resInformations = equipmentDTO.getResource() == null ? null
				: equipmentDTO.getResource().getInformations();
		if (resInformations != null && !resInformations.isEmpty()) {
			persistedEquipment.getResource().setInformations(resInformations.stream().map(info -> {
				ResourceInformation resInfo = info.getResourceInformation(new ResourceInformation());
				String keywordValue = resInfo.getKeywordValue();
				FunctionKeyword funKeyword = functionKeywordRepository.getFunctionByKeyword(keywordValue);
				if (funKeyword == null) {
					logger.info("keyword doesnt exist : " + keywordValue);
					throw new UnprocessableEntityException("keyword doesnt exist.");
				}
				resInfo.setKeywordValue(keywordValue);

				List<FunctionKeys> funkeys = funKeyword.getKeysList().stream()
						.filter(key -> key.getKeyValue().equals(resInfo.getKeyValue())).collect(Collectors.toList());
				if (funkeys == null || funkeys.isEmpty()) {
					logger.info("key doesnt exist : " + resInfo.getKeyValue());
					throw new UnprocessableEntityException("key doesnt exist.");
				}
				resInfo.setKeyValue(funkeys.get(0).getKeyValue());
				resInfo.setResource(resourceEntity);
				return resInfo;
			}).collect(Collectors.toList()));

		}
		persistedEquipment = equipmentRepository.save(persistedEquipment);

		this.enableOrDisableEquipment(persistedEquipment.getId(), equipmentDTO.isAvailable());
		EquipmentDTO responseEquipmentDTO = new EquipmentDTO(persistedEquipment);
		responseEquipmentDTO.setAvailable(equipmentDTO.isAvailable());
		return responseEquipmentDTO;
	}

	/**
	 * To delete a equipment by id.
	 * 
	 * @param equipmentId
	 */
	public void deleteEquipment(Long equipmentId) {
		logger.info(" Inside EquipmentService,deleteEquipment:equipmentId:" + equipmentId);
		validateEquipment(equipmentId);
		equipmentRepository.delete(equipmentId);
		logger.info(" Returning from EquipmentService,deleteEquipment :Successfully deleted.");
	}

	/**
	 * To get EquipmentDTO by id.
	 * 
	 * @param equipmentId
	 * @return
	 */
	public EquipmentDTO getEquipmentById(Long equipmentId) {
		Equipment equipment = findEquipment(equipmentId);
		EquipmentDTO equipmentDTO = new EquipmentDTO(equipment);
		// equipmentDTO.setAvailable(this.unavailabilityEventService.isAvailable(equipment.getResource()));

		// get recent bookings of this equipment
		List<BookingDTO> recentBooking = bookingService.getRecentBookings(equipment.getResource().getId());
		equipmentDTO.setRecentBooking(recentBooking);
		return equipmentDTO;
	}

	/**
	 * To validate a equipment.
	 * 
	 * @param equipmentId
	 */
	private Equipment validateEquipment(Long equipmentId) {
		return findEquipment(equipmentId);
	}

	/**
	 * Method to find Equipment by equipmentId.
	 * 
	 * @param equipmentId the equipmentId
	 * 
	 * @return the Equipment
	 */
	public Equipment findEquipment(Long equipmentId) {
		logger.info("Inside EquipmentService,findEquipment:equipmentId: " + equipmentId);
		if (equipmentId == null) {
			throw new NotFoundException("Equipment not found.");
		}
		Equipment equipment = equipmentRepository.findOne(equipmentId);
		if (equipment == null) {
			logger.error("Equipment not found.");
			throw new NotFoundException("Equipment not found.");
		}
		addMilestoneMilestoneColor(equipment.getMilestones());
		logger.info("Returning after finding equipment successfully.");
		return equipment;
	}

	/**
	 * To get channel dropdown data.
	 * 
	 * @return the ChannelDropDownDTO
	 */
	public EquipmentDropDownDTO getEquipmentDropDownData() {
		logger.info("To get equipment dropdown data.");
		if (!authenticationService.isEquipmentManager() && !authenticationService.isAdmin()) {
			logger.warn("A invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get equipment drop-down list.");

			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}

		EquipmentDropDownDTO equipmentDropDownDTO = new EquipmentDropDownDTO();
		equipmentDropDownDTO.setAttachments(EquipmentAttachmentTypeName.getAllAttachmentType());
		equipmentDropDownDTO.setMilestones(milestoneTypeService.getAllMilestoneTypeName(Constants.EQUIPMENT_STRING));
		equipmentDropDownDTO.setBrands(equipmentRepository.getBrands());
		equipmentDropDownDTO.setType(EquipmentTypeName.getAllEquipmentType());
		equipmentDropDownDTO.setModels(equipmentRepository.getModels());
		equipmentDropDownDTO.setReference(equipmentRepository.getReference());
		equipmentDropDownDTO.setLocations(equipmentRepository.getLocations());
		equipmentDropDownDTO.setFunctions(functionService.getFunctionDTOsByDiscrimination(EquipmentFunction.class));

		logger.info("Returning EquipmentDropDownDTO after getting equipment dropdown data.");
		return equipmentDropDownDTO;
	}

	/**
	 * Validate EquipmentDTO.
	 * 
	 * @param equipmentDTO
	 */
	private void validateEquipmentDTO(EquipmentDTO equipmentDTO) {
		String serialNumber = equipmentDTO.getSerialNumber();
		if (StringUtils.isBlank(serialNumber)) {
			logger.error("Serial number cannot be empty ");
			throw new UnprocessableEntityException("Serial number cannot be empty ");
		}
		Equipment savedEquipment = equipmentRepository.findBySerialNumberContainingIgnoreCase(serialNumber);
		if (savedEquipment != null && !savedEquipment.getId().equals(equipmentDTO.getId())) {
			logger.error("Equipment with serial number already exists.");
			throw new UnprocessableEntityException("Equipment with serial number already exists.");
		}

		if (equipmentDTO.getMilestones() != null && !equipmentDTO.getMilestones().isEmpty()) {
			for (Milestone milestone : equipmentDTO.getMilestones()) {
				if (StringUtils.isBlank(milestone.getType())) {
					logger.info("Please provide valid milestone name");
					throw new UnprocessableEntityException("Please provide valid milestone name");
				}
				MilestoneType milestoneType = milestoneTypeService.findMilestoneTypeByNameAndDiscriminator(
						milestone.getType(), Constants.EQUIPMENT_STRING.toString());
				if (milestoneType == null) {
					logger.info("Please provide valid milestone name");
					throw new UnprocessableEntityException("Please provide valid milestone name");
				}
				milestone.setMilestoneType(milestoneType);
			}
		}
	}

	/**
	 * Enable Or Disable Equipment.
	 * 
	 * @param equipmentId
	 * @param isAvailable
	 */
	public void enableOrDisableEquipment(long equipmentId, boolean isAvailable) throws Exception {
		this.logger.info("Inside EquipmentService : availableOrUnavailableEquipment");
		this.logger.info("equipmentId : " + equipmentId);
		this.logger.info("isAvailable : " + isAvailable);

		if (!authenticationService.isEquipmentManager() && !authenticationService.isAdmin()) {
			logger.warn("A invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get equipment drop-down list.");

			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}

		Equipment equipment = findEquipment(equipmentId);
		if (equipment.getResource().getId() == null) {
			throw new UnprocessableEntityException("Resource is not available for Equipment : " + equipment.getId());
		}
		this.unavailabilityService.setAvailable(equipment.getResource(), isAvailable);

		this.logger.info("Returning from EquipmentService : availableOrUnavailableEquipment");
	}

	/**
	 * Method to import equipment spreadsheet
	 * 
	 * @param file
	 * 
	 * @return SpreadsheetMessage wrapper object
	 * 
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public SpreadsheetMessageDTO upload(MultipartFile file) throws InvalidFormatException, IOException {
		logger.info("Inside EquipmentService :: upload() : To import data for equipment by spreadsheet.");

		StaffMember loggedInUser = authenticationService.getAuthenticatedUser();
		if (!authenticationService.isEquipmentManager()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to import equipment spreadsheet.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}

		String logDetails = "";
		Date currentDate = new Date();
		String dateString = new SimpleDateFormat("yyyyMMddHHmm").format(currentDate);
		Calendar calander = Calendar.getInstance();
		calander.setTime(currentDate);

		SpreadsheetUploadLog spreadsheetUploadLog = new SpreadsheetUploadLog();

		boolean hasInvalidHeader = false;
		boolean hasError = false;

		String fileExtention = null;

		String relativePath = null;
		String absolutePath = null;
		URL url = null;
		String logFileName = null;

		if (file != null) {
			logDetails = "Upload started.";
			String fileName = file.getOriginalFilename();
			logger.info("Importing " + fileName);

			fileExtention = FilenameUtils.getExtension(file.getOriginalFilename());
			Map<String, Integer> indexValue = new HashMap<String, Integer>();
			Map<String, String> numbers = new HashMap<>();
			AtomicInteger index = new AtomicInteger(0);
			int rowNumber = 0;
			int corruptedRowNumber = 0;

			String extension = fileExtention.toUpperCase();
			switch (extension) {

			case "XLSX":
			case "XLS":
				logger.info("Inside here in case XLS/XSLX for equipment");
				Workbook workbook = WorkbookFactory.create(file.getInputStream());
				Workbook errorWorkbook = new XSSFWorkbook();
				Sheet sheet = errorWorkbook.createSheet(
						StringUtils.capitalize(Constants.EQUIPMENT_STRING) + " " + Constants.INVALID_SPREADSHEET_NAME);
				Sheet spreadsheet = workbook.getSheetAt(0);

				for (Cell cell : spreadsheet.getRow(0)) {

					switch (getValue(cell).trim()) {

					case Constants.EQUIPMENT_NAME:
						indexValue.put(Constants.EQUIPMENT_NAME, index.getAndIncrement());
						continue;
					case Constants.EQUIPMENT_SERIAL_NUMBER:
						indexValue.put(Constants.EQUIPMENT_SERIAL_NUMBER, index.getAndIncrement());
						continue;
					case Constants.EQUIPMENT_TYPE:
						indexValue.put(Constants.EQUIPMENT_TYPE, index.getAndIncrement());
						continue;
					case Constants.EQUIPMENT_BRAND:
						indexValue.put(Constants.EQUIPMENT_BRAND, index.getAndIncrement());
						continue;
					case Constants.EQUIPMENT_MODEL:
						indexValue.put(Constants.EQUIPMENT_MODEL, index.getAndIncrement());
						continue;
					case Constants.EQUIPMENT_REFRENCE:
						indexValue.put(Constants.EQUIPMENT_REFRENCE, index.getAndIncrement());
						continue;
					case Constants.EQUIPMENT_LOCATION:
						indexValue.put(Constants.EQUIPMENT_LOCATION, index.getAndIncrement());
						continue;
					case Constants.EQUIPMENT_FORMAT:
						indexValue.put(Constants.EQUIPMENT_FORMAT, index.getAndIncrement());
						continue;
					case Constants.EQUIPMENT_DESCRIPTION:
						indexValue.put(Constants.EQUIPMENT_DESCRIPTION, index.getAndIncrement());
						continue;
					case Constants.EQUIPMENT_FUNCTION:
						indexValue.put(Constants.EQUIPMENT_FUNCTION, index.getAndIncrement());
						continue;
					case Constants.EQUIPMENT_IP_ADDRESS:
						indexValue.put(Constants.EQUIPMENT_IP_ADDRESS, index.getAndIncrement());
						continue;
					case Constants.EQUIPMENT_ORIGIN:
						indexValue.put(Constants.EQUIPMENT_ORIGIN, index.getAndIncrement());
						continue;
					case Constants.EQUIPMENT_BARCODE_TYPE:
						indexValue.put(Constants.EQUIPMENT_BARCODE_TYPE, index.getAndIncrement());
						continue;
					case Constants.EQUIPMENT_BARCODE:
						indexValue.put(Constants.EQUIPMENT_BARCODE, index.getAndIncrement());
						continue;

					case Constants.EQUIPMENT_CONSUMPTION:
						indexValue.put(Constants.EQUIPMENT_CONSUMPTION, index.getAndIncrement());
						continue;
					case Constants.EQUIPMENT_WEIGHT:
						indexValue.put(Constants.EQUIPMENT_WEIGHT, index.getAndIncrement());
						continue;
					case Constants.EQUIPMENT_SIZE_HXWXD:
						indexValue.put(Constants.EQUIPMENT_SIZE_HXWXD, index.getAndIncrement());
						continue;
					case Constants.EQUIPMENT_INSURANCE_VALUE:
						indexValue.put(Constants.EQUIPMENT_INSURANCE_VALUE, index.getAndIncrement());
						continue;
					case Constants.EQUIPMENT_PURCHASE_VALUE:
						indexValue.put(Constants.EQUIPMENT_PURCHASE_VALUE, index.getAndIncrement());
						continue;

					case Constants.EQUIPMENT_ATA_VALUE:
						indexValue.put(Constants.EQUIPMENT_ATA_VALUE, index.getAndIncrement());
						continue;

					}
					index.getAndIncrement();
				}

				addXlsRow(sheet, spreadsheet.getRow(0), 0, Constants.ERROR_FIELD);
				spreadsheet.removeRow(spreadsheet.getRow(0));

				if (indexValue.size() >= Constants.HEADERS_EQUIPMENT) {

					for (Row row : spreadsheet) {

						hasError = false;
						++rowNumber;
						if (!StringUtils.isBlank(getValue(row.getCell(0)))
								&& indexValue.get(Constants.EQUIPMENT_NAME) != null
								&& !getValue(row.getCell(indexValue.get(Constants.EQUIPMENT_NAME))).isEmpty()) {

							Equipment equipment = new Equipment();
							equipment.setId(null);
							EquipmentResource resource = new EquipmentResource();
							if (equipment.getResource() == null) {
								resource.setEquipment(equipment);
							}

							String equipmentName = getValue(row.getCell(indexValue.get(Constants.EQUIPMENT_NAME)));
							if (!StringUtils.isBlank(equipmentName)) {
								// setting equipment-name
								equipmentName = equipmentName.trim();
								equipment.setName(equipmentName);
							} else {
								// make a log for invalid equipment-name
								hasError = true;
								logDetails = "Invalid equipment-name. Equipment-Name : " + equipmentName
										+ " is invalid on row number : " + rowNumber;
								++corruptedRowNumber;
								addXlsRow(sheet, row, corruptedRowNumber, Constants.STAFF_EMP_CODE + " : "
										+ equipmentName + " is invalid, employee-code");
								continue;
							}

							String serialNumber = getValue(
									row.getCell(indexValue.get(Constants.EQUIPMENT_SERIAL_NUMBER)));
							if (!StringUtils.isBlank(serialNumber)) {
								serialNumber = serialNumber.trim();
								Equipment validateSerialNumber = equipmentRepository
										.findBySerialNumberContainingIgnoreCase(serialNumber);
								if (validateSerialNumber != null) {
									// make a log for duplicate serial-number
									hasError = true;
									logDetails = "Duplicate employee serial-number. Serial-number : " + serialNumber
											+ " is duplicate on row number : " + rowNumber;
									++corruptedRowNumber;
									addXlsRow(sheet, row, corruptedRowNumber, Constants.EQUIPMENT_SERIAL_NUMBER + " : "
											+ serialNumber + " is duplicate, employee-code");
									continue;
								}
							} else {
								// make a log for invalid serial-number
								hasError = true;
								logDetails = "Invalid equipment serial-number. Equipment serial-number : "
										+ serialNumber + " is invalid on row number : " + rowNumber;
								++corruptedRowNumber;
								addXlsRow(sheet, row, corruptedRowNumber, Constants.EQUIPMENT_SERIAL_NUMBER + " : "
										+ serialNumber + " is invalid, employee-code");
								continue;
							}
							equipment.setSerialNumber(serialNumber);

							String equipmentType = getValue(row.getCell(indexValue.get(Constants.EQUIPMENT_TYPE)));

							if (!StringUtils.isBlank(equipmentType)) {
								equipmentType = equipmentType.trim();

								if (equipmentType.equalsIgnoreCase("Hardware")) {
									String type = EquipmentTypeName.HW.toString();
									equipment.setType(type);
								} else if (equipmentType.equalsIgnoreCase("Infrastructure")) {
									String type = EquipmentTypeName.Infrastructure.toString();
									equipment.setType(type);
								} else if (equipmentType.equalsIgnoreCase("Software")) {
									String type = EquipmentTypeName.SW.toString();
									equipment.setType(type);
								} else if (equipmentType.equalsIgnoreCase("Truck")) {
									String type = EquipmentTypeName.Truck.toString();
									equipment.setType(type);
								}
							}

							String brand = getValue(row.getCell(indexValue.get(Constants.EQUIPMENT_BRAND)));
							if (!StringUtils.isBlank(brand)) {
								brand = brand.trim();
								equipment.setBrand(brand);
							}

							String model = getValue(row.getCell(indexValue.get(Constants.EQUIPMENT_MODEL)));
							if (!StringUtils.isBlank(model)) {
								model = model.trim();
								equipment.setModel(model);
							}

							String location = getValue(row.getCell(indexValue.get(Constants.EQUIPMENT_LOCATION)));
							if (!StringUtils.isBlank(location)) {
								location = location.trim();
								equipment.setLocation(location);
							}

							String format = getValue(row.getCell(indexValue.get(Constants.EQUIPMENT_FORMAT)));
							if (!StringUtils.isBlank(format)) {
								format = format.trim();
								equipment.setFormat(format);
							}

							String description = getValue(row.getCell(indexValue.get(Constants.EQUIPMENT_DESCRIPTION)));
							if (!StringUtils.isBlank(description)) {
								description = description.trim();
								equipment.setDescription(description);
							}

							// functions
							String functionString = getValue(row.getCell(indexValue.get(Constants.EQUIPMENT_FUNCTION)));
							if (!StringUtils.isBlank(functionString)) {
								String[] splitFunctions = functionString.split(",");
								for (String functionName : splitFunctions) {
									if (!StringUtils.isBlank(functionName)) {
										functionName = functionName.trim();

										Function fun = functionService.findFunctionByValueIgnoreCase(functionName);
										if (fun == null || ((fun != null) && !(fun instanceof EquipmentFunction))) {
											// make a log for wrong function
											hasError = true;
											logDetails = "Invalid function. Function : " + functionName
													+ " not found by name on row number : " + rowNumber;
											++corruptedRowNumber;
											addXlsRow(sheet, row, corruptedRowNumber,
													Constants.EQUIPMENT_FUNCTION + " : " + functionName);
											break;
										}
										ResourceFunction resourceFunction = equipment.getResource().assignFunction(fun);
										resourceFunction.setRating(0);
										resourceFunction.setRates(null);
										resourceFunction.setCreatedOn(Calendar.getInstance());
										resourceFunction.setPrimaryFunction(Boolean.FALSE);
									}
								}
								if (hasError) {
									continue;
								}
							}

							String ipAddress = getValue(row.getCell(indexValue.get(Constants.EQUIPMENT_IP_ADDRESS)));
							if (!StringUtils.isBlank(ipAddress)) {
								ipAddress = ipAddress.trim();
								equipment.setSpecsIp(ipAddress);
							}

							String origin = getValue(row.getCell(indexValue.get(Constants.EQUIPMENT_ORIGIN)));
							if (!StringUtils.isBlank(origin)) {
								origin = origin.trim();
								equipment.setSpecsOrign(origin);
							}

							// equipment-barcode is not set right now

							String consumption = getValue(row.getCell(indexValue.get(Constants.EQUIPMENT_CONSUMPTION)));
							if (!StringUtils.isBlank(consumption)) {
								consumption = consumption.trim();
								equipment.setSpecsConsumption(consumption);
							}

							String weight = getValue(row.getCell(indexValue.get(Constants.EQUIPMENT_WEIGHT)));
							if (!StringUtils.isBlank(weight)) {
								weight = weight.trim();
								equipment.setSpecsWeight(weight);
							}

							String sizeHD = getValue(row.getCell(indexValue.get(Constants.EQUIPMENT_SIZE_HXWXD)));
							if (!StringUtils.isBlank(sizeHD)) {
								sizeHD = sizeHD.trim();
								equipment.setSpecsSize(sizeHD);
							}

							String insuranceString = getValue(
									row.getCell(indexValue.get(Constants.EQUIPMENT_INSURANCE_VALUE)));
							if (!StringUtils.isBlank(insuranceString)) {
								insuranceString = insuranceString.trim();
								BigDecimal insuranceValue = new BigDecimal(getIntValueAsString(insuranceString));
								equipment.setInsurance(insuranceValue.doubleValue());
							}

							String purchaseString = getValue(
									row.getCell(indexValue.get(Constants.EQUIPMENT_PURCHASE_VALUE)));
							if (!StringUtils.isBlank(purchaseString)) {
								purchaseString = purchaseString.trim();
								BigDecimal purchaseValue = new BigDecimal(getIntValueAsString(purchaseString));
								equipment.setPurchase(purchaseValue.doubleValue());
							}

							String ataString = getValue(row.getCell(indexValue.get(Constants.EQUIPMENT_ATA_VALUE)));
							if (!StringUtils.isBlank(ataString)) {
								ataString = ataString.trim();
								BigDecimal ataValue = new BigDecimal(getIntValueAsString(ataString));
								equipment.setAta(ataValue.doubleValue());
							}

							String reference = getValue(row.getCell(indexValue.get(Constants.EQUIPMENT_REFRENCE)));
							if (!StringUtils.isBlank(reference)) {
								reference = reference.trim();
								equipment.setReference(reference);
							}

							if (!hasError) {
								logger.info(
										"No error in spreadsheet row. Uploading it in database row. : " + rowNumber);
								equipment.setCreation(Calendar.getInstance());
								equipment.setCreatedBy(authenticationService.getAuthenticatedUser());
								logger.info("Saving equipment from spreadsheet");
								equipment.getResource().setName(equipment.getName());
								equipment = equipmentRepository.save(equipment);
								logger.info(
										"Succesfully saved Equipment from spreadsheet with id : " + equipment.getId());
							}

						} else {
							for (Cell cell : row) {
								if (!StringUtils.isBlank(getValue(cell))) {
									logDetails = "Not importing data from row no:" + rowNumber
											+ "  because equipment name does not exist in given row.";
									++corruptedRowNumber;
									addXlsRow(sheet, row, corruptedRowNumber, Constants.EQUIPMENT_NAME);
									break;
								}
							}
							break;
						}
					}
				} else {
					hasInvalidHeader = true;
					hasError = true;
					logDetails = "Invalid Header List";
				}
				if (corruptedRowNumber > 0) {
					hasError = true;
					String resourcePath = propConfig.getTeamiumResourcesPath();
					logFileName = FilenameUtils.removeExtension(file.getOriginalFilename()) + "_" + dateString
							+ ".xlsx";

					relativePath = Constants.SPREADSHEET_STRING + "/" + Constants.EQUIPMENT_STRING + "/"
							+ loggedInUser.getId() + "/";
					absolutePath = resourcePath + "/" + relativePath + logFileName;
					String urlString = propConfig.getAppBaseURL() + "/" + relativePath;

					File pathStored = new File(resourcePath + "/" + relativePath);
					if (!pathStored.exists()) {
						pathStored.mkdirs();
					}

					File filePath = new File(resourcePath + "/" + relativePath, logFileName);
					FileOutputStream fos = new FileOutputStream(filePath);
					errorWorkbook.write(fos);
					fos.close();

					url = CommonUtil.validateURL(urlString + logFileName);
				}
				break;

			default:

				logDetails = "Error! , Invalid file format. Please provide xlsx/xls/csv format.";
				logger.info("Spredsheet not uploaded for equipment. Found invalid format. File found with extention: "
						+ fileExtention);
				throw new UnprocessableEntityException(
						"Spredsheet not uploaded for equipment. Found invalid format. File found with extention: "
								+ fileExtention);
			}
		} else {
			logger.info("File is null. Please upload valid file");
			throw new UnprocessableEntityException("File is null. Please upload valid file");
		}

		if (hasInvalidHeader) {
			// spreadsheet has invalid headers
			hasError = true;
			logDetails = " Spreadsheet has invalid headers, Upload failed.";
		}
		try {
			spreadsheetUploadLog = new SpreadsheetUploadLog(logDetails, calander,
					loggedInUser.getUserSetting().getLogin(), ImportFor.EQUIPMENT, fileExtention, hasError,
					loggedInUser.getId(), file.getOriginalFilename(), absolutePath);
			spreadsheetUploadLog.setUrl(url);
			logger.info("Saving spreadsheet log in database.");
			spreadsheetUploadLog = spreadsheetUploadLogRepository.save(spreadsheetUploadLog);
			logger.info("Successfully saved spreadsheet log in database with id : " + spreadsheetUploadLog.getId());
		} catch (Exception e) {
			logger.info("Exception in saving log in database");
			e.printStackTrace();
		}
		logger.info("Uploaded spredsheet for equipment.");
		if (hasError) {
			SpreadsheetMessageDTO message = new SpreadsheetMessageDTO(
					"Imported equipment spreadsheet contains error data", Boolean.TRUE);
			return message;
		} else if (hasInvalidHeader) {
			SpreadsheetMessageDTO message = new SpreadsheetMessageDTO(logDetails, Boolean.TRUE);
			return message;
		} else {
			SpreadsheetMessageDTO message = new SpreadsheetMessageDTO("Successfully imported equipment spreadsheet",
					Boolean.FALSE);
			return message;
		}
	}

	/**
	 * To get type specific data from sheet.
	 * 
	 * @param data
	 * @return value of cell as String
	 */
	@SuppressWarnings("deprecation")
	private static String getValue(Cell data) {
		if (data == null || data.getCellTypeEnum() == null)
			return "";
		switch (data.getCellTypeEnum()) {
		case STRING:
			return data.getStringCellValue();

		case NUMERIC:
			return new BigDecimal(data.getNumericCellValue()).toPlainString();
		// return String.valueOf(data.getNumericCellValue());

		case FORMULA:
			data.setCellType(data.getCachedFormulaResultTypeEnum());
			return getValue(data);

		}
		return "";
	}

	/**
	 * To get integer value from double as string
	 * 
	 * @param s
	 * @return number as string
	 */
	public String getIntValueAsString(String s) {
		Double number = null;
		try {
			number = Double.parseDouble(s);
		} catch (Exception e) {
			return "";
		}
		return number.intValue() + "";

	}

	/**
	 * Add new corrupted Row to ErrorSheet
	 * 
	 * @param sheet
	 * @param row
	 * @param rowNumber
	 */
	private void addXlsRow(Sheet sheet, Row row, int rowNumber, String errorField) {
		Row errorRow = sheet.createRow(rowNumber);
		int columnIndex = 0;
		for (Cell c : row) {
			columnIndex = c.getColumnIndex();
			errorRow.createCell(c.getColumnIndex()).setCellValue(getValue(c));
		}

		if (errorField != null) {
			errorRow.createCell(++columnIndex).setCellValue(errorField + " has invalid data");
		}
	}

	/**
	 * To get list of numbers from comma separated list
	 * 
	 * @param s
	 * @return list of string from a string
	 */
	public List<String> getContactNumbers(String s) {
		return Arrays.asList(s.split(","));
	}

	/**
	 * To get list of equipment by milestoneType
	 * 
	 * @param milestoneType
	 * 
	 * @return list of equipments
	 */
	public List<Equipment> getEquipmentByMilestoneType(MilestoneType milestoneType) {
		logger.info(
				"Inside EquipmentService :: getEquipmentByMilestoneType(), To get list of equipment by milestone-type : "
						+ milestoneType);
		List<Equipment> equipmentsByMilestone = equipmentRepository.findEquipmentByMilestone(milestoneType);
		logger.info("Returning after getting list of equipments by milestone-type");
		return equipmentsByMilestone;
	}

	public Long findCountByAvailable(boolean available) {
		return equipmentRepository.findCountByAvailable(available);
	}

}
