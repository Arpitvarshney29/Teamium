package com.teamium.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.teamium.domain.TeamiumException;
import com.teamium.domain.TeamiumPersistenceException;
import com.teamium.domain.prod.resources.CoveredUnavailabilityPeriodException;
import com.teamium.domain.prod.resources.ReferencedUnavailabilityException;
import com.teamium.domain.prod.resources.Resource;
import com.teamium.domain.prod.resources.Unavailability;
import com.teamium.domain.prod.resources.UnavailabilityEvent;
import com.teamium.domain.prod.resources.equipments.EquipmentResource;
import com.teamium.domain.prod.resources.staff.StaffResource;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.UnavailabilityRepository;

/**
 * 
 * A service class implementation for Unavailability Controller
 *
 */
@Service
public class UnavailabilityService {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	private UnavailabilityRepository unavailabilityRepository;
	private UnavailabilityEventService unavailabilityEventService;
	private StaffResourceService staffResourceService;

	@Autowired
	public UnavailabilityService(UnavailabilityRepository unavailabilityRepository,
			UnavailabilityEventService unavailabilityEventService, StaffResourceService staffResourceService) {
		this.unavailabilityRepository = unavailabilityRepository;
		this.unavailabilityEventService = unavailabilityEventService;
		this.staffResourceService = staffResourceService;
	}

	/**
	 * Tests if the given resource is available
	 * 
	 * @param resource
	 *            the resource to test
	 * @param bool
	 *            false to make it unavailable, true to make it available
	 * @return true if it is available else returns false
	 * @throws CloneNotSupportedException
	 */
	public void setAvailable(Resource<?> resource, Boolean isAvailable) throws Exception {
		this.logger.info("Inside UnavailabilityService,setAvailable");
		this.logger.info("resource : " + resource);
		this.logger.info("isAvailable : " + isAvailable);

		Calendar start = Calendar.getInstance();
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		start.set(Calendar.MILLISECOND, 0);

		Calendar end = (Calendar) start.clone();
		end.set(Calendar.HOUR_OF_DAY, 23);
		end.set(Calendar.MINUTE, 59);
		end.set(Calendar.SECOND, 59);
		end.set(Calendar.MILLISECOND, 999);

		this.logger.info("Fetching list of UnavailabilityEvent : findByResourceByPeriod");
		this.logger.info("resource : " + resource);
		this.logger.info("start : " + start);
		this.logger.info("end : " + end);

		List<UnavailabilityEvent> result = this.unavailabilityEventService.findByResourceByPeriod(resource, start, end);

		this.logger.info("Fetched list of UnavailabilityEvent : findByResourceByPeriod : found : " + result.size());

		if (isAvailable) {
			this.logger.info("Inside IF : isAvailable : " + isAvailable);
			if (!result.isEmpty()) {
				this.logger.info("Inside IF : result.isEmpty() " + result.isEmpty());
				Calendar yesterday = (Calendar) start.clone();
				yesterday.add(Calendar.DAY_OF_MONTH, -1);
				for (UnavailabilityEvent pu : result) {
					if (pu.getFrom() != null && start.get(Calendar.YEAR) == pu.getFrom().get(Calendar.YEAR)
							&& start.get(Calendar.MONTH) == pu.getFrom().get(Calendar.MONTH)
							&& start.get(Calendar.DAY_OF_MONTH) == pu.getFrom().get(Calendar.DAY_OF_MONTH)) {
						this.deleteUnavailability(pu);
					} else {
						pu.setTo(yesterday);
					}
					this.logger.info(this + "Setting off " + pu + " for " + resource);
				}
			} else {
				this.logger.info("Inside Else : result.isEmpty() " + result.isEmpty());
				this.logger.info(this + "" + resource + " is already available");
			}
		} else {
			this.logger.info("Inside Else : isAvailable : " + isAvailable);
			Unavailability pu = this.createUnavailability(resource, start, null);
			this.logger.info(this + "Setting on " + pu + " for " + resource);
		}
	}

	/**
	 * Create a periodic unavailability for the given resource within the given
	 * range of date
	 * 
	 * @param resource
	 *            the unavailable resource
	 * @param min
	 *            starting date
	 * @param max
	 *            ending date
	 * @throws TeamiumException
	 *             unavailability_create_failed
	 * @throws CoveredUnavailabilityPeriodException
	 */
	public Unavailability createUnavailability(Resource<?> resource, Calendar min, Calendar max)
			throws CloneNotSupportedException {
		this.logger.info("Inside UnavailabilityService,createUnavailability");
		this.logger.info("resource : " + resource);
		this.logger.info("min : " + min);
		this.logger.info("max : " + max);

		Unavailability unavailability = null;
		List<Unavailability> res = new ArrayList<Unavailability>();

		unavailability = new Unavailability();
		unavailability.setResource(resource);
		unavailability.setFrom(min);
		unavailability.setTo(max);
		unavailability = this.saveUnavailability(unavailability);
		res.add(unavailability);

		while (resource.getParent() != null && resource instanceof EquipmentResource) {
			Unavailability linkedUnavailability = unavailability.clone();
			linkedUnavailability.setReference(unavailability);
			resource = resource.getParent();
			linkedUnavailability.setResource(resource);
			linkedUnavailability = this.saveUnavailability(linkedUnavailability);
			res.add(linkedUnavailability);
		}

		// Create linked unavailability events
		for (Unavailability un : res) {
			for (UnavailabilityEvent event : Unavailability.createEvent(un)) {
				this.unavailabilityEventService.saveUnavailabilityEvent(event);
			}
			// Process group unavailability
			if (un.getResource() instanceof StaffResource && un.getResource().getEntity() == null) {
				// Find all res members
				List<StaffResource> result = this.staffResourceService.findChildren(un.getResource());
				if (result != null && !result.isEmpty()) {
					for (StaffResource r : result) {
						for (UnavailabilityEvent eventMember : Unavailability.createEvent(un)) {
							eventMember.setResource(r);
							this.unavailabilityEventService.saveUnavailabilityEvent(eventMember);
						}
					}
				}
			}
		}

		this.logger
				.info("Returning from UnavailabilityService,createUnavailability : unavailability : " + unavailability);

		return unavailability;

	}

	/**
	 * Save the unavailability given in parameter and its linked unavailabilities
	 */
	public Unavailability saveUnavailability(Unavailability unavailability) {
		this.logger.info("Inside UnavailabilityService,saveUnavailability");
		this.logger.info("unavailability : " + unavailability);

		unavailability = this.unavailabilityRepository.save(unavailability);
		for (Unavailability pu : this.getLinkedUnavailabilities(unavailability)) {
			pu.copy(unavailability);
			this.unavailabilityRepository.save(pu);
		}

		this.logger
				.info("Returning form UnavailabilityService,saveUnavailability : unavailability : " + unavailability);

		return unavailability;

	}

	/**
	 * Getting the unavailabilities having as reference the unavailability given in
	 * parameter
	 * 
	 * @return
	 * @throws TeamiumException
	 */
	public List<Unavailability> getLinkedUnavailabilities(Unavailability unavailability) {
		this.logger.info("Inside UnavailabilityService,getLinkedUnavailabilities");
		this.logger.info("unavailability : " + unavailability);

		List<Resource<?>> resources = new ArrayList<>();
		Resource<?> resource = unavailability.getResource();

		while (resource != null) {
			if (!resources.contains(resource)) {
				resources.add(resource);
				resource = resource.getParent();
			} else {
				resource = null;
			}
		}

		this.logger.info("Fetching list of Unavailability from findLinkedByUnavailability : ");
		this.logger.info("unavailability : " + unavailability);
		this.logger.info("resources : " + resources);

		List<Unavailability> unavailabilities = this.unavailabilityRepository.findLinkedByUnavailability(unavailability,
				resources);

		this.logger.info(
				"Fetched list of Unavailability from findLinkedByUnavailability : found : " + unavailabilities.size());

		return unavailabilities;
	}

	/**
	 * Delete the individual unavailability given in parameter. <strong>If the
	 * unavailability is generated from a group unavailability, it would be the
	 * group unavailability which would be remove</strong>
	 * 
	 * @param iu
	 *            the individual unavailability
	 * @throws TeamiumPersistenceException
	 *             entity_delete_failed
	 * @author slopes
	 * @since v5.7.0 Remove the unavailability given in parameter and the
	 *        unavailability having as reference the removed unavailability
	 * @throws ReferencedUnavailabilityException
	 *             If the unavailability have a reference
	 */
	public void deleteUnavailability(UnavailabilityEvent iu) {
		this.logger.info("Inside UnavailabilityService,deleteUnavailability");
		this.logger.info("UnavailabilityEvent : " + iu);
		if (iu.getUnavailability().getReference() != null) {
			this.logger.info("Inside If : " + iu.getUnavailability().getReference());
			throw new UnprocessableEntityException("One of the child is not available of this Entity.");
		} else {
			for (Unavailability pu : this.getLinkedUnavailabilities(iu.getUnavailability())) {
				this.logger.info("Deleting Unavailability : " + pu);
				this.unavailabilityRepository.delete(pu);
			}
			this.logger.info("Deleting Unavailability : " + iu.getUnavailability());
			this.unavailabilityRepository.delete(iu.getUnavailability());
		}
		this.logger.info("Returning from UnavailabilityService,deleteUnavailability");
	}

}
