package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamium.domain.prod.upload.log.SpreadsheetUploadLog;

@Repository
public interface SpreadsheetUploadLogRepository extends JpaRepository<SpreadsheetUploadLog, Long> {

	@Query("SELECT log FROM SpreadsheetUploadLog log WHERE log.staffId =:staffId AND log.hasError = FALSE")
	List<SpreadsheetUploadLog> getSpreadsheetLogByStaff(@Param("staffId") Long staffId);
	
}
