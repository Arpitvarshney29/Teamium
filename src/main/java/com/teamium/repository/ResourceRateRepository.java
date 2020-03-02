package com.teamium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.teamium.domain.prod.resources.staff.ResourceRate;

public interface ResourceRateRepository extends JpaRepository<ResourceRate, Long> {

	@Query("SELECT coalesce(max(r.value), 0) FROM ResourceRate r")
	Float getMaxValue();
	
	@Query("SELECT coalesce(min(r.value), 0) FROM ResourceRate r")
	Float getMinValue();

}
