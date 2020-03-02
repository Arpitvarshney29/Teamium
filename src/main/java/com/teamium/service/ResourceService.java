package com.teamium.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.teamium.domain.TeamiumException;
import com.teamium.domain.prod.projects.planning.Event;
import com.teamium.domain.prod.resources.Resource;
import com.teamium.domain.prod.resources.equipments.Equipment;
import com.teamium.domain.prod.resources.functions.DefaultResource;
import com.teamium.domain.prod.resources.functions.RatedFunction;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.domain.prod.resources.staff.StaffResource;
import com.teamium.dto.AvailablilityDTO;
import com.teamium.dto.FunctionDTO;
import com.teamium.dto.FunctionResourceDTO;
import com.teamium.dto.ResourceDTO;
import com.teamium.exception.NotFoundException;
import com.teamium.repository.DefaultResourceRepository;
import com.teamium.repository.ResourceRepository;
import com.teamium.service.prod.resources.functions.FunctionService;

/**
 * A service class implementation for Resource
 *
 */
@Service
public class ResourceService {

	private ResourceRepository resourceRepository;
	private FunctionService functionService;
	private DefaultResourceRepository defaultResourceRepository;
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * @param resourceRepository
	 * @param functionService
	 * @param defaultResourceRepository
	 */
	public ResourceService(ResourceRepository resourceRepository, FunctionService functionService,
			DefaultResourceRepository defaultResourceRepository) {
		this.resourceRepository = resourceRepository;
		this.functionService = functionService;
		this.defaultResourceRepository = defaultResourceRepository;
	}

	/**
	 * Method to find Resource by resourceId.
	 * 
	 * @param resourceId the resourceId
	 * 
	 */
	public Resource<?> findResource(Long resourceId) {
		logger.info("Inside ResourceService,findResource:resourceId: " + resourceId);
		if (resourceId == null) {
			logger.info("Please enter valid resourceId : " + resourceId);
			throw new NotFoundException("Please enter valid resourceId : " + resourceId);
		}
		Resource<?> resource = resourceRepository.findOne(resourceId);
		if (resource == null) {
			logger.info("Resource not found on Id" + resourceId);
			throw new NotFoundException("Resource not found on Id: " + resourceId);
		}
		logger.info("Returning from  findResource(),to get resource on Id: " + resourceId);
		return resource;
	}

	/**
	 * Method to find all resources .
	 * 
	 * @return Resources list
	 */
	public List<ResourceDTO> getResources() {
		logger.info("Inside ResourceService,findResources: ");
		List<ResourceDTO> resources = resourceRepository.findAll().stream().map(res -> new ResourceDTO(res))
				.collect(Collectors.toList());
		logger.info("Returning from  ResourceService,findResources: ");
		return resources;
	}

	/**
	 * Method to get Resource by resourceId.
	 * 
	 * @param resourceId the resourceId
	 * 
	 */
	public ResourceDTO<?> getResource(long resourceId) {
		logger.info("Inside ResourceService,getResources: ");
		return new ResourceDTO(findResource(resourceId));
	}

	/**
	 * Method to delete Resource by resourceId.
	 * 
	 * @param resourceId the resourceId
	 * 
	 */
	public void deleteResource(long resourceId) {
		logger.info("Inside ResourceService,deleteResource: ");
		findResource(resourceId);
		resourceRepository.delete(resourceId);
		logger.info("Returning ResourceService,deleteResource: ");
	}

	/**
	 * To get resource functions
	 * 
	 * @return FunctionResourceDTO list
	 */
	public List<FunctionResourceDTO> getResourceFunction() {
		logger.info("Inside ResourceService :: getResourceFunction() ");
		List<RatedFunction> functions = functionService.findAllRatedFunctions();
		List<FunctionResourceDTO> functionResource = new LinkedList<FunctionResourceDTO>();
		functions.forEach(fun -> {
			List<Resource> resources = resourceRepository.getResourceByFunction(fun.getId());
			functionResource.add(new FunctionResourceDTO(new FunctionDTO(fun),
					resources.stream().map(r -> new ResourceDTO<>(r)).collect(Collectors.toList()),
					fun.getDefaultResource() == null ? null : new ResourceDTO(fun.getDefaultResource())));
		});
		logger.info("Returning from ResourceService,getResourceFunction: ");
		return functionResource;
	}

	/**
	 * To get resource by functions
	 * 
	 * @return Resource list
	 */
	public List<Resource> getResourceByFunction(long functionId) {
		logger.info("Inside ResourceService,getResourceByFunction:: getting resource by function :" + functionId);
		return resourceRepository.getResourceByFunction(functionId);
	}

	/**
	 * To add default resource
	 * 
	 * @return DefaultResource
	 */
	public DefaultResource addDefaultResource(DefaultResource resource) {
		logger.info("Inside ResourceService,addDefaultResource:: adding default resource :" + resource);
		return defaultResourceRepository.save(resource);
	}

	/**
	 * Get the list of available resources for the current entity
	 * 
	 * @param function
	 * @param from
	 * @param to
	 * 
	 * @return the list of available Resources
	 * 
	 * @throws TeamiumException
	 */
	public List<Resource<?>> getResourcesAvailable(AvailablilityDTO availablilityDTO) {
		logger.info("Inside ResourceService,getResourcesAvailable:");
		List<Resource<?>> res = new ArrayList<>();
		res = resourceRepository.findResourcesAvailableByFunctionIdAndStartAndEndDate(availablilityDTO.getFunction(),
				availablilityDTO.getFrom(), availablilityDTO.getTo());
		logger.info("Returning from ResourceService,getResourcesAvailable :");
		return res;

	}

	/**
	 * Get the list of available resources between to dates
	 * 
	 * @param from
	 * @param to
	 * @return list of resources
	 */
	public List<Resource<?>> getResourcesAvailableBetween(Calendar from, Calendar to) {
		logger.info("Inside ResourceService,getResourcesAvailableBetween:");
		List<Resource<?>> res = resourceRepository.findResourcesAvailableBetween(from, to);
		logger.info("Returnign from ResourceService,getResourcesAvailableBetween:");
		return res;

	}

	/**
	 * To get resource functions which are checked as available
	 * 
	 * @return list of FunctionResourceDTOs
	 */
	public List<FunctionResourceDTO> getAvailableResourceFunction() {
		logger.info("Inside ResourceService :: getAvailableResourceFunction()");
		List<RatedFunction> functions = functionService.findAllRatedFunctions();
		List<FunctionResourceDTO> functionResource = new LinkedList<FunctionResourceDTO>();
		functions.forEach(fun -> {
			List<Resource> resources = resourceRepository.getResourceByFunction(fun.getId());
			functionResource.add(new FunctionResourceDTO(new FunctionDTO(fun), resources.stream().filter(r -> {
				if (r.getEntity() instanceof StaffMember) {
					// check available staff-resource
					if (((StaffMember) r.getEntity()).isAvailable()) {
						return true;
					}
					return false;
				} else if (r.getEntity() instanceof Equipment) {
					// check available equipment-resource
					if (((Equipment) r.getEntity()).isAvailable()) {
						return true;
					}
					return false;
				}
				return true;
			}).map(r -> new ResourceDTO<>(r)).collect(Collectors.toList()),
					fun.getDefaultResource() == null ? null : new ResourceDTO(fun.getDefaultResource())));
		});
		logger.info("Returning from getAvailableResourceFunction() ");
		return functionResource;
	}

}
