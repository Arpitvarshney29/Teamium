package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamium.domain.MilestoneType;

@Repository
public interface MilestoneTypeRepository extends JpaRepository<MilestoneType, Long> {

	MilestoneType findByName(String name);

	MilestoneType findByNameIgnoreCase(String name);
	
	MilestoneType findByDiscriminatorAndNameIgnoreCase(String discriminator, String name);

	MilestoneType findByIdAndDiscriminator(Long id, String discriminator);

	@Query("SELECT DISTINCT f from MilestoneType f WHERE f.discriminator=:discriminator AND f.name !='' AND f.name IS NOT NULL GROUP BY f.id ORDER By f.name ASC")
	List<MilestoneType> getAllMilestoneType(@Param("discriminator") String discriminator);

	@Query("SELECT DISTINCT f.name FROM MilestoneType f WHERE f.discriminator=:discriminator AND f.name !='' AND f.name IS NOT NULL GROUP BY f.name ORDER By f.name ASC")
	List<String> getAllMilestoneTypeName(@Param("discriminator") String discriminator);

}
