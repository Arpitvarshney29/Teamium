package com.teamium.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teamium.dto.BookingEventDTO;
import com.teamium.dto.RecurrenceDTO;
import com.teamium.dto.RosterEventDTO;
import com.teamium.service.EventService;

@RestController
@RequestMapping("/event")
public class EventController {

	EventService eventService;

	/**
	 * @param eventService
	 */
	public EventController(EventService eventService) {
		this.eventService = eventService;
	}

	/**
	 * To get list of booking events by function
	 * 
	 * Service URL: /event/by-function/{functionId} method: GET.
	 * 
	 * @return the list of BookingDTO
	 */
	@GetMapping("/by-function/{functionId}")
	List<BookingEventDTO> getBookingByFunction(@PathVariable long functionId) {
		return eventService.getBookingEvensByFunction(functionId);
	}

	/**
	 * To get list of booking events by Group
	 * 
	 * Service URL: /event/by-group/{groupId} method: GET.
	 * 
	 * @return the list of BookingDTO
	 */
	@GetMapping("/by-group/{groupId}")
	List<BookingEventDTO> getBookingByGroup(@PathVariable long groupId) {
		return eventService.getBookingEventsByGroup(groupId);
	}

	/**
	 * To save list booking events
	 * 
	 * Service URL: /event/ method: POST.
	 * 
	 * @param list
	 *            of events
	 * 
	 * @return the list of BookingDTO
	 */
	@PostMapping
	List<BookingEventDTO> saveOrUpdateEvent(@RequestBody List<BookingEventDTO> events) {
		return eventService.saveOrUpdateEvents(events);
	}

	/**
	 * To get list of booking events by project
	 * 
	 * Service URL: /event/by-project/{projectId} method: GET.
	 * 
	 * @return the list of BookingDTO
	 */
	@GetMapping("/by-project/{projectId}")
	List<BookingEventDTO> getBookingEventsByProject(@PathVariable long projectId) {
		return eventService.getProjectEvents(projectId);
	}

	/**
	 * To get list of booking events by project
	 * 
	 * Service URL: /event/by-project-between/{projectId} method: GET.
	 * 
	 * @return the list of BookingDTO
	 */
	@GetMapping("/by-project-between/{projectId}")
	List<BookingEventDTO> getBookingEventsByProject(@PathVariable long projectId, @RequestParam Calendar from,
			@RequestParam Calendar to) {
		return eventService.getProjectEventsBetween(projectId, from, to);
	}

	/**
	 * To get list of booking events by budget
	 * 
	 * Service URL: /event/by-budget/{projectId} method: GET.
	 * 
	 * @return the list of BookingDTO
	 */
	@GetMapping("/by-budget/{budgetId}")
	List<BookingEventDTO> getBookingEventsByBudget(@PathVariable long budgetId) {
		return eventService.getBudgetEvents(budgetId);
	}

	/**
	 * To get list of booking events by booking
	 * 
	 * Service URL: /event/by-booking/{bookingId} method: GET.
	 * 
	 * @return the list of BookingDTO
	 */
	@GetMapping("/by-booking/{bookingId}")
	List<BookingEventDTO> getEventsByBooking(@PathVariable long bookingId) {
		return eventService.getBookingEvents(bookingId);
	}

	/**
	 * To get booking events by id
	 * 
	 * Service URL: /event/{eventId} method: GET.
	 * 
	 * @return the list of BookingDTO
	 */
	@GetMapping("/{eventId}")
	public BookingEventDTO getBookingEvent(@PathVariable long eventId) {
		return eventService.getEvent(eventId);
	}

	/**
	 * To get list of booking events
	 * 
	 * Service URL: /event method: GET.
	 * 
	 * @return the list of BookingDTO
	 */
	@GetMapping
	public List<BookingEventDTO> getBookingEvents() {
		return eventService.getAllEvents();
	}

	/**
	 * To save Link of booking events
	 * 
	 * Service URL: /event/link method: POST.
	 * 
	 * @param link
	 *            or unlink booking events.
	 * 
	 * @return the BookingDTO
	 */
	@PostMapping("/link")
	BookingEventDTO linkOrUnlinkBookingEvents(@RequestBody BookingEventDTO event) {
		return eventService.linkOrUnlinkBookingEvents(event);
	}

	/**
	 * To get set of link events.
	 * 
	 * Service URL: /event/link method: GET.
	 * 
	 * @param link
	 *            or unlink booking events.
	 * 
	 * @return the BookingDTO
	 */
	@GetMapping("/link/{eventId}")
	Set<BookingEventDTO> getAllLinkinedBookingEvents(@PathVariable long eventId) {
		return eventService.getAllLinkinedBookingEvents(eventId);
	}

	/**
	 * To get booking conflict events with event id
	 * 
	 * Service URL: /event/conflict/{eventId} method: GET.
	 * 
	 * @return the list of BookingDTO
	 */
	@GetMapping("/conflict/{eventId}")
	public List<BookingEventDTO> getBookingConflictEvent(@PathVariable long eventId) {
		return eventService.findCollapseBookingEvents(eventId);
	}

	/**
	 * To copy booking event
	 * 
	 * Service URL: /event/copy method: POST.
	 * 
	 * @return BookingEventDTO
	 */
	@PostMapping("/copy")
	public BookingEventDTO getBookingConflictEvent(@RequestBody BookingEventDTO eventDTO) {
		return eventService.copyEvent(eventDTO);
	}

	/**
	 * To save split booking events
	 * 
	 * Service URL: /event/split method: POST.
	 * 
	 * @param list
	 *            of events
	 * 
	 * @return the list of BookingDTO
	 */
	@PostMapping("/split")
	List<BookingEventDTO> splitEvent(@RequestBody List<BookingEventDTO> events) {
		return eventService.splitBooking(events);
	}

	/**
	 * To get set of link Booking events ID.
	 * 
	 * Service URL: /event/link method: GET.
	 * 
	 * 
	 * @return the Set of link booking event ID.
	 */
	@GetMapping("/link")
	Set<Long> getAllLinkedEventId() {
		return eventService.getAllLinkedEventsId();
	}

	/**
	 * To get list of booking events for all Projecy.
	 * 
	 * Service URL: /event/project method: GET.
	 * 
	 * @return the list of RosterEventDTO
	 */
	@GetMapping(value = "/project")
	public List<RosterEventDTO> getBookingEventsForAllProject() {
		return eventService.getBookingEventsForAllProject();
	}

	/**
	 * To create Recurrence of the booking event.
	 * 
	 * Service URL: /event/recurrence method: POST.
	 * 
	 * @return list of Booki.ngEventDTO
	 */
	@PostMapping("/recurrence")
	public List<BookingEventDTO> createRecurrenceEvent(@RequestBody RecurrenceDTO recurrenceDTO) {
		return eventService.createRecurrenceEvent(recurrenceDTO);
	}
	
	
	/**
	 * To assign booking event to logged-in-user 
	 * 
	 * Service URL: /event/assign/to/logged-in-user method: POST.
	 * 
	 * @param list
	 *            of events
	 * 
	 * @return the list of BookingDTO
	 */
	@PostMapping("/assign/to/logged-in-user")
	List<BookingEventDTO> assigenBookingEventToLoggedInUser(@RequestBody List<BookingEventDTO> events) {
		return eventService.assigenBookingEventToLoggedInUser(events);
	}
	
	

}
