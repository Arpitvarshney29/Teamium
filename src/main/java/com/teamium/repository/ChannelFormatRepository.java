package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamium.domain.ChannelFormat;
import com.teamium.domain.Format;

@Repository
public interface ChannelFormatRepository extends JpaRepository<ChannelFormat, Long> {

	@Query("SELECT cf from ChannelFormat cf WHERE cf.format=:format AND cf.channel IS NOT NULL")
	List<ChannelFormat> findByFormat(@Param("format") Format format);

}
