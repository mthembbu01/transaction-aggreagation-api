# Docker Deployment Guide

This guide explains how to build and deploy the Transaction Aggregation API using Docker and Docker Compose.

## Table of Contents

1. [Quick Start](#quick-start)
2. [Prerequisites](#prerequisites)
3. [File Structure](#file-structure)
4. [Building Images](#building-images)
5. [Docker Compose](#docker-compose)
6. [Helper Script](#helper-script)
7. [Troubleshooting](#troubleshooting)

## Quick Start

The fastest way to get everything running:

```bash
# From the project root directory
./docker-run.sh start

# Check service health
./docker-run.sh health

# View logs
./docker-run.sh logs
```

Access the services at:
- **Eureka Dashboard**: http://localhost:8070/
- **API Gateway**: http://localhost:8071/
- **Accounts Service**: http://localhost:8081/
- **Customer Service**: http://localhost:8082/
- **Credit Cards Service**: http://localhost:8083/
- **Loans Service**: http://localhost:8084/

## Prerequisites

### System Requirements
- Docker Desktop (or Docker + Docker Compose) installed
- At least 8GB of available RAM
- 2+ CPU cores
- At least 10GB of disk space

### Installation

**macOS**
```bash
brew install docker
# Open Docker Desktop from Applications
```

**Linux**
```bash
# Ubuntu/Debian
sudo apt-get install docker.io docker-compose

# Fedora
sudo dnf install docker docker-compose
```

**Windows**
- Download and install Docker Desktop from https://www.docker.com/products/docker-desktop

## File Structure

```
transaction-aggreagation-api/
├── docker-compose.yml           # Main orchestration file
├── docker-run.sh                # Helper script for Docker commands
├── init-db.sql                  # PostgreSQL initialization script
├── .dockerignore                # Files to exclude from Docker build
├── eureka-server/
│   └── Dockerfile               # Eureka Server container definition
├── accounts/
│   └── Dockerfile               # Accounts service container
├── customer/
│   └── Dockerfile               # Customer service container
├── creditcards/
│   └── Dockerfile               # Credit Cards service container
├── loans/
│   └── Dockerfile               # Loans service container
├── api-gateway/
│   └── Dockerfile               # API Gateway container
└── README.md
```

## Building Images

### Build All Images

```bash
docker-compose build
```

This will build all Docker images defined in `docker-compose.yml`. Each service has its own multi-stage Dockerfile for optimized image size.

### Build Individual Images

```bash
# Eureka Server
cd eureka-server && docker build -t capitec/eureka-server:latest .

# Accounts Service
cd accounts && docker build -t capitec/accounts:latest .

# Customer Service
cd customer && docker build -t capitec/customer:latest .

# Credit Cards Service
cd creditcards && docker build -t capitec/creditcards:latest .

# Loans Service
cd loans && docker build -t capitec/loans:latest .

# API Gateway
cd api-gateway && docker build -t capitec/api-gateway:latest .
```

### View Built Images

```bash
docker images | grep capitec
```

## Docker Compose

### Start Services

```bash
# Start all services in detached mode
docker-compose up -d

# Start and view logs (foreground)
docker-compose up
```

### Stop Services

```bash
# Stop but keep containers and volumes
docker-compose stop

# Stop and remove containers
docker-compose down

# Stop and remove everything including volumes
docker-compose down -v
```

### View Service Status

```bash
docker-compose ps
```

### View Logs

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
docker-compose logs -f postgres
```

### Scale Services

```bash
# Scale a specific service to multiple instances
# (Note: The accounts service to 3 instances)
docker-compose up -d --scale accounts=3
```

## Helper Script

A convenience bash script is provided to manage Docker Compose operations:

### Usage

```bash
./docker-run.sh [COMMAND]
```

### Available Commands

| Command | Description |
|---------|-------------|
| `start` | Start all services in detached mode |
| `stop` | Stop all services |
| `restart` | Restart all services |
| `logs` | View logs from all services (follow mode) |
| `logs:SERVICE` | View logs for specific service |
| `status` | Show status of all services |
| `ps` | List running containers |
| `build` | Build all Docker images |
| `clean` | Remove containers, networks, and volumes |
| `reset` | Clean and rebuild everything |
| `health` | Check health of all services |

### Examples

```bash
# Start all services
./docker-run.sh start

# View logs from customer service
./docker-run.sh logs:customer

# Check service health
./docker-run.sh health

# Restart all services
./docker-run.sh restart

# Clean up everything
./docker-run.sh clean
```

## Service Configuration

### Environment Variables

Each service is configured with the following environment variables:

**Database Configuration**
```
SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/DB_NAME
SPRING_DATASOURCE_USERNAME: dev_cptc_user
SPRING_DATASOURCE_PASSWORD: localpass
```

**Eureka Configuration**
```
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: http://eureka-server:8070/eureka/
EUREKA_INSTANCE_HOSTNAME: SERVICE_NAME
```

**Service Configuration**
```
SPRING_APPLICATION_NAME: service-name
SERVER_PORT: 8080 (exposed on different ports via docker-compose)
```

### Port Mapping

| Service | Internal Port | External Port | Protocol |
|---------|---------------|---------------|----------|
| PostgreSQL | 5432 | 5432 | TCP |
| Eureka Server | 8070 | 8070 | HTTP |
| Accounts | 8080 | 8081 | HTTP |
| Customer | 8080 | 8082 | HTTP |
| Credit Cards | 8080 | 8083 | HTTP |
| Loans | 8080 | 8084 | HTTP |
| API Gateway | 8071 | 8071 | HTTP |

## Database

### PostgreSQL Setup

The `sql/init-db.sql` script automatically creates:
- 4 databases: `db_cptc_accounts`, `db_cptc_customers`, `db_cptc_creditcards`, `db_cptc_loans`
- 1 user: `dev_cptc_user` with password `localpass`
- Proper schema and table privileges

### Database Connection

From host machine:
```bash
psql -h localhost -U dev_cptc_user -d db_cptc_accounts
```

From within Docker container:
```bash
docker exec -it postgres-db psql -U dev_cptc_user -d db_cptc_accounts
```

### Flyway Migrations

Migrations are automatically executed when services start:
- Located in `src/main/resources/db/migration/` of each service
- Named following pattern: `V1__Initial_schema.sql`
- Automatically tracked in `flyway_schema_history` table

## Networking

Services communicate over a custom Docker bridge network named `capitec-network`. This allows services to reference each other by hostname:

```
accounts → postgres:5432
customer → postgres:5432
creditcards → postgres:5432
loans → postgres:5432
all services → eureka-server:8070
all services → api-gateway:8071
```

## Health Checks

Each service includes a health check that runs every 30 seconds:

```yaml
healthcheck:
  test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
  interval: 30s
  timeout: 10s
  retries: 5
  start_period: 40s
```

Services are considered healthy when they respond with HTTP 200 to the health endpoint.

## Troubleshooting

### Services Won't Start

**Check logs:**
```bash
./docker-run.sh logs
```

**Common Issues:**
- Port already in use: Stop other services using ports 8070-8084, 5432
- Docker daemon not running: Start Docker Desktop
- Insufficient resources: Free up RAM and disk space

### Database Connection Errors

```
ERROR: Unable to connect to the database. Configure the url, user and password!
```

**Solutions:**
1. Verify PostgreSQL is running: `docker-compose logs postgres`
2. Wait for PostgreSQL to be ready: `docker-compose ps` (check health)
3. Check environment variables are set correctly in `docker-compose.yml`

### Services Not Discovering Each Other

**Eureka Registration Issues:**
```
ERROR: Cannot get a toll-free number. 
Unable to connect to Eureka Server (expected to reach this server by http://eureka-server:8070/eureka/)
```

**Solutions:**
1. Ensure Eureka Server is running: `./docker-run.sh health`
2. Check network connectivity: `docker network ls` and `docker network inspect capitec-network`
3. Restart all services: `./docker-run.sh restart`

### High Memory Usage

```bash
# Check resource usage
docker stats

# Limit memory for a service by editing docker-compose.yml:
services:
  postgres:
    # ... other config ...
    deploy:
      resources:
        limits:
          memory: 2G
        reservations:
          memory: 1G
```

### Docker Build Failures

**Java compilation errors:**
```bash
# Try building with more memory
export DOCKER_BUILDKIT=1
docker-compose build --no-cache
```

### Viewing Docker Network

```bash
# List all networks
docker network ls

# Inspect the network
docker network inspect capitec-network

# Test network connectivity from a container
docker run -it --network capitec-network ubuntu bash
# Inside container: ping postgres, ping eureka-server, etc.
```

## Performance Optimization

### Enable BuildKit for Faster Builds

```bash
export DOCKER_BUILDKIT=1
export COMPOSE_DOCKER_CLI_BUILD=1
docker-compose build
```

### Reduce Image Size

The multi-stage Dockerfiles in this project:
1. Build the application with Maven in one stage
2. Copy only the final JAR to a clean JRE base image
3. Result: ~500MB per service image (vs ~1.5GB with full JDK)

### Use Volume Mounts for Development

For faster local development without rebuilding:

```bash
docker run -it \
  -v ./accounts/src:/app/src \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/db_cptc_accounts \
  capitec/accounts:latest
```

## Production Deployment

### Multi-Stage Composition

For production, create a `docker-compose.prod.yml`:

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:16-alpine
    restart: always
    environment:
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    volumes:
      - postgres_prod_data:/var/lib/postgresql/data

  # ... other services with restart policy
```

### Environment Variables

Create a `.env` file (never commit to git):

```
DB_PASSWORD=secure_password_here
EUREKA_HOSTNAME=eureka.production.com
API_GATEWAY_HOST=api.production.com
```

### Deployment Command

```bash
docker-compose -f docker-compose.prod.yml up -d
```

## Additional Resources

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot Docker Documentation](https://spring.io/guides/topicals/spring-boot-docker/)
- [PostgreSQL Docker Documentation](https://hub.docker.com/_/postgres)

