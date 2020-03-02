package com.teamium.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.prod.resources.functions.DefaultResource;
import com.teamium.domain.prod.resources.functions.RatedFunction;

/**
 * Wrapper class for Resource-Function
 * 
 * @author Teamium
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class RatedFunctionDTO extends FunctionDTO {

	private String description;
	private ResourceDTO<ResourceDTO<?>> defaultResource;

	public RatedFunctionDTO() {
		super();
	}

	public RatedFunctionDTO(RatedFunction entity) {
		super(entity);
		this.setId(entity.getId());
		this.setVersion(entity.getVersion());
		this.description = entity.getDescription();
		DefaultResource defResource = entity.getDefaultResource();
		if (defResource != null) {
			this.defaultResource = new ResourceDTO<>(defResource);
		}
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the defaultResource
	 */
	public ResourceDTO<ResourceDTO<?>> getDefaultResource() {
		return defaultResource;
	}

	/**
	 * @param defaultResource
	 *            the defaultResource to set
	 */
	public void setDefaultResource(ResourceDTO<ResourceDTO<?>> defaultResource) {
		this.defaultResource = defaultResource;
	}

	/**
	 * Method to set Rated-Function
	 * 
	 * @param ratedFunction
	 *            the ratedFunction
	 */
	public RatedFunction getRatedFunction(RatedFunction ratedFunction) {
		ratedFunction.setId(this.getId());
		ratedFunction.setVersion(this.getVersion());
		ratedFunction.setDescription(description);
		this.getFunction(ratedFunction);
		return ratedFunction;
	}

}
