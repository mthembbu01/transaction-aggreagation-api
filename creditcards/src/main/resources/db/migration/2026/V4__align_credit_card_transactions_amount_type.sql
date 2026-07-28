-- =========================================
-- Migration: align_credit_card_transactions_amount_type
-- Version: V4
-- Service: creditcards
-- Created at: Jul 28, 2026
-- =========================================
-- Align credit_card_transactions.amount with JPA BigDecimal mapping.

ALTER TABLE credit_card_transactions
    ALTER COLUMN amount TYPE NUMERIC(38,2)
    USING amount::NUMERIC(38,2);

