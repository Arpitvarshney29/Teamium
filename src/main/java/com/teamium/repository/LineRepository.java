package com.teamium.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.teamium.domain.prod.Line;

@Repository
public interface LineRepository extends JpaRepository<Line, Long> {

	@Query("SELECT line from Line line join line.function function WHERE function.id =:functionId")
	List<Line> getLinesRelatedToFunction(@Param("functionId") long functionId);
	
	List<Line> findByPersistentCurrencyIgnoreCase(String persistentCurrency);

}
