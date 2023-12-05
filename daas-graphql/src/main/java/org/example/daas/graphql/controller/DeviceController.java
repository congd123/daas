package org.example.daas.graphql.controller;

import org.example.daas.graphql.model.Device;
import org.example.daas.graphql.model.Location;
import org.example.daas.graphql.repository.DeviceRepository;
import org.example.daas.graphql.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class DeviceController {

    private final DeviceRepository deviceRepository;

    private final LocationRepository locationRepository;

    @Autowired
    public DeviceController(DeviceRepository deviceRepository, LocationRepository locationRepository) {
        this.deviceRepository = deviceRepository;
        this.locationRepository = locationRepository;
    }

    @QueryMapping
    public Device deviceById(@Argument String id) {
        return deviceRepository.findById(id);
    }

    @SchemaMapping(typeName = "Device", field = "location")
    public Location location(Device device) {
        return locationRepository.findById(device.getLocationId());
    }
}
