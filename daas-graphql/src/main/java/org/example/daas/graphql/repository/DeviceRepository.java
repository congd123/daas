package org.example.daas.graphql.repository;

import org.example.daas.graphql.model.Device;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.graphql.data.GraphQlRepository;

@GraphQlRepository
public interface DeviceRepository extends Repository<Device, String>, QueryByExampleExecutor<Device> {
    Device findById(String id);
}
