package com.teamium.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.projects.Project;
import com.teamium.domain.prod.resources.staff.contract.Contract;
import com.teamium.domain.prod.resources.staff.contract.ContractLine;
import com.teamium.domain.prod.resources.staff.contract.ContractStatus;
import com.teamium.dto.ContractDTO;
import com.teamium.dto.ContractLineDTO;
import com.teamium.dto.ProjectDTO;
import com.teamium.exception.NotFoundException;
import com.teamium.repository.ContractLineRepositiory;
import com.teamium.repository.ContractRepository;

@Service
public class ContractService {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private ContractRepository contractRepository;
	private ContractLineRepositiory contractLineRepositiory;
	private RecordService recordService;

	@Autowired
	public ContractService(ContractRepository contractRepository, ContractLineRepositiory contractLineRepositiory,
			RecordService recordService) {
		this.contractRepository = contractRepository;
		this.contractLineRepositiory = contractLineRepositiory;
		this.recordService = recordService;
	}

	/**
	 * 
	 * @param contractId
	 * @return
	 */
	public ContractDTO findContractById(Long contractId) {
		logger.info("Inside findContractById( " + contractId + " )");
		Contract contract = getContractById(contractId);
		logger.info("Successfully return from findContractById.");
		return new ContractDTO(contract);
	}

	/**
	 * 
	 * @param contractId
	 * @return
	 */
	private Contract getContractById(Long contractId) {
		logger.info("Inside getContractById( " + contractId + " )");
		Contract contract = contractRepository.findOne(contractId);
		if (contract == null) {
			logger.info("Please enter a valid contract id.");
			throw new NotFoundException("Please enter a valid contract id.");
		}
		logger.info("Successfully return from getContractById.");
		return contract;
	}

	private ContractLine getContractLineById(Long contractLineId) {
		logger.info("Inside getContractLineById( " + contractLineId + " )");
		ContractLine contractLine = this.contractLineRepositiory.findOne(contractLineId);
		if (contractLine == null) {
			logger.info("Please enter a valid contract line id.");
			throw new NotFoundException("Please enter a valid contract line id.");
		}
		logger.info("Successfully return from getContractLineById.");
		return contractLine;
	}

	public ContractLineDTO updateContractLine(ContractLineDTO contractLineDTO) {
		logger.info("Inside updateContractLine( " + contractLineDTO + " )");
		ContractLine contractLine = getContractLineById(contractLineDTO.getId());
		contractLine = contractLineDTO.getContractLineEntity(contractLine);
		contractLine = this.contractLineRepositiory.save(contractLine);
		logger.info("Successfully return from updateContractLine.");
		return new ContractLineDTO(contractLine);
	}

	public ContractLineDTO updateContractLineStatus(Long contractLineId) {
		logger.info("Inside updateContractLine( " + contractLineId + " )");
		ContractLine contractLine = getContractLineById(contractLineId);
		Booking booking = contractLine.getBookings().get(0);
		if (booking != null) {
			if (booking.getContractStatus() == null
					|| booking.getContractStatus().getValue().equals(ContractStatus.NOT_MADE.getValue())) {
				booking.setContractStatus(ContractStatus.MADE);
			} else if (booking.getContractStatus().getValue().equals(ContractStatus.MADE.getValue())) {
				booking.setContractStatus(ContractStatus.SIGNED);
			} 
			contractLine = this.contractLineRepositiory.save(contractLine);
		}

		logger.info("Successfully return from updateContractLine.");
		return new ContractLineDTO(contractLine);

	}

}
