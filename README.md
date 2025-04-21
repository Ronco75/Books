# Books Service

## Overview

This project is a RESTful API service for managing books, developed to deepen my understanding of Spring Boot, testing practices, and Docker containerization. The service provides endpoints for creating, retrieving, updating, and deleting books in a library system.

## Learning Objectives

The primary purpose of this project was to practice and explore:

- **Spring Boot Framework**: Building a complete application with Spring Boot, including controllers, services, repositories, and models with proper separation of concerns
- **Testing Strategies**: Implementing comprehensive test coverage with unit tests, mocking, and test profiles
- **Docker Integration**: Containerizing the application and its dependencies for consistent deployment across environments
- **Database Integration**: Working with both PostgreSQL for production and H2 for testing
- **API Documentation**: Using SpringDoc OpenAPI to document the API endpoints

## Technical Stack

- **Java 17**: Core programming language
- **Spring Boot 3.4.3**: Application framework
- **Spring Data JPA**: Data access and persistence
- **PostgreSQL**: Production database
- **H2 Database**: In-memory database for testing
- **Docker & Docker Compose**: Containerization and orchestration
- **JUnit 5 & Mockito**: Testing framework
- **Lombok**: Reducing boilerplate code
- **SpringDoc OpenAPI**: API documentation
- **Maven**: Build and dependency management

## Features

- Create, read, update, and delete books
- Search for books by ISBN
- Retrieve a list of all books
- Automated tests ensuring code quality and reliability
- Docker containerization for consistent deployment
- API documentation with Swagger UI

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/books` | Retrieve all books |
| GET | `/books/{isbn}` | Retrieve a specific book by ISBN |
| POST | `/books` | Create a new book |
| PUT | `/books/{isbn}` | Update a book (or create if it doesn't exist) |
| DELETE | `/books/{isbn}` | Delete a book |

## Getting Started

### Prerequisites

- Java 17 or higher
- Docker and Docker Compose
- Maven

### Running the Application

1. Clone the repository:
```
git clone git@github.com:Ronco75/Books.git
```

2. Start the PostgreSQL database using Docker Compose:
```
docker-compose up -d
```

3. Build and run the application:
```
./mvnw spring-boot:run
```

The application will be available at `http://localhost:8080`.

### Running Tests

Run the tests using Maven:
```
./mvnw test
```

### API Documentation

Once the application is running, you can access the Swagger UI documentation at:
```
http://localhost:8080/swagger-ui.html
```

## Learning Outcomes

Through this project, I gained practical experience with:

1. **Spring Boot Best Practices**: Implementing proper separation of concerns, dependency injection, and configuration
2. **Testing Strategies**: Writing comprehensive tests for all layers of the application
3. **Docker Integration**: Setting up services using Docker Compose and containerizing applications
4. **Database Interaction**: Working with JPA repositories and configuring different database environments
5. **API Design**: Creating a RESTful API with appropriate HTTP methods and status codes


## License

This project is open source and available under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
