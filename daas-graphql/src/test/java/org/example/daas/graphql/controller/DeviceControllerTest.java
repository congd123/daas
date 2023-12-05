package org.example.daas.graphql.controller;

import org.example.daas.graphql.model.Device;
import org.example.daas.graphql.model.Location;
import org.example.daas.graphql.repository.DeviceRepository;
import org.example.daas.graphql.repository.LocationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DeviceControllerTest {

    private DeviceController deviceController;

    private DeviceRepository deviceRepositoryMock;

    private LocationRepository locationRepositoryMock;

    @BeforeEach
    public void before() {
        deviceRepositoryMock = Mockito.mock(DeviceRepository.class);
        locationRepositoryMock = Mockito.mock(LocationRepository.class);
        deviceController = new DeviceController(deviceRepositoryMock, locationRepositoryMock);
    }

    @AfterEach
    public void after() {
        reset(deviceRepositoryMock);
        reset(locationRepositoryMock);
    }

    @Test
    public void deviceShouldReturnAValidResult() {
        String deviceId = "device-id";

        Device expectedDevice = new Device();
        expectedDevice.setId(deviceId);
        expectedDevice.setName("name-1");
        expectedDevice.setLocationId("location-1");

        when(deviceRepositoryMock.findById(deviceId)).thenReturn(expectedDevice);

        Device resultDevice = deviceController.deviceById(deviceId);

        assertEquals(expectedDevice, resultDevice);
        verify(deviceRepositoryMock, times(1)).findById(deviceId);
    }

    @Test
    public void locationShouldReturnAValidResult() {
        Device device = new Device();
        device.setId("device-id");
        device.setName("name-1");
        device.setLocationId("location-1");

        Location expectedLocation = new Location();
        expectedLocation.setName("location 1");
        expectedLocation.setCity("Porto");
        expectedLocation.setCountry("Portugal");
        expectedLocation.setCode(1);

        when(locationRepositoryMock.findById(device.getLocationId())).thenReturn(expectedLocation);

        Location resultLocation = deviceController.location(device);

        assertEquals(expectedLocation, resultLocation);
        verify(locationRepositoryMock, times(1)).findById(device.getLocationId());
    }
}