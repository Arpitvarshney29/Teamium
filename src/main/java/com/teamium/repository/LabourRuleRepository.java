package com.teamium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.teamium.domain.LabourRule;

public interface LabourRuleRepository extends JpaRepository<LabourRule, Long> {

	@Query("SELECT lr from LabourRule lr WHERE lr.labourRuleName =:labourRuleName")
	LabourRule findLabourByName(@Param("labourRuleName") String labourRuleName);
}
