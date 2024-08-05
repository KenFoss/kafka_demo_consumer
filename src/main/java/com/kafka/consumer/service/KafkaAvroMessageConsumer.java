package com.kafka.consumer.service;

import com.demo.kafka.config.avro.record.ProducerSync;
import org.apache.avro.generic.GenericRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaAvroMessageConsumer {

    @KafkaListener(topics = "producer-details", groupId = "consumer-group", containerFactory = "listenerContainerFactory")
    public void listen(GenericRecord message) {
        // Process the message
        System.out.println("Received message: " + message);
    }
}
