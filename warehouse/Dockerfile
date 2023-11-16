FROM maven:3.6.3-openjdk-17 AS build
COPY src /home/warehouseapp/src
#looking for swagger yaml in root folder
COPY src/main/resources/swagger /src/main/resources/swagger
COPY pom.xml /home/warehouseapp
RUN mvn -f /home/warehouseapp/pom.xml clean package

FROM openjdk:17-jdk-alpine
ARG JAR_FILE
COPY --from=build /home/warehouseapp/target/*.jar warehouseapp.jar
ENTRYPOINT ["java","-jar","/warehouseapp.jar"]