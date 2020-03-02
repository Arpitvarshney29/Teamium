package com.teamium.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamium.domain.prod.projects.order.OrderLine;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {
	@Query("SELECT o FROM OrderLine o WHERE  o.from <= :endDate AND o.to >= :startDate ORDER BY o.from ASC")
	List<OrderLine> findByProjectAndStartDateAndEndDate(@Param("startDate") Calendar startDate,
			@Param("endDate") Calendar endDate);

}
