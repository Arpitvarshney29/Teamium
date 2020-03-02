package com.teamium.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.teamium.domain.UserBooking;

@Repository
public interface UserBookinRepository extends JpaRepository<UserBooking, Long> {

	/**
	 * To get user by login.
	 * 
	 * @param login
	 * @return
	 */
	@Query("SELECT ub FROM UserBooking ub join ub.staffMember sm WHERE sm.id = :staffMemberId")
	public List<UserBooking> findAllUserBookingByStaffMemberId(@Param("staffMemberId") Long staffMemberId);

	/**
	 * To get user by login.
	 * 
	 * @param login
	 * @return
	 */
	@Query("SELECT ub FROM UserBooking ub WHERE ub.bookingId = :bookingId")
	public UserBooking findUserBookingByBookingId(@Param("bookingId") Long bookingId);

	@Query("SELECT ub FROM UserBooking ub join ub.staffMember sm WHERE (ub.userStartTime<=:endDate AND ub.userEndTime>=:startDate) AND sm.id=:staffMemberId")
	public List<UserBooking> findUserBookingByDate(@Param("staffMemberId") long staffMemberid,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
