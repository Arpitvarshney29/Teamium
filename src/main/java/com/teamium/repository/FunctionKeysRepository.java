package com.teamium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamium.domain.FunctionKeys;

@Repository
public interface FunctionKeysRepository extends JpaRepository<FunctionKeys, Long> {

}
