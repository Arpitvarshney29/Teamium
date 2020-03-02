package com.teamium.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamium.domain.prod.resources.staff.contract.ContractLine;

public interface ContractLineRepositiory extends JpaRepository<ContractLine, Long> {

}
