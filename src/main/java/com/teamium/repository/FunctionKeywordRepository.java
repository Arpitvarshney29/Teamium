package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamium.domain.FunctionKeyword;

/**
 * Repository interface for Function-Keyword Domain class to handle the all
 * related operation
 *
 */
@Repository
public interface FunctionKeywordRepository extends JpaRepository<FunctionKeyword, Long> {

	@Query("SELECT k from FunctionKeyword k")
	List<FunctionKeyword> findFunctionKeywords();

	@Query("SELECT k from FunctionKeyword k WHERE k.id =:id")
	List<FunctionKeyword> getFunctionKeywordById(@Param("id") Long id);

	@Query("SELECT k from FunctionKeyword k WHERE k.keyword =:keyword")
	FunctionKeyword getFunctionByKeyword(@Param("keyword") String keyword);

	FunctionKeyword findByKeywordIgnoreCase(String keyword);
}
