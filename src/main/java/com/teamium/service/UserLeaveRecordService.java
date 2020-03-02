package com.teamium.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamium.constants.Constants;
import com.teamium.domain.LeaveRecord;
import com.teamium.domain.LeaveRequest;
import com.teamium.domain.LeaveType;
import com.teamium.domain.UserLeaveRecord;
import com.teamium.dto.LeaveRecordDTO;
import com.teamium.dto.LeaveRequestDTO;
import com.teamium.dto.LeaveTypeDTO;
import com.teamium.dto.UserLeaveRecordDTO;
import com.teamium.enums.LeaveRequestStatus;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.UserLeaveRecordRepository;

@Service
public class UserLeaveRecordService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private UserLeaveRecordRepository userLeaveRecordRepository;
	private StaffMemberService staffMemberService;
	private LeaveTypeService leaveTypeService;
	private LeaveRequestService leaveRequestService;
	private AuthenticationService authenticationService;

	@Autowired
	public UserLeaveRecordService(UserLeaveRecordRepository userLeaveRecordRepository,
			StaffMemberService staffMemberService, LeaveTypeService leaveTypeService,
			LeaveRequestService leaveRequestService, AuthenticationService authenticationService) {
		this.userLeaveRecordRepository = userLeaveRecordRepository;
		this.staffMemberService = staffMemberService;
		this.leaveTypeService = leaveTypeService;
		this.leaveRequestService = leaveRequestService;
		this.authenticationService = authenticationService;
	}

	/**
	 * find UserLeaveRecord From staffMemebrId.
	 * 
	 * @param staffMemberId
	 * @return instance of UserLeaveRecordDTO.
	 */
	public UserLeaveRecordDTO findUserLeaveRecordByStaffId(Long staffMemberId) {
		this.logger.info("Inside findUserLeaveRecordByStaffId( " + staffMemberId + " )");
		UserLeaveRecordDTO userLeaveRecordDTO = convertUserLeaveRecordToUserLeaveRecordDTO(
				findUserLeaveRecordByStaffIdFromService(staffMemberId));
		this.logger.info("Successfully return from findUserLeaveRecordByStaffId :: " + userLeaveRecordDTO);
		return userLeaveRecordDTO;
	}

	/**
	 * Save or Update Leave Record.
	 * 
	 * @param userLeaveRecordDTO
	 * @return instance of UserLeaveRecordDTO.
	 */
	public UserLeaveRecordDTO saveOrUpdateLeaveRecord(UserLeaveRecordDTO userLeaveRecordDTO) {
		this.logger.info("Inside saveOrUpdateLeaveRecord( " + userLeaveRecordDTO + " )");
		if (!this.authenticationService.isHumanResource()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to create save or update leave record.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		UserLeaveRecord userLeaveRecord = null;
		validateLeaveRecord(userLeaveRecordDTO);
		LeaveRecordDTO leaveRecordDTO = userLeaveRecordDTO.getChangeLeaveRecordDTO();
		if (userLeaveRecordDTO.getUserLeaveRecordId() != null) {
			userLeaveRecord = findUserLeaveRecordById(userLeaveRecordDTO.getUserLeaveRecordId());
		} else {
			userLeaveRecord = findUserLeaveRecordByStaffIdFromService(userLeaveRecordDTO.getStaffMemberId());
		}
		if (leaveRecordDTO.getLeaveRecordId() == null && userLeaveRecord.getLeaveRecords().stream()
				.filter(dt -> dt.getLeaveType() == null
						|| leaveRecordDTO.getLeaveTypeDTO().getLeaveTypeId().equals(dt.getLeaveType().getLeaveTypeId()))
				.collect(Collectors.toList()).size() > 0) {
			this.logger.error("leave type can not be duplicate.");
			throw new UnprocessableEntityException("leave type can not be duplicate.");
		}
		LeaveType leaveType = this.leaveTypeService
				.findLeaveTypeById(leaveRecordDTO.getLeaveTypeDTO().getLeaveTypeId());
		LeaveRecord leaveRecord = null;
		if (leaveRecordDTO.getLeaveRecordId() != null) {
			leaveRecord = userLeaveRecord.getLeaveRecords().stream()
					.filter(dt -> dt.getLeaveRecordId().equals(leaveRecordDTO.getLeaveRecordId())).findFirst()
					.orElse(null);
			if (leaveRecord == null) {
				this.logger.error("Please provide a valid leave record id.");
				throw new UnprocessableEntityException("Please provide a valid leave record id.");
			} else if (!leaveRecord.getLeaveType().getLeaveTypeId()
					.equals(leaveRecordDTO.getLeaveTypeDTO().getLeaveTypeId())) {
				this.logger.error("Please provide a valid leave type that is associated to leave record id.");
				throw new UnprocessableEntityException(
						"Please provide a valid leave type that is associated to leave record id.");
			}
			leaveRecord = leaveRecordDTO.getLeaveRecordEntity(leaveRecord);
		} else {
			leaveRecord = leaveRecordDTO.getLeaveRecordEntity(new LeaveRecord());
			leaveRecord.setLeaveType(leaveType);
			userLeaveRecord.getLeaveRecords().add(leaveRecord);
		}
		UserLeaveRecordDTO userLeaveRecordDTOs = convertUserLeaveRecordToUserLeaveRecordDTO(
				this.userLeaveRecordRepository.save(userLeaveRecord));
		this.logger.info("Successfully return from saveOrUpdateLeaveRecord :: " + userLeaveRecordDTO);
		return userLeaveRecordDTOs;
	}

	/**
	 * Save or Update Leave Request.
	 * 
	 * @param userLeaveRecordDTO
	 * @return instance of UserLeaveRecordDTO.
	 */
	public UserLeaveRecordDTO saveOrUpdateLeaveRequest(UserLeaveRecordDTO userLeaveRecordDTO) {
		this.logger.info("Inside saveOrUpdateLeaveRequest( " + userLeaveRecordDTO + " )");
		validateLeaveRequest(userLeaveRecordDTO);

		UserLeaveRecord userLeaveRecord = findUserLeaveRecordById(userLeaveRecordDTO.getUserLeaveRecordId());
		LeaveRequestDTO leaveRequestDTO = userLeaveRecordDTO.getChangeLeaveRequestDTO();

		Date startDate = DateTime.parse(leaveRequestDTO.getStartTime()).withZone(DateTimeZone.forID("Asia/Calcutta"))
				.toDate();
		Date endDate = DateTime.parse(leaveRequestDTO.getEndTime()).withZone(DateTimeZone.forID("Asia/Calcutta"))
				.toDate();
		if (userLeaveRecord.getLeaveRequests().stream()
				.filter(lr -> !lr.getLeaveStatus().equals(LeaveRequestStatus.REJECTED.getStatus()))
				.filter(lr -> endDate.compareTo(lr.getStartTime()) >= 0 && lr.getEndTime().compareTo(startDate) >= 0)
				.filter(dt -> !dt.getLeaveRequestId().equals(leaveRequestDTO.getLeaveRequestId())).findFirst()
				.orElse(null) != null) {
			this.logger.error("Already applied for the Leave on the selected dates.");
			throw new UnprocessableEntityException("Already applied for the Leave on the selected dates.");
		}

		LeaveRecordDTO leaveRecordDTO = leaveRequestDTO.getLeaveRecordDTO();

		LeaveRecord leaveRecord = userLeaveRecord.getLeaveRecords().stream()
				.filter(dt -> dt.getLeaveRecordId().equals(leaveRecordDTO.getLeaveRecordId()))
				.filter(dt -> dt.getCreditLeave() != 0).findFirst().orElse(null);
		if (leaveRecord == null || !leaveRecord.getLeaveType().isActive()) {
			this.logger.error("Please provide a valid leave record id.");
			throw new UnprocessableEntityException("Please provide a valid leave record id.");
		}
		LeaveRequest leaveRequest = null;
		if (leaveRequestDTO.getLeaveRequestId() != null) {
			leaveRequest = this.leaveRequestService
					.findLeaveRequestById(userLeaveRecordDTO.getChangeLeaveRequestDTO().getLeaveRequestId());
			if (leaveRequest.getLeaveRecord() == null
					|| !leaveRequest.getLeaveRecord().getLeaveRecordId().equals(leaveRecordDTO.getLeaveRecordId())) {
				this.logger.error("Please provide a valid leave record id.");
				throw new UnprocessableEntityException("Please provide a valid leave record id.");
			}
		} else {
			leaveRequest = leaveRequestDTO.getLeaveRequestEntity(new LeaveRequest());
			leaveRequest.setLeaveRecord(leaveRecord);
			userLeaveRecord.getLeaveRequests().add(leaveRequest);
		}
		if (LeaveRequestStatus.APPROVED.getStatus().equals(leaveRequest.getLeaveStatus())) {
			leaveRecord.setDebitedLeave(leaveRecord.getDebitedLeave() + leaveRequest.getNumberOfDay());
		}
		UserLeaveRecordDTO userLeaveRecordDTOs = convertUserLeaveRecordToUserLeaveRecordDTO(
				this.userLeaveRecordRepository.save(userLeaveRecord));
		this.logger.info("Successfully return from saveOrUpdateLeaveRequest :: " + userLeaveRecordDTO);
		return userLeaveRecordDTOs;
	}

	/**
	 * change the status of the Leave Request.
	 * 
	 * @param leaveRequestDTO
	 * @return instance of LeaveRequestDTO.
	 */
	public LeaveRequestDTO changeStatusOfLeaveRequest(LeaveRequestDTO leaveRequestDTO) {
		this.logger.info("Inside changeStatusOfLeaveRequest( " + leaveRequestDTO + " )");
		if (!this.authenticationService.isHumanResource()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to create change status of leave request.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		LeaveRequest leaveRequest = this.leaveRequestService.findLeaveRequestById(leaveRequestDTO.getLeaveRequestId());
		if (!leaveRequest.getLeaveStatus().equalsIgnoreCase(LeaveRequestStatus.REQUEST.getStatus())) {
			this.logger.error("Leave Request is Already : " + leaveRequest.getLeaveStatus());
			throw new UnprocessableEntityException("Leave Request is Already : " + leaveRequest.getLeaveStatus());
		}
		leaveRequest.setLeaveStatus(LeaveRequestStatus.getEnum(leaveRequestDTO.getLeaveStatus()).getStatus());
		LeaveRecord leaveRecord = leaveRequest.getLeaveRecord();
		if (LeaveRequestStatus.APPROVED.getStatus().equals(leaveRequest.getLeaveStatus())) {
			leaveRecord.setDebitedLeave(leaveRecord.getDebitedLeave() + leaveRequest.getNumberOfDay());
		}
		leaveRequestDTO = new LeaveRequestDTO(this.leaveRequestService.saveLeaveRequest(leaveRequest));
		this.logger.info("Successfully return from changeStatusOfLeaveRequest :: " + leaveRequestDTO);
		return leaveRequestDTO;
	}

	/**
	 * find UserLeaveRecord by staffMemberId.
	 * 
	 * @param staffMemberId
	 * @return instance of UserLeaveRecord.
	 */
	public UserLeaveRecord findUserLeaveRecordByStaffIdFromService(Long staffMemberId) {
		this.logger.info("Inside findUserLeaveRecordByStaffIdFromService( " + staffMemberId + " )");
		UserLeaveRecord userLeaveRecord = this.staffMemberService.findById(staffMemberId).getUserLeaveRecord();
		this.logger.info("Successfully return from findUserLeaveRecordByStaffIdFromService :: " + userLeaveRecord);
		return userLeaveRecord;
	}

	/**
	 * Convert UserLeaveRecord To UserLeaveRecordDTO.
	 * 
	 * @param userLeaveRecord
	 * @return instance of UserLeaveRecordDTO.
	 */
	public UserLeaveRecordDTO convertUserLeaveRecordToUserLeaveRecordDTO(UserLeaveRecord userLeaveRecord) {
		this.logger.info("Inside convertUserLeaveRecordToUserLeaveRecordDTO( " + userLeaveRecord + " )");
		UserLeaveRecordDTO userLeaveRecordDTO = new UserLeaveRecordDTO(userLeaveRecord);
		List<LeaveTypeDTO> userleaveTypeDTOs = userLeaveRecordDTO.getLeaveRecordDTOs().stream()
				.map(dt -> dt.getLeaveTypeDTO()).collect(Collectors.toList());

		List<LeaveTypeDTO> allleaveTypeDTOs = this.leaveTypeService.findAllActiveLeaveTypes();

		userLeaveRecordDTO.setAvailableLeaveRecordDTOs(userLeaveRecordDTO.getLeaveRecordDTOs().stream()
				.filter(dt -> allleaveTypeDTOs.contains(dt.getLeaveTypeDTO())).filter(dt -> dt.getCreditLeave() != 0)
				.collect(Collectors.toList()));

		userLeaveRecordDTO.getLeaveRecordDTOs()
				.addAll(allleaveTypeDTOs.stream().filter(leaveTypeDTO -> !userleaveTypeDTOs.contains(leaveTypeDTO))
						.map(leaveTypeDTO -> new LeaveRecordDTO(leaveTypeDTO)).collect(Collectors.toList()));
		this.logger
				.info("successfully return from convertUserLeaveRecordToUserLeaveRecordDTO :: " + userLeaveRecordDTO);
		return userLeaveRecordDTO;
	}

	/**
	 * find UserLeaveRecord by id.
	 * 
	 * @param userLeaveRecordId
	 * @return instance of UserLeaveRecord.
	 */
	public UserLeaveRecord findUserLeaveRecordById(Long userLeaveRecordId) {
		this.logger.info("Inside findUserLeaveRecordById( " + userLeaveRecordId + " )");
		UserLeaveRecord userLeaveRecord = this.userLeaveRecordRepository.findOne(userLeaveRecordId);
		if (userLeaveRecord == null) {
			this.logger.error("Please enter a valid user leave record id.");
			throw new UnprocessableEntityException("Please enter a valid user leave record id.");
		}
		this.logger.info("successfully return from findUserLeaveRecordById :: " + userLeaveRecord);
		return userLeaveRecord;
	}

	/**
	 * Validate Leave Record.
	 * 
	 * @param userLeaveRecordDTO
	 */
	public void validateLeaveRecord(UserLeaveRecordDTO userLeaveRecordDTO) {
		this.logger.info("Inside validateLeaveRecord( " + userLeaveRecordDTO + " )");
		validateUserLeaveRecord(userLeaveRecordDTO);
		LeaveRecordDTO leaveRecordDTO = userLeaveRecordDTO.getChangeLeaveRecordDTO();
		if (leaveRecordDTO == null) {
			this.logger.error("Please provide leave record for save.");
			throw new UnprocessableEntityException("Please provide leave record for save.");
		}
		if (leaveRecordDTO.getCreditLeave() < 0) {
			this.logger.error("Credited leave can not be negative.");
			throw new UnprocessableEntityException("Credited leave can not be negative.");
		}
		if (leaveRecordDTO.getDebitedLeave() < 0) {
			this.logger.error("Debited leave can not be negative.");
			throw new UnprocessableEntityException("Debited leave can not be negative.");
		}
		if (leaveRecordDTO.getLeaveTypeDTO() == null || leaveRecordDTO.getLeaveTypeDTO().getLeaveTypeId() == null) {
			this.logger.error("Please provide leave type.");
			throw new UnprocessableEntityException("Please provide leave type.");
		}
		this.logger.info("successfully return from validateLeaveRecord");
	}

	/**
	 * Validate Leave Request.
	 */
	public void validateLeaveRequest(UserLeaveRecordDTO userLeaveRecordDTO) {
		this.logger.info("Inside validateLeaveRequest( " + userLeaveRecordDTO + " )");
		validateUserLeaveRecord(userLeaveRecordDTO);
		LeaveRequestDTO leaveRequestDTO = userLeaveRecordDTO.getChangeLeaveRequestDTO();
		if (leaveRequestDTO == null) {
			this.logger.error("Please provide leave request for save.");
			throw new UnprocessableEntityException("Please provide leave request for save.");
		}
		if (leaveRequestDTO.getLeaveStatus() == null) {
			this.logger.error("Please provide satus.");
			throw new UnprocessableEntityException("Please provide satus.");
		}
		if (leaveRequestDTO.getStartTime() == null) {
			this.logger.error("Please provide start date.");
			throw new UnprocessableEntityException("Please provide start date.");
		}
		if (leaveRequestDTO.getEndTime() == null) {
			this.logger.error("Please provide end date.");
			throw new UnprocessableEntityException("Please provide end date.");
		}
		DateTime startDate = DateTime.parse(leaveRequestDTO.getStartTime())
				.withZone(DateTimeZone.forID("Asia/Calcutta"));
		DateTime endDate = DateTime.parse(leaveRequestDTO.getEndTime()).withZone(DateTimeZone.forID("Asia/Calcutta"));
		if (endDate.toDate().before(startDate.toDate())) {
			this.logger.info("start date can not be smaller than end date.");
			throw new NotFoundException("start date can not be smaller than end date.");
		}
		int totalDays = Days.daysBetween(startDate, endDate).getDays() + 1;
		if (totalDays < leaveRequestDTO.getNumberOfDay()) {
			this.logger.info("Days Difference can not less than Number of days.");
			throw new NotFoundException("Days Difference can not less than Number of days.");
		}
		LeaveRecordDTO leaveRecordDTO = leaveRequestDTO.getLeaveRecordDTO();
		if (leaveRecordDTO == null || leaveRecordDTO.getLeaveRecordId() == null) {
			this.logger.error("Please provide leave record.");
			throw new UnprocessableEntityException("Please provide leave record.");
		}
		if (leaveRecordDTO.getLeaveTypeDTO() == null || leaveRecordDTO.getLeaveTypeDTO().getLeaveTypeId() == null) {
			this.logger.error("Please provide leave type.");
			throw new UnprocessableEntityException("Please provide leave type.");
		}
		this.logger.info("successfully return from validateLeaveRequest");
	}

	/**
	 * Validate User Leave Record.
	 */
	public void validateUserLeaveRecord(UserLeaveRecordDTO userLeaveRecordDTO) {
		this.logger.info("Inside validateUserLeaveRecord( " + userLeaveRecordDTO + " )");
		if (userLeaveRecordDTO.getStaffMemberId() == null && userLeaveRecordDTO.getUserLeaveRecordId() == null) {
			this.logger.error("Please provide either staff member id or user leave record id.");
			throw new UnprocessableEntityException("Please provide either staff member id or user leave record id.");
		}
		this.logger.info("successfully return from validateUserLeaveRecord");
	}
}
