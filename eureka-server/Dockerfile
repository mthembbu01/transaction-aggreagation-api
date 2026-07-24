# Multi-stage build
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy POM and source
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests -q

# Runtime stage
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy JAR from builder
COPY --from=builder /app/target/eureka-server-0.0.1-SNAPSHOT.jar app.jar

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD java -cp app.jar org.springframework.boot.loader.launch.PropertiesLauncher -Dspring.boot.application.class=org.springframework.cloud.netflix.eureka.server.EurekaServerApplication -Dspring.config.location=classpath:/application.yaml || exit 1

EXPOSE 8070

ENTRYPOINT ["java", "-jar", "app.jar"]

