package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamium.domain.Company;
import com.teamium.dto.CompanyDTO;


/**
 * Repository interface for Company Domain class to handle the all related
 * operation
 *
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

	@Query("SELECT new com.teamium.dto.CompanyDTO(c) FROM Company c")
	List<CompanyDTO> findCompanies();
	
	@Query("SELECT c from Company c WHERE TYPE(c) =:discriminator")
	List<Company> getCompanies(@Param("discriminator") Class<?> discriminator);

	@Query("SELECT c from Company c WHERE TYPE(c) =:discriminator and c.id =:id")
	Company getCompanyById(@Param("discriminator") Class<?> discriminator, @Param("id") Long id);
	
	List<Company> findByCurrencyIgnoreCase(String currency);

}
