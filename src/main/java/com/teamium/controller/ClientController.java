package com.teamium.controller;

import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.teamium.dto.CustomerDTO;
import com.teamium.dto.CustomerDropdownDTO;
import com.teamium.dto.SpreadsheetMessageDTO;
import com.teamium.service.CustomerService;

/**
 * <p>
 * Handles operations related to client like:providing list of client,Saving or
 * updating
 * </p>
 */

@RestController
@RequestMapping("/client")
public class ClientController {

	private CustomerService customerService;

	/**
	 * @param customerService
	 */
	public ClientController(CustomerService customerService) {
		this.customerService = customerService;
	}

	/**
	 * To save and update client.
	 * 
	 * Service URL:/client method: POST
	 * 
	 * @param CustomerDTO the CustomerDTO .
	 */

	@PostMapping
	public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
		return customerService.saveOrUpdateCustomer(customerDTO);
	}

	/**
	 * To get customer by id
	 * 
	 * Service URL: /customer/{customerId} method: GET.
	 * 
	 * @param customerId the customerId
	 * 
	 * @return the CustomerDTO
	 */
	@GetMapping("/{customerId}")
	public CustomerDTO findCustomer(@PathVariable long customerId) {
		return customerService.getCustomer(customerId);
	}

	/**
	 * To delete customer by id
	 * 
	 * Service URL: /customer/{customerId} method: DELETE.
	 * 
	 * @param customerId the customerId
	 * 
	 */
	@DeleteMapping("/{customerId}")
	public void deleteCustomer(@PathVariable long customerId) {
		customerService.deleteCustomer(customerId);
	}

	/**
	 * To customer list
	 * 
	 * Service URL: /customer method: GET.
	 * 
	 * @return the CustomerDTO list
	 */
	@GetMapping
	public List<CustomerDTO> getCustomers() {
		return customerService.getCustomers();
	}

	/**
	 * To get customer dropdowns
	 * 
	 * Service URL: /customer/dropdown method: GET.
	 * 
	 * @return the CustomerDropdownDTO
	 */
	@GetMapping("/dropdown")
	public CustomerDropdownDTO getCustomerDropdowns() {
		return customerService.getCustomerDropdowns();
	}

	/**
	 * To upload spreadsheet for client
	 * 
	 * Service URL:/client/upload/spreadsheet , method: GET.
	 *
	 * @param spreadsheetFile
	 * @return SpreadsheetMessageDTO
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	@RequestMapping(value = "/upload/spreadsheet", method = RequestMethod.POST)
	public SpreadsheetMessageDTO getUploadSpreadsheet(@RequestBody MultipartFile spreadsheetFile)
			throws InvalidFormatException, IOException {
		return customerService.upload(spreadsheetFile);
	}

}
