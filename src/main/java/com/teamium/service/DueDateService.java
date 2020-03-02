package com.teamium.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamium.domain.prod.DueDate;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.DueDateRepository;

/**
 * Service to manage due-date
 * 
 * @author Teamium
 *
 */
@Service
public class DueDateService {

	private DueDateRepository dueDateRepository;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public DueDateService(DueDateRepository dueDateRepository) {
		this.dueDateRepository = dueDateRepository;
	}

	/**
	 * Method to delete due-date
	 * 
	 * @param dueDateId
	 */
	public void unassignDueDate(Long dueDateId) {
		logger.info("Inside DueDateService :: unassignDueDate() :: unassign due-date with id: " + dueDateId);
		DueDate dueDate = this.validateDueDate(dueDateId);
		dueDate.setType(null);
		dueDateRepository.save(dueDate);
		logger.info("Returning from unassignDueDate()");
	}

	/**
	 * To Validate DueDate by dueDateId
	 * 
	 * @param dueDateId the dueDateId
	 * 
	 * @return the DueDate object
	 */
	private DueDate validateDueDate(Long dueDateId) {
		logger.info("Inside DueDateService :: validateDueDate() :: validating due-date with id: " + dueDateId);
		if (dueDateId == null) {
			logger.error("Milestone id is null");
			throw new NotFoundException("Milestone id can not be null");
		}
		DueDate milestone = dueDateRepository.findOne(dueDateId);
		if (milestone == null) {
			logger.error("Milestone not found ");
			throw new UnprocessableEntityException("Milestone not found");
		}
		logger.info("Returning from validateDocument() .");
		return milestone;
	}

}
