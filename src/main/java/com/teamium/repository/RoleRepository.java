package com.teamium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.teamium.domain.Role;

/**
 * Repository related to role operations.
 * 
 * @author TeamiumNishant
 *
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	/**
	 * Find role by name.
	 * 
	 * @param name
	 *            the name.
	 * 
	 * @return the Role.
	 */
	Role findByRoleName(String roleName);

	/**
	 * Find Role by name and active.
	 * 
	 * @param name
	 * @param active
	 * @return
	 */
	Role findByRoleNameAndActive(String roleName, boolean active);

}
