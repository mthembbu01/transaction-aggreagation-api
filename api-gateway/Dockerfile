FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY target/api-gateway-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8071

ENTRYPOINT ["java", "-jar", "app.jar"]

