package com.teamium.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.dto.StaffMemberDTO;

/**
 * Repository for staff member related operations.
 * 
 * @author TeamiumNishant
 *
 */
@Repository
public interface StaffMemberRepository extends JpaRepository<StaffMember, Long> {

	/**
	 * To get list of staffmember dto.
	 * 
	 * @return
	 */
	@Query("SELECT new com.teamium.dto.StaffMemberDTO(sm) FROM StaffMember sm")
	List<StaffMemberDTO> findStaffMembers();

	/**
	 * To get list locations of staff.
	 * 
	 * @return list of location
	 */
	@Query("SELECT s.address.city FROM StaffMember s WHERE s.address.city !='' AND s.address.city IS NOT NULL GROUP BY s.address.city ORDER BY s.address.city ASC")
	List<String> getLocations();

	/**
	 * To get staff from email
	 * 
	 * @param email
	 *            the email
	 * 
	 * @return staff
	 */
	@Query("SELECT sm FROM StaffMember sm join sm.userSetting us join us.emails staffemail WHERE :email IN(staffemail.email) and staffemail.email.primaryEmail = TRUE")
	StaffMember findByEmail(@Param("email") String email);

	/**
	 * To get staff from telephone
	 * 
	 * @param telephone
	 *            the telephone
	 * 
	 * @return staff
	 */
	@Query("SELECT sm FROM StaffMember sm join sm.userSetting us join us.telephones stafftelephone WHERE :telephone IN(stafftelephone.telephone)")
	List<StaffMember> findByTelephone(@Param("telephone") String telephone);

	/**
	 * To get staff from employeeCode
	 * 
	 * @param employeeCode
	 *            the employeeCode
	 * 
	 * @return staff
	 */
	StaffMember findByEmployeeCode(String employeeCode);

	/**
	 * To get list of sales staff-members
	 * 
	 * @return staff
	 */
	@Query("SELECT sm FROM StaffMember sm join sm.role roles WHERE :name IN(roles.roleName)")
	List<StaffMember> findAllSalesStaffMember(@Param("name") String name);

	/**
	 * To get count of available staffMember
	 * 
	 * @param available
	 * @return
	 */
	@Query("SELECT COUNT(staff) FROM StaffMember staff where staff.available = :available")
	Long findCountByAvailable(@Param("available") boolean available);

}
