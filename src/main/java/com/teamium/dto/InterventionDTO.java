package com.teamium.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.prod.projects.intervention.Intervention;

@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class InterventionDTO extends ProjectDTO{

	public InterventionDTO() {
		
	}
	
	public InterventionDTO(Intervention intervention) {
		super(intervention);
	}
	
	@JsonIgnore
	public void setInterventionDetail(Intervention intervention, InterventionDTO interventionDTO) {
		interventionDTO.setProjectDetail(intervention, interventionDTO);
	}

	@JsonIgnore
	public Intervention getIntervention(Intervention intervention, InterventionDTO interventionDTO) {
		setInterventionDetail(intervention, interventionDTO);
		return intervention;
	}
	
}
