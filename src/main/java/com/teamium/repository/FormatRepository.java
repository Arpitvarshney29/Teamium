package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamium.domain.Format;

@Repository
public interface FormatRepository extends JpaRepository<Format, Long> {

	Format findByName(String name);

	Format findByNameIgnoreCase(String name);

	@Query("SELECT DISTINCT f from Format f WHERE f.name !='' AND f.name IS NOT NULL GROUP BY f.id ORDER By f.name ASC")
	List<Format> getAllFormats();

	@Query("SELECT DISTINCT f.name FROM Format f WHERE f.name !='' AND f.name IS NOT NULL GROUP BY f.name ORDER By f.name ASC")
	List<String> getAllFormatsName();

}
