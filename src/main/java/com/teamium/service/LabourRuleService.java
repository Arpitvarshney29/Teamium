package com.teamium.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.teamium.domain.Holiday;
import com.teamium.domain.LabourRule;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.dto.HolidayDTO;
import com.teamium.dto.LabourRuleDTO;
import com.teamium.dto.StaffMemberDTO;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.HolidayRepository;
import com.teamium.repository.LabourRuleRepository;

@Service
public class LabourRuleService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private LabourRuleRepository labourRuleRepository;
	private HolidayRepository holidayRepository;
	private StaffMemberService staffMemberService;

	@Autowired
	public LabourRuleService(LabourRuleRepository labourRuleRepository, HolidayRepository holidayRepository,
			StaffMemberService staffMemberService) {
		this.labourRuleRepository = labourRuleRepository;
		this.holidayRepository = holidayRepository;
		this.staffMemberService = staffMemberService;
	}

	/**
	 * Save or update LabourRule.
	 * 
	 * @param labourRuleDTO
	 * @return labourRuleDTO
	 */
	public LabourRuleDTO saveOrUpdateLabourRule(LabourRuleDTO labourRuleDTO) {
		this.logger.info("Inside saveOrUpdateLabourRule( " + labourRuleDTO + ")");
		validateLabourRule(labourRuleDTO);
		LabourRule labourRule = null;
		if (labourRuleDTO.getLabourRuleId() != null) {
			labourRule = labourRuleDTO.getLabourEntity(getLabourRuleById(labourRuleDTO.getLabourRuleId()));
		} else {
			labourRule = labourRuleDTO.getLabourEntity(new LabourRule());
		}
		labourRuleDTO = new LabourRuleDTO(this.labourRuleRepository.save(labourRule));

		this.logger.info("Successfully return saveOrUpdateLabourRule :: " + labourRuleDTO);
		return labourRuleDTO;
	}

	/**
	 * find LabourRule by id.
	 * 
	 * @param labourRuleId
	 * @return labourRuleDTO
	 */
	public LabourRuleDTO findLabourRuleById(Long labourRuleId) {
		this.logger.info("Inside findLabourRuleById( " + labourRuleId + ")");
		LabourRule labourRule = getLabourRuleById(labourRuleId);
		LabourRuleDTO labourRuleDTO = new LabourRuleDTO(labourRule);
		this.logger.info("Successfully return findLabourRuleById :: " + labourRuleDTO);
		return labourRuleDTO;
	}

	/**
	 * fetch all LabourRule.
	 * 
	 * @return list of LabourRuleDTO.
	 */
	public List<LabourRuleDTO> fetchAllLabourRule() {
		this.logger.info("Inside fetchAllLabourRule()");
		List<LabourRuleDTO> labourRuleDTOs = this.labourRuleRepository.findAll().stream().map(LabourRuleDTO::new)
				.collect(Collectors.toList());
		this.logger.info("Successfully return fetchAllLabourRule :: " + labourRuleDTOs.size());
		return labourRuleDTOs;
	}

	/**
	 * find LabourRule by id.
	 * 
	 * @param labourRuleId
	 * @return LabourRule.
	 */
	public LabourRule getLabourRuleById(Long labourRuleId) {
		this.logger.info("Inside getLabourRuleById( " + labourRuleId + " )");
		if (labourRuleId == null) {
			this.logger.info("Please enter valid LabourRule Id : " + labourRuleId);
			throw new NotFoundException("Please enter valid LabourRule Id : " + labourRuleId);
		}
		LabourRule labourRule = this.labourRuleRepository.findOne(labourRuleId);
		if (labourRule == null) {
			this.logger.info("Please enter valid LabourRule Id : " + labourRuleId);
			throw new NotFoundException("Please enter valid LabourRule Id : " + labourRuleId);
		}
		this.logger.info("Successfully return getLabourRuleById :: " + labourRule);
		return labourRule;
	}

	/**
	 * find Holiday by id.
	 * 
	 * @param holidayId
	 * @return Holiday.
	 */
	public Holiday getHolidayById(Long holidayId) {
		this.logger.info("Inside getHolidayById( " + holidayId + " )");
		if (holidayId == null) {
			this.logger.info("Please enter valid Holiday Id : " + holidayId);
			throw new NotFoundException("Please enter valid Holiday Id : " + holidayId);
		}
		Holiday holiday = this.holidayRepository.findOne(holidayId);
		if (holiday == null) {
			this.logger.info("Please enter valid Holiday Id : " + holidayId);
			throw new NotFoundException("Please enter valid Holiday Id : " + holidayId);
		}
		this.logger.info("Successfully return getHolidayById :: " + holiday);
		return holiday;
	}

	/**
	 * save or update Holiday for a particular LabourRule.
	 * 
	 * @param holidayDTO
	 * @return list of HolidayDTO.
	 */
	public List<HolidayDTO> saveOrUpdateHolidayForLabourRule(HolidayDTO holidayDTO) {
		this.logger.info("Inside saveOrUpdateHolidayForLabourRule( " + holidayDTO + " )");
		validateHoliday(holidayDTO);
		LabourRule labourRule = getLabourRuleById(holidayDTO.getLaborRuleId());
		if (labourRule.getHolidays().stream()
				.filter(dt -> dt.getHolidayName() != null
						&& dt.getHolidayName().equalsIgnoreCase(holidayDTO.getHolidayName()))
				.filter(dt -> dt.getId() != null
						&& (holidayDTO.getId() == null || !dt.getId().equals(holidayDTO.getId())))
				.findFirst().orElse(null) != null) {
			this.logger.info("Holiday already added.");
			throw new NotFoundException("Holiday already added.");
		}

		Holiday holiday = null;
		if (holidayDTO.getId() != null) {
			holiday = holidayDTO.getHolidayEntity(getHolidayById(holidayDTO.getId()));
			// labourRule.getHolidays().add(holiday);
		} else {
			holiday = holidayDTO.getHolidayEntity(new Holiday());
			labourRule.getHolidays().add(holiday);
		}
		// labourRule.getHolidays().add(holiday);
		List<HolidayDTO> holidayDTOs = this.labourRuleRepository.save(labourRule).getHolidays().stream()
				.map(HolidayDTO::new).collect(Collectors.toList());
		this.logger.info("successfully return from saveOrUpdateHolidayForLabourRule :: " + holidayDTOs);
		return holidayDTOs;
	}

	private void validateHoliday(HolidayDTO holidayDTO) {
		this.logger.info("Inside validateHoliday( " + holidayDTO + " )");
		if (holidayDTO.getHolidayName() == null || StringUtils.isBlank(holidayDTO.getHolidayName())) {
			this.logger.info("Please enter a Holiday Name.");
			throw new NotFoundException("Please enter a Holiday Name.");
		}
		if (holidayDTO.getHolidayDate() == null) {
			this.logger.info("Please enter a Holiday Date.");
			throw new NotFoundException("Please enter a Holiday Date.");
		}
		Date holidayDate = DateTime.parse(holidayDTO.getHolidayDate()).withZone(DateTimeZone.forID("Asia/Calcutta"))
				.toDate();
		if (holidayDate.before(new DateTime().withZone(DateTimeZone.forID("Asia/Calcutta")).toDate())) {
			this.logger.info("Holiday Date can not be past date.");
			throw new NotFoundException("Holiday Date can not be past date.");
		}

	}

	/**
	 * Assign StaffMember To LabourRule.
	 * 
	 * @param labourId
	 * @param staffmemberId
	 * @return list of StaffMemberDTO.
	 */
	public List<StaffMemberDTO> assignStaffMemberToLabourRule(Long labourRuleId, Long staffMemberId) {
		this.logger.info("Inside assignStaffMemberToLabourRule( " + labourRuleId + " , " + staffMemberId + " )");
		LabourRule labourRule = getLabourRuleById(labourRuleId);
		StaffMember staffMember = this.staffMemberService.validateStaffMember(staffMemberId);
		labourRule.getStaffMembers().add(staffMember);
		List<StaffMemberDTO> staffMemberDTOs = this.labourRuleRepository.save(labourRule).getStaffMembers().stream()
				.map(StaffMemberDTO::new).collect(Collectors.toList());
		this.logger.info("Successfully return from assignStaffMemberToLabourRule :: " + staffMemberDTOs.size());
		return staffMemberDTOs;
	}

	/**
	 * 
	 * Remove Holiday From the Labour Rule.
	 * 
	 * @param labourRuleId
	 * @param holydayId
	 */
	public void removeHolidayFromLabourRule(Long labourRuleId, Long holydayId) {
		this.logger.info("Inside removeHolidayFromLabourRule( " + labourRuleId + " , " + holydayId + " )");
		LabourRule labourRule = getLabourRuleById(labourRuleId);
		labourRule.getHolidays().removeIf(holyday -> holyday.getId() == holydayId);
		this.labourRuleRepository.save(labourRule);
		this.logger.info("Successfully return from removeHolidayFromLabourRule");
	}

	/**
	 * Remove Staff Member From Labour Rule.
	 * 
	 * @param labourRuleId
	 * @param staffMemberId
	 */
	public void removeStaffMemberFromLabourRule(Long labourRuleId, Long staffMemberId) {
		this.logger.info("Inside removeStaffMemberFromLabourRule( " + labourRuleId + " , " + staffMemberId + " )");
		LabourRule labourRule = getLabourRuleById(labourRuleId);
		labourRule.getStaffMembers().removeIf(staff -> staff.getId() == staffMemberId);
		this.labourRuleRepository.save(labourRule);
		this.logger.info("Successfully return from removeStaffMemberFromLabourRule");
	}

	/**
	 * @return the labourRuleRepository
	 */
	public LabourRuleRepository getLabourRuleRepository() {
		return labourRuleRepository;
	}

	/**
	 * 
	 * @param labourRuleDTO
	 */
	private void validateLabourRule(LabourRuleDTO labourRuleDTO) {
		this.logger.info("Inside validateLabourRule( " + labourRuleDTO + " )");
		if (labourRuleDTO.getLabourRuleName() == null || StringUtils.isBlank(labourRuleDTO.getLabourRuleName())) {
			this.logger.info("Please enter a Labour rule Name.");
			throw new NotFoundException("Please enter a Labour rule Name.");
		}
		LabourRule labourRule = this.labourRuleRepository
				.findLabourByName(labourRuleDTO.getLabourRuleName().toLowerCase());
		if (labourRule != null) {
			if (labourRuleDTO.getLabourRuleId() != null) {
				if (!labourRule.getLabourRuleId().equals(labourRuleDTO.getLabourRuleId())) {
					this.logger.info("Labour rule name can not duplicate.");
					throw new NotFoundException("Labour rule name can not duplicate.");
				}
			} else {
				this.logger.info("Labour rule name can not duplicate.");
				throw new NotFoundException("Labour rule name can not duplicate.");
			}

		}
		this.logger.info("Inside validateLabourRule( " + labourRuleDTO + " )");
	}

	/**
	 * delete labour rule by labour rule id
	 * 
	 * @param labourRuleId
	 */
	public void deleteLabourRuleById(Long labourRuleId) {
		this.logger.info(
				"Inside .LabourRuleService :: deleteLabourRuleById(Long labourRuleId) , delete labour rule by Id: "
						+ labourRuleId);
		LabourRule labourRule = getLabourRuleById(labourRuleId);

		try {
			this.labourRuleRepository.delete(labourRule);
		} catch (Exception e) {
			logger.error("Cannot delete labour rule as it is assign to personnel.");
			throw new UnprocessableEntityException("Cannot delete labour rule as it is assign to personnel. ");
		}
		this.logger.info("Successfully delete labour rule by Id: " + labourRuleId);
	}

}
