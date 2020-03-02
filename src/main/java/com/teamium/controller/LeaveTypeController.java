package com.teamium.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.teamium.dto.LeaveTypeDTO;
import com.teamium.service.LeaveTypeService;

@RestController
@RequestMapping("/leave-type")
public class LeaveTypeController {

	private LeaveTypeService leaveTypeService;

	@Autowired
	public LeaveTypeController(LeaveTypeService leaveTypeService) {
		this.leaveTypeService = leaveTypeService;
	}

	/**
	 * To save or update leave type.
	 * 
	 * Service URL: /leave-type method: POST.
	 * 
	 * @param leaveTypeDTO
	 * @return instance of LeaveTypeDTO.
	 */
	@PostMapping
	public LeaveTypeDTO saveOrUpdateLeaveType(@RequestBody LeaveTypeDTO leaveTypeDTO) {
		return this.leaveTypeService.saveOrUpdateLeaveType(leaveTypeDTO);
	}

	/**
	 * find all LeaveTypes which are Active.
	 * 
	 * Service URL: /leave-type method: GET.
	 * 
	 * @return list of LeaveTypeDTO.
	 */
	@GetMapping
	public List<LeaveTypeDTO> findAllLeaveTypes() {
		return leaveTypeService.findAllLeaveTypes();
	}

	/**
	 * InActivate Leave Type Only change the Status Active to Inactive.
	 * 
	 * Service URL: /leave-type/{leaveTypeId} method: DELETE.
	 * 
	 * @param leaveTypeId
	 */
	@DeleteMapping(value = "/{leaveTypeId}")
	public void inActivateLeaveType(@PathVariable("leaveTypeId") Long leaveTypeId) {
		this.leaveTypeService.inActivateLeaveType(leaveTypeId);
	}
}
