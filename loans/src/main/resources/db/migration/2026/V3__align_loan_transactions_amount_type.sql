-- =========================================
-- Migration: align_loan_transactions_amount_type
-- Version: V3
-- Service: loans
-- Created at: Jul 27, 2026
-- =========================================
-- Align loan_transactions.amount type with JPA BigDecimal mapping.

ALTER TABLE loan_transactions
    ALTER COLUMN amount TYPE NUMERIC(38,2)
    USING amount::NUMERIC(38,2);

