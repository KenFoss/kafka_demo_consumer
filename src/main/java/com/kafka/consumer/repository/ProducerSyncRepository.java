package com.kafka.consumer.repository;

import com.kafka.consumer.domain.ProducerSync;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProducerSync entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProducerSyncRepository extends JpaRepository<ProducerSync, Long> {}
