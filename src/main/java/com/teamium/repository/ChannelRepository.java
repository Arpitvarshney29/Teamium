package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamium.domain.Channel;
import com.teamium.domain.Format;


/**
 * Repository interface for Channel Domain class to handle the all related
 * operation
 *
 */
@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {

	Channel findByNameIgnoreCase(String name);
	
	@Query("SELECT c FROM Channel c join c.formats chFormat WHERE :format IN (chFormat)")
	List<Channel> findChannelByFormatName(@Param("format") Format format);
	
}
