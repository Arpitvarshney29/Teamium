package com.teamium.service;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamium.domain.prod.resources.Resource;
import com.teamium.dto.AvailablilityDTO;
import com.teamium.dto.ResourceDTO;

@Service
public class AvailabilityService {

	private ResourceService resourceService;
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public AvailabilityService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	/**
	 * To Get Available Resource By function and Between two dates
	 * 
	 * @param availablilityDTO
	 * @return ResourceDTO list
	 */
	public List<ResourceDTO<ResourceDTO<?>>> getAavailableResourceByFunctionAndBetween(
			AvailablilityDTO availablilityDTO) {
		logger.info("Inside AvailabilityService,getAavailableResourceByFunctionAndBetween::");
		return resourceService.getResourcesAvailable(availablilityDTO).stream()
				.map(res -> new ResourceDTO<>(res.getId(), res.getName())).collect(Collectors.toList());
	}

	/**
	 * To Get Available Resource Between two dates
	 * 
	 * @param from
	 * @param to
	 * @return ResourceDTO list
	 */
	public List<ResourceDTO> getAavailableResourceBetween(Calendar from, Calendar to) {
		logger.info("Inside AvailabilityService,getAavailableResourceBetween::");
		return resourceService.getResourcesAvailableBetween(from, to).stream()
				.map(res -> new ResourceDTO<>(res.getId(), res.getName())).collect(Collectors.toList());
	}

	/**
	 * To Get check Availability of a Resource Between two dates
	 * 
	 * @param from
	 * @param to
	 * @return ResourceDTO list
	 */
	public boolean isResourceAvailableBetween(Resource<?> resource, Calendar from, Calendar to) {
		logger.info("Inside AvailabilityService,isResourceAvailableBetween:: checking availability of resource :"
				+ resource);
		List<Resource<?>> resources = resourceService.getResourcesAvailableBetween(from, to);
		return resources.contains(resource);
	}

}
