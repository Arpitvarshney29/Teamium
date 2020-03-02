package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamium.domain.MilestoneType;
import com.teamium.domain.prod.resources.equipments.Equipment;
import com.teamium.dto.EquipmentDTO;

/**
 * * @author Nishant-Teamium
 *
 */
@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

	@Query("SELECT new com.teamium.dto.EquipmentDTO(e) FROM Equipment e order by e.id ASC")
	public List<EquipmentDTO> findEquipments();

	@Query("SELECT DISTINCT e.brand FROM Equipment e WHERE e.brand !='' AND e.brand IS NOT NULL GROUP BY e.brand ORDER By e.brand ASC")
	List<String> getBrands();

	@Query("SELECT DISTINCT e.type FROM Equipment e WHERE e.type !='' AND e.type IS NOT NULL GROUP BY e.type ORDER By e.type ASC")
	List<String> getTypes();

	@Query("SELECT DISTINCT e.model FROM Equipment e WHERE e.model !='' AND e.model IS NOT NULL GROUP BY e.model ORDER By e.model ASC")
	List<String> getModels();

	@Query("SELECT DISTINCT e.reference FROM Equipment e WHERE e.reference !='' AND e.reference IS NOT NULL GROUP BY e.reference ORDER By e.reference ASC")
	List<String> getReference();

	@Query("SELECT e.location FROM Equipment e WHERE e.location !='' AND e.location IS NOT NULL GROUP BY e.location ORDER By e.location ASC")
	List<String> getLocations();

	Equipment findBySerialNumberContainingIgnoreCase(String serialnumber);

	@Query("SELECT eq FROM Equipment eq join eq.milestones milestoneList WHERE :dueDate IN (milestoneList.milestoneType)")
	List<Equipment> findEquipmentByMilestone(@Param("dueDate") MilestoneType dueDate);

	@Query("SELECT COUNT(eq) FROM Equipment eq where eq.available = :available")
	Long findCountByAvailable(@Param("available") boolean available);

	@Query("SELECT e FROM Equipment e WHERE e.name = :name")
	List<Equipment> findByNameContainingIgnoreCase(@Param("name") String name);

}
