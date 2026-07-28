-- Align credit_card_transactions foreign key column with JPA mapping (@JoinColumn(name = "card_id")).

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'credit_card_transactions'
          AND column_name = 'card_number'
    ) AND NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'credit_card_transactions'
          AND column_name = 'card_id'
    ) THEN
        ALTER TABLE credit_card_transactions RENAME COLUMN card_number TO card_id;
    END IF;
END $$;

ALTER TABLE credit_card_transactions
    DROP CONSTRAINT IF EXISTS fk_credit_card_transactions_card;

ALTER TABLE credit_card_transactions
    ADD CONSTRAINT fk_credit_card_transactions_card
    FOREIGN KEY (card_id)
    REFERENCES creditcards (id)
    ON DELETE CASCADE;

DROP INDEX IF EXISTS idx_credit_card_transactions_card_number;
CREATE INDEX IF NOT EXISTS idx_credit_card_transactions_card_id
    ON credit_card_transactions (card_id);

