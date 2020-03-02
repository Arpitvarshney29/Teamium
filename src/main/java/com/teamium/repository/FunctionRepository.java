package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamium.domain.prod.resources.functions.Function;
import com.teamium.domain.prod.resources.functions.RatedFunction;
import com.teamium.dto.FunctionDTO;

@Repository
public interface FunctionRepository extends JpaRepository<Function, Long> {

	/**
	 * To get list of function
	 * 
	 * @return List of all FunctionDTOs
	 */
	@Query("SELECT new com.teamium.dto.FunctionDTO(function) from Function function)")
	List<FunctionDTO> getFunctions();

	/**
	 * To get list of child functions by function id
	 * 
	 * @param the functionId
	 * 
	 * @return List of all Function
	 */
	@Query("SELECT function from Function function WHERE function.parent.id=:functionId)")
	List<Function> getChildFunctions(@Param("functionId") Long functionId);

	/**
	 * To get Function by discriminator and functionId
	 * 
	 * @param discriminator
	 * @param id
	 * 
	 * @return Function object
	 */
	@Query("SELECT f from Function f WHERE TYPE(f) =:discriminator and f.id =:id")
	Function getFunctionByIdAndDiscriminator(@Param("discriminator") Class<?> discriminator, @Param("id") Long id);

	/**
	 * To get functions by discriminator
	 * 
	 * @param discriminator the discriminator
	 * 
	 * @return list of functions
	 */
	@Query("SELECT f from Function f WHERE TYPE(f) =:discriminator")
	List<Function> getFunctionByAndDiscriminator(@Param("discriminator") Class<?> discriminator);

	/**
	 * To get FunctionDTOs by discriminator
	 * 
	 * @param discriminator the discriminator
	 * 
	 * @return list of FunctionDTOs
	 */
	@Query("SELECT new com.teamium.dto.FunctionDTO(f) from Function f WHERE TYPE(f) =:discriminator ORDER By f.qualifiedName ASC")
	List<FunctionDTO> getFunctionDTOsByDiscrimination(@Param("discriminator") Class<?> discriminator);

	/**
	 * To get all ratedFunctions
	 * 
	 * @return list of RatedFunction
	 */
	@Query("SELECT f from RatedFunction f WHERE TYPE(f) !='function'")
	List<RatedFunction> getAllRatedFunctions();

	/**
	 * To get RatedFunction by functionId
	 * 
	 * @param id
	 * 
	 * @return RatedFunction object
	 */
	@Query("SELECT f from Function f WHERE f.id =:id")
	RatedFunction getRatedFunctionById(@Param("id") Long id);

	/**
	 * To get Function by value
	 * 
	 * @param value
	 * 
	 * @return Function object
	 */
//	@Query("SELECT f from Function f WHERE LOWER(f.value)=:value")
	Function getFunctionByValue(@Param("value") String value);

	@Query("SELECT f from Function f WHERE TYPE(f) =:discriminator AND LOWER(f.value)=:value")
	Function getFunctionByDiscriminatorAndValue(@Param("discriminator") Class<?> discriminator, @Param("value") String value);
	
	List<Function> findFunctionByValueContainingIgnoreCase(String value);
	
	Function findByValueIgnoreCase(String value);

	List<Function> findByFunctionKeywordId(Long id);
	
}
