package com.teamium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamium.domain.Skill;
import java.lang.String;
import java.util.List;

/**
 * Repository related to skill operations.
 * 
 * @author Himanshu
 *
 */
@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

	/**
	 * Used for finding skills by the given name.
	 * 
	 * @param name
	 * @return
	 */
	List<Skill> findByName(String name);

	/**
	 * Used for finding skills by the given name ignoring case.
	 * 
	 * @param name
	 * @return
	 */
	Skill findByNameIgnoreCase(String name);
}
