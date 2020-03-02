package com.teamium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.teamium.domain.Company;

@NoRepositoryBean
public interface AbstractCompanyRepository<T extends Company> extends JpaRepository<T, Long>{

	
}
