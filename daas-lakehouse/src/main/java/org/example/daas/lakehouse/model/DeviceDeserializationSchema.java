package org.example.daas.lakehouse.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.apache.flink.api.common.serialization.AbstractDeserializationSchema;

import java.io.IOException;

public class DeviceDeserializationSchema extends AbstractDeserializationSchema<Device> {

    private static final long serialVersionUID = 1L;

    private transient ObjectMapper objectMapper;

    @Override
    public void open(InitializationContext context) {
        objectMapper = JsonMapper.builder().build();
    }

    @Override
    public Device deserialize(byte[] message) throws IOException {
        return objectMapper.readValue(message, Device.class);
    }
}
