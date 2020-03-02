package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamium.domain.prod.resources.staff.StaffMemberSkill;

/**
 * To manage StaffMemberSkill.
 * 
 * @author Himanshu
 *
 */
@Repository
public interface StaffMemberSkillRepository extends JpaRepository<StaffMemberSkill, Long> {

	/**
	 * Get all StaffMemberSkill by matching skill id.
	 * 
	 * @param skillId
	 * @return List of StaffMemberSkill
	 */
	public List<StaffMemberSkill> findBySkillId(Long skillId);
}
