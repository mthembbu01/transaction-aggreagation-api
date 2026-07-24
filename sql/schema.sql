-- Bootstrap script for local PostgreSQL environments.
-- Run this as a superuser (for example: postgres).

-- 1) Create one database per microservice.
CREATE DATABASE db_cptc_customers;
CREATE DATABASE db_cptc_accounts;
CREATE DATABASE db_cptc_creditcards;
CREATE DATABASE db_cptc_loans;

-- 2) Create the shared development role used by services.
CREATE USER dev_cptc_user WITH PASSWORD 'localpass';

-- 3) Allow the role to connect to each service database.
GRANT CONNECT ON DATABASE db_cptc_customers, db_cptc_accounts, db_cptc_creditcards, db_cptc_loans TO dev_cptc_user;

-- 4) Schema/table/sequence privileges (current database scope).
-- NOTE: The statements below apply to the CURRENT database only.
-- Re-run this section while connected to each target database if needed.
GRANT USAGE ON SCHEMA public TO dev_cptc_user;

GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO dev_cptc_user;

GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO dev_cptc_user;

-- 5) Ensure future tables/sequences in public inherit the same privileges.
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO dev_cptc_user;

ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT USAGE, SELECT ON SEQUENCES TO dev_cptc_user;