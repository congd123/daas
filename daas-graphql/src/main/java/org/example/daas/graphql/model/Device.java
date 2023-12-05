package org.example.daas.graphql.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "devices")
public class Device {

    @Id
    private String id;
    private String name;

    private String locationId;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
}
