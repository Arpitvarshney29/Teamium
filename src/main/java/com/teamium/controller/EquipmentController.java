package com.teamium.controller;

import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.teamium.dto.EquipmentDTO;
import com.teamium.dto.EquipmentDropDownDTO;
import com.teamium.dto.SpreadsheetMessageDTO;
import com.teamium.service.EquipmentService;

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
@RequestMapping(value = "/equipment")
@Api(value = "Equipments API", description = "All the Equipment's functions.")
public class EquipmentController {

	private EquipmentService equipmentService;

	@Autowired
	public EquipmentController(EquipmentService equipmentService) {
		this.equipmentService = equipmentService;
	}

	/**
	 * To save and update Equipment.
	 * 
	 * Service URL:/equipment method: POST
	 * 
	 * @param equipmentDTO
	 *            the EquipmentDTO object.
	 * 
	 * @throws Exception
	 */

	@ApiOperation(value = "Save the Equipment", response = EquipmentDTO.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = EquipmentDTO.class),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	@RequestMapping(method = RequestMethod.POST)
	EquipmentDTO save(@RequestBody EquipmentDTO equipmentDTO) throws Exception {
		return equipmentService.saveOrUpdate(equipmentDTO);
	}

	/**
	 * To delete the Equipment.
	 * 
	 * Service URL: /equipment/{equipmentId} method: DELETE.
	 * 
	 * @param equipmentId
	 *            the equipmentId.
	 */
	@RequestMapping(value = "/{equipmentId}", method = RequestMethod.DELETE)
	void deleteEquipment(@PathVariable long equipmentId) {
		equipmentService.deleteEquipment(equipmentId);
	}

	/**
	 * To get list of all Equipments
	 * 
	 * Service URL: /equipment method: GET.
	 * 
	 * @return list of EquipmentDTOs
	 */
	@RequestMapping(method = RequestMethod.GET)
	List<EquipmentDTO> findEquipments() {
		return equipmentService.findEquipments();
	}

	/**
	 * To get EquipmentDTO
	 * 
	 * Service URL: /equipment/{equipmentId} method: GET.
	 * 
	 * @param equipmentId
	 *            the equipmentId
	 * 
	 * @return the EquipmentDTO
	 */
	@RequestMapping(value = "/{equipmentId}", method = RequestMethod.GET)
	EquipmentDTO getEquipment(@PathVariable long equipmentId) {
		return equipmentService.getEquipmentById(equipmentId);
	}

	/**
	 * To get equipment drop-down data
	 * 
	 * Service URL:/equipment/dropdown , method: GET.
	 * 
	 * @return EquipmentDropDownDTO object
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/dropdown", method = RequestMethod.GET)
	EquipmentDropDownDTO getEquipmentDropDownData() throws Exception {
		return equipmentService.getEquipmentDropDownData();
	}

	/**
	 * To Enable Or Disable the Equipment.
	 * 
	 * Service URL: /equipment/{equipmentId}/{available} method: PUT.
	 * 
	 * @param equipmentId
	 *            the equipmentId.
	 * 
	 * @param available
	 *            the available.
	 */
	@ApiOperation(value = "Enable Or Disable the Equipment.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	@RequestMapping(value = "/{equipmentId}/{available}", method = RequestMethod.PUT)
	void enableOrDisableEquipment(@PathVariable long equipmentId, @PathVariable("available") boolean isAvailable)
			throws Exception {
		this.equipmentService.enableOrDisableEquipment(equipmentId, isAvailable);
	}
	
	/**
	 * To upload equipment spreadsheet
	 * 
	 * Service URL:/equipment/upload/spreadsheet , method: POST.
	 * 
	 * @param spreadsheetFile
	 * 
	 * @return SpreadsheetMessageDTO message object
	 * 
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	@RequestMapping(value = "/upload/spreadsheet", method = RequestMethod.POST)
	public SpreadsheetMessageDTO getUploadSpreadsheet(@RequestBody MultipartFile spreadsheetFile)
			throws InvalidFormatException, IOException {
		return equipmentService.upload(spreadsheetFile);
	}

}
