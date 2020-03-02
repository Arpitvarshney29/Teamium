package com.teamium.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.teamium.constants.Constants;
import com.teamium.domain.MilestoneType;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.resources.equipments.Equipment;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.MilestoneTypeRepository;

/**
 * A Service class implementation to manage milestone-type configuration
 * 
 * @author Teamium
 *
 */
@Service
public class MilestoneTypeService {

	private AuthenticationService authenticationService;
	private MilestoneTypeRepository milestoneTypeRepository;

	@Autowired
	@Lazy
	private RecordService recordService;

	@Autowired
	@Lazy
	private EquipmentService equipmentService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public MilestoneTypeService(MilestoneTypeRepository milestoneTypeRepository,
			AuthenticationService authenticationService) {
		this.milestoneTypeRepository = milestoneTypeRepository;
		this.authenticationService = authenticationService;
	}

	/**
	 * To save or update milestone-type
	 * 
	 * @param milestone-type
	 * 
	 * @return milestone-type object
	 */
	public MilestoneType saveOrUpdateMilestoneType(MilestoneType milestoneType) {
		logger.info(
				"Inside MilestoneTypeService:: saveOrUpdateMilestoneType() , To save or update milestone-type , milestone-type : "
						+ milestoneType);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to save/update milestone-type.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		if (milestoneType.getId() != null && milestoneType.getId() != 0) {
			this.getMilestoneTypeByIdAndDiscriminator(milestoneType.getDiscriminator(), milestoneType.getId());
		}
		if (StringUtils.isBlank(milestoneType.getName())) {
			logger.info("Please provide a valid milestone");
			throw new UnprocessableEntityException("Please provide a valid milestone");
		}
		String discriminator = this.validateDiscriminator(milestoneType.getDiscriminator());
		milestoneType.setDiscriminator(discriminator);
		MilestoneType validateMilestoneType = milestoneTypeRepository
				.findByDiscriminatorAndNameIgnoreCase(discriminator, milestoneType.getName());
		if (milestoneType.getId() == null && validateMilestoneType != null) {
			logger.info("milestone already exist with given name : " + milestoneType.getName());
			throw new UnprocessableEntityException("Milestone already exist with given name");
		}
		if (milestoneType.getId() != null && validateMilestoneType != null
				&& (milestoneType.getId().longValue() != validateMilestoneType.getId().longValue())) {
			logger.info("Milestone already exist with given name : " + milestoneType.getName());
			throw new UnprocessableEntityException("Milestone already exist with given name");
		}
		logger.info("Saving milestone-type with name : " + milestoneType.getName());
		return milestoneTypeRepository.save(milestoneType);
	}

	/**
	 * Method to get milestone-type by id and discriminator
	 * 
	 * @param id
	 * @param discriminator
	 * 
	 * @return the milestone-type object
	 */
	public MilestoneType getMilestoneTypeByIdAndDiscriminator(String discriminator, Long id) {
		logger.info(
				"Inside MilestoneTypeService:: getMilestoneTypeById() , To get milestone-type by id and discriminator, milestoneTypeId : "
						+ id + ", discriminator : " + discriminator);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get milestone-type by id and discriminator.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		discriminator = this.validateDiscriminator(discriminator);
		MilestoneType milestoneType = milestoneTypeRepository.findByIdAndDiscriminator(id, discriminator);
		if (milestoneType == null) {
			logger.info("Milestone not found with id : " + id + " and discriminator : " + discriminator);
			throw new NotFoundException("Milestone not found");
		}
		return milestoneType;
	}

	/**
	 * To get list of milestone-type.
	 * 
	 * @return list of milestone-type
	 */
	public List<MilestoneType> getAllMilestoneType(String discriminator) {
		logger.info(
				"Inside MilestoneTypeService:: getAllMilestoneType() , To get list of all milestone-types by discriminator : "
						+ discriminator);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get all milestone-types by discriminator");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		discriminator = this.validateDiscriminator(discriminator);
		return milestoneTypeRepository.getAllMilestoneType(discriminator);
	}

	/**
	 * Method to get all milestone-type name by discriminator
	 * 
	 * @param discriminator
	 * 
	 * @return list of milestone-type name
	 */
	public List<String> getAllMilestoneTypeName(String discriminator) {
		logger.info(
				"Inside MilestoneTypeService :: getAllMilestoneTypeName() , To get list of all milestone-types name by discriminator : "
						+ discriminator);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get all milestone-types name by discriminator");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		discriminator = this.validateDiscriminator(discriminator);
		return milestoneTypeRepository.getAllMilestoneTypeName(discriminator);
	}

	/**
	 * To delete milestone-type by id.
	 * 
	 * @param id
	 */
	public void deleteMilestoneType(String discriminator, long id) {
		logger.info("Inside MilestoneTypeService :: deleteMilestoneType() , To delete milestone-type by id : " + id
				+ ", discriminator : " + discriminator);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to delete milestone-type.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		MilestoneType milestoneType = this.getMilestoneTypeByIdAndDiscriminator(discriminator, id);
		if (discriminator.equalsIgnoreCase(Constants.PROJECT_STRING)) {
			// check from record-milestone(due-date) if assigned
			List<Record> records = recordService.getRecordByMilestoneType(milestoneType);
			if (records != null && !records.isEmpty()) {
				logger.info("Cannot delete project-milestone as it is already assigned on record");
				throw new UnprocessableEntityException(
						"Cannot delete project-milestone as it is already assigned on record");
			}
		} else if (discriminator.equalsIgnoreCase(Constants.EQUIPMENT_STRING)) {
			// check from equipment-milestone if assigned
			List<Equipment> equipments = equipmentService.getEquipmentByMilestoneType(milestoneType);
			if (equipments != null && !equipments.isEmpty()) {
				logger.info("Cannot delete equipment-milestone as it is already assigned on equipment");
				throw new UnprocessableEntityException(
						"Cannot delete equipment-milestone as it is already assigned on equipment");
			}
		}
		milestoneTypeRepository.delete(id);
		logger.info("Successfully deleted milestone-type");
	}

	/**
	 * Method to get milestone-type by name by IgnoreCase and discriminator
	 * 
	 * @param name
	 * @param discriminator
	 * 
	 * @return the milestone-type object
	 */
	public MilestoneType findMilestoneTypeByNameAndDiscriminator(String name, String discriminator) {
		logger.info(
				"Inside MilestoneTypeService :: findMilestoneTypeByNameAndDiscriminator(), To get milestone-type by name : "
						+ name + " and discriminator : " + discriminator);
		this.validateDiscriminator(discriminator);
		MilestoneType milestone = milestoneTypeRepository.findByDiscriminatorAndNameIgnoreCase(discriminator, name);
		return milestone;
	}

	/**
	 * Method to validate discriminator
	 * 
	 * @param discriminator
	 * 
	 * @return the validated discriminator string
	 */
	private String validateDiscriminator(String discriminator) {
		logger.info(
				"Inside MilestoneTypeService :: validateDiscriminator(), to validate discriminator : " + discriminator);
		if (StringUtils.isBlank(discriminator)) {
			logger.info("Please provide valid discriminator");
			throw new UnprocessableEntityException("Please provide valid discriminator");
		}
		switch (discriminator.toLowerCase()) {
		case Constants.EQUIPMENT_STRING:
			return Constants.EQUIPMENT_STRING.toLowerCase();
		case Constants.PROJECT_STRING:
			return Constants.PROJECT_STRING.toLowerCase();
		default:
			logger.info("Please provide valid discriminator");
			throw new UnprocessableEntityException("Please provide valid discriminator");
		}
	}

}
