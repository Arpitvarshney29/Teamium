package com.teamium.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.teamium.domain.LeaveRequest;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.LeaveRequestRepository;

@Service
public class LeaveRequestService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private LeaveRequestRepository leaveRequestRepository;

	public LeaveRequestService(LeaveRequestRepository leaveRequestRepository) {
		this.leaveRequestRepository = leaveRequestRepository;
	}

	/**
	 * find LeaveRequest by id.
	 * 
	 * @param leaveRequestId
	 * @return instance of LeaveRequest.
	 */
	public LeaveRequest findLeaveRequestById(Long leaveRequestId) {
		this.logger.info("Inside findLeaveRequestById( " + leaveRequestId + " )");
		LeaveRequest leaveRequest = this.leaveRequestRepository.findOne(leaveRequestId);
		if (leaveRequest == null) {
			this.logger.error("Please enter a valid leave request id.");
			throw new UnprocessableEntityException("Please enter a valid leave request id.");
		}
		this.logger.info("Successfully return from findLeaveRequestById :: " + leaveRequestId);
		return leaveRequest;
	}

	/**
	 * Save the leave request.
	 * 
	 * @param leaveRequest
	 * @return instance of LeaveRequest.
	 */
	public LeaveRequest saveLeaveRequest(LeaveRequest leaveRequest) {
		this.logger.info("Inside saveLeaveRequest( " + leaveRequest + " )");
		return this.leaveRequestRepository.save(leaveRequest);
	}
}
