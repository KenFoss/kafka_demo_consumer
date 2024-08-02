package com.kafka.consumer.service.mapper;

import static com.kafka.consumer.domain.ProducerSyncAsserts.*;
import static com.kafka.consumer.domain.ProducerSyncTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProducerSyncMapperTest {

    private ProducerSyncMapper producerSyncMapper;

    @BeforeEach
    void setUp() {
        producerSyncMapper = new ProducerSyncMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProducerSyncSample1();
        var actual = producerSyncMapper.toEntity(producerSyncMapper.toDto(expected));
        assertProducerSyncAllPropertiesEquals(expected, actual);
    }
}
