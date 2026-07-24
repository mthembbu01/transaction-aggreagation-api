-- =========================================
-- Migration: add_coutstanding_balance_column
-- Version: V3
-- Created at: Fri Jul 24 19:40:26 SAST 2026
-- Author: mac
-- =========================================

-- TODO: Write your SQL here

ALTER TABLE creditcards
    ADD COLUMN IF NOT EXISTS outstanding_balance DOUBLE PRECISION;

