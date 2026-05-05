#!/bin/bash
echo "=============================="
echo "Starting Eureka Server..."
echo "=============================="
cd eureka-server
mvn clean install
mvn spring-boot:run &
sleep 20

echo "=============================="
echo "Starting Auth Service..."
echo "=============================="
cd ../auth-service
mvn clean install
mvn spring-boot:run &
sleep 20

echo "=============================="
echo "Starting API Gateway..."
echo "=============================="
cd ../api-gateway
mvn clean install
mvn spring-boot:run &
sleep 20

echo "=============================="
echo "Starting Resort Service..."
echo "=============================="
cd ../resort-service
mvn clean install
mvn spring-boot:run &

echo "=============================="
echo " System is up (core services)"
echo "=============================="
