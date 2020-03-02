package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamium.domain.Group;
import com.teamium.dto.GroupDTO;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

	Group findByName(String name);
	
	@Query("SELECT new com.teamium.dto.GroupDTO(g) FROM Group g")
	List<GroupDTO>  findAllGroups();
	
}
