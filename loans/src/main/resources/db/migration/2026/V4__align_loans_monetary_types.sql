-- =========================================
-- Migration: align_loans_monetary_types
-- Version: V4
-- Service: loans
-- Created at: Jul 27, 2026
-- =========================================
-- Align loans monetary column types with JPA BigDecimal mappings.

ALTER TABLE loans
    ALTER COLUMN loan_amount TYPE NUMERIC(38,2)
    USING loan_amount::NUMERIC(38,2),
    ALTER COLUMN outstanding_balance TYPE NUMERIC(38,2)
    USING outstanding_balance::NUMERIC(38,2),
    ALTER COLUMN monthly_instalment TYPE NUMERIC(38,2)
    USING monthly_instalment::NUMERIC(38,2),
    ALTER COLUMN outstanding_amount TYPE NUMERIC(38,2)
    USING outstanding_amount::NUMERIC(38,2);

