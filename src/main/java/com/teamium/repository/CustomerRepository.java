package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.teamium.domain.Channel;
import com.teamium.domain.prod.resources.contacts.Customer;
import java.lang.String;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	@Query("SELECT DISTINCT customer.address.city FROM Customer customer WHERE customer.address.city!=null")
	List<String> getClientCities();
	
	Customer findByName(String name);
	Customer findByNameIgnoreCase(String name);

	List<Customer> findCustomerByNameContainingIgnoreCase(String name);

}
