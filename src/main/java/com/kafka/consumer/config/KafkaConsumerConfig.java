package com.kafka.consumer.config;

import com.demo.schema.record.AvroProducerSync;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value(value = "${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value(value = "${spring.kafka.consumer.properties.schema.registry.url}")
    private String schemaRegistryUrl;

    private Map<String, Object> props = new HashMap<>();

    @Bean
    public ConsumerFactory<String, AvroProducerSync> consumerFactory() {
        this.props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        this.props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        this.props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        this.props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        this.props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        this.props.put("schema.registry.url", schemaRegistryUrl);
        //        this.props.put("specific.avro.reader", "true"); THIS LINE MAKES IT REQUIRE AVROPRODUCERSYNC TO BE IN SAME CLASSPATH AS PRODUCER (so com.demo.kafka)
        // using will result in generator putting Avro.ProducerSunc outside of com.kafka.consumer
        return new DefaultKafkaConsumerFactory<>(this.props);
    }

    public Map<String, Object> getProps() {
        return this.props;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, AvroProducerSync>> listenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AvroProducerSync> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
