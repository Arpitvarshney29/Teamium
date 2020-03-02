package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.teamium.domain.prod.projects.invoice.Invoice;
import com.teamium.domain.prod.projects.invoice.InvoiceGeneration;

public interface InvoiceGenerationRepository extends JpaRepository<InvoiceGeneration, Long> {

	@Query("SELECT i from InvoiceGeneration i WHERE i.invoice IS NOT NULL AND i.invoice =:invoice")
	List<InvoiceGeneration> findInvoiceGenerationByInvoiceId(@Param("invoice") Invoice invoice);

	@Query("SELECT i from InvoiceGeneration i WHERE i.id IS NOT NULL AND i.id =:id")
	InvoiceGeneration findInvoiceGenerationById(@Param("id") long id);
}
