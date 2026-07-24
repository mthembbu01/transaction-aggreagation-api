-- =========================================
-- Migration: initial_schema
-- Version: V1
-- Service: creditcards
-- =========================================

CREATE SEQUENCE IF NOT EXISTS creditcards_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS credit_card_transactions_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS creditcards (
    id                  BIGINT          NOT NULL DEFAULT nextval('creditcards_sequence'),
    card_number         BIGINT,
    card_type           VARCHAR(50),
    mobile_number       VARCHAR(20),
    id_number           VARCHAR(13),
    credit_limit        DOUBLE PRECISION,
    available_credit    DOUBLE PRECISION,
    active_sw           BOOLEAN         NOT NULL DEFAULT FALSE,

    -- BaseEntity audit fields
    created_at          TIMESTAMP,
    created_by          VARCHAR(100),
    updated_at          TIMESTAMP,
    updated_by          VARCHAR(100),

    CONSTRAINT pk_creditcards PRIMARY KEY (id)
);

ALTER SEQUENCE creditcards_sequence OWNED BY creditcards.id;

CREATE TABLE IF NOT EXISTS credit_card_transactions (
    id              BIGINT          NOT NULL DEFAULT nextval('credit_card_transactions_sequence'),
    card_type       VARCHAR(50),
    category        VARCHAR(50),
    description     VARCHAR(255),
    amount          DOUBLE PRECISION,
    is_immediate    BOOLEAN         NOT NULL DEFAULT FALSE,
    reference       VARCHAR(255),
    time            TIME,
    date            DATE,
    card_number     BIGINT,

    CONSTRAINT pk_credit_card_transactions PRIMARY KEY (id),
    CONSTRAINT fk_credit_card_transactions_card
        FOREIGN KEY (card_number)
        REFERENCES creditcards (id)
        ON DELETE CASCADE
);

ALTER SEQUENCE credit_card_transactions_sequence OWNED BY credit_card_transactions.id;

CREATE INDEX IF NOT EXISTS idx_creditcards_card_number
    ON creditcards (card_number);

CREATE INDEX IF NOT EXISTS idx_creditcards_id_number
    ON creditcards (id_number);

CREATE INDEX IF NOT EXISTS idx_credit_card_transactions_card_number
    ON credit_card_transactions (card_number);

CREATE INDEX IF NOT EXISTS idx_credit_card_transactions_date
    ON credit_card_transactions (date);

