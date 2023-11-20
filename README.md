# DESCRIPTION:
Applications written in java 17 with springboot 3.1.1. 
Microservices based application, allowing users to manage products in the database, place orders based on available products, and process created orders. <br> 
Communication among services is done via a message-broker (rabbitMQ) and HTTP requests (WebClient). Services are secured by keycoak (client credentials among services and authorization code for users).

# Services:
 ### warehouse 
 A backend application that provides CRUD operations on products for other services.
 ### shopApp
 A backend application used to create orders and send all necessary information to other services.
 ### orderApp
 A backend application that manages orders and provides CRUD operations on orders for other services.

# INITIALIZATION:
  1. From the main folder <br>
  <code>docker compose up</code>
  2. login to keycloak admin console with <code>admin / admin</code>, switch to <code>ProjectRealm</code> and create a user and give it a <code>USER</code> realm role.
  3. Use postman collection with <code>authorization type OAuth 2.0</code> to login to keycloak as the created user or use postman AUTH/<code>GET token code</code> and AUTH/<code>POST token</code> endpoints and then post aquired code in the <code>Authorization type Bearer Token</code>.

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
- Keycloak
