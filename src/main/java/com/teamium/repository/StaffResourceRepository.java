package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamium.domain.prod.resources.Resource;
import com.teamium.domain.prod.resources.staff.StaffResource;

/**
 * Repository for Staff Resource related operations.
 * 
 * @author wittybrains
 *
 */
@Repository
public interface StaffResourceRepository extends JpaRepository<StaffResource, Long> {

	/**
	 * To find list of StaffResource's groups.
	 * 
	 * @return the list of StaffResource objects
	 */
	@Query("SELECT DISTINCT sr FROM StaffResource sr WHERE sr.entity IS NULL")
	List<StaffResource> findGroups();

	/**
	 * To get StaffResource by group name.
	 * 
	 * @return the StaffResource object
	 */
	@Query("SELECT sr FROM StaffResource sr WHERE sr.entity IS NULL and sr.name =:name ")
	StaffResource findByGroupName(@Param("name") String name);

	@Query("SELECT sr FROM StaffResource sr WHERE sr.parent = :parent")
	List<StaffResource> findChildren(@Param("parent") Resource<?> parent);

	 /**
	 * To find list of StaffResource's groups.
	 *
	 * @return the list of StaffResource objects
	 */
	 @Query("SELECT DISTINCT sr FROM StaffResource sr WHERE sr.entity IS NULL AND sr.name IS NOT NULL ORDER By sr.name ASC")
	 List<StaffResource> getAllGroups();
	 
}
