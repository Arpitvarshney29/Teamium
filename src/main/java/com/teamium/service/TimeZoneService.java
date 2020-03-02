package com.teamium.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamium.domain.TimeZone;
import com.teamium.dto.TimeZoneDTO;
import com.teamium.repository.TimeZoneRepository;

@Service
public class TimeZoneService {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private TimeZoneRepository timeZoneRepository;

	@Autowired
	public TimeZoneService(TimeZoneRepository timeZoneRepository) {
		this.timeZoneRepository = timeZoneRepository;
	}

	/**
	 * get time zone by zone name
	 * 
	 * @param name
	 * @return TimeZone
	 */
	public TimeZone getTimeZoneBYName(String name) {
		logger.info("Inside TimeZoneService:getTimeZoneBYName(String name),To get timezone by zone name: " + name);
		TimeZone timeZone = this.timeZoneRepository.findByZoneNameAndIsCheck(name, true);
		logger.info("Successfully returning TimeZone after getting Timezone by zone name");
		return timeZone;
	}

	/**
	 * get list of all time zone
	 * 
	 * @return timeZoneDTOs
	 */
	public List<TimeZoneDTO> getAllTimeZone() {
		logger.info("Inside getAllTimeZone(),To get all timezone");
		List<TimeZoneDTO> timeZoneDTOs = this.timeZoneRepository.findAll().stream().map(dto -> new TimeZoneDTO(dto))
				.collect(Collectors.toList());
		logger.info("Successfully returning timezones list");
		return timeZoneDTOs;
	}

}
