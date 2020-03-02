package com.teamium.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamium.domain.Company;
import com.teamium.dto.CompanyDTO;
import com.teamium.service.CompanyService;


@RestController
@RequestMapping("/company")
public class CompanyController {

	private CompanyService companyService;

	

	/**
	 * @param companyService
	 */
	public CompanyController(CompanyService companyService) {
		this.companyService = companyService;
	}

	/**
	 * To save and update company.
	 * 
	 * Service URL:/company method: POST
	 * 
	 * @param CompanyDTO the CompanyDTO .
	 */

	@PostMapping
	public CompanyDTO saveCompany(@RequestBody CompanyDTO companyDTO) {
		return companyService.saveOrUpdateCompany(companyDTO);
	}

	/**
	 * To get company by id
	 * 
	 * Service URL: /company/{companyId} method: GET.
	 * 
	 * @param companyId the companyId
	 * 
	 * @return the CompanyDTO
	 */
	@GetMapping("/{companyId}")
	public CompanyDTO findCompany(@PathVariable long companyId) {
		return companyService.findCompanyDTOById(Company.class, companyId);
	}

	/**
	 * To delete customer by id
	 * 
	 * Service URL: /company/{companyId} method: DELETE.
	 * 
	 * @param customerId the companyId
	 * 
	 */
	@DeleteMapping("/{companyId}")
	public void deleteCompany(@PathVariable long companyId) {
		companyService.deleteCompany(companyId);
	}

	
	/**
	 * To customer list
	 * 
	 * Service URL: /company method: GET.
	 * 
	 * @return the CustomerDTO list
	 */
	@GetMapping
	public List<CompanyDTO> getCompany() {
		return companyService.getAllCompany(Company.class);
	}
	
	
//	/**
//	 * To get customer dropdowns
//	 * 
//	 * Service URL: /customer/dropdown method: GET.
//	 * 
//	 * @return the CustomerDropdownDTO
//	 */
//	@GetMapping("/dropdown")
//	public CustomerDropdownDTO getCustomerDropdowns() {
//		return customerService.getCustomerDropdowns();
//	}
}
