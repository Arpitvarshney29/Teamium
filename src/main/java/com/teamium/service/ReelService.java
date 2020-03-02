package com.teamium.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.teamium.constants.Constants;
import com.teamium.domain.prod.resources.staff.Reel;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.dto.FileUploadDTO;
import com.teamium.dto.ReelDTO;
import com.teamium.enums.ReelType;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.ReelRepository;
import com.teamium.utils.FileReader;

/**
 * A service class implementation for staff reel
 *
 */
@Service
public class ReelService {

	private ReelRepository reelRepository;
	private AttachmentService attachmentService;
	private StaffMemberService staffMemberService;
	private AuthenticationService authenticationService;
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	public ReelService(ReelRepository reelRepository, AttachmentService attachmentService,
			StaffMemberService staffMemberService, AuthenticationService authenticationService) {
		this.reelRepository = reelRepository;
		this.attachmentService = attachmentService;
		this.staffMemberService = staffMemberService;
		this.authenticationService = authenticationService;
	}

	/**
	 * Method to save or update reel
	 * 
	 * @param ReelDTO
	 * 
	 * @return the entity object which is saved
	 */
	public ReelDTO saveOrUpdateReel(long staffId, ReelDTO reelDTO) {
		logger.info("Inside ReelService, saveOrUpdateReel():: save/update: " + reelDTO);
		if (!authenticationService.isHumanResource()) {
			logger.warn("A invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to save or update staff.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}

		StaffMember staff = staffMemberService.validateStaffMember(staffId);
		Reel reel = validateReel(reelDTO);
		FileUploadDTO fileUploadDto = (FileUploadDTO) reelDTO;
		if (fileUploadDto != null && !(ReelType.BIO.getReelType().equalsIgnoreCase(reel.getType()) && fileUploadDto.getFileContent()==null && fileUploadDto.getUrl()==null  )) {
			boolean feedByUrl = Boolean.valueOf(fileUploadDto.getIsFeedByUrl());
			if (!(reelDTO.getId() != null && !feedByUrl && reelDTO.getFileContent() == null)) {
				fileUploadDto.setAttachmentType(reel.getType());
				fileUploadDto.setDiscriminator("reel");
				Object pathObj = attachmentService.uploadAttachment(fileUploadDto, Long.valueOf(staffId));
				String path = (String) pathObj;

				path = feedByUrl ? fileUploadDto.getUrl() : path;
				reel.setUrl(path);
				reel.setUpload(!feedByUrl);
			}
		}
		reel.setStaff(staff);
		reel = reelRepository.save(reel);
		logger.info("Inside ReelService, saveOrUpdateReel():: returning from saveOrUpdateReel : ");
		return new ReelDTO(reel);

	}

	/**
	 * To validate reel .
	 * 
	 * @param ReelDTO.
	 * 
	 * @return the Reel.
	 */
	private Reel validateReel(ReelDTO reelDTO) {
		logger.info("Inside ReelService, validateReel():: validating reel: " + reelDTO);
		if (StringUtils.isBlank(reelDTO.getTitle())) {
			logger.info("Please choose valid reel title. " + reelDTO.getTitle());
			throw new UnprocessableEntityException("Please choose valid reel title.");
		}
//		if (StringUtils.isBlank(reelDTO.getBio())) {
//			logger.info("Please choose valid reel bio. " + reelDTO.getBio());
//			throw new UnprocessableEntityException("Please choose valid reel bio.");
//		}
		Reel reel;
		if (reelDTO.getId() != null) {
			reel = reelRepository.findOne(Long.valueOf(reelDTO.getId()));
			if (reel == null) {
				logger.info("Invalid reel id. ");
				throw new UnprocessableEntityException("Invalid reel id.");
			}
			reel = reelDTO.getReel(reel);
		} else {
			reel = reelDTO.getReel(null);
		}
		reel.setType(ReelType.getEnum(reelDTO.getType()).getReelType());
		reel.setUpdatedOn(new DateTime());
		logger.info("Inside ReelService, validateReel():: returning from validateReel() : ");
		return reel;
	}

	/**
	 * To Get reel by id .
	 * 
	 * @param reelId
	 *            .
	 * 
	 * @return the ReelDTO.
	 */
	public ReelDTO getReel(long reelId) {
		logger.info("Inside ReelService, getReel():: finding reel with id: " + reelId);
		Reel reel = validateReel(reelId);
		ReelDTO reelDTO = new ReelDTO(reel);
		if (reel.isUpload()) {
			try {
				byte[] fileData = FileReader.getEncodedFileByteArray(reel.getUrl());
				reelDTO.setFile(fileData);
			} catch (IOException e) {
				logger.error("Inside getReel, getReel():: error while finding reel content " + e.getMessage());
			}
		} else {
			reelDTO.setUrl(reel.getUrl());
		}

		logger.info("Inside ReelService, getReel():: returning from getReel() : ");
		return reelDTO;
	}

	/**
	 * To validate reel existence.
	 * 
	 * @param reelId
	 *            the reelId.
	 * 
	 * @return the Reel object.
	 */
	private Reel validateReel(long reelId) {
		logger.info("Inside ReelService, validateReel():: validating reel with id: " + reelId);
		Reel reel = reelRepository.findOne(reelId);
		if (reel == null) {
			logger.error("Reel not found");
			throw new NotFoundException("Reel not found.");
		}
		logger.info("Inside ReelService, validateReel():: returning from validateReel() : ");
		return reel;
	}

	/**
	 * To Get reel list by staffId .
	 * 
	 * @param StaffId
	 *            .
	 * 
	 * @return the ReelDTO list.
	 */
	public List<ReelDTO> getStaffReels(long staffId) {
		logger.info("Inside ReelService, getStaffReels():: finding reel associate with staff : " + staffId);
		StaffMember staff = staffMemberService.validateStaffMember(staffId);
		List<Reel> reels = reelRepository.findByStaff(staff);
		List<ReelDTO> reelsDTO = reels.stream().map(r -> new ReelDTO(r)).collect(Collectors.toList());
		logger.info("Inside ReelService, getStaffReels():: returning from getStaffReels() : ");
		return reelsDTO;
	}

	/**
	 * To delete reel by id .
	 * 
	 * @param reelId
	 *            .
	 * 
	 * @return the String.
	 */
	public void deleteReel(long reelId) {
		logger.info("Inside ReelService, deleteReel():: deleting reel with id: " + reelId);
		validateReel(reelId);
		reelRepository.delete(reelId);
		logger.info("Inside ReelService, deleteReel():: returning from deleteReel() : ");

	}

}
