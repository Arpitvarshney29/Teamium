package com.teamium.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.teamium.domain.prod.resources.functions.Function;
import com.teamium.dto.FunctionDTO;
import com.teamium.dto.FunctionDropDownDTO;
import com.teamium.service.prod.resources.functions.FunctionService;

/**
 * <p>
 * Handles operations related to function like:providing list of function,Saving
 * or updating
 * </p>
 */

@RestController
@RequestMapping("/function")
public class FunctionController {

	private FunctionService functionService;

	@Autowired
	public FunctionController(FunctionService functionService) {
		this.functionService = functionService;
	}

	/**
	 * To save and update Function.
	 * 
	 * Service URL:/function method: POST
	 * 
	 * @param functionDTO the FucntionDTO object. 
	 */
	@RequestMapping(method = RequestMethod.POST)
	FunctionDTO save(@RequestBody FunctionDTO functionDTO) throws Exception {
		return functionService.saveOrUpdateFunctionAndFolder(functionDTO);
	}

	/**
	 * To delete the function.
	 * 
	 * Service URL: /function/{functionId} method: DELETE.
	 * 
	 * @param fucntionId the fucntionId.
	 */
	@RequestMapping(value = "/{functionId}", method = RequestMethod.DELETE)
	void deleteFunction(@PathVariable long functionId) {
		functionService.deleteFunction(functionId);
	}

	/**
	 * To get list of all Function
	 * 
	 * Service URL: /function method: GET.
	 * 
	 * @return list of FunctionDTOs
	 */
	@RequestMapping(method = RequestMethod.GET)
	List<FunctionDTO> getAllFunctions() {
		return functionService.getFunctions();
	}

	/**
	 * To get FunctionDTO
	 * 
	 * Service URL: /function/{functionId} method: GET.
	 * 
	 * @param functionId the functionId
	 * 
	 * @return the FunctionDTO
	 */
	@RequestMapping(value = "/{functionId}", method = RequestMethod.GET)
	FunctionDTO getFunctionDTO(@PathVariable long functionId) {
		return functionService.findFunction(functionId);
	}

	/**
	 * To get list of all root function
	 * 
	 * Service URL: /function/roots method: GET.
	 * 
	 * @return list of FunctionDTOs
	 */
	@GetMapping("/roots")
	public List<FunctionDTO> getRootFunctions() {
		return functionService.getRootFunctions();
	}

	/**
	 * To get list of all accounting code
	 * 
	 * Service URL: /function/accounting-code method: GET.
	 * 
	 * @return list of accounting code
	 */
	@GetMapping("/accounting-code")
	public List<String> getAccountingCodes() {
		return functionService.getAccountingCodes();
	}

	/**
	 * To get function drop-down data
	 * 
	 * Service URL:/function/dropdown , method: GET.
	 * 
	 * @return FunctionDropDownDTO object
	 * 
	 */
	@GetMapping("/dropdown")
	public FunctionDropDownDTO getFunctionDropdown() {
		return functionService.getFunctionDropdown();
	}

}
