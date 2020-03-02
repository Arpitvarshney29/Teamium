package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamium.domain.EditionTemplateType;
import com.teamium.domain.SignatureHistory;
import com.teamium.domain.prod.Record;

@Repository
public interface SignatureHistoryRepository extends JpaRepository<SignatureHistory, Long> {

	List<SignatureHistory> findByRecordIdAndEditionTemplateTypeId(Long recordId, Long editionTemplateTypeId);
	
	List<SignatureHistory> findByRecordIdAndEditionTemplateTypeIdAndSigned(Long recordId, Long editionTemplateTypeId, boolean signed);
	
	List<SignatureHistory> findByRecordId(Long id);
}
