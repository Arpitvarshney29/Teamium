package com.teamium.repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamium.domain.prod.projects.order.OrderLine;
import com.teamium.domain.prod.resources.equipments.EquipmentFunction;
import com.teamium.domain.prod.resources.equipments.EquipmentResource;
import com.teamium.domain.prod.resources.staff.StaffFunction;
import com.teamium.domain.prod.resources.staff.StaffResource;

@Service
public class OrderLineService {
	private OrderLineRepository orderLineRepository;
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public OrderLineService(OrderLineRepository orderLineRepository) {
		this.orderLineRepository = orderLineRepository;
	}

	/**
	 * To get externalbookings By Function And Resource And StartDate And EndDate.
	 * 
	 * @param functionType
	 * @param startDate
	 * @param endDate
	 * @return externalbookings
	 */
	public List<OrderLine> findExternalBookingByFunctionAndResourceAndStartDateAndEndDate(String functionType,
			Calendar startDate, Calendar endDate) {
		logger.info("Inside BookingService,findExternalBookingByFunctionAndResourceAndStartDateAndEndDate: "
				+ "startDate : " + startDate + "endDate : " + endDate);
		startDate.set(Calendar.SECOND, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.HOUR, 0);
		
		endDate.set(Calendar.SECOND, 0);
		endDate.set(Calendar.MINUTE, 0);
		endDate.set(Calendar.HOUR, 0);
		List<OrderLine> externalbookings = new ArrayList<>();
		externalbookings = orderLineRepository.findByProjectAndStartDateAndEndDate(startDate, endDate);
		if (functionType.equals("EquipmentFunction")) {
			logger.info("Geting Bookings for all EquipmentFunction");
			externalbookings = externalbookings.stream().filter(b -> b.getFunction() instanceof EquipmentFunction)
					.collect(Collectors.toList());
		}
		logger.info("Returning after getting external bookings");
		return externalbookings;
	}
}
