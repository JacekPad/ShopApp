# DESCRIPTION:
Aapplication written in java 17 with springboot 3.1.1. 
The Service includes three applications, allowing users to manage products in the database, place orders based on available products, and process created orders. 
Communication among services is done via a message-broker (rabbitMQ) and/or HTTP requests (WebClient).

# Services:
 ### warehouse 
 A backend application that provides CRUD operations on products for other services.
 ### shopApp
 A backend application used to create orders and send all necessary information to other services.
 ### orderApp
 A backend application that manages orders and provides CRUD operations on orders for other services.

# TECHNOLOGIES:
- Java 17
- Springboot 3.1.1
- Spring Data, JPA, Web
- Hibernate
- Maven
- Webclient
- JUnit
- Mockito
- MySQL
- Flyway
- Docker
- DockerCompose
- RabbitMQ
- Swagger
