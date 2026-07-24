-- =========================================
-- Migration: initial_schema
-- Version: V1
-- Service: customer
-- =========================================

CREATE SEQUENCE IF NOT EXISTS customers_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS customers (
    id              BIGINT          NOT NULL DEFAULT nextval('customers_sequence'),
    first_name      VARCHAR(100),
    last_name       VARCHAR(100),
    mobile_number   VARCHAR(20),
    id_number       VARCHAR(13),
    email           VARCHAR(255),
    address         VARCHAR(255),
    active_sw       BOOLEAN         NOT NULL DEFAULT FALSE,

    -- BaseEntity audit fields
    created_at      TIMESTAMP,
    created_by      VARCHAR(100),
    updated_at      TIMESTAMP,
    updated_by      VARCHAR(100),

    CONSTRAINT pk_customers PRIMARY KEY (id)
);

ALTER SEQUENCE customers_sequence OWNED BY customers.id;

CREATE INDEX IF NOT EXISTS idx_customers_id_number
    ON customers (id_number);

CREATE INDEX IF NOT EXISTS idx_customers_mobile_number
    ON customers (mobile_number);

CREATE INDEX IF NOT EXISTS idx_customers_email
    ON customers (email);

