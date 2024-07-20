CREATE TABLE rooms (
 id BIGINT PRIMARY KEY,
 number VARCHAR(50) NOT NULL UNIQUE,
 price DECIMAL(5,2) NOT NULL,
 type VARCHAR(10) NOT NULL,
 status VARCHAR(10) NOT NULL
);