package com.teamium.dto;

import java.util.List;

import com.teamium.domain.prod.resources.Resource;

/**
 * The wrapper class for function and its resources
 * 
 * @author Hansraj
 *
 */
public class FunctionResourceDTO {

	private FunctionDTO function;
	private List<ResourceDTO> resources;
	private ResourceDTO defaultResource;
	

	public FunctionResourceDTO() {
	}
	
	/**
	 * @param function
	 * @param resources
	 * @param defaultResource
	 */
	public FunctionResourceDTO(FunctionDTO function, List<ResourceDTO> resources, ResourceDTO defaultResource) {
		this.function = function;
		this.resources = resources;
		this.defaultResource = defaultResource;
	}


	/**
	 * @return the function
	 */
	public FunctionDTO getFunction() {
		return function;
	}
	/**
	 * @param function the function to set
	 */
	public void setFunction(FunctionDTO function) {
		this.function = function;
	}



	/**
	 * @return the resources
	 */
	public List<ResourceDTO> getResources() {
		return resources;
	}



	/**
	 * @param resources the resources to set
	 */
	public void setResources(List<ResourceDTO> resources) {
		this.resources = resources;
	}



	/**
	 * @return the defaultResource
	 */
	public ResourceDTO getDefaultResource() {
		return defaultResource;
	}



	/**
	 * @param defaultResource the defaultResource to set
	 */
	public void setDefaultResource(ResourceDTO defaultResource) {
		this.defaultResource = defaultResource;
	}
	
}
