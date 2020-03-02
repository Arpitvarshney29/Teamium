package com.teamium.service;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamium.constants.Constants;
import com.teamium.domain.LeaveType;
import com.teamium.dto.LeaveTypeDTO;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.LeaveTypeRepository;

@Service
public class LeaveTypeService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private LeaveTypeRepository leaveTypeRepository;
	private AuthenticationService authenticationService;

	@Autowired
	public LeaveTypeService(LeaveTypeRepository leaveTypeRepository, AuthenticationService authenticationService) {
		this.leaveTypeRepository = leaveTypeRepository;
		this.authenticationService = authenticationService;
	}

	/**
	 * Save or Update LeaveType.
	 * 
	 * @param leaveTypeDTO
	 * @return instance of LeaveTypeDTO.
	 */
	public LeaveTypeDTO saveOrUpdateLeaveType(LeaveTypeDTO leaveTypeDTO) {
		this.logger.info("Inside saveOrUpdateLeaveType( " + leaveTypeDTO + " )");
		if (!this.authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to create save or update leave type.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		if (leaveTypeDTO.getType() == null) {
			this.logger.error("Please enter a valid leave type.");
			throw new UnprocessableEntityException("Please enter a valid leave type.");
		}
		LeaveType leaveType = this.leaveTypeRepository.findLeaveTypeByName(leaveTypeDTO.getType().toLowerCase());
		if (leaveTypeDTO.getLeaveTypeId() != null) {
			if (leaveType != null) {
				if (!leaveType.getLeaveTypeId().equals(leaveTypeDTO.getLeaveTypeId())) {
					this.logger.error("You can not create duplicate leave type.");
					throw new UnprocessableEntityException("You can not create duplicate leave type.");
				} else {
					leaveType = leaveTypeDTO.getLeaveTypeEntity(leaveType);
				}
			} else {
				leaveType = leaveTypeRepository.findOne(leaveTypeDTO.getLeaveTypeId());
				if (leaveType == null) {
					this.logger.error("Please enter a valid leave type id.");
					throw new UnprocessableEntityException("Please enter a valid leave type id.");
				}
				leaveType = leaveTypeDTO.getLeaveTypeEntity(leaveType);
			}
		} else {
			if (leaveType != null) {
				if (leaveType.isActive()) {
					this.logger.error("You can not create duplicate leave type.");
					throw new UnprocessableEntityException("You can not create duplicate leave type.");
				} else {
					leaveType.setActive(Boolean.TRUE);
				}
			} else {
				leaveType = leaveTypeDTO.getLeaveTypeEntity(new LeaveType());
			}
		}
		leaveTypeDTO = new LeaveTypeDTO(this.leaveTypeRepository.save(leaveType));
		this.logger.info("Successfully return from saveOrUpdateLeaveType");
		return leaveTypeDTO;
	}

	/**
	 * InActivate Leave Type Only change the Status Active to Inactive.
	 */
	public void inActivateLeaveType(Long leaveTypeId) {
		this.logger.info("Inside inActivateLeaveType( " + leaveTypeId + " )");
		if (!this.authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to create in-activate leave-type.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		if (leaveTypeId == null) {
			this.logger.error("Please enter a valid leave type id.");
			throw new UnprocessableEntityException("Please enter a valid leave type id.");
		}

		LeaveType leaveType = this.leaveTypeRepository.findOne(leaveTypeId);
		if (leaveType == null) {
			this.logger.error("Please enter a valid leave type id.");
			throw new UnprocessableEntityException("Please enter a valid leave type id.");
		}
		if (leaveType.isActive()) {
			leaveType.setActive(Boolean.FALSE);
			this.leaveTypeRepository.save(leaveType);
		}
		this.logger.info("Successfully return from inActivateLeaveType");
	}

	/**
	 * Find All Leave Type Which are Active.
	 * 
	 * @return list of LeaveTypeDTO.
	 */
	public List<LeaveTypeDTO> findAllActiveLeaveTypes() {
		this.logger.info("Indide findAllActiveLeaveTypes()");
		List<LeaveTypeDTO> leaveTypeDTOs = this.leaveTypeRepository.findAllLeaveType(Boolean.TRUE).stream()
				.map(LeaveTypeDTO::new).collect(Collectors.toList());
		this.logger.info("Fetched list of leavetype from DB :: size " + leaveTypeDTOs.size());
		this.logger.info("Successfully return from findAllActiveLeaveTypes");
		return leaveTypeDTOs;
	}

	/**
	 * Find All Leave Type.
	 * 
	 * @return list of LeaveTypeDTO.
	 */
	public List<LeaveTypeDTO> findAllLeaveTypes() {
		this.logger.info("Indide findAllLeaveTypes()");
		List<LeaveTypeDTO> leaveTypeDTOs = this.leaveTypeRepository.findAll().stream().map(LeaveTypeDTO::new)
				.sorted((dt, dt1) -> dt.getLeaveTypeId().compareTo(dt1.getLeaveTypeId())).collect(Collectors.toList());
		this.logger.info("Fetched list of leavetype from DB :: size " + leaveTypeDTOs.size());
		this.logger.info("Successfully return from findAllLeaveTypes");
		return leaveTypeDTOs;
	}

	/**
	 * find LeaveType by id.
	 * 
	 * @param leaveTypeId
	 * @return instance of LeaveType.
	 */
	public LeaveType findLeaveTypeById(Long leaveTypeId) {
		this.logger.info("Indide findLeaveTypeById( " + leaveTypeId + " )");
		LeaveType leaveType = this.leaveTypeRepository.findOne(leaveTypeId);
		if (leaveType == null || !leaveType.isActive()) {
			this.logger.error("Please enter a valid leave type id.");
			throw new UnprocessableEntityException("Please enter a valid leave type id.");
		}
		this.logger.info("Successfully return from findLeaveTypeById :: " + leaveType);
		return leaveType;
	}
}
