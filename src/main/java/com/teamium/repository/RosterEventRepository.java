package com.teamium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.teamium.domain.prod.projects.planning.roster.RosterEvent;

@Repository
public interface RosterEventRepository extends JpaRepository<RosterEvent, Long> {

}
