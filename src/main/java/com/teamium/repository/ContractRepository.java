package com.teamium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamium.domain.prod.resources.staff.contract.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

}
