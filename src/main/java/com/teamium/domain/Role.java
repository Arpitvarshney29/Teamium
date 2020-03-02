package com.teamium.domain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.exception.UnprocessableEntityException;

/**
 * Entity class for maintaining the roles to the users
 */
@Entity
@Table(name = "t_role")
public class Role {

	/**
	 * The Enum of available RoleName.
	 */
	public enum RoleName {

		Administrator("Administrator"), Finance("Finance"), Freelance("Freelance"), Human_Resources(
				"Human Resources"), Production("Production"), Production_Manager("Production Manager"), Program(
						"Program"), Sales("Sales"), Sales_Manager(
								"Sales Manager"), Support("Support"), EQUIPMENT_MANAGER("Equipment Manager");

		private String roleName;

		private RoleName(String roleName) {
			this.roleName = roleName;
		}

		/**
		 * Gets the qcType value.
		 *
		 * @return the qcType value
		 */
		public String getRoleNameString() {
			return this.roleName;
		}

		public static RoleName getEnum(String value) {
			for (RoleName v : values()) {
				if (v.getRoleNameString().equalsIgnoreCase(value)) {
					return v;
				}
			}
			throw new UnprocessableEntityException("Invalid Role name.");
		}

		/**
		 * To get list of roles
		 * 
		 * @return list of roles
		 */
		public static List<String> getRoles() {
			return Stream.of(values()).map(v -> v.getRoleNameString())
					.sorted((role1, role2) -> role1.toLowerCase().compareTo(role2.toLowerCase()))
					.collect(Collectors.toList());
		}
	}

	@Id
	@Column(name = "role_id", insertable = false, updatable = false)
	@TableGenerator(name = "idRole_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "role_idrole_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idRole_seq")
	private long roleId;

	/**
	 * Role name.
	 */
	@Column(name = "role_name")
	private String roleName;

	/**
	 * Role is active or not.
	 */
	private boolean active = true;

	public Role() {

	}

	/**
	 * @return the roleId
	 */
	public long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId
	 *            the roleId to set
	 */
	public void setRoleId(long roleId) {
		this.roleId = roleId;
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
}
