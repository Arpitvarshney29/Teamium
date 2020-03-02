package com.teamium.controller.prod.resources.suppliers;

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

import com.teamium.dto.SpreadsheetMessageDTO;
import com.teamium.dto.SupplierDTO;
import com.teamium.dto.VendorDropdownDTO;
import com.teamium.service.prod.resources.suppliers.SupplierService;

/**
 * <p>
 * Handles operations related to supplier like:providing list of supplier,Saving
 * or updating
 * </p>
 */

@RestController
@RequestMapping("/vendor")
public class SupplierController {

	private SupplierService supplierService;

	public SupplierController(SupplierService supplierService) {
		this.supplierService = supplierService;
	}

	/**
	 * To update or save vendor.
	 * 
	 * Service URL: /vendor method: POST.
	 * 
	 * @param SupplierDTO
	 * @return SupplierDTO
	 */
	@PostMapping
	public SupplierDTO addSupplier(@RequestBody SupplierDTO supplierDTO) {
		return supplierService.saveOrUpdateSupplier(supplierDTO);
	}

	/**
	 * To get vendor detail by vendorId
	 * 
	 * Service URL: /vendor/{vendorId} method: GET.
	 * 
	 * @param supplierId the supplierId
	 * 
	 * @return the SupplierDTO
	 */
	@GetMapping(value = "{vendorId}")
	public SupplierDTO getSupplierDetail(@PathVariable Long vendorId) {
		return supplierService.getSupplier(vendorId);
	}

	/**
	 * To get the list of all supplier.
	 * 
	 * Service URL: /vendor: GET.
	 * 
	 * @return list of SupplierDTO.
	 */
	@GetMapping
	public List<SupplierDTO> getSuppliers() {
		return supplierService.getSuppliers();
	}

	/**
	 * Delete The vendor.
	 * 
	 * Service URL: /vendor/{vendorId} method: DELETE.
	 * 
	 * @param vendorId the vendorId
	 */
	@DeleteMapping(value = "{vendorId}")
	public void deleteSupplier(@PathVariable Long vendorId) {
		supplierService.deleteSupplier(vendorId);
	}

	/**
	 * To get supplier dropdowns
	 * 
	 * Service URL: /vendor/dropdown method: GET.
	 * 
	 * @return the CustomerDropdownDTO
	 */
	@GetMapping("/dropdown")
	public VendorDropdownDTO getVendorDropdowns() {
		return supplierService.getVendorDropdowns();
	}
	
	/**
	 * To upload spreadsheet for client
	 * 
	 * Service URL:/vendor/upload/spreadsheet , method: GET.
	 * @return 
	 * 
	 * 
	 */
	@RequestMapping(value = "/upload/spreadsheet", method = RequestMethod.POST)
	public SpreadsheetMessageDTO getUploadSpreadsheet(@RequestBody MultipartFile spreadsheetFile) throws InvalidFormatException, IOException {
		return supplierService.upload(spreadsheetFile);
	}

}
