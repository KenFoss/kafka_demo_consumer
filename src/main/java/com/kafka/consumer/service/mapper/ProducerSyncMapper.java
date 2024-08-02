package com.kafka.consumer.service.mapper;

import com.kafka.consumer.domain.ProducerSync;
import com.kafka.consumer.service.dto.ProducerSyncDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProducerSync} and its DTO {@link ProducerSyncDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProducerSyncMapper extends EntityMapper<ProducerSyncDTO, ProducerSync> {}
