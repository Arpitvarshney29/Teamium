package com.teamium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamium.domain.prod.resources.staff.Reel;
import com.teamium.domain.prod.resources.staff.StaffMember;
import java.util.List;

@Repository
public interface ReelRepository extends JpaRepository<Reel, Long> {

	List<Reel> findByStaff(StaffMember staff);
}
