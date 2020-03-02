package com.teamium.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.teamium.dto.TimeZoneDTO;
import com.teamium.service.TimeZoneService;

@RestController
@RequestMapping(value = "/time-zone")
public class TimeZoneController {

	private TimeZoneService timeZoneService;

	@Autowired
	public TimeZoneController(TimeZoneService timeZoneService) {
		this.timeZoneService = timeZoneService;
	}

	/**
	 * To get list of all time zone
	 * 
	 * Service URL:/time-zone , method: GET.
	 * 
	 * @return List<TimeZoneDTO>
	 */
	@RequestMapping(method = RequestMethod.GET)
	List<TimeZoneDTO> getAllTimeZone() {
		return this.timeZoneService.getAllTimeZone();
	}

}
