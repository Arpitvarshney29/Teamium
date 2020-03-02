package com.teamium.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teamium.domain.prod.projects.Project;
import com.teamium.domain.prod.projects.order.Order;
import com.teamium.dto.OrderDTO;
import com.teamium.dto.ProjectDTO;
import com.teamium.dto.QuotationDTO;
import com.teamium.dto.RecordDTO;
import com.teamium.dto.SupplierDTO;
import com.teamium.service.EditionService;
import com.teamium.service.RecordService;

@RestController
@RequestMapping("/order")
public class OrderController {

	private RecordService recordService;
	private EditionService editionService;

	@Autowired
	public OrderController(RecordService recordService, EditionService editionService) {
		this.recordService = recordService;
		this.editionService = editionService;
	}

	/**
	 * To place an order related to a project-booking
	 * 
	 * Service URL:/order method: POST
	 * 
	 * @param orderDTO
	 * 
	 */
	@RequestMapping(method = RequestMethod.POST)
	OrderDTO placeOrder(@RequestBody OrderDTO orderDTO) {
		return recordService.placeOrder(orderDTO);
	}

	/**
	 * Method to get all orders
	 * 
	 * Service URL:/order method: GET
	 * 
	 * @return List of Orders
	 */
	@RequestMapping(method = RequestMethod.GET)
	List<RecordDTO> getOrders() {
		return recordService.getRecords(Order.class);
	}

	/**
	 * Method to get Order by id
	 * 
	 * Service URL:/order/{orderId} method: GET
	 * 
	 * @param orderId the orderId
	 * 
	 * @return the OrderDTO
	 */
	@RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
	OrderDTO findOrderById(@PathVariable long orderId) {
		return (OrderDTO) recordService.findRecordById(Order.class, orderId);
	}

	/**
	 * Method to get Orders by projectId
	 * 
	 * Service URL:/order/by-project/{projectId} method: GET
	 * 
	 * @param orderId the orderId
	 * 
	 * @return the list of Orders
	 */
	@RequestMapping(value = "/by-project/{projectId}", method = RequestMethod.GET)
	List<OrderDTO> findOrdersByProjectId(@PathVariable long projectId) {
		return recordService.getOrdersByProjectId(Project.class, projectId);
	}

	/**
	 * Method to get Orders by projectId
	 * 
	 * Service URL:/order/count/{orderId} method: GET
	 * 
	 * @param orderId the orderId
	 * 
	 * @return the list of Orders
	 */
	@RequestMapping(value = "/vendors/{projectId}", method = RequestMethod.GET)
	List<SupplierDTO> getVendorsForProcurment(@PathVariable("projectId") long projectId) {
		return recordService.getVendorsForProcurment(projectId);
	}

	/**
	 * To save and update Order
	 * 
	 * Service URL:/order/update method: POST
	 * 
	 * @param orderDTO the OrderDTO object.
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	OrderDTO saveOrUpdateProject(@RequestBody OrderDTO orderDTO) throws Exception {
		return (OrderDTO) recordService.updateOrder(orderDTO, OrderDTO.class);
	}

	
	/**
	 * Method to get callsheet pdf by project id
	 * 
	 * Service URL:/project/pdf/callsheet/ method: GET
	 * 
	 * @param projectId
	 * @param contactId
	 * @param orgnization
	 * @param location
	 * @param comment
	 * @return QuotationDTO
	 */
	@RequestMapping(value = "/pdf", method = RequestMethod.GET)
	RecordDTO getBudgetCallsheetPdf(@RequestParam long orderId, @RequestParam long contactId,
			@RequestParam(value = "", required = false) String comment,
			@RequestParam(value = "", required = false) String terms) {
		return editionService.generateOrderPdf(orderId, contactId);
	}

}
