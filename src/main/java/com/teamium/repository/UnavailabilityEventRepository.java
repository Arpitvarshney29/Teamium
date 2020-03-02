package com.teamium.repository;

import java.util.Calendar;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.teamium.domain.prod.resources.Resource;
import com.teamium.domain.prod.resources.UnavailabilityEvent;

/**
 * Repository related to role operations.
 * 
 * @author TeamiumNishant
 *
 */
@Repository
public interface UnavailabilityEventRepository extends JpaRepository<UnavailabilityEvent, Long> {

	/**
	 * Name of the query retrieving unavailable periods (excluding shared ones) for
	 * the given resource on the given period
	 * 
	 * @param 1
	 *            the given resource
	 * @param 2
	 *            beginning of period
	 * @param 3
	 *            end of period
	 */
	@Query("SELECT pu FROM UnavailabilityEvent pu WHERE pu.resource = :resource AND (pu.from IS NULL or pu.from <= :from) AND (pu.to IS NULL OR pu.to >= :to) AND TYPE(pu) = UnavailabilityEvent")
	public List<UnavailabilityEvent> findByResourceByPeriod(@Param("resource") Resource<?> resource,
			@Param("from") Calendar from, @Param("to") Calendar to);

}
