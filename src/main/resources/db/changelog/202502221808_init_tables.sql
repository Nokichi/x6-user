CREATE SCHEMA x6;

CREATE TABLE x6.user
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR        NOT NULL,
    email       VARCHAR UNIQUE NOT NULL,
    phone       VARCHAR UNIQUE NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    birthday    DATE,
    gender      VARCHAR(8),
    total_spent DECIMAL(16, 2)           DEFAULT 0
);