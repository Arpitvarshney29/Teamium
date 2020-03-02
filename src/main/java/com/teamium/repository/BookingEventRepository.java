package com.teamium.repository;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.teamium.domain.prod.projects.BookingEvent;
import com.teamium.domain.prod.projects.planning.Event;
import com.teamium.domain.prod.resources.Resource;
import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.projects.Booking;

public interface BookingEventRepository extends JpaRepository<BookingEvent, Long> {

	List<BookingEvent> findByResource(Resource resource);

	@Query("SELECT b from BookingEvent b  WHERE b.origin.record.id=:projectId")
	List<BookingEvent> getByProjectId(@Param("projectId") long projectId);

	@Query("SELECT b from BookingEvent b  WHERE b.origin.id=:bookingId")
	List<BookingEvent> getByBokingId(@Param("bookingId") long bookingId);

	@Query("SELECT b from BookingEvent b WHERE b.origin in :lines")
	List<BookingEvent> getByLine(@Param("lines") List<Line> lines);

	List<BookingEvent> findByOrigin(Booking origin);

	@Query("SELECT ev FROM BookingEvent ev WHERE ((ev.from < :to AND ev.to> :from) OR (ev.from = :from AND ev.to = :to)) AND  TYPE(ev.resource) != DefaultResource  AND ev.id!=:eventId AND ev.resource.id = :currentResource")
	List<BookingEvent> findCollapseBookingEvents(@Param("from") Calendar from, @Param("to") Calendar to,
			@Param("eventId") long eventId, @Param("currentResource") long currentResource);

	@Query("SELECT b from BookingEvent b join b.linkedEvents le  WHERE :bookingId IN(le.id)")
	Set<BookingEvent> getAllLinkedEventsByBokingId(@Param("bookingId") long bookingId);

	@Query("SELECT b from BookingEvent b  WHERE b.origin.record.id=:projectId  AND ((b.from BETWEEN :from AND :to) AND (b.to BETWEEN :from AND :to))")
	List<BookingEvent> getByProjectIdAndBetween(@Param("projectId") long projectId, @Param("from") Calendar from,
			@Param("to") Calendar to);
	
	@Query("SELECT ev FROM Event ev WHERE (CAST(ev.from AS date)<=CAST(:to AS date)) AND ( ev.to IS NULL OR (CAST(ev.to AS date)>=CAST(:from AS date))) AND EXISTS (SELECT evc FROM Event evc WHERE evc.from <= ev.to AND ( evc.to IS NULL or evc.to >= ev.from ) AND ev.resource = evc.resource AND ev.id <> evc.id )")
	List<BookingEvent> getEventsBetweenDateRange(@Param("from") Calendar from, @Param("to") Calendar to);
	
}
