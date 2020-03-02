package com.teamium.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamium.domain.LeaveRecord;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.LeaveRecordRepository;

@Service
public class LeaveRecordService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private LeaveRecordRepository leaveRecordRepository;

	@Autowired
	public LeaveRecordService(LeaveRecordRepository leaveRecordRepository) {
		this.leaveRecordRepository = leaveRecordRepository;
	}

	/**
	 * find LeaveRecord by id.
	 * 
	 * @param leaveRecordId
	 * @return instance of LeaveRecord.
	 */
	public LeaveRecord findLeaveRecordById(Long leaveRecordId) {
		this.logger.info("Inside findLeaveRecordById( " + leaveRecordId + " )");
		LeaveRecord leaveRecord = this.leaveRecordRepository.findOne(leaveRecordId);
		if (leaveRecord == null) {
			this.logger.error("Please enter a valid leave record id.");
			throw new UnprocessableEntityException("Please enter a valid leave record id.");
		}
		this.logger.info("Successfully return from findLeaveRecordById :: " + leaveRecord);
		return leaveRecord;
	}
}
