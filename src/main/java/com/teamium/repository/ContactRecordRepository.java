package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.teamium.domain.ContactRecord;
import com.teamium.domain.prod.projects.BookingEvent;

public interface ContactRecordRepository extends JpaRepository<ContactRecord, Long> {
	@Query("SELECT r from ContactRecord r WHERE r.id.idPerson = :personId AND r.id.idRecord=:recordId")
	List<BookingEvent> getByLine(@Param("personId")long personId,@Param("recordId")long recordId );

	
}
