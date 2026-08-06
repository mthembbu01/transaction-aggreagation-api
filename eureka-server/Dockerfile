FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy prebuilt JAR
COPY target/eureka-server-0.0.1-SNAPSHOT.jar app.jar

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD java -cp app.jar org.springframework.boot.loader.launch.PropertiesLauncher -Dspring.boot.application.class=org.springframework.cloud.netflix.eureka.server.EurekaServerApplication -Dspring.config.location=classpath:/application.yaml || exit 1

EXPOSE 8070

ENTRYPOINT ["java", "-jar", "app.jar"]

