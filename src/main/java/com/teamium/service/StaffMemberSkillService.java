package com.teamium.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamium.domain.prod.resources.staff.StaffMemberSkill;
import com.teamium.exception.NotFoundException;
import com.teamium.repository.StaffMemberSkillRepository;

/**
 * To manage the StaffMemberSkill.
 * 
 * @author Himanshu
 *
 */
@Service
public class StaffMemberSkillService {

	@Autowired
	private StaffMemberSkillRepository staffMemberSkillRepository;
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	public void removeStaffMemberSkillById(Long staffMemberSkillId) {
		logger.info("Inside StaffMemberSkillService:removeStaffMemberSkillById(" + staffMemberSkillId
				+ "),To remove staff member skill");
		if (staffMemberSkillRepository.findOne(staffMemberSkillId) == null) {
			logger.error("Given staffMemberSkillId is invalid.");
			throw new NotFoundException("Given staffMemberSkillId is invalid.");
		}

		logger.info("Returning from StaffMemberSkillService:removeStaffMemberSkillById()");
		staffMemberSkillRepository.delete(staffMemberSkillId);
	}

	/**
	 * To find StaffMemberSkills by skill id.
	 * 
	 * @param skillId
	 * @return List of StaffMemberSkill
	 */
	public List<StaffMemberSkill> findBySkillId(Long skillId) {
		logger.info("Inside findBySkillId(" + skillId + "),To find by skillId");
		return staffMemberSkillRepository.findBySkillId(skillId);
	}
}
