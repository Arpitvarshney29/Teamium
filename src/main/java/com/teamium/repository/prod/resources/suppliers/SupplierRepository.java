package com.teamium.repository.prod.resources.suppliers;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamium.domain.prod.resources.contacts.Customer;
import com.teamium.domain.prod.resources.suppliers.Supplier;
import com.teamium.repository.AbstractCompanyRepository;

/**
 * Repository interface for Supplier Domain class to handle the all related
 * operation
 *
 */

@Repository
public interface SupplierRepository extends AbstractCompanyRepository<Supplier> {

	@Query("SELECT supplier FROM Supplier supplier")
	List<Supplier> getSuppliers();

	@Query("SELECT DISTINCT supplier.address.city FROM Supplier supplier WHERE supplier.address.city!=null")
	List<String> getSuppliersCities();

	Supplier findByNameIgnoreCase(String name);

	List<Supplier> findSupplierByNameContainingIgnoreCase(String name);

}
