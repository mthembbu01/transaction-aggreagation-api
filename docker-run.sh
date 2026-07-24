#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Help function
show_help() {
    echo -e "${BLUE}Transaction Aggregation API - Docker Compose Helper${NC}"
    echo ""
    echo "Usage: ./docker-run.sh [COMMAND]"
    echo ""
    echo "Commands:"
    echo -e "  ${GREEN}start${NC}       - Start all services in detached mode"
    echo -e "  ${GREEN}stop${NC}        - Stop all services"
    echo -e "  ${GREEN}restart${NC}     - Restart all services"
    echo -e "  ${GREEN}logs${NC}        - View logs from all services (follow mode)"
    echo -e "  ${GREEN}logs:SERVICE${NC} - View logs for specific service (accounts, customer, creditcards, loans, eureka-server, api-gateway, postgres)"
    echo -e "  ${GREEN}status${NC}      - Show status of all services"
    echo -e "  ${GREEN}ps${NC}          - List running containers"
    echo -e "  ${GREEN}build${NC}       - Build all Docker images"
    echo -e "  ${GREEN}clean${NC}       - Remove containers, networks, and volumes"
    echo -e "  ${GREEN}reset${NC}       - Clean and rebuild everything"
    echo -e "  ${GREEN}health${NC}      - Check health of all services"
    echo ""
    echo "Examples:"
    echo "  ./docker-run.sh start"
    echo "  ./docker-run.sh logs"
    echo "  ./docker-run.sh logs:customer"
    echo "  ./docker-run.sh status"
}

# Parse command
case "${1}" in
    start)
        echo -e "${YELLOW}Starting all services...${NC}"
        docker-compose up -d
        echo -e "${GREEN}Services started!${NC}"
        echo ""
        echo "Access the services at:"
        echo -e "  Eureka:     ${BLUE}http://localhost:8070/${NC}"
        echo -e "  API Gateway: ${BLUE}http://localhost:8071/${NC}"
        echo -e "  Accounts:   ${BLUE}http://localhost:8081/${NC}"
        echo -e "  Customer:   ${BLUE}http://localhost:8082/${NC}"
        echo -e "  CreditCards: ${BLUE}http://localhost:8083/${NC}"
        echo -e "  Loans:      ${BLUE}http://localhost:8084/${NC}"
        ;;

    stop)
        echo -e "${YELLOW}Stopping all services...${NC}"
        docker-compose down
        echo -e "${GREEN}Services stopped!${NC}"
        ;;

    restart)
        echo -e "${YELLOW}Restarting all services...${NC}"
        docker-compose restart
        echo -e "${GREEN}Services restarted!${NC}"
        ;;

    logs)
        echo -e "${YELLOW}Following logs from all services (Ctrl+C to exit)...${NC}"
        docker-compose logs -f
        ;;

    logs:*)
        SERVICE="${1#logs:}"
        if [[ -z "$SERVICE" ]]; then
            docker-compose logs -f
        else
            echo -e "${YELLOW}Following logs from ${SERVICE} service (Ctrl+C to exit)...${NC}"
            docker-compose logs -f "$SERVICE"
        fi
        ;;

    status)
        echo -e "${BLUE}Service Status:${NC}"
        docker-compose ps
        ;;

    ps)
        docker-compose ps
        ;;

    build)
        echo -e "${YELLOW}Building all Docker images...${NC}"
        docker-compose build
        echo -e "${GREEN}Build completed!${NC}"
        ;;

    clean)
        echo -e "${RED}Cleaning up containers, networks, and volumes...${NC}"
        docker-compose down -v
        echo -e "${GREEN}Cleanup completed!${NC}"
        ;;

    reset)
        echo -e "${RED}Resetting everything (clean build)...${NC}"
        docker-compose down -v
        docker-compose build
        docker-compose up -d
        echo -e "${GREEN}System reset and restarted!${NC}"
        ;;

    health)
        echo -e "${BLUE}Checking service health...${NC}"
        echo ""

        echo -n "Eureka Server (8070): "
        curl -s -o /dev/null -w "%{http_code}" http://localhost:8070/eureka/apps && echo -e " ${GREEN}OK${NC}" || echo -e " ${RED}FAILED${NC}"

        echo -n "API Gateway (8071):   "
        curl -s -o /dev/null -w "%{http_code}" http://localhost:8071/actuator/health && echo -e " ${GREEN}OK${NC}" || echo -e " ${RED}FAILED${NC}"

        echo -n "Accounts (8081):      "
        curl -s -o /dev/null -w "%{http_code}" http://localhost:8081/actuator/health && echo -e " ${GREEN}OK${NC}" || echo -e " ${RED}FAILED${NC}"

        echo -n "Customer (8082):      "
        curl -s -o /dev/null -w "%{http_code}" http://localhost:8082/actuator/health && echo -e " ${GREEN}OK${NC}" || echo -e " ${RED}FAILED${NC}"

        echo -n "CreditCards (8083):   "
        curl -s -o /dev/null -w "%{http_code}" http://localhost:8083/actuator/health && echo -e " ${GREEN}OK${NC}" || echo -e " ${RED}FAILED${NC}"

        echo -n "Loans (8084):         "
        curl -s -o /dev/null -w "%{http_code}" http://localhost:8084/actuator/health && echo -e " ${GREEN}OK${NC}" || echo -e " ${RED}FAILED${NC}"
        ;;

    *)
        show_help
        ;;
esac

