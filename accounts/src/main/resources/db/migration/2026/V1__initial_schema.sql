-- =========================================
-- Migration: initial_schema
-- Version: V1
-- Created at: Thu Jul 23 17:36:54 SAST 2026
-- Author: mac
-- =========================================

-- -------------------------------------
-- Sequences
-- -------------------------------------

-- Drives accounts.id (@SequenceGenerator allocationSize = 1)
CREATE SEQUENCE IF NOT EXISTS accounts_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Drives accounts_transactions.id (@SequenceGenerator allocationSize = 1)
CREATE SEQUENCE IF NOT EXISTS account_transactions_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- -------------------------------------
-- Table: accounts
-- Entity: za.co.capitec.accounts.entity.Accounts
-- Inherits audit columns from BaseEntity
-- -------------------------------------
CREATE TABLE IF NOT EXISTS accounts (

    -- ---- own fields ----
    -- PK: @Id + @GeneratedValue(SEQUENCE, accounts_sequence)
    id              BIGINT          NOT NULL DEFAULT nextval('accounts_sequence'),

    -- @Column(name = "account_number")
    account_number   BIGINT,

    -- @Enumerated(EnumType.STRING): SAVINGS | CHEQUE | Transaction | Business
    account_type     VARCHAR(50),
    balance          NUMERIC(38,2),
    mobile_number    VARCHAR(20),
    id_number        VARCHAR(13),
    branch_address   VARCHAR(255),
    active_sw        BOOLEAN         NOT NULL DEFAULT FALSE,

    -- ---- BaseEntity audit fields ----
    -- @CreatedDate  @Column(updatable = false)
    created_at       TIMESTAMP,
    -- @CreatedBy    @Column(updatable = false)
    created_by       VARCHAR(100),
    -- @LastModifiedDate  @Column(insertable = false)
    updated_at       TIMESTAMP,
    -- @LastModifiedBy  @Column(insertable = false)
    updated_by       VARCHAR(100),

    CONSTRAINT pk_accounts PRIMARY KEY (id)
);

ALTER SEQUENCE accounts_sequence OWNED BY accounts.id;

-- -------------------------------------
-- Table: accounts_transactions
-- Entity: za.co.capitec.accounts.entity.AccountsTransactions
-- Does NOT extend BaseEntity → no audit columns
-- -------------------------------------
CREATE TABLE IF NOT EXISTS accounts_transactions (

    -- PK: @Id + @GeneratedValue(SEQUENCE, account_transactions_sequence)
    id              BIGINT          NOT NULL DEFAULT nextval('account_transactions_sequence'),

    -- @Enumerated(EnumType.STRING): SAVINGS | CHEQUE | Transaction | Business
    account_type    VARCHAR(50),

    -- @Enumerated(EnumType.STRING): FOOD | TRANSPORT | SHOPPING | SALARY | TRANSFER ...
    category        VARCHAR(50),
    is_immediate    BOOLEAN             DEFAULT FALSE,
    description     VARCHAR(255),
    amount          NUMERIC(38,2),
    reference       VARCHAR(255),
    time            TIME,
    date            DATE,

    -- @ManyToOne @JoinColumn(name = "account_number") → references accounts.id
    account_number  BIGINT,

    CONSTRAINT pk_accounts_transactions         PRIMARY KEY (id),
    CONSTRAINT fk_accounts_transactions_account FOREIGN KEY (account_number)
                                                REFERENCES accounts (id)
                                                ON DELETE CASCADE
);

ALTER SEQUENCE account_transactions_sequence OWNED BY accounts_transactions.id;

-- -------------------------------------
-- Indexes
-- -------------------------------------

-- Lookup accounts by their business account_number
CREATE INDEX IF NOT EXISTS idx_accounts_account_number
    ON accounts (account_number);

-- Lookup transactions for a specific account
CREATE INDEX IF NOT EXISTS idx_accounts_transactions_account_number
    ON accounts_transactions (account_number);

-- Date-range queries (bank statements)
CREATE INDEX IF NOT EXISTS idx_accounts_transactions_date
    ON accounts_transactions (date);

-- Lookup by transaction category
CREATE INDEX IF NOT EXISTS idx_accounts_transactions_category
    ON accounts_transactions (category);
