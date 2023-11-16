# DESCRIPTION:
Aapplication written in java 17 with springboot 3.1.1. 
The Service includes three applications, allowing users to manage products in the database, place orders based on available products, and process created orders. <br> 
Communication among services is done via a message-broker (rabbitMQ) and HTTP requests (WebClient). Services are secured by keycoak (client credentials among services and authorization code for users)

# Services:
 ### warehouse 
 A backend application that provides CRUD operations on products for other services.
 ### shopApp
 A backend application used to create orders and send all necessary information to other services.
 ### orderApp
 A backend application that manages orders and provides CRUD operations on orders for other services.

# INITIALIZATION:
  1. cd into main folder and <br>
  <code>docker compose up</code>
  2. login to keycloak admin console with admin / admin, switch to ProjectRealm and create a user and give it a 'USER' realm role.
  3. Use postman collection with authorization type OAuth 2.0 to login to keycloak as the created user or use postman auth/GET token code and POST token endpoints and then post aquired code in the Authorization type Bearer Token.

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
