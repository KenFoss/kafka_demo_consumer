package com.kafka.consumer.domain;

import static com.kafka.consumer.domain.ProducerSyncTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kafka.consumer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProducerSyncTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProducerSync.class);
        ProducerSync producerSync1 = getProducerSyncSample1();
        ProducerSync producerSync2 = new ProducerSync();
        assertThat(producerSync1).isNotEqualTo(producerSync2);

        producerSync2.setId(producerSync1.getId());
        assertThat(producerSync1).isEqualTo(producerSync2);

        producerSync2 = getProducerSyncSample2();
        assertThat(producerSync1).isNotEqualTo(producerSync2);
    }
}
