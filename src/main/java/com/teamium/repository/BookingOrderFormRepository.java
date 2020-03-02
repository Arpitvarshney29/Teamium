package com.teamium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamium.domain.prod.projects.order.BookingOrderForm;

@Repository
public interface BookingOrderFormRepository extends JpaRepository<BookingOrderForm, Long> {
 
	BookingOrderForm findByFormType(String formType);
}
