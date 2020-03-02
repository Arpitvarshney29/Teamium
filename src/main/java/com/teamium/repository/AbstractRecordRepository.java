package com.teamium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.teamium.domain.prod.Record;


@NoRepositoryBean
public interface AbstractRecordRepository<T extends Record> extends JpaRepository<T, Long> {

}
