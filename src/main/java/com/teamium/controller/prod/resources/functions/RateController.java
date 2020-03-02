package com.teamium.controller.prod.resources.functions;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamium.dto.FunctionDropDownDTO;
import com.teamium.dto.RateDropdownDTO;
import com.teamium.dto.prod.resources.functions.RateDTO;
import com.teamium.service.prod.resources.functions.RateService;

/**
 * <p>
 * Handles operations related to Rate like : providing list of rates, saving or
 * updating
 * </p>
 */
@RestController
@RequestMapping("/rate")
public class RateController {

	private RateService rateService;

	public RateController(RateService rateService) {
		this.rateService = rateService;
	}

	/**
	 * To update or save rate.
	 * 
	 * Service URL: /rate method: POST.
	 * 
	 * @param RateDTO
	 * @return
	 */
	@PostMapping
	public RateDTO addRate(@RequestBody RateDTO rateDTO) {
		return rateService.saveOrUpdateRate(rateDTO);
	}

	/**
	 * To get rate details by rateId
	 * 
	 * Service URL: /rate/{rateId} method: GET.
	 * 
	 * @param rateId the rateId
	 * 
	 * @return the RateDTO
	 */
	@GetMapping(value = "{rateId}")
	public RateDTO getRateDetail(@PathVariable long rateId) {
		return rateService.getRate(rateId);
	}

	/**
	 * To get the list of all rates.
	 * 
	 * Service URL: /rate: GET.
	 * 
	 * @return list of RateDTO.
	 */
	@GetMapping
	public List<RateDTO> getRates() {
		return rateService.getRates();
	}

	/**
	 * Delete a rateId.
	 * 
	 * Service URL: /rate/{rateId} method: DELETE.
	 * 
	 * @param rateId the rateId
	 */
	@DeleteMapping(value = "{rateId}")
	public void deleteRate(@PathVariable long rateId) {
		rateService.deleteRate(rateId);
	}

	/**
	 * To get rates by function
	 * 
	 * Service URL: /rate/function/{rateId} method: GET.
	 * 
	 * @param functionId the functionId
	 * 
	 * @return the list of RateDTO
	 */
	@GetMapping(value = "function/{functionId}")
	public List<RateDTO> getRateByFunction(@PathVariable long functionId) {
		return rateService.findRateByFunction(functionId);
	}

	/**
	 * To get rates by function
	 * 
	 * Service URL: /rate/company/{companyId} method: GET.
	 * 
	 * @param companyId the companyId
	 * 
	 * @return the list of rates
	 */
	@GetMapping(value = "company/{companyId}")
	public List<RateDTO> getRateByCompany(@PathVariable long companyId) {
		return rateService.findRatesByCompany(companyId);
	}

	/**
	 * To get rate drop-down data
	 * 
	 * Service URL:/rate/dropdown , method: GET.
	 * 
	 * @return RateDropdownDTO object
	 * 
	 */
	@GetMapping("/dropdown")
	public RateDropdownDTO getFunctionDropdown() {
		return rateService.getRateDropdowns();
	}

}
