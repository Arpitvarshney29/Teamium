package com.teamium.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamium.domain.DigitalSignatureEnvelope;
import com.teamium.domain.EditionTemplateType;
import com.teamium.domain.SignatureHistory;
import com.teamium.domain.SignatureRecipient;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.projects.Program;
import com.teamium.domain.prod.projects.Project;
import com.teamium.domain.prod.projects.Quotation;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.dto.DigitalSignatureEnvelopeDTO;
import com.teamium.dto.SignatureHistoryDTO;
import com.teamium.dto.SignatureRecipientDTO;
import com.teamium.dto.StaffMemberDTO;
import com.teamium.enums.ProjectStatus.ProjectFinancialStatusName;
import com.teamium.enums.ProjectStatus.ProjectStatusName;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnauthorizedException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.DigitalSignatureEnvelopeRepository;
import com.teamium.repository.RecordRepository;
import com.teamium.repository.SignatureHistoryRepository;

@Service
public class DigitalSignatureService {

	private DigitalSignatureEnvelopeRepository digitalSignatureEnvelopeRepository;
	private StaffMemberService staffMemberService;
	private EditionTemplateTypeService editionTemplateTypeService;
	private AuthenticationService authenticationService;
	private RecordRepository<Record> recordRepository;
	private RecordService recordService;
	private SignatureHistoryRepository signatureHistoryRepository;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public DigitalSignatureService(DigitalSignatureEnvelopeRepository dDigitalSignatureEnvelopeRepository,
			StaffMemberService staffMemberService, EditionTemplateTypeService editionTemplateTypeService,
			AuthenticationService authenticationService, RecordRepository<Record> recordRepository,
			RecordService recordService, SignatureHistoryRepository signatureHistoryRepository) {

		this.digitalSignatureEnvelopeRepository = dDigitalSignatureEnvelopeRepository;
		this.staffMemberService = staffMemberService;
		this.editionTemplateTypeService = editionTemplateTypeService;
		this.authenticationService = authenticationService;
		this.recordRepository = recordRepository;
		this.recordService = recordService;
		this.signatureHistoryRepository = signatureHistoryRepository;
	}

	/**
	 * To save or update DigitalSignatureEnvelopeDTO
	 * 
	 * @param digitalSignatureEnvelopeDTO
	 * @return DigitalSignatureEnvelopeDTO
	 */
	public DigitalSignatureEnvelopeDTO saveOrUpdateDigitalSignatureEnvelope(
			DigitalSignatureEnvelopeDTO digitalSignatureEnvelopeDTO) {
		logger.info(
				" Inside DigitalSignatureService: saveOrUpdateDigitalSignatureEnvelope digitalSignatureEnvelopeDTO: "
						+ digitalSignatureEnvelopeDTO.toString());
		if (!authenticationService.isAdmin()) {
			logger.warn("Invalid role");
			throw new UnauthorizedException("Invalid role");
		}
		validateDigitalSignatureEnvelopeDTO(digitalSignatureEnvelopeDTO);
		DigitalSignatureEnvelope digitalSignatureEnvelope = null;

		if (digitalSignatureEnvelopeDTO.getId() != null) {
			digitalSignatureEnvelope = digitalSignatureEnvelopeRepository.findOne(digitalSignatureEnvelopeDTO.getId());
			if (digitalSignatureEnvelope == null) {
				throw new UnprocessableEntityException("Invalid Envelope.");
			}
		}
		EditionTemplateType editionTemplateType = editionTemplateTypeService
				.findByTemplateNameIgnoreCase(digitalSignatureEnvelopeDTO.getTemplateName());
		if (editionTemplateType == null) {
			logger.error("Template not found");
			throw new NotFoundException("Template not found");
		}
		DigitalSignatureEnvelope digitalSignatureEnvelopeByName = digitalSignatureEnvelopeRepository
				.findByNameOrTemplateTypeId(digitalSignatureEnvelopeDTO.getName(), editionTemplateType.getId());
		if (digitalSignatureEnvelope != null) {

			if (digitalSignatureEnvelopeByName != null
					&& (digitalSignatureEnvelope.getId() != digitalSignatureEnvelopeByName.getId())) {
				logger.info("DigitalSignatureEnvelope already exist with given name or tempate name.");
				throw new UnprocessableEntityException(
						"DigitalSignatureEnvelope already exist with given name or tempate name.");
			}
		} else if (digitalSignatureEnvelopeByName != null) {
			logger.info("DigitalSignatureEnvelope already exist with given name or tempate name.");
			throw new UnprocessableEntityException(
					"DigitalSignatureEnvelope already exist with given name or tempate name.");
		}

		List<SignatureRecipient> recipients = validateReciepents(
				digitalSignatureEnvelopeDTO.getSignatureRecipientDTOs(),
				digitalSignatureEnvelope == null ? null : digitalSignatureEnvelope.getSignatureRecipients());
		digitalSignatureEnvelope = new DigitalSignatureEnvelope(digitalSignatureEnvelopeDTO.getId(),
				digitalSignatureEnvelopeDTO.getName(),
				editionTemplateTypeService.findByTemplateNameIgnoreCase(digitalSignatureEnvelopeDTO.getTemplateName()),
				recipients);

		return new DigitalSignatureEnvelopeDTO(digitalSignatureEnvelopeRepository.save(digitalSignatureEnvelope));

	}

	/**
	 * To get list of DigitalSignatureEnvelopeDTO.
	 * 
	 * @return list of DigitalSignatureEnvelopeDTO
	 */
	public List<DigitalSignatureEnvelopeDTO> getAllDigitalSignatureEnvelopes() {
		return digitalSignatureEnvelopeRepository.findAllDigitalSignatureEnvelopes();

	}

	/**
	 * To validate and handle SignatureRecipient.
	 * 
	 * @param signatureRecipientDTOs
	 * @param dbSignatureRecipients
	 * @return
	 */
	private List<SignatureRecipient> validateReciepents(List<SignatureRecipientDTO> signatureRecipientDTOs,
			List<SignatureRecipient> dbSignatureRecipients) {

		AtomicBoolean isRemove = new AtomicBoolean(false);

		if (dbSignatureRecipients != null) {
			List<SignatureRecipient> recipients = new ArrayList<>();
			List<SignatureRecipient> dbLines = dbSignatureRecipients;

			if (signatureRecipientDTOs != null && !signatureRecipientDTOs.isEmpty()) {
				recipients = signatureRecipientDTOs.stream().map(dto -> {

					// removing SignatureRecipient not present in dto
					if (dbLines != null && !dbLines.isEmpty()) {
						dbLines.removeIf(dt -> {
							isRemove.set(true);
							signatureRecipientDTOs.forEach(dt1 -> {
								if (dt1.getId() != null && dt.getId() != null && dt1.getId().equals(dt.getId())) {
									isRemove.set(false);
									return;
								}
							});
							return isRemove.get();
						});
					}
					StaffMember staffMember = staffMemberService.findById(dto.getRecipient().getId());
					if (staffMember == null) {
						throw new UnprocessableEntityException("Invalid recipient");
					}
					return new SignatureRecipient(dto.getId(), staffMember, dto.getRoutingOrder(),
							dto.isApprovalRequired());

				}).collect(Collectors.toList());

			}
			return recipients;
		} else {
			List<SignatureRecipient> recipients = new ArrayList<>();
			signatureRecipientDTOs.forEach(signatureRecipient -> {
				if (signatureRecipient.getRecipient() == null) {
					throw new UnprocessableEntityException("Invalid recipient.");
				}
				StaffMember staffMember = staffMemberService.findById(signatureRecipient.getRecipient().getId());
				if (staffMember == null) {
					throw new UnprocessableEntityException("Invalid recipient");
				} else {
					SignatureRecipient recipient = new SignatureRecipient(signatureRecipient.getId(), staffMember,
							signatureRecipient.getRoutingOrder(), signatureRecipient.isApprovalRequired());
					recipients.add(recipient);
				}
			});
			return recipients;
		}

	}

	/**
	 * To validate DigitalSignatureEnvelopeDTO
	 * 
	 * @param digitalSignatureEnvelopeDTO
	 */
	private void validateDigitalSignatureEnvelopeDTO(DigitalSignatureEnvelopeDTO digitalSignatureEnvelopeDTO) {
		logger.info(" Inside DigitalSignatureService: validateDigitalSignatureEnvelopeDTO digitalSignatureEnvelopeDTO: "
				+ digitalSignatureEnvelopeDTO.toString());
		if (StringUtils.isBlank(digitalSignatureEnvelopeDTO.getName())) {
			logger.error("Envelope name can not be empty.");
			throw new UnprocessableEntityException("Envelope name can not be empty.");
		}
		if (StringUtils.isBlank(digitalSignatureEnvelopeDTO.getTemplateName())) {
			logger.error("Template name can not be empty.");
			throw new UnprocessableEntityException("Template name can not be empty.");
		}
		if (editionTemplateTypeService
				.findByTemplateNameIgnoreCase(digitalSignatureEnvelopeDTO.getTemplateName()) == null) {
			logger.error("Invalid template name.");
			throw new UnprocessableEntityException("Invalid template name.");
		}
		if (digitalSignatureEnvelopeDTO.getSignatureRecipientDTOs().isEmpty()) {
			logger.error("Please add atleast one recipient");
			throw new UnprocessableEntityException("Please add atleast one recipient");
		}
		logger.info("Returning after validating digitalSignatureEnvelopeDTO");
	}

	/**
	 * To delete DigitalSignatureEnvelope by id.
	 * 
	 * @param id
	 */
	public void deleteDigitalSignatureEnvelopeById(long id) {
		logger.info("Inside DigitalSignatureService: deleteDigitalSignatureEnvelopeById:" + id);
		if (!authenticationService.isAdmin()) {
			logger.warn("Invalid role");
			throw new UnauthorizedException("Invalid role");
		}
		findById(id);
		digitalSignatureEnvelopeRepository.delete(id);
		logger.info("Successfully deleted");

	}

	/**
	 * To find DigitalSignatureEnvelope by id.
	 * 
	 * @param id
	 * @return DigitalSignatureEnvelope
	 */
	public DigitalSignatureEnvelope findById(long id) {
		logger.info("Inside DigitalSignatureService: findById:" + id);
		DigitalSignatureEnvelope digitalSignatureEnvelope = digitalSignatureEnvelopeRepository.findOne(id);
		if (digitalSignatureEnvelope == null) {
			logger.error("Invalid DigitalSignatureEnvelope id.");
			throw new NotFoundException("Invalid DigitalSignatureEnvelope id.");
		}
		logger.info("Returning after geting DigitalSignatureEnvelope by id");
		return digitalSignatureEnvelopeRepository.findOne(id);
	}

	/**
	 * To find DigitalSignatureEnvelopeDTO by id.
	 * 
	 * @param id
	 * @return DigitalSignatureEnvelopeDTO
	 */
	public DigitalSignatureEnvelopeDTO findDigitalSignatureEnvelopeDTOById(long id) {
		logger.info("Inside DigitalSignatureService: findDigitalSignatureEnvelopeDTOById:" + id);
		DigitalSignatureEnvelopeDTO digitalSignatureEnvelopeDTO = new DigitalSignatureEnvelopeDTO(findById(id));
		logger.info("Returning after geting DigitalSignatureEnvelopeDTO by id");
		return digitalSignatureEnvelopeDTO;
	}

	/**
	 * To get the list of SignatureRecipient for a record by
	 * templateName(EditionTemplateType) and record id.
	 * 
	 * @param templateName
	 * @param recordId
	 * @return List<SignatureHistoryDTO>
	 */
	public List<SignatureHistoryDTO> getAllSignatureRecipient(String templateName, Long recordId) {
		logger.info("Inside DigitalSignatureService: getAllSignatureRecipient() templateName:" + templateName
				+ ", recordId:" + recordId);
		List<SignatureHistoryDTO> signatureHistoryDTOs = new ArrayList<>();
		if (StringUtils.isBlank(templateName)) {
			logger.error("Invalid template name.");
			throw new NotFoundException("Invalid template name.");
		}
		EditionTemplateType editionTemplateType = editionTemplateTypeService.findByTemplateNameIgnoreCase(templateName);
		if (editionTemplateType == null) {
			logger.error("Invalid template name.");
			throw new NotFoundException("Invalid template name.");
		}

		DigitalSignatureEnvelope digitalSignatureEnvelope = digitalSignatureEnvelopeRepository
				.findByTemplateTypeId(editionTemplateType.getId());
		if (digitalSignatureEnvelope == null) {
			return new ArrayList<>();
		}

		Record record = recordRepository.findOne(recordId);
		if (record == null) {
			logger.error("Invalid record id: " + recordId);
			throw new NotFoundException("Invalid record id: " + recordId);
		}

		validateIfTemplateAvailableForRecord(editionTemplateType, recordId);

		signatureHistoryDTOs.addAll(signatureHistoryRepository
				.findByRecordIdAndEditionTemplateTypeId(record.getId(), editionTemplateType.getId()).stream()
				.map(signatureHistory -> new SignatureHistoryDTO(signatureHistory)).collect(Collectors.toList()));
		if (signatureHistoryDTOs.size() == 0) {
			digitalSignatureEnvelope.getSignatureRecipients().stream().forEach(signatureRecipient -> {
				if (signatureRecipient.isApprovalRequired()) {
					SignatureHistoryDTO signatureHistoryDTO = new SignatureHistoryDTO();
					signatureHistoryDTO.setEditionTemplateType(editionTemplateType);
					signatureHistoryDTO.setRecordId(recordId);
					signatureHistoryDTO.setRecipient(
							new StaffMemberDTO().getStaffWithoutContractSettingDTO(signatureRecipient.getRecipient()));
					signatureHistoryDTO.setRoutingOrder(signatureRecipient.getRoutingOrder());
					signatureHistoryDTOs.add(signatureHistoryDTO);
				}
			});
		}
		Optional<StaffMemberDTO> nextRecipientInRoutingOrder = nextRecipientInRoutingOrder(signatureHistoryDTOs);
		if (nextRecipientInRoutingOrder.isPresent()) {
			signatureHistoryDTOs.stream().forEach(signatureHistoryDTO -> {
				if (signatureHistoryDTO.getRecipient().getId().longValue() == nextRecipientInRoutingOrder.get().getId()
						.longValue()) {
					if (signatureHistoryDTO.getRecipient().getId().longValue() == authenticationService
							.getAuthenticatedUser().getId().longValue()) {
						signatureHistoryDTO.setNextInRoutingOrder(true);
					}
				}
			});
		}
		logger.info("Returning from getAllSignees()");
		return signatureHistoryDTOs.stream().sorted((a, b) -> a.getRoutingOrder() - b.getRoutingOrder())
				.collect(Collectors.toList());
	}

	/**
	 * To Validate if the desired template is available for the given record.
	 * 
	 * @param editionTemplateType
	 * @param recordId
	 * @return true if valid else exception.
	 */
	private Boolean validateIfTemplateAvailableForRecord(EditionTemplateType editionTemplateType, Long recordId) {
		logger.info("Inside DigitalSignatureService: validateIfTemplateAvailableForRecord() editionTemplateType:"
				+ editionTemplateType + ", recordId:" + recordId);
		switch (editionTemplateType.getTemplateName()) {
		case "Show Budget Quotation":
			if (recordService.validateRecordExistence(Program.class, recordId) == null) {
				logger.error("'Show Budget Quotation' is not available for this record: " + recordId);
				throw new NotFoundException("'Show Budget Quotation' is not available for this record: " + recordId);
			}
			break;
		case "Project Budget Quotation":
			if (recordService.validateRecordExistence(Quotation.class, recordId) == null) {
				logger.error("'Project Budget Quotation' is not available for this record: " + recordId);
				throw new NotFoundException("'Project Budget Quotation' is not available for this record: " + recordId);
			}
			break;
		case "Project Booking CallSheet":
			if (recordService.validateRecordExistence(Project.class, recordId) == null) {
				logger.error("'Project Booking CallSheet' is not available for this record: " + recordId);
				throw new NotFoundException(
						"'Project Booking CallSheet' is not available for this record: " + recordId);
			}
			break;
		case "Project Booking Packing List":
			if (recordService.validateRecordExistence(Project.class, recordId) == null) {
				logger.error("'Project Booking Packing List' is not available for this record: " + recordId);
				throw new NotFoundException(
						"'Project Booking Packing List' is not available for this record: " + recordId);
			}
			break;
		case "Project Booking Production Statement":
			if (recordService.validateRecordExistence(Project.class, recordId) == null) {
				logger.error("'Project Booking Production Statement' is not available for this record: " + recordId);
				throw new NotFoundException(
						"'Project Booking Production Statement' is not available for this record: " + recordId);
			}
			break;
		case "Project Purchase Order":
			if (recordService.getRecordById(Project.class, recordId) == null) {
				logger.error("'Project Purchase Order' is not available for this record: " + recordId);
				throw new NotFoundException("'Project Purchase Order' is not available for this record: " + recordId);
			}
			break;
		default:
			logger.error("Invalid template name.");
			throw new NotFoundException("Invalid template name.");
		}
		logger.info("Returning from validateIfTemplateAvailableForRecord()");
		return true;
	}

	/**
	 * To get the next Signature Recipient who will sign the document according to
	 * the routing order of the digital signature envelope.
	 * 
	 * @param signatureHistoryDTOs
	 * @return Optional<SignatureRecipientDTO>
	 */
	private Optional<StaffMemberDTO> nextRecipientInRoutingOrder(List<SignatureHistoryDTO> signatureHistoryDTOs) {
		logger.info("Inside DigitalSignatureService: nextRecipientInRoutingOrder() signatureHistoryDTOs:"
				+ signatureHistoryDTOs);
		signatureHistoryDTOs = signatureHistoryDTOs.stream().sorted((a, b) -> a.getRoutingOrder() - b.getRoutingOrder())
				.collect(Collectors.toList());
		for (SignatureHistoryDTO signatureHistoryDTO : signatureHistoryDTOs) {
			if (!signatureHistoryDTO.isSigned()) {
				return Optional.of(signatureHistoryDTO.getRecipient());
			} else if (signatureHistoryDTO.getApproved() != null && !signatureHistoryDTO.getApproved()) {
				return Optional.of(signatureHistoryDTO.getRecipient());
			}
		}
		logger.info("Returning from nextRecipientInRoutingOrder()");
		return Optional.ofNullable(null);
	}

	/**
	 * To sign a record
	 * 
	 * @param signatureHistoryDTO
	 * @return signatureHistoryDTO
	 */
	@Transactional
	public SignatureHistoryDTO signEdition(SignatureHistoryDTO signatureHistoryDTO) {
		logger.info("Inside DigitalSignatureService: sign(" + signatureHistoryDTO + ")");
		SignatureHistoryDTO nextInRoutingOrder = new SignatureHistoryDTO();
		SignatureHistory signatureHistory = new SignatureHistory();
		validateSignatureHistory(signatureHistoryDTO);

		List<SignatureHistoryDTO> signatureHistoryDTOs = getAllSignatureRecipient(
				signatureHistoryDTO.getEditionTemplateType().getTemplateName(), signatureHistoryDTO.getRecordId());
		if (signatureHistoryDTOs.isEmpty()) {
			logger.error("No recipient found.");
			throw new NotFoundException("No recipient found.");
		}

//		After the first signature of a document signatureHistoryDTO.getId should not be null.
		if (signatureHistoryDTO.getId() == null && (signatureHistoryDTOs.get(0).getId() != null)) {
			logger.error("Signature History id cannot be null");
			throw new UnauthorizedException("Signature History id cannot be null");
		}

		List<SignatureHistoryDTO> next = signatureHistoryDTOs.stream()
				.filter(shDTO -> shDTO.isNextInRoutingOrder() == true).collect(Collectors.toList());
		if (!next.isEmpty()) {
			nextInRoutingOrder = next.get(0);
		} else {
			logger.error("Unauthorised recipient to sign this document.");
			throw new UnprocessableEntityException("Unauthorised recipient to sign this document.");
		}
		if (signatureHistoryDTO.getRecipient().getId().longValue() != nextInRoutingOrder.getRecipient().getId()
				.longValue()) {
			logger.error("Unauthorised recipient to sign this document.");
			throw new UnprocessableEntityException("Unauthorised recipient to sign this document.");
		}
		if (signatureHistoryDTO.getRoutingOrder() != nextInRoutingOrder.getRoutingOrder()) {
			logger.error("Routing order is not correct");
			throw new UnprocessableEntityException("Routing order is not correct");
		}

		if (signatureHistoryDTO.getId() == null) {
			signatureHistory = getSignatureHistory(signatureHistoryDTO);
			signatureHistory.setSignatureDate(Calendar.getInstance());
			signatureHistory.setSigned(true);
			signatureHistory = signatureHistoryRepository.save(signatureHistory);
			signatureHistoryDTOs.stream().forEach(sh -> {
				if (sh.getRecipient().getId().longValue() != signatureHistoryDTO.getRecipient().getId().longValue()) {
					signatureHistoryRepository.save(getSignatureHistory(sh));
				}
			});
		} else {
			signatureHistory = signatureHistoryRepository.findOne(signatureHistoryDTO.getId());
			signatureHistory.setApproved(signatureHistoryDTO.getApproved());
			signatureHistory.setComment(signatureHistoryDTO.getComment());
			signatureHistory.setSignatureDate(Calendar.getInstance());
			signatureHistory.setSigned(true);
			signatureHistory = signatureHistoryRepository.save(signatureHistory);
		}
		if (signatureHistory.getApproved() != null) {
			Record record = signatureHistory.getRecord();
			if (signatureHistory.getApproved() == true) {
				record.setFinancialStatus(ProjectFinancialStatusName.APPROVED.getProjectFinancialStatusNameString());
			}
			recordRepository.save(record);
		}
		logger.info("Returning from DigitalSignatureService :: sign()");
		return new SignatureHistoryDTO(signatureHistory);
	}

	/**
	 * For validating a signature history DTO.
	 * 
	 * @param signatureHistoryDTO
	 * @return true if success else throws an exception.
	 */
	private Boolean validateSignatureHistory(SignatureHistoryDTO signatureHistoryDTO) {
		logger.info("Inside DigitalSignatureService: validateSignatureHistory() signatureHistoryDTO:"
				+ signatureHistoryDTO);
		if (signatureHistoryDTO.getApproved() == null) {
			logger.error("Please approve or reject the record to sign.");
			throw new UnprocessableEntityException("Please approve or reject the record to sign.");
		}

		if (signatureHistoryDTO.getApproved() == false && StringUtils.isBlank(signatureHistoryDTO.getComment())) {
			logger.error("Please mention a comment to reject the document.");
			throw new UnprocessableEntityException("Please mention a comment to reject the document.");
		}

//		Doesn't need comment for approval.
		if (signatureHistoryDTO.getApproved() == true && !StringUtils.isBlank(signatureHistoryDTO.getComment())) {
			signatureHistoryDTO.setComment("");
		}

		if (signatureHistoryDTO.getEditionTemplateType().getId() == null) {
			logger.error("Invalid template id");
			throw new UnprocessableEntityException("Invalid template id");
		}
		EditionTemplateType editionTemplateType = editionTemplateTypeService
				.findById(signatureHistoryDTO.getEditionTemplateType().getId());
		if (editionTemplateType == null) {
			logger.error("Invalid editionTemplateType.");
			throw new NotFoundException("Invalid editionTemplateType.");
		}
		signatureHistoryDTO.setEditionTemplateType(editionTemplateType);

		if (signatureHistoryDTO.getRecordId() == null) {
			logger.error("Invalid template id");
			throw new UnprocessableEntityException("Invalid template id");
		}
		Record record = recordRepository.findOne(signatureHistoryDTO.getRecordId());
		if (record == null) {
			logger.error("Invalid record id: " + signatureHistoryDTO.getRecordId());
			throw new NotFoundException("Invalid record id: " + signatureHistoryDTO.getRecordId());
		}

		if (record.getLines() == null || record.getLines().isEmpty()) {
			logger.error("Cannot sign the document as no lines are added yet");
			throw new UnprocessableEntityException("Cannot sign the document as no lines are added yet");
		}
		if (record instanceof Quotation || record instanceof Project) {
			if (record.getCompany() == null) {
				logger.error("Please assign client");
				throw new UnprocessableEntityException("Please assign client");
			}
			if (record.getRecordContacts() == null || record.getRecordContacts().isEmpty()) {
				logger.error("Atleast one contact must be present for the document to sign");
				throw new UnprocessableEntityException("Atleast one contact must be present for the document to sign");
			}
			if (record instanceof Quotation) {
				if (record.getStatus().getKey()
						.equalsIgnoreCase(ProjectStatusName.TO_DO.getProjectStatusNameString())) {
					logger.error("Cannot sign the document because the budget is in 'To Do' status.");
					throw new UnprocessableEntityException(
							"Cannot sign the document because the budget is in 'To Do' status.");
				}
			}

		}

		validateIfTemplateAvailableForRecord(editionTemplateType, signatureHistoryDTO.getRecordId());
		if (signatureHistoryDTO.getId() != null) {
			if (signatureHistoryRepository.findOne(signatureHistoryDTO.getId()) == null) {
				logger.error("Invalid signature history id:" + signatureHistoryDTO.getId());
				throw new UnprocessableEntityException("Invalid signature history id:" + signatureHistoryDTO.getId());
			}
		}

		if (signatureHistoryDTO.getRecipient().getId() == null) {
			logger.error("Invalid recipient id");
			throw new NotFoundException("Invalid recipient id");
		}
		StaffMember staffMember = staffMemberService.validateStaffMember(signatureHistoryDTO.getRecipient().getId());
		if (staffMember == null) {
			logger.error("Invalid recipient id: " + signatureHistoryDTO.getRecipient().getId());
			throw new NotFoundException("Invalid recipient id: " + signatureHistoryDTO.getRecipient().getId());
		}
		signatureHistoryDTO.setRecipient(new StaffMemberDTO(staffMember));
		logger.info("Successfully validated signatureHistoryDTO: " + signatureHistoryDTO);
		return true;
	}

	/**
	 * Converting the DTO to SignatureHistory.
	 * 
	 * @param signatureHistoryDTO
	 * @return SignatureHistory
	 */
	public SignatureHistory getSignatureHistory(SignatureHistoryDTO signatureHistoryDTO) {
		logger.info("Returning from DigitalSignatureService :: getSignatureHistory(" + signatureHistoryDTO + ")");
		SignatureHistory signatureHistory = new SignatureHistory();
		signatureHistory.setId(signatureHistoryDTO.getId());
		signatureHistory.setRecord(recordRepository.findOne(signatureHistoryDTO.getRecordId()));
		signatureHistory.setApproved(signatureHistoryDTO.getApproved());
		signatureHistory.setComment(signatureHistoryDTO.getComment());
		signatureHistory.setEditionTemplateType(signatureHistoryDTO.getEditionTemplateType());
		signatureHistory.setRecipient(signatureHistoryDTO.getRecipient().getStaffMember());
		signatureHistory.setRoutingOrder(signatureHistoryDTO.getRoutingOrder());
		signatureHistory.setSignatureDate(signatureHistoryDTO.getSignatureDate());
		signatureHistory.setSigned(signatureHistoryDTO.isSigned());
		logger.info("Returning from DigitalSignatureService :: getSignatureHistory()");
		return signatureHistory;
	}

	/**
	 * Get all signature by template type and recordId.
	 * 
	 * @param editionTemplateType
	 * @param recordId
	 * @return List of SignatureHistoryDTO
	 */
	public List<SignatureHistoryDTO> getAllSignatures(EditionTemplateType editionTemplateType, Long recordId) {
		logger.info("Returning from DigitalSignatureService :: getAllSignatures(" + editionTemplateType + "," + recordId
				+ ")");
		List<SignatureHistoryDTO> signatureHistoryDTOs = new ArrayList<>();
		if (editionTemplateTypeService.findById(editionTemplateType.getId()) == null) {
			logger.error("Invalid editionTemplateType.");
			throw new NotFoundException("Invalid editionTemplateType.");
		}

		Record record = recordRepository.findOne(recordId);
		if (record == null) {
			logger.error("Invalid record id: " + recordId);
			throw new NotFoundException("Invalid record id: " + recordId);
		}

		validateIfTemplateAvailableForRecord(editionTemplateType, recordId);

		signatureHistoryDTOs.addAll(signatureHistoryRepository
				.findByRecordIdAndEditionTemplateTypeIdAndSigned(record.getId(), editionTemplateType.getId(), true)
				.stream().map(signatureHistory -> new SignatureHistoryDTO(signatureHistory))
				.collect(Collectors.toList()));
		logger.info("Returning from DigitalSignatureService :: getAllSignatures()");
		return signatureHistoryDTOs;
	}

	/**
	 * Check if record needs to be frozen. Return true incase any template of a
	 * record is signed and approved. Else false.
	 * 
	 * @param recordId
	 * @return true or false.
	 */
	public boolean checkIfRecordFrozen(Long recordId) {
		Record record = recordRepository.findOne(recordId);
		if (record == null) {
			logger.error("Invalid record id: " + recordId);
			throw new NotFoundException("Invalid record id: " + recordId);
		}

		boolean freeze = false;

		List<SignatureHistory> signaturesOfAllTemplate = signatureHistoryRepository.findByRecordId(record.getId());

		Map<String, List<SignatureHistory>> collect = signaturesOfAllTemplate.stream()
				.collect(Collectors.groupingBy(sig -> sig.getEditionTemplateType().getTemplateName()));
		collect.entrySet().stream().forEach(entry -> {
			List<SignatureHistoryDTO> signatureHistoryDTOs = entry.getValue().stream()
					.map(entity -> new SignatureHistoryDTO(entity))
					.sorted((a, b) -> a.getRoutingOrder() - b.getRoutingOrder()).collect(Collectors.toList());
			Optional<StaffMemberDTO> nextRecipientInRoutingOrder = nextRecipientInRoutingOrder(signatureHistoryDTOs);
			if (nextRecipientInRoutingOrder.isPresent()) {
				IntStream.range(0, signatureHistoryDTOs.size()).forEach(i -> {
					if (signatureHistoryDTOs.get(i).getRecipient().getId().longValue() == nextRecipientInRoutingOrder
							.get().getId().longValue()) {
						if (i > 0 && signatureHistoryDTOs.get(i - 1).getApproved() != null
								&& signatureHistoryDTOs.get(i - 1).getApproved() == true
								&& signatureHistoryDTOs.get(i).isSigned() == false) {
							throw new UnprocessableEntityException(
									"Record is freezed because it is signed and approved.");
						}
						if (signatureHistoryDTOs.get(i).getApproved() != null
								&& signatureHistoryDTOs.get(i).getApproved() == true) {
							throw new UnprocessableEntityException(
									"Record is freezed because it is signed and approved.");
						}
					}
				});
			} else {
				if (signatureHistoryDTOs.size() != 0) {
					if (signatureHistoryDTOs.get(signatureHistoryDTOs.size() - 1).getApproved() != null
							&& signatureHistoryDTOs.get(signatureHistoryDTOs.size() - 1).getApproved() == true) {
						throw new UnprocessableEntityException("Record is freezed because it is signed and approved.");
					}
				}
			}
		});
		logger.info("Returning from DigitalSignatureService :: checkIfRecordFrozen()");
		return freeze;
	}
}
