package com.kafka.consumer.service;

import com.demo.schema.record.AvroProducerSync;
import com.kafka.consumer.service.dto.ProducerSyncDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaAvroMessageConsumer {

    ProducerSyncService producerSyncService;

    public KafkaAvroMessageConsumer(ProducerSyncService producerSyncService) {
        this.producerSyncService = producerSyncService;
    }

    @KafkaListener(topics = "producer-details", groupId = "consumer-group", containerFactory = "listenerContainerFactory")
    public void listen(ConsumerRecord<String, AvroProducerSync> message) {
        // Process the message
        System.out.println("Processing message: " + message);

        AvroProducerSync avroProducerSync = message.value();

        ProducerSyncDTO producerSync = new ProducerSyncDTO();
        producerSync.setId(avroProducerSync.getId());
        producerSync.setOwnerName(avroProducerSync.getOwnerName().toString());
        producerSync.setProductName(avroProducerSync.getProductName().toString());
        producerSync.setQuantity(avroProducerSync.getQuantity());

        producerSyncService.save(producerSync);
    }
}
