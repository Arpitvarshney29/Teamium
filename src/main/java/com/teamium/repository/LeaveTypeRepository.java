package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.teamium.domain.LeaveType;

@Repository
public interface LeaveTypeRepository extends JpaRepository<LeaveType, Long> {

	@Query("SELECT lt from LeaveType lt WHERE LOWER(lt.type) =:type")
	LeaveType findLeaveTypeByName(@Param("type") String type);

	@Query("SELECT lt from LeaveType lt WHERE lt.active =:active")
	List<LeaveType> findAllLeaveType(@Param("active") boolean active);
}
