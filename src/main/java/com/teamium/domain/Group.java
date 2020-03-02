package com.teamium.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

import com.teamium.domain.prod.resources.staff.StaffMember;

@Entity
@Table(name = "t_group")
public class Group {

	/**
	 * Group ID
	 */
	@Id
	@Column(name = "c_group_id", insertable = false, updatable = false)
	@TableGenerator(name = "idGroup_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "group_idGroup_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idGroup_seq")
	private Long id;
	/**
	 * Name of the Group.
	 */
	@Column(name = "c_name", unique = true)
	@NotNull
	private String name;

	@Column(name = "c_color_theme")
	private String colorTheme;

	@Column(name = "c_dynamic")
	private boolean dynamic;

	/**
	 * SignatureRecipient
	 * 
	 * @see com.teamium.domain.StaffMember
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "t_group_member", joinColumns = @JoinColumn(name = "c_group_id"), inverseJoinColumns = @JoinColumn(name = "c_idperson"))
	private Set<StaffMember> groupMembers = new HashSet<>();

	public Group() {

	}

	public Group(Long id, String name, String colorTheme, boolean dynamic, Set<StaffMember> groupMembers) {
		if (id != null) {
			this.id = id;
		}
		this.name = name;
		this.colorTheme = colorTheme;
		this.dynamic = dynamic;
		this.groupMembers = groupMembers;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the colorTheme
	 */
	public String getColorTheme() {
		return colorTheme;
	}

	/**
	 * @param colorTheme the colorTheme to set
	 */
	public void setColorTheme(String colorTheme) {
		this.colorTheme = colorTheme;
	}

	/**
	 * @return the dynamic
	 */
	public boolean isDynamic() {
		return dynamic;
	}

	/**
	 * @param dynamic the dynamic to set
	 */
	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

	/**
	 * @return the groupMembers
	 */
	public Set<StaffMember> getGroupMembers() {
		return groupMembers;
	}

	/**
	 * @param groupMembers the groupMembers to set
	 */
	public void setGroupMembers(Set<StaffMember> groupMembers) {
		this.groupMembers = groupMembers;
	}

	public void addGroupMamber(StaffMember staffMember) {
		groupMembers.add(staffMember);
		staffMember.getGroups().add(this);
	}

	public void removeGroupMamber(StaffMember staffMember) {
		groupMembers.remove(staffMember);
		staffMember.getGroups().remove(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Group [id=" + id + ", name=" + name + ", colorTheme=" + colorTheme + ", dynamic=" + dynamic
				+ ", groupMembers=" + groupMembers + "]";
	}

}
