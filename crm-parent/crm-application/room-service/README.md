# Room Service

Room Management Microservice for Crystal Resort Management System

## Overview

The Room Service handles all room-related operations including:
- Room CRUD operations
- Room availability tracking
- Room filtering by resort and status
- Timestamps for audit trails

## Service Details

- **Port**: 8083
- **Service Name**: ROOM-SERVICE
- **Database**: RoomDB (SQL Server)
- **Service Discovery**: Eureka (localhost:8761)

## Database Schema

### Rooms Table
```sql
CREATE TABLE dbo.Rooms (
    Id INT PRIMARY KEY IDENTITY(1,1),
    ResortId INT NOT NULL,
    RoomNumber VARCHAR(10) NOT NULL,
    Floor INT,
    Type VARCHAR(50),
    Capacity INT,
    BasePrice DECIMAL(10,2),
    Description NVARCHAR(MAX),
    Status VARCHAR(20),
    CreatedAt DATETIME,
    UpdatedAt DATETIME
);
```

## API Endpoints

### Public Endpoints (Authenticated Users)

#### GET /api/rooms
- **Description**: Get all rooms
- **Authorization**: Any authenticated user
- **Response**: List of RoomResponse objects with timestamps

#### GET /api/rooms/{id}
- **Description**: Get room by ID
- **Authorization**: Any authenticated user
- **Response**: RoomResponse object

#### GET /api/rooms/resort/{resortId}
- **Description**: Get all rooms for a specific resort
- **Authorization**: Any authenticated user
- **Response**: List of RoomResponse objects

#### GET /api/rooms/resort/{resortId}/available
- **Description**: Get available rooms for a specific resort
- **Authorization**: Any authenticated user
- **Response**: List of available RoomResponse objects

#### GET /api/rooms/status/{status}
- **Description**: Get rooms by status (AVAILABLE, MAINTENANCE, OCCUPIED, CLOSED)
- **Authorization**: Any authenticated user
- **Response**: List of RoomResponse objects

### Admin-Only Endpoints (ADMIN role required)

#### POST /api/rooms
- **Description**: Create a new room
- **Authorization**: ADMIN role required
- **Request Body**:
```json
{
    "resortId": 1,
    "roomNumber": "101",
    "floor": 1,
    "type": "Double",
    "capacity": 2,
    "basePrice": 120.00,
    "description": "Spacious double room with ocean view",
    "status": "AVAILABLE"
}
```
- **Response**: RoomResponse object with auto-populated timestamps
```json
{
    "id": 1,
    "resortId": 1,
    "roomNumber": "101",
    "floor": 1,
    "type": "Double",
    "capacity": 2,
    "basePrice": 120.00,
    "description": "Spacious double room with ocean view",
    "status": "AVAILABLE",
    "createdAt": "2026-02-07T10:15:30.123456",
    "updatedAt": null
}
```

#### PUT /api/rooms/{id}
- **Description**: Update an existing room
- **Authorization**: ADMIN role required
- **Request Body**: Same as POST (all fields optional)
- **Response**: Updated RoomResponse object with updatedAt timestamp

#### DELETE /api/rooms/{id}
- **Description**: Delete a room
- **Authorization**: ADMIN role required
- **Response**: HTTP 204 No Content

## Authentication & Authorization

This service uses JWT tokens passed from the API Gateway.

### Authentication Flow
1. Client sends JWT token in Authorization header
2. API Gateway validates JWT and forwards headers:
   - `X-User-Id`: Username from JWT
   - `X-Roles`: Comma-separated roles
   - `X-Auth-Source`: "api-gateway" (validation)
3. Room Service HeaderAuthenticationFilter reads headers
4. Spring Security context is populated
5. @PreAuthorize annotations enforce role-based access

### Authorization Rules
- **GET endpoints**: `@PreAuthorize("isAuthenticated()")` - Any authenticated user
- **POST, PUT, DELETE endpoints**: `@PreAuthorize("hasRole('ADMIN')")` - ADMIN only

## Timestamps

All rooms have automatic timestamp management:
- **createdAt**: Auto-set to current datetime when room is created (@PrePersist)
- **updatedAt**: Auto-set to current datetime when room is updated (@PreUpdate)

These timestamps are included in all RoomResponse DTOs.

## Configuration

### application.yml
```yaml
spring:
  application:
    name: ROOM-SERVICE
  datasource:
    url: jdbc:sqlserver://localhost:1433;databaseName=RoomDB
    username: sa
    password: YourPassword123!
  jpa:
    hibernate:
      ddl-auto: update

server:
  port: 8083

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

## Building & Running

### Build
```bash
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

### Docker (Optional)
```bash
docker build -t room-service:latest .
docker run -p 8083:8083 room-service:latest
```

## Testing with curl

### Create Room (ADMIN only)
```bash
curl -X POST http://localhost:8080/api/rooms \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "resortId": 1,
    "roomNumber": "101",
    "floor": 1,
    "type": "Double",
    "capacity": 2,
    "basePrice": 120.00,
    "description": "Nice room",
    "status": "AVAILABLE"
  }'
```

### Get All Rooms
```bash
curl -X GET http://localhost:8080/api/rooms \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

### Get Rooms by Resort
```bash
curl -X GET http://localhost:8080/api/rooms/resort/1 \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

### Update Room (ADMIN only)
```bash
curl -X PUT http://localhost:8080/api/rooms/1 \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "basePrice": 150.00,
    "status": "MAINTENANCE"
  }'
```

### Delete Room (ADMIN only)
```bash
curl -X DELETE http://localhost:8080/api/rooms/1 \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

## Future Enhancements

- Room availability calendar
- Dynamic pricing
- Room amenities management
- Room images/media
- Integration with booking service (Saga pattern)
- Event publishing to Kafka
- Database separation from resort-service

## Troubleshooting

### Service not registering with Eureka
- Check Eureka server is running on port 8761
- Verify `eureka.client.service-url.defaultZone` in application.yml

### 401/403 Unauthorized
- Ensure JWT token is valid
- Verify token has required roles (ADMIN for write operations)
- Check API Gateway is forwarding headers correctly

### Database connection issues
- Verify SQL Server is running
- Check connection string in application.yml
- Ensure RoomDB database exists
- Verify credentials

## Support

For issues or questions, contact the development team.
