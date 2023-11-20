CREATE KEYSPACE IF NOT EXISTS daas WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1};

CREATE TABLE IF NOT EXISTS daas.user (
    id int,
    name text,
    PRIMARY KEY (id, name)
);

INSERT INTO daas.user (id, name) VALUES (1, 'John');