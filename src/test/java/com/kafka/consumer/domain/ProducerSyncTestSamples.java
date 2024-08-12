package com.kafka.consumer.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProducerSyncTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProducerSync getProducerSyncSample1() {
        return new ProducerSync().id(1L).ownerName("ownerName1").productName("producerName1").quantity(1);
    }

    public static ProducerSync getProducerSyncSample2() {
        return new ProducerSync().id(2L).ownerName("ownerName2").productName("producerName2").quantity(2);
    }

    public static ProducerSync getProducerSyncRandomSampleGenerator() {
        return new ProducerSync()
            .id(longCount.incrementAndGet())
            .ownerName(UUID.randomUUID().toString())
            .productName(UUID.randomUUID().toString())
            .quantity(intCount.incrementAndGet());
    }
}
