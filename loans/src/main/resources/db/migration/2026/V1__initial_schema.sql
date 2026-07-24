-- =========================================
-- Migration: initial_schema
-- Version: V1
-- Service: loans
-- =========================================

CREATE SEQUENCE IF NOT EXISTS loans_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS loan_transactions_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS loans (
    id                      BIGINT          NOT NULL DEFAULT nextval('loans_sequence'),
    loan_number             BIGINT,
    loan_type               VARCHAR(50),
    mobile_number           VARCHAR(20),
    id_number               VARCHAR(13),
    loan_amount             DOUBLE PRECISION,
    outstanding_balance     DOUBLE PRECISION,
    monthly_instalment      DOUBLE PRECISION,
    start_date              DATE,
    end_date                DATE,
    status                  VARCHAR(50),
    active_sw               BOOLEAN         NOT NULL DEFAULT FALSE,

    -- BaseEntity audit fields
    created_at              TIMESTAMP,
    created_by              VARCHAR(100),
    updated_at              TIMESTAMP,
    updated_by              VARCHAR(100),

    CONSTRAINT pk_loans PRIMARY KEY (id)
);

ALTER SEQUENCE loans_sequence OWNED BY loans.id;

CREATE TABLE IF NOT EXISTS loan_transactions (
    id              BIGINT          NOT NULL DEFAULT nextval('loan_transactions_sequence'),
    category        VARCHAR(50),
    description     VARCHAR(255),
    amount          DOUBLE PRECISION,
    is_immediate    BOOLEAN         NOT NULL DEFAULT FALSE,
    reference       VARCHAR(255),
    time            TIME,
    date            DATE,
    loan_number     BIGINT,

    CONSTRAINT pk_loan_transactions PRIMARY KEY (id),
    CONSTRAINT fk_loan_transactions_loan
        FOREIGN KEY (loan_number)
        REFERENCES loans (id)
        ON DELETE CASCADE
);

ALTER SEQUENCE loan_transactions_sequence OWNED BY loan_transactions.id;

CREATE INDEX IF NOT EXISTS idx_loans_loan_number
    ON loans (loan_number);

CREATE INDEX IF NOT EXISTS idx_loans_id_number
    ON loans (id_number);

CREATE INDEX IF NOT EXISTS idx_loan_transactions_loan_number
    ON loan_transactions (loan_number);

CREATE INDEX IF NOT EXISTS idx_loan_transactions_date
    ON loan_transactions (date);

