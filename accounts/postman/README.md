# Accounts API Postman Data

This folder contains Postman artifacts for the `accounts` service.

## Files

- `accounts-api.postman_collection.json` - request collection for all exposed `accounts` endpoints.
- `accounts-api.local.postman_environment.json` - local environment variables.

## Quick Notes

- The `accounts` service runs on a random port (`server.port: 0`).
- The collection targets the API Gateway route by default: `http://localhost:8071/capitec/accounts`.
- Start order for local testing should include Eureka, accounts, and API Gateway.

## Suggested Request Order

1. `01 - Create Account`
2. `02 - Find Accounts by ID Number` (stores `accountNumber` automatically)
3. `03 - Find Account by Account Number`
4. `04 - Update Account (PUT)`
5. `05 - Patch Account (PATCH)`
6. `06 - Create Transaction`
7. `07 - Get Transactions by ID and Date Range`
8. `08 - Delete Account`

## Validation Rules Reflected in Payloads

- `idNumber`: exactly 13 digits.
- `mobileNumber`: South African format like `0712345678`.
- `accountType`: one of `SAVINGS`, `CHEQUE`, `Transaction`, `Business`.
- `category`: one of the enum values in `Categories` (e.g. `SALARY`, `TRANSFER`, `FOOD`).

