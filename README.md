
# Coffee Roasting and Stock Management System ☕️

This project is designed to manage coffee stock and monitor the coffee roasting process in a distributed microservice architecture. It utilizes **Kafka** for message-based communication and **gRPC** for real-time data processing.

## 🛠️ Technologies Used

- **Java 17**
- **Spring Boot**
- **Kafka** for asynchronous messaging
- **gRPC** for high-performance remote procedure calls
- **H2** for testing purposes
- **Lombok** to reduce boilerplate code
- **JUnit** and **Mockito** for testing

## 📑 Features

- **Kafka integration**: 
  - Asynchronous communication between microservices for coffee stock updates.
  
- **gRPC service**: 
  - Monitor and log the roasting process of coffee beans, calculating weight loss during roasting.
  
- **CRUD operations** for managing coffee stock:
  - Add, update, and view details of different coffee beans.
  
- **Unit testing**:
  - Comprehensive test coverage using JUnit for REST endpoints and Kafka consumers.

## 📂 Project Structure

The project is divided into several components:

- `api.kafkatz.controller`: Handles REST API requests for managing coffee stock.
- `api.kafkatz.service`: Contains business logic for Kafka consumers and gRPC services.
- `api.kafkatz.entities`: Contains the entity classes for managing the persistence layer.
- `api.kafkatz.grpc`: Generated classes from gRPC proto files.
- `api.kafkatz.repository`: JPA repositories for database access.

## 📦 Running the Project

1. **Kafka Setup**: 
   Make sure Kafka is running. You can use `docker-compose.yml` to spin up Kafka locally:
   ```bash
   docker-compose up -d
   ```

2. **Run the Application**:
   Use Maven to build and run the project:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

3. **Swagger API Documentation**:
   Once the application is running, you can access the Swagger UI to explore the available REST endpoints:
   ```
   http://localhost:8080/swagger-ui.html
   ```

## 📊 API Endpoints

| Method | Endpoint                  | Description                       |
|--------|---------------------------|-----------------------------------|
| GET    | `/coffee-stock/{id}`       | Get coffee stock by ID            |
| POST   | `/coffee-stock`            | Add new coffee stock              |
| PUT    | `/coffee-stock/{id}`       | Update coffee stock details       |
| DELETE | `/coffee-stock/{id}`       | Delete coffee stock               |

## ⚙️ gRPC Service

The gRPC service is responsible for recording the roasting process and calculating weight loss during roasting. Here's how to use it:

```proto
service RoastingService {
    rpc sendRoastingInfo (RoastingInfo) returns (Empty);
}
```

## 🧪 Testing

To run unit tests, execute the following command:
```bash
mvn test
```
The project includes tests for:
- REST controllers (Coffee stock management)
- Kafka consumer service
- gRPC service

