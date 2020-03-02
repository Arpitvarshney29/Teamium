package com.teamium.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamium.domain.prod.projects.planning.roster.RosterEvent;
import com.teamium.domain.prod.resources.Resource;
import com.teamium.domain.prod.resources.functions.Function;
import com.teamium.domain.prod.resources.staff.StaffFunction;
import com.teamium.domain.prod.resources.staff.StaffResource;
import com.teamium.dto.RosterEventDTO;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.RosterEventRepository;
import com.teamium.service.prod.resources.functions.FunctionService;

@Service
public class RosterEventService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private RosterEventRepository rosterEventRepository;
	private FunctionService functionService;
	private ResourceService resourceService;

	@Autowired
	public RosterEventService(RosterEventRepository rosterEventRepository, FunctionService functionService,
			ResourceService resourceService) {
		this.rosterEventRepository = rosterEventRepository;
		this.functionService = functionService;
		this.resourceService = resourceService;
	}

	/**
	 * To save/update roster-event
	 * 
	 * @param rosterEventDTO
	 * 
	 * @return instance of RosterEventDTO.
	 */
	public RosterEventDTO saveOrUpdateRosterEvent(RosterEventDTO rosterEventDTO) {
		this.logger.info("Inside RosterEventService :: saveOrUpdateRosterEvent() : rosterEventDTO : " + rosterEventDTO);
		RosterEvent rosterEvent = null;
		if (rosterEventDTO.getId() != null) {
			rosterEvent = this.rosterEventRepository.findOne(rosterEventDTO.getId());
			if (rosterEvent == null) {
				throw new NotFoundException("Please enter valid Roster Event Id : " + rosterEventDTO.getId());
			}
		} else {
			rosterEvent = new RosterEvent();
		}

		rosterEvent = validateAndGetInsatanceOfRosterEvent(rosterEvent, rosterEventDTO);

		this.logger.info("Successfully return from saveOrUpdateRosterEvent().");
		return new RosterEventDTO(this.rosterEventRepository.save(rosterEvent));
	}

	/**
	 * Get list of all RosterEventDTO.
	 * 
	 * @return List Of RosterEventDTO.
	 */
	public List<RosterEventDTO> getAllRosterEventDTO() {
		this.logger.info("Inside getAllRosterEventDTO()");
		List<RosterEvent> rosterEvents = this.rosterEventRepository.findAll();
		this.logger.info("fetched list of rosterEvent from DB: " + rosterEvents.size());
		return rosterEvents.stream().map(roster -> {
			RosterEventDTO rosterEventDTO = new RosterEventDTO(roster);
			if (roster.getResources() != null && !roster.getResources().isEmpty() && roster.getFrom() != null
					&& roster.getTo() != null && roster.getFunction() != null
					&& rosterEvents.stream().filter(rt -> rt.getFunction() != null).filter(rt -> rt.getFrom() != null)
							.filter(rt -> rt.getTo() != null)
							.filter(rt -> rt.getResources() != null && !rt.getResources().isEmpty())
							.filter(rt -> rt.getId().longValue() != roster.getId().longValue())
							.filter(rt -> rt.getFunction().getId().longValue() == roster.getFunction().getId())
							.filter(rt -> roster.getTo().getTime().compareTo(rt.getFrom().getTime()) > 0
									&& rt.getTo().getTime().compareTo(roster.getFrom().getTime()) > 0)
							.filter(rt -> roster.getResources().stream()
									.filter(res -> rt.getResources().stream()
											.filter(rtRes -> rtRes.getId().longValue() == res.getId().longValue())
											.findFirst().orElse(null) != null)
									.findFirst().orElse(null) != null)
							.findFirst().orElse(null) != null) {
				rosterEventDTO.setConflict(true);
			}
			return rosterEventDTO;
		}).collect(Collectors.toList());
	}

	/**
	 * To validate RosterEventDTO and set value on Roster-event.
	 * 
	 * @param rosterEvent
	 * @param rosterEventDTO
	 * 
	 * @return instance of Roster Event.
	 */
	private RosterEvent validateAndGetInsatanceOfRosterEvent(RosterEvent rosterEvent, RosterEventDTO rosterEventDTO) {
		this.logger.info(
				"Inside RosterEventService ::  validateAndGetInsatanceOfRosterEvent(), to validate rosterEventDTO : "
						+ rosterEventDTO);
		if (rosterEventDTO.getFunctionId() == null) {
			throw new NotFoundException("Please choose some function");
		}
		if (StringUtils.isBlank(rosterEventDTO.getStartTime())) {
			throw new NotFoundException("Please choose start date");
		}
		if (StringUtils.isBlank(rosterEventDTO.getEndTime())) {
			throw new NotFoundException("Please choose end date");
		}

		DateTime start = DateTime.parse(rosterEventDTO.getStartTime()).withZone(DateTimeZone.forID("Asia/Calcutta"));
		DateTime end = DateTime.parse(rosterEventDTO.getEndTime()).withZone(DateTimeZone.forID("Asia/Calcutta"));

		if (end.isBefore(start)) {
			logger.info("Start Date must be greater than End Date.");
			throw new NotFoundException("Start Date must be greater than End Date.");
		}
		Function function = functionService.validateFunction(rosterEventDTO.getFunctionId());
		if (!(function instanceof StaffFunction)) {
			logger.info("Please assign only Personnel Function");
			throw new UnprocessableEntityException("Please assign only Personnel Function");
		}
		List<Resource<?>> resources = null;
		if (rosterEventDTO.getResourcesDto() != null && !rosterEventDTO.getResourcesDto().isEmpty()) {
			resources = rosterEventDTO.getResourcesDto().stream().map(resourceDTO -> {
				Resource<?> resource = this.resourceService.findResource(resourceDTO.getId());
				if (resource.getFunction(function) == null) {
					logger.info("Please choose resource from same function.");
					throw new NotFoundException("Please choose resource from same function.");
				}
				return resource;
			}).collect(Collectors.toList());

			if (resources.size() > rosterEventDTO.getQuantity()) {
				logger.info("Quantity must be less than selected resource.");
				throw new NotFoundException("Quantity must be less than selected resource.");
			}
		}
		// this.resourceService.findResource(resourceId)
		Calendar calendarFrom = Calendar.getInstance();
		calendarFrom.setTimeInMillis(start.getMillis());

		Calendar calendarTo = Calendar.getInstance();
		calendarTo.setTimeInMillis(end.getMillis());

		rosterEvent.setFunction(function);
		rosterEvent.setFrom(calendarFrom);
		rosterEvent.setTo(calendarTo);
		rosterEvent.setResources(resources);
		rosterEvent.setQuantity(new Float(rosterEventDTO.getQuantity()));
		rosterEvent.setFunctionType(function.getValue());
		rosterEvent.setTitle(getRosterEventTitle(rosterEvent));
		logger.info("Returning from validateAndGetInsatanceOfRosterEvent()");
		return rosterEvent;
	}

	/**
	 * To get the title of the RosterEvent based on selected function, quantity and
	 * selected resources.
	 * 
	 * @param quantity
	 * @param fumctionType
	 * @param selectedResources
	 * 
	 * @return title of roster-event.
	 */
	private String getRosterEventTitle(RosterEvent rosterEvent) {
		logger.info("Inside RosterEventService :: getRosterEventTitle(), to get roster-event title");
		List<Resource<?>> selectedResources = rosterEvent.getResources();
		String title = "";
		int quantity = rosterEvent.getQuantity() != null ? rosterEvent.getQuantity().intValue() : 0;
		title += (quantity) + " " + rosterEvent.getFunctionType() + " : ";
		if (selectedResources == null || selectedResources.isEmpty()) {
			return title;
		}
		int length = selectedResources.size();
		int size = 1;
		for (Resource<?> resource : selectedResources) {
			if (size != length) {
				title += resource.getName() + ", ";
			} else {
				title += resource.getName();
			}
			size++;
		}
		logger.info("Returning the title : " + title + " from getRosterEventTitle()");
		return title;
	}

	/**
	 * To delete roster-event by id
	 * 
	 * @param rosterEventId
	 */
	public void deleteRosterEvent(long rosterEventId) {
		logger.info("Inside RosterEventService :: deleteRosterEvent( rosterEventId : " + rosterEventId
				+ " ), to delete roster-event by id");
		this.validateRosterEventExistence(rosterEventId);
		try {
			rosterEventRepository.delete(rosterEventId);
		} catch (Exception e) {
			logger.info("Exception occured in deleting roster-event");
			throw new UnprocessableEntityException("Exception occured in deleting roster-event");
		}
		logger.info("Returning after deleting roster-event");
	}

	/**
	 * To find roster-event by id
	 * 
	 * @param rosterEventId
	 * 
	 * @return the roster-event object
	 */
	private RosterEvent validateRosterEventExistence(Long rosterEventId) {
		logger.info("Inside RosterEventService :: validateRosterEventExistence(), To find roster-event by id");
		if (rosterEventId == null) {
			logger.info("Invalid roster-event id");
			throw new UnprocessableEntityException("Invalid roster-event id");
		}
		RosterEvent rosterEvent = rosterEventRepository.findOne(rosterEventId);
		if (rosterEvent == null) {
			logger.info("RosterEvent not found with id : " + rosterEventId);
			throw new NotFoundException("RosterEvent not found");
		}
		logger.info("Returning after finding roster-event by id");
		return rosterEvent;
	}

	/**
	 * To copy-paste roster-event
	 * 
	 * @param rosterEventId
	 * @param copyTo
	 * 
	 * @return roster-event object
	 */
	@SuppressWarnings("deprecation")
	public RosterEventDTO copyPasteRosterEvent(long rosterEventId, String copyTo) {
		logger.info("Inside RosterEventService :: copyPasteRosterEvent( rosterEventId : " + rosterEventId
				+ ", copyTo : " + copyTo + " ), To copy-paste roster-event");
		RosterEvent entity = this.validateRosterEventExistence(rosterEventId);
		if (StringUtils.isBlank(copyTo)) {
			logger.info("Date cannot be empty");
			throw new UnprocessableEntityException("Date cannot be empty.");
		}

		Calendar fromDateRoster = entity.getFrom();
		Calendar toDateRoster = entity.getTo();

		Date fromDate = fromDateRoster.getTime();
		long diff = toDateRoster.getTimeInMillis() - fromDateRoster.getTimeInMillis();

		if (diff < 0) {
			logger.info("End Date cannot be before Start Date.");
			throw new UnprocessableEntityException("End Date cannot be before Start Date.");
		}
		DateTime startDateTime = null;
		try {
			startDateTime = DateTime.parse(copyTo).withZone(DateTimeZone.forID("Asia/Calcutta"));
		} catch (Exception e) {
			logger.info("Invalid date");
			throw new UnprocessableEntityException("Invalid date");
		}

		DateTime startTime = startDateTime.withTime(fromDate.getHours(), fromDate.getMinutes(), fromDate.getSeconds(),
				0);

		Calendar start = Calendar.getInstance();
		start.setTimeInMillis(startTime.getMillis());

		Calendar end = Calendar.getInstance();
		end.setTimeInMillis(start.getTimeInMillis() + diff);

		RosterEvent copiedEvent = entity.clone(entity);
		copiedEvent.setFrom(start);
		copiedEvent.setTo(end);

		logger.info("Saving copied event");
		RosterEvent savedInstance = rosterEventRepository.save(copiedEvent);
		logger.info("Returning after copy paste roster-event");
		return new RosterEventDTO(savedInstance);
	}

}
