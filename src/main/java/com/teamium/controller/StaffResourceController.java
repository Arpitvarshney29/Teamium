package com.teamium.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teamium.dto.StaffResourceDTO;
import com.teamium.service.StaffResourceService;

import io.swagger.annotations.Api;

/**
 * <p>
 * Handles operations related to StaffResource .
 * </p>
 */
@RestController
@RequestMapping("/staff-resource")
@Api(value = "StaffResource API .")
public class StaffResourceController {

	private StaffResourceService staffResourceService;

	@Autowired
	public StaffResourceController(StaffResourceService staffResourceService) {
		this.staffResourceService = staffResourceService;
	}

	/**
	 * To save the group name for StaffResource
	 * 
	 * @param groupName
	 *            the groupName
	 * @return the StaffResourceDTO object
	 */
	@RequestMapping(value = "/group", method = RequestMethod.POST)
	StaffResourceDTO saveGroup(@RequestParam("groupName") String groupName) {
		return staffResourceService.saveGroup(groupName);
	}

	/**
	 * To find all the staffResource groups.
	 * 
	 * @return the list of StaffResourceDTO objects
	 */
	@RequestMapping(value = "/group", method = RequestMethod.GET)
	List<StaffResourceDTO> findAllGroups() {
		return staffResourceService.getGroups();
	}
}
