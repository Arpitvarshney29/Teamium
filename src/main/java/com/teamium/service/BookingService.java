package com.teamium.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.teamium.domain.ChannelFormat;
import com.teamium.domain.Group;
import com.teamium.domain.Person;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.DueDate;
import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.RecordInformation;
import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.projects.BookingEvent;
import com.teamium.domain.prod.projects.Program;
import com.teamium.domain.prod.projects.Project;
import com.teamium.domain.prod.projects.Quotation;
import com.teamium.domain.prod.projects.order.BookingOrderForm;
import com.teamium.domain.prod.projects.order.WorkAndTravelOrder;
import com.teamium.domain.prod.projects.order.WorkAndTravelOrder.OrderType;
import com.teamium.domain.prod.resources.Resource;
import com.teamium.domain.prod.resources.equipments.Attachment;
import com.teamium.domain.prod.resources.equipments.EquipmentFunction;
import com.teamium.domain.prod.resources.equipments.EquipmentResource;
import com.teamium.domain.prod.resources.functions.DefaultResource;
import com.teamium.domain.prod.resources.functions.ExtraLine;
import com.teamium.domain.prod.resources.functions.Function;
import com.teamium.domain.prod.resources.functions.ProcessFunction;
import com.teamium.domain.prod.resources.staff.StaffFunction;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.domain.prod.resources.staff.StaffResource;
import com.teamium.dto.AvailablilityDTO;
import com.teamium.dto.BookingDTO;
import com.teamium.dto.ProjectDTO;
import com.teamium.enums.EventStatus;
import com.teamium.enums.ProjectStatus.ProjectFinancialStatusName;
import com.teamium.enums.ProjectStatus.ProjectStatusName;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.BookingEventRepository;
import com.teamium.repository.BookingRepository;
import com.teamium.repository.RecordRepository;

/**
 * A service class implementation for Booking Controller
 *
 */
@Service
public class BookingService {

	private BookingRepository bookingRepository;
	private ResourceService resourceService;
	private BookingEventRepository eventRepository;
	private RecordRepository<Project> recordRepository;
	private AvailabilityService availabilityService;
	private RecordService recordService;
	private StaffMemberService staffMemberService;
	private GroupService groupService;
	private AuthenticationService authenticationService;
	private EventService eventService;
	private WorkAndTravelOrderService workAndTravelOrderService;
	private BookingOrderFormService bookingOrderFormService;
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * @param bookingRepository
	 * @param resourceService
	 * @param eventRepository
	 * @param recordRepository
	 * @param availabilityService
	 * @param recordService
	 */
	@Autowired
	public BookingService(BookingRepository bookingRepository, ResourceService resourceService,
			BookingEventRepository eventRepository, RecordRepository<Project> recordRepository,
			AvailabilityService availabilityService, RecordService recordService, StaffMemberService staffMemberService,
			GroupService groupService, AuthenticationService authenticationService, @Lazy EventService eventService,
			WorkAndTravelOrderService workAndTravelOrderService, BookingOrderFormService bookingOrderFormService) {
		this.bookingRepository = bookingRepository;
		this.resourceService = resourceService;
		this.eventRepository = eventRepository;
		this.recordRepository = recordRepository;
		this.availabilityService = availabilityService;
		this.recordService = recordService;
		this.staffMemberService = staffMemberService;
		this.groupService = groupService;
		this.authenticationService = authenticationService;
		this.eventService = eventService;
		this.workAndTravelOrderService = workAndTravelOrderService;
		this.bookingOrderFormService = bookingOrderFormService;
	}

	/**
	 * Method to find bookings by resourceId and/or dates
	 * 
	 * @param resourceId the resourceId
	 * @param startDate  the startDate
	 * @param endDate    the endDate
	 * @return the list of Booking object
	 */
	public List<Booking> getBookingsByResourceIdAndDates(Long resourceId, DateTime startDate, DateTime endDate) {
		logger.info("Inside BookingService,getBookingsByResourceIdAndDates:equipmentId: " + resourceId + "startDate : "
				+ startDate + "endDate : " + endDate);

		Resource<?> resource = resourceService.findResource(resourceId);

		if (resource == null) {
			logger.error("Resource not found.");
			throw new NotFoundException("Resource not found.");
		}

		List<Booking> bookings = new ArrayList<>();

		if (startDate == null && endDate == null) {
			bookings = bookingRepository.findByResources(resource, Project.class);
		} else if (startDate != null && endDate == null) {
			bookings = bookingRepository.findByResourcesAndStartDate(resource, startDate.toGregorianCalendar());
		} else if (startDate == null && endDate != null) {
			bookings = bookingRepository.findByResourcesAndEndDate(resource, endDate.toGregorianCalendar());
		} else if (startDate != null && endDate != null) {
			bookings = bookingRepository.findByResourcesAndStartDateAndEndDate(resource,
					startDate.toGregorianCalendar(), endDate.toGregorianCalendar());
		}
		logger.info("Returning after finding bookings successfully.");
		return bookings;
	}

	/**
	 * Method to get bookings
	 * 
	 * @param currentUser
	 * 
	 * @return bookings
	 */
	public List<BookingDTO> getAllBookingDTOs(boolean currentUser) {
		logger.info("Inside BookingService,getAllBookingDTOs(currentUser) " + currentUser);
		if (currentUser == true) {
			logger.info("Inside BookingService,return after getting list of bookings by resource function");
			return getBookingsByResouceFunctionForLoggedInUser();
		} else {
			logger.info("Inside BookingService,return after empty list");
			return new ArrayList<>();
		}
	}

	/**
	 * Method to find bookings by resourceId and/or dates
	 * 
	 * @param resourceId the resourceId
	 * @param startDate  the startDate
	 * @param endDate    the endDate
	 * @return the list of BookingDTO object
	 */
	public List<BookingDTO> getWrapperBookingsByResourceIdAndDates(Long resourceId, DateTime startDate,
			DateTime endDate) {
		logger.info("Inside BookingService,getWrapperBookingsByResourceIdAndDates:equipmentId: " + resourceId
				+ "startDate : " + startDate + "endDate : " + endDate);
		return this.getBookingsByResourceIdAndDates(resourceId, startDate, endDate).stream()
				.map(booking -> new BookingDTO(booking)).collect(Collectors.toList());
	}

	/**
	 * Method to find bookings by projcetId
	 * 
	 * @param projcetId the resourceId
	 * @return the list of BookingDTO object
	 */
	public List<BookingDTO> getBookingsByProject(long projectId) {
		List<Booking> bookings = bookingRepository.getBookingsByRecord(projectId);
		return bookings.stream().map(b -> new BookingDTO(b)).collect(Collectors.toList());
	}

	/**
	 * Method to find function -bookings by projcetId
	 * 
	 * @param projcetId the resourceId
	 * 
	 * @return the map of function id and BookingDTO
	 */
	public Map<Long, List<BookingDTO>> getBookingFunctionsByProject(long projectId) {
		List<BookingDTO> bookings = this.getBookingsByProject(projectId);
		return bookings.stream().filter(b -> b.getFunction() != null)
				.collect(Collectors.groupingBy(bookingDTO -> bookingDTO.getFunction().getId()));
	}

	/**
	 * To find booking by id
	 * 
	 * @param bookingId the bookingId
	 * 
	 * @return Booking
	 */
	public Booking findBooking(long bookingId) {
		logger.info("Inside BookingService,findBooking():: finding booking with id " + bookingId);
		Booking booking = bookingRepository.findOne(bookingId);
		if (booking == null) {
			logger.info("Invalid booking id ");
			throw new NotFoundException("Invalid booking id ");
		}
		logger.info("Returing from BookingService,findBooking:");
		return booking;
	}

	/**
	 * To add booking event
	 * 
	 * @param bookingId
	 * 
	 * @return BookingEventDTO
	 */
	public BookingDTO AddBookingEvent(long bookingId) {
		logger.info("Inside BookingService,AddBookingEvent():: Adding booking event to booking with id " + bookingId);
		Booking booking = findBooking(bookingId);
		if (booking.getBookingEvent() == null) {
			BookingEvent event = new BookingEvent();
			event.setFromRaw(booking.getFrom());
			event.setToRaw(booking.getTo());
			if (booking.getFunction() != null) {
				event.setResource(booking.getFunction().getDefaultResource());
			}
			// booking.setBookingEvent(event);
			booking = bookingRepository.save(booking);
			event.setOrigin(booking);
			eventRepository.save(event);
		}
		logger.info("Returing from BookingService,AddBookingEvent::");
		return new BookingDTO(booking);
	}

	/**
	 * To get booking by id
	 * 
	 * @param bookingId
	 * 
	 * @return BookingEventDTO
	 */
	public BookingDTO getBooking(long bookingId) {
		Booking booking = this.findBooking(bookingId);
		return new BookingDTO(booking);
	}

	/**
	 * To add booking event
	 * 
	 * @param bookingId
	 * 
	 * @return BookingEventDTO
	 */
	public List<BookingDTO> AddBookingEventByProject(ProjectDTO projectDTO) {
		Long projectId = projectDTO.getId();
		if (projectId == null) {
			logger.info("Invalid Quotation id ");
			throw new UnprocessableEntityException("Invalid Quotation id ");
		}
		logger.info(
				"Inside BookingService,AddBookingEventByProject():: Adding booking event to booking with project id "
						+ projectId);
		List<BookingDTO> bookingsDTO = new LinkedList<BookingDTO>();
		List<Booking> bookings = bookingRepository.getBookingsByRecord(projectId);
		if (bookings.size() > 0) {
			for (Booking booking : bookings) {
				if (booking.getBookingEvent() == null) {
					BookingEvent event = new BookingEvent();
					event.setFrom(booking.getFrom());
					event.setTo(booking.getTo());
					if (booking.getFunction() != null) {
						DefaultResource resource = new DefaultResource();
						if (booking.getFunction().getDefaultResource() != null) {
							resource = booking.getFunction().getDefaultResource();
						} else {
							resource.setName("Default Resource");
							resource.setFunction(booking.getFunction());
							resource = resourceService.addDefaultResource(resource);
						}
						event.setResource(resource);
					}
					event.setTitle(booking.getDescription());
					booking.setBookingEvent(event);
					// booking.setId(null);
					// booking = bookingRepository.save(booking);
					// booking.setRecord(project);
					event.setOrigin(booking);
					eventRepository.save(event);
				}
				BookingDTO bookingDto = new BookingDTO(booking);
				if (booking.getFunction() == null || booking.getFrom() == null || booking.getTo() == null) {
					continue;
				}
				AvailablilityDTO availablilityDTO = new AvailablilityDTO(booking.getFunction().getId(),
						booking.getFrom(), booking.getTo());
				bookingDto.setAvailableResources(
						availabilityService.getAavailableResourceByFunctionAndBetween(availablilityDTO));
				bookingsDTO.add(bookingDto);

			}
			return bookingsDTO;
		}
		// Record record = recordRepository.findOne(projectId);
		// if (record == null) {
		// logger.info("Invalid Quotation id ");
		// throw new UnprocessableEntityException("Invalid Quotation id ");
		// }
		// Quotation quotation = (Quotation) record;
		// Project project = new ProjectDTO(quotation).getProject(new Project());
		// List<Booking> savedLines = bookingRepository.getBookingseByRecord(projectId);
		// for (Booking line : savedLines) {
		// BookingDTO bookingDto = new BookingDTO(line);
		// Booking booking = bookingDto.getBooking(new Booking());
		// if (booking.getBookingEvent() == null) {
		// BookingEvent event = new BookingEvent();
		//
		// Calendar today = Calendar.getInstance();
		// event.setFrom(today);
		// Calendar tomorrow = Calendar.getInstance();
		// tomorrow.add(Calendar.DATE, 1);
		// event.setTo(tomorrow);
		// if (booking.getFunction() != null) {
		// DefaultResource resource = new DefaultResource();
		// if (line.getFunction().getDefaultResource() != null) {
		// resource = line.getFunction().getDefaultResource();
		// } else {
		// resource.setName("Default Resource");
		// resource.setFunction(booking.getFunction());
		// resource = resourceService.addDefaultResource(resource);
		// }
		// event.setResource(resource);
		// }
		// event.setTitle(booking.getDescription());
		// booking.setBookingEvent(event);
		//// booking.setId(null);
		// booking = bookingRepository.save(booking);
		//// booking.setRecord(project);
		// event.setOrigin(booking);
		// eventRepository.save(event);
		// }

		// bookingDTO.add(new BookingDTO(booking));
		// }

		// logger.info("Returing from BookingService,AddBookingEventByProject::");
		return bookingsDTO;
	}

	/**
	 * To add bookings Quantity
	 * 
	 * @param project
	 * @param booking
	 * @param qty
	 * @param bookingsList
	 */
	private void addBookingsQty(Project project, Booking savedBooking, int qty, List<Booking> bookingsList) {
		logger.info(
				"Inside BookingService :: addBookingsQty(), to add bookings quantity on individual line for record : title : "
						+ project.getTitle() + ", record-id : " + project.getId());
		AtomicBoolean resourceCheck = new AtomicBoolean(false);
		for (int i = 0; i < qty; i++) {
			try {
				Booking booking = savedBooking.cloneWhitoutEvent();
				booking.setOrigin(savedBooking.getId());
				BookingEvent event = new BookingEvent();
				event.setFrom(booking.getFrom());
				event.setTo(booking.getTo());

				XmlEntity statusEntity = new XmlEntity();
				if (booking.getFunction() != null && savedBooking.getResource() != null) {

					if (savedBooking.getResource() instanceof DefaultResource) {
						resourceCheck.set(false);
						DefaultResource resource = new DefaultResource();
						if (booking.getFunction().getDefaultResource() != null) {
							resource = booking.getFunction().getDefaultResource();
						}
						statusEntity.setKey(EventStatus.REQUESTED.getEventStatus());
						event.setStatus(statusEntity);
						event.setResource(resource);
						booking.setResource(resource);
					} else {
						// means if is not a default resource, so added on the first line only
						if (resourceCheck.get() == false) {
							event.setResource(savedBooking.getResource());
							statusEntity.setKey(EventStatus.CONFIRMED.getEventStatus());
							event.setStatus(statusEntity);
							booking.setResource(savedBooking.getResource());
						} else {
							DefaultResource resource = new DefaultResource();
							if (booking.getFunction().getDefaultResource() != null) {
								resource = booking.getFunction().getDefaultResource();
							}
							statusEntity.setKey(EventStatus.REQUESTED.getEventStatus());
							event.setStatus(statusEntity);
							event.setResource(resource);
							booking.setResource(resource);
						}
						resourceCheck.set(true);
					}
				} else {
					DefaultResource resource = new DefaultResource();
					if (booking.getFunction().getDefaultResource() != null) {
						resource = booking.getFunction().getDefaultResource();
					}
					statusEntity.setKey(EventStatus.REQUESTED.getEventStatus());
					event.setStatus(statusEntity);
					event.setResource(resource);
					booking.setResource(resource);
				}

				event.setTitle(booking.getDescription());

				// booking.setId(null);
				booking.setBookingEvent(event);
				booking.setRecord(project);
				booking.setOccurrenceCount(1f);
				if (savedBooking.getExtras() != null) {
					List<ExtraLine> extraList = new ArrayList<ExtraLine>();
					List<ExtraLine> dbExtraLine = savedBooking.getExtras();
					Booking bookEntity = booking;
					for (int k = 0; k < dbExtraLine.size(); k++) {
						ExtraLine extra = dbExtraLine.get(k);

						ExtraLine lineExtra = new ExtraLine();
						lineExtra.setId(null);
						lineExtra.setLine(bookEntity);
						lineExtra.setLabel(extra.getLabel());
						lineExtra.setExtraTotal(extra.getExtraTotal());
						lineExtra.setExtraTotalPrice(extra.getExtraTotalPrice());

						extraList.add(lineExtra);
					}
					booking.setExtras(extraList);
				}

				booking.setQtySoldPerOc(savedBooking.getQtySoldPerOc());
				booking.setQtyUsedPerOc(savedBooking.getQtyUsedPerOc());
				booking.setDiscountRate(savedBooking.getDiscountRate());
				booking.setVat(savedBooking.getVat());

				if (booking instanceof Line) {
					booking = (Booking) recordService.calculateAndUpdateBudgetLineCosts(booking);
				}

				booking = bookingRepository.save(booking);
				event.setOrigin(booking);
				event = eventRepository.save(event);
				booking.setBookingEvent(event);
				project.addBooking(booking);
				project.updateAmounts();
			} catch (CloneNotSupportedException e) {
				logger.info("Failure in making booking-lines from budget-lines on Record : title : "
						+ project.getTitle() + ", record-id : " + project.getId());
				logger.info("Failure in making booking-lines from budget-lines due to : " + e.getStackTrace());
			}
		}
		logger.info("Returning after adding bookings quantity on individual line for record : title : "
				+ project.getTitle() + ", record-id : " + project.getId());
	}

	/**
	 * Method to save/update event for a particular booking-line
	 * 
	 * @param line   the booking-line
	 * 
	 * @param record the booking-project object
	 * 
	 * @return the updated record object
	 */
	public Record saveOrUpdateEventFromBookingLine(Line line) {
		logger.info("Inside BookingService :: saveOrUpdateEventFromBookingLine(), line : " + line);
		Booking booking = new Booking();
		if (line instanceof Booking) {
			booking = (Booking) line;
		}
		Long bookingId = booking.getId();
		BookingEvent event = booking.getBookingEvent();
		if (event == null) {
			event = new BookingEvent();
		}
		event.setFrom(booking.getFrom());
		event.setTo(booking.getTo());
		event.setTitle(booking.getDescription());

		if (booking.getFunction() != null) {
			if (booking.getResource() == null) {
				DefaultResource resource = new DefaultResource();
				if (booking.getFunction().getDefaultResource() != null) {
					resource = booking.getFunction().getDefaultResource();
				}
				event.setResource(resource);
			} else {
				XmlEntity statusEntity = new XmlEntity();
				if (booking.getResource() instanceof DefaultResource) {
					event.setResource(booking.getResource());
					statusEntity.setKey(EventStatus.REQUESTED.getEventStatus());
					event.setStatus(statusEntity);
				} else {
					statusEntity.setKey(EventStatus.CONFIRMED.getEventStatus());
					event.setStatus(statusEntity);
					Resource<?> resource = this.resourceService.findResource(booking.getResource().getId());
					event.setResource(resource);
				}
			}
		}

		// if (booking.getResource() instanceof DefaultResource) {
		// event.setResource(booking.getResource());
		// XmlEntity statusEntity = new XmlEntity();
		// statusEntity.setKey("Requested");
		// event.setStatus(statusEntity);
		// } else {
		// XmlEntity statusEntity = new XmlEntity();
		// statusEntity.setKey("Confirmed");
		// event.setStatus(statusEntity);
		// event.setResource(booking.getResource());
		// }

		booking.setBookingEvent(event);
		booking = bookingRepository.save(booking);
		event.setOrigin(booking);
		event = eventRepository.save(event);
		booking.setBookingEvent(event);
		Record record = booking.getRecord();
		if (bookingId == null) {
			record.addBooking(booking);
		}
		logger.info("Returning after saving event from booking-line");
		Record updatedRecord = record;
		updatedRecord.setNewlySavedLine(booking.getId());
		return updatedRecord;
	}

	/**
	 * To add booking event on project-booking
	 * 
	 * @param bookingId the bookingId
	 * 
	 * @return BookingEventDTO object
	 */
	// @Transactional
	public ProjectDTO assignBookingEventByProject(ProjectDTO projectDTO) {
		Long quotationId = projectDTO.getId();
		long projectId = quotationId;
		logger.info(
				"Inside BookingService,AddBookingEventByProject():: Adding booking event to booking with project id "
						+ quotationId);

		if (quotationId == null) {
			logger.info("Invalid Budget-Quotation id");
			throw new UnprocessableEntityException("Invalid Budget-Quotation id");
		}
		ProjectDTO bookingProject = recordService.getProjectByQuotationId(quotationId);
		if (bookingProject != null) {
			return bookingProject;
		}
		recordService.findRecordById(Quotation.class, quotationId);
		Record record = recordService.validateRecordExistence(Quotation.class, quotationId);
		if (record == null) {
			logger.info("Invalid Quotation id");
			throw new NotFoundException("Invalid Quotation id");
		}
		if (record instanceof Quotation) {

			Quotation quotation = (Quotation) record;

			XmlEntity statusEntity = quotation.getStatus();
			if (statusEntity != null
					&& (statusEntity.getKey().equalsIgnoreCase(ProjectStatusName.TO_DO.getProjectStatusNameString())
							|| statusEntity.getKey()
									.equalsIgnoreCase(ProjectStatusName.IN_PROGRESS.getProjectStatusNameString()))
					&& (StringUtils.isBlank(quotation.getFinancialStatus())
							|| (!StringUtils.isBlank(quotation.getFinancialStatus())
									&& (quotation.getFinancialStatus().equalsIgnoreCase(
											ProjectFinancialStatusName.REVISED.getProjectFinancialStatusNameString())
											|| quotation.getFinancialStatus()
													.equalsIgnoreCase(ProjectFinancialStatusName.REJECTED
															.getProjectFinancialStatusNameString()))))) {
				logger.info("Cannot book as the budget has not been approved yet with id : " + quotation.getId());
				throw new UnprocessableEntityException("Cannot book as the budget has not been approved yet");

			}

			Project project = this.getCloneOfQuotation(quotation);
			project = recordRepository.save(project);
			if (project.getStatus() != null && project.getStatus().getKey()
					.equals(ProjectStatusName.IN_PROGRESS.getProjectStatusNameString())) {
				statusEntity.setKey(ProjectStatusName.IN_PROGRESS.getProjectStatusNameString());
				record.setStatus(statusEntity);
				recordRepository.save(record);
			}

			List<Booking> bookings = bookingRepository.getBookingsByRecord(quotationId);
			List<Booking> bookingsList = new LinkedList<Booking>();
			if (bookings.size() > 0) {
				for (Booking booking : bookings) {
					int qty = (int) booking.getOccurrenceCount().floatValue();
					qty = qty < 1 ? 1 : qty;
					this.addBookingsQty(project, booking, qty, bookingsList);
				}
			}
			projectId = project.getId();
			project.updateAmounts();
			project.updateTaxes(record.getCalcTotalPrice());
			project = recordRepository.save(project);
			if (project.getProgramId() != null) {
				Program show = (Program) recordService.validateRecordExistence(Program.class, project.getProgramId());
				project.setProgram(show);
			}
			return new ProjectDTO(project);
		}
		return null;
	}

	/**
	 * To get project clone from quotation
	 * 
	 * @param Quotation
	 * 
	 * @return Project
	 */
	public Project getCloneOfQuotation(Quotation quotation) {
		logger.info("Inside BookingService,getCloneOfQuotation()::");

		Project project = new Project();
		project.setId(null);
		project.setStartDate(quotation.getStartDate());
		project.setEndDate(quotation.getEndDate());
		project.setDate(quotation.getDate());
		project.setChannel(quotation.getChannel());
		quotation.setChannel(null);
		project.setProductionUnit(quotation.getProductionUnit());
		quotation.setProductionUnit(null);
		project.setAgency(quotation.getAgency());
		quotation.setAgency(null);
		project.setCategory(quotation.getCategory());
		project.setDirector(quotation.getDirector());
		quotation.setDirector(null);
		project.setInputs(quotation.getInputs());
		quotation.setInputs(null);
		project.setOutputs(quotation.getOutputs());
		quotation.setOutputs(null);
		project.setCompany(quotation.getCompany());
		project.setEntity(quotation.getEntity());
		project.setExchangeRates(quotation.getExchangeRates());
		quotation.setExchangeRates(null);
		project.setFees(quotation.getFees());
		quotation.setFees(null);
		project.setOrigin(quotation.getOrigin());
		if (quotation.getFollower() != null && quotation.getFollower().getId() != null) {
			StaffMember sf = staffMemberService.validateStaffMember(quotation.getFollower().getId());
			if (sf != null) {
				project.setFollower(sf);
			}
		}
		project.setFollower(quotation.getFollower());
		XmlEntity status = new XmlEntity();
		status.setKey(ProjectStatusName.IN_PROGRESS.getProjectStatusNameString());
		project.setStatus(status);
		project.setSource(quotation);
		project.setVatRate(quotation.getVatRates());
		quotation.setVatRate(null);
		project.setContacts(quotation.getContacts());
		quotation.setContacts(null);
		project.setSignPersonHR(quotation.getSignPersonHR());
		quotation.setSignPersonHR(null);
		project.setSignPersonFreelancer(quotation.getSignPersonFreelancer());
		quotation.setSignPersonFreelancer(null);
		project.setPurchaseOrderDate(quotation.getPurchaseOrderDate());
		quotation.setPurchaseOrderDate(null);
		project.setFinancialStatus(null);
		project.setTitle(quotation.getTitle());
		project.setCurrency(quotation.getCurrency());
		project.setLanguage(quotation.getLanguage());
		if (quotation.getProgram() != null) {
			project.setProgram(quotation.getProgram());
			project.setProgramId(quotation.getProgram().getId());
		}
		project.setCity(quotation.getCity());
		project.setCountry(quotation.getCountry());

		Set<Person> recordContacts = quotation.getRecordContacts();
		Set<Person> contacts = null;
		if (recordContacts != null && !recordContacts.isEmpty()) {
			contacts = new HashSet<Person>();
			List<Person> list = recordContacts.stream().collect(Collectors.toList());
			for (int k = 0; k < list.size(); k++) {
				Person extra = list.get(k);
				contacts.add(extra);
			}
			project.setRecordContacts(contacts);
		}

		project.setReferenceInternal(quotation.getReferenceInternal());
		project.setReferenceExternal(quotation.getReferenceExternal());

		if (quotation.getInformation() != null) {
			project.setInformation(copyRecordInformation(quotation.getInformation()));
		}
		if (quotation.getDueDates() != null) {
			project.setDueDates(copyDueDates(quotation.getDueDates()));
			project.getDueDates().stream().forEach(dueDate -> dueDate.setRecord(project));
		}
		project.setChannelFormat(copyChannelDetails(quotation.getChannelFormat()));
		project.setAttachments(copyAttachments(quotation.getAttachments()));

		project.setTheme(quotation.getTheme());

		logger.info("Returning from BookingService,getCloneOfQuotation()::");
		return project;
	}

	/**
	 * 
	 * @param booking
	 * @return Booking
	 */
	public Booking saveOrUpdate(Booking booking) {
		logger.info("Inside BookingService :: saveOrUpdate(), To save/update booking, booking : " + booking);
		return bookingRepository.save(booking);
	}

	/**
	 * To find booking by origin
	 * 
	 * @param bookingId
	 * @return Booking
	 */
	private Booking findBookingByOrigin(long bookingId) {
		logger.info("Inside BookingService,findBookingByOrigin():: finding booking by origin id " + bookingId);
		Booking booking = bookingRepository.findByOrigin(bookingId);
		if (booking == null) {
			logger.info("Invalid booking id ");
			throw new NotFoundException("Invalid booking id ");
		}
		logger.info("Returing from BookingService,findBooking:");
		return booking;
	}

	/**
	 * To get booking by origin
	 * 
	 * @param bookingId
	 * @return BookingDTO
	 */
	public BookingDTO getBookingByOrigin(long bookingId) {
		logger.info("Inside BookingService,getBookingByOrigin():: finding booking by origin id " + bookingId);
		Booking booking = this.findBookingByOrigin(bookingId);
		return new BookingDTO(booking);
	}

	/**
	 * To copy copy channel formats.
	 * 
	 * @param channelFormats the channelFormats
	 * 
	 * @return
	 */
	protected Set<ChannelFormat> copyChannelDetails(Set<ChannelFormat> channelFormats) {
		logger.info("Inside BookingService :: copyChannelDetails(), channelFormats : " + channelFormats);
		Set<ChannelFormat> channelFormatsForProject = channelFormats.stream()
				.map(cf -> new ChannelFormat(cf.getChannel(), cf.getFormat())).collect(Collectors.toSet());
		return channelFormatsForProject;
	}

	/**
	 * To copy copy Attachment.
	 * 
	 * @param channelFormats the channelFormats
	 * 
	 * @return set of Attachment
	 */
	protected Set<Attachment> copyAttachments(Set<Attachment> attachments) {
		Set<Attachment> attachmentsForProject = attachments.stream().map(at -> new Attachment(at))
				.collect(Collectors.toSet());
		return attachmentsForProject;
	}

	/**
	 * To copy recordInformation
	 * 
	 * @param budgetRecordInformation the budgetRecordInformation
	 * 
	 * @return RecordInformation instance
	 */
	protected RecordInformation copyRecordInformation(RecordInformation budgetRecordInformation) {
		RecordInformation recordInformation = new RecordInformation();
		recordInformation.setComment(budgetRecordInformation.getComment());
		recordInformation.setLength(budgetRecordInformation.getLength());
		recordInformation.setVersion(budgetRecordInformation.getVersion());
		return recordInformation;
	}

	/**
	 * To copy due-dates
	 * 
	 * @param duedates the duedates
	 * 
	 * @return
	 */
	protected Set<DueDate> copyDueDates(Set<DueDate> dueDates) {
		Set<DueDate> dueDatesForProject = dueDates.stream().map(at -> new DueDate(at)).collect(Collectors.toSet());
		return dueDatesForProject;
	}

	/**
	 * Method to delete booking-line
	 * 
	 * @param booking the booking
	 */
	public void deleteBookingLine(Booking booking) {
		logger.info("Inside BookingService :: deleteBookingLine(), To delete booking-line");
		if (booking != null && booking.getId() != null) {
			bookingRepository.delete(booking);
		}
		logger.info("Returning after deleting booking-line");
	}

	/**
	 * Get recent booking
	 * 
	 * @param resourceId
	 * 
	 * @return list of recent bookings
	 */
	public List<BookingDTO> getRecentBookings(Long resourceId) {
		logger.info("Inside BookingService :: getRecentBookings() : To get recent booking, resourceId: " + resourceId);
		Resource<?> resource = resourceService.findResource(resourceId);
		if (resource == null) {
			logger.error("Resource not found.");
			throw new NotFoundException("Resource not found.");
		}
		List<BookingDTO> bookings = bookingRepository
				.findRecentBooking(resource, Project.class, new DateTime().toGregorianCalendar()).stream().limit(3)
				.map(ent -> new BookingDTO(ent, Boolean.TRUE)).collect(Collectors.toList());
		logger.info("Returning successfully after finding recent bookings.");
		return bookings;
	}

	/**
	 * To get projects by staff resource
	 * 
	 * @param resource
	 * 
	 * @return list of projects
	 */
	public List<Record> findProjectsByBookingByResources(StaffResource resource) {
		return bookingRepository.findProjectsByBookingByResources(resource);
	}

	/**
	 * To add group on booking.
	 * 
	 * @param bookingId
	 * @param groupId
	 * @return BookingDTO
	 */
	public BookingDTO addGroupOnBooking(long bookingId, long groupId) {
		logger.info("Inside BookingService :: addGroupOnBooking() : To add group groupId:" + groupId
				+ " on booking, bookingId: " + bookingId);
		Booking booking = bookingRepository.findOne(bookingId);
		if (booking == null) {
			logger.error("Invalid booking id.");
			throw new NotFoundException("Invalid booking id.");
		}
		if (!(booking.getFunction() instanceof StaffFunction)) {
			logger.error("Group can be assigned only for personnel bookings.");
			throw new UnprocessableEntityException("Group can be assigned only for personnel bookings.");
		}
		if (booking.getResource() instanceof StaffResource) {
			logger.error("Resource already assigned.You can't add group now.");
			throw new UnprocessableEntityException("Resource already assigned.You can't add group now.");
		}

		Group group = groupService.findById(groupId);
		booking.setGroup(group);
		booking = bookingRepository.save(booking);
		logger.info("Returning from addGroupOnBooking by adding group");
		return new BookingDTO(booking);
	}

	/**
	 * To remove group on booking.
	 * 
	 * @param bookingId
	 * @param groupId
	 * @return BookingDTO
	 */
	public BookingDTO removeGroupFromBooking(long bookingId) {
		logger.info("Inside BookingService :: removeGroupFromBooking() : To remove group:" + " on booking, bookingId: "
				+ bookingId);
		Booking booking = bookingRepository.findOne(bookingId);
		if (booking == null) {
			logger.error("Invalid booking id.");
			throw new NotFoundException("Invalid booking id.");
		}
		booking.setGroup(null);
		booking = bookingRepository.save(booking);
		logger.info("Returning from removeGroupFromBooking.");
		return new BookingDTO(booking);
	}

	/**
	 * To get list of booking assigned to group of logged-in-user.
	 * 
	 * @return BookingDTO
	 */
	public List<BookingDTO> getBookingsByGroupForLoggedInUser() {
		logger.info(
				"Inside BookingService :: getBookingsByGroupForLoggedInUser() : To get all booking by group and by functions.");
		StaffMember staffMember = authenticationService.getAuthenticatedUser();
		Set<Function> functions = staffMember.getResource().getFunctions().stream().map(fun -> fun.getFunction())
				.collect(Collectors.toSet());
		Set<Group> groups = staffMember.getGroups();
		if (groups.size() < 1 || functions.size() < 1) {
			return new ArrayList<>();
		}
		List<BookingDTO> bookingDTOs = bookingRepository
				.findByGroupsAndResouceFunction(groups, functions, Project.class).stream()
				.filter(b -> (b.getResource() instanceof DefaultResource)).map(bookng -> new BookingDTO(bookng))
				.collect(Collectors.toList());
		logger.info("Returning from getBookingsByGroupForLoggedInUser() by getting bookings.");
		return bookingDTOs;
	}

	/**
	 * To get list of booking assigned to functions of logged-in-user
	 * 
	 * @return BookingDTOs
	 */
	private List<BookingDTO> getBookingsByResouceFunctionForLoggedInUser() {
		logger.info(
				"Inside BookingService :: getBookingsByResouceFunctionForLoggedInUser() : To get all booking by functions.");
		StaffMember staffMember = authenticationService.getAuthenticatedUser();
		Set<Function> functions = staffMember.getResource().getFunctions().stream().map(fun -> fun.getFunction())
				.collect(Collectors.toSet());
		List<BookingDTO> bookingDTOs = bookingRepository.findByResouceFunction(functions, Project.class).stream()
				.filter(b -> (b.getResource() instanceof DefaultResource)).map(booking -> new BookingDTO(booking))
				.collect(Collectors.toList());
		logger.info("Returning from getBookingsByResouceFunctionForLoggedInUser() by getting bookings.");
		return bookingDTOs;
	}

	/**
	 * To get list of booking assigned logged-in-user not 100% completed.
	 * 
	 * @return List<BookingDTO>
	 */
	public List<BookingDTO> getBookingsByLoggedInUser() {
		logger.info("Inside BookingService :: getBookingsByLoggedInUser() : To get all booking by loggedInUser.");
		StaffMember staffMember = authenticationService.getAuthenticatedUser();
		List<BookingDTO> bookingDTOs = bookingRepository.findByResources(staffMember.getResource(), Project.class)
				.stream().map(booking -> new BookingDTO(booking)).collect(Collectors.toList());
		logger.info("Returning from getBookingsByLoggedInUser() by getting bookings.");
		return bookingDTOs;
	}

	/**
	 * Method to unassign resource on booking-line
	 * 
	 * @param bookingId
	 * 
	 * @return the booking-line wrapper object
	 */
	public BookingDTO unassignResourceOnBooking(long bookingId) {
		logger.info("Inside BookingService :: unassignResourceOnBooking(" + bookingId
				+ ") : To unassign resource on booking.");
		Booking booking = bookingRepository.findOne(bookingId);
		if (booking == null) {
			logger.error("Invalid booking id.");
			throw new NotFoundException("Invalid booking id.");
		}
		if (booking.getBookingEvent().getCompletion().intValue() == 100) {
			logger.error("Booking already done.Can not be unassigned.");
			throw new UnprocessableEntityException("Booking already done.Can not be unassigned.");
		}
		if (!(booking.getResource() instanceof DefaultResource)) {
			booking.setResource(booking.getFunction().getDefaultResource());
			if (booking.getBookingEvent() != null) {
				booking.getBookingEvent().setResource(booking.getFunction().getDefaultResource());
			}
			booking = bookingRepository.save(booking);
		}
		eventService.updateUserBooking(booking.getBookingEvent(), bookingId);
		logger.info("Returning from  BookingService :: unassignResourceOnBooking().");
		return new BookingDTO(booking);
	}

	public BookingDTO addUserStartOrEndTimeOnBooking(long bookingId, boolean start) {
		logger.info("Inside BookingService :: unassignResourceOnBooking(bookingId:" + bookingId + ",completion:" + start
				+ ") : To unassign resource on booking.");
		Booking booking = bookingRepository.findOne(bookingId);
		if (booking == null) {
			logger.error("Invalid booking id.");
			throw new NotFoundException("Invalid booking id.");
		}
		if (booking.getResource() instanceof StaffResource) {
			if (start) {// to add start time
				if (booking.getUserStartTime() == null) {
					booking.setUserStartTime(new DateTime().withZone(DateTimeZone.forID("Asia/Calcutta")).toDate());
				} else {
					throw new UnprocessableEntityException("Booking already started.");
				}
			} else {// to add end time
				if (booking.getUserStartTime() == null) {
					throw new UnprocessableEntityException("Please start booking first.");
				}
				if (booking.getUserEndTime() == null) {
					booking.setUserEndTime(new DateTime().withZone(DateTimeZone.forID("Asia/Calcutta")).toDate());
					booking.getBookingEvent().setCompletion(new Float(100));
				} else {
					throw new UnprocessableEntityException("Booking already ended.");
				}
			}

			booking = bookingRepository.save(booking);
		}

		logger.info("Returning from  BookingService :: unassignResourceOnBooking().");
		return new BookingDTO(booking);
	}

	/**
	 * To change completion time on booking.
	 * 
	 * @param bookingId
	 * @param completion
	 * @return BookingDTO
	 */
	public BookingDTO changeCompletionTimeOnBooking(long bookingId, int completion) {
		logger.info("Inside BookingService :: changeCompletionTimeOnBooking(bookingId:" + bookingId + ",completion:"
				+ completion + ") : To change completion time on booking.");
		Booking booking = bookingRepository.findOne(bookingId);
		if (booking == null) {
			logger.error("Invalid booking id.");
			throw new NotFoundException("Invalid booking id.");
		}
		if (completion > 100 || completion < 0) {
			logger.error("Invalid completion range.");
			throw new NotFoundException("Invalid completion range.");
		}
		if (booking.getResource() instanceof StaffResource) {

			if (booking.getBookingEvent() != null) {
				booking.getBookingEvent().setCompletion(new Float(completion));
				if (completion == 100) {
					booking.setUserEndTime(new DateTime().withZone(DateTimeZone.forID("Asia/Calcutta")).toDate());
				}
			} else {
				logger.error("No event associated with this booking.");
				throw new UnprocessableEntityException("No event associated with this booking.");
			}
			booking = bookingRepository.save(booking);
		}
		logger.error("Returning after changing completion time on booking");
		return new BookingDTO(booking);
	}

	/**
	 * To add order form on booking.
	 * 
	 * @param bookingId
	 * @param workAndTravelOrder
	 * @return
	 */
	public BookingDTO addOrderOnBookingLine(long bookingId, WorkAndTravelOrder workAndTravelOrder) {
		logger.info("Inside BookingService :: addOrderOnBookingLine(bookingId:" + bookingId + ",workAndTravelOrder:"
				+ workAndTravelOrder + ") : To add workAndTravelOder to a booking line.");
		Booking booking = bookingRepository.findOne(bookingId);
		if (booking == null) {
			logger.error("Invalid booking id.");
			throw new NotFoundException("Invalid booking id.");
		}

		if (!((booking.getFunction() instanceof StaffFunction) || (booking.getFunction() instanceof ProcessFunction))) {
			logger.error("Order can only be created for personnel function and service function.");
			throw new UnprocessableEntityException(
					"Order can only be created for personnel function and service function.");
		}

		WorkAndTravelOrder validatedworkAndTravelOrder = validateWorkAndTravelOrder(workAndTravelOrder);
		validatedworkAndTravelOrder.setProjectId(booking.getRecord().getId());
		if (workAndTravelOrder.getOrderType().equals(OrderType.WORK_ORDER)) {
			booking.setWorkOrder(validatedworkAndTravelOrder);
		}
		if (workAndTravelOrder.getOrderType().equals(OrderType.TRAVEL_ORDER)) {
			booking.setTravelOrder(validatedworkAndTravelOrder);
		}
		if (workAndTravelOrder.getOrderType().equals(OrderType.MEDIA_ORDER)) {
			booking.setMediaOrder(validatedworkAndTravelOrder);
		}
		logger.info("Returning from addOrderOnBookingLine().");
		return new BookingDTO(bookingRepository.save(booking));
	}

	private WorkAndTravelOrder validateWorkAndTravelOrder(WorkAndTravelOrder workAndTravelOrder) {
		logger.info(
				"Inside BookingService :: validateWorkAndTravelOrder(workAndTravelOrder:" + workAndTravelOrder + ")");
		BookingOrderForm bookingOrderForm = null;

		if (workAndTravelOrder == null) {
			logger.error("WorkAndTravelOrder can not be null.");
			throw new UnprocessableEntityException("WorkAndTravelOrder can not be null.");
		}
		// Order can only be changed while the status is 'To Do' or 'In Progress'. But
		// Media Id, Form Type can only be changed in 'To Do' status.
		if (workAndTravelOrder.getId() != null) {
			WorkAndTravelOrder workAndTravelOrderInDb = workAndTravelOrderService.findById(workAndTravelOrder.getId());
			// As can't change this fields after 'To Do' status. And frontend sending
			// null.Binding issue.
			if (workAndTravelOrder.getStatus() > 0) {
				if (workAndTravelOrder.getMediaId() == null)
					workAndTravelOrder.setMediaId(workAndTravelOrderInDb.getMediaId());
				if (workAndTravelOrder.getFormType() == null)
					workAndTravelOrder.setFormType(workAndTravelOrderInDb.getFormType());
			}
			if ((workAndTravelOrder.getStatus() > 1) || (workAndTravelOrder.getStatus() == 1 && ((!StringUtils
					.equalsIgnoreCase(workAndTravelOrder.getMediaId(), workAndTravelOrderInDb.getMediaId())
					|| (!StringUtils.equalsIgnoreCase(workAndTravelOrder.getFormType(),
							workAndTravelOrderInDb.getFormType())))))) {
				logger.error(
						"Order can only be changed while the status is 'To Do' or 'In Progress'. But Media Id, Form Type can only be changed in 'To Do' status.");
				throw new UnprocessableEntityException(
						"Order can only be changed while the status is 'To Do' or 'In Progress'. But Media Id, Form Type can only be changed in 'To Do' status.");
			}
		}
		if (!OrderType.getAllOrderTypes().contains(workAndTravelOrder.getOrderType().toString())) {
			logger.error("Invalid order type.");
			throw new UnprocessableEntityException("Invalid order type.");
		}
		if (StringUtils.isBlank(workAndTravelOrder.getFormType())) {
			logger.error("Please select a BookingOrderForm.");
			throw new UnprocessableEntityException("Please select a BookingOrderForm.");
		} else {
			bookingOrderForm = bookingOrderFormService.findByType(workAndTravelOrder.getFormType());
		}
		if (!bookingOrderForm.getOrderType().equalsIgnoreCase(workAndTravelOrder.getOrderType().toString())) {
			logger.error("Order type mismatch.");
			throw new UnprocessableEntityException("Order type mismatch.");
		}
		if (workAndTravelOrder.getKeywords() == null || workAndTravelOrder.getKeywords().isEmpty()) {
			logger.error("Please provide valid keywords according to selected BookingOrderForm.");
			throw new UnprocessableEntityException(
					"Please provide proper keywords according to selected BookingOrderForm.");
		} else {
			List<String> keys = workAndTravelOrder.getKeywords().stream().map(k -> k.getKeyword())
					.collect(Collectors.toList());
			// All keys should present.
			if (keys.size() != bookingOrderForm.getKeyword().size()) {
				logger.error("Number of keyword in this order are different.");
				throw new UnprocessableEntityException("Number of keyword in this order are different.");
			}
			if (!keys.containsAll(
					bookingOrderForm.getKeyword().stream().map(x -> x.getKey()).collect(Collectors.toList()))) {
				logger.error("Please provide valid keywords according to selected BookingOrderForm.");
				throw new UnprocessableEntityException(
						"Please provide valid keywords according to selected BookingOrderForm.");
			}
		}

		if (!StringUtils.isBlank(workAndTravelOrder.getMediaId())) {
			List<WorkAndTravelOrder> workAndTravelOrderByMediaId = workAndTravelOrderService
					.findByMediaIdIgnoreCase(workAndTravelOrder.getMediaId());
			if (workAndTravelOrder.getId() != null) {
				if ((workAndTravelOrderByMediaId.size() > 0) && workAndTravelOrderByMediaId.get(0).getId()
						.longValue() != workAndTravelOrder.getId().longValue()) {
					logger.error("Media id already exist.");
					throw new UnprocessableEntityException("Media id already exist.");
				}
			} else {
				if ((workAndTravelOrderByMediaId.size() > 0)) {
					logger.error("Media id already exist.");
					throw new UnprocessableEntityException("Media id already exist.");
				}
			}
		}
		// Preventing empty mediaId to be inserted in DB. Because it is unique. And
		// mediaId is not mandatory.
		if (workAndTravelOrder.getMediaId() != null && workAndTravelOrder.getMediaId().isEmpty()) {
			workAndTravelOrder.setMediaId(null);
		}
		if (workAndTravelOrder.getStatus() > 2 || workAndTravelOrder.getStatus() < 0) {
			logger.error("Invalid Status.");
			throw new UnprocessableEntityException("Invalid Status.");
		}
		logger.info("Returning after validating  workAndTravelOrder.");
		return new WorkAndTravelOrder(workAndTravelOrder);

	}

	/**
	 * To get all the booking order forms.
	 * 
	 * @return
	 */
	public Map<String, List<BookingOrderForm>> getBookingOrderFormWithCategory() {
		logger.info("Inside BookingService :: getBookingOrderFormWithCategory() : To get all BookingOrderForms.");
		List<BookingOrderForm> bookingOrderForms = bookingOrderFormService.findAll();
		logger.info("Returning from getBookingOrderFormWithCategory()");
		return bookingOrderForms.stream().collect(Collectors.groupingBy(BookingOrderForm::getOrderType));
	}

	/**
	 * To remove a workAndTravelOrder from Booking line.
	 * 
	 * @param bookingId
	 * @param orderType
	 * @return
	 */
	public BookingDTO removeOrderOnBookingLine(long bookingId, OrderType orderType) {
		logger.info("Inside BookingService :: addOrderOnBookingLine(bookingId:" + bookingId + ",orderType:" + orderType
				+ ") : To remove workAndTravelOder for a booking line.");
		Booking booking = bookingRepository.findOne(bookingId);
		if (booking == null) {
			logger.error("Invalid booking id.");
			throw new NotFoundException("Invalid booking id.");
		}

		if (!OrderType.getAllOrderTypes().contains(orderType.toString())) {
			logger.error("Invalid order type.");
			throw new NotFoundException("Invalid order type.");
		}

		Boolean deleted = false;
		if (booking.getWorkOrder() != null && booking.getWorkOrder().getOrderType().equals(orderType)) {
			if (booking.getWorkOrder().getStatus() > 0) {
				logger.error("Order can only be deleted while the status is 'To Do'.");
				throw new UnprocessableEntityException("Order can only be deleted while the status is 'To Do'.");
			}
			workAndTravelOrderService.remove(booking.getWorkOrder().getId());
			booking.setWorkOrder(null);
			deleted = true;
		}
		if (booking.getMediaOrder() != null && booking.getMediaOrder().getOrderType().equals(orderType)) {
			if (booking.getMediaOrder().getStatus() > 0) {
				logger.error("Order can only be deleted while the status is 'To Do'.");
				throw new UnprocessableEntityException("Order can only be deleted while the status is 'To Do'.");
			}
			workAndTravelOrderService.remove(booking.getMediaOrder().getId());
			booking.setMediaOrder(null);
			deleted = true;
		}
		if (booking.getTravelOrder() != null && booking.getTravelOrder().getOrderType().equals(orderType)) {
			if (booking.getTravelOrder().getStatus() > 0) {
				logger.error("Order can only be deleted while the status is 'To Do'.");
				throw new UnprocessableEntityException("Order can only be deleted while the status is 'To Do'.");
			}
			workAndTravelOrderService.remove(booking.getTravelOrder().getId());
			booking.setTravelOrder(null);
			deleted = true;
		}

		if (!deleted) {
			logger.error("Order type not found for this booking line.");
			throw new NotFoundException("Order type not found for this booking line.");
		}
		logger.info("Returning from removeOrderOnBookingLine().");
		return new BookingDTO(bookingRepository.save(booking));
	}

	/**
	 * To update the status of a WorkAndTravelOrder.
	 * 
	 * @param bookingId
	 * @param orderType
	 * @return
	 */
	public WorkAndTravelOrder updateOrderFormStatusOnBookingLine(long bookingId, OrderType orderType) {
		logger.info("Inside BookingService :: updateOrderFormStatusOnBookingLine(bookingId:" + bookingId + ",orderType:"
				+ orderType + ") : To update workAndTravelOder status for a booking line.");
		Booking booking = bookingRepository.findOne(bookingId);
		if (booking == null) {
			logger.error("Invalid booking id.");
			throw new NotFoundException("Invalid booking id.");
		}

		if (!OrderType.getAllOrderTypes().contains(orderType.toString())) {
			logger.error("Invalid order type.");
			throw new NotFoundException("Invalid order type.");
		}

		WorkAndTravelOrder workAndTravelOrder = null;
		Boolean deleted = false;
		if (booking.getWorkOrder() != null && booking.getWorkOrder().getOrderType().equals(orderType)) {
			workAndTravelOrder = workAndTravelOrderService.findById(booking.getWorkOrder().getId());
			deleted = true;
		}
		if (booking.getMediaOrder() != null && booking.getMediaOrder().getOrderType().equals(orderType)) {
			workAndTravelOrder = workAndTravelOrderService.findById(booking.getMediaOrder().getId());
			deleted = true;
		}
		if (booking.getTravelOrder() != null && booking.getTravelOrder().getOrderType().equals(orderType)) {
			workAndTravelOrder = workAndTravelOrderService.findById(booking.getTravelOrder().getId());
			deleted = true;
		}

		if (!deleted) {
			logger.error("Order type not found for this booking line.");
			throw new NotFoundException("Order type not found for this booking line.");
		}

		// Status values will be 0,1,2. Not more then 2.
		workAndTravelOrder.setStatus(workAndTravelOrder.getStatus() < 2 ? workAndTravelOrder.getStatus() + 1
				: workAndTravelOrder.getStatus());

		logger.info("Returning from updateOrderFormStatusOnBookingLine().");
		return workAndTravelOrderService.save(workAndTravelOrder);
	}

	/**
	 * To get bookings By Function And StartDate And EndDate.
	 * 
	 * @param functionType
	 * @param startDate
	 * @param endDate
	 * @return bookings
	 */
	public List<Booking> findByFunctionAndStartDateAndEndDate(String functionType, Calendar startDate,
			Calendar endDate) {
		logger.info("Inside BookingService :: findByFunctionAndStartDateAndEndDate(functionType:" + functionType
				+ "startDate:" + startDate.getTime() + "endDate:" + endDate.getTime() + ")");
		startDate.set(Calendar.SECOND, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.HOUR, 0);

		endDate.set(Calendar.SECOND, 0);
		endDate.set(Calendar.MINUTE, 0);
		endDate.set(Calendar.HOUR, 0);
		List<Booking> bookings = bookingRepository.findByProjectAndStartDateAndEndDate(startDate, endDate,
				Project.class);
		List<Booking> filteredBooking = new ArrayList<>();
		if (functionType == null) {
			logger.info("Geting Bookings for all functions");
			filteredBooking = bookings;
		} else if (functionType.equals("EquipmentFunction")) {
			logger.info("Geting Bookings for all EquipmentFunction");
			filteredBooking = bookings.stream().filter(b -> b.getFunction() instanceof EquipmentFunction)
					.collect(Collectors.toList());
		} else if (functionType.equals("StaffFunction")) {
			logger.info("Geting Bookings for all StaffFunction");
			filteredBooking = bookings.stream().filter(b -> b.getFunction() instanceof StaffFunction)
					.collect(Collectors.toList());
		}
		logger.info("Returning from findByResourceAndStartDateAndEndDate().");
		return filteredBooking;
	}

	/**
	 * To get bookings By Function And Resource And StartDate And EndDate.
	 * 
	 * @param functionType
	 * @param startDate
	 * @param endDate
	 * 
	 * @return bookings
	 */
	List<Booking> findByFunctionAndResourceAndStartDateAndEndDate(String functionType, Calendar startDate,
			Calendar endDate) {
		logger.info("Inside BookingService :: findByFunctionAndResourceAndStartDateAndEndDate(functionType:"
				+ functionType + "startDate:" + startDate.getTime() + "endDate:" + endDate.getTime() + ")");
		List<Booking> bookings = new ArrayList<>();
		startDate.set(Calendar.SECOND, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.HOUR, 0);

		endDate.set(Calendar.SECOND, 0);
		endDate.set(Calendar.MINUTE, 0);
		endDate.set(Calendar.HOUR, 0);

		bookings = bookingRepository.findByProjectAndStartDateAndEndDate(startDate, endDate, Project.class);
		if (functionType.equals("EquipmentFunction")) {
			logger.info("Geting Bookings for all EquipmentFunction");
			bookings = bookings.stream().filter(b -> b.getResource() instanceof EquipmentResource)
					.collect(Collectors.toList());
		}
		if (functionType.equals("StaffFunction")) {
			logger.info("Geting Bookings for all StaffFunction");
			bookings = bookings.stream().filter(b -> b.getResource() instanceof StaffResource)
					.collect(Collectors.toList());
		}
		logger.info("Returning from findByFunctionAndResourceAndStartDateAndEndDate");
		return bookings;
	}

}
