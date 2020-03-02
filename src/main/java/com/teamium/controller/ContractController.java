package com.teamium.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamium.domain.prod.resources.staff.contract.Contract;
import com.teamium.dto.ContractDTO;
import com.teamium.dto.ContractLineDTO;
import com.teamium.service.ContractService;

@RestController
@RequestMapping("/contract")
public class ContractController {

	ContractService contractService;

	@Autowired
	public ContractController(ContractService contractService) {
		this.contractService = contractService;
	}

	/**
	 * To get contract by id
	 * 
	 * Service URL: /contract/{contractId} method: GET.
	 * 
	 * @param contractId
	 *            the contractId
	 * 
	 * @return the ContractDTO
	 */
	@GetMapping("/{contractId}")
	public ContractDTO findContractById(@PathVariable long contractId) {
		return contractService.findContractById(contractId);
	}

	@PutMapping
	public ContractLineDTO updateContractLine(@RequestBody ContractLineDTO contractLineDTO) {
		return this.contractService.updateContractLine(contractLineDTO);
	}

	@PutMapping(value = "/{contractId}")
	public ContractLineDTO updateContractLineStatus(@PathVariable("contractId") Long contractLineId) {
		return this.contractService.updateContractLineStatus(contractLineId);
	}
}
