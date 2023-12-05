package org.example.daas.graphql.repository;

import org.example.daas.graphql.model.Location;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.graphql.data.GraphQlRepository;

@GraphQlRepository
public interface LocationRepository extends Repository<Location, String>, QueryByExampleExecutor<Location> {
    Location findById(String id);
}
