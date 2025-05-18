Airline Reservation System

This is a Java-based airline reservation system built with Spring Boot. The application supports core airline functionalities such as managing flights, passengers, and bookings. It integrates with a SQL Server backend for data persistence and is configured to operate on a custom port (`8080`).

---

## Features

- RESTful APIs for airline operations
- SQL Server integration for persistent storage
- Layered architecture (Controller, Service, Repository)
- Spring Data JPA for simplified database interactions
- Built and managed with Maven
- Runs locally using IntelliJ IDEA or command line

---

## Tech Stack

- **Language:** Java 17+
- **Framework:** Spring Boot
- **Database:** Microsoft SQL Server (via JDBC)
- **IDE:** IntelliJ IDEA
- **Build Tool:** Maven

---

## Database Configuration

The application connects to a Microsoft SQL Server instance running on port `8080`. Ensure SQL Server is configured to listen on that port, and update your `application.properties` with the correct credentials:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:8080;databaseName=AirlineDB
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
