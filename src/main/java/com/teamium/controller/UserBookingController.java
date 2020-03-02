package com.teamium.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamium.dto.BookingDTO;
import com.teamium.dto.UserBookingDTO;
import com.teamium.dto.WeekDurationDTO;
import com.teamium.service.UserBookingService;

/**
 * 
 * Handles operations related to logged in user like
 * 
 * @author TeamiumNishant
 *
 */
@RestController
@RequestMapping(value = "/user-booking")
public class UserBookingController {

	private UserBookingService userBookingService;

	@Autowired
	public UserBookingController(UserBookingService userBookingService) {
		this.userBookingService = userBookingService;
	}

	/**
	 * To save and update UserBooking.
	 * 
	 * Service URL:/user-booking method: POST
	 * 
	 * @param UserBookingDTO the userBookingDTO .
	 */
	@PostMapping
	public UserBookingDTO saveCustomer(@RequestBody UserBookingDTO userBookingDTO) {
		return userBookingService.saveOrUpdateUserBooking(userBookingDTO);
	}

	/**
	 * To UserBooking list
	 * 
	 * Service URL: /user-booking/{staff} method: GET.
	 * 
	 * @return the UserBookingDTO list
	 */
	@GetMapping(value = "/{staff}")
	public List<UserBookingDTO> findAllUserBookingByStaffMemberId(@PathVariable("staff") Long staffMemberId) {
		return userBookingService.findAllUserBookingByStaffMemberId(staffMemberId);
	}

	@DeleteMapping(value = "/{userBookingId}")
	public void deleteUserBooking(@PathVariable Long userBookingId) {
		userBookingService.deleteUserBooking(userBookingId);

	}

	/**
	 * To Week Duration list
	 * 
	 * Service URL: /user-booking/week/{staff} method: GET.
	 * 
	 * @return the WeekDurationDTO list
	 */
	@GetMapping(value = "/week/{staff}")
	public List<WeekDurationDTO> findWeeklyWorkingDurationForUser(@PathVariable("staff") Long staffMemberId) {
		return userBookingService.findWeeklyWorkingDurationForUser(staffMemberId);
	}

	/**
	 * To save Time-Report add on.
	 * 
	 * Service URL:/user-booking/time-report method: POST
	 * 
	 * @param bookingDTO the bookingDTO .
	 */
	@PostMapping(value = "/time-report")
	public UserBookingDTO saveTimeReportAddOn(@RequestBody BookingDTO bookingDTO) {
		return userBookingService.saveTimeReportAddOn(bookingDTO);
	}

}
