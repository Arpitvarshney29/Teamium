package com.teamium.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.AtomicDouble;
import com.teamium.constants.Constants;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.docsign.DocumentGeneration;
import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.projects.Project;
import com.teamium.domain.prod.projects.invoice.Invoice;
import com.teamium.domain.prod.projects.invoice.InvoiceGeneration;
import com.teamium.dto.DateRangeDTO;
import com.teamium.dto.InvoiceDTO;
import com.teamium.dto.InvoiceGenerationDTO;
import com.teamium.dto.LineCostDTO;
import com.teamium.dto.LineDTO;
import com.teamium.dto.ProjectDTO;
import com.teamium.enums.InvoiceType;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.BookingRepository;
import com.teamium.repository.InvoiceGenerationRepository;
import com.teamium.repository.RecordRepository;
import com.teamium.utils.CommonUtil;

@Service
public class InvoiceService {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private RecordService recordService;
	private BookingService bookingService;
	private BookingRepository bookingRepository;

	private RecordRepository recordRepository;
	private InvoiceGenerationRepository invoiceGenerationRepository;

	@Autowired
	@Lazy
	private EditionService editionService;

	@Autowired
	public InvoiceService(RecordService recordService, BookingService bookingService,
			BookingRepository bookingRepository, RecordRepository recordRepository,
			InvoiceGenerationRepository invoiceGenerationRepository) {
		this.recordService = recordService;
		this.bookingService = bookingService;
		this.bookingRepository = bookingRepository;
		this.recordRepository = recordRepository;
		this.invoiceGenerationRepository = invoiceGenerationRepository;
	}

	/**
	 * To get invoice basic details
	 * 
	 * @param projectId
	 * 
	 * @return InvoiceDTO
	 */
	public InvoiceDTO getInvoiceDetail(long projectId) {
		logger.info("Inside InvoiceService, getInvoiceDetail(projectId : " + projectId + ")");

		Record record = validateAndGetRecord(projectId);
		ProjectDTO projectDTO = new ProjectDTO((Project) record);
		projectDTO.getLines().stream().forEach(t -> {
			if (t.getOrigin() != null) {
				t.setFromBudget(true);
			} else {
				t.setFromBudget(false);
			}
			long dateDifference = CommonUtil.dayDifference(t.getFrom(), t.getTo());
			dateDifference = dateDifference + 1;
			t.setNumberOfday(dateDifference);

		});

		String managedBy = record.getFollower() != null
				? record.getFollower().getFirstName() + " " + record.getFollower().getName()
				: "";
		String comapny = record.getEntity() != null ? record.getEntity().getName() : "";
		String currency = record.getCurrency() != null ? record.getCurrency().getCurrencyCode() : "";
		String language = record.getLanguage() != null ? record.getLanguage().getKey() : "";
		String internalReference = record.getReferenceInternal() != null ? record.getReferenceInternal() : "";

		InvoiceDTO invoiceDTO = new InvoiceDTO(projectDTO.getTitle(), projectDTO.getStatus(),
				projectDTO.getFinancialStatus(), managedBy, projectDTO.getOrigin(), comapny, currency, language,
				internalReference);
		invoiceDTO.setBookingDTOs(projectDTO.getLines());
		logger.info("Returning after getting invoice detail");
		return invoiceDTO;

	}

	/**
	 * To validate and get record if invoice not present then create invoice
	 * 
	 * @param projectId
	 * 
	 * @return record
	 */
	private Record validateAndGetRecord(long projectId) {
		logger.info("Inside InvoiceService, validateAndGetRecord(projectId : " + projectId + ")");
		Record record = this.recordService.validateRecordExistence(Project.class, projectId);
		List<Record> invoiceRecords = this.recordRepository.findRecordByProjectId(Invoice.class, projectId);
		invoiceRecords.stream().filter(entity -> entity.getSource() != null);
		if (invoiceRecords.size() == 0) {
			Invoice invoice = new Invoice();
			invoice.setSource(record);
			this.recordRepository.save(invoice);
		}
		logger.info("Returning after getting record");
		return record;
	}

	/**
	 * To create invoice
	 * 
	 * @param invoiceGenerationDTO
	 * 
	 * @return invoiceGenerationDTO
	 */
	public InvoiceGenerationDTO createInvoice(InvoiceGenerationDTO invoiceGenerationDTO) {
		logger.info("Inside InvoiceService, Create(invoiceGenerationDTO : " + invoiceGenerationDTO + ")");
		validateInvoiceGenerationDTO(invoiceGenerationDTO);
		long projectId = invoiceGenerationDTO.getProjectId();
		Record record = validateAndGetRecord(projectId);
		List<Record> invoiceRecords = this.recordRepository.findRecordByProjectId(Invoice.class, projectId);

		if (invoiceRecords.size() == 0) {
			logger.error("No invoice present on this project id");
			throw new UnprocessableEntityException("No invoice present on this project id");
		}
		Optional<Record> invoiceRecord = invoiceRecords.stream().findFirst();
		Invoice invoice = null;
		if (invoiceRecord.isPresent()) {
			invoice = (Invoice) invoiceRecord.get();
		}

		List<InvoiceGeneration> invoiceGenerations = this.invoiceGenerationRepository
				.findInvoiceGenerationByInvoiceId(invoice);

		validateForFinalInvoice(invoiceGenerations, invoiceGenerationDTO);
		InvoiceGeneration invoiceGeneration = new InvoiceGeneration();

		if (invoiceGenerationDTO.getInvoiceType().equalsIgnoreCase(InvoiceType.FINAL.getInvoiceType())) {
			logger.info("Inside final invoice");
			// final type
			if (!record.getStatus().getKey().equalsIgnoreCase(Constants.PROJECT_STATUS_DONE_STRING)) {
				logger.error("Project status must be Done");
				throw new UnprocessableEntityException("Project status must be Done");
			}

			AtomicDouble updateInvoiceAmount = new AtomicDouble(0);
			AtomicDouble updateInvoiceTax = new AtomicDouble(0);
			AtomicDouble updateInvoiceDiscount = new AtomicDouble(0);
			List<Line> lines = new ArrayList<>();
			List<LineDTO> lineDTOs = new ArrayList<>();
			record.getLines().stream().forEach(line -> {
				Float totalPaidAmount = line.getTotalPaidAmount() != null ? line.getTotalPaidAmount() : 0;
				Float remainAmount = line.getTotalWithTax() - totalPaidAmount;
				Float lineVat = line.getVat() == null ? 0f
						: line.getVat().getRate() == null ? 0f : line.getVat().getRate();

				Float finalTax = ((remainAmount * 100) / (100 + lineVat));
				Float finalDiscount = ((finalTax * 100) / (100 - line.getDiscountRate()));
				Float lineCost = finalDiscount;
				finalDiscount = finalDiscount - finalTax;
				finalTax = remainAmount - finalTax;
				line.setTotalPaidAmount(totalPaidAmount + remainAmount);
				lines.add(line);
				updateInvoiceAmount.addAndGet(remainAmount);
				updateInvoiceTax.addAndGet(finalTax);
				updateInvoiceDiscount.addAndGet(finalDiscount);

				LineDTO lineDTO = new LineDTO(line);
				lineDTO.setInvoiceAmount(lineCost);
				lineDTO.setInvoiceDiscount(finalDiscount);
				lineDTO.setInvoiceTax(finalTax);
				lineDTOs.add(lineDTO);

			});
			invoiceGeneration.setInvoice(invoice);
			invoiceGeneration.setInvoicedAmount((float) updateInvoiceAmount.get());
			Calendar currentDate = Calendar.getInstance();
			invoiceGeneration.setInvoiceDate(currentDate);
			XmlEntity xmlEntity = new XmlEntity();
			xmlEntity.setKey(InvoiceType.FINAL.getInvoiceType());
			invoiceGeneration.setInvoiceType(xmlEntity);

			record.setLines(lines);
			this.recordRepository.save(record);
			invoiceGeneration = this.invoiceGenerationRepository.save(invoiceGeneration);

			InvoiceGenerationDTO generationDTO = new InvoiceGenerationDTO(invoiceGeneration);
			generationDTO.setTotalDiscount((float) updateInvoiceDiscount.get());
			generationDTO.setTotalTax((float) updateInvoiceTax.get());
			generationDTO.setLineDTOs(lineDTOs);
			generationDTO.setProjectId(invoiceGenerationDTO.getProjectId());
			generationDTO.setContactId(invoiceGenerationDTO.getContactId());

			invoiceGeneration = editionService.createInvoicePDF(generationDTO);
			generationDTO.setInvoicePdfPath(invoiceGeneration.getInvoicePdfPath());

			logger.info("Returning after create final invoice");
			return generationDTO;

		} else {
			if (invoiceGenerationDTO.getInvoiceType().equalsIgnoreCase(InvoiceType.PROGRESS.getInvoiceType())) {
				// progress type
				logger.info("Inside progress invoice");
				if (invoiceGenerationDTO.getInvoicePercent() == null) {
					logger.error("Please provide invoice percent");
					throw new UnprocessableEntityException("Please provide invoice percent ");
				}

				AtomicDouble updateInvoiceAmount = new AtomicDouble(0);
				AtomicDouble updateInvoiceTax = new AtomicDouble(0);
				AtomicDouble updateInvoiceDiscount = new AtomicDouble(0);

				List<Line> lines = new ArrayList<>();
				List<LineDTO> lineDTOs = new ArrayList<>();
				record.getLines().stream().forEach(line -> {
					LineCostDTO costDTO = getLineCost(line);
					Float previousPaidAmount = line.getTotalPaidAmount() != null ? line.getTotalPaidAmount() : 0f;

					Float calculatedAmount = (((line.getTotalWithTax() - previousPaidAmount)
							* invoiceGenerationDTO.getInvoicePercent()) / 100);
					updateInvoiceAmount.addAndGet(calculatedAmount);

					Float lineAmount = ((costDTO.getLineCost() * invoiceGenerationDTO.getInvoicePercent()) / 100);
					Float calculatedTax = ((costDTO.getVat() * invoiceGenerationDTO.getInvoicePercent()) / 100);
					Float calculateDiscount = ((costDTO.getDiscount() * invoiceGenerationDTO.getInvoicePercent())
							/ 100);

					updateInvoiceTax.addAndGet(calculatedTax);
					updateInvoiceDiscount.addAndGet(calculateDiscount);
					Float linePaidAmount = previousPaidAmount + calculatedAmount;
					line.setTotalPaidAmount(linePaidAmount);
					lines.add(line);

					LineDTO lineDTO = new LineDTO(line);
					lineDTO.setInvoiceAmount(calculatedAmount);
					lineDTO.setInvoiceDiscount(calculateDiscount);
					lineDTO.setInvoiceTax(calculatedTax);
					lineDTO.setInvoiceLineAmount(lineAmount);

					lineDTOs.add(lineDTO);

				});

				invoiceGeneration.setInvoicePercent(invoiceGenerationDTO.getInvoicePercent());
				XmlEntity xmlEntity = new XmlEntity();
				xmlEntity.setKey(InvoiceType.PROGRESS.getInvoiceType());
				invoiceGeneration.setInvoiceType(xmlEntity);
				invoiceGeneration.setInvoicedAmount((float) updateInvoiceAmount.get());
				Calendar currentDate = Calendar.getInstance();
				invoiceGeneration.setInvoiceDate(currentDate);
				invoiceGeneration.setInvoice(invoice);
				record.setLines(lines);
				this.recordRepository.save(record);
				invoiceGeneration = this.invoiceGenerationRepository.save(invoiceGeneration);

				InvoiceGenerationDTO generationDTO = new InvoiceGenerationDTO(invoiceGeneration);
				generationDTO.setTotalDiscount((float) updateInvoiceDiscount.get());
				generationDTO.setTotalTax((float) updateInvoiceTax.get());
				generationDTO.setLineDTOs(lineDTOs);
				generationDTO.setProjectId(invoiceGenerationDTO.getProjectId());
				generationDTO.setContactId(invoiceGenerationDTO.getContactId());

				invoiceGeneration = editionService.createInvoicePDF(generationDTO);
				generationDTO.setInvoicePdfPath(invoiceGeneration.getInvoicePdfPath());

				logger.info("Returning after create progress invoice");
				return generationDTO;

			} else if (invoiceGenerationDTO.getInvoiceType().equalsIgnoreCase(InvoiceType.PERIOD.getInvoiceType())) {
				// period type
				logger.info("Inside period invoice");
				if (StringUtils.isBlank(invoiceGenerationDTO.getFromDate())
						|| StringUtils.isBlank(invoiceGenerationDTO.getToDate())) {
					if (StringUtils.isBlank(invoiceGenerationDTO.getFromDate())) {
						logger.error("Please provide From date");
						throw new UnprocessableEntityException("Please provide From date ");
					} else {
						logger.error("Please provide To date");
						throw new UnprocessableEntityException("Please provide To date ");
					}
				}

				DateRangeDTO range = CommonUtil.validateDateRange(invoiceGenerationDTO.getFromDate(),
						invoiceGenerationDTO.getToDate());

				AtomicDouble updateInvoiceAmount = new AtomicDouble(0);
				AtomicDouble updateInvoiceTax = new AtomicDouble(0);
				AtomicDouble updateInvoiceDiscount = new AtomicDouble(0);
				List<Line> lines = new ArrayList<>();
				List<LineDTO> lineDTOs = new ArrayList<>();
				record.getLines().forEach(line -> {
					LineCostDTO costDTO = getLineCost(line);
					Set<DateTime> userDates = new HashSet<DateTime>();
					Set<DateTime> lineDates = new HashSet<DateTime>();
					Set<DateTime> previousInvoiceDates = new HashSet<DateTime>();
					DateTime start = new DateTime(range.getStartDate(), DateTimeZone.forID("Asia/Calcutta"))
							.withTimeAtStartOfDay();
					DateTime end = new DateTime(range.getEndDate(), DateTimeZone.forID("Asia/Calcutta"))
							.withTimeAtStartOfDay();

					for (DateTime date = start; date.isBefore(end) || date.equals(end); date = date.plusDays(1)) {
						userDates.add(date.withTimeAtStartOfDay());
					}

					start = new DateTime(((Booking) line).getFrom(), DateTimeZone.forID("Asia/Calcutta"))
							.withTimeAtStartOfDay();
					end = new DateTime(((Booking) line).getTo(), DateTimeZone.forID("Asia/Calcutta"))
							.withTimeAtStartOfDay();

					for (DateTime date = start; date.isBefore(end) || date.equals(end); date = date.plusDays(1)) {
						lineDates.add(date.withTimeAtStartOfDay());
					}

					if (invoiceGenerations.size() != 0) {
						invoiceGenerations.stream().forEach(entity -> {

							if (entity.getUserFromDate() != null || entity.getUserToDate() != null) {
								DateTime invoiceStart = null;
								DateTime invoiceEnd = null;

								if (entity.getUserFromDate() != null) {
									invoiceStart = new DateTime(entity.getUserFromDate(),
											DateTimeZone.forID("Asia/Calcutta")).withTimeAtStartOfDay();
								}
								if (entity.getUserToDate() != null) {
									invoiceEnd = new DateTime(entity.getUserToDate(),
											DateTimeZone.forID("Asia/Calcutta")).withTimeAtStartOfDay();

								}
								for (DateTime date = invoiceStart; date.isBefore(invoiceEnd)
										|| date.equals(invoiceEnd); date = date.plusDays(1)) {
									previousInvoiceDates.add(date.withTimeAtStartOfDay());
								}

							}

						});

					}

					// remove if prevoius invoice created

					if (previousInvoiceDates.size() != 0) {
						userDates.removeIf(date -> previousInvoiceDates.contains(date));
					}

					// remove if line is not contain
					if (lineDates.size() != 0) {
						userDates.removeIf(st -> !lineDates.contains(st));
					}

					int calculateSize = userDates.size();
					// sum of all total invoice created days
					long totalInvoiceDays = line.getInvoiceDay() != null ? line.getInvoiceDay() : 0;

					long totalDaysofLine = CommonUtil.dayDifference(((Booking) line).getFrom(),
							((Booking) line).getTo());
					totalDaysofLine = totalDaysofLine + 1;

					Float lineCost = line.getQtySoldPerOc() * line.getUnitPrice();
					Float lineCostWithDiscount = (lineCost * line.getDiscountRate()) / 100;
					Float lineCostWithTax = ((lineCost - lineCostWithDiscount) * line.getVat().getRate()) / 100;
					Float lineCostWithAll = lineCost - lineCostWithDiscount + lineCostWithTax;
					Float previousPaidAamount = line.getTotalPaidAmount() != null ? line.getTotalPaidAmount() : 0;
					Float remainAmount = lineCostWithAll - previousPaidAamount;
					long remainDay = totalDaysofLine - totalInvoiceDays;
					Float linePaidAmount = null;

					if (remainDay == 0) {
						linePaidAmount = 0f;
					} else {
						linePaidAmount = ((lineCostWithAll - previousPaidAamount) * userDates.size())
								/ (totalDaysofLine - totalInvoiceDays);
					}
					// not change position updateInvoiceAmount
					updateInvoiceAmount.addAndGet(linePaidAmount);
					linePaidAmount = previousPaidAamount + linePaidAmount;

					Float calculatedDiscount = costDTO.getDiscount();

					Float calculatedTax = costDTO.getVat();

					Float lineAmount = ((costDTO.getLineCost() * userDates.size())
							/ (totalDaysofLine - totalInvoiceDays));

					if (remainDay == 0) {
						calculatedDiscount = 0f;
					} else {
						calculatedDiscount = ((calculatedDiscount * userDates.size())
								/ (totalDaysofLine - totalInvoiceDays));

					}

					if (remainDay == 0) {
						calculatedTax = 0f;
					} else {
						calculatedTax = ((calculatedTax * userDates.size()) / (totalDaysofLine - totalInvoiceDays));
					}

					updateInvoiceTax.addAndGet(calculatedTax);
					updateInvoiceDiscount.addAndGet(calculatedDiscount);

					totalInvoiceDays = totalInvoiceDays + calculateSize;
					line.setTotalPaidAmount(linePaidAmount);
					line.setInvoiceDay(totalInvoiceDays);
					lines.add(line);

					LineDTO lineDTO = new LineDTO(line);
					lineDTO.setInvoiceAmount(linePaidAmount);
					lineDTO.setInvoiceDiscount(calculatedDiscount);
					lineDTO.setInvoiceTax(calculatedTax);
					lineDTO.setInvoiceLineAmount(lineAmount);
					lineDTOs.add(lineDTO);
				});
				invoiceGeneration.setInvoice(invoice);
				invoiceGeneration.setInvoicedAmount((float) updateInvoiceAmount.get());
				Calendar currentDate = Calendar.getInstance();
				invoiceGeneration.setInvoiceDate(currentDate);
				invoiceGeneration.setUserFromDate(range.getStartDate());
				invoiceGeneration.setUserToDate(range.getEndDate());
				XmlEntity xmlEntity = new XmlEntity();
				xmlEntity.setKey(InvoiceType.PERIOD.getInvoiceType());
				invoiceGeneration.setInvoiceType(xmlEntity);

				record.setLines(lines);
				this.recordRepository.save(record);
				invoiceGeneration = this.invoiceGenerationRepository.save(invoiceGeneration);

				InvoiceGenerationDTO generationDTO = new InvoiceGenerationDTO(invoiceGeneration);
				generationDTO.setTotalDiscount((float) updateInvoiceDiscount.get());
				generationDTO.setTotalTax((float) updateInvoiceTax.get());
				generationDTO.setLineDTOs(lineDTOs);

				generationDTO.setProjectId(invoiceGenerationDTO.getProjectId());
				generationDTO.setContactId(invoiceGenerationDTO.getContactId());

				invoiceGeneration = editionService.createInvoicePDF(generationDTO);
				generationDTO.setInvoicePdfPath(invoiceGeneration.getInvoicePdfPath());

				logger.info("Returning after create progress invoice");
				return generationDTO;
			} else {
				logger.error("Invalid invoice type");
				throw new UnprocessableEntityException("Invalid invoice type");
			}
		}

	}

	/**
	 * To validate for final invoice
	 * 
	 * @param invoiceGenerations
	 * @param invoiceGenerationDTO
	 */
	private void validateForFinalInvoice(List<InvoiceGeneration> invoiceGenerations,
			InvoiceGenerationDTO invoiceGenerationDTO) {
		logger.info("Inside InvoiceService, validateForFinalInvoice()");
		boolean finalInvoice = invoiceGenerations.stream()
				.filter(t -> t.getInvoiceType().getKey().equalsIgnoreCase("final")).collect(Collectors.toList())
				.size() >= 1;

		if (finalInvoice) {
			throw new UnprocessableEntityException("Final invoice created");
		}
		logger.info("Returning after validate invoice");
	}

	/**
	 * To validate invoiceGenerationDTO
	 * 
	 * @param invoiceGenerationDTO
	 */
	private void validateInvoiceGenerationDTO(InvoiceGenerationDTO invoiceGenerationDTO) {
		logger.info("Inside InvoiceService, validateInvoiceGenerationDTO(InvoiceGenerationDTO " + invoiceGenerationDTO
				+ ")");
		if (invoiceGenerationDTO == null) {
			logger.error("Please provide project id");
			throw new UnprocessableEntityException("Please provide project id");
		}
		if (invoiceGenerationDTO.getProjectId() == 0) {
			logger.error("Please provide project id");
			throw new UnprocessableEntityException("Please provide project id");
		}
		if (invoiceGenerationDTO.getContactId() == 0) {
			logger.error("Please provide contact id");
			throw new UnprocessableEntityException("Please provide contact id");
		}
		if (StringUtils.isBlank(invoiceGenerationDTO.getInvoiceType())) {
			logger.error("Please provide valid invoice type");
			throw new UnprocessableEntityException("Please provide valid invoice type");
		}
		logger.info("Returning after validate invoiceDTO");
	}

	/**
	 * To get line cost
	 * 
	 * @param line
	 * 
	 * @return costDTO
	 */
	private LineCostDTO getLineCost(Line line) {
		logger.info("Inside InvoiceService, getLineCost(Line " + line + ")");

		Float previousPaidAmount = line.getTotalPaidAmount() != null ? line.getTotalPaidAmount() : 0f;
		Float remainAmount = line.getTotalWithTax() - previousPaidAmount;
		Float lineVat = line.getVat() == null ? 0f : line.getVat().getRate() == null ? 0f : line.getVat().getRate();

		Float finalTax = ((remainAmount * 100) / (100 + lineVat));
		Float finalDiscount = ((finalTax * 100) / (100 - line.getDiscountRate()));
		Float lineCost = finalDiscount;
		finalDiscount = finalDiscount - finalTax;
		finalTax = remainAmount - finalTax;

		LineCostDTO costDTO = new LineCostDTO(lineCost, finalDiscount, finalTax);
		logger.info("Returning after get line cost");
		return costDTO;
	}

	/**
	 * To get total paid amount of all previous invoices
	 * 
	 * @param projectId
	 * 
	 * @return previousAmount
	 */
	public double getTotalPaidAmountPreviousInvoices(long projectId) {
		logger.info("Inside InvoiceService :: getTotalPaidAmountPreviousInvoices(projectId " + projectId + ")");
		Invoice invoice = getInvoiceByProjectId(projectId);
		List<InvoiceGeneration> invoiceGenerations = this.invoiceGenerationRepository
				.findInvoiceGenerationByInvoiceId(invoice);

		double previousAmount = invoiceGenerations.stream().mapToDouble(price -> price.getInvoicedAmount()).sum();
		logger.info("Returning after get total paid amount of all previous invoices");
		return previousAmount;
	}

	/**
	 * To get invoice by projectId
	 * 
	 * @param projectId
	 * 
	 * @return invoice
	 */
	public Invoice getInvoiceByProjectId(long projectId) {
		logger.info("Inside InvoiceService :: getInvoiceByProjectId(projectId " + projectId + ")");
		List<Record> invoiceRecords = this.recordRepository.findRecordByProjectId(Invoice.class, projectId);

		if (invoiceRecords.size() == 0) {
			logger.error("No invoice present on this project id");
			throw new UnprocessableEntityException("No invoice present on this project id");
		}
		Optional<Record> invoiceRecord = invoiceRecords.stream().findFirst();
		Invoice invoice = null;
		if (invoiceRecord.isPresent()) {
			invoice = (Invoice) invoiceRecord.get();
		}
		logger.info("Returning after get invoice by projectId");
		return invoice;
	}

	/**
	 * To save/update invoice
	 * 
	 * @param invoice
	 * 
	 * @return saveInvoice
	 */
	public InvoiceGeneration saveOrUpdateInvoice(InvoiceGeneration invoice) {
		logger.info("Inside InvoiceService :: save/update invoice : " + invoice);
		InvoiceGeneration saveInvoice = this.invoiceGenerationRepository.save(invoice);
		logger.info("Returning from saveOrUpdateInvoice :: Invoice has saved/updated successfuly");
		return saveInvoice;

	}

	/**
	 * find invoice generation by id
	 * 
	 * @param invoiceGenerationId
	 * 
	 * @return InvoiceGeneration
	 */
	public InvoiceGeneration findInvoiceGenerationById(long invoiceGenerationId) {
		logger.info("Inside InvoiceService :: findInvoiceGenerationById : " + invoiceGenerationId);
		System.out.println(invoiceGenerationId +" invoiceGenerationId");
		InvoiceGeneration saveInvoice = this.invoiceGenerationRepository.findInvoiceGenerationById(invoiceGenerationId);
		logger.info("Returning from get InvoiceGeneration by id");
		return saveInvoice;
	}

	/**
	 * To get all invoice by project id
	 * 
	 * @param projectId
	 * 
	 * @return list of invoiceGenerationDTO
	 */
	public List<InvoiceGenerationDTO> getAllInvoicesByProjectId(long projectId) {
		logger.info("Inside InvoiceService :: getAllInvoicesByProjectId : " + projectId);
		List<Record> invoiceRecords = this.recordRepository.findRecordByProjectId(Invoice.class, projectId);

		Invoice invoice = null;
		Optional<Record> invoiceRecord = invoiceRecords.stream().findFirst();
		if (invoiceRecord.isPresent()) {
			invoice = (Invoice) invoiceRecord.get();
		}
		List<InvoiceGenerationDTO> invoiceGenerationDTOs = new ArrayList<>();
		if (invoice == null) {
			logger.info("Returning empty invoice list");
			return invoiceGenerationDTOs;
		}
		List<InvoiceGeneration> invoiceGenerations = this.invoiceGenerationRepository
				.findInvoiceGenerationByInvoiceId(invoice);

		invoiceGenerationDTOs = invoiceGenerations.stream().map(entity -> new InvoiceGenerationDTO(entity, true))
				.collect(Collectors.toList());
		logger.info("Returning from get all invoice by project id");
		return invoiceGenerationDTOs;
	}

}
