package com.teamium.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.teamium.dto.EquipmentDTO;
import com.teamium.service.PackageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * <p>
 * Handles operations related to equipment like: saving or updating a equipment,
 * deleting an existing equipment, providing list of all equipments.
 * </p>
 */
@RestController
@RequestMapping(value = "/package")
@Api(value = "Package API", description = "All the Package functions.")
public class PackageController {

	private PackageService packageService;

	@Autowired
	public PackageController(PackageService packageService) {
		this.packageService = packageService;
	}

	/**
	 * To get list of all EquipmentDTOs which are not assigned to any Package.
	 * 
	 * Service URL: /package method: GET.
	 * 
	 * @return list of EquipmentDTOs
	 */
	 @ApiOperation(value = "Find all EquipmentDTOs which are not assigned to any Package.")
	    @ApiResponses(value = {
	            @ApiResponse(code = 200, message = "Success"),
	            @ApiResponse(code = 401, message = "Unauthorized"),
	            @ApiResponse(code = 403, message = "Forbidden"),
	            @ApiResponse(code = 404, message = "Not Found"),
	            @ApiResponse(code = 500, message = "Failure")})
	@RequestMapping(value = "/equipment/{equipmentId}" ,method = RequestMethod.GET)
	List<EquipmentDTO> findEquipmentForPackage(@PathVariable long equipmentId) {
		return packageService.findEquipmentForPackage(equipmentId);
	}

	/**
	 * To get list of all EquipmentDTOs on the bases of Selected Package.
	 * 
	 * Service URL: /package method: GET.
	 * 
	 * @return list of EquipmentDTOs
	 */
	 @ApiOperation(value = " Find all EquipmentDTOs on the bases of Selected Package.")
	    @ApiResponses(value = {
	            @ApiResponse(code = 200, message = "Success"),
	            @ApiResponse(code = 401, message = "Unauthorized"),
	            @ApiResponse(code = 403, message = "Forbidden"),
	            @ApiResponse(code = 404, message = "Not Found"),
	            @ApiResponse(code = 500, message = "Failure")})
	@RequestMapping(value = "/{packageId}", method = RequestMethod.GET)
	List<EquipmentDTO> findEquipmentForSelectedPackage(@PathVariable Long packageId) {
		return packageService.findEquipmentForSelectedPackage(packageId);
	}

	/**
	 * To Add or Remove EquipmentResource to the Package.
	 * 
	 * Service URL: /{packageId} method: POST.
	 * 
	 * @param packageId
	 * @param equipmentResourceId
	 * @return String.
	 */
	 @ApiOperation(value = "Add Or Remove EquipmentResource to the Package.")
	    @ApiResponses(value = {
	            @ApiResponse(code = 200, message = "Success"),
	            @ApiResponse(code = 401, message = "Unauthorized"),
	            @ApiResponse(code = 403, message = "Forbidden"),
	            @ApiResponse(code = 404, message = "Not Found"),
	            @ApiResponse(code = 500, message = "Failure")})
	@RequestMapping(value = "/{packageId}", method = RequestMethod.POST)
	void addEquipmentResourceToPackage(@PathVariable Long packageId, @RequestBody List<Long> equipmentResourceId) {
		 packageService.addEquipmentResourceToPackage(packageId, equipmentResourceId);
	}

}
