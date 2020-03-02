package com.teamium.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamium.domain.Group;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.dto.GroupDTO;
import com.teamium.dto.StaffMemberDTO;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnauthorizedException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.GroupRepository;

/**
 * Service to manage group configuration
 * 
 * @author Nishant
 *
 */
@Service
public class GroupService {

	private GroupRepository groupRepository;
	private StaffMemberService staffMemberService;
	private AuthenticationService authenticationService;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public GroupService(GroupRepository groupRepository, StaffMemberService staffMemberService,
			AuthenticationService authenticationService) {
		this.groupRepository = groupRepository;
		this.staffMemberService = staffMemberService;
		this.authenticationService = authenticationService;
	}

	/**
	 * To save or update GroupDTO
	 * 
	 * @param groupDTO
	 * @return GroupDTO
	 */
	public GroupDTO saveOrUpdate(GroupDTO groupDTO) {
		logger.info(" Inside GroupService: saveOrUpdateGroup GroupDTO: " + groupDTO.toString());
		if (!authenticationService.isAdmin()) {
			logger.warn("Invalid role");
			throw new UnauthorizedException("Invalid role");
		}
		validateGroupDTO(groupDTO);
		Group group = null;
		Group groupByName = groupRepository.findByName(groupDTO.getName());
		if (groupDTO.getId() != null) {
			group = findById(groupDTO.getId());
			if (groupByName != null && (group.getId() != groupByName.getId())) {
				logger.error("Group already exist with given name.");
				throw new UnprocessableEntityException("Group already exist with given name.");
			}
		} else if (groupByName != null) {
			logger.error("Group already exist with given name.");
			throw new UnprocessableEntityException("Group already exist with given name.");
		}

		Set<StaffMember> members = validateGroupMembers(groupDTO.getGroupMembers(),
				group == null ? null : group.getGroupMembers());
		group = new Group(groupDTO.getId(), groupDTO.getName(), groupDTO.getColorTheme(), groupDTO.isDynamic(),
				members);

		return new GroupDTO(groupRepository.save(group));

	}

	/**
	 * To add staff member on a group.
	 * 
	 * @param groupId
	 * @param staffMemberId
	 * @return GroupDTO
	 */
	public GroupDTO addStaffMemberToGroup(long groupId, long staffMemberId) {
		logger.info(
				" Inside GroupService: addStaffMemberToGroup groupId: " + groupId + "staffMemberId:" + staffMemberId);
		Group group = findById(groupId);
		StaffMember staffMember = staffMemberService.findById(staffMemberId);
		group.addGroupMamber(staffMember);
		logger.info(" Returning from GroupService: addStaffMemberToGroup groupId: " + groupId + "staffMemberId:"
				+ staffMemberId);
		return new GroupDTO(groupRepository.save(group));

	}

	/**
	 * To remove a staff member from group.
	 * 
	 * @param groupId
	 * @param staffMemberId
	 * @return GroupDTO
	 */
	public GroupDTO removeStaffMemberFromGroup(long groupId, long staffMemberId) {
		logger.info(" Inside GroupService: removeStaffMemberFromGroup groupId: " + groupId + "staffMemberId:"
				+ staffMemberId);
		Group group = findById(groupId);
		StaffMember staffMember = staffMemberService.findById(staffMemberId);
		group.removeGroupMamber(staffMember);
		logger.info(" Returning from GroupService: removeStaffMemberFromGroup groupId: " + groupId + "staffMemberId:"
				+ staffMemberId);
		return new GroupDTO(groupRepository.save(group));

	}

	/**
	 * To get list of GroupDTO.
	 * 
	 * @return list of GroupDTO
	 */
	public List<GroupDTO> getAllGroups() {
		return groupRepository.findAllGroups();

	}

	/**
	 * To validate and handle group members.
	 * 
	 * @param staffMemberDTOs
	 * @param dbGroupMembers
	 * @return
	 */
	private Set<StaffMember> validateGroupMembers(Set<StaffMemberDTO> staffMemberDTOs,
			Set<StaffMember> dbGroupMembers) {
		logger.info(" Inside GroupService: validateGroupMembers GroupDTO: ");
		AtomicBoolean isRemove = new AtomicBoolean(false);

		if (dbGroupMembers != null) {
			Set<StaffMember> members = new HashSet<>();
			Set<StaffMember> dbLines = dbGroupMembers;

			if (staffMemberDTOs != null && !staffMemberDTOs.isEmpty()) {
				members = staffMemberDTOs.stream().map(dto -> {
					// removing members not present in dto
					if (dbLines != null && !dbLines.isEmpty()) {
						dbLines.removeIf(dt -> {
							isRemove.set(true);
							staffMemberDTOs.forEach(dt1 -> {
								if (dt1.getId() != null && dt.getId() != null && dt1.getId().equals(dt.getId())) {
									isRemove.set(false);
									return;
								}
							});
							return isRemove.get();
						});
					}
					StaffMember staffMember = staffMemberService.findById(dto.getId());
					if (staffMember == null) {
						logger.error("Invalid group member.With id:" + dto.getId());
						throw new UnprocessableEntityException("Invalid group member.");
					}
					return staffMember;

				}).collect(Collectors.toSet());

			}
			return members;
		} else {
			Set<StaffMember> groupMenbers = new HashSet<>();
			staffMemberDTOs.forEach(member -> {

				StaffMember staffMember = staffMemberService.findById(member.getId());
				if (staffMember == null) {
					throw new UnprocessableEntityException("Invalid group member");
				} else {
					groupMenbers.add(staffMember);
				}
			});
			return groupMenbers;
		}
	}

	/**
	 * To validate GroupDTO
	 * 
	 * @param GroupDTO
	 */
	private void validateGroupDTO(GroupDTO groupDTO) {
		logger.info(" Inside GroupService: validateGroupDTO GroupDTO: " + groupDTO.toString());
		if (StringUtils.isBlank(groupDTO.getName())) {
			logger.error("Group name can not be empty.");
			throw new UnprocessableEntityException("Group name can not be empty.");
		}

		if (groupDTO.getGroupMembers().isEmpty()) {
			logger.error("Please add atleast one group member");
			throw new UnprocessableEntityException("Please add atleast one group member.");
		}
		logger.info("Returning after validating groupDTO");
	}

	/**
	 * To delete Group by id.
	 * 
	 * @param id
	 */
	public void deleteGroupById(long id) {
		logger.info("Inside GroupService: deleteGroupById:" + id);
		if (!authenticationService.isAdmin()) {
			logger.warn("Invalid role");
			throw new UnauthorizedException("Invalid role");
		}
		findById(id);
		groupRepository.delete(id);
		logger.info("Successfully deleted");

	}

	/**
	 * To find Group by id.
	 * 
	 * @param id
	 * @return Group
	 */
	public Group findById(long id) {
		logger.info("Inside GroupService: findById:" + id);
		Group group = groupRepository.findOne(id);
		if (group == null) {
			logger.error("Invalid Group id.");
			throw new NotFoundException("Invalid Group id.");
		}
		return group;
	}

	/**
	 * To find GroupDTO by id.
	 * 
	 * @param id
	 * @return GroupDTO
	 */
	public GroupDTO findGroupDTOById(long id) {
		logger.info("Inside GroupService: findGroupDTOById:" + id);
		GroupDTO groupDTO = new GroupDTO(findById(id));
		logger.info("Returning after geting GroupDTO by id");
		return groupDTO;
	}

	/**
	 * To get Group by name.
	 * 
	 * @param name
	 * @return
	 */
	public Group findByName(String name) {
		if (StringUtils.isBlank(name)) {
			throw new UnprocessableEntityException("Name can not be empty or null");
		}
		return groupRepository.findByName(name);
	}
}
