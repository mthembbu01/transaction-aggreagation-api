-- =========================================
-- Migration: add_credit_card_columns
-- Version: V2
-- Service: creditcards
-- Created at: Jul 24, 2026
-- =========================================
-- Add missing columns to creditcards table
-- Entity: za.co.capitec.creditcards.entity.CreditCards

ALTER TABLE creditcards
    ADD COLUMN IF NOT EXISTS account_number BIGINT,
    ADD COLUMN IF NOT EXISTS minimum_payment DOUBLE PRECISION,
    ADD COLUMN IF NOT EXISTS issue_date DATE,
    ADD COLUMN IF NOT EXISTS expiry_date DATE;

