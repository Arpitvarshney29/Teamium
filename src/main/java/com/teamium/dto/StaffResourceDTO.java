package com.teamium.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.teamium.domain.prod.resources.staff.StaffResource;

/**
 * DTO for StaffResource
 * 
 * @author Wittybrains
 *
 */
public class StaffResourceDTO extends ResourceDTO<ResourceDTO<?>> {

	private Set<StaffResourceDTO> staffGroupDTOs;

	public StaffResourceDTO() {
		super();
	}

	public StaffResourceDTO(StaffResource staffResource) {
		super(staffResource);
		this.staffGroupDTOs = staffResource.getStaffGroups().stream().map(StaffResourceDTO::new)
				.collect(Collectors.toSet());
	}

	public StaffResourceDTO(StaffResourceDTO staffResourceDTO) {
		this.setId(staffResourceDTO.getId());
		this.setName(staffResourceDTO.getName());
	}
	
	public void setResourceDetails(StaffResource staffResource) {
		super.getResource(staffResource);
	}

	/**
	 * @return the staffGroupDTOs
	 */
	public Set<StaffResourceDTO> getStaffGroupDTOs() {
		return staffGroupDTOs;
	}

	/**
	 * @param staffGroupDTOs
	 *            the staffGroupDTOs to set
	 */
	public void setStaffGroupDTOs(Set<StaffResourceDTO> staffGroupDTOs) {
		this.staffGroupDTOs = staffGroupDTOs;
	}

}
