package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamium.domain.DocumentType;
import com.teamium.domain.Person;
import com.teamium.domain.PersonalDocument;
import com.teamium.domain.prod.projects.Project;

@Repository
public interface PersonalDocumentRepository extends JpaRepository<PersonalDocument, Long> {

	public PersonalDocument findByDocumentPersonAndType(Person documentPerson, DocumentType documentType);

	List<PersonalDocument> findByType(DocumentType documentType);

	@Query("SELECT d from PersonalDocument d WHERE d.type=:documentType AND d.documentPerson IS NOT NULL")
	List<PersonalDocument> findByTypeAndPerson(@Param("documentType") DocumentType documentType);

}
