package com.teamium.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.teamium.domain.prod.resources.equipments.EquipmentResource;

@Repository
public interface PackageRepository extends JpaRepository<EquipmentResource, Long> {

	/**
	 * The named query used to find children available to be add as a children.
	 */
	@Query("SELECT er FROM EquipmentResource er WHERE er.parent IS NULL AND er.entity IS NOT NULL ORDER By er.name ASC")
	List<EquipmentResource> findEquipmentResourcesForPackage();

	/**
	 * The named query used to find children of the equipment given in parameter
	 * 
	 * @param 1
	 *            The EquipmentResource
	 */
	@Query("SELECT er FROM EquipmentResource er WHERE er.parent = :parent ORDER By er.name ASC")
	List<EquipmentResource> findEquipmentResourcesForSelectedPackage(@Param("parent") EquipmentResource parent);
}
