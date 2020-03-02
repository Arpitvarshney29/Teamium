package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamium.domain.CurrencySymbol;
import com.teamium.dto.CurrencyDTO;

@Repository
public interface CurrencySymbolRepository extends JpaRepository<CurrencySymbol, Long> {

	CurrencySymbol findByCodeIgnoreCase(String code);

	@Query("SELECT new com.teamium.dto.CurrencyDTO(cur) from CurrencySymbol cur WHERE cur.code IS NOT NULL ORDER By cur.code ASC")
	List<CurrencyDTO> getAllCurrency();

	@Query("SELECT new com.teamium.dto.CurrencyDTO(cur) from CurrencySymbol cur WHERE cur.active=:active AND cur.code IS NOT NULL ORDER By cur.code ASC")
	List<CurrencyDTO> getAllCurrencyByActive(@Param("active") boolean active);

	@Query("SELECT DISTINCT c.code FROM CurrencySymbol c WHERE c.code !='' AND c.code IS NOT NULL AND c.active=:active GROUP BY c.code ORDER By c.code ASC")
	List<String> getAllCurrencyCodeByActive(@Param("active") boolean active);
	
	@Query("SELECT cur from CurrencySymbol cur where cur.defaultCurrency = true")
	CurrencySymbol getDefaultCurrency();
	
	List<CurrencySymbol> findByIdNotIn(List<Long> currencySymbolIdList);

}
