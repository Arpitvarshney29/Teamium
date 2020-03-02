package com.teamium.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.teamium.dto.InvoiceDTO;
import com.teamium.dto.InvoiceGenerationDTO;
import com.teamium.service.InvoiceService;
import com.teamium.service.RecordService;

/**
 * <p>
 * Handles operations related to Invoice like:providing list of budget and
 * booking line,Saving or updating
 * </p>
 */

@RequestMapping(value = "/invoice")
@RestController
public class InvoiceController {

	private RecordService recordService;
	private InvoiceService invoiceService;

	@Autowired
	public InvoiceController(RecordService recordService, InvoiceService invoiceService) {
		this.recordService = recordService;
		this.invoiceService = invoiceService;
	}

	/**
	 * To generate invoice
	 * 
	 * Service URL: /invoice/generate method: POST
	 * 
	 * @param invoiceGenerationDTO
	 * 
	 * @return InvoiceGenerationDTO
	 */
	@RequestMapping(method = RequestMethod.POST)
	InvoiceGenerationDTO invoiceGenerate(@RequestBody InvoiceGenerationDTO invoiceGenerationDTO) {
		return invoiceService.createInvoice(invoiceGenerationDTO);
	}

	/**
	 * To get invoice detail
	 * 
	 * Service URL: /invoice/projectId method: GET
	 * 
	 * @param projectId
	 * 
	 * @return the basic detail of invoice
	 * 
	 */
	@GetMapping("/{projectId}")
	InvoiceDTO getInvoiceDetail(@PathVariable long projectId) {
		return this.invoiceService.getInvoiceDetail(projectId);

	}

	/**
	 * To get all invoice by project Id
	 * 
	 * Service URL: /invoice/projectId method: GET
	 * 
	 * @param projectId
	 * 
	 * @return list of invoiceGenerationDTO
	 */
	@GetMapping("project/{projectId}")
	List<InvoiceGenerationDTO> getAllInvoicesByProjectId(@PathVariable long projectId) {
		return invoiceService.getAllInvoicesByProjectId(projectId);

	}

}
