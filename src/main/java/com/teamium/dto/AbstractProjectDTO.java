package com.teamium.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.Category;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.projects.AbstractProject;
import com.teamium.domain.prod.projects.Program;
import com.teamium.domain.prod.resources.Asset;
import com.teamium.domain.prod.resources.Director;
import com.teamium.domain.prod.resources.contacts.Customer;
import com.teamium.enums.ProjectStatus.ProjectStatusName;

/**
 * Wrapper class for Abstract-Project
 * 
 * @author Teamium
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AbstractProjectDTO extends RecordDTO {

	private String prodAddress;
	private String prodOrganisation;
	private String client;
	private String title;
	private String subCategory;
	private Float ponderation;

	private DirectorDTO director;
	private CustomerDTO agency;
	private String category;
	private ProgramDTO program;
	private List<AssetDTO> inputs;
	private List<AssetDTO> outputs;
	private String theme;

	public AbstractProjectDTO() {

	}

	public AbstractProjectDTO(Record record) {
		super(record);
	}

	public AbstractProjectDTO(String prodAddress, String prodOrganisation, String client, String title,
			String subCategory, Float ponderation) {
		this.prodAddress = prodAddress;
		this.prodOrganisation = prodOrganisation;
		this.client = client;
		this.title = title;
		this.subCategory = subCategory;
		this.ponderation = ponderation;
	}

	public AbstractProjectDTO(AbstractProject abstractProject) {
		super(abstractProject);
		if (abstractProject != null) {
			this.prodAddress = abstractProject.getProdAddress();
			this.prodOrganisation = abstractProject.getProdOrganisation();
			this.client = abstractProject.getClient();
			this.title = abstractProject.getTitle();
			this.subCategory = abstractProject.getSubCategory();
			this.ponderation = abstractProject.getPonderation();

			Director director = abstractProject.getDirector();
			if (director != null) {
				DirectorDTO directorDTO = new DirectorDTO(director);
				this.director = directorDTO;
			}

			Customer customer = abstractProject.getAgency();
			if (customer != null) {
				CustomerDTO customerDTO = new CustomerDTO(customer);
				this.agency = customerDTO;
			}

			Category category = abstractProject.getCategory();
			this.category = category == null ? null : category.getName();

			Program program = abstractProject.getProgram();
			if (program != null) {
				ProgramDTO programDTO = null;
				try {
					programDTO = new ProgramDTO(program, Boolean.TRUE);
				} catch (Exception e) {

				}
				this.program = programDTO;
			}

			List<Asset> inputs = abstractProject.getInputs();
			if (inputs != null && !inputs.isEmpty()) {
				this.inputs = inputs.stream().map(input -> new AssetDTO(input)).collect(Collectors.toList());
			}

			List<Asset> outputs = abstractProject.getOutputs();
			if (outputs != null && !outputs.isEmpty()) {
				this.outputs = outputs.stream().map(input -> new AssetDTO(input)).collect(Collectors.toList());
			}
		}
	}

	public AbstractProjectDTO(AbstractProject abstractProject, String forProgram, String discriminator) {
		super(abstractProject);
		if (abstractProject != null) {
			this.prodAddress = abstractProject.getProdAddress();
			this.prodOrganisation = abstractProject.getProdOrganisation();
			this.client = abstractProject.getClient();
			this.title = abstractProject.getTitle();
			this.subCategory = abstractProject.getSubCategory();
			this.ponderation = abstractProject.getPonderation();

			Director director = abstractProject.getDirector();
			if (director != null) {
				DirectorDTO directorDTO = new DirectorDTO(director);
				this.director = directorDTO;
			}

			Customer customer = abstractProject.getAgency();
			if (customer != null) {
				CustomerDTO customerDTO = new CustomerDTO(customer);
				this.agency = customerDTO;
			}

			Category category = abstractProject.getCategory();
			this.category = category == null ? null : category.getName();

			// Program program = abstractProject.getProgram();
			// if (program != null) {
			// ProgramDTO programDTO = new ProgramDTO(program);
			// this.program = programDTO;
			// }

			List<Asset> inputs = abstractProject.getInputs();
			if (inputs != null && !inputs.isEmpty()) {
				this.inputs = inputs.stream().map(input -> new AssetDTO(input)).collect(Collectors.toList());
			}

			List<Asset> outputs = abstractProject.getOutputs();
			if (outputs != null && !outputs.isEmpty()) {
				this.outputs = outputs.stream().map(input -> new AssetDTO(input)).collect(Collectors.toList());
			}
			this.setRecordDiscriminator(discriminator);
		}
	}

	/**
	 * @return the prodAddress
	 */
	public String getProdAddress() {
		return prodAddress;
	}

	/**
	 * @param prodAddress the prodAddress to set
	 */
	public void setProdAddress(String prodAddress) {
		this.prodAddress = prodAddress;
	}

	/**
	 * @return the prodOrganisation
	 */
	public String getProdOrganisation() {
		return prodOrganisation;
	}

	/**
	 * @param prodOrganisation the prodOrganisation to set
	 */
	public void setProdOrganisation(String prodOrganisation) {
		this.prodOrganisation = prodOrganisation;
	}

	/**
	 * @return the client
	 */
	public String getClient() {
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(String client) {
		this.client = client;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the subCategory
	 */
	public String getSubCategory() {
		return subCategory;
	}

	/**
	 * @param subCategory the subCategory to set
	 */
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	/**
	 * @return the ponderation
	 */
	public Float getPonderation() {
		return ponderation;
	}

	/**
	 * @param ponderation the ponderation to set
	 */
	public void setPonderation(Float ponderation) {
		this.ponderation = ponderation;
	}

	/**
	 * @return the agency
	 */
	public CustomerDTO getAgency() {
		return agency;
	}

	/**
	 * @param agency the agency to set
	 */
	public void setAgency(CustomerDTO agency) {
		this.agency = agency;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the program
	 */
	public ProgramDTO getProgram() {
		return program;
	}

	/**
	 * @param program the program to set
	 */
	public void setProgram(ProgramDTO program) {
		this.program = program;
	}

	/**
	 * @return the inputs
	 */
	public List<AssetDTO> getInputs() {
		return inputs;
	}

	/**
	 * @param inputs the inputs to set
	 */
	public void setInputs(List<AssetDTO> inputs) {
		this.inputs = inputs;
	}

	/**
	 * @return the outputs
	 */
	public List<AssetDTO> getOutputs() {
		return outputs;
	}

	/**
	 * @param outputs the outputs to set
	 */
	public void setOutputs(List<AssetDTO> outputs) {
		this.outputs = outputs;
	}

	/**
	 * @return the director
	 */
	public DirectorDTO getDirector() {
		return director;
	}

	/**
	 * @param director the director to set
	 */
	public void setDirector(DirectorDTO director) {
		this.director = director;
	}

	/**
	 * @return the theme
	 */
	public String getTheme() {
		return theme;
	}

	/**
	 * @param theme the theme to set
	 */
	public void setTheme(String theme) {
		this.theme = theme;
	}

	/**
	 * To get Abstract-Project
	 * 
	 * @param abstractProject    the abstractProject
	 * 
	 * @param abstractProjectDTO the abstractProjectDTO
	 * 
	 * @return Absttact-Project object
	 */
	public AbstractProject getAbstractProject(AbstractProject abstractProject, AbstractProjectDTO abstractProjectDTO) {
		setAbstractProjectDetail(abstractProject, abstractProjectDTO);
		return abstractProject;
	}

	/**
	 * To set abstract-project details
	 * 
	 * @param abstractProject    the abstractProject
	 * 
	 * @param abstractProjectDTO the abstractProjectDTO
	 */
	public void setAbstractProjectDetail(AbstractProject abstractProject, AbstractProjectDTO abstractProjectDTO) {
		abstractProject.setProdAddress(abstractProjectDTO.getProdAddress());
		abstractProject.setProdOrganisation(abstractProjectDTO.getProdOrganisation());
		abstractProject.setClient(abstractProjectDTO.getClient());

		XmlEntity statusEntity = new XmlEntity();
		if (abstractProjectDTO.getId() == null) {
			statusEntity.setKey(ProjectStatusName.TO_DO.getProjectStatusNameString());
			abstractProject.setStatus(statusEntity);
			abstractProject.setFinancialStatus(null);
		}

		if (StringUtils.isBlank(abstractProject.getFinancialStatus()) || (abstractProject.getFinancialStatus() != null
				&& !abstractProject.getFinancialStatus().equalsIgnoreCase("Approved"))) {
			// set only if financial status is blank or financial status is not Approved
			abstractProject.setFinancialStatus(
					abstractProjectDTO.getId() == null ? null : abstractProjectDTO.getFinancialStatus());
			abstractProject.setTitle(
					!StringUtils.isBlank(abstractProjectDTO.getTitle()) ? abstractProjectDTO.getTitle().trim() : null);
			if (abstractProjectDTO.getId() != null && !StringUtils.isBlank(abstractProjectDTO.getStatus())) {
				statusEntity.setKey(abstractProjectDTO.getStatus());
				abstractProject.setStatus(statusEntity);
			}
		}

		abstractProject.setSubCategory(abstractProjectDTO.getSubCategory());
		abstractProject.setPonderation(abstractProjectDTO.getPonderation());

		DirectorDTO directorDTO = abstractProjectDTO.getDirector();
		if (directorDTO != null) {
			abstractProject.setDirector(directorDTO.getDirector(new Director(), directorDTO));
		}

		List<AssetDTO> assetInputDTOs = abstractProjectDTO.getInputs();
		if (assetInputDTOs != null && !assetInputDTOs.isEmpty()) {
			abstractProject.setInputs(assetInputDTOs.stream().map(dto -> {
				return dto.getAsset(new Asset(), dto);
			}).collect(Collectors.toList()));
		}

		List<AssetDTO> assetOutputDTOs = abstractProjectDTO.getOutputs();
		if (assetOutputDTOs != null && !assetOutputDTOs.isEmpty()) {
			abstractProject.getInputs().clear();
			abstractProject.setInputs(assetOutputDTOs.stream().map(dto -> {
				return dto.getAsset(new Asset(), dto);
			}).collect(Collectors.toList()));
		}

		abstractProjectDTO.setRecordDetail(abstractProject, abstractProjectDTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AbstractProjectDTO [prodAddress=" + prodAddress + ", prodOrganisation=" + prodOrganisation + ", client="
				+ client + ", title=" + title + ", subCategory=" + subCategory + ", ponderation=" + ponderation
				+ ", director=" + director + ", agency=" + agency + ", category=" + category + ", program=" + program
				+ ", inputs=" + inputs + ", outputs=" + outputs + "]";
	}

}
