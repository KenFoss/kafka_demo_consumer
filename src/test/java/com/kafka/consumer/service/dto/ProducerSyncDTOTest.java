package com.kafka.consumer.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.kafka.consumer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProducerSyncDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProducerSyncDTO.class);
        ProducerSyncDTO producerSyncDTO1 = new ProducerSyncDTO();
        producerSyncDTO1.setId(1L);
        ProducerSyncDTO producerSyncDTO2 = new ProducerSyncDTO();
        assertThat(producerSyncDTO1).isNotEqualTo(producerSyncDTO2);
        producerSyncDTO2.setId(producerSyncDTO1.getId());
        assertThat(producerSyncDTO1).isEqualTo(producerSyncDTO2);
        producerSyncDTO2.setId(2L);
        assertThat(producerSyncDTO1).isNotEqualTo(producerSyncDTO2);
        producerSyncDTO1.setId(null);
        assertThat(producerSyncDTO1).isNotEqualTo(producerSyncDTO2);
    }
}
