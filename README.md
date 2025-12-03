# Restaurant Web Application

A full-stack web application for restaurant order management built with Java, JSP, Servlet, and containerized with Docker for easy deployment.

## Overview

This project is a restaurant order management system that allows users to view the menu, place orders, and manage restaurant operations. It features a modern web interface with backend business logic and persistent data storage using PostgreSQL.

## Technologies Used

- **Backend:** Java, Servlet, JSP
- **Frontend:** HTML, CSS, JavaScript
- **Database:** PostgreSQL
- **Build & Deployment:** Docker, Docker Compose
- **Application Server:** Apache Tomcat


## Quick Start

### Prerequisites

- **Docker** and **Docker Compose** installed on your system
- Git (for cloning the repository)


### Running the Application status

1. Clone the repository:
```
git clone https://github.com/adrianmadry/restaurant_webapp.git
cd restaurant_webapp
```

2. Start the application with Docker Compose:
```
docker-compose up --build
```

3. Access the application in your browser:
```
http://localhost:8080/
```

The application will automatically initialize the database with seed data on first run.

### Stopping the Application

```
docker-compose down
```
### User's credentials to login

```
user1 / password
user2 / password
user3 / password
```

## Key Features

**User Management & Authentication**
- User Login authentication
- Session management

**Order Management**
- Shopping basket for order completion
- Order form for order submission
- Order data validation
- Process valid order to database

