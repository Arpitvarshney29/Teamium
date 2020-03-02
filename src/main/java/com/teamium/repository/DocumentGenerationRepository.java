package com.teamium.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamium.domain.prod.docsign.DocumentGeneration;

public interface DocumentGenerationRepository extends JpaRepository<DocumentGeneration, Long>{

}
