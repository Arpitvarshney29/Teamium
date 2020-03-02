package com.teamium.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamium.domain.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

}
