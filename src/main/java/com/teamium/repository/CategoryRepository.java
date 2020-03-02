package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamium.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	Category findByName(String name);

	Category findByNameIgnoreCase(String name);

	@Query("SELECT DISTINCT c from Category c WHERE c.name !='' AND c.name IS NOT NULL GROUP BY c.id ORDER By c.name ASC")
	List<Category> getAllCategory();

	@Query("SELECT DISTINCT c.name FROM Category c WHERE c.name !='' AND c.name IS NOT NULL GROUP BY c.name ORDER By c.name ASC")
	List<String> getAllCategoryName();

}
