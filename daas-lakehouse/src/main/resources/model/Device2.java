package model;

import org.apache.flink.types.Row;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class Device2 {

    public String id;
    public String name;
    public String state;
    public Long createdTimestamp;
    public Long updatedTimestamp;

    public Device2() {

    }

    public Device2(String id, String name, String state, Long createdTimestamp, Long updatedTimestamp) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.createdTimestamp = createdTimestamp;
        this.updatedTimestamp = updatedTimestamp;
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

    public Long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public Long getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setCreatedTimestamp(Long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public void setUpdatedTimestamp(Long updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    public Row toRow() {
        Instant instant = Instant.ofEpochMilli(createdTimestamp);
        OffsetDateTime dateUTC = OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
        if (dateUTC.getMonthValue() > 9) {

        }
        String date = partitionDate(dateUTC);
        return Row.of(id, name, state, createdTimestamp, updatedTimestamp, date);
    }

    private String partitionDate(OffsetDateTime offsetDateTime) {
        if (offsetDateTime.getMonthValue() > 9) {
            return String.format("%d%d", offsetDateTime.getYear(), offsetDateTime.getMonthValue());
        } else {
            return String.format("%d0%d", offsetDateTime.getYear(), offsetDateTime.getMonthValue());
        }
    }
}