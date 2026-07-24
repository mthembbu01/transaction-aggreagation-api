-- =========================================
-- Migration: add_outstanding_amount_column
-- Version: V2
-- Service: loans
-- Created at: Jul 24, 2026
-- =========================================
-- Add missing 'outstanding_amount' column to loans table
-- Entity: za.co.capitec.loans.entity.Loans
-- Note: This is distinct from 'outstanding_balance' which already exists

ALTER TABLE loans ADD COLUMN IF NOT EXISTS outstanding_amount DOUBLE PRECISION;

