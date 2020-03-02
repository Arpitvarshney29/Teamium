package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamium.domain.prod.resources.ResourceInformation;

@Repository
public interface ResourceInformationRepository extends JpaRepository<ResourceInformation, Long> {

	@Query("SELECT rf from ResourceInformation rf WHERE rf.resource IS NOT NULL AND rf.keywordValue=:keywordValue AND rf.keyValue=:keyValue")
	List<ResourceInformation> findByKeywordValueAndkeyValue(@Param("keywordValue") String keywordValue,
			@Param("keyValue") String keyValue);

}
