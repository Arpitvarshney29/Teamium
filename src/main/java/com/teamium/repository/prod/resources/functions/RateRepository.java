package com.teamium.repository.prod.resources.functions;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamium.domain.prod.resources.functions.Rate;
import com.teamium.dto.prod.resources.functions.RateDTO;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {

	@Query("SELECT new com.teamium.dto.prod.resources.functions.RateDTO(rate) FROM Rate rate")
	List<RateDTO> getRates();

	@Query("SELECT new com.teamium.dto.prod.resources.functions.RateDTO(rate) FROM Rate rate WHERE rate.company.id=:companyId")
	List<RateDTO> findByCompanyId(@Param("companyId") Long companyId);

	@Query("SELECT new com.teamium.dto.prod.resources.functions.RateDTO(rate) FROM Rate rate WHERE rate.function.id=:functionId")
	List<RateDTO> findByFunctionId(@Param("functionId") Long functionId);

	@Query("SELECT rate FROM Rate rate WHERE rate.function.id=:functionId")
	List<Rate> findRateByFunction(@Param("functionId") Long functionId);

	@Query("SELECT count(rate) FROM Rate rate WHERE rate.function.id=:functionId AND rate.unit.key=:unit ")
	int countRateByFunctionAndUnit(@Param("functionId") Long functionId, @Param("unit") String unit);

	@Query("SELECT rate FROM Rate rate WHERE rate.company.id=:companyId AND rate.unit.key=:unit ")
	List<Rate> findByCompanyIdAndUnit(@Param("companyId") Long companyId, @Param("unit") String unit);

	@Query("SELECT rate FROM Rate rate WHERE rate.function.id =:functionId AND rate.company.id =:companyId AND rate.archived != TRUE")
	List<Rate> findRateByFunctionAndCompany(@Param("functionId") Long functionId, @Param("companyId") Long companyId);

	@Query("SELECT new com.teamium.dto.prod.resources.functions.RateDTO(rate) FROM Rate rate WHERE rate.entity IS NOT NULL AND rate.entity.id=:saleEntityId")
	List<RateDTO> findRateBySaleEntityId(@Param("saleEntityId") Long saleEntityId);

	List<Rate> findByCurrencyIgnoreCase(String currency);

}
