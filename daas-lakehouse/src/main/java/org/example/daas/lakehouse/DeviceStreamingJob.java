package org.example.daas.lakehouse;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.table.data.RowData;
import org.apache.hudi.common.model.HoodieTableType;
import org.apache.hudi.common.model.WriteOperationType;
import org.apache.hudi.configuration.FlinkOptions;
import org.apache.hudi.util.HoodiePipeline;
import org.example.daas.lakehouse.model.Device;
import org.example.daas.lakehouse.model.DeviceDeserializationSchema;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DeviceStreamingJob {
    public static void main(String[] args) throws Exception {
        ParameterTool parameters = ParameterTool.fromArgs(args);

        String pipelineName = parameters.get("jobName", "DeviceJob");
        String kafkaEndpoint = parameters.get("kafkaEndpoint", "localhost:9092");
        String kafkaGroupId = parameters.get("kafkaGroupId", "device-group-id");
        String kafkaOffset = parameters.get("kafkaOffset", "latest");
        String checkpointLocation = parameters.get("checkpoint", "file:///tmp/device_flink_checkpoint_1");
        String kafkaTopicName = parameters.get("topic", "devices.updates");
        String hiveEndpoint = parameters.get("hiveEndpoint", "thrift://hive-server2:9083");

        String targetTable = parameters.get("table", "devices");
        String hudiBasePath = parameters.get("tablePath", "s3a://datastream-devices/" + targetTable);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        configureCheckpointing(env, checkpointLocation);

        FlinkKafkaConsumer<Device> kafkaConsumer = createKafkaConsumer(kafkaEndpoint, kafkaTopicName, kafkaGroupId, kafkaOffset);

        DataStream<Device> kafkaStream = env.addSource(kafkaConsumer);
        DataStream<RowData> transformedStream = kafkaStream.map(new HudiDataSource());
        Map<String, String> options = createHudiOptions(hiveEndpoint, hudiBasePath);
        HoodiePipeline.Builder builder = createHoodiePipeline(targetTable, options);
        builder.sink(transformedStream, false);

        env.execute(pipelineName);
    }

    private static void configureCheckpointing(StreamExecutionEnvironment env, String checkpointLocation) {
        env.enableCheckpointing(10000);
        CheckpointConfig checkpointConfig = env.getCheckpointConfig();
        checkpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        checkpointConfig.setMinPauseBetweenCheckpoints(1000);
        checkpointConfig.setCheckpointTimeout(60000);
        checkpointConfig.setCheckpointStorage(checkpointLocation);
    }

    private static FlinkKafkaConsumer<Device> createKafkaConsumer(String kafkaEndpoint,
                                                                  String topicName,
                                                                  String kafkaGroupId,
                                                                  String kafkaOffset) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", kafkaEndpoint);
        properties.setProperty("group.id", kafkaGroupId);
        properties.setProperty("auto.offset.reset", kafkaOffset);

        return new FlinkKafkaConsumer<>(
                topicName,
                new DeviceDeserializationSchema(),
                properties
        );
    }

    private static Map<String, String> createHudiOptions(String hiveEndpoint, String basePath) {
        Map<String, String> options = new HashMap<>();
        options.put(FlinkOptions.OPERATION.key(), WriteOperationType.UPSERT.name());
        options.put(FlinkOptions.PATH.key(), basePath);
        options.put(FlinkOptions.TABLE_TYPE.key(), HoodieTableType.MERGE_ON_READ.name());
        options.put(FlinkOptions.PRECOMBINE_FIELD.key(), "createdtimestamp");
        options.put(FlinkOptions.RECORD_KEY_FIELD.key(), "id");
        options.put(FlinkOptions.HIVE_SYNC_ENABLED.key(), "true");
        options.put(FlinkOptions.HIVE_STYLE_PARTITIONING.key(), "true");
        options.put(FlinkOptions.HIVE_SYNC_MODE.key(), "hms");
        options.put(FlinkOptions.HIVE_SYNC_USE_JDBC.key(), "false");
        options.put(FlinkOptions.HIVE_SYNC_METASTORE_URIS.key(), hiveEndpoint);
        options.put(FlinkOptions.COMPACTION_DELTA_SECONDS.key(), "10");
        options.put(FlinkOptions.COMPACTION_DELTA_COMMITS.key(), "2");
        options.put(FlinkOptions.IGNORE_FAILED.key(), "true");

        return options;
    }

    private static HoodiePipeline.Builder createHoodiePipeline(String targetTable, Map<String, String> options) {
        return HoodiePipeline.builder(targetTable)
                .column("id VARCHAR(10)")
                .column("name VARCHAR(10)")
                .column("state VARCHAR(10)")
                .column("createdtimestamp bigint")
                .column("updatedtimestamp bigint")
                .column("`date` int")
                .pk("id")
                .partition("date")
                .options(options);
    }

    static class HudiDataSource implements MapFunction<Device, RowData> {
        @Override
        public RowData map(Device device) throws Exception {
            return device.toRow();
        }
    }
}
