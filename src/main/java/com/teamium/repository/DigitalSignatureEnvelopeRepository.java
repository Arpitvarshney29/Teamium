package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamium.domain.DigitalSignatureEnvelope;
import com.teamium.domain.EditionTemplateType;
import com.teamium.dto.DigitalSignatureEnvelopeDTO;

@Repository
public interface DigitalSignatureEnvelopeRepository extends JpaRepository<DigitalSignatureEnvelope, Long> {

	
	@Query("SELECT new com.teamium.dto.DigitalSignatureEnvelopeDTO(ev) FROM DigitalSignatureEnvelope ev")
	List<DigitalSignatureEnvelopeDTO> findAllDigitalSignatureEnvelopes();
	
	DigitalSignatureEnvelope findByNameOrTemplateTypeId(String name,Long id);
	
	DigitalSignatureEnvelope findByTemplateTypeId(Long id);	
}
