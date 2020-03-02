package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamium.domain.prod.resources.Resource;
import com.teamium.domain.prod.resources.Unavailability;

/**
 * Repository related to role operations.
 * 
 * @author TeamiumNishant
 *
 */
@Repository
public interface UnavailabilityRepository extends JpaRepository<Unavailability, Long> {

	/**
	 * Name of the query retrieving the unavailabilities matching the unavailability
	 * given in parameter as reference and resource in the resources given parameter
	 * 
	 * @param 1
	 *            The unavailability
	 * @param 2
	 *            The resources
	 */
	@Query("SELECT pu FROM Unavailability pu WHERE pu.reference = :unavailability AND pu.resource IN(:resources) AND TYPE(pu) = Unavailability")
	List<Unavailability> findLinkedByUnavailability(@Param("unavailability") Unavailability unavailability,
			@Param("resources") List<Resource<?>> resources);
}
