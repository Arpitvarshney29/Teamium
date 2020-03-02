package com.teamium.repository;

import java.util.Calendar;
import java.util.List;

import javax.persistence.NamedQuery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.projects.planning.Event;
import com.teamium.domain.prod.resources.Resource;
import com.teamium.domain.prod.resources.functions.Function;

/**
 * Repository related to role operations.
 * 
 * @author TeamiumNishant
 *
 */
@Repository
public interface ResourceRepository extends JpaRepository<Resource<?>, Long> {

	@Query("SELECT r from Resource r join r.functions f WHERE f.function.id =:functionId")
	List<Resource> getResourceByFunction(@Param("functionId") long functionId);

	@Query("SELECT r from Resource r WHERE TYPE(r) =:discriminator AND r.function.id=:functionId")
	List<Function> getResourceByDiscriminatorAndFunction(@Param("discriminator") Class<?> discriminator,
			@Param("functionId") long functionId);

	@Query("SELECT l.resource FROM Line l WHERE l.record =:recordId")
	List<Resource> getResourceByRecord(@Param("recordId") Long recordId);

	@Query("SELECT r FROM Resource r, IN(r.functions) f WHERE TYPE(r) != DefaultResource AND f.function = :function "
			+ "AND NOT EXISTS(SELECT ev FROM Event ev WHERE  ev.resource = r AND ev.from >= :from AND ev.to <= :to) "
			+ "AND NOT EXISTS(SELECT pu FROM UnavailabilityEvent pu WHERE TYPE(pu) = UnavailabilityEvent AND pu.resource = r AND ((pu.from <= :from AND pu.to IS NULL) or (pu.from <= :from AND pu.to >= :to))) "
			+ "AND( TYPE(r) != StaffResource OR NOT EXISTS (SELECT rosterEvent FROM RosterEvent rosterEvent join rosterEvent.resources res WHERE (rosterEvent.from <= :to and rosterEvent.to >= :from) AND rosterEvent.function =:function AND res = r )) "
			+ "AND NOT EXISTS(SELECT b FROM Booking b WHERE b.resource = r AND b.function = :function AND (b.from >= :from AND b.to <= :to))")
	List<Resource<?>> findResourcesAvailableByFunctionAndStartAndEndDate(@Param("function") Function function,
			@Param("from") Calendar from, @Param("to") Calendar to);

	@Query("SELECT r FROM Resource r, IN(r.functions) f WHERE TYPE(r) != DefaultResource AND f.function.id = :function "
			+ "AND NOT EXISTS(SELECT ev FROM Event ev WHERE  ev.resource = r AND ((ev.from > :from AND ev.from< :to) OR (ev.to > :from AND ev.to < :to)) ) "
			+ "AND NOT EXISTS(SELECT pu FROM UnavailabilityEvent pu WHERE TYPE(pu) = UnavailabilityEvent AND pu.resource = r AND ((pu.from <= :from AND pu.to IS NULL) or (pu.from <= :from AND pu.to >= :to))) "
			+ "AND( TYPE(r) != StaffResource OR NOT EXISTS (SELECT rosterEvent FROM RosterEvent rosterEvent join rosterEvent.resources res WHERE (rosterEvent.from <= :to and rosterEvent.to >= :from) AND rosterEvent.function =:function AND res = r )) "
			+ "AND NOT EXISTS(SELECT b FROM Booking b WHERE b.resource = r AND b.function = :function AND (b.from >= :from AND b.to <= :to))")
	List<Resource<?>> findResourcesAvailableByFunctionIdAndStartAndEndDate(@Param("function") long function,
			@Param("from") Calendar from, @Param("to") Calendar to);

	@Query("SELECT res FROM Resource res WHERE TYPE(res) != DefaultResource "
			+ "AND NOT EXISTS (FROM Event ev,IN(ev.resource) res  WHERE ((ev.from > :from AND ev.from< :to) OR (ev.to > :from AND ev.to < :to)))"
			+ "AND NOT EXISTS(SELECT pu FROM UnavailabilityEvent pu WHERE TYPE(pu) = UnavailabilityEvent AND pu.resource = res AND ((pu.from <= :from AND pu.to IS NULL) or (pu.from <= :from AND pu.to >= :to))) "
			+ "AND( TYPE(res) != StaffResource OR NOT EXISTS (SELECT rosterEvent FROM RosterEvent rosterEvent join rosterEvent.resources reso WHERE (rosterEvent.from <= :to and rosterEvent.to >= :from) AND res = reso )) ")
	List<Resource<?>> findResourcesAvailableBetween(@Param("from") Calendar from, @Param("to") Calendar to);
}
