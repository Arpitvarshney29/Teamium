package com.teamium.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamium.domain.prod.resources.functions.ExtraFunction;
import com.teamium.domain.prod.resources.functions.Rate;
import java.util.List;

public interface ExtraRepository extends JpaRepository<ExtraFunction, Long> {
	List<ExtraFunction> findByRate(Rate rate);
}
