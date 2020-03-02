package com.teamium.service;

import java.util.Calendar;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.teamium.domain.prod.resources.Resource;
import com.teamium.domain.prod.resources.UnavailabilityEvent;
import com.teamium.repository.UnavailabilityEventRepository;

/**
 * 
 * A service class implementation for Unavailability Controller
 *
 */
@Service
public class UnavailabilityEventService {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private UnavailabilityEventRepository unavailabilityEventRepository;

	@Autowired
	public UnavailabilityEventService(UnavailabilityEventRepository unavailabilityEventRepository) {
		this.unavailabilityEventRepository = unavailabilityEventRepository;
	}

	public void saveUnavailabilityEvent(UnavailabilityEvent unavailabilityEvent) {
		logger.info("Inside UnavailabilityEventService,saveUnavailabilityEvent");
		this.unavailabilityEventRepository.save(unavailabilityEvent);
		logger.info("Returning from UnavailabilityEventService,saveUnavailabilityEvent");
	}

	public List<UnavailabilityEvent> findByResourceByPeriod(Resource<?> resource, Calendar from, Calendar to) {
		logger.info("Inside UnavailabilityEventService,findByResourceByPeriod");
		logger.info("resource : " + resource);
		logger.info("from : " + from);
		logger.info("to : " + to);

		List<UnavailabilityEvent> unavailabilityEvents = this.unavailabilityEventRepository
				.findByResourceByPeriod(resource, from, to);

		logger.info("Returning from UnavailabilityEventService,findByResourceByPeriod :found "
				+ unavailabilityEvents.size());

		return unavailabilityEvents;
	}

	/**
	 * Tests if the given resource is available
	 * 
	 * @param resource
	 *            the resource to test
	 * @return true if it is available else returns false
	 */
	public Boolean isAvailable(Resource<?> resource) {
		if (resource.getId() == null) {
			return Boolean.TRUE;
		}
		Calendar now = Calendar.getInstance();
		logger.info("Inside UnavailabilityEventService,isAvailable");
		logger.info("resource : " + resource);
		logger.info("now : " + now);

		List<UnavailabilityEvent> result = findByResourceByPeriod(resource, now, now);

		logger.info("Returning from UnavailabilityEventService,isAvailable : found : " + result.size());
		return result.isEmpty();
	}
}
