package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamium.domain.DocumentType;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {

	DocumentType findByType(String type);

	DocumentType findByTypeIgnoreCase(String type);

	@Query("SELECT DISTINCT d from DocumentType d WHERE d.type !='' AND d.type IS NOT NULL GROUP BY d.id ORDER By d.type ASC")
	List<DocumentType> getAllDocumentType();

	@Query("SELECT DISTINCT d.type FROM DocumentType d WHERE d.type !='' AND d.type IS NOT NULL GROUP BY d.type ORDER By d.type ASC")
	List<String> getAllDocumentTypeName();

}
