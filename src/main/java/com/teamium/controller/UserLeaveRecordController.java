package com.teamium.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamium.dto.LeaveRequestDTO;
import com.teamium.dto.UserLeaveRecordDTO;
import com.teamium.service.UserLeaveRecordService;

@RestController
@RequestMapping("/user-leave-record")
public class UserLeaveRecordController {

	private UserLeaveRecordService userLeaveRecordService;

	@Autowired
	public UserLeaveRecordController(UserLeaveRecordService userLeaveRecordService) {
		this.userLeaveRecordService = userLeaveRecordService;
	}

	/**
	 * find user leave record for the staff.
	 * 
	 * Service URL: /user-leave-record/{staffMemberId} method: GET.
	 * 
	 * @return instance of UserLeaveRecordDTOs.
	 */
	@GetMapping(value = "/{staffMemberId}")
	public UserLeaveRecordDTO findUserLeaveRecordByStaffId(@PathVariable("staffMemberId") Long staffMemberId) {
		return userLeaveRecordService.findUserLeaveRecordByStaffId(staffMemberId);
	}

	/**
	 * save or update leave record.
	 * 
	 * Service URL: /user-leave-record/leave-record method: POST.
	 * 
	 * @return instance of UserLeaveRecordDTO.
	 */
	@PostMapping(value = "/leave-record")
	public UserLeaveRecordDTO saveOrUpdateLeaveRecord(@RequestBody UserLeaveRecordDTO userLeaveRecordDTO) {
		return userLeaveRecordService.saveOrUpdateLeaveRecord(userLeaveRecordDTO);
	}

	/**
	 * save or update leave request.
	 * 
	 * Service URL: /user-leave-record/leave-request method: POST.
	 * 
	 * @return instance of UserLeaveRecordDTO.
	 */
	@PostMapping(value = "/leave-request")
	public UserLeaveRecordDTO saveOrUpdateLeaveRequest(@RequestBody UserLeaveRecordDTO userLeaveRecordDTO) {
		return userLeaveRecordService.saveOrUpdateLeaveRequest(userLeaveRecordDTO);
	}

	/**
	 *Change status of leave request.
	 * 
	 * Service URL: /user-leave-record/leave-request/status method: POST.
	 * 
	 * @return instance of LeaveRequestDTO.
	 */
	@PostMapping(value = "/leave-request/status")
	public LeaveRequestDTO changeStatusOfLeaveRequest(@RequestBody LeaveRequestDTO leaveRequestDTO) {
		return userLeaveRecordService.changeStatusOfLeaveRequest(leaveRequestDTO);
	}
	
}
