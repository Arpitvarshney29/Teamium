package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.teamium.domain.prod.resources.ResourceFunction;
import com.teamium.domain.prod.resources.functions.Function;

public interface ResourceFunctionRepository extends JpaRepository<ResourceFunction, Long> {

	@Query("SELECT f from ResourceFunction f WHERE f.function.id =:functionId")
	List<ResourceFunction> getAssignedFunction(@Param("functionId") long functionId);
	
	@Query("SELECT f from ResourceFunction f WHERE f.function in :functions")
	List<ResourceFunction> getByFunctions(@Param("functions") List<Function> functions);
}
