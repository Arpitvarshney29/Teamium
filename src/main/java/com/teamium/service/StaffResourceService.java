package com.teamium.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamium.domain.prod.resources.Resource;
import com.teamium.domain.prod.resources.staff.StaffResource;
import com.teamium.dto.StaffResourceDTO;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.StaffResourceRepository;

@Service
public class StaffResourceService {

	private StaffResourceRepository staffResourceRepository;
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public StaffResourceService(StaffResourceRepository staffResourceRepository) {
		this.staffResourceRepository = staffResourceRepository;
	}

	/**
	 * To save the group for StaffResource
	 * 
	 * @param groupName
	 *            the group - name
	 * @return the StaffResourceDTO object
	 */
	public StaffResourceDTO saveGroup(String groupName) {

		logger.info("Inside StaffResourceervice,saveGroup:groupName: " + groupName);
		if (groupName == null) {
			logger.error("Please enter valid group Name");
			throw new UnprocessableEntityException("Please enter valid group Name");
		}

		if (staffResourceRepository.findByGroupName(groupName) != null) {
			logger.error(groupName + " Group name is already exist. ");
			throw new UnprocessableEntityException(groupName + " Group name is already exist. ");
		}

		StaffResource staffResource = new StaffResource();
		staffResource.setName(groupName);
		staffResource = staffResourceRepository.save(staffResource);
		logger.info("Successfully returning StaffResourceDTO after saving group. ");
		return new StaffResourceDTO(staffResource);
	}

	/**
	 * To get all StaffResource's Groups
	 * 
	 * @return the list of StaffResourceDTO objects
	 */
	public List<StaffResourceDTO> getGroups() {
		logger.info(" Inside StaffResourceService,getGroups :: Finding All groups");
		return staffResourceRepository.findGroups().stream().map(staffResource -> new StaffResourceDTO(staffResource))
				.collect(Collectors.toList());
	}

	/**
	 * The named query used to find children of the staff resource given in
	 * parameter
	 * 
	 * @param 1
	 *            The staff resource
	 */
	public List<StaffResource> findChildren(Resource<?> parent) {
		logger.info("Inside StaffResourceService,findChildren :: Finding All Children");
		List<StaffResource> staffResources = this.staffResourceRepository.findChildren(parent);
		logger.info("returning From StaffResourceService,findChildren :found " + staffResources.size());
		return staffResources;
	}

	/**
	 * To validate the group and list
	 * 
	 * @param groupNames
	 *            the groupName
	 * @return the set of StaffResource Object
	 */
	public Set<StaffResource> validateGroupNameAndGetGroupList(Set<String> groupNames) {
		Set<StaffResource> staffResources = new HashSet<>();
		logger.info("To validate the groupNames,validateGroupNameAndGetGroupList :: Validating the groupNames");

		if (groupNames == null || groupNames.isEmpty()) {
			return staffResources;
		}

		for (String groupName : groupNames) {
			StaffResource staffResource = staffResourceRepository.findByGroupName(groupName);
			if (staffResource == null) {
				throw new UnprocessableEntityException("Please choose valid group name : " + groupName);
			} else {
				staffResources.add(staffResource);
			}
		}
		logger.info("Returning after groupNames validation,validateGroupNameAndGetGroupList :: Validation successful.");

		return staffResources;
	}

	/**
	 * To get groups
	 *
	 * @return list of groups
	 */
	public List<StaffResourceDTO> getAllGroups() {
		logger.info(" Inside StaffResourceService,getGroups :: Finding All groups");
		return staffResourceRepository.getAllGroups().stream().map(staffResource -> new StaffResourceDTO(staffResource))
				.collect(Collectors.toList());
	}
}
