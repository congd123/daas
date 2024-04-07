/*import model.Device2;
import model.DeviceDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.DistributionMode;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.TableProperties;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.flink.CatalogLoader;
import org.apache.iceberg.flink.FlinkSchemaUtil;
import org.apache.iceberg.flink.TableLoader;
import org.apache.iceberg.flink.sink.FlinkSink;
import org.apache.iceberg.types.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class FlinkIcebergTableMergeInto {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlinkIcebergTableMergeInto.class);

    public static void main(String[] args) throws Exception {
        ParameterTool parameters = ParameterTool.fromArgs(args);
        Configuration hadoopConf = new Configuration();
        hadoopConf.set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");
        hadoopConf.set("fs.s3a.access.key", "minio");
        hadoopConf.set("fs.s3a.secret.key", "minio123");
        hadoopConf.set("fs.s3a.endpoint", "http://minio:9000");
        hadoopConf.set("fs.s3a.path.style.access", "true");
        hadoopConf.set("hive.metastore.uris", "thrift://hive-server2:9083");

        Schema schema = new Schema(
                Types.NestedField.optional(1, "id", Types.StringType.get()),
                Types.NestedField.optional(2, "name", Types.StringType.get()),
                Types.NestedField.optional(3, "state", Types.StringType.get()),
                Types.NestedField.optional(4, "timestampstart", Types.LongType.get()),
                Types.NestedField.optional(5, "timestampend", Types.LongType.get()),
                Types.NestedField.optional(6, "date", Types.IntegerType.get())
        );

        CatalogLoader catalogHiveLoader = CatalogLoader.hive(
                "iceberg",
                hadoopConf,
                Map.of(TableProperties.ENGINE_HIVE_ENABLED, "true", TableProperties.FORMAT_VERSION, "2")
        );


        TableIdentifier outputTable = TableIdentifier.of(
                "iceberg",
                "devices");

        Catalog catalog = catalogHiveLoader.loadCatalog();

        if (!catalog.tableExists(outputTable)) {
            catalog.createTable(outputTable, schema, PartitionSpec.unpartitioned());
        }

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        env.enableCheckpointing(Integer.parseInt(parameters.get("checkpoint", "1000")));

        KafkaSource<Device2> source = KafkaSource.<Device2>builder()
                .setBootstrapServers("localhost:49092")
                .setTopics("users")
                .setGroupId("users-flink-consumer-group")
                .setStartingOffsets(OffsetsInitializer.latest())
                .setValueOnlyDeserializer(new DeviceDeserializationSchema())
                .build();

        DataStreamSource<Device2> stream = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");
        DataStream<Row> streamRow = stream.map(map -> {
            return map.toRow();
        });

        tableEnv.executeSql("SHOW CATALOGS").print();

        // transform the data here!
        Table table = tableEnv.fromDataStream(streamRow, org.apache.flink.table.api.Schema.newBuilder()
                .column("f0", DataTypes.of(Device2.class))
                .build()).as("device");

        tableEnv.createTemporaryView("table1", table);


        TableResult result = tableEnv.executeSql(
                "SELECT COUNT(device.id) FROM iceberg.devices WHERE name LIKE 'device1'");

        result.print();


        env.execute();
    }
}
*/