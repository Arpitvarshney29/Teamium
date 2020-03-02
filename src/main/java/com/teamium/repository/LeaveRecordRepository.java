package com.teamium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamium.domain.LeaveRecord;

@Repository
public interface LeaveRecordRepository extends JpaRepository<LeaveRecord, Long> {

}
