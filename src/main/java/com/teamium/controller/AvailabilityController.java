package com.teamium.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamium.domain.prod.resources.Resource;
import com.teamium.dto.AvailablilityDTO;
import com.teamium.dto.ResourceDTO;
import com.teamium.service.AvailabilityService;

@RestController
@RequestMapping("/available")
public class AvailabilityController {

	private AvailabilityService availabilityService;

	@Autowired
	public AvailabilityController(AvailabilityService availabilityService) {
		this.availabilityService = availabilityService;
	}
	
	
	@PostMapping("/resource/between")
	 List<ResourceDTO<ResourceDTO<?>>> getAavailableResourceByFunctionAndBetween(@RequestBody AvailablilityDTO availablilityDTO){
		return availabilityService.getAavailableResourceByFunctionAndBetween(availablilityDTO);
	}
	
	
	
}
