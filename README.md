# PayFlow API

A simplified payment processing system built with Spring Boot.

## Overview

PayFlow is a REST API that simulates a payment processing system. It allows users to register, maintain wallet balances, and transfer money between users. The system uses an H2 in-memory database for persistence.

### Key Features

- User registration with UPI IDs
- Wallet balance management
- Money transfers between users
- Transaction history
- REST API endpoints for all operations

## Technology Stack

- **Java 21**: Programming language
- **Spring Boot 4.1.0**: Application framework
- **Spring Data JPA**: Database operations and ORM
- **H2 Database**: In-memory database for development
- **Maven**: Build tool and dependency management

## How Spring Boot Works in This Project

### Embedded Server
Spring Boot embeds a Tomcat server directly into the application. When you run the application, Spring Boot starts the embedded Tomcat server on port 8080, eliminating the need to deploy to an external server.

### Auto Configuration
Spring Boot's auto-configuration automatically configures beans based on the dependencies on the classpath:
- **Spring Web**: Auto-configures DispatcherServlet, Jackson (JSON serialization), and REST controllers
- **Spring Data JPA**: Auto-configures EntityManagerFactory, DataSource, and TransactionManager
- **H2 Database**: Auto-configures the H2 data source and enables the H2 console

### Production-Ready Defaults
Spring Boot provides production-ready defaults including health checks, configurable logging, global exception handling, externalized property management, and database connection pooling.

## Project Structure
