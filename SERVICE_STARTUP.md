# Service Startup Guide

This guide explains how to properly start all services to avoid connection errors (like `Connection refused`).

## Prerequisites
- PostgreSQL running on `localhost:5432`
- Java 21+
- Maven 3.8+
- All databases created (see README.md for SQL)

## Critical Startup Order

**Services MUST be started in this order:**

### 1. Eureka Server (Port 8070)
```zsh
cd "/eureka-server"
./mvnw clean install
./mvnw spring-boot:run
```
**Wait for:** `Started EurekaServer in X seconds`

### 2. Microservices (in any order, but all before gateway)

#### Terminal 2: Accounts Service
```zsh
cd "/accounts"
./mvnw clean install
./mvnw spring-boot:run
```
**Wait for:** `Registering application accounts with eureka with initial status UP`

#### Terminal 3: Customer Service
```zsh
cd "/customer"
./mvnw clean install
./mvnw spring-boot:run
```
**Wait for:** `Registering application customers with eureka with initial status UP`

#### Terminal 4: Credit Cards Service
```zsh
cd "/creditcards"
./mvnw clean install
./mvnw spring-boot:run
```
**Wait for:** `Registering application cards with eureka with initial status UP`

#### Terminal 5: Loans Service
```zsh
cd "/loans"
./mvnw clean install
./mvnw spring-boot:run
```
**Wait for:** `Registering application loans with eureka with initial status UP`

### 3. API Gateway (Port 8071)
**ONLY after all microservices have registered with Eureka**

```zsh
cd "/api-gateway"
./mvnw clean install
./mvnw spring-boot:run
```
**Wait for:** `Started ApiGateway in X seconds` + `Registered with Eureka`

## Verification

### Check Eureka Dashboard
Open in browser: `http://localhost:8070/`

You should see:
- **Accounts** (orange status if just started)
- **Customers** 
- **Cards**
- **Loans**

All should turn green (UP) within 30 seconds.

### Check Gateway Health
```bash
curl http://localhost:8071/actuator/health
```

Expected response:
```json
{"status":"UP"}
```

### Test Accounts API (via Gateway)
```bash
curl http://localhost:8071/capitec/accounts/api/v1/accounts?idNumber=9202204720082
```

If you get `Connection refused`, the accounts service hasn't registered yet. **Wait 10+ seconds and retry.**

## Common Errors

### Error: `Connection refused: /192.168.x.x:xxxxx`
**Cause:** Microservice not running or not yet registered with Eureka.
**Fix:** 
1. Verify the service is running (check its terminal for startup logs)
2. Wait 10-30 seconds for Eureka registration
3. Check Eureka dashboard to confirm service is UP

### Error: `Unable to connect to Eureka`
**Cause:** Eureka server not running.
**Fix:** Start Eureka first and wait for it to initialize.

### Warning: `Unable to load io.netty.resolver.dns.macos...`
**Fixed:** All `pom.xml` files now include `netty-resolver-dns-native-macos` dependency.
Run `./mvnw clean install` in each module.

## Quick Start Script (macOS)

Create a shell script `start-all.sh`:

```bash
#!/bin/zsh

# Terminal 1: Eureka
open -a Terminal && sleep 1
tell app "Terminal" to do script "cd '/eureka-server' && ./mvnw spring-boot:run"

sleep 10

# Terminal 2: Accounts
open -a Terminal && sleep 1
tell app "Terminal" to do script "cd '/accounts' && ./mvnw spring-boot:run"

# Terminal 3: Customer
open -a Terminal && sleep 1
tell app "Terminal" to do script "cd '/customer' && ./mvnw spring-boot:run"

# Terminal 4: Credit Cards
open -a Terminal && sleep 1
tell app "Terminal" to do script "cd '/creditcards' && ./mvnw spring-boot:run"

# Terminal 5: Loans
open -a Terminal && sleep 1
tell app "Terminal" to do script "cd '/loans' && ./mvnw spring-boot:run"

sleep 15

# Terminal 6: API Gateway
open -a Terminal && sleep 1
tell app "Terminal" to do script "cd '/api-gateway' && ./mvnw spring-boot:run"
```

Or use **Docker Compose** (recommended):
```bash
cd /transaction-aggreagation-api
docker-compose up -d
```

## Postman Testing

Once all services are UP in Eureka, import the collection from the `postman/` folder at the project root:

1. Open Postman → **Import**
2. Import `postman/Transaction Aggregation API.postman_collection.json`
3. Import `postman/transaction-aggregation-api.local.postman_environment.json`
4. Select **"Transaction Aggregation API - Local"** from the environment dropdown

> Note: `postman/transaction-aggregation-api.postman_collection.json` is still present, but `postman/Transaction Aggregation API.postman_collection.json` is the primary collection to use.

The collection covers all modules with pre-populated sample values:

| Folder | Service | Port |
|--------|---------|------|
| Accounts Service | Accounts CRUD + Transactions | 8081 |
| Customer Service | Customer CRUD | 8082 |
| Credit Cards Service | Credit Card CRUD + Transactions | 8083 |
| Loans Service | Loan CRUD + Transactions | 8084 |
| API Gateway | Composite customer summary | 8071 |
| Eureka Server | Dashboard + registered apps | 8070 |

> See `postman/README.md` for full endpoint reference and enum values.

