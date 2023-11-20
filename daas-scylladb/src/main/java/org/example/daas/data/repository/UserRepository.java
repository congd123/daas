package org.example.daas.data.repository;

import org.example.daas.data.model.User;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface UserRepository extends CassandraRepository<User, Integer> {

}
