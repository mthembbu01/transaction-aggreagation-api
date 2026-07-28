-- Align loan_transactions foreign key column with JPA mapping (@JoinColumn(name = "loan_id")).

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'loan_transactions'
          AND column_name = 'loan_number'
    ) AND NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'loan_transactions'
          AND column_name = 'loan_id'
    ) THEN
        ALTER TABLE loan_transactions RENAME COLUMN loan_number TO loan_id;
    END IF;
END $$;

ALTER TABLE loan_transactions
    DROP CONSTRAINT IF EXISTS fk_loan_transactions_loan;

ALTER TABLE loan_transactions
    ADD CONSTRAINT fk_loan_transactions_loan
    FOREIGN KEY (loan_id)
    REFERENCES loans (id)
    ON DELETE CASCADE;

DROP INDEX IF EXISTS idx_loan_transactions_loan_number;
CREATE INDEX IF NOT EXISTS idx_loan_transactions_loan_id
    ON loan_transactions (loan_id);

