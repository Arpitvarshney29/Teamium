package com.teamium.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamium.dto.HolidayDTO;
import com.teamium.dto.LabourRuleDTO;
import com.teamium.dto.StaffMemberDTO;
import com.teamium.service.LabourRuleService;

@RestController
@RequestMapping("/labour-rule")
public class LabourRuleController {

	private LabourRuleService labourRuleService;

	@Autowired
	public LabourRuleController(LabourRuleService labourRuleService) {
		this.labourRuleService = labourRuleService;
	}

	/**
	 * To save or update labour rule.
	 * 
	 * Service URL: /labour-rule method: POST.
	 * 
	 * @param labourRuleDTO
	 * @return labourRuleDTO
	 */
	@PostMapping
	public LabourRuleDTO saveOrUpdateLabourRule(@RequestBody LabourRuleDTO labourRuleDTO) {
		return this.labourRuleService.saveOrUpdateLabourRule(labourRuleDTO);
	}

	/**
	 * Save Holiday For LabourRule.
	 * 
	 * Service URL: /labour-rule/holiday method: POST.
	 * 
	 * @param holidayDTO
	 * @return list of HolidayDTO.
	 */
	@PostMapping(value = "/holiday")
	public List<HolidayDTO> saveOrUpdateHolidayForLabourRule(@RequestBody HolidayDTO holidayDTO) {
		return this.labourRuleService.saveOrUpdateHolidayForLabourRule(holidayDTO);
	}

	/**
	 * Remove Holiday From LabourRule.
	 * 
	 * Service URL: /labour-rule/holiday/{labour}/{holiday} method: DELETE.
	 * 
	 * @param labourId,holydayId
	 * 
	 */
	@DeleteMapping(value = "/holiday/{labour}/{holiday}")
	public void removeHolidayFromLabourRule(@PathVariable("labour") Long labourRuleId,
			@PathVariable("holiday") Long holydayId) {
		this.labourRuleService.removeHolidayFromLabourRule(labourRuleId, holydayId);
	}

	/**
	 * Remove StaffMember From LabourRule.
	 * 
	 * Service URL: /labour-rule/staff-member/{labour}/{staff} method: DELETE.
	 * 
	 * @param labourId,staffMemberId
	 * 
	 */
	@DeleteMapping(value = "/staff-member/{labour}/{staff}")
	public void removeStaffMemberFromLabourRule(@PathVariable("labour") Long labourRuleId,
			@PathVariable("staff") Long staffMemberId) {
		this.labourRuleService.removeStaffMemberFromLabourRule(labourRuleId, staffMemberId);
	}

	/**
	 * Assign StaffMember To LabourRule.
	 * 
	 * Service URL: /labour-rule/{labour}/{staff} method: POST.
	 * 
	 * @param labourId,staffmemberId
	 * @return list of StaffMemberDTO.
	 */
	@PostMapping(value = "/staff-member/{labour}/{staff}")
	public List<StaffMemberDTO> assignStaffMemberToLabourRule(@PathVariable("labour") Long labourRuleId,
			@PathVariable("staff") Long staffMemberId) {
		return this.labourRuleService.assignStaffMemberToLabourRule(labourRuleId, staffMemberId);
	}

	/**
	 * Fetch all labour rule.
	 * 
	 * Service URL: /labour-rule method: GET.
	 * 
	 * @param id
	 * @return list of labourRuleDTO
	 */
	@GetMapping
	public List<LabourRuleDTO> fetchAllLabourRule() {
		return this.labourRuleService.fetchAllLabourRule();
	}

	/**
	 * find labour rule by id.
	 * 
	 * Service URL: /labour-rule/{id} method: GET.
	 * 
	 * @param id
	 * @return labourRuleDTO
	 */
	@GetMapping("/{id}")
	public LabourRuleDTO findLabourRuleById(@PathVariable Long id) {
		return this.labourRuleService.findLabourRuleById(id);
	}

	/**
	 * delete labour rule by labour rule id
	 * 
	 * Service URL: /labour-rule method: GET.
	 * 
	 * @param labourRuleId
	 */
	@DeleteMapping(value = "/{labour-rule-id}")
	public void deleteLabourRuleById(@PathVariable("labour-rule-id") Long labourRuleId) {
		this.labourRuleService.deleteLabourRuleById(labourRuleId);
	}

}