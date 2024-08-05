package com.kafka.consumer.config;

//import com.kafka.consumer.config.avro.record.ProducerSync;
import com.demo.kafka.config.avro.record.ProducerSync;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import java.util.Map;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.Deserializer;

public class CustomAvroDeserializer implements Deserializer<ProducerSync> {

    private final KafkaAvroDeserializer inner;

    public CustomAvroDeserializer() {
        this.inner = new KafkaAvroDeserializer();
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        inner.configure(configs, isKey);
    }

    @Override
    public ProducerSync deserialize(String topic, byte[] data) {
        GenericRecord record = (GenericRecord) inner.deserialize(topic, data);
        if (record == null) {
            return null;
        }

        return new ProducerSync(
            (Long) record.get("id"),
            record.get("ownerName").toString(),
            record.get("productName").toString(),
            (Integer) record.get("quantity")
        );
    }

    @Override
    public void close() {
        inner.close();
    }
}
