package com.teamium.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamium.config.PropConfig;
import com.teamium.constants.Constants;
import com.teamium.domain.Company;
import com.teamium.domain.Document;
import com.teamium.domain.LeaveRequest;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.projects.Program;
import com.teamium.domain.prod.projects.Quotation;
import com.teamium.domain.prod.projects.order.Order;
import com.teamium.domain.prod.resources.equipments.Attachment;
import com.teamium.domain.prod.resources.equipments.Equipment;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.dto.CompanyDTO;
import com.teamium.dto.EquipmentDTO;
import com.teamium.dto.FileUploadDTO;
import com.teamium.dto.LeaveRequestDTO;
import com.teamium.dto.ProgramDTO;
import com.teamium.dto.RecordDTO;
import com.teamium.dto.StaffMemberDTO;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.CompanyRepository;
import com.teamium.repository.EquipmentRepository;
import com.teamium.repository.RecordRepository;
import com.teamium.repository.StaffMemberRepository;
import com.teamium.utils.CommonUtil;
import com.teamium.utils.FileWriter;

/**
 * A service class implementation for Attachment
 *
 */
@Service
public class AttachmentService {

	private EquipmentRepository equipmentRepository;
	private StaffMemberRepository staffMemberRepository;
	private PropConfig propConfig;
	private AuthenticationService authenticationService;
	private RecordRepository<Record> recordRepository;
	private CompanyService companyService;
	private CompanyRepository companyRepository;
	private LeaveRequestService leaveRequestService;
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public AttachmentService(EquipmentRepository equipmentRepository, StaffMemberRepository staffMemberRepository,
			AuthenticationService authenticationService, PropConfig propConfig,
			RecordRepository<Record> recordRepository, CompanyService companyService,
			CompanyRepository companyRepository, LeaveRequestService leaveRequestService) {
		this.equipmentRepository = equipmentRepository;
		this.staffMemberRepository = staffMemberRepository;
		this.authenticationService = authenticationService;
		this.propConfig = propConfig;
		this.recordRepository = recordRepository;
		this.companyService = companyService;
		this.companyRepository = companyRepository;
		this.leaveRequestService = leaveRequestService;
	}

	/**
	 * Method to upload attachment
	 * 
	 * @param fileUploadDTO
	 * @param id
	 * 
	 * @return the entity object which is saved
	 */
	public synchronized Object uploadAttachment(FileUploadDTO fileUploadDTO, Long id) {
		logger.info("Inside AttachmentService, uploadAttachment(): id: " + id);
		if (id == null) {
			logger.info("Please provide id.");
			throw new UnprocessableEntityException("Please provide id.");
		}

		validateFileDTO(fileUploadDTO, Boolean.FALSE);

		if (fileUploadDTO != null) {

			Set<Attachment> attachments = new HashSet<Attachment>();
			Object entity = null;
			String discriminator = fileUploadDTO.getDiscriminator();
			String relativePath = "";

			switch (discriminator) {

			case Constants.EQUIPMENT_STRING:
				if (!authenticationService.isEquipmentManager()) {
					logger.warn("A invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
							+ " tried to upload attachment on equipment.");
					throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
				}
				Equipment equipment = equipmentRepository.findOne(id);
				if (equipment == null) {
					logger.info("Equipment not found.");
					throw new UnprocessableEntityException("Equipment not found.");
				}
				entity = equipment;
				attachments = equipment.getAttachments();
				break;

			case Constants.PERSON_STRING:
				if (!authenticationService.isAdmin()) {
					logger.warn("A invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
							+ " tried to upload attachment on staff.");
					throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
				}
				StaffMember staff = staffMemberRepository.findOne(id);
				if (staff == null) {
					logger.info("Staff not found.");
					throw new NotFoundException("Staff not found.");
				}
				entity = staff;
				attachments = staff.getAttachments();
				break;

			case Constants.PERSON_REEL_STRING:
				break;

			case Constants.BUDGET_STRING:
				if (!authenticationService.isAdmin()) {
					logger.warn("A invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
							+ " tried to upload attachment on budget.");
					throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
				}
				Record budget = this.recordRepository.getRecordById(Quotation.class, id);
				if (budget == null) {
					logger.info("Budget not found with given id : " + id);
					throw new NotFoundException("Budget not found");
				}
				entity = budget;
				attachments = budget.getAttachments();
				break;

			case Constants.COMPANY_STRING:
				if (!authenticationService.isEquipmentManager()) {
					logger.warn("A invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
							+ " tried to upload attachment on company.");
					throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
				}
				Company company = companyService.findCompanyById(id);
				if (company == null) {
					logger.info("Customer not found.");
					throw new NotFoundException("Customer not found.");
				}
				entity = company;
				attachments = company.getAttachments();
				break;

			case Constants.SHOWS_STRING:
				if (!authenticationService.isAdmin()) {
					logger.warn("A invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
							+ " tried to upload attachment on program-shows.");
					throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
				}
				Record program = this.recordRepository.getRecordById(Program.class, id);
				if (program == null) {
					logger.info("Show not found with given id : " + id);
					throw new NotFoundException("Show not found");
				}
				entity = program;
				attachments = program.getAttachments();
				break;

			case Constants.ORDER_STRING:
				if (!authenticationService.isAdmin()) {
					logger.warn("A invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
							+ " tried to upload attachment on order-procurment.");
					throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
				}
				Record order = this.recordRepository.getRecordById(Order.class, id);
				if (order == null) {
					logger.info("Order not found with given id : " + id);
					throw new NotFoundException("Order not found");
				}
				entity = order;
				attachments = order.getAttachments();
				break;
			case Constants.Leave_STRING:
				entity = this.leaveRequestService.findLeaveRequestById(id);
				attachments = new HashSet<Attachment>();
				break;
			default:
				logger.info("Discriminator not valid : " + discriminator);
				throw new UnprocessableEntityException("Please provide valid discriminator.");
			}

			Attachment attachmentEntity = null;
			Optional<Attachment> optionalAttachment = attachments.stream().filter(attachment -> {
				return attachment.getType() != null
						? attachment.getType().equalsIgnoreCase(fileUploadDTO.getAttachmentType())
						: false;
			}).findFirst();
			try {
				attachmentEntity = optionalAttachment.get();
			} catch (Exception e) {
				attachmentEntity = new Attachment();
			}
			if (fileUploadDTO.getIsFeedByUrl() != null && fileUploadDTO.getIsFeedByUrl().equalsIgnoreCase("true")) {
				attachmentEntity.setUrl(CommonUtil.validateURL(fileUploadDTO.getUrl()));
				attachmentEntity.setFeedByUrl(true);
				attachmentEntity.setFileName(null);
				attachmentEntity.setPath(null);
				attachmentEntity.setExtension(null);
			} else {
				try {

					relativePath = discriminator + Constants.ATTACHMENT_STRING + id + "/"
							+ fileUploadDTO.getAttachmentType();

					String absolutePath = FileWriter.saveFile(fileUploadDTO.getFileContent().getBytes(),
							propConfig.getTeamiumResourcesPath() + "/" + relativePath,
							fileUploadDTO.getFileContent().getOriginalFilename());

					String url = propConfig.getAppBaseURL() + "/" + discriminator + Constants.ATTACHMENT_STRING + id
							+ "/" + fileUploadDTO.getAttachmentType() + "/";
					attachmentEntity
							.setUrl(CommonUtil.validateURL(url + fileUploadDTO.getFileContent().getOriginalFilename()));
					attachmentEntity.setFileName(fileUploadDTO.getFileContent().getOriginalFilename());
					attachmentEntity.setPath(absolutePath);
					String extension = FilenameUtils.getExtension(fileUploadDTO.getFileContent().getOriginalFilename());
					attachmentEntity.setExtension(extension);
					logger.info("Attachment:" + fileUploadDTO.getAttachmentType() + " is uploading by file at path: "
							+ absolutePath);
				} catch (Exception e) {
					logger.error("Unable to write file.");
					throw new UnprocessableEntityException("Unable to write file.");
				}
			}
			attachmentEntity.setType(fileUploadDTO.getAttachmentType());

			attachments.add(attachmentEntity);
			logger.info("Saving uplaoding attachment.");
			if (discriminator.equalsIgnoreCase(Constants.EQUIPMENT_STRING)) {
				Equipment object = (Equipment) entity;
				object.setAttachments(attachments);
				return new EquipmentDTO(equipmentRepository.save(object));

			} else if (discriminator.equalsIgnoreCase(Constants.PERSON_STRING)) {
				StaffMember object = (StaffMember) entity;
				object.setAttachments(attachments);
				return new StaffMemberDTO(staffMemberRepository.save(object));

			} else if (discriminator.equalsIgnoreCase(Constants.BUDGET_STRING)) {
				Record object = (Record) entity;
				object.setAttachments(attachments);
				return new RecordDTO(recordRepository.save(object));

			} else if (discriminator.equalsIgnoreCase(Constants.PERSON_REEL_STRING)) {
				// return attachmentEntity.getUrl() == null ? "" :
				// attachmentEntity.getUrl().getPath();
				return attachmentEntity.getUrl().toString();

			} else if (discriminator.equalsIgnoreCase(Constants.COMPANY_STRING)) {
				Company object = (Company) entity;
				object.setAttachments(attachments);
				return new CompanyDTO(companyRepository.save(object));

			} else if (discriminator.equalsIgnoreCase(Constants.SHOWS_STRING)) {
				Record object = (Record) entity;
				object.setAttachments(attachments);
				return new RecordDTO(recordRepository.save(object));

			} else if (discriminator.equalsIgnoreCase(Constants.ORDER_STRING)) {
				Record object = (Record) entity;
				object.setAttachments(attachments);
				return new RecordDTO(recordRepository.save(object));
			} else if (discriminator.equalsIgnoreCase(Constants.Leave_STRING)) {
				LeaveRequest leaveRequest = (LeaveRequest) entity;
				leaveRequest.setAttachment(attachmentEntity);
				return new LeaveRequestDTO(leaveRequestService.saveLeaveRequest(leaveRequest));
			}

		} else {
			logger.error("Tried to upload invalid data.");
			throw new UnprocessableEntityException("Inavlid request.");
		}
		return null;
	}

	/**
	 * Validate FileUploadDTO.
	 * 
	 * @param fileUploadDTO
	 * @param isPicture
	 */
	private void validateFileDTO(FileUploadDTO fileUploadDTO, boolean isPicture) {
		logger.info("Inside AttachmentService::validateFileDTO()");
		if (!isPicture) {
			if (fileUploadDTO.getAttachmentType() == null) {
				logger.info("Please choose valid attachment type. " + fileUploadDTO.getAttachmentType());
				throw new UnprocessableEntityException("Please choose valid attachment type.");
			}
		}
		if (isPicture) {
			if (fileUploadDTO.getFileContent() != null) {
				String extension = FilenameUtils.getExtension(fileUploadDTO.getFileContent().getOriginalFilename());
				if (!extension.equalsIgnoreCase("jpg") && !extension.equalsIgnoreCase("jpeg")
						&& !extension.equalsIgnoreCase("png")) {
					logger.info("Please choose valid file extension : " + extension);
					throw new UnprocessableEntityException(
							"Please choose valid file extension. Supported only JPG, JPEG and PNG formats.");
				}
			}
		}

		if (fileUploadDTO.getIsFeedByUrl() == null || fileUploadDTO.getIsFeedByUrl().equalsIgnoreCase("false")) {
			if (fileUploadDTO.getFileContent() == null) {
				logger.info("Please choose file.");
				throw new UnprocessableEntityException("Please choose file.");
			}

			String extension = FilenameUtils.getExtension(fileUploadDTO.getFileContent().getOriginalFilename());
			if (!extension.equalsIgnoreCase("pdf") && !extension.equalsIgnoreCase("jpg")
					&& !extension.equalsIgnoreCase("jpeg") && !extension.equalsIgnoreCase("png")) {
				logger.info("Please choose valid file extension : " + extension);
				throw new UnprocessableEntityException(
						"Please choose valid file extension. Supported only JPG, JPEG, PNG and PDF formats.");
			}
			if (fileUploadDTO.getFileContent().getSize() > (2 * 1024 * 1024)) {
				logger.info("Please choose file smaller than 2 MB.");
				throw new UnprocessableEntityException("Please choose file smaller than 2 MB.");
			}
		} else if (StringUtils.isBlank(fileUploadDTO.getUrl())) {
			logger.info("Please provide url.");
			throw new UnprocessableEntityException("Please provide url.");
		}
		logger.info("Returning after validating fileUploadDTO.");
	}

	/**
	 * To upload picture.
	 * 
	 * @param fileUploadDTO
	 * @param id
	 * 
	 * @return object which is saved
	 */
	public Object uploadPicture(FileUploadDTO fileUploadDTO, Long id) {
		logger.info("Inside AttachmentService :: uploadPicture() , id: " + id);
		if (id == null) {
			logger.info("Please provide valid id.");
			throw new UnprocessableEntityException("Please provide valid id.");
		}

		validateFileDTO(fileUploadDTO, Boolean.TRUE);

		if (fileUploadDTO != null) {

			Document document = new Document();
			Object entity = null;
			String discriminator = fileUploadDTO.getDiscriminator();

			switch (discriminator) {
			case Constants.EQUIPMENT_STRING:
				if (!authenticationService.isEquipmentManager()) {
					logger.warn("A invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
							+ " tried to upload picture on equipment.");
					throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
				}
				Equipment equipment = equipmentRepository.findOne(id);
				if (equipment == null) {
					logger.info("Equipment not found.");
					throw new UnprocessableEntityException("Equipment not found.");
				}
				entity = equipment;
				document = equipment.getPhoto() == null ? new Document() : equipment.getPhoto();
				break;

			case Constants.PERSON_STRING:
				if (!authenticationService.isAdmin()) {
					logger.warn("A invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
							+ " tried to upload picture on staff.");
					throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
				}
				StaffMember staff = staffMemberRepository.findOne(id);
				if (staff == null) {
					logger.info("Staff not found.");
					throw new UnprocessableEntityException("Staff not found.");
				}
				entity = staff;
				document = staff.getPhoto() == null ? new Document() : staff.getPhoto();
				break;

			case Constants.COMPANY_STRING:
				if (!authenticationService.isAdmin()) {
					logger.warn("A invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
							+ " tried to upload picture on company.");
					throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
				}
				Company company = companyRepository.findOne(id);
				if (company == null) {
					logger.info("Staff not found.");
					throw new UnprocessableEntityException("Staff not found.");
				}
				entity = company;
				document = company.getLogo() == null ? new Document() : company.getLogo();
				break;

			case Constants.SHOWS_STRING:
				if (!authenticationService.isAdmin()) {
					logger.warn("A invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
							+ " tried to upload picture on show.");
					throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
				}
				Program program = null;
				Record recordEntity = recordRepository.getRecordById(Program.class, id);
				if (recordEntity == null) {
					logger.info("Show not found with given id : " + id);
					throw new UnprocessableEntityException("Show not found.");
				}
				if (recordEntity instanceof Program) {
					logger.info("Record found is an instance of program-show : recordId : " + id);
					program = (Program) recordEntity;
				}
				if (program == null) {
					logger.info("Record found is not an instance of show : recordId : " + id);
					throw new UnprocessableEntityException("Record found is not an instance of show");
				}
				entity = program;
				document = program.getDocument() == null ? new Document() : program.getDocument();
				break;

			default:
				logger.info("discriminator not valid : " + discriminator);
				throw new UnprocessableEntityException("Please provide valid discriminator.");
			}

			String url = "";
			if (fileUploadDTO.getIsFeedByUrl() != null && fileUploadDTO.getIsFeedByUrl().equalsIgnoreCase("true")) {
				url = fileUploadDTO.getUrl();
				logger.info("Picture is uploading by url.");
				document.setPath(null);
			} else {
				if (fileUploadDTO.getFileContent() != null) {
					try {
						String absolutePath = FileWriter.saveFile(
								fileUploadDTO.getFileContent().getBytes(), propConfig.getTeamiumResourcesPath() + "/"
										+ discriminator + Constants.PICTURE_STRING + id + "/",
								fileUploadDTO.getFileContent().getOriginalFilename());
						document.setPath(absolutePath);
						url = propConfig.getAppBaseURL() + "/" + discriminator + Constants.PICTURE_STRING + id + "/"
								+ fileUploadDTO.getFileContent().getOriginalFilename();
						logger.info("Picture is uploading by file at path: " + absolutePath);
					} catch (Exception e) {
						logger.error("Null file uploaded.");
						throw new UnprocessableEntityException("Please choose a file.");
					}
				}
			}

			document.setType("image");
			document.setUrl(CommonUtil.validateURL(url));

			if (discriminator.equalsIgnoreCase(Constants.EQUIPMENT_STRING)) {
				Equipment object = (Equipment) entity;
				object.setPhoto(document);
				logger.info("Returning after uploading picture.");
				return new EquipmentDTO(equipmentRepository.save(object));

			} else if (discriminator.equalsIgnoreCase(Constants.PERSON_STRING)) {
				StaffMember object = (StaffMember) entity;
				object.setPhoto(document);
				logger.info("Returning after uploading picture.");
				return new StaffMemberDTO(staffMemberRepository.save(object));
			} else if (discriminator.equalsIgnoreCase(Constants.COMPANY_STRING)) {
				Company object = (Company) entity;
				object.setLogo(document);
				logger.info("Returning after uploading picture.");
				return new CompanyDTO(companyRepository.save(object));
			} else if (discriminator.equalsIgnoreCase(Constants.SHOWS_STRING)) {
				Program object = (Program) entity;
				object.setDocument(document);
				logger.info("Returning after uploading picture.");
				return new ProgramDTO((Program) recordRepository.save(object));
			}
		} else {
			logger.error("Tried to upload invalid data.");
			throw new UnprocessableEntityException("Inavlid request.");
		}
		return null;
	}

	/**
	 * To delete attachment
	 * 
	 * @param id
	 * @param attachmentId
	 * @param discriminator
	 */
	public void deleteAttachment(Long id, List<Long> attachmentIds, String discriminator) {
		logger.info("Inside AttachmentService :: deleteAttachment()");
		logger.info("discriminator : " + discriminator);
		logger.info("id : " + id);
		logger.info("attachmentIds : " + attachmentIds);

		Object entity = null;
		Set<Attachment> attachments = new HashSet<Attachment>();

		switch (discriminator) {
		case Constants.EQUIPMENT_STRING:
			if (!authenticationService.isEquipmentManager()) {
				logger.warn("A invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
						+ " tried to delete attachment on equipment.");
				throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
			}
			Equipment equipment = equipmentRepository.findOne(id);
			if (equipment == null) {
				logger.info("Equipment not found.");
				throw new UnprocessableEntityException("Equipment not found.");
			}
			entity = equipment;
			attachments = equipment.getAttachments();
			break;

		case Constants.PERSON_STRING:
			if (!authenticationService.isAdmin()) {
				logger.warn("A invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
						+ " tried to delete attachment on staff.");
				throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
			}
			StaffMember staff = staffMemberRepository.findOne(id);
			if (staff == null) {
				logger.info("Staff not found.");
				throw new UnprocessableEntityException("Staff not found.");
			}
			entity = staff;
			attachments = staff.getAttachments();
			break;

		case Constants.BUDGET_STRING:
			if (!authenticationService.isAdmin()) {
				logger.warn("A invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
						+ " tried to delete attachment on budget.");
				throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
			}
			Record record = recordRepository.getRecordById(Quotation.class, id);
			if (record == null) {
				logger.info("Budget not found with id : " + id);
				throw new UnprocessableEntityException("Budget not found.");
			}
			entity = record;
			attachments = record.getAttachments();
			break;

		case Constants.ORDER_STRING:
			if (!authenticationService.isAdmin()) {
				logger.warn("A invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
						+ " tried to delete attachment on order-procurment.");
				throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
			}
			Record order = recordRepository.getRecordById(Order.class, id);
			if (order == null) {
				logger.info("Order not found with id : " + id);
				throw new UnprocessableEntityException("Order not found.");
			}
			entity = order;
			attachments = order.getAttachments();
			break;
		case Constants.Leave_STRING:
			entity = this.leaveRequestService.findLeaveRequestById(id);
			attachments = new HashSet<Attachment>();
			LeaveRequest lr = (LeaveRequest) entity;
			if (lr.getAttachment() != null) {
				attachments.add(lr.getAttachment());
			}
			break;
		default:
			logger.info("Discriminator not valid : " + discriminator);
			throw new UnprocessableEntityException("Please provide valid discriminator.");
		}

		if (attachments == null || attachments.isEmpty()) {
			logger.info("Attachment not found.");
			throw new UnprocessableEntityException("No attachment not found with id : " + id);
		}

		if (discriminator.equalsIgnoreCase(Constants.EQUIPMENT_STRING)) {
			Equipment object = (Equipment) entity;
			object.setAttachments(object.getAttachments().stream()
					.filter(attachment -> !attachmentIds.contains(attachment.getId())).collect(Collectors.toSet()));

			this.equipmentRepository.save(object);

		} else if (discriminator.equalsIgnoreCase(Constants.PERSON_STRING)) {
			StaffMember object = (StaffMember) entity;
			object.setAttachments(object.getAttachments().stream()
					.filter(attachment -> !attachmentIds.contains(attachment.getId())).collect(Collectors.toSet()));

			this.staffMemberRepository.save(object);

		} else if (discriminator.equalsIgnoreCase(Constants.BUDGET_STRING)) {
			Record object = (Record) entity;
			object.setAttachments(object.getAttachments().stream()
					.filter(attachment -> !attachmentIds.contains(attachment.getId())).collect(Collectors.toSet()));

			this.recordRepository.save(object);

		} else if (discriminator.equalsIgnoreCase(Constants.ORDER_STRING)) {
			Record object = (Record) entity;
			object.setAttachments(object.getAttachments().stream()
					.filter(attachment -> !attachmentIds.contains(attachment.getId())).collect(Collectors.toSet()));

			this.recordRepository.save(object);
		} else if (discriminator.equalsIgnoreCase(Constants.Leave_STRING)) {
			LeaveRequest leaveRequest = (LeaveRequest) entity;
			leaveRequest.setAttachment(null);
			this.leaveRequestService.saveLeaveRequest(leaveRequest);
		}
		logger.info("Returning after deleting attachment.");
	}

	/**
	 * To delete the picture.
	 * 
	 * @param id
	 *            the id.
	 *
	 * @param discriminator
	 *            the discriminator.
	 */
	public void deletePicture(Long id, String discriminator) {
		logger.info("Inside AttachmentService :: deletePicture()");
		logger.info("discriminator : " + discriminator);
		logger.info("discriminator id : " + id);

		Object entity = null;
		Document document = null;

		switch (discriminator) {
		case Constants.EQUIPMENT_STRING:
			if (!authenticationService.isEquipmentManager()) {
				logger.warn("A invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
						+ " tried to upload attachment to equipment.");
				throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
			}
			Equipment equipment = equipmentRepository.findOne(id);
			if (equipment == null) {
				logger.info("Equipment not found");
				throw new UnprocessableEntityException("Equipment not found");
			}
			entity = equipment;
			document = equipment.getPhoto();
			break;

		case Constants.PERSON_STRING:
			if (!authenticationService.isAdmin()) {
				logger.warn("A invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
						+ " tried to upload attachment to equipment.");
				throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
			}
			StaffMember staff = staffMemberRepository.findOne(id);
			if (staff == null) {
				logger.info("Staff not found.");
				throw new UnprocessableEntityException("Staff not found.");
			}
			entity = staff;
			document = staff.getPhoto();
			break;

		default:
			logger.info("discriminator not valid : " + discriminator);
			throw new UnprocessableEntityException("Please provide valid discriminator.");
		}

		if (document == null) {
			logger.info("Picture not found.");
			throw new UnprocessableEntityException(
					"Picture not found on : " + discriminator + " with given id = " + id);
		}

		if (discriminator.equalsIgnoreCase(Constants.EQUIPMENT_STRING)) {
			Equipment object = (Equipment) entity;
			object.setPhoto(null);
			this.equipmentRepository.save(object);

		} else if (discriminator.equalsIgnoreCase(Constants.PERSON_STRING)) {
			StaffMember object = (StaffMember) entity;
			object.setPhoto(null);
			this.staffMemberRepository.save(object);
		}
		logger.info("Returning after deleting picture.");
	}

}
