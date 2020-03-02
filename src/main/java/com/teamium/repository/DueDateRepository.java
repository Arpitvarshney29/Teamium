package com.teamium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamium.domain.prod.DueDate;

@Repository
public interface DueDateRepository extends JpaRepository<DueDate, Long> {

}
