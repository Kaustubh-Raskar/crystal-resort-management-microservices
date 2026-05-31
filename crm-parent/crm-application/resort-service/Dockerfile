# Build stage
FROM maven:3.9.8-eclipse-temurin-17 AS builder
WORKDIR /workspace
COPY crm-parent/pom.xml ./pom.xml
COPY crm-parent/crm-application/pom.xml ./crm-application/pom.xml
COPY crm-parent/crm-application/resort-service ./crm-application/resort-service
RUN mvn -f pom.xml -pl crm-application/resort-service -am -DskipTests package

# Runtime stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /workspace/crm-application/resort-service/target/*.jar ./app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
