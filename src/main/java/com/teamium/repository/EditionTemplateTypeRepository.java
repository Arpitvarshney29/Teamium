package com.teamium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamium.domain.EditionTemplateType;

@Repository
public interface EditionTemplateTypeRepository extends JpaRepository<EditionTemplateType, Long> {
	
	EditionTemplateType findByTemplateNameIgnoreCase(String templateName);

}
