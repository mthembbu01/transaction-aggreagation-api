-- =========================================
-- Migration: align_creditcards_monetary_types
-- Version: V5
-- Service: creditcards
-- Created at: Jul 28, 2026
-- =========================================
-- Align creditcards monetary columns with JPA BigDecimal mappings.

ALTER TABLE creditcards
    ADD COLUMN IF NOT EXISTS amount NUMERIC(38,2);

ALTER TABLE creditcards
    ALTER COLUMN credit_limit TYPE NUMERIC(38,2)
    USING credit_limit::NUMERIC(38,2),
    ALTER COLUMN available_credit TYPE NUMERIC(38,2)
    USING available_credit::NUMERIC(38,2),
    ALTER COLUMN minimum_payment TYPE NUMERIC(38,2)
    USING minimum_payment::NUMERIC(38,2),
    ALTER COLUMN outstanding_balance TYPE NUMERIC(38,2)
    USING outstanding_balance::NUMERIC(38,2);

