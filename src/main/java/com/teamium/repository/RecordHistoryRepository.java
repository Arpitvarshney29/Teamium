package com.teamium.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamium.domain.RecordHistory;

@Repository
public interface RecordHistoryRepository extends JpaRepository<RecordHistory, Long> {
	@Query("SELECT r from RecordHistory r WHERE r.record.id =:recordId")
	RecordHistory getRecordHistoryByRecordId(@Param("recordId") Long recordId);

	@Modifying
	@Query("Delete from RecordHistory r WHERE r.record.id =:recordId")
	void deleteRecordHistoryByRecordId(@Param("recordId") Long recordId);
}
