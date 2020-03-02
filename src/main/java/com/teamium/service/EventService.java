package com.teamium.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamium.domain.Group;
import com.teamium.domain.UserBooking;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.projects.AbstractProject;
import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.projects.BookingEvent;
import com.teamium.domain.prod.projects.Project;
import com.teamium.domain.prod.projects.order.SupplyResource;
import com.teamium.domain.prod.resources.Resource;
import com.teamium.domain.prod.resources.ResourceFunction;
import com.teamium.domain.prod.resources.functions.DefaultResource;
import com.teamium.domain.prod.resources.functions.Function;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.domain.prod.resources.staff.StaffResource;
import com.teamium.dto.BookingDTO;
import com.teamium.dto.BookingEventDTO;
import com.teamium.dto.ProjectDTO;
import com.teamium.dto.RecurrenceDTO;
import com.teamium.dto.RosterEventDTO;
import com.teamium.enums.EventStatus;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.BookingEventRepository;
import com.teamium.repository.BookingRepository;
import com.teamium.service.prod.resources.functions.FunctionService;

@Service
public class EventService {

	private BookingEventRepository bookingEventRepository;
	private ResourceService resourceService;
	private BookingService bookingService;
	private BookingRepository bookingRepository;
	private AuthenticationService authenticationService;
	private AvailabilityService availabilityService;
	private RecordService recordService;
	private FunctionService functionService;
	private UserBookingService userBookingService;
	private GroupService groupService;

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * @param bookingEventRepository
	 * @param resourceService
	 * @param bookingService
	 * @param authenticationService
	 * @param availabilityService
	 * @param recordService
	 * @param functionService
	 */
	@Autowired
	public EventService(BookingEventRepository bookingEventRepository, ResourceService resourceService,
			BookingService bookingService, AuthenticationService authenticationService,
			AvailabilityService availabilityService, RecordService recordService, FunctionService functionService,
			BookingRepository bookingRepository, UserBookingService userBookingService, GroupService groupService) {
		this.bookingEventRepository = bookingEventRepository;
		this.resourceService = resourceService;
		this.bookingService = bookingService;
		this.authenticationService = authenticationService;
		this.availabilityService = availabilityService;
		this.recordService = recordService;
		this.functionService = functionService;
		this.bookingRepository = bookingRepository;
		this.userBookingService = userBookingService;
		this.groupService = groupService;
	}

	/**
	 * Method to save booking event
	 * 
	 * @param booking event the booking event
	 * @return BookingEventDTO
	 * 
	 */
	public List<BookingEventDTO> saveOrUpdateEvents(List<BookingEventDTO> events) {
		logger.info("Inside EventService,saveOrUpdateEvents::event " + events);
//		if (!authenticationService.isAdmin()) {
//			logger.warn("A invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
//					+ " tried to save or update to event.");
//
//			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
//		}

		if (events.size() < 1) {
			logger.info("Empty event list ");
			throw new UnprocessableEntityException("Empty event list. ");
		}
		List<BookingEventDTO> savedEvents = new LinkedList<BookingEventDTO>();
		for (BookingEventDTO event : events) {

			BookingEvent bookingEvent = validateEvent(event);

			bookingEvent = bookingEventRepository.save(bookingEvent);
			if (event.isChangeable()) {
				this.updateEventBooking(bookingEvent, event.getStartTime(), event.getEndTime());
			} else {
				this.updateEventBooking(bookingEvent, null, null);
			}
			if (event.isMoveLinkedEvent()) {
				Set<BookingEvent> linkedEvents = new HashSet<>();
				getAllLinkedBookingEvents(bookingEvent, linkedEvents);

				linkedEvents.forEach(dt -> {
					if (dt.getId() != event.getId()) {
						DateTime start = new DateTime(dt.getFrom());
						DateTime end = new DateTime(dt.getTo());
						int minutes = Minutes.minutesBetween(start, end).getMinutes();
						start = start.plusMinutes(event.getOffSet());
						end = start.plusMinutes(minutes);

						Calendar calendarFrom = Calendar.getInstance();
						calendarFrom.setTimeInMillis(start.getMillis());

						Calendar calendarTo = Calendar.getInstance();
						calendarTo.setTimeInMillis(end.getMillis());

						dt.setFrom(calendarFrom);
						dt.setTo(calendarTo);
						BookingEventDTO beD = new BookingEventDTO(bookingEventRepository.save(dt));
						this.updateEventBooking(dt, beD.getStartTime(), beD.getEndTime());
						savedEvents.add(beD);
					}
				});

			}

			savedEvents.add(new BookingEventDTO(bookingEvent));
		}
		logger.info("Returning from EventService,EventService:: ");
		return savedEvents;
	}

	/**
	 * To assign a resource on bookings.
	 * 
	 * @param bookingEvents
	 * @return
	 */
	public List<BookingEventDTO> assigenBookingEventToLoggedInUser(List<BookingEventDTO> bookingEvents) {
		logger.info("Inside EventService,assigenBookingEventToLoggedInUser::event " + bookingEvents);
		StaffMember loggedInUser = authenticationService.getAuthenticatedUser();
		StaffResource staffResource = loggedInUser.getResource();
		bookingEvents.forEach(event -> event.setResource(staffResource.getId()));
		logger.info("Returning from EventService,assigenBookingEventToLoggedInUser:: ");
		return saveOrUpdateEvents(bookingEvents);
	}

	/**
	 * Method to validate booking event
	 * 
	 * @param booking event the booking event
	 * @return BookingEventDTO
	 * 
	 */
	public BookingEvent validateEvent(BookingEventDTO event) {
		logger.info("Inside EventService,validateEvent::event " + event);
		if (event.getStart() == null) {
			logger.info("Start date is quired . ");
			throw new UnprocessableEntityException("Start date is quired .");
		}
		if (event.getEnd() == null) {
			logger.info("End date is quired . ");
			throw new UnprocessableEntityException("End date is quired .");
		}
		if (event.getEnd().isBefore(event.getStart())) {
			logger.info("End date should be after start date . ");
			throw new UnprocessableEntityException("End date should be after start date . ");
		}
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTimeInMillis(event.getStart().getMillis());
		DateTime newStart = null;
		DateTime newend = null;

		BookingEvent bookingEvent;
		if (event.getId() != null) {
			BookingEvent savedEvent = bookingEventRepository.findOne(event.getId());
			if (savedEvent == null) {
				logger.info("Invalid event Id ");
				throw new UnprocessableEntityException("Invalid event Id .");
			}

			// if event time is not changeable
			if (!event.isChangeable()) {
				logger.info("Event time is not changeable:: updating only dates of event ");
				// DateTime start = new DateTime(savedEvent.getFrom().getTimeInMillis());
				// DateTime end = new DateTime(savedEvent.getTo().getTimeInMillis());
				// Duration duration = new Duration(start, end);
				// newStart = new DateTime(event.getStart().getYear(),
				// event.getStart().getMonthOfYear(),
				// event.getStart().getDayOfMonth(), start.getHourOfDay(),
				// start.getMinuteOfHour());
				// newend = newStart.plus(duration);
				newStart = new DateTime(savedEvent.getFrom());
				newend = new DateTime(savedEvent.getTo());
				logger.info("Event dates have updated");
			} else {
				DateTime start1 = DateTime.parse(event.getStartTime()).withZone(DateTimeZone.forID("UTC"));

				newStart = DateTime.parse(event.getStartTime()).withZone(DateTimeZone.forID("Asia/Calcutta"));
				newend = DateTime.parse(event.getEndTime()).withZone(DateTimeZone.forID("Asia/Calcutta"));
			}

			int minutes = Minutes.minutesBetween(new DateTime(savedEvent.getFrom()), newStart).getMinutes();

			event.setOffSet(minutes);
			event.setStart(newStart);
			event.setEnd(newend);

			bookingEvent = event.getBookingEvent(savedEvent);

		} else {
			DateTime start1 = DateTime.parse(event.getStartTime()).withZone(DateTimeZone.forID("UTC"));

			newStart = DateTime.parse(event.getStartTime()).withZone(authenticationService.getUserTimeZone());
			newend = DateTime.parse(event.getEndTime()).withZone(authenticationService.getUserTimeZone());

			event.setStart(newStart);
			event.setEnd(newend);

			bookingEvent = event.getBookingEvent(new BookingEvent());
		}
		if (event.getBookingId() != null) {
			bookingEvent.setOrigin(bookingService.findBooking(event.getBookingId()));
		}

		Resource<?> resource = resourceService.findResource(event.getResource());
		if (resource == null) {
			logger.error("Invalid Resource");
			throw new UnprocessableEntityException("Invalid Resource");
		}
		if (!(resource instanceof SupplyResource) && bookingEvent.getOrigin() != null) {
			ResourceFunction fun = resource.getFunction(bookingEvent.getOrigin().getFunction());
			if (fun == null) {
				logger.error("Invalid Resource");
				throw new UnprocessableEntityException("Invalid Resource");
			}
		}
		XmlEntity statusEntity = getEventStatus(resource, event.getStatus());
		if (statusEntity.getKey() != null) {
			bookingEvent.setStatus(statusEntity);
		}
		bookingEvent.setResource(resource);
		logger.info("Returning from EventService,validateEvent: ");
		return bookingEvent;

	}

	/**
	 * Method to delete booking event by id
	 * 
	 * @param eventId
	 * 
	 */
	public void deleteEvent(long eventId) {
		logger.info("Inside EventService,deleteEvent::deleting booking with id : " + eventId);
		findEvent(eventId);
		bookingEventRepository.delete(eventId);
		logger.info("Returning from EventService,deleteEvent ");

	}

	/**
	 * Method to get booking event by id
	 * 
	 * @param eventId
	 * @return
	 * @return BookingEventDTO
	 * 
	 */
	public BookingEventDTO getEvent(long eventId) {
		logger.info("Inside EventService,deleteEvent::deleting booking with id : " + eventId);
		BookingEvent bookingEvent = findEvent(eventId);
		logger.info("Returning from EventService,deleteEvent ");
		return new BookingEventDTO(bookingEvent);
	}

	/**
	 * Method to find booking event by id
	 * 
	 * @param eventId
	 * @return BookingEvent
	 * 
	 */
	private BookingEvent findEvent(long eventId) {
		logger.info("Inside EventService,findEvent::finding booking with id : " + eventId);
		BookingEvent event = bookingEventRepository.findOne(eventId);
		if (event == null) {
			logger.info("Invalid event Id . ");
			throw new NotFoundException("Invalid event Id .");
		}
		logger.info("Returning from EventService,findEvent ");
		return event;
	}

	/**
	 * Method to find booking event by projectId
	 * 
	 * @param projectId
	 * @return BookingEvent
	 * 
	 */
	public List<BookingEventDTO> getProjectEvents(long projectId) {
		logger.info("Inside EventService,getProjectEvents::finding booking events associated with project id : "
				+ projectId);
		List<BookingEvent> events = new ArrayList<BookingEvent>();
		events = bookingEventRepository.getByProjectId(projectId);
		return events.stream().map(e -> {
			BookingEventDTO bookingEvent = new BookingEventDTO(e);
			boolean collapse = (bookingEventRepository
					.findCollapseBookingEvents(e.getFrom(), e.getTo(), e.getId(), bookingEvent.getResource())
					.size() > 0);
			bookingEvent.setCollapse(collapse);
			return bookingEvent;
		}).collect(Collectors.toList());
	}

	/**
	 * Method to find booking event by projectId
	 * 
	 * @param projectId
	 * @return BookingEvent
	 * 
	 */
	public List<BookingEventDTO> getProjectEventsBetween(long projectId, Calendar from, Calendar to) {
		logger.info("Inside EventService,getProjectEvents::finding booking events associated with project id : "
				+ projectId);
		List<BookingEvent> events = new ArrayList<BookingEvent>();
		events = bookingEventRepository.getByProjectIdAndBetween(projectId, from, to);
		return events.stream().map(e -> {
			BookingEventDTO bookingEvent = new BookingEventDTO(e);
			boolean collapse = (bookingEventRepository
					.findCollapseBookingEvents(e.getFrom(), e.getTo(), e.getId(), bookingEvent.getResource())
					.size() > 0);
			bookingEvent.setCollapse(collapse);
			return bookingEvent;
		}).collect(Collectors.toList());
	}

	/**
	 * Method to find booking event by projectId
	 * 
	 * @param budgetId
	 * @return BookingEvent
	 * 
	 */
	public List<BookingEventDTO> getBudgetEvents(long budgetId) {
		logger.info(
				"Inside EventService,getBudgetEvents::finding booking events associated with budget id : " + budgetId);
		ProjectDTO project = recordService.getProjectByQuotationId(budgetId);
		if (project == null) {
			return null;
		}
		logger.info("returning from getBudgetEvents");
		return this.getProjectEvents(project.getId());
	}

	/**
	 * Method to find booking event by bookingId
	 * 
	 * @param bookingId
	 * @return BookingEvent
	 * 
	 */
	public List<BookingEventDTO> getBookingEvents(long bookingId) {
		List<BookingEvent> events = new ArrayList<BookingEvent>();
		events = bookingEventRepository.getByBokingId(bookingId);
		return events.stream().map(e -> new BookingEventDTO(e)).collect(Collectors.toList());
	}

	/**
	 * Method to find booking event by functionId
	 * 
	 * @param projectId
	 * @return BookingEvent
	 * 
	 */
	public List<BookingDTO> getBookingByFunction(long functionId) {
		logger.info("Inside getBookingByFunction() :: finding booking related to function : " + functionId);
		List<BookingDTO> bookings = new LinkedList<>();

		resourceService.getResourceByFunction(functionId).forEach(res -> {
			bookings.addAll(bookingService.getWrapperBookingsByResourceIdAndDates(res.getId(), null, null));
		});
		logger.info("Returning after finding bookings by function successfully.");
		return bookings;
	}

	/**
	 * Method to find booking events by function
	 * 
	 * @param functionId the functionId
	 * 
	 * @return the list of BookingEvent object
	 */
	public List<BookingEvent> findBookingEventsByGroup(long groupId) {
		logger.info("Inside findBookingEventsByGroup( " + groupId + ")");
		Group group = groupService.findById(groupId);
		List<BookingEvent> events = new LinkedList<BookingEvent>();

		group.getGroupMembers().forEach(res -> {
			events.addAll(bookingEventRepository.findByResource(res.getResource()));
		});
		logger.info("Successfull return from findBookingEventsByGroup( " + groupId + ")");
		return events;
	}

	/**
	 * Method to find booking events by function
	 * 
	 * @param functionId the functionId
	 * 
	 * @return the list of BookingEvent object
	 */
	public List<BookingEvent> findBookingEvensByFunction(long functionId) {
		logger.info("Inside getBookingEvensByFunction() :: finding booking event related to function : " + functionId);
		List<BookingEvent> events = new LinkedList<BookingEvent>();

		resourceService.getResourceByFunction(functionId).forEach(res -> {
			events.addAll(bookingEventRepository.findByResource(res));
		});
		return events;
	}

	/**
	 * Method to find booking events by function
	 * 
	 * @param functionId the functionId
	 * 
	 * @return the list of BookingEventDTO object
	 */
	public List<BookingEventDTO> getBookingEvensByFunction(long functionId) {
		return this.findBookingEvensByFunction(functionId).stream().map(e -> {
			BookingEventDTO bookingEvent = new BookingEventDTO(e);
			boolean collapse = (bookingEventRepository
					.findCollapseBookingEvents(e.getFrom(), e.getTo(), e.getId(), bookingEvent.getResource())
					.size() > 0);

			bookingEvent.setCollapse(collapse);
			return bookingEvent;
		}).collect(Collectors.toList());
	}

	/**
	 * Method to find booking events by Group
	 * 
	 * @param functionId the functionId
	 * 
	 * @return the list of BookingEventDTO object
	 */
	public List<BookingEventDTO> getBookingEventsByGroup(long groupId) {
		logger.info("Inside getBookingEventsByGroup( " + groupId + " )");
		return this.findBookingEventsByGroup(groupId).stream().map(e -> {
			BookingEventDTO bookingEvent = new BookingEventDTO(e);
			boolean collapse = (bookingEventRepository
					.findCollapseBookingEvents(e.getFrom(), e.getTo(), e.getId(), bookingEvent.getResource())
					.size() > 0);
			bookingEvent.setCollapse(collapse);
			return bookingEvent;
		}).collect(Collectors.toList());
	}

	/**
	 * Method to find all booking events
	 * 
	 * @return the list of BookingEvent object
	 */
	public List<BookingEventDTO> getAllEvents() {
		logger.info("Inside getAllEvents() :: fetching all booking event : ");
		List<BookingEventDTO> events = bookingEventRepository.findAll().stream().map(e -> new BookingEventDTO(e))
				.collect(Collectors.toList());
		logger.info("Inside getAllEvents() :: found  :" + events + " events");
		logger.info("Returning from getAllEvents() :: ");
		return events;

	}

	/**
	 * Get set of Linked Booking event object.
	 * 
	 * @param bookingEvent
	 * @param linkedEvents
	 * @return Set of BookingEvent object.
	 */
	private Set<BookingEvent> getAllLinkedBookingEvents(BookingEvent bookingEvent, Set<BookingEvent> linkedEvents) {
		logger.info("Inside getAllLinkedBookingEvents() :: get all Linked Booking Event : " + bookingEvent);

		Set<BookingEvent> linkEvents = bookingEventRepository.getAllLinkedEventsByBokingId(bookingEvent.getId());
		linkEvents.addAll(bookingEvent.getLinkedEvents());

		linkedEvents.add(bookingEvent);
		List<Long> ls = linkedEvents.stream().map(dt -> dt.getId()).collect(Collectors.toList());

		linkEvents = linkEvents.stream().filter(dt -> !ls.contains(dt.getId())).collect(Collectors.toSet());
		if (linkEvents.isEmpty()) {
			return linkedEvents;
		}

		for (BookingEvent ev : linkEvents) {
			getAllLinkedBookingEvents(ev, linkedEvents);
		}
		linkedEvents.add(bookingEvent);
		return linkedEvents;
	}

	/**
	 * Get set of Linked Event with the Selected Eventid.
	 * 
	 * @param eventId
	 * @return set of BookingEventDTO object.
	 */
	public Set<BookingEventDTO> getAllLinkinedBookingEvents(Long eventId) {
		logger.info("Inside getAllLinkinedBookingEvents() :: get all Linked Booking Event : " + eventId);

		if (eventId == null) {
			logger.info("Booking event id can not null.");
			throw new UnprocessableEntityException("Booking event id can not null.");
		}
		BookingEvent bookingEvent = bookingEventRepository.findOne(eventId);
		if (bookingEvent == null) {
			logger.info("please enter a valid booking event id : " + eventId);
			throw new UnprocessableEntityException("please enter a valid booking event id : " + eventId);
		}
		return this.getAllLinkedBookingEvents(bookingEvent, new HashSet<>()).parallelStream().map(BookingEventDTO::new)
				.collect(Collectors.toSet());
	}

	/**
	 * Save or update Event Link.
	 * 
	 * @param event
	 * @return object of BookingEventDTO.
	 */
	public BookingEventDTO linkOrUnlinkBookingEvents(BookingEventDTO event) {
		logger.info("Inside linkOrUnlinkBookingEvents() :: save or update Event Link : " + event);

		if (event.getId() == null) {
			logger.info("Booking event id can not null.");
			throw new UnprocessableEntityException("Booking event id can not null.");
		}
		BookingEvent bookingEvent = bookingEventRepository.findOne(event.getId());
		if (bookingEvent == null) {
			logger.info("please enter a valid booking event id : " + event.getId());
			throw new UnprocessableEntityException("please enter a valid booking event id : " + event.getId());
		}
		List<Long> linkedBookingEventIds = bookingEvent.getLinkedEvents().parallelStream().map(dt -> dt.getId())
				.collect(Collectors.toList());
		List<Long> linkedEventIds = this.getAllLinkedBookingEvents(bookingEvent, new HashSet<>()).parallelStream()
				.map(dt -> dt.getId()).collect(Collectors.toList());

		Set<BookingEvent> linkedEvents = new HashSet<BookingEvent>();
		event.getLinkedEvents().forEach(dt -> {
			if (dt.getId() == null) {
				logger.info("Link event Id can not null");
				throw new UnprocessableEntityException("Link event Id can not null");
			}
			if (linkedEventIds.contains(dt.getId()) && !linkedBookingEventIds.contains(dt.getId())) {
				logger.info("Circular Dipendency due to Event Id : " + dt.getId());
				throw new UnprocessableEntityException("Circular Dipendency.");
			}
			BookingEvent linkEvent = bookingEventRepository.findOne(dt.getId());
			if (linkEvent == null) {
				logger.info("Please enter a valid link event Id : " + dt.getId());
				throw new UnprocessableEntityException("Please enter a valid link event Id : " + dt.getId());
			}
			linkedEvents.add(linkEvent);
		});
		bookingEvent.setLinkedEvents(linkedEvents);
		return new BookingEventDTO(bookingEventRepository.save(bookingEvent));
	}

	/**
	 * Method to find all collapse booking events with an event
	 * 
	 * @return EventDTO
	 * 
	 * @return the list of BookingEvent object
	 */
	public List<BookingEventDTO> findCollapseBookingEvents(long eventId) {
		logger.info("Inside EventService,findCollapseBookingEvents() :: ");
		BookingEvent event = this.findEvent(eventId);
		List<BookingEvent> events = bookingEventRepository.findCollapseBookingEvents(event.getFrom(), event.getTo(),
				eventId, event.getResource().getId());
		logger.info("Returning from EventService,findCollapseBookingEvents() :: ");
		return events.stream().map(e -> new BookingEventDTO(e)).collect(Collectors.toList());
	}

	/**
	 * Method to Copy Booking event
	 * 
	 * @param BookingEventDTO
	 * 
	 * @return BookingEventDTO
	 */
	public BookingEventDTO copyEvent(BookingEventDTO eventDTO) {
		logger.info("Inside EventService,copyEvent() :: ");
		long bookingId = eventDTO.getBookingId();
		try {
			Booking booking = bookingService.findBooking(bookingId).clone();
			booking.setId(null);
			booking.setBookingEvent(null);
			if (eventDTO.getFunctionId() != null) {
				booking.setFunction(functionService.getRatedFunctionById(eventDTO.getFunctionId()));
			}
			Resource<?> resource = resourceService.findResource(eventDTO.getResource());
			if (resource == null) {
				logger.error("Invalid Resource");
				throw new UnprocessableEntityException("Invalid Resource");
			}
			booking.setResource(resource);
			booking.setOccurrenceCount(1f);
			booking = bookingService.saveOrUpdate(booking);
			eventDTO.setBookingId(booking.getId());

			BookingEvent be = null;
			if (eventDTO.getCloneEventId() == null) {
				logger.info("Invalid event Id ");
				throw new UnprocessableEntityException("Invalid event Id .");

			} else {
				be = bookingEventRepository.findOne(eventDTO.getCloneEventId());
				if (be == null) {
					logger.info("Invalid event Id ");
					throw new UnprocessableEntityException("Invalid event Id .");
				}
			}

			DateTime start = new DateTime(be.getFrom().getTimeInMillis());
			DateTime end = new DateTime(be.getTo().getTimeInMillis());
			Duration duration = new Duration(start, end);
			DateTime newStart = new DateTime(eventDTO.getStart().getYear(), eventDTO.getStart().getMonthOfYear(),
					eventDTO.getStart().getDayOfMonth(), start.getHourOfDay(), start.getMinuteOfHour());
			DateTime newend = newStart.plus(duration);

			BookingEvent event = validateEvent(eventDTO);
//			event.setOrigin(booking);

			Calendar calendarFrom = Calendar.getInstance();
			calendarFrom.setTimeInMillis(newStart.getMillis());
			Calendar calendarEnd = Calendar.getInstance();
			calendarEnd.setTimeInMillis(newend.getMillis());
			event.setFrom(calendarFrom);
			event.setTo(calendarEnd);

			event = bookingEventRepository.save(event);
			BookingEventDTO bookingEventDTO = new BookingEventDTO(event);
			System.out.println("event => " + event);
			this.updateEventBooking(event, bookingEventDTO.getStartTime(), bookingEventDTO.getEndTime());
			logger.info("Returning from  EventService,copyEvent() :: ");
			return bookingEventDTO;
		} catch (CloneNotSupportedException e) {
			logger.error("Error :" + e.getMessage());
			throw new UnprocessableEntityException("Error :" + e.getMessage());
		}
	}

	/**
	 * Method to add Booking events
	 * 
	 * @param BookingEventDTO
	 * 
	 * @return BookingEventDTO
	 */
	public BookingEventDTO addBookingsEvents(BookingEventDTO event) {
		logger.info("Inside EventService,addBookingsEvents() :: ");
		List<BookingEventDTO> linkedEvents = event.getLinkedEvents();
		long projectId = event.getProjectId();
		event.setLinkedEvents(null);
		BookingEvent validatedEvent = this.validateEvent(event);
		Project project = (Project) recordService.validateRecordExistence(Project.class, projectId);
		if (project != null) {
			Booking currerntBooking = validatedEvent.getOrigin();
			if (currerntBooking != null && !currerntBooking.getRecord().getId().equals(project.getId())) {
				currerntBooking.setRecord(project);
				bookingService.saveOrUpdate(currerntBooking);
			}
		}

		Set<BookingEvent> savedLinkedEvent = validatedEvent.getLinkedEvents() != null ? validatedEvent.getLinkedEvents()
				: new LinkedHashSet<BookingEvent>();

		if (event.getId() == null) {
			logger.info("adding new booking");
			Booking booking = new Booking();
			booking.setFrom(validatedEvent.getFrom());
			booking.setTo(validatedEvent.getTo());
			booking.setRecord(project);
			booking.setFunction(functionService.getRatedFunctionById(event.getFunctionId()));

			Line lineInstance = recordService.applyRate(booking, project);
			if (lineInstance instanceof Booking) {
				booking = (Booking) lineInstance;
			}

			booking = bookingService.saveOrUpdate(booking);
			validatedEvent.setOrigin(booking);
			logger.info(" new Booking booking added");
		} else {
			if (event.isMoveLinkedEvent()) {
				Set<BookingEvent> linkedEventsValue = new HashSet<>();
				getAllLinkedBookingEvents(validatedEvent, linkedEventsValue);

				linkedEventsValue.forEach(dt -> {
					if (dt.getId() != event.getId()) {
						DateTime start = new DateTime(dt.getFrom());
						DateTime end = new DateTime(dt.getTo());
						int minutes = Minutes.minutesBetween(start, end).getMinutes();
						start = start.plusMinutes(event.getOffSet());
						end = start.plusMinutes(minutes);

						Calendar calendarFrom = Calendar.getInstance();
						calendarFrom.setTimeInMillis(start.getMillis());

						Calendar calendarTo = Calendar.getInstance();
						calendarTo.setTimeInMillis(end.getMillis());

						dt.setFrom(calendarFrom);
						dt.setTo(calendarTo);

						bookingEventRepository.save(dt);
					}
				});

			}
		}

		for (BookingEventDTO linkedEvent : linkedEvents) {
			try {
				BookingEvent clonedEvent = validatedEvent.clone();
				clonedEvent.setId(null);
				clonedEvent.setLinkedEvents(null);
				Resource<?> resource = resourceService.findResource(linkedEvent.getResource());
				XmlEntity statusEntity = getEventStatus(resource, event.getStatus());
				if (statusEntity.getKey() != null) {
					clonedEvent.setStatus(statusEntity);
				}
				clonedEvent.setResource(resource);
				Booking otherBooking = new Booking();
				otherBooking.setFrom(validatedEvent.getFrom());
				otherBooking.setTo(validatedEvent.getTo());
				otherBooking.setRecord(project);
				otherBooking.setFunction(functionService.getRatedFunctionById(linkedEvent.getFunctionId()));
				otherBooking.setBookingEvent(clonedEvent);

				Line lineInstance = recordService.applyRate(otherBooking, project);
				if (lineInstance instanceof Booking) {
					otherBooking = (Booking) lineInstance;
				}

				otherBooking = bookingService.saveOrUpdate(otherBooking);
				clonedEvent.setOrigin(otherBooking);
				clonedEvent = bookingEventRepository.save(clonedEvent);

				BookingEventDTO bd = new BookingEventDTO(clonedEvent);
				this.updateEventBooking(clonedEvent, bd.getStartTime(), bd.getEndTime());

				savedLinkedEvent.add(clonedEvent);
			} catch (CloneNotSupportedException e) {

			}
		}
		validatedEvent.setLinkedEvents(savedLinkedEvent);
		validatedEvent = bookingEventRepository.save(validatedEvent);
		BookingEventDTO bd = new BookingEventDTO(validatedEvent);
		logger.info("Returnig from EventService,addBookingsEvents() :: ");

		this.updateEventBooking(validatedEvent, bd.getStartTime(), bd.getEndTime());
		return bd;
	}

	/**
	 * to split booking event
	 * 
	 * @param list of booking events
	 * @return list of booking events
	 */
	public List<BookingEventDTO> splitBooking(List<BookingEventDTO> bookingEvents) {
		logger.info("Inside EventService,splitBooking() :: ");

		Optional<BookingEventDTO> opBookingEvent = bookingEvents.stream().filter(e -> e.getId() != null).findFirst();
		if (opBookingEvent.isPresent()) {
			List<BookingEventDTO> updateList = new ArrayList<BookingEventDTO>();
			BookingEventDTO saveBookingEvent = opBookingEvent.get();
			updateList.add(saveBookingEvent);
			for (BookingEventDTO currentEvent : bookingEvents) {
				if (currentEvent.getId() == null) {
					try {
						Booking booking = bookingService.findBooking(currentEvent.getBookingId());
						booking = booking.cloneWhitoutEvent();
						booking.setId(null);
						booking = bookingService.saveOrUpdate(booking);
						currentEvent.setBookingId(booking.getId());
						updateList.add(currentEvent);
					} catch (CloneNotSupportedException e1) {
						e1.printStackTrace();
					}
				}
			}
			logger.info("Inside EventService,findCollapseBookingEvents() :: ");

			return this.saveOrUpdateEvents(updateList);
		}
		logger.info("Returning from EventService,splitBooking() :: ");

		return null;
	}

	/**
	 * Get set of linked booking event ID.
	 * 
	 * @return set of linked booking event ID.
	 */
	public Set<Long> getAllLinkedEventsId() {
		logger.info("Inside EventService,getAllLinkedEventsId() :: ");

		List<BookingEvent> bookingEvents = this.bookingEventRepository.findAll();

		Set<Long> bookingEventId = bookingEvents.stream()
				.flatMap(el -> el.getLinkedEvents().stream().map(linkedEvent -> linkedEvent.getId()))
				.collect(Collectors.toSet());
		bookingEvents.stream().filter(dt -> !dt.getLinkedEvents().isEmpty())
				.forEach(dt -> bookingEventId.add(dt.getId()));
		logger.info("successfull return from getAllLinkedEventsId.");
		return bookingEventId;
	}

	/**
	 * to update event booking
	 * 
	 * @param booking events
	 */
	private void updateEventBooking(BookingEvent event, String start, String end) {
		logger.info("Inside EventService,updateEventBooking() :: ");

		Booking booking = event.getOrigin();
		if (booking == null || booking.getRecord() == null) {
			return;
		}

		booking.setOccurrenceCount(1f);
		booking.setDisabled(Boolean.FALSE);
		BookingDTO bookingDTO = new BookingDTO(booking);
		if (start != null)
			bookingDTO.setStart(start);
		if (end != null)
			bookingDTO.setEnd(end);
		// recordService.calculateAndUpdateBudgetLineCosts(booking);
		// long projectId = booking.getRecord().getId();
		// bookingService.saveOrUpdate(booking);
		Line bookingLine = recordService.validateLineCalculation(booking.getRecord(), bookingDTO, Project.class);
		if (bookingLine instanceof Booking) {
			Booking instance = (Booking) bookingLine;
			bookingService.saveOrUpdate(instance);

			// update start-time and end-time of record
			Record record = recordService.validateRecordExistence(Project.class, instance.getRecord().getId());
			record = recordService.updateStartAndEndTime(record);
			recordService.updateAndPersistRecord(record);
		}
		if (booking.getResource() instanceof StaffResource) {
			recordService.createContract(booking.getRecord().getId(), booking.getId());

		}
		this.updateUserBooking(event, booking.getId());
		logger.info("Returning from EventService,updateEventBooking() :: ");

	}

	/**
	 * Create or Update UserBooking.
	 * 
	 * @param event
	 * @param bookingId
	 */
	public void updateUserBooking(BookingEvent event, Long bookingId) {
		logger.info("Inside updateUserBooking( " + event + " , " + bookingId + " )");
		UserBooking userBooking = userBookingService.findUserBookingByBookingId(bookingId);
		if (event.getResource() instanceof StaffResource) {
			if (userBooking == null) {
				userBooking = new UserBooking();
				if (event.getTheme() != null) {
					userBooking.setTheme(event.getTheme().getKey());
				}
			}
			userBooking.setName(event.getTitle());
			userBooking.setBookingId(bookingId);
			userBooking.setStaffMember((StaffMember) event.getResource().getEntity());
			userBooking.setStartTime(event.getFrom().getTime());
			userBooking.setEndTime(event.getTo().getTime());
			userBookingService.saveUserBooking(userBooking);
		} else if (userBooking != null) {
			userBooking.setStaffMember(null);
			userBookingService.saveUserBooking(userBooking);
		}
		logger.info("Returning from EventService,updateUserBooking()");
	}

	/**
	 * Get Event status.
	 * 
	 * @param resource
	 * @param status
	 * @return XmlEntity for event status.
	 */
	public XmlEntity getEventStatus(Resource<?> resource, String status) {
		XmlEntity statusEntity = new XmlEntity();

		if (resource instanceof DefaultResource) {
			statusEntity.setKey(EventStatus.REQUESTED.getEventStatus());
		} else {
			if (!StringUtils.isBlank(status)) {
				if (EventStatus.REQUESTED.getEventStatus().equals(EventStatus.getEnum(status).getEventStatus())) {
					statusEntity.setKey(EventStatus.CONFIRMED.getEventStatus());
				} else {
					statusEntity.setKey(EventStatus.getEnum(status).getEventStatus());
				}
			} else {
				statusEntity.setKey(EventStatus.CONFIRMED.getEventStatus());
			}
		}

		return statusEntity;
	}

	/**
	 * Get list of RosterEventDTO for all project.
	 * 
	 * @return List of RosterEventDTO.
	 */
	public List<RosterEventDTO> getBookingEventsForAllProject() {
		logger.info("Inside getBookingEventsForAllProject() :: fetching all booking event : ");
		List<RosterEventDTO> events = bookingEventRepository.findAll().stream().filter(dt -> dt.getOrigin() != null)
				.collect(Collectors.groupingBy(dt -> {
					Booking origin = dt.getOrigin();
					Function rootFunction = origin.getFunction().getRootFunction();
					// return ((AbstractProject) origin.getRecord()).getId() + "_"
					// + ((AbstractProject) origin.getRecord()).getTitle() + "_" +
					// rootFunction.getId() + "_"
					// + rootFunction.getValue();
					return ((AbstractProject) origin.getRecord()).getId() + "_" + rootFunction.getId();
				})).entrySet().stream().filter(dt -> !dt.getValue().isEmpty()).map(dt -> {
					RosterEventDTO rosterEventDTO = new RosterEventDTO();
					String title = "";
					Date bookingStartDate = dt.getValue().stream()
							.sorted((b1, b2) -> b1.getFrom().getTime().before(b2.getFrom().getTime()) ? -1 : 1)
							.map(b -> b.getFrom().getTime()).findFirst().orElse(null);
					Date bookingEndDate = dt.getValue().stream()
							.sorted((b1, b2) -> b1.getTo().getTime().after(b2.getTo().getTime()) ? -1 : 1)
							.map(b -> b.getTo().getTime()).findFirst().orElse(null);

					BookingEvent bookingEvent = dt.getValue().get(0);
					Booking origin = bookingEvent.getOrigin();

					if (origin != null) {
						if (origin.getFunction() != null) {
							Function rootFunction = origin.getFunction().getRootFunction();
							rosterEventDTO.setFunctionId(origin.getFunction().getId());
							title = rootFunction.getValue() + " "
									+ (origin.getRecord() != null ? ((AbstractProject) origin.getRecord()).getTitle()
											: "");
							if (rootFunction.getTheme() != null) {
								rosterEventDTO.setColor(rootFunction.getTheme().getKey());
							}
						}
					}
					rosterEventDTO.setTitle(title.replace("'", " "));
					rosterEventDTO.setStartTime(new DateTime(bookingStartDate).withZone(DateTimeZone.UTC).toString());
					rosterEventDTO.setEndTime(bookingEndDate == null ? rosterEventDTO.getStartTime()
							: new DateTime(bookingEndDate).withZone(DateTimeZone.UTC).toString());

					return rosterEventDTO;
				}).collect(Collectors.toList());
		return events;
	}

	/**
	 * To create Recurrence of the booking event.
	 * 
	 * @param recurrenceDTO
	 * @return list of BookingEventDTO.
	 */
	public List<BookingEventDTO> createRecurrenceEvent(RecurrenceDTO recurrenceDTO) {
		logger.info("Inside createRecurrenceEvent( " + recurrenceDTO + " ).");
		if (recurrenceDTO.getStartDate() == null) {
			logger.info("Start date is quired . ");
			throw new UnprocessableEntityException("Start date is quired .");
		}
		if (recurrenceDTO.getEndDate() == null) {
			logger.info("End date is quired . ");
			throw new UnprocessableEntityException("End date is quired .");
		}
		LocalDate startDate = DateTime.parse(recurrenceDTO.getStartDate()).withZone(DateTimeZone.forID("Asia/Calcutta"))
				.toLocalDate();

		LocalDate endDate = DateTime.parse(recurrenceDTO.getEndDate()).withZone(DateTimeZone.forID("Asia/Calcutta"))
				.toLocalDate();
		if (endDate.isBefore(startDate)) {
			logger.info("End date should be after start date . ");
			throw new UnprocessableEntityException("End date should be after start date . ");
		}

		List<BookingEventDTO> bookingEventDTOs = new ArrayList<>();
		BookingEvent bookingEvent = findEvent(recurrenceDTO.getCloneEventId());
		DateTime start = new DateTime(bookingEvent.getFrom().getTimeInMillis());
		DateTime end = new DateTime(bookingEvent.getTo().getTimeInMillis());
		Duration duration = new Duration(start, end);

		recurrenceDTO.getDays().stream().map(dt -> {
			switch (dt.toUpperCase()) {
			case "MONDAY":
				return DateTimeConstants.MONDAY;
			case "TUESDAY":
				return DateTimeConstants.TUESDAY;
			case "WEDNESDAY":
				return DateTimeConstants.WEDNESDAY;
			case "THURSDAY":
				return DateTimeConstants.THURSDAY;
			case "FRIDAY":
				return DateTimeConstants.FRIDAY;
			case "SATURDAY":
				return DateTimeConstants.SATURDAY;
			case "SUNDAY":
				return DateTimeConstants.SUNDAY;
			}
			return -1;
		}).collect(Collectors.toList()).forEach(dt -> {
			// System.out.println("======== " + dt + " ==========");
			LocalDate stDate = new LocalDate(startDate);
			LocalDate thisMonday = stDate.withDayOfWeek(dt);

			if (stDate.isAfter(thisMonday)) {
				stDate = thisMonday.plusWeeks(1); // start on next day
			} else {
				stDate = thisMonday; // start on this day
			}
			Booking booking = null;
			System.out.println("stDate => " + stDate + " endDate => " + endDate + " stDate.isBefore(endDate) => "
					+ stDate.isBefore(endDate) + " stDate.isEqual(endDate) => " + stDate.isEqual(endDate));
			while (stDate.isBefore(endDate) || stDate.isEqual(endDate)) {
				try {
					System.out.println("Inside");
					booking = booking == null ? bookingEvent.getOrigin().clone() : booking.clone();
					booking.setId(null);
					booking.setBookingEvent(null);
					booking.setOccurrenceCount(1f);

					DateTime newStart = new DateTime(stDate.getYear(), stDate.getMonthOfYear(), stDate.getDayOfMonth(),
							start.getHourOfDay(), start.getMinuteOfHour());
					DateTime newend = newStart.plus(duration);
					Calendar calendarFrom = Calendar.getInstance();
					calendarFrom.setTimeInMillis(newStart.getMillis());
					Calendar calendarEnd = Calendar.getInstance();
					calendarEnd.setTimeInMillis(newend.getMillis());

					booking.setFrom(calendarFrom);
					booking.setTo(calendarEnd);
					booking = bookingService.saveOrUpdate(booking);

					BookingEvent bookingEvents = bookingEvent.clone();
					bookingEvents.setOrigin(booking);
					bookingEvents.setLinkedEvents(bookingEvent.getLinkedEvents());
					bookingEvents.setResource(bookingEvent.getResource());
					bookingEvents.setTheme(bookingEvent.getTheme());
					bookingEvents.setStatus(bookingEvent.getStatus());

					bookingEvents.setFrom(calendarFrom);
					bookingEvents.setTo(calendarEnd);

					bookingEvents = bookingEventRepository.save(bookingEvents);
					bookingEventDTOs.add(new BookingEventDTO(bookingEvents));
					this.updateUserBooking(bookingEvents, booking.getId());

					Record record = recordService.validateRecordExistence(Project.class, booking.getRecord().getId());
					record = recordService.updateStartAndEndTime(record);
					recordService.updateAndPersistRecord(record);

				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				stDate = stDate.plusWeeks(recurrenceDTO.getPlusWeeks());
			}
		});
		logger.info("Successfully return from createRecurrenceEvent() ::");
		return bookingEventDTOs;
	}

	/**
	 * Method to get booking-events within specified date-range
	 * 
	 * @param from
	 * @param to
	 * 
	 * @return List of BookingEvents
	 */
	public List<BookingEvent> getBookingEventsWithinDateRange(Calendar from, Calendar to) {
		logger.info("Inside EventService :: getBookingEventsWithinDateRange(from : " + from + ", to : " + to
				+ ") , To find booking-events within date-range");
		return bookingEventRepository.getEventsBetweenDateRange(from, to);
	}

}
