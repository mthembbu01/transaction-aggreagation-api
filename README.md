# Transaction Aggregation API

A microservices-based Spring Boot application that aggregates customer financial transaction data from multiple sources and provides comprehensive APIs for transaction categorization and retrieval.

## Project Overview

The system consists of the following components:

- **Eureka Server** (Port 8070) - Service Discovery
- **API Gateway** (Port 8071) - API routing and load balancing
- **Accounts Service** - Core account management and accountsTransactions
- **Customer Service** - Customer profile and information
- **Credit Cards Service** - Credit card management
- **Loans Service** - Loan management and tracking
- **PostgreSQL Database** - Data persistence layer

## Prerequisites

### Local Development
- Java 21+
- Maven 3.8+
- PostgreSQL 14+ (running on localhost:5432)
- Docker & Docker Compose (for containerized setup)

### Database Setup (Local)

Before running services locally, create the databases and user:

```sql
-- Create databases
CREATE DATABASE db_cptc_accounts;
CREATE DATABASE db_cptc_customers;
CREATE DATABASE db_cptc_creditcards;
CREATE DATABASE db_cptc_loans;

-- Create user
CREATE USER dev_cptc_user WITH PASSWORD 'localpass';

-- Grant permissions
GRANT CREATE ON SCHEMA public TO dev_cptc_user;
GRANT ALL PRIVILEGES ON DATABASE db_cptc_accounts TO dev_cptc_user;
GRANT ALL PRIVILEGES ON DATABASE db_cptc_customers TO dev_cptc_user;
GRANT ALL PRIVILEGES ON DATABASE db_cptc_creditcards TO dev_cptc_user;
GRANT ALL PRIVILEGES ON DATABASE db_cptc_loans TO dev_cptc_user;
```

## Running the Application

### Option 1: Local Development (Maven)

#### Start Eureka Server
```bash
cd eureka-server
./mvnw spring-boot:run
```

#### Start Microservices (in separate terminals)
```bash
# Terminal 1: Accounts Service
cd accounts
./mvnw spring-boot:run

# Terminal 2: Customer Service
cd customer
./mvnw spring-boot:run

# Terminal 3: Credit Cards Service
cd creditcards
./mvnw spring-boot:run

# Terminal 4: Loans Service
cd loans
./mvnw spring-boot:run

# Terminal 5: API Gateway
cd api-gateway
./mvnw spring-boot:run
```

#### Access the Application
- Eureka Dashboard: http://localhost:8070/
- API Gateway: http://localhost:8071/
- Swagger UI: http://localhost:8071/swagger-ui.html

### Option 2: Docker Compose (Recommended)

#### Build and Start All Services
```bash
# From the project root directory
docker-compose up -d
```

This will start:
- PostgreSQL database with all required databases and migrations
- Eureka Server
- All microservices
- API Gateway

#### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f accounts
docker-compose logs -f customer
docker-compose logs -f creditcards
docker-compose logs -f loans
docker-compose logs -f eureka-server
docker-compose logs -f api-gateway
```

#### Stop All Services
```bash
docker-compose down
```

#### Stop and Remove Data
```bash
docker-compose down -v
```

### Option 3: Build Individual Docker Images

#### Build Eureka Server
```bash
cd eureka-server
./mvnw clean package -DskipTests
docker build -t capitec/eureka-server:latest .
```

#### Build Microservices
```bash
# Accounts
cd accounts
./mvnw clean package -DskipTests
docker build -t capitec/accounts:latest .

# Customer
cd customer
./mvnw clean package -DskipTests
docker build -t capitec/customer:latest .

# Credit Cards
cd creditcards
./mvnw clean package -DskipTests
docker build -t capitec/creditcards:latest .

# Loans
cd loans
./mvnw clean package -DskipTests
docker build -t capitec/loans:latest .

# API Gateway
cd api-gateway
./mvnw clean package -DskipTests
docker build -t capitec/api-gateway:latest .
```

#### Run Individual Containers
```bash
docker run -d --name postgres-db \
  -e POSTGRES_PASSWORD=rootpass \
  -p 5432:5432 \
  postgres:16

docker run -d --name eureka-server \
  -p 8070:8070 \
  --network capitec-network \
  capitec/eureka-server:latest

docker run -d --name accounts \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-db:5432/db_cptc_accounts \
  -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8070/eureka/ \
  -p 8080:0 \
  --network capitec-network \
  capitec/accounts:latest

# ... similar for other services
```

## Architecture

```
┌─────────────────────────────────────────┐
│         API Gateway (8071)              │
└──────────────┬──────────────────────────┘
               │
        ┌──────┴──────┐
        │             │
   ┌────▼────┐   ┌───▼─────┐
   │ Eureka  │   │ Postgres │
   │ (8070)  │   │ Database │
   └────┬────┘   └───▲─────┬┘
        │            │     │
    ┌───┴────────────┼─────┼──────┬──────────┐
    │                │     │      │          │
┌───▼────┐ ┌────────▼┐ ┌──▼──┐ ┌─▼──┐ ┌───▼──┐
│Accounts│ │Customer │ │ CC  │ │Loan│ │ Auth │
└────────┘ └─────────┘ └─────┘ └────┘ └──────┘
```

## Testing the Services

### Health Check
```bash
curl http://localhost:8071/actuator/health
```

### List All Services (Eureka)
```bash
curl http://localhost:8070/eureka/apps
```

### Sample API Calls via Gateway
```bash
# Create a customer
curl -X POST http://localhost:8071/customer/api/v1 \
  -H "Content-Type: application/json" \
  -d '{
    "firstname": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "mobileNumber": "0712345678",
    "idNumber": "1234567890123",
    "address": "123 Main St"
  }'

# Get customer by ID
curl http://localhost:8071/customer/api/v1/{idNumber}

# List all customers
curl http://localhost:8071/customer/api/v1?pageNo=0&pageSize=10
```

## Database Migrations

Flyway handles automatic schema migrations. Migration files are located in:
- `accounts/src/main/resources/db/migration/`
- `customer/src/main/resources/db/migration/`
- `creditcards/src/main/resources/db/migration/`
- `loans/src/main/resources/db/migration/`

To manually run migrations:
```bash
cd accounts
./mvnw clean flyway:migrate -X
```

## Troubleshooting

### Services Not Registering with Eureka
- Ensure Eureka Server is running on port 8070
- Check logs: `docker-compose logs eureka-server`
- Verify network connectivity in Docker Compose

### Database Connection Errors
- Verify PostgreSQL is running
- Check database credentials in `application.yaml`
- Ensure all required databases exist

### Port Conflicts
- Stop existing services: `docker-compose down`
- Or use a different port by modifying docker-compose.yml

## Development Notes

### Adding New Endpoints
1. Add method to controller
2. Implement logic in service layer
3. Update repository if needed
4. Test with `./mvnw test`
5. Rebuild Docker image if using containers

### Service Layer Pattern
Each microservice follows this architecture:
- **Repository** - Data access (Spring Data JPA)
- **Service** - Business logic
- **Controller** - REST endpoints
- **DTO** - Request/response objects

## Deployment

### Production Considerations
- Use environment-based configuration
- Set secure database passwords
- Enable HTTPS for API Gateway
- Configure proper logging levels
- Use database connection pooling
- Set resource limits in Docker

