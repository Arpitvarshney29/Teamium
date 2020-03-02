package com.teamium.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.prod.projects.order.Order;
import com.teamium.domain.prod.projects.order.OrderLine;

/**
 * DTO class for OrderLine Entity
 * 
 * @author Teamium
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrderLineDTO extends LineWrapperDTO {

	public OrderLineDTO() {
		super();
	}

	public OrderLineDTO(OrderLine orderLine) {
		super(orderLine);
	}

	/**
	 * Get Order-Line entity from DTO
	 * 
	 * @param orderLine
	 * 
	 * @return OrderLine
	 */
	@JsonIgnore
	public OrderLine getOrderLine(OrderLine orderLine) {
		super.getLineWrapper(orderLine);
		return orderLine;
	}

}
