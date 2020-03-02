package com.teamium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamium.domain.TimeZone;

@Repository
public interface TimeZoneRepository extends JpaRepository<TimeZone, Long> {

	public TimeZone findByZoneNameAndIsCheck(String zoneName,boolean isCheck);

}
