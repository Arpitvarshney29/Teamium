package com.teamium.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamium.domain.Skill;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnauthorizedException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.SkillRepository;

/**
 * Service to manager the skills.
 * 
 * @author Himanshu
 *
 */
@Service
public class SkillService {

	private SkillRepository skillRepository;
	private AuthenticationService authenticationService;
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	private StaffMemberSkillService staffMemberSkillService;

	/**
	 * @param skillRepository
	 * @param authenticationService
	 */
	@Autowired
	public SkillService(SkillRepository skillRepository, AuthenticationService authenticationService,
			StaffMemberSkillService staffMemberSkillService) {
		this.skillRepository = skillRepository;
		this.authenticationService = authenticationService;
		this.staffMemberSkillService = staffMemberSkillService;
	}

	/**
	 * To add skill.
	 * 
	 * @param skill
	 * @return
	 */
	public Skill addSkill(Skill skill) {
		logger.info("Inside SkillService :: addSkill(), To add skill: " + skill);

		if (!authenticationService.isAdmin()) {
			logger.error("An invalid user: " + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to add skill.");
			throw new UnauthorizedException("You do not have authority to add skills.");
		}

		if (skill.getName() == null || skill.getName().trim().isEmpty()) {
			logger.error("Provided skill name is empty.");
			throw new UnprocessableEntityException("The skill name you provided cannot be empty.");
		}

		// Remove extra space.
		skill.setName(skill.getName().trim());

		if (skill.getName().matches("[0-9]+")) {
			logger.error("The skill name you provided only contains numbers.");
			throw new UnprocessableEntityException("The skill name you provided only contains numbers.");
		}

		if (skill.getId() != null && skillRepository.findOne(skill.getId()) == null) {
			logger.error("Skill id is not valid.");
			throw new NotFoundException("Skill id is not valid.");
		}

		Skill skillInDb = skillRepository.findByNameIgnoreCase(skill.getName());
		if (skillInDb != null
				&& !(skill.getId() != null && (skill.getId().longValue() == skillInDb.getId().longValue()))) {
			logger.error("Skill already exists.");
			throw new UnprocessableEntityException("Skill already exists.");
		}

		logger.info("Returning from SkillService :: addSkill()");
		return skillRepository.save(skill);
	}

	/**
	 * To get skill by id.
	 * 
	 * @param skillId
	 * @return
	 */
	public Skill getSkillById(Long skillId) {
		logger.info("Inside SkillService :: getSkillById(), To get skill with id : " + skillId);

		if (!authenticationService.isAdmin()) {
			logger.error("You do not have authority to get skill.");
			throw new UnauthorizedException("You do not have authority to get skill.");
		}

		Skill skill = skillRepository.findOne(skillId);
		if (skill == null) {
			logger.error("Skill id is not valid.");
			throw new NotFoundException("Skill id is not valid.");
		}

		logger.info("Returning from SkillService :: getSkillById()");
		return skill;
	}

	/**
	 * To get all skills.
	 * 
	 * @return
	 */
	public List<Skill> getSkills() {
		logger.info("Inside SkillService :: getSkills()");
		return skillRepository.findAll().stream().sorted((x, y) -> x.getName().compareToIgnoreCase(y.getName()))
				.collect(Collectors.toList());
	}

	/**
	 * To get all skill name.
	 * 
	 * @return
	 */
	public List<String> getSkillNames() {
		logger.info("Inside SkillService :: getSkillNames()");
		List<String> skills = skillRepository.findAll().stream().map(x -> x.getName())
				.sorted((x, y) -> x.compareToIgnoreCase(y)).collect(Collectors.toList());
		return skills == null ? new ArrayList<String>() : skills;
	}

	/**
	 * To remove skill by given id.
	 * 
	 * @param skillId
	 */
	public void removeSkillById(Long skillId) {
		logger.info("Inside SkillService :: removeSkillById(), To remove skill with id : " + skillId);

		if (!authenticationService.isAdmin()) {
			logger.error("You do not have authority to add skills.");
			throw new UnauthorizedException("You do not have authority to add skills.");
		}

		Skill skill = skillRepository.findOne(skillId);
		if (skill == null) {
			logger.error("Skill id is not valid.");
			throw new NotFoundException("Skill id is not valid.");
		}

		if (!staffMemberSkillService.findBySkillId(skillId).isEmpty()) {
			logger.error("The skill is asigned to personnel(s).");
			throw new NotFoundException("The skill is asigned to personnel(s).");
		}

		logger.info("Returning from SkillService :: removeSkillById()");
		skillRepository.delete(skillId);
	}

	/**
	 * Find skill by skill name.
	 * 
	 * @param skillName
	 * @return
	 */
	public Skill findBySkillName(String skillName) {
		logger.info("Inside SkillService :: findBySkillName(), To find skill by skill name : " + skillName);

		Skill skill = skillRepository.findByNameIgnoreCase(skillName);
		if (skill == null) {
			logger.error("Cannot find the skill by given skill name.");
			throw new NotFoundException("Cannot find the skill by given skill name.");
		}
		logger.info("Returning from SkillService :: findBySkillName()");
		return skill;
	}
}
