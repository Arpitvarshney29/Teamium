package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamium.domain.prod.projects.order.WorkAndTravelOrder;
import com.teamium.domain.prod.projects.order.WorkAndTravelOrder.OrderType;

@Repository
public interface WorkAndTravelOrderRepository extends JpaRepository<WorkAndTravelOrder, Long> {

	public List<WorkAndTravelOrder> findByOrderType(OrderType orderType);
	
	public List<WorkAndTravelOrder> findByMediaIdIgnoreCase(String mediaId);
}
