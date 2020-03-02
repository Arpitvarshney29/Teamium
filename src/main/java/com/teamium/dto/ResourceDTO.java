package com.teamium.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.prod.DueDate;
import com.teamium.domain.prod.resources.Resource;
import com.teamium.domain.prod.resources.ResourceFunction;
import com.teamium.domain.prod.resources.ResourceInformation;
import com.teamium.domain.prod.resources.functions.Function;

@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ResourceDTO<T extends ResourceDTO<?>> extends AbstractDTO {

	private String position;
	private String name;
	private Set<ResourceFunctionDTO> functions;
	private List<ResourceInformationDTO> informations;
	private ResourceDTO<?> parent;
	private Boolean isExternalize;

	public ResourceDTO() {
		super();
	}

	public ResourceDTO(Resource<?> entity) {
		super(entity);
		if (entity != null) {
			this.position = entity.getPosition();
			this.name = entity.getName();
			this.functions = new HashSet<ResourceFunctionDTO>();
			for (ResourceFunction fun : entity.getFunctions()) {
				if (fun != null) {
					functions.add(new ResourceFunctionDTO(fun));
				}
			}
			this.informations = entity.getInformations().stream().filter(inf -> inf != null)
					.map(inf -> new ResourceInformationDTO(inf)).collect(Collectors.toList());
			this.isExternalize = entity.getIsExternalize();
			this.parent = entity.getParent() == null ? null : new ResourceDTO(entity.getParent());
		}
	}

	public ResourceDTO(Long entityId, String name) {
		this.setId(entityId);
		this.name = name;
	}

	public Resource<?> getResource(Resource<?> resource) {
		resource.setName(name);

		List<ResourceInformationDTO> resourceInfoDTOs = this.getInformations();
		if (resourceInfoDTOs != null && !resourceInfoDTOs.isEmpty()) {
			resource.setInformations(resourceInfoDTOs.stream().map(dto -> {
				ResourceInformation entity = dto.getResourceInformation(new ResourceInformation());
				return entity;
			}).collect(Collectors.toList()));
		}

		resource.setIsExternalize(isExternalize);
		return resource;
	}

	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the functions
	 */
	public Set<ResourceFunctionDTO> getFunctions() {
		return functions;
	}

	/**
	 * @param functions the functions to set
	 */
	public void setFunctions(Set<ResourceFunctionDTO> functions) {
		this.functions = functions;
	}

	/**
	 * @return the informations
	 */
	public List<ResourceInformationDTO> getInformations() {
		return informations;
	}

	/**
	 * @param informations the informations to set
	 */
	public void setInformations(List<ResourceInformationDTO> informations) {
		this.informations = informations;
	}

	/**
	 * @return the parent
	 */
	public ResourceDTO<?> getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(ResourceDTO<?> parent) {
		this.parent = parent;
	}

	/**
	 * @return the isExternalize
	 */
	public Boolean getIsExternalize() {
		return isExternalize;
	}

	/**
	 * @param isExternalize the isExternalize to set
	 */
	public void setIsExternalize(Boolean isExternalize) {
		this.isExternalize = isExternalize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ResourceDTO [position=" + position + ", name=" + name + ", functions=" + functions + ", informations="
				+ informations + ", parent=" + parent + ", isExternalize=" + isExternalize + "]";
	}

}
