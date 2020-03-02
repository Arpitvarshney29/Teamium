package com.teamium.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.teamium.dto.FileUploadDTO;
import com.teamium.service.AttachmentService;
import com.teamium.service.StaffMemberService;

import io.swagger.annotations.Api;

/**
 * <p>
 * Handles operations related to uploading attachments and picture.
 * </p>
 */
@RestController
@RequestMapping(value = "/upload")
@Api(value = "Upload API", description = "All the Attachment Uploads.")
public class AttachmentController {

	private AttachmentService attachmentService;
	private StaffMemberService staffMemberService;

	@Autowired
	public AttachmentController(AttachmentService attachmentService, StaffMemberService staffMemberService) {
		this.attachmentService = attachmentService;
		this.staffMemberService = staffMemberService;
	}

	/**
	 * To upload attachment.
	 * 
	 * Service URL: /upload/attachment/{id} method: POST.
	 * 
	 * @param id            the id
	 * @param fileUploadDTO the fileUploadDTO
	 * @param request       the request
	 * 
	 * @return the object
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/attachment/{id}", method = RequestMethod.POST)
	Object uplaodAttachment(@PathVariable long id, FileUploadDTO fileUploadDTO, HttpServletRequest request)
			throws Exception {
		return attachmentService.uploadAttachment(fileUploadDTO, id);
	}

	/**
	 * To upload picture.
	 * 
	 * Service URL: /upload/picture/{id} method: POST.
	 * 
	 * @param id            the id
	 * @param fileUploadDTO the fileUploadDTO
	 * @param request       the request
	 * 
	 * @return the object
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/picture/{id}", method = RequestMethod.POST)
	Object uplaodPicture(@PathVariable long id, FileUploadDTO fileUploadDTO, HttpServletRequest request)
			throws Exception {
		return attachmentService.uploadPicture(fileUploadDTO, id);
	}

	/**
	 * To delete the Attachment.
	 * 
	 * Service URL: /upload/{id}/{attachmentId}/{discriminator} method: DELETE.
	 * 
	 * @param id            the id.
	 * @param attachmentId  the attachmentId.
	 * @param discriminator the discriminator.
	 */
	@RequestMapping(value = "/attachment/{id}/{discriminator}", method = RequestMethod.DELETE)
	void deleteAttachment(@PathVariable long id, @RequestBody(required = false) List<Long> attachmentIds,
			@PathVariable String discriminator) {
		attachmentService.deleteAttachment(id, attachmentIds, discriminator);
	}

	/**
	 * To delete the picture.
	 * 
	 * Service URL: /upload/picture/{id}/{discriminator} method: DELETE.
	 * 
	 * @param id            the id.
	 *
	 * @param discriminator the discriminator.
	 */
	@RequestMapping(value = "/picture/{id}/{discriminator}", method = RequestMethod.DELETE)
	void deletePicture(@PathVariable long id, @PathVariable String discriminator) {
		attachmentService.deletePicture(id, discriminator);
	}

	@PostMapping(value = "/staff")
	String uploadStaff(@RequestParam("file") MultipartFile file, HttpServletRequest request)
			throws InvalidFormatException, IOException {
		staffMemberService.upload(file);
		return "success";
	}

}
