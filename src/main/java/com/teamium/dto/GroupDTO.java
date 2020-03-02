package com.teamium.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.teamium.domain.Group;

public class GroupDTO {
	private Long id;
	private String name;
	private String colorTheme;
	private boolean dynamic;
	private Set<StaffMemberDTO> groupMembers = new HashSet<>();

	public GroupDTO() {

	}

	public GroupDTO(Group group) {
		this.id = group.getId();
		this.name = group.getName();
		this.colorTheme = group.getColorTheme();
		this.dynamic = group.isDynamic();
		this.groupMembers = group.getGroupMembers().stream().map(staff -> new StaffMemberDTO(staff, ""))
				.collect(Collectors.toSet());
	}

	public GroupDTO(Group group, String forStaffView) {
		this.id = group.getId();
		this.name = group.getName();
		this.colorTheme = group.getColorTheme();
		this.dynamic = group.isDynamic();
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
	public Set<StaffMemberDTO> getGroupMembers() {
		return groupMembers;
	}

	/**
	 * @param groupMembers the groupMembers to set
	 */
	public void setGroupMembers(Set<StaffMemberDTO> groupMembers) {
		this.groupMembers = groupMembers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GroupDTO other = (GroupDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GroupDTO [id=" + id + ", name=" + name + ", colorTheme=" + colorTheme + ", dynamic=" + dynamic
				+ ", groupMembers=" + groupMembers + "]";
	}

}
