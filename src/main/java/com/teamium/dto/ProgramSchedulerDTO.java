package com.teamium.dto;

import java.util.ArrayList;
import java.util.List;

public class ProgramSchedulerDTO {
	List<ResourceDTO<?>> resourceDTOs = new ArrayList<>();
	List<ProgramEventDTO> programEventDTOs = new ArrayList<>();

	public ProgramSchedulerDTO() {

	}

	public ProgramSchedulerDTO(List<ResourceDTO<?>> resourceDTOs, List<ProgramEventDTO> programEventDTOs) {
		this.resourceDTOs = resourceDTOs;
		this.programEventDTOs = programEventDTOs;
	}

	/**
	 * @return the resourceDTOs
	 */
	public List<ResourceDTO<?>> getResourceDTOs() {
		return resourceDTOs;
	}

	/**
	 * @param resourceDTOs
	 *            the resourceDTOs to set
	 */
	public void setResourceDTOs(List<ResourceDTO<?>> resourceDTOs) {
		this.resourceDTOs = resourceDTOs;
	}

	/**
	 * @return the programEventDTOs
	 */
	public List<ProgramEventDTO> getProgramEventDTOs() {
		return programEventDTOs;
	}

	/**
	 * @param programEventDTOs
	 *            the programEventDTOs to set
	 */
	public void setProgramEventDTOs(List<ProgramEventDTO> programEventDTOs) {
		this.programEventDTOs = programEventDTOs;
	}

}
