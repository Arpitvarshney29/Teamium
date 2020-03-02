package com.teamium.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamium.domain.UserLeaveRecord;

public interface UserLeaveRecordRepository extends JpaRepository<UserLeaveRecord, Long> {

}
