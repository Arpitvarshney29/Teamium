package com.teamium.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.projects.order.BookingOrderForm;
import com.teamium.domain.prod.projects.order.WorkAndTravelOrder;
import com.teamium.domain.prod.projects.order.WorkAndTravelOrder.OrderType;
import com.teamium.dto.BookingDTO;
import com.teamium.dto.BookingEventDTO;
import com.teamium.dto.EquipmentDTO;
import com.teamium.service.BookingService;
import com.teamium.service.EventService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * <p>
 * Handles operations related to bookings .
 * </p>
 */
@RestController
@RequestMapping(value = "/booking")
@Api(value = "Bookings API", description = "All the Bookings of resources.")
public class BookingController {

	private BookingService bookingService;
	private EventService eventService;

	/**
	 * @param bookingService
	 * @param eventService
	 */
	public BookingController(BookingService bookingService, EventService eventService) {
		this.bookingService = bookingService;
		this.eventService = eventService;
	}

	/**
	 * To get list of all Bookings by resourceId
	 * 
	 * Service URL: /booking/by-resource/{resourceId} method: GET.
	 * 
	 * @param resourceId
	 *            the resourceId
	 * @return the list of BookingDTO
	 */
	@ApiOperation(value = "Find the bookings by resourceId And/Or dates")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = EquipmentDTO.class),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	@RequestMapping(value = "/by-resource/{resourceId}", method = RequestMethod.GET)
	List<BookingDTO> getBookingsByResourceId(@PathVariable Long resourceId) {
		return bookingService.getBookingsByResourceIdAndDates(resourceId, null, null).stream()
				.map(booking -> new BookingDTO(booking)).collect(Collectors.toList());
	}

	/**
	 * 
	 * To get list of bookings
	 * 
	 * Service URL: /booking method: GET.
	 *
	 * @param currentUser
	 * 
	 * @return the list of BookingDTO
	 */
	@ApiOperation(value = "Find All bookings.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = EquipmentDTO.class),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	@RequestMapping(method = RequestMethod.GET)
	List<BookingDTO> getAllBookings(@RequestParam(value = "currentUser", required = false) boolean currentUser) {
		return bookingService.getAllBookingDTOs(currentUser);
	}

	/**
	 * To get list of all bookings by project
	 * 
	 * Service URL: /booking/by-project/{projectId} method: GET.
	 * 
	 * @return the list of BookingDTO
	 */
	@GetMapping("/by-project/{projectId}")
	List<BookingDTO> getBookingByProject(@PathVariable long projectId) {
		return bookingService.getBookingsByProject(projectId);
	}

	/**
	 * To get list function and its bookings by project
	 * 
	 * Service URL: /booking/function/by-project/{projectId} method: GET.
	 * 
	 * @return the map of function id and BookingDTO
	 */
	@GetMapping("function/by-project/{projectId}")
	Map<Long, List<BookingDTO>> getBookinFunctionsByProject(@PathVariable long projectId) {
		return bookingService.getBookingFunctionsByProject(projectId);
	}

	/**
	 * To get booking by id
	 * 
	 * Service URL: /booking/event/{bookingId} method: GET.
	 * 
	 * @return the list of BookingEventDTO
	 */
	@PostMapping("/{bookingId}")
	BookingDTO addBookingEvent(@PathVariable long bookingId) {
		return bookingService.AddBookingEvent(bookingId);
	}

	/**
	 * To get booking by id
	 * 
	 * Service URL: /booking/{bookingId} method: GET.
	 * 
	 * @return the list of BookingDTO
	 */
	@GetMapping("/{bookingId}")
	BookingDTO getBooking(@PathVariable long bookingId) {
		return bookingService.getBooking(bookingId);
	}

	@PostMapping("/events")
	public BookingEventDTO addBookingsEvent(@RequestBody BookingEventDTO event) {
		return eventService.addBookingsEvents(event);
	}

	/**
	 * To get booking by id
	 * 
	 * Service URL: /booking/{bookingId} method: GET.
	 * 
	 * @return the list of BookingDTO
	 */
	@GetMapping("/by-origin/{bookingId}")
	BookingDTO getBookingByOrigin(@PathVariable long bookingId) {
		return bookingService.getBookingByOrigin(bookingId);
	}

	/**
	 * To remove group on booking by id
	 * 
	 * Service URL: /booking/{bookingId} method: GET.
	 * 
	 * @return the list of BookingDTO
	 */
	@PostMapping("/remove-group/{bookingId}")
	BookingDTO removeGroupFromBooking(@PathVariable("bookingId") long bookingId) {
		return bookingService.removeGroupFromBooking(bookingId);
	}

	/**
	 * To get booking by id
	 * 
	 * Service URL: /booking/{bookingId} method: GET.
	 * 
	 * @return the list of BookingDTO
	 */
	@PostMapping("/add-to-group/{bookingId}/{groupId}")
	BookingDTO addGroupOnBooking(@PathVariable("bookingId") long bookingId, @PathVariable("groupId") long groupId) {
		return bookingService.addGroupOnBooking(bookingId, groupId);
	}

	/**
	 * To get booking by logged-in-user's group
	 * 
	 * Service URL: /booking/by-logged-in-user-group method: GET.
	 * 
	 * @return the list of BookingDTO
	 */
	@GetMapping("/by-logged-in-user-group")
	List<BookingDTO> getBookingsByGroupForLoggedInUser() {
		return bookingService.getBookingsByGroupForLoggedInUser();
	}

	/**
	 * To get all bookings by logged-in-user not 100% completed.
	 * 
	 * Service URL: /booking/by-logged-in-user method: GET.
	 * 
	 * @return the list of BookingDTO
	 */
	@GetMapping("/by-logged-in-user")
	List<BookingDTO> getBookingsByLoggedInUser() {
		return bookingService.getBookingsByLoggedInUser();
	}

	/**
	 * To remove resource from booking.
	 * 
	 * Service URL: /booking/unassign/resource/{bookingId} method: GET.
	 * 
	 * @return the BookingDTO
	 */
	@PostMapping("/unassign/resource/{bookingId}")
	BookingDTO unassignResourceOnBooking(@PathVariable("bookingId") long bookingId) {
		return bookingService.unassignResourceOnBooking(bookingId);
	}

	/**
	 * To add user start-time/end-time on booking.
	 * 
	 * Service URL: /booking/unassign/resource/{bookingId} method: POST.
	 * 
	 * @return the list of BookingDTO
	 */
	@PostMapping("/add/start-or-end-time/{bookingId}/{start}")
	BookingDTO addUserStartOrEndTimeOnBooking(@PathVariable("bookingId") long bookingId,
			@PathVariable("start") boolean start) {
		return bookingService.addUserStartOrEndTimeOnBooking(bookingId, start);
	}

	/**
	 * To change completion time on booking.
	 * 
	 * Service URL: /booking/change-completion/{bookingId}/{completion} method:
	 * POST.
	 * 
	 * @return the list of BookingDTO
	 */
	@PostMapping("/change-completion/{bookingId}/{completion}")
	BookingDTO changeCompletionTimeOnBooking(@PathVariable("bookingId") long bookingId,
			@PathVariable("completion") int completion) {
		return bookingService.changeCompletionTimeOnBooking(bookingId, completion);
	}

	/**
	 * To add order form on booking.
	 * 
	 * @param bookingId
	 * @param workAndTravelOrder
	 * @return
	 */
	@PostMapping("/add/order-form/{bookingId}")
	BookingDTO addOrderOnBookingLine(@PathVariable("bookingId") long bookingId,
			@RequestBody WorkAndTravelOrder workAndTravelOrder) {
		return bookingService.addOrderOnBookingLine(bookingId, workAndTravelOrder);
	}

	// /**
	// * To assign booking event to logged-in-user
	// *
	// * Service URL: /event/assign/to/logged-in-user method: POST.
	// *
	// * @param list
	// * of events
	// *
	// * @return the list of BookingDTO
	// */
	// @PostMapping("/assign/to/logged-in-user/{bookingId}")
	// BookingDTO assigenBookingEventToLoggedInUser(@PathVariable("bookingId") long
	// bookingId) {
	// return bookingService.assigenBookingEventToLoggedInUser(bookingId);
	// }

	/**
	 * To get all the booking order forms.
	 * 
	 * @return
	 */
	@GetMapping("/booking-order-form/by-order-type")
	public Map<String, List<BookingOrderForm>> getBookingOrderFormWithCategory() {
		return bookingService.getBookingOrderFormWithCategory();
	}

	/**
	 * To remove a workAndTravelOrder from Booking line.
	 * 
	 * @param bookingId
	 * @param orderType
	 * @return
	 */
	@PostMapping("/remove/order-form")
	public BookingDTO removeOrderOnBookingLine(@RequestParam(value = "bookingId", required = true) long bookingId,
			@RequestParam(value = "orderType", required = true) OrderType orderType) {
		return bookingService.removeOrderOnBookingLine(bookingId, orderType);
	}

	/**
	 * To update the status of a workAndTravelOrder from Booking line.
	 * 
	 * @param bookingId
	 * @param orderType
	 * @return
	 */
	@PostMapping("/order-form/change-status")
	public WorkAndTravelOrder updateOrderFormStatusOnBookingLine(
			@RequestParam(value = "bookingId", required = true) long bookingId,
			@RequestParam(value = "orderType", required = true) OrderType orderType) {
		return bookingService.updateOrderFormStatusOnBookingLine(bookingId, orderType);
	}

}
