# Build stage
FROM maven:3.9.8-eclipse-temurin-17 AS builder
WORKDIR /workspace
COPY crm-parent/pom.xml ./pom.xml
COPY crm-parent/crm-application/pom.xml ./crm-application/pom.xml
COPY crm-parent/crm-application/eureka-server ./crm-application/eureka-server
RUN mvn -f pom.xml -pl crm-application/eureka-server -am -DskipTests package

# Runtime stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /workspace/crm-application/eureka-server/target/*.jar ./app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
