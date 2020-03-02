package com.teamium.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.prod.resources.staff.contract.Contract;

@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ContractDTO {
	private Long id;
	private List<ContractLineDTO> contractLineDTOs;

	public ContractDTO(Contract contract) {
		this.id = contract.getId();
		contractLineDTOs = contract.getLines().stream().map(ContractLineDTO::new).collect(Collectors.toList());
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the contractLineDTOs
	 */
	public List<ContractLineDTO> getContractLineDTOs() {
		return contractLineDTOs;
	}

	/**
	 * @param contractLineDTOs
	 *            the contractLineDTOs to set
	 */
	public void setContractLineDTOs(List<ContractLineDTO> contractLineDTOs) {
		this.contractLineDTOs = contractLineDTOs;
	}

}
