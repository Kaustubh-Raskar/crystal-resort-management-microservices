#!/bin/bash

set -e

echo "Starting Eureka..."
cd crm-application/eureka-server
mvn spring-boot:run &
EUREKA_PID=$!

sleep 10

echo "Starting Auth Service..."
cd ../auth-service
mvn spring-boot:run &
AUTH_PID=$!

sleep 5

echo "Starting API Gateway..."
cd ../api-gateway
mvn spring-boot:run &
GATEWAY_PID=$!

sleep 5

echo "Starting Resort Service..."
cd ../resort-service
mvn spring-boot:run &
RESORT_PID=$!

echo "All services started"
echo "PIDs: $EUREKA_PID $AUTH_PID $GATEWAY_PID $RESORT_PID"

wait
