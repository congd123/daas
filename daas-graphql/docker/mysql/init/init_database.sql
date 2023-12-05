CREATE TABLE IF NOT EXISTS daas.locations (
    id varchar(50) NOT NULL,
    name varchar(256) NOT NULL,
    city varchar(256) NOT NULL,
    country varchar(256) NOT NULL,
    code int NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS daas.devices (
    id varchar(50) NOT NULL,
    name varchar(256) NOT NULL,
    location_id varchar(50),
    PRIMARY KEY (id),
    FOREIGN KEY (location_id) REFERENCES daas.locations(id) ON DELETE RESTRICT ON UPDATE CASCADE
);

INSERT INTO daas.locations (id, name, city, country, code) VALUES ('location-1', 'Location 1', 'Porto', 'Portugal', 001);
INSERT INTO daas.locations (id, name, city, country, code) VALUES ('location-2', 'Location 2', 'Madrid', 'Spain', 002);
INSERT INTO daas.locations (id, name, city, country, code) VALUES ('location-3', 'Location 3', 'Rome', 'Italy', 003);
INSERT INTO daas.devices (id, name, location_id) VALUES ('device-1', 'Device 1', 'location-1');
INSERT INTO daas.devices (id, name, location_id) VALUES ('device-2', 'Device 2', 'location-2');
INSERT INTO daas.devices (id, name, location_id) VALUES ('device-3', 'Device 3', 'location-3');