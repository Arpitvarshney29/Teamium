package com.teamium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamium.domain.prod.resources.staff.StaffMember;

/**
 * Repository related to logged in user operations.
 * 
 * @author TeamiumNishant
 *
 */
@Repository
public interface UserRepository extends JpaRepository<StaffMember, Long> {

	/**
	 * To find user by login and password.
	 * 
	 * @param login
	 * @param password
	 * @return
	 */
	@Query("SELECT sm FROM StaffMember sm WHERE sm.userSetting.login = :login and sm.userSetting.password =:password")
	public StaffMember findByUsernameAndPassword(@Param("login") String login, @Param("password") String password);

	/**
	 * To get user by login.
	 * 
	 * @param login
	 * @return
	 */
	@Query("SELECT sm FROM StaffMember sm WHERE sm.userSetting.login = :login")
	public StaffMember findByLogin(@Param("login") String login);

	
	/**
	 * To find staff-member by email
	 * 
	 * @param email
	 *            the email
	 * 
	 * @return the StaffMember.
	 */
	@Query("SELECT sm FROM StaffMember sm WHERE sm.userSetting.emails In email")
	StaffMember findByEmail(@Param("email") String email);

}
