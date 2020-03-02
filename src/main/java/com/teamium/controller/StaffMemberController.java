package com.teamium.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.teamium.domain.prod.upload.log.SpreadsheetUploadLog;
import com.teamium.dto.DigitalSignatureByteDTO;
import com.teamium.dto.PersonalDocumentDTO;
import com.teamium.dto.ReelDTO;
import com.teamium.dto.SpreadsheetMessageDTO;
import com.teamium.dto.StaffDropDownDTO;
import com.teamium.dto.StaffMemberDTO;
import com.teamium.dto.TimeReportDTO;
import com.teamium.service.PersonalDocumentService;
import com.teamium.service.ReelService;
import com.teamium.service.StaffMemberService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * <p>
 * Handles operations related to StaffMember like: saving or updating a
 * staffMember, deleting an existing staffMember, providing list of all
 * staffMembers.
 * </p>
 * 
 * @author TeamiumNishant
 *
 */
@RestController
@RequestMapping(value = "/staff")
public class StaffMemberController {

	private StaffMemberService staffMemberService;
	private PersonalDocumentService personalDocumentService;
	private ReelService reelService;

	@Autowired
	public StaffMemberController(StaffMemberService staffMemberService, PersonalDocumentService personalDocumentService,
			ReelService reelService) {
		this.staffMemberService = staffMemberService;
		this.personalDocumentService = personalDocumentService;
		this.reelService = reelService;
	}

	/**
	 * To save or update StaffMember
	 * 
	 * @param staffMemberDTO
	 * @return StaffMemberDTO
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST)
	StaffMemberDTO save(@RequestBody StaffMemberDTO staffMemberDTO) throws Exception {
		return this.staffMemberService.saveOrUpdate(staffMemberDTO);
	}

	/**
	 * To get StaffMemberDTO
	 * 
	 * Service URL: /staff/{staffId} method: GET.
	 * 
	 * @param staffId
	 *            the staffId
	 * 
	 * @return the StaffMemberDTO
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	StaffMemberDTO findStaffById(@PathVariable("id") long id) {
		return this.staffMemberService.findStaffById(id);
	}

	/**
	 * To get list of staff-members.
	 * 
	 * @return list of StaffMemberDTO
	 */
	@RequestMapping(method = RequestMethod.GET)
	List<StaffMemberDTO> getAllStaffMember() {
		return this.staffMemberService.findStaffMembers();
	}
	
	/**
	 * To get list of available staff-members.
	 * 
	 * Service URL: /staff/available method: GET.
	 * 
	 * @return list of avilable StaffMemberDTO
	 */
	@RequestMapping(value = "/available", method = RequestMethod.GET)
	List<StaffMemberDTO> getAvailableStaffMembers() {
		return this.staffMemberService.getAvailableStaffMembers();
	}

	/**
	 * To delete the StaffMember.
	 * 
	 * Service URL: /staff/{staffId} method: DELETE.
	 * 
	 * @param staffId
	 *            the staffMemberId.
	 */
	@RequestMapping(value = "/{staffId}", method = RequestMethod.DELETE)
	void deleteStaffMember(@PathVariable("staffId") long staffMemberId) {
		this.staffMemberService.deleteStaffMember(staffMemberId);
	}

	/**
	 * To save or update personal document Service URL: /staff/document method:
	 * POST.
	 * 
	 * @param staffMemberDTO
	 * 
	 * @return PersonalDocumentDTO
	 */
	@RequestMapping(value = "/document", method = RequestMethod.POST)
	PersonalDocumentDTO saveDocument(@RequestBody PersonalDocumentDTO documentDTO) {
		return this.personalDocumentService.saveOrUpdateDocument(documentDTO);
	}

	/**
	 * To delete the document.
	 * 
	 * Service URL: /staff/document/{documentId} method: DELETE.
	 * 
	 * @param documentId
	 *            the documentId.
	 */
	@RequestMapping(value = "/document/{documentId}", method = RequestMethod.DELETE)
	void deleteDocument(@PathVariable("documentId") Long documentId) {
		this.personalDocumentService.deleteDocument(documentId);
	}

	/**
	 * To delete the document.
	 * 
	 * Service URL: /staff/document/{documentId} method: GET.
	 * 
	 * @param documentId
	 *            the documentId.
	 * @return PersonalDocumentDTO
	 */
	@RequestMapping(value = "/document/{documentId}", method = RequestMethod.GET)
	PersonalDocumentDTO getDocument(@PathVariable("documentId") Long documentId) {
		return this.personalDocumentService.findDocument(documentId);
	}

	/**
	 * To get staff drop-down data
	 * 
	 * Service URL:/staff/dropdown , method: GET.
	 * 
	 * @return StaffDropDownDTO object
	 * 
	 * 
	 */
	@RequestMapping(value = "/dropdown", method = RequestMethod.GET)
	StaffDropDownDTO getEquipmentDropDownData() {
		return staffMemberService.getStaffDropDownData();
	}

	/**
	 * To Enable Or Disable the StaffMember.
	 * 
	 * Service URL: /staff/{staffMemberId}/{available} method: PUT.
	 * 
	 * @param staffMemberId
	 *            the staffMemberId.
	 * 
	 * @param available
	 *            the available.
	 */
	@ApiOperation(value = "Enable Or Disable the StaffMember.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	@RequestMapping(value = "/{staffMemberId}/{available}", method = RequestMethod.PUT)
	void enableOrDisableStaffMember(@PathVariable long staffMemberId, @PathVariable("available") boolean isAvailable)
			throws Exception {
		this.staffMemberService.enableOrDisableStaffMember(staffMemberId, isAvailable);
	}

	/**
	 * To save/update staff reel
	 * 
	 * Service URL:/staff/{staffId}/reel , method: POST.
	 * 
	 * @return ReelDTO object
	 * 
	 * 
	 */
	@PostMapping("/{staffId}/reel")
	ReelDTO saveOrUpdateReel(@PathVariable long staffId, ReelDTO reelDTO, HttpServletRequest request) {
		return reelService.saveOrUpdateReel(staffId, reelDTO);
	}

	/**
	 * To get staff reel by id
	 * 
	 * Service URL:/staff/reel/{reelId} , method: GET.
	 * 
	 * @return ReelDTO object
	 * 
	 */
	@GetMapping("/reel/{reelId}")
	ReelDTO getReel(@PathVariable long reelId) {
		return reelService.getReel(reelId);
	}

	/**
	 * To find staff reel list by staff id
	 * 
	 * Service URL:/staff/{staffId}/reel , method: GET.
	 * 
	 * @return ReelDTO object
	 */
	@GetMapping("/{staffId}/reel")
	List<ReelDTO> getReels(@PathVariable long staffId) {
		return reelService.getStaffReels(staffId);
	}

	/**
	 * To delete staff reel by id
	 * 
	 * Service URL:/staff/reel/{reelId} , method: DELETE.
	 * 
	 */
	@DeleteMapping("/reel/{reelId}")
	void deleteReel(@PathVariable long reelId) {
		reelService.deleteReel(reelId);
	}

	/**
	 * To get logged-in staff-member
	 * 
	 * Service URL:/staff/logged-in-user , method: GET.
	 * 
	 * @return StaffMemberDTO object
	 * 
	 */
	@RequestMapping(value = "/logged-in-user", method = RequestMethod.GET)
	public StaffMemberDTO getLoggedInUser() {
		return staffMemberService.getLoggedInUser();
	}

	/**
	 * To upload staff spreadsheet
	 * 
	 * Service URL:/staff/upload/spreadsheet , method: POST.
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
		return staffMemberService.upload(spreadsheetFile);
	}

	/**
	 * To get spreadsheets-log by logged-in-user
	 * 
	 * @return list of spreadsheet-log
	 */
	@RequestMapping(value = "/all/spreadsheet", method = RequestMethod.GET)
	public List<SpreadsheetUploadLog> getUploadSpreadsheet() {
		return staffMemberService.getSpreadsheetUploadLogByLoggedInUser();

	}

	/**
	 * edit user profile
	 * 
	 * Service URL:/staff/edit-profile/{id} , method: POST.
	 * 
	 * @param staffMemberDTO
	 * @param id
	 * @return StaffMemberDTO
	 * @throws IOException
	 */
	@RequestMapping(value = "/edit-profile/{id}", method = RequestMethod.POST)
	public StaffMemberDTO editProfile(@RequestBody StaffMemberDTO staffMemberDTO, @PathVariable long id,
			HttpServletRequest request) throws IOException {
		return staffMemberService.editProfile(staffMemberDTO, id, request);

	}

	@GetMapping("/timereport")
	public TimeReportDTO TimeReportById(@RequestParam long staffMemberid,
			@DateTimeFormat(pattern = "dd/MM/yyyy") Date startDate,
			@DateTimeFormat(pattern = "dd/MM/yyyy") Date endDate, String type) {
		return staffMemberService.findTimeReportById(staffMemberid, startDate, endDate, type);
	}

	/**
	 * To get digital signature bytecode
	 * 
	 * Service URL:/staff/digital-signature/{id} , method: GET.
	 * 
	 * @param id
	 * 
	 * @return DigitalSignatureByteDTO
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/digital-signature/{id}", method = RequestMethod.GET)
	public DigitalSignatureByteDTO getDigitalSignatureByteCode(@PathVariable long id) throws IOException {
		return staffMemberService.getDigitalSignatureByteCode(id);
	}

	/**
	 * To save digital signature on staff 
	 * 
	 * Service URL:/staff/digital-signature , method: POST.
	 * 
	 * @param signatureByteDTO
	 * @return
	 */
	@RequestMapping(value = "/digital-signature", method = RequestMethod.POST)
	public DigitalSignatureByteDTO setDigitalSignatureUrl(@RequestBody DigitalSignatureByteDTO signatureByteDTO) {
		return staffMemberService.setDigitalSignatureUrl(signatureByteDTO);

	}

}
