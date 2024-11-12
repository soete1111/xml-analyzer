# XML Analyzer API

A Spring Boot application that analyzes XML files containing Stack Overflow posts.

## Requirements

- Java 23 (21LTS might have been a better choice)
- Maven
- Docker (optional)

## Running the Application

### Using Maven 

1. Clone the repository
2. Run `mvn clean install` to build the application
3. Run `mvn spring-boot:run` to start the application

### Using Docker(https://hub.docker.com/r/soete/xml-analyzer)

1. Run `docker run -d -p 8080:8080 soete/xml-analyzer` to build and start the application

### Using Docker

1. Clone the repository
2. Run `docker build -t xml-analyzer .` to build the Docker image
3. Run `docker run -d -p 8080:8080 xml-analyzer` to start the application
