-- =========================================
-- Migration: add_balance_column
-- Version: V2
-- Service: accounts
-- Created at: Jul 24, 2026
-- =========================================
-- Add missing 'balance' column to accounts table
-- Entity: za.co.capitec.accounts.entity.Accounts

ALTER TABLE accounts ADD COLUMN IF NOT EXISTS balance DOUBLE PRECISION;

