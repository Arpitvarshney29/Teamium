package com.teamium.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamium.domain.prod.resources.functions.DefaultResource;

public interface DefaultResourceRepository extends JpaRepository<DefaultResource, Long> {

}
