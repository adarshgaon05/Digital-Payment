# Digital Wallet – Secure Payment System

A full-stack **digital wallet and payment system** that allows users to register, log in securely, manage wallet balance, and transfer money between users. The application uses **JWT authentication** to secure APIs and tracks all user transactions.

## Features

* User registration and login with JWT authentication
* Secure REST APIs with Spring Security
* Wallet balance management
* Money transfer between users
* Transaction history tracking
* Responsive UI using HTML, CSS
* Backend developed with Spring Boot

## Tech Stack

**Backend:** Java, Spring Boot, Spring Security, JWT, Spring Data JPA, REST APIs
**Database:** PostgreSQL / MySQL
**Frontend:** HTML, CSS, JavaScript
**Tools:** Eclipse, Postman, Maven, Git, GitHub

## API Overview

* **POST** /api/auth/register — Register new user
* **POST** /api/auth/login — User login
* **POST** /api/wallet/transfer — Transfer money
* **GET** /api/wallet/balance — Check wallet balance
* **GET** /api/wallet/history — Transaction history

## Project Architecture

```
/digital-wallet
 ├── src
 │   ├── main
 │   │   ├── java
 │   │   │    └── com.adarsh.wallet
 │   │   │         ├── controller
 │   │   │         ├── service
 │   │   │         ├── repository
 │   │   │         ├── entity
 │   │   │         ├── security
 │   │   │         └── config
 │   │   ├── resources
 │   │        ├── application.properties
 │   │        └── static
 │   │             ├── login.html
 │   │             ├── register.html
 │   │             ├── dashboard.html
 │   │             └── history.html
 ├── pom.xml
```

## Running the Project

**Backend:**

```
mvn spring-boot:run
```

Then open browser:

```
http://localhost:8080
```

## Developed by

Adarsh Gaon
