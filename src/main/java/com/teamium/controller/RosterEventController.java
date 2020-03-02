package com.teamium.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teamium.dto.RosterEventDTO;
import com.teamium.service.RosterEventService;

@RestController
@RequestMapping(value = "/roster-event")
public class RosterEventController {

	private RosterEventService rosterEventService;

	@Autowired
	public RosterEventController(RosterEventService rosterEventService) {
		this.rosterEventService = rosterEventService;
	}

	/**
	 * Method to save or update RosterEvent.
	 * 
	 * Service URL : /roster-event method : POST
	 * 
	 * @param rosterEventDTO
	 * 
	 * @return instance of RosterEventDTO.
	 */
	@PostMapping
	RosterEventDTO saveOrUpdateRosterEvent(@RequestBody RosterEventDTO rosterEventDTO) {
		return rosterEventService.saveOrUpdateRosterEvent(rosterEventDTO);
	}

	/**
	 * Method to get list of RosterEventDTO.
	 * 
	 * Service URL : /roster-event method : GET
	 * 
	 * @return list of RosterEventDTO.
	 */
	@GetMapping
	List<RosterEventDTO> getAllRosterEventDTO() {
		return rosterEventService.getAllRosterEventDTO();
	}

	/**
	 * To delete the RosterEvent.
	 * 
	 * Service URL: /roster-event/{rosterEventId} method: DELETE.
	 * 
	 * @param rosterEventId the rosterEventId.
	 */
	@RequestMapping(value = "/{rosterEventId}", method = RequestMethod.DELETE)
	void deleteRosterEvent(@PathVariable long rosterEventId) {
		rosterEventService.deleteRosterEvent(rosterEventId);
	}
	
	/**
	 * To copy-paste the RosterEvent.
	 * 
	 * Service URL: /roster-event/copy/{rosterEventId} method: POST.
	 * 
	 * @param rosterEventId the rosterEventId.
	 */
	@RequestMapping(value = "/copy/{rosterEventId}", method = RequestMethod.POST)
	RosterEventDTO copyPasteRosterEvent(@PathVariable long rosterEventId, @RequestParam(value = "copyTo", required = true) String copyTo) {
		return rosterEventService.copyPasteRosterEvent(rosterEventId, copyTo);
	}
}
