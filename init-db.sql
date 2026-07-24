-- Create databases
CREATE DATABASE db_cptc_accounts;
CREATE DATABASE db_cptc_customers;
CREATE DATABASE db_cptc_creditcards;
CREATE DATABASE db_cptc_loans;

-- Create user
CREATE USER dev_cptc_user WITH PASSWORD 'localpass';

-- Grant privileges on all databases
GRANT ALL PRIVILEGES ON DATABASE db_cptc_accounts TO dev_cptc_user;
GRANT ALL PRIVILEGES ON DATABASE db_cptc_customers TO dev_cptc_user;
GRANT ALL PRIVILEGES ON DATABASE db_cptc_creditcards TO dev_cptc_user;
GRANT ALL PRIVILEGES ON DATABASE db_cptc_loans TO dev_cptc_user;

-- Connect to each database and grant schema privileges
\c db_cptc_accounts
GRANT ALL ON SCHEMA public TO dev_cptc_user;
CREATE SCHEMA IF NOT EXISTS public;
GRANT ALL PRIVILEGES ON SCHEMA public TO dev_cptc_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO dev_cptc_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO dev_cptc_user;

\c db_cptc_customers
GRANT ALL ON SCHEMA public TO dev_cptc_user;
CREATE SCHEMA IF NOT EXISTS public;
GRANT ALL PRIVILEGES ON SCHEMA public TO dev_cptc_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO dev_cptc_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO dev_cptc_user;

\c db_cptc_creditcards
GRANT ALL ON SCHEMA public TO dev_cptc_user;
CREATE SCHEMA IF NOT EXISTS public;
GRANT ALL PRIVILEGES ON SCHEMA public TO dev_cptc_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO dev_cptc_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO dev_cptc_user;

\c db_cptc_loans
GRANT ALL ON SCHEMA public TO dev_cptc_user;
CREATE SCHEMA IF NOT EXISTS public;
GRANT ALL PRIVILEGES ON SCHEMA public TO dev_cptc_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO dev_cptc_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO dev_cptc_user;

