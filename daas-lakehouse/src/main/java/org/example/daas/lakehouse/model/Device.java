package org.example.daas.lakehouse.model;

import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.data.StringData;
import org.apache.flink.table.data.binary.BinaryRowData;
import org.apache.flink.table.data.writer.BinaryRowWriter;
import org.apache.flink.table.data.writer.BinaryWriter;
import org.apache.flink.table.runtime.typeutils.InternalSerializers;
import org.apache.flink.table.types.DataType;
import org.apache.flink.table.types.logical.LogicalType;
import org.apache.flink.table.types.logical.RowType;
import org.apache.flink.types.Row;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class Device {

    public String id;
    public String name;
    public String state;
    public Long createdtimestamp;
    public Long updatedtimestamp;

    public Device() {

    }

    public Device(String id, String name, String state, Long createdTimestamp, Long updatedtimestamp) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.createdtimestamp = createdTimestamp;
        this.updatedtimestamp = updatedtimestamp;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getCreatedtimestamp() {
        return createdtimestamp;
    }

    public Long getUpdatedtimestamp() {
        return updatedtimestamp;
    }

    public void setCreatedtimestamp(Long createdtimestamp) {
        this.createdtimestamp = createdtimestamp;
    }

    public void setUpdatedtimestamp(Long updatedtimestamp) {
        this.updatedtimestamp = updatedtimestamp;
    }

    public RowData toRow() {
        Instant instant = Instant.ofEpochMilli(createdtimestamp);
        OffsetDateTime dateUTC = OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
        String date = partitionDate(dateUTC);
        return insertRow(
                StringData.fromString(id),
                StringData.fromString(name),
                StringData.fromString(state),
                createdtimestamp,
                updatedtimestamp,
                Integer.parseInt(date)
        );
    }

    private String partitionDate(OffsetDateTime offsetDateTime) {
        if (offsetDateTime.getMonthValue() > 9) {
            return String.format("%d%d", offsetDateTime.getYear(), offsetDateTime.getMonthValue());
        } else {
            return String.format("%d0%d", offsetDateTime.getYear(), offsetDateTime.getMonthValue());
        }
    }

    private BinaryRowData insertRow(Object... fields) {
        RowType rowType = (RowType) ROW_DATA_TYPE.getLogicalType();
        LogicalType[] types = rowType.getFields().stream().map(RowType.RowField::getType)
                .toArray(LogicalType[]::new);
        BinaryRowData row = new BinaryRowData(fields.length);
        BinaryRowWriter writer = new BinaryRowWriter(row);
        writer.reset();
        for (int i = 0; i < fields.length; i++) {
            Object field = fields[i];
            if (field == null) {
                writer.setNullAt(i);
            } else {
                BinaryWriter.write(writer, i, field, types[i], InternalSerializers.create(types[i]));
            }
        }
        writer.complete();
        return row;
    }

    private final transient DataType ROW_DATA_TYPE = DataTypes.ROW(
                    DataTypes.FIELD("id", DataTypes.VARCHAR(10)),
                    DataTypes.FIELD("name", DataTypes.VARCHAR(10)),
                    DataTypes.FIELD("state", DataTypes.VARCHAR(10)),
                    DataTypes.FIELD("createdtimestamp", DataTypes.BIGINT()),
                    DataTypes.FIELD("updatedtimestamp", DataTypes.BIGINT()),
                    DataTypes.FIELD("date", DataTypes.INT()))
            .notNull();
}