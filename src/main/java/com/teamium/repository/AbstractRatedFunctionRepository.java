package com.teamium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.teamium.domain.prod.resources.functions.RatedFunction;
/**
 * The AbstractRatedFunction Repository
 * 
 *
 * @param <T>
 */

@NoRepositoryBean
public interface AbstractRatedFunctionRepository<T extends RatedFunction> extends JpaRepository<T,Long>{

}
