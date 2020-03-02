package com.teamium.repository;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamium.domain.Group;
import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.resources.Resource;
import com.teamium.domain.prod.resources.functions.Function;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	@Query("SELECT b FROM Booking b WHERE b.resource = :resource AND b.from <= :endDate AND b.to >= :startDate ORDER BY b.from ASC")
	List<Booking> findByResourcesAndStartDateAndEndDate(@Param("resource") Resource<?> resource,
			@Param("startDate") Calendar startDate, @Param("endDate") Calendar endDate);

	@Query("SELECT b FROM Booking b WHERE b.resource = :resource AND  b.from = :startDate ORDER BY b.from ASC")
	List<Booking> findByResourcesAndStartDate(@Param("resource") Resource<?> resource,
			@Param("startDate") Calendar startDate);

	@Query("SELECT b FROM Booking b WHERE b.resource = :resource AND  b.to = :endDate ORDER BY b.to ASC")
	List<Booking> findByResourcesAndEndDate(@Param("resource") Resource<?> resource,
			@Param("endDate") Calendar endDate);

	@Query("SELECT b FROM Booking b  join b.record r WHERE TYPE(r) =:discriminator  AND (b.resource = :resource AND b.bookingEvent IS NOT NULL) ORDER BY b.from ASC")
	List<Booking> findByResources(@Param("resource") Resource<?> resource,
			@Param("discriminator") Class<?> discriminator);

	@Query("SELECT b FROM Booking b WHERE b.record.id =:recordId")
	List<Booking> getBookingsByRecord(@Param("recordId") Long recordId);

	Booking findByOrigin(Long origin);

	@Query("SELECT Count(l) from Line l WHERE l.rate.id =:rateId")
	long getLineCountByRate(@Param("rateId") long rateId);

	@Query("SELECT b.record FROM Booking b WHERE b.resource = ?1 ORDER BY b.from ASC")
	List<Record> findProjectsByBookingByResources(@Param("resource") Resource<?> resource);

	@Query("SELECT b FROM Booking b join b.record r WHERE b.resource =:resource AND TYPE(r) =:discriminator AND b.to<=:to ORDER BY b.to DESC")
	List<Booking> findRecentBooking(@Param("resource") Resource<?> resource,
			@Param("discriminator") Class<?> discriminator, @Param("to") Calendar to);

	@Query("SELECT b FROM Booking b join b.record r WHERE TYPE(r) =:discriminator AND  b.group IN(:groups) AND b.function IN(:functions)")
	List<Booking> findByGroupsAndResouceFunction(@Param("groups") Set<Group> groups,
			@Param("functions") Set<Function> functions, @Param("discriminator") Class<?> discriminator);

	@Query("SELECT b FROM Booking b join b.record r WHERE TYPE(r) =:discriminator AND b.function IN(:functions)")
	List<Booking> findByResouceFunction(@Param("functions") Set<Function> functions,
			@Param("discriminator") Class<?> discriminator);

	@Query("SELECT b FROM Booking b  join b.record r WHERE TYPE(r) =:discriminator AND CAST(b.from AS date) <= CAST(:endDate AS date) AND CAST(b.to AS date) >= CAST(:startDate AS date) ORDER BY b.from ASC")
	List<Booking> findByProjectAndStartDateAndEndDate(@Param("startDate") Calendar startDate,
			@Param("endDate") Calendar endDate, @Param("discriminator") Class<?> discriminator);

	@Query("SELECT b FROM Booking b WHERE TYPE(b) =:discriminator AND b.item IS NOT NULL AND b.item.id=:bookingId ")
	Booking findOrderLineByBooking(@Param("discriminator") Class<?> discriminator, @Param("bookingId") Long bookingId);

	@Query("SELECT b FROM Booking b  join b.record r WHERE TYPE(r) =:discriminator AND  TYPE(b.resource)= :resourceType AND CAST(b.from  AS date) <= CAST(:endDate  AS date) AND CAST(b.to  AS date) >= CAST(:startDate AS date) ORDER BY b.from ASC")
	List<Booking> findByProjectAndReosurceAndStartDateAndEndDate(@Param("startDate") Calendar startDate,
			@Param("endDate") Calendar endDate, @Param("discriminator") Class<?> discriminator,
			@Param("resourceType") Class<?> resourceType);

}
