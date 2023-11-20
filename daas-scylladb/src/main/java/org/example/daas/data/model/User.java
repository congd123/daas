package org.example.daas.data.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
public class User {
    @PrimaryKey
    private int id;

    public User() {

    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
