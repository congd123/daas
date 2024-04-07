package model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import org.apache.flink.api.common.serialization.AbstractDeserializationSchema;

public class DeviceDeserializationSchema extends AbstractDeserializationSchema<Device2> {

    private static final long serialVersionUID = 1L;

    private transient ObjectMapper objectMapper;

    /**
     * For performance reasons it's better to create on ObjectMapper in this open method rather than
     * creating a new ObjectMapper for every record.
     */
    @Override
    public void open(InitializationContext context) {
        objectMapper = JsonMapper.builder().build();
    }

    /**
     * If our deserialize method needed access to the information in the Kafka headers of a
     * KafkaConsumerRecord, we would have implemented a KafkaRecordDeserializationSchema instead of
     * extending AbstractDeserializationSchema.
     */
    @Override
    public Device2 deserialize(byte[] message) throws IOException {
        return objectMapper.readValue(message, Device2.class);
    }
}