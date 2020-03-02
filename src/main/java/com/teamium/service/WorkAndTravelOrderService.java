package com.teamium.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamium.domain.prod.projects.order.WorkAndTravelOrder;
import com.teamium.exception.NotFoundException;
import com.teamium.repository.WorkAndTravelOrderRepository;

@Service
public class WorkAndTravelOrderService {

	private WorkAndTravelOrderRepository workAndTravelOrderRepository;
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public WorkAndTravelOrderService(WorkAndTravelOrderRepository workAndTravelOrderRepository) {
		this.workAndTravelOrderRepository = workAndTravelOrderRepository;
	}

	/**
	 * Finding WorkAndTravelOrder by id.
	 * 
	 * @param id
	 * @return
	 */
	public WorkAndTravelOrder findById(long id) {
		logger.info("Inside WorkAndTravelOrderService :: findById(" + id + ") : To find all WorkAndTravelOrders.");
		WorkAndTravelOrder workAndTravelOrder = workAndTravelOrderRepository.findOne(id);
		if (workAndTravelOrder == null) {
			logger.error("Invalid WorkAndTravelOrder id.");
			throw new NotFoundException("Invalid WorkAndTravelOrder id.");
		}
		logger.info("Returning from findById().");
		return workAndTravelOrder;
	}

	/**
	 * Removing WorkAndTravelOrder by id.
	 * 
	 * @param workAndTravelOrderId
	 */
	public void remove(long workAndTravelOrderId) {
		logger.info("Inside WorkAndTravelOrderService :: remove(" + workAndTravelOrderId
				+ ") : To remove WorkAndTravelOrder.");
		WorkAndTravelOrder workAndTravelOrder = workAndTravelOrderRepository.findOne(workAndTravelOrderId);
		if (workAndTravelOrder == null) {
			logger.error("Invalid WorkAndTravelOrder id.");
			throw new NotFoundException("Invalid WorkAndTravelOrder id.");
		}
		workAndTravelOrderRepository.delete(workAndTravelOrderId);
		logger.info("Returning from remove().");
	}

	/**
	 * For saving the WorkAndTravelOrder.
	 * 
	 * @param workAndTravelOrder
	 * @return
	 */
	public WorkAndTravelOrder save(WorkAndTravelOrder workAndTravelOrder) {
		return workAndTravelOrderRepository.save(workAndTravelOrder);
	}

	/**
	 * Find WorkAndTravelOrders by media id ignoring case.
	 * 
	 * @param mediaId
	 * @return
	 */
	public List<WorkAndTravelOrder> findByMediaIdIgnoreCase(String mediaId) {
		return workAndTravelOrderRepository.findByMediaIdIgnoreCase(mediaId);
	}

}
