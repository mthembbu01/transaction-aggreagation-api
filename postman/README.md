# Transaction Aggregation API — Postman Collection

This folder contains the Postman collection and environment for all microservices in the **Transaction Aggregation API**.

## Files

| File | Description |
|------|-------------|
| `transaction-aggregation-api.postman_collection.json` | Full Postman collection covering all modules |
| `transaction-aggregation-api.local.postman_environment.json` | Environment variables for local development |

---

## How to Import

1. Open **Postman**
2. Click **Import** (top-left)
3. Import both files:
   - `transaction-aggregation-api.postman_collection.json`
   - `transaction-aggregation-api.local.postman_environment.json`
4. Select the **"Transaction Aggregation API - Local"** environment from the environment dropdown

---

## Access Pattern

All requests in the collection now use one shared variable:

- `base_url = http://localhost:8071`

All service calls are routed through API Gateway:

- Accounts: `/capitec/accounts/**`
- Customer: `/capitec/customers/**`
- Credit Cards: `/capitec/cards/**`
- Loans: `/capitec/loans/**`

## Service Port Mapping (Local / Docker)

| Service          | Host Port | Container Port |
|------------------|-----------|----------------|
| Eureka Server    | 8070      | 8070           |
| API Gateway      | 8071      | 8071           |
| Accounts         | 8081      | 8080           |
| Customer         | 8082      | 8080           |
| Credit Cards     | 8083      | 8080           |
| Loans            | 8084      | 8080           |

---

## Collection Structure

### 📁 Accounts Service (via `{{base_url}}/capitec/accounts`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/{accountNumber}` | Get account by account number |
| GET | `/api/v1/accounts?idNumber={idNumber}` | Get all accounts by ID number |
| POST | `/api/v1/` | Create a new account |
| PUT | `/api/v1/{accountNumber}` | Full update of an account |
| PATCH | `/api/v1/{accountNumber}` | Partial update of an account |
| DELETE | `/api/v1/{accountNumber}` | Delete an account |
| POST | `/api/v1/transaction` | Post a transaction to an account |
| GET | `/api/v1/transaction/{idNumber}/{startDate}/{endDate}` | Get paginated account transactions (bank statement) |

### 📁 Customer Service (via `{{base_url}}/capitec/customers`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/{idNumber}` | Get customer by ID number |
| GET | `/api/v1/customers` | Get all customers (paginated) |
| POST | `/api/v1/` | Create a new customer |
| PUT | `/api/v1/{idNumber}` | Full update of a customer |
| PATCH | `/api/v1/{idNumber}` | Partial update of a customer |
| DELETE | `/api/v1/{idNumber}` | Delete a customer |

### 📁 Credit Cards Service (via `{{base_url}}/capitec/cards`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/{cardNumber}` | Get credit card by card number |
| GET | `/api/v1/creditcards?idNumber={idNumber}` | Get all credit cards by ID number |
| POST | `/api/v1/` | Create a new credit card |
| PUT | `/api/v1/{cardNumber}` | Full update of a credit card |
| PATCH | `/api/v1/{cardNumber}` | Partial update of a credit card |
| DELETE | `/api/v1/{cardNumber}` | Delete a credit card |
| POST | `/api/v1/transaction` | Post a transaction to a credit card |
| GET | `/api/v1/transaction/{idNumber}/{startDate}/{endDate}` | Get paginated credit card transactions |

### 📁 Loans Service (via `{{base_url}}/capitec/loans`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/{loanNumber}` | Get loan by loan number |
| GET | `/api/v1/loans?idNumber={idNumber}` | Get all loans by ID number |
| POST | `/api/v1/` | Create a new loan |
| PUT | `/api/v1/{loanNumber}` | Full update of a loan |
| PATCH | `/api/v1/{loanNumber}` | Partial update of a loan |
| DELETE | `/api/v1/{loanNumber}` | Delete a loan |
| POST | `/api/v1/transaction` | Post a transaction to a loan |
| GET | `/api/v1/transaction/{idNumber}/{startDate}/{endDate}` | Get paginated loan transactions |

### 📁 API Gateway (`{{base_url}}`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/composite/customersummary?idNumber=&startDate=&endDate=` | Get full customer summary (aggregated across all services) |

## Environment Variables

| Variable | Default Value | Description |
|----------|---------------|-------------|
| `base_url` | `http://localhost:8071` | Shared API Gateway URL for all requests |
| `id_number` | `9001015009087` | Sample SA ID number (13 digits) |
| `account_number` | `1001` | Sample account number |
| `card_number` | `4000123456789010` | Sample credit card number |
| `loan_number` | `2001` | Sample loan number |
| `start_date` | `2024-01-01` | Start date for transaction queries |
| `end_date` | `2024-12-31` | End date for transaction queries |

---

## Enum Reference

### Transaction Categories
`FOOD`, `TRANSPORT`, `SHOPPING`, `UTILITIES`, `INSURANCE`, `MEDICAL`, `SALARY`, `TRANSFER`, `UNKNOWN`, `CREDIT`, `DEBIT`, `DEPOSIT`, `WITHDRAWAL`, `INTEREST`, `FEE`, `CURRENT`, `SAVINGS`

### Credit Card Types
`VISA`, `MASTERCARD`, `AMEX`

### Loan Types
`PERSONAL`, `HOME`, VEHICLE`, `BUSINESS`, `STUDENT`

---

## Validation Rules

| Field | Format |
|-------|--------|
| `idNumber` | Must be exactly 13 digits |
| `mobileNumber` | South African format: `+27` or `0` prefix followed by 9 digits |
| `email` | Standard email format |
| `startDate` / `endDate` | ISO date format: `YYYY-MM-DD` |

