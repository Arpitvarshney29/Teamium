package com.teamium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamium.domain.ProductionUnit;

/**
 * Repository interface for Production-Unit Domain class to handle the all
 * related operation
 *
 */

@Repository
public interface ProductionUnitRepository extends JpaRepository<ProductionUnit, Long> {

}
