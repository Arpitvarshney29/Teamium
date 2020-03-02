package com.teamium.dto;

import com.teamium.domain.Role;

/**
 * @author TeamiumNishant
 *
 */
public class RoleDTO {

	private Long id;
	/**
	 * Role name.
	 */

	private String roleName;

	/**
	 * Role is active or not.
	 */
	private boolean active = true;

	public RoleDTO() {

	}

	public RoleDTO(Role role) {

		this.id = role.getRoleId();
		this.roleName = role.getRoleName();
		this.active = role.isActive();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName
	 *            the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
public Role getRole() {
	Role role = new Role();
	role.setActive(this.active);
	role.setRoleName(this.roleName);
	role.setRoleId(this.id);
	return role;
}
}
