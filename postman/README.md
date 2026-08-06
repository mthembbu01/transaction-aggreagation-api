# Transaction Aggregation API - Postman Collection

This folder contains Postman assets for the Transaction Aggregation API microservices and composite endpoint.

## Files

| File | Description |
|------|-------------|
| `Transaction Aggregation API.postman_collection.json` | Primary collection with all requests and sample responses |
| `transaction-aggregation-api.local.postman_environment.json` | Local environment values |
| `transaction-aggregation-api.postman_collection.json` | Legacy/alternate collection file |

## Import Steps

1. Open Postman.
2. Click **Import**.
3. Import:
   - `Transaction Aggregation API.postman_collection.json`
   - `transaction-aggregation-api.local.postman_environment.json`
4. Select the local environment before running requests.

## Base URL and Routing

All requests use:

- `base_url = http://localhost:8071`

All service calls go through API Gateway with these prefixes:

- Accounts: `/capitec/accounts/**`
- Customer: `/capitec/customers/**`
- Credit Cards: `/capitec/cards/**`
- Loans: `/capitec/loans/**`
- Composite endpoint: `/api/composite/customersummary`

## Collection Overview

### Accounts Service

Base path: `{{base_url}}/capitec/accounts/api/v1`

- `GET /{account_number}`
- `GET /accounts?idNumber={{id_number}}`
- `POST /`
- `PUT /{account_number}`
- `PATCH /{account_number}`
- `DELETE /{account_number}`
- `POST /transaction`
- `GET /transaction/{{id_number}}/{{start_date}}/{{end_date}}?pageNo=0&pageSize=10&sortBy=date&sortDir=desc`

### Customer Service

Base path: `{{base_url}}/capitec/customers/api/v1`

- `GET /{{id_number}}`
- `GET /customers?pageNo=0&pageSize=10&sortBy=lastName&sortDir=asc`
- `POST /`
- `PUT /{{id_number}}`
- `PATCH /{{id_number}}`
- `DELETE /{{id_number}}`

### Credit Cards Service

Base path: `{{base_url}}/capitec/cards/api/v1`

- `GET /{{card_number}}`
- `GET /creditcards?idNumber={{id_number}}`
- `POST /`
- `PUT /{{card_number}}`
- `PATCH /{{card_number}}`
- `DELETE /{{card_number}}`
- `POST /transaction`
- `GET /transaction/{{id_number}}/{{start_date}}/{{end_date}}?pageNo=0&pageSize=10&sortBy=date&sortDir=desc`

### Loans Service

Base path: `{{base_url}}/capitec/loans/api/v1`

- `GET /{{loan_number}}`
- `GET /loans?idNumber={{id_number}}`
- `POST /`
- `PUT /{{loan_number}}`
- `PATCH /{{loan_number}}`
- `DELETE /{{loan_number}}`
- `POST /transaction`
- `GET /transaction/{{id_number}}/{{start_date}}/{{end_date}}?pageNo=0&pageSize=10&sortBy=date&sortDir=desc`

### API Gateway (Composite)

- `GET {{base_url}}/api/composite/customersummary?idNumber={{id_number}}&startDate={{start_date}}&endDate={{end_date}}`

Returns an aggregated summary containing customer profile plus account, card, and loan transactions.

## Collection Variables (from collection file)

| Variable | Default |
|----------|---------|
| `base_url` | `http://localhost:8071` |
| `id_number` | `9001015009087` |
| `account_number` | `1001` |
| `card_number` | `4000123456789010` |
| `loan_number` | `2001` |
| `start_date` | `2024-01-01` |
| `end_date` | `2024-12-31` |

## Notes

- The collection includes sample `200/201/204` responses for most requests.
- Some account requests include test scripts that capture `account_number` from responses into collection variables.
- Date parameters use ISO format: `YYYY-MM-DD`.

## Enum Reference

- Transaction categories: `FOOD`, `TRANSPORT`, `SHOPPING`, `UTILITIES`, `INSURANCE`, `MEDICAL`, `SALARY`, `TRANSFER`, `UNKNOWN`, `CREDIT`, `DEBIT`, `DEPOSIT`, `WITHDRAWAL`, `INTEREST`, `FEE`, `CURRENT`, `SAVINGS`
- Credit card types: `VISA`, `MASTERCARD`, `AMEX`
- Loan types: `PERSONAL`, `HOME`, `VEHICLE`, `BUSINESS`, `STUDENT`

