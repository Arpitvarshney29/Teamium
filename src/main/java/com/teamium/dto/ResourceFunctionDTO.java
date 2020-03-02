package com.teamium.dto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;

import com.teamium.domain.prod.resources.ResourceFunction;
import com.teamium.domain.prod.resources.functions.Function;
import com.teamium.domain.prod.resources.staff.ResourceRate;
import com.teamium.domain.prod.resources.staff.contract.ContractEdition;

/**
 * DTO Class for ResourceFunctionDTO
 * 
 * @author wittybrains
 *
 */
public class ResourceFunctionDTO extends AbstractDTO {

	private FunctionDTO function;
	private Integer rating;
	private List<ResourceRateDTO> rates;
	private String contractEdition;
	private Calendar createdOn;
	private boolean primaryFunction;
	private boolean rateSelected;

	public ResourceFunctionDTO() {
		super();
	}

	public ResourceFunctionDTO(ResourceFunction entity) {
		super(entity);
		this.function = new FunctionDTO(entity.getFunction());
		this.rating = entity.getRating();
		this.rates = entity.getRates().stream().filter(rate -> rate != null).map(rate -> new ResourceRateDTO(rate))
				.collect(Collectors.toList());
		this.contractEdition = entity.getContractEdition();
		this.createdOn = entity.getCreatedOn();
		this.primaryFunction = entity.isPrimaryFunction();
		if (entity.getRateSelected() != null) {
			this.rateSelected = entity.getRateSelected();
		}
	}

	/**
	 * Get ResourceFunction entity from DTO
	 * 
	 * @param function
	 * @return ResourceFunction
	 */
	public ResourceFunction getResourceFunction(ResourceFunction function) {
		function.setFunction(this.getFunction().getFunction(new Function()));
		function.setRates(rates.stream().filter(rate -> (rate != null))
				.map(rate -> rate.getResourceRateDetails(new ResourceRate())).collect(Collectors.toList()));
		function.setContractEdition(contractEdition);
		function.setRating(rating);
		function.setCreatedOn(createdOn);
		function.setPrimaryFunction(primaryFunction);
		function.setRateSelected(rateSelected);
		return function;
	}

	/**
	 * @return the function
	 */
	public FunctionDTO getFunction() {
		return function;
	}

	/**
	 * @param function
	 *            the function to set
	 */
	public void setFunction(FunctionDTO function) {
		this.function = function;
	}

	/**
	 * @return the rating
	 */
	public Integer getRating() {
		return rating;
	}

	/**
	 * @param rating
	 *            the rating to set
	 */
	public void setRating(Integer rating) {
		int starRating = rating == null ? 0 : rating.intValue();
		this.rating = (starRating < 0) ? 0 : starRating > 5 ? 5 : starRating;
	}

	/**
	 * @return the rates
	 */
	public List<ResourceRateDTO> getRates() {
		if(this.rates == null) {
			this.rates = new ArrayList<ResourceRateDTO>();
		}
		return rates;
	}

	/**
	 * @param rates
	 *            the rates to set
	 */
	public void setRates(List<ResourceRateDTO> rates) {
		this.rates = rates;
	}

	/**
	 * @return the contractEdition
	 */
	public String getContractEdition() {
		return contractEdition;
	}

	/**
	 * @param contractEdition
	 *            the contractEdition to set
	 */
	public void setContractEdition(String contractEdition) {
		this.contractEdition = contractEdition;
	}

	/**
	 * @return the createdOn
	 */
	public Calendar getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn
	 *            the createdOn to set
	 */
	public void setCreatedOn(Calendar createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the primaryFunction
	 */
	public boolean isPrimaryFunction() {
		return primaryFunction;
	}

	/**
	 * @param primaryFunction
	 *            the primaryFunction to set
	 */
	public void setPrimaryFunction(boolean primaryFunction) {
		this.primaryFunction = primaryFunction;
	}

	/**
	 * @return the rateSelected
	 */
	public boolean isRateSelected() {
		return rateSelected;
	}

	/**
	 * @param rateSelected
	 *            the rateSelected to set
	 */
	public void setRateSelected(boolean rateSelected) {
		this.rateSelected = rateSelected;
	}

}
