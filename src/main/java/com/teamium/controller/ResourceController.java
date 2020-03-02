package com.teamium.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamium.dto.FunctionResourceDTO;
import com.teamium.service.ResourceService;

/**
 * <p>
 * Handles operations related to resources .
 * </p>
 */
@RestController
@RequestMapping("/resource")
public class ResourceController {
	private ResourceService resourceService;

	/**
	 * @param resourceService
	 */
	public ResourceController(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	/**
	 * To get list of all resource functions
	 * 
	 * Service URL: /resource/functions method: GET.
	 * 
	 * @return the list of FunctionResourceDTO
	 */
	@GetMapping("/functions")
	public List<FunctionResourceDTO> getResource() {
		return resourceService.getResourceFunction();
	}
	
	/**
	 * To get list of all available resource functions
	 * 
	 * Service URL: /resource/functions/available method: GET.
	 * 
	 * @return the list of FunctionResourceDTO
	 */
	@GetMapping("/functions/available")
	public List<FunctionResourceDTO> getAvailableResourceFunction() {
		return resourceService.getAvailableResourceFunction();
	}

}
